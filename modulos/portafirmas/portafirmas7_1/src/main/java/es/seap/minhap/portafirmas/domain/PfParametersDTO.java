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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PF_PARAMETROS", uniqueConstraints = @UniqueConstraint(columnNames = "C_PARAMETRO"))
public class PfParametersDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cparameter;
	private String dparameter;
	private String ctype;
	
	private Long esParametroGeneralAdmin;
	private String subtipoAdmin;
	
	private Set<PfApplicationsParameterDTO> pfApplicationsParameters = new HashSet<PfApplicationsParameterDTO>(
			0);
	private Set<PfConfigurationsParameterDTO> pfConfigurationParameters = new HashSet<PfConfigurationsParameterDTO>(
			0);
	private Set<PfOthersParameterDTO> pfOthersParameters = new HashSet<PfOthersParameterDTO>(
			0);

	public PfParametersDTO() {
		super();
	}

	public PfParametersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cparameter, String dparameter, String ctype) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cparameter = cparameter;
		this.dparameter = dparameter;
		this.ctype = ctype;
	}

	public PfParametersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cparameter, String dparameter, String ctype,
			Set<PfApplicationsParameterDTO> pfApplicationsParameters,
			Set<PfConfigurationsParameterDTO> pfConfigurationParameters,
			Set<PfOthersParameterDTO> pfOthersParameters) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cparameter = cparameter;
		this.dparameter = dparameter;
		this.ctype = ctype;
		this.pfApplicationsParameters = pfApplicationsParameters;
		this.pfConfigurationParameters = pfConfigurationParameters;
		this.pfOthersParameters = pfOthersParameters;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_PAR")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_PAR", sequenceName = "PF_S_PAR")
	@Column(name = "X_PARAMETRO", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	
	@Column(name = "ES_PARAMETRO_GENERAL_ADMIN")
	public Long getEsParametroGeneralAdmin() {
		return esParametroGeneralAdmin;
	}

	
	public void setEsParametroGeneralAdmin(Long esParametroGeneralAdmin) {
		this.esParametroGeneralAdmin = esParametroGeneralAdmin;
	}

	
	@Column(name = "SUBTIPO_ADMIN")
	public String getSubtipoAdmin() {
		return subtipoAdmin;
	}

	public void setSubtipoAdmin(String subtipoAdmin) {
		this.subtipoAdmin = subtipoAdmin;
	}

	@Column(name = "C_PARAMETRO", unique = true, nullable = false, length = 30)
	public String getCparameter() {
		return this.cparameter;
	}

	public void setCparameter(String cparameter) {
		this.cparameter = cparameter;
	}

	@Column(name = "D_PARAMETRO", nullable = false)
	public String getDparameter() {
		return this.dparameter;
	}

	public void setDparameter(String dparameter) {
		this.dparameter = dparameter;
	}

	@Column(name = "C_TIPO", nullable = false, length = 10)
	public String getCtype() {
		return this.ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfParameter")
	public Set<PfApplicationsParameterDTO> getPfApplicationsParameters() {
		return this.pfApplicationsParameters;
	}

	public void setPfApplicationsParameters(
			Set<PfApplicationsParameterDTO> pfApplicationsParameters) {
		this.pfApplicationsParameters = pfApplicationsParameters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfParameter")
	public Set<PfConfigurationsParameterDTO> getPfConfigurationParameters() {
		return this.pfConfigurationParameters;
	}

	public void setPfConfigurationParameters(
			Set<PfConfigurationsParameterDTO> pfConfigurationParameters) {
		this.pfConfigurationParameters = pfConfigurationParameters;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfParameter")
	public Set<PfOthersParameterDTO> getPfOthersParameters() {
		return this.pfOthersParameters;
	}

	public void setPfOthersParameters(
			Set<PfOthersParameterDTO> pfOthersParameters) {
		this.pfOthersParameters = pfOthersParameters;
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
