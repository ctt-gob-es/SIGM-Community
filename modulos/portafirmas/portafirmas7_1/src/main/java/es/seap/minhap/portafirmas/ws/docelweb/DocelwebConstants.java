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

package es.seap.minhap.portafirmas.ws.docelweb;

public interface DocelwebConstants {
	
	// TWO MONTH GAP
	public static int DOCELWEB_TWO_MONTH_GAP = -2;

	// SOAP VERSION
	public static String SOAP_V11 = "SOAP11";
	public static String SOAP_V12 = "SOAP12";
	public static String MTOM_MIME_TYPE = "application/octet-stream";
	public static String MTOM_ENABLE_PROP = "mtom-enabled";

	// CONFIGURACIÓN SSL PARA CLIENTES WS
	public static String DOCELWEB_CLIENT_SSL_TRUSTEDSTORE = "TRUSTSTORE.FILE";
	public static String DOCELWEB_CLIENT_SSL_TRUSTEDSTORE_PASS = "TRUSTSTORE.PASSWORD";

	// CONFIGURACIÓN CLIENTE SISTEMA DE GESTIÓN
	// ENDPOINT
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_URL = "DOCEL.SMC.URL";
	// SECURITY MODE
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_MODE = "DOCEL.SMC.SECURITY.MODE";
	// SECURITY CONFIG USER_NAME_TOKEN
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_USER = "DOCEL.SMC.SECURITY.USER";
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_PASSWORD = "DOCEL.SMC.SECURITY.PASSWORD";
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_PASSWORD_TYPE = "DOCEL.SMC.SECURITY.PASS.TYPE";
	// SECURITY CONFIG BINARY_SECURITY_TOKEN
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_CERT_ALIAS = "DOCEL.SMC.SECURITY.CERT.ALIAS";
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_CERT_PWD = "DOCEL.SMC.SECURITY.CERT.PWD";
	public static String DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_FILE_NAME = "DOCEL.SMC.SECURITY.FILE.NAME";

	// CONFIGURACIÓN CLIENTE SISTEMA PORTAFIRMAS
	// ENDPOINT
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_URL = "DOCEL.SPC.URL";
	// SECURITY MODE
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_MODE = "DOCEL.SPC.SECURITY.MODE";
	// SECURITY CONFIG USER_NAME_TOKEN
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_USER = "DOCEL.SPC.SECURITY.USER";
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_PASSWORD = "DOCEL.SPC.SECURITY.PASSWORD";
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_PASSWORD_TYPE = "DOCEL.SPC.SECURITY.PASS.TYPE";
	// SECURITY CONFIG BINARY_SECURITY_TOKEN
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_CERT_ALIAS = "DOCEL.SPC.SECURITY.CERT.ALIAS";
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_CERT_PWD = "DOCEL.SPC.SECURITY.CERT.PWD";
	public static String DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_FILE_NAME = "DOCEL.SPC.SECURITY.FILE.NAME";

	// WSS PROPERTIES
	public static String USERNAME_TOKEN_MODE = "UsernameToken";
	public static String BINARY_SECURITY_TOKEN_MODE = "BinarySecurityToken";
	public static String NONE_MODE = "None";
	public static String WSSECURITY_DIRECT_REFERENCE = "DirectReference";

	// CODIGOS FAULT
	public static String FAULT_CODE_SEPARATOR = " - ";
	public static String CODE_E00_UNKNOWN = "E00";
	public static String CODE_E01_UNSUPPORTED_SIGNTYPE = "E01";
	public static String CODE_E02_UNSUPPORTED_VERSION = "E02";
	public static String CODE_E03_UNRECOGNIZED_SIGNER = "E03";
	public static String CODE_E04_OTHER_ERROR = "E04";
	public static String CODE_E05_UNKNOWN_REQUEST = "E05";
	public static String CODE_E06_VOID_DOC_CONTENT = "E06";
	public static String CODE_E07_MISSPMATCH_DOCUMENT = "E07";
	public static String CODE_E08_RETRY_REQUEST = "E08";
	public static String CODE_E09_UNSUPPORTED_MULTISIGN = "E09";

