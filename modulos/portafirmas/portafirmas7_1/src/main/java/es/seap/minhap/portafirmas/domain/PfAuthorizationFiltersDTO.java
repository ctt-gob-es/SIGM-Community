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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "PF_FILTROS_AUTORIZACION")
public class PfAuthorizationFiltersDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfUsersAuthorizationDTO pfUsersAuthorization;
	private PfFiltersDTO pfFilter;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private Date fstart;
	private Date fend;
	private Boolean lvalid;
	private Set<PfSignersDTO> pfSigners = new HashSet<PfSignersDTO>(0);

	public PfAuthorizationFiltersDTO() {
		super();
	}

	public PfAuthorizationFiltersDTO(
			PfUsersAuthorizationDTO pfUsersAuthorization,
			PfFiltersDTO pfFilter, String ccreated, Date fcreated,
			String cmodified, Date fmodified, Date fstart, Boolean lvalid) {
		super();
		this.pfUsersAuthorization = pfUsersAuthorization;
		this.pfFilter = pfFilter;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.fstart = fstart;
		this.lvalid = lvalid;
	}

	public PfAuthorizationFiltersDTO(
			PfUsersAuthorizationDTO pfUsersAuthorization,
			PfFiltersDTO pfFilter, String ccreated, Date fcreated,
			String cmodified, Date fmodified, Date fstart, Date fend,
			Boolean lvalid, Set<PfSignersDTO> pfSigners) {
		super();
		this.pfUsersAuthorization = pfUsersAuthorization;
		this.pfFilter = pfFilter;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.fstart = fstart;
		this.fend = fend;
		this.lvalid = lvalid;
		this.pfSigners = pfSigners;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_FAUT")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_FAUT", sequenceName = "PF_S_FAUT")
	@Column(name = "X_FILTRO_AUTORIZACION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UAUT_X_USUARIO_AUTORIZACION")
	public PfUsersAuthorizationDTO getPfUsersAuthorization() {
		return this.pfUsersAuthorization;
	}

	public void setPfUsersAuthorization(
			PfUsersAuthorizationDTO pfUsersAuthorization) {
		this.pfUsersAuthorization = pfUsersAuthorization;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIL_X_FILTRO", nullable = false)
	public PfFiltersDTO getPfFilter() {
		return this.pfFilter;
	}

	public void setPfFilter(PfFiltersDTO pfFilter) {
		this.pfFilter = pfFilter;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_INICIO", nullable = false, length = 7)
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfAuthorizationFilter")
	public Set<PfSignersDTO> getPfSigners() {
		return this.pfSigners;
	}

	public void setPfSigners(Set<PfSignersDTO> pfSigners) {
		this.pfSigners = pfSigners;
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

}
