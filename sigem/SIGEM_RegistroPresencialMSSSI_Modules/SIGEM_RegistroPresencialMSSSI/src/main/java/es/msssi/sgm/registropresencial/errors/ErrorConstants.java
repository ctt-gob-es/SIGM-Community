/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/


package es.msssi.sgm.registropresencial.errors;

/**
 * Clase que recoge las constantes de error, que se mostrarán en las excepciones
 * y en el log.
 * 
 * @author jortizs
 */
public class ErrorConstants {
    /** Mensaje de error para excepciones de Faces: Conversion Error. */
    public static final String CONVERSION_ERROR_FACES_MESSAGE = "Conversion Error";

    /** Mensaje de error para asuntos: El asunto no existe. */
    public static final String ASUNT_DOES_NOT_EXISTS_MESSAGE = "El asunto no existe";
    /** Mensaje de error para organismos: El organismo no existe. */
    public static final String ORGANISM_DOES_NOT_EXISTS_MESSAGE = "El organismo no existe";
    /** Mensaje de error para interesados: El organismo no existe. */
    public static final String INTERESADO_DOES_NOT_EXISTS_MESSAGE = "El interesado no existe";
    /** Mensaje de error para grupos: El grupo no existe. */
    public static final String GROUP_DOES_NOT_EXISTS_MESSAGE = "El grupo no existe";
    /** Mensaje de error para usuarios: El usuario no existe. */
    public static final String USER_DOES_NOT_EXISTS_MESSAGE = "El usuario no existe";

    /**
     * Mensaje de error de manejo de libros: Error en la obtención de los
     * libros.
     */
    public static final String GET_BOOKS_ERROR_MESSAGE = "Error en la obtención de los libros";
    /** Mensaje de error de manejo de libros: Error en la obtención del libro. */
    public static final String GET_BOOK_ERROR_MESSAGE = "Error en la obtención del libro";
    /** Mensaje de error de manejo de libros: Error en la apertura del libro. */
    public static final String OPEN_BOOK_ERROR_MESSAGE = "Error en la apertura del libro";
    /** Mensaje de error de manejo de libros: Error en el cierre del libro. */
    public static final String CLOSE_BOOK_ERROR_MESSAGE = "Error en el cierre del libro";
    /**
     * Mensaje de error de manejo de libros: El libro no estaba cargado
     * previamente.
     */
    public static final String BOOK_NOT_PREVIOUSLY_LOADED_ERROR_MESSAGE =
	"El libro no estaba cargado previamente";
    /**
     * Mensaje de error de manejo de libros: No ha sido posible obtener la
     * información del libro.
     */
    public static final String GET_INFORMATION_BOOK_ERROR_MESSAGE =
	"No ha sido posible obtener la información del libro";
    /**
     * Mensaje de error de manejo de libros: Error al obtener los datos de
     * sesión del libro.
     */
    public static final String GET_SESSION_INFORMATION_BOOK_ERROR_MESSAGE =
	"Error al obtener los datos de sesión del libro";

    /**
     * Mensaje de error en tareas de exportación: Error al obtener la imagen de
     * la cabecera.
     */
    public static final String GET_HEADER_IMAGE_ERROR_MESSAGE =
	"Error al obtener la imagen de la cabecera";
    /**
     * Mensaje de error en tareas de exportación: Error al exportar a un
     * documento Excel.
     */
    public static final String EXCEL_DOCUMENT_EXPORT_ERROR_MESSAGE =
	"Error al exportar a un documento Excel";
    /**
     * Mensaje de error en tareas de exportación: Error al escribir el documento
     * Excel en la respuesta.
     */
    public static final String WRITE_EXCEL_INTO_RESPONSE_ERROR_MESSAGE =
	"Error al escribir el documento Excel en la respuesta";
    /**
     * Mensaje de error en tareas de exportación: Error al exportar a un
     * documento PDF.
     */
    public static final String PDF_DOCUMENT_EXPORT_ERROR_MESSAGE =
	"Error al exportar a un documento PDF";
    /**
     * Mensaje de error en tareas de exportación: Error al escribir el documento
     * PDF en la respuesta.
     */
    public static final String WRITE_PDF_INTO_RESPONSE_ERROR_MESSAGE =
	"Error al escribir el documento PDF en la respuesta";

