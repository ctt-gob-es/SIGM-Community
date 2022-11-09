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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="CheckOptions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}CheckOptionsType" minOccurs="0"/>
 *         &lt;element name="ReportOptions" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}ReportOptionsType" minOccurs="0"/>
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
    "checkOptions",
    "reportOptions"
})
@XmlRootElement(name = "ReturnVerificationReport")
public class ReturnVerificationReport {

    @XmlElement(name = "CheckOptions")
    protected CheckOptionsType checkOptions;
    @XmlElement(name = "ReportOptions")
    protected ReportOptionsType reportOptions;

    /**
     * Gets the value of the checkOptions property.
     * 
     * @return
     *     possible object is
     *     {@link CheckOptionsType }
     *     
     */
    public CheckOptionsType getCheckOptions() {
        return checkOptions;
    }

    /**
     * Sets the value of the checkOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link CheckOptionsType }
     *     
     */
    public void setCheckOptions(CheckOptionsType value) {
        this.checkOptions = value;
    }

    /**
     * Gets the value of the reportOptions property.
     * 
     * @return
     *     possible object is
     *     {@link ReportOptionsType }
     *     
     */
    public ReportOptionsType getReportOptions() {
        return reportOptions;
    }

    /**
     * Sets the value of the reportOptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReportOptionsType }
     *     
     */
    public void setReportOptions(ReportOptionsType value) {
        this.reportOptions = value;
    }

}
