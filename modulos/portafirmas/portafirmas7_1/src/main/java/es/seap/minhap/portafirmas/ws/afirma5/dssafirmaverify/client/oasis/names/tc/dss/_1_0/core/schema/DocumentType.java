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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DocumentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DocumentType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:dss:1.0:core:schema}DocumentBaseType">
 *       &lt;choice>
 *         &lt;element name="InlineXML" type="{urn:oasis:names:tc:dss:1.0:core:schema}InlineXMLType"/>
 *         &lt;element name="Base64XML" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="EscapedXML" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}Base64Data"/>
 *         &lt;element ref="{urn:oasis:names:tc:dss:1.0:core:schema}AttachmentReference"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentType", propOrder = {
    "inlineXML",
    "base64XML",
    "escapedXML",
    "base64Data",
    "attachmentReference"
})
public class DocumentType
    extends DocumentBaseType
{

    @XmlElement(name = "InlineXML")
    protected InlineXMLType inlineXML;
    @XmlElement(name = "Base64XML")
    protected byte[] base64XML;
    @XmlElement(name = "EscapedXML")
    protected String escapedXML;
    @XmlElement(name = "Base64Data")
    protected Base64Data base64Data;
    @XmlElement(name = "AttachmentReference")
    protected AttachmentReferenceType attachmentReference;

    /**
     * Gets the value of the inlineXML property.
     * 
     * @return
     *     possible object is
     *     {@link InlineXMLType }
     *     
     */
    public InlineXMLType getInlineXML() {
        return inlineXML;
    }

    /**
     * Sets the value of the inlineXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link InlineXMLType }
     *     
     */
    public void setInlineXML(InlineXMLType value) {
        this.inlineXML = value;
    }

    /**
     * Gets the value of the base64XML property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBase64XML() {
        return base64XML;
    }

    /**
     * Sets the value of the base64XML property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBase64XML(byte[] value) {
        this.base64XML = ((byte[]) value);
    }

    /**
     * Gets the value of the escapedXML property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEscapedXML() {
        return escapedXML;
    }

    /**
     * Sets the value of the escapedXML property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEscapedXML(String value) {
        this.escapedXML = value;
    }

    /**
     * Gets the value of the base64Data property.
     * 
     * @return
     *     possible object is
     *     {@link Base64Data }
     *     
     */
    public Base64Data getBase64Data() {
        return base64Data;
    }

    /**
     * Sets the value of the base64Data property.
     * 
     * @param value
     *     allowed object is
     *     {@link Base64Data }
     *     
     */
    public void setBase64Data(Base64Data value) {
        this.base64Data = value;
    }

    /**
     * Gets the value of the attachmentReference property.
     * 
     * @return
     *     possible object is
     *     {@link AttachmentReferenceType }
     *     
     */
    public AttachmentReferenceType getAttachmentReference() {
        return attachmentReference;
    }

    /**
     * Sets the value of the attachmentReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link AttachmentReferenceType }
     *     
     */
    public void setAttachmentReference(AttachmentReferenceType value) {
        this.attachmentReference = value;
    }

}
