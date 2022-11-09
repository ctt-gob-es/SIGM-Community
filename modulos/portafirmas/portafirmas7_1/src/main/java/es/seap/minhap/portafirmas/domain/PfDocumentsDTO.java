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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

import es.seap.minhap.portafirmas.utils.Constants;

@Entity
@Table(name = "PF_DOCUMENTOS", uniqueConstraints = @UniqueConstraint(columnNames = "C_HASH") )
public class PfDocumentsDTO extends AbstractBaseDTO implements Comparable<PfDocumentsDTO> {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private PfDocumentTypesDTO pfDocumentType;
	private PfFilesDTO pfFile;
	private PfRequestsDTO pfRequest;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String chash;
	private String dname;
	private String dmime;
	private String tcomments;
	private Boolean lsign;
	private String cpresign;
	private PfDocumentScopesDTO pfDocumentScope;
	private Set<PfActionsDTO> pfActions = new HashSet<PfActionsDTO>(0);
	private Set<PfSignsDTO> pfSigns = new HashSet<PfSignsDTO>(0);
	private Boolean lissign;
	private String comentarioAnexo;


	private List<PfUsersDTO> pfUsuariosList;
	private PfUsersDTO usuXUsuarioAnexa;

	public PfDocumentsDTO() {
		super();
	}

	public PfDocumentsDTO(PfDocumentTypesDTO pfDocumentType, PfFilesDTO pfFile, PfRequestsDTO pfRequest,
			String ccreated, Date fcreated, String cmodified, Date fmodified, String chash, String dname, String dmime,
			Boolean lsign, String cpresign, PfDocumentScopesDTO pfDocumentScope, Boolean lIsSign) {
		super();
		this.pfDocumentType = pfDocumentType;
		this.pfFile = pfFile;
		this.pfRequest = pfRequest;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.dname = dname;
		this.dmime = dmime;
		this.lsign = lsign;
		this.cpresign = cpresign;
		this.pfDocumentScope = pfDocumentScope;
		this.lissign = lIsSign;
	}

	public PfDocumentsDTO(PfDocumentTypesDTO pfDocumentType, PfFilesDTO pfFile, PfRequestsDTO pfRequest,
			String ccreated, Date fcreated, String cmodified, Date fmodified, String chash, String dname, String dmime,
			String tcomments, Boolean lsign, PfDocumentScopesDTO pfDocumentScope, Set<PfActionsDTO> pfActions,
			Set<PfSignsDTO> pfSigns, Boolean lIsSign) {
		super();
		this.pfDocumentType = pfDocumentType;
		this.pfFile = pfFile;
		this.pfRequest = pfRequest;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.dname = dname;
		this.dmime = dmime;
		this.tcomments = tcomments;
		this.lsign = lsign;
		this.pfDocumentScope = pfDocumentScope;
		this.pfActions = pfActions;
		this.pfSigns = pfSigns;
		this.lissign = lIsSign;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_DOC")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_DOC", sequenceName = "PF_S_DOC")
	@Column(name = "X_DOCUMENTO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		if("tempHash".equalsIgnoreCase(chash)){
			chash = Long.toHexString(primaryKey);
		}
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TDOC_X_TIPO_DOCUMENTO", nullable = false)
	public PfDocumentTypesDTO getPfDocumentType() {
		return this.pfDocumentType;
	}

