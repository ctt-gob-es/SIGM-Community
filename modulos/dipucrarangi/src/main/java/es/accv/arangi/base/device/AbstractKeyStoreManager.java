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
package es.accv.arangi.base.device;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.util.ASN1Dump;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;

import sun.security.util.ObjectIdentifier;
import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.algorithm.CipherAlgorithm;
import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.device.util.CSRUtil;
import es.accv.arangi.base.device.util.DeviceUtil;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.AliasNotFreeException;
import es.accv.arangi.base.exception.device.CertificateInvalidException;
import es.accv.arangi.base.exception.device.CipherException;
import es.accv.arangi.base.exception.device.DeletingObjectException;
import es.accv.arangi.base.exception.device.DeviceNotFoundException;
import es.accv.arangi.base.exception.device.DeviceNotInitializedException;
import es.accv.arangi.base.exception.device.IncorrectPINException;
import es.accv.arangi.base.exception.device.KeyPairException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.device.SavingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.util.Util;

/**
 * Clase abstracta para tratar almacenes de claves en formato software. 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public abstract class AbstractKeyStoreManager extends DeviceManager {
	
	/** Logger de clase */
	private static Logger logger = Logger.getLogger(AbstractKeyStoreManager.class);
	
	/** Keystore de tipo JKS (Java Key Store) **/
	public static final String STORE_TYPE_JKS = "jks";
	
	/** Keystore de tipo PKCS#12 **/
	public static final String STORE_TYPE_PKCS12 = "PKCS12";

	/** Keystore de Mozilla **/
	public static final String STORE_TYPE_MOZILLA = "mozilla";

	/** Keystore de Apple Safari para Mac OS X **/
	public static final String STORE_TYPE_APPLE = "apple";

	/** Keystore de certificados personales de Windows **/
	public static final String STORE_TYPE_PERSONAL_WINDOWS = "pwindows";

	/** Keystore de certificados de ca de Windows **/
	public static final String STORE_TYPE_CA_WINDOWS = "cawindows";

	protected String pin;
	protected byte[] contenido;
	protected KeyStore ks;
	protected String ksType;
	protected String ksFilePath;
	
	//---- IMPLEMENTACION DE MÉTODOS DE IDeviceManager -----//

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#generateKeyPair(java.lang.String)
	 */
	public KeyPair generateKeyPair(String label) throws AliasNotFreeException, DeviceNotInitializedException, KeyPairException {
		return generateKeyPair(label, Util.DEFAULT_KEY_LENGTH);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#generateKeyPair(java.lang.String, int)
	 */
	public KeyPair generateKeyPair(String label, int keySize) throws AliasNotFreeException, DeviceNotInitializedException, KeyPairException {
		logger.debug("Generando par de claves...");
		//-- Comprobamos si existen objetos con el label ``oldLabel" 
		try {
			if (ks.containsAlias(label)){
				logger.debug("El alias ``" + label + "\" ya existe en el KeyStore. No se puede crear el par de claves con ese label");				
				throw new AliasNotFreeException("El alias ``" + label + "\" ya existe en el KeyStore. No se puede crear el par de claves con ese label");
			}
		} catch (KeyStoreException e1) {
			throw new DeviceNotInitializedException("Error comprobando si el label ``" + label + "\" existe", e1);
		}

		//-- Generamos el par de claves
		KeyPair keyPair = null;
		try {
			logger.debug("Generando par...");
			keyPair = genKeys(keySize);	        
		} catch (Exception e) {
			//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
			//-- el error se habría producido mucho antes
			logger.debug("Generando par de claves...ERROR", e);
		}

		//-- Guardamos en el KeyStore la clave privada y un certificado autofirmado 
		//-- que contenga la clave pública.
		try {
	        X509Certificate selfSignedCert = es.accv.arangi.base.certificate.Certificate.generateSelfCertificate(DeviceManager.SELFSIGNED_CERTIFICATE_DN, 30, null, keyPair.getPrivate(), keyPair.getPublic(), false);
			ks.setKeyEntry(label, keyPair.getPrivate(), pin.toCharArray(), new Certificate[]{selfSignedCert});
			logger.debug("Guardando par...OK");
		} catch (Exception e) {
			logger.info("Guardando par de claves...ERROR", e);
			throw new KeyPairException("Error al guardar el nuevo par de claves", e);
		}
		logger.debug("Generando par de claves...OK");
		return keyPair;
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#generatePKCS10(java.lang.String, java.lang.String, java.security.KeyPair)
	 */
	public byte[] generatePKCS10(String subjectDN, String subjectAlternativeDN,
			KeyPair rsaKeyPair) {
			logger.debug("Generando PKCS#10...");
			//-- Construimos la instancia de CertificationRequest
			PKCS10CertificationRequest cr;
			cr = getCertificationRequest(rsaKeyPair, subjectDN, subjectAlternativeDN);
			logger.debug("\n\n=========" + ASN1Dump.dumpAsString(cr) + "\n=========\n\n");
			logger.debug("CertificationRequest creada. dn: " + cr.getSubject());
			logger.debug("Generando PKCS#10...OK");
			try {
				return cr.getEncoded();
			} catch (IOException e) {
				logger.info("No se ha podido obtener el array de bytes del PKCS#10", e);
				return null;
			}
	}
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#changeLabel(java.lang.String, java.lang.String)
	 */
	public void changeAlias(String oldLabel, String newLabel) throws AliasNotFoundException, AliasNotFreeException, SavingObjectException, DeletingObjectException{
		logger.debug("Renombrando objetos...");
		//-- Comprobamos si existen objetos con el label ``oldLabel" 
		if (isAliasFree (oldLabel)){
			logger.debug("El alias '" + oldLabel + "' no existe en el KeyStore");
			throw new AliasNotFoundException ("El alias '" + oldLabel + "' no existe en el KeyStore");
		}
		
		//-- Comprobamos si existen objetos con el label ``newLabel"
		if (!isAliasFree(newLabel)){
			logger.debug("El alias '" + newLabel + "' ya existe en el KeyStore y no puede ser utilizado");
			throw new AliasNotFreeException("El alias '" + newLabel + "' ya existe en el KeyStore y no puede ser utilizado");
		}
		
		//-- Renombramos
		try {
			logger.debug("Renombrando objetos '" + oldLabel + "' a '" + newLabel + "' ...");
			Enumeration<String> items = ks.aliases();
		    while (items.hasMoreElements()) {
				String alias = (String) items.nextElement();
				if (oldLabel.equalsIgnoreCase(alias)){
					//-- Guardar en el alias nuevo y borrar el viejo
					logger.debug("\tActualizando certificado y clave con alias '" + alias + "' a ' " + newLabel + "'");
					if (ks.isKeyEntry(alias)) {
						
						Key key = ks.getKey(alias, pin.toCharArray());
						Certificate[] chain = ks.getCertificateChain(alias);
						
						try {
							ks.setKeyEntry(newLabel, key, pin.toCharArray(), chain);
						} catch (KeyStoreException e) {
							logger.info("[KeyStoreManager.changeAlias]::No se puede guardar la clave privada en el alias '" + newLabel + "'", e);
							throw new SavingObjectException ("No se puede guardar la clave privada en el alias '" + newLabel + "'", e);
						}
						try {
							ks.deleteEntry(alias);
						} catch (KeyStoreException e) {
							logger.info("[KeyStoreManager.changeAlias]::No se puede eliminar la clave privada en el alias '" + alias + "'", e);
							throw new DeletingObjectException ("No se puede eliminar la clave privada en el alias '" + alias + "'", e);
						}
						
					} else if (ks.isCertificateEntry(alias)) {
						logger.debug("\tAñadiendo certificado con alias '" + alias + "' a ' " + newLabel + "");
						Certificate cert = ks.getCertificate(alias);
						try {
							ks.setCertificateEntry(alias, cert);
						} catch (KeyStoreException e) {
							logger.info("[KeyStoreManager.changeAlias]::No se puede guardar el certificado en el alias '" + newLabel + "'", e);
							throw new SavingObjectException ("No se puede guardar el certificado en el alias '" + newLabel + "'", e);
						}
						try {
							ks.deleteEntry(alias);
						} catch (KeyStoreException e) {
							logger.info("[KeyStoreManager.changeAlias]::No se puede eliminar el certificado en el alias '" + alias + "'", e);
							throw new DeletingObjectException ("No se puede eliminar el certificado en el alias '" + alias + "'", e);
						}
					}
				}
			}

		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
			//-- el error se habría producido mucho antes
			logger.debug("Keystore no inicializado", e);
		} catch (NoSuchAlgorithmException e) {
			//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
			//-- el error se habría producido mucho antes
			logger.debug("No se encuentra el algoritmo para alguno de los certificados de la cadena", e);
		} catch (UnrecoverableKeyException e) {
			//-- Antes de obtener la clave se comprueba que existe, por lo que no
			//-- se llegará nunca hasta aquí
			logger.debug("No se encuentra la clave que existía en el alias '" + oldLabel + "'", e);
		}
		logger.debug("Renombrando objetos...OK");
	}
	

	/**
	 * Cambia el PIN que permite abrir el dispositivo criptográfico subyacente. Si el keystore
	 * con el que se inicializó este objeto estaba en memoria, la ejecución de este método no
	 * tiene consecuencias
	 * 
	 * @param newpin PIN nuevo
	 * @throws SavingObjectException Error cambiando el PIN
	 */
	public void changePin(String newpin) throws SavingObjectException {
		logger.debug("Cambiando pin");
		
		//-- Si el keystore está en memoria no tiene sentido
		if (ksFilePath == null) {
			logger.debug("[KeyStoreManager.changePin]::El keystore está en memoria y no tiene sentido cambiarle el PIN");
			return;
		}
		
		//-- Comprobar que el PIN no es nulo
		if (newpin == null || newpin.equals("")) {
			logger.info("[KeyStoreManager.changePin]::El PIN es nulo o está vacío");
			throw new SavingObjectException ("El PIN es nulo o está vacío");
		}
		
		OutputStream os = null;
		try {
			//-- Cambiar el password de todas las claves
			Enumeration<String> items = ks.aliases();
		    while (items.hasMoreElements()) {
		    	String alias = items.nextElement();
		    	if (ks.isKeyEntry(alias)) {
		    		try {
						ks.setKeyEntry(alias, ks.getKey(alias, pin.toCharArray()), newpin.toCharArray(), ks.getCertificateChain(alias));
					} catch (UnrecoverableKeyException e) {
						logger.info("[KeyStoreManager.getPrivateKey]::La clave privada del alias '" + alias + "' no ha podido ser recuperada. " +
								"Compruebe que su password coincide con el PIN del dispositivo.", e);
						throw new SavingObjectException("La clave privada del alias '" + alias + "' no ha podido ser recuperada. Compruebe " +
								"que su password coincide con el PIN del dispositivo.", e);
					}
		    	}
		    }
			//-- Sobreescribimos el keyStore con el nuevo PIN
			os = new FileOutputStream(ksFilePath);
			ks.store(os, newpin.toCharArray());
			pin = newpin;
			logger.debug("PIN cambiado");
		} catch (FileNotFoundException e) {
			//-- No se va a dar 
			logger.info ("[KeyStoreManager.changePin]::No se puede crear un stream de escritura a " + ksFilePath, e);
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.changePin]::Keystore no inicializado", e);
		} catch (NoSuchAlgorithmException e) {
			//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
			//-- el error se habría producido mucho antes
			logger.debug("[KeyStoreManager.changePin]::No se encuentra el algoritmo para alguno de los certificados de la cadena", e);
		} catch (CertificateException e) {
			logger.info("[KeyStoreManager.changePin]::No se puede guardar uno de los certificados del keystore", e);
			throw new SavingObjectException ("No se puede guardar uno de los certificados del keystore", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.changePin]::No se puede guardar el keystore", e);
			throw new SavingObjectException ("No se puede guardar el keystore", e);
		} finally {
			closeStreamSilently(os, "Error cerrando OnputStream sobre ``" + ksFilePath + "´´");
		}
		logger.debug("Cambiando pin...OK");
	}
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getCertificates(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<X509Certificate> getCertificates(String findInAlias, String findInSubjectDn, String findInIssuerDn, String findSerialNumber) {
		
		logger.debug("[KeyStoreManager.getCertificates]::Entrada::" + Arrays.asList(new Object [] { findInAlias, findInSubjectDn, findInIssuerDn, findSerialNumber }));
		List<X509Certificate> result = new ArrayList<X509Certificate>();
		
		//-- Iteramos sobre todos los elementos y filtramos según los criterios
		//-- de búsqueda proporcionados
		try {
			Enumeration<String> items = ks.aliases();
		    while (items.hasMoreElements()) {
				String alias = (String) items.nextElement();
				X509Certificate x509cert = (X509Certificate)ks.getCertificate(alias);
				
				//-- Puede que no haya certificado (sólo clave)
				if (x509cert == null) {
					logger.debug("No hay certificado para el alias:" + alias);
					continue;
				}

				//-- Sometemos al certificado a tantos análisis como filtros se
				//-- haya definido. En cuanto incumpla alguno, desestimamos el
				//-- certificado y vamos a procesar el siguiente.
				
				//-- Búsqueda en el Alias
				if (findInAlias != null){
					if (alias.indexOf(findInAlias) == -1){
						logger.debug("Excluido cert de alias:" + alias);
						continue;
					}
				}
		
				//-- Búsqueda en SubjectDN
				if (findInSubjectDn != null){
					String subjectDn = x509cert.getSubjectDN().toString();
					if (subjectDn != null && subjectDn.indexOf(findInSubjectDn) == -1){
						logger.debug("Excluido cert de SubjectDN:" + subjectDn);
						continue;
					}
				}
				
				//-- Búsqueda en IssuerDN
				if (findInIssuerDn != null){
					String issuerDn = x509cert.getIssuerDN().getName();
					logger.debug("Encontrado issuerDn: " + issuerDn);
					if (issuerDn != null && issuerDn.indexOf(findInIssuerDn) == -1){
						logger.debug("Excluido cert de IssuerDN:" + issuerDn);
						continue;
					}
				}
	
				//-- Búsqueda en Nº de serie
				if (findSerialNumber != null){
					String serialNumber = x509cert.getSerialNumber().toString(16);
					if (serialNumber != null && !serialNumber.equalsIgnoreCase(findSerialNumber)){
						logger.debug("Excluido cert con Nº serie:" + serialNumber);
						continue;
					}
				}
				
				//-- Decisión final: las búsquedas llegados aquí han superado
				//-- todos los filtros definidos ergo hay que devolver el 
				//-- certificado examinado en la iteración en curso
				result.add(x509cert);
			}
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.getCertificates]::Keystore no inicializado", e);
		}
		
		logger.debug("Nº certificados en KeyStore COINCIDENTES: " + result.size());
		logger.debug("getCertificates...OK");
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getCertificate(java.lang.String)
	 */
	public X509Certificate getCertificate(String alias) {
		logger.debug("getCertificate...");
		logger.debug("alias: " + alias);
		
		try {
			X509Certificate certificate = (X509Certificate) ks.getCertificate(alias);
			logger.debug ("[KeyStoreManager.getCertificate]::Obtenido certificado::" + certificate);
			return certificate;
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.getCertificates]::Keystore no inicializado", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importCertificate(java.security.cert.X509Certificate, java.lang.String)
	 */
	public void importCertificate(X509Certificate cert, String theLabel) throws CertificateInvalidException, SavingObjectException {
		importCertificate(cert, theLabel, null);		
	}
	
	/**
	 * Importa el certificado dando la posibilidad de asociarlo con su cadena. Esto
	 * es importante en ciertos casos, como los JKS de firma de código. En éstos,
	 * si no se asocia la cadena las firmas no la incluyen y los applets no funcionan.
	 * 
	 * @param cert Certificado
	 * @param theLabel Alias
	 * @param chain Cadena del certificado
	 * @throws CertificateInvalidException El certificado no es correcto
	 * @throws SavingObjectException Error importando el certificado
	 */
	public void importCertificate(X509Certificate cert, String theLabel, X509Certificate[] chain) throws CertificateInvalidException, SavingObjectException {
		logger.debug("[KeyStoreManager.importCertificate]::Entrada::" + Arrays.asList(new Object[] { cert, theLabel }));
		
		//-- Comprobar que el certificado no es nulo
		if (cert == null) {
			logger.info("[KeyStoreManager.importCertificate]::El certificado a importar es nulo");
			throw new CertificateInvalidException("El certificado a importar es nulo");
		}
		
		//-- Añadir el certificado
		try {
			if (isAliasFree(theLabel)) {
				ks.setCertificateEntry(theLabel, cert);
				logger.debug ("[KeyStoreManager.importCertificate]::Creado nuevo alias '" + theLabel + "' con el certificado");
			} else {
				PrivateKey privateKey = (PrivateKey) ks.getKey(theLabel, pin.toCharArray());
				PublicKey publicKey = getPublicKey(theLabel);
				
				//-- Comprobar que el certificado se corresponde con las claves que hay en el almacén
				if (!Arrays.equals(cert.getPublicKey().getEncoded(), publicKey.getEncoded())) {
					logger.info("[KeyStoreManager.importCertificate]::No ha sido posible asociar el certificado a la clave privada que existía con el alias '" + theLabel + "'");
					throw new SavingObjectException("No ha sido posible asociar el certificado a la clave privada que existía con el alias '" + theLabel + "'");
				}				
				
				if (chain == null) {
					chain = new X509Certificate[0];
				}
				X509Certificate[] completeChain = new X509Certificate[chain.length + 1];
				completeChain [0] = cert;
				for (int i=0;i<chain.length;i++) {
					completeChain[i+1] = chain[i];
				}
				ks.setKeyEntry(theLabel, privateKey, pin.toCharArray(), completeChain);
				logger.debug ("[KeyStoreManager.importCertificate]::Creado nuevo alias '" + theLabel + "' con el certificado y su clave privada");
			}
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.importCertificate]::Keystore no inicializado", e);
		} catch (NoSuchAlgorithmException e) {
			//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
			//-- el error se habría producido mucho antes
			logger.debug("[KeyStoreManager.importCertificate]::No se encuentra el algoritmo para alguno de los certificados de la cadena", e);
		} catch (UnrecoverableKeyException e) {
			logger.info("[KeyStoreManager.importCertificate]::No ha sido posible obtener la clave privada del alias '" + theLabel + "'", e);
			throw new SavingObjectException("No ha sido posible obtener la clave privada del alias '" + theLabel + "'", e);
		} 
		logger.debug("Importando certificado...OK");
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importFromKeystore(es.accv.arangi.base.device.DeviceManager)
	 */
	public void importFromKeystore(DeviceManager deviceManager) throws DeviceNotFoundException, LoadingObjectException, SavingObjectException  {
		
	    logger.debug("[Pkcs11Manager.importAliasFromKeystore]::Entrada::" + Arrays.asList(new Object[] { deviceManager }));

		//-- Llamar al método
		try {
			importAliasFromKeystore(deviceManager, null, null);
		} catch (AliasNotFoundException e) {
			//-- No se dará ya que no se hace la llamada con alias
		}
	    
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#importAliasFromKeystore(es.accv.arangi.base.device.DeviceManager, java.lang.String, java.lang.String)
	 */
	public void importAliasFromKeystore(DeviceManager deviceManager,
			String labelOrigen, String labelDestino) throws DeviceNotFoundException, AliasNotFoundException, LoadingObjectException, SavingObjectException {
		
		logger.debug("[KeyStoreManager.importAliasFromKeystore]::Entrada::" + Arrays.asList(new Object[] { deviceManager, labelOrigen, labelDestino }));
		
		//-- Verificaciones
	    if (deviceManager == null) {
	    	logger.info("[KeyStoreManager.importAliasFromKeystore]::El dispositivo origen es nulo");
	    	throw new DeviceNotFoundException("El dispositivo origen es nulo.");
	    }
	    
	    // Comprobar que el label a importar existe en el dispositivo
	    try {
			if (labelOrigen != null && deviceManager.isAliasFree(labelOrigen)) {
				logger.info ("[KeyStoreManager.importAliasFromKeystore]::El alias '" + labelOrigen + "' no existe en el dispositivo origen de la importación.");
				throw new AliasNotFoundException("El alias '" + labelOrigen + "' no existe en el dispositivo origen de la importación.");
			}
		} catch (SearchingException e) {
	    	logger.info ("[KeyStoreManager.importAliasFromKeystore]::No se ha podido determinar si el alias '" + labelOrigen +"' existe en el dispositivo origen.", e);
	    	throw new LoadingObjectException("No se ha podido determinar si el alias '" + labelOrigen +"' existe en el dispositivo origen.", e);
		}
	    
	    //-- Iniciamos el proceso de importacion: si labelOrigen es nulo importamos todo	    
	    String[] aliasToImport;
	    if (labelOrigen != null) {
	    	aliasToImport = new String [] {labelOrigen};
	    } else {
	    	try {
				aliasToImport = deviceManager.getAliasNamesList();
			} catch (SearchingException e) {
		    	logger.info ("[KeyStoreManager.importAliasFromKeystore]::No se ha podido leer la información del dispositivo origen.", e);
		    	throw new LoadingObjectException("No se ha podido leer la información del dispositivo origen.", e);
			}
	    }
    	
	    //-- Recorrer todos los elementos e ir importando
    	for (int i = 0; i < aliasToImport.length; i++) {
			String alias = aliasToImport [i];
		
			//-- Buscar alias destino
			String nombreEnDestino = labelDestino;
			if (labelOrigen != null && labelDestino == null) {
				nombreEnDestino = labelOrigen;
			}
			if (labelOrigen == null) {
				nombreEnDestino = alias;
			}
			
			//-- Si el alias ya existe buscar otro nombre
			int aliasNumber = 0;
			while (true) {
				if (isAliasFree(nombreEnDestino)) {
					break;
				}
				nombreEnDestino += aliasNumber;
				aliasNumber++;
			}
			
			try {
				//-- Buscar el certificado y la clave
				X509Certificate certificate;
				try {
					certificate = deviceManager.getCertificate(alias);
				} catch (SearchingException e) {
			    	logger.info ("[KeyStoreManager.importAliasFromKeystore]::Error al obtener el certificado para el alias '" + alias + "'", e);
			    	throw new SavingObjectException("Error al obtener el certificado para el alias '" + alias + "'", e);
				}
				PrivateKey privateKey = deviceManager.getPrivateKey(alias);
				
				//-- Si este manager es de un PKCS#12 y no hay clave privada a importar nos
				//-- saltamos este elemento
				if (this.ksType.equals(STORE_TYPE_PKCS12) && privateKey == null) {
					logger.info("[KeyStoreManager.importAliasFromKeystore]::El alias '" + alias + "' no se puede " +
							"importar porque no contiene clave privada y este manager es un PKCS#12.");
					continue;
				}
			
				if (privateKey == null) {
					ks.setCertificateEntry(nombreEnDestino, certificate);
					logger.debug ("[KeyStoreManager.importAliasFromKeystore]::Creado nuevo alias '" + nombreEnDestino + "' con el certificado");
				} else {
					ks.setKeyEntry(nombreEnDestino, privateKey, pin.toCharArray(), new X509Certificate [] { certificate });
					logger.debug ("[KeyStoreManager.importAliasFromKeystore]::Creado nuevo alias '" + nombreEnDestino + "' con el certificado y su clave privada");
				}
				
			} catch (KeyStoreException e) {
		    	logger.info ("[KeyStoreManager.importAliasFromKeystore]::Error al guardar los elementos para el alias '" + alias + "'", e);
		    	throw new SavingObjectException("Error al guardar los elementos para el alias '" + alias + "'", e);
			} 
			
    	}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#deleteObjects(java.lang.String)
	 */
	public int deleteObjects(String label) throws DeletingObjectException {
		logger.debug("Borrando objetos...");
		logger.debug("Label: " + label);
		//-- Comprobamos si existen objetos con el label ``oldLabel"
		int deleted = 0;
		try {
			if (!isAliasFree (label)){
				ks.deleteEntry(label);
				deleted++;
			}
		} catch (KeyStoreException e1) {
			logger.info ("Borrando objetos...ERROR", e1);
			throw new DeletingObjectException("Error borrando objetos con label '" + label + "'", e1);
		}
		
		logger.debug("Eliminados: " + deleted);
		logger.debug("Borrando objetos...OK");
		return deleted;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getAliasNamesList()
	 */
	public String[] getAliasNamesList() {
		logger.debug("Obteniendo todos los alias...");
		
		Enumeration<String> enumAlias = null;
		try {
			enumAlias = ks.aliases();
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.getAliasNamesList]::Keystore no inicializado", e);
		}
		List<String> lAlias = new ArrayList<String> ();
		while (enumAlias != null && enumAlias.hasMoreElements()) {
			String alias = (String) enumAlias.nextElement();
			lAlias.add(alias);
		}
		
		return (String[]) lAlias.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#deletePrivateKey(java.lang.String)
	 */
	public int deletePrivateKey(String label) throws DeletingObjectException {
		logger.debug("[KeyStoreManager.deletePrivateKey]::Entrada::" + label);
		
		//-- Comprobar si la etiqueta existe y es un PKCS#12 (no se puede eliminar la clave)
		boolean casoErroneo = false;
		try {
			casoErroneo = ksType != null && ksType.equals(STORE_TYPE_PKCS12) && ks.containsAlias(label) && ks.isKeyEntry(label);
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.deletePrivateKey]::Keystore no inicializado", e);
		}
		if (casoErroneo) {
			logger.info("[KeyStoreManager.deletePrivateKey]::No se puede eliminar una clave privada de un alias de un PKCS#12");
			throw new DeletingObjectException("No se puede eliminar una clave privada de un alias de un PKCS#12");
		}
		
		try {
			//-- Si no es una entrada con clave devolver 0
			if (!ks.containsAlias(label) || !ks.isKeyEntry(label)) {
				logger.debug("[KeyStoreManager.deletePrivateKey]::No se puede eliminar la clave privada del alias '" + label + "' porque ésta no existe");
				return 0;
			}
			
			//-- Obtener el certificado del alias antes de eliminarlo
			Certificate certificate = ks.getCertificate(label);
			try {
				ks.deleteEntry(label);
			} catch (KeyStoreException e) {
				logger.info("[KeyStoreManager.deletePrivateKey]::No se puede eliminar la clave privada del alias '" + label + "'", e);
				throw new DeletingObjectException("No se puede eliminar la clave privada del alias '" + label + "'", e);
			}
			
			//-- Si existía el certificado volver a crear el alias sólo con él
			if (certificate == null) {
				logger.debug("[KeyStoreManager.deletePrivateKey]::El alias sólo contenía la clave privada y ya está eliminada");
			} else {
				try {
					ks.setCertificateEntry(label, certificate);
				} catch (KeyStoreException e) {
					logger.info("[KeyStoreManager.deletePrivateKey]::No ha sido posible guardar el certificado que había junto a la clave privada en el alias '" + label + "'", e);
					throw new DeletingObjectException("No ha sido posible guardar el certificado que había junto a la clave privada en el alias '" + label + "'", e);
				}
			}
			
			return 1;
			
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.deletePrivateKey]::Keystore no inicializado", e);
			return 0;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getPrivateKeys(java.lang.String)
	 */
	public PrivateKey getPrivateKey(String alias) throws LoadingObjectException{
		return getPrivateKey(alias, pin);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getPrivateKey(java.lang.String, java.lang.String)
	 */
	public PrivateKey getPrivateKey(String alias, String keyPassword) throws LoadingObjectException{
		
		logger.debug("[KeyStoreManager.getPrivateKey]::Entrada::" + Arrays.asList(new Object [] { alias }));
		
		try {
			//-- Si el alias no existe devolver null
			if (isAliasFree(alias)) {
				logger.debug("[KeyStoreManager.getPrivateKey]::No existe el alias '" + alias + "'. Devolver null");
				return null;
			}
			
			//-- Comprobar si hay clave privada
			if (!ks.isKeyEntry(alias)) {
				logger.debug("[KeyStoreManager.getPrivateKey]::El alias '" + alias + "' no tiene una clave privada");
				return null;
			}
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.deletePrivateKey]::Keystore no inicializado", e);
		}

		try {
			//-- Devolver la clave privada
			logger.debug("[KeyStoreManager.getPrivateKey]::Encontrada clave privada con alias '" + alias + "'");
			
			return (PrivateKey) ks.getKey(alias, keyPassword.toCharArray());
	
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.getPrivateKey]::Keystore no inicializado", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			//-- Este error sólo se produce si no se ha cargado Bouncy, con lo que
			//-- el error se habría producido mucho antes
			logger.debug("[KeyStoreManager.getPrivateKey]::No se encuentra el algoritmo para alguno de los certificados de la cadena", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			logger.info("[KeyStoreManager.getPrivateKey]::La clave privada del alias '" + alias + "' no ha podido ser recuperada. " +
					"Compruebe que su password coincide con el PIN del dispositivo.", e);
			throw new LoadingObjectException("La clave privada del alias '" + alias + "' no ha podido ser recuperada. Compruebe " +
					"que su password coincide con el PIN del dispositivo.", e);
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#getPublicKeys(java.lang.String)
	 */
	public PublicKey getPublicKey(String alias) {
		logger.debug("[KeyStoreManager.getPublicKey]::Entrada::" + alias);
		
		try {
			//-- Si el alias no existe devolver null
			if (!ks.containsAlias(alias)) {
				logger.debug("[KeyStoreManager.getPublicKey]::No existe el alias '" + alias + "'. Devolver null");
				return null;
			}
			
			//-- Buscar la clave pública
			Certificate certificate = ks.getCertificate(alias);
			if (certificate == null || certificate.getPublicKey() == null) {
				logger.debug("[KeyStoreManager.getPublicKey]::El alias '" + alias + "' no tiene una clave pública");
				return null;
			}
			
			//-- Clave pública encontrada
			logger.debug("[KeyStoreManager.getPublicKey]::Encontrada clave pública con alias '" + alias + "'");
			return certificate.getPublicKey();
			
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.getPublicKey]::Keystore no inicializado", e);
			return null;
		} 
		
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.es.accv.arangi.base.device.DeviceManager#signDocument(java.io.InputStream, java.lang.String)
	 */
	public byte[] signDocument(InputStream document, String label) 
		throws AliasNotFoundException, LoadingObjectException, SignatureException {
		return signDocument(document, label, DigitalSignatureAlgorithm.SHA1_RSA);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.es.accv.arangi.base.device.DeviceManager#signDocument(java.io.InputStream, java.lang.String, java.lang.String)
	 */
	public byte[] signDocument(InputStream document, String label, String signatureAlgorithm) 
		throws AliasNotFoundException, LoadingObjectException, SignatureException {
		
		logger.debug ("[KeyStoreManager.signDocument]::Entrada::" + Arrays.asList(new Object[] { document, label }));
		
		//-- Comprobar que la etiqueta existe
		if (isAliasFree(label)) {
			logger.info("[KeyStoreManager.signDocument]::El alias '" + label + "' no se encuentra en el keystore");
			throw new AliasNotFoundException ("El alias '" + label + "' no se encuentra en el keystore");
		}
		
		//-- Obtener la clave privada
		PrivateKey privateKey = getPrivateKey (label);
		if (privateKey == null) {
			logger.info("[KeyStoreManager.signDocument]::El alias '" + label + "' hace referencia a un objeto que no es una clave privada");
			throw new LoadingObjectException ("El alias '" + label + "' hace referencia a un objeto que no es una clave privada");
		}
		
		//-- Firmar
		try {
			Signature sign = Signature.getInstance(signatureAlgorithm);
			sign.initSign(privateKey);
			byte[] buffer = new byte[1024];
			while (true) {
				int n = document.read(buffer);
				if (n < 0) {
					break;
				} else {
					sign.update(buffer, 0, n);
				}
			}
			byte[] bytesSignature = sign.sign();
	        logger.debug("[KeyStoreManager.signDocument]::Se ha completado la firma");
	        return bytesSignature;
	        
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.signDocument]::No existe el algoritmo de firma '" + signatureAlgorithm + "'", e);
			throw new SignatureException ("No existe el algoritmo de firma '" + signatureAlgorithm + "'", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.signDocument]::No se puede firmar debido a que la clave privada no es válida", e);
			throw new SignatureException ("No se puede firmar debido a que la clave privada no es válida", e);
		} catch (java.security.SignatureException e) {
			logger.info("[KeyStoreManager.signDocument]::No es posible realizar la firma", e);
			throw new SignatureException ("No es posible realizar la firma", e);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.signDocument]::Se ha producido una excepción de entrada/salida leyendo el stream del fichero", e);
			throw new SignatureException ("Se ha producido una excepción de entrada/salida leyendo el stream del fichero", e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.es.accv.arangi.base.device.DeviceManager#signDocument(es.accv.arangi.base.document.IDocument, java.lang.String)
	 */
	public byte[] signDocument(IDocument document, String label) 
		throws AliasNotFoundException, LoadingObjectException, SignatureException {
		return signDocument (document.getInputStream(), label);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.es.accv.arangi.base.device.DeviceManager#signDocument(es.accv.arangi.base.document.IDocument, java.lang.String, java.lang.String)
	 */
	public byte[] signDocument(IDocument document, String label, String signatureAlgorithm) 
		throws AliasNotFoundException, LoadingObjectException, SignatureException {
		return signDocument (document.getInputStream(), label, signatureAlgorithm);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.es.accv.arangi.base.device.DeviceManager#signHash(byte[], java.lang.String)
	 */
	public byte[] signBytesHash(byte[] hash, String label) 
		throws HashingException, AliasNotFoundException, LoadingObjectException, CipherException {
		return signBytesHash(hash, label, CipherAlgorithm.RSA);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.es.accv.arangi.base.device.DeviceManager#signHash(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] signBytesHash(byte[] hash, String label, String cipherAlgorithm) 
		throws HashingException, AliasNotFoundException, LoadingObjectException, CipherException {
		logger.debug("[KeyStoreManager.signHash]::Entrada::" + Arrays.asList(new Object[] { hash, label, cipherAlgorithm }));
		
		//-- Comprobar que la etiqueta existe
		if (isAliasFree(label)) {
			logger.info("[KeyStoreManager.signHash]::El alias '" + label + "' no se encuentra en el keystore");
			throw new AliasNotFoundException ("El alias '" + label + "' no se encuentra en el keystore");
		}
		
		//-- Obtener la clave privada
		PrivateKey privateKey = getPrivateKey (label);
		if (privateKey == null) {
			logger.info("[KeyStoreManager.signDocument]::El alias '" + label + "' hace referencia a un objeto que no es una clave privada");
			throw new LoadingObjectException ("El alias '" + label + "' hace referencia a un objeto que no es una clave privada");
		}
		
		//-- Comprobar que el hash no sea nulo
		if (hash == null) {
			logger.info("[KeyStoreManager.signHash]::El hash a firmar es nulo");
			throw new HashingException ("No es posible obtener el objeto DER que contiene el hash");
		}
		
		//-- Algoritmo de hashing
		String hashingAlgorithm = HashingAlgorithm.getAlgorithmNameFromHash(hash);
		
		//-- Transformar el hash en un objeto DER
		try {
			hash = DeviceUtil.getSignData(new ObjectIdentifier (HashingAlgorithm.getOID (hashingAlgorithm)), hash);
		} catch (IOException e) {
			logger.info("[KeyStoreManager.signHash]::No es posible obtener el objeto DER que contiene el hash", e);
			throw new HashingException ("No es posible obtener el objeto DER que contiene el hash", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.signHash]::El algoritmo de hashing '" + hashingAlgorithm + "' provoca una excepción", e);
			throw new HashingException ("El algoritmo de hashing '" + hashingAlgorithm + "' provoca una excepción", e);
		}
		
		//-- Cifrar con la clave privada
		return encrypt(hash, privateKey, cipherAlgorithm);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#decrypt(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] decrypt(byte[] encryptedDocument, String label, String cipherAlgorithm) throws AliasNotFoundException, LoadingObjectException, CipherException {
		
		logger.debug ("[KeyStoreManager.decrypt]::Entrada::" + Arrays.asList(new Object[] { encryptedDocument, label }));
		
		//-- Comprobar que la etiqueta existe
		if (isAliasFree(label)) {
			logger.info("[KeyStoreManager.decrypt]::El alias '" + label + "' no se encuentra en el keystore");
			throw new AliasNotFoundException ("El alias '" + label + "' no se encuentra en el keystore");
		}
		
		//-- Comprobar que la etiqueta hace referencia a una clave
		try {
			if (!ks.isKeyEntry(label)) {
				logger.info("[KeyStoreManager.decrypt]::El alias '" + label + "' no contiene una clave privada");
				throw new LoadingObjectException ("El alias '" + label + "'  no contiene una clave privada");
			}
		} catch (KeyStoreException e) {
			//-- Este error sólo se produce si no se ha inicializado la clase, lo que
			//-- ya se comprueba y no puede ocurrir
			logger.debug("[KeyStoreManager.decrypt]::Keystore no inicializado", e);
			return null;
		}
		
		//-- Obtener la clave privada
		PrivateKey privateKey = getPrivateKey (label);
		if (privateKey == null) {
			logger.info("[KeyStoreManager.decrypt]::El alias '" + label + "' hace referencia a un objeto que no es una clave privada");
			throw new LoadingObjectException ("El alias '" + label + "' hace referencia a un objeto que no es una clave privada");
		}
		
		//-- Descifrar
		try {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] bytesSignature = cipher.doFinal(encryptedDocument);
			logger.debug("[KeyStoreManager.decrypt]::Se ha completado el cifrado");
	        return bytesSignature;
	        
		} catch (NoSuchAlgorithmException e) {
			logger.info("[KeyStoreManager.decrypt]::No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
			throw new CipherException ("No existe el algoritmo de cifrado '" + cipherAlgorithm + "'", e);
		} catch (InvalidKeyException e) {
			logger.info("[KeyStoreManager.decrypt]::La clave privada no es válida", e);
			throw new CipherException ("La clave privada no es válida", e);
		} catch (IllegalBlockSizeException e) {
			logger.info("[KeyStoreManager.decrypt]::El tamaño de la información de un bloque de cifrado es incorrecta", e);
			throw new CipherException ("El tamaño de la información de un bloque de cifrado es incorrecta", e);
		} catch (BadPaddingException e) {
			logger.info("[KeyStoreManager.decrypt]::El relleno de la información no es el adecuado para el algoritmo de relleno", e);
			throw new CipherException ("El relleno de la información no es el adecuado para el algoritmo de relleno", e);
		} catch (NoSuchPaddingException e) {
			logger.info("[KeyStoreManager.decrypt]::No existe el algoritmo de relleno", e);
			throw new CipherException ("No existe el algoritmo de relleno", e);
		} 
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#encrypt(byte[], java.lang.String, java.lang.String)
	 */
	public byte[] encrypt(byte[] document, String label, String cipherAlgorithm) throws AliasNotFoundException, CipherException {
		logger.debug ("[KeyStoreManager.encrypt]::Entrada::" + Arrays.asList(new Object[] { document, label }));
		
		//-- Comprobar que la etiqueta existe
		if (isAliasFree(label)) {
			logger.info("[KeyStoreManager.encrypt]::El alias '" + label + "' no se encuentra en el keystore");
			throw new AliasNotFoundException ("El alias '" + label + "' no se encuentra en el keystore");
		}
		
		//-- Obtener la clave pública
		PublicKey key = getPublicKey(label);
		
		//-- Cifrar el documento
		return encrypt(document, key, cipherAlgorithm);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#format()
	 */
	public void clear() throws DeletingObjectException {

		logger.debug ("[KeyStoreManager.clear]::Entrada");

		//-- Recorrer todos los alias y eliminar los objetos que hay en ellos
		
		String[] aliases = getAliasNamesList();
		for (int i = 0; i < aliases.length; i++) {
			logger.debug ("[KeyStoreManager.clear]::Procediendo a eliminar el alias '" + aliases[i] + "'");
			deleteObjects(aliases[i]);
			logger.debug ("[KeyStoreManager.clear]::Eliminado el alias '" + aliases[i] + "'");
		}
		logger.debug ("[KeyStoreManager.clear]::Fin");

	}

	//---- METODOS PÚBLICOS ----//

	/**
	 * Devuelve el objeto KeyStore al que recubre esta clase.
	 * 
	 * @return Objeto KeyStore
	 */
	public KeyStore getKeyStore () {
		logger.debug("[KeyStoreManager.getKeyStore]::Entrada");
		return this.ks;
	}
	
	/**
	 * Devuelve el tipo de keystore (PKCS12 o JKS)
	 * 
	 * @return Tipo de keystore
	 */
	public String getKeyStoreType () {
		logger.debug("[KeyStoreManager.getKeyStoreType]::Entrada");
		return this.ksType;
	}
	
	//-- Métodos abstractos
	
	public abstract void initialize() throws OpeningDeviceException, IncorrectPINException ;
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.device.DeviceManager#isAliasFree(java.lang.String)
	 */
	public abstract boolean isAliasFree(String alias);

	

	//---- METODOS PRIVADOS ----//
	
	/*
	 * Cierra el stream. Si salta una excepción la recoge en los logs.
	 */
	private void closeStreamSilently(OutputStream os, String mensaje){
		if (os != null){
			try {
				os.close();
			} catch (Exception e) {/* Glurp*/
				if (mensaje != null){
					logger.info(mensaje);
				}
			}
		}
	}
	
	/*
	 * 
	 * Obtiene una peticion de certificado.
	 * 
	 * @param signatureAlgorithmString
	 * @param wscr
	 * @param rsaKeys
	 * @return
	 * @throws Exception 
	 */
	private static PKCS10CertificationRequest getCertificationRequest(KeyPair rsaKeys, String subjectDN, String subjectAlternativeDN) {

		PrivateKey privateKey = rsaKeys.getPrivate();
		PublicKey publicKey = rsaKeys.getPublic();
    
		try {
			// -- Obtenemos el nombre estandar
			X500Name x500Name = es.accv.arangi.base.certificate.Certificate.stringToBcX500Name( subjectDN );
			logger.debug("[KeyStoreManager.getCertificationRequest]:: x500Name: " + x500Name);
	  
			//-- Parametros para la creacion del bloque de extensiones
			Map extensionsMap = new HashMap();
			extensionsMap.put("subjectAlternativeNameDN", subjectAlternativeDN);
			
		    // Hacer el publicKeyInfo
		    SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo((ASN1Sequence) new ASN1InputStream(
		            new ByteArrayInputStream(publicKey.getEncoded())).readObject());
	
		    // Generate PKCS10 certificate request
		    PKCS10CertificationRequestBuilder builder = new PKCS10CertificationRequestBuilder(x500Name, spki);
		    builder.addAttribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest, CSRUtil.getPKCS10Set(publicKey, publicKey, extensionsMap));
		    
		    // Hace el contentSigner
		    ContentSigner signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build(privateKey);
		    
		    // Obtener el pkcs10
		    PKCS10CertificationRequest req = builder.build(signer);
	
			logger.debug("[KeyStoreManager.getCertificationRequest]:: req: " + req);
			return req;
			
		} catch (Exception e) {
			logger.info("No es posible generar el PKCS#10", e);
			return null;
		}
	}
	
    /*
     * Genera un par de claves
     *
     * @param keysize tamaño de la clave a generar, 1024 es el valor más habitual para
     * claves RSA
     *
     * @return KeyPair Par de claves generado
     */
    protected static KeyPair genKeys(int keysize)
        throws NoSuchAlgorithmException, NoSuchProviderException {

        KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA", ArangiObject.CRYPTOGRAPHIC_PROVIDER);
        keygen.initialize(keysize);

        KeyPair rsaKeys = keygen.generateKeyPair();

        logger.debug("Generated " + rsaKeys.getPublic().getAlgorithm() + " keys with length " +
            ((RSAPrivateKey) rsaKeys.getPrivate()).getPrivateExponent().bitLength());

        return rsaKeys;
    } // genKeys


}

