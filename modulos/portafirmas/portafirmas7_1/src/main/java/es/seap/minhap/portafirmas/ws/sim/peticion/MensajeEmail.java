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


package es.seap.minhap.portafirmas.ws.sim.peticion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para MensajeEmail complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="MensajeEmail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Asunto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Cuerpo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="1000000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Origen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Modo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Adjuntos" type="{http://misim.redsara.es/misim-bus-webapp/peticion}Adjuntos" minOccurs="0"/>
 *         &lt;element name="DestinatariosMail" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatariosMail"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MensajeEmail", propOrder = {
    "asunto",
    "cuerpo",
    "origen",
    "modo",
    "adjuntos",
    "destinatariosMail"
})
public class MensajeEmail {

    @XmlElement(name = "Asunto", required = true)
    protected String asunto;
    @XmlElement(name = "Cuerpo", required = true)
    protected String cuerpo;
    @XmlElement(name = "Origen")
    protected String origen;
    @XmlElement(name = "Modo")
    protected String modo;
    @XmlElement(name = "Adjuntos")
    protected Adjuntos adjuntos;
    @XmlElement(name = "DestinatariosMail", required = true)
    protected DestinatariosMail destinatariosMail;

    /**
     * Obtiene el valor de la propiedad asunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Define el valor de la propiedad asunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsunto(String value) {
        this.asunto = value;
    }

    /**
     * Obtiene el valor de la propiedad cuerpo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCuerpo() {
        return cuerpo;
    }

    /**
     * Define el valor de la propiedad cuerpo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCuerpo(String value) {
        this.cuerpo = value;
    }

    /**
     * Obtiene el valor de la propiedad origen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Define el valor de la propiedad origen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

    /**
     * Obtiene el valor de la propiedad modo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModo() {
        return modo;
    }

    /**
     * Define el valor de la propiedad modo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModo(String value) {
        this.modo = value;
    }

    /**
     * Obtiene el valor de la propiedad adjuntos.
     * 
     * @return
     *     possible object is
     *     {@link Adjuntos }
     *     
     */
    public Adjuntos getAdjuntos() {
        return adjuntos;
    }

    /**
     * Define el valor de la propiedad adjuntos.
     * 
     * @param value
     *     allowed object is
     *     {@link Adjuntos }
     *     
     */
    public void setAdjuntos(Adjuntos value) {
        this.adjuntos = value;
    }

    /**
     * Obtiene el valor de la propiedad destinatariosMail.
     * 
     * @return
     *     possible object is
     *     {@link DestinatariosMail }
     *     
     */
    public DestinatariosMail getDestinatariosMail() {
        return destinatariosMail;
    }

    /**
     * Define el valor de la propiedad destinatariosMail.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinatariosMail }
     *     
     */
    public void setDestinatariosMail(DestinatariosMail value) {
        this.destinatariosMail = value;
    }

}
