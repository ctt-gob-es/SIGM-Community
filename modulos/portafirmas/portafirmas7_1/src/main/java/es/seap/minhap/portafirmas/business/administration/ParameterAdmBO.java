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

package es.seap.minhap.portafirmas.business.administration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ParameterAdmBO{

	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Obtiene los par&aacute;metros de configuraci&oacute;n de tipo administraci&oacute;n, par&aacute;metros de tipo
	 * distinto a 'ESTILO' y con c&oacute;digo de par&aacute;metro distinto de 'PREFIRMA%'.
	 * @return los par&aacute;metros de configuraci&oacute;n de administraci&oacute;n
	 */
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.parameterAdmAll", null);
	}

	/**
	 * M&eacute;todo que actualiza una lista de par&aacute;metros de configuraci&oacute;n en BD.
	 * @param parameterList Listado de par&aacute;metros de configuraci&oacute;n.
	 */
	@Transactional(readOnly = false)
	public void saveParameterList(List<AbstractBaseDTO> parameterList) {
		baseDAO.updateList(parameterList);
	}

	/**
	 * M&ntilde;etodo que duplica la configuraci&oacute;n de un par&aacute;metro.
	 * @param parameter parametro a duplicar.
	 * @return Par&aacute;metro copiado.
	 */
	public PfConfigurationsParameterDTO getParameterConfigurationCopy(PfConfigurationsParameterDTO parameter) {
		PfConfigurationsParameterDTO copy = new PfConfigurationsParameterDTO();
		copy.setCcreated(parameter.getCcreated());
		copy.setCmodified(parameter.getCmodified());
		copy.setFcreated(parameter.getFcreated());
		copy.setFmodified(parameter.getFmodified());
		copy.setPfConfiguration(parameter.getPfConfiguration());
		copy.setPfParameter(parameter.getPfParameter());
		copy.setPrimaryKey(parameter.getPrimaryKey());
		copy.setTvalue(parameter.getTvalue());
		return copy;
	}

	/**
	 * M&eacute;todo que comprueba que el valor de un par&aacute;metro de configuraci&oacute;n sea correcto.
	 * @param configurationsParameterDTO Par&aacute;metro a comprobar.
	 * @param userVO Usuario que configura el par&aacute;metro.
	 * @return "True" si el par&aacute;metro es correcto, "false" en caso contrario.
	 */
	//TODO: Adaptar a Spring MVC
	/*public boolean checkParameter(PfConfigurationsParameterDTO configurationsParameterDTO, UserVO userVO) {
		boolean result = true;
		if (configurationsParameterDTO.getTvalue() == null || configurationsParameterDTO.getTvalue().equals("")) {
//			FacesMessage facesMessage = FacesMessages.createFacesMessage(
//					FacesMessage.SEVERITY_ERROR, "errorParameterValueNull", "", configurationsParameterDTO);
//			userVO.addMessage("", facesMessage);
			result = false;
		}
		if (configurationsParameterDTO.getTvalue() != null && configurationsParameterDTO.getTvalue().length() > 1000) {
//			FacesMessage facesMessage = FacesMessages.createFacesMessage(
//					FacesMessage.SEVERITY_ERROR,
//					"errorParameterValueLength", "", configurationsParameterDTO);
//			userVO.addMessage("", facesMessage);
			result = false;
		}
		return result;
	}*/
}
