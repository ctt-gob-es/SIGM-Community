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

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_USUARIOS_REMITENTE")
public class PfUsersRemitterDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfRequestsDTO pfRequest;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private Boolean lnotifyEmail;
	private Boolean lnotifyMobile;
	private PfUsersDTO pfUser;
	private PfGroupsDTO pfGroup;
	
	private String estadoParaRemitente;

	public PfUsersRemitterDTO() {
		super();
	}

	public PfUsersRemitterDTO(PfRequestsDTO pfRequest, String ccreated,
			Date fcreated, String cmodified, Date fmodified,
			Boolean lnotifyEmail, Boolean lnotifyMobile,
			PfUsersDTO pfUser, PfGroupsDTO pfGroup) {
		super();
		this.pfRequest = pfRequest;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.lnotifyEmail = lnotifyEmail;
		this.lnotifyMobile = lnotifyMobile;
		this.pfUser = pfUser;
		this.pfGroup = pfGroup;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_UREM")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_UREM", sequenceName = "PF_S_UREM")
	@Column(name = "X_USUARIO_REMITENTE", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PET_X_PETICION", nullable = false)
	public PfRequestsDTO getPfRequest() {
		return this.pfRequest;
	}

	public void setPfRequest(PfRequestsDTO pfRequest) {
		this.pfRequest = pfRequest;
	}

	@Column(name = "L_NOTIFICA_EMAIL", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLnotifyEmail() {
		return this.lnotifyEmail;
	}

	public void setLnotifyEmail(Boolean lnotifyEmail) {
		this.lnotifyEmail = lnotifyEmail;
	}

	@Column(name = "L_NOTIFICA_MOVIL", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLnotifyMobile() {
		return this.lnotifyMobile;
	}

	public void setLnotifyMobile(Boolean lnotifyMobile) {
		this.lnotifyMobile = lnotifyMobile;
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
	@JoinColumn(name = "GRU_X_GRUPO", nullable = true)
	public PfGroupsDTO getPfGroup() {
		return this.pfGroup;
	}

	public void setPfGroup(PfGroupsDTO pfGroup) {
		this.pfGroup = pfGroup;
	}

	@Column(name = "ESTADO_PARA_REMITENTE")
	public String getEstadoParaRemitente() {
		return estadoParaRemitente;
	}

	public void setEstadoParaRemitente(String estadoParaRemitente) {
		this.estadoParaRemitente = estadoParaRemitente;
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
}
