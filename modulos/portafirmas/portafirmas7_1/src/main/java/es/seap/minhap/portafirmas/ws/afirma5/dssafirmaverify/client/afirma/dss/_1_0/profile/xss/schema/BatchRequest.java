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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.AnyType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}OptionalInputs" minOccurs="0"/>
 *         &lt;element ref="{urn:afirma:dss:1.0:profile:XSS:schema}Requests"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Profile" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "optionalInputs",
    "requests"
})
@XmlRootElement(name = "BatchRequest")
public class BatchRequest {

    @XmlElement(name = "OptionalInputs", namespace = "urn:oasis:names:tc:dss:1.0:core:schema")
    protected AnyType optionalInputs;
    @XmlElement(name = "Requests", required = true)
    protected Requests requests;
    @XmlAttribute(name = "RequestID")
    protected String requestID;
    @XmlAttribute(name = "Profile")
    @XmlSchemaType(name = "anyURI")
    protected String profile;
    @XmlAttribute(name = "Type")
    @XmlSchemaType(name = "anyURI")
    protected String type;

    /**
     * Gets the value of the optionalInputs property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getOptionalInputs() {
        return optionalInputs;
    }

    /**
     * Sets the value of the optionalInputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setOptionalInputs(AnyType value) {
        this.optionalInputs = value;
    }

    /**
     * Gets the value of the requests property.
     * 
     * @return
     *     possible object is
     *     {@link Requests }
     *     
     */
    public Requests getRequests() {
        return requests;
    }

    /**
     * Sets the value of the requests property.
     * 
     * @param value
     *     allowed object is
     *     {@link Requests }
     *     
     */
    public void setRequests(Requests value) {
        this.requests = value;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfile(String value) {
        this.profile = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
