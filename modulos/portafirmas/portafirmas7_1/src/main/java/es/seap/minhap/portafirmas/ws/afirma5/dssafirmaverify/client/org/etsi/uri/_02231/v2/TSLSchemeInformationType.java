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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._02231.v2;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for TSLSchemeInformationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TSLSchemeInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TSLVersionIdentifier" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="TSLSequenceNumber" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="TSLType" type="{http://uri.etsi.org/02231/v2#}NonEmptyURIType"/>
 *         &lt;element name="SchemeOperatorName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/>
 *         &lt;element name="SchemeOperatorAddress" type="{http://uri.etsi.org/02231/v2#}AddressType"/>
 *         &lt;element name="SchemeName" type="{http://uri.etsi.org/02231/v2#}InternationalNamesType"/>
 *         &lt;element name="SchemeInformationURI" type="{http://uri.etsi.org/02231/v2#}NonEmptyMultiLangURIListType"/>
 *         &lt;element name="StatusDeterminationApproach" type="{http://uri.etsi.org/02231/v2#}NonEmptyURIType"/>
 *         &lt;element name="SchemeTypeCommunityRules" type="{http://uri.etsi.org/02231/v2#}NonEmptyURIListType" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}SchemeTerritory" minOccurs="0"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PolicyOrLegalNotice" minOccurs="0"/>
 *         &lt;element name="HistoricalInformationPeriod" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PointersToOtherTSL" minOccurs="0"/>
 *         &lt;element name="ListIssueDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}NextUpdate"/>
 *         &lt;element name="SchemeExtensions" type="{http://uri.etsi.org/02231/v2#}ExtensionsListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSLSchemeInformationType", propOrder = {
    "tslVersionIdentifier",
    "tslSequenceNumber",
    "tslType",
    "schemeOperatorName",
    "schemeOperatorAddress",
    "schemeName",
    "schemeInformationURI",
    "statusDeterminationApproach",
    "schemeTypeCommunityRules",
    "schemeTerritory",
    "policyOrLegalNotice",
    "historicalInformationPeriod",
    "pointersToOtherTSL",
    "listIssueDateTime",
    "nextUpdate",
    "schemeExtensions"
})
public class TSLSchemeInformationType {

