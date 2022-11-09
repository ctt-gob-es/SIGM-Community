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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProfilesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.User;

@Component
public class UserConverter {

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private ApplicationBO applicationBO;
	
	/**
	 * Crea el objeto de transferencia de datos de usuario a partir del objeto de la vista
	 * 
	 * @param user
	 * @return
	 */
	public PfUsersDTO envelopeToDTO(User user) {
		PfUsersDTO pfUsersDTO = null;
		if(Util.esVacioONulo(user.getPrimaryKey())) {
			pfUsersDTO = new PfUsersDTO();
		} else {
			pfUsersDTO = userAdmBO.queryUsersByPk(Long.parseLong(user.getPrimaryKey()));
		}
		pfUsersDTO.setCidentifier(user.getNif());
		pfUsersDTO.setDname(user.getName());
		pfUsersDTO.setDsurname1(user.getLastName1());
		pfUsersDTO.setDsurname2(user.getLastName2());
		pfUsersDTO.setLvalid(Constants.C_YES.equals(user.getValid()));
		pfUsersDTO.setLvisible(Constants.C_YES.equals(user.getPublico()));
		pfUsersDTO.setPfProvince(provinceBO.getProvinceByCod(user.getProvince()));
		pfUsersDTO.setCtype("USUARIO");
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
		User user = new User();
		user.setPrimaryKey(pfUsersDTO.getPrimaryKeyString());
		user.setNif(pfUsersDTO.getCidentifier());
		user.setName(pfUsersDTO.getDname());
		user.setLastName1(pfUsersDTO.getDsurname1());
		user.setLastName2(pfUsersDTO.getDsurname2());
		if(pfUsersDTO.getLvalid()) {
			user.setValid(Constants.C_YES);
		}
		if(pfUsersDTO.getLvisible()) {
			user.setPublico(Constants.C_YES);
		}
		if(pfUsersDTO.getPfProvince() != null) {
			user.setProvince(pfUsersDTO.getPfProvince().getCcodigoprovincia());
			user.setProvinceName(pfUsersDTO.getPfProvince().getCnombre());
		}
		user.setPassword(getUserParameter(pfUsersDTO, Constants.USUARIO_PASSWORD));
		user.setLdapId(getUserParameter(pfUsersDTO, Constants.USUARIO_LDAP_IDATRIBUTO));
		user.setProfiles(getProfiles(pfUsersDTO));
		
		if(pfUsersDTO.getPfUnidadOrganizacionalList() != null) {
			for(PfUnidadOrganizacionalDTO admin: pfUsersDTO.getPfUnidadOrganizacionalList()){
				user.setAdminOrg(admin.getCodigo()+" - "+admin.getDenominacion());
			}
		}
		if(pfUsersDTO.getCtype() != null) {
			user.setTipoUsuario(pfUsersDTO.getCtype());
		}
		return user;
	}

	private String[] getProfiles(PfUsersDTO pfUsersDTO) {
		int i = 0;
		Set<PfUsersProfileDTO> setUsersProfilesDTO = pfUsersDTO.getPfUsersProfiles();
		String[] profiles = new String[setUsersProfilesDTO.size()];
		for (Iterator<PfUsersProfileDTO> iterator = setUsersProfilesDTO.iterator(); iterator.hasNext();) {
			PfUsersProfileDTO pfUsersProfileDTO = (PfUsersProfileDTO) iterator.next();
			profiles[i++] = pfUsersProfileDTO.getPfProfile().getPrimaryKeyString();
		}
		return profiles;
	}

	public String getUserParameter(PfUsersDTO pfUsersDTO, String parameterCode) {
		String userParameterValue = null;
		Set<PfUsersParameterDTO> parameters = pfUsersDTO.getPfUsersParameters();
		for (Iterator<PfUsersParameterDTO> iterator = parameters.iterator(); iterator.hasNext();) {
			PfUsersParameterDTO pfUsersParameterDTO = (PfUsersParameterDTO) iterator.next();
			if(pfUsersParameterDTO.getPfParameter().getCparameter().equals(parameterCode)) {
				userParameterValue = pfUsersParameterDTO.getTvalue();
			}
		}
		return userParameterValue;
	}

	/**
	 * Recupera la lista de relaciones usuario-parametro que se van a guardar
	 * @param user
	 * @return
	 */
	public List<PfUsersParameterDTO> getUserParamenterList(User user) {
		List<PfUsersParameterDTO> userParameterList = new ArrayList<PfUsersParameterDTO>();
		// parametro para la password
		PfUsersParameterDTO userParameterPassword = new PfUsersParameterDTO();
		userParameterPassword.setTvalue(user.getPassword());
		userParameterPassword.setPfParameter(applicationBO.queryPasswordParameter());
		userParameterList.add(userParameterPassword);
		// parametro para el identificador ldap
		PfUsersParameterDTO userParameterLdapId = new PfUsersParameterDTO();
		userParameterLdapId.setTvalue(user.getLdapId());
		userParameterLdapId.setPfParameter(applicationBO.queryLdapIdParameter());
		userParameterList.add(userParameterLdapId);
		
		return userParameterList;
	}

	/**
	 * Recupera la lista de relaciones usuario-perfil
	 * @param user
	 * @return
	 */
	public List<PfUsersProfileDTO> getUserProfileList(User user) {
		// catálogo de perfiles
		List<AbstractBaseDTO> profilesList = applicationBO.queryProfileList();
		String[] profilesId = user.getProfiles();
		Date now = new Date();
		ArrayList<PfUsersProfileDTO> pfUsersProfiles = new ArrayList<PfUsersProfileDTO>();
		for (int i = 0; i < profilesId.length; i++) {
			PfUsersProfileDTO pfUsersProfile = new PfUsersProfileDTO();
			pfUsersProfile.setFstart(now);
			pfUsersProfile.setPfProfile((PfProfilesDTO) getItem(profilesId[i], profilesList));
			pfUsersProfiles.add(pfUsersProfile);
		}
		return pfUsersProfiles;
	}

	/**
	 * Recupera la lista de relaciones administradores-sedes
	 * @param user
	 * @return
	 */
	public List<PfProvinceAdminDTO> getProvinceAdminList(User user) {
		// catálogo de sedes
		List<AbstractBaseDTO> seatList = provinceBO.getAllProvinces();
		ArrayList<PfProvinceAdminDTO> adminProvinceList = new ArrayList<PfProvinceAdminDTO>();
		String[] adminProvinceId = user.getAdminSeats();
		for (int i = 0; i < adminProvinceId.length; i++) {
			PfProvinceAdminDTO pfProvinceAdminDTO = new PfProvinceAdminDTO();
			pfProvinceAdminDTO.setPfProvince((PfProvinceDTO) getItem(adminProvinceId[i], seatList));
			adminProvinceList.add(pfProvinceAdminDTO);
		}

		return adminProvinceList;
	}

	/**
	 * Recupera un objeto a partir del catálogo y una clave primaria dados
	 * @param primaryKey
	 * @param profilesList
	 * @return
	 */
	private AbstractBaseDTO getItem(String primaryKey, List<AbstractBaseDTO> profilesList) {
		for (Iterator<AbstractBaseDTO> iterator = profilesList.iterator(); iterator.hasNext();) {
			AbstractBaseDTO abstractBaseDTO = iterator.next();
			if(abstractBaseDTO.getPrimaryKey().equals(Long.parseLong(primaryKey))) {
				return abstractBaseDTO;
			}
		}
		return null;
	}

}
