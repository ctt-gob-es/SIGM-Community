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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.storage.domain.StoredRequest;
import es.seap.minhap.portafirmas.storage.util.StorageConstants;
import es.seap.minhap.portafirmas.web.beans.Month;
import es.seap.minhap.portafirmas.web.beans.Storage;
import es.seap.minhap.portafirmas.ws.eeutil.EeUtilConfigManager;

/**
 * @author domingo
 *
 */
@Component
public class UtilComponent {
	
	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * Devuelve los meses del año
	 * @return
	 */
	public ArrayList<Month> getMonths() {
		ArrayList<Month> monthList = new ArrayList<Month>();
		ArrayList<String> names = new ArrayList<String>();
		names.add(messages.getProperty("january"));
		names.add(messages.getProperty("february"));
		names.add(messages.getProperty("march"));
		names.add(messages.getProperty("april"));
		names.add(messages.getProperty("may"));
		names.add(messages.getProperty("june"));
		names.add(messages.getProperty("july"));
		names.add(messages.getProperty("august"));
		names.add(messages.getProperty("september"));
		names.add(messages.getProperty("october"));
		names.add(messages.getProperty("november"));
		names.add(messages.getProperty("december"));
		for (int i = 0; i < names.size(); i++) {
			Month month = new Month();
			month.setId(String.valueOf(i+1));
			month.setName(names.get(i));
			monthList.add(month);
		}
		return monthList;
	}
	
	public List<Month> getMonthsExtended() {
		ArrayList<Month> listaMeses = new ArrayList<Month>();

		Month todas = new Month();
		todas.setId("all");
		todas.setName("Todas");
		listaMeses.add(todas);

		Month ultimas24Horas = new Month();
		ultimas24Horas.setId("last24Hours");
		ultimas24Horas.setName("Últimas 24 horas");
		listaMeses.add(ultimas24Horas);
		
		Month ultimaSemana = new Month();
		ultimaSemana.setId("lastWeek");
		ultimaSemana.setName("Última semana");
		listaMeses.add(ultimaSemana);
		
		Month ultimoMes = new Month();
		ultimoMes.setId("lastMonth");
		ultimoMes.setName("Último mes");
		listaMeses.add(ultimoMes);
		
		listaMeses.addAll(this.getMonths());
		
		return listaMeses;
	}

	/**
	 * Devuelve los últimos años
	 * @return
	 */
	public ArrayList<String> getYears() {
		ArrayList<String> yearList = new ArrayList<String>();
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for (int i = currentYear; i >= Constants.FIRST_YEAR; i--) {
			yearList.add(String.valueOf(i));
		}
		return yearList;
	}
	
	/**
	 * @param storage
	 * @return
	 */
	public Map<String, String> getFilters(Storage storage) {
		Map<String, String> filters = new HashMap<String, String>();
		if(!Util.esVacioONulo(storage.getRemitter())) {
			filters.put(StorageConstants.REMITTER_FILTER, storage.getRemitter());
		}
		filters.put(StorageConstants.APPLICATION_FILTER, storage.getApplication());
		filters.put(StorageConstants.MONTH_FILTER, storage.getMonth());
		filters.put(StorageConstants.YEAR_FILTER, storage.getYear());
		return filters;
	}

	/**
	 * Cambia el id del mes por su nombre
	 * @param requestList
	 */
	public void getMonthNames(List<StoredRequest> requestList) {
		ArrayList<Month> months = getMonths();
		Month month = null;
		for (StoredRequest storedRequest : requestList) {
			month = months.get(Integer.valueOf(storedRequest.getMonth())-1);
			storedRequest.setMonth(month.getName());
		}
	}

	public List<String> getPageSizeList() {
		List<String> sizeList = new ArrayList<String>();
		for (int size = 40; size <= 100 ; size +=10) {
			sizeList.add(String.valueOf(size));
		}
		return sizeList;
	}
	
