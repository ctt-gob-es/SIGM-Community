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
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_PLANTILLA_PETICION", uniqueConstraints = @UniqueConstraint(columnNames = "C_CODIGO_PLANTILLA"))
public class PfRequestTemplatesDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String ctemplateCode;
	private String dsigners;
	private String dsubject;
	private String dreference;
	private String csignType;
	private Boolean lreadNotice;
	private Boolean lrejectNotice;
	private Boolean lsignNotice;
	private Boolean lvalidateNotice;
	private Boolean laddPass;
	private Boolean lOnlyNotifyActionsToRemitter;
	private Date fstart;
	private Date fend;
	private Long cdocumentScope;
	private String cimportanceLevel;
	private Boolean ltimestamp;
	private String csignConfiguration;
	private String trequest;
	private PfUsersDTO pfUser;
	private String cSignPass;
	private String cAccionesFirma;

	public PfRequestTemplatesDTO() {
		super();
	}

	public PfRequestTemplatesDTO(Boolean lreadNotice, Boolean lrejectNotice,
								 Boolean lsignNotice, Boolean lvalidateNotice,
								 Boolean laddPass, Boolean ltimestamp, Boolean lOnlyNotifyActionsToRemitter) {
		super();
		this.lreadNotice = lreadNotice;
		this.lrejectNotice = lrejectNotice;
		this.lsignNotice = lsignNotice;
		this.lvalidateNotice = lvalidateNotice;
		this.laddPass = laddPass;
		this.ltimestamp = ltimestamp;
		this.lOnlyNotifyActionsToRemitter = lOnlyNotifyActionsToRemitter;
	}

	public PfRequestTemplatesDTO(String ccreated, Date fcreated,
			String cmodified, Date fmodified, String ctemplateCode, String dsigners,
			String dsubject, String dreference, String csignType, Boolean lreadNotice,
			Boolean lrejectNotice, Boolean lsignNotice, Boolean lvalidateNotice,
			Boolean laddPass, Date fstart, Date fend,Long cdocumentScope, String cimportanceLevel,
			Boolean ltimestamp, Boolean lOnlyNotifyActionsToRemitter, String csignConfiguration, 
			String trequest, PfUsersDTO pfUser) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.ctemplateCode = ctemplateCode;
		this.dsigners = dsigners;
		this.dsubject = dsubject;
		this.dreference = dreference;
		this.csignType = csignType;
		this.lreadNotice = lreadNotice;
		this.lrejectNotice = lrejectNotice;
		this.lsignNotice = lsignNotice;
		this.lvalidateNotice = lvalidateNotice;
		this.laddPass = laddPass;
		this.fstart = fstart;
		this.fend = fend;
		this.cdocumentScope = cdocumentScope;
		this.cimportanceLevel = cimportanceLevel;
		this.ltimestamp = ltimestamp;
		this.lOnlyNotifyActionsToRemitter = lOnlyNotifyActionsToRemitter;
		this.csignConfiguration = csignConfiguration;
		this.trequest = trequest;
		this.pfUser = pfUser;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_PLAN")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_PLAN", sequenceName = "PF_S_PLAN")
	@Column(name = "X_PLANTILLA_PETICION", unique = true, nullable = false, precision = 10, scale = 0)
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

	@Column(name = "C_CODIGO_PLANTILLA", unique = true, nullable = false, length = 255)
	public String getCtemplateCode() {
		return ctemplateCode;
	}

	public void setCtemplateCode(String ctemplateCode) {
		this.ctemplateCode = ctemplateCode;
	}

	@Column(name = "D_DESTINATARIOS", unique = false, nullable = true, length = 255)
	public String getDsigners() {
		return dsigners;
	}

	public void setDsigners(String dsigners) {
		this.dsigners = dsigners;
	}

	@Column(name = "D_ASUNTO", unique = false, nullable = true, length = 255)
	public String getDsubject() {
		return dsubject;
	}

	public void setDsubject(String dsubject) {
		this.dsubject = dsubject;
	}

	@Column(name = "D_REFERENCIA", unique = false, nullable = true, length = 30)
	public String getDreference() {
		return dreference;
	}

	public void setDreference(String dreference) {
		this.dreference = dreference;
	}

	@Column(name = "C_TIPO_FIRMA", unique = false, nullable = false, length = 30)
	public String getCsignType() {
		return csignType;
	}

	public void setCsignType(String csignType) {
		this.csignType = csignType;
	}

	@Column(name = "L_AVISO_LEIDO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLreadNotice() {
		return lreadNotice;
	}

	public void setLreadNotice(Boolean lreadNotice) {
		this.lreadNotice = lreadNotice;
	}

	@Column(name = "L_AVISO_DEVUELTO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLrejectNotice() {
		return lrejectNotice;
	}

	public void setLrejectNotice(Boolean lrejectNotice) {
		this.lrejectNotice = lrejectNotice;
	}

	@Column(name = "L_AVISO_FIRMADO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLsignNotice() {
		return lsignNotice;
	}

	public void setLsignNotice(Boolean lsignNotice) {
		this.lsignNotice = lsignNotice;
	}

	@Column(name = "L_AVISO_VISTO_BUENO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLvalidateNotice() {
		return lvalidateNotice;
	}

	public void setLvalidateNotice(Boolean lvalidateNotice) {
		this.lvalidateNotice = lvalidateNotice;
	}

	@Column(name = "L_AGREGAR_VISTO_BUENO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLaddPass() {
		return laddPass;
	}

	public void setLaddPass(Boolean laddPass) {
		this.laddPass = laddPass;
	}
	
	@Column(name = "L_SOLO_NOTIF_ACC_REMITE", nullable = true, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLOnlyNotifyActionsToRemitter() {
		return this.lOnlyNotifyActionsToRemitter;
	}

	public void setLOnlyNotifyActionsToRemitter(Boolean lOnlyNotifyActionsToRemitter) {
		this.lOnlyNotifyActionsToRemitter = lOnlyNotifyActionsToRemitter;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_INICIO", nullable = true, length = 7)
	public Date getFstart() {
		return fstart;
	}

	public void setFstart(Date fstart) {
		this.fstart = fstart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_CADUCIDAD", nullable = true, length = 7)
	public Date getFend() {
		return fend;
	}

	public void setFend(Date fend) {
		this.fend = fend;
	}

	@Column(name = "C_AMBITO_DOCUMENTOS", unique = false, nullable = false, precision = 10, scale = 0)
	public Long getCdocumentScope() {
		return cdocumentScope;
	}

	public void setCdocumentScope(Long cdocumentScope) {
		this.cdocumentScope = cdocumentScope;
	}

	@Column(name = "C_NIVEL_IMPORTANCIA", unique = false, nullable = false, length = 1)
	public String getCimportanceLevel() {
		return cimportanceLevel;
	}

	public void setCimportanceLevel(String cimportanceLevel) {
		this.cimportanceLevel = cimportanceLevel;
	}

	@Column(name = "L_AGREGAR_TIMESTAMP", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLtimestamp() {
		return ltimestamp;
	}

	public void setLtimestamp(Boolean ltimestamp) {
		this.ltimestamp = ltimestamp;
	}

	@Column(name = "C_CONFIGURACION_FIRMA", unique = false, nullable = false, length = 50)
	public String getCsignConfiguration() {
		return csignConfiguration;
	}

	public void setCsignConfiguration(String csignConfiguration) {
		this.csignConfiguration = csignConfiguration;
	}

	@Column(name = "T_PETICION")
	@Type(type = "es.seap.minhap.portafirmas.domain.type.StringClobType")
	public String getTrequest() {
		return trequest;
	}

	public void setTrequest(String trequest) {
		this.trequest = trequest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO", nullable = false)
	public PfUsersDTO getPfUser() {
		return pfUser;
	}

	public void setPfUser(PfUsersDTO pfUser) {
		this.pfUser = pfUser;
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
	
	@Column(name = "C_FIRMA_VISTOBUENO", unique = false, nullable = false, length = 50)
	public String getCSignPass() {
		return cSignPass;
	}
	
	public void setCSignPass(String cSignPass) {
		this.cSignPass = cSignPass;
	}
	
	@Column(name = "C_ACCIONES_FIRMA", unique = false, nullable = false, length = 50)
	public String getCAccionesFirma() {
		return cAccionesFirma;
	}
	
	public void setCAccionesFirma(String cAccionesFirma) {
		this.cAccionesFirma = cAccionesFirma;
	}
	

}
