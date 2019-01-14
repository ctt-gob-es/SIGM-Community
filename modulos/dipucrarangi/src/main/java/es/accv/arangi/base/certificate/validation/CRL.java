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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.DEREnumerated;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.x509.extension.X509ExtensionUtil;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.CRLParsingException;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.ConnectionException;
import es.accv.arangi.base.exception.certificate.InvalidCRLException;



/**
 * Representación de una lista de certificados revocados perteneciente a una CA de
 * acuerdo a la <a href="http://www.ietf.org/rfc/rfc3280.txt" target="rfc">RFC-3280</a>. <br><br>
 * 
 * La CRL puede cargarse de varias fuentes. Si se carga de una URL se entiende que
 * se está descargando una CRL para validar certificados, por lo que se comprobará que
 * la CRL sea válida. En cualquier otro caso la CRL puede estar caducada, por lo que
 * no se realizará la validación durante el proceso de inicialización.<br><br>
 * 
 * Ejemplo de uso:<br>
 * <code>
 *  CAList caList = new CAList (new File ("/listCA"));<br>
 * 	CRL crl = new CRL (new URL ("http://server/crl"), caList);<br>
 * 	Certificate certificate = new Certificate (new File ("c:/certificates/myCertificate.cer"));<br><br>
 * 
 * 	System.out.println ("Is revoked? " + crl.isRevoked (certificate));<br>
 * </code>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class CRL extends ArangiObject{

	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CRL.class);
	
	/*
	 * Objeto CRL
	 */
	private X509CRL crl;
	
	/**
	 * Constructor. El objeto CRL que se pasa como parámetro no se valida. Si se
	 * considera que la CRL debería ser válida a día de hoy llamar al método estático
	 * {@link #validate(X509CRL,CAList) validate}.
	 * 
	 * @param crl CRL a la que envolverá esta clase
	 */
	public CRL (X509CRL crl) {
		this.crl = crl;
	}
	
	/**
	 * Obtiene una CRL en base a un array de bytes. Esta CRL no se 
	 * valida. Si se considera que la CRL debería ser válida a día de hoy llamar al 
	 * método estático {@link #validate(X509CRL,CAList) validate}.
	 * 
	 * @param bytesCRL Array de bytes que contiene la CRL
	 * @throws CRLParsingException No es posible parsear como CRL el contenido del
	 * 	array de bytes
	 */
	public CRL (byte[] bytesCRL) throws CRLParsingException {
		
		logger.debug("[CRL(byte[])]:: Creando CRL desde array de bytes :: " + bytesCRL);
		
		//-- obtener stream
		ByteArrayInputStream bais = new ByteArrayInputStream (bytesCRL);
		
		this.crl = getCRL(bais);
	}
	
	/**
	 * Obtiene una CRL en base a un stream de lectura. Esta CRL no se 
	 * valida. Si se considera que la CRL debería ser válida a día de hoy llamar al 
	 * método estático {@link #validate(X509CRL,CAList) validate}.
	 * 
	 * @param isCRL Stream de lectura que contiene la CRL
	 * @throws CRLParsingException No es posible parsear como CRL el contenido del
	 * 	stream de lectura
	 */
	public CRL (InputStream isCRL) throws CRLParsingException {
		
		logger.debug("[CRL(InputStream)]:: Creando CRL desde stream de lectura :: " + isCRL);
		
		this.crl = getCRL(isCRL);
	}
	
	/**
	 * Obtiene una CRL del fichero que se pasa como parámetro. Esta CRL no se 
	 * valida. Si se considera que la CRL debería ser válida a día de hoy llamar al 
	 * método estático {@link #validate(X509CRL,CAList) validate}.
	 * 
	 * @param crlFile Fichero que contiene la CRL
	 * @throws CRLParsingException No es posible parsear como CRL el contenido del
	 * 	fichero
	 * @throws FileNotFoundException El fichero no existe
	 */
	public CRL (File crlFile) throws CRLParsingException, FileNotFoundException {
		
		logger.debug("[CRL(file)]:: Creando CRL desde " + crlFile.getAbsolutePath());
		
		X509CRL crl;
		FileInputStream fis = null;
		try {
			//-- Get stream
			fis = new FileInputStream (crlFile);
			
			//-- Get CRL
			crl = getCRL(fis);
			
		} catch (FileNotFoundException e) {
			logger.info("[CRL(file)]:: El fichero " + crlFile.getAbsolutePath() + " no existe", e);
			throw e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.info("[CRL(file)]:: Error cerrando el stream de lectura del fichero " + crlFile.getAbsolutePath(), e);
				}
			}
		}
		
		//-- Es válida, la construcción del objeto puede llevarse a cabo
		this.crl = crl;
	}
	
	/**
	 * Obtiene una CRL de la URL que se pasa como parámetro. Comprueba que la CRL es 
	 * válida, si no es así lanza una excepción y no permite que se inicialice este
	 * objeto.
	 * 
	 * @param crlURL URL a una CRL
	 * @param caList Lista de CAs donde poder validar la firma de la CRL
	 * @throws ConnectionException Problemas de conexión impiden obtener la CRL
	 * @throws CRLParsingException No es posible parsear como CRL el documento que
	 * 	se encuentra en la URL
	 * @throws InvalidCRLException La CRL no es válida
	 * @throws CertificateCANotFoundException No se puede validar la firma porque 
	 * 	en la lista de certificados de las CAs falta alguno de la cadena de
	 * 	confianza y es necesario para la correcta finalización del proceso
	 */
	public CRL (URL crlURL, CAList caList) throws ConnectionException, CRLParsingException, InvalidCRLException, CertificateCANotFoundException {
		
		logger.debug("[CRL(url,calist)]:: Buscando una CRL en " + crlURL);
		
		X509CRL crl;
		InputStream inStream = null;
		try {
			//-- Get stream
			URLConnection urlConnection = crlURL.openConnection();
			inStream = urlConnection.getInputStream();
			
			//-- Get CRL
			crl = getCRL(inStream);
			
		} catch (IOException e) {
			logger.info("[CRL(url,calist)]:: Error conectándose a " + crlURL, e);
			throw new ConnectionException ("Error conectándose a " + crlURL, e);
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					logger.info("[CRL(url,calist)]:: Error cerrando el stream de lectura de la URL " + crlURL, e);
				}
			}
		}
		
		//-- Validar la CRL
		validate (crl, caList);
		
		//-- Es válida, la construcción del objeto puede llevarse a cabo
		this.crl = crl;
	}
	
	/**
	 * Devuelve la fecha de revocación del certificado que se pasa como parámetro.
	 * 
	 * @param certificate Certificado
	 * @return Fecha de revocación del certificado o null si el certificado no aparece
	 * 	como revocado en la CRL
	 */
	public Date getRevocationDate (Certificate certificate) {
		
		logger.debug ("[CRL.getRevocationDate]::Entrada::" + certificate.getCommonName());
		
		return getRevocationDate(certificate.getSerialNumberBigInteger());
	}
	
	/**
	 * Devuelve la fecha de revocación del certificado cuyo número de serie
	 * se pasa como parámetro.
	 * 
	 * @param serialNumber Número de serie del certificado
	 * @return Fecha de revocación del certificado o null si el certificado no aparece
	 * 	como revocado en la CRL
	 */
	public Date getRevocationDate (BigInteger serialNumber) {
		
		logger.debug ("[CRL.getRevocationDate]::Entrada::" + serialNumber);
		
		//-- Obtener el número de serie del certificado
		X509CRLEntry entry = crl.getRevokedCertificate(serialNumber);
		if (entry == null) {
			logger.debug ("[CRL.getRevocationDate]::El certificado no está revocado");
			return null;
		}
		
		return entry.getRevocationDate();
	}
	
	/**
	 * Devuelve el motivo de la revocación.
	 * 
	 * @param certificate Certificado
	 * @return Motivo de la revocación o -1 si éste no existe o
	 * el certificado no está revocado
	 */
	public int getRevocationReason (Certificate certificate) {
		
		logger.debug ("[CRL.getRevocationReason]::Entrada::" + certificate.getCommonName());
		
		return getRevocationReason(certificate.getSerialNumberBigInteger());
	}
	
	/**
	 * Devuelve el motivo de la revocación.
	 * 
	 * @param serialNumber Número de serie del certificado
	 * @return Motivo de la revocación o -1 si éste no existe o
	 * el certificado no está revocado
	 */
	public int getRevocationReason (BigInteger serialNumber) {
		
		logger.debug ("[CRL.getRevocationReason]::Entrada::" + serialNumber);
		
		//-- Obtener la entrada del certificado
		X509CRLEntry entry = crl.getRevokedCertificate(serialNumber);
		if (entry == null) {
			logger.debug ("[CRL.getRevocationReason]::El certificado no está revocado");
			return -1;
		}
		
		//-- Obtener la extensión
		byte[] extensionValue = entry.getExtensionValue(Extension.reasonCode.getId());
		if (extensionValue == null) {
			logger.debug ("[CRL.getRevocationReason]::El certificado no contiene el motivo de revocación");
			return -1;
		}
		DEREnumerated reasonCode;
		try {
			reasonCode = (DEREnumerated)X509ExtensionUtil.fromExtensionValue(extensionValue);
		} catch (IOException e) {
			logger.debug ("[CRL.getRevocationReason]::No se puede leer la extensión con el motivo de revocación");
			return -1;
		}
		return reasonCode.getValue().intValue();
	}
	
	/**
	 * Comprueba si el certificado está entre los revocados de la CRL
	 * 
	 * @param certificate Certificado
	 * @return Cierto si el certificado está revocado según esta CRL
	 */
	public boolean isRevoked (Certificate certificate) {
		
		logger.debug ("[CRL.isRevoked]::Entrada::" + certificate.getCommonName());
		
		return getRevocationDate(certificate) != null;
	}
	
	/**
	 * Comprueba si el certificado cuyo número de serie se pasa como
	 * parámetro está entre los revocados de la CRL
	 * 
	 * @param serialNumber Número de serie del certificado
	 * @return Cierto si el certificado está revocado según esta CRL
	 */
	public boolean isRevoked (BigInteger serialNumber) {
		
		logger.debug ("[CRL.isRevoked]::Entrada::" + serialNumber);
		
		return getRevocationDate(serialNumber) != null;
	}
	
	/**
	 * Obtiene una CRL del stream de lectura pasado como parámetro
	 * 
	 * @param is Stream de lectura a una CRL
	 * @return CRL
	 * @throws CRLParsingException No es posible parsear como CRL el documento que
	 * 	se encuentra en la URL
	 */
	public static X509CRL getCRL (InputStream is) throws CRLParsingException {
		
		logger.debug("[CRL.getCRL]::Entrada::Obteniendo CRL de " + is);
		
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509", CRYPTOGRAPHIC_PROVIDER);
			
			//-- Get CRL
			return (X509CRL)cf.generateCRL(is);
		} catch (CertificateException e) {
			logger.info("[CRL(file,calist)]:: Error obteniendo el CertificateFactory para tratar la CRL", e);
			throw new CRLParsingException ("Error obteniendo el CertificateFactory para tratar la CRL", e);
		} catch (CRLException e) {
			logger.info("[CRL(file,calist)]:: Error parseando la CRL", e);
			throw new CRLParsingException ("Error parseando la CRL", e);
		} 

	}
	
	/**
	 * Obtiene la fecha a partir de la cual es válida la CRL.
	 * 
	 * @return Fecha de inicio del periodo de validez de la CRL
	 */
	public Date getValidityPeriodBeginning () {
		return this.crl.getThisUpdate();
	}
	
	/**
	 * Obtiene la fecha a partir de la cual ya no es válida la CRL.
	 * 
	 * @return Fecha de fin del periodo de validez de la CRL
	 */
	public Date getValidityPeriodEnd () {
		return this.crl.getNextUpdate();
	}
	
	/**
	 * Valida la CRL. Las razones para que una CRL no sea válida son:
	 * <ul>
	 * 	<li>Su firma es incorrecta</li>
	 * 	<li>No pertenece a las Autoridades de Certificación de confianza</li>
	 * </ul>
	 * 
	 * @param caList Lista de CAs donde poder validar la firma de la CRL
	 * @throws InvalidCRLException La CRL no es válida
	 * @throws CertificateCANotFoundException No se puede validar la firma porque 
	 * 	en la lista de certificados de las CAs falta alguno de la cadena de
	 * 	confianza y es necesario para la correcta finalización del proceso
	 */
	public void validate (CAList caList) throws InvalidCRLException, CertificateCANotFoundException {
		CRL.validate(this.crl, caList);
	}	
	
	/**
	 * Valida la CRL. Las razones para que una CRL no sea válida son:
	 * <ul>
	 * 	<li>Su firma es incorrecta</li>
	 * 	<li>No pertenece a las Autoridades de Certificación de confianza</li>
	 * </ul>
	 * 
	 * @param crl CRL
	 * @param caList Lista de CAs donde poder validar la firma de la CRL
	 * @throws InvalidCRLException La CRL no es válida
	 * @throws CertificateCANotFoundException No se puede validar la firma porque 
	 * 	en la lista de certificados de las CAs falta alguno de la cadena de
	 * 	confianza y es necesario para la correcta finalización del proceso
	 */
	public static void validate (X509CRL crl, CAList caList) throws InvalidCRLException, CertificateCANotFoundException {
		
		logger.debug ("[CRL.validate]::Entrada");
		
		//-- Comprobar la firma
		boolean validCRLSignature = false;
		for (Iterator iterator = caList.getCACertificates().iterator(); iterator.hasNext();) { 
			
			X509Certificate caCertificate = (X509Certificate) iterator.next();
			
			//-- Verify Signature
			try {
				crl.verify(caCertificate.getPublicKey(), CRYPTOGRAPHIC_PROVIDER_NAME);
				validCRLSignature = true;
				break;
			} catch (Exception e) {
			} 
			
		}
		
		if (!validCRLSignature) {
   			logger.info ("[CRL.validate] :: No es posible verificar la firma de la CRL en la lista de certificados de CA");
   			throw new InvalidCRLException ("No es posible verificar la firma de la CRL en la lista de certificados de CA");
		}
	}
	
	/**
	 * Comprueba si este objeto es la CRL del certificado pasado como
	 * parámetro. Para ello:
	 * <ul>
	 *  <li>Verifica que el emisor del certificado es el que firmó la CRL</li>
	 * </ul>
	 * 
	 * @param certificate Certificado
	 * @return Cierto si la CRL es la del certificado
	 */
	public boolean match (ValidateCertificate certificate) {
		logger.debug("[CRL.match]::Entrada");
		
		try {
			crl.verify(certificate.getIssuerCertificate().getPublicKey(), CRYPTOGRAPHIC_PROVIDER_NAME);
			return true;
		} catch (Exception e) {
   			logger.debug ("[CRL.match]::La CRL no es la del certificado::" + certificate.getIssuerDN());
   			return false;
		} 
		
	}
	
	/**
	 * Obtiene el contenido de la respuesta OCSP de forma que puede ser guardada.
	 * 
	 * @return Contenido de la respuesta OCSP
	 */
	public byte[] toDER () {
		try {
			return crl.getEncoded();
		} catch (CRLException e) {
			logger.info("[CRL.toDER]::Error codificando la CRL a der", e);
			return null;
		}
	}
	
	/**
	 * Obtiene el objecto X509CRL
	 */
	public X509CRL getX509CRL() {
		return this.crl;
	}

}
