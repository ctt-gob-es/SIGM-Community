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

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.UserParameterBO;
import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.business.administration.ThemeBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.EmailConfBO;
import es.seap.minhap.portafirmas.business.configuration.FilterBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProfilesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.converter.UserConverter;

@Controller
@RequestMapping("configuration")
public class ConfigurationController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private EmailConfBO emailConfBO;
	
	@Autowired
	private FilterBO filterBO;
	
	@Autowired
	private UserAdmBO	userAdmBO;
	
	@Autowired
	private ThemeBO themeBO;
	
	@Autowired
	private UtilComponent utilComponent;
	
	@Autowired
	private ApplicationAdmBO appBO;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private UserParameterBO userParameterBO;

	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView initForm(ModelMap model) {
		// Pestaña datos personsales: usuario autenticado, cargo, perfil, correos electrónicos
		try {
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = userAdmBO.getUserByPK(authorization.getUserDTO().getPrimaryKey());
			//Actualizo en el userAuthentication el user, por si algún atributo hubiese cambiado a lo largo de la sesión
			authorization.setUserDTO(user);
			SecurityContextHolder.getContext().setAuthentication(authorization);
			PfUsersDTO job = Util.getInstance().getUserValidJob(user);
						
			List<PfProfilesDTO> profilesUserList = userAdmBO.getUserProfileList(user); // perfiles del usuario
			List<AbstractBaseDTO> profilesList = getProfiles(); // catálogo de perfiles para el empleado
			List<AbstractBaseDTO> emailsUserList = emailConfBO.queryList(user);
			List<AbstractBaseDTO> authorizationsTypesList = filterBO.queryAuthorizationTypesList();
			List<AbstractBaseDTO> appsList = appBO.queryList();
			String pageSize = Util.vacioSiNulo(themeBO.loadUserPageSize(user));
			String province = "";
			if(user.getPfProvince() != null) {
				province = user.getPfProvince().getCnombre();
			}
			
			
			//Quitamos el perfil SIMULAR
			List<AbstractBaseDTO> newProfilesList = new ArrayList<AbstractBaseDTO>();
			for(AbstractBaseDTO abs : profilesList){
				PfProfilesDTO profile = (PfProfilesDTO) abs;
				if(!Constants.C_PROFILES_SIMULATE.equals(profile.getCprofile())){
					newProfilesList.add(abs);
				}
			}
			
			model.addAttribute("user", user);
			model.addAttribute("job", job);
			model.addAttribute("province", province);
			model.addAttribute("profilesUserList", profilesUserList);
			model.addAttribute("profilesList", newProfilesList);
			model.addAttribute("emailsUserList", emailsUserList);
			model.addAttribute("pageSize", pageSize);
			model.addAttribute("sizeList", utilComponent.getPageSizeList());
			model.addAttribute("appsList", appsList);

			model.addAttribute("authorizationsTypesList", authorizationsTypesList);
			String filtroValidadorMovilActivo = Constants.C_YES;
			try {
				String filtroValidadorMovilActivoAux = userConverter.getUserParameter(user, Constants.FILTRO_VALIDADOR_MOVIL_ACTIVO);
				if (filtroValidadorMovilActivoAux!=null && (Constants.C_YES.equals(filtroValidadorMovilActivoAux) || Constants.C_NOT.equals(filtroValidadorMovilActivoAux))) {
					filtroValidadorMovilActivo = filtroValidadorMovilActivoAux;
				}
			} catch (Exception e) {
				//Por defecto el filtro está activo
			}
			model.addAttribute("filtroValidadorMovilActivo", filtroValidadorMovilActivo);
			
		} catch (Exception e) {
			log.error("Error al iniciarl la configuración: ", e);
		}
		
		return new ModelAndView("configuration", model);
	}

	private List<AbstractBaseDTO> getProfiles() {
		List<AbstractBaseDTO> profiles = applicationBO.queryProfileList();
		PfProfilesDTO wsProfile = new PfProfilesDTO();
		wsProfile.setCprofile(Constants.C_PROFILES_WEBSERVICE);
		profiles.remove(wsProfile);
		return profiles;
	}

	/**
	 * Para insertar el e-mail del usuario
	 * @param demail
	 * @param notify
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/insertEmail", method = RequestMethod.GET)
	public void insertEmail(
			@RequestParam("demail") final String demail,
			@RequestParam("lnotify") final String lnotify,
			final HttpServletResponse response) throws IOException {

		response.setContentType("application/json");
		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO pfUser = authorization.getUserDTO();
			
			// DTO de etiqueta
			PfUsersEmailDTO pfUsersEmail = new PfUsersEmailDTO();
			pfUsersEmail.setDemail(demail);
			pfUsersEmail.setLnotify(Constants.C_YES.equals(lnotify));
			pfUsersEmail.setPfUser(pfUser);
			
			String msgError = emailConfBO.validEmail(pfUsersEmail);
			if(Util.esVacioONulo(msgError)) {
				emailConfBO.saveEmail(pfUsersEmail);
				response.getWriter().write("{\"status\": \"success\",\"primaryKey\":\"" + pfUsersEmail.getPrimaryKeyString() + "\"}");
			} else {
				response.getWriter().write("{\"status\": \"error\",\"msgError\":\"" + msgError + "\"}");
			}
			
		} catch (Exception e) {
			// Respuesta JSON si algo ha ido mal
			log.error("Error al insertar el email: ", e);
			response.getWriter().write("{\"status\": \"error\",\"msgError\":\"" + Constants.MSG_GENERIC_ERROR + "\"}");
		}
	}
	
	/**
	 * Para modificar el e-mail del usuario
	 * @param primaryKey
	 * @param demail
	 * @param notify
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/modifyEmail", method = RequestMethod.GET)
	public void modifyEmail(
			@RequestParam("primaryKey") final String primaryKey,
			@RequestParam("demail") final String demail,
			@RequestParam("lnotify") final String lnotify,
			final HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		try {
			PfUsersEmailDTO pfUsersEmail = emailConfBO.queryUsersEmailById(primaryKey);			
			if(pfUsersEmail != null) {
				//Se cargan las modificaciones
				pfUsersEmail.setDemail(demail);
				pfUsersEmail.setLnotify(Constants.C_YES.equals(lnotify));
				emailConfBO.saveEmail(pfUsersEmail);
				response.getWriter().write("{\"status\": \"success\"}");
			} else {
				response.getWriter().write("{\"status\": \"error\",\"msgError\":\"No se encontró el correo electrónico a modificar.\"}");
			}
		} catch (Exception e) {
			log.error("Error al borrar email: ", e);
			// Respuesta JSON si algo ha ido mal
			response.getWriter().write("{\"status\": \"error\",\"msgError\":\"" + Constants.MSG_GENERIC_ERROR + "\"}");
		}
	}
	
	/**
	 * Para borrar el e-mail del usuario
	 * @param primaryKey
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteEmail", method = RequestMethod.GET)
	public void deleteEmail(
			@RequestParam("primaryKey") final String primaryKey,
			final HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		try {
			PfUsersEmailDTO pfUsersEmail = emailConfBO.queryUsersEmailById(primaryKey);
			if(pfUsersEmail != null) {
				emailConfBO.deleteEmail(pfUsersEmail);
				response.getWriter().write("{\"status\": \"success\"}");
			} else {
				response.getWriter().write("{\"status\": \"error\",\"msgError\":\"No se encontró el correo electrónico a borrar.\"}");
			}
		} catch (Exception e) {
			log.error("Error al borrar email: ", e);
			// Respuesta JSON si algo ha ido mal
			response.getWriter().write("{\"status\": \"error\",\"msgError\":\"" + Constants.MSG_GENERIC_ERROR + "\"}");
		}
	}

	@RequestMapping(value = "/savePageSize", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> savePageSize(
			@RequestParam(value = "pageSize") final String pageSize) throws Exception {
		ArrayList<String> errors = new ArrayList<String>();
		try {			
			// Se recupera el usuario autenticado..
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			
			themeBO.savePageSizeUser(pageSize, user);
			
		} catch (Exception e) {
			log.error("Error al insertar el tamaño de página: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Para modificar el flag de notificaciones Push del usuario
	 * @param primaryKey
	 * @param notify
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/modifyPushNotif", method = RequestMethod.GET)
	public void modifyPushNotif(
			@RequestParam("primaryKey") final String primaryKey,
			@RequestParam("lnotify") final String lnotify,
			final HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		try {
			PfUsersDTO pfUser = userAdmBO.getUserByPK(Long.parseLong(primaryKey));
			if(pfUser  != null) {
				//Se cargan las modificaciones
				userAdmBO.updateLNotifyPush(pfUser, Constants.C_YES.equals(lnotify));
				response.getWriter().write("{\"status\": \"success\"}");
			} else {
				response.getWriter().write("{\"status\": \"error\",\"msgError\":\"No se encontró el usuario a modificar.\"}");
			}
		} catch (Exception e) {
			log.error("Error al borrar el flag de notificaciones Push: ", e);
			// Respuesta JSON si algo ha ido mal
			response.getWriter().write("{\"status\": \"error\",\"msgError\":\"" + Constants.MSG_GENERIC_ERROR + "\"}");
		}
	}
	
	/**
	 * Para borrar el flag de notificaciones Push del usuario
	 * @param primaryKey
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/deletePushNotif", method = RequestMethod.GET)
	public void deletePushNotif(
			@RequestParam("primaryKey") final String primaryKey,
			final HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		try {
			PfUsersDTO pfUser = userAdmBO.getUserByPK(Long.parseLong(primaryKey));
			if(pfUser != null) {
				userAdmBO.updateLNotifyPush(pfUser, false);
				response.getWriter().write("{\"status\": \"success\"}");
			} else {
				response.getWriter().write("{\"status\": \"error\",\"msgError\":\"No se encontró el usuario a borrar.\"}");
			}
		} catch (Exception e) {
			log.error("Error al borrar el flag de notificaciones Push: ", e);
			// Respuesta JSON si algo ha ido mal
			response.getWriter().write("{\"status\": \"error\",\"msgError\":\"" + Constants.MSG_GENERIC_ERROR + "\"}");
		}
	}

	
	/**
	 * Inserción de filtro de peticiones validadas
	 * @param primaryKey
	 * @param apps
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insertarValorfiltroValidadorMovilActivo")
	public @ResponseBody ArrayList<String> insertFilter(@RequestParam("hasFilter") final String hasFilter) throws Exception {

		ArrayList<String> errors = new ArrayList<String>();
		try {
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO userBD = userAdmBO.getUserByPK(authorization.getUserDTO().getPrimaryKey());
			if (hasFilter.equalsIgnoreCase(Constants.C_YES)){
				userParameterBO.saveParamUser(Constants.FILTRO_VALIDADOR_MOVIL_ACTIVO, Constants.C_YES, userBD);
			}else{				
				userParameterBO.saveParamUser(Constants.FILTRO_VALIDADOR_MOVIL_ACTIVO, Constants.C_NOT, userBD);
			}
		} catch (Exception e) {
			log.error("Error al insertar el filtro de peticiones validadas: ", e);
			errors.add("Error al insertar el filtro de peticiones validadas: " + e.getMessage());
		}
		return errors;
	}
	
	@RequestMapping(value = "/modifyMostrarFirmanteAnterior", method = RequestMethod.GET)
	public @ResponseBody ArrayList<String> modifyMostrarFirmanteAnterior(
			@RequestParam(value = "mostrarFirmanteAnterior") final String mostrarFirmanteAnterior) throws Exception {
		ArrayList<String> errors = new ArrayList<String>();
		try {			
			// Se recupera el usuario autenticado..
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			
			userAdmBO.updateMostrarFirmanteAnterior(user, Constants.C_YES.equals(mostrarFirmanteAnterior));
			
		} catch (Exception e) {
			log.error("Error al modificar el parámetro Mostrar firmante anterior: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}

}