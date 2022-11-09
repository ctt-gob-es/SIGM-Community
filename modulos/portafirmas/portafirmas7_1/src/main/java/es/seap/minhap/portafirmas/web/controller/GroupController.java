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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.GroupBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.Group;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.ErrorsAndWarnings;
import es.seap.minhap.portafirmas.web.controller.requestsandresponses.InsertGroupRequest;
import es.seap.minhap.portafirmas.web.converter.GroupConverter;
import es.seap.minhap.portafirmas.web.validation.GroupValidator;

@Controller
@RequestMapping("usersManagement/group")
public class GroupController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private GroupBO groupBO;

	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private GroupValidator groupValidator;
	
	@Autowired
	private GroupConverter groupConverter;

	/**
	 * Carga la lista de grupos de un usuario en la pestaña correspondiente
	 * @return ModelAndView
	 * @throws Exception 
	 */
	@RequestMapping(value = "/loadGroups", method = RequestMethod.GET)
	public ModelAndView loadGroups(ModelMap model) throws Exception {
		try {
			setupModel(model);
		} catch (Exception e) {
			log.error("Error al cargar los grupos: ", e);
			throw e;
		}
		return new ModelAndView("group", model);
	}
	
	/**
	 * Recarga el listado de grupos
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "/reloadGroups", method = RequestMethod.GET)
	public ModelAndView reloadGroups(ModelMap model) throws Exception {
		try {
			setupModel(model);		
		} catch (Exception e) {
			log.error("Error al recargar los grupos: ", e);
			throw e;
		}
		return new ModelAndView("groupList", model);
	}
	
	/**
	 * @param model
	 */
	private void setupModel(ModelMap model) {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se obtienen los grupos segun su perfil
		List<AbstractBaseDTO> listGroup = groupBO.getGroups(user);
		model.addAttribute("listGroup", listGroup);
		model.addAttribute("seatList", provinceBO.getSeatList(user));
	}

	/**
	 * Crea un grupo para usuario
	 * @RequestParam(value = "code"), String con el nombre del grupo
	 * @RequestParam(value = "description") String la descripcion del grupo,
	 * @RequestParam(value = "sede") String con el identificador de la provincia 
	 *								del grupo a editar
	 * @return  @ResponseBody Array de String con los errores generados
	 */
	@RequestMapping(value = "/insertGroup", method = RequestMethod.POST)
	public 	@ResponseBody ErrorsAndWarnings insertGroup(@RequestBody InsertGroupRequest insertGroupRequest) {

		Group group = insertGroupRequest.getGroup();
		
		ErrorsAndWarnings returnValue = new ErrorsAndWarnings();
		try {
			// Se valida la vista
			groupValidator.validateGroup(group, returnValue);
			if(!returnValue.getErrors().isEmpty()
					|| (!returnValue.getWarnings().isEmpty() 
							&& !insertGroupRequest.getIgnoreWarnings()) ) {
				return returnValue;
			}

			// Se obtiene el DTO
			PfGroupsDTO groupDTO = groupConverter.envelopeToDTO(group);

			// Se valida el negocio
			groupBO.validate(groupDTO, returnValue);
			if(!returnValue.getErrors().isEmpty()
					|| (!returnValue.getWarnings().isEmpty() 
							&& !insertGroupRequest.getIgnoreWarnings()) ){
				return returnValue;
			}

			// Se persiste el grupo
			groupBO.save(groupDTO);

		} catch (Exception e) {
			log.error("Error al insertar el cargo: ", e);
			returnValue.getErrors().add(Constants.MSG_GENERIC_ERROR);
		}
		
		return returnValue;
	}
	
	/**
	 * Borra un grupo del usuario
	 * @RequestParam(value = "id"), String con el identificador de grupo
	 * @return  @ResponseBody Array de String con los errores generados
	 */
	@RequestMapping(value = "/deleteGroup")
	public 	@ResponseBody ArrayList<String> deleteGroup(
			@RequestParam(value = "id") final String id) {

		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se obtiene el DTO del grupo
			PfGroupsDTO group = groupBO.findGroupById(Long.parseLong(id));

			// Se validan las reglas de negocio para el borrado
			groupBO.validateDelete(group, errors);
			if(!errors.isEmpty()){
				return errors;
			}
			
			// Si no hubo errores, se borra el grupo
			groupBO.delete(group);

		} catch (Exception e) {
			log.error("Error al insertar el cargo: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		return errors;
	}

}
