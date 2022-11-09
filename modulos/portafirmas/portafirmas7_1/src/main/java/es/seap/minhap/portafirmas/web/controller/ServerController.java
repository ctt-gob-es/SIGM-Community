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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.administration.ServerAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfServersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Server;
import es.seap.minhap.portafirmas.web.converter.ServerConverter;
import es.seap.minhap.portafirmas.web.validation.ServerValidator;

@Controller
@RequestMapping("administration/server")
public class ServerController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ServerAdmBO serverAdmBO;
	
	@Autowired
	private ServerValidator serverValidation;
	
	@Autowired
	private ServerConverter serverConverter;
	
	@RequestMapping(value = "/loadServers", method = RequestMethod.GET)
	public ModelAndView loadServers() {
		ModelMap model = null;
		
		try {
			// Carga de los servidores
			List<AbstractBaseDTO> servers = serverAdmBO.queryList();
			model = new ModelMap();
			model.addAttribute("serverList", servers);		

		} catch (Throwable t) {
			log.error("Error al cargar servidores: ", t);
		}
		return new ModelAndView("servers", model);
	}
	
	/**
	 * Alta o modificación de una sede
	 * @param primaryKey
	 * @param serverCode
	 * @param serverDescription
	 * @param serverIsDefault
	 * @return
	 */
	@RequestMapping(value = "/saveServer")
	public @ResponseBody ArrayList<String> saveServer(
			@RequestParam(value = "primaryKey") final String primaryKey,
			@RequestParam(value = "serverCode") final String serverCode,
			@RequestParam(value = "serverDescription") final String serverDescription,
			@RequestParam(value = "serverIsDefault") final String serverIsDefault) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			// Se obtienen la información de la vista
			Server server = new Server (serverCode, serverDescription, Boolean.parseBoolean(serverIsDefault));
			if(!Util.esVacioONulo(primaryKey)) {
				server.setPrimaryKey(Long.parseLong(primaryKey));
			}
			
			// Se valida la vista
			serverValidation.validate(server, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se monta el servidor
			PfServersDTO pfServerDTO = null;
			if(server.getPrimaryKey() == null) {
				pfServerDTO = new PfServersDTO();
			} else {
				pfServerDTO = serverAdmBO.serverByPk(server.getPrimaryKey());
			}
			
			// se vuelcan los datos obtenidos de la vista
			serverConverter.envelopeToDTO(server, pfServerDTO);

			// Se validan las reglas de negocio
			serverAdmBO.validateServer(pfServerDTO, errors);
			
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza la persistencia
			serverAdmBO.saveServer(pfServerDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar el servidor: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}
	
	/**
	 * Borrado de un servidor
	 * @param primaryKey
	 * @return
	 */
	@RequestMapping(value = "/deleteServer")
	public @ResponseBody ArrayList<String> deleteServer(
			@RequestParam(value = "primaryKey") final String primaryKey) {
		ArrayList<String> errors = new ArrayList<String>();
		try {

			// Se obtiene el servidor a borrar
			PfServersDTO pfServerDTO = serverAdmBO.serverByPk(Long.parseLong(primaryKey));	
			
			
			// Se validan las reglas de negocio
			serverAdmBO.validateDeleteServer(pfServerDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}
			
			// Se realiza el borrado
			serverAdmBO.deleteServer(pfServerDTO);
			
		} catch (Exception e) {
			log.error("Error al borrar el servidor: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}
		
		return errors;
	}

	
}
