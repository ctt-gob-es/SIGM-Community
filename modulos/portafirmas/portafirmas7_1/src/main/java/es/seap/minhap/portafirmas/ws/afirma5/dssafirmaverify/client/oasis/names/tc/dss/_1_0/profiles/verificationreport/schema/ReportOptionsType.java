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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReportOptionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReportOptionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IncludeVerifier" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeCurrentTime" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeCertificateValues" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IncludeRevocationValues" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ExpandBinaryValues" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="ReportDetailLevel" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReportOptionsType", propOrder = {
    "includeVerifier",
    "includeCurrentTime",
    "includeCertificateValues",
    "includeRevocationValues",
    "expandBinaryValues",
    "reportDetailLevel"
})
public class ReportOptionsType {

    @XmlElement(name = "IncludeVerifier", defaultValue = "true")
    protected Boolean includeVerifier;
    @XmlElement(name = "IncludeCurrentTime", defaultValue = "true")
    protected Boolean includeCurrentTime;
    @XmlElement(name = "IncludeCertificateValues", defaultValue = "false")
    protected Boolean includeCertificateValues;
    @XmlElement(name = "IncludeRevocationValues", defaultValue = "false")
    protected Boolean includeRevocationValues;
    @XmlElement(name = "ExpandBinaryValues", defaultValue = "false")
    protected Boolean expandBinaryValues;
    @XmlElement(name = "ReportDetailLevel")
    @XmlSchemaType(name = "anyURI")
    protected String reportDetailLevel;

    /**
     * Gets the value of the includeVerifier property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeVerifier() {
        return includeVerifier;
    }

    /**
     * Sets the value of the includeVerifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeVerifier(Boolean value) {
        this.includeVerifier = value;
    }

    /**
     * Gets the value of the includeCurrentTime property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeCurrentTime() {
        return includeCurrentTime;
    }

    /**
     * Sets the value of the includeCurrentTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeCurrentTime(Boolean value) {
        this.includeCurrentTime = value;
    }

    /**
     * Gets the value of the includeCertificateValues property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeCertificateValues() {
        return includeCertificateValues;
    }

    /**
     * Sets the value of the includeCertificateValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeCertificateValues(Boolean value) {
        this.includeCertificateValues = value;
    }

    /**
     * Gets the value of the includeRevocationValues property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIncludeRevocationValues() {
        return includeRevocationValues;
    }

    /**
     * Sets the value of the includeRevocationValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIncludeRevocationValues(Boolean value) {
        this.includeRevocationValues = value;
    }

    /**
     * Gets the value of the expandBinaryValues property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isExpandBinaryValues() {
        return expandBinaryValues;
    }

    /**
     * Sets the value of the expandBinaryValues property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setExpandBinaryValues(Boolean value) {
        this.expandBinaryValues = value;
    }

    /**
     * Gets the value of the reportDetailLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportDetailLevel() {
        return reportDetailLevel;
    }

    /**
     * Sets the value of the reportDetailLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportDetailLevel(String value) {
        this.reportDetailLevel = value;
    }

}
