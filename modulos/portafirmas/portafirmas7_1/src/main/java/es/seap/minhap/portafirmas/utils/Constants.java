/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de AndalucÃ­a

 Derechos de explotaciÃ³n propiedad de la Junta de AndalucÃ­a.

 Ã‰ste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los tÃ©rminos de la Licencia EUPL European Public License publicada
 por el organismo IDABC de la ComisiÃ³n Europea, en su versiÃ³n 1.0. o posteriores.

 Ã‰ste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÃ�A, incluso sin las presuntas garantÃ­as implÃ­citas de USABILIDAD o ADECUACIÃ“N A PROPÃ“SITO
 CONCRETO. Para mas informaciÃ³n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por algÃºn motivo no le es posible visualizarla, puede
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reÃ§u une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.utils;

/**
 * Guarda constantes para usar en toda la aplicaci&oacute;n
 * @author daniel.palacios
 *
 */
public class Constants {

	public static final String ACCION_DESBLOQUEAR_REGISTRO_FIRMA = "DESBLOQUEAR_FIRMA";
	public static final String PARAMETRO_ID_REQUEST_ACCION_DESBLOQUEAR_REGISTRO_FIRMA = "request.id";
	
	// Variables de sistema
	public static final String SGTIC_CONFIGPATH = "sgtic.configpath";
	public static final String CLAVE_CONFIGPATH = "clave.config.path";	
	public static final String SYSTEM_TRUSTSTORE_FILE = "javax.net.ssl.trustStore";
	public static final String SYSTEM_TRUSTSTORE_PASSWORD = "javax.net.ssl.trustStorePassword";
	public static final String SYSTEM_TRUSTSTORE_TYPE = "javax.net.ssl.trustStoreType";
	
	public static final String FIRE_TRUSTSTORE_FILE = "javax.net.ssl.trustStore";
	public static final String FIRE_KEYSTORE_FILE = "javax.net.ssl.keyStore";

	public static final String FS = System.getProperty("file.separator");
	public static final String PATH_DOC = System.getProperty(SGTIC_CONFIGPATH) + FS + "documentos" + FS;
	public static final String PATH_TEMP = System.getProperty(SGTIC_CONFIGPATH) + FS + "temp" + FS;
	public static final String PATH_CONF = System.getProperty(SGTIC_CONFIGPATH) + FS + "properties" + FS;
	public static final String PATH_CERTIFICADOS = System.getProperty(SGTIC_CONFIGPATH) + FS + "certificados" + FS;

	public static final String PATH_TEMP_NEW_ATTACHMENTS_REQUEST = "tempNuevosAnexos";
	
	public static final String APP_PFIRMA_CODE = "PFIRMA";

	public static final String EEUTIL_SEUDONIMO = "seudonimo";

	public static final String CHARSET_UTF_8 = "UTF-8";	
	public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";	
	/*
	 * XHTML COMPONENT ID'S
	 */
	/**
	 * Mantiene el valor del nombre de la pesta&ntilde;a de servidor.
	 * El valor de esta constante es {@value}.
	 */
	public static final String SERVER_TAB_ID = "server";
	/**
	 * Mantiene el valor del nombre de la pesta&ntilde;a de administraci&oacute;n.
	 * El valor de esta constante es {@value}.
	 */
	public static final String ADMIN_TAB_ID = "admin";
	/**
	 * Mantiene el valor del si para la apilcaci&oacute;n.
	 * El valor de esta constante es {@value}.
	 */
	public static final String C_YES = "S";
	/**
	 * Mantiene el valor del no para la apilcaci&oacute;n.
	 * El valor de esta constante es {@value}.
	 */
	public static final String C_NOT = "N";
	/**
	 * Mantiene el valor del nulo para la apilcación.
	 * El valor de esta constante es {@value}.
	 */
	public static final String C_NULL = "X";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String APP_PARAMETER_RESPUESTA_WS_ACTIVA = "RESPUESTA.WS.ACTIVA";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String APP_PARAMETER_RESPUESTA_WS_WSDLLOCATION = "RESPUESTA.WS.WSDLLOCATION";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String APP_PARAMETER_RESPUESTA_WS_USUARIO = "RESPUESTA.WS.USUARIO";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String APP_PARAMETER_RESPUESTA_WS_PASSWORD = "RESPUESTA.WS.PASSWORD";
	
	public static final String APP_PARAMETER_RESPUESTA_WS_NOTIFINTERMEDIOS = "RESPUESTA.WS.NOTIF.INTERMEDIOS";

	public static final String APP_PARAMETER_RESPUESTA_WS_VERSION_ANTERIOR = "RESPUESTA.WS.VERSION.ANTERIOR";

	/**
	 * n&uacute;mero de elementos por fila.
	 *  El valor de esta constante es {@value}.
	 */
	public static final int N_ROWS_PER_TABLE = 10;
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final int N_ROWS_PER_TABLE_COMMENTS = 4;

	/*
	 * C_HASH_ALG
	 */
	public static final String C_HASH_ALG_SHA1 = "SHA1";

	/*
	 * C_TYPES
	 */

	public static final String MSG_GENERIC_ERROR = 
			"Ha ocurrido un error interno en la aplicación. Si el problema persiste, contacte con el administrador.";
		public static final String TITLE_GENERIC_ERROR = "Error general";
	public static final String MSG_FORBIDDEN_ERROR = 
			"No posee los permisos necesarios para acceder a la ubicación solicitada.";
	public static final String MSG_CSV_ERROR = "No se ha encontrado documento para el CSV proporcionado.";
	public static final String MSG_BLOCK_REQUEST_ERROR = "Existe otro usuario firmando. Por favor, inténtelo más tarde.";
	public static final String TITLE_FORBIDDEN_ERROR = "Acceso restringido";
	
	// PF_ARCHIVOS
	public static final String C_TYPE_FILE_DB = "BLOB";
	public static final String C_TYPE_FILE_NAS = "NAS";
	public static final String C_TYPE_FILE_FICHERO = "FICHERO";
	public static final String C_TYPE_FILE_PATH = "FICHERO";
	public static final String C_TYPE_FILE_DOE = "DOE";
	public static final int BUFFER_SIZE = 1024;

	// PF_ETIQUETAS
	public static final String C_TYPE_TAG_USER = "USUARIO";
	public static final String C_TYPE_TAG_STATE = "ESTADO";

	// PF_LINEAS_FIRMA
	public static final String C_TYPE_SIGNLINE_SIGN = "FIRMA";
	
	public static final String C_TYPE_SIGNLINE__COSIGN = "COFIRMA";
	public static final String C_TYPE_SIGNLINE__COUNTERSIGN = "CONTRAFIRMA";
	
