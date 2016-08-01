package es.dipucr.sigem.api.rule.procedures;

public class Constants {
	
	//Tipo de Relación Participantes
	public static final String _RELACION_INTERESADO = "INT";
	public static final String _RELACION_TRASLADO = "TRAS";
	//Extensiones
	public static final String _EXTENSION_DOC = "doc";
	public static final String _EXTENSION_ODT = "odt";
	public static final String _EXTENSION_PDF = "pdf";
	//Mimetypes
	public static final String _MIMETYPE_DOC = "application/msword";
	public static final String _MIMETYPE_ODT = "application/vnd.oasis.opendocument.text";
	public static final String _MIMETYPE_PDF = "application/pdf";
	
	
	public interface AVISOSELECTRONICOS{
		public static final String MENSAJEREGISTROTELEMATICO = "Expediente iniciado mediante Registro Telemático";
	}
	
	public interface COMPARECE {
		public static final String PARTICIPANTES_COMPARECE = "Participantes Comparece";
		public static final String ACUSE_COMPARECE = "Acuse Comparece";
		public static final String SIGEM = "1";
	}
	
	public interface TESORERIA {
		public static final String CERTIFICADO_RETENCIONES = "Certificado de Retenciones";
	}
	
	public interface TABLASBBDD {
		//
		public static final String DPCR_ACUSE_EMAIL = "DPCR_ACUSE_EMAIL";
		public static final String SUBV_CONVOCATORIA = "SUBV_CONVOCATORIA";
		public static final String SPAC_EXP_RELACIONADOS = "SPAC_EXP_RELACIONADOS";
		public static final String SPAC_DT_DOCUMENTOS = "SPAC_DT_DOCUMENTOS";
		public static final String SPAC_DT_DOCUMENTOS_H = "SPAC_DT_DOCUMENTOS_H";
		public static final String SPAC_DT_INTERVINIENTES = "SPAC_DT_INTERVINIENTES";
		public static final String SPAC_DT_INTERVINIENTES_H = "SPAC_DT_INTERVINIENTES_H";
		public static final String SECR_PROPUESTA = "SECR_PROPUESTA";
		public static final String SECR_URGENCIAS = "SECR_URGENCIAS";
		public static final String SPAC_EXPEDIENTES = "SPAC_EXPEDIENTES";
		public static final String SPAC_EXPEDIENTES_H = "SPAC_EXPEDIENTES_H";
		public static final String SPAC_FASES = "SPAC_FASES";
		public static final String SPAC_P_TRAMITES = "SPAC_P_TRAMITES";
		public static final String SGD_DECRETO = "SGD_DECRETO";
		public static final String SPAC_P_PLANTDOC = "SPAC_P_PLANTDOC";
		public static final String SPAC_P_PROCEDIMIENTOS="SPAC_P_PROCEDIMIENTOS";
		public static final String DPCR_ACUSES_COMPARECE="DPCR_ACUSES_COMPARECE";
		public static final String SPAC_CT_TPDOC = "SPAC_CT_TPDOC";
		public static final String DPCR_PARTICIPANTES_COMPARECE = "DPCR_PARTICIPANTES_COMPARECE";
		public static final String SPAC_DT_TRAMITES = "SPAC_DT_TRAMITES";
		public static final String SPAC_DT_TRAMITES_H = "SPAC_DT_TRAMITES_H";
		public static final String SPAC_CT_PROCEDIMIENTOS = "SPAC_CT_PROCEDIMIENTOS";
		public static final String ASES_REPRES = "ASES_REPRES";
		public static final String ASES_EMISION = "ASES_EMISION";
		
		public static final String SPAC_HITOS = "SPAC_HITOS";
		public static final String SPAC_HITOS_H = "SPAC_HITOS_H";
		public static final String SECR_SESION = "SECR_SESION";
		
		public static final String SPAC_P_FASES = "SPAC_P_FASES";

	}
	
	public interface CONSTANTES {
		//
		public static final String PROPUESTA = "PROPUESTA";
		public static final String PROCEDIMIENTO_ANUNCIO_INTERNO_BOP = "Inserción de Anuncio Interno en el BOP";
	}
	
	public interface SUBVENCIONES {
		//
		public static final String PROCHIJODECRETO = "decreto";
		public static final String PROCHIJOPROPUESTA = "propuesta";
		public static final String RELACION = ".relacion.";
		public static final String NOMBREPROCSOL = "Solicitud de Subvención a Entidad Pública";
	}
	
