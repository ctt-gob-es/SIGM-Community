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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.sim.peticion.Mensajes2;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Status" type="{http://misim.redsara.es/misim-bus-webapp/respuesta}responseStatusType"/>
 *         &lt;element name="Lote" type="{http://misim.redsara.es/misim-bus-webapp/respuesta}lote" minOccurs="0"/>
 *         &lt;element name="Mensajes" type="{http://misim.redsara.es/misim-bus-webapp/respuesta}mensajes" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "status",
    "lote",
    "mensajes"
})
@XmlRootElement(name = "Respuesta", namespace = "http://misim.redsara.es/misim-bus-webapp/respuesta")
public class Respuesta {

    @XmlElement(name = "Status", namespace = "http://misim.redsara.es/misim-bus-webapp/respuesta", required = true)
    protected ResponseStatusType status;
    @XmlElement(name = "Lote", namespace = "http://misim.redsara.es/misim-bus-webapp/respuesta")
    protected Lote lote;
    @XmlElement(name = "Mensajes", namespace = "http://misim.redsara.es/misim-bus-webapp/respuesta")
    protected List<Mensajes2> mensajes;

    /**
     * Obtiene el valor de la propiedad status.
     * 
     * @return
     *     possible object is
     *     {@link ResponseStatusType }
     *     
     */
    public ResponseStatusType getStatus() {
        return status;
    }

    /**
     * Define el valor de la propiedad status.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseStatusType }
     *     
     */
    public void setStatus(ResponseStatusType value) {
        this.status = value;
    }

    /**
     * Obtiene el valor de la propiedad lote.
     * 
     * @return
     *     possible object is
     *     {@link Lote }
     *     
     */
    public Lote getLote() {
        return lote;
    }

    /**
     * Define el valor de la propiedad lote.
     * 
     * @param value
     *     allowed object is
     *     {@link Lote }
     *     
     */
    public void setLote(Lote value) {
        this.lote = value;
    }

    /**
     * Gets the value of the mensajes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mensajes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMensajes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mensajes2 }
     * 
     * 
     */
    public List<Mensajes2> getMensajes() {
        if (mensajes == null) {
            mensajes = new ArrayList<Mensajes2>();
        }
        return this.mensajes;
    }

}
