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

package es.seap.minhap.portafirmas.ws.legacy.bean;

/**
 * Represents a request deliver for v1 web services.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
public class EntregaWS implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String CESTADO;
	private java.lang.String CTRANSACTIONID;
	private java.lang.String DARCHIVO;
	private java.lang.String DESTCDNI;
	private java.lang.String DOCCHASH;
	private java.util.Calendar FESTADO;

	/**
	 * Sole constructor.
	 */
	public EntregaWS() {
	}

	/**
	 * Gets the value of the CESTADO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getCESTADO() {
		return CESTADO;
	}

	/**
	 * Sets the value of the CESTADO property.
	 * 
	 * @param CESTADO
	 *            allowed object is {@link String }
	 */
	public void setCESTADO(java.lang.String CESTADO) {
		this.CESTADO = CESTADO;
	}

	/**
	 * Gets the value of the CTRANSACTIONID property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getCTRANSACTIONID() {
		return CTRANSACTIONID;
	}

	/**
	 * Sets the value of the CTRANSACTIONID property.
	 * 
	 * @param CTRANSACTIONID
	 *            allowed object is {@link String }
	 */
	public void setCTRANSACTIONID(java.lang.String CTRANSACTIONID) {
		this.CTRANSACTIONID = CTRANSACTIONID;
	}

	/**
	 * Gets the value of the DARCHIVO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDARCHIVO() {
		return DARCHIVO;
	}

	/**
	 * Sets the value of the DARCHIVO property.
	 * 
	 * @param DARCHIVO
	 *            allowed object is {@link String }
	 */
	public void setDARCHIVO(java.lang.String DARCHIVO) {
		this.DARCHIVO = DARCHIVO;
	}

	/**
	 * Gets the value of the DESTCDNI property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDESTCDNI() {
		return DESTCDNI;
	}

	/**
	 * Sets the value of the DESTCDNI property.
	 * 
	 * @param DESTCDNI
	 *            allowed object is {@link String }
	 */
	public void setDESTCDNI(java.lang.String DESTCDNI) {
		this.DESTCDNI = DESTCDNI;
	}

	/**
	 * Gets the value of the DOCCHASH property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDOCCHASH() {
		return DOCCHASH;
	}

	/**
	 * Sets the value of the DOCCHASH property.
	 * 
	 * @param DOCCHASH
	 *            allowed object is {@link String }
	 */
	public void setDOCCHASH(java.lang.String DOCCHASH) {
		this.DOCCHASH = DOCCHASH;
	}

	/**
	 * Gets the value of the FESTADO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.util.Calendar getFESTADO() {
		return FESTADO;
	}

	/**
	 * Sets the value of the FESTADO property.
	 * 
	 * @param FESTADO
	 *            allowed object is {@link String }
	 */
	public void setFESTADO(java.util.Calendar FESTADO) {
		this.FESTADO = FESTADO;
	}

	@Override
	public String toString() {
		return DOCCHASH + ":" + DESTCDNI;
	}
}
