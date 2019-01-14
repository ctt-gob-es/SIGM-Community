/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.vo;

import java.io.Serializable;

import es.msssi.dir3.core.type.OperatorCriterionEnum;
import es.msssi.dir3.core.type.UOCriterionEnum;

/**
 * Clase que implementa un criterio de búsqueda para las UO.
 * 
 * @author cmorenog
 * 
 */
public class CriterionUO implements Serializable {
    private static final long serialVersionUID = 5164807099767853323L;

    /**
     * Nombre del criterio.
     */
    private UOCriterionEnum name = null;
    /**
     * Operador.
     */
    private OperatorCriterionEnum operator = OperatorCriterionEnum.EQUAL;

    /**
     * Valor.
     */
    private String value = null;

    /**
     * Constructor.
     */
    public CriterionUO() {
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del criterio.
     * @param value
     *            Valor.
     */
    public CriterionUO(UOCriterionEnum name, String value) {
	this(name, OperatorCriterionEnum.EQUAL, value);
    }

    /**
     * Constructor.
     * 
     * @param name
     *            Nombre del criterio.
     * @param operator
     *            Operator.
     * @param value
     *            Valor.
     */
    public CriterionUO(UOCriterionEnum name, OperatorCriterionEnum operator, String value) {
	this();
	this.name = name;
	this.operator = operator;
	this.value = value;
    }

    /**
     * Obtiene el valor del parámetro name.
     * 
     * @return name valor del campo a obtener.
     * 
     * 
     */
    public UOCriterionEnum getName() {
	return name;
    }

    /**
     * Guarda el valor del parámetro name.
     * 
     * @param name
     *            valor del campo a guardar.
     */
    public void setName(
	UOCriterionEnum name) {
	this.name = name;
    }

    /**
     * Obtiene el valor del parámetro operator.
     * 
     * @return operator valor del campo a obtener.
     * 
     * 
     */
    public OperatorCriterionEnum getOperator() {
	return operator;
    }

    /**
     * Guarda el valor del parámetro operator.
     * 
     * @param operator
     *            valor del campo a guardar.
     */
    public void setOperator(
	OperatorCriterionEnum operator) {
	this.operator = operator;
    }

    /**
     * Obtiene el valor del parámetro value.
     * 
     * @return value valor del campo a obtener.
     * 
     * 
     */
    public String getValue() {
	return value;
    }

    /**
     * Guarda el valor del parámetro value.
     * 
     * @param value
     *            valor del campo a guardar.
     */
    public void setValue(
	String value) {
	this.value = value;
    }

}
