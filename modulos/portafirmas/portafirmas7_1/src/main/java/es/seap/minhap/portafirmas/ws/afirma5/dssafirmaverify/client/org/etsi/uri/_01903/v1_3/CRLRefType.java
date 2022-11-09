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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CRLRefType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CRLRefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DigestAlgAndValue" type="{http://uri.etsi.org/01903/v1.3.2#}DigestAlgAndValueType"/>
 *         &lt;element name="CRLIdentifier" type="{http://uri.etsi.org/01903/v1.3.2#}CRLIdentifierType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CRLRefType", propOrder = {
    "digestAlgAndValue",
    "crlIdentifier"
})
public class CRLRefType {

    @XmlElement(name = "DigestAlgAndValue", required = true)
    protected DigestAlgAndValueType digestAlgAndValue;
    @XmlElement(name = "CRLIdentifier")
    protected CRLIdentifierType crlIdentifier;

    /**
     * Gets the value of the digestAlgAndValue property.
     * 
     * @return
     *     possible object is
     *     {@link DigestAlgAndValueType }
     *     
     */
    public DigestAlgAndValueType getDigestAlgAndValue() {
        return digestAlgAndValue;
    }

    /**
     * Sets the value of the digestAlgAndValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link DigestAlgAndValueType }
     *     
     */
    public void setDigestAlgAndValue(DigestAlgAndValueType value) {
        this.digestAlgAndValue = value;
    }

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

}
