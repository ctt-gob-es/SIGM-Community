package es.msssi.tecdoc.fwktd.dir.ws.object;

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
@XmlType(name = "OperadorCriterioUOEnum")
@XmlEnum
public enum OperadorCriterioUOEnum implements Serializable {
	
	EQUAL("="), NOT_EQUAL("!="), LIKE("like"),
	LESS_THAN("<"), EQUAL_OR_LESS_THAN("<="),
	GREATER_THAN(">"),
	EQUAL_OR_GREATER_THAN(">="), IN("in");
	
	private final String value;
	
	/**
	 * Constructor.
	 * 
	 * @param v
	 *            Valor del enumerado.
	 */
	OperadorCriterioUOEnum(String v) {
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
	 * @param v
	 * 			valor del enumeral que se quiere consultar.
	 * @return OperadorCriterioUOEnum
	 * 			enumeral.
	 */
	public static OperadorCriterioUOEnum fromValue(String v) {
		for (OperadorCriterioUOEnum c : OperadorCriterioUOEnum.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
