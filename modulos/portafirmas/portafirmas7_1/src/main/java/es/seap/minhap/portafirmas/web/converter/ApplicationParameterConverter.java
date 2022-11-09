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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.application.ApplicationParameterUtil;
import es.seap.minhap.portafirmas.web.beans.Application;
import es.seap.minhap.portafirmas.web.beans.ParameterConfig;

@Component
public class ApplicationParameterConverter {
	
	@Autowired
	private BaseDAO baseDAO;
		
	@Autowired
	UtilComponent util;
	
	@Transactional(readOnly=false)
	public List<PfApplicationsParameterDTO> envelopeToDTO(Application application) {
		
		List<AbstractBaseDTO> parametersList = baseDAO.queryListMoreParameters("administration.applicationParametersPk", null);
		
		List<PfApplicationsParameterDTO> pfApplParamDTO = new ArrayList<>();
		
		for(int i=0; i<parametersList.size()-1; i++){
			
			PfApplicationsParameterDTO apDTO = new PfApplicationsParameterDTO();
						 
			PfParametersDTO pfParametersDTO = (PfParametersDTO) parametersList.get(i);
			pfParametersDTO.getPrimaryKey();
			String cpar = pfParametersDTO.getCparameter();
			
			if(cpar.equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_USUARIO)){
				apDTO.setTvalue(application.getWsUser());
			}
			else if(cpar.equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_PASSWORD)){
				apDTO.setTvalue(application.getWsPassword());
			}
			else if(cpar.equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_ACTIVA)){
				apDTO.setTvalue(application.getWsActiva());
			}
			else if(cpar.equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_WSDLLOCATION)){
				apDTO.setTvalue(application.getWsLocation());
			}
			else if(cpar.equalsIgnoreCase(Constants.APP_PARAMETER_RESPUESTA_WS_NOTIFINTERMEDIOS)){
				apDTO.setTvalue(application.getWsNotifIntermedios());
			}
			if(!Util.esVacioONulo(apDTO.getTvalue())) {
				apDTO.setPfParameter(pfParametersDTO);
				pfApplParamDTO.add(apDTO);
			}
			
		}	
		return pfApplParamDTO;
	}
	
	public List<PfApplicationsParameterDTO> parametersInformeToDTO(List<ParameterConfig> parameters) {
		List<PfApplicationsParameterDTO> retorno = new ArrayList<>();
		
		if (util.isNotEmpty(parameters)) {
			for (ParameterConfig parameter : parameters) {
				if (!Util.esVacioONulo(parameter.getValue())
					&& ApplicationParameterUtil.PARAMETER_INFORME.exists(parameter.getName())) {
					PfApplicationsParameterDTO apDTO = new PfApplicationsParameterDTO();
					
					PfParametersDTO pfParametersDTO = new PfParametersDTO();
					pfParametersDTO.setPrimaryKey(Long.valueOf(parameter.getId()));
					apDTO.setPfParameter(pfParametersDTO);
					
					apDTO.setTvalue(parameter.getValue());
					
					retorno.add(apDTO);
				}
			}
		}
		
		return retorno;
	}
}