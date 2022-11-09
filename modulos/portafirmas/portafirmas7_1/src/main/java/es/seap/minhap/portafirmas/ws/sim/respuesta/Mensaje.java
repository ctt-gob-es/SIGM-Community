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


package es.seap.minhap.portafirmas.ws.sim.respuesta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mensaje complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mensaje">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idExterno" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="idMensaje" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ErrorMensaje" type="{http://misim.redsara.es/misim-bus-webapp/respuesta}responseStatusType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mensaje", namespace = "http://misim.redsara.es/misim-bus-webapp/respuesta", propOrder = {
    "idExterno",
    "idMensaje",
    "errorMensaje"
})
public class Mensaje {

    @XmlElement(required = true)
    protected String idExterno;
    @XmlElement(required = true)
    protected String idMensaje;
    @XmlElement(name = "ErrorMensaje")
    protected ResponseStatusType errorMensaje;

    /**
     * Obtiene el valor de la propiedad idExterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdExterno() {
        return idExterno;
    }

    /**
     * Define el valor de la propiedad idExterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdExterno(String value) {
        this.idExterno = value;
    }

    /**
     * Obtiene el valor de la propiedad idMensaje.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdMensaje() {
        return idMensaje;
    }

    /**
     * Define el valor de la propiedad idMensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdMensaje(String value) {
        this.idMensaje = value;
    }

    /**
     * Obtiene el valor de la propiedad errorMensaje.
     * 
     * @return
     *     possible object is
     *     {@link ResponseStatusType }
     *     
     */
    public ResponseStatusType getErrorMensaje() {
        return errorMensaje;
    }

    /**
     * Define el valor de la propiedad errorMensaje.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseStatusType }
     *     
     */
    public void setErrorMensaje(ResponseStatusType value) {
        this.errorMensaje = value;
    }

}
