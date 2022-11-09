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
@Table(name = "PF_SERVIDORES", uniqueConstraints = @UniqueConstraint(columnNames = "C_SERVIDOR"))
public class PfServersDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String cserver;
	private String dserver;
	private Boolean lmain;
	private Set<PfConfigurationsDTO> pfConfigurations = new HashSet<PfConfigurationsDTO>(
			0);

	public PfServersDTO() {
		super();
	}

	public PfServersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cserver, String dserver, Boolean lmain) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cserver = cserver;
		this.dserver = dserver;
		this.lmain = lmain;
	}

	public PfServersDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String cserver, String dserver, Boolean lmain,
			Set<PfConfigurationsDTO> pfConfigurations) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.cserver = cserver;
		this.dserver = dserver;
		this.lmain = lmain;
		this.pfConfigurations = pfConfigurations;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_SER")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_SER", sequenceName = "PF_S_SER")
	@Column(name = "X_SERVIDOR", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "C_SERVIDOR", unique = true, nullable = false, length = 10)
//	@Length(max = 10, message = "{errorCServerLength}")
	public String getCserver() {
		return this.cserver;
	}

	public void setCserver(String cserver) {
		this.cserver = cserver;
	}

	@Column(name = "D_SERVIDOR", nullable = false, length = 50)
//	@Length(max = 50, message = "{errorDServerLength}")
	public String getDserver() {
		return this.dserver;
	}

	public void setDserver(String dserver) {
		this.dserver = dserver;
	}

	@Column(name = "L_PRINCIPAL", nullable = false, length = 1)
	@Type(type = "es.seap.minhap.portafirmas.domain.type.CheckboxType")
	public Boolean getLmain() {
		return this.lmain;
	}

	public void setLmain(Boolean lmain) {
		this.lmain = lmain;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfServer")
	public Set<PfConfigurationsDTO> getPfConfigurations() {
		return this.pfConfigurations;
	}

	@Transient
	public List<AbstractBaseDTO> getPfConfigurationsList() {
		List<AbstractBaseDTO> list = new ArrayList<AbstractBaseDTO>();
		for (Iterator<PfConfigurationsDTO> iterator = this.pfConfigurations.iterator(); iterator.hasNext();) {
			PfConfigurationsDTO aux = iterator.next();
			list.add(aux);
		}
		return list;
	}

	public void setPfConfigurations(Set<PfConfigurationsDTO> pfConfigurations) {
		this.pfConfigurations = pfConfigurations;
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
	public String getUpdatedChar(){
		String upChar = "";
		if(this.updated){
			upChar = "*";
		}
		return upChar;
	}
}
