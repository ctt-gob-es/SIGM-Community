/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author domingo
 *
 */
@Component
public class DateComponent {
	
	@Autowired
	private UtilComponent util;
	
	private final Log log = LogFactory.getLog(getClass());
	
	public static final String DEFAULT_FORMAT = "dd/MM/yyyy";

	public static final String HOUR_FORMAT = "HH:mm:ss";

	public static final String EXTENDED_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	/**
	 * @param fecha
	 * @return
	 */
	public Date stringToDate(String fecha) {
		return stringToDate(fecha, null);
	}

	/**
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public Date stringToDate(String fecha, String formato) {
		if (util.esVacioONulo(fecha)) {
			return null;
		}
		if (util.esVacioONulo(formato)) {
			formato = DEFAULT_FORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		try {
			return formatter.parse(fecha);
		} catch (ParseException e) {
			log.error("Error al formatear fecha " + fecha, e);
			return null;
		}
	}
	
	/**
	 * @param fecha
	 * @return
	 */
	public String dateToString(Date fecha) {
		return dateToString(fecha, null);
	}
		
	/**
	 * Crea un objeto de tipo Date a partir de fecha y hora de tipo String
	 * @param fecha
	 * @param hora
	 * @return
	 */
	public Date dateComplete(String fecha, String hora) {
		return stringToDate(fecha + ' ' + hora, Util.EXTENDED_DATE_FORMAT);
	}
	/**
	 * @param fecha
	 * @param formato
	 * @return
	 */
	public String dateToString(Date fecha, String formato) {
		if (fecha == null) {
			return null;
		}
		if (util.esVacioONulo(formato)) {
			formato = DEFAULT_FORMAT;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(formato);
		return formatter.format(fecha);
	}
	
    /**
     * @param fecha
     * @param dias
     * @return
     */
    public Date addDays(Date fecha, int dias) {
        return addTime(fecha, dias, Calendar.DATE);
    }
	
    /**
     * @param fecha
     * @param minutos
     * @return
     */
    public Date addMinutes(Date fecha, int minutos) {
        return addTime(fecha, minutos, Calendar.MINUTE);
    }
    
    /**
     * @param fecha
     * @param segundos
     * @return
     */
    public Date addSeconds(Date fecha, int segundos) {
        return addTime(fecha, segundos, Calendar.SECOND);
    }
    
    /**
     * @param fecha
     * @param dias
     * @return
     */
    public Date addMonths(Date fecha, int meses) {
        return addTime(fecha, meses, Calendar.MONTH);
    }
    
    private Date addTime(Date fecha, int tiempo, int campo) {
    	if (fecha == null) {
    		return null;
    	}
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.add(campo, tiempo);
        return cal.getTime();
    }
    
    public Date obtenerFechaDesdeDeFiltros(String mes, String año){
    	Calendar cal = Calendar.getInstance();
    	if(Constants.FILTRO_MESES_ULTIMA_SEMANA.equals(mes)){
    		return addDays(cal.getTime(),-7);
    	}else if(Constants.FILTRO_MESES_ULTIMO_MES.equals(mes)){
    		return addDays(cal.getTime(),-30);
    	}else if(Constants.FILTRO_MESES_ULTIMAS_24_HORAS.equals(mes)){
    		return addDays(cal.getTime(),-1);
    	}else if(mes == null || mes.equals("") || Constants.FILTRO_MESES_TODAS.equals(mes)){
    		cal.set(Calendar.YEAR, 2010);
    		cal.set(Calendar.MONTH, 0);
    		cal.set(Calendar.DATE, 1);
    		cal.set(Calendar.HOUR_OF_DAY, 0);
    		cal.set(Calendar.MINUTE, 0);
    		cal.set(Calendar.SECOND, 0);
    		cal.set(Calendar.MILLISECOND, 0);
    		return cal.getTime();
    	}else{
    		cal.set(Calendar.YEAR, 2010);
    		cal.set(Calendar.MONTH, 0);
    		cal.set(Calendar.DATE, 1);
    		cal.set(Calendar.HOUR_OF_DAY, 0);
    		cal.set(Calendar.MINUTE, 0);
    		cal.set(Calendar.SECOND, 0);
    		cal.set(Calendar.MILLISECOND, 0);
    		return cal.getTime();
    	}
    }
    
    public Date obtenerFechaHastaDeFiltros(String mes, String año){
    	if(mes == null || "".equals(mes) || año == null || "".equals(año)){
    		return new Date();
    	}
    	Calendar cal = Calendar.getInstance();
    	if(Constants.FILTRO_MESES_ULTIMA_SEMANA.equals(mes) ||
    			Constants.FILTRO_MESES_ULTIMO_MES.equals(mes) || 
    			Constants.FILTRO_MESES_ULTIMAS_24_HORAS.equals(mes) ||
    			Constants.FILTRO_MESES_TODAS.equals(mes)){
    		return cal.getTime();
    	}else{
    		cal.set(Calendar.YEAR, Integer.parseInt(año));
    		cal.set(Calendar.MONTH, Integer.parseInt(mes)-1);
    		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    		cal.set(Calendar.HOUR_OF_DAY, 23);
    		cal.set(Calendar.MINUTE, 59);
    		cal.set(Calendar.SECOND, 59);
    		cal.set(Calendar.MILLISECOND, 999);
    		return cal.getTime();
    	}
    }
	
}