	public void setPfDocumentType(PfDocumentTypesDTO pfDocumentType) {
		this.pfDocumentType = pfDocumentType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARC_X_ARCHIVO", nullable = false)
	public PfFilesDTO getPfFile() {
		return this.pfFile;
	}

	public void setPfFile(PfFilesDTO pfFile) {
		this.pfFile = pfFile;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PET_X_PETICION", nullable = false)
	public PfRequestsDTO getPfRequest() {
		return this.pfRequest;
	}

	public void setPfRequest(PfRequestsDTO pfRequest) {
		this.pfRequest = pfRequest;
	}

	@Column(name = "C_HASH", unique = true, nullable = false, length = 10)
	public String getChash() {
		return this.chash;
	}

	public void setChash(String chash) {
		this.chash = chash;
	}

	@Column(name = "D_NOMBRE", nullable = false)
	public String getDname() {
		return this.dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	@Column(name = "D_MIME", nullable = false, length = 50)
	public String getDmime() {
		return this.dmime;
	}

	public void setDmime(String dmime) {
		this.dmime = dmime;
	}

	@Column(name = "T_COMENTARIOS", length = 1000)
	public String getTcomments() {
		return this.tcomments;
	}

	public void setTcomments(String tcomments) {
		this.tcomments = tcomments;
	}

	@Column(name = "L_FIRMABLE", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLsign() {
		return this.lsign;
	}

	public void setLsign(Boolean lsign) {
		this.lsign = lsign;
	}

	@Column(name = "C_PREFIRMA", length = 100)
	public String getCpresign() {
		return this.cpresign;
	}

	public void setCpresign(String cpresign) {
		this.cpresign = cpresign;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfDocument")
	public Set<PfActionsDTO> getPfActions() {
		return this.pfActions;
	}

	public void setPfActions(Set<PfActionsDTO> pfActions) {
		this.pfActions = pfActions;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "pfDocument")
	public Set<PfSignsDTO> getPfSigns() {
		return this.pfSigns;
	}

	public void setPfSigns(Set<PfSignsDTO> pfSigns) {
		this.pfSigns = pfSigns;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AMB_X_AMBITO", nullable = false)
	public PfDocumentScopesDTO getPfDocumentScope() {
		return pfDocumentScope;
	}

	public void setPfDocumentScope(PfDocumentScopesDTO pfDocumentScope) {
		this.pfDocumentScope = pfDocumentScope;
	}

	@Column(name = "L_ES_FIRMA", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLissign() {
		return this.lissign;
	}

	public void setLissign(Boolean lIsSign) {
		this.lissign = lIsSign;
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

	@Transient
	public boolean isTcn() {
		return this.dname.endsWith(".tcn") || this.dname.endsWith(".TCN") || this.dname.endsWith(".Tcn");
		//sustituir y probar por la optimización:
		//return this.dname.toLowerCase().endsWith(".tcn");
		//.. pero si hay más extensiones que transformar, hay que cambiar todo
	}

	@Transient
	public boolean isSigned() {
		boolean signed = false;
		Iterator<PfSignsDTO> signs = pfSigns.iterator();
		while (signs.hasNext() && !signed) {
			if (!signs.next().getCtype().contentEquals(Constants.C_TYPE_SIGN_PASS)) {
				signed = true;
			}
		}
		return signed;
	}

	@Override
	public int compareTo(PfDocumentsDTO o) {
		if (dname.compareTo(o.dname) < 0) {
			return -1;
		}
		if (dname.compareTo(o.dname) > 0) {
			return 1;
		}
		return 0;
	}

	@JoinTable(name = "PF_USUARIOS_DOC_VISIBLES", joinColumns = {
			@JoinColumn(name = "DOC_X_DOCUMENTO", referencedColumnName = "X_DOCUMENTO") }, inverseJoinColumns = {
			@JoinColumn(name = "USU_X_USUARIO", referencedColumnName = "X_USUARIO") })
	@ManyToMany(fetch = FetchType.LAZY)
	public List<PfUsersDTO> getPfUsuariosList() {
		return pfUsuariosList;
	}

	public void setPfUsuariosList(List<PfUsersDTO> pfUsuariosList) {
		this.pfUsuariosList = pfUsuariosList;
	}

	@JoinColumn(name = "USU_X_USUARIO_ANEXA", referencedColumnName = "X_USUARIO")
	@ManyToOne
	public PfUsersDTO getUsuXUsuarioAnexa() {
		return usuXUsuarioAnexa;
	}

	public void setUsuXUsuarioAnexa(PfUsersDTO usuXUsuarioAnexa) {
		this.usuXUsuarioAnexa = usuXUsuarioAnexa;
	}

	@Column(name = "C_COMENTARIO_ANEXO", nullable = true, length = 50)
	public String getComentarioAnexo() {
		return comentarioAnexo;
	}

	public void setComentarioAnexo(String comentarioAnexo) {
		this.comentarioAnexo = comentarioAnexo;
	}

	
	
}
