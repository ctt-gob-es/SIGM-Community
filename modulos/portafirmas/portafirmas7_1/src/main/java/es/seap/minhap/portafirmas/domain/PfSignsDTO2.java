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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "PF_FIRMAS")
public class PfSignsDTO2 extends AbstractBaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long primaryKey;
	
	private byte[] bInforme;
	
	private byte[] bFirma;
	
	private byte[] bInformeNormalizado;

	public PfSignsDTO2() {
		super();
	}

	
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PF_S_FIRM")
	@SequenceGenerator(allocationSize = 1, name = "PF_S_FIRM", sequenceName = "PF_S_FIRM")
	@Column(name = "X_FIRMA", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrimaryKey() {
		return this.primaryKey;
	}

	@Override
	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Basic(fetch=FetchType.LAZY, optional = true )
	@Column(name = "B_INFORME", nullable = true )
	@Lob
	public byte[] getBInforme() {
		return this.bInforme;
	}

	public void setBInforme(byte[] bInforme) {
		this.bInforme = bInforme;
	}

	@Column(name = "B_FIRMA", nullable = true)
	@Lob
	public byte[] getBFirma() {
		return this.bFirma;
	}

	public void setBFirma(byte[] bFirma) {
		this.bFirma = bFirma;
	}
	
	@Basic(fetch=FetchType.LAZY, optional = true )
	@Column(name = "B_NORMALIZADO", nullable = true )
	@Lob
	public byte[] getBInformeNormalizado() {
		return this.bInformeNormalizado;
	}

	public void setBInformeNormalizado(byte[] bInformeNormalizado) {
		this.bInformeNormalizado = bInformeNormalizado;
	}


	@Transient
	@Override
	public void setCcreated(String ccreated) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	@Override
	public void setFcreated(Date fcreated) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	@Override
	public void setCmodified(String cmodified) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	@Override
	public void setFmodified(Date fmodified) {
		// TODO Auto-generated method stub
		
	}

	@Transient
	@Override
	public String getCcreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	@Override
	public Date getFcreated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	@Override
	public String getCmodified() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transient
	@Override
	public Date getFmodified() {
		// TODO Auto-generated method stub
		return null;
	}
	


}