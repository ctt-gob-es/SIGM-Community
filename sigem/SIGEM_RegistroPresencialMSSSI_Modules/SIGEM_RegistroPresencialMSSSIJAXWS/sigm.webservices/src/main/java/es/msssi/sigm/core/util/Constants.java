/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;

import es.msssi.sgm.registropresencial.beans.WebParameter;

/**
 * Contiene las constantes utilizadas en los servicios.
 * 
 * @author jortizs
 */
public class Constants {
	
	public static String PATH_REPO = null;
	
	static {
		PATH_REPO = (String) WebParameter.getEntryParameter("PATH_REPO");
	}

    public static Integer MAX_REPORT_REGISTER = new Integer(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_MAX_REPORT_REGISTERS));
	
	
    // CARACTERES Y SÍMBOLOS
    /** Número cero '0'. */
    public static final String ZERO_CHAR = "0";
    /** Caracter espacio ' '. */
    public static final String SPACE_CHAR = " ";
    /** Caracter espacio en HTML '%20'. */
    public static final String SPACE_CHAR_HTML = "%20";
    /** Caracter y '&'. */
    public static final String AMPERSAND_CHAR = "&";
    /** Caracter '&' escapado en XML. */
    public static final String AMPERSAND_XML_ESCAPED_CHAR = "&amp;";
    /** Caracter barra '/'. */
    public static final String SLASH_CHAR = "/";
    /** Caracter barra vertical '/'. */
    public static final String VERTICAL_BAR_CHAR = "|";
    /** Caracter dos puntos ':'. */
    public static final String COLON_CHAR = ":";
    /** Caracter guión '-'. */
    public static final String HYPHEN_CHAR = "-";
    /** Caracter guión '.'. */
    public static final String POINT_CHAR = ".";
    /** Caracter guión bajo '_'. */
    public static final String UNDERSCORE_CHAR = "_";
    /** Caracter menor que '<'. */
    public static final String LESS_THAN_CHAR = "<";
    /** Caracter mayor que '>'. */
    public static final String GREATER_THAN_CHAR = ">";
    /** Caracter paréntesis izquierdo '('. */
    public static final String LEFT_PARNT_BRACKET_CHAR = "(";
    /** Caracter paréntesis derecho ')'. */
    public static final String RIGHT_PARNT_BRACKET_CHAR = ")";
    /** Caracter corchete izquierdo '['. */
    public static final String LEFT_CLASP_BRACKET_CHAR = "[";
    /** Caracter corchete derecho ']'. */
    public static final String RIGHT_CLASP_BRACKET_CHAR = "]";

    /** Caracter asterisco '*'. */
    public static final String ASTERISK_CHAR = "*";
    /** Caracter igual '='. */
    public static final String EQUAL_CHAR = "=";
    /** Caracter comillas '"'. */
    public static final String QUOTATION_MARK_CHAR = "\"";
    /** Representación del caracter salto de línea '\n'. */
    public static final String LINE_BREAK_REP_CHAR = "\\n";
    /** Representación del caracter retorno de carro '\r'. */
    public static final String CARRIAGE_RETURN_REP_CHAR = "\\r";
    /** Representación del caracter tabulador '\t'. */
    public static final String TAB_REP_CHAR = "\\t";
    /** Representación del caracter espacio '\s'. */
    public static final String SPACE_REP_CHAR = "\\s";

    /** Caracteres disponibles en código Hexadecimal '0123456789ABCDEFabcdef'. */
    public static final String HEX_CODE_CHARS = "0123456789ABCDEFabcdef";

    /** Caracteres de inicio de cabecera XML '<?'. */
    public static final String XML_HEADER_INIT_TAG = "<?";
    /** Caracteres de fin de cabecera XML '?>'. */
    public static final String XML_HEADER_END_TAG = "?>";

    // ENCODINGS, ALGORITMOS DE CIFRADO, PROVEEDORES Y CÓDIGOS DE IDIOMAS
    /** Código local de español. */
    public static final String ESP_LOCALE = "es";
    /** Encoding ISO-8859-1. */
    public static final String ISO_8859_1_ENCODING = "ISO-8859-1";
    /** Encoding UTF-8. */
    public static final String UTF_8_ENCODING = "UTF-8";
    /** Encoding UTF-16LE. */
    public static final String UTF_16LE_ENCODING = "UTF-16LE";
    
    /**
     * Encoding UTF8. Similar a UTF-8, utilizado en sistemas que no permiten
     * guiones.
     */
    public static final String UTF8_ENCODING = "UTF8";
    /** Encoding "UTF_32BE. */
    public static final String UTF_32BE_ENCODING = "UTF_32BE";
    /** Encoding UTF_32LE. */
    public static final String UTF_32LE_ENCODING = "UTF_32LE";
    /** Encoding ASCII. */
    public static final String ASCII_ENCODING = "ASCII";
    /** Encoding CP037. */
    public static final String CP037_ENCODING = "CP037";
    /** Encoding UnicodeBigUnmarked. */
    public static final String UNICODE_BIG_UNMARKED_ENCODING = "UnicodeBigUnmarked";
    /** Encoding UnicodeLittleUnmarked. */
    public static final String UNICODE_LITTLE_UNMARKED_ENCODING = "UnicodeLittleUnmarked";

    /** Algoritmo de cifrado MD5. */
    public static final String MD5_ALGORITHM = "MD5";
    /** Algoritmo de cifrado MD5. */
    public static final String RC4_ALGORITHM = "RC4";

    /** Proveedor BC BouncyCastle. */
    public static final String BC_PROVIDER = "BC";

    // FORMATEOS DE DATOS
    /** Formateo de fecha 'yyyy-MM-dd hh:mm:ss.fff'. */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    /** Nivel de indentado al formatear un documento. */
    public static final int INDENTING_LEVEL = 2;
    /** Caracteres máximos por línea al formatear un documento. */
    public static final int LINE_LEVEL_WIDTH = 65;
    /** Tamaño máximo del buffer de detección del encoding del documento. */
    public static final int ENCODING_DETECTION_BUFFER_SIZE = 400;
    /** Taño máximo de un número de registro sin el año. */
    public static final int REGISTER_MAX_LENGTH_WITHOUT_YEAR = 8;

    // SIGM
    /** Entidad de SIGM. */
    public static final String SIGM_ENTITY = "001";
    /** Asunto genérico para alta de registros. */
    public static final String SIGM_GENERIC_ASUNT_TYPE = "TGEN";
    /** Asunto genérico para alta de registros. */
    public static final String SIGM_GENERIC_ASUNT = "ASUNTO GENÉRICO";
    /** Nombre de la oficina de registro electrónico. */
    public static final String SIGM_GENERIC_REGISTER_OFFICE_NAME =
	    "OFICINA DE REGISTRO ELECTRÓNICO";
    /** Código de la oficina de registro electrónico. */
    public static final String SIGM_GENERIC_REGISTER_OFFICE_COD = "999";
    /** Id del libro de entrada. */
    public static final Integer ID_LIBRO_ENTRADA = 6;
    /** Id del libro de salida. */
    public static final Integer ID_LIBRO_SALIDA = 7;
    /** Código de tipo de registro de entrada. */
    public static final String INPUT_REGISTER_TYPE_CODE = "E";
    /** Código de tipo de registro de entrada. */
    public static final String OUTPUT_REGISTER_TYPE_CODE = "S";
    /** Código de tipo de interesado (persona física). */
    public static final String PHYSICAL_INTERESTED_TYPE_CODE = "F";
    /** Código de tipo de interesado (persona física) para SIGM. */
    public static final String SIGM_PHYSICAL_INTERESTED_TYPE_CODE = "P";
    /** Código de tipo de documento de interesado (persona física) para SIGM. */
    public static final String SIGM_PHYSICAL_INTERESTED_TYPE_DOC_CODE = "2";
    /** Código de tipo de interesado (persona jurídica). */
    public static final String JURIDICAL_INTERESTED_TYPE_CODE = "J";
    /** Código de tipo de documento de interesado (persona jurídica). */
    public static final String JURIDICAL_INTERESTED_TYPE_DOC_CODE = "1";

    // UTILES
    /** Nombre de las peticiones que se guardan en disco. */
    public static final String REQUEST_FILE_NAME = "SIGMWS_REQ";
    
    /** Extensión de documentos xml. */
    public static final String XML_EXTENSION = ".xml";
    /** Tipo de documento xml. */
    public static final String XML_DOC_TYPE = "xml";
    /** Constante sobre indentado de documentos xml. */
    public static final String XML_DOC_INDENT_URL = "{http://xml.apache.org/xslt}indent-amount";
    /** Constante positiva para un valor de configuración de xml 'yes'. */
    public static final String XML_PROP_YES = "yes";
    /** Constante positiva para un valor de configuración de xml 'no'. */
    public static final String XML_PROP_NO = "no";
        
    /**
     * Milisegundos para añadir a fechas que no los lleven y evitar errores
     * posteriores de parseo.
     */
    public static final String MILLISECONDS = ".000";
    /** Número máximo de registros a devolver en una consulta múltiple. */
    public static final int MAX_REGISTERS_RESULT_MULTIPLE_QUERY = 200;
    // SQL
    /** Constante donde guardar el documento XML de las consultas. */
    public static final String XML_DOCUMENT = "xmlDocument";
    /** Constante donde guardar el id interno del registro de las consultas. */
    public static final String REGISTER_ID = "registerId";
    /** Constante donde guardar el número interno del registro de las consultas. */
    public static final String REGISTER_NUMBER = "registerNumber";
    /** Constante donde guardar el la fecha del registro de las consultas. */
    public static final String REGISTER_DATE = "registerDate";

    // ARCHIVOS DE PROPIEDADES
    /** Nombre del archivo de propiedades 'crypto.properties'. */
    public static final String CRYPTO_PROPS_NAME = "crypto.properties";
    
    /** Nombre de la propiedad de aplicaciones que verifican la firma. */
    public static final String APLICACIONES_VERIFICA_PROP = "APLICACIONES_VERIFICA";
    /** Nombre de la propiedad de aplicaciones que firman con un CDATA y no se pueden validar. */
    public static final String APLICACIONES_CDATA_PROP = "APLICACIONES_CDATA";
    
    // LITERALES
    /** Literal de lista de resultados. **/
    public static final String RESULTS_LIST = "listaResultados";

    // NODOS DE LAS PETICIONES
    /** Constante correspondiente al nodo registrar. */
    public static final String NODO_REGISTRAR = "registrar";
    /** Constante correspondiente al nodo aplicacion. */
    public static final String NODO_APLICACION = "aplicacion";
    /** Constante correspondiente al nodo tipo. */
    public static final String NODO_TIPO = "tipo";
    /** Constante correspondiente al nodo oficina. */
    public static final String NODO_CD_OFICINA = "oficina";
    /** Constante correspondiente al nodo organoorigen. */
    public static final String NODO_ORGANO_ORIGEN = "organoorigen";
    /** Constante correspondiente al nodo remitente. */
    public static final String NODO_REMITENTE = "remitente";
    /** Constante correspondiente al nodo asunto. */
    public static final String NODO_ASUNTO = "asunto";
    /** Constante correspondiente al nodo email. */
    public static final String NODO_EMAIL = "email";
    /** Constante correspondiente al nodo expediente. */
    public static final String NODO_NUM_EXP_EXT = "expediente";
    /** Constante correspondiente al nodo firma. */
    public static final String NODO_FIRMA = "firma";
    /** Constante correspondiente al nodo organodestino. */
    public static final String NODO_ORGANO_DESTINO = "organodestino";
    /** Constante correspondiente al nodo destinatario. */
    public static final String NODO_DESTINATARIO = "destinatario";
    /** Constante correspondiente al nodo ejercicio. */
    public static final String NODO_EJERCICIO = "ejercicio";
    /** Constante correspondiente al nodo numregistrooficina. */
    public static final String NODO_NUM_REG_OFICINA = "numregistrooficina";
    /** Constante correspondiente al nodo numregistrogeneral. */
    public static final String NODO_NUM_REG_GENERAL = "numregistrogeneral";
    /** Constante correspondiente al nodo peticionregistro. */
    public static final String NODO_PETICION = "peticionregistro";
    /** Constante correspondiente al nodo consultaregistro. */
    public static final String NODO_CONSULTA = "consultaregistro";
    /** Constante correspondiente al nodo respuestaregistro. */
    public static final String NODO_RESPUESTA = "respuestaregistro";
    /** Constante correspondiente al nodo resultadoconsulta. */
    public static final String NODO_RES_CONSULTA = "resultadoconsulta";
    /** Constante correspondiente al nodo registro. */
    public static final String NODO_REGISTRO = "registro";
    /** Constante correspondiente al nodo datosrecibidos. */
    public static final String NODO_DATOS_RECIBIDOS = "datosrecibidos";
    /** Constante correspondiente al nodo fecharegistro. */
    public static final String NODO_FECHA_REGISTRO = "fecharegistro";
    /** Constante correspondiente al nodo codigorespuesta. */
    public static final String NODO_CODIGO_RESPUESTA = "codigorespuesta";
    /** Constante correspondiente al nodo fechaconsulta. */
    public static final String NODO_FECHA_CONSULTA = "fechaconsulta";
    /** Constante correspondiente al nodo divisionorigen. */
    public static final String NODO_DIV_ORIGEN = "divisionorigen";
    /** Constante correspondiente al nodo divisiondestino. */
    public static final String NODO_DIV_DESTINO = "divisiondestino";
    /** Constante correspondiente al nodo departamentodestino. */
    public static final String NODO_DEP_DESTINO = "departamentodestino";
    /** Constante correspondiente al nodo departamentoorigen. */
    public static final String NODO_DEP_ORIGEN = "departamentoorigen";
    /** Constante correspondiente al nodo descripcion. */
    public static final String NODO_DESC = "descripcion";
    /** Constante correspondiente al nodo representante. */
    public static final String NODO_REPRESENTANTE = "representante";
    /** Constante correspondiente al nodo codPeticion. */
    public static final String NODO_COD_PETICION = "codPeticion";
    /** Constante correspondiente al nodo firmaFichero. */
    public static final String NODO_FIRMA_FICHEROS = "firmaFichero";
    /** Constante correspondiente al nodo numFicheros. */
    public static final String NODO_NUM_FICHEROS = "numFicheros";
    /** Constante correspondiente al nodo ficheroPeticion. */
    public static final String NODO_FICHERO_PETICION = "ficheroPeticion";
    /** Constante correspondiente al nodo datosAnexos. */
    public static final String NODO_DATOS_ANEXOS = "datosAnexos";
    /** Constante correspondiente al nodo tipotransporte. */
    public static final String NODO_TIP_TRANSPORTE = "tipotransporte";
    /** Constante correspondiente al nodo numregistroaplicacion. */
    public static final String NODO_NUM_REG_AP = "numregistroaplicacion";
    /** Constante correspondiente al nodo fechahora. */
    public static final String NODO_FECHA_HORA_AP = "fechahora";
    /** Constante correspondiente al nodo registrarReturn. */
    public static final String NODO_RESPUESTA_DOTNET = "registrarReturn";
    /** Constante correspondiente al nodo consultarReturn. */
    public static final String NODO_RES_CONSULTA_DOTNET = "consultarReturn";
    /** Constante correspondiente al nodo consultarReturn. */
    public static final String NODO_FECHA_DESDE = "fechadesde";
    /** Constante correspondiente al nodo consultarReturn. */
    public static final String NODO_FECHA_HASTA = "fechahasta";

    // NODOS DE LAS PETICIONES CORRESPONDIENTES CON INTERESADOS
    /** Constante correspondiente al nodo tipoPersona. */
    public static final String NODO_TIPO_PERSONA = "tipoPersona";
    /** Constante correspondiente al nodo RazonSocialInteresado. */
    public static final String NODO_RAZON_SOCIAL_INT = "RazonSocialInteresado";
    /** Constante correspondiente al nodo CIFInteresado. */
    public static final String NODO_CIF_INT = "CIFInteresado";
    /** Constante correspondiente al nodo DNIInteresado. */
    public static final String NODO_DNI_INT = "DNIInteresado";
    /** Constante correspondiente al nodo NombreInteresado. */
    public static final String NODO_NOMBRE_INT = "NombreInteresado";
    /** Constante correspondiente al nodo TelefonoInteresado. */
    public static final String NODO_TELEFONO_INT = "TelefonoInteresado";
    /** Constante correspondiente al nodo DomicilioInteresado. */
    public static final String NODO_DOMICILIO_INT = "DomicilioInteresado";
    /** Constante correspondiente al nodo PoblacionInteresado. */
    public static final String NODO_POBLACION_INT = "PoblacionInteresado";
    /** Constante correspondiente al nodo ProvinciaInteresado. */
    public static final String NODO_PROVINCIA_INT = "ProvinciaInteresado";
    /** Constante correspondiente al nodo CPInteresado. */
    public static final String NODO_CP_INT = "CPInteresado";
    /** Constante correspondiente al nodo DNIRepresentante. */
    public static final String NODO_DNI_REP = "DNIRepresentante";
    /** Constante correspondiente al nodo NombreRepresentante. */
    public static final String NODO_NOMBRE_REP = "NombreRepresentante";
    /** Constante correspondiente al nodo TelefonoRepresentante. */
    public static final String NODO_TELEFONO_REP = "TelefonoRepresentante";
    /** Constante correspondiente al nodo DomicilioRepresentante. */
    public static final String NODO_DOMICILIO_REP = "DomicilioRepresentante";
    /** Constante correspondiente al nodo PoblacionRepresentante. */
    public static final String NODO_POBLACION_REP = "PoblacionRepresentante";
    /** Constante correspondiente al nodo ProvinciaRepresentante. */
    public static final String NODO_PROVINCIA_REP = "ProvinciaRepresentante";
    /** Constante correspondiente al nodo CPRepresentante. */
    public static final String NODO_CP_REP = "CPRepresentante";

    // ATRIBUTOS DE NODOS
    /** Constante correspondiente al atributo xmlns. */
    public static final String ATRIBUTO_NAMESPACE = "xmlns";
    /** Constante correspondiente al atributo encoding. */
    public static final String ATRIBUTO_ENCODING = "encoding";

    // CONSTANTES DE DATOS
    /** Constante correspondiente al código de la oficina. */
    public static final String CD_OFICINA = "CD_Oficina";
    /** Constante correspondiente al nombre de la oficina. */
    public static final String DS_OFICINA = "DS_Oficina";
    /** Constante correspondiente al número de egistro. */
    public static final String NUM_REGISTRO = "Num_Registro";
    /** Constante correspondiente al ejercicio. */
    public static final String EJERCICIO = "Ejercicio";

    // CÓDIGOS DE ERROR
    /** Constante correspondiente al código de error 000. */
    public static final String CD_OK = "000";
    /** Constante correspondiente al código de error 001. */
    public static final String CD_ERROR_REGISTRO = "001";
    /** Constante correspondiente al código de error 002. */
    public static final String CD_ERROR_VALIDACION_SCHEMA = "002";
    /** Constante correspondiente al código de error 003. */
    public static final String CD_ERROR_VALIDACION_FIRMA = "003";
    /** Constante correspondiente al código de error 004. */
    public static final String CD_ERROR_FIRMA_DATOS = "004";
    /** Constante correspondiente al código de error 005. */
    public static final String CD_ERROR_AXIS = "005";
    /** Constante correspondiente al código de error 006. */
    public static final String CD_ERROR_SSL = "006";
    /** Constante correspondiente al código de error 007. */
    public static final String CD_ERROR_GENERAL = "007";
    /** Constante correspondiente al código de error 008. */
    public static final String CD_ERROR_DATOS_ESPERADOS = "008";
    /** Constante correspondiente al código de error 009. */
    public static final String CD_ERROR_FORMATO_PETICION = "009";
    /** Constante correspondiente al código de error 010. */
    public static final String CD_ERROR_DB = "010";
    /** Constante correspondiente al código de error 011. */
    public static final String CD_ERROR_LIMITE = "011";

    // MENSAJES
    /**
     * Constante correspondiente al mensaje: La insercion en el Registro se ha
     * realizado correctamente.
     */
    public static final String MSG_OK =
	    "La insercion en el Registro se ha realizado correctamente.";
    /**
     * Constante correspondiente al mensaje: No se ha encontrado ningun registro
     * con los datos recibidos.
     */
    public static final String MSG_NO_DATA_FOUND =
	    "No se ha encontrado ningun registro con los datos recibidos.";
    /**
     * Constante correspondiente al mensaje: La consulta ha devuelto mas de un
     * resultado.
     */
    public static final String MSG_TOO_MANY_ROWS = "La consulta ha devuelto mas de un resultado.";
    /** Constante correspondiente al mensaje: Consulta realizada. */
    public static final String MSG_OK_CONSULTA = "Consulta realizada.";

    // FORMULARIOS
    /** Constante correspondiente al valor de codFichero. */
    public static final String NODO_COD_FICHERO = "codFichero";
    /** Constante correspondiente al valor de Aplicacion. */
    public static final String APLICACION = "Aplicacion";
    /** Constante correspondiente al valor de Registro. */
    public static final String APP_NAME = "Registro";

    // PROPIEDADES CLIENTE
    /** Constante correspondiente a la propiedad SERVLET_PATH. */
    public static final String SERVLET_PATH = "SERVLET_PATH";
    /** Constante correspondiente a la propiedad ACCEPT_FILES. */
    public static final String ACCEPT_FILES = "ACCEPT_FILES";
    /** Constante correspondiente a la propiedad ONLYCERTIFICATECONEXION. */
    public static final String ONLYCERTIFICATECONEXION = "ONLYCERTIFICATECONEXION";

    // PROPIEDADES SERVIDOR
    /** Constante correspondiente a la propiedad ROOT_UPL_PATH. */
    public static final String ROOT_UPLOAD_PATH = "ROOT_UPL_PATH";
    /** Constante correspondiente a la propiedad DATASOURCE_JNDI. */
    public static final String DATASOURCE_JNDI = "DATASOURCE_JNDI";
    /** Constante correspondiente a la propiedad URL_AXIS. */
    public static final String URL_AXIS = "URL_AXIS";
    /** Constante correspondiente a la propiedad WS_SERVICE_NAME. */
    public static final String WS_SERVICE_NAME = "WS_SERVICE_NAME";

    // AUXILIARES
    /** Constante auxiliar con valor checkFirma. */
    public static final String CHECK_FIRMA = "checkFirma";
    /** Constante auxiliar con valor mensajeFirmado. */
    public static final String MENSAJE_FIRMADO = "mensajeFirmado";
    /** Constante auxiliar con valor usaCAPICOM. */
    public static final String AP_USA_CAPICOM = "usaCAPICOM";
    /** Constante auxiliar con valor lenguajeAP. */
    public static final String AP_LENGUAJE = "lenguajeAP";
    /** Constante auxiliar con valor CERT_STORE. */
    public static final String CERT_STORE = "CERT_STORE";
    /** Constante auxiliar con valor FILE_PATH. */
    public static final String FILE_PATH = "FILE_PATH";
    /** Constante auxiliar con valor CHECK_CRL. */
    public static final String CHECK_CRL = "CHECK_CRL";
    /** Constante auxiliar con valor SAVE_XML_REQUESTS. */
    public static final String SAVE_XML_REQUESTS = "SAVE_XML_REQUESTS";
    /** Constante auxiliar con valor N. */
    public static final String LENGUAJE_DOTNET = "N";
    /** Constante auxiliar con valor V. */
    public static final String LENGUAJE_VB6 = "V";
    /** Constante auxiliar con valor J. */
    public static final String LENGUAJE_JAVA = "J";
    /** Constante auxiliar con valor CA_FILE. */
    public static final String CA_FILE = "CA_FILE";
    /** Constante auxiliar con valor CA_CRL. */
    public static final String CA_CRL = "CA_CRL";
    /** Constante auxiliar con valor _LDAP_SERV. */
    public static final String LDAP_SERV = "_LDAP_SERV";
    /** Constante auxiliar con valor _LDAP_PORT. */
    public static final String LDAP_PORT = "_LDAP_PORT";
    /** Constante auxiliar con valor _LDAP_USER. */
    public static final String LDAP_USER = "_LDAP_USER";
    /** Constante auxiliar con valor _LDAP_PASS. */
    public static final String LDAP_PASS = "_LDAP_PASS";
    /** Constante auxiliar con valor FIRMA_ATACHED. */
    public static final String FIRMA_ATTACHED = "FIRMA_ATACHED";
    /** Constante auxiliar con valor REGISTRO_DATASOURCE. */
    public static final String REGISTRO_DATASOURCE = "REGISTRO_DATASOURCE";
    /** Constante auxiliar con valor BASE64. */
    public static final String BASE64 = "BASE64";
    /** Constante auxiliar con valor HEX. */
    public static final String HEX = "HEX";
    /** Constante auxiliar con valor 8. */
    public static final int DIGITOS_NUM_RG = 8;
    /** Constante auxiliar con valor 4. */
    public static final int DIGITOS_FILE_PATH = 4;

    // CONSTANTES DE PETICIONES
    /** Constante con valor My. */
    public static final String NAVIGATOR_STORE = "My";
    /** Constante con valor jcert.jar. */
    public static final String NAME_JAVA_FIRMA_1 = "jcert.jar";
    /** Constante con valor jnet.jar. */
    public static final String NAME_JAVA_FIRMA_2 = "jnet.jar";
    /** Constante con valor jsse.jar. */
    public static final String NAME_JAVA_FIRMA_3 = "jsse.jar";
    /** Constante con valor BRKNavegador.dll. */
    public static final String NAME_DLL_FIRMA = "BRKNavegador.dll";
    /** Constante con valor client.keystore. */
    public static final String NAME_KEY_STORE = "client.keystore";
    /** Constante con valor datosRegistro. */
    public static final String DATOS = "datosRegistro";
    /** Constante con valor certName. */
    public static final String CERT_NAME = "certName";
    /** Constante con valor operacion. */
    public static final String TIPO_PETICION = "operacion";
    /** Constante con valor datosFicheros. */
    public static final String DATOS_FICHEROS = "datosFicheros";
    /** Constante con valor cabecera. */
    public static final String CABECERA = "cabecera";
    /** Constante con valor fin. */
    public static final String FIN = "fin";
    /** Constante con valor checkEnvio. */
    public static final String ENVIAR = "checkEnvio";
    /** Constante con valor KEYS_PETICION. */
    public static final String KEYS_PETICION = "KEYS_PETICION";
    /** Constante con valor KEYS_REGISTRO. */
    public static final String KEYS_REGISTRO = "KEYS_REGISTRO";
    /** Constante con valor AP_GENERICA. */
    public static final String AP_GENERICA = "AP_GENERICA";
    /** Constante con valor APL1C4G3N. */
    public static final String CUSTOM_APP_NAME = "APL1C4G3N";

    // CONSTANTES UTILES
    /**
     * Indicador de posición 0 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_0 = 0;
    /**
     * Indicador de posición 1 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_1 = 1;
    /**
     * Indicador de posición 2 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_2 = 2;
    /**
     * Indicador de posición 3 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_3 = 3;
    /**
     * Indicador de posición 4 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_4 = 4;
    /**
     * Indicador de posición 5 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_5 = 5;
    /**
     * Indicador de posición 6 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_6 = 6;
    /**
     * Indicador de posición 7 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_7 = 7;
    /**
     * Indicador de posición 8 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_8 = 8;
    /**
     * Indicador de posición 9 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_9 = 9;
    /**
     * Indicador de posición 10 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_10 = 10;
    /**
     * Indicador de posición 11 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_11 = 11;
    /**
     * Indicador de posición 12 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_12 = 12;
    /**
     * Indicador de posición 13 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_13 = 13;
    /**
     * Indicador de posición 14 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_14 = 14;
    /**
     * Indicador de posición 15 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_15 = 15;
    /**
     * Indicador de posición 16 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_16 = 16;
    /**
     * Indicador de posición 17 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_17 = 17;
    /**
     * Indicador de posición 18 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_18 = 18;
    /**
     * Indicador de posición 19 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_19 = 19;
    /**
     * Indicador de posición 20 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_20 = 20;
    /**
     * Indicador de posición 21 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_21 = 21;
    /**
     * Indicador de posición 22 del campo para sentencias SQL, listas, arrays,
     * etc.
     */
    public static final int POS_INDEX_22 = 22;

    /** Constante para construcción de fechas: día 1 del mes. */
    public static final int FIRST_DAY_OF_MONTH = 1;
    /** Constante para construcción de fechas: primera hora del día. */
    public static final int FIRST_HOUR_OF_DAY = 0;
    /** Constante para construcción de fechas: primer minuto del día. */
    public static final int FIRST_MINUTE_OF_DAY = 0;
    /** Constante para construcción de fechas: primer segundo del día. */
    public static final int FIRST_SECOND_OF_DAY = 0;
    /** Constante para construcción de fechas: primer milisegundo del día. */
    public static final int FIRST_MILLISECOND_OF_DAY = 0;
    /** Constante para construcción de fechas: día 31 del mes. */
    public static final int LAST_DAY_OF_MONTH = 31;
    /** Constante para construcción de fechas: última hora del día. */
    public static final int LAST_HOUR_OF_DAY = 23;
    /** Constante para construcción de fechas: último minuto del día. */
    public static final int LAST_MINUTE_OF_DAY = 59;
    /** Constante para construcción de fechas: último segundo del día. */
    public static final int LAST_SECOND_OF_DAY = 59;
    /** Constante para construcción de fechas: último milisegundo del día. */
    public static final int LAST_MILLISECOND_OF_DAY = 999;

    /** Byte de posición 0x00. */
    public static final byte BYTE_POS_00 = 0x00;
    /** Byte de posición 0xFE. */
    public static final byte BYTE_POS_FE = (byte) 0xFE;
    /** Byte de posición 0xFF. */
    public static final byte BYTE_POS_FF = (byte) 0xFF;
    /** Byte de posición 0x3C. */
    public static final byte BYTE_POS_3C = 0x3C;
    /** Byte de posición 0x3F. */
    public static final byte BYTE_POS_3F = 0x3F;
    /** Byte de posición 0x78. */
    public static final byte BYTE_POS_78 = 0x78;
    /** Byte de posición 0x6D. */
    public static final byte BYTE_POS_6D = 0x6D;
    /** Byte de posición 0xBB. */
    public static final byte BYTE_POS_BB = (byte) 0xBB;
    /** Byte de posición 0xBF. */
    public static final byte BYTE_POS_BF = (byte) 0xBF;
    /** Byte de posición 0xEF. */
    public static final byte BYTE_POS_EF = (byte) 0xEF;
    /** Byte de posición 0x4C. */
    public static final byte BYTE_POS_4C = 0x4C;
    /** Byte de posición 0x94. */
    public static final byte BYTE_POS_94 = (byte) 0x94;
    /** Byte de posición 0xA7. */
    public static final byte BYTE_POS_A7 = (byte) 0xA7;
    /** Byte de posición 0x6F. */
    public static final byte BYTE_POS_6F = 0x6F;

    // CONSTANTES DE FIRMA
    /** Constante correspondiente al alias de la aplicación que invoca a PFE. */
    public static final String PFE_APP_ALIAS = "SIGM";
    /** Constante correspondiente al tipo de firma a realizar attached. */
    public static final String PFE_ATTACHMENT_TYPE_ATTACHED = "ATTACHED";
    /** Constante correspondiente al tipo de firma a realizar detached. */
    public static final String PFE_ATTACHMENT_TYPE_DETACHED = "DETACHED";
    /** Constante correspondiente al nombre del informe firmado. */
    public static final String PFE_SIGNED_DOC_NAME = "SignedReport.pdf";    
    /** Constante correspondiente al formato de firma. */
	public static final String PFE_SIGN_TYPE_PADES = "PAdES_BASIC";
    /** Constante correspondiente al formato de firma. */
    public static final String PFE_VERIFY_SIGN_TYPE = "CAdES";
    /** Constante correspondiente al formato de firma. */
    public static final String PFE_SIGN_TYPE = "CAdES_BES";
    /** Constante correspondiente a si se realiza una firma o varias. */
    public static final String PFE_MULTI_SIGN_TYPE = "SIMPLE";
    /** Constante que indica que debe comprobarse la CRL. */
    public static final String CHECK_CRL_PROPERTY_TRUE = "Y";
    /** Constante que indica que no debe comprobarse la CRL. */
    public static final String CHECK_CRL_PROPERTY_FALSE = "N";
    /** Constante de la ropiedad crl.appNombre. */
    public static final String CRL_APP_NAME_PROPERTY = "crl.appNombre";
    /** Constante de la propiedad crl.url. */
    public static final String CRL_URL_PROPERTY = "crl.url";
    /** Constante del nombre de fichero crypto.properties. */
    public static final String CRYPTO_PROPS_FILE_NAME = "crypto.properties";

    // CONSTANTES DE LOG
    /**
     * Fragmento de log que describe el valor del contenido de un nodo
     * decodificado.
     */
    public static final String DECODED_VALUE_LOG_MESSAGE = "Valor decodificado: ";
    /**
     * Fragmento de log que describe el valor del contenido de un nodo
     * decodificado.
     */
    public static final String ENCODED_VALUE_LOG_MESSAGE = "Valor codificado: ";
    /**
     * Fragmento de log que describe el nodo padre del nuevo documento de
     * respuesta.
     */
    public static final String NEW_PARENT_NODE_LOG_MESSAGE =
	    "Se crea el documento con nodo principal ";
    /**
     * Fragmento de log que describe un nuevo nodo en el documento de respuesta.
     */
    public static final String NEW_NODE_LOG_MESSAGE = "Se añade el nodo ";
    /**
     * Fragmento de log que describe el valor de un nuevo nodo en el documento
     * de respuesta.
     */
    public static final String NEW_NODE_VALUE_LOG_MESSAGE = " con valor ";
    /**
     * Fragmento de log que describe un nuevo nodo vacío en el documento de
     * respuesta.
     */
    public static final String NEW_NULL_NODE_VALUE_LOG_MESSAGE = " vacío";
    /** Constante correspondiente a si la firma es correcta. */
    public static final String SIGN_VERIFICATION_TRUE = "CORRECTO";
    /** Constante correspondiente a si la firma es incorrecta. */
    public static final String SIGN_VERIFICATION_FALSE = "FALLIDO";
    /** Constante correspondiente a la palabra 'parámetro'. */
    public static final String PARAMETER_STRING = "Parámetro";
    
    
    
    /** Constante que indica a partir de que valor es considerado un campo extendido en registro presencial MSSI. */
    public static final int FLDID_CAMPO_EXTENDIDO_RP_MSSSI = 600;
    
    public static final String SIGEM_USER = "sigem";
    public static final String SIGEM_PASSWORD = "sigem";
    public static final String SIGEM_LOCALE = "es";
    public static final String SIGEM_ENTIDAD_MSSSI = "001";
	public static final String SIGEM_USER_REGISTRO_ELECTRONICO = "REGISTRO_ELECTRONICO";
	public static final String SIGEM_USER_TEST_ECM = "ECM";
	public static final String SIGEM_PASSWORD_TEST_ECM = "ECM";
    
    
	public static final String FLD1_FIELD = "fld1";
	
	public static final String REGISTRO_ENTRADA = "E";
	public static final String REGISTRO_SALIDA = "S";
    public static final String REGISTRO_ENTRADA_TEXT = "ENTRADA";
    public static final String REGISTRO_SALIDA_TEXT = "ENTRADA";
	public static final int REGISTRO_ENTRADA_BBDD = 1;
	public static final int REGISTRO_SALIDA_BBDD = 2;
	public static final int REGISTRO_ELECTRONICO_ENTRADA = 6;
	public static final int REGISTRO_ELECTRONICO_SALIDA = 7;
	public static final int REPORT_OPTION_0 = 0;
	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_ERROR = "ERROR";
	public static final String SI = "SI";
	public static final String NO = "NO";

	
    public static final String DATE_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_ACUSE_FORMAT_LONG = "dd-MM-yyyy HH:mm:ss";        
    public static final String DATE_FORMAT_SHORT = "dd-MM-yyyy";
    
    public static final String TIPO_PERSONA_FISICA = "P";
    public static final String TIPO_PERSONA_JURIDICA = "J";
    
	public static final String XML_TAG_INFORME_RP = "INFORME_RP";
	public static final String XML_TAG_REGISTRO = "REGISTRO";
	public static final String XML_TAG_EXTENDIDOS = "EXTENDIDOS";
	public static final String XML_TAG_DOCUMENTOS = "DOCUMENTOS";
	public static final String XML_TAG_DOCUMENTO = "DOCUMENTO";
	public static final String XML_TAG_NOMBRE = "NOMBRE";
	public static final String XML_TAG_HASH = "HASH";
	public static final String XML_TAG_HASH_ALG = "HASH_ALG";
	public static final String XML_TAG_TAMANIO = "TAMANIO";	
	public static final String XML_TAG_VALIDEZ = "VALIDEZ";	
	public static final String XML_TAG_TIPO = "TIPO";	
	public static final String XML_TAG_COMENTARIOS = "COMENTARIOS";	
	public static final String XML_TAG_SECCION = "SECCION";
	public static final String XML_TAG_DATO = "DATO";
	public static final String XML_TAG_VALOR = "VALOR";
	public static final String XML_TAG_DESCRIPCION = "DESCRIPCION";
	public static final String XML_TAG_PROCEEDING = "PROCEEDING";
	public static final String XML_VALOR_SEPARATOR = "_-";
	public static final String XML_ATTRIBUTE_NOMBRE = "nombre";
	public static final String XML_TAG_TIPO_PROC = "TIPO_PROC";
	public static final String XML_TAG_OTROS = "OTROS";
	public static final String XML_EXPRESION_ROOT = "/INFORME_RP/REGISTRO";
	
    /**
     * Constante correspondiente al texto del campo diligencia de un informe.
     */
    public static final String REPORT_PROCEEEDING_TEXT =
	"De conformidad con lo establecido en el art. 70.3 de la ley 30/92,"
	    + " del 26 de noviembre, de Régimen Jurídico de las "
	    + " Administraciones Públicas y del Prodecidimiento Administrativo Común"
	    + " se extiende el presente recibo a efectos de acreditación de presentación"
	    + " de documentos.";
	
	public static final String TIPO_PROCEDIMIENTO_FIELD_BBDD = "tipoProcedimiento";
 
    public static final String FILE_NAME_EXTENSION_PDF = ".pdf";
	public static final String OR_REPORT_CERTIFICATE_NAME = "AcuseReciboRS";
	public static final String IR_REPORT_CERTIFICATE_NAME = "AcuseReciboRE";
    public static final String OR_REPORT_CERTIFICATE_TEMPLATE_NAME = "OutputRegistersCertificatesReport.jasper";
    public static final String IR_REPORT_CERTIFICATE_TEMPLATE_NAME = "InputRegistersCertificatesReport.jasper";
	public static final String PREFIX_MESSAGE_TEXT = "sigmwsMsg";
	
	public static final String SCHEMA_VALIDATION_FILENAME = "sigm-ws-schema.xsd";

	public static final String PATH_SCHEMA = "PATH_SCHEMA";
	public static final String PATH_REPORTS = "PATH_REPORTS";
	public static final String QNAME_ROOT = "ROOT";

	public static final String JASPER_PARAM_IDBOOK = "idBook";
	public static final String JASPER_PARAM_FDRID = "fdrid";
	public static final String JASPER_PARAM_MAXREPORTREGISTERS = "maxReportRegisters";


	public static final String FILE_NAME_FIRMA = "FIRMA.xsig";
	public static final String FILE_NAME_SOLICITUD = "SOLICITUD.xml";
	public static final String COMENTARIO_SOLICITUD = "Solicitud registro";

	

    	
}