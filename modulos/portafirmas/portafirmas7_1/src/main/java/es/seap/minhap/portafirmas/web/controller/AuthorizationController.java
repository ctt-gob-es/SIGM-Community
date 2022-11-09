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
import java.util.List;

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

import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.business.configuration.FilterBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationTypesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.UserAuthorization;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.converter.UserAuthorizationConverter;
import es.seap.minhap.portafirmas.web.validation.UserAuthorizationValidator;

@Controller
@RequestMapping("configuration/authorization")
public class AuthorizationController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private AuthorizationBO authorizationBO;

	@Autowired
	private FilterBO filterBO;
	
	@Autowired
	private NoticeBO noticeBO;
	
	@Autowired
	private RequestBO requestBO;

	@Autowired
	private UserAdmBO	userAdmBO;
	
	@Autowired
	private UserAuthorizationConverter userAuthorizationConverter;
	
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;
	
	/**
	 * Carga la lista de autorizados en la pestaña correspondiente
	 * @return
	 */
	@RequestMapping(value = "/loadAuthorizations", method = RequestMethod.GET)
	public ModelAndView loadAuthorizations() {
		ModelMap model = null;
		try {
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			
			List<AbstractBaseDTO> authorizedList = authorizationBO.queryUserAuthorizationsList(user);
			List<AbstractBaseDTO> authorizationsTypesList = filterBO.queryAuthorizationTypesList();

			model = new ModelMap();
			model.addAttribute("user", user);
			model.addAttribute("authorizedList",authorizedList);
			model.addAttribute("authorizationsTypesList", authorizationsTypesList);
		} catch (Exception e) {
			log.error("Error al cargar las autorizaciones: ", e);
		}

		return new ModelAndView("authorizations", model);
	}	
	
	/**
	 * Método para aceptar, revocar y rechazar
	 * @param primaryKey
	 * @param accion
	 * @return
	 */
	@RequestMapping(value = "/changeAuthorization")
	public @ResponseBody ArrayList<String> changeAuthorization(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "accion") final String accion) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			PfUsersAuthorizationDTO usersAuthorizationDTO = authorizationBO.queryAuthorizationByPkTrans(Long.parseLong(primaryKey));
			if("revocar".equals(accion)) {
				authorizationBO.revokeAuthorization(usersAuthorizationDTO);
			}
			if("rechazar".equals(accion)) {
				authorizationBO.rejectAuthorization(usersAuthorizationDTO);
			    noticeBO.noticeDeniedAuthorization(usersAuthorizationDTO, true, false);
			}
			if("aceptar".equals(accion)) {
				authorizationBO.acceptAuthorization(usersAuthorizationDTO);
			    noticeBO.noticeAcceptedAuthorization(usersAuthorizationDTO, true, false);
			}
		} catch (Exception e) {
			log.error("Error al " + accion + " la autorización: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		return errors;
	}
	
	/**
	 * @param primaryKey
	 * @param frequest
	 * @param frevocation
	 * @param observations
	 * @return
	 */
	@RequestMapping(value = "/insertAuthorization")
	public @ResponseBody ArrayList<String> insertAuthorization(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "authorizationType") final String authorizationTypePk,
			@RequestParam(value = "authorizedPk") final String authorizedPk,
			@RequestParam(value = "frequest") final String frequest,
			@RequestParam(value = "hrequest") final String hrequest,
			@RequestParam(value = "frevocation") final String frevocation,
			@RequestParam(value = "hrevocation") final String hrevocation,
			@RequestParam(value = "observations") final String observations) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se recupera el usuario autenticado..
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO pfUserDTO = authorization.getUserDTO();

			// Se obtienen la información de la vista
			UserAuthorization userAuthorization = new UserAuthorization();
			if(!Util.esVacioONulo(primaryKey)) {
				userAuthorization.setPrimaryKey(Long.parseLong(primaryKey));
			}
			userAuthorization.setAuthorizationTypeId(authorizationTypePk);
			userAuthorization.setReceiverId(authorizedPk);
			userAuthorization.setFrequest(frequest);
			userAuthorization.setHrequest(hrequest);
			userAuthorization.setFrevocation(frevocation);
			userAuthorization.setHrevocation(hrevocation);
			userAuthorization.setTobservations(observations);
			
			// Se valida la vista
			userAuthorizationValidator.validate(userAuthorization, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se monta la autorización para dar de alta
			PfUsersDTO pfAuthorizedUserDTO = userAdmBO.queryUsersByPk(Long.parseLong(userAuthorization.getReceiverId()));
			PfAuthorizationTypesDTO pfAuthorizationTypeDTO = 
				authorizationBO.queryAuthorizationTypeByPk(Long.parseLong(userAuthorization.getAuthorizationTypeId()));
			
			// se vuelcan los datos obtenidos de la vista
			PfUsersAuthorizationDTO pfUsersAuthorizationDTO = userAuthorizationConverter.envelopeToDTO(userAuthorization);

			pfUsersAuthorizationDTO.setPfUser(pfUserDTO);
			pfUsersAuthorizationDTO.setPfAuthorizedUser(pfAuthorizedUserDTO);
			pfUsersAuthorizationDTO.setPfAuthorizationType(pfAuthorizationTypeDTO);

			// Se validan las reglas de negocio
			authorizationBO.validateAuthorization(pfUsersAuthorizationDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza la persistencia
			authorizationBO.saveAuthorization(pfUsersAuthorizationDTO);
		    noticeBO.noticeNewAuthorization(pfUsersAuthorizationDTO, true, false);
			
		} catch (Exception e) {
			log.error("Error al insertar la autorización: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Recupera los usuarios para el autocompletado de alta de autorizados.
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteAuthorizations")
	public @ResponseBody List<UserAutocomplete> autocompleteAuthorizations(@RequestParam(value = "term") final String term) {
		// Se obtiene la lista
		List<UserAutocomplete> results = null;
		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			results = getUsersList(user, term);
		} catch (Exception e) {
			log.error("Error en autocompletar nombre en autorizaciones: ", e);
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