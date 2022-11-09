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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for RevocationValuesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RevocationValuesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CRLValues" type="{http://uri.etsi.org/01903/v1.3.2#}CRLValuesType" minOccurs="0"/>
 *         &lt;element name="OCSPValues" type="{http://uri.etsi.org/01903/v1.3.2#}OCSPValuesType" minOccurs="0"/>
 *         &lt;element name="OtherValues" type="{http://uri.etsi.org/01903/v1.3.2#}OtherCertStatusValuesType" minOccurs="0"/>
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
@XmlType(name = "RevocationValuesType", propOrder = {
    "crlValues",
    "ocspValues",
    "otherValues"
})
public class RevocationValuesType {

    @XmlElement(name = "CRLValues")
    protected CRLValuesType crlValues;
    @XmlElement(name = "OCSPValues")
    protected OCSPValuesType ocspValues;
    @XmlElement(name = "OtherValues")
    protected OtherCertStatusValuesType otherValues;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the crlValues property.
     * 
     * @return
     *     possible object is
     *     {@link CRLValuesType }
     *     
     */
    public CRLValuesType getCRLValues() {
        return crlValues;
    }

    /**
     * Sets the value of the crlValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRLValuesType }
     *     
     */
    public void setCRLValues(CRLValuesType value) {
        this.crlValues = value;
    }

    /**
     * Gets the value of the ocspValues property.
     * 
     * @return
     *     possible object is
     *     {@link OCSPValuesType }
     *     
     */
    public OCSPValuesType getOCSPValues() {
        return ocspValues;
    }

    /**
     * Sets the value of the ocspValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link OCSPValuesType }
     *     
     */
    public void setOCSPValues(OCSPValuesType value) {
        this.ocspValues = value;
    }

    /**
     * Gets the value of the otherValues property.
     * 
     * @return
     *     possible object is
     *     {@link OtherCertStatusValuesType }
     *     
     */
    public OtherCertStatusValuesType getOtherValues() {
        return otherValues;
    }

    /**
     * Sets the value of the otherValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherCertStatusValuesType }
     *     
     */
    public void setOtherValues(OtherCertStatusValuesType value) {
        this.otherValues = value;
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
