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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PF_VALIDADOR_APLICACION")
public class PfValidatorApplicationDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private PfUsersDTO pfUser;
	private PfUsersDTO pfValidatorUser;
	private PfApplicationsDTO pfApplication;
	
	private Set<PfApplicationsDTO> pfApplications = new HashSet<PfApplicationsDTO>(
			0);

	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;

	public PfValidatorApplicationDTO(){

		super();
	}

	public PfValidatorApplicationDTO(PfUsersDTO pfUser, PfUsersDTO pfValidatorUser, PfApplicationsDTO pfApplication,
			Set<PfApplicationsDTO> pfApplications) {
		super();
		this.pfUser = pfUser;
		this.pfValidatorUser = pfValidatorUser;
		this.pfApplication = pfApplication;
		this.pfApplications = pfApplications;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_VAAP")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_VAAP", sequenceName = "PF_S_VAAP")
	@Column(name = "X_VALIDADOR_APLICACION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APL_X_APLICACION")
	public PfApplicationsDTO getPfApplication() {
		return this.pfApplication;
	}

	public void setPfApplication(PfApplicationsDTO pfApplication) {
		this.pfApplication = pfApplication;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO")
	public PfUsersDTO getPfUser() {
		return this.pfUser;
	}

	public void setPfUser(PfUsersDTO pfUser) {
		this.pfUser = pfUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO_VALIDADOR")
	public PfUsersDTO getPfValidatorUser() {
		return this.pfValidatorUser;
	}

	public void setPfValidatorUser(PfUsersDTO pfValidatorUser) {
		this.pfValidatorUser = pfValidatorUser;
	}

	@Override
	@Column(name = "C_CREADO", nullable = false, length = 20)
	public String getCcreated() {
		return this.ccreated;
	}

	@Override
	@Column(name = "C_MODIFICADO", nullable = false, length = 20)
	public String getCmodified() {
		return this.cmodified;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_CREADO", nullable = false, length = 7)
	public Date getFcreated() {
		return this.fcreated;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_MODIFICADO", nullable = false, length = 7)
	public Date getFmodified() {
		return this.fmodified;
	}

	@Override
	public void setCcreated(String ccreated) {
		this.ccreated = ccreated;
	}

	@Override
	public void setCmodified(String cmodified) {
		this.cmodified = cmodified;
	}

	@Override
	public void setFcreated(Date fcreated) {
		this.fcreated = fcreated;
	}

	@Override
	public void setFmodified(Date fmodified) {
		this.fmodified = fmodified;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfApplicationsDTO> getPfApplications() {
		return this.pfApplications;
	}

	public void setPfApplications(Set<PfApplicationsDTO> pfApplications) {
		this.pfApplications = pfApplications;
	}
}
