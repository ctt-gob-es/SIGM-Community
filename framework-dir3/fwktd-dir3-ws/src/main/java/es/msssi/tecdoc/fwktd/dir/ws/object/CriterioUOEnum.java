package es.msssi.tecdoc.fwktd.dir.ws.object;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Enumerados para las constantes de los nombres de los criterios
 * de las búsquedas.
 *
 * @author cmorenog
 *
 */
@XmlType(name = "CriterioUnidadOrganicaUOEnum")
@XmlEnum
public enum CriterioUOEnum implements Serializable  {
	UO_ID("CODIGO_UNIDAD_ORGANICA"), UO_NOMBRE("NOMBRE_UNIDAD_ORGANICA"), UO_ID_EXTERNO_FUENTE("CODIGO_EXTERNO_FUENTE"),	
	UO_ID_UNIDAD_ORGANICA_SUPERIOR("CODIGO_UNIDAD_SUP_JERARQUICA"),UO_NOMBRE_UNIDAD_ORGANICA_SUPERIOR("DENOM_UNIDAD_SUP_JERARQUICA");

	private final String value;
	public static final String TABLE_DIR_UNIDAD_ORGANICA = "UO";
	public static final String TABLE_CAT_NIVEL_ADMINISTRACION = "CNA";
	public static final String TABLE_CAT_ESTADO_ENTIDAD = "CEA";

	public static final String TABLE_UNIDAD_ORGANICA_SUPERIOR = "UO_SUP";
	public static final String TABLE_UNIDAD_ORGANICA_PRINCIPAL = "UO_PRI";
	public static final String TABLE_UNIDAD_ORGANICA_EDP = "UO_EDP";

	
	/**
	 * Constructor.
	 * 
	 * @param v
	 *            Valor del enumerado.
	 */
	CriterioUOEnum(String v) {
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
	 * @return CriterioUOEnum
	 * 			enumeral.
	 */
	public static CriterioUOEnum fromValue(String v) {
		for (CriterioUOEnum c : CriterioUOEnum.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}
}
