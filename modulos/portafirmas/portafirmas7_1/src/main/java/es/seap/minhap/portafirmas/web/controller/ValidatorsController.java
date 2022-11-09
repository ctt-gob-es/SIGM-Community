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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfValidatorApplicationDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;

@Controller
@RequestMapping("configuration/validators")
public class ValidatorsController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private ValidatorUsersConfBO validatorUsersConfBO;

	@Autowired
	private ApplicationAdmBO appBO;

	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * Carga la lista de validadores en la pestaña correspondiente
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/loadValidators", method = RequestMethod.POST)
	public ModelAndView loadValidators() {

		ModelMap model = null;
		List <PfValidatorApplicationDTO> validatorByAppList = null;
		try {
			PfUsersDTO userBD = obtenerUsuarioAutenticado();
			Set<PfUsersDTO> validatorsSet = userBD.getValidadores();

			//Si tiene validadores por aplicacion tambien se cargan y se agregan a la vista.
			validatorByAppList = validatorUsersConfBO.queryValidatorsByValidatorList(userBD);

			for (PfValidatorApplicationDTO validatorByApp : validatorByAppList)
				validatorsSet.add(validatorByApp.getPfValidatorUser());

			model = new ModelMap();
			model.addAttribute("user", userBD);
			model.addAttribute("validatorsSet", validatorsSet);

		} catch (Exception e) {
			log.error("Error al cargar validadores: ", e);
		}

		return new ModelAndView("validators", model);
	}	

	/**
	 * Inserción de un validador
	 * @param primaryKey
	 * @param apps
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertValidator")
	public @ResponseBody ArrayList<String> insertValidator(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "apps") final ArrayList<String> appsSelected) throws Exception {

		ArrayList<String> errors = new ArrayList<String>();

		try {	

			if(Util.esVacioONulo(primaryKey)) {
				errors.add(messages.getProperty("field.validator.required"));
				return errors;
			}
			PfUsersDTO userBD = obtenerUsuarioAutenticado();
			PfUsersDTO validatorUser = userAdmBO.queryUsersByPk(Long.parseLong(primaryKey));

			if (appsSelected.isEmpty()){
				// ... se guarda el usuario validador en la tabla PF_Usuarios_Validadores
				userBD.getValidadores().add(validatorUser);
				validatorUsersConfBO.saveUser(userBD);

			}else{
				// ... se guarda el usuario validador y las aplicaciones en la tabla PF_Validador_Aplicacion
				List <PfValidatorApplicationDTO> validatorsByAppsList = generateListValidatorsByApps(appsSelected, userBD, validatorUser);				
				validatorUsersConfBO.saveValidatorByApplicationList(validatorsByAppsList);
			}

		} catch (Exception e) {
			log.error("Error al insertar validador: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		return errors;
	}

	/**
	 * Borrado de un validador
	 * @param primaryKey
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteValidator", method = RequestMethod.GET)
	public @ResponseBody ArrayList<String> deleteValidator(@RequestParam("primaryKey") final String primaryKey,
			final HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		ArrayList<String> errors = new ArrayList<String>();

		try {
			PfUsersDTO userBD = obtenerUsuarioAutenticado();
			// .. y el usuario validador
			PfUsersDTO validatorUser = userAdmBO.queryUsersByPk(Long.parseLong(primaryKey));

			if (validatorUser != null){

				List<PfApplicationsDTO> appsListValidates = validatorUsersConfBO.queryValidatorsAppByValidatorList(validatorUser, userBD);

				if (!appsListValidates.isEmpty()){				
					List <PfValidatorApplicationDTO> appValidators = validatorUsersConfBO.queryValidatorsAppByPkAndValidatorList(userBD, validatorUser);				
					validatorUsersConfBO.deleteValidatorsAppList(appValidators);
				}

				// Se borra el validador del usuario logado
				if (validatorUser != null) {
					userBD.getValidadores().remove(validatorUser);
					validatorUsersConfBO.saveUser(userBD);
				}
			}else{
				errors.add("Error al borrar el validador");
			}			
		} catch (Exception e) {
			log.error("Error al borrar el validador: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		return errors;
	}

	/**
	 * Recupera los usuarios para el autocompletado de alta de validadores
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteValidators")
	public @ResponseBody List<UserAutocomplete> autocompleteValidators(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = null;
		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();

			results = getUsersList(user, term);

			// Se obtienen los validadores
			PfUsersDTO userBD = userAdmBO.queryUsersByPk(user.getPrimaryKey());
			Set<PfUsersDTO> validatorsSet = userBD.getValidadores();

			// Se quitan los validadores que ya tiene
			for (Iterator<PfUsersDTO> iterator = validatorsSet.iterator(); iterator.hasNext();) {
				PfUsersDTO pfUsersDTO = iterator.next();
				results.remove(new UserAutocomplete(pfUsersDTO));
			}
		} catch (Exception e) {
			log.error("Error al autocompletar nombre en validadores: ", e);
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

	/**Genera una lista de validadores por aplicaciones
	 * 
	 * @param apps
	 * @param userBD
	 * @param validatorUser
	 * @return
	 */
	public List<PfValidatorApplicationDTO> generateListValidatorsByApps (ArrayList<String> apps, PfUsersDTO userBD, PfUsersDTO validatorUser){

		List<PfValidatorApplicationDTO> validatorByAppList = new ArrayList <PfValidatorApplicationDTO>();		

		for (String appPrimaryKey : apps){
			PfValidatorApplicationDTO validatorByApp = new PfValidatorApplicationDTO();			
			validatorByApp = generateValidatorByApp(appPrimaryKey, userBD, validatorUser);			
			validatorByAppList.add(validatorByApp);
		}
		return validatorByAppList;
	}

	/**Genera un validador por aplicacion
	 * 
	 * @param appPrimaryKey
	 * @param userBD
	 * @param validatorUser
	 * @return
	 */
	public PfValidatorApplicationDTO generateValidatorByApp (String appPrimaryKey, PfUsersDTO userBD, PfUsersDTO validatorUser){		

		PfValidatorApplicationDTO validatorByApp = new PfValidatorApplicationDTO();

		//Se recuperan las aplicaciones a validar
		validatorByApp = new PfValidatorApplicationDTO();
		PfApplicationsDTO app = (PfApplicationsDTO) appBO.applicationPkQuery(Long.parseLong(appPrimaryKey));

		validatorByApp.setPfUser(userBD);
		validatorByApp.setPfValidatorUser(validatorUser);
		validatorByApp.setPfApplication(app);
		validatorByApp.setCcreated(userBD.getPrimaryKeyString());
		validatorByApp.setCmodified(userBD.getPrimaryKeyString());
		validatorByApp.setFcreated(Calendar.getInstance().getTime());
		validatorByApp.setFmodified(Calendar.getInstance().getTime());

		return validatorByApp;
	}

	/**Edicion de los validadores
	 * 
	 * @param primaryKey
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editValidator", method = RequestMethod.POST)
	public String editValidator(@RequestParam(value = "primaryKey") final String primaryKey, Model model) {

		boolean allApps = false;

		try {			
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO userDTO = authorization.getUserDTO();

			//Se recupera el validador seleccionado para cargar las apps que valida.
			PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(primaryKey));

			//Se obtiene la lista de aplicaciones que valida.
			List<PfApplicationsDTO> appValidatedList = validatorUsersConfBO.queryValidatorsAppByValidatorList(pfUsersDTO, userDTO);

			if (appValidatedList.isEmpty()){
				allApps = true;
			}

			List<AbstractBaseDTO> allAppsList = appBO.queryList();

			model.addAttribute("user", pfUsersDTO);
			model.addAttribute("appListValidadas", appValidatedList);
			model.addAttribute("appsList", allAppsList);
			model.addAttribute("allApps", allApps);

		} catch (Exception e) {
			log.error("Error al obtener el usuario: ", e);
		}
		return "validatorModal";
	}

	/**Método asociado al botón de guardar de la ventana de edición de validador
	 * 
	 * @param validatorPrimaryKey
	 * @param allApplications
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/saveValidator", method = RequestMethod.POST)	
	public @ResponseBody ArrayList<String> saveValidator(@ModelAttribute("primaryKey") String validatorPrimaryKey,
			@ModelAttribute("allApplications") String allApplications, HttpServletRequest request) {

		ArrayList<String> errors = new ArrayList<String>();

		try {

			//Se extrae un array con las aplicaciones seleccionadas en la vista
			String [] appsSelected = request.getParameterValues("aplicaciones");

			if (appsSelected == null && allApplications.isEmpty()){
				errors.add("Debe seleccionar al menos una aplicación.");				
				return errors;
			}

			PfUsersDTO userBD = obtenerUsuarioAutenticado();
			// Se recupera el validador seleccionado.
			PfUsersDTO validator = userAdmBO.queryUsersByPk(Long.parseLong(validatorPrimaryKey));

			//Se obtiene la lista de aplicaciones que valida el usuario seleccionado.
			List<PfApplicationsDTO> appsListVal = validatorUsersConfBO.queryValidatorsAppByValidatorList(validator, userBD);

			//Si no han seleccionado todas las aplicaciones para validar.	
			if (allApplications.isEmpty()){

				List <String> aplicacionesSeleccionadas = Arrays.asList(appsSelected);

				//Se obtienen todas las aplicaciones
				List<AbstractBaseDTO> appsList = appBO.queryList();
				if (aplicacionesSeleccionadas.size() == appsList.size()){		
					allApplications = "true";	
				}

				List<PfApplicationsDTO> appLIst = updateValidatingApps(userBD, validator, appsListVal, aplicacionesSeleccionadas);
				appsListVal.removeAll(appLIst);

				//Eliminamos las aplicaciones que no han sido seleccionadas.
				deleteUnselectedApps(userBD, validator, appsListVal);

				//En el caso de que exista el validador - usuario en la tabla de PF_USUARIOS se debe borrar
				userBD.getValidadores().remove(validator);
				validatorUsersConfBO.saveUser(userBD);

			}else{

				//En el caso de que no fuera validador universal y tenga aplicaciones a validar.
				if (!userBD.getValidadores().contains(validator)){
					//las borramos de la tabla de app-usuario
					List <PfValidatorApplicationDTO> appValidators = validatorUsersConfBO.queryValidatorsAppByPkAndValidatorList(userBD, validator);				
					validatorUsersConfBO.deleteValidatorsAppList(appValidators);

					//insertamos el usuario en la tabla de PF_USUARIOS
					userBD.getValidadores().add(validator);
					validatorUsersConfBO.saveUser(userBD);
				}
			}

		} catch (Exception e) {
			log.error("Error al editar el validador: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		return errors;		
	}

	private PfUsersDTO obtenerUsuarioAutenticado() {

		//Obtenemos el usuario autenticado...
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();									
		// .. se recuperan los validadores de BB.DD. los del 'user' dan problemas en la segunda inserción
		PfUsersDTO userBD = userAdmBO.queryUsersByPk(user.getPrimaryKey());

		return userBD;
	}

	/**Actualiza en el validador las aplicaciones
	 * 
	 * @param userBD
	 * @param validator
	 * @param appsListVal
	 * @param aplicacionesSeleccionadas
	 * @return
	 */
	private List<PfApplicationsDTO> updateValidatingApps(PfUsersDTO userBD, PfUsersDTO validator,
			List<PfApplicationsDTO> appsListVal, List<String> aplicacionesSeleccionadas) {
		List <PfApplicationsDTO> appLIst = new ArrayList<PfApplicationsDTO>();

		//Actualizamos la tabla de app-usuario con las nuevas apps.				
		for (String appId : aplicacionesSeleccionadas){				

			PfApplicationsDTO pfApp = (PfApplicationsDTO) appBO.applicationPkQuery(Long.parseLong(appId));

			if (!appsListVal.contains(pfApp)){

				PfValidatorApplicationDTO validatorByApp = new PfValidatorApplicationDTO();					
				validatorByApp.setPfUser(userBD);
				validatorByApp.setPfValidatorUser(validator);
				validatorByApp.setPfApplication(pfApp);					
				validatorUsersConfBO.saveValidatorByApplication(validatorByApp);					
			}

			appLIst.add(pfApp);
		}
		return appLIst;
	}

	/**Borrar del validador las aplicaciones que no han sido seleccionadas
	 * 
	 * @param userDTO
	 * @param validator
	 * @param appsListVal
	 */
	private void deleteUnselectedApps(PfUsersDTO userDTO, PfUsersDTO validator, List<PfApplicationsDTO> appsListVal) {

		for (PfApplicationsDTO app : appsListVal){

			PfValidatorApplicationDTO appValidator = validatorUsersConfBO.queryValidatorsAppByValidator(userDTO, validator, app);
			validatorUsersConfBO.deleteValidatorApp(appValidator);
		}
	}
}