    /**
     * Mensaje de error en tareas con registros de entrada: Error en la creación
     * de un nuevo registro de entrada.
     */
    public static final String CREATE_INPUT_REGISTER_ERROR_MESSAGE =
	"Error en la creación de un nuevo registro de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la
     * modificación de un nuevo registro de entrada.
     */
    public static final String UPDATE_INPUT_REGISTER_ERROR_MESSAGE =
	"Error en la modificación de un nuevo registro de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en el guardado
     * de un nuevo registro de entrada.
     */
    public static final String SAVE_INPUT_REGISTER_ERROR_MESSAGE =
	"Error en el guardado de un nuevo registro de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la copia de
     * un nuevo registro de entrada.
     */
    public static final String COPY_INPUT_REGISTER_ERROR_MESSAGE =
	"Error en la copia de un nuevo registro de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error al obtener el
     * registro de entrada.
     */
    public static final String GET_INPUT_REGISTER_ERROR_MESSAGE =
	"Error al obtener el registro de entrada";

    /**
     * Mensaje de error en tareas con registros de salida: Error al obtener el
     * registro de salida.
     */
    public static final String GET_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error al obtener el registro de salida";

    /**
     * Mensaje de error en tareas con registros de salida: Error en la creación
     * de un nuevo registro de salida.
     */
    public static final String CREATE_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error en la creación de un nuevo registro de salida";
    /**
     * Mensaje de error en tareas con registros de salida: Error en la
     * modificación de un nuevo registro de salida.
     */
    public static final String UPDATE_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error en la modificación de un nuevo registro de salida";
    /**
     * Mensaje de error en tareas con registros de salida: Error en el guardado
     * de un nuevo registro de salida.
     */
    public static final String SAVE_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error en el guardado de un nuevo registro de salida";
    /**
     * Mensaje de error en tareas con registros de salida: Error en la copia de
     * un nuevo registro de salida.
     */
    public static final String COPY_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error en la copia de un nuevo registro de salida";

    /**
     * Mensaje de error en tareas con registros: Error en la marcar el registro
     * de entrada como sólo lectura.
     */
    public static final String LOCK_REGISTER_ERROR_MESSAGE =
	"Error en la marcar el registro de entrada como sólo lectura";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la
     * obtención de los registros de entrada.
     */
    public static final String GET_INPUT_REGISTERS_ERROR_MESSAGE =
	"Error en la obtención de los registros de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la carga de
     * los registros de entrada.
     */
    public static final String LOAD_INPUT_REGISTER_ERROR_MESSAGE =
	"Error en la carga de los registros de entrada";

    /**
     * Mensaje de error en tareas con registros de salida: Error en la carga de
     * los registros de salida.
     */
    public static final String LOAD_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error en la carga de los registros de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la
     * obtención del histórico de registros.
     */
    public static final String GET_HISTORICAL_REGISTER_ERROR_MESSAGE =
	"Error en la obtención del histórico de registros";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la
     * obtención del histórico de registros o del registro.
     */
    public static final String GET_HISTORICAL_OR_REGISTER_ERROR_MESSAGE =
	"Error en la obtención del histórico de registros o del registro de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Error en la búsqueda
     * de los registros del usuario.
     */
    public static final String NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE =
	"Error en la búsqueda de los registros del usuario";
    /**
     * Mensaje de error en tareas con registros de salida: Error en la búsqueda
     * de los registros del usuario.
     */
    public static final String NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE =
	"Error en la búsqueda de los registros del usuario";
    /**
     * Mensaje de error en tareas con registros de entrada: Error al obtener el
     * listado de oficinas de registro.
     */
    public static final String GET_INPUT_REGISTER_OFFICE_LIST_ERROR_MESSAGE =
	"Error al obtener el listado de oficinas de registro";
    /**
     * Mensaje de error en tareas con registros de entrada: Error al validar el
     * Id del registro de entrada.
     */
    public static final String VALIDATE_INPUT_REGISTER_ERROR_MESSAGE =
	"Error al validar el Id del registro de entrada";
    /**
     * Mensaje de error en tareas con registros de entrada: Se ha producido un
     * error en la validación de la fecha de registro.
     */
    public static final String VALIDATE_INPUT_REGISTER_DATE_ERROR_MESSAGE =
	"Se ha producido un error en la validación de la fecha de registro";
    /**
     * Mensaje de error en tareas con registros de entrada: La fecha de registro
     * no puede ser superior a la fecha actual ni inferior a 1970.
     */
    public static final String INPUT_REGISTER_DATE_RANGE_ERROR_MESSAGE =
	"La fecha de registro no puede ser superior a la fecha actual ni inferior a 1970";

