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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * The persistent class for the PF_DOCELWEB_DOCUMENTO database table.
 * 
 */
@Entity
@Table(name = "PF_DOCELWEB_DOCUMENTO")
public class PfDocelwebDocumentDTO extends AbstractBaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long primaryKey;
	private String dDescription;
	private String dDispositionUrl;
	private Boolean dReqSign;
	private String dDocumentPath;
	private String dDocelType;
	private PfDocelwebRequestSpfirmaDTO pfRequest;
	private PfDocumentsDTO pfDoc;
	private PfFilesDTO pfFile;
	private String ccreated;
	private String cmodified;
	private Date fcreated;
	private Date fmodified;

	public PfDocelwebDocumentDTO() {
	}

	@Id
	@SequenceGenerator(name = "PF_S_DOCELWEB_DOCUMENTO", sequenceName = "PF_S_DOCELWEB_DOCUMENTO", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_DOCELWEB_DOCUMENTO")
	@Column(name = "X_DOCUMENTO", unique = true, nullable = false, precision = 10)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "D_DESCRIPCION", length = 500)
	public String getdDescription() {
		return this.dDescription;
	}

	public void setdDescription(String dDescription) {
		this.dDescription = dDescription;
	}

	@Column(name = "D_DISPOSICION_URL", length = 500)
	public String getdDispositionUrl() {
		return this.dDispositionUrl;
	}

	public void setdDispositionUrl(String dDispositionUrl) {
		this.dDispositionUrl = dDispositionUrl;
	}

	@Column(name = "D_REQ_FIRMA", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getdReqSign() {
		return this.dReqSign;
	}

	public void setdReqSign(Boolean dReqSign) {
		this.dReqSign = dReqSign;
	}

	@Column(name = "D_RUTA_DOCUMENTO", length = 255)
	public String getdDocumentPath() {
		return this.dDocumentPath;
	}

	public void setdDocumentPath(String dDocumentPath) {
		this.dDocumentPath = dDocumentPath;
	}

	@Column(name = "D_TIPO_DOCEL", nullable = false, length = 1)
	public String getdDocelType() {
		return this.dDocelType;
	}

	public void setdDocelType(String dDocelType) {
		this.dDocelType = dDocelType;
	}

	// uni-directional many-to-one association to PfArchivo
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_X_ARCHIVO")
	public PfFilesDTO getPfFile() {
		return this.pfFile;
	}

	public void setPfFile(PfFilesDTO pfFile) {
		this.pfFile = pfFile;
	}

	// bi-directional many-to-one association to PfDocelwebRequestDTO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_X_SOLICITUD", nullable = false)
	public PfDocelwebRequestSpfirmaDTO getPfRequest() {
		return this.pfRequest;
	}

	public void setPfRequest(PfDocelwebRequestSpfirmaDTO pfRequest) {
		this.pfRequest = pfRequest;
	}

	// bi-directional one-to-one association to PfSignsDTO
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_X_PFDOC")
	public PfDocumentsDTO getPfDoc() {
		return pfDoc;
	}

	public void setPfDoc(PfDocumentsDTO pfDoc) {
		this.pfDoc = pfDoc;
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