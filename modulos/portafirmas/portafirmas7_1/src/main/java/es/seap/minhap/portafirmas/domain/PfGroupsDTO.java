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
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PF_GRUPOS", uniqueConstraints = @UniqueConstraint(columnNames = {"C_NOMBRE", "PROV_X_PROVINCIA"} ))
public class PfGroupsDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cnombre;
	private String cdescripcion;
	private PfProvinceDTO pfProvince;
	private Set<PfUsersGroupsDTO> pfUsersGroups = new HashSet<PfUsersGroupsDTO>(0);

	public PfGroupsDTO() {
		super();
	}

	public PfGroupsDTO(String ccreated, Date fcreated,
			String cmodified, Date fmodified, String cnombre,
			String cdescripcion, PfProvinceDTO pfProvince,
			Set<PfUsersGroupsDTO> pfUsersGroups) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cnombre = cnombre;
		this.cdescripcion = cdescripcion;
		this.pfProvince = pfProvince;
		this.pfUsersGroups = pfUsersGroups;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_GRUPO")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_GRUPO", sequenceName = "PF_S_GRUPO")
	@Column(name = "X_GRUPO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "C_NOMBRE", nullable = false, length = 255)
	public String getCnombre() {
		return cnombre;
	}

	public void setCnombre(String cnombre) {
		this.cnombre = cnombre;
	}

	@Column(name = "C_DESCRIPCION", length = 255)
	public String getCdescripcion() {
		return cdescripcion;
	}

	public void setCdescripcion(String cdescripcion) {
		this.cdescripcion = cdescripcion;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfGroup")
	public Set<PfUsersGroupsDTO> getPfUsersGroups() {
		return pfUsersGroups;
	}

	public void setPfUsersGroups(
			Set<PfUsersGroupsDTO> pfUsersGroups) {
		this.pfUsersGroups = pfUsersGroups;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PROV_X_PROVINCIA", nullable = true)
	public PfProvinceDTO getPfProvince() {
		return pfProvince;
	}

	public void setPfProvince(PfProvinceDTO pfProvince) {
		this.pfProvince = pfProvince;
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