    /**
     * Mensaje de error en tareas con consultas de registros de entrada: Error
     * en la apertura de la consulta de registros.
     */
    public static final String OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE =
	"Error en la apertura de la consulta de registros de entrada";
    /**
     * Mensaje de error en tareas con consultas de registros de salida: Error en
     * la apertura de la consulta de registros.
     */
    public static final String OPEN_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE =
	"Error en la apertura de la consulta de registros de salida";
    /**
     * Mensaje de error en tareas con consultas de registros de entrada: Error
     * en la carga de la consulta de registros.
     */
    public static final String LOAD_INPUT_REGISTER_QUERY_ERROR_MESSAGE =
	"Error en la carga de la consulta de registros";

    /**
     * Mensaje de error en tareas con consultas de registros de salida: Error en
     * la carga de la consulta de registros.
     */
    public static final String LOAD_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE =
	"Error en la carga de la consulta de registros";

    /**
     * Mensaje de error en tareas con consultas de registros de entrada: Error
     * en el cierre de la consulta de registros.
     */
    public static final String CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE =
	"Error en el cierre de la consulta de registros de entrada";
    /**
     * Mensaje de error en tareas con consultas de registros de salida: Error en
     * el cierre de la consulta de registros.
     */
    public static final String CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE =
	"Error en el cierre de la consulta de registros de salida";
    /**
     * Mensaje de error en tareas con consultas de registros de entrada: Error
     * al rellenar los parámetros de la consulta.
     */
    public static final String FILL_QUERY_PARAMETERS_ERROR_MESSAGE =
	"Error al rellenar los parámetros de la consulta";

    /**
     * Mensaje de error en tareas con consultas de registros de entrada: Error
     * abriendo registros.
     */
    public static final String OPENR_INPUT_REGISTER_ERROR_MESSAGE =
	"Error abriendo registros de entrada";

    /**
     * Mensaje de error en tareas con consultas de registros de salida: Error
     * abriendo registros.
     */
    public static final String OPENR_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error abriendo registros de salida";

    /**
     * Mensaje de error en tareas con consultas de registros de entrada: Error
     * cerrando registros.
     */
    public static final String CLOSE_INPUT_REGISTER_ERROR_MESSAGE =
	"Error cerrando registros de entrada";

    /**
     * Mensaje de error en tareas con consultas de registros de salida: Error
     * cerrando registros.
     */
    public static final String CLOSE_OUTPUT_REGISTER_ERROR_MESSAGE =
	"Error cerrando registros de salida";

    /**
     * Mensaje de error en distribuciones: Error en la apertura de la consulta
     * de registros.
     */
    public static final String GET_DISTRIBUTION_ERROR_MESSAGE =
	"Error al obtener las distribuciones";
    /**
     * Mensaje de error en distribuciones: Error al obtener los resultados de la
     * búsqueda de distribuciones.
     */
    public static final String GET_DISTRIBUTION_RESULTS_ERROR_MESSAGE =
	"Error al obtener los resultados de la búsqueda de distribuciones";
    /**
     * Mensaje de error en distribuciones: Error al mostrar las alertas de
     * distribución.
     */
    public static final String SHOW_DISTRIBUTION_ALERTS_ERROR_MESSAGE =
	"Error al mostrar las alertas de distribución";
    /** Mensaje de error en distribuciones: Error al archivar distribución. */
    public static final String ARCHIVE_DISTRIBUTION_ERROR_MESSAGE =
	"Error archivando la distribución";
    /** Mensaje de error en distribuciones: Error al aceptar distribución. */
    public static final String ACCEPT_DISTRIBUTION_ERROR_MESSAGE =
	"Error aceptando la distribución";
    /** Mensaje de error en distribuciones: Error al rechazar distribución. */
    public static final String REJECT_DISTRIBUTION_ERROR_MESSAGE =
	"Error rechazando la distribución";
    /**
     * Mensaje de error en distribuciones: Error al cambiar destino
     * distribución.
     */
    public static final String CHANGE_DISTRIBUTION_ERROR_MESSAGE =
	"Error cambiando el destino de la distribución";
    /** Mensaje de error en distribuciones: Error redistribuyendo distribución. */
    public static final String REDISTRIBUTION_DISTRIBUTION_ERROR_MESSAGE =
	"Error redistribuyendo la distribución";

