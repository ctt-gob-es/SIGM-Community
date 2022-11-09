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
 * Represents a document for v1 web services.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
public class DocumentoWS implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String CHASH;
	private java.lang.String DARCHIVO;
	private java.lang.String DMIME;
	private java.util.Calendar FCREACION;
	private java.util.Calendar FMODIFICACION;
	private java.math.BigDecimal NTAMANIO;
	private java.lang.String PETCHASH;
	private java.lang.String TDOCCTIPODOCUMENTO;

	/**
	 * Sole constructor.
	 */
	public DocumentoWS() {
	}

	/**
	 * Gets the value of the CHASH property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getCHASH() {
		return CHASH;
	}

	/**
	 * Sets the value of the CHASH property.
	 * 
	 * @param CHASH
	 *            allowed object is {@link String }
	 */
	public void setCHASH(java.lang.String CHASH) {
		this.CHASH = CHASH;
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
	 * Gets the value of the DMIME property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDMIME() {
		return DMIME;
	}

	/**
	 * Sets the value of the DMIME property.
	 * 
	 * @param DMIME
	 *            allowed object is {@link String }
	 */
	public void setDMIME(java.lang.String DMIME) {
		this.DMIME = DMIME;
	}

	/**
	 * Gets the value of the FCREACION property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.util.Calendar getFCREACION() {
		return FCREACION;
	}

	/**
	 * Sets the value of the FCREACION property.
	 * 
	 * @param FCREACION
	 *            allowed object is {@link String }
	 */
	public void setFCREACION(java.util.Calendar FCREACION) {
		this.FCREACION = FCREACION;
	}

	/**
	 * Gets the value of the FMODIFICACION property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.util.Calendar getFMODIFICACION() {
		return FMODIFICACION;
	}

	/**
	 * Sets the value of the FMODIFICACION property.
	 * 
	 * @param FMODIFICACION
	 *            allowed object is {@link String }
	 */
	public void setFMODIFICACION(java.util.Calendar FMODIFICACION) {
		this.FMODIFICACION = FMODIFICACION;
	}

	/**
	 * Gets the value of the NTAMANIO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.math.BigDecimal getNTAMANIO() {
		return NTAMANIO;
	}

	/**
	 * Sets the value of the NTAMANIO property.
	 * 
	 * @param NTAMANIO
	 *            allowed object is {@link String }
	 */
	public void setNTAMANIO(java.math.BigDecimal NTAMANIO) {
		this.NTAMANIO = NTAMANIO;
	}

	/**
	 * Gets the value of the PETCHASH property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getPETCHASH() {
		return PETCHASH;
	}

	/**
	 * Sets the value of the PETCHASH property.
	 * 
	 * @param PETCHASH
	 *            allowed object is {@link String }
	 */
	public void setPETCHASH(java.lang.String PETCHASH) {
		this.PETCHASH = PETCHASH;
	}

	/**
	 * Gets the value of the TDOCCTIPODOCUMENTO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getTDOCCTIPODOCUMENTO() {
		return TDOCCTIPODOCUMENTO;
	}

	/**
	 * Sets the value of the TDOCCTIPODOCUMENTO property.
	 * 
	 * @param TDOCCTIPODOCUMENTO
	 *            allowed object is {@link String }
	 */
	public void setTDOCCTIPODOCUMENTO(java.lang.String TDOCCTIPODOCUMENTO) {
		this.TDOCCTIPODOCUMENTO = TDOCCTIPODOCUMENTO;
	}

	@Override
	public String toString() {
		return CHASH;
	}
}
