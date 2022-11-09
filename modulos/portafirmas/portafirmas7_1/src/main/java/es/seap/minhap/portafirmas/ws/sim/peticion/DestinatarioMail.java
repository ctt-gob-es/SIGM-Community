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
 * <p>Clase Java para DestinatarioMail complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DestinatarioMail">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DocUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdExterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Destinatarios" type="{http://misim.redsara.es/misim-bus-webapp/peticion}Destinatarios"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DestinatarioMail", propOrder = {
    "docUsuario",
    "idExterno",
    "destinatarios"
})
public class DestinatarioMail {

    @XmlElement(name = "DocUsuario")
    protected String docUsuario;
    @XmlElement(name = "IdExterno")
    protected String idExterno;
    @XmlElement(name = "Destinatarios", required = true)
    protected Destinatarios destinatarios;

    /**
     * Obtiene el valor de la propiedad docUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocUsuario() {
        return docUsuario;
    }

    /**
     * Define el valor de la propiedad docUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocUsuario(String value) {
        this.docUsuario = value;
    }

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
     * Obtiene el valor de la propiedad destinatarios.
     * 
     * @return
     *     possible object is
     *     {@link Destinatarios }
     *     
     */
    public Destinatarios getDestinatarios() {
        return destinatarios;
    }

    /**
     * Define el valor de la propiedad destinatarios.
     * 
     * @param value
     *     allowed object is
     *     {@link Destinatarios }
     *     
     */
    public void setDestinatarios(Destinatarios value) {
        this.destinatarios = value;
    }

}
