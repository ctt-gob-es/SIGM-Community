/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for CriterionOperatorEnum.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="CriterionOperatorEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EQUAL"/>
 *     &lt;enumeration value="NOT_EQUAL"/>
 *     &lt;enumeration value="LIKE"/>
 *     &lt;enumeration value="LESS_THAN"/>
 *     &lt;enumeration value="EQUAL_OR_LESS_THAN"/>
 *     &lt;enumeration value="GREATER_THAN"/>
 *     &lt;enumeration value="EQUAL_OR_GREATER_THAN"/>
 *     &lt;enumeration value="IN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CriterionOperatorEnum")
@XmlEnum
public enum CriterionOperatorEnum {

    EQUAL, NOT_EQUAL, LIKE, LESS_THAN, EQUAL_OR_LESS_THAN, GREATER_THAN, EQUAL_OR_GREATER_THAN, IN;

    public String value() {
	return name();
    }

    public static CriterionOperatorEnum fromValue(
	String v) {
	return valueOf(v);
    }

}
