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
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerifyManifestResultsType;


/**
 * <p>Java class for DetailedReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DetailedReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FormatOK" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="Properties" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}PropertiesType" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}VerifyManifestResults" minOccurs="0"/>
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
@XmlType(name = "DetailedReportType", propOrder = {
    "formatOK",
    "properties",
    "verifyManifestResults",
    "signatureOK",
    "certificatePathValidity"
})
public class DetailedReportType {

    @XmlElement(name = "FormatOK", required = true)
    protected DetailType formatOK;
    @XmlElement(name = "Properties")
    protected PropertiesType properties;
    @XmlElement(name = "VerifyManifestResults", namespace = "urn:oasis:names:tc:dss:1.0:core:schema")
    protected VerifyManifestResultsType verifyManifestResults;
    @XmlElement(name = "SignatureOK", required = true)
    protected SignatureValidityType signatureOK;
    @XmlElement(name = "CertificatePathValidity")
    protected CertificatePathValidityType certificatePathValidity;

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
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link PropertiesType }
     *     
     */
    public PropertiesType getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertiesType }
     *     
     */
    public void setProperties(PropertiesType value) {
        this.properties = value;
    }

    /**
     * Gets the value of the verifyManifestResults property.
     * 
     * @return
     *     possible object is
     *     {@link VerifyManifestResultsType }
     *     
     */
    public VerifyManifestResultsType getVerifyManifestResults() {
        return verifyManifestResults;
    }

    /**
     * Sets the value of the verifyManifestResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerifyManifestResultsType }
     *     
     */
    public void setVerifyManifestResults(VerifyManifestResultsType value) {
        this.verifyManifestResults = value;
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
