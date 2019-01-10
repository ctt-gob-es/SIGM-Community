package es.dipucr.bdns.constantes;

public class Constantes {
	
	public static final String DIR3_ESPAÑA = "ES";
	
	public static final String NIF_IGAE = "S2826015F";
	public static final String NOMBRE_IGAE = "IGAE";
	
	public static final String DIR3_CENPRI = "LA0006599";
	public static final String NOMBRE_CENPRI = "CENTRO PROVINCIAL DE INFORMÁTICA";
	
	public static final String DIR3_DIPUCR = "L02000013";
	public static final String NOMBRE_DIPUCR = "DIPUTACIÓN PROVINCIAL DE CIUDAD REAL";
	public static final String TIPOMOVIMIENTO_ALTA = "ALTA";
	public static final Object TIPOMOVIMIENTO_MODIFICACION = "MODIFICACION";
	public static final Object TIPOMOVIMIENTO_BAJA = "BAJA";
	public static final String NUTS_PROV_CIUDADREAL = "ES422";
	
	public interface TIPO_TERCERO{
		/** PERSONAS FÍSICAS QUE NO DESARROLLAN ACTIVIDAD ECONÓMICA **/
		public static final String FISICAS_NO_ECO = "FSA";
		/** PERSONAS JURÍDICAS QUE NO DESARROLLAN ACTIVIDAD ECONÓMICA **/
		public static final String JURIDICAS_NO_ECO = "JSA";
		/** PYME Y PERSONAS FÍSICAS QUE DESARROLLAN ACTIVIDAD ECONÓMICA **/
		public static final String PYME_FIS_SI_ECO = "PFA";
		/** GRAN EMPRESA **/
		public static final String GRAN_EMPRESA = "GRA";
	}
	
	/** Código en la columna NUM_ACTO de SPAC_DT_DOCUMENTOS para identificar el documento resolución de una subvención **/
	public static final String COD_DTDOC_NUMACTO_BDNS = "BDNS";

	/**
	 * Permite al requirente el envío de la información relativa a la normativa que rige la 
	 * gestión de la subvención o de cualquier otra ayuda pública.
	 * **/
	public static String COD_APP_CONVOCATORIAS = "BDNSCONVOC";
	
	/**
	 * Permite al requirente el envío de información relativa a los datos personales de cada uno de los terceros 
	 * sobre los que esté obligado a facilitar algún tipo de información (beneficiario de una subvención, sancionado, 
	 * inhabilitado o partícipe de un proyecto).
	 * **/
	public static String COD_APP_DATOSPERSONALES = "BDNSDATPER";
	
	/**
	 * Permite al requirente el envío de la información relativa a concesiones (subvención, préstamo, aval…), 
	 * pagos a los beneficiarios y proyectos (ejecución material de la actividad con detalle sobre los compromisos 
	 * asumidos en el tiempo por sus ejecutores). Se han unificado estos tres conceptos en un mismo servicio web, 
	 * pero en una misma petición (llamada al servicio) se admitirá únicamente información, de concesiones, de pagos o bien de proyectos.
	 * **/
	public static String COD_APP_CONCESIONES = "BDNSCONCPAGPRY";
	
	/**
	 * Permite al requirente el envío de información relativa a devoluciones voluntarias a iniciativa del beneficiario, 
	 * y reintegros procedentes de subvención. Se han unificado estos dos conceptos en un mismo servicio web, pero en una misma 
	 * petición (llamada al servicio) se admitirá únicamente información, o bien de devoluciones o de reintegros.
	 * **/
	public static String COD_APP_DEVOLUCIONES = "BDNSDEVOLREINT";
	
	/**
	 * Permite al requirente el envío de información relativa a resoluciones firmes del procedimiento sancionador 
	 * (sanciones pecuniarias impuestas en aplicación de la LGS) así como las inhabilitaciones. Se han unificado estos 
	 * dos conceptos en un mismo servicio web, pero en una misma petición (llamada al servicio) se admitirá únicamente 
	 * información, o bien de sanciones o de inhabilitaciones.
	 * **/
	public static String COD_APP_CODSANCIONADOR = "BDNSSANCINH";
		
}
