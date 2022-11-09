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

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import es.seap.minhap.portafirmas.utils.ListComparador;

@Entity
@Table(name = "PF_FILTROS")
public class PfFiltersDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private PfApplicationsDTO pfApplication;
	private PfAuthorizationTypesDTO pfAuthorizationType;
	private PfUsersDTO pfUser;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cfilter;
	private Date fstart;
	private Date fend;
	private Boolean lvalid;
	private String treason;
	private String tsubjectFilter;
	private Long corder;
	private Set<PfAuthorizationFiltersDTO> pfAuthorizationFilters = new HashSet<PfAuthorizationFiltersDTO>(0);

	public PfFiltersDTO() {
		super();
	}

	public PfFiltersDTO(PfAuthorizationTypesDTO pfAuthorizationType,
			PfUsersDTO pfUser, String ccreated, Date fcreated,
			String cmodified, Date fmodified, String cfilter, Date fstart,
			Boolean lvalid) {
		super();
		this.pfAuthorizationType = pfAuthorizationType;
		this.pfUser = pfUser;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cfilter = cfilter;
		this.fstart = fstart;
		this.lvalid = lvalid;
	}

	public PfFiltersDTO(PfApplicationsDTO pfApplication,
			PfAuthorizationTypesDTO pfAuthorizationType, PfUsersDTO pfUser,
			String ccreated, Date fcreated, String cmodified, Date fmodified,
			String cfilter, Date fstart, Date fend, Boolean lvalid,
			String treason, String tsubjectFilter, Long corder,
			Set<PfAuthorizationFiltersDTO> pfAuthorizationFilters) {
		super();
		this.pfApplication = pfApplication;
		this.pfAuthorizationType = pfAuthorizationType;
		this.pfUser = pfUser;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cfilter = cfilter;
		this.fstart = fstart;
		this.fend = fend;
		this.lvalid = lvalid;
		this.treason = treason;
		this.tsubjectFilter = tsubjectFilter;
		this.corder = corder;
		this.pfAuthorizationFilters = pfAuthorizationFilters;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_FIL")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_FIL", sequenceName = "PF_S_FIL")
	@Column(name = "X_FILTRO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long xfilter) {
		this.primaryKey = xfilter;
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
	@JoinColumn(name = "TAUT_X_TIPO_AUTORIZACION", nullable = false)
	public PfAuthorizationTypesDTO getPfAuthorizationType() {
		return this.pfAuthorizationType;
	}

	public void setPfAuthorizationType(
			PfAuthorizationTypesDTO pfAuthorizationType) {
		this.pfAuthorizationType = pfAuthorizationType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO", nullable = false)
	public PfUsersDTO getPfUser() {
		return this.pfUser;
	}

	public void setPfUser(PfUsersDTO pfUser) {
		this.pfUser = pfUser;
	}

//	@Length(max = 30, message = "{errorCFilterLength}")
	@Column(name = "C_FILTRO", nullable = false, length = 30)
	public String getCfilter() {
		return this.cfilter;
	}

	public void setCfilter(String cfilter) {
		this.cfilter = cfilter;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_INICIO", length = 7)
	public Date getFstart() {
		return this.fstart;
	}

	public void setFstart(Date fstart) {
		this.fstart = fstart;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_FIN", length = 7)
	public Date getFend() {
		return this.fend;
	}

	public void setFend(Date fend) {
		this.fend = fend;
	}

	@Column(name = "L_VALIDO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLvalid() {
		return this.lvalid;
	}

	public void setLvalid(Boolean lvalid) {
		this.lvalid = lvalid;
	}

//	@Length(max = 200, message = "{errorTReasonLength}")
	@Column(name = "T_MOTIVO", length = 200)
	public String getTreason() {
		return this.treason;
	}

	public void setTreason(String treason) {
		this.treason = treason;
	}

	@Column(name = "T_FILTRO_ASUNTO", length = 50)
	public String getTsubjectFilter() {
		return this.tsubjectFilter;
	}

	public void setTsubjectFilter(String tsubjectFilter) {
		this.tsubjectFilter = tsubjectFilter;
	}

	@Column(name = "C_ORDEN", precision = 10, scale = 0)
	public Long getCorder() {
		return this.corder;
	}

	public void setCorder(Long corder) {
		this.corder = corder;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfFilter")
	public Set<PfAuthorizationFiltersDTO> getPfAuthorizationFilters() {
		return this.pfAuthorizationFilters;
	}

	public void setPfAuthorizationFilters(
			Set<PfAuthorizationFiltersDTO> pfAuthorizationFilters) {
		this.pfAuthorizationFilters = pfAuthorizationFilters;
	}

	@Transient
	public List<PfAuthorizationFiltersDTO> getPfAuthorizationFiltersList() {
		List<PfAuthorizationFiltersDTO> result = new ArrayList<PfAuthorizationFiltersDTO>();
		for (Iterator<PfAuthorizationFiltersDTO> iterator = pfAuthorizationFilters
				.iterator(); iterator.hasNext();) {
			PfAuthorizationFiltersDTO aux = (PfAuthorizationFiltersDTO) iterator
					.next();
			result.add(aux);
		}
		return result;
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
	public String getReceivers() {
		String result = "";

		List<PfAuthorizationFiltersDTO> aux = this.getPfAuthorizationFiltersList();
		
		ListComparador comparador = new ListComparador("fullName", 1);
		Collections.sort(aux, comparador);
		int i = 0;
		for (Iterator<PfAuthorizationFiltersDTO> iterator = aux.iterator(); iterator.hasNext();) {
			PfAuthorizationFiltersDTO autFilter = (PfAuthorizationFiltersDTO) iterator
					.next();
			PfUsersAuthorizationDTO userAut = autFilter
					.getPfUsersAuthorization();
			if (i == 0) {
				result += userAut.getPfAuthorizedUser().getFullName();
			} else {
				result += ", " + userAut.getPfAuthorizedUser().getFullName();
			}
			i++;
		}

		return result;
	}
}
