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
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for signer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="signer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="userJob" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}userJob" minOccurs="0"/>
 *         &lt;element name="state" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}state" minOccurs="0"/>
 *         &lt;element name="fstate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signer", propOrder = {
    "userJob",
    "state",
    "fstate"
})
public class Signer {

    @XmlElementRef(name = "userJob", type = JAXBElement.class, required = false)
    protected JAXBElement<UserJob> userJob;
    @XmlElementRef(name = "state", type = JAXBElement.class, required = false)
    protected JAXBElement<State> state;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fstate;

    /**
     * Gets the value of the userJob property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link UserJob }{@code >}
     *     
     */
    public JAXBElement<UserJob> getUserJob() {
        return userJob;
    }

    /**
     * Sets the value of the userJob property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link UserJob }{@code >}
     *     
     */
    public void setUserJob(JAXBElement<UserJob> value) {
        this.userJob = ((JAXBElement<UserJob> ) value);
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link State }{@code >}
     *     
     */
    public JAXBElement<State> getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link State }{@code >}
     *     
     */
    public void setState(JAXBElement<State> value) {
        this.state = ((JAXBElement<State> ) value);
    }

    /**
     * Gets the value of the fstate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFstate() {
        return fstate;
    }

    /**
     * Sets the value of the fstate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFstate(XMLGregorianCalendar value) {
        this.fstate = value;
    }

}
