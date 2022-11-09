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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.activation.DataHandler;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for signature complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="signature">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sign" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="signFormat" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}signFormat" minOccurs="0"/>
 *         &lt;element name="content" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signature", propOrder = {
    "identifier",
    "sign",
    "signFormat",
    "content"
})
public class Signature {

    @XmlElementRef(name = "identifier", type = JAXBElement.class, required = false)
    protected JAXBElement<String> identifier;
    @XmlElementRef(name = "sign", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> sign;
    @XmlElementRef(name = "signFormat", type = JAXBElement.class, required = false)
    protected JAXBElement<SignFormat> signFormat;
    @XmlElementRef(name = "content", type = JAXBElement.class, required = false)
    protected JAXBElement<DataHandler> content;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdentifier(JAXBElement<String> value) {
        this.identifier = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the sign property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getSign() {
        return sign;
    }

    /**
     * Sets the value of the sign property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setSign(JAXBElement<Boolean> value) {
        this.sign = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the signFormat property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SignFormat }{@code >}
     *     
     */
    public JAXBElement<SignFormat> getSignFormat() {
        return signFormat;
    }

    /**
     * Sets the value of the signFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SignFormat }{@code >}
     *     
     */
    public void setSignFormat(JAXBElement<SignFormat> value) {
        this.signFormat = ((JAXBElement<SignFormat> ) value);
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DataHandler }{@code >}
     *     
     */
    public JAXBElement<DataHandler> getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DataHandler }{@code >}
     *     
     */
    public void setContent(JAXBElement<DataHandler> value) {
        this.content = ((JAXBElement<DataHandler> ) value);
    }

}
