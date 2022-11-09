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

import es.seap.minhap.portafirmas.business.administration.ServerConfigBO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.ParameterConfig;
import es.seap.minhap.portafirmas.web.beans.ParameterConfigList;
import es.seap.minhap.portafirmas.web.converter.ParamConfigConverter;
import es.seap.minhap.portafirmas.web.validation.ParamConfigValidator;


@Controller
@RequestMapping("administration/parameterConfig")
public class ParameterConfigController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ServerConfigBO serverConfigBO;
	
	@Autowired
	private ParamConfigValidator paramConfigValidator;
	
	@Autowired
	private ParamConfigConverter paramConfigConverter;
	
	/**
	 * Carga del listado de de parametros de una configuración
	 * @return
	 */
	@RequestMapping(value = "load", method = RequestMethod.POST)
	public String loadServerConfig(@ModelAttribute("configurationPk") String configurationPk, ModelMap model) {
		// catálogo de parametros
		List<ParameterConfig> parameters = serverConfigBO.getParameterConfigList(configurationPk);
		model.addAttribute("parameterList", parameters);
		model.addAttribute("configurationPk", configurationPk);

		return "parameterConfig";
	}	
	
	/**
	 * Actualiza los parametros de una configuración
	 * @param serverConfig
	 * @return
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public @ResponseBody ArrayList<String> saveServerConfig(@ModelAttribute ParameterConfigList parameterConfigList) {
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			ArrayList<ParameterConfig> parameterConfigArray = paramConfigConverter.getParametersConfig(parameterConfigList);
			
			// Se valida la vista
			paramConfigValidator.validate(parameterConfigArray, warnings);
			if(!warnings.isEmpty()) {
				return warnings;
			}
			// Se obtiene el DTO de la configuración
			PfConfigurationsDTO pfConfigurationDTO = 
					(PfConfigurationsDTO) serverConfigBO.queryConfigurationByPk(Long.parseLong(parameterConfigList.getConfigurationPk()));
			// Se crea la configuración DTO a persistir
			ArrayList<PfConfigurationsParameterDTO> pfConfigParamDTOArray =
				paramConfigConverter.envelopeToDTO(parameterConfigArray, pfConfigurationDTO);
			
			// Se realiza la persistencia de la configuración
			serverConfigBO.saveParamConfig(pfConfigParamDTOArray, pfConfigurationDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar el parámetro de configuración: ", e);
			warnings.add(Constants.MSG_GENERIC_ERROR);
		}
		return warnings;
	}	
	
}