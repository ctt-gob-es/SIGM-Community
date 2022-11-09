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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.w3._2000._09.xmldsig.KeyInfoType;


/**
 * <p>Java class for SubjectConfirmationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectConfirmationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}ConfirmationMethod" maxOccurs="unbounded"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}SubjectConfirmationData" minOccurs="0"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}KeyInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectConfirmationType", propOrder = {
    "confirmationMethod",
    "subjectConfirmationData",
    "keyInfo"
})
public class SubjectConfirmationType {

    @XmlElement(name = "ConfirmationMethod", required = true)
    @XmlSchemaType(name = "anyURI")
    protected List<String> confirmationMethod;
    @XmlElement(name = "SubjectConfirmationData")
    protected Object subjectConfirmationData;
    @XmlElement(name = "KeyInfo", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected KeyInfoType keyInfo;

    /**
     * Gets the value of the confirmationMethod property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the confirmationMethod property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfirmationMethod().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getConfirmationMethod() {
        if (confirmationMethod == null) {
            confirmationMethod = new ArrayList<String>();
        }
        return this.confirmationMethod;
    }

    /**
     * Gets the value of the subjectConfirmationData property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSubjectConfirmationData() {
        return subjectConfirmationData;
    }

    /**
     * Sets the value of the subjectConfirmationData property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSubjectConfirmationData(Object value) {
        this.subjectConfirmationData = value;
    }

    /**
     * Gets the value of the keyInfo property.
     * 
     * @return
     *     possible object is
     *     {@link KeyInfoType }
     *     
     */
    public KeyInfoType getKeyInfo() {
        return keyInfo;
    }

    /**
     * Sets the value of the keyInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link KeyInfoType }
     *     
     */
    public void setKeyInfo(KeyInfoType value) {
        this.keyInfo = value;
    }

}
