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

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_PETICIONES_INFORMACION")
public class PfInformationRequestsDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfApplicationsDTO pfApplication;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cinformation;
	private String dinformation;
	private Boolean lvisible;
	private Boolean lrequired;
	private Boolean lactive;
	private Integer norder;
	private Set<PfRequestsValueDTO> pfRequestsValues = new HashSet<PfRequestsValueDTO>(
			0);

	public PfInformationRequestsDTO() {
		super();
	}

	public PfInformationRequestsDTO(PfApplicationsDTO pfApplication,
			String ccreated, Date fcreated, String cmodified, Date fmodified,
			String cinformation, Boolean lvisible, Boolean lrequired,
			Boolean lactive, Integer norder) {
		super();
		this.pfApplication = pfApplication;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cinformation = cinformation;
		this.lvisible = lvisible;
		this.lrequired = lrequired;
		this.lactive = lactive;
		this.norder = norder;
	}

	public PfInformationRequestsDTO(PfApplicationsDTO pfApplication,
			String ccreated, Date fcreated, String cmodified, Date fmodified,
			String cinformation, String dinformation, Boolean lvisible,
			Boolean lrequired, Boolean lactive, Integer norder,
			Set<PfRequestsValueDTO> pfRequestsValues) {
		super();
		this.pfApplication = pfApplication;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cinformation = cinformation;
		this.dinformation = dinformation;
		this.lvisible = lvisible;
		this.lrequired = lrequired;
		this.lactive = lactive;
		this.norder = norder;
		this.pfRequestsValues = pfRequestsValues;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_PEIN")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_PEIN", sequenceName = "PF_S_PEIN")
	@Column(name = "X_PETICION_INFORMACION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "APL_X_APLICACION", nullable = false)
	public PfApplicationsDTO getPfApplication() {
		return this.pfApplication;
	}

	public void setPfApplication(PfApplicationsDTO pfApplication) {
		this.pfApplication = pfApplication;
	}

	@Column(name = "C_INFORMACION", nullable = false, length = 30)
	public String getCinformation() {
		return this.cinformation;
	}

	public void setCinformation(String cinformation) {
		this.cinformation = cinformation;
	}

	@Column(name = "D_INFORMACION")
	public String getDinformation() {
		return this.dinformation;
	}

	public void setDinformation(String dinformation) {
		this.dinformation = dinformation;
	}

	@Column(name = "L_VISIBLE", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLvisible() {
		return this.lvisible;
	}

	public void setLvisible(Boolean lvisible) {
		this.lvisible = lvisible;
	}

	@Column(name = "L_OBLIGATORIO", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLrequired() {
		return this.lrequired;
	}

	public void setLrequired(Boolean lrequired) {
		this.lrequired = lrequired;
	}

	@Column(name = "L_VIGENTE", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLactive() {
		return this.lactive;
	}

	public void setLactive(Boolean lactive) {
		this.lactive = lactive;
	}

	@Column(name = "N_ORDEN", nullable = false, precision = 2, scale = 0)
	public Integer getNorder() {
		return this.norder;
	}

	public void setNorder(Integer norder) {
		this.norder = norder;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfInformationRequest")
	public Set<PfRequestsValueDTO> getPfRequestsValues() {
		return this.pfRequestsValues;
	}

	public void setPfRequestsValues(Set<PfRequestsValueDTO> pfRequestsValues) {
		this.pfRequestsValues = pfRequestsValues;
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
