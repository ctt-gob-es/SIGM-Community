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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for enhancedUserJobAssociated complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enhancedUserJobAssociated">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="enhancedUser" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}enhancedUser"/>
 *         &lt;element name="enhancedJob" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}enhancedJob"/>
 *         &lt;element name="fstart" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fend" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedUserJobAssociated", propOrder = {
    "enhancedUser",
    "enhancedJob",
    "fstart",
    "fend"
})
public class EnhancedUserJobAssociated {

    @XmlElement(required = true)
    protected EnhancedUser enhancedUser;
    @XmlElement(required = true)
    protected EnhancedJob enhancedJob;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fstart;
    @XmlElementRef(name = "fend", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> fend;

    /**
     * Gets the value of the enhancedUser property.
     * 
     * @return
     *     possible object is
     *     {@link EnhancedUser }
     *     
     */
    public EnhancedUser getEnhancedUser() {
        return enhancedUser;
    }

    /**
     * Sets the value of the enhancedUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnhancedUser }
     *     
     */
    public void setEnhancedUser(EnhancedUser value) {
        this.enhancedUser = value;
    }

    /**
     * Gets the value of the enhancedJob property.
     * 
     * @return
     *     possible object is
     *     {@link EnhancedJob }
     *     
     */
    public EnhancedJob getEnhancedJob() {
        return enhancedJob;
    }

    /**
     * Sets the value of the enhancedJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link EnhancedJob }
     *     
     */
    public void setEnhancedJob(EnhancedJob value) {
        this.enhancedJob = value;
    }

    /**
     * Gets the value of the fstart property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFstart() {
        return fstart;
    }

    /**
     * Sets the value of the fstart property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFstart(XMLGregorianCalendar value) {
        this.fstart = value;
    }

    /**
     * Gets the value of the fend property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getFend() {
        return fend;
    }

    /**
     * Sets the value of the fend property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setFend(JAXBElement<XMLGregorianCalendar> value) {
        this.fend = ((JAXBElement<XMLGregorianCalendar> ) value);
    }

}
