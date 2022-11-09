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

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.AnyType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.Result;


/**
 * <p>Java class for IndividualSignatureReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IndividualSignatureReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SignatureIdentifier" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SignatureIdentifierType"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}Result"/>
 *         &lt;element name="Details" type="{urn:oasis:names:tc:dss:1.0:core:schema}AnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IndividualSignatureReportType", propOrder = {
    "signatureIdentifier",
    "result",
    "details"
})
public class IndividualSignatureReportType {

    @XmlElement(name = "SignatureIdentifier", required = true)
    protected SignatureIdentifierType signatureIdentifier;
    @XmlElement(name = "Result", namespace = "urn:oasis:names:tc:dss:1.0:core:schema", required = true)
    protected Result result;
    @XmlElement(name = "Details")
    protected AnyType details;

    /**
     * Gets the value of the signatureIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureIdentifierType }
     *     
     */
    public SignatureIdentifierType getSignatureIdentifier() {
        return signatureIdentifier;
    }

    /**
     * Sets the value of the signatureIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureIdentifierType }
     *     
     */
    public void setSignatureIdentifier(SignatureIdentifierType value) {
        this.signatureIdentifier = value;
    }

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
     * Gets the value of the details property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getDetails() {
        return details;
    }

    /**
     * Sets the value of the details property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setDetails(AnyType value) {
        this.details = value;
    }

}