	public interface TIPODOC {
		//
		public static final String CONTENIDO_PROPUESTA = "Contenido de la propuesta";
		public static final String DOCUMENTACION_PROPUESTA = "Documentación de Propuesta";
		public static final String DOCUMENTACION_APROBACION = "Documentación de la Aprobación";
		public static final String DOCUMENTACION = "Documentación";
		public static final String DECRETOS_CONVOCATORIA = "Decretos convocatoria";
		public static final String PLANTILLA_NOTIFICACIONES_TRAMITE_RESOLUCION = "Plantilla Notificaciones Trámite Resolución";
		public static final String CONTRAPORTADA = "Contraportada publicaciones";
		public static final String CERTIFICADO_DICTAMEN = "Certificado de dictamen";
		public static final String ACTA_PLENO = "Borrador de Acta de Pleno";
		public static final String ACTA_JUNTA = "Borrador de Acta de Junta";
		public static final String ACTA_MESA = "Borrador de Acta de Mesa";
		public static final String ACTA_COMISION = "Borrador de Acta de Comisión";
		// INICIO [eCenpri-Felipe #911]
		public static final String LIBRO_ACTAS = "Libro de Actas";
		public static final String LIBRO_ACTAS_DILIG = "Libro de Actas: Diligencia";
		public static final String LIBRO_ACTAS_DILIG_JUNTA = "Libro de Actas Junta: Diligencia";
		public static final String LIBRO_ACTAS_DILIG_COMISION = "Libro de Actas Comision: Diligencia";
		// FIN [eCenpri-Felipe #911]
	}
	public interface PLANTILLADOC {
		public static final String DECRETO_CABECERA = "Decreto Cabecera";
		public static final String DECRETO_PIE = "Decreto Pie";
		public static final String NOTIFICACIONES_CABECERA = "Plantilla Notificaciones Cabecera";
		public static final String NOTIFICACIONES_PIE = "Plantilla Notificaciones Pie";
		public static final String NOTIFICACIONES_LEVANTAMIENTO_ACTAS = "EXPR-011 - Notificacion de Levantamiento de Actas Previas";
		public static final String BOPANUNCIO = "BOP - Anuncio";
		public static final String IMPRIMIR = "Documento Imprimir";
	}
	public interface SECRETARIATRAMITES {
		public static final String NOTIFTRASLDICTAMEN = "Notificaciones y traslado de dictamenes";
		public static final String CERTIFICADACUERD = "Certificado de acuerdos";
		public static final String CERTIFICADACUERDNOTIFICACI ="Notificaciones y traslado de acuerdos";
		public static final String CONVOCATORIAMESA ="Convocatoria de la Mesa";
		//INICIO [eCenpri-Felipe #571]
		public static final String SESION_PLENO_EXTRACTO = "Extracto de Acta de Pleno";
		public static final String SESION_PLENO_DEBATES = "Acta de Pleno con debates";
		//FIN [eCenpri-Felipe #571]
		public static final String COD_TRAM_LIBROACTAS_DILIG = "LIBROACTAS_DILIG"; //[eCenpri-Felipe #911]
		public static final String SESION_NO_CELEBRADA = "Diligencia de Sesión no celebrada"; //[eCenpri-Felipe #974]
	}
	public interface DECRETOSTRAMITES {
		public static final String FIRMASTRASLADO = "Preparación de firmas y traslado del Decreto";
	}
	
	public interface SECRETARIAPROC {
		//Sesión de Comisión Informativa Permanente Bienestar Social             
		public static final String sCOMIBB = "PCD-27";
		//Sesión de Comisión Informativa Permanente de Cultura
		public static final String sCOMICutl = "PCD-25";
		//Sesión de Comisión Informativa Permanente de Hacienda y Promoción Económica
		public static final String sCOMIHac = "PCD-24";
		//Sesión de Comisión Informativa Permanente de Infraestructuras
		public static final String sCOMIInf = "PCD-28";
		//Sesión de Comisión Informativa Permanente de Personal 
		public static final String sCOMIPers = "PCD-26";
		//Sesión de Junta de Gobierno
		public static final String sJuntG = "CR-SECR-05";
		//Sesión de Mesa de Contratación
		public static final String sMesCont = "CR-SECR-06";
		//Sesión de Pleno
		public static final String sPleno = "CR-SECR-03";
		
		//Documentos de secretaria
		public static final String docAsistencia = "CERTIFICADO DE ASISTENCIA";
		
		//Texto para las notificaciones
		public static final String sinRECUSO = "Lo que le comunico para su conocimiento y efecto.";
		public static final String conRECUSO = " Lo que le notifico para su conocimiento a los efectos oportunos, sin perjuicio de que " +
				"pueda emprender cuantas acciones tenga por convenientes en defensa de sus derechos.";
		
