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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.AnyType;


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
 *         &lt;element name="CRLValues" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="VerifiedCRL" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CRLValidityType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OCSPValues" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="VerifiedOCSPResponse" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}OCSPValidityType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="OtherValues" type="{urn:oasis:names:tc:dss:1.0:core:schema}AnyType" minOccurs="0"/>
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
    protected RevocationValuesType.CRLValues crlValues;
    @XmlElement(name = "OCSPValues")
    protected RevocationValuesType.OCSPValues ocspValues;
    @XmlElement(name = "OtherValues")
    protected AnyType otherValues;
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
     *     {@link RevocationValuesType.CRLValues }
     *     
     */
    public RevocationValuesType.CRLValues getCRLValues() {
        return crlValues;
    }

    /**
     * Sets the value of the crlValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevocationValuesType.CRLValues }
     *     
     */
    public void setCRLValues(RevocationValuesType.CRLValues value) {
        this.crlValues = value;
    }

    /**
     * Gets the value of the ocspValues property.
     * 
     * @return
     *     possible object is
     *     {@link RevocationValuesType.OCSPValues }
     *     
     */
    public RevocationValuesType.OCSPValues getOCSPValues() {
        return ocspValues;
    }

    /**
     * Sets the value of the ocspValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link RevocationValuesType.OCSPValues }
     *     
     */
    public void setOCSPValues(RevocationValuesType.OCSPValues value) {
        this.ocspValues = value;
    }

    /**
     * Gets the value of the otherValues property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getOtherValues() {
        return otherValues;
    }

    /**
     * Sets the value of the otherValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setOtherValues(AnyType value) {
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="VerifiedCRL" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CRLValidityType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "verifiedCRL"
    })
    public static class CRLValues {

        @XmlElement(name = "VerifiedCRL", required = true)
        protected List<CRLValidityType> verifiedCRL;

        /**
         * Gets the value of the verifiedCRL property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the verifiedCRL property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVerifiedCRL().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CRLValidityType }
         * 
         * 
         */
        public List<CRLValidityType> getVerifiedCRL() {
            if (verifiedCRL == null) {
                verifiedCRL = new ArrayList<CRLValidityType>();
            }
            return this.verifiedCRL;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="VerifiedOCSPResponse" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}OCSPValidityType"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "verifiedOCSPResponse"
    })
    public static class OCSPValues {

        @XmlElement(name = "VerifiedOCSPResponse", required = true)
        protected List<OCSPValidityType> verifiedOCSPResponse;

        /**
         * Gets the value of the verifiedOCSPResponse property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the verifiedOCSPResponse property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVerifiedOCSPResponse().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link OCSPValidityType }
         * 
         * 
         */
        public List<OCSPValidityType> getVerifiedOCSPResponse() {
            if (verifiedOCSPResponse == null) {
                verifiedOCSPResponse = new ArrayList<OCSPValidityType>();
            }
            return this.verifiedOCSPResponse;
        }

    }

}