    @XmlElement(name = "TSLVersionIdentifier", required = true)
    protected BigInteger tslVersionIdentifier;
    @XmlElement(name = "TSLSequenceNumber", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger tslSequenceNumber;
    @XmlElement(name = "TSLType", required = true)
    protected String tslType;
    @XmlElement(name = "SchemeOperatorName", required = true)
    protected InternationalNamesType schemeOperatorName;
    @XmlElement(name = "SchemeOperatorAddress", required = true)
    protected AddressType schemeOperatorAddress;
    @XmlElement(name = "SchemeName", required = true)
    protected InternationalNamesType schemeName;
    @XmlElement(name = "SchemeInformationURI", required = true)
    protected NonEmptyMultiLangURIListType schemeInformationURI;
    @XmlElement(name = "StatusDeterminationApproach", required = true)
    protected String statusDeterminationApproach;
    @XmlElement(name = "SchemeTypeCommunityRules")
    protected NonEmptyURIListType schemeTypeCommunityRules;
    @XmlElement(name = "SchemeTerritory")
    protected String schemeTerritory;
    @XmlElement(name = "PolicyOrLegalNotice")
    protected PolicyOrLegalnoticeType policyOrLegalNotice;
    @XmlElement(name = "HistoricalInformationPeriod", required = true)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger historicalInformationPeriod;
    @XmlElement(name = "PointersToOtherTSL")
    protected OtherTSLPointersType pointersToOtherTSL;
    @XmlElement(name = "ListIssueDateTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar listIssueDateTime;
    @XmlElement(name = "NextUpdate", required = true)
    protected NextUpdateType nextUpdate;
    @XmlElement(name = "SchemeExtensions")
    protected ExtensionsListType schemeExtensions;

    /**
     * Gets the value of the tslVersionIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTSLVersionIdentifier() {
        return tslVersionIdentifier;
    }

    /**
     * Sets the value of the tslVersionIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTSLVersionIdentifier(BigInteger value) {
        this.tslVersionIdentifier = value;
    }

    /**
     * Gets the value of the tslSequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTSLSequenceNumber() {
        return tslSequenceNumber;
    }

    /**
     * Sets the value of the tslSequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTSLSequenceNumber(BigInteger value) {
        this.tslSequenceNumber = value;
    }

    /**
     * Gets the value of the tslType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTSLType() {
        return tslType;
    }

    /**
     * Sets the value of the tslType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTSLType(String value) {
        this.tslType = value;
    }

    /**
     * Gets the value of the schemeOperatorName property.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getSchemeOperatorName() {
        return schemeOperatorName;
    }

    /**
     * Sets the value of the schemeOperatorName property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setSchemeOperatorName(InternationalNamesType value) {
        this.schemeOperatorName = value;
    }

    /**
     * Gets the value of the schemeOperatorAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressType }
     *     
     */
    public AddressType getSchemeOperatorAddress() {
        return schemeOperatorAddress;
    }

    /**
     * Sets the value of the schemeOperatorAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressType }
     *     
     */
    public void setSchemeOperatorAddress(AddressType value) {
        this.schemeOperatorAddress = value;
    }

    /**
     * Gets the value of the schemeName property.
     * 
     * @return
     *     possible object is
     *     {@link InternationalNamesType }
     *     
     */
    public InternationalNamesType getSchemeName() {
        return schemeName;
    }

    /**
     * Sets the value of the schemeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link InternationalNamesType }
     *     
     */
    public void setSchemeName(InternationalNamesType value) {
        this.schemeName = value;
    }

    /**
     * Gets the value of the schemeInformationURI property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public NonEmptyMultiLangURIListType getSchemeInformationURI() {
        return schemeInformationURI;
    }

    /**
     * Sets the value of the schemeInformationURI property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyMultiLangURIListType }
     *     
     */
    public void setSchemeInformationURI(NonEmptyMultiLangURIListType value) {
        this.schemeInformationURI = value;
    }

    /**
     * Gets the value of the statusDeterminationApproach property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusDeterminationApproach() {
        return statusDeterminationApproach;
    }

    /**
     * Sets the value of the statusDeterminationApproach property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusDeterminationApproach(String value) {
        this.statusDeterminationApproach = value;
    }

    /**
     * Gets the value of the schemeTypeCommunityRules property.
     * 
     * @return
     *     possible object is
     *     {@link NonEmptyURIListType }
     *     
     */
    public NonEmptyURIListType getSchemeTypeCommunityRules() {
        return schemeTypeCommunityRules;
    }

    /**
     * Sets the value of the schemeTypeCommunityRules property.
     * 
     * @param value
     *     allowed object is
     *     {@link NonEmptyURIListType }
     *     
     */
    public void setSchemeTypeCommunityRules(NonEmptyURIListType value) {
        this.schemeTypeCommunityRules = value;
    }

    /**
     * Gets the value of the schemeTerritory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchemeTerritory() {
        return schemeTerritory;
    }

    /**
     * Sets the value of the schemeTerritory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchemeTerritory(String value) {
        this.schemeTerritory = value;
    }

    /**
     * Gets the value of the policyOrLegalNotice property.
     * 
     * @return
     *     possible object is
     *     {@link PolicyOrLegalnoticeType }
     *     
     */
    public PolicyOrLegalnoticeType getPolicyOrLegalNotice() {
        return policyOrLegalNotice;
    }

    /**
     * Sets the value of the policyOrLegalNotice property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicyOrLegalnoticeType }
     *     
     */
    public void setPolicyOrLegalNotice(PolicyOrLegalnoticeType value) {
        this.policyOrLegalNotice = value;
    }

    /**
     * Gets the value of the historicalInformationPeriod property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getHistoricalInformationPeriod() {
        return historicalInformationPeriod;
    }

    /**
     * Sets the value of the historicalInformationPeriod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setHistoricalInformationPeriod(BigInteger value) {
        this.historicalInformationPeriod = value;
    }

    /**
     * Gets the value of the pointersToOtherTSL property.
     * 
     * @return
     *     possible object is
     *     {@link OtherTSLPointersType }
     *     
     */
    public OtherTSLPointersType getPointersToOtherTSL() {
        return pointersToOtherTSL;
    }

    /**
     * Sets the value of the pointersToOtherTSL property.
     * 
     * @param value
     *     allowed object is
     *     {@link OtherTSLPointersType }
     *     
     */
    public void setPointersToOtherTSL(OtherTSLPointersType value) {
        this.pointersToOtherTSL = value;
    }

    /**
     * Gets the value of the listIssueDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getListIssueDateTime() {
        return listIssueDateTime;
    }

    /**
     * Sets the value of the listIssueDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setListIssueDateTime(XMLGregorianCalendar value) {
        this.listIssueDateTime = value;
    }

    /**
     * Gets the value of the nextUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link NextUpdateType }
     *     
     */
    public NextUpdateType getNextUpdate() {
        return nextUpdate;
    }

    /**
     * Sets the value of the nextUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link NextUpdateType }
     *     
     */
    public void setNextUpdate(NextUpdateType value) {
        this.nextUpdate = value;
    }

    /**
     * Gets the value of the schemeExtensions property.
     * 
     * @return
     *     possible object is
     *     {@link ExtensionsListType }
     *     
     */
    public ExtensionsListType getSchemeExtensions() {
        return schemeExtensions;
    }

    /**
     * Sets the value of the schemeExtensions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionsListType }
     *     
     */
    public void setSchemeExtensions(ExtensionsListType value) {
        this.schemeExtensions = value;
    }

}
