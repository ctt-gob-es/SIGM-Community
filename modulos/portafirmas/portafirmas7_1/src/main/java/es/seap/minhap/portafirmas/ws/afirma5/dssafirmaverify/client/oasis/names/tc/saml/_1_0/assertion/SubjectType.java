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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.saml._1_0.assertion;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}NameIdentifier"/>
 *           &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}SubjectConfirmation" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}SubjectConfirmation"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectType", propOrder = {
    "content"
})
public class SubjectType {

    @XmlElementRefs({
        @XmlElementRef(name = "NameIdentifier", namespace = "urn:oasis:names:tc:SAML:1.0:assertion", type = JAXBElement.class),
        @XmlElementRef(name = "SubjectConfirmation", namespace = "urn:oasis:names:tc:SAML:1.0:assertion", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "SubjectConfirmation" is used by two different parts of a schema. See: 
     * line 101 of file:/home/rus/desarrollo/Servicios-DSS-Afirma/esquemas/oasis-sstc-saml-schema-assertion-1.1.xsd
     * line 99 of file:/home/rus/desarrollo/Servicios-DSS-Afirma/esquemas/oasis-sstc-saml-schema-assertion-1.1.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link SubjectConfirmationType }{@code >}
     * {@link JAXBElement }{@code <}{@link NameIdentifierType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

}