	/**
	 * mantiene el valor del tipo de almacenamiento de la firma para 'VISTOBUENO'
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TYPE_SIGNLINE_PASS = "VISTOBUENO";
	
	public static final String C_TYPE_SIGN_PASS = "VISTOBUENO";

	// PF_PARAMETROS
	public static final String C_TYPE_PARAMETER_APPLICATION = "APLICACION";
	public static final String C_TYPE_PARAMETER_STYLE = "ESTILO";
	public static final String C_TYPE_PARAMETER_GLOBAL = "GLOBAL";
	public static final String C_TYPE_PARAMETER_SERVER = "SERVIDOR";
	public static final String C_TYPE_PARAMETER_LOGIN = "LOGIN";
	public static final String C_TYPE_PARAMETER_SIM = "SIM";
	public static final String C_TYPE_PARAMETER_NOTIFICATION = "NOTIFICACION";
	public static final String C_TYPE_PARAMETER_JOBS = "JOBS";
	public static final String C_TYPE_PARAMETER_SM_DOCEL = "SM_DOCEL";
	public static final String C_TYPE_PARAMETER_SP_DOCEL = "SP_DOCEL";
	public static final String C_TYPE_PARAMETER_VALIDATION = "VALIDACION";
	public static final String C_TYPE_PARAMETER_AUTENTICA = "AUTENTICA";
	public static final String C_TYPE_PARAMETER_BROKER = "BROKER";
	public static final String C_TYPE_PARAMETER_DIR3 = "DIR3";
	public static final String C_TYPE_PARAMETER_FIRE = "FIRE";
	public static final String C_TYPE_PARAMETER_CUSTODIA = "CUSTODIA";
	
	public static final String C_PARAMETER_CVE_URL_VALIDATION_INTERNAL = "CVE.URL.VALIDACION.INTERNO";
	public static final String C_PARAMETER_CVE_URL_VALIDATION_EXTERNAL = "CVE.URL.VALIDACION.EXTERNO";
	public static final String C_PARAMETER_ACTIVED_HISTORIC = "HISTORICO.ACTIVADO";
	public static final String C_PARAMETER_LOGIN_DEBUG = "LOGIN.DEBUG";
	public static final String C_PARAMETER_LOGIN_LDAP = "LOGIN.LDAP";
	
	public static final String C_PARAMETER_NOTIFICATION_NOTICE_ADMIN = "NOTIFICACION.AVISAR.ADMIN";
	public static final String C_PARAMETER_NOTIFICATION_EMAIL_ADMIN = "NOTIFICACION.CORREO.ADMIN";
	
	public static final String C_PARAMETER_DOCEL_SMC_URL = "DOCEL.SMC.URL";
	public static final String C_PARAMETER_DOCEL_SMC_SECURITY_MODE = "DOCEL.SMC.SECURITY.MODE";
	public static final String C_PARAMETER_DOCEL_SMC_SECURITY_PASSWORD = "DOCEL.SMC.SECURITY.PASSWORD";
	public static final String C_PARAMETER_DOCEL_SMC_SECURITY_PASSWORD_TYPE = "DOCEL.SMC.SECURITY.PASS.TYPE";
	public static final String C_PARAMETER_DOCEL_SMC_SECURITY_CERT_ALIAS="DOCEL.SMC.SECURITY.CERT.ALIAS";
	public static final String C_PARAMETER_DOCEL_SMC_SECURITY_CERT_PWD="DOCEL.SMC.SECURITY.CERT.PWD";
	public static final String C_PARAMETER_DOCEL_SMC_SECURITY_FILENAME="DOCEL.SMC.SECURITY.FILE.NAME";
	
	public static final String C_PARAMETER_DOCEL_SPC_URL = "DOCEL.SPC.URL";
	public static final String C_PARAMETER_DOCEL_SPC_SECURITY_MODE = "DOCEL.SPC.SECURITY.MODE";
	public static final String C_PARAMETER_DOCEL_SPC_SECURITY_PASSWORD = "DOCEL.SPC.SECURITY.PASSWORD";
	public static final String C_PARAMETER_DOCEL_SPC_SECURITY_PASSWORD_TYPE = "DOCEL.SPC.SECURITY.PASS.TYPE";
	public static final String C_PARAMETER_DOCEL_SPC_SECURITY_CERT_ALIAS="DOCEL.SPC.SECURITY.CERT.ALIAS";
	public static final String C_PARAMETER_DOCEL_SPC_SECURITY_CERT_PWD="DOCEL.SPC.SECURITY.CERT.PWD";
	public static final String C_PARAMETER_DOCEL_SPC_SECURITY_FILENAME="DOCEL.SPC.SECURITY.FILE.NAME";

	public static final String C_PARAMETER_VALIDATION_AUTOMATICA = "VALIDACION.AUTOMATICA";
	public static final String C_PARAMETER_VALIDATION_PDFA = "VALIDACION.PDFA";
	public static final String C_PARAMETER_VALIDATION_TAMANIO = "VALIDACION.TAMANIO";
	
	public static final String C_PARAMETER_FIRE_ACTIVO = "FIRE.ACTIVO";

	public static final String C_PARAMETER_AUTENTICA_ACTIVO = "AUTENTICA.ACTIVO";

	public static final String C_PARAMETER_TRIFASICA_ACTIVA = "FIRMA.TRIFASICA.ACTIVA";
	public static final String C_PARAMETER_TRIFASICA_URL = "FIRMA.TRIFASICA.URL";
	public static final String SUFIJO_OPERACION_TRIFASICA = "tri";


	public static final String C_PARAMETER_USUARIO_BROKER = "BROKER.USUARIO";
	public static final String C_PARAMETER_CLAVE_BROKER = "BROKER.CLAVE";
	
	public static final String C_PARAMETER_USER_DIR3 = "DIR3.USER";
	public static final String C_PARAMETER_URL_DIR3 = "DIR3.URL";
	public static final String C_PARAMETER_PW_DIR3 = "DIR3.PW";
	public static final String C_PARAMETER_VERSION_DIR3 = "DIR3.VERSION";
	
	public static final String C_PARAMETER_URL_AUTENTICA = "AUTENTICA.ENDPOINT";
	public static final String C_PARAMETER_USER_ADMIN_LDAP_AUTENTICA = "AUTENTICA.USER.LDAP";
	
	public static final String C_PARAMETER_USUARIO_AUTENTICA = "AUTENTICA.USUARIO";
	public static final String C_PARAMETER_CLAVE_AUTENTICA = "AUTENTICA.CLAVE";
	
	/**
	 * mantiene el valor del par&aacute;metro del tema de la aplicaci&oacute;n.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PARAMETER_THEME = "ESTILO.TEMA";
	/**
	 * mantiene el valor del par&aacute;metro del n&uacute;mero de filas de la bandeja de la aplicaci&oacute;n.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PARAMETER_INBOX_N_ROWS = "ESTILO.BANDEJA.NFILAS";
	/**
	 * mantiene el valor del par&aacute;metro de lenguaje de la aplicaci&oacute;n.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PARAMETER_LANGUAGE = "ESTILO.IDIOMA";
	// TODO: mas adelante se mantendra a nivel de BBDD por aplicacion y
	// usuarios, ahora mismo se guardara a nivel de sesion
	public static final String C_PARAMETER_FONT_SIZE = "ESTILO.LETRA.TAMANYO";
	public static final String C_PARAMETER_REPORT_TYPE = "INFORME.TIPO";
	public static final String C_PARAMETER_REPORT_URL_VERIFICATION = "INFORME.URL.VERIFICACION";
	public static final String C_PARAMETER_REPORT_KEY_3DES = "INFORME.CLAVE.3DES";
	public static final String C_PARAMETER_USERTOKEN = "UsernameToken";
	public static final String C_PARAMETER_BINARY = "BinarySecurityToken";
	public static final String C_PARAMETER_STORAGE_DB = "BLOB";
	public static final String C_PARAMETER_STORAGE_INPUTCLASS = "INPUTCLASS";
	public static final String C_PARAMETER_STORAGE_OUTPUTCLASS = "OUTPUTCLASS";
	/**
	 * Mantiene el valor del par&aacute;metro de separaci&oacute;n
	 * El valor de esta constante es {@value}.
	 */
	public static final String C_PARAMETER_STORAGE_SEPARATOR = ".";
	public static final String C_PARAMETER_SIGN_MODE = "FIRMA.MODO";
	public static final String C_PARAMETER_SIGNATURE_MODE = "FIRMA.SIGNATURE.MODE";
	
	// FIRE
	public static final String C_PARAMETER_FIRE_ID = "FIRE.ID";
	
	// EEUtil oper-firma
	public static final String C_PARAMETER_EEUTIL_OPER_FIRMA_URL = "EEUTIL.OPER.FIRMA.URL";
	public static final String C_PARAMETER_EEUTIL_OPER_FIRMA_USER = "EEUTIL.OPER.FIRMA.USER";
	public static final String C_PARAMETER_EEUTIL_OPER_FIRMA_PASSWORD = "EEUTIL.OPER.FIRMA.PASSWORD";
	public static final String C_PARAMETER_EEUTIL_VALIDATE_SIGN_ACTIVE = "EEUTIL.VALIDAR.FIRMA.ACTIVO";
	public static final String C_PARAMETER_EEUTIL_VALIDATE_CERT_ACTIVE = "EEUTIL.VALIDAR.CERT.ACTIVO";
	public static final String C_PARAMETER_EEUTIL_SELLO_ACTIVE = "EEUTIL.SELLO.ACTIVO";
	// EEUtil util-firma
	public static final String C_PARAMETER_EEUTIL_UTIL_FIRMA_URL = "EEUTIL.UTIL.FIRMA.URL";
	public static final String C_PARAMETER_EEUTIL_UTIL_FIRMA_USER = "EEUTIL.UTIL.FIRMA.USER";
	public static final String C_PARAMETER_EEUTIL_UTIL_FIRMA_PASSWORD = "EEUTIL.UTIL.FIRMA.PASSWORD";
	public static final String C_PARAMETER_EEUTIL_CSV_ACTIVE = "EEUTIL.CSV.ACTIVO";
	public static final String C_PARAMETER_EEUTIL_REPORT_ACTIVE = "EEUTIL.REPORT.ACTIVO";
	public static final String C_PARAMETER_EEUTIL_VIS_PREFIRMA_ACTIVE = "EEUTIL.VIS.PREFIRMA.ACTIVO";
	// EEUtil misc
	public static final String C_PARAMETER_EEUTIL_MISC_URL = "EEUTIL.MISC.URL";
	public static final String C_PARAMETER_EEUTIL_MISC_USER = "EEUTIL.MISC.USER";
	public static final String C_PARAMETER_EEUTIL_MISC_PASSWORD = "EEUTIL.MISC.PASSWORD";
	public static final String C_PARAMETER_EEUTIL_VISUALIZAR_TCN_ACTIVO = "EEUTIL.VISUALIZAR.TCN.ACTIVO";
	// EEUtil vis
	public static final String C_PARAMETER_EEUTIL_VIS_URL = "EEUTIL.VIS.URL";
	public static final String C_PARAMETER_EEUTIL_VIS_USER = "EEUTIL.VIS.USER";
	public static final String C_PARAMETER_EEUTIL_VIS_PASSWORD = "EEUTIL.VIS.PASSWORD";
	
	
	//EEUtil util-firma

	public static final String C_PARAMETER_EEUTIL_FIRMA_URL = "EEUTIL.FIRMA.URL";
	public static final String C_PARAMETER_EEUTIL_FIRMA_USER = "EEUTIL.FIRMA.USER";
	public static final String C_PARAMETER_EEUTIL_FIRMA_PASSWORD = "EEUTIL.FIRMA.PASSWORD";
	
	// Sign report parameters
	/**
	 * Mantiene el valor del par&aacute;metro de firma de informes que indica 
	 * si se firman los informes de firma tras ser generados (S/N).
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGNREPORT_PARAM = "INFORME.FIRMAPDF";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGNREPORT_KEYSTORE_PARAM = "INFORME.FIRMAPDF.KEYSTORE";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGNREPORT_KEYSTOREPASS_PARAM = "INFORME.FIRMAPDF.KEYSTORE.PWD";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGNREPORT_PARAM_CERTALIAS = "INFORME.FIRMAPDF.CERT.ALIAS";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGNREPORT_PARAM_CERTPASSWORD = "INFORME.FIRMAPDF.CERT.PASSWORD";

	// Path of image used for report sign
	/**
	 * Mantiene el valor de la ruta de la imagen de marca de agua para la firma de informes.
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGNREPORT_IMAGE_PATH = "/theme/img/stamp_report.png";

	// ZIP Constants
	/**
	 * Mantiene el valor del prefijo de los archivos temporales.
	 * El valor de esta constante es {@value}.
	 */
	public static final String TEMP_FILE_PREFIX = "pfirma";
	
	public static final String SIGN_REPORT_FILE_PREFIX = "InformeFirma";
	
	public static final String ZIP_EXTENSION = ".zip";

