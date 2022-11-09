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
import java.util.Iterator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import es.seap.minhap.portafirmas.utils.Constants;

@Entity
@Table(name = "PF_ETIQUETAS_PETICION")
public class PfRequestTagsDTO extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	private PfTagsDTO pfTag;
	private String ccreated;
	private Date fcreated;
	private String cmodified;
	private Date fmodified;
	private String chash;
	private PfRequestsDTO pfRequest;
	private PfUsersDTO pfUser;
	private PfSignLinesDTO pfSignLine;
	
	// Indica si las firmas de la petición han sido validadas o no
	private boolean validada = false;

	private boolean signModeCorrect = true;

	private PfGroupsDTO pfGroup;
	
	private PfUsersDTO pfUsuarioValidador;
	
	// Atributos para barra de progreso de proceso de firma.
	//private boolean enableBarStatus = false;
	//private String barCurrentPercent = "0";

	public PfRequestTagsDTO() {
		super();
	}

	public PfRequestTagsDTO(PfTagsDTO pfTag, String ccreated, Date fcreated,
			String cmodified, Date fmodified, String chash, PfRequestsDTO pfRequest,
			PfUsersDTO pfUser, PfSignLinesDTO pfSignLine) {
		super();
		this.pfTag = pfTag;
		this.ccreated = ccreated;
		this.fcreated = fcreated;
		this.cmodified = cmodified;
		this.fmodified = fmodified;
		this.chash = chash;
		this.pfRequest = pfRequest;
		this.pfUser = pfUser;
		this.pfSignLine = pfSignLine;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GRO_X_GROUP", nullable = false)
	public PfGroupsDTO getPfGroup() {
		return this.pfGroup;
	}
	
	public void setPfGroup(PfGroupsDTO pfGroup) {
		this.pfGroup = pfGroup;
	}

	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_ETPE")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_ETPE", sequenceName = "PF_S_ETPE")
	@Column(name = "X_ETIQUETA_PETICION", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ETIQ_X_ETIQUETA", nullable = false)
	public PfTagsDTO getPfTag() {
		return this.pfTag;
	}

	public void setPfTag(PfTagsDTO pfTag) {
		this.pfTag = pfTag;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PET_X_PETICION", nullable = false)
	public PfRequestsDTO getPfRequest() {
		return this.pfRequest;
	}

	public void setPfRequest(PfRequestsDTO pfRequest) {
		this.pfRequest = pfRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_USUARIO")
	public PfUsersDTO getPfUser() {
		return this.pfUser;
	}

	public void setPfUser(PfUsersDTO pfUser) {
		this.pfUser = pfUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LFIR_X_LINEA_FIRMA")
	public PfSignLinesDTO getPfSignLine() {
		return this.pfSignLine;
	}

	public void setPfSignLine(PfSignLinesDTO pfSignLine) {
		this.pfSignLine = pfSignLine;
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

	@Column(name = "C_HASH", unique = true, nullable = true, length = 10)
	public String getChash() {
		return this.chash;
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

	public void setChash(String chash) {
		this.chash = chash;
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
	public boolean isValidada() {
		return validada;
	}

	public void setValidada(boolean validada) {
		this.validada = validada;
	}

	@Transient
	public boolean isSignModeCorrect() {
		return signModeCorrect;
	}

	public void setSignModeCorrect(boolean signModeCorrect) {
		this.signModeCorrect = signModeCorrect;
	}

	@Transient
	public PfRequestTagsDTO getStateUser() {
		PfRequestTagsDTO result = null;
		Boolean pass = false;

		if(this.getPfRequest().getLinvited() && !this.getPfRequest().getLaccepted()){
			if (Constants.C_TYPE_TAG_STATE.equals(this.getPfTag().getCtype())){
				result = this;
			}
		}else{
			if (Constants.C_TYPE_TAG_STATE.equals(this.getPfTag().getCtype()) &&
				!this.getPfUser().isJob()) {
				result = this;
			}
		}

		if (this.getPfSignLine() != null) {
			if (Constants.C_TAG_PASSED.equals(this.getPfSignLine().getCtype()) && this.getPfUser()!=null &&
				!this.getPfUser().isJob() && result != null) {
				result.getPfTag().setPass(true);
			} else {
				if (result != null) {
					result.getPfTag().setPass(false);
				}
			}
		} else {
			// Recorre las etiquetas de la petici&oacute;n
			for (Iterator<PfRequestTagsDTO> iterator = this.getPfRequest().getPfRequestsTags().iterator(); iterator.hasNext();) {
				PfRequestTagsDTO aux = iterator.next();
				// Si la etiqueta es "TIPO.VISTOBUENO" se marca el flag "pass"
				if (Constants.C_TAG_SYSTEM_PASSED.equals(aux.getPfTag().getCtag()) &&
					!aux.getPfUser().isJob()) {
					if (result != null && this.getPfUser().getPrimaryKeyString().equals(aux.getPfUser().getPrimaryKeyString())) {
						pass = true;
					}
				}
			}
			if (result != null) {
				result.getPfTag().setPass(pass);
			}
		}

		return result;
	}

	@Transient
	public PfRequestTagsDTO getStateJob() {
		PfRequestTagsDTO result = null;
		Boolean pass = false;

		if(!this.getPfRequest().getLinvited() || (this.getPfRequest().getLinvited() && this.getPfRequest().getLaccepted())){
			if (Constants.C_TYPE_TAG_STATE.equals(this.getPfTag().getCtype()) &&
				this.getPfUser().isJob()) {
				result = this;
			}
		}else if (Constants.C_TYPE_TAG_STATE.equals(this.getPfTag().getCtype())) {
					result = this;
		}

		if (this.getPfSignLine() != null) {
			if (Constants.C_TAG_PASSED.equals(this.getPfSignLine().getCtype()) &&
				this.getPfUser().isJob() && result != null) {
				result.getPfTag().setPass(true);
			} else {
				if (result != null) {
					result.getPfTag().setPass(false);
				}
			}
		} else {
			// Recorre las etiquetas de la petici&oacute;n
			for (Iterator<PfRequestTagsDTO> iterator = this.getPfRequest().getPfRequestsTags().iterator(); iterator.hasNext();) {
				PfRequestTagsDTO aux = iterator.next();
				// Si la etiqueta es "TIPO.VISTOBUENO" se marca el flag "pass"
				if (Constants.C_TAG_SYSTEM_PASSED.equals(aux.getPfTag().getCtag()) &&
					aux.getPfUser().isJob()) {
					if (result != null && this.getPfUser().getPrimaryKeyString().equals(aux.getPfUser().getPrimaryKeyString())) {
						pass = true;
					}
				}
			}
			if (result != null) {
				result.getPfTag().setPass(pass);
			}
		}

		return result;
	}

	@Transient
	public boolean isNewRequest() {
		boolean newRequest = false;
		if ((this.getStateJob() != null && this.getStateJob()
				.getPfTag().getCtag().equals(Constants.C_TAG_NEW))
				|| (this.getStateUser() != null &&
					this.getStateUser().getPfTag().getCtag().equals(Constants.C_TAG_NEW))) {
			newRequest = true;
		}
		return newRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USU_X_VALIDADOR")
	public PfUsersDTO getPfUsuarioValidador() {
		return pfUsuarioValidador;
	}

	public void setPfUsuarioValidador(PfUsersDTO pfUsuarioValidador) {
		this.pfUsuarioValidador = pfUsuarioValidador;
	}

	/*public void setEnableBarStatus (boolean enableBarStatus) {
		this.enableBarStatus = enableBarStatus;
	}
	@Transient
	public boolean getEnableBarStatus () {
		return this.enableBarStatus;
	}
	
	public void setBarCurrentPercent (String barCurrentPercent) {
		System.out.println("setBarCurrentPercent: " +barCurrentPercent);
		this.barCurrentPercent = barCurrentPercent;
	}
	@Transient
	public String getBarCurrentPercent () {
		System.out.println("getBarCurrentPercent: " +this.barCurrentPercent + " / " + this.getPrimaryKeyString());
		return this.barCurrentPercent;
	}*/
}
