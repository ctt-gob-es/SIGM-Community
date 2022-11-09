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

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3.CRLIdentifierType;


/**
 * <p>Java class for CRLValidityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CRLValidityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CRLIdentifier" type="{http://uri.etsi.org/01903/v1.3.2#}CRLIdentifierType"/>
 *         &lt;element name="CRLValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="CRLContent" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CRLContentType" minOccurs="0"/>
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
@XmlType(name = "CRLValidityType", propOrder = {
    "crlIdentifier",
    "crlValue",
    "crlContent",
    "signatureOK",
    "certificatePathValidity"
})
public class CRLValidityType {

    @XmlElement(name = "CRLIdentifier", required = true)
    protected CRLIdentifierType crlIdentifier;
    @XmlElement(name = "CRLValue")
    protected byte[] crlValue;
    @XmlElement(name = "CRLContent")
    protected CRLContentType crlContent;
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
     * Gets the value of the crlIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link CRLIdentifierType }
     *     
     */
    public CRLIdentifierType getCRLIdentifier() {
        return crlIdentifier;
    }

    /**
     * Sets the value of the crlIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRLIdentifierType }
     *     
     */
    public void setCRLIdentifier(CRLIdentifierType value) {
        this.crlIdentifier = value;
    }

    /**
     * Gets the value of the crlValue property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCRLValue() {
        return crlValue;
    }

    /**
     * Sets the value of the crlValue property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCRLValue(byte[] value) {
        this.crlValue = ((byte[]) value);
    }

    /**
     * Gets the value of the crlContent property.
     * 
     * @return
     *     possible object is
     *     {@link CRLContentType }
     *     
     */
    public CRLContentType getCRLContent() {
        return crlContent;
    }

    /**
     * Sets the value of the crlContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRLContentType }
     *     
     */
    public void setCRLContent(CRLContentType value) {
        this.crlContent = value;
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
