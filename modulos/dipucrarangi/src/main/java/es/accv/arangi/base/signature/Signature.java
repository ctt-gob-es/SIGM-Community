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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureClassNotFoundException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.util.validation.ValidationResult;


/**
 * Clase base para todas las implementaciones de {@link ISignature ISignature}
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public abstract class Signature extends ArangiObject implements ISignature{

	/*
	 * Nombre de método que se buscará en los reconocedores
	 */
	private static final String RECOGNIZER_METHOD_NAME	= "getSignatureInstance";
	
	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(Signature.class);
	
	/*
	 * Lista de clases que pueden reconocer una firma
	 */
	private static List<Class> recognizersList;
	
	/**
	 * Valida una firma attached mediante servicios de validación.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @param validationServices Servicios de validación
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public static ValidationResult[] validateSignature (byte[] bSignature, List<CertificateValidationService> validationServices) throws SignatureClassNotFoundException, HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException {
		logger.debug("[Signature.validateSignature]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		return signature.isValid(validationServices);
	}
	
	/**
	 * Valida una firma attached mediante una lista de certificados de Autoridades de
	 * Certificación.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @param caList Lista de certificados de CA
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public static ValidationResult[] validateSignature (byte[] bSignature, CAList caList) throws SignatureClassNotFoundException, HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException {
		logger.debug("[Signature.validateSignature]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		return signature.isValid(caList);
	}
	
	/**
	 * Valida una firma attached mediante servicios de validación y, si no puede
	 * conseguirlo prueba con una lista de certificados de Autoridades de
	 * Certificación.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @param validationServices Servicios de validación
	 * @param caList Lista de certificados de CA
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public static ValidationResult[] validateSignature (byte[] bSignature, List<CertificateValidationService> validationServices, CAList caList) throws SignatureClassNotFoundException, HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException {
		logger.debug("[Signature.validateSignature]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		ValidationResult[] result = null;
		try {
			result = signature.isValid(caList);
		} catch (Exception e) {
			logger.debug("[Signature.validateSignature]::No se ha podido validar CAList, probamos con servicios de validación");
		}
		
		boolean validarConServicios = false;
		if (result == null) {
			validarConServicios = true;
		} else {
			for (int i = 0; i < result.length; i++) {
				if (result[i].getResult() == ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS) {
					validarConServicios = true;
				}
			}
		}
		
		if (!validarConServicios) {
			return result;
		} else {
			return signature.isValid(validationServices);
		}
	}
	
	/**
	 * Valida una firma detached mediante servicios de validación.
	 * 
	 * @param document Archivo que generó la firma
	 * @param bSignature Firma como array de bytes
	 * @param validationServices Servicios de validación
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public static ValidationResult[] validateSignature (IDocument document, byte[] bSignature, List<CertificateValidationService> validationServices) throws SignatureClassNotFoundException, HashingException, SignatureException, NormalizeCertificateException {
		logger.debug("[Signature.validateSignature]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		return signature.isValid(document, validationServices);
	}
	
	/**
	 * Valida una firma detached mediante una lista de certificados de Autoridades de
	 * Certificación.
	 * 
	 * @param document Archivo que generó la firma
	 * @param bSignature Firma como array de bytes
	 * @param caList Lista de certificados de CA
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public static ValidationResult[] validateSignature (IDocument document, byte[] bSignature, CAList caList) throws SignatureClassNotFoundException, HashingException, SignatureException, NormalizeCertificateException {
		logger.debug("[Signature.validateSignature]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		return signature.isValid(document, caList);
	}
	
	/**
	 * Valida una firma detached mediante servicios de validación y, si no puede
	 * conseguirlo prueba con una lista de certificados de Autoridades de
	 * Certificación.
	 * 
	 * @param document Archivo que generó la firma
	 * @param bSignature Firma como array de bytes
	 * @param validationServices Servicios de validación
	 * @param caList Lista de certificados de CA
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 */
	public static ValidationResult[] validateSignature (IDocument document, byte[] bSignature, List<CertificateValidationService> validationServices, CAList caList) throws SignatureClassNotFoundException, HashingException, SignatureException, NormalizeCertificateException {
		logger.debug("[Signature.validateSignature]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		try {
			ValidationResult[] result = signature.isValid(document, caList);
			boolean unknownCertificates = false;
			for (int i = 0; i < result.length; i++) {
				if (result[i].getResult() == ValidationResult.RESULT_CERTIFICATE_UNKNOWN ||
						result[i].getResult() == ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED) {
					unknownCertificates = true;
					break;
				}
			}
			if (!unknownCertificates) {
				return result;
			}
		} catch (Exception e) {
			logger.debug("[Signature.validateSignature]::No se ha podido validar con CAList, probamos con servicios de validación");
		}
		
		//-- Si estamos aquí es porque no se ha podido validar con CAList, probamos con servicios de validación
		return signature.isValid(document, validationServices);
	}
	
	/**
	 * Valida una firma attached sin validar los certificados.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public static ValidationResult[] validateSignatureOnly (byte[] bSignature) throws SignatureClassNotFoundException, HashingException, SignatureException, NoDocumentToSignException {
		logger.debug("[Signature.validateSignatureOnly]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		return signature.isValidSignatureOnly();
	}
	
	/**
	 * Valida una firma detached sin validar los certificados.
	 * 
	 * @param document Archivo que generó la firma
	 * @param bSignature Firma como array de bytes
	 * @return Resultado de la validación
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. Utilizar este mismo método pero pasándole el documento que originó la
	 * 	firma
	 */
	public static ValidationResult[] validateSignatureOnly (IDocument document, byte[] bSignature) throws SignatureClassNotFoundException, HashingException, SignatureException, NoDocumentToSignException {
		logger.debug("[Signature.validateSignatureOnly]::Entrada");
		
		ISignature signature = getSignatureObject (bSignature);
		return signature.isValidSignatureOnly(document);
	}
	
	/**
	 * Añade una nueva clase que permite reconocer tipos de firma. La nueva clase
	 * deberá contener un método estático 'getSignatureInstance'.
	 * 
	 * @param recognizerClass Clase que reconoce un tipo de firma
	 */
	public static void addRecognizerClass (Class recognizerClass) {
		List<Class> recognizerClasses = getRecognizersList();
		if (!recognizerClasses.contains(recognizerClass)) {
			recognizerClasses.add(recognizerClass);
		}
	}

	/**
	 * Obtiene el tipo de la firma que se pasa como parámetro.
	 * 
	 * @param bSignature Contenido de la firma como array de bytes
	 * @return tipo de la firma
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 */
	public static String getType (byte[] bSignature) throws SignatureClassNotFoundException {
		logger.debug("[Signature.getSignatureType]::Entrada");
		
		ISignature signature = getSignatureObject(bSignature);
		return signature.getSignatureType();
	}
	
	/**
	 * Obtiene un objeto de tipo ISignature para tratar la firma pasada
	 * como parámetro.
	 * 
	 * @param bSignature Contenido de la firma como array de bytes
	 * @return Objeto firma
	 * @throws SignatureClassNotFoundException Ninguna de las clases de firma de Arangí
	 *  es capaz de cargar la firma pasada como parámetro 
	 */
	public static ISignature getSignatureObject (byte[] bSignature) throws SignatureClassNotFoundException {
		logger.debug("[Signature.getSignatureObject]::Entrada");
		
		//-- Recorrer la lista de reconocedores
		for(Class recognizerClass : getRecognizersList()) {
			logger.debug("[Signature.getSignatureObject]::Probar con la clase: " + recognizerClass);
			Method method;
			try {
				method = recognizerClass.getMethod(RECOGNIZER_METHOD_NAME,new Class[] { byte[].class });
			} catch (SecurityException e1) {
				logger.info("[Signature.getSignatureObject]::Excepción de seguridad al llamar al método getSignatureInstance de la clase: " + recognizerClass);
				continue;
			} catch (NoSuchMethodException e1) {
				logger.info("[Signature.getSignatureObject]::El método getSignatureInstance no existe en la clase: " + recognizerClass);
				continue;
			}
			try {
				return (ISignature) method.invoke(null, new Object[] { bSignature });
			} catch (IllegalArgumentException e) {
				logger.info("[Signature.getSignatureObject]::El método getSignatureInstance existe pero con parámetros equivocados en la clase: " + recognizerClass);
				continue;
			} catch (IllegalAccessException e) {
				logger.info("[Signature.getSignatureObject]::Acceso ilegal al llamar al método getSignatureInstance de la clase: " + recognizerClass);
				continue;
			} catch (InvocationTargetException e) {
				logger.debug("[Signature.getSignatureObject]::La firma no es del tipo que trata: " + recognizerClass);
				continue;
			}
		}
		
		throw new SignatureClassNotFoundException("No se ha encontrado ninguna clase para tratar la firma");
	}
	
	//-- Métodos privados
	
	/*
	 * Obtiene la lista de clases reconocedoras de firma
	 */
	private static List<Class> getRecognizersList () {
		if (recognizersList == null) {
			//-- Añadir los tipos tratados por Arangí
			recognizersList = new ArrayList<Class>();
			recognizersList.add(XAdESSignature.class);
			recognizersList.add(BasePDFSignature.class);
			recognizersList.add(CMSPKCS7Signature.class);
		}
		
		return recognizersList;
	}
	
}