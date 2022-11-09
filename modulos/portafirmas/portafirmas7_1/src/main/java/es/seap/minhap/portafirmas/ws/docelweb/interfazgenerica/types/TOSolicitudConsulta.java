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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TOSolicitudConsulta complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TOSolicitudConsulta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="solicitante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="prioridad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idsDocumentos" type="{http://www.w3.org/2001/XMLSchema}long" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="login_firmante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="estadoSolicitud" type="{http://ip2.docelweb.wsInterfazGenerica/types/}TOSolicitudEstado"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TOSolicitudConsulta", propOrder = {
    "solicitante",
    "prioridad",
    "idsDocumentos",
    "descripcion",
    "loginFirmante",
    "estadoSolicitud"
})
public class TOSolicitudConsulta {

    @XmlElement(required = true, nillable = true)
    protected String solicitante;
    @XmlElement(required = true, nillable = true)
    protected String prioridad;
    @XmlElement(nillable = true)
    protected List<Long> idsDocumentos;
    @XmlElement(required = true, nillable = true)
    protected String descripcion;
    @XmlElement(name = "login_firmante", required = true, nillable = true)
    protected String loginFirmante;
    @XmlElement(required = true, nillable = true)
    protected TOSolicitudEstado estadoSolicitud;

    /**
     * Gets the value of the solicitante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolicitante() {
        return solicitante;
    }

    /**
     * Sets the value of the solicitante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolicitante(String value) {
        this.solicitante = value;
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
     * Gets the value of the idsDocumentos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idsDocumentos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdsDocumentos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Long }
     * 
     * 
     */
    public List<Long> getIdsDocumentos() {
        if (idsDocumentos == null) {
            idsDocumentos = new ArrayList<Long>();
        }
        return this.idsDocumentos;
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
     * Gets the value of the estadoSolicitud property.
     * 
     * @return
     *     possible object is
     *     {@link TOSolicitudEstado }
     *     
     */
    public TOSolicitudEstado getEstadoSolicitud() {
        return estadoSolicitud;
    }

    /**
     * Sets the value of the estadoSolicitud property.
     * 
     * @param value
     *     allowed object is
     *     {@link TOSolicitudEstado }
     *     
     */
    public void setEstadoSolicitud(TOSolicitudEstado value) {
        this.estadoSolicitud = value;
    }

}
