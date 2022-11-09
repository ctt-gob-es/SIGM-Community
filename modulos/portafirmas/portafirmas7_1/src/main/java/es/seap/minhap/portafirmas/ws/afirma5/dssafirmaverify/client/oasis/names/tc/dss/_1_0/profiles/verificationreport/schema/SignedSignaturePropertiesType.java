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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3.CertIDListType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3.SignaturePolicyIdType;


/**
 * <p>Java class for SignedSignaturePropertiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignedSignaturePropertiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="SigningCertificate" type="{http://uri.etsi.org/01903/v1.3.2#}CertIDListType" minOccurs="0"/>
 *         &lt;element name="SignaturePolicyIdentifier" type="{http://uri.etsi.org/01903/v1.3.2#}SignaturePolicyIdType" minOccurs="0"/>
 *         &lt;element name="SignatureProductionPlace" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SignerRole" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SignerRoleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignedSignaturePropertiesType", propOrder = {
    "signingTime",
    "signingCertificate",
    "signaturePolicyIdentifier",
    "signatureProductionPlace",
    "signerRole"
})
public class SignedSignaturePropertiesType {

    @XmlElement(name = "SigningTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    @XmlElement(name = "SigningCertificate")
    protected CertIDListType signingCertificate;
    @XmlElement(name = "SignaturePolicyIdentifier")
    protected SignaturePolicyIdType signaturePolicyIdentifier;
    @XmlElement(name = "SignatureProductionPlace")
    protected String signatureProductionPlace;
    @XmlElement(name = "SignerRole")
    protected SignerRoleType signerRole;

    /**
     * Gets the value of the signingTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSigningTime() {
        return signingTime;
    }

    /**
     * Sets the value of the signingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSigningTime(XMLGregorianCalendar value) {
        this.signingTime = value;
    }

    /**
     * Gets the value of the signingCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link CertIDListType }
     *     
     */
    public CertIDListType getSigningCertificate() {
        return signingCertificate;
    }

    /**
     * Sets the value of the signingCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertIDListType }
     *     
     */
    public void setSigningCertificate(CertIDListType value) {
        this.signingCertificate = value;
    }

    /**
     * Gets the value of the signaturePolicyIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link SignaturePolicyIdType }
     *     
     */
    public SignaturePolicyIdType getSignaturePolicyIdentifier() {
        return signaturePolicyIdentifier;
    }

    /**
     * Sets the value of the signaturePolicyIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignaturePolicyIdType }
     *     
     */
    public void setSignaturePolicyIdentifier(SignaturePolicyIdType value) {
        this.signaturePolicyIdentifier = value;
    }

    /**
     * Gets the value of the signatureProductionPlace property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureProductionPlace() {
        return signatureProductionPlace;
    }

    /**
     * Sets the value of the signatureProductionPlace property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureProductionPlace(String value) {
        this.signatureProductionPlace = value;
    }

    /**
     * Gets the value of the signerRole property.
     * 
     * @return
     *     possible object is
     *     {@link SignerRoleType }
     *     
     */
    public SignerRoleType getSignerRole() {
        return signerRole;
    }

    /**
     * Sets the value of the signerRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignerRoleType }
     *     
     */
    public void setSignerRole(SignerRoleType value) {
        this.signerRole = value;
    }

}