		public static final String JUNTA = "Sesión de Junta de Gobierno";
		public static final String PLENO = "Sesión de Pleno";
		public static final String MESA = "Sesión de Mesa de Contratación";
		
		//Tipos de órgano de gobierno
		public static final String TIPO_PLENO = "PLEN";
		public static final String TIPO_JUNTA = "JGOB";
		public static final String TIPO_MESA = "MESA";
		public static final String TIPO_COMISION = "COMI";
	}
	public interface MENSAJESECRETARIA{
		public static final String MENSAJEDICTAMINADO = "La propuesta no ha sido todavía dictaminada o certificada.";
	}
	public interface MENSAJESDECRETOS{
		public static final String MENSAJEDECRETADO = "La propuesta no ha sido todavía decretada.";
	}
	public interface MENSAJE {
		public static final String MENSAJERELACIONEXPEDIENTE = "No esta relacionado el expediente con ningún otro expediente.";
		public static final String MENSAJENOCERRAREXP = "No se ha podido cerrar el expediente contacte con el administrador";
		public static final String MENSAJECOMPARECE = "Error al comparecer a los usuarios. Avisa al administrador de SIGEM";
	}
	
	public interface PROPUESTA{
		public static final String NOMBRE_GRUPO = "SECR";
		public static final String NOMBRE = "propuesta";
		public static final String PROPUESTA_URGENCIA = "Propuesta Urgencia";
		public static final String URGENCIA = ".- Urgencia";
	}
	
	public interface DECRETOS{
		
		//Documentos de decretos
		public static final String _DOC_DECRETO = "Decreto";
		public static final String _DOC_NOTIFICACIONES = "Plantilla de Notificaciones";
		public static final String _DOC_LIBRO_DECRETOS = "Libro de decretos";
		public static final String _DOC_LIBRO_DECRETOS_DILIGENCIA = "Libro de decretos: Diligencia";
		
		//Trámites
		public static final String _TRAM_FIRMAS_Y_TRALADO = "Preparación de firmas y traslado del Decreto";
		public static final String _COD_TRAM_LIBRODEC_DILIG = "LIBRODEC_DILIG";
		//Nombre del grupo
		public static final String NOMBRE_GRUPO = "DCR";
	}
	
	public interface BOP{
		//Formato de los coste de la facturación
//		public static final String _COSTE_FORMAT = "#.00";
		public static final String _COSTE_FORMAT = "###,##0.00"; //[eCenpri-Felipe #453] Separador de miles
		public static final String _COSTE_CARACTER_FORMAT = "###,##0.00##"; //[eCenpri-Felipe #561] hasta 4 decimales
		//Documentos del boletín
		public static final String _DOC_ANUNCIO = "BOP - Anuncio";
//		public static final String _DOC_FACTURA = "BOP - Factura";
		public static final String _DOC_FACTURA = "BOP Factura"; //[eCenpri-Felipe #474] Registrar todo
		public static final String _DOC_ANUNCIOS_SIN_FACTURAR = "BOP - Anuncios sin facturar";
		public static final String _DOC_BOP = "BOP - General"; //[eCenpri-Felipe #593]
		//[eCenpri-Felipe #489]
		public static final String _TIPODOC_NOTIFCONTROL = "BOP Notificación de control";
	}
	
	//[eCenpri-Felipe #632]
	public interface CERTPERSONAL{
		public static final String _DOC_CERTIFICADO = "Certificado de Personal";
		public static final String _DOC_CARTADIGITAL = "Plantilla Carta digital Cert.Personal";
		public static final String _TRAMITE_SUPERVISION = "Supervisión Serv.Personal";
		public static final String _TRAMITE_FIRMA = "Firma del Certificado";
		public static final String _COD_PCD_CARTA = "PCD-CARTACERT";
	}
	
	//[eCenpri-Felipe #743]
	public interface CERTPADRON{
		public static final String _DOC_CERTIFICADO = "Certificado del Padrón";
		public static final int _TIPO_VOLANTE = 1;
		public static final int _TIPO_CERTIFICADO = 2;
		public static final int _TIPO_VOL_FAMILIAR = 3; //[eCenpri-Felipe #1035]
		public static final int _TIPO_CERT_FAMILIAR = 4; //[eCenpri-Felipe #1035]
		public static final int _TIPO_DOC_NIF = 1;
		public static final int _TIPO_DOC_PASAPORTE = 2;
		public static final int _TIPO_DOC_NIE = 3;
		public static final String _INICIO_DOC_PASAPORTE = "PTE";
		public static final String _TIPO_PERSONA_FISICA = "F";
	}
	
	public interface VALIDACION{
		public static final String SI = "SI";
		public static final String NO = "NO";
	}

}
