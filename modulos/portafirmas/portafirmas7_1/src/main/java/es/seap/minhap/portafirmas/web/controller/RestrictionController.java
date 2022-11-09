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
import java.util.HashMap;
import java.util.List;

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
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.RedactionBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.RestrictionBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;

@Controller
@RequestMapping("userManagement/restriction")
public class RestrictionController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private UtilComponent util;

	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private RestrictionBO restrictionBO;
	
	@Autowired
	private RedactionBO redactionBO;

	@RequestMapping(method = RequestMethod.GET)
	public String loadJobs(ModelMap model) {
		process(model, new UsersParameters());
		return "restriction";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String loadJobs(@ModelAttribute("restrictionParameters") UsersParameters restrictionParameters, ModelMap model) {
		process(model, restrictionParameters);
		return "restriction";
	}

	private void process(ModelMap model, UsersParameters restrictionParameters) {
		// Se obtiene la información para el conformar el modelo
		List<AbstractBaseDTO> usersList = null;
		if(util.noEsVacio(restrictionParameters.getSearchText())) {
			// Se obtiene el usuario conectado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO userLogDTO = authorization.getUserDTO();
			restrictionParameters.setUser(userLogDTO);

			List<String> tipos = new ArrayList<String>();
			tipos.add(Constants.C_TYPE_USER_USER);
			restrictionParameters.setType(tipos);

			usersList = userAdmBO.queryUsersComplete(restrictionParameters);
		}

		model.addAttribute("usersList", usersList);
		model.addAttribute("restrictionParameters", restrictionParameters);
		model.addAttribute("user", new User());
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> save(ModelMap model, @ModelAttribute ("userRestrict") String idUserRestringido,
			@ModelAttribute ("userValid") String idUserValido) {
		return restrictionBO.saveRestrinction(idUserRestringido, idUserValido);
	}

	@RequestMapping(value = "deleteUserValid", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, ArrayList<String>> deleteUserValid(@ModelAttribute ("primaryKey") String primaryKey,
			@ModelAttribute ("userRestrict") String userRestrict) {
		HashMap<String, ArrayList<String>> response = new HashMap<>();
		try {
			restrictionBO.deleteRestrict(userRestrict, primaryKey);
		} catch (Exception e) {
			log.error("Error al borrar el cargo: ", e);
			ArrayList<String> errors = new ArrayList<String>();
			errors.add(Constants.MSG_GENERIC_ERROR);
			response.put("errors", errors);
		}
		return response;
	}

	/**
	 * Recupera los usuarios para el autocompletado 
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteUserRestriction")
	public @ResponseBody List<UserAutocomplete> autocompleteUserRestrict(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = null;
		PfUsersDTO user = null;

		try {			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			user = authorization.getUserDTO();

			String codSede = null;
			if (user.getPfProvince() != null) {
				codSede = user.getPfProvince().getCcodigoprovincia();
			}

			List<AbstractBaseDTO> usersList = requestBO.queryUsersComplete(term, codSede);

			// Se convierten los resultados
			results = new ArrayList<UserAutocomplete>();
			for (AbstractBaseDTO userAux : usersList) {
				results.add(new UserAutocomplete((PfUsersDTO) userAux));
			}			
		} catch (Exception e) {
			log.error("Error al autocompletar nombre: ", e);
		}
		return results;
	}

	/**
	 * Carga las lista de usuarios válidos de un usuario restringido
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping(value = "/loadList", method = RequestMethod.GET)
	public ModelAndView loadList(ModelMap model, @RequestParam(value = "userId") final String userId) throws Exception {
		try {
			//Obtiene el usuario
			PfUsersDTO userRestrict = (PfUsersDTO) redactionBO.getUser(userId);			
			List <PfUsersDTO> userList = restrictionBO.queryUserRestrict(userRestrict);			
			model.addAttribute("userList", userList);
			model.addAttribute("userId", userId);
		} catch (Exception e) {
			log.error("Error al cargar los usuarios validos de un usuario restringido: ", e);
			throw e;
		}
		return new ModelAndView("restrictionList", model);
	}
}
