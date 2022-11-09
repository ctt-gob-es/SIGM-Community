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

package es.seap.minhap.portafirmas.web.validation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.User;

@Component
public class UserValidator {
	@Autowired
	private ValidatorUsersConfBO validatorUsersConfBO;

	@Resource(name = "messageProperties")
	private Properties messages;

	public void validate(User user, ArrayList<String> errors) {
		if(Util.esVacioONulo(user.getNif())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("nif")));
		} else if(Util.vacioSiNulo(user.getNif()).length() > 30) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("nif"), 30));
		}
		if(Util.esVacioONulo(user.getName())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("name")));
		} else if(Util.vacioSiNulo(user.getName()).length() > 50) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("name"), 50));
		}
		if(Util.esVacioONulo(user.getLastName1())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("lastName1")));
		} else if(Util.vacioSiNulo(user.getLastName1()).length() > 50) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("lastName1"), 50));
		}
		if(Util.vacioSiNulo(user.getLastName2()).length() > 50) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("lastName2"), 50));
		}
		if(Util.vacioSiNulo(user.getPassword()).length() > 1000) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("password"), 1000));
		}
		if(Util.vacioSiNulo(user.getLdapId()).length() > 1000) {
			errors.add(String.format(messages.getProperty("field.length.exceded"), messages.getProperty("ldapId"), 1000));
		}
		if(Util.esVacioONulo(user.getProvince())) {
			errors.add(String.format(messages.getProperty("field.required"), messages.getProperty("province")));
		}
	}
	
	public boolean necesitaPantallaIntermediaDeLogin (PfUsersDTO user) {
		boolean necesitaIntermedia = false;
		if (hayValidadores(user) || hayGrupos(user) || hayValidadoresPorAplicacion(user) || hayUsuariosGestionados(user)) {
			necesitaIntermedia = true;
		}
		return necesitaIntermedia;
	}
	
	private boolean hayValidadores(PfUsersDTO authUser) {
		boolean existen = false;
		if (authUser.getValidadorDe() != null) {
			for (Iterator<PfUsersDTO> it = authUser.getValidadorDe().iterator(); it.hasNext();) {
				PfUsersDTO validador = (PfUsersDTO) it.next();
				if (validador.getLvalid()) {
					existen = true;
				}
			}
		}
		return existen;
	}
	
	private boolean hayValidadoresPorAplicacion(PfUsersDTO authUser) {
		boolean existen = false;
		List<PfUsersDTO> validatedByAppUsersList = validatorUsersConfBO.queryValidatorsByValidatorListTest(authUser);
		if (validatedByAppUsersList.size()>0) {
			existen = true;
		}
		return existen;
	}

	private boolean hayGrupos(PfUsersDTO authUser) {
		return authUser.getPfUsersGroups() != null && !authUser.getPfUsersGroups().isEmpty();
	}
	
	private boolean hayUsuariosGestionados(PfUsersDTO authUser) {
		boolean existen = false;
		List<AbstractBaseDTO> gestionatedUsersList = getUserList(authUser.getGestorDe());
		if (gestionatedUsersList.size()>0) {
			existen = true;
		}
		return existen;
	}
	
	private List<AbstractBaseDTO> getUserList(Set<PfUsersDTO> users){
		List<AbstractBaseDTO> userList = new ArrayList<AbstractBaseDTO>();
		if (users != null && !users.isEmpty()){
			Iterator<PfUsersDTO> usersIt = users.iterator();
			while(usersIt.hasNext()){
				userList.add(usersIt.next());
			}
		}
		return userList;
	}

}
