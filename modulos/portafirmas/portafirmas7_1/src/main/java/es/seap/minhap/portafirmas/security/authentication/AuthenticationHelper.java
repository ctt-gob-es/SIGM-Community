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

package es.seap.minhap.portafirmas.security.authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.security.authorities.GrantedAuthorityImpl;

@Component
public class AuthenticationHelper {
	
	protected final Log log = LogFactory.getLog(getClass());
	/**
	 * Método que obtiene los roles del usuario autenticado
	 * @param user Usuario autenticado
	 * @return Roles del usuario
	 */
	public List<GrantedAuthority> getRoles(PfUsersDTO user) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (PfUsersProfileDTO profile : user.getPfUsersProfiles()) {
			log.debug("Profile: " + profile.getPfProfile().getCprofile());

			// Se añade el perfil si es válido
			if (profile.getFend() == null || profile.getFend().after(new Date())) {
	     		authorities.add(new GrantedAuthorityImpl(profile.getPfProfile().getCprofile()));
			}
		}
		return authorities;
	}


}
