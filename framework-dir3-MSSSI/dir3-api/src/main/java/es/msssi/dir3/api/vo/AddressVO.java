/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.vo;

import es.msssi.dir3.core.vo.Entity;

/**
 * Datos básicos de la dirección una oficina.
 * 
 * @author cmorenog
 * 
 */
public class AddressVO extends Entity {

    private static final long serialVersionUID = 5515960788820716930L;
    /**
     * Id del tipo de vía.
     */
    private String streetTypeId;

    /**
     * Descripción del tipo de vía.
     */
    private String streetTypeName;

    /**
     * Nombre de la vía.
     */
    private String streetName;

    /**
     * Número de la vía.
     */
    private String addressNum;

    /**
     * Información complementaría de la dirección.
     */
    private String addessInformation;

    /**
     * Código postal de la dirección.
     */
    private String postalCode;

    /**
     * Id del país de la dirección.
     */
    private String countryId;

    /**
     * Nombre del país de la dirección.
     */
    private String countryName;

    /**
     * Id de la CCAA de la dirección.
     */
    private String autonomousCommunityId;

    /**
     * Nombre de la CCAA de la dirección.
     */
    private String autonomousCommunityName;

    /**
     * Id de la entidad geográfica de la dirección.
     */
    private String geographicalEntityId;

    /**
     * Descripción de la entidad geográfica de la dirección.
     */
    private String geographicalEntityName;

    /**
     * Id de la provincia de la dirección.
     */
    private String provinceId;

    /**
     * Descripción de la provincia de la dirección.
     */
    private String provinceName;

    /**
     * Id de la localidad de la dirección.
     */
    private String cityId;

    /**
     * Descripción de la dirección.
     */
    private String cityName;

    /**
     * Localidad en la que ejerce las competencias la UNIDAD, cuando el País no
     * es España.
     */
    private String foreignLocation;

    /**
     * Dirección no normalizada para Unidades con sede ubicada en el extranjero.
     */
    private String foreignAddress;

    /**
     * Observaciones de la dirección.
     */
    private String observations;

    /**
     * Obtiene el valor del parámetro streetTypeId.
     * 
     * @return streetTypeId valor del campo a obtener.
     */
    public String getStreetTypeId() {
	return streetTypeId;
    }

    /**
     * Guarda el valor del parámetro streetTypeId.
     * 
     * @param streetTypeId
     *            valor del campo a guardar.
     */
    public void setStreetTypeId(
	String streetTypeId) {
	this.streetTypeId = streetTypeId;
    }

    /**
     * Obtiene el valor del parámetro streetTypeName.
     * 
     * @return streetTypeName valor del campo a obtener.
     */
    public String getStreetTypeName() {
	return streetTypeName;
    }

    /**
     * Guarda el valor del parámetro streetTypeName.
     * 
     * @param streetTypeName
     *            valor del campo a guardar.
     */
    public void setStreetTypeName(
	String streetTypeName) {
	this.streetTypeName = streetTypeName;
    }

    /**
     * Obtiene el valor del parámetro streetName.
     * 
     * @return streetName valor del campo a obtener.
     */
    public String getStreetName() {
	return streetName;
    }

    /**
     * Guarda el valor del parámetro streetName.
     * 
     * @param streetName
     *            valor del campo a guardar.
     */
    public void setStreetName(
	String streetName) {
	this.streetName = streetName;
    }

    /**
     * Obtiene el valor del parámetro addressNum.
     * 
     * @return addressNum valor del campo a obtener.
     */
    public String getAddressNum() {
	return addressNum;
    }

    /**
     * Guarda el valor del parámetro addressNum.
     * 
     * @param addressNum
     *            valor del campo a guardar.
     */
    public void setAddressNum(
	String addressNum) {
	this.addressNum = addressNum;
    }

    /**
     * Obtiene el valor del parámetro addessInformation.
     * 
     * @return addessInformation valor del campo a obtener.
     */
    public String getAddessInformation() {
	return addessInformation;
    }

    /**
     * Guarda el valor del parámetro addessInformation.
     * 
     * @param addessInformation
     *            valor del campo a guardar.
     */
    public void setAddessInformation(
	String addessInformation) {
	this.addessInformation = addessInformation;
    }

    /**
     * Obtiene el valor del parámetro postalCode.
     * 
     * @return postalCode valor del campo a obtener.
     */
    public String getPostalCode() {
	return postalCode;
    }

    /**
     * Guarda el valor del parámetro postalCode.
     * 
     * @param postalCode
     *            valor del campo a guardar.
     */
    public void setPostalCode(
	String postalCode) {
	this.postalCode = postalCode;
    }

    /**
     * Obtiene el valor del parámetro countryId.
     * 
     * @return countryId valor del campo a obtener.
     */
    public String getCountryId() {
	return countryId;
    }

    /**
     * Guarda el valor del parámetro countryId.
     * 
     * @param countryId
     *            valor del campo a guardar.
     */
    public void setCountryId(
	String countryId) {
	this.countryId = countryId;
    }

    /**
     * Obtiene el valor del parámetro countryName.
     * 
     * @return countryName valor del campo a obtener.
     */
    public String getCountryName() {
	return countryName;
    }

