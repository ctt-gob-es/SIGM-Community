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
 * <p>Clase Java para MensajePush complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="MensajePush">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Titulo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Cuerpo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Icono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Sonido" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DestinatariosPush" type="{http://misim.redsara.es/misim-bus-webapp/peticion}DestinatariosPush" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MensajePush", propOrder = {
    "titulo",
    "cuerpo",
    "icono",
    "sonido",
    "destinatariosPush"
})
public class MensajePush {

    @XmlElement(name = "Titulo", required = true)
    protected String titulo;
    @XmlElement(name = "Cuerpo", required = true)
    protected String cuerpo;
    @XmlElement(name = "Icono")
    protected String icono;
    @XmlElement(name = "Sonido")
    protected String sonido;
    @XmlElement(name = "DestinatariosPush")
    protected DestinatariosPush destinatariosPush;

    /**
     * Obtiene el valor de la propiedad titulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define el valor de la propiedad titulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
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
     * Obtiene el valor de la propiedad icono.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIcono() {
        return icono;
    }

    /**
     * Define el valor de la propiedad icono.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIcono(String value) {
        this.icono = value;
    }

    /**
     * Obtiene el valor de la propiedad sonido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSonido() {
        return sonido;
    }

    /**
     * Define el valor de la propiedad sonido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSonido(String value) {
        this.sonido = value;
    }

    /**
     * Obtiene el valor de la propiedad destinatariosPush.
     * 
     * @return
     *     possible object is
     *     {@link DestinatariosPush }
     *     
     */
    public DestinatariosPush getDestinatariosPush() {
        return destinatariosPush;
    }

    /**
     * Define el valor de la propiedad destinatariosPush.
     * 
     * @param value
     *     allowed object is
     *     {@link DestinatariosPush }
     *     
     */
    public void setDestinatariosPush(DestinatariosPush value) {
        this.destinatariosPush = value;
    }

}
