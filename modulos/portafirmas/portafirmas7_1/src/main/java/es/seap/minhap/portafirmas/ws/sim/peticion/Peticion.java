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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.sim.respuesta.Mensajes;


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
 *         &lt;element name="Usuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="NombreLote" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Servicio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CodSia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodOrganismo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CodOrganismoPagadorSMS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Mensajes" type="{http://misim.redsara.es/misim-bus-webapp/peticion}Mensajes"/>
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
    "usuario",
    "password",
    "nombreLote",
    "servicio",
    "codSia",
    "codOrganismo",
    "codOrganismoPagadorSMS",
    "mensajes"
})
@XmlRootElement(name = "Peticion")
public class Peticion {

    @XmlElement(name = "Usuario", required = true)
    protected String usuario;
    @XmlElement(name = "Password", required = true)
    protected String password;
    @XmlElement(name = "NombreLote", required = true)
    protected String nombreLote;
    @XmlElement(name = "Servicio", required = true)
    protected String servicio;
    @XmlElement(name = "CodSia")
    protected String codSia;
    @XmlElement(name = "CodOrganismo")
    protected String codOrganismo;
    @XmlElement(name = "CodOrganismoPagadorSMS")
    protected String codOrganismoPagadorSMS;
    @XmlElement(name = "Mensajes", required = true)
    protected Mensajes mensajes;

    /**
     * Obtiene el valor de la propiedad usuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Define el valor de la propiedad usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Obtiene el valor de la propiedad password.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Define el valor de la propiedad password.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreLote.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreLote() {
        return nombreLote;
    }

    /**
     * Define el valor de la propiedad nombreLote.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreLote(String value) {
        this.nombreLote = value;
    }

    /**
     * Obtiene el valor de la propiedad servicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServicio() {
        return servicio;
    }

    /**
     * Define el valor de la propiedad servicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServicio(String value) {
        this.servicio = value;
    }

    /**
     * Obtiene el valor de la propiedad codSia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSia() {
        return codSia;
    }

    /**
     * Define el valor de la propiedad codSia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSia(String value) {
        this.codSia = value;
    }

    /**
     * Obtiene el valor de la propiedad codOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOrganismo() {
        return codOrganismo;
    }

    /**
     * Define el valor de la propiedad codOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOrganismo(String value) {
        this.codOrganismo = value;
    }

    /**
     * Obtiene el valor de la propiedad codOrganismoPagadorSMS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodOrganismoPagadorSMS() {
        return codOrganismoPagadorSMS;
    }

    /**
     * Define el valor de la propiedad codOrganismoPagadorSMS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodOrganismoPagadorSMS(String value) {
        this.codOrganismoPagadorSMS = value;
    }

    /**
     * Obtiene el valor de la propiedad mensajes.
     * 
     * @return
     *     possible object is
     *     {@link Mensajes }
     *     
     */
    public Mensajes getMensajes() {
        return mensajes;
    }

    /**
     * Define el valor de la propiedad mensajes.
     * 
     * @param value
     *     allowed object is
     *     {@link Mensajes }
     *     
     */
    public void setMensajes(Mensajes value) {
        this.mensajes = value;
    }

}
