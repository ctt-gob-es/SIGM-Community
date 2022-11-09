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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema.BatchResponseType;


/**
 * <p>Java class for ResponseBaseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResponseBaseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}Result"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}OptionalOutputs" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="RequestID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Profile" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResponseBaseType", propOrder = {
    "result",
    "optionalOutputs"
})
@XmlSeeAlso({
    BatchResponseType.class,
    SignResponse.class
})
public class ResponseBaseType {

    @XmlElement(name = "Result", required = true)
    protected Result result;
    @XmlElement(name = "OptionalOutputs")
    protected AnyType optionalOutputs;
    @XmlAttribute(name = "RequestID")
    protected String requestID;
    @XmlAttribute(name = "Profile", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String profile;

    /**
     * Gets the value of the result property.
     * 
     * @return
     *     possible object is
     *     {@link Result }
     *     
     */
    public Result getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     * @param value
     *     allowed object is
     *     {@link Result }
     *     
     */
    public void setResult(Result value) {
        this.result = value;
    }

    /**
     * Gets the value of the optionalOutputs property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getOptionalOutputs() {
        return optionalOutputs;
    }

    /**
     * Sets the value of the optionalOutputs property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setOptionalOutputs(AnyType value) {
        this.optionalOutputs = value;
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

}