	/**
	 * Para enviar a la página de error con un mensaje personalizado
	 * @param error
	 * @param model
	 * @return modelo y vista
	 */
	public ModelAndView throwError(String error, ModelMap model) {
		model.addAttribute("errorMessage", error);
		return new ModelAndView("error", model);
	}
	
	/**
	 * Comprueba si el valor de un parámetro asociado a una configuración es 'S'
	 * @param idConf
	 * @param parametro
	 * @param eeUtilConfigManager
	 * @return
	 */
	public boolean parametroValorS (Long idConf, String parametro, EeUtilConfigManager eeUtilConfigManager) {
		Map<String, String> params = eeUtilConfigManager.cargarConfiguracion (idConf);
		String valorParam = params.get(parametro);
		return valorParam != null && valorParam.equals(Constants.C_YES);
	}
	
	/**
	 * @param objeto
	 * @return verdadero si el objeto es null o vacio
	 */
	public boolean esVacioONulo(Object objeto){
		boolean retorno = false;
		if(objeto == null){
			retorno = true;
		} else {
			if(objeto instanceof String){
				String campo = (String) objeto;
				if(campo.trim().equals("")){
					retorno = true;
				}
			}
		}
		return retorno;
	}
	
	/**
	 * @param objeto
	 * @return verdadero si el objeto contiene información
	 */
	public boolean noEsVacio(Object objeto) {
		return !esVacioONulo(objeto);
	}

	/**
	 * @param objeto
	 * @return vacio si es nulo o lo mismo si no
	 */
	public String vacioSiNulo(String objeto) {
		String retorno = "";
		if(objeto != null){
			retorno = (String) objeto ;
		}
		return retorno;
	}
	
	public boolean isNotEmpty(Collection<?> coll) {
		return !this.isEmpty(coll);
	}

	public boolean isEmpty(Collection<?> coll) {
		return (coll == null || coll.isEmpty());
	}
	
	public boolean convertStringToBoolean(String value) {
		boolean retorno = false;
		if (StringUtils.isNotEmpty(value)
			&& "S".equals(value)) {
			retorno = true;
		}
		return retorno;
	}
	
	public String getMime (byte[] datos) throws Exception{
		MagicMatch match;
		String mime = null;
		try {
			match = Magic.getMagicMatch(datos);
			mime = match.getMimeType();
		} catch (MagicParseException e) {
			throw new MagicParseException("No se ha podido obtener el Mime del documento ", e);
		} catch (MagicMatchNotFoundException e) {
			throw new MagicMatchNotFoundException("No se ha podido obtener el Mime del documento ", e);
		} catch (MagicException e) {
			throw new MagicException("No se ha podido obtener el Mime del documento ", e);
		}
		
		return mime;
	}
	
	/**
	 * Construye un nombre a partir del nombre original, un prefijo, un sufijo y una nueva extensión.
	 * Los parámetros nulos se ignoran.
	 * Si el parámetro "conservarExtension" es true, no se le quitará la extensión al nombre, si es false,
	 * se elimina todo a partir del último punto.
	 * @param originalName
	 * @param conservarExtension
	 * @param prefix
	 * @param suffix
	 * @param extension
	 * @return
	 */
	public String getNombreFichero (String originalName, boolean conservarExtension, String prefix, String suffix, String extension) {
		String name = originalName;
		if (!conservarExtension) {
			int indexOfDot = originalName.lastIndexOf(".");
			if (indexOfDot > -1) {
				name = originalName.substring(0, indexOfDot);
			}
		}
		if (prefix != null) {
			name = prefix + name; 
		}
		if (suffix != null) {
			name = name + suffix;
		}
		if (extension != null) {
			name = name + "." + extension;
		}
		return name;
	}
	
	public String getSignatureParameters(String formato, String mime) {
		return getSignatureParameters(null, formato, mime);
	}
	
