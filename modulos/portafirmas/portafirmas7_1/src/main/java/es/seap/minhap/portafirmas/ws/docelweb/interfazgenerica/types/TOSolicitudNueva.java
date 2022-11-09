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


package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TOSolicitudNueva complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TOSolicitudNueva">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="version_firma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prioridad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firma_multiple" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipo_firma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fecha_limite" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="login_firmante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombre_solicitante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TOSolicitudNueva", propOrder = {
    "versionFirma",
    "prioridad",
    "firmaMultiple",
    "tipoFirma",
    "fechaLimite",
    "descripcion",
    "loginFirmante",
    "nombreSolicitante"
})
public class TOSolicitudNueva {

    @XmlElement(name = "version_firma", required = true, nillable = true)
    protected String versionFirma;
    @XmlElement(required = true, nillable = true)
    protected String prioridad;
    @XmlElement(name = "firma_multiple", required = true, nillable = true)
    protected String firmaMultiple;
    @XmlElement(name = "tipo_firma", required = true, nillable = true)
    protected String tipoFirma;
    @XmlElement(name = "fecha_limite", required = true, nillable = true)
    protected String fechaLimite;
    @XmlElement(required = true, nillable = true)
    protected String descripcion;
    @XmlElement(name = "login_firmante", required = true, nillable = true)
    protected String loginFirmante;
    @XmlElement(name = "nombre_solicitante", required = true, nillable = true)
    protected String nombreSolicitante;

    /**
     * Gets the value of the versionFirma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionFirma() {
        return versionFirma;
    }

    /**
     * Sets the value of the versionFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionFirma(String value) {
        this.versionFirma = value;
    }

    /**
     * Gets the value of the prioridad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Sets the value of the prioridad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrioridad(String value) {
        this.prioridad = value;
    }

    /**
     * Gets the value of the firmaMultiple property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirmaMultiple() {
        return firmaMultiple;
    }

    /**
     * Sets the value of the firmaMultiple property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirmaMultiple(String value) {
        this.firmaMultiple = value;
    }

    /**
     * Gets the value of the tipoFirma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoFirma() {
        return tipoFirma;
    }

    /**
     * Sets the value of the tipoFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoFirma(String value) {
        this.tipoFirma = value;
    }

    /**
     * Gets the value of the fechaLimite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaLimite() {
        return fechaLimite;
    }

    /**
     * Sets the value of the fechaLimite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaLimite(String value) {
        this.fechaLimite = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Gets the value of the loginFirmante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoginFirmante() {
        return loginFirmante;
    }

    /**
     * Sets the value of the loginFirmante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoginFirmante(String value) {
        this.loginFirmante = value;
    }

    /**
     * Gets the value of the nombreSolicitante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    /**
     * Sets the value of the nombreSolicitante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSolicitante(String value) {
        this.nombreSolicitante = value;
    }

}