	// TEXTO DE RECHAZO
	public static String DOCEL_REJECTION_TEXT = "Petición anulada mediante servicio web de InterfazGenérica.";
	
	public static String MSJ_ERROR_PARAMETRO_NULO = "Se recibió algún parámetro vacío";

	// CADENA VACIA
	public static String VOID_CONTENT_PARAM = "";

	// ESTADO DE LAS SOLICITUDES
	public static String REQUEST_STATE_NEW = "N"; // En proceso de alta
	public static String REQUEST_STATE_WAITING = "P"; // Pendiente
	public static String REQUEST_STATE_SIGN_NOTIFIED = "X"; // firma Notificada
	public static String REQUEST_STATE_SIGNED = "F"; // Firmada
	public static String REQUEST_STATE_REJECTED = "R"; // Rechazada
	public static String REQUEST_STATE_VOID = "A"; // Anulada

	// TIPOS DE DOCUMENTOS
	public static String ELECTRONIC_DOC = "E"; // Documento electrónico
	public static String DISPOSITION_DOC = "U"; // Disposición/URL
	public static String PAPER_DOC = "P"; // Documento en papel

	// BOOLEAN
	public static String YES = "S"; // Si
	public static String NO = "N"; // No

	// FORMATOS DE FIRMA
	public static String XAdES = "XADES";
	public static String CAdES = "CADES";
	public static String PAdES = "PADES";

	// VERSIONES XAdES
	public static String XAdES_122 = "1.2.2";
	public static String XAdES_132 = "1.3.2";

	// PRIORIDAD
	public static String PRIORITY_HIGH = "A";
	public static String PRIORITY_NORMAL = "N";

	// TIPOS MULTIFIRMA
	public static String MULTISIGN_NO = "NO";
	public static String MULTISIGN_PARALLEL = "PARALELO";
	public static String MULTISIGN_SERIES = "SERIE";

