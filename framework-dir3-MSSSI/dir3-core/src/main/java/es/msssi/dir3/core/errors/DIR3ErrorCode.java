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

import core.error.ErrorCode;

/**
 * Clase que recoge los códigos de error referentes a operaciones del dir3.
 * 
 * @author cmorenog
 */
public class DIR3ErrorCode extends ErrorCode {
    /** Constante de código DIR001: Error obteniendo el número de oficinas. */
    public static final DIR3ErrorCode GET_COUNT_OFFICE_ERROR = new DIR3ErrorCode(
	"DIR001", "GET COUNT OFFICE");
    /**
     * Constante de código DIR002: Error obteniendo el número de unidades
     * orgánicas.
     */
    public static final DIR3ErrorCode GET_COUNT_UNIT_ERROR = new DIR3ErrorCode(
	"DIR002", "GET COUNT UNIT");
    /**
     * Constante de código DIR003: Error obteniendo el número de
     * actualizaciones.
     */
    public static final DIR3ErrorCode GET_COUNT_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR003", "GET COUNT UDPATE");
    /** Constante de código DIR004: Error obteniendo una oficina. */
    public static final DIR3ErrorCode GET_OFFICE_ERROR = new DIR3ErrorCode(
	"DIR004", "GET OFFICE");
    /** Constante de código DIR005: Error obteniendo una unidad. */
    public static final DIR3ErrorCode GET_UNIT_ERROR = new DIR3ErrorCode(
	"DIR005", "GET UNIT");
    /** Constante de código DIR006: Error obteniendo una actualización. */
    public static final DIR3ErrorCode GET_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR006", "GET UPDATE");
    /** Constante de código DIR007: Error obteniendo la última actualización. */
    public static final DIR3ErrorCode GET_LAST_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR007", "GET LAST UPDATE");
    /** Constante de código DIR008: Error obteniendo todas las oficinas. */
    public static final DIR3ErrorCode GET_ALL_OFFICES_ERROR = new DIR3ErrorCode(
	"DIR008", "GET ALL OFFICES");
    /**
     * Constante de código DIR009: Error obteniendo todas las unidades
     * orgánicas.
     */
    public static final DIR3ErrorCode GET_ALL_UNITS_ERROR = new DIR3ErrorCode(
	"DIR009", "GET ALL UNITS");
    /** Constante de código DIR010: Error obteniendo todas las actualizaciones. */
    public static final DIR3ErrorCode GET_ALL_UDPATES_ERROR = new DIR3ErrorCode(
	"DIR010", "GET ALL UDPATES");
    /** Constante de código DIR011: Error en la búsqueda de oficinas. */
    public static final DIR3ErrorCode FIND_OFFICES_ERROR = new DIR3ErrorCode(
	"DIR011", "FIND OFFICES ERROR");
    /** Constante de código DIR012: Error en la búsqueda de unidades orgánicas. */
    public static final DIR3ErrorCode FIND_UNITS_ERROR = new DIR3ErrorCode(
	"DIR012", "FIND UNIT ERROR");
    /** Constante de código DIR013: Error en la búsqueda de actualizaciones. */
    public static final DIR3ErrorCode FIND_UPDATES_ERROR = new DIR3ErrorCode(
	"DIR013", "FIND UPDATE ERROR");
    /**
     * Constante de código DIR014: Error comprobando la existencia de una
     * oficina.
     */
    public static final DIR3ErrorCode EXISTS_OFFICE_ERROR = new DIR3ErrorCode(
	"DIR014", "EXISTS OFFICE ERROR");
    /**
     * Constante de código DIR015: Error comprobando la existencia de una unidad
     * orgánica.
     */
    public static final DIR3ErrorCode EXISTS_UNIT_ERROR = new DIR3ErrorCode(
	"DIR015", "EXISTS UNIT ERROR");
    /**
     * Constante de código DIR016: Error comprobando la existencia de una
     * actualización.
     */
    public static final DIR3ErrorCode EXISTS_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR016", "EXISTS UPDATE ERROR");
    /** Constante de código DIR017: Error guardando una actualización. */
    public static final DIR3ErrorCode SAVE_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR017", "SAVE UPDATE ERROR");
    /** Constante de código DIR018: Error eliminando una actualización. */
    public static final DIR3ErrorCode DELETE_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR018", "DELETE UPDATE ERROR");
    /** Constante de código DIR019: Error modificando una actualización. */
    public static final DIR3ErrorCode UPDATE_UPDATE_ERROR = new DIR3ErrorCode(
	"DIR019", "UPDATE UPDATE ERROR");
    /** Constante de código DIR020: Error convirtiendo xml a oficinas. */
    public static final DIR3ErrorCode CONVERT_XMLTOOFFICE_ERROR = new DIR3ErrorCode(
	"DIR020", "CONVERT XML TO OFFICE ERROR");
    /** Constante de código DIR021: Error convirtiendo xml a oficinas. */
    public static final DIR3ErrorCode CONVERT_XMLTOSERVICES_ERROR = new DIR3ErrorCode(
	"DIR021", "CONVERT XML TO SERVICES ERROR");
    /** Constante de código DIR022: Error convirtiendo xml a oficinas. */
    public static final DIR3ErrorCode CONVERT_XMLTOCONTACTS_ERROR = new DIR3ErrorCode(
	"DIR022", "CONVERT XML TO CONTACTS ERROR");
    /** Constante de código DIR023: Error convirtiendo xml a relaciones sir. */
    public static final DIR3ErrorCode CONVERT_XMLTORELATIONSSIR_ERROR = new DIR3ErrorCode(
	"DIR023", "CONVERT XML TO RELATIONS SIR ERROR");
    /** Constante de código DIR024: Error convirtiendo xml a relaciones. */
    public static final DIR3ErrorCode CONVERT_XMLTORELATIONS_ERROR = new DIR3ErrorCode(
	"DIR024", "CONVERT XML TO RELATIONS ERROR");
    /** Constante de código DIR025: Error convirtiendo xml a relaciones. */
    public static final DIR3ErrorCode CONVERT_XMLTOHISTORIC_ERROR = new DIR3ErrorCode(
	"DIR025", "CONVERT XML TO HISTORIC ERROR");
    /** Constante de código DIR026: Error convirtiendo xml a relaciones. */
    public static final DIR3ErrorCode CONVERT_XMLTOUNITS_ERROR = new DIR3ErrorCode(
	"DIR026", "CONVERT XML TO UNIST ERROR");
    /**
     * Constante de código DIR027: Error obteniendo la última actualización en
     * el servicio de actualizacion.
     */
    public static final DIR3ErrorCode GET_LAST_UPDATE_UPDATESERVICE_ERROR = new DIR3ErrorCode(
	"DIR027", "GET LAST UPDATE IN UPDATE SERVICE");
    /**
     * Constante de código DIR028: Error recuperando el fichero de unidades
     * orgánicas.
     */
    public static final DIR3ErrorCode GET_FILE_UNIT_UPDATESERVICE_ERROR = new DIR3ErrorCode(
	"DIR028", "GET FILE UNIT UPDATE SERVICE ERROR");
    /**
     * Constante de código DIR029: Error recuperando el fichero de oficinas
     * orgánicas.
     */
    public static final DIR3ErrorCode GET_FILE_OFFICE_UPDATESERVICE_ERROR = new DIR3ErrorCode(
	"DIR029", "GET FILE OFFICE UPDATE SERVICE ERROR");
    /**
     * Constante de código DIR030: Error recuperando el fichero de oficinas
     * orgánicas.
     */
    public static final DIR3ErrorCode UNZIP_ERROR_MESSAGE = new DIR3ErrorCode(
	"DIR030", "UNZIP ERROR");
    /**
     * Constante de código DIR031: Error recuperando el fichero de oficinas
     * orgánicas.
     */
    public static final DIR3ErrorCode IO_ERROR_MESSAGE = new DIR3ErrorCode(
	"DIR031", "IO ERROR");
    /**
     * Constante de código DIR032: Error borrando una oficina.
     */
    public static final DIR3ErrorCode SAVE_OFFICE_ERROR = new DIR3ErrorCode(
	"DIR032", "SAVE OFFICE ERROR");
    /**
     * Constante de código DIR033: Error guardando una oficina.
     */
    public static final DIR3ErrorCode UPDATE_OFFICE_ERROR_MESSAGE = new DIR3ErrorCode(
	"DIR033", "UPDATE OFFICE ERROR");
    /**
     * Constante de código DIR034: Error guardando una oficina.
     */
    public static final DIR3ErrorCode INSERT_UNIT_ERROR = new DIR3ErrorCode(
	"DIR034", "INSERT UNIT ERROR");
    /**
     * Constante de código DIR035: Error guardando una oficina.
     */
    public static final DIR3ErrorCode UPDATE_UNIT_ERROR = new DIR3ErrorCode(
	"DIR035", "UPDATE UNIT ERROR");
    /**
     * Constante de código DIR036: Error insertando histónico de una unidad.
     */
    public static final DIR3ErrorCode INSERT_HISTORIES_UNIT_ERROR = new DIR3ErrorCode(
	"DIR036", "INSERT HISTORIES UNIT ERROR");
    /**
     * Constante de código DIR037: Error insertando histónico de una oficina.
     */
    public static final DIR3ErrorCode INSERT_HISTORIES_OFFICE_ERROR = new DIR3ErrorCode(
	"DIR037", "INSERT HISTORIES OFFICE ERROR");
    /** Constante de código DIR038: Error obteniendo una relación. */
    public static final DIR3ErrorCode GET_RELATIONSHIP_ERROR = new DIR3ErrorCode(
	"DIR038", "GET RELATIONSHIP");
    /**
     * Constante de código DIR039: Error comprobando la existencia de una
     * relación.
     */
    public static final DIR3ErrorCode EXISTS_RELATIONSHIP_ERROR = new DIR3ErrorCode(
	"DIR039", "EXISTS RELATIONSHIP ERROR");
    /**
     * Constante de código DIR040: Error insertando relación.
     */
    public static final DIR3ErrorCode INSERT_RELATIONSHIP_ERROR = new DIR3ErrorCode(
	"DIR040", "INSERT RELATIONSHIP ERROR");
    /**
     * Constante de código DIR041: Error modificando una relación.
     */
    public static final DIR3ErrorCode UPDATE_RELATIONSHIP_ERROR = new DIR3ErrorCode(
	"DIR041", "UPDATE RELATIONSHIP ERROR");
    /**
     * Constante de código DIR028: Error recuperando el fichero de unidades no 
     * orgánicas.
     */
    public static final DIR3ErrorCode GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR = new DIR3ErrorCode(
		"DIR042", "GET FILE NO ORG UNIT UPDATE SERVICE ERROR");
    /**
     * Constante de código DIR043: Error borrando una relación en registro.
     */
    public static final DIR3ErrorCode DELETE_RELATIONSHIPREGISTER_ERROR  = new DIR3ErrorCode(
		"DIR043", "DELETE REGISTER RELATIONSHIP ERROR");
    /**
     * Constructor con código de error y mensaje.
     * 
     * @param code
     *            Código interno del error.
     * @param message
     *            Mensaje descriptivo del error.
     */
    protected DIR3ErrorCode(String code, String message) {
	super(code, message);
    }
}