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

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttrCertIDType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttrCertIDType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Holder" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}EntityType" minOccurs="0"/>
 *         &lt;element name="Issuer" type="{urn:oasis:names:tc:dss:1.0:profiles:verificationreport:schema#}EntityType"/>
 *         &lt;element name="SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttrCertIDType", propOrder = {
    "holder",
    "issuer",
    "serialNumber"
})
public class AttrCertIDType {

    @XmlElement(name = "Holder")
    protected EntityType holder;
    @XmlElement(name = "Issuer", required = true)
    protected EntityType issuer;
    @XmlElement(name = "SerialNumber", required = true)
    protected BigInteger serialNumber;

    /**
     * Gets the value of the holder property.
     * 
     * @return
     *     possible object is
     *     {@link EntityType }
     *     
     */
    public EntityType getHolder() {
        return holder;
    }

    /**
     * Sets the value of the holder property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityType }
     *     
     */
    public void setHolder(EntityType value) {
        this.holder = value;
    }

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link EntityType }
     *     
     */
    public EntityType getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link EntityType }
     *     
     */
    public void setIssuer(EntityType value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the serialNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    /**
     * Sets the value of the serialNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSerialNumber(BigInteger value) {
        this.serialNumber = value;
    }

}
