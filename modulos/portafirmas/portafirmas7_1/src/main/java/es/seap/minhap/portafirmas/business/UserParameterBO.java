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

package es.seap.minhap.portafirmas.business;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;


@Service
public class UserParameterBO {
	Logger log = Logger.getLogger(UserParameterBO.class);
	
	
	@Autowired
	private BaseDAO baseDAO;
	
	public boolean rescueParamUser(long id){
		log.debug("** Recuperamos el parametro para un id = " + id);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("value", Constants.C_PARAMETER_USUARIO_NO_VALIDAR);
		PfUsersParameterDTO resultado = (PfUsersParameterDTO) baseDAO.queryElementMoreParameters("configuration.userParamValidate", parameters);
		if((resultado== null)||(resultado.getTvalue().equalsIgnoreCase(Constants.C_NOT))){
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * Metodo que guarda en BD el valor de un parametro de usuario.
	 * @param paramName Nombre del parametro a guardar.
	 * @param paramValue Valor del parametro a guardar.
	 * @param user Usuario que guarda su parametro.
	 */
	public void saveParamUser(String paramName, String paramValue, PfUsersDTO user) {
		// Se obtiene el parametro
		PfParametersDTO paramTheme = 
			(PfParametersDTO) baseDAO.queryElementOneParameter("configuration.parameter", "cparameter", paramName);

		// Se obtiene la relacion
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("pfUser", user);
		filter.put("pfParameter", paramTheme);
		PfUsersParameterDTO userParameter = (PfUsersParameterDTO) baseDAO.queryElementMoreParameters("configuration.userParam", filter);

		// Si el parámetro tiene un valor, se salva..
		if (!Util.esVacioONulo(paramValue)) {
			// Si no existía la relación, se crea
			if (userParameter == null) {
				userParameter = new PfUsersParameterDTO();
				userParameter.setPfParameter(paramTheme);
				userParameter.setPfUser(user);
			}
			userParameter.setTvalue(paramValue);
			baseDAO.insertOrUpdate(userParameter);
		} else {
			// .. si no lleva parámetro, se borra la relación
			if (userParameter != null) {
				baseDAO.delete(userParameter);
			}
		}
	}

}
