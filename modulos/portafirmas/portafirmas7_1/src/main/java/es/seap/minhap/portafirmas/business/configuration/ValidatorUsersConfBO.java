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

package es.seap.minhap.portafirmas.business.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfValidatorApplicationDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ValidatorUsersConfBO {

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private TagBO tagBO;

	/**
	 * Actualiza el usuario en base de datos
	 * @param user el usuario a guardar
	 */
	@Transactional(readOnly = false)
	public void saveUser(AbstractBaseDTO user) {
		user.setUpdated(true);
		baseDAO.update(user);
	}
	
	/**
	 * Guarda una lista de validadores por aplicaciones
	 * @param user el usuario a guardar
	 */
	@Transactional(readOnly = false)
	public void saveValidatorByApplicationList (List <PfValidatorApplicationDTO> validatorList) {
		baseDAO.insertOrUpdateList(new ArrayList<AbstractBaseDTO>(validatorList));
	}
	
	/**Guarda un validador por aplicacion
	 * @param validator
	 */
	@Transactional(readOnly = false)
	public void saveValidatorByApplication (PfValidatorApplicationDTO validator) {
		baseDAO.update(validator);
	}	

	/**Obtiene una lista de todas las aplicaciones de un usuario y un validador de la tabla PF_VALIDADOR_APLICACION
	 * @param user
	 * @param validator
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<PfValidatorApplicationDTO> queryValidatorsAppByPkAndValidatorList (PfUsersDTO user, PfUsersDTO validator) {
		
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("user", user.getPrimaryKey());
		queryParams.put("validator", validator.getPrimaryKey());
		
		return baseDAO.queryListMoreParameters("configuration.validatorAppPkAndValidatorList", queryParams);
	}
	
	/**Obtiene una lista de todos los usuarios que tiene un validador.
	 * @param user
	 * @param validator
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<PfUsersDTO> queryValidatorsByValidatorListTest (PfUsersDTO validator) {
		
		return baseDAO.queryListOneParameter("configuration.validatorAppPkDistinctUserList", "validator", validator.getPrimaryKey());
	}
	
	/**Obtiene una lista de aplicaciones de un usuario por validador
	 * @param validator
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<PfApplicationsDTO> queryValidatorsAppByValidatorList (PfUsersDTO validator, PfUsersDTO user) {
		
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("validator", validator.getPrimaryKey());
		queryParams.put("user", user.getPrimaryKey());
		
		return baseDAO.queryListMoreParameters("configuration.validatorAppByValidatorList", queryParams);
	}
	
	/**Elimina una lista de validadores de la tabla PF_VALIDADOR_APLICACION
	 * @param validators
	 */
	@Transactional(readOnly = false)
	public void deleteValidatorsAppList (List<PfValidatorApplicationDTO> validators) {		
		baseDAO.deleteList(new ArrayList<AbstractBaseDTO>(validators));
	}
	
	/**Elimina un validador de la tabla PF_VALIDADOR_APLICACION
	 * @param validator
	 */
	@Transactional(readOnly = false)
	public void deleteValidatorApp (PfValidatorApplicationDTO validator) {		
		baseDAO.delete(validator);
	}
	
	/**Obtiene un validador y una aplicacion de la tabla PF_VALIDADOR_APLICACION
	 * @param user
	 * @param validator
	 * @param application
	 * @return
	 */
	@Transactional(readOnly = false)
	public PfValidatorApplicationDTO queryValidatorsAppByValidator (PfUsersDTO user, PfUsersDTO validator, PfApplicationsDTO application) {
		
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("user", user.getPrimaryKey());
		queryParams.put("validator", validator.getPrimaryKey());
		queryParams.put("application", application.getPrimaryKey());
		
		return (PfValidatorApplicationDTO) baseDAO.queryElementMoreParameters("configuration.validatorAppPkAndValidator", queryParams);
	}
	
	
	/**Obtiene una lista de todos los usuarios que tienen un validador.
	 * @param user
	 * @param validator
	 * @return
	 */
	@Transactional(readOnly = false)
	public List<PfValidatorApplicationDTO> queryValidatorsByValidatorList (PfUsersDTO user) {
		
		return baseDAO.queryListOneParameter("configuration.validatorAppPkList", "pk", user.getPrimaryKey());
	}
}
