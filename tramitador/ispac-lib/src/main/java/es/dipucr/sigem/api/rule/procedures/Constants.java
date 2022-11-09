package es.dipucr.sigem.api.rule.procedures;

public class Constants {
	
	//[dipucr-Felipe #1246]
	public static final String DEFAULT_USER_PORTAFIRMAS = "PORTAFIRMAS";
	public static final String DEFAULT_USER_PORTAFIRMAS_PWD = "sigem";
	
	//[dipucr-Felipe #1716]
	public static final String DEFAULT_USER_AUTOMATIZACION = "AUTOMATIZACION";
	public static final String DEFAULT_USER_AUTOMATIZACION_PWD = "sigem";
	
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
	
	
	public interface TESORERIA {
		public static final String NUM_CERT_RETEN_TRAM = "NUM_CERT_RETEN_TRAM";
		
		public static final String CERTIFICADO_RETENCIONES = "Certificado de Retenciones";
		public static final String CERTIFICADO_ACTIVIDADES_ECONOMICAS = "Certificado de Actividades Económicas";
	}
	
	public interface TABLASBBDD {
		//
		public static final String DPCR_ACUSE_EMAIL = "DPCR_ACUSE_EMAIL";
		public static final String SUBV_CONVOCATORIA = "SUBV_CONVOCATORIA";
		public static final String SPAC_EXP_RELACIONADOS = "SPAC_EXP_RELACIONADOS";
		public static final String SPAC_DT_DOCUMENTOS = "SPAC_DT_DOCUMENTOS";
		public static final String SPAC_DT_DOCUMENTOS_H = "SPAC_DT_DOCUMENTOS_H";
		public static final String SPAC_DT_DOCUMENTOS_BORRADOS = "SPAC_DT_DOCUMENTOS_BORRADOS";//[dipucr-Felipe #1462]
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
		public static final String DPCR_ACUSES_NOTIFICA="DPCR_ACUSES_NOTIFICA";
		public static final String SPAC_CT_TPDOC = "SPAC_CT_TPDOC";
		public static final String DPCR_PARTICIPANTES_COMPARECE = "DPCR_PARTICIPANTES_COMPARECE";
		public static final String SPAC_DT_TRAMITES = "SPAC_DT_TRAMITES";
		public static final String SPAC_DT_TRAMITES_H = "SPAC_DT_TRAMITES_H";
		public static final String SPAC_CT_PROCEDIMIENTOS = "SPAC_CT_PROCEDIMIENTOS";
		public static final String ASES_REPRES = "ASES_REPRES";
		public static final String ASES_EMISION = "ASES_EMISION";
		public static final String FIRMA_DOC_EXTERNO = "FIRMA_DOC_EXTERNO";
		
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
		public static final String ACTA_PLENO_AUDIO = "Acta de pleno con audio";//[dipucr-Felipe #663]
		public static final String ACTA_JUNTA = "Borrador de Acta de Junta";
		public static final String ACTA_MESA = "Borrador de Acta de Mesa";
		public static final String ACTA_COMISION = "Borrador de Acta de Comisión";
		// INICIO [eCenpri-Felipe #911]
		public static final String LIBRO_ACTAS = "Libro de Actas";
		public static final String LIBRO_ACTAS_DILIG = "Libro de Actas: Diligencia";
		public static final String LIBRO_ACTAS_DILIG_JUNTA = "Libro de Actas Junta: Diligencia";
		public static final String LIBRO_ACTAS_DILIG_PLENO = "Libro de Actas Pleno: Diligencia";//[dipucr-Felipe #890]
		public static final String LIBRO_ACTAS_DILIG_CONSEJO = "Libro de Actas Consejo: Diligencia";
		public static final String LIBRO_ACTAS_DILIG_COMISION = "Libro de Actas Comision: Diligencia";
		// FIN [eCenpri-Felipe #911]
		// [dipucr-Felipe #1606]
		public static final String CREACION_AVANZADA = "Creación avanzada";
		
	}
	public interface PLANTILLADOC {
		public static final String DECRETO_CABECERA = "Decreto Cabecera";
		public static final String DECRETO_PIE = "Decreto Pie";
		public static final String NOTIFICACIONES_CABECERA = "Plantilla Notificaciones Cabecera";
		public static final String NOTIFICACIONES_PIE = "Plantilla Notificaciones Pie";
		public static final String NOTIFICACIONES_LEVANTAMIENTO_ACTAS = "EXPR-011 - Notificacion de Levantamiento de Actas Previas";
		public static final String BOPANUNCIO = "BOP - Anuncio";
		public static final String IMPRIMIR = "Documento Imprimir";
		// [dipucr-Felipe #1606]
		public static final String NOTIF_AVANZADA_CABECERA = "Notificación Avanzada - Cabecera";
		public static final String NOTIF_AVANZADA_PIE = "Notificación Avanzada - Pie";
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
		public static final String SESION_PLENO_AUDIO = "Acta de Pleno"; //[dipucr-Felipe #663]
		public static final String SESION_PLENO_AUDIO_BIS = "Acta con debates en audio"; //[dipucr-Felipe #663]
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
		public static final String conRECUSO = "\n\nLo que le notifico para su conocimiento a los efectos oportunos, sin perjuicio de que " +
				"pueda emprender cuantas acciones tenga por convenientes en defensa de sus derechos.";
		
