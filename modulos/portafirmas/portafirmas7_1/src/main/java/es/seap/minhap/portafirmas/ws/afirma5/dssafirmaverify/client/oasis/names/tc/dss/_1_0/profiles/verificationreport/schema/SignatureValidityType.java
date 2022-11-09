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
 * <p>Java class for SignatureValidityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SignatureValidityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SigMathOK" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="SignatureAlgorithm" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType" minOccurs="0"/>
 *         &lt;element name="HashAlgUpToDate" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType" minOccurs="0"/>
 *         &lt;element name="SigAlgUpToDate" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SignatureValidityType", propOrder = {
    "sigMathOK",
    "signatureAlgorithm",
    "hashAlgUpToDate",
    "sigAlgUpToDate"
})
public class SignatureValidityType {

    @XmlElement(name = "SigMathOK", required = true)
    protected DetailType sigMathOK;
    @XmlElement(name = "SignatureAlgorithm")
    protected DetailType signatureAlgorithm;
    @XmlElement(name = "HashAlgUpToDate")
    protected DetailType hashAlgUpToDate;
    @XmlElement(name = "SigAlgUpToDate")
    protected DetailType sigAlgUpToDate;

    /**
     * Gets the value of the sigMathOK property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getSigMathOK() {
        return sigMathOK;
    }

    /**
     * Sets the value of the sigMathOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setSigMathOK(DetailType value) {
        this.sigMathOK = value;
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
     * Gets the value of the hashAlgUpToDate property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getHashAlgUpToDate() {
        return hashAlgUpToDate;
    }

    /**
     * Sets the value of the hashAlgUpToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setHashAlgUpToDate(DetailType value) {
        this.hashAlgUpToDate = value;
    }

    /**
     * Gets the value of the sigAlgUpToDate property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getSigAlgUpToDate() {
        return sigAlgUpToDate;
    }

    /**
     * Sets the value of the sigAlgUpToDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setSigAlgUpToDate(DetailType value) {
        this.sigAlgUpToDate = value;
    }

}
