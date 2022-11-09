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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._02231.v2.TSLSchemeInformationType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._02231.v2.TrustServiceProviderListType;


/**
 * <p>Java class for TrustStatusListValidityType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TrustStatusListValidityType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeInformation"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}TrustServiceProviderList" minOccurs="0"/>
 *         &lt;element name="SignatureOK" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}SignatureValidityType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="TSLTag" use="required" type="{http://uri.etsi.org/02231/v2#}TSLTagType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TrustStatusListValidityType", propOrder = {
    "schemeInformation",
    "trustServiceProviderList",
    "signatureOK"
})
public class TrustStatusListValidityType {

    @XmlElement(name = "SchemeInformation", namespace = "http://uri.etsi.org/02231/v2#", required = true)
    protected TSLSchemeInformationType schemeInformation;
    @XmlElement(name = "TrustServiceProviderList", namespace = "http://uri.etsi.org/02231/v2#")
    protected TrustServiceProviderListType trustServiceProviderList;
    @XmlElement(name = "SignatureOK", required = true)
    protected SignatureValidityType signatureOK;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "TSLTag", required = true)
    protected String tslTag;

    /**
     * Gets the value of the schemeInformation property.
     * 
     * @return
     *     possible object is
     *     {@link TSLSchemeInformationType }
     *     
     */
    public TSLSchemeInformationType getSchemeInformation() {
        return schemeInformation;
    }

    /**
     * Sets the value of the schemeInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSLSchemeInformationType }
     *     
     */
    public void setSchemeInformation(TSLSchemeInformationType value) {
        this.schemeInformation = value;
    }

    /**
     * Gets the value of the trustServiceProviderList property.
     * 
     * @return
     *     possible object is
     *     {@link TrustServiceProviderListType }
     *     
     */
    public TrustServiceProviderListType getTrustServiceProviderList() {
        return trustServiceProviderList;
    }

    /**
     * Sets the value of the trustServiceProviderList property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrustServiceProviderListType }
     *     
     */
    public void setTrustServiceProviderList(TrustServiceProviderListType value) {
        this.trustServiceProviderList = value;
    }

    /**
     * Gets the value of the signatureOK property.
     * 
     * @return
     *     possible object is
     *     {@link SignatureValidityType }
     *     
     */
    public SignatureValidityType getSignatureOK() {
        return signatureOK;
    }

    /**
     * Sets the value of the signatureOK property.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureValidityType }
     *     
     */
    public void setSignatureOK(SignatureValidityType value) {
        this.signatureOK = value;
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
     * Gets the value of the tslTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSLTag() {
        return tslTag;
    }

    /**
     * Sets the value of the tslTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSLTag(String value) {
        this.tslTag = value;
    }

}
