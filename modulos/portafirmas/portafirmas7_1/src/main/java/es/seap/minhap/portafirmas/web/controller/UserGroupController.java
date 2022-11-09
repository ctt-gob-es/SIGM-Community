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
import java.util.Iterator;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.GroupBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersGroupsDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.Group;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UserGroup;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;
import es.seap.minhap.portafirmas.web.converter.UserGroupConverter;

@Controller
@RequestMapping("usersManagement/userGroup")
public class UserGroupController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private GroupBO groupBO;
	
	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private UserGroupConverter userGroupConverter;
	
	@RequestMapping(value = "load", method = RequestMethod.POST)
	public String load(@ModelAttribute Group group, Model model) throws Exception {
		try {
			// Se recupera el grupo y sus usuarios
			PfGroupsDTO groupDTO =  groupBO.findGroupById(Long.parseLong(group.getId()));
			List<AbstractBaseDTO> usersGroup =  groupBO.getUsersGroupList(groupDTO);
			
			// Se configura el modelo
			setupModel(model, groupDTO, usersGroup);

		} catch (Exception e) {
			log.error("Error al cargar usuarios de grupo: ", e);
			throw e;
		}
		return "groupUsersModal";
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@ModelAttribute UserGroup userGroup, Model model) throws Exception {
		try {
			// Se crea el usuario DTO a persistir
			PfUsersGroupsDTO pfUsersGroupsDTO = userGroupConverter.envelopeToDTO(userGroup);

			// Se realiza la persistencia del usuario
			groupBO.saveUserGroup(pfUsersGroupsDTO);
			
			// Se prepara el modelo para la vista
			List<AbstractBaseDTO> usersGroup =  groupBO.getUsersGroupList(pfUsersGroupsDTO.getPfGroup());
			setupModel(model, pfUsersGroupsDTO.getPfGroup(), usersGroup);

		} catch (Exception e) {
			log.error("Error al insertar el usuario del grupo: ", e);
			throw e;
		}
		return "groupUsersModal";
	}
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String delete(@ModelAttribute UserGroup userGroup, Model model) throws Exception {
		try {
			// Se obtiene la relación usuario-grupo
			PfUsersGroupsDTO userGroupDTO = groupBO.getUserGroupById(Long.parseLong(userGroup.getUserGroupId()));

			// Se borra la relación
			groupBO.deleteUserGroup(userGroupDTO);
			
			// Se prepara el modelo para la vista
			List<AbstractBaseDTO> usersGroup =  groupBO.getUsersGroupList(userGroupDTO.getPfGroup());	
			setupModel(model, userGroupDTO.getPfGroup(), usersGroup);
			
		} catch (Exception e) {
			log.error("Error al borrar el usuario del grupo: ", e);
			throw e;
		}
		return "groupUsersModal";
	}

	/**
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocomplete")
	public @ResponseBody List<UserAutocomplete> autocomplete(@RequestParam(value = "term") final String term,
			@RequestParam(value = "groupId") final String groupId) {
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
			
			// Se obtienen los validadores
			PfGroupsDTO groupDTO =  groupBO.findGroupById(Long.parseLong(groupId));
			List<AbstractBaseDTO> usersGroup =  groupBO.getUsersGroupList(groupDTO);
			
			// Se quitan los validadores que ya tiene
			for (Iterator<AbstractBaseDTO> it = usersGroup.iterator(); it.hasNext();) {
				PfUsersGroupsDTO pfUsersGroupsDTO = (PfUsersGroupsDTO) it.next();
				results.remove(new UserAutocomplete(pfUsersGroupsDTO.getPfUser()));
			}
			
		} catch (Exception e) {
			log.error("Error al autocompletar en usuarios de grupo: ", e);
			return null;
		}

		return results;
	}

	/**
	 * Para obligar a enviar siempre la misma información a la vista
	 * @param model
	 * @param pfGroup
	 * @param usersGroup
	 */
	private void setupModel(Model model, PfGroupsDTO pfGroup, List<AbstractBaseDTO> usersGroup) {
		model.addAttribute("group", pfGroup);
		model.addAttribute("userGroupList", usersGroup);
	}
	
}
