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
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.w3._2000._09.xmldsig.X509IssuerSerialType;


/**
 * <p>Java class for CertificatePathValidityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CertificatePathValidityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PathValiditySummary" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="CertificateIdentifier" type="{http://www.w3.org/2000/09/xmldsig#}X509IssuerSerialType"/>
 *         &lt;element name="PathValidityDetail" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CertificatePathValidityDetailType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificatePathValidityType", propOrder = {
    "pathValiditySummary",
    "certificateIdentifier",
    "pathValidityDetail"
})
public class CertificatePathValidityType {

    @XmlElement(name = "PathValiditySummary", required = true)
    protected DetailType pathValiditySummary;
    @XmlElement(name = "CertificateIdentifier", required = true)
    protected X509IssuerSerialType certificateIdentifier;
    @XmlElement(name = "PathValidityDetail")
    protected CertificatePathValidityDetailType pathValidityDetail;

    /**
     * Gets the value of the pathValiditySummary property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getPathValiditySummary() {
        return pathValiditySummary;
    }

    /**
     * Sets the value of the pathValiditySummary property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setPathValiditySummary(DetailType value) {
        this.pathValiditySummary = value;
    }

    /**
     * Gets the value of the certificateIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link X509IssuerSerialType }
     *     
     */
    public X509IssuerSerialType getCertificateIdentifier() {
        return certificateIdentifier;
    }

    /**
     * Sets the value of the certificateIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link X509IssuerSerialType }
     *     
     */
    public void setCertificateIdentifier(X509IssuerSerialType value) {
        this.certificateIdentifier = value;
    }

    /**
     * Gets the value of the pathValidityDetail property.
     * 
     * @return
     *     possible object is
     *     {@link CertificatePathValidityDetailType }
     *     
     */
    public CertificatePathValidityDetailType getPathValidityDetail() {
        return pathValidityDetail;
    }

    /**
     * Sets the value of the pathValidityDetail property.
     * 
     * @param value
     *     allowed object is
     *     {@link CertificatePathValidityDetailType }
     *     
     */
    public void setPathValidityDetail(CertificatePathValidityDetailType value) {
        this.pathValidityDetail = value;
    }

}
