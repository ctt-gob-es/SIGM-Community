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
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DetailType;


/**
 * <p>Java class for CertificatePathValidityDetailType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CertificatePathValidityDetailType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="CertificateValidity" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CertificateValidityType"/>
 *         &lt;/sequence>
 *         &lt;element name="TSLValidity" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}TrustStatusListValidityType" minOccurs="0"/>
 *         &lt;element name="TrustOrigin" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CertificatePathValidityDetailType", propOrder = {
    "certificateValidity",
    "tslValidity",
    "trustOrigin"
})
public class CertificatePathValidityDetailType {

    @XmlElement(name = "CertificateValidity")
    protected List<CertificateValidityType> certificateValidity;
    @XmlElement(name = "TSLValidity")
    protected TrustStatusListValidityType tslValidity;
    @XmlElement(name = "TrustOrigin", required = true)
    protected DetailType trustOrigin;

    /**
     * Gets the value of the certificateValidity property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the certificateValidity property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCertificateValidity().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CertificateValidityType }
     * 
     * 
     */
    public List<CertificateValidityType> getCertificateValidity() {
        if (certificateValidity == null) {
            certificateValidity = new ArrayList<CertificateValidityType>();
        }
        return this.certificateValidity;
    }

    /**
     * Gets the value of the tslValidity property.
     * 
     * @return
     *     possible object is
     *     {@link TrustStatusListValidityType }
     *     
     */
    public TrustStatusListValidityType getTSLValidity() {
        return tslValidity;
    }

    /**
     * Sets the value of the tslValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link TrustStatusListValidityType }
     *     
     */
    public void setTSLValidity(TrustStatusListValidityType value) {
        this.tslValidity = value;
    }

    /**
     * Gets the value of the trustOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getTrustOrigin() {
        return trustOrigin;
    }

    /**
     * Sets the value of the trustOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setTrustOrigin(DetailType value) {
        this.trustOrigin = value;
    }

}
