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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import es.seap.minhap.portafirmas.utils.Util;

@Entity
@Table(name = "PF_USUARIOS_INVITADOS", uniqueConstraints = @UniqueConstraint(columnNames ={"C_DNI", "C_MAIL"}))
public class PfInvitedUsersDTO extends AbstractBaseDTO {
	
	private static final long serialVersionUID = 1L;
	
	private String cDni;
	private String cMail;
	private String cName;
	private String cSurname1;
	private String cSurname2;
	
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;

	public PfInvitedUsersDTO() {
		super();
	}

	public PfInvitedUsersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cDni, String cName, String cSurname1, 
			String cSurname2, String cMail) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cDni=cDni;
		this.cMail=cMail;
		this.cName=cName;
		this.cSurname1 = cSurname1;
		this.cSurname2 = cSurname2;
	}

	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_USU_INV_GEN")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_USU_INV_GEN", sequenceName = "PF_S_USU_INV")
	@Column(name = "X_USU_INV", unique = true, nullable = false, precision = 10, scale = 0)
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


	@Column(name = "C_DNI", length = 10)
	public String getcDni() {
		return cDni;
	}

	public void setcDni(String cDni) {
		this.cDni = cDni;
	}


	@Column(name = "C_MAIL", length = 50, nullable=false)
	public String getcMail() {
		return cMail;
	}

	public void setcMail(String cMail) {
		this.cMail = cMail;
	}


	@Column(name = "C_NOMBRE", length = 20)
	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
	}


	@Column(name = "C_APELLIDO1", length = 20)
	public String getcSurname1() {
		return cSurname1;
	}

	public void setcSurname1(String cSurname1) {
		this.cSurname1 = cSurname1;
	}

	@Column(name = "C_APELLIDO2", length = 20)
	public String getcSurname2() {
		return cSurname2;
	}

	public void setcSurname2(String cSurname2) {
		this.cSurname2 = cSurname2;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PfInvitedUsersDTO other = (PfInvitedUsersDTO) obj;
		if(!cMail.equals(other.getcMail())){
			return false;
		}
		return true;
	}
	

}