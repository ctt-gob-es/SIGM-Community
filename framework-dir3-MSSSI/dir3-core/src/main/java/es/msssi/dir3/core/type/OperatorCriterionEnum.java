/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.type;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Enumerados para las constantes de los operadores de los criterios de las
 * búsquedas.
 * 
 * @author cmorenog
 * 
 */
@XmlType(name = "CriterionOperatorEnum")
@XmlEnum
public enum OperatorCriterionEnum implements Serializable {
    /**
     * Igual.
     */
    EQUAL("="),
    /**
     * No igual.
     */
    NOT_EQUAL("!="),
    /**
     * like.
     */
    LIKE("like"),
    /**
     * Menor que.
     */
    LESS_THAN("<"),
    /**
     * Menor o igual que.
     */
    EQUAL_OR_LESS_THAN("<="),
    /**
     * Mayor que.
     */
    GREATER_THAN(">"),
    /**
     * Mayor o igual que.
     */
    EQUAL_OR_GREATER_THAN(">="),
    /**
     * En.
     */
    IN("in");

    private String value = null;

    /**
     * Constructor.
     * 
     * @param v
     *            Valor del enumerado.
     */
    OperatorCriterionEnum(String v) {
	value = v;
    }

    /**
     * Devuelve el valor del enumeral.
     * 
     * @return value
     * 
     */
    public String value() {
	return value;
    }

    /**
     * Devuelve el valor del enumeral.
     * 
     * @return value
     * 
     */
    public String getValue() {
	return value;
    }

    /**
     * Devuelve el enumeral del valor que se pasa como parámetro.
     * 
     * @param v
     *            valor del enumeral que se quiere consultar.
     * @return OperadorCriterioUOEnum enumeral.
     */
    public static OperatorCriterionEnum fromValue(
	String v) {
	for (OperatorCriterionEnum c : OperatorCriterionEnum.values()) {
	    if (c.value.equals(v)) {
		return c;
	    }
	}
	throw new IllegalArgumentException(
	    v);
    }
}