		public static final String JUNTA = "Sesión de Junta de Gobierno";
		public static final String PLENO = "Sesión de Pleno";
		public static final String MESA = "Sesión de Mesa de Contratación";
		public static final String CONSEJO = "Sesión del Consejo Rector";
		
		//Tipos de órgano de gobierno
		public static final String TIPO_PLENO = "PLEN";
		public static final String TIPO_PLENO_TABLON = "PLENO";
		public static final String TIPO_JUNTA = "JGOB";
		public static final String TIPO_MESA = "MESA";
		public static final String TIPO_COMISION = "COMI";
		public static final String TIPO_CONSEJO = "CONS";
		public static final String TIPO_CONSEJOGERENCIA = "CGE";
		public static final String TIPO_CONSEJORECTOR = "JRE";
		public static final String TIPO_MESAGENERALNEGOCIACION = "MGN";
		
		public static final String TIPO_RELACION = "Sesion/Propuesta";
		
		public static final String TABLA_SECR_PROPUESTA = "SECR_PROPUESTA";
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
		public static final int _TIPO_DOC_NIF = 1;
		public static final int _TIPO_DOC_PASAPORTE = 2;
		public static final int _TIPO_DOC_NIE = 3;
		public static final String _INICIO_DOC_PASAPORTE = "PTE";
		public static final String _TIPO_PERSONA_FISICA = "F";
	}
	
	//[dipucr-Felipe 3#693]
	public interface COMISIONSERV{
		public static final String CODTRAM_FIRMA = "COM-SERV";
		public static final String COD_TPDOC_COMISION = "COM-SERV";
	}
	
	//[dipucr-Felipe #1677] 
	public interface TRAMITES_PERSONALIZADOS{
		public interface TIPODOC{
			public static final String NOMBRE = "Documento Personalizado";
			public static final String CODIGO = "DOC-CUSTOM";
			
		}
		public interface PLANTILLA{
			public static final String NOMBRE = "Plantilla Personalizada";
			public static final String CODIGO = "PLANT-CUSTOM";
		}
		public interface TRAMITE{
			public static final String NOMBRE = "Trámite Personalizado";
			public static final String CODIGO = "TRAM-CUSTOM";
		}
	}
	
	public interface VALIDACION{
		public static final String SI = "SI";
		public static final String NO = "NO";
	}
	
	public interface COMPARECE {
		public static final String PARTICIPANTES_COMPARECE = "Participantes Comparece";
		public static final String ACUSE_COMPARECE = "Acuse Comparece";
		public static final String SIGEM = "1";
	}
	
	//Agustin #414 Integracion de Notifica
	public interface NOTIFICA{
		
