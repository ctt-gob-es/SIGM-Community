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
package es.accv.arangi.base.signature;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureException;

/**
 * Interfaz que deben cumplir todos las clases que representan firmas digitales.<br><br>
 * 
 * En la medida de lo posible se ha intentado distinguir entre los bytes de la
 * firma, resultantes de los procesos de hashing más encriptado, de las firmas
 * digitales donde se guardan, además de estos bytes de firma, los certificados, 
 * sellos de tiempo y cualquier otra información que sirva a su validación. Las 
 * clases que implementan esta interfaz representan a los segundos. Los métodos 
 * de firma de las clases que extienden {@link es.accv.arangi.base.device.DeviceManager DeviceManager} 
 * devuelven sólo los bytes de la firma.  
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public interface ISignature {

	/**
	 * Devuelve los certificados con los que se ha realizado la firma
	 * 
	 * @return Certificados con los que se ha realizado la firma
	 */
	public Certificate[] getCertificates ();
	
	/**
	 * Comprueba que las firmas son correctas en firmas attached, sin validar los certificados 
	 * de las mismas.<br><br>
	 * 
	 * IMPORTANTE: este método sólo puede ser utilizado si la firma es attached (el documento
	 * que originó la firma se incluye en ésta). Si no es así utilizar este mismo método pero 
	 * pasándole el documento que originó la firma.
	 * 
	 * @return Para cada certificado resultado de validar la firma
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public ValidationResult[] isValidSignatureOnly () throws HashingException, SignatureException, NoDocumentToSignException;
	
	/**
	 * Comprueba que las firmas son correctas en firmas attached y sus certificados son válidos. <br><br>
	 * 
	 * Sobre la validación de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, éste sólo será valido mientras el certificado no
	 * 	haya caducado. Después, es imposible obtener la información de revocación por lo
	 * 	que este método devolverá un resultado falso</li>
	 * 	<li>Si la firma incluye información de revocación (CRLs o respuestas OCSP) ésta 
	 * 	si que se tendrá en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * </ul><br><br>
	 * 
	 * IMPORTANTE: este método sólo puede ser utilizado si la firma es attached (el documento
	 * que originó la firma se incluye en ésta). Si no es así utilizar este mismo método pero 
	 * pasándole el documento que originó la firma.
	 * 
	 * @param caList Lista de certificados de CA admitidos por la aplicación que usa
	 * 	Arangi
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	válido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public ValidationResult[] isValid (CAList caList) throws HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException;
	
	/**
	 * Comprueba que las firmas son correctas en firmas attached y sus certificados son válidos. <br><br>
	 * 
	 * Sobre la validación de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, éste sólo será valido mientras el certificado no
	 * 	haya caducado. Después, es imposible obtener la información de revocación por lo
	 * 	que este método devolverá un resultado falso</li>
	 * 	<li>Si la firma incluye información de revocación (CRLs o respuestas OCSP) ésta 
	 * 	si que se tendrá en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * <li>La validación se llevará a cabo a través de los servicios de validación
	 * 	pasados como parámetro.</li>
	 * </ul><br><br>
	 * 
	 * IMPORTANTE: este método sólo puede ser utilizado si la firma es attached (el documento
	 * que originó la firma se incluye en ésta). Si no es así utilizar este mismo método pero 
	 * pasándole el documento que originó la firma.
	 * 
	 * @param validationServices Lista de servicios de validación
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	válido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public ValidationResult[] isValid (List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException;
	
	/**
	 * Comprueba que las firmas son correctas, sin validar los certificados de las mismas.
	 * 
	 * @param document Documento que originó la firma
	 * @return Para cada certificado resultado de validar la firma
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 */
	public ValidationResult[] isValidSignatureOnly (IDocument document) throws HashingException, SignatureException;
	
	/**
	 * Comprueba que las firmas son correctas y sus certificados son válidos.<br><br> 
	 * 
	 * Sobre la validación de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, éste sólo será valido mientras el certificado no
	 * 	haya caducado. Después, es imposible obtener la información de revocación por lo
	 * 	que este método devolverá un resultado falso</li>
	 * 	<li>Si la firma incluye información de revocación (CRLs o respuestas OCSP) ésta 
	 * 	si que se tendrá en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * </ul>
	 * 
	 * @param document Documento que originó la firma
	 * @param caList Lista de certificados de CA admitidos por la aplicación que usa
	 * 	Arangi
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	válido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangi o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public ValidationResult[] isValid (IDocument document, CAList caList) throws HashingException, SignatureException, NormalizeCertificateException;
	
	/**
	 * Comprueba que las firmas son correctas y sus certificados son válidos.<br><br> 
	 * 
	 * Sobre la validación de certificados hay que tener en cuenta:<br>
	 * <ul>
	 * 	<li>Si existe sello de tiempos, éste sólo será valido mientras el certificado no
	 * 	haya caducado. Después, es imposible obtener la información de revocación por lo
	 * 	que este método devolverá un resultado falso</li>
	 * 	<li>Si la firma incluye información de revocación (CRLs o respuestas OCSP) ésta 
	 * 	si que se tendrá en cuenta aunque el certificado haya caducado: concepto de
	 * 	firma longeva</li>
	 * <li>La validación se llevará a cabo a través de los servicios de validación
	 * 	pasados como parámetro.</li>
	 * </ul>
	 * 
	 * @param document Documento que originó la firma
	 * @param validationServices Lista de servicios de validación
	 * @return Para cada certificado resultado de comprobar si la firma es correcta y el certificado es
	 * 	válido
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangi o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public ValidationResult[] isValid (IDocument document, List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException;
	
	/**
	 * Guarda la firma en disco
	 * 
	 * @param file Fichero donde se guardará la firma
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (File file) throws IOException;
	
	/**
	 * Guarda la firma en un stream de escritura.
	 * 
	 * @param out Stream de escritura
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (OutputStream out) throws IOException;
	
	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType ();
}
