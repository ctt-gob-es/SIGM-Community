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
 * Represents an user for v1 web services.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
public class UsuarioWS implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String CDNI;
	private java.lang.String DAPELL1;
	private java.lang.String DAPELL2;
	private java.lang.String DNOMBRE;

	/**
	 * Sole constructor.
	 */
	public UsuarioWS() {
	}

	/**
	 * Gets the value of the CDNI property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getCDNI() {
		return CDNI;
	}

	/**
	 * Sets the value of the CDNI property.
	 * 
	 * @param CDNI
	 *            allowed object is {@link String }
	 */
	public void setCDNI(java.lang.String CDNI) {
		this.CDNI = CDNI;
	}

	/**
	 * Gets the value of the DAPELL1 property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDAPELL1() {
		return DAPELL1;
	}

	/**
	 * Sets the value of the DAPELL1 property.
	 * 
	 * @param DAPELL1
	 *            allowed object is {@link String }
	 */
	public void setDAPELL1(java.lang.String DAPELL1) {
		this.DAPELL1 = DAPELL1;
	}

	/**
	 * Gets the value of the DAPELL2 property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDAPELL2() {
		return DAPELL2;
	}

	/**
	 * Sets the value of the DAPELL2 property.
	 * 
	 * @param DAPELL2
	 *            allowed object is {@link String }
	 */
	public void setDAPELL2(java.lang.String DAPELL2) {
		this.DAPELL2 = DAPELL2;
	}

	/**
	 * Gets the value of the DNOMBRE property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDNOMBRE() {
		return DNOMBRE;
	}

	/**
	 * Sets the value of the DNOMBRE property.
	 * 
	 * @param DNOMBRE
	 *            allowed object is {@link String }
	 */
	public void setDNOMBRE(java.lang.String DNOMBRE) {
		this.DNOMBRE = DNOMBRE;
	}

	@Override
	public String toString() {
		return CDNI;
	}

}
