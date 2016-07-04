/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.errors;

/**
 * Clase que recoge las constantes de error, que se mostrarán en las excepciones
 * y en el log.
 * 
 * @author cmorenog
 */
public class ErrorConstants {
    /** Mensaje de error para excepciones. */

    public static final String GET_COUNT_OFFICE_ERROR_MESSAGE =
	"Error recuperando el número de oficinas";
    public static final String GET_COUNT_UNIT_ERROR_MESSAGE =
	"Error recuperando el número de unidades orgánicas";
    public static final String GET_COUNT_UPDATE_ERROR_MESSAGE =
	"Error recuperando el número de actualizaciones";
    public static final String GET_OFFICE_ERROR_MESSAGE = "Error recuperando una oficina";
    public static final String GET_UNIT_ERROR_MESSAGE = "Error recuperando una unidad orgánica";
    public static final String GET_UPDATE_ERROR_MESSAGE = "Error recuperando una actualización";
    public static final String GET_LAST_UPDATE_ERROR_MESSAGE =
	"Error recuperando la última actualización actualización";
    public static final String GET_ALL_OFFICES_ERROR_MESSAGE =
	"Error recuperando todas las oficinas";
    public static final String GET_ALL_UNITS_ERROR_MESSAGE =
	"Error recuperando todas las unidades orgánicas";
    public static final String GET_ALL_UPDATES_ERROR_MESSAGE =
	"Error recuperando todas las actualizaciones";
    public static final String FIND_OFFICES_ERROR_MESSAGE = "Error en la búsqueda de oficinas";
    public static final String FIND_UNITS_ERROR_MESSAGE = "Error en la búsqueda de unidades";
    public static final String FIND_UPDATES_ERROR_MESSAGE =
	"Error en la búsqueda de actualizaciones";
    public static final String EXISTS_OFFICE_ERROR_MESSAGE =
	"Error comprobando la existencia de una oficina";
    public static final String EXISTS_UNIT_ERROR_MESSAGE =
	"Error comprobando la existencia de una unidad orgánica";
    public static final String EXISTS_UPDATE_ERROR_MESSAGE =
	"Error comprobando la existencia de una actualización";
    public static final String SAVE_UPDATE_ERROR_MESSAGE = "Error guardando una actualización";
    public static final String DELETE_UPDATE_ERROR_MESSAGE = "Error eliminando una actualización";
    public static final String UPDATE_UPDATE_ERROR_MESSAGE = "Error modificando una actualización";
    public static final String START_TRANSACTION_ERROR = "Error iniciando transacción";
    public static final String COMMIT_TRANSACTION_ERROR = "Error en el commit de la transacción";
    public static final String END_TRANSACTION_ERROR = "Error cerrando la transacción";
    public static final String CONVERT_XMLTOOFFICE_ERROR =
	"Error convirtiendo el xml en una lista de oficinas.";
    public static final String CONVERT_XMLTOSERVICES_ERROR =
	"Error convirtiendo el xml en una lista de servicios.";
    public static final String CONVERT_XMLTOCONTACTS_ERROR =
	"Error convirtiendo el xml en una lista de contactos.";
    public static final String CONVERT_XMLTORELATIONSSIR_ERROR =
	"Error convirtiendo el xml en una lista de relaciones sir.";
    public static final String CONVERT_XMLTORELATIONS_ERROR =
	"Error convirtiendo el xml en una lista de relaciones.";
    public static final String CONVERT_XMLTOHISTORIC_ERROR =
	"Error convirtiendo el xml en una lista de históricos.";
    public static final String CONVERT_XMLTOUNITS_ERROR =
	"Error convirtiendo el xml en una lista de unidades orgánicas.";
    public static final String GET_FILE_UNIT_UPDATESERVICE_ERROR =
	"Error recuperando el fichero de unidades orgánicas.";
    public static final String GET_FILE_OFFICE_UPDATESERVICE_ERROR =
	"Error recuperando el fichero de oficinas.";
    public static final String UNZIP_ERROR_MESSAGE = "Error descomprimiendo un fichero.";
    public static final String IO_ERROR_MESSAGE = "Error leyendo un fichero.";
    public static final String SAVE_OFFICE_ERROR_MESSAGE = "Insertando una oficina.";
    public static final String UPDATE_OFFICE_ERROR_MESSAGE = "Modificando una oficina.";
    public static final String INSERT_UNIT_ERROR_MESSAGE = "Insertando una unidad orgánica.";
    public static final String UPDATE_UNIT_ERROR_MESSAGE = "Modificando una unidad orgánica.";
    public static final String INSERT_HISTORIES_UNIT_ERROR_MESSAGE =
	"Insertando el histórico de una unidad orgánica.";
    public static final String INSERT_HISTORIES_OFFICE_ERROR_MESSAGE =
	"Insertando el histórico de una oficina.";
    public static final String GET_RELATIONSHIP_ERROR_MESSAGE =
	"Error recuperando una relación de oficina y unidad orgánica.";
    public static final String EXISTS_RELATIONSHIP_ERROR_MESSAGE =
	"Error comprobando la existencia de una relación de oficina y unidad orgánica.";
    public static final String INSERT_RELATIONSHIP_ERROR_MESSAGE =
	"Error insertando una relación de oficina y unidad orgánica.";
    public static final String UPDATE_RELATIONSHIP_ERROR_MESSAGE =
	"Error modificando una relación de oficina y unidad orgánica.";
    public static final String INSERTUPDATE_OFFICE_ERROR_MESSAGE =
	"Error insertando o modificando la oficina:";
    public static final String INSERTUPDATE_UNIT_ERROR_MESSAGE =
	"Error insertando o modificando la unidad:";
    public static final String GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR =
	    "Error recuperando el fichero de unidades no orgánicas.";
    public static final String DELETE_RELATIONSHIPREGISTER_ERROR_MESSAGE = 
	    "Error borrando una relación de oficina y unidad orgánica en registro.";
}