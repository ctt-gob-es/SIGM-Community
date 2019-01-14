/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.exception;

/**
 * Clase que recoge las constantes de error, que se mostrarán en las excepciones
 * y en el log.
 * 
 * @author jortizs
 */
public class ErrorConstants {
    public static final String ENCODING_NOT_DETECTED_ERROR = "No se ha detectado el encoding. ";
	public static final String ERROR_SIGM = "Error respuesta SIGM. ";
	public static final String ERROR_SIGM_PARSING_XML = "Error SIGM. Parseando XML. ";
	public static final String ERROR_REMOTE_EXCEPTION = "Error parseando XML. RemoteException. ";
	
	public static final String ERROR_XML_PETICION_PARSING_XML = "Error XML petición. Parseando XML. ";
	
	public static final String ERROR_SERVCERT_DNIREPRESENTANTE_COINCIDENCIA = "Error. No coincide la firma con el DNI del representante. ";
	public static final String ERROR_SERVCERT_DNIINTERESADO_COINCIDENCIA = "Error. No coincide la firma con el DNI del interesado. ";
	public static final String ERROR_SERVCERT_CERTIFICADO_NO_VALIDO = "Error. Certificado no valido. ";
	public static final String ERROR_SIGN_NO_COINCIDE = "Error. Firmas no coinciden. ";
	public static final String ERROR_SIGN_EXTRACT_CERTIFICATE = "Error al extraer los certificados. ";
	public static final String ERROR_FILE_SIGN_VERIFY = "Error al verificar las firmas de los ficheros. ";
	public static final String ERROR_CERTIFICATE_EXTRACT = "Error al extraer el certificado. ";
	public static final String ERROR_CALIDAD_SUTITUIR_VALORES = "Error al sustituir valores en la plantilla de calidad. ";
	public static final String ERROR_CALIDAD_CAMPOS_FECHA = "Error al parsear la fecha de registro para calidad. ";
	public static final String ERROR_CALIDAD_ENVIO_XML = "Error al enviar el xml a calidad. ";
	public static final String ERROR_ANTIVIRUS = "Error al lanzar el antivirus. ";
	public static final String ERROR_SERVCERT_TASK = "Error en la tarea servcert. ";
	public static final String ANTIVIRUS_FICHERO_NO_AUTORIZADO = "Contenido verificado y no autorizado. ";
	public static final String ERROR_RESPONSE_MESSAGE = "Error durante la generación del acuse de recibo. ";
	public static final String ERROR_UPDATE_BBDD = "Error al actualizar la bbdd. ";
	public static final String SIGN_NO_VALID = "Firma no válida. ";
	public static final String RESPONSE_ERROR_PROCESS = "Error procesando la petición. ";

	public static final String ERR_EXIT_MSG = "err.exit.msg";
//	public static final String ERR_EXIT_CODE = "err.exit.code";

}