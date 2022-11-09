package ieci.tdw.ispac.ispaclib.common.constants;

/**
 * Constantes que identifican Estados de Firma
 *
 */
public class SignStatesConstants {

	public static final String SIN_FIRMA = "00";
	@Deprecated // [dipucr-Felipe] No se usa y provoca muchas confusiones con el pendiente de circuito de firma 
	public static final String PENDIENTE_FIRMA = "01";
	public static final String FIRMADO = "02";
	@Deprecated
	public static final String FIRMADO_CON_REPAROS = "03";
	public static final String RECHAZADO = "04";
	public static final String PENDIENTE_CIRCUITO_FIRMA = "05";
	public static final String SELLADO = "06";//[dipucr-Felipe #1143]
	public static final String PENDIENTE_PORTAFIRMAS = "07";//[dipucr-Felipe #1246]

}
