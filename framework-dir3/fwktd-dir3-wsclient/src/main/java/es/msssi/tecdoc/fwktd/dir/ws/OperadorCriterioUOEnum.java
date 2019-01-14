
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OperadorCriterioUOEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="OperadorCriterioUOEnum">
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
@XmlType(name = "OperadorCriterioUOEnum")
@XmlEnum
public enum OperadorCriterioUOEnum {

    EQUAL,
    NOT_EQUAL,
    LIKE,
    LESS_THAN,
    EQUAL_OR_LESS_THAN,
    GREATER_THAN,
    EQUAL_OR_GREATER_THAN,
    IN;

    public String value() {
        return name();
    }

    public static OperadorCriterioUOEnum fromValue(String v) {
        return valueOf(v);
    }

}