		public static final String PARTICIPANTES_NOTIFICA = "Participantes Notifica";
		public static final String ACUSE_NOTIFICA = "Acuse Notifica";
		public static final String API_KEY_NOTIFICA = "API_KEY_NOTIFICA";
		
	}	
	
	public interface NOTIFICACIONES_VALOR{
	    public static final String VALOR_ESTADO_PENDIENTE = "NTPENSEDE";
	    public static final String VALOR_ESTADO_EN_PROCESO = "PR";
	    public static final String VALOR_ESTADO_OK = "OK";
	    public static final String VALOR_ESTADO_CADUCADA = "CA";
	    public static final String VALOR_ESTADO_RECHAZADA = "RE";
	    public static final String VALOR_ESTADO_ERROR = "ER";
	    public static final String VALOR_ESTADO_COMPARECE = "CO";
	    public static final String VALOR_ESTADO_NOTIFICA = "NT";      //Habría dos posibles valores Notifica devuelve 000 si todo ha ido bien, una vez que lo ha procesado asigna el estado pendiente de comparecencia
	    public static final String VALOR_ESTADO_NOTIFICA_000 = "000";
	    public static final String VALOR_ESTADO_NOTIFICA_ENVIO_POSTAL = "NTPOSTAL"; //Este campo no viene de Notifica, lo indico al generar documentos sellados para mandarlos por carta
	    public static final String VALOR_ESTADO_NOTIFICA_AUSENTE = "NTAUSENT";
		public static final String VALOR_ESTADO_NOTIFICA_DESCONOCIDO = "NTDESCON";
		public static final String VALOR_ESTADO_NOTIFICA_DIRECCION_INCORRECTA = "NTDIERRO";
		public static final String VALOR_ESTADO_NOTIFICA_ENVIO_AL_CENTRO_DE_IMPRESION = "NTENVIMP";
		public static final String VALOR_ESTADO_NOTIFICA_ENVIO_A_LA_DEH = "NTENVDEH";
		public static final String VALOR_ESTADO_NOTIFICA_LEIDA = "NTILEIDA";
		public static final String VALOR_ESTADO_NOTIFICA_ERROR = "NTIERROR";
		public static final String VALOR_ESTADO_NOTIFICA_EXTRAVIADA = "NTEXTRAV";
		public static final String VALOR_ESTADO_NOTIFICA_FALLECIDO = "NTFALLEC";
		public static final String VALOR_ESTADO_NOTIFICA_NOTIFICADA = "NTIFCAOK";
		public static final String VALOR_ESTADO_NOTIFICA_PENDIENTE_DE_ENVIO = "NTPENENV";
		public static final String VALOR_ESTADO_NOTIFICA_PENDIENTE_DE_COMPARECENCIA = "NTPENCOM";
		public static final String VALOR_ESTADO_NOTIFICA_REHUSADA = "NTREHUSA";
		public static final String VALOR_ESTADO_NOTIFICA_FECHA_ENVIO_PROGRAMADO = "NTWNVPRO";
		public static final String VALOR_ESTADO_NOTIFICA_SIN_INFORMACION = "NTSININF";
		public static final String VALOR_ESTADO_NOTIFICA_CADUCADA = "NTCADUCA";
		public static final String VALOR_ESTADO_NOTIFICA_EXPIRADA = "NTEXPIRADA";
	    
	}
	
