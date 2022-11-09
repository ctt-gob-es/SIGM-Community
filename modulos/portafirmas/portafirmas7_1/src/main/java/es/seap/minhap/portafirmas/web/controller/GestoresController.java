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
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.GestorUsersConfBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;

@Controller
@RequestMapping("configuration/gestores")
public class GestoresController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private UserAdmBO	userAdmBO;
	
	@Autowired
	private GestorUsersConfBO gestorUsersConfBO;
	
	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * Carga la lista de gestores en la pestaña correspondiente
	 * @return
	 */
	@RequestMapping(value = "/loadGestores", method = RequestMethod.POST)
	public ModelAndView loadGestores() {
		ModelMap model = null;
		try {
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			
			PfUsersDTO userBD = userAdmBO.queryUsersByPk(user.getPrimaryKey());
			Set<PfUsersDTO> gestoresSet = userBD.getGestores();

			model = new ModelMap();
			model.addAttribute("gestoresSet", gestoresSet);
		} catch (Exception e) {
			log.error("Error al cargar gestores: ", e);
		}

		return new ModelAndView("gestores", model);
	}	
	
	/**
	 * Inserción de un gestor
	 * @param primaryKey
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/insertGestor")
	public @ResponseBody ArrayList<String> insertGestor(
			@RequestParam(value = "primaryKey") final String primaryKey) throws Exception {
		ArrayList<String> errors = new ArrayList<String>();
		// .. y el nuevo usuario gestor..
		PfUsersDTO gestorUser = null;
		try {			
			if(Util.esVacioONulo(primaryKey)) {
				errors.add(messages.getProperty("field.gestor.required"));
				return errors;
			}
			// Se recupera el usuario autenticado..
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			// .. se recuperan los gestores de BB.DD. los del 'user' dan problemas en la segunda inserción
			PfUsersDTO userBD = userAdmBO.queryUsersByPk(user.getPrimaryKey());
			gestorUser = userAdmBO.queryUsersByPk(Long.parseLong(primaryKey));
			
			// .. se guarda en BB.DD.
			userBD.getGestores().add(gestorUser);
			gestorUsersConfBO.saveUser(userBD);
		} catch (Exception e) {
			log.error("Error al insertar gestor: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}

	/**
	 * Borrado de un gestor
	 * @param primaryKey
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteGestor", method = RequestMethod.GET)
	public @ResponseBody ArrayList<String> deleteGestor(
			@RequestParam("primaryKey") final String primaryKey,
			final HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se recupera el usuario autenticado..
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			PfUsersDTO userBD = userAdmBO.queryUsersByPk(user.getPrimaryKey());
			// .. y el usuario gestor
			PfUsersDTO gestorUser = userAdmBO.queryUsersByPk(Long.parseLong(primaryKey));
			
			// Se borra el gestor del usuario logado
			if (gestorUser != null) {
				userBD.getGestores().remove(gestorUser);
				gestorUsersConfBO.saveUser(userBD);
			}
		} catch (Exception e) {
			log.error("Error al borrar gestor: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		return errors;
	}
	
	/**
	 * Recupera los usuarios para el autocompletado de alta de gestores
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteGestores")
	public @ResponseBody List<UserAutocomplete> autocompleteGestores(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = null;
		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();

			results = getUsersList(user, term);

			// Se obtienen los gestores
			PfUsersDTO userBD = userAdmBO.queryUsersByPk(user.getPrimaryKey());
			Set<PfUsersDTO> gestoresSet = userBD.getGestores();
			
			// Se quitan los gestores que ya tiene
			for (Iterator<PfUsersDTO> iterator = gestoresSet.iterator(); iterator.hasNext();) {
				PfUsersDTO pfUsersDTO = iterator.next();
				results.remove(new UserAutocomplete(pfUsersDTO));
			}
		} catch (Exception e) {
			log.error("Error al autocompletar nombre en gestor: ", e);
		}

		return results;
	}
	
	/**
	 * Recupera la lista de usuario sin el propio usuario
	 * @param user
	 * @param term
	 * @return
	 */
	private List<UserAutocomplete> getUsersList(PfUsersDTO user, String term) {
		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();
		// Se obtiene la sede del usuario autenticado
		String codSede = null;
		if (user.getPfProvince() != null) {
			codSede = user.getPfProvince().getCcodigoprovincia();
		}

		// Se toma para el filtro el último valor introducido
		String[] values = term.split(",");

		// Se filtran los usuarios en base a la búsqueda
		List<AbstractBaseDTO> users = requestBO.queryUsersComplete(values[values.length-1].trim(), codSede);

		// Se convierten los resultados
		for (AbstractBaseDTO userAux : users) {
			results.add(new UserAutocomplete((PfUsersDTO) userAux));
		}
		
		// Se quita el usuario logado de la lista
		results.remove(new UserAutocomplete(user));
		return results;
	}

}