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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for OCSPContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OCSPContentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="ResponderID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="producedAt" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Responses">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="SingleResponse" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SingleResponseType"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ResponseExtensions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ExtensionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OCSPContentType", propOrder = {
    "version",
    "responderID",
    "producedAt",
    "responses",
    "responseExtensions"
})
public class OCSPContentType {

    @XmlElement(name = "Version", required = true)
    protected BigInteger version;
    @XmlElement(name = "ResponderID", required = true)
    protected String responderID;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar producedAt;
    @XmlElement(name = "Responses", required = true)
    protected OCSPContentType.Responses responses;
    @XmlElement(name = "ResponseExtensions")
    protected ExtensionsType responseExtensions;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVersion(BigInteger value) {
        this.version = value;
    }

    /**
     * Gets the value of the responderID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponderID() {
        return responderID;
    }

    /**
     * Sets the value of the responderID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponderID(String value) {
        this.responderID = value;
    }

    /**
     * Gets the value of the producedAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getProducedAt() {
        return producedAt;
    }

    /**
     * Sets the value of the producedAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setProducedAt(XMLGregorianCalendar value) {
        this.producedAt = value;
    }

    /**
     * Gets the value of the responses property.
     * 
     * @return
     *     possible object is
     *     {@link OCSPContentType.Responses }
     *     
     */
    public OCSPContentType.Responses getResponses() {
        return responses;
    }

    /**
     * Sets the value of the responses property.
     * 
     * @param value
     *     allowed object is
     *     {@link OCSPContentType.Responses }
     *     
     */
    public void setResponses(OCSPContentType.Responses value) {
        this.responses = value;
    }

    /**
     * Gets the value of the responseExtensions property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsType }
     *     
     */
    public ExtensionsType getResponseExtensions() {
        return responseExtensions;
    }

    /**
     * Sets the value of the responseExtensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsType }
     *     
     */
    public void setResponseExtensions(ExtensionsType value) {
        this.responseExtensions = value;
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
     *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *         &lt;element name="SingleResponse" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SingleResponseType"/>
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
        "singleResponse"
    })
    public static class Responses {

        @XmlElement(name = "SingleResponse")
        protected List<SingleResponseType> singleResponse;

        /**
         * Gets the value of the singleResponse property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the singleResponse property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSingleResponse().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link SingleResponseType }
         * 
         * 
         */
        public List<SingleResponseType> getSingleResponse() {
            if (singleResponse == null) {
                singleResponse = new ArrayList<SingleResponseType>();
            }
            return this.singleResponse;
        }

    }

}
