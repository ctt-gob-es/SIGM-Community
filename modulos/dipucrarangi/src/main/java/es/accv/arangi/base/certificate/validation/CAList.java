/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.certificate.validation;

import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.device.KeyStoreManager;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.ValidationXMLException;

/**
 * Clase que gestiona una lista de certificados de Autoridades de Validación.<br><br>
 * 
 * A la hora de validar un certificado esta clase posee las siguientes funcionalidades:
 * <ul>
 * 	<li>Permite restringir la validación únicamente a las Autoridades de Certificación que
 * 	interese.</li>
 * 	<li>Permite obtener la cadena de confianza del certificado.</li>
 * 	<li>Permite definir las URLs de CRLs y OCSPs donde se realizará la validación</li>
 * </ul><br>
 * 
 * Para definir las URLs de CRLs y OCSPs donde se realizarán las validaciones para certificados
 * de unas determinadas Autoridades de Certificación se utiliza un fichero XML con un formato 
 * determinado y que puede ser tratado con la clase {@link ValidationXML ValidationXML}.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class CAList extends ArangiObject {

	/*
	 * Nombre del fichero validationXML
	 */
	private static final String VALIDATION_XML_FILE = "validation_data.xml";

	/*
	 * Class logger
	 */
	private Logger logger = Logger.getLogger(CAList.class);
	
	/*
	 * List of CA certificates
	 */
	private HashMap<String, X509Certificate> hmCACertificates;
	
	/*
	 * Alternative list of CA certificates (by issuer CN)
	 */
	private HashMap<String, X509Certificate> hmAlternativeCACertificates;
	
	/*
	 * Validation xml object
	 */
	private ValidationXML validationXML;
	
	/**
	 * Constructor por defecto. Inicializa una lista de CAs vacía.
	 */
	public CAList () {
		logger.debug("[CAList()] :: Inicio");
		
		hmCACertificates = new HashMap<String, X509Certificate> ();
		hmAlternativeCACertificates = new HashMap<String, X509Certificate> ();
	}
	
	/**
	 * Constructor que inicializa el objeto con una lista de certificados 
	 * java.security.cert.X509Certificate.
	 * 
	 * @param caList Lista de java.security.cert.X509Certificate
	 */
	public CAList (List caList) throws NormalizeCertificateException {
		logger.debug("[CAList(caList)] :: " + (caList==null?null:caList.size()));
		
		hmCACertificates = new HashMap<String, X509Certificate> ();
		hmAlternativeCACertificates = new HashMap<String, X509Certificate> ();
		
		//-- iterate and get values for hashmap
		Iterator itCAs = caList.iterator();
		while (itCAs.hasNext()) {
			Object objectCertificate = itCAs.next();
			Certificate certificate;
			if (objectCertificate instanceof Certificate) {
				//-- La lista es de objetos Certificate
				certificate = (Certificate) objectCertificate;
			} else {
				//-- La lista es de objetos X509Certificate
				try {
					certificate = new Certificate((X509Certificate) objectCertificate);
				} catch (ClassCastException e) {
					logger.debug("[CAList(caList)] :: Some element in the list is not a valid X.509 certificate");
					throw new NormalizeCertificateException ("Algún elemento de la lista no es un objeto X509Certificate");
				} catch (NormalizeCertificateException e) {
					logger.debug("[CAList(caList)] :: Cannot normalize as a bouncy castle X.509 certificate an element in the list");
					throw e;
				}
			}
			if (certificate.getSubjectKeyIdentifier() != null) {
				hmCACertificates.put(certificate.getSubjectKeyIdentifier(), certificate.toX509Certificate());
			}
			hmAlternativeCACertificates.put(certificate.getCommonName(), certificate.toX509Certificate());
		}
	}

	/**
	 * Constructor que inicializa la lista de CAs con los certificados que se encuentran
	 * en el directorio indicado en el parámetro. Si en el directorio también se halla
	 * un fichero validation_data.xml, la información que contiene se incluirá en este
	 * objeto.<br><br>
	 * Si el objeto pasado como parámetro es un fichero (no directorio) el objeto se
	 * incializará con este fichero, que debe ser un certificado. 
	 * 
	 * @param listSource Fichero (certificado) o carpeta qie contiene una lista de certificados.
	 */
	public CAList (File listSource) throws Exception {
		logger.debug("[CAList(listSource)] :: " + listSource.getAbsolutePath());
		
		//-- Check that listSource is not null
		if (listSource == null) {
			logger.info ("[CAList(listSource)] :: List source from CA certificates is a null value");
			throw new Exception ("List source from CA certificates is a null value");
		}
		
		hmCACertificates = new HashMap ();
		hmAlternativeCACertificates = new HashMap();
		if (listSource.isDirectory()) {
			//-- listSource is a directory -> try to load all files in the folder like a X509Certificate or a
			//-- validation xml
			for (int i=0;i<listSource.listFiles().length;i++) {
				
				//-- if is a directory continue
				if (listSource.listFiles()[i].isDirectory()) {
					continue;
				}
				
				//-- Check if file is a validation xml
				if (listSource.listFiles()[i].getName().equalsIgnoreCase(VALIDATION_XML_FILE)) {
					validationXML = new ValidationXML (listSource.listFiles()[i], this);
				} else {
				
					//-- Check if file is a X509Certificate
					try {
						Certificate certificate = new Certificate(listSource.listFiles()[i]);
						if (certificate.getSubjectKeyIdentifier() != null) {
							hmCACertificates.put(certificate.getSubjectKeyIdentifier(), certificate.toX509Certificate());
						}
						hmAlternativeCACertificates.put(certificate.getCommonName(), certificate.toX509Certificate());
						logger.debug ("[CAList(listSource)] :: File " + listSource.listFiles()[i].getAbsolutePath() + " imported as a CA certificate");
					} catch (Exception e) {
						logger.debug ("[CAList(listSource)] :: File " + listSource.listFiles()[i].getAbsolutePath() + " is not a X509Certificate", e);
					}
				}
			}
			
			//-- If no file are found to be a X509Certificate throws an exception
			if (hmCACertificates.isEmpty() && hmAlternativeCACertificates.isEmpty()) {
				logger.info ("[CAList(listSource)] :: No X509 certificate is found in folder " + listSource.getAbsolutePath());
				throw new Exception ("They are not X509 certificates in the folder " + listSource.getAbsolutePath());
			}
			
		} else {
			//-- listSource is a file -> the list will only have 1 element
			try {
				Certificate certificate = new Certificate(listSource);
				if (certificate.getSubjectKeyIdentifier() != null) {
					hmCACertificates.put(certificate.getSubjectKeyIdentifier(), certificate.toX509Certificate());
				}
				hmAlternativeCACertificates.put(certificate.getCommonName(), certificate.toX509Certificate());
			} catch (Exception e) {
				logger.info ("[CAList(listSource)] :: Error when getting the CA certificate from path " + listSource.getAbsolutePath(), e);
				throw new Exception ("Error when getting the CA certificate from path ", e);
			}
		}
		
	}

	/**
	 * Metodo para obtener el objeto {@link ValidationXML ValidationXML}
	 * 
	 * @return Objeto {@link ValidationXML ValidationXML} o nulo si no se ha definido
	 */
	public ValidationXML getValidationXML () {
		return validationXML;
	}
	
	/**
	 * Metodo para cargar el objeto {@link ValidationXML ValidationXML} junto a
	 * la lista de CAs
	 * 
	 * @param validationXml Fichero de validación XML 
	 * @throws Exception 
	 */
	public void setValidationXML (File validationXml) throws Exception {
		this.validationXML = new ValidationXML (validationXml, this);
	}
	
	/**
	 * Metodo para cargar el objeto {@link ValidationXML ValidationXML} junto a
	 * la lista de CAs
	 * 
	 * @param validationXml Array de bytes con el fichero de validación XML  
	 * @throws ValidationXMLException Error parseando el fichero de validación
	 */
	public void setValidationXML (byte[] validationXml) throws ValidationXMLException {
		this.validationXML = new ValidationXML (validationXml, this);
	}
	
	/**
	 * Método que trata de encontrar en la lista de certificados de CA  uno que 
	 * sea el emisor del certificado pasado como parámetro.
	 * 
	 * @param x509Certificate Certificado del que se busca el emisor 
	 * @return Certificado emisor o nulo si éste no se encuentra en la lista
	 */
	public X509Certificate getCACertificate(X509Certificate x509Certificate) {

		logger.debug("[CAList.getCACertificate]::Entrada");
		
		Certificate certificate;
		try {
			certificate = new Certificate (x509Certificate);
			logger.debug("[CAList.getCACertificate]::Certificado: " + certificate.getCommonName());
		} catch (NormalizeCertificateException e) {
			logger.info ("[CAList.getCACertificate]::El certificado no puede ser normalizado según el proveedor criptográfico de Arangí");
			return null;
		}
		
		//-- Comprobar si tiene el campo IKI
		if (certificate.getIssuerKeyIdentifier() != null) {
			return (X509Certificate)hmCACertificates.get(certificate.getIssuerKeyIdentifier());
		}
		
		//-- Probar con el CN del issuer
		logger.debug("[CAList.getCACertificate] :: El certificado no tiene SKI, probar por el CN del issuer");
		if (certificate.getIssuerCommonName() != null) {
			return (X509Certificate)hmAlternativeCACertificates.get(certificate.getIssuerCommonName());
		}
		
		logger.debug("[CAList.getCACertificate] :: Devolviendo null");
		return null;
		
	}

	/**
	 * Método que determina si la lista se encuentra vacía.
	 * 
	 * @return Cierto si la lista está vacía.
	 */
	public boolean isEmpty() {
		return hmCACertificates.isEmpty() && hmAlternativeCACertificates.isEmpty();
	}

	/**
	 * Comprueba si el emisor del certificado pasado como parámetro pertenece
	 * a la lista de certificados de CA.
	 * 
	 * @param x509Certificate Certificado
	 * @return Cierto si el emisor del certificado se encuentra en la lista.
	 */
	public boolean isOwned (X509Certificate x509Certificate) {
		logger.debug("[CAList.isOwned]::Entrada");
		
		Certificate certificate;
		try {
			certificate = new Certificate (x509Certificate);
			logger.debug("[CAList.isOwned]::Certificado: " + certificate.getCommonName());
		} catch (NormalizeCertificateException e) {
			logger.info ("[CAList.isOwned]::El certificado no puede ser normalizado según el proveedor criptográfico de Arangí");
			return false;
		}
		
		//-- Comprobar si tiene el campo IKI
		if (certificate.getIssuerKeyIdentifier() != null) {
			return hmCACertificates.containsKey(certificate.getIssuerKeyIdentifier());
		}
		
		//-- Probar con el CN del issuer
		logger.debug("[CAList.isOwned] :: El certificado no tiene SKI, probar por el CN del issuer");
		if (certificate.getIssuerCommonName() != null) {
			return hmAlternativeCACertificates.containsKey(certificate.getIssuerCommonName());
		}
		
		logger.debug("[CAList.isOwned] :: Devolviendo null");
		return false;
		
	}

	/**
	 * Comprueba si el certificado pasado como parámetro pertenece a la lista
	 * de certificados de CA.
	 * 
	 * @param certificate Certificado
	 * @return Cierto si el certificado pertenece a la lista
	 */
	public boolean contains (X509Certificate certificate) { 
		Certificate cert;
		try {
			cert = new Certificate (certificate);
		} catch (NormalizeCertificateException e) {
			return false;
		}
		if (cert.getSubjectKeyIdentifier() != null) {
			return hmCACertificates.containsKey(cert.getSubjectKeyIdentifier());
		} else {
			return hmAlternativeCACertificates.containsKey(cert.getCommonName());
		}
	}

	/**
	 * Comprueba si el Subject Key Identifier pasado como parámetro pertenece a uno de
	 * los certificados de CA de la lista.
	 * 
	 * @param base64SKI SKI del certificado de una CA en formato base64, tal y como se
	 * obtiene del método getIssuerKeyIdentifier de la
	 * clase {@link es.accv.arangi.base.certificate.Certificate Certificate}.
	 *  
	 * @return Cierto si Subject Key Identifier es de un certificado de la lista
	 */
	public boolean containsKey (String base64SKI) { 
		return hmCACertificates.containsKey(base64SKI);
	}

	/**
	 * Obtiene la lista de certificados de CA. La lista no tiene ningún orden.
	 * 
	 * @return Lista de certificados de CA en formato java.security.X509Certificate
	 */
	public List getCACertificates () {
		Collection colection = hmCACertificates.values();	
		List result = new ArrayList ();
		for (Iterator iterator = colection.iterator(); iterator.hasNext();) {
			X509Certificate caCertificate = (X509Certificate) iterator.next();
			result.add(caCertificate);
		}
		for (Iterator iterator = hmAlternativeCACertificates.values().iterator(); iterator.hasNext();) {
			X509Certificate caCertificate = (X509Certificate) iterator.next();
			if (!result.contains(caCertificate)) {
				result.add(caCertificate);
			}
		}
		
		return result;
	}
	
	/**
	 * Añade un elemento a la lista de certificados de CA
	 * 
	 * @param caCertificate Certificado de CA
	 */
	public void addCACertificate (X509Certificate caCertificate) {
		Certificate cert = null;
		try {
			cert = new Certificate (caCertificate);
		} catch (NormalizeCertificateException e) {
		}
		if (cert != null) {
			if (cert.getSubjectKeyIdentifier() != null) {
				hmCACertificates.put(cert.getSubjectKeyIdentifier(), caCertificate);
			}
			hmAlternativeCACertificates.put(cert.getCommonName(), caCertificate);
		}
	}
	
	/**
	 * Obtiene un keystore con los certificados contenidos en este objeto. El
	 * alias de cada uno de estos certificados es el SKI del certificado.
	 * 
	 * @return Keystore
	 * @throws KeyStoreException Error creando el keystore o añadiendo alguno de
	 * 	los elementos
	 */
	public KeyStore toKeyStore () throws KeyStoreException {
		
		//-- Crear keystore
		KeyStore ks = KeyStore.getInstance(KeyStoreManager.STORE_TYPE_JKS);
		try {
			ks.load(null, null);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar
		} catch (CertificateException e) {
			// No se va a dar
		} catch (IOException e) {
			// No se va a dar
		}

		//-- Añadir los certificados del objeto al keystore
		List lCACertificates = getCACertificates();
		int i = 0;
		for (Iterator iterator = lCACertificates.iterator(); iterator.hasNext();) {
			X509Certificate caCertificate = (X509Certificate) iterator.next();
			ks.setCertificateEntry("alias" + i, caCertificate);
			i++;
		}
		
		return ks;
	}
}
