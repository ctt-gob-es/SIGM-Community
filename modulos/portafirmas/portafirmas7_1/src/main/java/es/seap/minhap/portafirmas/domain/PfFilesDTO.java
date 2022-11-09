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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PF_ARCHIVOS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"C_HASH", "C_TIPO" }))
public class PfFilesDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String chash;
	private String cHashAlg;
	private String ctype;
	private String curi;
	// private Blob bfile;
	private String tcomments;
	private PfFilesDTO pfFile;
	private Set<PfFilesDTO> pfFiles = new HashSet<PfFilesDTO>(0);
	private Set<PfDocumentsDTO> pfDocuments = new HashSet<PfDocumentsDTO>(0);
	private String refNasDir3;
	private String idEni;
	
	public PfFilesDTO() {
		super();
	}

	public PfFilesDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String chash, String cHashAlg, String ctype,
			String curi, String tcomments) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.cHashAlg = cHashAlg;
		this.ctype = ctype;
		this.curi = curi;
		this.tcomments = tcomments;
	}

	public PfFilesDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String chash, String cHashAlg, String ctype,
			String curi, String tcomments, Set<PfDocumentsDTO> pfDocuments,
			PfFilesDTO pfFile, Set<PfFilesDTO> pfFiles) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.cHashAlg = cHashAlg;
		this.ctype = ctype;
		this.curi = curi;
		// this.bfile = bfile;
		this.tcomments = tcomments;
		this.pfDocuments = pfDocuments;
		this.pfFile = pfFile;
		this.pfFiles = pfFiles;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_ARC")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_ARC", sequenceName = "PF_S_ARC")
	@Column(name = "X_ARCHIVO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "C_HASH", nullable = false, length = 512)
	public String getChash() {
		return this.chash;
	}

	public void setChash(String chash) {
		this.chash = chash;
	}

	@Column(name = "C_HASH_ALG", nullable = false, length = 10)
	public String getChashAlg() {
		return this.cHashAlg;
	}

	public void setChashAlg(String cHashAlg) {
		this.cHashAlg = cHashAlg;
	}

	@Column(name = "C_TIPO", nullable = false, length = 10)
	public String getCtype() {
		return this.ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	@Column(name = "C_URI", length = 1000)
	public String getCuri() {
		return this.curi;
	}

	public void setCuri(String curi) {
		this.curi = curi;
	}

	/*
	 * @Column(name = "B_ARCHIVO") public Blob getBfile() { return this.bfile; }
	 * 
	 * public void setBfile(Blob bfile) { this.bfile = bfile; }
	 */

	@Column(name = "T_COMENTARIOS", length = 1000)
	public String getTcomments() {
		return this.tcomments;
	}

	public void setTcomments(String tcomments) {
		this.tcomments = tcomments;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARC_X_ARCHIVO")
	public PfFilesDTO getPfFile() {
		return this.pfFile;
	}

	public void setPfFile(PfFilesDTO pfFile) {
		this.pfFile = pfFile;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfFile")
	public Set<PfFilesDTO> getPfFiles() {
		return this.pfFiles;
	}

	public void setPfFiles(Set<PfFilesDTO> pfFiles) {
		this.pfFiles = pfFiles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfFile")
	public Set<PfDocumentsDTO> getPfDocuments() {
		return this.pfDocuments;
	}

	public void setPfDocuments(Set<PfDocumentsDTO> pfDocuments) {
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

	@Column(name = "REF_NAS_DIR3", length = 255)
	public String getRefNasDir3() {
		return refNasDir3;
	}

	public void setRefNasDir3(String refNasDir3) {
		this.refNasDir3 = refNasDir3;
	}

	@Column(name = "REF_NAS_IDENI", length = 255)
	public String getIdEni() {
		return idEni;
	}

	public void setIdEni(String idEni) {
		this.idEni = idEni;
	}
}
