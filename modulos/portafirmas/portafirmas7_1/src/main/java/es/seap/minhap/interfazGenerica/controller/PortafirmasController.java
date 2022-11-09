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

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.interfazGenerica.repository.PortafirmasRepository;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;

@Controller
@RequestMapping("portafirmas")
public class PortafirmasController {
	
	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private PortafirmasRepository portafirmasRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(Model model) {
		// Se obtiene el usuario conectado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();

		model.addAttribute("portafirmas", new Portafirmas());
        model.addAttribute("listaPortafirmas", portafirmasRepository.obtenerTodos());
		model.addAttribute("authentication", authorization);

		return Constants.IG_RUTA_HTML + "portafirmas";
	}

	@RequestMapping(value = "load", method = RequestMethod.GET)
	public String cargarPortafirmas(@ModelAttribute("portafirmas") Portafirmas portafirmas, Model model) {
		Portafirmas pf = portafirmasRepository.obtener(portafirmas.getIdPortafirmas());
		model.addAttribute("portafirmas", pf);
        return Constants.IG_RUTA_HTML + "portafirmasModal";
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, ArrayList<String>> guardar(Portafirmas portafirmas) {
		HashMap<String, ArrayList<String>> response = new HashMap<String, ArrayList<String>>();
		ArrayList<String> errors = new ArrayList<String>();
		
		try {
			// Se valida la vista
			//userValidator.validate(user, errors);
			if(!errors.isEmpty()) {
				response.put("errors", errors);
				return response;
			}
			
			// Se validan las reglas de negocio
			//userAdmBO.validateUser(pfUsersDTO, errors);
			//userAdmBO.validateLDAP(pfUsersDTO, errors, user);			
			
			if(!errors.isEmpty()) {
				response.put("errors", errors);
				return response;
			}
			
			portafirmasRepository.guardar(portafirmas);
			
		} catch (Exception e) {
			log.error("Error al insertar el usuario: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return response;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, ArrayList<String>> borrar(Portafirmas portafirmas) {
		HashMap<String, ArrayList<String>> response = new HashMap<String, ArrayList<String>>();
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			
			portafirmasRepository.borrar(portafirmas);
			
			response.put("warnings", warnings);

		} catch (Exception e) {
			log.error("Error al borrar el portafirmas: ", e);
			ArrayList<String> errors = new ArrayList<String>();
			errors.add(Constants.MSG_GENERIC_ERROR);
			response.put("errors", errors);
		}
		return response;
	}
	
}
