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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.VerificationTimeInfoType;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion.NameIdentifierType;


/**
 * <p>Java class for VerificationReportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VerificationReportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CurrentTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}VerificationTimeInfo" minOccurs="0"/>
 *         &lt;element name="VerifierIdentity" type="{urn:oasis:names:tc:SAML:1.0:assertion}NameIdentifierType" minOccurs="0"/>
 *         &lt;element name="IndividualSignatureReport" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}IndividualSignatureReportType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerificationReportType", propOrder = {
    "currentTime",
    "verificationTimeInfo",
    "verifierIdentity",
    "individualSignatureReport"
})
public class VerificationReportType {

    @XmlElement(name = "CurrentTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar currentTime;
    @XmlElement(name = "VerificationTimeInfo", namespace = "urn:oasis:names:tc:dss:1.0:core:schema")
    protected VerificationTimeInfoType verificationTimeInfo;
    @XmlElement(name = "VerifierIdentity")
    protected NameIdentifierType verifierIdentity;
    @XmlElement(name = "IndividualSignatureReport")
    protected List<IndividualSignatureReportType> individualSignatureReport;

    /**
     * Gets the value of the currentTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets the value of the currentTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCurrentTime(XMLGregorianCalendar value) {
        this.currentTime = value;
    }

    /**
     * Gets the value of the verificationTimeInfo property.
     * 
     * @return
     *     possible object is
     *     {@link VerificationTimeInfoType }
     *     
     */
    public VerificationTimeInfoType getVerificationTimeInfo() {
        return verificationTimeInfo;
    }

    /**
     * Sets the value of the verificationTimeInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link VerificationTimeInfoType }
     *     
     */
    public void setVerificationTimeInfo(VerificationTimeInfoType value) {
        this.verificationTimeInfo = value;
    }

    /**
     * Gets the value of the verifierIdentity property.
     * 
     * @return
     *     possible object is
     *     {@link NameIdentifierType }
     *     
     */
    public NameIdentifierType getVerifierIdentity() {
        return verifierIdentity;
    }

    /**
     * Sets the value of the verifierIdentity property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIdentifierType }
     *     
     */
    public void setVerifierIdentity(NameIdentifierType value) {
        this.verifierIdentity = value;
    }

    /**
     * Gets the value of the individualSignatureReport property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the individualSignatureReport property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIndividualSignatureReport().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IndividualSignatureReportType }
     * 
     * 
     */
    public List<IndividualSignatureReportType> getIndividualSignatureReport() {
        if (individualSignatureReport == null) {
            individualSignatureReport = new ArrayList<IndividualSignatureReportType>();
        }
        return this.individualSignatureReport;
    }

}
