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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_MENSAJES")
public class PfMessagesDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String dsubject;
	private String ttext;
	private Date fstart;
	private Date fexpiration;
	private PfProvinceDTO pfProvince;
	private PfUsersDTO pfUser;
	private PfMessageScopesDTO pfMessageScope;
	private Set<PfUsersMessageDTO> pfUsersMessages = new HashSet<PfUsersMessageDTO>(0);


	public PfMessagesDTO() {
		super();
	}

	public PfMessagesDTO(String ccreated, Date fcreated, String cmodified, Date fmodified,
			String dsubject, Date fstart,Date fexpiration, PfProvinceDTO pfProvince,
			PfMessageScopesDTO pfMessageScope, Set<PfUsersMessageDTO> pfUsersMessages) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.dsubject = dsubject;
		this.fstart = fstart;
		this.fexpiration = fexpiration;
		this.pfProvince = pfProvince;
		this.pfMessageScope = pfMessageScope;
		this.pfUsersMessages = pfUsersMessages;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_MEN")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_MEN", sequenceName = "PF_S_MEN")
	@Column(name = "X_MENSAJE", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}


	@Column(name = "D_ASUNTO", nullable = false, length = 255)
	public String getDsubject() {
		return this.dsubject;
	}

	public void setDsubject(String dsubject) {
		this.dsubject = dsubject;
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

	/**
	 * @return the fstart
	 */
	@Column(name = "F_INICIO", nullable = false, length = 7)
	public Date getFstart() {
		return fstart;
	}

	/**
	 * @param fstart the fstart to set
	 */
	public void setFstart(Date fstart) {
		this.fstart = fstart;
	}

	/**
	 * @return the fexpiration
	 */
	@Column(name = "F_CADUCIDAD", nullable = false, length = 7)
	public Date getFexpiration() {
		return fexpiration;
	}

	/**
	 * @param fexpiration the fexpiration to set
	 */
	public void setFexpiration(Date fexpiration) {
		this.fexpiration = fexpiration;
	}

	/**
	 * @return the pfUsersMessages
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfMessage")
	public Set<PfUsersMessageDTO> getPfUsersMessages() {
		return pfUsersMessages;
	}

	/**
	 * @param pfUsersMessages the pfUsersMessages to set
	 */
	public void setPfUsersMessages(Set<PfUsersMessageDTO> pfUsersMessages) {
		this.pfUsersMessages = pfUsersMessages;
	}
	

	@Transient
	public boolean isExpired() {
		boolean expired = false;
		if (this.fexpiration != null && this.fexpiration.before(new Date())) {
			expired = true;
		}
		return expired;
	}

	/**
	 * @return the ttext
	 */
	@Column(name = "T_TEXTO")
	@Type(type = "es.seap.minhap.portafirmas.domain.type.StringClobType")
	public String getTtext() {
		return ttext;
	}

	/**
	 * @param ttext the ttext to set
	 */
	public void setTtext(String ttext) {
		this.ttext = ttext;
	}

	/**
	 * @return the pfProvince
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROV_X_PROVINCIA", nullable = true	)
	public PfProvinceDTO getPfProvince() {
		return pfProvince;
	}

	/**
	 * @param pfProvince the pfProvince to set
	 */
	public void setPfProvince(PfProvinceDTO pfProvince) {
		this.pfProvince = pfProvince;
	}

	/**
	 * @return the pfMessageScope
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AMB_X_AMBITO_MENSAJE", nullable = false)
	public PfMessageScopesDTO getPfMessageScope() {
		return pfMessageScope;
	}

	/**
	 * @param pfMessageScope the pfMessageScope to set
	 */
	public void setPfMessageScope(PfMessageScopesDTO pfMessageScope) {
		this.pfMessageScope = pfMessageScope;
	}

	/**
	 * @return the pfUser
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO", nullable = false)
	public PfUsersDTO getPfUser() {
		return pfUser;
	}

	/**
	 * @param pfUser the pfUser to set
	 */
	public void setPfUser(PfUsersDTO pfUser) {
		this.pfUser = pfUser;
	}
	
	
}
