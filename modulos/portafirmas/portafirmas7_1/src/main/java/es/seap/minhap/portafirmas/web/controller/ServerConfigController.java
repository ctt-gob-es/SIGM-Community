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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.administration.ServerConfigBO;
import es.seap.minhap.portafirmas.business.administration.ServerAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfServersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.ServerConfig;
import es.seap.minhap.portafirmas.web.converter.ServerConfigConverter;
import es.seap.minhap.portafirmas.web.validation.ServerConfigValidator;


@Controller
@RequestMapping("administration/serverConfig")
public class ServerConfigController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ServerAdmBO serverAdmBO;
	
	@Autowired
	private ServerConfigBO serverConfigBO;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private ServerConfigValidator serverConfigValidator;
	
	@Autowired
	private ServerConfigConverter serverConfigConverter;
	
	/**
	 * Carga del listado de sedes
	 * @return
	 */
	@RequestMapping(value = "load", method = RequestMethod.POST)
	public String loadServerConfig(@ModelAttribute("searchServerPk") String search, ModelMap model) {
		List<AbstractBaseDTO> serverConfigList = null;
		if(Util.esVacioONulo(search)) {
			PfServersDTO mainServer = (PfServersDTO) serverAdmBO.mainServerQuery();
			search = mainServer.getPrimaryKeyString();
		}
		// lista de configuraciones para un servidor
		serverConfigList = serverConfigBO.queryConfigurationsList(Util.vacioSiNulo(search));
		// catálogo de servidores
		List<AbstractBaseDTO> servers = serverAdmBO.queryList();
		// catálogo de parametros
		List<AbstractBaseDTO> parameters = applicationBO.queryServerParameterList();
				
		model.addAttribute("serverConfigList", serverConfigList);		
		model.addAttribute("serverList", servers);
		model.addAttribute("searchServerPk", search);
		model.addAttribute("parameterList", parameters);

		return "serverConfig";
	}	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> saveServerConfig(@ModelAttribute ServerConfig serverConfig) {
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			// Se valida la vista
			serverConfigValidator.validate(serverConfig, warnings);
			if(!warnings.isEmpty()) {
				return warnings;
			}
			
			// Se crea la configuración DTO a persistir
			PfConfigurationsDTO pfConfigurationsDTO = serverConfigConverter.envelopeToDTO(serverConfig);

			// Se validan las reglas de negocio
			serverConfigBO.validateServerConfig(pfConfigurationsDTO, warnings);
			if(!warnings.isEmpty()) {
				return warnings;
			}
			
			// Se realiza la persistencia de la configuración
			serverConfigBO.saveConfigurationServer(pfConfigurationsDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar el cargo del usuario: ", e);
			warnings.add(Constants.MSG_GENERIC_ERROR);
		}
		return warnings;
	}	
	
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> deleteServerConfig(@ModelAttribute ServerConfig serverConfig) {
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			PfConfigurationsDTO pfConfigurationsDTO = 
					(PfConfigurationsDTO) serverConfigBO.queryConfigurationByPk(Long.parseLong(serverConfig.getConfigurationPk()));
			serverConfigBO.validateDeleteServerConfig(pfConfigurationsDTO, warnings);
			if(!warnings.isEmpty()) {
				return warnings;
			}
			serverConfigBO.deleteConfigurationServer(pfConfigurationsDTO);
		} catch (Exception e) {
			log.error("Error al borrar el cargo del usuario: ", e);
			warnings.add(Constants.MSG_GENERIC_ERROR);
		}
		return warnings;
	}
	
}