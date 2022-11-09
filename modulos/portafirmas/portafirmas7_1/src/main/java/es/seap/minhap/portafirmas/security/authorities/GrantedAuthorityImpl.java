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

package es.seap.minhap.portafirmas.security.authorities;

import org.springframework.security.core.GrantedAuthority;

/**
 * Implementación de GrantedAuthority, permite definir los roles de usuario
 * @author hugo
  */
public class GrantedAuthorityImpl implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	private String role;

	public GrantedAuthorityImpl(String role) {
		this.setRole(role);
	}

	@Override
	public String getAuthority() {
		return role;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
