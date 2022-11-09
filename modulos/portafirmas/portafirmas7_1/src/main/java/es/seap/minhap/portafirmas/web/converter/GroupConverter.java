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
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Group;

@Component
public class GroupConverter {

	@Autowired
	private GroupBO groupBO;
	
	@Autowired
	private ProvinceBO provinceBO;

	/**
	 * Crea el objeto de transferencia de datos de usuario a partir del objeto de la vista
	 * 
	 * @param job
	 * @return
	 */
	public PfGroupsDTO envelopeToDTO(Group group) {
		PfGroupsDTO groupDTO = null;
		if(Util.esVacioONulo(group.getId())) {
			groupDTO = new PfGroupsDTO();
		} else {
			groupDTO = groupBO.findGroupById(Long.parseLong(group.getId()));
		}
		groupDTO.setCdescripcion(group.getDescription());
		groupDTO.setCnombre(group.getCode());
		groupDTO.setPfProvince(provinceBO.getProvinceByCod(group.getSeat()));
		return groupDTO;
	}

}
