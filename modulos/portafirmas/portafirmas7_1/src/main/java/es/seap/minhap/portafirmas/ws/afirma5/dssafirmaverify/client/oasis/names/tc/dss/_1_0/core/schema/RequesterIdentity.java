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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion.NameIdentifierType;


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
 *         &lt;element name="Name" type="{urn:oasis:names:tc:SAML:1.0:assertion}NameIdentifierType"/>
 *         &lt;element name="SupportingInfo" type="{urn:oasis:names:tc:dss:1.0:core:schema}AnyType" minOccurs="0"/>
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
    "name",
    "supportingInfo"
})
@XmlRootElement(name = "RequesterIdentity")
public class RequesterIdentity {

    @XmlElement(name = "Name", required = true)
    protected NameIdentifierType name;
    @XmlElement(name = "SupportingInfo")
    protected AnyType supportingInfo;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link NameIdentifierType }
     *     
     */
    public NameIdentifierType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link NameIdentifierType }
     *     
     */
    public void setName(NameIdentifierType value) {
        this.name = value;
    }

    /**
     * Gets the value of the supportingInfo property.
     * 
     * @return
     *     possible object is
     *     {@link AnyType }
     *     
     */
    public AnyType getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * Sets the value of the supportingInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnyType }
     *     
     */
    public void setSupportingInfo(AnyType value) {
        this.supportingInfo = value;
    }

}
