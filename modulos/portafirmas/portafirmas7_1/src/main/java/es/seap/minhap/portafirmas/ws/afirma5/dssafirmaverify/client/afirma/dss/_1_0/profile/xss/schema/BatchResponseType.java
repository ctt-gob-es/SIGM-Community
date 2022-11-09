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


package es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.afirma.dss._1_0.profile.xss.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.ResponseBaseType;


/**
 * <p>Java class for BatchResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BatchResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:oasis:names:tc:dss:1.0:core:schema}ResponseBaseType">
 *       &lt;sequence>
 *         &lt;element ref="{urn:afirma:dss:1.0:profile:XSS:schema}Responses"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchResponseType", propOrder = {
    "responses"
})
public class BatchResponseType
    extends ResponseBaseType
{

    @XmlElement(name = "Responses", required = true)
    protected Responses responses;
    @XmlAttribute(name = "Type")
    @XmlSchemaType(name = "anySimpleType")
    protected String type;

    /**
     * Gets the value of the responses property.
     * 
     * @return
     *     possible object is
     *     {@link Responses }
     *     
     */
    public Responses getResponses() {
        return responses;
    }

    /**
     * Sets the value of the responses property.
     * 
     * @param value
     *     allowed object is
     *     {@link Responses }
     *     
     */
    public void setResponses(Responses value) {
        this.responses = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

}
