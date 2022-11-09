
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para InteresadoExpediente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="InteresadoExpediente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="indPrincipal" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="mobilePhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nifcif" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="notificationAddressType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="phone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="placeCity" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="postalAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="regionCountry" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="telematicAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="thirdPartyId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InteresadoExpediente", propOrder = {
    "indPrincipal",
    "mobilePhone",
    "name",
    "nifcif",
    "notificationAddressType",
    "phone",
    "placeCity",
    "postalAddress",
    "postalCode",
    "regionCountry",
    "telematicAddress",
    "thirdPartyId"
})
public class InteresadoExpediente {

    @XmlElement(required = true, nillable = true)
    protected String indPrincipal;
    @XmlElement(required = true, nillable = true)
    protected String mobilePhone;
    @XmlElement(required = true, nillable = true)
    protected String name;
    @XmlElement(required = true, nillable = true)
    protected String nifcif;
    @XmlElement(required = true, nillable = true)
    protected String notificationAddressType;
    @XmlElement(required = true, nillable = true)
    protected String phone;
    @XmlElement(required = true, nillable = true)
    protected String placeCity;
    @XmlElement(required = true, nillable = true)
    protected String postalAddress;
    @XmlElement(required = true, nillable = true)
    protected String postalCode;
    @XmlElement(required = true, nillable = true)
    protected String regionCountry;
    @XmlElement(required = true, nillable = true)
    protected String telematicAddress;
    @XmlElement(required = true, nillable = true)
    protected String thirdPartyId;

    /**
     * Obtiene el valor de la propiedad indPrincipal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndPrincipal() {
        return indPrincipal;
    }

    /**
     * Define el valor de la propiedad indPrincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndPrincipal(String value) {
        this.indPrincipal = value;
    }

    /**
     * Obtiene el valor de la propiedad mobilePhone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * Define el valor de la propiedad mobilePhone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhone(String value) {
        this.mobilePhone = value;
    }

    /**
     * Obtiene el valor de la propiedad name.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Define el valor de la propiedad name.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Obtiene el valor de la propiedad nifcif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifcif() {
        return nifcif;
    }

    /**
     * Define el valor de la propiedad nifcif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifcif(String value) {
        this.nifcif = value;
    }

    /**
     * Obtiene el valor de la propiedad notificationAddressType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotificationAddressType() {
        return notificationAddressType;
    }

    /**
     * Define el valor de la propiedad notificationAddressType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotificationAddressType(String value) {
        this.notificationAddressType = value;
    }

    /**
     * Obtiene el valor de la propiedad phone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Define el valor de la propiedad phone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * Obtiene el valor de la propiedad placeCity.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlaceCity() {
        return placeCity;
    }

    /**
     * Define el valor de la propiedad placeCity.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlaceCity(String value) {
        this.placeCity = value;
    }

    /**
     * Obtiene el valor de la propiedad postalAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalAddress() {
        return postalAddress;
    }

    /**
     * Define el valor de la propiedad postalAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalAddress(String value) {
        this.postalAddress = value;
    }

    /**
     * Obtiene el valor de la propiedad postalCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Define el valor de la propiedad postalCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Obtiene el valor de la propiedad regionCountry.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegionCountry() {
        return regionCountry;
    }

    /**
     * Define el valor de la propiedad regionCountry.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegionCountry(String value) {
        this.regionCountry = value;
    }

    /**
     * Obtiene el valor de la propiedad telematicAddress.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelematicAddress() {
        return telematicAddress;
    }

    /**
     * Define el valor de la propiedad telematicAddress.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelematicAddress(String value) {
        this.telematicAddress = value;
    }

    /**
     * Obtiene el valor de la propiedad thirdPartyId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getThirdPartyId() {
        return thirdPartyId;
    }

    /**
     * Define el valor de la propiedad thirdPartyId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setThirdPartyId(String value) {
        this.thirdPartyId = value;
    }

}
