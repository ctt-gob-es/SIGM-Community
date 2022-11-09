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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;choice>
 *         &lt;element name="XPathAfter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="XPathFirstChildOf" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *       &lt;attribute name="WhichDocument" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="CreateEnvelopedSignature" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "xPathAfter",
    "xPathFirstChildOf"
})
@XmlRootElement(name = "SignaturePlacement")
public class SignaturePlacement {

    @XmlElement(name = "XPathAfter")
    protected String xPathAfter;
    @XmlElement(name = "XPathFirstChildOf")
    protected String xPathFirstChildOf;
    @XmlAttribute(name = "WhichDocument")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object whichDocument;
    @XmlAttribute(name = "CreateEnvelopedSignature")
    protected Boolean createEnvelopedSignature;

    /**
     * Gets the value of the xPathAfter property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXPathAfter() {
        return xPathAfter;
    }

    /**
     * Sets the value of the xPathAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXPathAfter(String value) {
        this.xPathAfter = value;
    }

    /**
     * Gets the value of the xPathFirstChildOf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXPathFirstChildOf() {
        return xPathFirstChildOf;
    }

    /**
     * Sets the value of the xPathFirstChildOf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXPathFirstChildOf(String value) {
        this.xPathFirstChildOf = value;
    }

    /**
     * Gets the value of the whichDocument property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getWhichDocument() {
        return whichDocument;
    }

    /**
     * Sets the value of the whichDocument property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setWhichDocument(Object value) {
        this.whichDocument = value;
    }

    /**
     * Gets the value of the createEnvelopedSignature property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCreateEnvelopedSignature() {
        if (createEnvelopedSignature == null) {
            return true;
        } else {
            return createEnvelopedSignature;
        }
    }

    /**
     * Sets the value of the createEnvelopedSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCreateEnvelopedSignature(Boolean value) {
        this.createEnvelopedSignature = value;
    }

}