	// PF_USUARIOS
	/**
	 * Mantiene el valor del tipo de job de usuario
	 * El valor de esta constante es {@value}.
	 */
	public static final String C_TYPE_USER_JOB = "CARGO";
	public static final String C_TYPE_USER_USER = "USUARIO";
	public static final String C_TYPE_USER_APLI = "APLICACION";
	public static final String C_TYPE_USER_EXTERNAL = "EXTERNO";
	
	// TIPOS DE FIRMA
	public static final String FIRMA_PRIMER_FIRMANTE = "PRIMER FIRMANTE";
	public static final String FIRMA_PARALELA = "PARALELA";
	public static final String FIRMA_CASCADA = "CASCADA";

	public static String SIGN_TYPE_CASCADE = "signTypeCascade";
	public static String SIGN_TYPE_PARALLEL = "signTypeParallel";

	public static final String C_TYPE_SUBJECT_COMMENT = "COMENTARIO";

	/*
	 * REQUEST TEMPLATE
	 */
	public static final String C_FIRMANTE_PLANTILLA = "F";
	public static final String C_VISTO_BUENO_PLANTILLA = "V";

	/*
	 * Codes and descriptions
	 */

	// PF_PETICIONES_HISTORICO
	public static final String C_HISTORICREQUEST_REMITTER = "REMITENTE";
	public static final String C_HISTORICREQUEST_REMITTERNAME = "REMITENTE.NOMBRE";
	public static final String C_HISTORICREQUEST_REMITTEREMAIL = "REMITENTE.EMAIL";
	public static final String C_HISTORICREQUEST_REMITTERMOBILE = "REMITENTE.MOVIL";
	public static final String C_HISTORICREQUEST_MODIFICATION = "PETICION.MODIFICIACION";
	public static final String D_HISTORICREQUEST_UNKNOW = "DESCONOCIDO";

	// PF_FIRMAS
	public static final String SIGN_FORMAT_NONE = "NINGUNO";
	public static final String SIGN_TYPE_BLOCK = "BLOCK";
	/**
	 * Mantiene el valor del tipo de firma de servidor
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_TYPE_SERVER = "SERVER";
	/**
	 * Mantiene el valor del formato de firma PKCS7
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_PKCS7 = "PKCS7";
	/**
	 * Mantiene el valor del formato de firma CMS
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_CMS = "CMS";
	/**
	 * Mantiene el valor del formato de firma XADES
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES = "XADES";
	/**
	 * Mantiene el valor del formato de firma XADES-BES
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES_BES = "XADES-BES";
	/**
	 * Mantiene el valor del formato de firma XADES-T
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES_T = "XADES-T";
	/**
	 * Mantiene el valor del formato de firma CADES
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_CADES = "CADES";
	/**
	 * Mantiene el valor del formato de firma PDF
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_PDF = "PDF";
	/**
	 * Mantiene el valor del formato de firma XADES_ENVELOPING
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES_ENVELOPING = "XADES ENVELOPING";
	/**
	 * Mantiene el valor del formato de firma XADES_ENVELOPED
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES_ENVELOPED = "XADES ENVELOPED";
	/**
	 * Mantiene el valor del formato de firma XADES_IMPLICITO
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES_IMPLICIT = "XADES IMPLICITO";
	/**
	 * Mantiene el valor del formato de firma XADES_EXPLICITO
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_XADES_EXPLICIT = "XADES EXPLICITO";
	/**
	 * Mantiene el valor del formato de firma FACTURAE
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_FORMAT_FACTURAE = "FACTURAE";
	/**
	 * Mantiene el valor de la extensi&oacute;n de firma tipo CMS
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_EXTENSION_CMS = "p7s";
	/**
	 * Mantiene el valor de la extensi&oacute;n de firma tipo XADES
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_EXTENSION_XADES = "xsig";
	/**
	 * Mantiene el valor de la extensi&oacute;n de firma tipo CADES
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_EXTENSION_CADES = "csig";
	/**
	 * Mantiene el valor de la extensi&oacute;n de firma tipo PDF
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_EXTENSION_PDF = "pdf";
	/**
	 * Mantiene el valor del MIMETYPE para PKCS7
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_MIMETYPE_PKCS7 = "application/pkcs7-signature";
	/**
	 * Mantiene el valor del MIMETYPE para XML
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_MIMETYPE_XML = "application/xml";
	
	
	public static final String SIGN_MIMETYPE_TXT_XML = "text/xml";
	
	/**
	 * Mantiene el valor del MIMETYPE para CADES
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_MIMETYPE_CADES = "application/octet-stream";
	/**
	 * Mantiene el valor del MIMETYPE para PDF
	 * El valor de esta constante es {@value}.
	 */
	public static final String SIGN_MIMETYPE_PDF = "application/pdf";

	public static final String SIGN_MODE_BLOCK = "BLOQUE";
	public static final String SIGN_MODE_MASSIVE = "MASIVA";
	public static final String SIGN_HASH_SIGNATURE_MODE = "HASH";
	public static final String SIGN_BINARY_SIGNATURE_MODE = "BINARIO";
	public static final String SIGN_HASH = "HASH";
	public static final String SIGN_HASH_PREFFIX = "HASH;";
	public static final String SIGN_BINARY = "BINARY";
	public static final String SIGN_BINARY_PREFFIX = "BINARY;";
	public static final String SIGN_MULTISIGN = "MULTISIGN";
	public static final String SIGN_MULTISIGN_PREFFIX = "MULTISIGN;";
	public static final String SIGN_SIGN_OPPERATION = "sign";
	public static final String SIGN_SIGN_OPPERATION_PREFFIX = "FIRMA;";
	public static final String SIGN_COSIGN_OPPERATION = "cosign";
	public static final String SIGN_COSIGN_OPPERATION_PREFFIX = "COFIRMA;";
	public static final String SIGN_COUNTERSIGN_OPPERATION = "countersign";
	public static final String SIGN_COUNTERSIGN_OPPERATION_PREFFIX = "CONTRAFIRMA;";
	public static final String SIGN_FORMAT_SUFFIX = ";";
	public static final String SIGN_FORMAT_DEFAULT = "DEFAULT";
	public static final String SIGN_FORMAT_AUTO = "AUTO";
	public static final String MULTISIGN_JS_FUNCTION = "firmaMasiva();";
	public static final String BLOCK_SIGN_JS_FUNCTION = "firmaBloque();";
	public static final String SIGN_MASSIVE_ERROR = "#ERROR#";
	public static final String AFIRMA_MIME_GENERICO = "application/octet-stream";

	/**
	 * Tipos de informe de copia auténtica
	 */
	public static final String COPIA_AUTENTICA = "COPIA_AUTENTICA";
	public static final String PREVISUALIZACION = "PREVISUALIZACION";
	
	// PF_ETIQUETAS
	// states
	/**
	 * mantiene el valor para la etiqueta de 'nuevo'.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_NEW = "NUEVO";
	/**
	 * mantiene el valor para la etiqueta de 'leido'.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_READ = "LEIDO";
	
	
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_NOTIFIED = "NOTIFICADO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_NO_NOTIFIED = "NO NOTIFI.";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_SIGNED = "FIRMADO";
	/**
	 * mantiene el valor para la etiqueta de 'en espera'.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_AWAITING = "EN ESPERA";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_REJECTED = "DEVUELTO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_ENQUEUED = "ENCOLADO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_EXPIRED = "CADUCADO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_PASSED = "VISTOBUENO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_AWAITING_PASSED = "VALIDADO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_REMOVED = "RETIRADO";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_FORWARDED = "REENVIADO";
	
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_CANCELED = "ANULADA";

	public static final String C_TAG_NONE = "";
	/**
	 * El valor de esta constante es {@value}
	 */
	public static final String[] NOT_FINISHED_TAGS = {Constants.C_TAG_NEW,
												      Constants.C_TAG_READ, 
												      Constants.C_TAG_AWAITING,
												      Constants.C_TAG_AWAITING_PASSED};

	public static final String FORWARED_COMMENT = "La petición ha sido reenviada a ";

	// system
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_SYSTEM_PASSED   = "TIPO.VISTOBUENO";
	public static final String C_TAG_SYSTEM_FORWARED = "TIPO.REENVIADA";
	public static final String C_TAG_SIGN_TYPE = "TIPO.FIRMA";
	public static final String C_TAG_ANULLED_TYPE = "TIPO.ANULADA";

	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_TAG_SYSTEM_PRIVATE = "TIPO.PRIVATE";

	public static final String TAG_DELETE_ASSOCIATED = "labelAssociated";
	public static final String TAG_DELETE = "deleteLabel";

