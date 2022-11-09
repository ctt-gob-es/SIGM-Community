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

package es.seap.minhap.portafirmas.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PF_USUARIOS_RESTRINGIDOS")
public class PfUsersRestrictDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	private PfUsersDTO pfUserRestrict;
	private PfUsersDTO pfUserValid;

	public PfUsersRestrictDTO() {
		super();
	}

	public PfUsersRestrictDTO(PfUsersDTO pfUserRestrict, PfUsersDTO pfUserValid) {
		super();
		this.pfUserRestrict = pfUserRestrict;
		this.pfUserValid = pfUserValid;
	}	

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO_RESTRINGIDO", nullable = false)
	public PfUsersDTO getPfUserRestrict() {
		return this.pfUserRestrict;
	}

	public void setPfUserRestrict(PfUsersDTO pfUserRestrict) {
		this.pfUserRestrict = pfUserRestrict;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO_VALIDO", nullable = false)
	public PfUsersDTO getPfUserValid() {
		return this.pfUserValid;
	}

	public void setPfUserValid(PfUsersDTO pfUserValid) {
		this.pfUserValid = pfUserValid;
	}
}