    /**
     * Mensaje de error en operaciones de sesión de usuario: Error en la
     * autenticación del usuario.
     */
    public static final String AUTHENTICATION_ERROR_MESSAGE =
	"Error en la autenticación del usuario";
    /**
     * Mensaje de error en operaciones de sesión de usuario: Error al cerrar la
     * sesión del usuario.
     */
    public static final String CLOSE_SESSION_ERROR_MESSAGE =
	"Error al cerrar la sesión del usuario";
    /**
     * Mensaje de error en operaciones de sesión de usuario: Error en la
     * validación del usuario.
     */
    public static final String USER_VALIDATION_ERROR_MESSAGE = "Error en la validación del usuario";

    /**
     * Mensaje de error en operaciones con oficinas: Error al obtener las
     * oficinas.
     */
    public static final String GET_OFFICES_ERROR_MESSAGE = "Error al obtener las oficinas";
    /**
     * Mensaje de error en operaciones con oficinas: Error al validar el código
     * de oficina.
     */
    public static final String VALIDATION_OFFICE_CODE_ERROR_MESSAGE =
	"Error al validar el código de oficina";

    /**
     * Mensaje de error en listados de datos: Error al obtener la información de
     * los listados.
     */
    public static final String GET_INFORMATION_LISTS_ERROR_MESSAGE =
	"Error al obtener la información de los listados";
    /**
     * Mensaje de error en listados de datos: Error al obtener la lista de tipos
     * de transporte.
     */
    public static final String GET_TRANSPORT_TYPES_LIST_ERROR_MESSAGE =
	"Error al obtener la lista de tipos de transporte";
    /**
     * Mensaje de error en listados de datos: Error al obtener la lista de
     * oficinas.
     */
    public static final String GET_OFFICES_LIST_ERROR_MESSAGE =
	"Error al obtener la lista de oficinas";
    /**
     * Mensaje de error en listados de datos: Error al obtener la lista de
     * asuntos por oficina.
     */
    public static final String GET_OFFICE_ASUNTS_LIST_ERROR_MESSAGE =
	"Error al obtener la lista de asuntos por oficina";
    /**
     * Mensaje de error en listados de datos: Error al obtener la lista de
     * organismos.
     */
    public static final String GET_ORGANISMS_LIST_ERROR_MESSAGE =
	"Error al obtener la lista de organismos";

    /** Mensaje de error genéricos: Error en la validación de los parámetros. */
    public static final String PARAMETERS_VALIDATION_ERROR_MESSAGE =
	"Error en la validación de los parámetros";
    /** Mensaje de error genéricos: Error en la sesión. */
    public static final String SESSION_ERROR_MESSAGE = "Error en la sesión";
    /** Mensaje de error genéricos: Error al obtener el listado de atributos. */
    public static final String ATTRIBUTES_ERROR_MESSAGE =
	"Error al obtener el listado de atributos";
    /** Mensaje de error genéricos: La tabla de datos está vacía. */
    public static final String NULL_DATA_TABLE_ERROR_MESSAGE = "La tabla de datos está vacía";
    /** Mensaje de error genéricos: Error al obtener los permisos. */
    public static final String GET_PERMISSIONS_ERROR_MESSAGE = "Error al obtener los permisos";
    /**
     * Mensaje de error genéricos: La página a la que se intenta redireccionar
     * no existe.
     */
    public static final String NULL_PAGE_TO_REDIRECT_ERROR_MESSAGE =
	"La página a la que se intenta redireccionar no existe";

    /** Mensaje de error de manejo de bbdd: Error al cerrar la sesión de bbdd. */
    public static final String CLOSE_SESSION_BD_ERROR_MESSAGE = "Error al cerrar la sesión";
    /** Mensaje de error de manejo de bbdd: Error al abrir la sesión de bbdd. */
    public static final String OPEN_SESSION_BD_ERROR_MESSAGE = "Error al abrir la sesión";
    /** Mensaje de error de usuarios: Error al recuperar el usuario. */
    public static final String GET_USER_ERROR_MESSAGE = "Error al recuperar el usuario";

    /**
     * Mensaje de error en generación de informes: No hay datos para generar el
     * informe.
     */
    public static final String REPORT_DATA_ERROR = "No hay datos para generar el informe";

