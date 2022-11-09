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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.w3._2000._09.xmldsig.SignatureType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *           &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}Timestamp"/>
 *           &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}Base64Signature"/>
 *           &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}SignaturePtr"/>
 *           &lt;element name="Other" type="{urn:oasis:names:tc:dss:1.0:core:schema}AnyType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *       &lt;attribute name="SchemaRefs" type="{http://www.w3.org/2001/XMLSchema}IDREFS" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "signature",
    "timestamp",
    "base64Signature",
    "signaturePtr",
    "other"
})
@XmlRootElement(name = "SignatureObject")
public class SignatureObject {

    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlElement(name = "Timestamp")
    protected Timestamp timestamp;
    @XmlElement(name = "Base64Signature")
    protected Base64Signature base64Signature;
    @XmlElement(name = "SignaturePtr")
    protected SignaturePtr signaturePtr;
    @XmlElement(name = "Other")
    protected AnyType other;
    @XmlAttribute(name = "SchemaRefs")
    @XmlIDREF
    @XmlSchemaType(name = "IDREFS")
    protected List<Object> schemaRefs;

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the timestamp property.
     * 
     * @return
     *     possible object is
     *     {@link Timestamp }
     *     
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the value of the timestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Timestamp }
     *     
     */
    public void setTimestamp(Timestamp value) {
        this.timestamp = value;
    }

    /**
     * Gets the value of the base64Signature property.
     * 
     * @return
     *     possible object is
     *     {@link Base64Signature }
     *     
     */
    public Base64Signature getBase64Signature() {
        return base64Signature;
    }

    /**
     * Sets the value of the base64Signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link Base64Signature }
     *     
     */
    public void setBase64Signature(Base64Signature value) {
        this.base64Signature = value;
    }

    /**
     * Gets the value of the signaturePtr property.
     * 
     * @return
     *     possible object is
     *     {@link SignaturePtr }
     *     
     */
    public SignaturePtr getSignaturePtr() {
        return signaturePtr;
    }

    /**
     * Sets the value of the signaturePtr property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignaturePtr }
     *     
     */
    public void setSignaturePtr(SignaturePtr value) {
        this.signaturePtr = value;
    }

    /**
     * Gets the value of the other property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getOther() {
        return other;
    }

    /**
     * Sets the value of the other property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setOther(AnyType value) {
        this.other = value;
    }

    /**
     * Gets the value of the schemaRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the schemaRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSchemaRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getSchemaRefs() {
        if (schemaRefs == null) {
            schemaRefs = new ArrayList<Object>();
        }
        return this.schemaRefs;
    }

}