	// FORMATO DE FECHA Y HORA
	public static String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	// CONSULTAS SQL
	public static String QUERY_GENERIC_USER = "request.docelwebGenericUser";
	public static String QUERY_PRIORITY_TYPE_CODE = "request.queryImportanceLevelByCode";
	public static String QUERY_PRIORITY_NORMAL = "request.queryNormalLevel";
	public static String QUERY_FILE_CONTENT_HASH = "file.parameterFileHash";
	public static String QUERY_PF_DOCUMENT_BY_FILE_ID = "request.documentByFileId";
	public static String QUERY_DOCEL_REQUEST_SPFIRMA = "request.docelwebRequestSpfirma";
	public static String QUERY_DOCEL_REQUEST_SMANAGER_BY_SPFIRMA_DATA = "request.docelwebRequestSmanagerBySpfirmaData";
	public static String QUERY_DOCEL_REQUEST_SMANAGER = "request.docelwebRequestSmanager";
	public static String QUERY_DOCEL_SM_ALL_REQUEST = "request.docelwebAllRequestSmanager";
	public static String QUERY_DOCEL_SM_REQUESTS_BY_STATUS = "request.systemManagerByStatus";
	public static String QUERY_DOCEL_SM_OLD_REQUESTS_BY_STATUS = "request.systemManagerOldByStatus";
	public static String QUERY_DOCEL_SPFIRMA_DOCUMENT = "request.docelwebSpfirmaDocumentById";	
	public static String QUERY_DOCEL_SM_DOC_BY_ID = "request.docelwebSManagerDocumentById";
	public static String QUERY_DOCEL_SM_DOC_BY_REQUEST_ID = "request.docelwebSManagerDocumentByRequestId";
	public static String QUERY_DOCEL_DOCUMENT_REQUEST = "request.docelwebRequestDocumentByDescription";
	public static String QUERY_DOCEL_PFIRMA_REQUEST_BY_HASH = "request.docelwebRequestPfirmaRequestHash";
	public static String QUERY_DOCEL_DEFAUTL_SYSTEM_MANAGER_CLIENT_CONFIG = "request.docelwebSystemManagerClientDefaultConfig";
	public static String QUERY_DOCEL_CONCRETE_SYSTEM_MANAGER_CLIENT_CONFIG = "request.docelwebSystemManagerClientConcreteConfig";
	public static String QUERY_DOCEL_DEFAUTL_SYSTEM_PFIRMA_CLIENT_CONFIG = "request.docelwebSystemPfirmaClientDefaultConfig";
	public static String QUERY_DOCEL_CONCRETE_SYSTEM_PFIRMA_CLIENT_CONFIG = "request.docelwebSystemPfirmaClientConcreteConfig";
	public static String QUERY_DOCEL_DEFAUTL_SYSTEM_MANAGER_CLIENT_CONFIG_PARAM = "request.docelwebSystemManagerClientDefaultConfigParam";
	public static String QUERY_DOCEL_CONCRETE_SYSTEM_MANAGER_CLIENT_CONFIG_PARAM = "request.docelwebSystemManagerClientConcreteConfigParam";
	public static String QUERY_DOCEL_DEFAUTL_SYSTEM_PFIRMA_CLIENT_CONFIG_PARAM = "request.docelwebSystemPfirmaClientDefaultConfigParam";
	public static String QUERY_DOCEL_CONCRETE_SYSTEM_PFIRMA_CLIENT_CONFIG_PARAM = "request.docelwebSystemPfirmaClientConcreteConfigParam";	
	public static String QUERY_DOCEL_PFIRMA_CONFIG_PARAM = "request.paramValue";
	public static String QUERY_DOCEL_PFIRMA_GENERAL_PARAM = "request.genericParamValue";
	public static String QUERY_DOCEL_SMANAGER_REQUESTS_CANCELED_OR_NEW = "request.docelwebSystemManagerRequestsCanceledAndNew";
	public static String QUERY_DOCEL_DOCUMENT_BY_FILE_ID = "request.docelwebDocumentByFileId";

	// PARAMETROS SQL
	public static String QUERY_PARAM_PRIORITY_CODE = "codLevel";
	public static String QUERY_PARAM_NAME_SURNAMES = "nameAndSurnames";
	public static String QUERY_PARAM_FILE_HASH = "hash";
	public static String QUERY_PARAM_FILE_ID = "idFile";	
	public static String QUERY_PARAM_DOCEL_SYSTEM_MANAGER = "manager";
	public static String QUERY_PARAM_DOCEL_SYSTEM_PFIRMA = "pfirma";
	public static String QUERY_PARAM_DOCEL_SP_TRANSACTION_ID = "idTransaction";
	public static String QUERY_PARAM_DOCEL_SM_REQUEST_ID = "idRequest";
	public static String QUERY_PARAM_DOCEL_SPFIRMA_ID = "idSystempfirma";
	public static String QUERY_PARAM_DOCEL_DOCUMENT_ID = "idDocumento";
	public static String QUERY_PARAM_DOCEL_DOCUMENT_DESCRIPTION = "dDescripcion";
	public static String QUERY_PARAM_DOCEL_REQUEST = "request";
	public static String QUERY_PARAM_DOCEL_CONF_ID = "idConf";
	public static String QUERY_PARAM_DOCEL_CONF_VALUE = "tvalor";
	public static String QUERY_PARAM_DOCEL_CONF_KEY = "paramkey";

	// APLICACIONES PARA FORMATOS DE FIRMA SQL
	public static String QUERY_APPLICATION_IG = "request.appInterfazGenerica";

	public static String STATE_DELIMITER = "###";

	public static Long INIT_ID_TRANSACTION = -1L;

	public static String MI_PORTAFIRMAS = "interfazGenerica.miPortafirmas";
	
	public static String DISABLE_CN_CHECK = "disableCNCheck";

	public static String MENSAJE_ANULACION = "Solicitud anulada después de tres meses de espera desde su solicitud";
	
}
