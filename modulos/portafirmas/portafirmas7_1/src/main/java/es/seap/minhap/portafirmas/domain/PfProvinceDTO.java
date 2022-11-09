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

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_PROVINCIA", uniqueConstraints = @UniqueConstraint(columnNames = "C_CODIGO_PROVINCIA"))
public class PfProvinceDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cnombre;
	private String ccodigoprovincia;
	private Boolean lLargaDuracion;
	private String cUrlCSV;
	private Boolean lLdap;
	private Set<PfUsersDTO> pfUsers = new HashSet<PfUsersDTO>(0);
	private Set<PfGroupsDTO> pfGroups = new HashSet<PfGroupsDTO>(0);
	private Set<PfProvinceAdminDTO> pfUsersProvinces = new HashSet<PfProvinceAdminDTO>(0);
	
	private PfUnidadOrganizacionalDTO organismo;
	private String provinciaPadre;

	public PfProvinceDTO() {
		super();
	}

	public PfProvinceDTO(String ccreated, Date fcreated, String cmodified, Date fmodified, String cnombre,
			String ccodigoprovincia, Boolean lLargaDuracion, String cUrlCSV, Boolean lLdap, String provinciaPadre) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cnombre = cnombre;
		this.ccodigoprovincia = ccodigoprovincia;
		this.lLargaDuracion = lLargaDuracion;
		this.cUrlCSV = cUrlCSV;
		this.lLdap = lLdap;
		this.provinciaPadre = provinciaPadre;
	}

	public PfProvinceDTO(String ccreated, Date fcreated, String cmodified, Date fmodified, String cnombre,
			String ccodigoprovincia, Set<PfUsersDTO> pfUsers, Set<PfGroupsDTO> pfGroups, 
			Set<PfProvinceAdminDTO> pfUsersProvinces, Boolean lLargaDuracion, String cUrlCSV, Boolean lLdap,
			String provinciaPadre) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cnombre = cnombre;
		this.ccodigoprovincia = ccodigoprovincia;
		this.pfUsers = pfUsers;
		this.pfGroups = pfGroups;
		this.pfUsersProvinces = pfUsersProvinces;
		this.lLargaDuracion = lLargaDuracion;
		this.cUrlCSV = cUrlCSV;
		this.lLdap = lLdap;
		this.provinciaPadre = provinciaPadre;
	}
	
	@Column(name = "L_LDAP", nullable = true, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLLdap() {
		return this.lLdap;
	}

	public void setLLdap(Boolean lLdap) {
		this.lLdap = lLdap;
	}
	

	@Column(name = "L_LARGA_DURACION", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLLargaDuracion() {
		return this.lLargaDuracion;
	}

	public void setLLargaDuracion(Boolean lLargaDuracion) {
		this.lLargaDuracion = lLargaDuracion;
	}
	
	@Column(name = "C_URL_CSV", nullable = true)
	public String getCUrlCSV() {
		return this.cUrlCSV;
	}

	public void setCUrlCSV(String cUrlCSV) {
		this.cUrlCSV = cUrlCSV;
	}
	
	@Column(name = "PROVINCIA_PADRE", nullable = true)
	public String getProvinciaPadre() {
		return this.provinciaPadre;
	}

	public void setProvinciaPadre(String provinciaPadre) {
		this.provinciaPadre = provinciaPadre;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_ORGANISMO")
	public PfUnidadOrganizacionalDTO getOrganismo() {
		return organismo;
	}

	public void setOrganismo(PfUnidadOrganizacionalDTO organismo) {
		this.organismo = organismo;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_PROV")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_PROV", sequenceName = "PF_S_PROV")
	@Column(name = "X_PROVINCIA", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "C_NOMBRE", unique = true, nullable = false, length = 255)
	public String getCnombre() {
		return cnombre;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfProvince")
	public Set<PfUsersDTO> getPfUsers() {
		return pfUsers;
	}

	public void setPfUsers(Set<PfUsersDTO> pfUsers) {
		this.pfUsers = pfUsers;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfProvince")
	public Set<PfGroupsDTO> getPfGroups() {
		return pfGroups;
	}

	public void setPfGroups(Set<PfGroupsDTO> pfGroups) {
		this.pfGroups = pfGroups;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfProvince")
	public Set<PfProvinceAdminDTO> getPfUsersProvinces() {
		return pfUsersProvinces;
	}

	public void setPfUsersProvinces(Set<PfProvinceAdminDTO> pfUsersProvinces) {
		this.pfUsersProvinces = pfUsersProvinces;
	}

	public void setCnombre(String cnombre) {
		this.cnombre = cnombre;
	}

	@Column(name = "C_CODIGO_PROVINCIA", unique = true, nullable = false, length = 255)
	public String getCcodigoprovincia() {
		return ccodigoprovincia;
	}

	public void setCcodigoprovincia(String ccodigoprovincia) {
		this.ccodigoprovincia = ccodigoprovincia;
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
		PfProvinceDTO other = (PfProvinceDTO) obj;
		if (primaryKey == null) {
			if (other.primaryKey != null)
				return false;
		} else if (!primaryKey.equals(other.primaryKey))
			return false;
		return true;
	}
	
	@Transient
	public String getNombreConPadres(){
		if(this.getOrganismo()!=null){
			return this.getOrganismo().getDenominacion()+" -> "+this.cnombre;
		}
		else{
			return this.cnombre;
		}
	}
}