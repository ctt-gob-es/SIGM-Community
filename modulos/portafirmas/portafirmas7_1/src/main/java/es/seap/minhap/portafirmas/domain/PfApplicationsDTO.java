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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PF_APLICACIONES", uniqueConstraints = @UniqueConstraint(columnNames = "C_APLICACION"))
public class PfApplicationsDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfApplicationsDTO pfApplication;
	private PfConfigurationsDTO pfConfiguration;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String capplication;
	private String dapplication;
	private Set<PfRequestsDTO> pfRequests = new HashSet<PfRequestsDTO>(0);
	private Set<PfDocumentTypesDTO> pfDocumentTypes = new HashSet<PfDocumentTypesDTO>(
			0);
	private Set<PfFiltersDTO> pfFilters = new HashSet<PfFiltersDTO>(0);
	private Set<PfInformationRequestsDTO> pfInformationRequests = new HashSet<PfInformationRequestsDTO>(
			0);
	private Set<PfApplicationUsersDTO> pfApplicationUsers = new HashSet<PfApplicationUsersDTO>(
			0);
	private Set<PfApplicationsParameterDTO> pfApplicationsParameters = new HashSet<PfApplicationsParameterDTO>(
			0);
	private Set<PfApplicationsDTO> pfApplications = new HashSet<PfApplicationsDTO>(
			0);
	private Set<PfApplicationsMetadataDTO> pfApplicationsMetadatas = new HashSet<PfApplicationsMetadataDTO>(
			0);
	private Set<PfValidatorApplicationDTO> pfValidatorApplications = new HashSet<PfValidatorApplicationDTO>(0);

	private Long visibleEnPortaFirmaWeb = 0L;
	private String descripcionWeb = "_";

	@Column(name = "DESCRIPCION_WEB")
	public String getDescripcionWeb() {
		return descripcionWeb;
	}

	public void setDescripcionWeb(String descripcionWeb) {
		this.descripcionWeb = descripcionWeb;
	}

	@Column(name = "VISIBLE_EN_PORTAFIRMA_WEB")
	public Long getVisibleEnPortaFirmaWeb() {
		return visibleEnPortaFirmaWeb;
	}

	public void setVisibleEnPortaFirmaWeb(Long visibleEnPortaFirmaWeb) {
		this.visibleEnPortaFirmaWeb = visibleEnPortaFirmaWeb;
	}

	public PfApplicationsDTO() {
		super();
	}

	public PfApplicationsDTO(PfConfigurationsDTO pfConfiguration,
			String ccreated, Date fcreated, String cmodified, Date fmodified,
			String capplication) {
		super();
		this.pfConfiguration = pfConfiguration;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.capplication = capplication;
	}

	public PfApplicationsDTO(PfApplicationsDTO pfApplication,
			PfConfigurationsDTO pfConfiguration, String ccreated,
			Date fcreated, String cmodified, Date fmodified,
			String capplication, String dapplication,
			Set<PfRequestsDTO> pfRequests,
			Set<PfDocumentTypesDTO> pfDocumentTypes,
			Set<PfFiltersDTO> pfFilters,
			Set<PfInformationRequestsDTO> pfInformationRequests,
			Set<PfApplicationUsersDTO> pfApplicationUsers,
			Set<PfApplicationsParameterDTO> pfApplicationsParameters,
			Set<PfApplicationsDTO> pfApplications) {
		super();
		this.pfApplication = pfApplication;
		this.pfConfiguration = pfConfiguration;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.capplication = capplication;
		this.dapplication = dapplication;
		this.pfRequests = pfRequests;
		this.pfDocumentTypes = pfDocumentTypes;
		this.pfFilters = pfFilters;
		this.pfInformationRequests = pfInformationRequests;
		this.pfApplicationUsers = pfApplicationUsers;
		this.pfApplicationsParameters = pfApplicationsParameters;
		this.pfApplications = pfApplications;
	}

	@Transient
	public String getHierarchy() {
		String hierarchy = this.capplication;
		if (pfApplication != null && pfApplication.getPrimaryKey() != null) {
			hierarchy = pfApplication.getHierarchy() + " > " + hierarchy;
		}
		return hierarchy;
	}
	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_APL")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_APL", sequenceName = "PF_S_APL")
	@Column(name = "X_APLICACION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APL_X_APLICACION")
	public PfApplicationsDTO getPfApplication() {
		return this.pfApplication;
	}

	public void setPfApplication(PfApplicationsDTO pfApplication) {
		this.pfApplication = pfApplication;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CON_X_CONFIGURACION", nullable = false)
	public PfConfigurationsDTO getPfConfiguration() {
		return this.pfConfiguration;
	}

	public void setPfConfiguration(PfConfigurationsDTO pfConfiguration) {
		this.pfConfiguration = pfConfiguration;
	}

	@Column(name = "C_APLICACION", unique = true, nullable = false, length = 30)
//	@Length(max = 30, message = "{errorCApplicationLength}")
	public String getCapplication() {
		return this.capplication;
	}

	public void setCapplication(String capplication) {
		this.capplication = capplication;
	}

	@Column(name = "D_APLICACION", length = 255)
	public String getDapplication() {
		return this.dapplication;
	}

	public void setDapplication(String dapplication) {
		this.dapplication = dapplication;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfRequestsDTO> getPfRequests() {
		return this.pfRequests;
	}

	public void setPfRequests(Set<PfRequestsDTO> pfRequests) {
		this.pfRequests = pfRequests;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfDocumentTypesDTO> getPfDocumentTypes() {
		return this.pfDocumentTypes;
	}

	public void setPfDocumentTypes(Set<PfDocumentTypesDTO> pfDocumentTypes) {
		this.pfDocumentTypes = pfDocumentTypes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfFiltersDTO> getPfFilters() {
		return this.pfFilters;
	}

	public void setPfFilters(Set<PfFiltersDTO> pfFilters) {
		this.pfFilters = pfFilters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfInformationRequestsDTO> getPfInformationRequests() {
		return this.pfInformationRequests;
	}

	public void setPfInformationRequests(
			Set<PfInformationRequestsDTO> pfInformationRequests) {
		this.pfInformationRequests = pfInformationRequests;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfApplicationUsersDTO> getPfApplicationUsers() {
		return this.pfApplicationUsers;
	}

	public void setPfApplicationUsers(
			Set<PfApplicationUsersDTO> pfApplicationUsers) {
		this.pfApplicationUsers = pfApplicationUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfApplicationsParameterDTO> getPfApplicationsParameters() {
		return this.pfApplicationsParameters;
	}

	public void setPfApplicationsParameters(Set<PfApplicationsParameterDTO> pfApplicationsParameters) {
		this.pfApplicationsParameters = pfApplicationsParameters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfApplicationsDTO> getPfApplications() {
		return this.pfApplications;
	}

	public void setPfApplications(Set<PfApplicationsDTO> pfApplications) {
		this.pfApplications = pfApplications;
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

	@PrePersist
	@Override
	public void createAuditing() {
		super.createAuditing();
	}

	@PreUpdate
	@Override
	public void updateAuditing() {
		super.updateAuditing();
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfApplicationsMetadataDTO> getPfApplicationsMetadatas() {
		return pfApplicationsMetadatas;
	}

	public void setPfApplicationsMetadatas(
			Set<PfApplicationsMetadataDTO> pfApplicationsMetadatas) {
		this.pfApplicationsMetadatas = pfApplicationsMetadatas;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfApplication")
	public Set<PfValidatorApplicationDTO> getPfValidatorApplications() {
		return pfValidatorApplications;
	}

	public void setPfValidatorApplications(
			Set<PfValidatorApplicationDTO> pfValidatorApplications) {
		this.pfValidatorApplications = pfValidatorApplications;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((primaryKey == null) ? 0 : primaryKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PfApplicationsDTO other = (PfApplicationsDTO) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}

}
