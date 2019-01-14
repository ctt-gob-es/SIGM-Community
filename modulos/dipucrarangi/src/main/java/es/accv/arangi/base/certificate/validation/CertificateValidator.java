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

import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import es.accv.arangi.base.util.validation.ValidationResult;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;


/**
 * Clase que ofrece métodos para validar certificados. Por ejemplo:<br><br>
 * 
 * <code>
 * CAList caList = new CAList (new File ("c:/certificates/caCertificates"));<br>
 * CertificateValidator validator = new CertificateValidator (caList); // configure validator<br>
 * X509Certificate certificate = Util.getCertificate(new File ("c:/certificates/myCertificate.cer"));<br>
 * ValidatingTrace trace = new ValidatingTrace ();<br>
 * int result = validator.validate (certificate, trace);<br>
 * System.out.println ("Result: " + validator.getString (result));<br>
 * System.out.println ("Trace: " + trace.toString());<br>
 * </code><br><br>
 * 
 * Para validar un certificado se pueden utilizar los métodos de {@link ValidateCertificate ValidateCertificate}.
 * Esta clase es útil cuando hay que realizar múltiples validaciones, ya que se
 * inicializa una vez y luego sólo hay que ir pasándole los certificados a validar.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class CertificateValidator {

	/*
	 * Class logger
	 */
	private Logger logger = Logger.getLogger(CertificateValidator.class);
	
	/*
	 * Lista de certificados de CA
	 */
	CAList caList;

	/**
	 * Constructor. Inicializa el validador con la lista de CAs de confianza.
	 * 
	 * @param caList Lista de CAs de confianza.
	 */
	public CertificateValidator (CAList caList) {
		this.caList = caList;
	}
	
	/**
	 * Método que valida un certificado X.509.
	 * 
	 * @param certificate Certificado a validar
	 * @return Estado del certificado
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptográfico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public int validate (X509Certificate certificate) throws NormalizeCertificateException{
		return validate (certificate, new ValidatingTrace());
	}
	
	/**
	 * Método que valida un certificado X.509 para una fecha determinada.
	 * 
	 * @param certificate Certificado a validar
	 * @param validationDate Fecha de validación
	 * @return Estado del certificado
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptográfico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public int validate (X509Certificate certificate, Date validationDate) throws NormalizeCertificateException{
		return validate (certificate, validationDate, new ValidatingTrace());
	}
	
	/**
	 * Método que valida un certificado X.509. El objeto traza va recogiendo los pasos
	 * que se van dando hasta llegar a realizar la validación.
	 * 
	 * @param certificate Certificado a validar
	 * @param trace Traza para obtener cómo se ha conseguido la validación
	 * @return Estado del certificado
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptográfico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public int validate (X509Certificate certificate, ValidatingTrace trace) throws NormalizeCertificateException {
		return validate (certificate, new Date(), trace);
	}
		
	/**
	 * Método que valida un certificado X.509 para una fecha determinada. El objeto traza 
	 * va recogiendo los pasos que se van dando hasta llegar a realizar la validación. <br><br>
	 * 
	 * Nota: la mayoría de Autoridades de Certificación eliminan la información de revocación
	 * de los certificados caducados de sus CRLs. En estos casos la validación histórica no
	 * es posible y Arangi devolverá que el certificado era válido, ya que tanto la CRL como
	 * el OCSP devuelven este resultado. La única forma de realizar validación histórica sería 
	 * dentro de un proceso de firma longeva.
	 * 
	 * @param certificate Certificado a validar
	 * @param validationDate Fecha de validación
	 * @param trace Traza para obtener cómo se ha conseguido la validación
	 * @return Estado del certificado
	 * @throws NormalizeCertificateException El certificado no puede ser normalizado al formato
	 * 	reconocido por el proveedor criptográfico de Arangi o su firma no es correcta o no
	 * 	puede ser analizada
	 */
	public int validate (X509Certificate certificate, Date validationDate, ValidatingTrace trace) throws NormalizeCertificateException{
		
		logger.debug ("[CertificateValidator.validate] :: " + Arrays.asList(new Object [] { certificate, validationDate, trace }));
		
		//-- Init a ValidateCertificate
		ValidateCertificate validateCertificate;
		try {
			validateCertificate = new ValidateCertificate (certificate, caList);
		} catch (CertificateCANotFoundException e) {
			logger.debug ("[CertificateValidator.validate] :: Certificate with issuerDN=" + certificate.getIssuerDN().toString() + 
				" not belong to the list of trusted CAs.");
			return ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS;
		} 
		
		//-- Validate the certificate
		return validateCertificate.validate(validationDate, trace);
	}

	/**
	 * Devuelve un texto (en inglés) para el resultado de la validación pasado
	 * como parámetro.
	 * 
	 * @param result Resultado de la validación
	 * @return Texto con una descripción del resultado.
	 */
	public static String getString(int result) {
		
		switch (result) {
			case ValidationResult.RESULT_VALID:
				return "valid certificate";
			case ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS:
				return "certificate not belongs to trusted CAs";
			case ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE:
				return "certificate has expired or is not active";
			case ValidationResult.RESULT_CERTIFICATE_REVOKED:
				return "revoked certificate";
			case ValidationResult.RESULT_CERTIFICATE_UNKNOWN:
				return "unknown certificate";
			case ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED:
				return "it is impossible to validate certificate";
			case ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID:
				return "certificate's chain validation is not valid";
			case ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA:
				return "signature does not match the document";
			case ValidationResult.RESULT_REFERENCE_DIGEST_NOT_MATCH_DATA:
				return "some reference in XMLDSig does not match data that references";
			case ValidationResult.RESULT_INVALID:
				return "is not valid";
			case ValidationResult.RESULT_INVALID_TIMESTAMP:
				return "timestamp is not valid";
			case ValidationResult.RESULT_INVALID_VALIDITY_ITEM:
				return "OCSP response or CRL is not valid";
			case ValidationResult.RESULT_TIMESTAMP_AFTER_VALIDITY_ITEM:
				return "OCSP response or CRL are not valid in timestamp date";
		}
		
		return null;
	}
	
}
