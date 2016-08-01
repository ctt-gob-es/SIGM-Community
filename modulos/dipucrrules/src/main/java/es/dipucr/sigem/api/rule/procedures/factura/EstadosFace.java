package es.dipucr.sigem.api.rule.procedures.factura;

public class EstadosFace {

	/**
	 * FLUJO ORDINARIO
	 */
	//Nombre público Registrada
	public static String REGISTRADA = "1200";
	public static String RECIBIDA_RCF = "1300";
	public static String VERIFICADA_RCF = "1400";
	public static String RECIBIDA_DESTINO = "2100";
	public static String CONFORMADA = "2300";
	public static String RECONOCIDA_OBLIG_PAGO = "2400";
	
	public static String PAGADA = "2500";
	public static String RECHAZADA = "2600";
	public static String ANULADA = "3100";
	
	
	/**
	 * FLUJO ANULACIÓN
	 */
	public static String NO_SOLICITADA_ANULACION = "4100";
	public static String SOLICITADA_ANULACION = "4200";
	public static String ACEPTADA_ANULACION = "4300";
	public static String RECHAZADA_ANULACION = "4400";
}
