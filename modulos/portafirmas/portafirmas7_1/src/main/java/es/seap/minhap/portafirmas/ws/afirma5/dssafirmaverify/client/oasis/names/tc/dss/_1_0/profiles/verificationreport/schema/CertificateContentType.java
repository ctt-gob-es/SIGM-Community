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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.profiles.verificationreport.schema;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DetailType;


/**
 * <p>Java class for CertificateContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CertificateContentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="SignatureAlgorithm" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="Issuer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Validity" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ValidityType"/>
 *         &lt;element name="Subject" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Extensions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ExtensionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificateContentType", propOrder = {
    "version",
    "serialNumber",
    "signatureAlgorithm",
    "issuer",
    "validity",
    "subject",
    "extensions"
})
public class CertificateContentType {

    @XmlElement(name = "Version")
    protected BigInteger version;
    @XmlElement(name = "SerialNumber", required = true)
    protected BigInteger serialNumber;
    @XmlElement(name = "SignatureAlgorithm", required = true)
    protected DetailType signatureAlgorithm;
    @XmlElement(name = "Issuer", required = true)
    protected String issuer;
    @XmlElement(name = "Validity", required = true)
    protected ValidityType validity;
    @XmlElement(name = "Subject", required = true)
    protected String subject;
    @XmlElement(name = "Extensions")
    protected ExtensionsType extensions;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVersion(BigInteger value) {
        this.version = value;
    }

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
     * Gets the value of the signatureAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Sets the value of the signatureAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setSignatureAlgorithm(DetailType value) {
        this.signatureAlgorithm = value;
    }

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the validity property.
     * 
     * @return
     *     possible object is
     *     {@link ValidityType }
     *     
     */
    public ValidityType getValidity() {
        return validity;
    }

    /**
     * Sets the value of the validity property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValidityType }
     *     
     */
    public void setValidity(ValidityType value) {
        this.validity = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

    /**
     * Gets the value of the extensions property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsType }
     *     
     */
    public ExtensionsType getExtensions() {
        return extensions;
    }

    /**
     * Sets the value of the extensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsType }
     *     
     */
    public void setExtensions(ExtensionsType value) {
        this.extensions = value;
    }

}
