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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;

/**
 * The persistent class for the PF_DOCELWEB_SOLICITUD_SPFIRMA database table.
 * 
 */
@Entity
@Table(name = "PF_DOCELWEB_SOLICITUD_SPFIRMA")
public class PfDocelwebRequestSpfirmaDTO extends AbstractBaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long primaryKey;
	private String dDescription;
	private String dState;
	private String dSignatureFormat;
	private String dPriority;
	private String dSenderName;
	private String dMultisignType;
	private String dXadesVersion;
	private Date fDeadline;
	private Portafirmas portafirmas;
	private List<PfUsersDTO> pfSigners;
	private PfRequestsDTO pfRequest;
	private Set<PfDocelwebDocumentDTO> pfDocuments = new HashSet<PfDocelwebDocumentDTO>(0);
	private String ccreated;
	private String cmodified;
	private Date fcreated;
	private Date fmodified;

	public PfDocelwebRequestSpfirmaDTO() {
	}

	@Id
	@SequenceGenerator(name = "PF_S_DOCELWEB_SOL_SPFIR", sequenceName = "PF_S_DOCELWEB_SOL_SPFIR", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_DOCELWEB_SOL_SPFIR")
	@Column(name = "X_ID_TRANSACCION", unique = true, nullable = false, precision = 10)
	@Override
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "D_DESCRIPCION", length = 255)
	public String getdDescription() {
		return this.dDescription;
	}

	public void setdDescription(String dDescription) {
		this.dDescription = dDescription;
	}

	@Column(name = "D_ESTADO", nullable = false, length = 1)
	public String getdState() {
		return this.dState;
	}

	public void setdState(String dState) {
		this.dState = dState.toUpperCase();
	}

	@Column(name = "D_FORMATO_FIRMA", nullable = false, length = 5)
	public String getdSignatureFormat() {
		return this.dSignatureFormat;
	}

	public void setdSignatureFormat(String dSignatureFormat) {
		this.dSignatureFormat = dSignatureFormat.toUpperCase();
	}

	@Column(name = "D_PRIORIDAD", nullable = false, length = 1)
	public String getdPriority() {
		return this.dPriority;
	}

	public void setdPriority(String dPriority) {
		this.dPriority = dPriority.toUpperCase();
	}

	@Column(name = "D_REMITENTE", nullable = false, length = 100)
	public String getdSenderName() {
		return this.dSenderName;
	}

	public void setdSenderName(String dSenderName) {
		this.dSenderName = dSenderName;
	}

	@Column(name = "D_TIPO_MULTIFIRMA", nullable = false, length = 10)
	public String getdMultisignType() {
		return this.dMultisignType;
	}

	public void setdMultisignType(String dMultisignType) {
		this.dMultisignType = dMultisignType.toUpperCase();
	}

	@Column(name = "D_VERSION_XADES", length = 5)
	public String getdXadesVersion() {
		return this.dXadesVersion;
	}

	public void setdXadesVersion(String dXadesVersion) {
		this.dXadesVersion = dXadesVersion;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_FECHA_LIMITE")
	public Date getfDeadline() {
		return this.fDeadline;
	}

	public void setfDeadline(Date fDeadline) {
		this.fDeadline = fDeadline;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOLICITUD_X_GESTOR", nullable = false)
	public Portafirmas getPortafirmas() {
		return portafirmas;
	}
	
	public void setPortafirmas(Portafirmas portafirmas) {
		this.portafirmas = portafirmas;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "PF_DOCELWEB_X_LISTA_FIRMANTES", joinColumns = @JoinColumn(name = "X_ID_TRANSACCION", nullable = false), inverseJoinColumns = @JoinColumn(name = "X_USUARIO", nullable = false))
	public List<PfUsersDTO> getPfSigners() {
		return pfSigners;
	}

	public void setPfSigners(List<PfUsersDTO> pfSigners) {
		this.pfSigners = pfSigners;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOLICITUD_X_PETICION")
	public PfRequestsDTO getPfRequest() {
		return this.pfRequest;
	}

	public void setPfRequest(PfRequestsDTO pfRequest) {
		this.pfRequest = pfRequest;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfRequest", cascade = CascadeType.REMOVE)
	public Set<PfDocelwebDocumentDTO> getPfDocuments() {
		return pfDocuments;
	}

	public void setPfDocuments(Set<PfDocelwebDocumentDTO> pfDocuments) {
		this.pfDocuments = pfDocuments;
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