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
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

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

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.MessageAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfMessagesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersMessageDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Message;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;
import es.seap.minhap.portafirmas.web.validation.MessageValidator;

@Controller
@RequestMapping("administration/message")
public class MessageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private MessageAdmBO messageAdmBO;
	
	@Autowired
	private ProvinceBO provinceBO;
	
	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private MessageValidator messageValidator;
	
	@Resource(name = "messageProperties")
	private Properties messages;
	
	@RequestMapping(value = "/loadMessages", method = RequestMethod.GET)
	public ModelAndView loadMessages() {
		ModelMap model = null;
		
		try {
			// Carga de los mensajes
			List<AbstractBaseDTO> messages = messageAdmBO.queryList();
		
			
			model = new ModelMap();
			
			loadCombos(model);
			model.addAttribute("message", new Message());
			model.addAttribute("messageList", messages);		

		} catch (Throwable t) {
			log.error("Error al cargar los mensajes: ", t);
		}
		return new ModelAndView("messages", model);
	}
	
	
	@RequestMapping(value = "/message", method = RequestMethod.POST)
	public String load(@ModelAttribute Message message, ModelMap model, 
			@RequestParam(value = "primaryKey") final String primaryKey) {

		try {
			if(!Util.esVacioONulo(primaryKey)) {
				// Carga de los datos de la request
				PfMessagesDTO pfMessageDTO = messageAdmBO.messagesByPk(Long.valueOf(primaryKey));
				
				if (pfMessageDTO != null) {
					message.setPrimaryKey(pfMessageDTO.getPrimaryKeyString());
					message.setDsubject(pfMessageDTO.getDsubject());
					message.setTtext(pfMessageDTO.getTtext());
					message.setFstart(Util.getInstance().dateToString(pfMessageDTO.getFstart(), "dd/MM/yyyy"));
					message.setFexpiration(Util.getInstance().dateToString(pfMessageDTO.getFexpiration(), "dd/MM/yyyy"));
					
					if (pfMessageDTO.getPfMessageScope() != null) {
						message.setScopeType(pfMessageDTO.getPfMessageScope().getPrimaryKeyString());
						if (Constants.SCOPE_MESSAGE_USER.equals(pfMessageDTO.getPfMessageScope().getPrimaryKeyString())) {
							List<AbstractBaseDTO> userMessageList = messageAdmBO.getUserMessageById(pfMessageDTO.getPrimaryKey());
							if (userMessageList != null && userMessageList.size() > 0) {
								PfUsersMessageDTO userMessage = (PfUsersMessageDTO) userMessageList.get(0);
								
								UserAutocomplete userAuto = new UserAutocomplete(userMessage.getPfUser());
								
								message.setUserId(userAuto.getId());
								message.setUserName(userAuto.getLabel());
							}
						}
					}
					
					if (pfMessageDTO.getPfProvince() != null) {
						message.setProvinceCode(pfMessageDTO.getPfProvince().getCcodigoprovincia());
					}
					
					
				}
			}
			
			loadCombos(model);
			model.addAttribute("message", message);
			model.addAttribute("messageList", null);

		} catch (Throwable t) {
			log.error("Error al cargar mensajes: ", t);
		}
		return "messageModal";
	
	}
	
	/**
	 * Alta o modificación de un mensaje
	 * @param primaryKey
	 * @param serverCode
	 * @param serverDescription
	 * @param serverIsDefault
	 * @return
	 */
	@RequestMapping(value = "/saveMessage")
	public @ResponseBody ArrayList<String> saveMessage(
			@ModelAttribute(value = "message") Message message) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
	
			messageValidator.validate(message, errors);
			
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = 
					(UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO currentUser = authorization.getUserDTO();
			
			// Se obtienen la información de la vista
			PfMessagesDTO pfMessageDTO = new PfMessagesDTO();
			if(!Util.esVacioONulo(message.getPrimaryKey())) {
				pfMessageDTO = messageAdmBO.messagesByPk(Long.valueOf(message.getPrimaryKey()));
			}

			pfMessageDTO.setDsubject(message.getDsubject());
			pfMessageDTO.setTtext(message.getTtext());
			pfMessageDTO.setFstart(Util.getInstance().stringToDate(message.getFstart()));
			if(!Util.esVacioONulo(message.getFexpiration())) {
				pfMessageDTO.setFexpiration(Util.getInstance().stringToDate(message.getFexpiration()));					
			} else {
				pfMessageDTO.setFexpiration(null);
			}
			
			if (Constants.SCOPE_MESSAGE_PROVINCE.equals(message.getScopeType())) {
				if(!Util.esVacioONulo(message.getProvinceCode())) {
					PfProvinceDTO pfProvinceDTO = provinceBO.getProvinceByCod(message.getProvinceCode());
					pfMessageDTO.setPfProvince(pfProvinceDTO);
				} 
			} else if (Constants.SCOPE_MESSAGE_USER.equals(message.getScopeType())) {
				if(!Util.esVacioONulo(message.getUserId())) {
					PfUsersDTO pfUsersDTO = userAdmBO.queryUsersByPk(Long.valueOf(message.getUserId()));
					pfMessageDTO.setPfUser(pfUsersDTO);
					
				}
			}
		
			messageAdmBO.insertMessage(pfMessageDTO, message.getScopeType(), currentUser);
			
		} catch (Exception e) {
			log.error("Error al insertar el mensaje: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Borrado de un servidor
	 * @param primaryKey
	 * @return
	 */
	@RequestMapping(value = "/deleteMessage")
	public @ResponseBody ArrayList<String> deleteMessage(
			@RequestParam(value = "primaryKey") final String primaryKey) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			if(!Util.esVacioONulo(primaryKey)) {
				messageAdmBO.deleteMessage(Long.valueOf(primaryKey));
			}
			
		} catch (Exception e) {
			log.error("Error al borrar el mensaje: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
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
	
	private void loadCombos(ModelMap model) {
		// Carga el combo de los ámbitos
		Map<String, Object> scopeListMap = messageAdmBO.getScopeList();
		// Carga el combo de las sedes
		Map<String, Object> provincesMap = messageAdmBO.getProvices();
		
		model.addAttribute("scopeList", scopeListMap);
		model.addAttribute("provincesMap", provincesMap);
	}

	
}
