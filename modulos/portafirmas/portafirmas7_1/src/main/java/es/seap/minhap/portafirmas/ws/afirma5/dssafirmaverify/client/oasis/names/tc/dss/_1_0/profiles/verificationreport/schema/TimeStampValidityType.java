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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DetailType;


/**
 * <p>Java class for TimeStampValidityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TimeStampValidityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TimeStampIdentifier" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SignatureIdentifierType" minOccurs="0"/>
 *         &lt;element name="FormatOK" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="TimeStampContent" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}TstContentType" minOccurs="0"/>
 *         &lt;element name="MessageHashAlg" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="MessageHashAlgUpToDate" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType" minOccurs="0"/>
 *         &lt;element name="SignatureOK" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SignatureValidityType"/>
 *         &lt;element name="CertificatePathValidity" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CertificatePathValidityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TimeStampValidityType", propOrder = {
    "timeStampIdentifier",
    "formatOK",
    "timeStampContent",
    "messageHashAlg",
    "messageHashAlgUpToDate",
    "signatureOK",
    "certificatePathValidity"
})
public class TimeStampValidityType {

    @XmlElement(name = "TimeStampIdentifier")
    protected SignatureIdentifierType timeStampIdentifier;
    @XmlElement(name = "FormatOK", required = true)
    protected DetailType formatOK;
    @XmlElement(name = "TimeStampContent")
    protected TstContentType timeStampContent;
    @XmlElement(name = "MessageHashAlg", required = true)
    protected DetailType messageHashAlg;
    @XmlElement(name = "MessageHashAlgUpToDate")
    protected DetailType messageHashAlgUpToDate;
    @XmlElement(name = "SignatureOK", required = true)
    protected SignatureValidityType signatureOK;
    @XmlElement(name = "CertificatePathValidity")
    protected CertificatePathValidityType certificatePathValidity;

    /**
     * Gets the value of the timeStampIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureIdentifierType }
     *     
     */
    public SignatureIdentifierType getTimeStampIdentifier() {
        return timeStampIdentifier;
    }

    /**
     * Sets the value of the timeStampIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureIdentifierType }
     *     
     */
    public void setTimeStampIdentifier(SignatureIdentifierType value) {
        this.timeStampIdentifier = value;
    }

    /**
     * Gets the value of the formatOK property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getFormatOK() {
        return formatOK;
    }

    /**
     * Sets the value of the formatOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setFormatOK(DetailType value) {
        this.formatOK = value;
    }

    /**
     * Gets the value of the timeStampContent property.
     * 
     * @return
     *     possible object is
     *     {@link TstContentType }
     *     
     */
    public TstContentType getTimeStampContent() {
        return timeStampContent;
    }

    /**
     * Sets the value of the timeStampContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link TstContentType }
     *     
     */
    public void setTimeStampContent(TstContentType value) {
        this.timeStampContent = value;
    }

    /**
     * Gets the value of the messageHashAlg property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getMessageHashAlg() {
        return messageHashAlg;
    }

    /**
     * Sets the value of the messageHashAlg property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setMessageHashAlg(DetailType value) {
        this.messageHashAlg = value;
    }

    /**
     * Gets the value of the messageHashAlgUpToDate property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getMessageHashAlgUpToDate() {
        return messageHashAlgUpToDate;
    }

    /**
     * Sets the value of the messageHashAlgUpToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setMessageHashAlgUpToDate(DetailType value) {
        this.messageHashAlgUpToDate = value;
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

}
