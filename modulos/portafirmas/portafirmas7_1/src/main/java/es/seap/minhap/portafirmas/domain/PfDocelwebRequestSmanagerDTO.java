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
import java.util.Set;

import javax.persistence.CascadeType;
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

import org.hibernate.annotations.Type;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;

/**
 * The persistent class for the PF_DOCELWEB_SOLICITUD_SGESTION database table.
 * 
 */
@Entity
@Table(name = "PF_DOCELWEB_SOLICITUD_SGESTION")
public class PfDocelwebRequestSmanagerDTO extends AbstractBaseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long primaryKey;
	private String ccreated;
	private String cmodified;
	private String dDescription;
	private String dState;
	private String textRejct;
	private Long idTransaccion;
	private String dPriority;
	private Date fcreated;
	private Date fmodified;
	private Boolean lReturnRequest;
	private Portafirmas portafirmas;
	private PfRequestTagsDTO pfEtiquetaPeticion;
	private Set<PfDocelwebSmDocDTO> pfDocelwebSmDocs;

	public PfDocelwebRequestSmanagerDTO() {
	}

	@Id
	@SequenceGenerator(name = "PF_S_DOCELWEB_SOL_SGEST", sequenceName = "PF_S_DOCELWEB_SOL_SGEST", allocationSize = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_DOCELWEB_SOL_SGEST")
	@Column(name = "X_ID", unique = true, nullable = false, precision = 10)
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
		this.dState = dState;
	}

	@Column(name = "TEXTO_RECHAZO", nullable = true, length = 200)
	public String getTextRejct() {
		return textRejct;
	}

	public void setTextRejct(String textRejct) {
		this.textRejct = textRejct;
	}

	@Column(name = "ID_TRANSACCION", nullable = false, precision = 10)
	public Long getIdTransaccion() {
		return idTransaccion;
	}

	public void setIdTransaccion(Long idTransaccion) {
		this.idTransaccion = idTransaccion;
	}

	@Column(name = "D_PRIORIDAD", nullable = false, length = 1)
	public String getdPriority() {
		return this.dPriority;
	}

	public void setdPriority(String dPriority) {
		this.dPriority = dPriority;
	}

	@Column(name = "L_DEVOLUCION_SOLICITUD", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getlReturnRequest() {
		return this.lReturnRequest;
	}

	public void setlReturnRequest(Boolean lReturnRequest) {
		this.lReturnRequest = lReturnRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOLICITUD_X_PFIRMA", nullable = false)
	public Portafirmas getPortafirmas() {
		return portafirmas;
	}
	
	public void setPortafirmas(Portafirmas portafirmas) {
		this.portafirmas = portafirmas;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "X_ETIQUETA_PETICION", nullable = false)
	public PfRequestTagsDTO getPfEtiquetaPeticion() {
		return this.pfEtiquetaPeticion;
	}

	public void setPfEtiquetaPeticion(PfRequestTagsDTO pfEtiquetaPeticion) {
		this.pfEtiquetaPeticion = pfEtiquetaPeticion;
	}

	// bi-directional many-to-one association to PfDocelwebSmDocDTO
	@OneToMany(mappedBy = "pfDocelwebSolicitudSgestion", cascade = CascadeType.REMOVE)
	public Set<PfDocelwebSmDocDTO> getPfDocelwebSmDocs() {
		return this.pfDocelwebSmDocs;
	}

	public void setPfDocelwebSmDocs(Set<PfDocelwebSmDocDTO> pfDocelwebSmDocs) {
		this.pfDocelwebSmDocs = pfDocelwebSmDocs;
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