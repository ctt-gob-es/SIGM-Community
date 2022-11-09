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

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for enhancedUserJobInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="enhancedUserJobInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="seat" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}seat" minOccurs="0"/>
 *         &lt;element name="valid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="visibleOtherSeats" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="parameterList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}parameterList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "enhancedUserJobInfo", propOrder = {
    "seat",
    "valid",
    "visibleOtherSeats",
    "parameterList"
})
public class EnhancedUserJobInfo {

    @XmlElementRef(name = "seat", type = JAXBElement.class, required = false)
    protected JAXBElement<Seat> seat;
    @XmlElementRef(name = "valid", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> valid;
    @XmlElementRef(name = "visibleOtherSeats", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> visibleOtherSeats;
    @XmlElementRef(name = "parameterList", type = JAXBElement.class, required = false)
    protected JAXBElement<ParameterList> parameterList;

    /**
     * Gets the value of the seat property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Seat }{@code >}
     *     
     */
    public JAXBElement<Seat> getSeat() {
        return seat;
    }

    /**
     * Sets the value of the seat property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Seat }{@code >}
     *     
     */
    public void setSeat(JAXBElement<Seat> value) {
        this.seat = ((JAXBElement<Seat> ) value);
    }

    /**
     * Gets the value of the valid property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getValid() {
        return valid;
    }

    /**
     * Sets the value of the valid property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setValid(JAXBElement<Boolean> value) {
        this.valid = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the visibleOtherSeats property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getVisibleOtherSeats() {
        return visibleOtherSeats;
    }

    /**
     * Sets the value of the visibleOtherSeats property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setVisibleOtherSeats(JAXBElement<Boolean> value) {
        this.visibleOtherSeats = ((JAXBElement<Boolean> ) value);
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ParameterList }{@code >}
     *     
     */
    public JAXBElement<ParameterList> getParameterList() {
        return parameterList;
    }

    /**
     * Sets the value of the parameterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ParameterList }{@code >}
     *     
     */
    public void setParameterList(JAXBElement<ParameterList> value) {
        this.parameterList = ((JAXBElement<ParameterList> ) value);
    }

}
