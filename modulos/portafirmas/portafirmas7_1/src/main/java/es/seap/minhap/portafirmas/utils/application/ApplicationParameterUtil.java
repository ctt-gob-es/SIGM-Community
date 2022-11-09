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

package es.seap.minhap.portafirmas.utils.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.web.beans.ParameterConfig;

@Component
public class ApplicationParameterUtil {
	
	@Autowired
	UtilComponent util;
	
	private static String[] PARAMETER_INFORME_BOOLEAN = { PARAMETER_INFORME.estamparLogo.name()};
	private static String[] PARAMETER_INFORME_OPCIONES_PAGINA = { PARAMETER_INFORME.porcentajeDocumento.name(), PARAMETER_INFORME.separacionX.name(), PARAMETER_INFORME.separacionY.name()};
	
	public static Boolean isBooleanParameter(String param) {
		return Arrays.asList(PARAMETER_INFORME_BOOLEAN).contains(param);
	}
	
	public static Boolean isOpcionesPaginaParameter(String param) {
		return Arrays.asList(PARAMETER_INFORME_OPCIONES_PAGINA).contains(param);
	}
	
	public static enum PARAMETER_INFORME {
		idAplicacion("EEUTIL.PARAM.IDAPLICACION"),
		csv("EEUTIL.PARAM.CSV"),
		fecha("EEUTIL.PARAM.FECHA"),
		expediente("EEUTIL.PARAM.EXPEDIENTE"),
		nif("EEUTIL.PARAM.NIF"),
		urlSede("EEUTIL.PARAM.URLSEDE"),
		tituloAplicacion("EEUTIL.PARAM.TITULOAPLICACION"),
		tituloCSV("EEUTIL.PARAM.TITULOCSV"),
		tituloFecha("EEUTIL.PARAM.TITULOFECHA"),
		tituloExpediente("EEUTIL.PARAM.TITULOEXPEDIENTE"),
		tituloNif("EEUTIL.PARAM.TITULONIF"),
		tituloURL("EEUTIL.PARAM.TITULOURL"),
		estamparLogo("EEUTIL.PARAM.ESTAMPARLOGO"),
		lateral("EEUTIL.PARAM.LATERAL"),
		urlQR("EEUTIL.PARAM.URLQR"),
		porcentajeDocumento("EEUTIL.PARAM.PORCENTAJE"),
		separacionX("EEUTIL.PARAM.SEPARACIONX"),
		separacionY("EEUTIL.PARAM.SEPARACIONY");
		
		private final String value;
		
		private PARAMETER_INFORME(String value) {
	        this.value = value;
	    }

		public String getValue() {
	        return value;
	    }
		
		public static Boolean exists(String parameter) {
			for(PARAMETER_INFORME prop : values()){
			      if(prop.value.equals(parameter)){
			        return Boolean.TRUE;
			      }
			    }
			return Boolean.FALSE;
		}
		
		public static PARAMETER_INFORME getValueOf(String parameter) {
			for(PARAMETER_INFORME prop : values()){
			      if(prop.value.equals(parameter)){
			        return prop;
			      }
			    }
			return null;
		}
	};
	
	public List<ParameterConfig> getInformeParameters(List<AbstractBaseDTO> list) {
		List<ParameterConfig> retorno = new ArrayList<ParameterConfig>();
		
		if (util.isNotEmpty(list)) {
			for (AbstractBaseDTO parameter : list) {
				if (PARAMETER_INFORME.exists(((PfParametersDTO) parameter).getCparameter())) {
					ParameterConfig param = new ParameterConfig();
					param.setId(parameter.getPrimaryKey().toString());
					param.setName(((PfParametersDTO) parameter).getCparameter());
					param.setDescription(((PfParametersDTO) parameter).getDparameter());
					retorno.add(param);
				}
			}
		}
		
		return retorno;
	}

	public List<ParameterConfig> getParametersInformes(List<AbstractBaseDTO> list, PfApplicationsDTO applicationDto) {
		//obtenemos la lista de parametros posibles
		List<ParameterConfig> retorno = getInformeParameters(list);
		
		if (applicationDto != null
			&& util.isNotEmpty(applicationDto.getPfApplicationsParameters())) {
			for (PfApplicationsParameterDTO parameter : applicationDto.getPfApplicationsParameters()) {
				
				ParameterConfig paramAxu = new ParameterConfig();
				paramAxu.setName(parameter.getPfParameter().getCparameter());
				int position = retorno.indexOf(paramAxu);
				if (position != -1) {
					retorno.get(position).setValue(parameter.getTvalue());
				}
			}
		}
		return retorno;
	}
	
}
