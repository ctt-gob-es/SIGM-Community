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

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion.NameIdentifierType;


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
 *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="CreationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Policy" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="ErrorBound" type="{http://www.w3.org/2001/XMLSchema}duration" minOccurs="0"/>
 *         &lt;element name="Ordered" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="TSA" type="{urn:oasis:names:tc:SAML:1.0:assertion}NameIdentifierType" minOccurs="0"/>
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
    "serialNumber",
    "creationTime",
    "policy",
    "errorBound",
    "ordered",
    "tsa"
})
@XmlRootElement(name = "TstInfo")
public class TstInfo {

    @XmlElement(name = "SerialNumber", required = true)
    protected BigInteger serialNumber;
    @XmlElement(name = "CreationTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationTime;
    @XmlElement(name = "Policy")
    @XmlSchemaType(name = "anyURI")
    protected String policy;
    @XmlElement(name = "ErrorBound")
    protected Duration errorBound;
    @XmlElement(name = "Ordered", defaultValue = "false")
    protected Boolean ordered;
    @XmlElement(name = "TSA")
    protected NameIdentifierType tsa;

    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSerialNumber(BigInteger value) {
        this.serialNumber = value;
    }

    /**
     * Gets the value of the creationTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationTime() {
        return creationTime;
    }

    /**
     * Sets the value of the creationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationTime(XMLGregorianCalendar value) {
        this.creationTime = value;
    }

    /**
     * Gets the value of the policy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicy() {
        return policy;
    }

    /**
     * Sets the value of the policy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicy(String value) {
        this.policy = value;
    }

    /**
     * Gets the value of the errorBound property.
     * 
     * @return
     *     possible object is
     *     {@link Duration }
     *     
     */
    public Duration getErrorBound() {
        return errorBound;
    }

    /**
     * Sets the value of the errorBound property.
     * 
     * @param value
     *     allowed object is
     *     {@link Duration }
     *     
     */
    public void setErrorBound(Duration value) {
        this.errorBound = value;
    }

    /**
     * Gets the value of the ordered property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isOrdered() {
        return ordered;
    }

    /**
     * Sets the value of the ordered property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setOrdered(Boolean value) {
        this.ordered = value;
    }

    /**
     * Gets the value of the tsa property.
     * 
     * @return
     *     possible object is
     *     {@link NameIdentifierType }
     *     
     */
    public NameIdentifierType getTSA() {
        return tsa;
    }

    /**
     * Sets the value of the tsa property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIdentifierType }
     *     
     */
    public void setTSA(NameIdentifierType value) {
        this.tsa = value;
    }

}
