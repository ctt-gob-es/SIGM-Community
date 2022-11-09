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

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DetailType;


/**
 * <p>Java class for ExtensionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ExtensionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExtnId" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *         &lt;element name="Critical" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ExtnValue" type="{urn:oasis:names:tc:dss:1.0:core:schema}DetailType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtensionType", propOrder = {
    "extnId",
    "critical",
    "extnValue"
})
public class ExtensionType {

    @XmlElement(name = "ExtnId", required = true)
    protected DetailType extnId;
    @XmlElement(name = "Critical", defaultValue = "false")
    protected boolean critical;
    @XmlElement(name = "ExtnValue", required = true)
    protected DetailType extnValue;

    /**
     * Gets the value of the extnId property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getExtnId() {
        return extnId;
    }

    /**
     * Sets the value of the extnId property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setExtnId(DetailType value) {
        this.extnId = value;
    }

    /**
     * Gets the value of the critical property.
     * 
     */
    public boolean isCritical() {
        return critical;
    }

    /**
     * Sets the value of the critical property.
     * 
     */
    public void setCritical(boolean value) {
        this.critical = value;
    }

    /**
     * Gets the value of the extnValue property.
     * 
     * @return
     *     possible object is
     *     {@link DetailType }
     *     
     */
    public DetailType getExtnValue() {
        return extnValue;
    }

    /**
     * Sets the value of the extnValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link DetailType }
     *     
     */
    public void setExtnValue(DetailType value) {
        this.extnValue = value;
    }

}