    /**
     * Mensaje de error en tareas con registros: Error mostrando registros
     * asociados.
     */
    public static final String GET_ASOC_REGISTER_ERROR_MESSAGE =
	"Error en la recuperación de registros asociados";

    /**
     * Mensaje de error en tareas con registros de entrada: Error en la marcar
     * el registro de entrada como abierto.
     */
    public static final String OPEN_REGISTER_ERROR_MESSAGE = "Error al abrir un registro";

    /**
     * Mensaje de error en tareas con registros de entrada: Error en la marcar
     * el registro como cerrado.
     */
    public static final String CLOSED_REGISTER_ERROR_MESSAGE = "Error al cerrar un registro";

    /**
     * Mensaje de error en tareas con registros: Error eliminando asociación.
     */
    public static final String DELETE_ASOC_REGISTER_ERROR_MESSAGE =
	"Error eliminando la asociación de registros";
    /**
     * Mensaje de error en tareas con registros: Error asociando registros.
     */
    public static final String SAVE_ASOC_REGISTER_ERROR_MESSAGE = "Error asociando registros";

    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * unidad de tramitación por defecto.
     */
    public static final String SEARCH_DESTINATION_REG_INTERCHANGE_ERROR_MESSAGE =
	"Error recuperando unidad de tramitación por defecto";

    /**
     * Mensaje de error en tareas con intercambio registral: Error enviando el
     * asiento registral.
     */
    public static final String SEND_REG_INTERCHANGE_ERROR_MESSAGE =
	"Error enviando el asiento registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error consultando
     * el histórico de entrada del intercambio registral.
     */
    public static final String GET_HIST_INPUT_REG_INTERCHANGE_ERROR_MESSAGE =
	"Error consultando el histórico de entrada del intercambio registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error consultando
     * el histórico de salida del intercambio registral.
     */
    public static final String GET_HIST_OUTPUT_REG_INTERCHANGE_ERROR_MESSAGE =
	"Error consultando el histórico de salida del intercambio registral";
    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * la bandeja de entrada.
     */
    public static final String GET_INBOX_INTERCHANGE_ERROR_MESSAGE =
	"Error recuperando la bandeja de entrada del intercambio registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * la bandeja de salida.
     */
    public static final String GET_OUTBOX_INTERCHANGE_ERROR_MESSAGE =
	"Error recuperando la bandeja de salida del intercambio registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * el número de registros pendientes en la bandeja de entrada.
     */
    public static final String GET_PENDING_INBOX_INTERCHANGE_ERROR_MESSAGE =
	"Error recuperando el número de registros pendientes " +
	"en la bandeja de entrada del intercambio registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error aceptando
     * registros.
     */
    public static final String ACCEPT_INBOX_INTERCHANGE_ERROR_MESSAGE =
	"Error aceptando registros del intercambio registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error rechazando
     * registros.
     */
    public static final String REJECT_INBOX_INTERCHANGE_ERROR_MESSAGE =
	"Error rechazando registros del intercambio registral";

    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * un asiento registral.
     */
    public static final String GET_INPUT_INTERCHANGE_ERROR_MESSAGE =
	"Error recuperando un asiento registral";
    /**
     * Mensaje de error en tareas con intercambio registral: Error reenviando un
     * asiento registral.
     */
    public static final String FORWARD_INBOX_INTERCHANGE_ERROR_MESSAGE =
	"Error reenviando un asiento registral";
    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * la lista de entidades registrales.
     */
    public static final String GET_LIST_ENTID_REGISTER_ERROR_MESSAGE =
	"Error recuperando la lista de entidades registrales";
    /**
     * Mensaje de error en tareas con intercambio registral: Entidad registral
     * no existe.
     */
    public static final String ENTITY_DOES_NOT_EXISTS_MESSAGE = "La entidad registral no existe";
    /**
     * Mensaje de error en tareas con intercambio registral: Unidad Tramitadora
     * no existe.
     */
    public static final String UNID_DOES_NOT_EXISTS_MESSAGE = "La unidad tramitadora no existe";
    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * la lista de unidades tramitadoras.
     */
    public static final String GET_LIST_UNID_REGISTER_ERROR_MESSAGE =
	"Error recuperando la lista de unidades tramitadoras";

