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

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.JobAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.exception.PortafirmasExceptionBussinesRollback;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UserJob;

@Controller
@RequestMapping("usersManagement/userJob")
public class UserJobController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private ProvinceBO provinceBO;
	
	@Autowired
	private JobAdmBO jobAdmBO;
	
	@RequestMapping(value = "load", method = RequestMethod.POST)
	public String load(@ModelAttribute User user, Model model) {
		try {
			// Se obtiene el usuario DTO
			PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(user.getPrimaryKey()));
			
			model.addAttribute("loadUser", pfUsersDTO);
			model.addAttribute("userJobList", userAdmBO.getPfUsersJobs(pfUsersDTO));
		} catch (Exception e) {
			log.error("Error al obtener los cargos de usuario: ", e);
		}
		return "userJobsModal";
	}

	/**
	 * Recupera los usuarios para el autocompletado de alta de autorizados.
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocomplete")
	public @ResponseBody List<UserAutocomplete> autocomplete(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();
		try {
			// Se recupera el usuario editado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO userLogDTO = authorization.getUserDTO();
			
			// Se toma para el filtro el último valor introducido..
			String[] values = term.split(",");
			String value = values[values.length-1].trim();
			// .. y la lista de provincias que administra el usuario logado
			List<AbstractBaseDTO> provinceList = provinceBO.getSeatList(userLogDTO);
			// Se obtienen los cargos definidos para dichas provincias y que cumplen con la búsqueda
			List<AbstractBaseDTO> jobs = jobAdmBO.getJobsByProvinces(provinceList, value);

			// Se carga la lista a devolver convirtiendo los resultados
			for (AbstractBaseDTO jobAux : jobs) {
				results.add(new UserAutocomplete((PfUsersDTO) jobAux));
			}
		} catch (Exception e) {
			log.error("Error en autocompletar nombre en gestión de cargos de usuario: ", e);
		}
		return results;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> save(@ModelAttribute UserJob userJob) {
		
		return userAdmBO.saveUserJob(userJob);
		
//		ArrayList<String> warnings = new ArrayList<String>();
//		try {
//			// Se valida la vista
//			userJobValidator.validate(userJob, warnings);
//			if(!warnings.isEmpty()) {
//				return warnings;
//			}
//			
//			// Se crea el usuario DTO a persistir
//			PfUsersJobDTO pfUsersJobDTO = userJobConverter.envelopeToDTO(userJob);
//
//			// Se validan las reglas de negocio
//			userAdmBO.validateUserJob(pfUsersJobDTO, warnings);
//			if(!warnings.isEmpty()) {
//				return warnings;
//			}
//			
//			// Se realiza la persistencia del usuario
//			userAdmBO.saveUserJob(pfUsersJobDTO);
//			
//		} catch (Exception e) {
//			log.error("Error al insertar el cargo del usuario: ", e);
//			warnings.add(Constants.MSG_GENERIC_ERROR);
//		}
//		return warnings;
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> delete(@ModelAttribute UserJob userJob) {
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			PfUsersJobDTO pfUsersJobDTO = userAdmBO.queryUserJobByPk(Long.parseLong(userJob.getUserJobPk()));
			userAdmBO.deleteUserJob(pfUsersJobDTO);
		} catch (Exception e) {
			log.error("Error al borrar el cargo del usuario: ", e);
			warnings.add(Constants.MSG_GENERIC_ERROR);
		}
		return warnings;
	}

	@RequestMapping(value = "actualizarUsuarioConAutentica", method = RequestMethod.GET)
	public @ResponseBody List<String> actualizarCargoConAutentica(@RequestParam(value="idUsuario") Long idUsuario){
		log.error("inicia el metodo UserJobController.actualizarCargoConAutentica");
		List<String> returnValue = new ArrayList<String>();
		log.error("List<String> returnValue = new ArrayList<String>();");
		
		try {
			userAdmBO.actualizarCargoConAutentica(idUsuario, returnValue);
			log.error("userAdmBO.actualizarCargoConAutentica(idUsuario, returnValue);");
		} catch (MalformedURLException e) {
			log.error(e);
			returnValue.add("Ha ocurrido un error en la comunicacion con el servicio de autentica, por favor valide la configuracion");
		} catch (JAXBException e) {
			log.error(e);
			returnValue.add("Ha ocurrido un error en el mapeo de valores con autentica");
		} catch (PortafirmasExceptionBussinesRollback e) {
			log.error(e);
			returnValue.add(e.getMessage());
		}
		return returnValue;
	}
	
}
