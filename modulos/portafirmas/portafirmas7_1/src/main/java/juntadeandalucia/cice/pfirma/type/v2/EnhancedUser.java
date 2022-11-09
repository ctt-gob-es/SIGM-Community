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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enhancedUser complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enhancedUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}user" minOccurs="0"/>
 *         &lt;element name="enhancedUserJobInfo" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}enhancedUserJobInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedUser", propOrder = {
    "user",
    "enhancedUserJobInfo"
})
public class EnhancedUser {

    @XmlElementRef(name = "user", type = JAXBElement.class, required = false)
    protected JAXBElement<User> user;
    @XmlElementRef(name = "enhancedUserJobInfo", type = JAXBElement.class, required = false)
    protected JAXBElement<EnhancedUserJobInfo> enhancedUserJobInfo;

    /**
     * Gets the value of the user property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link User }{@code >}
     *     
     */
    public JAXBElement<User> getUser() {
        return user;
    }

    /**
     * Sets the value of the user property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link User }{@code >}
     *     
     */
    public void setUser(JAXBElement<User> value) {
        this.user = ((JAXBElement<User> ) value);
    }

    /**
     * Gets the value of the enhancedUserJobInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link EnhancedUserJobInfo }{@code >}
     *     
     */
    public JAXBElement<EnhancedUserJobInfo> getEnhancedUserJobInfo() {
        return enhancedUserJobInfo;
    }

    /**
     * Sets the value of the enhancedUserJobInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link EnhancedUserJobInfo }{@code >}
     *     
     */
    public void setEnhancedUserJobInfo(JAXBElement<EnhancedUserJobInfo> value) {
        this.enhancedUserJobInfo = ((JAXBElement<EnhancedUserJobInfo> ) value);
    }

}
