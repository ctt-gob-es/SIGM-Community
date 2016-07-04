/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for address complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="address">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir3.msssi.es/}entity">
 *       &lt;sequence>
 *         &lt;element name="addessInformation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addressNum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autonomousCommunityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autonomousCommunityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="geographicalEntityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="geographicalEntityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="streetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="streetTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="streetTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "address", propOrder = { "addessInformation", "addressNum",
    "autonomousCommunityId", "autonomousCommunityName", "cityId", "cityName", "countryId",
    "countryName", "geographicalEntityId", "geographicalEntityName", "observations", "postalCode",
    "provinceId", "provinceName", "streetName", "streetTypeId", "streetTypeName" })
public class Address extends Entity {

    protected String addessInformation;
    protected String addressNum;
    protected String autonomousCommunityId;
    protected String autonomousCommunityName;
    protected String cityId;
    protected String cityName;
    protected String countryId;
    protected String countryName;
    protected String geographicalEntityId;
    protected String geographicalEntityName;
    protected String observations;
    protected String postalCode;
    protected String provinceId;
    protected String provinceName;
    protected String streetName;
    protected String streetTypeId;
    protected String streetTypeName;

    /**
     * Gets the value of the addessInformation property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAddessInformation() {
	return addessInformation;
    }

    /**
     * Sets the value of the addessInformation property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAddessInformation(
	String value) {
	this.addessInformation = value;
    }

    /**
     * Gets the value of the addressNum property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAddressNum() {
	return addressNum;
    }

    /**
     * Sets the value of the addressNum property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAddressNum(
	String value) {
	this.addressNum = value;
    }

    /**
     * Gets the value of the autonomousCommunityId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAutonomousCommunityId() {
	return autonomousCommunityId;
    }

    /**
     * Sets the value of the autonomousCommunityId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAutonomousCommunityId(
	String value) {
	this.autonomousCommunityId = value;
    }

    /**
     * Gets the value of the autonomousCommunityName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAutonomousCommunityName() {
	return autonomousCommunityName;
    }

    /**
     * Sets the value of the autonomousCommunityName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAutonomousCommunityName(
	String value) {
	this.autonomousCommunityName = value;
    }

    /**
     * Gets the value of the cityId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCityId() {
	return cityId;
    }

    /**
     * Sets the value of the cityId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCityId(
	String value) {
	this.cityId = value;
    }

    /**
     * Gets the value of the cityName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCityName() {
	return cityName;
    }

    /**
     * Sets the value of the cityName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCityName(
	String value) {
	this.cityName = value;
    }

    /**
     * Gets the value of the countryId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCountryId() {
	return countryId;
    }

    /**
     * Sets the value of the countryId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCountryId(
	String value) {
	this.countryId = value;
    }

    /**
     * Gets the value of the countryName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCountryName() {
	return countryName;
    }

    /**
     * Sets the value of the countryName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCountryName(
	String value) {
	this.countryName = value;
    }

    /**
     * Gets the value of the geographicalEntityId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGeographicalEntityId() {
	return geographicalEntityId;
    }

    /**
     * Sets the value of the geographicalEntityId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGeographicalEntityId(
	String value) {
	this.geographicalEntityId = value;
    }

    /**
     * Gets the value of the geographicalEntityName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGeographicalEntityName() {
	return geographicalEntityName;
    }

    /**
     * Sets the value of the geographicalEntityName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGeographicalEntityName(
	String value) {
	this.geographicalEntityName = value;
    }

    /**
     * Gets the value of the observations property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getObservations() {
	return observations;
    }

    /**
     * Sets the value of the observations property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setObservations(
	String value) {
	this.observations = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPostalCode() {
	return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPostalCode(
	String value) {
	this.postalCode = value;
    }

    /**
     * Gets the value of the provinceId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProvinceId() {
	return provinceId;
    }

    /**
     * Sets the value of the provinceId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProvinceId(
	String value) {
	this.provinceId = value;
    }

    /**
     * Gets the value of the provinceName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProvinceName() {
	return provinceName;
    }

    /**
     * Sets the value of the provinceName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProvinceName(
	String value) {
	this.provinceName = value;
    }

    /**
     * Gets the value of the streetName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStreetName() {
	return streetName;
    }

    /**
     * Sets the value of the streetName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStreetName(
	String value) {
	this.streetName = value;
    }

    /**
     * Gets the value of the streetTypeId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStreetTypeId() {
	return streetTypeId;
    }

    /**
     * Sets the value of the streetTypeId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStreetTypeId(
	String value) {
	this.streetTypeId = value;
    }

    /**
     * Gets the value of the streetTypeName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStreetTypeName() {
	return streetTypeName;
    }

    /**
     * Sets the value of the streetTypeName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStreetTypeName(
	String value) {
	this.streetTypeName = value;
    }

}
