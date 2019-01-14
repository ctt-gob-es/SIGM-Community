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

import java.util.Date;

import org.apache.log4j.Logger;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que contiene cada una de las respuestas contenidas en la respuesta OCSP. 
 * Normalmente sólo habrá una, pero se puede realizar una petición para varios
 * certificados, en cuyo caso habrá una de estas respuestas para cada uno de
 * ellos.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class CertificateOCSPResponse extends ArangiObject {
	
	/*
	 * Certificado de firma de la respuesta OCSP
	 */
	Certificate signatureCertificate;
	
	/*
	 * Respuesta para un certificado
	 */
	SingleResp singleResponse;
	
	/*
	 * Logger de la clase
	 */
	Logger logger = Logger.getLogger(CertificateOCSPResponse.class);
	
	/**
	 * Constructor
	 * 
	 * @param signatureCertificate Certificado de firma de la respuesta OCSP
	 * @param singleResponse Respuesta para un certificado
	 */
	public CertificateOCSPResponse (Certificate signatureCertificate, SingleResp singleResponse) {
		this.signatureCertificate = signatureCertificate;
		this.singleResponse = singleResponse;
	}
	
	/**
	 * Comprueba si este objeto es una respuesta para el certificado pasado como
	 * parámetro. Para ello:
	 * <ul>
	 * 	<li>Verifica que la respuesta se corresponde con el certificado</li>
	 *  <li>Verifica que el emisor del certificado de firma de la respuesta esté
	 *  	en la cadena de confianza del certificado[aunque se mantiene el log
	 *  	que avisa de este caso sólo se tiene en cuenta la comprobación anterior
	 *  	para obtener el resultado del método]</li>
	 * </ul>
	 * 
	 * @param certificate Certificado
	 * @return Cierto si esta respuesta coincide con el certificado
	 */
	public boolean match (ValidateCertificate certificate) {
		logger.debug("[CertificateOCSPResponse.match]::Entrada");
		
		//-- Obtener el certID de la respuesta
		CertificateID certIdResp = getCertificateID ();
		
		//-- Obtener el certID del certificado
		CertificateID certIdCert;
		try {
			certIdCert = new CertificateID(new JcaDigestCalculatorProviderBuilder().
					setProvider(CRYPTOGRAPHIC_PROVIDER_NAME).build().get(CertificateID.HASH_SHA1), 
					certificate.getIssuerCertificate().toX509CertificateHolder(), certificate.getSerialNumberBigInteger());
		} catch (Exception e) {
			logger.info("[CertificateOCSPResponse.match]::No se puede obtener el certId del certificado", e);
			return false;
		} 

		//-- Comparar
		if (certIdResp.equals(certIdCert)) {
			ValidateCertificate[] chainCertificate = certificate.getCertificationChain();
			for (int i = 0; i < chainCertificate.length; i++) {
				if (this.signatureCertificate.getIssuerKeyIdentifier() != null) {
					if (chainCertificate[i].getSubjectKeyIdentifier().equals(
							this.signatureCertificate.getIssuerKeyIdentifier())) {
						return true;
					}					
				} else {
					if (chainCertificate[i].getSubjectDN().equals(
							this.signatureCertificate.getIssuerDN())) {
						return true;
					}
				}
			}
			logger.warn("[CertificateOCSPResponse.match]::La respuesta OCSP se corresponde con el certificado pero el emisor del certificado " +
					"de firma de la respuesta no se encuentra en la cadena de confianza del certificado");
			//-- Hacemos una validación menos estricta permitiendo que respuestas firmadas con certificados fuera de la cadena de certificación   
			//return false;
			return true;
		} else {
			logger.debug("[CertificateOCSPResponse.match]::La respuesta OCSP no se corresponde con el certificado");
			return false;
		}
	}
	
	/**
	 * Comprueba si este objeto es una respuesta para el certificado pasado como
	 * parámetro. Para ello verifica que la respuesta se corresponde con el certificado.
	 * 
	 * @param certificate Certificado
	 * @return Cierto si esta respuesta coincide con el certificado
	 */
	public boolean match (Certificate certificate) {
		logger.debug("[CertificateOCSPResponse.match]::Entrada");
		
		//-- Obtener el certID de la respuesta
		CertificateID certIdResp = getCertificateID ();
		
		//-- Obtener el certID del certificado
		CertificateID certIdCert;
		try {
			certIdCert = OCSPClient.getCertificateID(certificate);
		} catch (Exception e) {
			logger.info("[CertificateOCSPResponse.match]::No se puede obtener el certId del certificado", e);
			return false;
		} 

		//-- Comparar
		if (certIdResp.equals(certIdCert)) {
			logger.debug("[CertificateOCSPResponse.match]::La respuesta OCSP se corresponde con el certificado");
			return true;
		} else {
			logger.debug("[CertificateOCSPResponse.match]::La respuesta OCSP no se corresponde con el certificado");
			return false;
		}
	}
	
	/**
	 * Obtiene el certificateID de la respuesta. Este objeto identifica 
	 * al certificado sobre el que se ha verificado el certificado y 
	 * contiene su Issuer and Serial Number
	 * 
	 * @return CertificateID de la respuesta
	 */
	public CertificateID getCertificateID () {
		return singleResponse.getCertID();
	}
	
	/**
	 * Obtiene la fecha a partir de la cual es válida la respuesta.
	 * 
	 * @return Fecha de inicio del periodo de validez de la respuesta
	 */
	public Date getValidityPeriodBeginning () {
		return singleResponse.getThisUpdate();
	}
	
	/**
	 * Obtiene la fecha a partir de la cual ya no es válida la respuesta.
	 * 
	 * @return Fecha de fin del periodo de validez de la respuesta
	 */
	public Date getValidityPeriodEnd () {
		return singleResponse.getNextUpdate();
	}
	
	/**
	 * Devuelve el estado de la respuesta, que será alguna de las siguientes constantes de
	 * la clase {@link es.accv.arangi.base.util.validation.ValidationResult ValidationResult}:<br>
	 * 
	 * <ul>
	 * 	<li><i>RESULT_VALID</i>: El certificado es válido.</li>
	 * 	<li><i>RESULT_CERTIFICATE_REVOKED</i>: El certificado está revocado</li>
	 * 	<li><i>RESULT_CERTIFICATE_UNKNOWN</i>: El certificado es desconocido para el OCSP</li>
	 * </ul>
	 * 
	 * @return Estado de la respuesta
	 */
	public int getStatus () {
		logger.debug("[CertificateOCSPResponse.getStatus]::Entrada");
		
     	if(singleResponse.getCertStatus()== null){
     		//-- Certificate valid
     		return ValidationResult.RESULT_VALID;
     		
     	}else{
     		//-- Certificate is not valid
     		if(singleResponse.getCertStatus() instanceof RevokedStatus) {
     			//-- Certificate revoked
     			return ValidationResult.RESULT_CERTIFICATE_REVOKED;
     		} 
     		//-- Certificate unknown
     		return ValidationResult.RESULT_CERTIFICATE_UNKNOWN;
     		
     	}			
	}
	
	/**
	 * Obtiene la fecha de revocación. Si se llama a este método y el estado de 
	 * la respuesta no es revocado, se devolverá un valor nulo.
	 * 
	 * @return Fecha de revocación o nulo si la respuesta no es de revocación
	 */
	public Date getRevocationTime () {

		if(!(singleResponse.getCertStatus() instanceof RevokedStatus)) {
 			return null;
 		}
 		
 		RevokedStatus revokedStatus = (RevokedStatus) singleResponse.getCertStatus();
 		
 		logger.debug("[CertificateOCSPResponse.getRevocationTime]::revocationtime=" + revokedStatus.getRevocationTime());
 		
 		return revokedStatus.getRevocationTime();
	}
	
	/**
	 * Obtiene la razón de revocación. Si se llama a este método y el estado de 
	 * la respuesta no es revocado, se devolverá un valor -1. La razón de 
	 * revocación es un campo opcional, por lo que aunque el estado sea
	 * revocado este método puede devolver un valor -1.
	 * 
	 * @return Razón de revocación o -1 si no existe razón de revocación o la 
	 * 	respuesta no es de revocación
	 */
	public int getRevocationReason () {

		if(!(singleResponse.getCertStatus() instanceof RevokedStatus)) {
 			return -1;
 		}
 		
 		RevokedStatus revokedStatus = (RevokedStatus) singleResponse.getCertStatus();
 		if (!revokedStatus.hasRevocationReason()) {
 			return -1;
 		}
 		return revokedStatus.getRevocationReason();
	}

}
