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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3.OCSPIdentifierType;


/**
 * <p>Java class for OCSPValidityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OCSPValidityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="OCSPIdentifier" type="{http://uri.etsi.org/01903/v1.3.2#}OCSPIdentifierType"/>
 *         &lt;element name="OCSPValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="OCSPContent" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}OCSPContentType" minOccurs="0"/>
 *         &lt;element name="SignatureOK" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SignatureValidityType"/>
 *         &lt;element name="CertificatePathValidity" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CertificatePathValidityType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OCSPValidityType", propOrder = {
    "ocspIdentifier",
    "ocspValue",
    "ocspContent",
    "signatureOK",
    "certificatePathValidity"
})
public class OCSPValidityType {

    @XmlElement(name = "OCSPIdentifier", required = true)
    protected OCSPIdentifierType ocspIdentifier;
    @XmlElement(name = "OCSPValue")
    protected byte[] ocspValue;
    @XmlElement(name = "OCSPContent")
    protected OCSPContentType ocspContent;
    @XmlElement(name = "SignatureOK", required = true)
    protected SignatureValidityType signatureOK;
    @XmlElement(name = "CertificatePathValidity")
    protected CertificatePathValidityType certificatePathValidity;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the ocspIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link OCSPIdentifierType }
     *     
     */
    public OCSPIdentifierType getOCSPIdentifier() {
        return ocspIdentifier;
    }

    /**
     * Sets the value of the ocspIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link OCSPIdentifierType }
     *     
     */
    public void setOCSPIdentifier(OCSPIdentifierType value) {
        this.ocspIdentifier = value;
    }

    /**
     * Gets the value of the ocspValue property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getOCSPValue() {
        return ocspValue;
    }

    /**
     * Sets the value of the ocspValue property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setOCSPValue(byte[] value) {
        this.ocspValue = ((byte[]) value);
    }

    /**
     * Gets the value of the ocspContent property.
     * 
     * @return
     *     possible object is
     *     {@link OCSPContentType }
     *     
     */
    public OCSPContentType getOCSPContent() {
        return ocspContent;
    }

    /**
     * Sets the value of the ocspContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link OCSPContentType }
     *     
     */
    public void setOCSPContent(OCSPContentType value) {
        this.ocspContent = value;
    }

    /**
     * Gets the value of the signatureOK property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureValidityType }
     *     
     */
    public SignatureValidityType getSignatureOK() {
        return signatureOK;
    }

    /**
     * Sets the value of the signatureOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureValidityType }
     *     
     */
    public void setSignatureOK(SignatureValidityType value) {
        this.signatureOK = value;
    }

    /**
     * Gets the value of the certificatePathValidity property.
     * 
     * @return
     *     possible object is
     *     {@link CertificatePathValidityType }
     *     
     */
    public CertificatePathValidityType getCertificatePathValidity() {
        return certificatePathValidity;
    }

    /**
     * Sets the value of the certificatePathValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificatePathValidityType }
     *     
     */
    public void setCertificatePathValidity(CertificatePathValidityType value) {
        this.certificatePathValidity = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