	//Agustin #414 Integracion de Notifica
	public interface NOTIFICACIONES_ESTADOS{
		public static final String ESTADO_PENDIENTE = "pendiente_sede";
		public static final String ESTADO_EN_PROCESO = "En Proceso";		
		public static final String ESTADO_OK = "Finalizada";		
		public static final String ESTADO_CADUCADA = "Caducada";		
		public static final String ESTADO_RECHAZADA = "rehusada";		
		public static final String ESTADO_ERROR = "Error";		
		public static final String ESTADO_COMPARECE = "Enviada a Comparece";
		public static final String ESTADO_NOTIFICA = "Enviada a Notifica";
		public static final String ESTADO_NOTIFICA_ENVIO_POSTAL = "Envío postal"; //Este campo no viene de Notifica, lo indico al generar documentos sellados para mandarlos por carta
		public static final String ESTADO_NOTIFICA_AUSENTE = "ausente";//(sólo notificaciones)
		public static final String ESTADO_NOTIFICA_DESCONOCIDO = "desconocido";//(sólo notificaciones)
		public static final String ESTADO_NOTIFICA_DIRECCION_INCORRECTA = "direccion_incorrecta";//(sólo notificaciones)
		public static final String ESTADO_NOTIFICA_ENVIO_AL_CENTRO_DE_IMPRESION = "enviado_ci";
		public static final String ESTADO_NOTIFICA_ENVIO_A_LA_DEH = "enviado_deh";
		public static final String ESTADO_NOTIFICA_LEIDA = "leida"; //(sólo comunicaciones leídas en Carpeta Ciudadana o Sede Electrónica).
		public static final String ESTADO_NOTIFICA_ERROR = "error";
		public static final String ESTADO_NOTIFICA_EXTRAVIADA = "extraviada"; //(sólo notificaciones)
		public static final String ESTADO_NOTIFICA_FALLECIDO = "fallecido"; //cuando el destinatario de la notificación o comunicación ha fallecido. (sólo cuando el destinatario de la notificación o comunicación ha fallecido. (sólo notificaciones)
		public static final String ESTADO_NOTIFICA_NOTIFICADA = "notificada"; //(sólo notificaciones)
		public static final String ESTADO_NOTIFICA_PENDIENTE_DE_ENVIO = "pendiente_envio";
		public static final String ESTADO_NOTIFICA_PENDIENTE_DE_COMPARECENCIA = "pendiente_sede"; //Cuando existe un número de días naturales que estará disponible el envío para su comparecencia desde la sede electrónica del Punto de Acceso General (Carpeta Ciudadana) antes de enviar a otro medio alternativo de entrega.
		public static final String ESTADO_NOTIFICA_REHUSADA = "Rehusada"; //Cuando la comunicación o notificación es rechazada por el interesado. También se dará este estado cuando después de los intentos de entrega estipulados por ley, no comparece el interesado. (sólo notificaciones)
		public static final String ESTADO_NOTIFICA_FECHA_ENVIO_PROGRAMADO = "envio_programado"; //Cuando la comunicación o notificación se encuentra en espera de ser enviada en la fecha indicada por el usuario.
		public static final String ESTADO_NOTIFICA_SIN_INFORMACION = "sin_informacion";	
		public static final String ESTADO_NOTIFICA_EXPIRADA = "expirada";
	}
	
	public interface NOTIFICACIONES_ERROR{		
		public static final String NOTIFICA_DUPLICADA = "Referencia emisor duplicada para ese Organismo";
		public static final String NOTIFICA_HASH_NO_VALIDO = "No se corresponde el sha1 del documento con el contenido";
		public static final String NOTIFICA_NIF_NO_VALIDO_DEH = "El valor del NIF para la DEH está vacío";
		public static final String NOTIFICA_NO_EXISTE_API_KEY = "No existe esa api_key en nuestro sistema";
		public static final String NOTIFICA_SIA_NO_ENCONTRADO = "Código SIA no encontrado";
		public static final String NOTIFICA_API_KEY_SIN_PERMISOS_PARA_ESE_ORGANISMO = "El organismo no tiene permisos de acceso con esa api_key";
		
