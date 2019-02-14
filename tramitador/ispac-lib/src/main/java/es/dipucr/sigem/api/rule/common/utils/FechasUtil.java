package es.dipucr.sigem.api.rule.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class FechasUtil {

	public static String SIMPLE_DATE_PATTERN = "dd/MM/yyyy";
	public static String SIMPLE_DATE_PATTERN_HOUR = "dd/MM/yyyy HH:mm:ss";
	public static String QUERY_DATE_PATTERN = "yyyy-MM-dd";
	public static String QUERY_DATE_PATTERN_WITHTIME = "yyyy-MM-dd HH:mm:ss";
	public static String DOC_DATE_PATTERN = "d 'de' MMMM 'de' yyyy";
	public static long MS_POR_DIA = DateUtils.MILLIS_PER_DAY;
	public static int MES_ENERO = 1;
	
	public static SimpleDateFormat defaultSdf = new SimpleDateFormat(SIMPLE_DATE_PATTERN);
	
	/**
	 * Devuelve el año actual
	 */
	public static int getAnyoActual(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}
	
	/**
	 * Devuelve el mes actual
	 */
	public static int getMesActual(){
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * Añade tiempo especificado a la fecha pasada como parámetro
	 * @param fecha
	 * @param dias
	 */
	private static Date addTime(Date fecha, int tipoTiempo, int tiempoSumar){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(tipoTiempo, tiempoSumar);
		return calendar.getTime();
	}
	
	/**
	 * Añade los segundos especificados a la fecha pasada como parámetro
	 * @param fecha
	 * @param dias
	 */
	public static Date addSegundos(Date fecha, int segundos){
		return addTime(fecha, Calendar.SECOND, segundos);
	}
	
	/**
	 * Añade los días especificados a la fecha pasada como parámetro
	 * @param fecha
	 * @param dias
	 */
	public static Date addDias(Date fecha, int dias){
		return addTime(fecha, Calendar.DAY_OF_MONTH, dias);
	}
	
	/**
	 * Añade los meses especificados a la fecha pasada como parámetro
	 * @param fecha
	 * @param meses
	 */
	public static Date addMeses(Date fecha, int meses){
		return addTime(fecha, Calendar.MONTH, meses);
	}
	
	/**
	 * Añade los anios especificados a la fecha pasada como parámetro
	 * @param fecha
	 * @param meses
	 */
	public static Date addAnios(Date fecha, int anios){
		return addTime(fecha, Calendar.YEAR, anios);
	}
	
	/**
	 * Obtiene la diferencia en días entre dos fechas
	 * @param fecha
	 * @param dias
	 */
	public static int getDiferenciaEnDias(Date fechaInicial, Date fechaFinal){
		int dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime())/MS_POR_DIA);
		return dias;
	}
	
	/**
	 * Devuelve la fecha formateada con el patrón pasado como parámetro
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getFormattedDate(Date date, String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * Devuelve la fecha formateada para su uso en consultas a la BBDD
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getFormattedDateForQuery(Date date){
		return getFormattedDate(date, QUERY_DATE_PATTERN);
	}
	
	/**0
	 * Devuelve la fecha formateada para su uso en consultas a la BBDD
	 * Añade hora, minutos y segundos
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getFormattedDateTimeForQuery(Date date){
		return getFormattedDate(date, QUERY_DATE_PATTERN_WITHTIME);
	}
	
	/**
	 * Devuelve la fecha formateada con el patrón por defecto
	 * @param date
	 * @return
	 */
	public static String getFormattedDate(Date date){
		return defaultSdf.format(date);
	}
	
	/**
	 * Devuelve un vector de tres posiciones con el día, el mes y el año
	 * a partir de la fecha pasada como parámetro 
	 * @param fecha
	 * @return
	 */
	public static FechaDesglosada getFechaDesglosada(Date fecha){
		
		Calendar calendarInicio = Calendar.getInstance();
		calendarInicio.setTime(fecha);
		FechaDesglosada oFecha = new FechaDesglosada();
		oFecha.setDia(calendarInicio.get(Calendar.DAY_OF_MONTH));
		oFecha.setMes(calendarInicio.get(Calendar.MONTH) + 1);
		oFecha.setAnio(calendarInicio.get(Calendar.YEAR));
		return oFecha;
	}
	
	/**
	 * Devuelve un objeto Date a partir de un dia, me y año
	 * @param dia
	 * @param mes
	 * @param anio
	 * @return
	 */
	public static Date getDate(int dia, int mes, int anio){
		Calendar calendar = Calendar.getInstance();
		calendar.set(anio, mes - 1, dia);
		return calendar.getTime();
	}

	/**
	 * Devuelve si un día es fin de semana
	 * @param fecha
	 * @return
	 */
	public static boolean esFinDeSemana(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int diaSemana = calendar.get(Calendar.DAY_OF_WEEK); 
		if (diaSemana == Calendar.SUNDAY || diaSemana == Calendar.SATURDAY){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Devuelve la diferencia entre dos fechas
	 * @param dFechaNac
	 * @return [eCenpri-Felipe #62] Devolvemos fecha desglosada en vez de Date
	 */
	public static FechaDesglosada getDiferencia(Date dFechaInicial, Date dFechaFinal){
		
		int anios = 0, meses = 0, dias = 0;
		FechaDesglosada fdFechaNac = getFechaDesglosada(dFechaInicial);
		FechaDesglosada fdFechaActual = getFechaDesglosada(dFechaFinal);
		
		//Obtenemos años, meses y días
		anios = fdFechaActual.getAnio() - fdFechaNac.getAnio();
		meses = fdFechaActual.getMes() - fdFechaNac.getMes();
		dias = fdFechaActual.getDia() - fdFechaNac.getDia();
		
		//Disminuimos los meses si los días son negativos
		if (dias < 0){
			dias = 30 + dias;//son negativos
			meses--;
		}
		//Disminuimos los años si los meses son negativos
		if (meses < 0){
			meses = 12 + meses;//son negativos
			anios--;
		}
		//Devolvemos el date
		//[eCenpri-Felipe #62]
//		return getDate(dias, meses, anios);
		return new FechaDesglosada(dias, meses, anios);
	}
	
	/**
	 * Devuelve la edad a partir de la fecha de nacimiento
	 * @param dFechaNac
	 * @return [eCenpri-Felipe #62] Devolvemos fecha desglosada en vez de Date
	 */
	public static FechaDesglosada getEdad(Date dFechaNac){
		
		return getDiferencia(dFechaNac, new Date());
	}
	
	/**
	 * Devuelve la diferencia en años entre dos fechas
	 * @param dFechaNac
	 * @return
	 * [eCenpri-Felipe #62] Cambios nuevo método getDiferencia
	 */
	public static int getDiferenciaEnAnios(Date dFechaInicial, Date dFechaFinal){
		
		FechaDesglosada fdDiferencia = getDiferencia(dFechaInicial, dFechaFinal);
		return fdDiferencia.getAnio();
	}
	
	/**
	 * Devuelve la diferencia en meses entre dos fechas
	 * @param dFechaNac
	 * @return
	 * [eCenpri-Felipe #62] Cambios nuevo método getDiferencia
	 */
	public static int getDiferenciaEnMeses(Date dFechaInicial, Date dFechaFinal){
		
		FechaDesglosada fdDiferencia = getDiferencia(dFechaInicial, dFechaFinal);
		int meses = (fdDiferencia.getAnio() * 12) + fdDiferencia.getMes();
		return meses;
	}
	
	/**
	 * Devuelve el número de días que tiene el mes
	 * @param mes
	 * @param ano
	 * @return
	 */
	public static int getNumDiasMes(int mes, int ano){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(ano, mes - 1, 1);
		
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * Devuelve la fecha en formato Date a partir de la
	 * cadena introducida como parámetro y el pattern
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException 
	 */
	public static Date convertToDate(String date, String pattern) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(date);
	}
	
	/**
	 * Devuelve un Date a partir de la fecha recibida como cadena
	 * @param sDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertToDate(String sDate) throws Exception{
		return defaultSdf.parse(sDate);
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar convertToCalendar(Date date){ 
	  Calendar cal = Calendar.getInstance();
	  cal.setTime(date);
	  return cal;
	}
	
	/**
	 * [dipucr-Felipe #888]
	 * Comprueba si la fecha pasada por parámetro tiene la hora vacía
	 * @param date
	 * @return
	 */
	public static boolean tieneHoraVacia(Date date){
		
		Calendar calendarFecha = Calendar.getInstance();
		calendarFecha.setTime(date);
		
		boolean bEsVacia = false;
		if (calendarFecha.get(Calendar.HOUR) == 0 && calendarFecha.get(Calendar.MINUTE) == 0
				&& calendarFecha.get(Calendar.SECOND) == 0){
			bEsVacia = true;
		}
		return bEsVacia;
		
	}
}
