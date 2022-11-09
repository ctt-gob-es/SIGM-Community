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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DetailType;


/**
 * <p>Java class for CRLContentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CRLContentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Version" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="Signature" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="Issuer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ThisUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="NextUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="RevokedCertificates" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                   &lt;element name="UserCertificate" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                   &lt;element name="RevocationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                   &lt;element name="CrlEntryExtensions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ExtensionsType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CrlExtensions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ExtensionsType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CRLContentType", propOrder = {
    "version",
    "signature",
    "issuer",
    "thisUpdate",
    "nextUpdate",
    "revokedCertificates",
    "crlExtensions"
})
public class CRLContentType {

    @XmlElement(name = "Version")
    protected BigInteger version;
    @XmlElement(name = "Signature", required = true)
    protected DetailType signature;
    @XmlElement(name = "Issuer", required = true)
    protected String issuer;
    @XmlElement(name = "ThisUpdate", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar thisUpdate;
    @XmlElement(name = "NextUpdate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar nextUpdate;
    @XmlElement(name = "RevokedCertificates")
    protected CRLContentType.RevokedCertificates revokedCertificates;
    @XmlElement(name = "CrlExtensions")
    protected ExtensionsType crlExtensions;

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
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setSignature(DetailType value) {
        this.signature = value;
    }

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
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
     * Gets the value of the revokedCertificates property.
     * 
     * @return
     *     possible object is
     *     {@link CRLContentType.RevokedCertificates }
     *     
     */
    public CRLContentType.RevokedCertificates getRevokedCertificates() {
        return revokedCertificates;
    }

    /**
     * Sets the value of the revokedCertificates property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRLContentType.RevokedCertificates }
     *     
     */
    public void setRevokedCertificates(CRLContentType.RevokedCertificates value) {
        this.revokedCertificates = value;
    }

    /**
     * Gets the value of the crlExtensions property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsType }
     *     
     */
    public ExtensionsType getCrlExtensions() {
        return crlExtensions;
    }

    /**
     * Sets the value of the crlExtensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsType }
     *     
     */
    public void setCrlExtensions(ExtensionsType value) {
        this.crlExtensions = value;
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
     *         &lt;element name="UserCertificate" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *         &lt;element name="RevocationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *         &lt;element name="CrlEntryExtensions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ExtensionsType" minOccurs="0"/>
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
        "userCertificateAndRevocationDateAndCrlEntryExtensions"
    })
    public static class RevokedCertificates {

        @XmlElements({
            @XmlElement(name = "UserCertificate", type = BigInteger.class),
            @XmlElement(name = "CrlEntryExtensions", type = ExtensionsType.class),
            @XmlElement(name = "RevocationDate", type = XMLGregorianCalendar.class)
        })
        protected List<Object> userCertificateAndRevocationDateAndCrlEntryExtensions;

        /**
         * Gets the value of the userCertificateAndRevocationDateAndCrlEntryExtensions property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the userCertificateAndRevocationDateAndCrlEntryExtensions property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getUserCertificateAndRevocationDateAndCrlEntryExtensions().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BigInteger }
         * {@link ExtensionsType }
         * {@link XMLGregorianCalendar }
         * 
         * 
         */
        public List<Object> getUserCertificateAndRevocationDateAndCrlEntryExtensions() {
            if (userCertificateAndRevocationDateAndCrlEntryExtensions == null) {
                userCertificateAndRevocationDateAndCrlEntryExtensions = new ArrayList<Object>();
            }
            return this.userCertificateAndRevocationDateAndCrlEntryExtensions;
        }

    }

}
