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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PF_EMAILS_PETICION")
public class PfEmailsRequestDTO extends AbstractBaseDTO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cemail;
	private PfRequestsDTO pfRequest;

	
	public PfEmailsRequestDTO() {
		super();
	}

	
	
	
	public PfEmailsRequestDTO(String ccreated, Date fcreated,
			String cmodified, Date fmodified, String cemail,
			PfRequestsDTO pfRequest) {
		super();		
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cemail = cemail;
		this.pfRequest = pfRequest;
	}




	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_EMA_PET_GEN")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_EMA_PET_GEN", sequenceName = "PF_S_EMA_PET")
	@Column(name = "X_EMAIL_PETICION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
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

	
	@Column(name = "C_EMAIL", nullable = false, length = 255)
	public String getCemail() {
		return cemail;
	}

	public void setCemail(String cemail) {
		this.cemail = cemail;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PET_X_PETICION", nullable = false)
	public PfRequestsDTO getPfRequest() {
		return this.pfRequest;
	}

	public void setPfRequest(PfRequestsDTO pfPeticiones) {
		this.pfRequest = pfPeticiones;
	}
	
	@Override
	@PrePersist
	public void createAuditing() {
		super.createAuditing();
	}

	@Override
	@PreUpdate
	public void updateAuditing() {
		super.updateAuditing();
	}

}
