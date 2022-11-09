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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.org.etsi.uri._01903.v1_3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObjectIdentifierType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectIdentifierType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Identifier" type="{http://uri.etsi.org/01903/v1.3.2#}IdentifierType"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DocumentationReferences" type="{http://uri.etsi.org/01903/v1.3.2#}DocumentationReferencesType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectIdentifierType", propOrder = {
    "identifier",
    "description",
    "documentationReferences"
})
public class ObjectIdentifierType {

    @XmlElement(name = "Identifier", required = true)
    protected IdentifierType identifier;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "DocumentationReferences")
    protected DocumentationReferencesType documentationReferences;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link IdentifierType }
     *     
     */
    public IdentifierType getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentifierType }
     *     
     */
    public void setIdentifier(IdentifierType value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the documentationReferences property.
     * 
     * @return
     *     possible object is
     *     {@link DocumentationReferencesType }
     *     
     */
    public DocumentationReferencesType getDocumentationReferences() {
        return documentationReferences;
    }

    /**
     * Sets the value of the documentationReferences property.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentationReferencesType }
     *     
     */
    public void setDocumentationReferences(DocumentationReferencesType value) {
        this.documentationReferences = value;
    }

}
