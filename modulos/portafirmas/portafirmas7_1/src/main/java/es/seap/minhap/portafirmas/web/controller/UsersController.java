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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
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

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.business.OrganismBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProfilesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.security.authentication.AuthenticationHelper;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.security.authorities.GrantedAuthorityImpl;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.beans.AdminSeat;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;
import es.seap.minhap.portafirmas.web.converter.UserConverter;
import es.seap.minhap.portafirmas.web.validation.AuthenticateValidator;
import es.seap.minhap.portafirmas.web.validation.UserValidator;

@Controller
@RequestMapping("usersManagement")
public class UsersController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private OrganismBO organismBO;

	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private UserValidator userValidator;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private AuthenticateBO authenticateBO;

	@Autowired
	private AuthenticateValidator authenticateValidator;

	@Autowired
	private AuthenticationHelper authenticationHelper;

	@Autowired
	private SessionBO sessionBO;

	@Autowired
	private UtilComponent util;
	
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
		return "usersManagement";
	}

	/**
	 * Si la petición llega por POST, se está recargando la página por
	 * situaciones varias: Paginación, búsqueda, alta de usuario, eliminación,
	 * etc..
	 * 
	 * @param usersParameters
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String processSubmit(@ModelAttribute("usersParameters") UsersParameters usersParameters, Model model,
			final HttpServletRequest request) {
		
		process(model, usersParameters);
		
		//Se refrescan los perfiles del usuario
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
		PfUsersDTO userToValidate = authenticateBO.authenticateByPK(user.getPrimaryKeyString());
		
		List<GrantedAuthority> authorities = null;
		authorities = authenticationHelper.getRoles(userToValidate);
		
		UserAuthentication userAuthentication = new UserAuthentication(
				user, authorization.getCredentials(), authorities, userToValidate, authorization.getSerialNumber(),user);
		
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
		
		HttpSession session = request.getSession();
		sessionBO.insertSessionAttributes(
				session.getId(), request.getRemoteHost(), request.getHeader("User-Agent"), userToValidate.getFullName(), userToValidate);
		
		return "usersManagement";
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
		tipos.add(Constants.C_TYPE_USER_USER);
		usersParameters.setType(tipos);

		// Se obtiene la información para el conformar el modelo
		List<AbstractBaseDTO> usersList = null;
		if(!userLogDTO.isAdministrator() || util.noEsVacio(usersParameters.getSearchText())) {
			usersList = userAdmBO.queryUsersComplete(usersParameters);
		}
		
		//Catalogo de perfiles
		List<AbstractBaseDTO> profilesList = applicationBO.queryProfileList(userLogDTO);
		
		List<PfUnidadOrganizacionalDTO> orgList = provinceBO.getOrgList(userLogDTO);
		
		List<AbstractBaseDTO> seatList = provinceBO.getSeatList(userLogDTO);
		// Se obtiene la lista de sedes que administra en su caso
		List<AdminSeat> adminSeatList = userAdmBO.getAdminSeatList(seatList);
		
		// Se obtiene la lista de organismos que administra en su caso
		List<AdminSeat> adminOrgList = userAdmBO.getAdminOrgList(orgList);

		// Se recupera el usuario autenticado
		model.addAttribute("usersList", usersList);
		model.addAttribute("usersParameters", usersParameters);
		model.addAttribute("profilesList", profilesList);
		model.addAttribute("seatList", seatList);
		model.addAttribute("user", new User());
		model.addAttribute("adminSeatList", adminSeatList);
		model.addAttribute("adminOrgList", adminOrgList);
		model.addAttribute("authentication", authorization);
	}

	@RequestMapping(value = "user/save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> saveUser(@ModelAttribute("user") User user) {
		ArrayList<String> errors = new ArrayList<String>();
				
		try {
			// Se valida la vista
			userValidator.validate(user, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se vuelcan los datos obtenidos de la vista
			PfUsersDTO pfUsersDTO = userConverter.envelopeToDTO(user);

			// Se validan las reglas de negocio
			userAdmBO.validateUser(pfUsersDTO, errors);
			userAdmBO.validateLDAP(pfUsersDTO, errors, user);			
			
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se obtienen los perfiles del usuario,..
			List <PfUsersProfileDTO> userProfileList = userConverter.getUserProfileList(user);
			// .. el usuario lDap y la contraseña..
			List <PfUsersParameterDTO> userParameterList = userConverter.getUserParamenterList(user);
			// .. y por último, las sedes que administra
			List <PfProvinceAdminDTO> provinceAdminList = userConverter.getProvinceAdminList(user);
			//..y los emails para que no se pierdan
			List <PfUsersEmailDTO> emailList = new ArrayList<PfUsersEmailDTO>();
			emailList.addAll(pfUsersDTO.getPfUsersEmails());
			//añadir el correo electrónico de usuario
			if (user.getEmail()!=null && !"".equals(user.getEmail())) {
				PfUsersEmailDTO userMail = new PfUsersEmailDTO();
				userMail.setLnotify(true);
				userMail.setDemail(user.getEmail());
				emailList.add(0, userMail);	
			}
			
			List <PfUnidadOrganizacionalDTO> organismoList = pfUsersDTO.getPfUnidadOrganizacionalList();				
			if (organismoList != null && !organismoList.isEmpty())				
				pfUsersDTO.getPfUnidadOrganizacionalList().removeAll(organismoList);

			if(user.getAdminOrg()!=null && !"".equalsIgnoreCase(user.getAdminOrg())){
				String id = user.getAdminOrg();
				PfUnidadOrganizacionalDTO organismo = organismBO.getOrganismoById(id);
				pfUsersDTO.getPfUnidadOrganizacionalList().add(organismo);
			}

			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			// Se realiza la persistencia del usuario
			userAdmBO.saveUser(authorization, pfUsersDTO, userProfileList, userParameterList, provinceAdminList, emailList);
			
		} catch (Exception e) {
			log.error("Error al insertar el usuario: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}

	@RequestMapping(value = "user/load", method = RequestMethod.POST)
	public String loadUser(@ModelAttribute("user") User user, Model model) {
		try {
			// Se recupera el usuario autenticado para obtener la lista de sedes
			// que administra
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO userLogDTO = authorization.getUserDTO();
			List<AbstractBaseDTO> seatList = provinceBO.getSeatList(userLogDTO);
			
			//Catalogo de perfiles
			List<AbstractBaseDTO> profilesList = applicationBO.queryProfileList(userLogDTO);
			
			List<PfUnidadOrganizacionalDTO> orgList = provinceBO.getOrgList(userLogDTO);

			//Quitamos el perfil SIMULAR
			List<AbstractBaseDTO> newProfilesList = new ArrayList<AbstractBaseDTO>();
			for(AbstractBaseDTO abs : profilesList){
				PfProfilesDTO profile = (PfProfilesDTO) abs;
				if(!Constants.C_PROFILES_SIMULATE.equals(profile.getCprofile())){
					newProfilesList.add(abs);
				}
			}
			
			// Se crea obtiene el usuario DTO
			PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(user.getPrimaryKey()));
			user = userConverter.DTOtoEnvelope(pfUsersDTO);

			// Se obtiene la lista de sedes que administra en su caso
			List<AdminSeat> adminSeatList = userAdmBO.getAdminSeatList(pfUsersDTO, seatList);
			
			// Se obtiene la lista de organismos que administra en su caso
			List<AdminSeat> adminOrgList = userAdmBO.getAdminOrgList(pfUsersDTO, orgList);

			model.addAttribute("profilesList", newProfilesList);
			model.addAttribute("user", user);
			model.addAttribute("seatList", seatList);
			model.addAttribute("adminSeatList", adminSeatList);
			model.addAttribute("adminOrgList", adminOrgList);

		} catch (Exception e) {
			log.error("Error al obtener el usuario: ", e);
		}
		return "userModal";
	}

	@RequestMapping(value = "user/load", method = RequestMethod.GET)
	public @ResponseBody User loadUser(@ModelAttribute("dni") String dni) {
		User user = null;
		try {
			// Se crea obtiene el usuario DTO
			PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByIdentifier(dni.toUpperCase());
			user = userConverter.DTOtoEnvelope(pfUsersDTO);

		} catch (Exception e) {
			log.error("Error al obtener el usuario: ", e);
		}
		return user;
	}

	@RequestMapping(value = "user/delete", method = RequestMethod.POST)
	public @ResponseBody HashMap<String, ArrayList<String>> deleteUser(@ModelAttribute("user") User user) {
		HashMap<String, ArrayList<String>> response = new HashMap<String, ArrayList<String>>();
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(user.getPrimaryKey()));

			warnings = userAdmBO.requestAssociated(pfUsersDTO);
			if (warnings.isEmpty()) {
				userAdmBO.deleteUser(pfUsersDTO);
			}
			response.put("warnings", warnings);

		} catch (Exception e) {
			log.error("Error al borrar el usuario: ", e);
			ArrayList<String> errors = new ArrayList<String>();
			errors.add(Constants.MSG_GENERIC_ERROR);
			response.put("errors", errors);
		}
		return response;
	}

	@RequestMapping(value = "user/revoke", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> revokeUser(@ModelAttribute("user") User user) {
		ArrayList<String> error = new ArrayList<String>();
		try {
			PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(user.getPrimaryKey()));
			userAdmBO.revokeUser(pfUsersDTO);
		} catch (Exception e) {
			log.error("Error al borrar el usuario: ", e);
			error.add(Constants.MSG_GENERIC_ERROR);
		}
		return error;
	}

	@RequestMapping(value = "user/simulate", method = RequestMethod.POST)
	public ModelAndView simulateUser(@RequestParam("simulateUserPk") String simulateUserPk, ModelMap model,
			HttpServletRequest request) {
		String targetUrl = null;
		try {
			String error = authenticateValidator.valiteUser(simulateUserPk);
			if (error != null) {
				return util.throwError(error, model);
			}

			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
					// PfUsersDTO user = (PfUsersDTO)
					// authorization.getPrincipal();

			// Se obtiene el usuario que queremos simular
			PfUsersDTO simulatedUser = authenticateBO.authenticateByPK(simulateUserPk);

			// Si el usuario es el principal se asignan todos los roles
			List<GrantedAuthority> authorities = null;
			if (simulatedUser.getPrimaryKeyString()
					.equals(((PfUsersDTO) authorization.getPrincipal()).getPrimaryKeyString())) {
				authorities = authenticationHelper.getRoles(simulatedUser);
			} else {
				// Si es un validador sólo de acceso y validador
				authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_ACCESS));
				//authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_VALIDATOR));
			}

			// Añadimos el rol de simulador
			authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_SIMULATE));

			UserAuthentication userAuthentication = new UserAuthentication(simulatedUser,
					authorization.getCredentials(), authorities, simulatedUser, authorization.getSerialNumber(),(PfUsersDTO) authorization.getPrincipal());

			//marcamos el flag de usuario simulado.
			userAuthentication.setUserSimulado(true);
			
			// Se guardan el nuevo objeto de autenticación
			SecurityContextHolder.getContext().setAuthentication(userAuthentication);

			// Se modifican los atributos de sesion para que coincidan con el
			HttpSession session = request.getSession();
			sessionBO.insertSessionAttributes(session.getId(), request.getRemoteHost(), request.getHeader("User-Agent"),
					simulatedUser.getFullName(), simulatedUser);

			if (userValidator.necesitaPantallaIntermediaDeLogin(simulatedUser)) {
				// Si el usuario es validador se le muestra la opción de hacer
				// login con ese rol
				targetUrl = "/login/validatorLogin";

				// Se obtiene la lista de usuarios validados y se mete en el
				// objeto de autenticación
				userAuthentication.setValidatedUsers(simulatedUser.getValidadorDe());

			} else {
				targetUrl = "/inbox";
			}

		} catch (Exception e) {
			log.error("Error al simular el usuario: ", e);
			return util.throwError(Constants.MSG_GENERIC_ERROR, model);
		}
		return new ModelAndView("redirect:" + targetUrl, model);
	}

	
}
