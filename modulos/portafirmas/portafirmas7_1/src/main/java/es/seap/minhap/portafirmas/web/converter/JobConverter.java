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

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.User;

@Component
public class JobConverter {

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private ProvinceBO provinceBO;

	/**
	 * Crea el objeto de transferencia de datos de usuario a partir del objeto de la vista
	 * 
	 * @param job
	 * @return
	 */
	public PfUsersDTO envelopeToDTO(User job) {
		PfUsersDTO pfUsersDTO = null;
		if(Util.esVacioONulo(job.getPrimaryKey())) {
			pfUsersDTO = new PfUsersDTO();
		} else {
			pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(job.getPrimaryKey()));
		}
		pfUsersDTO.setCidentifier(job.getNif());
		pfUsersDTO.setDname(job.getName());
		pfUsersDTO.setLvalid(Constants.C_YES.equals(job.getValid()));
		pfUsersDTO.setLvisible(Constants.C_YES.equals(job.getPublico()));
		pfUsersDTO.setPfProvince(provinceBO.getProvinceByCod(job.getProvince()));
		pfUsersDTO.setCtype(Constants.C_TYPE_USER_JOB);
		pfUsersDTO.setLshownotifwarning(false);
		
		return pfUsersDTO;
	}

	/**
	 * crea el objeto de la vista a partir del objeto de transferencia de datos de usuario
	 * 
	 * @param pfUsersDTO
	 * @return
	 */
	public User DTOtoEnvelope(PfUsersDTO pfUsersDTO) {
		User job = new User();
		job.setPrimaryKey(pfUsersDTO.getPrimaryKeyString());
		job.setNif(pfUsersDTO.getCidentifier());
		job.setName(pfUsersDTO.getDname());
		if(pfUsersDTO.getLvalid()) {
			job.setValid(Constants.C_YES);
		}
		if(pfUsersDTO.getLvisible()) {
			job.setPublico(Constants.C_YES);
		}
		if(pfUsersDTO.getPfProvince() != null) {
			job.setProvince(pfUsersDTO.getPfProvince().getCcodigoprovincia());
		}		
		return job;
	}

}
