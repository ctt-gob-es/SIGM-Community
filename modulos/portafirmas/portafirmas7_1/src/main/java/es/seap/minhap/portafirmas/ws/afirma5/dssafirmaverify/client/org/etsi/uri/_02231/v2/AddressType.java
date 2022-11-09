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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}PostalAddresses"/>
 *         &lt;element ref="{http://uri.etsi.org/02231/v2#}ElectronicAddress"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
    "postalAddresses",
    "electronicAddress"
})
public class AddressType {

    @XmlElement(name = "PostalAddresses", required = true)
    protected PostalAddressListType postalAddresses;
    @XmlElement(name = "ElectronicAddress", required = true)
    protected ElectronicAddressType electronicAddress;

    /**
     * Gets the value of the postalAddresses property.
     * 
     * @return
     *     possible object is
     *     {@link PostalAddressListType }
     *     
     */
    public PostalAddressListType getPostalAddresses() {
        return postalAddresses;
    }

    /**
     * Sets the value of the postalAddresses property.
     * 
     * @param value
     *     allowed object is
     *     {@link PostalAddressListType }
     *     
     */
    public void setPostalAddresses(PostalAddressListType value) {
        this.postalAddresses = value;
    }

    /**
     * Gets the value of the electronicAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ElectronicAddressType }
     *     
     */
    public ElectronicAddressType getElectronicAddress() {
        return electronicAddress;
    }

    /**
     * Sets the value of the electronicAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElectronicAddressType }
     *     
     */
    public void setElectronicAddress(ElectronicAddressType value) {
        this.electronicAddress = value;
    }

}
