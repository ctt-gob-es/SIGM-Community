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

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.ocsp.CertID;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.ocsp.OCSPException;
import org.bouncycastle.ocsp.OCSPRespStatus;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.document.ByteArrayDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.validation.MalformedOCSPResponseException;
import es.accv.arangi.base.exception.certificate.validation.OCSPRequestGenerationException;
import es.accv.arangi.base.exception.certificate.validation.OCSPServerConnectionException;
import es.accv.arangi.base.exception.certificate.validation.OCSPValidateException;
import es.accv.arangi.base.exception.device.OpeningDeviceException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que representa un cliente OCSP. Los formatos de las llamadas al OCSP y de
 * sus respuestas siguen el estándar definido en la <a href="http://www.ietf.org/rfc/rfc2560.txt" target="rfc">RFC-2560</a><br><br>
 * 
 * Para validar un certificado:<br><br>
 * <code>
 *  CAList caList = new CAList (new File ("/listCA"));<br>
 *  URL url = new URL ("http://server/ocsp");<br>
 * 	OCSPClient ocsp = new OCSPClient (url, caList);<br><br>
 * 
 * 	Certificate certificate = new Certificate (new File ("c:/certificates/myCertificate.cer"));<br>
 * 	Certificate issuer = new Certificate (new File ("c:/certificates/myCertificateIssuer.cer"));<br><br>
 * 
 * 	int result = ocsp.validate (certificate, issuer);
 * </code>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class OCSPClient extends ArangiObject {

	/*
	 * Class logger
	 */
	static Logger logger = Logger.getLogger(OCSPClient.class);
	
	/*
	 * OCSP's URL
	 */
	URL urlOCSP;
	
	/*
	 * Name that identifies the requestor in the OCSP (normally has null value)
	 */
	String requestorName;
	
	/*
	 * Keystore to sign requests
	 */
	KeyStore keyStore;
	
	/*
	 * Alias name associated with a private key inside the keystore. Private key is
	 * used to sign the OCSP request
	 */
	String alias;
	
	/*
	 * Private key password
	 */
	String aliasPassword;
	
	/**
	 * Constructor con todos los parámetros posibles para realizar la llamada. Normalmente
	 * no es necesario crear las llamadas firmadas o con requestor name, por lo que sólo
	 * se debe usar este constructor para algún OCSP especial.
	 * 
	 * @param urlOCSP URL al OCSP
	 * @param requestorName Nombre que identifica al llamador en el OCSP
	 * @param keystoreFile Fichero keystore
	 * @param keystoreType Tipo de keystore (JKS o PKCS12)
	 * @param keystorePassword Contraseña del keystore
	 * @param alias Alias donde se encuentra la clave privada con la que firmar las
	 * 	llamadas OCSP
	 * @param aliasPassword Contraseña de la clave privada
	 * @throws OpeningDeviceException No se puede abrir el keystore o el alias no existe
	 */
	public OCSPClient(URL urlOCSP, String requestorName, File keystoreFile, 
			String keystoreType, String keystorePassword, String alias, String aliasPassword) throws OpeningDeviceException {
		
		//-- Load keystore
		try {
			//keyStore = KeyStore.getInstance(keystoreType, CRYPTOGRAPHIC_PROVIDER);
			keyStore = KeyStore.getInstance(keystoreType);
		} catch (KeyStoreException e) {
			logger.info("[OCSPClient]::Cannot instantiate a keystore with type '" + keystoreType + "'", e);
			throw new OpeningDeviceException ("Cannot instantiate a keystore with type '" + keystoreType + "'", e);
		} 
		FileInputStream fis = null;
		try {
			fis = new FileInputStream (keystoreFile);
			keyStore.load(fis, keystorePassword.toCharArray());
		} catch (FileNotFoundException e) {
			logger.error("[OCSPClient]::File not found (" + keystoreFile + ")", e);
			throw new OpeningDeviceException ("File not found (" + keystoreFile + ")", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[OCSPClient]::The algorithm used to check the integrity of the keystore cannot be found", e);
			throw new OpeningDeviceException ("The algorithm used to check the integrity of the keystore cannot be found", e);
		} catch (CertificateException e) {
			logger.info("[OCSPClient]::Any of the certificates in the keystore could not be loaded", e);
			throw new OpeningDeviceException ("Any of the certificates in the keystore could not be loaded", e);
		} catch (IOException e) {
			logger.info("[OCSPClient]::there is an I/O or format problem with the keystore data", e);
			throw new OpeningDeviceException ("there is an I/O or format problem with the keystore data", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
		
		//-- Check if alias exists
		try {
			if (!keyStore.containsAlias(alias)) {
				logger.info("[OCSPClient]::The alias '" + alias + "' is not present in the keystore");
				throw new OpeningDeviceException ("The alias '" + alias + "' is not present in the keystore");
			}
		} catch (KeyStoreException e) {
			logger.info("[OCSPClient]::The keystore has not been initialized (loaded)", e);
			throw new OpeningDeviceException ("The keystore has not been initialized (loaded)", e);
		}
		
		//-- Load the rest of parameters
		this.urlOCSP = urlOCSP;
		this.requestorName = requestorName;
		this.alias = alias;
		this.aliasPassword = aliasPassword;
		
	}
	
	/**
	 * Constructor con requestorName.
	 * 
	 * @param urlOCSP URL al OCSP
	 * @param requestorName Nombre que identifica al llamador en el OCSP
	 */
	public OCSPClient(URL urlOCSP, String requestorName) {
		
		this.urlOCSP = urlOCSP;
		this.requestorName = requestorName;
		
	}
	
	/**
	 * Constructor simple: es el caso más habitual
	 * 
	 * @param urlOCSP URL al OCSP
	 */
	public OCSPClient(URL urlOCSP) {
		this (urlOCSP, null);
	}
	
	/**
	 * Valida el certificado llamando al OCSP.
	 * 
	 * @param certificate Certificado a validar
	 * @param issuerCertificate Certificado emisor
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int validate (Certificate certificate, Certificate issuerCertificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		return validate(certificate, issuerCertificate, new Date());
   	}
   		
	/**
	 * Valida el certificado llamando al OCSP.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificate Certificado a validar
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int validate (Certificate certificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		return validate(certificate, new Date());
   	}
   		
	/**
	 * Valida el certificado llamando al OCSP.<br><br>
	 * 
	 * NOTA: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.
	 * 
	 * @param certificate Certificado a validar
	 * @param issuerCertificate Certificado emisor
	 * @param validationDate Fecha en que se realizará la validación
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int validate (Certificate certificate, Certificate issuerCertificate, Date validationDate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		return validate (new Certificate[] { certificate }, new Certificate [] { issuerCertificate }, validationDate) [0];
   	}
   	
	/**
	 * Valida el certificado llamando al OCSP.<br><br>
	 * 
	 * NOTA: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificate Certificado a validar
	 * @param validationDate Fecha en que se realizará la validación
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int validate (Certificate certificate, Date validationDate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		return validate (new Certificate[] { certificate }, validationDate) [0];
   	}
   	
	/**
	 * Valida los certificados llamando al OCSP.
	 * 
	 * @param certificates Certificados a validar
	 * @param issuerCertificates Certificados emisores
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 * @throws NormalizeCertificateException No es posible normalizar alguno de los certificados 
	 * 	de acuerdo al proveedor criptográfico de Arangí
	 */
   	public int[] validate (X509Certificate[] certificates, X509Certificate[] issuerCertificates) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException, NormalizeCertificateException {
   		return validate (certificates, issuerCertificates, new Date());
   	}
   	
	/**
	 * Valida los certificados llamando al OCSP.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que los certificados dispongan de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificates Certificados a validar
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 * @throws NormalizeCertificateException No es posible normalizar alguno de los certificados 
	 * 	de acuerdo al proveedor criptográfico de Arangí
	 */
   	public int[] validate (X509Certificate[] certificates) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException, NormalizeCertificateException {
   		return validate (certificates, new Date());
   	}
   	
	/**
	 * Valida los certificados llamando al OCSP.
	 * 
	 * NOTA: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.
	 * 
	 * @param certificates Certificados a validar
	 * @param issuerCertificates Certificados emisores
	 * @param validationDate Fecha en que se realizará la validación
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 * @throws NormalizeCertificateException No es posible normalizar alguno de los certificados 
	 * 	de acuerdo al proveedor criptográfico de Arangí
	 */
   	public int[] validate (X509Certificate[] certificates, X509Certificate[] issuerCertificates, Date validationDate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException, NormalizeCertificateException {
   		Certificate[] arangiCertificates = new Certificate [certificates.length];
   		Certificate[] arangiIssuers = new Certificate [certificates.length];
   		for (int i = 0; i < certificates.length; i++) {
   			arangiCertificates[i] = new Certificate (certificates[i]);
   			arangiIssuers[i] = new Certificate (issuerCertificates[i]);
		}
   		return validate (arangiCertificates, arangiIssuers, validationDate);
   	}
   		
	/**
	 * Valida los certificados llamando al OCSP.
	 * 
	 * NOTA: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que los certificados dispongan de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificates Certificados a validar
	 * @param validationDate Fecha en que se realizará la validación
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 * @throws NormalizeCertificateException No es posible normalizar alguno de los certificados 
	 * 	de acuerdo al proveedor criptográfico de Arangí
	 */
   	public int[] validate (X509Certificate[] certificates, Date validationDate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException, NormalizeCertificateException {
   		Certificate[] arangiCertificates = new Certificate [certificates.length];
   		for (int i = 0; i < certificates.length; i++) {
   			arangiCertificates[i] = new Certificate (certificates[i]);
		}
   		return validate (arangiCertificates, validationDate);
   	}
   		
	/**
	 * Valida los certificados llamando al OCSP.<br><br>
	 * 
	 * @param certificates Certificados a validar
	 * @param issuerCertificates Certificados emisores
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int[] validate (Certificate[] certificates, Certificate[] issuerCertificates) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		return validate (certificates, issuerCertificates, new Date());
   	}
   	
	/**
	 * Valida los certificados llamando al OCSP.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.<br><br>
	 * 
	 * @param certificates Certificados a validar
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int[] validate (Certificate[] certificates) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		return validate (certificates, new Date());
   	}
   	
	/**
	 * Valida los certificados llamando al OCSP.<br><br>
	 * 
	 * NOTA: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.
	 * 
	 * @param certificates Certificados a validar
	 * @param issuerCertificates Certificados emisores
	 * @param validationDate Fecha en que se realizará la validación
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int[] validate (Certificate[] certificates, Certificate[] issuerCertificates, Date validationDate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		logger.debug("[OCSPClient.validate] :: Start :: " + Arrays.asList(new Object[] { certificates, validationDate }));
   		
   		//-- Obtener la respuesta OCSP
   		OCSPResponse oscpResponse = getOCSPResponse(certificates, issuerCertificates);
   		
     	if (!oscpResponse.isSignatureValid ()) {
   			logger.info ("[OCSPClient.validate] :: La firma de la respuesta OCSP no es correcta");
   			throw new OCSPValidateException ("La firma de la respuesta OCSP no es correcta");
     	}
		
     	//-- Objeto resultados
     	int [] result = new int [certificates.length];
     	
     	//-- Obtener resultados
     	CertificateOCSPResponse[] singleResponses = oscpResponse.getSingleResponses();
     	for (int i = 0; i < singleResponses.length; i++) {
			if(singleResponses[i] != null) {
				
				//-- Check validity interval
				if (singleResponses[i].getValidityPeriodEnd() != null && singleResponses[i].getValidityPeriodEnd().before(new Date())) {
		   			logger.error ("[OCSPClient.validate] :: La respuesta OCSP ha superado su periodo de validez, que acababa el " + Util.dateFormatAccurate.format(singleResponses[i].getValidityPeriodEnd()));
		   			throw new OCSPValidateException ("La respuesta OCSP ha superado su periodo de validez, que acababa el " + Util.dateFormatAccurate.format(singleResponses[i].getValidityPeriodEnd()));
				}
				
				//-- Return result
		     	if(singleResponses[i].getStatus() != ValidationResult.RESULT_CERTIFICATE_REVOKED){
		     		//-- Certificate valid or unknown
		     		result [i] = singleResponses[i].getStatus();
		     		
		     	}else{
		     		//-- Certificate is revoked: mirar la fecha de revocación y compararla con la fecha en que se quiere la validación
	     			if (singleResponses[i].getRevocationTime().before(validationDate) || singleResponses[i].getRevocationTime().equals(validationDate)) {
		     			//-- Certificate revoked
	     				result [i] =  ValidationResult.RESULT_CERTIFICATE_REVOKED;
	     			} else {
			     		//-- Certificate revoked, but revokation is after the validation date
	     				result [i] =  ValidationResult.RESULT_VALID;
	     			}
		     	}			
			}
			
		}
     	
     	//-- Devolver resultado
     	return result;
   	}
   	
	/**
	 * Valida los certificados llamando al OCSP.<br><br>
	 * 
	 * NOTA: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificates Certificados a validar
	 * @param validationDate Fecha en que se realizará la validación
	 * @return int Resultado de la validación
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 * @throws OCSPValidateException la respuesta no es válida (mal firmada o caducada)
	 */
   	public int[] validate (Certificate[] certificates, Date validationDate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, OCSPValidateException {
   		
   		logger.debug("[OCSPClient.validate] :: Start :: " + Arrays.asList(new Object[] { certificates, validationDate }));
   		
   		//-- Obtener la respuesta OCSP
   		OCSPResponse oscpResponse = getOCSPResponse(certificates);
   		
     	if (!oscpResponse.isSignatureValid ()) {
   			logger.info ("[OCSPClient.validate] :: La firma de la respuesta OCSP no es correcta");
   			throw new OCSPValidateException ("La firma de la respuesta OCSP no es correcta");
     	}
		
     	//-- Objeto resultados
     	int [] result = new int [certificates.length];
     	
     	//-- Obtener resultados
     	CertificateOCSPResponse[] singleResponses = oscpResponse.getSingleResponses();
     	for (int i = 0; i < singleResponses.length; i++) {
			if(singleResponses[i] != null) {
				
				//-- Check validity interval
				if (singleResponses[i].getValidityPeriodEnd() != null && singleResponses[i].getValidityPeriodEnd().before(new Date())) {
		   			logger.error ("[OCSPClient.validate] :: La respuesta OCSP ha superado su periodo de validez, que acababa el " + Util.dateFormatAccurate.format(singleResponses[i].getValidityPeriodEnd()));
		   			throw new OCSPValidateException ("La respuesta OCSP ha superado su periodo de validez, que acababa el " + Util.dateFormatAccurate.format(singleResponses[i].getValidityPeriodEnd()));
				}
				
				//-- Return result
		     	if(singleResponses[i].getStatus() != ValidationResult.RESULT_CERTIFICATE_REVOKED){
		     		//-- Certificate valid or unknown
		     		result [i] = singleResponses[i].getStatus();
		     		
		     	}else{
		     		//-- Certificate is revoked: mirar la fecha de revocación y compararla con la fecha en que se quiere la validación
	     			if (singleResponses[i].getRevocationTime().before(validationDate) || singleResponses[i].getRevocationTime().equals(validationDate)) {
		     			//-- Certificate revoked
	     				result [i] =  ValidationResult.RESULT_CERTIFICATE_REVOKED;
	     			} else {
			     		//-- Certificate revoked, but revokation is after the validation date
	     				result [i] =  ValidationResult.RESULT_VALID;
	     			}
		     	}			
			}
			
		}
     	
     	//-- Devolver resultado
     	return result;
	}

	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.
	 * 
	 * @param certificate Certificado a validar
	 * @param issuerCertificate Certificado emisor
	 * @return Respuesta del OCSP
	 * @throws NormalizeCertificateException Alguno de los certificados no ha podido ser 
	 * 	normalizado al proveedor criptográfico de Arangí
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (X509Certificate certificate, X509Certificate issuerCertificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, NormalizeCertificateException {
   		return getOCSPResponse(new Certificate (certificate), new Certificate (issuerCertificate));
   	}
   		
	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificate Certificado a validar
	 * @return Respuesta del OCSP
	 * @throws NormalizeCertificateException Alguno de los certificados no ha podido ser 
	 * 	normalizado al proveedor criptográfico de Arangí
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (X509Certificate certificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException, NormalizeCertificateException {
   		return getOCSPResponse(new Certificate (certificate));
   	}
   		
	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.
	 * 
	 * @param certificate Certificado a validar
	 * @param issuerCertificate Certificado emisor
	 * @return Respuesta del OCSP
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (Certificate certificate, Certificate issuerCertificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException {
   		return getOCSPResponse(new Certificate[] {certificate}, new Certificate [] {issuerCertificate});
   	}
   		
	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.
	 * 
	 * @param certificate Certificado a validar
	 * @return Respuesta del OCSP
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (ValidateCertificate certificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException {
   		return getOCSPResponse(new Certificate[] {certificate}, new Certificate[] {certificate.getIssuerCertificate()});
   	}
   		
	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificate Certificado a validar
	 * @return Respuesta del OCSP
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (Certificate certificate) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException {
   		return getOCSPResponse(new Certificate[] {certificate});
   	}
   		
	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.
	 * 
	 * @param certificates Certificados a validar (debe tener el mismo tamaño que issuerCertificates)
	 * @param issuerCertificates Certificados emisores (debe tener el mismo tamaño que 
	 * 	certificates)
	 * @return Respuesta del OCSP
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (Certificate[] certificates, Certificate[] issuerCertificates) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException {
   		
   		logger.debug("[OCSPClient.getOCSPResponse] :: Entrada");
   		
  		//-- If certificate is null throw an exception
   		if (certificates == null || certificates.length == 0) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: No hay certificados a validar");
   			throw new OCSPRequestGenerationException ("No hay certificados a validar");
   		}
   		
   		//-- If certificate issuer is null throw an exception
   		if (issuerCertificates == null || issuerCertificates.length == 0) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: No hay certificados emisores para realizar la validación");
   			throw new OCSPRequestGenerationException ("No hay certificados emisores para realizar la validación");
   		}
   		
   		//-- If certificate issuer is null throw an exception
   		if (certificates.length != issuerCertificates.length) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: El tamaño de los arrays de certificados y emisores es diferente");
   			throw new OCSPRequestGenerationException ("El tamaño de los arrays de certificados y emisores es diferente");
   		}
   		
   		//-- Generate the OCSP request
   		byte[] ocspRequest = generateOCSPRequest(certificates,	issuerCertificates, requestorName, keyStore, alias, aliasPassword);
   		
   		//-- Obtener respuesta
	    return getOCSPResponse(ocspRequest);
	}

	/**
	 * Obtiene una respuesta del OCSP. En caso de que el OCSP devuelva una respuesta con
	 * errores, éstos se transformarán en una excepción OCSPException. <br><br>
	 * 
	 * Esta respuesta puede ser utilizada dentro del proceso de creación de una firma longeva.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que los certificados dispongan de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificates Certificados a validar (debe tener el mismo tamaño que issuerCertificates)
	 * @return Respuesta del OCSP
	 * @throws OCSPRequestGenerationException Errores generando la petición OCSP, probablemente
	 * 	debido a los parámetros pasados a éste método
	 * @throws OCSPServerConnectionException Errores en la conexión y respuesta del servidor OCSP
	 * @throws MalformedOCSPResponseException La respuesta del servidor OCSP no es una respuesta
	 * 	OCSP bien formada
	 */
   	public OCSPResponse getOCSPResponse (Certificate[] certificates) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException {
   		
   		logger.debug("[OCSPClient.getOCSPResponse] :: Entrada");
   		
   		//-- If certificate is null throw an exception
   		if (certificates == null || certificates.length == 0) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: No hay certificados a validar");
   			throw new OCSPRequestGenerationException ("No hay certificados a validar");
   		}
   		
   		//-- Generate the OCSP request
   		byte[] ocspRequest = generateOCSPRequest(certificates, requestorName, keyStore, alias, aliasPassword);
   		
   		//-- Obtener respuesta
	    return getOCSPResponse(ocspRequest);
	}

	/**
	 * Genera una petición OCSP.
	 * 
	 * @param certificate Certificado a validar
	 * @param issuerCertificate Certificado emisor
	 * @return Array de bytes con la petición OCSP
	 * @throws Exception Errores durante la generación
	 */
   	public static  byte[] generateOCSPRequest(Certificate certificate, Certificate issuerCertificate) throws Exception{
   		
   		return generateOCSPRequest(new Certificate [] { certificate }, new Certificate [] { issuerCertificate });
   		
   	}
   	
	/**
	 * Genera una petición OCSP.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que el certificado disponga de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificate Certificado a validar
	 * @return Array de bytes con la petición OCSP
	 * @throws Exception Errores durante la generación
	 */
   	public static  byte[] generateOCSPRequest(Certificate certificate) throws Exception{
   		
   		return generateOCSPRequest(new Certificate [] { certificate });
   		
   	}
   	
   	/**
	 * Genera una petición OCSP múltiple: se validarán varios certificados al mismo tiempo.
	 * 
	 * @param certificates Certificados a validar
	 * @param issuerCertificates Emisores de los certificados
	 * @return Array de bytes con la petición OCSP
	 * @throws Exception Errores durante la generación
	 */
   	public static  byte[] generateOCSPRequest(Certificate [] certificates, Certificate [] issuerCertificates) throws Exception{
   		
   		return OCSPClient.generateOCSPRequest(certificates, issuerCertificates, null, null, null, null);
   		
   	}
   	
   	/**
	 * Genera una petición OCSP múltiple: se validarán varios certificados al mismo tiempo.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que los certificados dispongan de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
	 * 
	 * @param certificates Certificados a validar
	 * @return Array de bytes con la petición OCSP
	 * @throws Exception Errores durante la generación
	 */
   	public static  byte[] generateOCSPRequest(Certificate [] certificates) throws Exception{
   		
   		return OCSPClient.generateOCSPRequest(certificates, null, null, null, null);
   		
   	}
   	
   	/**
	 * Genera una petición OCSP múltiple: se validarán varios certificados al mismo tiempo.
	 * La petición ser firmará.
   	 * 
	 * @param certificates Certificados a validar
	 * @param issuerCertificates Emisores de los certificados
	 * @param requestorName Nombre que identifica al llamador en el OCSP
	 * @param keystore Keystore abierto que contiene la clave privada que firma la petición
	 * @param alias Alias del keystore para firmar la petición
	 * @param aliasPassword Contraseña de la clave privada
	 * @return Array de bytes con la petición OCSP
   	 * @throws OCSPRequestGenerationException Errores durante la generación
   	 */
   	public static  byte[] generateOCSPRequest(Certificate [] certificates, Certificate [] issuerCertificates,
   			String requestorName, KeyStore keystore, String alias, String aliasPassword) throws OCSPRequestGenerationException{
		
   		//-- Validate parameters
   		if (certificates == null || certificates.length == 0) {
   			throw new OCSPRequestGenerationException ("List of certificates to validate is empty");
   		}
   		if (issuerCertificates == null || issuerCertificates.length == 0) {
   			throw new OCSPRequestGenerationException ("List of CA certificates to validate is empty");
   		}
   		if (certificates.length != issuerCertificates.length) {
   			throw new OCSPRequestGenerationException ("List of certificates and CA certificates must have the same number of elements");
   		}
   		
		// Preparing the request
		OCSPReqBuilder constructorPeticion = new OCSPReqBuilder();
        for (int i=0;i<certificates.length;i++) {
			// Generating the certificateId for request
    		CertificateID identificadorCert;
			try {
				identificadorCert = new CertificateID(new JcaDigestCalculatorProviderBuilder().
						setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build().get(CertificateID.HASH_SHA1),
						new X509CertificateHolder(issuerCertificates [i].toDER()), certificates[i].getSerialNumberBigInteger());
			} catch (Exception e) {
	   			logger.info ("[OCSPClient.generateOCSPRequest] :: Cannot create a new certificate ID for the OCSP request", e);
	   			throw new OCSPRequestGenerationException ("Cannot create a new certificate ID for the OCSP request", e);
			} 
			
			// Adding the request certificate 
			constructorPeticion.addRequest(identificadorCert);
        }
        
        // Requestor name
        if (requestorName != null) {
        	constructorPeticion.setRequestorName(new GeneralName (GeneralName.rfc822Name, requestorName));
        }
		
		// Create the request
   		OCSPReq peticionOCSP;
		try {
			if (keystore == null) {
				peticionOCSP = constructorPeticion.build();
			} else {
				PrivateKey pk = (PrivateKey) keystore.getKey(alias, aliasPassword.toCharArray());
				ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build(pk);
				peticionOCSP = constructorPeticion.build(sha1Signer, new X509CertificateHolder[] { new X509CertificateHolder(keystore.getCertificate(alias).getEncoded()) });
			}
		} catch (Exception e) {
   			logger.info ("[OCSPClient.generateOCSPRequest] :: Cannot create a new OCSP request", e);
   			throw new OCSPRequestGenerationException ("Cannot create a new OCSP request", e);
		} 
		
		// Devolvemos la peticion codificada.
		try {
			return peticionOCSP.getEncoded();
		} catch (IOException e) {
   			logger.info ("[OCSPClient.generateOCSPRequest] :: Cannot encode the OCSP request", e);
   			throw new OCSPRequestGenerationException ("Cannot encode the OCSP request", e);
		}
			
	}

   	/**
	 * Genera una petición OCSP múltiple: se validarán varios certificados al mismo tiempo.
	 * La petición ser firmará.<br/><br/>
	 * 
	 * NOTA: Para que el método funcione es necesario que los certificados dispongan de extensión 
	 * 	Authority Key Identifier. De acuerdo al punto 4.2.1.1 de la RFC-3280 este campo es
	 * 	obligatorio salvo para certificados autofirmados, pero es posible encontrar certificados
	 * 	antiguos o no estandarizados que no dispongan de la extensión.
   	 * 
	 * @param certificates Certificados a validar
	 * @param requestorName Nombre que identifica al llamador en el OCSP
	 * @param keystore Keystore abierto que contiene la clave privada que firma la petición
	 * @param alias Alias del keystore para firmar la petición
	 * @param aliasPassword Contraseña de la clave privada
	 * @return Array de bytes con la petición OCSP
   	 * @throws OCSPRequestGenerationException Errores durante la generación
   	 */
   	public static  byte[] generateOCSPRequest(Certificate [] certificates,
   			String requestorName, KeyStore keystore, String alias, String aliasPassword) throws OCSPRequestGenerationException{
		
   		//-- Validate parameters
   		if (certificates == null || certificates.length == 0) {
   			throw new OCSPRequestGenerationException ("List of certificates to validate is empty");
   		}
   		
		// Preparing the request
   		OCSPReqBuilder constructorPeticion = new OCSPReqBuilder();
        for (int i=0;i<certificates.length;i++) {
			// Generating the certificateId for request
    		CertificateID identificadorCert;
			try {
				identificadorCert = getCertificateID(certificates[i]);
			} catch (HashingException e) {
	   			logger.info ("[OCSPClient.generateOCSPRequest] :: Cannot create a new certificate ID for the OCSP request", e);
	   			throw new OCSPRequestGenerationException ("Cannot create a new certificate ID for the OCSP request", e);
			} 
			
			// Adding the request certificate 
			constructorPeticion.addRequest(identificadorCert);
        }
        
        // Requestor name
        if (requestorName != null) {
        	constructorPeticion.setRequestorName(new GeneralName (GeneralName.rfc822Name, requestorName));
        }
		
		// Create the request
   		OCSPReq peticionOCSP;
		try {
			if (keystore == null) {
				peticionOCSP = constructorPeticion.build();
			} else {
				PrivateKey pk = (PrivateKey) keystore.getKey(alias, aliasPassword.toCharArray());
				ContentSigner sha1Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build(pk);
				peticionOCSP = constructorPeticion.build(sha1Signer, new X509CertificateHolder[] { new X509CertificateHolder(keystore.getCertificate(alias).getEncoded()) });
			}
		} catch (Exception e) {
   			logger.info ("[OCSPClient.generateOCSPRequest] :: Cannot create a new OCSP request", e);
   			throw new OCSPRequestGenerationException ("Cannot create a new OCSP request", e);
		} 
		
		// Devolvemos la peticion codificada.
		try {
			return peticionOCSP.getEncoded();
		} catch (IOException e) {
   			logger.info ("[OCSPClient.generateOCSPRequest] :: Cannot encode the OCSP request", e);
   			throw new OCSPRequestGenerationException ("Cannot encode the OCSP request", e);
		}
			
		
	}

	/**
   	 * Obtiene la URL de este OCSP
   	 * 
   	 * @return URL de este OCSP
   	 */
	public URL getURL() {
		return this.urlOCSP;
	}
	
   	/**
   	 * Obtiene un certificateID (utilizado para realizar peticiones OCSP) a partir 
   	 * de su certificado
   	 * 
   	 * @param certificate Certificado
   	 * @return CertificateID
   	 * @throws HashingException Error realizando el hash del campo issuerDN
   	 */
   	public static CertificateID getCertificateID (Certificate certificate) throws HashingException {
		
   		//-- Obtener el algoritmo
   		AlgorithmIdentifier hashAlg = CertificateID.HASH_SHA1;
   		
   		//-- Obtener el hash del nombre del issuer
   		ByteArrayDocument document = new ByteArrayDocument(certificate.toX509Certificate().getIssuerX500Principal().getEncoded());
   		ASN1OctetString issuerNameHash = new DEROctetString(document.getHash(HashingAlgorithm.SHA1));
   		
   		//-- Obtener el hash de la clave publica del issuer (el authority key identifier)
   		ASN1OctetString issuerKeyHash = null;
   		if (certificate.getIssuerKeyIdentifier() != null) {
   			issuerKeyHash = new DEROctetString(Util.decodeBase64(certificate.getIssuerKeyIdentifier()));
   		}   		 
   		
   		//-- Obtener el CertId
   		CertID certId = new CertID(hashAlg, issuerNameHash, issuerKeyHash, new ASN1Integer(certificate.getSerialNumberBigInteger()));
   		
		return new CertificateID(certId);
	}

	//-- Métodos privados
	
	/*
	 * Obtiene la respuesta OCSP a partir de una petición OCSP
	 */
   	private OCSPResponse getOCSPResponse (byte[] ocspRequest) throws OCSPServerConnectionException, MalformedOCSPResponseException, OCSPRequestGenerationException {
   		
   		logger.debug("[OCSPClient.getOCSPResponse] :: Entrada");
   		
   		//-- Init variables		
		DataOutputStream dataOut = null;
		HttpURLConnection httpConnection;
		try {
			httpConnection = (HttpURLConnection) urlOCSP.openConnection();
		} catch (IOException e) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: Unable to connect to " + urlOCSP, e);
   			throw new OCSPServerConnectionException ("Unable to connect to " + urlOCSP, e);
		}
		
		// Setup the HTTP request			
		httpConnection.setRequestProperty("Content-Type", "application/ocsp-request");	
		httpConnection.setRequestProperty("Accept", "application/ocsp-response");
		
		httpConnection.setDoOutput(true);
        
        // Send the request
        try {
        	dataOut = new DataOutputStream(new BufferedOutputStream (httpConnection.getOutputStream()));
        	dataOut.write(ocspRequest, 0, ocspRequest.length);
        	dataOut.flush();
		} catch (IOException e) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: Error sending the OCSP request to: " + urlOCSP, e);
   			throw new OCSPServerConnectionException ("Error sending the OCSP request to: " + urlOCSP, e);
		} finally {
			if (dataOut != null) {
				try { dataOut.close(); } catch (IOException e) { logger.info ("[OCSPClient.getOCSPResponse] :: No se puede cerrar el stream de escritura a " + urlOCSP, e); }
			}
		}
        
        // Check the HTTP response	        
        int lIntResponseCode;
		try {
			lIntResponseCode = httpConnection.getResponseCode();
		} catch (IOException e) {
   			logger.info ("[OCSPClient.getOCSPResponse] :: Error getting the OCSP response code", e);
   			throw new OCSPServerConnectionException ("Error getting the OCSP response code", e);
		}
        String 	lStrContentType = httpConnection.getContentType();
        if (lIntResponseCode==200 && lStrContentType.equalsIgnoreCase("application/ocsp-response")) {	        
	        // Process the result             
	        int lLngLongitud = httpConnection.getContentLength();
	        
	        DataInputStream dataIn = null;
	        byte[] lBytRespuesta;
			try {
				dataIn = new DataInputStream(httpConnection.getInputStream());
		        lBytRespuesta = new byte[lLngLongitud];
		     	dataIn.readFully(lBytRespuesta, 0, lBytRespuesta.length);	        
		     	
		     	// Get the response from OCSP
		     	OCSPResp ocspResponse = new OCSPResp(lBytRespuesta);
		     	
		     	// Get possible error message
		     	if (ocspResponse.getStatus() == OCSPRespStatus.TRY_LATER) {
		     		throw new OCSPException ("The OCSP response indicates that it will be try later");
		     	}
		     	if (ocspResponse.getStatus() == OCSPRespStatus.INTERNAL_ERROR) {
		     		throw new OCSPException ("The OCSP response indicates that there is an internal error in OCSP server");
		     	}
		     	if (ocspResponse.getStatus() == OCSPRespStatus.MALFORMED_REQUEST) {
		     		throw new OCSPException ("The OCSP response indicates that the request is malformed");
		     	}
		     	if (ocspResponse.getStatus() == OCSPRespStatus.SIGREQUIRED) {
		     		throw new OCSPException ("The OCSP response indicates that the request must be signed");
		     	}
		     	if (ocspResponse.getStatus() == OCSPRespStatus.UNAUTHORIZED) {
		     		throw new OCSPException ("The OCSP response indicates that the request must contain authorization information");
		     	}
		     	
		     	//-- Devolver la respuesta
		     	return new OCSPResponse (ocspResponse);
		     	
			} catch (IOException e) {
	   			logger.info ("[OCSPClient.getOCSPResponse] :: Cannot get the OCSP response", e);
	   			throw new OCSPServerConnectionException ("Cannot get the the OCSP response", e);
			} catch (OCSPException e) {
	   			logger.info ("[OCSPClient.getOCSPResponse] :: Cannot get the OCSP basic response", e);
	   			throw new OCSPServerConnectionException ("Cannot get the the OCSP basic response", e);
			} finally {
				if (dataIn != null) {
					try { dataIn.close(); } catch (IOException e) { logger.info ("[OCSPClient.getOCSPResponse] :: No se puede cerrar el stream de lectura de " + urlOCSP, e); }
				}
			}
	     	
        } else {
   			logger.info ("[OCSPClient.getOCSPResponse] :: HTTP response code is " + lIntResponseCode);
   			throw new OCSPServerConnectionException ("HTTP response code is " + lIntResponseCode);
        }
	    
	}

}
