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

@Entity
@Table(name = "PF_BLOQUES")
public class PfBlocksDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;

	private Long primaryKey;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String chash;
	private String ctransaction;
	private Date fsign;
	private String capplication;
	private String cserver;
	private String ctype;
	private String cformat;
	private String curi;
	private Set<PfBlockSignsDTO> pfBlockSigns = new HashSet<PfBlockSignsDTO>(0);

	public PfBlocksDTO() {
		super();
	}

	public PfBlocksDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String chash, String ctype, String cformat) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.ctype = ctype;
		this.cformat = cformat;
	}

	public PfBlocksDTO(String ccreated, Date fcreated, String cmodified,
			Date fmodified, String chash, String ctransaction, Date fsign,
			String capplication, String cserver, String ctype, String cformat,
			String curi, Set<PfBlockSignsDTO> pfBlockSigns) {
		super();
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.ctransaction = ctransaction;
		this.fsign = fsign;
		this.capplication = capplication;
		this.cserver = cserver;
		this.ctype = ctype;
		this.cformat = cformat;
		this.curi = curi;
		this.pfBlockSigns = pfBlockSigns;
	}

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_BLQ")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_BLQ", sequenceName = "PF_S_BLQ")
	@Column(name = "X_BLOQUE", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "C_HASH", nullable = false, length = 10)
	public String getChash() {
		return this.chash;
	}

	public void setChash(String chash) {
		this.chash = chash;
	}

	@Column(name = "C_TRANSACCION")
	public String getCtransaction() {
		return this.ctransaction;
	}

	public void setCtransaction(String ctransaction) {
		this.ctransaction = ctransaction;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "F_FIRMA", nullable = false, length = 7)
	public Date getFsign() {
		return this.fsign;
	}

	public void setFsign(Date fsign) {
		this.fsign = fsign;
	}

	@Column(name = "C_APLICACION")
	public String getCapplication() {
		return this.capplication;
	}

	public void setCapplication(String capplication) {
		this.capplication = capplication;
	}

	@Column(name = "C_SERVIDOR")
	public String getCserver() {
		return this.cserver;
	}

	public void setCserver(String cserver) {
		this.cserver = cserver;
	}

	@Column(name = "C_TIPO", nullable = false, length = 10)
	public String getCtype() {
		return this.ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
	}

	@Column(name = "C_FORMATO", nullable = false, length = 10)
	public String getCformat() {
		return this.cformat;
	}

	public void setCformat(String cformat) {
		this.cformat = cformat;
	}

	@Column(name = "C_URI", length = 1000)
	public String getCuri() {
		return this.curi;
	}

	public void setCuri(String curi) {
		this.curi = curi;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pfBlock")
	public Set<PfBlockSignsDTO> getPfBlockSigns() {
		return this.pfBlockSigns;
	}

	public void setPfBlockSigns(Set<PfBlockSignsDTO> pfBlockSigns) {
		this.pfBlockSigns = pfBlockSigns;
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
