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

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de Andaluc&iacute;a

 Derechos de explotaci&oacute;n propiedad de la Junta de Andaluc&iacute;a.

 Éste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los t&eacute;rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la Comisi&oacute;n Europea, en su versi&oacute;n 1.0. o posteriores.

 Éste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÍA, incluso sin las presuntas garant&iacute;as impl&iacute;citas de USABILIDAD o ADECUACIÓN A PROPÓSITO 
 CONCRETO. Para mas informaci&oacute;n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por alg&uacute;n motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reçu une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

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
@Table(name = "PF_USUARIOS_CARGO")
public class PfUsersJobDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfUsersDTO pfUser;
	private PfUsersDTO pfUserJob;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private Date fstart;
	private Date fend;

	public PfUsersJobDTO() {
		super();
	}

	public PfUsersJobDTO(PfUsersDTO pfUser, PfUsersDTO pfUserJob,
			String ccreated, Date fcreated, String cmodified, Date fmodified,
			Date fstart) {
		super();
		this.pfUser = pfUser;
		this.pfUserJob = pfUserJob;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.fstart = fstart;
	}

	public PfUsersJobDTO(PfUsersDTO pfUser, PfUsersDTO pfUserJob,
			String ccreated, Date fcreated, String cmodified, Date fmodified,
			Date fstart, Date fend) {
		super();
		this.pfUser = pfUser;
		this.pfUserJob = pfUserJob;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.fstart = fstart;
		this.fend = fend;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_UCAR")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_UCAR", sequenceName = "PF_S_UCAR")
	@Column(name = "X_USUARIO_CARGO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long xusuarioCargo) {
		this.primaryKey = xusuarioCargo;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO", nullable = false)
	public PfUsersDTO getPfUser() {
		return this.pfUser;
	}

	public void setPfUser(PfUsersDTO pfUser) {
		this.pfUser = pfUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO_CARGO", nullable = false)
	public PfUsersDTO getPfUserJob() {
		return this.pfUserJob;
	}

	public void setPfUserJob(PfUsersDTO pfUserJob) {
		this.pfUserJob = pfUserJob;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_INICIO", nullable = false, length = 7)
	public Date getFstart() {
		return this.fstart;
	}

	public void setFstart(Date fstart) {
		this.fstart = fstart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_FIN", length = 7)
	public Date getFend() {
		return this.fend;
	}

	public void setFend(Date fend) {
		this.fend = fend;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PfUsersJobDTO other = (PfUsersJobDTO) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
	
}