    /**
     * Guarda el valor del parámetro countryName.
     * 
     * @param countryName
     *            valor del campo a guardar.
     */
    public void setCountryName(
	String countryName) {
	this.countryName = countryName;
    }

    /**
     * Obtiene el valor del parámetro autonomousCommunityId.
     * 
     * @return autonomousCommunityId valor del campo a obtener.
     */
    public String getAutonomousCommunityId() {
	return autonomousCommunityId;
    }

    /**
     * Guarda el valor del parámetro autonomousCommunityId.
     * 
     * @param autonomousCommunityId
     *            valor del campo a guardar.
     */
    public void setAutonomousCommunityId(
	String autonomousCommunityId) {
	this.autonomousCommunityId = autonomousCommunityId;
    }

    /**
     * Obtiene el valor del parámetro autonomousCommunityName.
     * 
     * @return autonomousCommunityName valor del campo a obtener.
     */
    public String getAutonomousCommunityName() {
	return autonomousCommunityName;
    }

    /**
     * Guarda el valor del parámetro autonomousCommunityName.
     * 
     * @param autonomousCommunityName
     *            valor del campo a guardar.
     */
    public void setAutonomousCommunityName(
	String autonomousCommunityName) {
	this.autonomousCommunityName = autonomousCommunityName;
    }

    /**
     * Obtiene el valor del parámetro geographicalEntityId.
     * 
     * @return geographicalEntityId valor del campo a obtener.
     */
    public String getGeographicalEntityId() {
	return geographicalEntityId;
    }

    /**
     * Guarda el valor del parámetro geographicalEntityId.
     * 
     * @param geographicalEntityId
     *            valor del campo a guardar.
     */
    public void setGeographicalEntityId(
	String geographicalEntityId) {
	this.geographicalEntityId = geographicalEntityId;
    }

    /**
     * Obtiene el valor del parámetro geographicalEntityName.
     * 
     * @return geographicalEntityName valor del campo a obtener.
     */
    public String getGeographicalEntityName() {
	return geographicalEntityName;
    }

    /**
     * Guarda el valor del parámetro geographicalEntityName.
     * 
     * @param geographicalEntityName
     *            valor del campo a guardar.
     */
    public void setGeographicalEntityName(
	String geographicalEntityName) {
	this.geographicalEntityName = geographicalEntityName;
    }

    /**
     * Obtiene el valor del parámetro provinceId.
     * 
     * @return provinceId valor del campo a obtener.
     */
    public String getProvinceId() {
	return provinceId;
    }

    /**
     * Guarda el valor del parámetro provinceId.
     * 
     * @param provinceId
     *            valor del campo a guardar.
     */
    public void setProvinceId(
	String provinceId) {
	this.provinceId = provinceId;
    }

    /**
     * Obtiene el valor del parámetro provinceName.
     * 
     * @return provinceName valor del campo a obtener.
     */
    public String getProvinceName() {
	return provinceName;
    }

    /**
     * Guarda el valor del parámetro provinceName.
     * 
     * @param provinceName
     *            valor del campo a guardar.
     */
    public void setProvinceName(
	String provinceName) {
	this.provinceName = provinceName;
    }

    /**
     * Obtiene el valor del parámetro cityId.
     * 
     * @return cityId valor del campo a obtener.
     */
    public String getCityId() {
	return cityId;
    }

    /**
     * Guarda el valor del parámetro cityId.
     * 
     * @param cityId
     *            valor del campo a guardar.
     */
    public void setCityId(
	String cityId) {
	this.cityId = cityId;
    }

    /**
     * Obtiene el valor del parámetro cityName.
     * 
     * @return cityName valor del campo a obtener.
     */
    public String getCityName() {
	return cityName;
    }

    /**
     * Guarda el valor del parámetro cityName.
     * 
     * @param cityName
     *            valor del campo a guardar.
     */
    public void setCityName(
	String cityName) {
	this.cityName = cityName;
    }

    /**
     * Obtiene el valor del parámetro observations.
     * 
     * @return observations valor del campo a obtener.
     */
    public String getObservations() {
	return observations;
    }

    /**
     * Guarda el valor del parámetro observations.
     * 
     * @param observations
     *            valor del campo a guardar.
     */
    public void setObservations(
	String observations) {
	this.observations = observations;
    }

    /**
     * Obtiene el valor del parámetro foreignLocation.
     * 
     * @return foreignLocation valor del campo a obtener.
     */
    public String getForeignLocation() {
	return foreignLocation;
    }

    /**
     * Guarda el valor del parámetro foreignLocation.
     * 
     * @param foreignLocation
     *            valor del campo a guardar.
     */
    public void setForeignLocation(
	String foreignLocation) {
	this.foreignLocation = foreignLocation;
    }

    /**
     * Obtiene el valor del parámetro foreignAddress.
     * 
     * @return foreignAddress valor del campo a obtener.
     */
    public String getForeignAddress() {
	return foreignAddress;
    }

    /**
     * Guarda el valor del parámetro foreignAddress.
     * 
     * @param foreignAddress
     *            valor del campo a guardar.
     */
    public void setForeignAddress(
	String foreignAddress) {
	this.foreignAddress = foreignAddress;
    }

}