	// PF_TIPOS_DOCUMENTO
	/**
	 * mantiene el valor del tipo de documento gen&eacute;rico.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_DOCUMENTTYPE_GENERIC = "GENERICO";
	public static final String C_DOCUMENTTYPE_FACTURAE = "FACTURAE";

	// PF_PERFILES
	/**
	 * mantiene el valor del perfil de acceso.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_ACCESS = "ACCESO";
	/**
	 * mantiene el valor del perfil de administrador.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_ADMIN = "ADMIN";
	/**
	 * mantiene el valor del perfil de firma.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_SIGN = "FIRMA";
	/**
	 * mantiene el valor del perfil de redacci&oacute;n
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_REDACTION = "REDACCION";
	/**
	 * mantiene el valor del perfil de simular.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_SIMULATE = "SIMULAR";
	/**
	 * mantiene el valor del perfil de redacci&oacute;n de job.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_REDACTION_JOB = "REDACCARGO";
	/**
	 * mantiene el valor del perfil de webservice.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_WEBSERVICE = "WEBSERVICE";
	
	/**
	 * mantiene el valor del perfil de webservice con permisos de administrador.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_WEBSERVICE_ADMIN = "WEBSERVICEADMIN";	
	
	/**
	 * mantiene el valor del perfil de administración de provincias.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_ADMIN_PROVINCE = "ADMINPROV";
	
	/**
	 * mantiene el valor del perfil de administración del CAID.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_ADMIN_CAID = "ADMINCAID";
	
	public static final String C_PROFILES_ADMIN_ORGANISM = "ADMIN_ORG";
	/**
	 * mantiene el valor del perfil de administración de provincias.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_GROUP = "GRUPO";
	/**
	 * mantiene el valor del perfil de administración de provincias.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_VALIDATOR = "VALIDADOR";
	
	/**
	 * mantiene el valor del perfil de gestor.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_PROFILES_GESTOR = "GESTOR";

	// PF_ACCIONES
	public static final String C_ACTIONS_WEB = "WEB";
	public static final String C_ACTIONS_PLSQL = "PLSQL";

	/*
	 * Notification
	 */
	/**
	 * mantiene el valor del par&aacute;metro del email de notificaci&oacute;n.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EMAIL_NOTICE_PARAM = "NOTIFICACION.CORREO";
	/**
	 * mantiene el valor del par&aacute;metro del sms de notificaci&oacute;n.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String SMS_NOTICE_PARAM = "NOTIFICACION.SMS";
	/**
	 * mantiene el valor del par&aacute;metro para notificaciones de tipo email
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EMAIL_NOTICE = "EMAIL";

	public static final String PUSH_NOTICE = "PUSH";
	
	public static final String INVITATION_NOTICE = "INVITATION";
	
	/**
	 * mantiene el valor del par&aacute;metro para notificaciones de tipo SMS
	 *  El valor de esta constante es {@value}.
	 */
	public static final String SMS_NOTICE = "SMS";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String NOTICE = "NOTICE";
	/**
	 * Reintentos para la tarea Quartz
	 */
	//Agustin el update request no le hace caso a esto
	public static final int NOTICE_RETRY = 5;
	/**
	 * Intervalo de tiempo para la tarea Quartz
	 */
	//Agustin el update request no le hace caso a esto
	public static final int NOTICE_MS_INTERVAL = 30000;
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String NOTICE_CONFIGURATION = "noticeConfiguration";
	/**
	 * El valor de esta constante es {@value}.
	 */
	public static final String NOTICE_REQUEST_HASH = "noticeHash";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String NOTICE_MESSAGE = "noticeMessage";
	
	public static final String NOTICE_EVENT = "noticeEvent";
	
	public static final String NOTICE_ABSTRACT_DTO = "abstractDTO";
	
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String NOTICE_UNKNOWN_REMMITTER = "noticeUnknownRemmitter";

	/*
	 * Email parameters
	 */
	/**
	 * mantiene el valor del email del usuario
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EMAIL_USER = "NOTIFICACION.CORREO.USUARIO";
	/**
	 * mantiene el valor del password del email del usuario
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EMAIL_PASSWORD = "NOTIFICACION.CORREO.CLAVE";
	/**
	 * mantiene el valor del email del remitente
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EMAIL_REMITTER = "NOTIFICACION.CORREO.REMITENTE";
	/**
	 * mantiene el valor del servidor SMTP
	 *  El valor de esta constante es {@value}.
	 */
	public static final String SMTP_SERVER = "NOTIFICACION.SMTP.SERVIDOR";
	/**
	 * mantiene el valor del puerto del servidor del SMTP
	 *  El valor de esta constante es {@value}.
	 */
	public static final String SMTP_PORT = "NOTIFICACION.SMTP.PUERTO";
	/**
	 * mantiene el valor del par&aacute;metro de notificaci&oacute;n del nombre del remitente
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EMAIL_REMITTER_NAME = "NOTIFICACION.CORREO.NOMBRE";
	/**
	 * mantiene el valor del modo de autenticaci&oacute;n
	 *  El valor de esta constante es {@value}.
	 */
	public static final String AUTHENTICATION_MODE = "NOTIFICACION.CORREO.AUTH";

	/* SIZE PARAMETER */

	/**
	 * Mantiene el valor del tamaño máximo en bytes permitido para los documentos
	 * El valor de esta constante es {@value}.
	 */
	public static final String MAX_DOCUMENT_SIZE = "DOCUMENTO.LONGITUD";
	
	/**
	 * Mantiene el nombre del parámetro que indica si se quiere mostrar las opciones
	 * de visibilidad de usuarios.
	 */
	public static final String SHOW_VISIBILITY_OPTIONS = "MOSTRAR.OPCIONES.VISIBILIDAD";
	
	public static final String VALIDATE_SIGN = "VALIDAR.FIRMAS";
	

	/* MIME TYPES */

	/**
	 * Mantiene el valor de la lista de mimes permitidos
	 * El valor de esta constante es {@value}.
	 */
	public static final String ACCEPTED_MIME_TYPES = "MIME.TYPES.ACEPTADOS";

	/* FILE EXTENSIONS */

	/**
	 * Mantiene el valor de la lista de extensioens permitidas
	 * El valor de esta constante es {@value}.
	 */
	public static final String ACCEPTED_FILE_EXTENSIONS = "EXTENSIONES.ACEPTADAS";
	
	public static final String SIGN_WAIT_TIME = "TIEMPO.ESPERA.FIRMA";
	
	/**
	 * Mantiene el valor del entorno donde está desplegada la aplicación
	 * El valor de esta constante es {@value}.
	 */
	public static final String ENVIRONMENT = "ENTORNO";

	/*
	 * TIPOS DE AUTORIZACION
	 */
	public static final String DELEGATE   = "DELEGADO";
	public static final String SUBSTITUTE = "SUSTITUTO";

	/*
	 * ÁMBITOS DE DOCUMENTOS
	 */
	public static final String INTERNAL_SCOPE = "INTERNO";
	public static final String EXTERNAL_SCOPE = "EXTERNO";

	/*
	 * Email api config constants
	 */
	/**
	 * mantiene el valor del par&aacute;metro para poner el host smtp de en las 
	 * propiedades del sistema
	 *  El valor de esta constante es {@value}.
	 */
	public static final String MAIL_SERVER = "mail.smtp.host";
	public static final String MAIL_SERVER_AUTH = "mail.smtp.auth";
	public static final String MAIL_NOTIFICATION_HEADER = "Disposition-Notification-To";
	public static final String MAIL_MIME_CHARSET = "mail.mime.charset";
	public static final String MAIL_MIME_CHARSET_UTF8 = "UTF-8";	
	public static final String MAIL_MIME_TYPE_HTML_UTF8 = "text/html; charset=UTF-8";
	public static final String MAIL_MIME_TYPE_PLAIN_UTF8 = "text/plain; charset=UTF-8";
	public static final String MAIL_RELATED_TYPE = "related";
	public static final String MAIL_ALTERNATIVE_TYPE = "alternative";
	public static final String MAIL_STORE_TYPE = "pop3";
	public static final String MAIL_FOLDER_TYPE = "INBOX";
	public static final String MAIL_CONS_TRUE = "true";
	public static final String MAIL_CONS_FALSE = "false";
	public static final String MAIL_CONS_TRANPORT = "smtp";
	public static final String MAIL_SERVER_PORT = "mail.smtp.port";
	public static final String MAIL_SERVER_STARTTLS = "mail.smtp.starttls.enable";

	/*
	 * Email MPT adress
	 */
	public static final String EMAIL_SERVER_MPT = "@seap.minhap.es";

	/*
	 * Notice text messages
	 */
	public static final String NOTICE_TEXT_HEADER = "noticeTextHeader";
	public static final String NOTICE_READ = "requestRead";
	public static final String NOTICE_SIGNED = "requestSigned";
	public static final String NOTICE_PASSED = "requestPassed";
	public static final String NOTICE_REJECTED = "requestRejected";
	public static final String NOTICE_REMOVED = "requestRemoved";
	public static final String NOTICE_NOREPLY_TEXT = "noticeNoReply";
	public static final String NOTICE_NEWINVITEDREQUEST_SUBJECT="noticeNewInvitedRequestSubject";
	public static final String NOTICE_STATECHANGED_SUBJECT = "noticeStateChangedSubject";
	public static final String NOTICE_NEWCOMMENT_SUBJECT = "noticeNewCommentSubject";
	public static final String NOTICE_SIGNERADDED_SUBJECT = "noticeSignerAddedSubject";
	public static final String NOTICE_NEWREQUEST_SUBJECT = "noticeNewRequestSubject";
	public static final String NOTICE_FORWARDREQUEST_SUBJECT = "noticeForwardRequestSubject";
	public static final String NOTICE_STATECHANGED_TEXT = "noticeStateChangedTextBody";
	public static final String NOTICE_NEWINVITEDREQUEST_TEXT = "noticeNewInvitedRequestTextBody";
	public static final String NOTICE_STATECHANGED_REJECTIONREASON_TEXT = "noticeStateChangedTextRejectionReason";
	public static final String NOTICE_NEWCOMMENT_TEXT = "noticeCommentTextBody";
	public static final String NOTICE_SIGNERADDED_TEXT = "noticeSignerAddedTextBody";
	public static final String NOTICE_NEWREQUEST_TEXT = "noticeNewRequestTextBody";
	//Agustin #1406 mostrar el nombre del destinatario de las peticiones de firma
	public static final String NOTICE_NEWREQUEST_TEXT_SIGNER = "noticeNewRequestTextBodySigner";
	public static final String NOTICE_NEWREQUEST_VALIDADOR_TEXT = "noticeNewRequestTextBodyValidador";
	public static final String NOTICE_FORWARDREQUEST_TEXT = "noticeForwardRequestTextBody";
	public static final String NOTICE_URL = "noticeURL";
	public static final String NOTICE_REQUEST_START_DATE = "noticeRequestStartDate";
	public static final String NOTICE_REQUEST_EXPIRATION_DATE = "noticeRequestExpirationDate";
	public static final String NOTICE_NEWAUT_SUBJECT = "noticeNewAuthorization";
	public static final String NOTICE_NEWAUT_TEXT = "noticeNewAuthorizationTextBody";
	public static final String NOTICE_NEWAUT_ADVICE = "noticeNewAuthorizationText";
	public static final String NOTICE_ADMAUT_SUBJECT = "noticeAdminAuthorization";
	public static final String NOTICE_ADMAUT_TEXT = "noticeAdminAuthorizationTextBody";
	public static final String NOTICE_ADMAUT_TEXT_NO_END_TIME = "noticeAdminAuthorizationTextBodyNoEndDate";
	public static final String NOTICE_ACCEPTED_AUT_SUBJECT = "noticeAcceptedAuthorization";
	public static final String NOTICE_ACCEPTED_AUT_TEXT = "noticeAcceptedAuthorizationTextBody";
	public static final String NOTICE_DENIED_AUT_SUBJECT = "noticeDeniedAuthorization";
	public static final String NOTICE_DENIED_AUT_TEXT = "noticeDeniedAuthorizationTextBody";
	public static final String NOTICE_REQUEST_EXPIRED_TEXT = "noticeRequestExpiredTextBody";
	public static final String NOTICE_REQUEST_EXPIRED = "noticeRequestExpiredSubject";
	public static final String NOTICE_REQUEST_ANULLED_TEXT = "noticeRequestAnulledTextBody";
	public static final String NOTICE_REQUEST_ANULLED = "noticeRequestAnulledSubject";
	public static final String NOTICE_REQUEST_ANULLED_RECOVER_TEXT = "noticeRequestAnulledRecoveredTextBody";
	public static final String NOTICE_REQUEST_ANULLED_RECOVER = "noticeRequestAnulledRecoveredSubject";
	public static final String NOTICE_REQUEST_SIGNED = "noticeRequestSignedSubject";
	public static final String NOTICE_REQUEST_SIGNED_TEXT = "noticeRequestSignedTextBody";
	public static final String NOTICE_EEUTIL_EXCEPTION_SUBJECT = "noticeEeutilExceptionSubject";
	public static final String NOTICE_EEUTIL_EXCEPTION_TEXT = "noticeEeutilExceptionText";
	public static final String NOTICE_EEUTIL_EXCEPTION_VALIDAR_FIRMA = "noticeEeutilExceptionTextValidarFirma";	
	public static final String NOTICE_EEUTIL_EXCEPTION_GENERAR_INFORME = "noticeEeutilExceptionTextGenerarInforme";
	public static final String NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO = "noticeEeutilExceptionTextCrearServicio";
	public static final String NOTICE_EEUTIL_EXCEPTION_OBTENER_FIRMANTES = "noticeEeutilExceptionTextObtenerFirmantes";
	public static final String NOTICE_EEUTIL_EXCEPTION_VALIDAR_CERTIFICADO = "noticeEeutilExceptionTextValidarCertificado";
	
