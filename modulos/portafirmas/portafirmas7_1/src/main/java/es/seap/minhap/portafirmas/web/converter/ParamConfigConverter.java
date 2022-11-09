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

package es.seap.minhap.portafirmas.web.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.web.beans.ParameterConfig;
import es.seap.minhap.portafirmas.web.beans.ParameterConfigList;

@Component
public class ParamConfigConverter {

	
	@Autowired
	private ApplicationBO applicationBO;

	/**
	 * Crea un objeto para guardar en BB.DD a partir de los datos que llegaron de la vista
	 * @param parameterConfigArray
	 * @param string 
	 * @return
	 */
	public ArrayList<PfConfigurationsParameterDTO> envelopeToDTO(ArrayList<ParameterConfig> parameterConfigArray, PfConfigurationsDTO configurationDTO) {
		ArrayList<PfConfigurationsParameterDTO> pfConfigParamDTOArray = new ArrayList<PfConfigurationsParameterDTO>();
		// Catálogo de parámetros de servidor
		List<AbstractBaseDTO> parameterList = applicationBO.queryServerParameterList();
		for (ParameterConfig parameterConfig : parameterConfigArray) {
			// Si es un alta
			PfConfigurationsParameterDTO configParamDTO = new PfConfigurationsParameterDTO();
			configParamDTO.setPfConfiguration(configurationDTO);
			configParamDTO.setPfParameter(getParameter(parameterList, parameterConfig.getId()));
			configParamDTO.setTvalue(parameterConfig.getValue());
			pfConfigParamDTOArray.add(configParamDTO);
		}
		return pfConfigParamDTOArray;
	}

	/**
	 * Obtiene la referencia del parámetro de la lista que coincide con la clave primaria
	 * @param parameterList
	 * @param parameterPk
	 * @return
	 */
	private PfParametersDTO getParameter(List<AbstractBaseDTO> parameterList, String parameterPk) {
		for (Iterator<AbstractBaseDTO> it = parameterList.iterator(); it.hasNext();) {
			PfParametersDTO parameterDTO = (PfParametersDTO) it.next();
			if(parameterDTO.getPrimaryKeyString().equals(parameterPk)) {
				return parameterDTO;
			}
		}
		return null;
	}

	/**
	 * Crea una lista de objetos a partir de los parametros recibidos
	 * @param parameterConfigList
	 * @return
	 */
	public ArrayList<ParameterConfig> getParametersConfig(ParameterConfigList parameterConfigList) {
		ArrayList<ParameterConfig> parameterConfigArray = new ArrayList<ParameterConfig>();
		List<String> arrayId = Arrays.asList(parameterConfigList.getArrayId());
		List<String> arrayName = Arrays.asList(parameterConfigList.getArrayName());
		List<String> arrayValue = Arrays.asList(parameterConfigList.getArrayValue());
		if(parameterConfigList.getArrayChecked() != null) {
			for (String id : parameterConfigList.getArrayChecked()) {
				int index = arrayId.indexOf(id);
				if(index != -1) {
					ParameterConfig parameterConfig = new ParameterConfig();
					parameterConfig.setId(arrayId.get(index));
					parameterConfig.setName(arrayName.get(index));
					parameterConfig.setValue(arrayValue.get(index));
					parameterConfigArray.add(parameterConfig);
				}
			}
		}
		return parameterConfigArray;
	}

}
