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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.administration.StatsBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.ParameterStat;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.stats.StatsTotals;

@Controller
@RequestMapping("stats")
public class StatsController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private StatsBO statsBO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model) {
		try {
			model.addAttribute("parameterStat", new ParameterStat());
			model.addAttribute("seatList", loadSeatsAffected());
			model.addAttribute("applicationList", statsBO.getApplicationList());
		} catch (Throwable t) {
			log.error("Error al iniciar formulario: ", t);
			model.addAttribute("errorMessage", Constants.MSG_GENERIC_ERROR);
			return "error";
		}				
		return "stats";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String loadStats(@ModelAttribute ParameterStat parameterStat, ModelMap model) {
		try {
			List<String> headers = new ArrayList<String> ();
			List<Object[]> rows = new ArrayList<Object[]>();

			statsBO.stats(loadSeatsAffected(), parameterStat, headers, rows);			
			
			model.addAttribute("headers", headers);
			model.addAttribute("rows", rows);
			
			model.addAttribute("parameterStat", parameterStat);
			model.addAttribute("seatList", loadSeatsAffected());
			model.addAttribute("applicationList", statsBO.getApplicationList());

		} catch (Throwable t) {
			log.error("Error al cargar estadisticas: ", t);
			model.addAttribute("errorMessage", Constants.MSG_GENERIC_ERROR);
			return "error";
		}	
		return "stats";
	}
	
	/**
	 * Cálculo de totales
	 * @return
	 */
	@RequestMapping(value = "/loadTotals", method = RequestMethod.GET)
	public String loadTotals(ModelMap model) {
		try {
			StatsTotals totals = new StatsTotals ();
			List<AbstractBaseDTO> seatList = loadSeatsAffected();
			totals.setSeatsNumber(seatList.size() + "");
			totals.setApplicationsNumber(statsBO.getApplicationList().size() + "");
			totals.setRequestsNumber(statsBO.countRequests(seatList).toString());		
			totals.setSignaturesNumber(statsBO.countSignatures(seatList).toString());
			totals.setUsersNumber(statsBO.countUsers(seatList).toString());
			totals.setYearsNumber(statsBO.getYearList().size() + "");
			
			model.addAttribute("totals", totals);			
		} catch (Throwable t) {
			log.error("Error al cargar totales: ", t);
		}
		return "stats-totals";
	}
	
	/**
	 * Exporta las estadísticas a excel o pdf.
	 * Los atributos headers y rows se han debido calcular previamente.
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/exportStats", method = RequestMethod.POST)
	public void exportStats(final HttpServletRequest request,	final HttpServletResponse response,
			@ModelAttribute ParameterStat parameterStat) throws IOException {
		List<String> headers = new ArrayList<String> ();
		List<Object[]> rows = new ArrayList<Object[]>();
		try {		
			statsBO.stats(loadSeatsAffected(), parameterStat, headers, rows);	
			
			statsBO.export(request, response, parameterStat, headers, rows);			
		} catch (Throwable t) {
			log.error("Error al exportar estadísticas", t);
			response.sendError(500);
		}
	}
	
	/**
	 * Recupera los usuarios para el autocompletado de estadísticas.
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteUser")
	public @ResponseBody List<UserAutocomplete> autocompleteUser(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();
		try {
			
			// Se toma para el filtro el último valor introducido..
			String[] values = term.split(",");
			String value = values[values.length-1].trim();

			// Se obtienen los usuarios definidos para las provincias y que cumplen con la búsqueda
			List<AbstractBaseDTO> users = statsBO.searchUsers(loadSeatsAffected(), value);

			// Se carga la lista a devolver convirtiendo los resultados
			for (AbstractBaseDTO userAux : users) {
				results.add(new UserAutocomplete((PfUsersDTO) userAux));
			}
		} catch (Exception e) {
			log.error("Error en autocompletar nombre en estadísticas: ", e);
		}
		return results;
	}
	
	/**
	 * Carga las sedes para las que un usuario tiene visibilidad.
	 */
	private List<AbstractBaseDTO> loadSeatsAffected() {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();
		PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
		
		return statsBO.getSeatsAffected(user);
	}
	
}