	public static final String NOTICE_EEUTIL_EXCEPTION_AMPLIAR_FIRMA = "noticeEeutilExceptionTextAmpliarFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILFIRMA = "noticeEeutilExceptionTextCrearServicioUtilFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_OPERFIRMA = "noticeEeutilExceptionTextCrearServicioUtilOperFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILTUILFIRMA = "noticeEeutilExceptionTextCrearServicioUtilUtilFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILVIS = "noticeEeutilExceptionTextCrearServicioUtilVis";
	public static final String NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILMISC = "noticeEeutilExceptionTextCrearServicioUtilMisc";
	public static final String NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILFIRMA = "noticeEeutilExceptionTextCargarConfiguracionServicioUtilFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_OPERFIRMA = "noticeEeutilExceptionTextCargarConfiguracionServicioUtilOperFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILUTILFIRMA = "noticeEeutilExceptionTextCargarConfiguracionServicioUtilUtilFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILVIS = "noticeEeutilExceptionTextCargarConfiguracionServicioUtilVis";
	public static final String NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILMISC = "noticeEeutilExceptionTextCargarConfiguracionServicioUtilMisc";
	public static final String NOTICE_EEUTIL_EXCEPTION_INFO_CERTIFICADO = "noticeEeutilExceptionTextInfoCertificado";
	public static final String NOTICE_EEUTIL_EXCEPTION_CONVERTIR_TCNAPDF = "noticeEeutilExceptionTextConvertirTCNAPdf";
	public static final String NOTICE_EEUTIL_EXCEPTION_OBTENER_INFO_FIRMA = "noticeEeutilExceptionTextObtenerInformacionFirma";
	public static final String NOTICE_EEUTIL_EXCEPTION_VISUALIZAR_FACTURAE_PDF = "noticeEeutilExceptionTextVisualizarFacturaePDF";
	public static final String NOTICE_EEUTIL_EXCEPTION_VISUALIZAR_CONTENIDO_ORIGINAL = "noticeEeutilExceptionTextVisualizarContenidoOriginal";
	public static final String NOTICE_EEUTIL_EXCEPTION_FIRMA_FICHERO = "noticeEeutilExceptionTextFirmaFichero";

	public static final String NOTICE_EEUTIL_EXCEPTION_GENERAR_CSV = "noticeEeutilExceptionTextGenerarCSV";
	public static final String NOTICE_EEUTIL_EXCEPTION_COMPROBAR_PDFA = "noticeEeutilExceptionTextComprobarPDFA";
	
	public static final String NOTICE_SEND_REPORTS_SUBJECT = "sendReportsSubject";
	public static final String NOTICE_SEND_REPORTS_TEXT = "noticeSendReportsTextBody";
	public static final String NOTICE_SEND_REPORTS_TEXT_PETICION = "noticeSendReportsTextBodyPeticionComodin";
	public static final String NOTICE_REQUEST_VALIDATED_SUBJECT = "noticeRequestValidatedSubject";
	public static final String NOTICE_REQUEST_VALIDATED_TEXT = "noticeRequestValidatedTextBody";
	public static final String NOTICE_FORWARDREQUESTREMITENTE_SUBJECT = "noticeForwardRequestRemitenteSubject"; 
	public static final String NOTICE_FORWARDREQUESTREMITENTE_TEXT = "noticeForwardRequestRemitenteTextBody";
	public static final String NOTICE_FORWARDREQUESTREMITENTE_TEXT_DESTINATARIO= "noticeForwardRequestRemitenteTextDestinatario";

	
	/*
	 * Notice text vars
	 */
	public static final String NOTICE_STATE_VAR = "$1";
	public static final String NOTICE_SUBJECT_VAR = "$2";
	public static final String NOTICE_SIGNER_VAR = "$3";
	public static final String NOTICE_REFERENCE_VAR = "$4";
	public static final String NOTICE_REJECTIONREASON_VAR = "$5";
	public static final String NOTICE_REMITTER_VAR = "$6";
	public static final String NOTICE_USERWHOCOMMENT_VAR = "$7";
	public static final String NOTICE_USERHISTORIC_VAR = "$8";
	public static final String NOTICE_JOBHISTORIC_VAR = "$9";
	public static final String NOTICE_COMMENT_VAR = "$10";
	public static final String NOTICE_URL_VAR = "$11";
	public static final String NOTICE_FSTART_DATE_VAR = "$12";
	public static final String NOTICE_FSTART_TIME_VAR = "$13";
	public static final String NOTICE_FEXPIRATION_DATE_VAR = "$14";
	public static final String NOTICE_FEXPIRATION_TIME_VAR = "$15";
	public static final String NOTICE_AUTHORIZATION_USER = "$16";
	public static final String NOTICE_AUT_FSTART_DATE_VAR = "$17";
	public static final String NOTICE_AUT_FREVOCATION_DATE_VAR = "$18";
	public static final String NOTICE_AUTHORIZED_USER = "$19";
	public static final String NOTICE_EEUTIL_HOST_NAME = "$20";
	
	/*Invited Requests*/
	public static final String NOTICE_INV_REQ_EMAIL_VAR="$1";
	public static final String NOTICE_INV_REQ_DNI_VAR="$2";
	public static final String NOTICE_INV_REQ_NAME_VAR="$3";
	public static final String NOTICE_INV_REQ_USER_CREATION="$4";
	
	/*
	 * Notice events
	 */
	public static final String NOTICE_EVENT_STATE_CHANGE = "stateChange";
	/**
	 *  Mantiene el valor para nuevo comentario
	 *  El valor de esta constante es {@value}.
	 */
	public static final String NOTICE_EVENT_NEW_COMMENT = "newComment";
	public static final String NOTICE_EVENT_SIGNER_ADDED = "SignerAdded";
	public static final String NOTICE_EVENT_NEW_REQUEST = "newRequest";
	public static final String NOTICE_EVENT_NEW_REQUEST_VALIDADOR = "newRequestValidador";
	public static final String NOTICE_EVENT_FORWARD_REQUEST = "forwardRequest";
	public static final String NOTICE_EVENT_FORWARD_REQUEST_REMITENTE = "forwardRequestRemitente";
	public static final String NOTICE_EVENT_SEND_REPORTS = "sendReports";
	public static final String NOTICE_EVENT_INVITED_REQUEST = "newInvitedRequest";
	public static final String NOTICE_EVENT_ANULLED_REQUEST = "anulledRequest";
	public static final String NOTICE_EVENT_REMOVE_ANULLED_REQUEST = "removeAnulledRequest";
	public static final String NOTICE_EVENT_VALIDATED_REQUEST = "newValidatedRequest";
	
	public static final String NOTICE_AUTHORIZATION_ACCEPTED = "autAccepted";
	public static final String NOTICE_AUTHORIZATION_DENIED = "autDenied";
	public static final String NOTICE_NEW_AUTHORIZATION = "newAuthorization";
	public static final String NOTICE_ADMIN_AUTHORIZATION = "adminAuthorization";

	public static final String NOTICE_EEUTIL_EXCEPTION = "eeutilException";

	/* EEUTILS EVENTS */

