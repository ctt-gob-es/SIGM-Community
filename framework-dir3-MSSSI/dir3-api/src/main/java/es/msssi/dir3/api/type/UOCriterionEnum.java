/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.type;

/**
 * Enumerados para las constantes de los nombres de los criterios de las
 * búsquedas.
 * 
 * @author cmorenog
 * 
 */
public class UOCriterionEnum extends CriterionEnum {

    private static final long serialVersionUID = -6615854045641957655L;
    /** Identificador de la tabla. */
    public static final String TABLE_DIR_UNIDAD_ORGANICA = "UO";
    /** Identificador de la unidad orgánica. */
    public static final UOCriterionEnum UO_ID = new UOCriterionEnum(
	TABLE_DIR_UNIDAD_ORGANICA, "UO_ID", "C_ID_UD_ORGANICA");
    /** Nombre de la unidad orgánica. */
    public static final UOCriterionEnum UO_NOMBRE = new UOCriterionEnum(
	TABLE_DIR_UNIDAD_ORGANICA, "UO_NOMBRE", "C_DNM_UD_ORGANICA");
    /** Código externo de la Entidad Pública. */
    public static final UOCriterionEnum UO_ID_EXTERNO_FUENTE = new UOCriterionEnum(
	TABLE_DIR_UNIDAD_ORGANICA, "UO_ID_EXTERNO_FUENTE", "C_ID_EXTERNO_FUENTE");
    /** Estado de la unidad orgánica. */
    public static final UOCriterionEnum UO_ESTADO = new UOCriterionEnum(
	TABLE_DIR_UNIDAD_ORGANICA, "UO_ESTADO", "C_ID_ESTADO");
    /** Identificador de la unidad orgánica superior. */
    public static final UOCriterionEnum UO_ID_UNIDAD_ORGANICA_SUPERIOR = new UOCriterionEnum(
	TABLE_DIR_UNIDAD_ORGANICA, "UO_ID_UNIDAD_ORGANICA_SUPERIOR", "C_ID_DEP_UD_SUPERIOR");
    /** Nombre de la unidad orgánica superior. */
    public static final UOCriterionEnum UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR = new UOCriterionEnum(
	TABLE_DIR_UNIDAD_ORGANICA, "UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR",
	"DENOM_UNIDAD_SUP_JERARQUICA");

    /**
     * Constructor.
     * 
     * @param table
     *            Nombre de la tabla.
     * @param name
     *            Nombre del enumerado.
     * @param value
     *            Valor del enumerado.
     */
    protected UOCriterionEnum(String table, String name, String value) {
	super(table, name, value);
    }

    /**
     * Obtiene la constante asociada al valor.
     * 
     * @param value
     *            Valor de la constante
     * @return Constante.
     */
    public static UOCriterionEnum getCriterion(
	String value) {
	return (UOCriterionEnum) StringValuedEnum.getEnum(
	    UOCriterionEnum.class, value);
    }

    /**
     * Obtiene la constante asociada al nombre.
     * 
     * @param nombre
     *            Valor de la constante
     * @return Constante.
     */
    public static UOCriterionEnum getNameCriterion(
	String nombre) {
	return (UOCriterionEnum) StringValuedEnum.getEnumName(
	    UOCriterionEnum.class, nombre);
    }
}
