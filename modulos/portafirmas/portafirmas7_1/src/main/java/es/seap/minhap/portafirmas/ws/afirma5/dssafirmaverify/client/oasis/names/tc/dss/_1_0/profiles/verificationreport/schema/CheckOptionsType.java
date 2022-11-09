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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CheckOptionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CheckOptionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CheckCertificatePath" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CheckCertificateStatus" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="VerifyManifests" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="CheckAlgorithms" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CheckOptionsType", propOrder = {
    "checkCertificatePath",
    "checkCertificateStatus",
    "verifyManifests",
    "checkAlgorithms"
})
public class CheckOptionsType {

    @XmlElement(name = "CheckCertificatePath", defaultValue = "true")
    protected Boolean checkCertificatePath;
    @XmlElement(name = "CheckCertificateStatus", defaultValue = "true")
    protected Boolean checkCertificateStatus;
    @XmlElement(name = "VerifyManifests", defaultValue = "false")
    protected Boolean verifyManifests;
    @XmlElement(name = "CheckAlgorithms", defaultValue = "false")
    protected Boolean checkAlgorithms;

    /**
     * Gets the value of the checkCertificatePath property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCheckCertificatePath() {
        return checkCertificatePath;
    }

    /**
     * Sets the value of the checkCertificatePath property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCheckCertificatePath(Boolean value) {
        this.checkCertificatePath = value;
    }

    /**
     * Gets the value of the checkCertificateStatus property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCheckCertificateStatus() {
        return checkCertificateStatus;
    }

    /**
     * Sets the value of the checkCertificateStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCheckCertificateStatus(Boolean value) {
        this.checkCertificateStatus = value;
    }

    /**
     * Gets the value of the verifyManifests property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVerifyManifests() {
        return verifyManifests;
    }

    /**
     * Sets the value of the verifyManifests property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVerifyManifests(Boolean value) {
        this.verifyManifests = value;
    }

    /**
     * Gets the value of the checkAlgorithms property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCheckAlgorithms() {
        return checkAlgorithms;
    }

    /**
     * Sets the value of the checkAlgorithms property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCheckAlgorithms(Boolean value) {
        this.checkAlgorithms = value;
    }

}
