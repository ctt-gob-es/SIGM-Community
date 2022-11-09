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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for SigningTimeInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SigningTimeInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="SigningTimeBoundaries" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LowerBoundary" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element name="UpperBoundary" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SigningTimeInfoType", propOrder = {
    "signingTime",
    "signingTimeBoundaries"
})
public class SigningTimeInfoType {

    @XmlElement(name = "SigningTime", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar signingTime;
    @XmlElement(name = "SigningTimeBoundaries")
    protected SigningTimeInfoType.SigningTimeBoundaries signingTimeBoundaries;

    /**
     * Gets the value of the signingTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSigningTime() {
        return signingTime;
    }

    /**
     * Sets the value of the signingTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSigningTime(XMLGregorianCalendar value) {
        this.signingTime = value;
    }

    /**
     * Gets the value of the signingTimeBoundaries property.
     * 
     * @return
     *     possible object is
     *     {@link SigningTimeInfoType.SigningTimeBoundaries }
     *     
     */
    public SigningTimeInfoType.SigningTimeBoundaries getSigningTimeBoundaries() {
        return signingTimeBoundaries;
    }

    /**
     * Sets the value of the signingTimeBoundaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link SigningTimeInfoType.SigningTimeBoundaries }
     *     
     */
    public void setSigningTimeBoundaries(SigningTimeInfoType.SigningTimeBoundaries value) {
        this.signingTimeBoundaries = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="LowerBoundary" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *         &lt;element name="UpperBoundary" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "lowerBoundary",
        "upperBoundary"
    })
    public static class SigningTimeBoundaries {

        @XmlElement(name = "LowerBoundary")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar lowerBoundary;
        @XmlElement(name = "UpperBoundary")
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar upperBoundary;

        /**
         * Gets the value of the lowerBoundary property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getLowerBoundary() {
            return lowerBoundary;
        }

        /**
         * Sets the value of the lowerBoundary property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setLowerBoundary(XMLGregorianCalendar value) {
            this.lowerBoundary = value;
        }

        /**
         * Gets the value of the upperBoundary property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getUpperBoundary() {
            return upperBoundary;
        }

        /**
         * Sets the value of the upperBoundary property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setUpperBoundary(XMLGregorianCalendar value) {
            this.upperBoundary = value;
        }

    }

}
