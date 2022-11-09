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
 * Represents a request for v1 web services.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
public class PeticionWS implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private java.lang.String APLCAPLICACION;
	private java.lang.String CHASH;
	private java.lang.String DASUNTO;
	private java.lang.String DREFERENCIA;
	private java.lang.String DREMITENTEEMAIL;
	private java.lang.String DREMITENTEMOVIL;
	private java.lang.String DREMITENTENOMBRE;
	private java.util.Calendar FCADUCIDAD;
	private java.util.Calendar FENTRADA;
	private java.util.Calendar FINICIO;
	private java.lang.String LFIRMAENCASCADA;
	private java.lang.String LFIRMAORDENADA;
	private java.lang.String LNOTIFICAAVISO;
	private java.lang.String LNOTIFICAEMAIL;
	private java.lang.String LNOTIFICAMOVIL;
	private java.math.BigDecimal NPRIORIDAD;
	private java.lang.String PETCHASH;
	private java.lang.String TTEXTO;
	private java.lang.String USUCDNI;

	/**
	 * Sole constructor.
	 */
	public PeticionWS() {
	}

	/**
	 * Gets the value of the APLCAPLICACION property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getAPLCAPLICACION() {
		return APLCAPLICACION;
	}

	/**
	 * Sets the value of the APLCAPLICACION property.
	 * 
	 * @param APLCAPLICACION
	 *            allowed object is {@link String }
	 */
	public void setAPLCAPLICACION(java.lang.String APLCAPLICACION) {
		if (APLCAPLICACION != null) {
			this.APLCAPLICACION = APLCAPLICACION;
		} else {
			this.APLCAPLICACION = "";
		}
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
		if (CHASH != null) {
			this.CHASH = CHASH;
		} else {
			this.CHASH = CHASH;
		}
	}

	/**
	 * Gets the value of the DASUNTO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDASUNTO() {
		return DASUNTO;
	}

	/**
	 * Sets the value of the DASUNTO property.
	 * 
	 * @param DASUNTO
	 *            allowed object is {@link String }
	 */
	public void setDASUNTO(java.lang.String DASUNTO) {
		this.DASUNTO = DASUNTO;
	}

	/**
	 * Gets the value of the DREFERENCIA property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDREFERENCIA() {
		return DREFERENCIA;
	}

	/**
	 * Sets the value of the DREFERENCIA property.
	 * 
	 * @param DREFERENCIA
	 *            allowed object is {@link String }
	 */
	public void setDREFERENCIA(java.lang.String DREFERENCIA) {
		this.DREFERENCIA = DREFERENCIA;
	}

	/**
	 * Gets the value of the DREMITENTEEMAIL property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDREMITENTEEMAIL() {
		return DREMITENTEEMAIL;
	}

	/**
	 * Sets the value of the DREMITENTEEMAIL property.
	 * 
	 * @param DREMITENTEEMAIL
	 *            allowed object is {@link String }
	 */
	public void setDREMITENTEEMAIL(java.lang.String DREMITENTEEMAIL) {
		this.DREMITENTEEMAIL = DREMITENTEEMAIL;
	}

	/**
	 * Gets the value of the DREMITENTEMOVIL property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDREMITENTEMOVIL() {
		return DREMITENTEMOVIL;
	}

	/**
	 * Sets the value of the DREMITENTEMOVIL property.
	 * 
	 * @param DREMITENTEMOVIL
	 *            allowed object is {@link String }
	 */
	public void setDREMITENTEMOVIL(java.lang.String DREMITENTEMOVIL) {
		this.DREMITENTEMOVIL = DREMITENTEMOVIL;
	}

	/**
	 * Gets the value of the DREMITENTENOMBRE property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getDREMITENTENOMBRE() {
		return DREMITENTENOMBRE;
	}

	/**
	 * Sets the value of the DREMITENTENOMBRE property.
	 * 
	 * @param DREMITENTENOMBRE
	 *            allowed object is {@link String }
	 */
	public void setDREMITENTENOMBRE(java.lang.String DREMITENTENOMBRE) {
		this.DREMITENTENOMBRE = DREMITENTENOMBRE;
	}

	/**
	 * Gets the value of the FCADUCIDAD property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.util.Calendar getFCADUCIDAD() {
		return FCADUCIDAD;
	}

	/**
	 * Sets the value of the FCADUCIDAD property.
	 * 
	 * @param FCADUCIDAD
	 *            allowed object is {@link String }
	 */
	public void setFCADUCIDAD(java.util.Calendar FCADUCIDAD) {
		this.FCADUCIDAD = FCADUCIDAD;
	}

	/**
	 * Gets the value of the FENTRADA property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.util.Calendar getFENTRADA() {
		return FENTRADA;
	}

	/**
	 * Sets the value of the FENTRADA property.
	 * 
	 * @param FENTRADA
	 *            allowed object is {@link String }
	 */
	public void setFENTRADA(java.util.Calendar FENTRADA) {
		this.FENTRADA = FENTRADA;
	}

	/**
	 * Gets the value of the FINICIO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.util.Calendar getFINICIO() {
		return FINICIO;
	}

	/**
	 * Sets the value of the FINICIO property.
	 * 
	 * @param FINICIO
	 *            allowed object is {@link String }
	 */
	public void setFINICIO(java.util.Calendar FINICIO) {
		this.FINICIO = FINICIO;
	}

	/**
	 * Gets the value of the LFIRMAENCASCADA property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getLFIRMAENCASCADA() {
		return LFIRMAENCASCADA;
	}

	/**
	 * Sets the value of the LFIRMAENCASCADA property.
	 * 
	 * @param LFIRMAENCASCADA
	 *            allowed object is {@link String }
	 */
	public void setLFIRMAENCASCADA(java.lang.String LFIRMAENCASCADA) {
		this.LFIRMAENCASCADA = LFIRMAENCASCADA;
	}

	/**
	 * Gets the value of the LFIRMAORDENADA property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getLFIRMAORDENADA() {
		return LFIRMAORDENADA;
	}

	/**
	 * Sets the value of the LFIRMAORDENADA property.
	 * 
	 * @param LFIRMAORDENADA
	 *            allowed object is {@link String }
	 */
	public void setLFIRMAORDENADA(java.lang.String LFIRMAORDENADA) {
		this.LFIRMAORDENADA = LFIRMAORDENADA;
	}

	/**
	 * Gets the value of the LNOTIFICAAVISO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getLNOTIFICAAVISO() {
		return LNOTIFICAAVISO;
	}

	/**
	 * Sets the value of the LNOTIFICAAVISO property.
	 * 
	 * @param LNOTIFICAAVISO
	 *            allowed object is {@link String }
	 */
	public void setLNOTIFICAAVISO(java.lang.String LNOTIFICAAVISO) {
		this.LNOTIFICAAVISO = LNOTIFICAAVISO;
	}

	/**
	 * Gets the value of the LNOTIFICAEMAIL property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getLNOTIFICAEMAIL() {
		return LNOTIFICAEMAIL;
	}

	/**
	 * Sets the value of the LNOTIFICAEMAIL property.
	 * 
	 * @param LNOTIFICAEMAIL
	 *            allowed object is {@link String }
	 */
	public void setLNOTIFICAEMAIL(java.lang.String LNOTIFICAEMAIL) {
		this.LNOTIFICAEMAIL = LNOTIFICAEMAIL;
	}

	/**
	 * Gets the value of the LNOTIFICAMOVIL property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getLNOTIFICAMOVIL() {
		return LNOTIFICAMOVIL;
	}

	/**
	 * Sets the value of the LNOTIFICAMOVIL property.
	 * 
	 * @param LNOTIFICAMOVIL
	 *            allowed object is {@link String }
	 */
	public void setLNOTIFICAMOVIL(java.lang.String LNOTIFICAMOVIL) {
		this.LNOTIFICAMOVIL = LNOTIFICAMOVIL;
	}

	/**
	 * Gets the value of the NPRIORIDAD property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.math.BigDecimal getNPRIORIDAD() {
		return NPRIORIDAD;
	}

	/**
	 * Sets the value of the NPRIORIDAD property.
	 * 
	 * @param NPRIORIDAD
	 *            allowed object is {@link String }
	 */
	public void setNPRIORIDAD(java.math.BigDecimal NPRIORIDAD) {
		this.NPRIORIDAD = NPRIORIDAD;
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
	 * Gets the value of the TTEXTO property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getTTEXTO() {
		return TTEXTO;
	}

	/**
	 * Sets the value of the TTEXTO property.
	 * 
	 * @param TTEXTO
	 *            allowed object is {@link String }
	 */
	public void setTTEXTO(java.lang.String TTEXTO) {
		this.TTEXTO = TTEXTO;
	}

	/**
	 * Gets the value of the USUCDNI property.
	 * 
	 * @return possible object is {@link String }
	 */
	public java.lang.String getUSUCDNI() {
		return USUCDNI;
	}

	/**
	 * Sets the value of the USUCDNI property.
	 * 
	 * @param USUCDNI
	 *            allowed object is {@link String }
	 */
	public void setUSUCDNI(java.lang.String USUCDNI) {
		this.USUCDNI = USUCDNI;
	}

	@Override
	public String toString() {
		return CHASH;
	}

}
