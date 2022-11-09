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


package es.seap.minhap.portafirmas.business.ws.clients.autentica;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para LdapPositionWS complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="LdapPositionWS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="dir4DirCenCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4JobCentreCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4OrganizationCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="dir4Title" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LdapPositionWS", namespace = "http://obj.dir4.ws.sag.es", propOrder = {
    "dir4DirCenCode",
    "dir4JobCentreCode",
    "dir4OrganizationCode",
    "dir4Title"
})
public class LdapPositionWS {

    @XmlElement(required = true, nillable = true)
    protected String dir4DirCenCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4JobCentreCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4OrganizationCode;
    @XmlElement(required = true, nillable = true)
    protected String dir4Title;

    /**
     * Obtiene el valor de la propiedad dir4DirCenCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4DirCenCode() {
        return dir4DirCenCode;
    }

    /**
     * Define el valor de la propiedad dir4DirCenCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4DirCenCode(String value) {
        this.dir4DirCenCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4JobCentreCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4JobCentreCode() {
        return dir4JobCentreCode;
    }

    /**
     * Define el valor de la propiedad dir4JobCentreCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4JobCentreCode(String value) {
        this.dir4JobCentreCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4OrganizationCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4OrganizationCode() {
        return dir4OrganizationCode;
    }

    /**
     * Define el valor de la propiedad dir4OrganizationCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4OrganizationCode(String value) {
        this.dir4OrganizationCode = value;
    }

    /**
     * Obtiene el valor de la propiedad dir4Title.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir4Title() {
        return dir4Title;
    }

    /**
     * Define el valor de la propiedad dir4Title.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir4Title(String value) {
        this.dir4Title = value;
    }

}
