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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_FIRMAS")
public class PfSignsDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfSignersDTO pfSigner;
	private PfUsersDTO pfUser;
	private PfDocumentsDTO pfDocument;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private Date fstate;
	private String tobservations;
	private String ctransaction;
	private String capplication;
	private String cserver;
	private String ctype;
	private String curi;
	private String cformat;
	private String csv;
	private Set<PfBlockSignsDTO> pfBlockSigns = new HashSet<PfBlockSignsDTO>(0);
	
	private PfSignsDTO2 documentos;
	private PfSignsDTO3 tieneDocumentos;
	private Date fValid;
	private Boolean lmovil;	
	private String csvNormalizado;
	private String refNASDir3Informe; 
	private String refNASDir3InfNormalizado;
	private String refNASDir3Firma;
	private String refNASIdEniFirma;
	
	private String cTipoStorageInforme;
	private String cTipoStorageInformeNormalizado;
	
	public PfSignsDTO() {
		super();
	}

	public PfSignsDTO(PfSignersDTO pfSigner, PfUsersDTO pfUser,
			PfDocumentsDTO pfDocument, String ccreated, Date fcreated,
			String cmodified, Date fmodified, Date fstate, String ctype,
			String cformat, String csv, Boolean lmovil) {
		super();
		this.pfSigner = pfSigner;
		this.pfUser = pfUser;
		this.pfDocument = pfDocument;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.fstate = fstate;
		this.ctype = ctype;
		this.cformat = cformat;
		this.csv = csv;
		this.lmovil = lmovil;
	}

	public PfSignsDTO(PfSignersDTO pfSigner, PfUsersDTO pfUser,
			PfDocumentsDTO pfDocument, String ccreated, Date fcreated,
			String cmodified, Date fmodified, Date fstate,
			String tobservations, String ctransaction, String capplication,
			String ctype, String curi, String cserver, String csv,
			Set<PfBlockSignsDTO> pfBlockSigns) {
		super();
		this.pfSigner = pfSigner;
		this.pfUser = pfUser;
		this.pfDocument = pfDocument;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.fstate = fstate;
		this.tobservations = tobservations;
		this.ctransaction = ctransaction;
		this.capplication = capplication;
		this.cserver = cserver;
		this.ctype = ctype;
		this.curi = curi;
		this.csv = csv;
		this.pfBlockSigns = pfBlockSigns;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_FIRM")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_FIRM", sequenceName = "PF_S_FIRM")
	@Column(name = "X_FIRMA", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIR_X_FIRMANTE", nullable = false)
	public PfSignersDTO getPfSigner() {
		return this.pfSigner;
	}

	public void setPfSigner(PfSignersDTO pfSigner) {
		this.pfSigner = pfSigner;
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
	@JoinColumn(name = "DOC_X_DOCUMENTO", nullable = false)
	public PfDocumentsDTO getPfDocument() {
		return this.pfDocument;
	}

	public void setPfDocument(PfDocumentsDTO pfDocument) {
		this.pfDocument = pfDocument;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_ESTADO", nullable = false, length = 7)
	public Date getFstate() {
		return this.fstate;
	}

	public void setFstate(Date fstate) {
		this.fstate = fstate;
	}

	@Column(name = "T_OBSERVACIONES", length = 1000)
	public String getTobservations() {
		return this.tobservations;
	}

	public void setTobservations(String tobservations) {
		this.tobservations = tobservations;
	}

	@Column(name = "C_TRANSACCION")
	public String getCtransaction() {
		return this.ctransaction;
	}

	public void setCtransaction(String ctransaction) {
		this.ctransaction = ctransaction;
	}

	@Column(name = "C_APLICACION")
	public String getCapplication() {
		return this.capplication;
	}

	public void setCapplication(String capplication) {
		this.capplication = capplication;
	}

	@Column(name = "C_SERVIDOR")
	public String getCserver() {
		return this.cserver;
	}

	public void setCserver(String cserver) {
		this.cserver = cserver;
	}

	@Column(name = "C_TIPO", nullable = false, length = 10)
	public String getCtype() {
		return this.ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	@Column(name = "C_FORMATO", nullable = false, length = 10)
	public String getCformat() {
		return this.cformat;
	}

	public void setCformat(String cformat) {
		this.cformat = cformat;
	}

	@Column(name = "C_URI", length = 1000)
	public String getCuri() {
		return this.curi;
	}

	public void setCuri(String curi) {
		this.curi = curi;
	}

	@Column(name = "CSV", length = 32)
	public String getCsv() {
		return this.csv;
	}

	public void setCsv(String csv) {
		this.csv = csv;
	}


	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfSign")
	public Set<PfBlockSignsDTO> getPfBlockSigns() {
		return this.pfBlockSigns;
	}

	public void setPfBlockSigns(Set<PfBlockSignsDTO> pfBlockSigns) {
		this.pfBlockSigns = pfBlockSigns;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_VALIDO", nullable = false, length = 7)
	public Date getfValid() {
		return fValid;
	}

	public void setfValid(Date fValid) {
		this.fValid = fValid;
	}
	
	@OneToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="X_FIRMA", referencedColumnName="X_FIRMA", nullable=true, updatable=true, insertable=false)
	public PfSignsDTO2 getDocumentos() {
	    return documentos;
	}

	public void setDocumentos(PfSignsDTO2 documentos) {
	    this.documentos = documentos;
	}

	@OneToOne(fetch = FetchType.LAZY, optional=false)
	@JoinColumn(name="X_FIRMA", referencedColumnName="X_FIRMA", nullable=true, updatable=true, insertable=false)
	public PfSignsDTO3 getTieneDocumentos() {
	    return tieneDocumentos;
	}

	public void setTieneDocumentos(PfSignsDTO3 tieneDocumentos) {
	    this.tieneDocumentos = tieneDocumentos;
	}
	
	@Column(name = "L_MOVIL")
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLmovil() {
		return lmovil;
	}

	public void setLmovil(Boolean lmovil) {
		this.lmovil = lmovil;
	}

	@Column(name = "CSV_INF_NORMALIZADO", length = 32)
	public String getCsvNormalizado() {
		return csvNormalizado;
	}

	public void setCsvNormalizado(String csvNormalizado) {
		this.csvNormalizado = csvNormalizado;
	}

	@Column(name = "REF_NAS_DIR3_INFORME", length = 255)
	public String getRefNASDir3Informe() {
		return refNASDir3Informe;
	}

	public void setRefNASDir3Informe(String refNASDir3Informe) {
		this.refNASDir3Informe = refNASDir3Informe;
	}

	@Column(name = "REF_NAS_DIR3_INF_NORMALIZADO", length = 255)
	public String getRefNASDir3InfNormalizado() {
		return refNASDir3InfNormalizado;
	}

	public void setRefNASDir3InfNormalizado(String refNASDir3InfNormalizado) {
		this.refNASDir3InfNormalizado = refNASDir3InfNormalizado;
	}
	
	@Column(name = "C_TIPO_INFORME", length = 10)
	public String getcTipoInforme() {
		return cTipoStorageInforme;
	}

	public void setcTipoInforme(String cTipoInforme) {
		this.cTipoStorageInforme = cTipoInforme;
	}
	
	@Column(name = "C_TIPO_INF_NORMALIZADO", length = 10)
	public String getcTipoInformeNormalizado() {
		return cTipoStorageInformeNormalizado;
	}

	public void setcTipoInformeNormalizado(String cTipoInformeNormalizado) {
		this.cTipoStorageInformeNormalizado = cTipoInformeNormalizado;
	}
	
	@Column(name = "REF_NAS_DIR3_FIRMA", length = 10)
	public String getRefNASDir3Firma() {
		return refNASDir3Firma;
	}

	public void setRefNASDir3Firma(String refNASDir3Firma) {
		this.refNASDir3Firma = refNASDir3Firma;
	}
	
	@Column(name = "REF_NAS_IDENI", length = 10)
	public String getRefNASIdEniFirma() {
		return refNASIdEniFirma;
	}

	public void setRefNASIdEniFirma(String refNASIdEniFirma) {
		this.refNASIdEniFirma = refNASIdEniFirma;
	}

}
