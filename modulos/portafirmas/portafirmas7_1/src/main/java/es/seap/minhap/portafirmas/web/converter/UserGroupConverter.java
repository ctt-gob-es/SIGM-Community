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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.GroupBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.PfUsersGroupsDTO;
import es.seap.minhap.portafirmas.web.beans.UserGroup;

@Component
public class UserGroupConverter {

	@Autowired
	private GroupBO groupBO;

	@Autowired
	private UserAdmBO userAdmBO;

	/**
	 * Crea un objeto para guardar en BB.DD a partir de los datos que llegaron de la vista
	 * @param userJob
	 * @return
	 */
	public PfUsersGroupsDTO envelopeToDTO(UserGroup userGroup) {
		PfUsersGroupsDTO pfUsersGroupsDTO = null;
		pfUsersGroupsDTO = new PfUsersGroupsDTO();
		pfUsersGroupsDTO.setPfGroup(groupBO.findGroupById(Long.parseLong(userGroup.getGroupId())));
		pfUsersGroupsDTO.setPfUser(userAdmBO.queryUsersByPk(Long.parseLong(userGroup.getUserId())));
		return pfUsersGroupsDTO;
	}

}
