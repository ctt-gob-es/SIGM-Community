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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ConditionsType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ConditionsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}AudienceRestrictionCondition"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}DoNotCacheCondition"/>
 *         &lt;element ref="{urn:oasis:names:tc:SAML:1.0:assertion}Condition"/>
 *       &lt;/choice>
 *       &lt;attribute name="NotBefore" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="NotOnOrAfter" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConditionsType", propOrder = {
    "audienceRestrictionConditionOrDoNotCacheConditionOrCondition"
})
public class ConditionsType {

    @XmlElements({
        @XmlElement(name = "Condition"),
        @XmlElement(name = "AudienceRestrictionCondition", type = AudienceRestrictionConditionType.class),
        @XmlElement(name = "DoNotCacheCondition", type = DoNotCacheConditionType.class)
    })
    protected List<ConditionAbstractType> audienceRestrictionConditionOrDoNotCacheConditionOrCondition;
    @XmlAttribute(name = "NotBefore")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar notBefore;
    @XmlAttribute(name = "NotOnOrAfter")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar notOnOrAfter;

    /**
     * Gets the value of the audienceRestrictionConditionOrDoNotCacheConditionOrCondition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the audienceRestrictionConditionOrDoNotCacheConditionOrCondition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAudienceRestrictionConditionOrDoNotCacheConditionOrCondition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ConditionAbstractType }
     * {@link AudienceRestrictionConditionType }
     * {@link DoNotCacheConditionType }
     * 
     * 
     */
    public List<ConditionAbstractType> getAudienceRestrictionConditionOrDoNotCacheConditionOrCondition() {
        if (audienceRestrictionConditionOrDoNotCacheConditionOrCondition == null) {
            audienceRestrictionConditionOrDoNotCacheConditionOrCondition = new ArrayList<ConditionAbstractType>();
        }
        return this.audienceRestrictionConditionOrDoNotCacheConditionOrCondition;
    }

    /**
     * Gets the value of the notBefore property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNotBefore() {
        return notBefore;
    }

    /**
     * Sets the value of the notBefore property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNotBefore(XMLGregorianCalendar value) {
        this.notBefore = value;
    }

    /**
     * Gets the value of the notOnOrAfter property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getNotOnOrAfter() {
        return notOnOrAfter;
    }

    /**
     * Sets the value of the notOnOrAfter property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setNotOnOrAfter(XMLGregorianCalendar value) {
        this.notOnOrAfter = value;
    }

}
