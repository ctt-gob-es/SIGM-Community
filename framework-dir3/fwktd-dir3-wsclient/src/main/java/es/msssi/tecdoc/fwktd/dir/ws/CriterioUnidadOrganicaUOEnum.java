
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CriterioUnidadOrganicaUOEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CriterioUnidadOrganicaUOEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UO_ID"/>
 *     &lt;enumeration value="UO_NOMBRE"/>
 *     &lt;enumeration value="UO_ID_EXTERNO_FUENTE"/>
 *     &lt;enumeration value="UO_ID_UNIDAD_ORGANICA_SUPERIOR"/>
 *     &lt;enumeration value="UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CriterioUnidadOrganicaUOEnum")
@XmlEnum
public enum CriterioUnidadOrganicaUOEnum {

    UO_ID,
    UO_NOMBRE,
    UO_ID_EXTERNO_FUENTE,
    UO_ID_UNIDAD_ORGANICA_SUPERIOR,
    UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR;

    public String value() {
        return name();
    }

    public static CriterioUnidadOrganicaUOEnum fromValue(String v) {
        return valueOf(v);
    }

}