	public static final String EEUTIL_VALIDAR_FIRMA = "validarFirma";
	public static final String EEUTIL_AMPLIAR_FIRMA = "ampliarFirma";
	public static final String EEUTIL_GENERAR_INFORME = "generarInforme";
	public static final String EEUTIL_OBTENER_FIRMANTES = "obtenerFirmantes";
	public static final String EEUTIL_CREAR_SERVICIO = "crearServicio";
	public static final String EEUTIL_CREAR_SERVICIO_UTILFIRMA = "crearServicioUtilFirma";
	public static final String EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA = "crearServicioUtilOperFirma";
	public static final String EEUTIL_CREAR_SERVICIO_UTILUTILFIRMA = "crearServicioUtilUtilFirma";
	public static final String EEUTIL_CREAR_SERVICIO_UTILVIS = "crearServicioUtilVis";
	public static final String EEUTIL_CREAR_SERVICIO_UTILMISC = "crearServicioUtilMisc";
	public static final String EEUTIL_CARGAR_CONFIGURACION_UTILFIRMA = "cargarConfiguracionServicioUtilFirma";
	public static final String EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA = "cargarConfiguracionServicioUtilOperFirma";
	public static final String EEUTIL_CARGAR_CONFIGURACION_UTILUTILFIRMA = "cargarConfiguracionServicioUtilUtilFirma";
	public static final String EEUTIL_CARGAR_CONFIGURACION_UTILVIS = "cargarConfiguracionServicioUtilVis";
	public static final String EEUTIL_CARGAR_CONFIGURACION_UTILMISC = "cargarConfiguracionServicioUtilMisc";
	public static final String EEUTIL_VALIDAR_CERTIFICADO = "validarCertificado";
	public static final String EEUTIL_INFO_CERTIFICADO = "infoCertificado";
	public static final String EEUTIL_CONVERTIR_TCN = "convertirTCNAPdf";
	public static final String EEUTIL_OBTENER_INFORMACION_FIRMA = "obtenerInformacionFirma";
	public static final String EEUTIL_CONVERTIR_EFACTURA_PDF = "visualizarFacturaePDF";
	public static final String EEUTIL_COMPROBAR_PDFA = "comprobarPDFA";
	public static final String EEUTIL_VISUALIZAR_CONTENIDO_ORIGINAL = "visualizarContenidoOriginal";
	public static final String EEUTIL_GENERAR_CSV = "generarCSV";
	public static final String EEUTIL_FIRMA_FICHERO= "firmaFichero";

	/*
	 * HISTORIC_REQUEST
	 */
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para devuelto
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_REJECTED = "CAMBIO.DEVUELTO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para lectura
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_READ = "CAMBIO.LEIDO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para firma
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_SIGNED = "CAMBIO.FIRMADO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para firma
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_VALIDATED = "CAMBIO.VALIDADO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para una petici&oacute;n con visto bueno pasado
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_PASSED = "CAMBIO.VISTOBUENO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para una petici&oacute;n retirada
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_REMOVED = "CAMBIO.RETIRADO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico para una petici&oacute;n retirada
	 *  El valor de esta constante es {@value}.
	 */
	public static final String C_HISTORIC_REQUEST_EXPIRED = "CAMBIO.CADUCADO";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico de devuelto para el mapa de messages
	 *  El valor de esta constante es {@value}.
	 */
	public static final String PROP_HISTORIC_REQUEST_REJECTED = "historicRequestRejected";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico de lectura para el mapa de messages
	 *  El valor de esta constante es {@value}.
	 */
	public static final String PROP_HISTORIC_REQUEST_READ = "historicRequestRead";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico de firma para el mapa de messages
	 *  El valor de esta constante es {@value}.
	 */
	public static final String PROP_HISTORIC_REQUEST_SIGNED = "historicRequestSigned";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico de firma para el mapa de messages
	 *  El valor de esta constante es {@value}.
	 */
	public static final String PROP_HISTORIC_REQUEST_VALIDATED = "historicRequestValidated";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico de una petici&oacute;n con visto bueno pasado 
	 *  para el mapa de mensajes
	 *  El valor de esta constante es {@value}.
	 */
	public static final String PROP_HISTORIC_REQUEST_PASSED = "historicRequestPassed";
	/**
	 *  Mantiene el codigo de texto de hist&oacute;rico de una petici&oacute;n retirada 
	 *  para el mapa de mensajes
	 *  El valor de esta constante es {@value}.
	 */
	public static final String PROP_HISTORIC_REQUEST_REMOVED = "historicRequestRemoved";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String HISTORIC_REQUEST_USER_VAR = "$1";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String HISTORIC_REQUEST_JOB_VAR = "$2";
	/**
	 *  El valor de esta constante es {@value}.
	 */
	public static final String HISTORIC_REQUEST_VAR_SEPARATOR = ";";
	public static final String HISTORIC_REQUEST_VAR_EQUAL = "=";

	/*
	 * HASH
	 */
	
	/**
	 * Mantiene un array de char con caracteres alfanumericos
	 */
	public static final char C_HASH_ALFANUMERICS[] = { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };
	// Longitud por defecto de la cadena CHASH
	public static final long C_HASH_DEFAULTLENGTH = 10;

	// Export
	/**
	 * Mantiene el valor del tipo de exportaci&oacute;n xml
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_XML_TYPE = "xml";
	/**
	 * Mantiene el valor del tipo de exportaci&oacute;n pdf
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_PDF_TYPE = "pdf";
	/**
	 * Mantiene el valor del tipo de exportaci&oacute;n xls
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_XLS_TYPE = "xls";
	/**
	 * Mantiene el valor del tipo de exportaci&oacute;n csv
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_CSV_TYPE = "csv";
	/**
	 * Mantiene el valor del tipo de exportaci&oacute;n
	 *  Contiene el nombre de la cadena los archivos mime de tipo xml
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_XML_MIME = "application/xml";
	/**
	 *  Contiene el nombre de la cadena los archivos mime de tipo pdf
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_PDF_MIME = "application/pdf";
	/**
	 *  Contiene el nombre de la cadena los archivos mime de tipo excel
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_XLS_MIME = "application/vnd.ms-excel";
	/**
	 *  Contiene el nombre de la cadena los archivos mime de tipo html
	 *  El valor de esta constante es {@value}.
	 */
	public static final String EXPORT_CSV_MIME = "text/html";

	// Signers tree
	public static final String PANEL_SIGNER_STYLE = "input100";
	public static final String PANEL_SIGNER_CLIENT = "client";

	// application tree
	public static final String PANEL_APP_STYLE = "input100";
	public static final String PANEL_APP_CLIENT = "client";
	
	public static final String PDF_MIME = "application/pdf";
	public static final String HTML_MIME = "text/html";
	
	public static final String REPORT_PREFFIX = "report_";
	public static final String VISOR_PREFFIX = "visor_";
	public static final String SIGNATURE_SUFFIX = "_firmado";
	public static final String VISOR_TCN_PREFFIX = "TCNVisor_";
	public static final String VISOR_FACTURAE_PREFFIX = "facturae_";
	
	public static final String PDF_LOWER = "pdf";
	public static final String HTML_LOWER = "html";
	public static final String ZIP_LOWER = "zip";

	// Messages properties names
	/**
	 *  Contiene el nombre de la cadena para todos en un arbol de richfaces
	 *  El valor de esta constante es {@value}.
	 */
	public static final String MESSAGES_ALL = "allTree";

	// Request inbox
	/**
	 *  Contiene el nombre de la cadena para mensaje no resuelto
	 *  El valor de esta constante es {@value}.
	 */
	public static final String MESSAGES_UNRESOLVED = "unresolved";
	public static final String MESSAGES_AWAITING = "awaiting";
	public static final String MESSAGES_SIGNED = "signed";
	public static final String MESSAGES_REJECTED = "rejected";

	/**
	 *  Contiene la cadena para el mensaje finalizado.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String MESSAGES_FINISHED = "finished";
	/**
	 *  Contiene la cadena para el mensaje enviado.
	 *  El valor de esta constante es {@value}.
	 */
	public static final String MESSAGES_SENT = "sent";
	public static final String MESSAGES_REDACTION = "redaction";
	public static final String MESSAGES_SENT_FINISHED = "sentFinished";
	public static final String MESSAGES_EXPIRED = "expired";
	public static final String MESSAGES_CANCELLED = "cancelled";
	public static final String MESSAGES_SENT_CANCELLED ="sentCancelled";
	public static final String MESSAGES_GROUP_SENT = "groupSent";
	public static final String MESSAGES_GROUP_SENT_FINISHED = "groupSentFinished";
	public static final String MESSAGES_GROUP_EXPIRED = "groupExpired";
	public static final String MESSAGES_GROUP_CANCELLED = "groupCancelled";
	public static final String MESSAGES_DIFFUSION = "diffusion";
	public static final String MESSAGES_INVITED = "invited";
	
	/**
	 *  Contiene la cadena para el valor del 1.
	 *  El valor de esta constante es {@value}.
	 */
	public static Integer N_ONE = Integer.valueOf(1);
	/**
	 *  Contiene el nombre del directorio de plantillas 
	 *  El valor de esta constante es {@value}.
	 */
	public static final String TEMPLATE_FOLDER = "/templates";
	/**
	 *  Contiene el nombre del fichero de plantillas 
	 *  El valor de esta constante es {@value}.
	 */
	public static final String TEMPLATE_FILE_NAME = "mail_template.html";
	public static final String TEMPLATE_TITLE_VAR = "$$TITLE$$";
	public static final String TEMPLATE_DATE_VAR = "$$DATE$$";
	public static final String TEMPLATE_MESSAGE_VAR = "$$MESSAGE$$";
	public static final String TEMPLATE_NOREPLY_VAR = "$$NOREPLY$$";