		//Errores de Notifica que no consigo leer, cuando hay un error salta directmante una excepcion soap
//		4000 Petición XML errónea
//		4010 Campo Organismo Emisor incorrecto
//		4011 Campo DIR3 del Organismo Emisor no encontrado
//		4012 Campo Organismo Emisor vacío
//		4020 Campo Referencia Origen demasiado largo
//		4021 Campo Referencia Origen no encontrado
//		4150 Campo Contenido del Documento no encontrado
//		4151 Campo Normalizado vacío
//		4152 Campo Normalizado no válido
//		4153 Campo Generar CSV vacío
//		4154 Campo Generar CSV erróneo
//		4160 Campo Referencia Origen Duplicada para ese Organismo Emisor
//		4200 Campo NIF erróneo
//		4201 Campo NIF Vacío (DEPRECATED)
//		4210 Campo Nombre / Razón Social vacío
//		4211 Campo Nombre demasiado largo
//		4220 Campo Apellidos vacío
//		4221 Campo Apellidos demasiado largo
//		4230 Campo Teléfono vacío (DEPRECATED)
//		4231 Campo Teléfono demasiado largo
//		4240 Campo Email vacío
//		4241 Campo Email demasiado largo
//		4300 Campo Hash del Documento no encontrado
//		4301 Campo Hash no válido
//		4310 Demasiado documentos enviados
//		4311 No se están enviando documento en el envío
//		4320 El Hash introducido no es válido para el documento PDF enviado
//		4330 Tamaño demasiado grande del documento enviado
//		4400 Demasiado destinatarios para un mismo envío
//		4401 Es necesario que existan destinatarios para un envío
//		4402 Valor erróneo para la prioridad del servicio (normal o urgente)
//		4403 Campo de la Prioridad de servicio no encontrada
//		4500 Campo concepto no encontrado
//		4501 Campo concepto demasiado largo
//		4510 Campo tipo envío vacío
//		4511 Campo tipo envío demasiado erróneo (notificacion o comunicación)
//		4520 Agrupación vacía (DEPRECATED)
//		4521 Agrupación errónea (DEPRECATED)
//		4530 Código SIA vacío
//		4531 Código SIA erróneo
//		4532 Campo fecha programada erróneo
//		4533 Formato de fecha no válido para la fecha programada
//		4534 Código SIA no pertenece al DIR3 del Organismo Emisor
//		4535 Formato del código SIA erróneo
//		4560 Error con el NIF introducido para el envío a la DEH
//		4561 Dirección errónea para la DEH (DEPRECATED)
//		4562 NIF no autorizado para ese procedimiento en el censo de la DEH
//		4600 Tipo Domicilio Incorrecto
//		4601 Dirección no válida, falta Número de casa o Punto kilométrico
//		4602 Tipo de domicilio vacío
//		4604 Tipo de domicilio concreto no válido
//		4610 Nombre vía vacío (Domicilios nacionales) / Domicilio vacío (Domicilios Extranjeros)
//		4620 Tipo de Vía vacío
//		4630 Tipo de numeración vacío
//		4631 Tipo de numeración no válido
//		4632 El número de casa debe ser un valor numérico
//		4640 Población vacía
//		4650 Código Postal vacío
//		4660 Apartado de correos vacío
//		4670 Municipio vacío
//		4671 Código de Municipio no válido
//		4680 Provincia vacío
//		4681 Código Provincia no válido
//		4690 País vacío
//		4691 Código País no válido
//		4695 Línea 1 Domicilio No Normalizado vacía
//		4696 Línea 2 Domicilio no normalizado demasiado larga
//		4697 Línea 1 domicilio no normalizado demasiado larga
//		4901 Identificador de envío vacío
//		4902 Identificador de envío no encontrado
//		4903 Identificador de envío sin datados
//		4904 Identificador de envío sin certificaciones
//		6101 Número de contrato de correos vacío (DEPRECATED)
//		6102 Código de cliente de correos vacío (DEPRECATED)
//		6103 Vigencia de contrato de correos vacía (DEPRECATED)
//		6104 Vigencia de contrato con el CIE vacía (DEPRECATED)
//		6105 CIE no encontrado
//		6106 CIE no válido
//		6107 Vigencia de contrato CIE no válida (DEPRECATED)
//		6201 Fecha de caducidad del envío necesaria para envíos exclusivos a Carpeta/Sede
//		6202 Fecha de caducidad no válida (YYYY-MM-DD)
	}
	



}
