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
import java.util.Date;
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

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.business.configuration.FilterBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationTypesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.UserAuthorization;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;
import es.seap.minhap.portafirmas.web.converter.UserAuthorizationConverter;
import es.seap.minhap.portafirmas.web.validation.UserAuthorizationValidator;

@Controller
@RequestMapping("usersManagement/seatAuthorization")
public class SeatAuthorizationController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private AuthorizationBO authorizationBO;
	
	@Autowired
	private FilterBO filterBO;
	
	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private UserAuthorizationConverter userAuthorizationConverter;
	
	@Autowired
	private UserAuthorizationValidator userAuthorizationValidator;
	
	/**
	 * Carga la pestaña de autorizaciones de inicio
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadAuthorizations", method = RequestMethod.GET)
	public ModelAndView loadAuthorizations(ModelMap model) throws Exception {
		try {
			List<AbstractBaseDTO> authorizationsTypesList = filterBO.queryAuthorizationTypesList();
			model.addAttribute("authorizationsTypesList", authorizationsTypesList);
		} catch (Exception e) {
			log.error("Error al cargar la pestaña de autorizaciones para administrador de sede: ", e);
			throw e;
		}
		return new ModelAndView("seatAuthorizations", model);
	}
	
	/**
	 * Carga las lista de autorizaciones de un usuario concreto
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping(value = "/loadList", method = RequestMethod.GET)
	public ModelAndView loadList(ModelMap model, @RequestParam(value = "userId") final String userId) throws Exception {
		try {
			PfUsersDTO user = userAdmBO.queryUsersByPk(Long.parseLong(userId));
			
			List<AbstractBaseDTO> sendListDTO = authorizationBO.queryUserAutSentList(user);
			List<UserAuthorization> sendList = userAuthorizationConverter.DTOtoEnvelopeList(sendListDTO);
			List<AbstractBaseDTO> receiverListDTO = authorizationBO.queryUserAutReceiverList(user);
			List<UserAuthorization> receiverList = userAuthorizationConverter.DTOtoEnvelopeList(receiverListDTO);		
			List<AbstractBaseDTO> authorizationsTypesList = filterBO.queryAuthorizationTypesList();

			model.addAttribute("user", user);
			model.addAttribute("receiverAutList",receiverList);
			model.addAttribute("sendAutList",sendList);
			model.addAttribute("authorizationsTypesList", authorizationsTypesList);

		} catch (Exception e) {
			log.error("Error al cargar las autorizaciones para administrador de sede: ", e);
			throw e;
		}
		return new ModelAndView("seatAuthorizationsList", model);
	}
	
	@RequestMapping(value = "/insert")
	public 	@ResponseBody ArrayList<String> insert(@ModelAttribute UserAuthorization userAuthorization) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se valida la vista
			userAuthorizationValidator.validateSeatAdmin(userAuthorization, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se monta la autorización para dar de alta
			PfUsersDTO pfAuthorizedUserDTO = userAdmBO.queryUsersByPk(Long.parseLong(userAuthorization.getReceiverId()));
			PfUsersDTO pfUserDTO = userAdmBO.queryUsersByPk(Long.parseLong(userAuthorization.getRemittentId()));
			PfAuthorizationTypesDTO pfAuthorizationTypeDTO = 
				authorizationBO.queryAuthorizationTypeByPk(Long.parseLong(userAuthorization.getAuthorizationTypeId()));
			
			// se vuelcan los datos obtenidos de la vista
			PfUsersAuthorizationDTO pfUsersAuthorizationDTO = userAuthorizationConverter.envelopeToDTO(userAuthorization);

			pfUsersAuthorizationDTO.setPfUser(pfUserDTO);
			pfUsersAuthorizationDTO.setPfAuthorizedUser(pfAuthorizedUserDTO);
			pfUsersAuthorizationDTO.setPfAuthorizationType(pfAuthorizationTypeDTO);
			pfUsersAuthorizationDTO.setFauthorization(new Date());

			// Se validan las reglas de negocio
			authorizationBO.validateAuthorization(pfUsersAuthorizationDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza la persistencia
			authorizationBO.saveAuthorization(pfUsersAuthorizationDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar la autorización: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	@RequestMapping(value = "/update")
	public 	@ResponseBody ArrayList<String> update(@ModelAttribute UserAuthorization userAuthorization) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se obtiene el DTO que se está modificando
			PfUsersAuthorizationDTO pfUsersAuthorizationDTO = userAuthorizationConverter.envelopeToDTOUpdate(userAuthorization);

			// Se realiza la persistencia
			authorizationBO.saveAuthorization(pfUsersAuthorizationDTO);
			
		} catch (Exception e) {
			log.error("Error al actualizar la autorización: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Para cargar una autorización
	 * @param userAuthorization
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/load")
	public String load(@ModelAttribute UserAuthorization userAuthorization, ModelMap model) throws Exception {
		try {
			// Se realiza la persistencia
			PfUsersAuthorizationDTO authorizationDTO = authorizationBO.queryAuthorizationByPk(userAuthorization.getPrimaryKey());
			UserAuthorization authorization = userAuthorizationConverter.DTOtoEnvelope(authorizationDTO);
			List<AbstractBaseDTO> authorizationsTypesList = filterBO.queryAuthorizationTypesList();

			model.addAttribute("authorization", authorization);
			model.addAttribute("authorizationsTypesList", authorizationsTypesList);
			
		} catch (Exception e) {
			log.error("Error al cargar la autorización a modificar: ", e);
			throw e;
		}	
		return "modifySeatAuthorizationModal";
	}
	
	@RequestMapping(value = "autocomplete")
	public @ResponseBody List<UserAutocomplete> autocomplete(@RequestParam(value = "term") final String term) {
		List<UserAutocomplete> results = null;
		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();

			// Se toma para el filtro el último valor introducido
			String[] values = term.split(",");
			String searchText = values[values.length-1].trim();

			UsersParameters usersParameters = new UsersParameters();
			usersParameters.setUser(user);
			usersParameters.setSearchText(searchText);
			
			List<String> tipos = new ArrayList<String>();
			tipos.add(Constants.C_TYPE_USER_USER);
			usersParameters.setType(tipos);

			// Se filtran los usuarios en base a la búsqueda
			List<AbstractBaseDTO> users = userAdmBO.queryUsersComplete(usersParameters);

			// Se convierten los resultados
			results = new ArrayList<UserAutocomplete>();
			for (AbstractBaseDTO userAux : users) {
				results.add(new UserAutocomplete((PfUsersDTO) userAux));
			}
			
		} catch (Exception e) {
			log.error("Error al autocompletar en usuarios de grupo: ", e);
			return null;
		}

		return results;
	}

}
