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

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class PersonalDataBO {


	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Obtiene el usuario de la base de datos con los perfiles 
	 * de usuario cargados
	 * @param userDTO usuario
	 * @return el usuario con los perfiles de usuario cargados
	 */
	public AbstractBaseDTO query(PfUsersDTO userDTO) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("key", userDTO.getPrimaryKey());
		return baseDAO.queryElementMoreParameters("configuration.dataUser",
				params);
	}
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
	 * Hace una copia del usuario que pasamos como par&aacute;metro
	 * @param userDTO el usuario
	 * @return la copia del usuario
	 */
	public PfUsersDTO getUserCopy(PfUsersDTO userDTO) {
		PfUsersDTO copy = new PfUsersDTO();
		
		copy.setCanagram(userDTO.getCanagram());
		copy.setCcreated(userDTO.getCcreated());
		copy.setCidentifier(userDTO.getCidentifier());
		copy.setCmodified(userDTO.getCmodified());
		copy.setCtype(userDTO.getCtype());
		copy.setDname(userDTO.getDname());
		copy.setDsurname1(userDTO.getDsurname1());
		copy.setDsurname2(userDTO.getDsurname2());
		copy.setFcreated(userDTO.getFcreated());
		copy.setFmodified(userDTO.getFmodified());
		copy.setLvalid(userDTO.getLvalid());
		copy.setLvisible(userDTO.getLvisible());
		copy.setLshownotifwarning(userDTO.getLshownotifwarning());
		copy.setPfComments(userDTO.getPfComments());
		copy.setPfFilters(userDTO.getPfFilters());		
		copy.setPfHistoricRequests(userDTO.getPfHistoricRequests());
		copy.setPfOthersUsers(userDTO.getPfOthersUsers());
		copy.setPfSigners(userDTO.getPfSigners());
		copy.setPfTagsUsers(userDTO.getPfTagsUsers());
		copy.setPfUsers(userDTO.getPfUsers());
		copy.setPfUsersApplications(userDTO.getPfUsersApplications());
		copy.setPfUsersAuthorizations(userDTO.getPfUsersAuthorizations());
		copy.setPfUsersEmails(userDTO.getPfUsersEmails());
		copy.setPfUsersJobs(userDTO.getPfUsersJobs());
		copy.setPfUsersMobiles(userDTO.getPfUsersMobiles());
		copy.setPfUsersParameters(userDTO.getPfUsersParameters());
		copy.setPfUsersProfiles(userDTO.getPfUsersProfiles());
		copy.setPfUsersRemitters(userDTO.getPfUsersRemitters());
		copy.setPfProvince(userDTO.getPfProvince());
		copy.setPfUsersProvinces(userDTO.getPfUsersProvinces());		
		copy.setPfSessionAttributes(userDTO.getPfSessionAttributes());
		copy.setPfUsersGroups(userDTO.getPfUsersGroups());		
		copy.setPrimaryKey(userDTO.getPrimaryKey());
		copy.setPrimaryKeyString(userDTO.getPrimaryKeyString());
		copy.setSelected(userDTO.isSelected());
		copy.setUpdated(userDTO.isUpdated());
		
		return copy;
	}
}
