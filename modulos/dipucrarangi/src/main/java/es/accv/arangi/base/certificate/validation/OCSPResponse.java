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
import java.security.PublicKey;

import javax.security.auth.x500.X500Principal;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.validation.MalformedOCSPResponseException;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que representa una respuesta OCSP de acuerdo a la especificación indicada
 * en el punto 4.2.1. de la <a href="http://www.ietf.org/rfc/rfc2560.txt" target="rfc">RFC-2560</a>. 
 * Concretamente esta clase envuelve a una respuesta que contiene una respuesta
 * básica OCSP: BasicOCSPResponse. 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class OCSPResponse extends ArangiObject {

	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(OCSPResponse.class);
	
	/*
	 * Respuesta básica OCSP
	 */
	private OCSPResp ocspResponse;
	
	/*
	 * Respuesta básica OCSP
	 */
	private BasicOCSPResp basicOcspResponse;
	
	/*
	 * Respuestas individuales
	 */
	private CertificateOCSPResponse[] responses;
	
	//-- Constructores
	
	/**
	 * Constructor que inicializa el objeto a partir de una respuesta OCSP.
	 * 
	 * @param ocspResponse Respuesta OCSP
	 * @throws MalformedOCSPResponseException La respuesta OCSP no contiene una respuesta OCSP 
	 * 	básica
	 */
	public OCSPResponse (OCSPResp ocspResponse) throws MalformedOCSPResponseException {
		//-- Inicializar el objeto respuesta
		try {
			this.ocspResponse = ocspResponse;
			this.basicOcspResponse = (BasicOCSPResp)ocspResponse.getResponseObject();
		} catch (OCSPException e) {
			logger.info ("[OCSPResponse]::No se encuentra una respuesta básica OCSP en la respuesta OCSP", e);
			throw new MalformedOCSPResponseException ("No se encuentra una respuesta básica OCSP en la respuesta OCSP", e);
		} catch (ClassCastException e) {
			logger.info ("[OCSPResponse]::El objeto contenido en la respuesta OCSP no es una respuesta básica OCSP y no puede ser tratada por esta clase", e);
			throw new MalformedOCSPResponseException ("El objeto contenido en la respuesta OCSP no es una respuesta básica OCSP y no puede ser tratada por esta clase", e);
		}

		//-- Inicializar lista de respuestas individuales
		this.responses = getResponses (basicOcspResponse);
	}
	
	/**
	 * Constructor que inicializa el objeto a partir de un fichero que contiene la respuesta
	 * OCSP.
	 * 
	 * @param fileOCSPResponse Fichero que contiene la respuesta OCSP
	 * @throws MalformedOCSPResponseException El contenido no se corresponde con una respuesta
	 * 	OCSP o no puede ser leído
	 * @throws FileNotFoundException El fichero no existe
	 */
	public OCSPResponse (File fileOCSPResponse) throws MalformedOCSPResponseException, FileNotFoundException {
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream (fileOCSPResponse);
			initialize (fis);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.info ("[OCSPResponse(file)]::No se ha podido cerrar el stream de lectura al fichero: " + fileOCSPResponse.getAbsolutePath(), e);
				}
			}
		}
	}
	
	/**
	 * Constructor que inicializa el objeto a partir de un array de bytes con el contenido de 
	 * la respuesta OCSP.
	 * 
	 * @param bytesOCSPResponse Array de bytes con el contenido de la respuesta OCSP
	 * @throws MalformedOCSPResponseException El contenido no se corresponde con una respuesta
	 * 	OCSP
	 */
	public OCSPResponse (byte[] bytesOCSPResponse) throws MalformedOCSPResponseException {
		initialize (new ByteArrayInputStream (bytesOCSPResponse));
	}
	
	/**
	 * Constructor que inicializa el objeto a partir de un stream de lectura que apunta
	 * al contenido de la respuesta OCSP.
	 * 
	 * @param isOCSPResponse Stream de lectura al contenido de la respuesta OCSP
	 * @throws MalformedOCSPResponseException El contenido no se corresponde con una respuesta
	 * 	OCSP o no puede ser leído
	 */
	public OCSPResponse (InputStream isOCSPResponse) throws MalformedOCSPResponseException {
		initialize (isOCSPResponse);
	}
	
	//-- Métodos públicos
	
	/**
	 * Comprueba la firma de una respuesta OCSP. Este método sólo comprueba que los
	 * datos firmados no han sido modificados desde su firma.
	 * 
	 * @return Cierto si no se han modificado los datos de la respuesta OCSP
	 */
	public boolean isSignatureValid () {
		logger.debug ("[OCSPResponse.isSignatureValid]::Entrada");
		
		//-- Obtener el certificado de la firma
		Certificate signCertificate = getSignatureCertificate();
		
		//-- Validar que los datos de la firma no han cambiado
	 	PublicKey keyCertificadoOCSP = signCertificate.getPublicKey();
	 	try {
	 	    if (!basicOcspResponse.isSignatureValid(new JcaContentVerifierProviderBuilder().build(keyCertificadoOCSP))) {
	 	    	logger.debug ("[OCSPResponse.isSignatureValid]::Los datos de la respuesta OCSP han sido modificados desde su firma");
	 	    	return false;
	 	    }
	 	} catch (OCSPException e) {
	 		logger.info ("[OCSPResponse.isSignatureValid]::No se puede validar la firma", e);
	 		return false;
		} catch (OperatorCreationException e) {
	 		logger.info ("[OCSPResponse.isSignatureValid]::No se puede construir el validador de la firma", e);
	 		return false;
		}
	 	
	 	return true;

	}

	/**
	 * Obtiene el certificado con el que se firmó la respuesta OCSP
	 * 
	 * @return Certificado de firma de la respuesta OCSP
	 */
	public Certificate getSignatureCertificate () {
		logger.debug ("[OCSPResponse.getSignatureCertificate]::Entrada");
		
		return OCSPResponse.getSignatureCertificate(this.basicOcspResponse);
	}
	
	/**
	 * Obtiene las respuestas individuales para cada certificado del que se pidió
	 * su estado.
	 * 
	 * @return Respuestas individuales dentro de la respuesta OCSP
	 */
	public CertificateOCSPResponse[] getSingleResponses () {
		return responses;
	}
	
	/**
	 * Devuelve el estado de la respuesta para el certificado que se pasa como parámetro, 
	 * que será alguna de las siguientes constantes de la clase {@link CertificateValidator CertificateValidator}:<br>
	 * 
	 * <ul>
	 * 	<li><i>RESULT_CERTIFICATE_VALID</i>: El certificado es válido.</li>
	 * 	<li><i>RESULT_CERTIFICATE_REVOKED</i>: El certificado está revocado</li>
	 * 	<li><i>RESULT_CERTIFICATE_UNKNOWN</i>: El certificado es desconocido para el OCSP</li>
	 * 	<li><i>RESULT_CERTIFICATE_CANNOT_BE_VALIDATED</i>: No hay ninguna respuesta para el 
	 * 	certificado</li>
	 * </ul>
	 * 
	 * @param certificate Certificado que originó la respuesta OCSP
	 * @return Estado de la respuesta
	 */
	public int getStatus (ValidateCertificate certificate) {
		logger.debug("[OCSPResponse.getStatus]::Entrada");
		
		for (int i = 0; i < responses.length; i++) {
			if (responses[i].match(certificate)) {
				return responses[i].getStatus();
			}
		}
		
		return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
	}
	
	/**
	 * Devuelve el estado de la respuesta, que será alguna de las siguientes constantes 
	 * de la clase {@link CertificateValidator CertificateValidator}:<br>
	 * 
	 * <ul>
	 * 	<li><i>RESULT_CERTIFICATE_VALID</i>: El certificado es válido.</li>
	 * 	<li><i>RESULT_CERTIFICATE_REVOKED</i>: El certificado está revocado</li>
	 * 	<li><i>RESULT_CERTIFICATE_UNKNOWN</i>: El certificado es desconocido para el OCSP</li>
	 * 	<li><i>RESULT_CERTIFICATE_CANNOT_BE_VALIDATED</i>: No hay ninguna respuesta</li>
	 * </ul><br>
	 * 
	 * Si la petición OCSP incluía varios certificados, habrán varias respuestas en 
	 * este objeto. En este caso es mejor utilizar el método {@link #getStatus(ValidateCertificate) getStatus} 
	 * pasándole el certificado del que se quiere conocer la respuesta.
	 * 
	 * @return Estado de la primera respuesta contenida en este objeto
	 */
	public int getStatus () {
		logger.debug("[OCSPResponse.getStatus]::Entrada");
		
		if (responses.length == 0) {
			return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED; 
		} else {
			return responses[0].getStatus();
		}
	}
	
	/**
	 * Obtiene el valor contenido en el ResponderID de la respuesta OCSP si es un X500Name.
	 * 
	 * @return ResponderID de la respuesta OCSP si es un X500Name. Si es un keyhash devuelve null;
	 */
	public String getResponderIdName () {
		logger.debug("[OCSPResponse.getResponderIdName]::Entrada");
		
		DERTaggedObject derTagged = (DERTaggedObject)basicOcspResponse.getResponderId().toASN1Object().toASN1Primitive();
		if (!(derTagged.getObject() instanceof DERSequence)) {
			logger.debug("[OCSPResponse.getResponderIdName]::Es un KeyHash y no un X509Name");
			return null;
		}
		String valorResponder = X500Name.getInstance((DERSequence)derTagged.getObject()).toString();
		X509Principal certX509Principal = new X509Principal(valorResponder);
		X500Principal cerX500Principal;
		try {
			cerX500Principal = new X500Principal(certX509Principal.getEncoded(ASN1Encoding.DER));
		} catch (IOException e) {
			logger.debug("[OCSPResponse.getResponderIdName]::No se puede obtener el ResponderIDName", e);
			return null;
		}
		return cerX500Principal.getName();

	}
	
	/**
	 * Obtiene el valor contenido en el ResponderID de la respuesta OCSP si es un KeyHash.
	 * 
	 * @return ResponderID de la respuesta OCSP si es un KeyHash. Si es un X509Name devuelve null;
	 */
	public String getResponderIdKeyHash () {
		logger.debug("[OCSPResponse.getResponderIdKeyHash]::Entrada");
		
		DERTaggedObject derTagged = (DERTaggedObject)basicOcspResponse.getResponderId().toASN1Object().toASN1Primitive();
		if (derTagged.getObject() instanceof DERSequence) {
			logger.debug("[OCSPResponse.getResponderIdKeyHash]::Es un X509Name y no un KeyHash");
			return null;
		}
		return ((DEROctetString)derTagged.getObject()).toString();

	}
	
	
	/**
	 * Obtiene el contenido de la respuesta OCSP de forma que puede ser guardada.
	 * 
	 * @return Contenido de la respuesta OCSP
	 */
	public byte[] toDER () {
		try {
			return ocspResponse.getEncoded();
		} catch (IOException e) {
			//-- En la inicialización ya se comprueban estos errores
			logger.info("[OCSPResponse.toDER]::Error de entrada/salida", e);
			return null;
		}
	}
	
	/**
	 * Obtiene la respuesta básica contenida en la respuesta. En algunos tipos de
	 * firma es esta respuesta la que se guarda como respuesta OCSP.
	 * 
	 * @return Respuesta OCSP básica
	 */
	public BasicOCSPResp getBasicOCSPResponse () {
		return basicOcspResponse;
	}
	
	/**
	 * Obtiene la lista de respuestas contenidas en la respuesta OCSP: una por cada
	 * certificado que se mandó validar cuando se hizo la llamada al servidor OCSP.
	 * 
	 * @param basicOcspResponse Respuesta básica OCSP
	 * @return Array de respuestas contenidas en la respuesta OCSP
	 */
	public static CertificateOCSPResponse[] getResponses (BasicOCSPResp basicOcspResponse) {
		
		logger.debug ("[OCSPResponse.getListResponses]::Entrada::" + basicOcspResponse);
		
     	//-- Obtener la lista
		CertificateOCSPResponse[] responses = new CertificateOCSPResponse [basicOcspResponse.getResponses().length];
     	for (int i = 0; i < basicOcspResponse.getResponses().length; i++) {
     		responses [i] = new CertificateOCSPResponse (getSignatureCertificate(basicOcspResponse),
     				basicOcspResponse.getResponses() [i]);
		}
     	
     	return responses;
	}
	
	/**
	 * Obtiene el certificado con el que se firmó la respuesta OCSP que se pasa
	 * como parámetro
	 * 
	 * @param basicOcspResponse Respuesta básica OCSP
	 * @return Certificado de firma de la respuesta OCSP
	 */
	public static Certificate getSignatureCertificate (BasicOCSPResp basicOcspResponse) {
		logger.debug ("[OCSPResponse.getSignatureCertificate]::Entrada::" + basicOcspResponse);
		
		X509CertificateHolder[] certificatePath = basicOcspResponse.getCerts();
	 
	 	// Recuperamos el inicio del camino ( suponemos que el resto de
	 	// certificados estara ya
	 	if (certificatePath == null || certificatePath.length == 0) {
	 		logger.info("[OCSPResponse.getSignatureCertificate]::La respuesta OCSP no está firmada");
	 	    return null;
	 	}
	 	
	 	try {
			return new Certificate (certificatePath[0]);
		} catch (NormalizeCertificateException e) {
			logger.info("[OCSPResponse.getSignatureCertificate]::El certificado no puede ser normalizado para el proveedor criptográfico de Arangi", e);
			return null;
		}

	}
	
	//-- Métodos privados
	
	/*
	 * Inicializa la respuesta OCSP en base a un stream de lectura
	 */
	private void initialize (InputStream is) throws MalformedOCSPResponseException {
		
		//-- Inicializar respuesta
		try {
			ocspResponse = new OCSPResp (is);
			basicOcspResponse = (BasicOCSPResp)ocspResponse.getResponseObject();
		} catch (IOException e) {
			logger.info ("[OCSPResponse.initialize]::El objeto no es una respuesta OCSP bien formada", e);
			throw new MalformedOCSPResponseException ("El objeto no es una respuesta OCSP bien formada", e);
		} catch (OCSPException e) {
			logger.info ("[OCSPResponse.initialize]::No se encuentra una respuesta básica OCSP en la respuesta OCSP", e);
			throw new MalformedOCSPResponseException ("No se encuentra una respuesta básica OCSP en la respuesta OCSP", e);
		} catch (ClassCastException e) {
			logger.info ("[OCSPResponse.initialize]::El objeto contenido en la respuesta OCSP no es una respuesta básica OCSP y no puede ser tratada por esta clase", e);
			throw new MalformedOCSPResponseException ("El objeto contenido en la respuesta OCSP no es una respuesta básica OCSP y no puede ser tratada por esta clase", e);
		}

		
		//-- Inicializar lista de respuestas individuales
		this.responses = getResponses (basicOcspResponse);
	}
	
}
