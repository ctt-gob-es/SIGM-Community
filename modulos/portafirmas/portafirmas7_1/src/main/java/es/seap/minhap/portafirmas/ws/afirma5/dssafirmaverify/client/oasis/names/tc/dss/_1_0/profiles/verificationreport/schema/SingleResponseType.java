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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DetailType;


/**
 * <p>Java class for SingleResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SingleResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CertID">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="HashAlgorithm" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *                   &lt;element name="IssuerNameHash" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/>
 *                   &lt;element name="IssuerKeyHash" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/>
 *                   &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CertStatus" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="ThisUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="NextUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="SingleExtensions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ExtensionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SingleResponseType", propOrder = {
    "certID",
    "certStatus",
    "thisUpdate",
    "nextUpdate",
    "singleExtensions"
})
public class SingleResponseType {

    @XmlElement(name = "CertID", required = true)
    protected SingleResponseType.CertID certID;
    @XmlElement(name = "CertStatus", required = true)
    protected DetailType certStatus;
    @XmlElement(name = "ThisUpdate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar thisUpdate;
    @XmlElement(name = "NextUpdate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar nextUpdate;
    @XmlElement(name = "SingleExtensions")
    protected ExtensionsType singleExtensions;

    /**
     * Gets the value of the certID property.
     * 
     * @return
     *     possible object is
     *     {@link SingleResponseType.CertID }
     *     
     */
    public SingleResponseType.CertID getCertID() {
        return certID;
    }

    /**
     * Sets the value of the certID property.
     * 
     * @param value
     *     allowed object is
     *     {@link SingleResponseType.CertID }
     *     
     */
    public void setCertID(SingleResponseType.CertID value) {
        this.certID = value;
    }

    /**
     * Gets the value of the certStatus property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getCertStatus() {
        return certStatus;
    }

    /**
     * Sets the value of the certStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setCertStatus(DetailType value) {
        this.certStatus = value;
    }

    /**
     * Gets the value of the thisUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getThisUpdate() {
        return thisUpdate;
    }

    /**
     * Sets the value of the thisUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setThisUpdate(XMLGregorianCalendar value) {
        this.thisUpdate = value;
    }

    /**
     * Gets the value of the nextUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Sets the value of the nextUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNextUpdate(XMLGregorianCalendar value) {
        this.nextUpdate = value;
    }

    /**
     * Gets the value of the singleExtensions property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsType }
     *     
     */
    public ExtensionsType getSingleExtensions() {
        return singleExtensions;
    }

    /**
     * Sets the value of the singleExtensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsType }
     *     
     */
    public void setSingleExtensions(ExtensionsType value) {
        this.singleExtensions = value;
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
     *       &lt;sequence>
     *         &lt;element name="HashAlgorithm" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
     *         &lt;element name="IssuerNameHash" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/>
     *         &lt;element name="IssuerKeyHash" type="{http://www.w3.org/2001/XMLSchema}hexBinary"/>
     *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
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
        "hashAlgorithm",
        "issuerNameHash",
        "issuerKeyHash",
        "serialNumber"
    })
    public static class CertID {

        @XmlElement(name = "HashAlgorithm", required = true)
        protected DetailType hashAlgorithm;
        @XmlElement(name = "IssuerNameHash", required = true, type = String.class)
        @XmlJavaTypeAdapter(HexBinaryAdapter.class)
        @XmlSchemaType(name = "hexBinary")
        protected byte[] issuerNameHash;
        @XmlElement(name = "IssuerKeyHash", required = true, type = String.class)
        @XmlJavaTypeAdapter(HexBinaryAdapter.class)
        @XmlSchemaType(name = "hexBinary")
        protected byte[] issuerKeyHash;
        @XmlElement(name = "SerialNumber", required = true)
        protected BigInteger serialNumber;

        /**
         * Gets the value of the hashAlgorithm property.
         * 
         * @return
         *     possible object is
         *     {@link DetailType }
         *     
         */
        public DetailType getHashAlgorithm() {
            return hashAlgorithm;
        }

        /**
         * Sets the value of the hashAlgorithm property.
         * 
         * @param value
         *     allowed object is
         *     {@link DetailType }
         *     
         */
        public void setHashAlgorithm(DetailType value) {
            this.hashAlgorithm = value;
        }

        /**
         * Gets the value of the issuerNameHash property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public byte[] getIssuerNameHash() {
            return issuerNameHash;
        }

        /**
         * Sets the value of the issuerNameHash property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIssuerNameHash(byte[] value) {
            this.issuerNameHash = ((byte[]) value);
        }

        /**
         * Gets the value of the issuerKeyHash property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public byte[] getIssuerKeyHash() {
            return issuerKeyHash;
        }

        /**
         * Sets the value of the issuerKeyHash property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIssuerKeyHash(byte[] value) {
            this.issuerKeyHash = ((byte[]) value);
        }

        /**
         * Gets the value of the serialNumber property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getSerialNumber() {
            return serialNumber;
        }

        /**
         * Sets the value of the serialNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setSerialNumber(BigInteger value) {
            this.serialNumber = value;
        }

    }

}