	// THEME
	 /**
	 *  Valor de la cadena por defecto para el tema
     *  El valor de esta constante es {@value}.
     */
	public static final String THEME_DEFAULT = "default";
	public static final String THEME_SESSION = "session";
	public static final String THEME_USER = "user";
	public static final String THEME_FONTSIZE_DEFAULT = "11";
	 /**
	 *  Valor minimo de la fuente de un tema
     *  El valor de esta constante es {@value}.
     */
	public static final String THEME_FONTSIZE_MIN = "10";
	 /**
	 *  Valor maximo de la fuente de un tema
     *  El valor de esta constante es {@value}.
     */
	public static final String THEME_FONTSIZE_MAX = "20";
	// public static final String THEME_SKIN_DARKX = "darkX";
	// public static final String THEME_SKIN_BLUESKY = "blueSky";
	// public static final String THEME_SKIN_CLASSIC = "classic";
	// public static final String THEME_SKIN_WINE = "wine";
	// public static final String THEME_SKIN_DEEPMARINE = "deepMarine";
	// public static final String THEME_SKIN_EMERALTOWN = "emeraldTown";

	// REDACTION
	 /**
	 *  Mantiene el valor para filtrar 'todos' en un listado
     *  El valor de esta constante es {@value}.
     */
	public static final String SIGNER_FILTER_ALL = "filterAll";
	public static final String SIGNER_FILTER_PEOPLE = "filterPeople";
	public static final String SIGNER_FILTER_JOB = "filterJobs";
	public static final String SIGNER_FILTER_MOST_USED = "filterMostUsed";

	// ACTION
	public static final String ACTION_PARAM_STATE = "ESTADO";
	public static final String ACTION_PARAM_IDENTIFIER = "DNI";
	public static final String ACTION_PARAM_REQ_HASH = "HASHPET";
	public static final String ACTION_PARAM_DOC_HASH = "EHASH";
	public static final String ACTION_PARAM_EQUAL = "=";
	public static final String ACTION_PARAM_QUOTE = "'";
	public static final String ACTION_PARAM_POINTS = ":";

	// LOGIN LDAP
	public static final String LOGIN_LDAP_ID = "LOGIN.LDAP.IDENTIFICADOR";
	public static final String LOGIN_LDAP_IDATTRIBUTE = "USUARIO.LDAP.IDATRIBUTO";
	public static final String LOGIN_LDAP_URL = "LOGIN.LDAP.URL";
	public static final String LOGIN_LDAP_BASEDN = "LOGIN.LDAP.BASEDN";
	public static final String LOGIN_LDAP_DN = "LOGIN.LDAP.DN";
	public static final String LOGIN_LDAP_USER_VAR = "$1";
	public static final String LOGIN_LDAP_SEPARATOR = "\\|";

	// CONEXIÓN SERVICIOS SIM
	public static final String SIM_URL = "SIM.URL";
	public static final String SIM_ENVIO_URL = "SIM.ENVIO.URL";
	public static final String SIM_PROXY_URL = "SIM.PROXY.URL";
	public static final String SIM_USER = "SIM.USER";
	public static final String SIM_PASSWORD = "SIM.PASSWORD";
	public static final String SIM_SERVICE = "SIM.SERVICE";
	public static final String SIM_REGISTER = "SIM.REGISTER";

	// PROXY
	public static final String PROXY = "PROXY";
	public static final String PROXY_PASSWORD = "PROXY.CLAVE";
	public static final String PROXY_PORT = "PROXY.PUERTO";
	public static final String PROXY_SERVER = "PROXY.SERVIDOR";
	public static final String PROXY_USER = "PROXY.USUARIO";

	// TRUSTEDSTORE
	public static final String TRUSTSTORE_FILE = "TRUSTSTORE.FILE";
	public static final String TRUSTSTORE_PASSWORD = "TRUSTSTORE.PASSWORD";
	public static final String TRUSTSTORE_TYPE = "TRUSTSTORE.TYPE";
	
	// JOBS
	 /**
	 *  Mantiene el valor del par&aacute;metro de PREFIRMA
     *  El valor de esta constante es {@value}.
     */
	public static final String JOB_PRESIGN = "PREFIRMA";
	public static final String JOB_PRESIGN_EXPRESSION = "PREFIRMA.EXPRESION";
	public static final String JOB_ACTION = "ACTION";
	public static final String JOB_EXPIRED_REQUESTS = "EXPIRED_REQUESTS";
	public static final String JOB_ADVICE_SERVICE = "EXTERNALADVICE";
	public static final String JOB_DAILY_ADVICE_SERVICE = "DAILYEXTERNALADVICE";
	public static final String JOB_GENERATE_REPORTS = "GENERATE_REPORTS";
	public static final String JOB_ORGANISM_DIR3 = "JOB_ORGANISM_DIR3";
	public static final String JOB_LINEAFIRMA_AUTORIZACION = "JOB_LINEAFIRMA_AUTORIZACION";
	
	public static final String JOB_PRINCIPAL = "principalPF";
	
	public static final String JOB_REQUESTS_TO_HISTORIC= "REQUESTS_TO_HISTORIC";

	public static final String JOB_EXPIRED_REQUESTS_CRON_EXPRESSION = "0 0 0 * * ?";
	
	public static final String JOB_CLEAN_OLD_SESSIONS = "CLEAN_OLD_SESSIONS";

	public static final String JOB_CLEAN_OLD_SESSIONS_CRON_EXPRESSION = "0 0 0 * * ?";

	public static final String JOB_EXTERNAL_APP_EVERY_DAY = "0 0 0 * * ?";

	public static final String JOB_GENERATE_REPORTS_EVERY_DAY = "0 55 23 * * ?";
	
	public static final String JOB_DOCELWEB_CLEANER_EVERY_DAY = "0 0 1 * * ?";
	
	public static final String JOB_ORGANISM_DIR3_EVERY_DAY = "0 0 1 * * ?";
	
	public static final String JOB_LINEAFIRMA_AUTORIZACION_EVERY_DAY = "0 55 23 * * ?";
	//public static final String JOB_LINEAFIRMA_AUTORIZACION_EVERY_DAY = "2 30 9 * * ?";
	
	// Se dispara a las 2:00 de la noche todos los días
	public static final String JOB_REQUESTS_TO_HISTORIC_CRON_TRIGGER = "0 0 2 * * ?";
	
//	Ejecución cada minuto
//	public static final String JOB_DOCELWEB_CLEANER_EVERY_DAY = "0 0/1 * * * ?";

	// STATISTICS

	public static final String REQUESTS_BY_USER = "requests_user";
	public static final String REQUESTS_BY_SEAT = "requests_seat";
	public static final String REQUESTS_BY_DATE_USER_SEAT = "requests_date_user_seat";
	public static final String SIGNS_BY_USER = "signs_user";
	public static final String SIGNS_BY_SEAT = "signs_seat";
	public static final String SIGNS_BY_DATE_USER_SEAT = "signs_date_user_seat";
	public static final String SIGNS_AND_REQUESTS_BY_APP_SEAT = "signs_requests_app_seat";
	public static final String SIGNS_AND_REQUESTS_BY_APP_SEAT_DATE = "signs_requests_app_seat_date";
	public static final String SIGNS_BY_APP_SEAT_DATE = "signs_app_seat_date";
	public static final String SIGNS_BY_APP_SEAT = "signs_app_seat";
	public static final String REQUESTS_BY_APP_SEAT_DATE = "requests_app_seat_date";
	public static final String REQUESTS_BY_APP_SEAT = "requests_app_seat";
	public static final String USERS_BY_SEAT = "users_seat";

	public static final String USER = "Usuario";
	public static final String SEAT = "Sede";
	public static final String APPLICATION = "Aplicacion";
	public static final String MONTH = "Mes";
	public static final String YEAR = "Año";
	public static final String N_REQUESTS = "Peticiones";
	public static final String N_SIGNS    = "Firmas";
	public static final String N_USERS    = "Usuarios";
	
	public static final String REQUESTS = "requests";
	public static final String SIGNATURES = "signatures";
	
	public static String XU = "xu";
	public static String XS = "xs";
	public static String XSU = "xsu";
	public static String XSUF = "xsuf";
	public static String XSA = "xsa";
	public static String XSAF = "xsaf";
	

	/** CLAVES PARA SOLICITAR LA DOCUMENTACIÓN **/

	public static final String NAME_USER_HANDBOOK = "Manual de usuario";
	public static final String NAME_ADM_HANDBOOK = "Manual de administrador";
	public static final String NAME_SEAT_HANDBOOK = "Manual de administrador de sedes";
	public static final String NAME_GUIA_RAPIDA_HANDBOOK = "Guía rápida para usuarios del Portafirmas Electrónico";
	
	public static final String ID_USER_HANDBOOK = "user";
	public static final String ID_ADM_HANDBOOK = "admin";
	public static final String ID_ADM_SEAT_HANDBOOK = "admin_seat";
	public static final String ID_GUIA_RAPIDA_HANDBOOK = "guia_rapida";

	 /**
	 *  Mantiene el valor del nombre del job de limpieza temporal.
     *  El valor de esta constante es {@value}.
     */
	public static final String JOB_CLEANUP_TEMP = "LIMPIEZA_TEMPORAL";
	 /**
	 *  Mantiene el valor de los milisegundos de un d&iacute;a.
     *  El valor de esta constante es {@value}.
     */
	public static final Long MILISEC_BY_DAY = 24 * 60 * 60 * 1000L;

	// signers tree
	public static final String SIGNERS_TREE_ICON_JOB_PATH = "/theme/img/medal_silver.png";
	public static final String SIGNERS_TREE_ICON_USER_PATH = "/theme/img/login.gif";

	// SQL TRANSLATE
	private static final String TRANSLATE_TARGET_SQL = "AEIOUNCAEIOU";
	private static final String TRANSLATE_SOURCE_SQL = "Ã�Ã‰Ã�Ã“ÃšÃ‘Ã‡Ã„Ã‹Ã�Ã–Ãœ";
	public static final String TRANSLATE_SQL_END = "),'"
			+ Constants.TRANSLATE_SOURCE_SQL + "','"
			+ Constants.TRANSLATE_TARGET_SQL + "')) like '%'||:search||'%' ";
	public static final String TRANSLATE_SQL_BEGIN = "(translate(upper(";