    /**
     * Mensaje de error en tareas con registros: Error recuperando los
     * documentos de un registro.
     */
    public static final String GET_REGISTER_DOCUMENTS_ERROR_MESSAGE =
	"Error recuperando los documentos de un registro";

    /**
     * Mensaje de error en tareas con registros: Error descargando un fichero.
     */
    public static final String DOWNLOADFILE_ERROR_MESSAGE = "Error descargando un fichero";

    /**
     * Mensaje de error en tareas con registros: Error adjuntando un fichero.
     */
    public static final String UPLOADFILE_ERROR_MESSAGE = "Error adjuntando un fichero";
    /**
     * Mensaje de error en tareas de distribución: Error contando el número de
     * distribuciones.
     */
    public static final String GET_COUNT_DISTRIBUTION_ERROR_MESSAGE =
	"Error contand el número de distribuciones de la búsqueda";
    
    /**
     * Mensaje de error en tareas con registros: Error iniciando transacción.
     */
    public static final String START_TRANSACTION_ERROR = "Error iniciando transacción";
    /**
     * Mensaje de error en tareas con registros: Error en el commit de la transacción.
     */
    public static final String COMMIT_TRANSACTION_ERROR = "Error en el commit de la transacción";
    /**
     * Mensaje de error en tareas con registros: Error cerrando la transacción.
     */
    public static final String END_TRANSACTION_ERROR = "Error cerrando la transacción";
    /**
     * Mensaje de error en tareas con registros: Error obteniendo la lista de CCAA.
     */
    public static final String GET_LIST_CCAA_MESSAGE = "Error obteniendo la lista de CCAA";
    /**
     * Mensaje de error en tareas con registros: Error obteniendo la lista de provincias.
     */
    public static final String GET_LIST_PROV_MESSAGE = "Error obteniendo la lista de provincias";
    /**
     * Mensaje de error en tareas con intercambio registral: Error recuperando
     * una unidad tramitadora.
     */
    public static final String GET_UNID_REGISTER_ERROR_MESSAGE =
	"Error recuperando una unidad tramitadora";
    /**
     * Mensaje de error en tareas con registros: Error recuperando
     * la lista de unidades .
     */
    public static final String GET_LIST_UNIT_REGISTER_ERROR_MESSAGE =
	"Error recuperando la lista de unidades orgánicas";
    
    public static final String WITHOUT_PERMITS_ERROR_MESSAGE =
    		"El usuario no tiene permisos para acceder a la aplicación";

    public static final String CREATE_QRCODE_ERROR_MESSAGE =
	    "Error generando el código QR de la etiqueta";
    /**
     * Mensaje de error en mover registro de libro.
     */
    public static final String MOVE_REGISTER_ERROR = "No se puede mover el registro de libro";
    /**
     * Mensaje de error en recuperar la dirección de la oficina.
     */
    public static final String GET_DIR_MESSAGE = "No se puede recuperar la dirección de la oficina";
    /**
     * Mensaje de error en recuperar los campos extendidos.
     */
    public static final String GET_EXTENDEDFIELDS_ERROR = "No se pueden recuperar los datos extendidos";
    /**
     * Mensaje de error en borrado de documento.
     */
    public static final String DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE = "No se puede borrar el documento.";
    /**
     * Mensaje de error recuperando un intercambio registral.
     */
    public static final String GET_INTERCHANGE_ERROR_MESSAGE = "Error recuperando un intercambio registral";
    /**
     * Mensaje de error actualizando DCO.
     */
    public static final String UPDATE_UNID_REGISTER_ERROR_MESSAGE = "Error actualizando DCO";
    /**
     * Mensaje de error cargando usuarios en SIGM.
     */
    public static final String INSERT_USERS_ERROR_MESSAGE = "Error cargando usuarios en SIGM";
    /**
     * Mensaje de error modificando un documento.
     */
    public static final String UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE = "Error modificando un documento";
    /**
     * Mensaje de error recuperando estado de un registro.
     */
    public static final String GET_STATE_INPUT_REGISTER_ERROR_MESSAGE = "Error recuperando estado de un registro";
    
    /**
     * Mensaje de error al crear un usuario en bbdd.
     */
    public static final String INSERT_USER_ERROR = "Error al crear usuario en bbbdd";
    
    /**
     * Mensaje de error al consultar al existencia de usuario.
     */
    public static final String QUERY_EXIST_USER = "Error al consultar la existencia de usuario en bbbdd";           
    
}