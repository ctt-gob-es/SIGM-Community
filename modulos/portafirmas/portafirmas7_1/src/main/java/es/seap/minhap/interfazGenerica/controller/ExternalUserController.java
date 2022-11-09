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

package es.seap.minhap.interfazGenerica.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.interfazGenerica.repository.PortafirmasRepository;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;

@Controller
@RequestMapping("externalUser")
public class ExternalUserController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private PortafirmasRepository portafirmasRepository;
	
	/**
	 * Si la petición llega por el GET, significa que se está pidiendo la página
	 * por primera vez
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(Model model) {
		process(model, new UsersParameters());
		return Constants.IG_RUTA_HTML + "externalManagement";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(Model model) {
		return Constants.IG_RUTA_HTML + "externalManagement";
	}

	/**
	 * Recoge la funcionalidad común para las peticiones GET y POST
	 * 
	 * @param model
	 * @param usersParameters
	 */
	private void process(Model model, UsersParameters usersParameters) {
		// Se obtiene el usuario conectado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO userLogDTO = authorization.getUserDTO();
		usersParameters.setUser(userLogDTO);

		List<String> tipos = new ArrayList<String>();
		tipos.add(Constants.C_TYPE_USER_EXTERNAL);
		usersParameters.setType(tipos);

		// Se obtiene la información para el conformar el modelo
		List<AbstractBaseDTO> usersList = userAdmBO.queryUsersComplete(usersParameters);

		
		// Se recupera el usuario autenticado
		model.addAttribute("usersList", usersList);
		model.addAttribute("usersParameters", usersParameters);
		model.addAttribute("user", new PfUsersDTO());
		model.addAttribute("portafirmasList", portafirmasRepository.obtenerTodos());
		model.addAttribute("authentication", authorization);
	}

	@RequestMapping(value = "user/save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> saveUser(@ModelAttribute("usuario") PfUsersDTO usuario) {
		ArrayList<String> errors = new ArrayList<String>();
				
		try {
			// Se valida la vista
			//userValidator.validate(user, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se validan las reglas de negocio
			//userAdmBO.validateUser(pfUsersDTO, errors);
			//userAdmBO.validateLDAP(pfUsersDTO, errors, user);			
			
			if(!errors.isEmpty()) {
				return errors;
			}
			
			userAdmBO.saveExternalUser(usuario);
			
		} catch (Exception e) {
			log.error("Error al insertar el usuario: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	@RequestMapping(value = "user/delete", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, ArrayList<String>> deleteUser(@ModelAttribute("usuario") PfUsersDTO usuario) {
		HashMap<String, ArrayList<String>> response = new HashMap<String, ArrayList<String>>();
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			PfUsersDTO usuarioBBDD = userAdmBO.queryUsersByPk(usuario.getPrimaryKey());
			userAdmBO.revokeUser(usuarioBBDD);
			response.put("warnings", warnings);

		} catch (Exception e) {
			log.error("Error al borrar el usuario: ", e);
			ArrayList<String> errors = new ArrayList<String>();
			errors.add(Constants.MSG_GENERIC_ERROR);
			response.put("errors", errors);
		}
		return response;
	}
	
	@RequestMapping(value = "user/load", method = RequestMethod.GET)
	public String loadUser(@ModelAttribute("usuario") PfUsersDTO usuario, Model model) {
		try {
			PfUsersDTO user = userAdmBO.queryUsersByPk(usuario.getPrimaryKey());

			model.addAttribute("user", user);
			model.addAttribute("portafirmasList", portafirmasRepository.obtenerTodos());
		
		} catch (Exception e) {
			log.error("Error al obtener el usuario: ", e);
		}
		return Constants.IG_RUTA_HTML + "externalUserModal";
	}

}
