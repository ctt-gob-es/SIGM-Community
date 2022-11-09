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
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.business.GroupBO;
import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.AuthenticationHelper;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.security.authorities.GrantedAuthorityImpl;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.validation.AuthenticateValidator;

@Controller
@RequestMapping("authentication")
public class AuthenticationController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private AuthenticateBO authenticateBO;

	@Autowired
	private GroupBO groupBO;
	
	@Autowired
	private SessionBO sessionBO;

	@Autowired
	private UtilComponent util;
	
	@Autowired
	private AuthenticateValidator authenticateValidator;
	
	@Autowired
	private AuthenticationHelper authenticationHelper;
	
	/**
	 * Método para cambiar la autenticación de usuario a validador y viceversa
	 * 
	 * @param userPk
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeUser", method = RequestMethod.POST)
	public ModelAndView changeUser(@RequestParam("userPk") String userPk, ModelMap model, HttpServletRequest request) throws Exception {

		try {
			// Se valida la información de entrada
			String error = authenticateValidator.valiteUser(userPk);
			if(error != null) {
				return util.throwError(error, model);
			}
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
			PfUsersDTO userToValidate = authenticateBO.authenticateByPK(userPk);

			// Validación de negocio
			error = authenticateBO.valiteToValidator(user, userToValidate);
			if(error != null) {
				return util.throwError(error, model);
			}

			// Si el usuario es el principal se asignan todos los roles
			List<GrantedAuthority> authorities = null;
			if (userToValidate.getPrimaryKeyString().equals(((PfUsersDTO)authorization.getPrincipal()).getPrimaryKeyString())) {
				authorities = authenticationHelper.getRoles(userToValidate);
			} else {
				// Si es un validador sólo de acceso y validador
				authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_ACCESS));
				authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_VALIDATOR));
			}

			UserAuthentication userAuthentication =
					new UserAuthentication(user, authorization.getCredentials(),
									   	   authorities, userToValidate, authorization.getSerialNumber(),authorization.getUserOriginal());
			userAuthentication.setUserSimulado(authorization.isUserSimulado());
			// Se guardan el nuevo objeto de autenticación
			SecurityContextHolder.getContext().setAuthentication(userAuthentication);
			
			// Se modifican los atributos de sesion para que coincidan con el 
			HttpSession session = request.getSession();
			sessionBO.insertSessionAttributes(session.getId(), request.getRemoteHost(),
											  request.getHeader("User-Agent"), userToValidate.getFullName(), userToValidate);


		} catch (Exception e) {
			return util.throwError(Constants.MSG_GENERIC_ERROR, model);
		}

		return new ModelAndView("redirect:/inbox", model);
	}

	/**
	 * Método para autenticarse en un grupo del usuario
	 * 
	 * @param groupPk
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/group", method = RequestMethod.POST)
	public ModelAndView submitGroupLogin(@RequestParam(value = "groupPk") final String groupPk, ModelMap model) {
		
		try {
			// Se valida la información de entrada
			String error = authenticateValidator.valiteGroup(groupPk);
			if(error != null) {
				return util.throwError(error, model);
			}
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
			
			// Se obtiene el grupo
			PfGroupsDTO group = groupBO.findGroupByIdTrans(Long.parseLong(groupPk));

			// Validación de negocio
			error = authenticateBO.valiteGroup(user, group);
			if(error != null) {
				return util.throwError(error, model);
			}

			// Si todo va bien, se agrega el rol de grupo,..
			Collection<GrantedAuthority> authorities = authenticationHelper.getRoles(user);
			authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_GROUP));
			
			// Si el usuario es simulador se mantiene ese perfil para que solo pueda ver las peticiones
			if (authorization.isSimulador()) {
				authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_SIMULATE));
			}

			//.. y el grupo, al usuario en el contexto de autenticación
			UserAuthentication authentication = new UserAuthentication(
					user, authorization.getCredentials(), authorities, user,
					authorization.getSerialNumber(), group, authorization.getUserOriginal());
			authentication.setUserSimulado(authorization.isUserSimulado());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (Exception e) {
			return util.throwError(Constants.MSG_GENERIC_ERROR, model);
		}
		
		return new ModelAndView("redirect:/inbox");
	}
	
	/**
	 * Método para cambiar la autenticación de usuario a gestor y viceversa
	 * 
	 * @param userPk
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/changeUserGestor", method = RequestMethod.POST)
	public ModelAndView changeUserGestor(@RequestParam("gestorPk") String gestorPk, ModelMap model, HttpServletRequest request) throws Exception {

		try {
			// Se valida la información de entrada
			String error = authenticateValidator.valiteUser(gestorPk);
			if(error != null) {
				return util.throwError(error, model);
			}
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
			PfUsersDTO userToGestionar = authenticateBO.authenticateByPK(gestorPk);

			// Validación de negocio
			error = authenticateBO.valiteToGestor(user, userToGestionar);
			if(error != null) {
				return util.throwError(error, model);
			}

			// Si el usuario es el principal se asignan todos los roles
			List<GrantedAuthority> authorities = null;
			if (userToGestionar.getPrimaryKeyString().equals(((PfUsersDTO)authorization.getPrincipal()).getPrimaryKeyString())) {
				authorities = authenticationHelper.getRoles(userToGestionar);
			} else {
				// Si es un gestor sólo de acceso y gestor
				authorities = new ArrayList<GrantedAuthority>();
				authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_ACCESS));
				authorities.add(new GrantedAuthorityImpl(Constants.C_PROFILES_GESTOR));
			}

			UserAuthentication userAuthentication = 
					new UserAuthentication(user, authorization.getCredentials(),
						   	   authorities, userToGestionar, authorization.getSerialNumber(),authorization.getUserOriginal());
//			Authentication authentication = new UserAuthentication(
//					user, authorization.getCredentials(), authorities, user,
//					authorization.getSerialNumber(), authorization.getValidatedUsers(), group, authorization.getGestionatedUsers());

			// Se guardan el nuevo objeto de autenticación
			userAuthentication.setUserSimulado(authorization.isUserSimulado());
			SecurityContextHolder.getContext().setAuthentication(userAuthentication);
			
			// Se modifican los atributos de sesion para que coincidan con el 
			HttpSession session = request.getSession();
			sessionBO.insertSessionAttributes(session.getId(), request.getRemoteHost(),
											  request.getHeader("User-Agent"), userToGestionar.getFullName(), userToGestionar);


		} catch (Exception e) {
			return util.throwError(Constants.MSG_GENERIC_ERROR, model);
		}

		return new ModelAndView("redirect:/inbox", model);
	}


}
