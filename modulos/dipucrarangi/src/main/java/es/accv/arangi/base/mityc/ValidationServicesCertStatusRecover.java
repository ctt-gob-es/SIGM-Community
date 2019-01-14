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
package es.accv.arangi.base.mityc;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationServiceResult;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.mityc.javasign.certificate.CertStatusException;
import es.mityc.javasign.certificate.ICertStatus;
import es.mityc.javasign.certificate.ICertStatusRecoverer;

/**
 * Clase que implementa el validador de certificados de las clases del MITyC.
 * Valida los certificados en base a servicios de validación.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class ValidationServicesCertStatusRecover implements ICertStatusRecoverer {

	/**
	 * Logger de la clase
	 */
	private Logger logger = Logger.getLogger(ValidationServicesCertStatusRecover.class);
	
	/**
	 * Servicios de validación
	 */
	private List<CertificateValidationService> validationServices; 
	
	//-- Constructores
	
	/**
	 * Construye un validador en base a servicios de validación
	 */
	public ValidationServicesCertStatusRecover (List<CertificateValidationService> validationServices) {
		this.validationServices = validationServices;
	}
	
	//-- Implementación de ICertStatusRecover
	
	/**
	 * Recupera el estado de la cadena de certificación del certificado indicado.
	 * 
	 * @param x509Certificate Certificado X509
	 * @return Estados de cada uno de los certificados de la cadena de confianza
	 */
	public List<ICertStatus> getCertChainStatus(X509Certificate x509Certificate) throws CertStatusException {
		logger.debug("[ArangiCertStatusRecoverer.getCertChainStatus]::Entrada::" + x509Certificate.getSubjectDN());
		
		List<ICertStatus> lStatus = new ArrayList<ICertStatus>();
		lStatus.add (getCertStatus (x509Certificate));
		
		return lStatus;
	}

	/**
	 * Recupera el estado de la cadena de certificación del conjunto de certificados indicados.
	 * 
	 * @param certificates Certificados
	 * @return Estados de cada uno de los certificados de la cadena de confianza de cada uno
	 * 	de los certificados
	 */
	public List<List<ICertStatus>> getCertChainStatus(List<X509Certificate> certificates) throws CertStatusException {
		//-- Iterar sobre la lista y obtener los resultados
		List<List<ICertStatus>> lStatus = new ArrayList<List<ICertStatus>>();
		for (Iterator<X509Certificate> iterator = certificates.iterator(); iterator.hasNext();) {
			X509Certificate certificate = iterator.next();
			lStatus.add (getCertChainStatus(certificate));
		}
		
		return lStatus;
	}

	/**
	 * Recupera el estado del certificado indicado.
	 * 
	 * @param x509Certificate Certificado
	 * @return Estado del certificado
	 */
	public ICertStatus getCertStatus(X509Certificate x509Certificate) throws CertStatusException {
		
		logger.debug("[ValidationServicesCertStatusRecover.getCertStatus]::Entrada::" + x509Certificate.getSubjectDN());
		
		//-- Obtener el certificado de Arangí
		Certificate certificate;
		try {
			certificate = new Certificate(x509Certificate);
		} catch (NormalizeCertificateException e) {
			logger.info("[ArangiCertStatusRecoverer.getCertChainStatus]::El certificado no se puede normalizar", e);
			throw new CertStatusException("El certificado no se puede normalizar", e);
		}
		
		//-- Si el certificado es autofirmado es válido
		try {
			if (certificate.isSelfSigned()) {
				logger.debug("[ArangiCertStatusRecoverer.getCertStatus]::El certificado es autofirmado");
				return new ValidStatus (x509Certificate);
			}
		} catch (NormalizeCertificateException e) {
			logger.info("[ArangiCertStatusRecoverer.getCertStatus]::No se puede comprobar si el certificado es autofirmado", e);
			throw new CertStatusException("No se puede comprobar si el certificado es autofirmado", e);
		}
		
		//-- Si el certificado ha caducado se da por válido (es lo que diría cualquier OCSP)
		if (certificate.isExpired()) {
			logger.debug("[ArangiCertStatusRecoverer.getCertStatus]::El certificado ha caducado");
			return new ValidStatus (x509Certificate);
		}
		
		//-- Llamar a los servicios de validación para obtener la respuesta OCSP.
		CertificateValidationServiceResult validationResult = certificate.validate(validationServices);
		OCSPResponse ocspResponse = validationResult.getOcspResponse();
		if (ocspResponse != null) {
			logger.debug ("[ValidationServicesCertStatusRecover.getCertStatus]::Respuesta OCSP del servicio de validación: " + ocspResponse);
			return new OCSPStatus(ocspResponse, certificate.toX509Certificate());
		}
		
		//-- No se ha encontrado ninguna respuesta
		logger.debug ("[ValidationServicesCertStatusRecover.getCertStatus]::Certificado desconocido porque no tiene definidos " +
				"OCSPs en lo que validarse");
		return new UnknownStatus(certificate.toX509Certificate());
	}

	/**
	 * Recupera el estado del certificado indicado.
	 * 
	 * @param certificates Certificados
	 * @return Estados de los certificados
	 */
	public List<ICertStatus> getCertStatus(List<X509Certificate> certificates) throws CertStatusException {
		
		//-- Iterar sobre la lista y obtener los resultados
		List<ICertStatus> lStatus = new ArrayList<ICertStatus>();
		for (Iterator<X509Certificate> iterator = certificates.iterator(); iterator.hasNext();) {
			X509Certificate certificate = iterator.next();
			lStatus.add (getCertStatus(certificate));
		}
		
		return lStatus;
	}

}
