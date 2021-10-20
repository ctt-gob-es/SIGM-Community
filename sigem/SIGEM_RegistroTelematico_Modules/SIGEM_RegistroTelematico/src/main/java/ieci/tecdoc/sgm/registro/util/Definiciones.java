package ieci.tecdoc.sgm.registro.util;

/**
 * Clase que define c�digos de registro
 *
 */
public class Definiciones
{
   private Definiciones()
   {
   }

   public static final String DEFAULT_DATE_FORMAT ="yyyy-MM-dd";
   public static final String DEFAULT_HOUR_FORMAT ="HH:mm:ss";

   public static final String REGISTRY_REQUEST_CODE = "Solicitud Firmada";
   public static final String REGISTRY_REQUEST_NOTSIGNED_CODE = "Solicitud";
   public static final String REGISTRY_RECEIPT_CODE = "Justificante de Registro";

   public static final String VERSION = "Version";
   public static final String VERSION_NUMBER = "1.3";
   public static final String REQUEST_HEADER = "Solicitud_Registro";
   public static final String REGISTRY_DATA = "Datos_Registro";
   public static final String REGISTRY_NUMBER = "Numero_Registro";
   public static final String REGISTRY_DATE_TAG_V1_8 = "Fecha_Registro";
   public static final String REGISTRY_HOUR_TAG_V1_8 = "Hora_Registro";
   public static final String REGISTRY_DATE = "Fecha_Presentacion";
   public static final String REGISTRY_HOUR = "Hora_Presentacion";
   public static final String REGISTRY_EFFECTIVE_DATE = "Fecha_Efectiva";
   public static final String REGISTRY_EFFECTIVE_HOUR = "Hora_Efectiva";
   public static final String SIGNED_DATA = "Datos_Firmados";
   public static final String GENERIC_DATA = "Datos_Genericos";
   public static final String ORG = "Organismo";
   public static final String TOPIC = "Asunto";
   public static final String DESCRIPTION = "Descripcion";
   public static final String ADDRESSEE = "Destino";
   public static final String DESC_ORGANO = "descr_organo";
   public static final String IDIOMA = "Idioma";
   public static final String SUBJECT = "Materia";
   public static final String TYPE = "Tipo";
   public static final String SUBTYPE = "Subtipo";
   public static final String IDIOM = "Idioma";
   public static final String SENDER = "Remitente";
   public static final String LEGAL_REPRESENTATIVE = "Representante_Legal";
   public static final String ID = "Documento_Identificacion";
   public static final String DOCUMENTO_IDENTIFICACION_NUMERO = "Numero";
   public static final String SENDER_ID_TYPE = "Tipo";
   public static final String SENDER_ID = "Numero";
   public static final String SENDER_NAME = "Nombre";
   public static final String SENDER_NAME_SURNAMES = "Nombre_Apellidos";
   public static final String SENDER_SURNAME = "Primer_Apellido";
   public static final String SENDER_SURNAME2 = "Segundo_Apellido";
   public static final String SENDER_EMAIL = "Correo_Electronico";
   public static final String SENDER_PHONE = "Telefono";
   public static final String FOLDER_ID = "Numero_Expediente";
   public static final String SPECIFIC_DATA = "Datos_Especificos";
   public static final String ADDRESS= "Domicilio";
   public static final String DOMICILIO_NOTIFICACION = "Domicilio_Notificacion";
   public static final String LOCALIDAD = "Localidad";
   public static final String PROVINCIA = "Provincia";
   public static final String PAIS = "Pais";
   public static final String CP = "Codigo_Postal";
   public static final String TELEFONO = "Telefono";
   public static final String TELEFONO_MOVIL = "Telefono_Movil";
   public static final String SOLICITAR_ENVIO = "Solicitar_Envio";
   public static final String DEU = "Direccion_Electronica_Unica";
   public static final String DOCUMENTS = "Documentos";
   public static final String DOCUMENT = "Documento";
   public static final String CODE = "Codigo";
   public static final String EXTENSION = "Extension";
   public static final String HASH = "Hash";
   public static final String NAME = "Nombre";
   public static final String SIGNATURE_HOOK = "Firma";
   public static final String VALIDATION_HOOK = "Contenido";
   public static final String ANTIVIRUS = "Antivirus";
   public static final String ANTIVIRUS_OK = "OK";
   public static final String ANTIVIRUS_ERROR = "VIRUS";
   public static final String ORG_ES = "SIGEM";
   public static final String SIGNATURE = "Firma";
   public static final String XPATH_REGISTRY_DATA = "Datos_Registro";
   public static final String XPATH_GENERIC_DATA = "Datos_Firmados/Datos_Genericos";
   public static final String XPATH_SPECIFIC_DATA = "Datos_Firmados/Datos_Especificos";
   public static final String XPATH_DOCUMENTS = "Datos_Firmados/Documentos";
   public static final String XPATH_SENDER_DATA = XPATH_GENERIC_DATA + "/" + SENDER;
   public static final String TAG_INICIAR_EXPEDIENTE_DATOS_ESPECIFICOS = "datos_especificos";
   
   public static final String ID_TRANSACCION = "Id_Transaccion";//[dipucr-Felipe #457]
   public static final String NUMERO_EXPEDIENTE = "numExpediente";//[dipucr-Felipe #1354]
   
   public static final String FIRMA_X509_BEGIN = "<ds:X509Certificate>";//[Agustin #1354]
   public static final String FIRMA_X509_END = "</ds:X509Certificate>";//[Agustin #1354]
   
}