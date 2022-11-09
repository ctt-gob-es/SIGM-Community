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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the PF_DOCELWEB_SM_DOC database table.
 * 
 */
@Entity
@Table(name = "PF_DOCELWEB_SM_DOC")
public class PfDocelwebSmDocDTO extends AbstractBaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long primaryKey;
	private String dDescripcion;
	private Long xDocumento;
	private PfDocelwebRequestSmanagerDTO pfDocelwebSolicitudSgestion;
	private PfDocumentsDTO pfDocumentDTO;
	private String ccreated;
	private String cmodified;
	private Date fcreated;
	private Date fmodified;

	public PfDocelwebSmDocDTO() {
	}

	@Id
	@SequenceGenerator(name = "PF_S_DOCELWEB_SM_DOC", sequenceName = "PF_S_DOCELWEB_SM_DOC", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_DOCELWEB_SM_DOC")
	@Column(name = "X_SM_DOC", unique = true, nullable = false, precision = 10)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "D_DESCRIPCION", nullable = false, length = 255)
	public String getDDescripcion() {
		return this.dDescripcion;
	}

	public void setDDescripcion(String dDescripcion) {
		this.dDescripcion = dDescripcion;
	}

	@Column(name = "X_DOCUMENTO", nullable = false, precision = 10)
	public Long getXDocumento() {
		return this.xDocumento;
	}

	public void setXDocumento(Long xDocumento) {
		this.xDocumento = xDocumento;
	}

	// bi-directional many-to-one association to PfDocelwebRequestSmanagerDTO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "X_SM_REQUEST", nullable = false)
	public PfDocelwebRequestSmanagerDTO getPfDocelwebSolicitudSgestion() {
		return this.pfDocelwebSolicitudSgestion;
	}

	public void setPfDocelwebSolicitudSgestion(PfDocelwebRequestSmanagerDTO pfDocelwebSolicitudSgestion) {
		this.pfDocelwebSolicitudSgestion = pfDocelwebSolicitudSgestion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DOC_X_DOCUMENTO", nullable = false)
	public PfDocumentsDTO getPfDocumentDTO() {
		return pfDocumentDTO;
	}

	public void setPfDocumentDTO(PfDocumentsDTO pfDocumentDTO) {
		this.pfDocumentDTO = pfDocumentDTO;
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