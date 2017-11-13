package es.dipucr.bdns.constantes;

public class Constantes {
	public static final String NIFIGAE = "S2826015F";
	public static String nombreIGAE = "IGAE";
	
	public static final String DIR3Cenpri = "U03500057";
	public static String nombreCenpri = "SERVICIO INFORMÁTICO";
	
	public static final String DIR3Dipu = "U03500001";
	public static final String TIPOMOVIMIENTO_ALTA = "ALTA";
	public static final Object TIPOMOVIMIENTO_MODIFICACION = "MODIFICACION";
	public static final Object TIPOMOVIMIENTO_BAJA = "BAJA";
	public static String nombreDipu = "UNIVERSIDAD PÚBLICA DE NAVARRA";


	/**
	 * Permite al requirente el envío de la información relativa a la normativa que rige la 
	 * gestión de la subvención o de cualquier otra ayuda pública.
	 * **/
	public static String codConvocatoria = "BDNSCONVOC";
	
	/**
	 * Permite al requirente el envío de información relativa a los datos personales de cada uno de los terceros 
	 * sobre los que esté obligado a facilitar algún tipo de información (beneficiario de una subvención, sancionado, 
	 * inhabilitado o partícipe de un proyecto).
	 * **/
	public static String codDatosPersonales = "BDNSDATPER";
	
	/**
	 * Permite al requirente el envío de la información relativa a concesiones (subvención, préstamo, aval…), 
	 * pagos a los beneficiarios y proyectos (ejecución material de la actividad con detalle sobre los compromisos 
	 * asumidos en el tiempo por sus ejecutores). Se han unificado estos tres conceptos en un mismo servicio web, 
	 * pero en una misma petición (llamada al servicio) se admitirá únicamente información, de concesiones, de pagos o bien de proyectos.
	 * **/
	public static String codConcesiones = "BDNSCONCPAGPRY";
	
	/**
	 * Permite al requirente el envío de información relativa a devoluciones voluntarias a iniciativa del beneficiario, 
	 * y reintegros procedentes de subvención. Se han unificado estos dos conceptos en un mismo servicio web, pero en una misma 
	 * petición (llamada al servicio) se admitirá únicamente información, o bien de devoluciones o de reintegros.
	 * **/
	public static String codDevolucionesVol = "BDNSDEVOLREINT";
	
	/**
	 * Permite al requirente el envío de información relativa a resoluciones firmes del procedimiento sancionador 
	 * (sanciones pecuniarias impuestas en aplicación de la LGS) así como las inhabilitaciones. Se han unificado estos 
	 * dos conceptos en un mismo servicio web, pero en una misma petición (llamada al servicio) se admitirá únicamente 
	 * información, o bien de sanciones o de inhabilitaciones.
	 * **/
	public static String codProcSancionador = "BDNSSANCINH";
		
}
