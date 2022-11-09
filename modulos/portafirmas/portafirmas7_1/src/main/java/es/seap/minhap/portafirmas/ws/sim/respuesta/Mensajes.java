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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.sim.peticion.MensajeEmail;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajePush;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajeSMS;
import es.seap.minhap.portafirmas.ws.sim.peticion.MensajeWebPush;


/**
 * <p>Clase Java para Mensajes complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Mensajes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MensajeSMS" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajeSMS" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MensajeEmail" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajeEmail" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MensajePush" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajePush" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="MensajeWebPush" type="{http://misim.redsara.es/misim-bus-webapp/peticion}MensajeWebPush" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Mensajes", propOrder = {
    "mensajeSMS",
    "mensajeEmail",
    "mensajePush",
    "mensajeWebPush"
})
public class Mensajes {

    @XmlElement(name = "MensajeSMS")
    protected List<MensajeSMS> mensajeSMS;
    @XmlElement(name = "MensajeEmail")
    protected List<MensajeEmail> mensajeEmail;
    @XmlElement(name = "MensajePush")
    protected List<MensajePush> mensajePush;
    @XmlElement(name = "MensajeWebPush")
    protected List<MensajeWebPush> mensajeWebPush;

    /**
     * Gets the value of the mensajeSMS property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensajeSMS property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMensajeSMS().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MensajeSMS }
     * 
     * 
     */
    public List<MensajeSMS> getMensajeSMS() {
        if (mensajeSMS == null) {
            mensajeSMS = new ArrayList<MensajeSMS>();
        }
        return this.mensajeSMS;
    }

    /**
     * Gets the value of the mensajeEmail property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensajeEmail property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMensajeEmail().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MensajeEmail }
     * 
     * 
     */
    public List<MensajeEmail> getMensajeEmail() {
        if (mensajeEmail == null) {
            mensajeEmail = new ArrayList<MensajeEmail>();
        }
        return this.mensajeEmail;
    }

    /**
     * Gets the value of the mensajePush property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensajePush property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMensajePush().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MensajePush }
     * 
     * 
     */
    public List<MensajePush> getMensajePush() {
        if (mensajePush == null) {
            mensajePush = new ArrayList<MensajePush>();
        }
        return this.mensajePush;
    }

    /**
     * Gets the value of the mensajeWebPush property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensajeWebPush property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMensajeWebPush().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MensajeWebPush }
     * 
     * 
     */
    public List<MensajeWebPush> getMensajeWebPush() {
        if (mensajeWebPush == null) {
            mensajeWebPush = new ArrayList<MensajeWebPush>();
        }
        return this.mensajeWebPush;
    }

}
