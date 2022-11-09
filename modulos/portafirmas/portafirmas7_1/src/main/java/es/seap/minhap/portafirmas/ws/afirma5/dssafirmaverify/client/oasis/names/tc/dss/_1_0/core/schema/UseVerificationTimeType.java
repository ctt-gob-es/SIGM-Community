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
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for UseVerificationTimeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UseVerificationTimeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="CurrentTime" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="SpecificTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;any namespace='##other'/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UseVerificationTimeType", propOrder = {
    "currentTime",
    "specificTime",
    "any"
})
public class UseVerificationTimeType {

    @XmlElement(name = "CurrentTime")
    protected Object currentTime;
    @XmlElement(name = "SpecificTime")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar specificTime;
    @XmlAnyElement(lax = true)
    protected Object any;

    /**
     * Gets the value of the currentTime property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getCurrentTime() {
        return currentTime;
    }

    /**
     * Sets the value of the currentTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setCurrentTime(Object value) {
        this.currentTime = value;
    }

    /**
     * Gets the value of the specificTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSpecificTime() {
        return specificTime;
    }

    /**
     * Sets the value of the specificTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSpecificTime(XMLGregorianCalendar value) {
        this.specificTime = value;
    }

    /**
     * Gets the value of the any property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getAny() {
        return any;
    }

    /**
     * Sets the value of the any property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setAny(Object value) {
        this.any = value;
    }

}
