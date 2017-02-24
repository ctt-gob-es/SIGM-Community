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
 * Enumerados para las constantes de los operadores de los criterios de las
 * búsquedas.
 * 
 * @author cmorenog
 * 
 */
public class OperatorCriterionEnum extends StringValuedEnum {

    private static final long serialVersionUID = -5427563304712979383L;

    public static final OperatorCriterionEnum EQUAL = new OperatorCriterionEnum(
	"==", "=");
    public static final OperatorCriterionEnum NOT_EQUAL = new OperatorCriterionEnum(
	"!=", "!=");
    public static final OperatorCriterionEnum LIKE = new OperatorCriterionEnum(
	"=", "like");
    public static final OperatorCriterionEnum LESS_THAN = new OperatorCriterionEnum(
	"<", "<");
    public static final OperatorCriterionEnum EQUAL_OR_LESS_THAN = new OperatorCriterionEnum(
	"<=", "<=");
    public static final OperatorCriterionEnum GREATER_THAN = new OperatorCriterionEnum(
	">", ">");
    public static final OperatorCriterionEnum EQUAL_OR_GREATER_THAN = new OperatorCriterionEnum(
	">=", ">=");
    public static final OperatorCriterionEnum IN = new OperatorCriterionEnum(
	"in", "in");
    public static final OperatorCriterionEnum NULL = new OperatorCriterionEnum(
	"null", "IS NULL");
    public static final OperatorCriterionEnum NOTNULL = new OperatorCriterionEnum(
	"notnull", "IS NOT NULL");

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del enumerado.
     * @param value
     *            Valor del enumerado.
     */
    protected OperatorCriterionEnum(String name, String value) {
	super(name, value);
    }

    /**
     * Obtiene la constante asociada al valor.
     * 
     * @param value
     *            Valor de la constante.
     * @return Constante.
     */
    public static OperatorCriterionEnum getOperatorCriterion(
	String value) {
	return (OperatorCriterionEnum) StringValuedEnum.getEnum(
	    OperatorCriterionEnum.class, value);
    }
}
