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

package es.seap.minhap.portafirmas.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.StorageBO;
import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.storage.domain.StoredRequest;
import es.seap.minhap.portafirmas.storage.util.StorageConstants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.beans.Paginator;
import es.seap.minhap.portafirmas.web.beans.Storage;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;


@Controller
@RequestMapping("administration/storage")
public class StorageController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ApplicationAdmBO applicationAdmBO;
	
	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private StorageBO storageBO;
	
	@Autowired
	private UtilComponent util;
	
	/**
	 * Carga del listado de sedes
	 * @return
	 */
	@RequestMapping(value = "load")
	public String loadStorage(ModelMap model) {
		model.addAttribute("appList", applicationAdmBO.queryListByHierarchy());
		model.addAttribute("currentMonthId", String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1));
		model.addAttribute("monthList", util.getMonths());
		model.addAttribute("yearList", util.getYears());
		
		model.addAttribute("data", new ArrayList<StoredRequest>());
		
		//incluimos los datos de paginacion
		Paginator paginator = new Paginator();
		paginator.setInboxSize(0);
		model.put("paginator", paginator);
		
		return "storage";
	}	
	
	@RequestMapping(value = "search", method = RequestMethod.POST)
	public String search(@ModelAttribute Storage storage, ModelMap model) {
		try {
			Paginator paginator = getPaginator(storage);
			
			List<StoredRequest> data = null; 
			Map<String, String> filters = util.getFilters(storage);
			// Discriminación por base de datos: Búsqueda en BB.DD. del histórico..
			if(StorageConstants.STORAGE.equals(storage.getStorage())) {
				// Discriminación según se filtre por firmante o no
				if(Util.esVacioONulo(storage.getSign())) {
					data = storageBO.queryStoredRequests(filters, paginator);
				} else {
					data = storageBO.queryStoredRequestsBySigner(filters, storage.getSign(), paginator);
				}
				
				//indicamos el metodo de busqueda para la paginación
				model.put("methodJS", "searchRequest('Hist')");
				model.put("idPagination", "Hist");
			} else {
				// .. búsqueda en BB.DD. del Portafirmas
				// Discriminación según se filtre por firmante o no
				if(Util.esVacioONulo(storage.getSign())) {
					data = storageBO.queryRequests(filters, paginator);
				} else {
					data = storageBO.queryRequestsBySigner(filters, storage.getSign(), paginator);
				}
				
				//indicamos el metodo de busqueda para la paginación
				model.put("methodJS", "searchRequest('Pf')");
				model.put("idPagination", "Pf");
			}
			
			//incluimos los datos de paginacion
			model.put("paginator", paginator);
			
			util.getMonthNames(data);
			model.addAttribute("data", data);
		} catch (Exception e) {
			log.error("Error al buscar peticiones en la pestaña de histórico: ", e);
		}
		return "storageTable";
	}	
	
	private Paginator getPaginator(Storage storage) {
		//obtenemos los datos de paginacion
		Paginator paginator = null;
		if(StorageConstants.STORAGE.equals(storage.getStorage())) {
			paginator = storage.getPaginatorHist();
		} else {
			paginator = storage.getPaginatorPf();
		}
		if (paginator == null) {
			paginator = new Paginator();
		}
		return paginator;
	}
	
	@RequestMapping(value = "move", method = RequestMethod.POST)
	public String move(@ModelAttribute Storage storage, ModelMap model) {
		try {
			if(storage.getRequestIds() != null) {
				List<String> requestIds = Arrays.asList(storage.getRequestIds());
				// Discriminación por base de datos: Del histórico al Portafirmas..
				if(StorageConstants.STORAGE.equals(storage.getStorage())) {
					storageBO.returnFromStorage(requestIds);
				} else {
					// .. del Portafirmas al historico
					storageBO.moveToStorage(requestIds);
				}
			}
			// .. y se actualiza la búsqueda
			search(storage, model);
		} catch (Throwable e) {
			log.error("Error al mover peticiones en la pestaña de histórico: ", e);
		}
		return "storageTable";
	}	
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String delete(@ModelAttribute Storage storage, ModelMap model) {
		try {
			if(storage.getRequestIds() != null) {
				List<String> requestIds = Arrays.asList(storage.getRequestIds());
				// Se borran las peticiones del histórico..
				storageBO.deleteForever(requestIds);
			}
			// .. y se actualiza la búsqueda
			search(storage, model);
		} catch (Throwable e) {
			log.error("Error al borrar peticiones en el histórico: ", e);
		}
		return "storageTable";
	}	
	
	@RequestMapping(value = "autocomplete")
	public @ResponseBody List<UserAutocomplete> autocomplete(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();
		try {
			// Se toma para el filtro el último valor introducido
			String[] values = term.split(",");
			String value = values[values.length-1].trim();

			// Se filtran los usuarios en base a la búsqueda
			List<AbstractBaseDTO> users = requestBO.queryUsersComplete(value, null);

			// Se convierten los resultados
			for (AbstractBaseDTO userAux : users) {
				results.add(new UserAutocomplete((PfUsersDTO) userAux));
			}
		} catch (Exception e) {
			log.error("Error al autocompletar nombre en la pestaña de histórico: ", e);
		}
		return results;
	}
	
}