	public String getSignatureParameters(PfRequestsDTO peticion, String formato, String mime) {
		String parametros = "expPolicy=FirmaAGE\nheadless=true";
		if (formato.indexOf("XADES IMPLICITO") != -1) {		
			parametros+= "\nmimeType=" + mime;
		
		} else if (formato.indexOf("XADES EXPLICITO") != -1) {
			parametros+= "\nmode=explicit";
		
		} else if (formato.indexOf("XADES ENVELOPING") != -1) {
			// atención, se reescribe parametros. No se envía parametro FirmaAGE
			parametros= "nheadless=true\nformat=XAdES Enveloping";
			parametros+= "\nmimeType=" + mime;
		
		} else if (formato.indexOf("XADES ENVELOPED") != -1) {
			// atención, se reescribe parametros. No se envía parametro FirmaAGE
			parametros = "nheadless=true\nformat=XAdES Enveloped";
			parametros+= "\nmimeType=" + mime;

		} else if (formato.indexOf("CADES") != -1) {
			parametros+= "\nmode=implicit"; // Se incluye porque la politica AGE no siempre lo aplica cuando son documentos de mas de 1MB
			
		} else if (formato.indexOf("PDF") != -1) {		
			parametros+= "\nsignatureSubFilter=ETSI.CAdES.detached";
		}  else if (formato.equals("FacturaE")) {
			parametros = "";
		}
		if(peticion != null && peticion.getlSignMarked()){				
				parametros += "\nsignaturePositionOnPageLowerLeftX=5" + "\nsignaturePositionOnPageLowerLeftY=5" + 
				"\nsignaturePositionOnPageUpperRightX=550"+ "\nsignaturePositionOnPageUpperRightY=28" + 
				"\nsignaturePage=1" + "\nlayer2Text= Firmado por $$SUBJECTCN$$ el día $$SIGNDATE=dd/MM/yyyy$$" + 
				"\nlayer2FontSize=12" + "\nlayer2FontStyle=0";
		}
		
		return parametros;
	}

	public String getSignFormatParameter(String formato) {
		String formatParam = "";
		if (formato.equals("XADES IMPLICITO")) {
			formatParam = "XAdES";
		} else if (formato.equals("XADES EXPLICITO")) {
			formatParam = "XAdES";
		} else if (formato.equals("XADES ENVELOPING")) {
			formatParam = "XAdES";
		} else if (formato.equals("XADES ENVELOPED")) {		
			formatParam = "XAdES";
		} else if (formato.equals("CADES")) {
			formatParam = "CAdES";
		} else if (formato.equals("PDF")) {
			formatParam = "PAdES";
		} else if (formato.equals("FacturaE")) {
			formatParam = "FacturaE";
		}
		return formatParam;
	}

	public String getTipoFirmaInterfazGenerica(String formato) {
		String formatParam = "";
		if (formato.equals("XADES IMPLICITO")) {
			formatParam = "XAdES";
		} else if (formato.equals("CADES")) {
			formatParam = "CAdES";
		} else if (formato.equals("PDF")) {
			formatParam = "PAdES";
		}
		return formatParam;
	}

	public String evaluarAlgoritmoDefecto(String signatureAlgorithm) {
		if (signatureAlgorithm.startsWith("SHA384")) {
			return "SHA384withRSA";		
		}
		if (signatureAlgorithm.startsWith("SHA512")) {
			return "SHA512withRSA";		
		}
		return "SHA256withRSA";
	}

	/** 
	 * Metodo para extrae la url de la aplicacion.
	 * Ojo! No funciona fuera de contexto, por ejemplo, desde Spring Task o desde Quartz
	 * @return direccionBase
	 */
	public String urlAplicacion (){
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) attr.getRequest();

		String direccion = request.getRequestURL().toString();
		int finDireccionBase = direccion.lastIndexOf(request.getContextPath()) + request.getContextPath().length();

		return direccion.substring(0, finDireccionBase);
	}

}