	// JAVA TRANSLATE
	public static final String TRANSLATE_TARGET_A = "A";
	public static final String TRANSLATE_SOURCE_AT = "Ã�";
	public static final String TRANSLATE_SOURCE_AD = "Ã„";
	public static final String TRANSLATE_TARGET_E = "E";
	public static final String TRANSLATE_SOURCE_ET = "Ã‰";
	public static final String TRANSLATE_SOURCE_ED = "Ã‹";
	public static final String TRANSLATE_TARGET_I = "I";
	public static final String TRANSLATE_SOURCE_IT = "Ã�";
	public static final String TRANSLATE_SOURCE_ID = "Ã�";
	public static final String TRANSLATE_TARGET_O = "O";
	public static final String TRANSLATE_SOURCE_OT = "Ã“";
	public static final String TRANSLATE_SOURCE_OD = "Ã–";
	public static final String TRANSLATE_TARGET_U = "U";
	public static final String TRANSLATE_SOURCE_UT = "Ãš";
	public static final String TRANSLATE_SOURCE_UD = "Ãœ";
	public static final String TRANSLATE_TARGET_N = "N";
	public static final String TRANSLATE_SOURCE_N = "Ã‘";
	public static final String TRANSLATE_TARGET_C = "C";
	public static final String TRANSLATE_SOURCE_C = "Ã‡";

	// BYTE VALUE
	public static final int BYTE_VALUE = 1048576;

	// panel total values
	 /**
	 *  Mantiene el valor del panel de administraci&oacute;n
     *  El valor de esta constante es {@value}.
     */
	public static final String PANEL_ADM = "ADM";
	public static final String PANEL_CONF = "CONF";
	 /**
	 *  Mantiene el valor del panel de redacci&oacute;n
     *  El valor de esta constante es {@value}.
     */
	public static final String PANEL_REDACT = "REDACT";
	 /**
	 *  Mantiene el valor del panel inbox.
     *  El valor de esta constante es {@value}.
     */
	public static final String PANEL_INBOX = "INBOX";
	 /**
	 *  Mantiene el valor del panel de petici&oacute;n
     *  El valor de esta constante es {@value}.
     */
	public static final String PANEL_REQ = "REQUEST";

	// CONSTANTES PARA SERVICIOS DE AMPLIACIÓN DE FIRMA DSS //

	public static String UPGRADE_TIMESTAMP = "TIMESTAMP";

	// BBDD POSTGRES
	public static final String BBDD_POSTGRES = "postgres";

	public static final String UPLOAD_TEMP_EXTENSION = ".upload";
	
	// Authorizations state
	public static final String AUTHORIZATIONS_UNRESOLVED = "unresolvedAuthorization";
	public static final String AUTHORIZATIONS_ACCEPTED = "accepted";
	public static final String AUTHORIZATIONS_REVOKED = "revoked";

	// VALIDAR USUARIO PARAMETRO
	public static final String C_PARAMETER_USUARIO_NO_VALIDAR = "USUARIO.FIRMA.NOVALIDAR";

	public static final int RESPONSE_FORBIDDEN = 403;

	public static final String CHECKED = "ON";

	public static final String USUARIO_PASSWORD = "USUARIO.PASSWORD";

	public static final String USUARIO_LDAP_IDATRIBUTO = "USUARIO.LDAP.IDATRIBUTO";

	public static final int FIRST_YEAR = 2010;
	
	// Constantes para el uso de Cl@ve
    public static final String PROPERTY_URL_CLAVE = "clave.service.url";
    public static final String PROPERTY_SP_URL_CLAVE = "sp.url";
    public static final String PROPERTY_SP_RETURN_URL_CLAVE = "sp.return";
    public static final String PROPERTY_EXCLUDED_IDPLIST = "clave.service.excludedIdPList";
    public static final String PROPERTY_FORCED_IDP = "clave.service.forcedIdP";
    public static final String PROPERTY_IDENTIFICADOR = "attribute1.name";
    public static final String ATRIBUTO_EXCLUDED_IDPLIST = "excludedIdPList";
    public static final String ATRIBUTO_FORCED_IDP = "forcedIdP";
    public static final String ATRIBUTO_SAML_REQUEST = "SAMLRequest";
    public static final String ATRIBUTO_SAML_RESPONSE = "SAMLResponse";
    
    public static final String CREDENTIALS_ERROR_GENERATE_SAML = "Error generando petición de acceso a Cl@ve";
    
	public static final String TYPE_CERTIFICATE_PADES = "PAdEStri";
	public static final String TYPE_CERTIFICATE_XADES_DETACHED = "XAdEStri Detached";
	public static final String TYPE_CERTIFICATE_XADES_ENVELOPED = "XAdEStri Enveloped";
	public static final String TYPE_CERTIFICATE_CADES = "CAdEStri";

	// Constantes para el uso de Autentica
	public static final String AUTENTICA_ATRIBUTO_SAML_RESPONSE = "SAML_RESPONSE";
	public static final String AUTENTICA_ID = "id";
	public static final String AUTENTICA_USER_ATTRIBUTE_VALUE = "saml:AttributeValue";
	public static final String AUTENTICA_SAML_RESULTADO = "resultado";
	
	// GINSIDE
	public static final String C_PARAMETER_GINSIDE_URL = "GINSIDE.URL";
	public static final String C_PARAMETER_GINSIDE_USER = "GINSIDE.USER";
	public static final String C_PARAMETER_GINSIDE_PASSWORD = "GINSIDE.PASSWORD";
	public static final String C_PARAMETER_GINSIDE_PASSWORD_TYPE = "GINSIDE.PASSWORD.TYPE";
	
	public static final String REGULACION_CSV ="BOE-A-2014-3729";
	
	public static final String SCOPE_MESSAGE_ALL = "1";
	public static final String SCOPE_MESSAGE_PROVINCE = "2";
	public static final String SCOPE_MESSAGE_USER = "3";
	
	public static final String RESIGN_REQUEST_VAR_SEPARATOR = ";";
	public static final String RESIGN_FORMAT_PADES_LTV = "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:LTV";
	public static final String RESIGN_FORMAT_X_L = "urn:oasis:names:tc:dss:1.0:profiles:AdES:forms:ES-X-L";
	public static final String RESIGN_VALID_DATE = "31/12/9999";
	
	//jobs
	
	public static final String C_PARAMETER_JOBS_JOB_HISTORICO = "JOB.HISTORICO";
	
	
	public static enum APPLICATION_MODE {
		ACTUAL(0),
		HITORIC(1);
		
		private final int value;
		
		private APPLICATION_MODE(int value) {
	        this.value = value;
	    }

		public int getValue() {
	        return value;
	    }
	};
	
	public static final String[] LIST_BOOLEAN_VALUES = {C_YES, C_NOT};
	
	// Metadatos ENI
	public static final String METADATO_ENI_VERSION_NTI = "VersionNTI";
	public static final String METADATO_ENI_IDENTIFICADOR = "Identificador";
	public static final String METADATO_ENI_ORGANO = "Organo";
	public static final String METADATO_ENI_ORIGEN = "OrigenCiudadanoAdministracion";
	public static final String METADATO_ENI_ESTADO_ELABORACION = "EstadoElaboracion";
	public static final String METADATO_ENI_TIPO_DOCUMENTAL = "TipoDocumental";
	
	public static final String FILE_ENI_PREFIX = "ENI_";
	

	public static final String SERVICIOS_CENTRALES = "Servicios Centrales";
	
	// Valores para el combo de meses en el filtro de peticiones
	public static final String FILTRO_MESES_ULTIMAS_24_HORAS = "last24Hours";
	public static final String FILTRO_MESES_ULTIMA_SEMANA = "lastWeek";
	public static final String FILTRO_MESES_ULTIMO_MES = "lastMonth";
	public static final String FILTRO_MESES_TODAS = "all";
	
	public static final String QUERY_PFIRMA_GENERAL_PARAM = "request.genericParamValue";
	
	public static final String QUERY_PARAM_CONF_VALUE = "tvalor";
	
	public static final String MERLIN_TRUSTSTORE_FILE = "org.apache.ws.security.crypto.merlin.truststore.file";

	public static final String MERLIN_KEYSTORE_FILE = "org.apache.ws.security.crypto.merlin.keystore.file";
	public static final Object MERLIN_KEYSTORE_ALIAS = "org.apache.ws.security.crypto.merlin.keystore.alias";
	public static final Object MERLIN_KEYSTORE_PRIVATE_PASSWORD = "org.apache.ws.security.crypto.merlin.keystore.private.password";

	public static final String MINIAPPLET_SIGN_FORMAT_PDF = "Adobe PDF";
	
	public static final String FACTURAE_CODE_CONF = "FACTURAE";
	public static final String FACTURAE_FIRMA_SIGNATURE_FORMAT = "FacturaE";
	
	public static final String MOBILE_PROXY_URL = "PROXYURL";
	public static final String MOBILE_DNI = "DNI";
	public static final String MOBILE_SEPARATOR = "$$";
	public static final int    MOBILE_TIPO_PET_ENTRANTE = 1;
	
	public static final String FILTRO_VALIDADOR_MOVIL_ACTIVO = "FILTRO.VALIDADOR.MOVIL.ACTIVO";

	public static final String IG_RUTA_HTML = "interfazGenerica/";
	
	public static final Short SOAP_12 = 2;

	
	//CUSTODIA
	public static final String DONDE_SE_CUSTODIAN_LOS_INFOMES= "CUSTODIA.INFORMES";
	public static final String DONDE_SE_CUSTODIAN_LAS_FIRMAS= "CUSTODIA.FIRMAS";
	public static final String DONDE_SE_CUSTODIAN_LOS_DOCUMENTOS= "CUSTODIA.DOCUMENTOS";
	
	private static final String INFORMES = "INFORMES";
	private static final String FIRMAS = "FIRMAS";
	private static final String DOCUMENTOS = "DOCUMENTOS";
	
	public static final String ENI_MIMETYPE_XML = "application/xml";
	
	public static enum tipoDocumentoACustodiar
	{
		INFORMES, FIRMAS, DOCUMENTOS
	}
		
}
