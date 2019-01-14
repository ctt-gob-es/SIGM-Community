/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.vo;

import java.util.Date;
import java.util.List;

/**
 * Datos básicos de una unidad orgánica.
 * 
 * @author cmorenog
 * 
 */
public class Unit extends BasicDataUnit {

    private static final long serialVersionUID = 2776750599101372424L;

    /*
     * =======================================================================
     * Datos identificativos
     * 
     * El campo "id" heredado de la clase Entity es el código único.
     * =======================================================================
     */

    /**
     * Siglas de la unidad.
     */
    private String acronyms;
    /**
     * Código del nivel de administración (Estatal, Autonómica o Local).
     */
    private String administrationLevelId;

    /**
     * Descripción del nivel de administración.
     */
    private String administrationLevelName;

    /**
     * Indicador de si la unidad pertenece a una Entidad de Derecho Público.
     */
    private String entityPublicLawIndicator;

    /**
     * Código externo utilizado por la entidad pública que aporta los datos al
     * Directorio Común.
     */
    private String externalId;

    /*
     * =======================================================================
     * Datos de dependencia jerárquica
     * =======================================================================
     */

    /**
     * Unidad raíz (máximo nivel jerárquico en la entidad pública a la que
     * pertenece la unidad).
     */
    private String principalUnitId;

    /**
     * Denominación de la unidad raíz (máximo nivel jerárquico en la entidad
     * pública a la que pertenece la unidad).
     */
    private String principalUnitName;

    /**
     * Unidad raíz que representa a la Administración a la que está vinculada
     * una Entidad de Derecho Público.
     */
    private String entityUnitOfPublicLawId;

    /**
     * Denominación de la unidad raíz que representa a la Administración a la
     * que está vinculada una Entidad de Derecho Público.
     */
    private String entityUnitOfPublicLawName;

    /**
     * Nivel jerárquico en la estructura de la entidad pública (secuencial).
     */
    private Integer publicEntityLevel;

    /**
     * Id de tipo de entidad pública.
     */
    private String publicEntityTypeId;

    /**
     * Descripción de tipo de entidad pública.
     */
    private String publicEntityTypeName;
    /**
     * 
     * Id de tipo de unidad.
     */
    private String unitTypeId;

    /**
     * Descripción de tipo de unidad.
     */
    private String unitTypeName;

    /**
     * Id del poder.
     */
    private Integer authorityId;
    /**
     * Descripción del poder.
     */
    private String authorityName;

    /*
     * Datos de Locaclización de la Unidad Orgánica
     */
    /**
     * Si tiene la misma dirección que su unidad superior.
     */
    private String bSameAddressFatherUnit;
    /**
     * Dirección de la unidad.
     */
    private Address address;

    /*
     * =======================================================================
     * Datos de vigencia
     * =======================================================================
     */

    /**
     * Fecha de creación oficial.
     */
    private Date addOfficialDate;

    /**
     * Fecha de supresión oficial.
     */
    private Date deleteOfficialDate;

    /**
     * Fecha de extinción final.
     */
    private Date extinctionDate;

    /**
     * Fecha de anulación.
     */
    private Date annulationDate;

    /*
     * Datos de observaciones
     */
    /**
     * Observaciones generales.
     */
    private String generalObservations;
    /**
     * Observaciones de baja.
     */
    private String deleteObservations;
    /**
     * Observaciones de contacto.
     */
    private String contactObservations;

    /*
     * Ámbito Territorial de Ejercicio de Competencias
     */
    /**
     * Id del Ámbito Territorial de Ejercicio de Competencias.
     */
    private String territorialScopeOfCompetenceId;
    /**
     * Descripción del Ámbito Territorial de Ejercicio de Competencias.
     */
    private String territorialScopeOfCompetenceName;

    /**
     * Id de la entidad geográfica.
     */
    private String geographicalEntityId;
    /**
     * Descripción de la entidad geográfica.
     */
    private String geographicalEntityName;
    /**
     * Id del país.
     */
    private String countryId;
    /**
     * Descripción del país.
     */
    private String countryName;
    /**
     * Id de la CCAA.
     */
    private String autonomousCommunityId;
    /**
     * Descripción de la CCAA.
     */
    private String autonomousCommunityName;
    /**
     * Id de la provincia.
     */
    private String provinceId;
    /**
     * Descripción de la provincia.
     */
    private String provinceName;
    /**
     * Id de la isla.
     */
    private String islandId;
    /**
     * Descripción de la isla.
     */
    private String islandName;
    /**
     * Id del municipio.
     */
    private String cityId;
    /**
     * Descripción del municipio.
     */
    private String cityName;
    /**
     * Id de la entidad local menor.
     */
    private String minorLocalEntityId;
    /**
     * Descripción de la entidad local menor.
     */
    private String minorLocalEntityName;
    /**
     * Id de la localidad extranjera.
     */
    private String foreignLocation;
    /**
     * Descripción de las funciones que corresponden a la Unidad Orgánica.
     */
    private String competencesOfUnit;
    /**
     * Identificación de la Disposición Legal que regula las competencias de la
     * Unidad.
     */
    private String legalProvisionOfCompetencesOfUnit;
    /**
     * Id del organismo raiz.
     */
    private String rootUnit;

    /**
     * el cif de la unidad.
     */
    private String cif;

    /**
     * Código del dioma.
     */
    private Integer language;
    /**
     * Oficina que da servicio de sir a la unidad.
     */
    private BasicDataOffice relationshipsSIRUO;
    /**
     * Lista de oficinas relacionadas con la unidad.
     */
    private List<BasicDataOffice> organizationalRelationshipsUO;

    /**
     * Lista de datos de contacto de la oficina.
     */
    private List<Contact> contacts;
    /**
     * Fecha de alta en el sistema.
     */
    private Date systemCreationDate;

    /**
     * Fecha de la última actualización.
     */
    private Date lastUpdateDate;

    /**
     * Constructor.
     */
    public Unit() {
	super();
    }

    /**
     * Obtiene el valor del parámetro acronyms.
     * 
     * @return acronyms valor del campo a obtener.
     */
    public String getAcronyms() {
	return acronyms;
    }

    /**
     * Guarda el valor del parámetro acronyms.
     * 
     * @param acronyms
     *            valor del campo a guardar.
     */
    public void setAcronyms(
	String acronyms) {
	this.acronyms = acronyms;
    }

    /**
     * Obtiene el valor del parámetro administrationLevelId.
     * 
     * @return administrationLevelId valor del campo a obtener.
     */
    public String getAdministrationLevelId() {
	return administrationLevelId;
    }

    /**
     * Guarda el valor del parámetro administrationLevelId.
     * 
     * @param administrationLevelId
     *            valor del campo a guardar.
     */
    public void setAdministrationLevelId(
	String administrationLevelId) {
	this.administrationLevelId = administrationLevelId;
    }

    /**
     * Obtiene el valor del parámetro administrationLevelName.
     * 
     * @return administrationLevelName valor del campo a obtener.
     */
    public String getAdministrationLevelName() {
	return administrationLevelName;
    }

    /**
     * Guarda el valor del parámetro administrationLevelName.
     * 
     * @param administrationLevelName
     *            valor del campo a guardar.
     */
    public void setAdministrationLevelName(
	String administrationLevelName) {
	this.administrationLevelName = administrationLevelName;
    }

    /**
     * Obtiene el valor del parámetro entityPublicLawIndicator.
     * 
     * @return entityPublicLawIndicator valor del campo a obtener.
     */
    public String getEntityPublicLawIndicator() {
	return entityPublicLawIndicator;
    }

    /**
     * Guarda el valor del parámetro entityPublicLawIndicator.
     * 
     * @param entityPublicLawIndicator
     *            valor del campo a guardar.
     */
    public void setEntityPublicLawIndicator(
	String entityPublicLawIndicator) {
	this.entityPublicLawIndicator = entityPublicLawIndicator;
    }

    /**
     * Obtiene el valor del parámetro externalId.
     * 
     * @return externalId valor del campo a obtener.
     */
    public String getExternalId() {
	return externalId;
    }

    /**
     * Guarda el valor del parámetro externalId.
     * 
     * @param externalId
     *            valor del campo a guardar.
     */
    public void setExternalId(
	String externalId) {
	this.externalId = externalId;
    }

    /**
     * Obtiene el valor del parámetro principalUnitId.
     * 
     * @return principalUnitId valor del campo a obtener.
     */
    public String getPrincipalUnitId() {
	return principalUnitId;
    }

    /**
     * Guarda el valor del parámetro principalUnitId.
     * 
     * @param principalUnitId
     *            valor del campo a guardar.
     */
    public void setPrincipalUnitId(
	String principalUnitId) {
	this.principalUnitId = principalUnitId;
    }

    /**
     * Obtiene el valor del parámetro principalUnitName.
     * 
     * @return principalUnitName valor del campo a obtener.
     */
    public String getPrincipalUnitName() {
	return principalUnitName;
    }

    /**
     * Guarda el valor del parámetro principalUnitName.
     * 
     * @param principalUnitName
     *            valor del campo a guardar.
     */
    public void setPrincipalUnitName(
	String principalUnitName) {
	this.principalUnitName = principalUnitName;
    }

    /**
     * Obtiene el valor del parámetro entityUnitOfPublicLawId.
     * 
     * @return entityUnitOfPublicLawId valor del campo a obtener.
     */
    public String getEntityUnitOfPublicLawId() {
	return entityUnitOfPublicLawId;
    }

    /**
     * Guarda el valor del parámetro entityUnitOfPublicLawId.
     * 
     * @param entityUnitOfPublicLawId
     *            valor del campo a guardar.
     */
    public void setEntityUnitOfPublicLawId(
	String entityUnitOfPublicLawId) {
	this.entityUnitOfPublicLawId = entityUnitOfPublicLawId;
    }

    /**
     * Obtiene el valor del parámetro entityUnitOfPublicLawName.
     * 
     * @return entityUnitOfPublicLawName valor del campo a obtener.
     */
    public String getEntityUnitOfPublicLawName() {
	return entityUnitOfPublicLawName;
    }

    /**
     * Guarda el valor del parámetro entityUnitOfPublicLawName.
     * 
     * @param entityUnitOfPublicLawName
     *            valor del campo a guardar.
     */
    public void setEntityUnitOfPublicLawName(
	String entityUnitOfPublicLawName) {
	this.entityUnitOfPublicLawName = entityUnitOfPublicLawName;
    }

    /**
     * Obtiene el valor del parámetro publicEntityLevel.
     * 
     * @return publicEntityLevel valor del campo a obtener.
     */
    public Integer getPublicEntityLevel() {
	return publicEntityLevel;
    }

    /**
     * Guarda el valor del parámetro publicEntityLevel.
     * 
     * @param publicEntityLevel
     *            valor del campo a guardar.
     */
    public void setPublicEntityLevel(
	Integer publicEntityLevel) {
	this.publicEntityLevel = publicEntityLevel;
    }

    /**
     * Obtiene el valor del parámetro publicEntityTypeId.
     * 
     * @return publicEntityTypeId valor del campo a obtener.
     */
    public String getPublicEntityTypeId() {
	return publicEntityTypeId;
    }

    /**
     * Guarda el valor del parámetro publicEntityTypeId.
     * 
     * @param publicEntityTypeId
     *            valor del campo a guardar.
     */
    public void setPublicEntityTypeId(
	String publicEntityTypeId) {
	this.publicEntityTypeId = publicEntityTypeId;
    }

    /**
     * Obtiene el valor del parámetro publicEntityTypeName.
     * 
     * @return publicEntityTypeName valor del campo a obtener.
     */
    public String getPublicEntityTypeName() {
	return publicEntityTypeName;
    }

    /**
     * Guarda el valor del parámetro publicEntityTypeName.
     * 
     * @param publicEntityTypeName
     *            valor del campo a guardar.
     */
    public void setPublicEntityTypeName(
	String publicEntityTypeName) {
	this.publicEntityTypeName = publicEntityTypeName;
    }

    /**
     * Obtiene el valor del parámetro unitTypeId.
     * 
     * @return unitTypeId valor del campo a obtener.
     */
    public String getUnitTypeId() {
	return unitTypeId;
    }

    /**
     * Guarda el valor del parámetro unitTypeId.
     * 
     * @param unitTypeId
     *            valor del campo a guardar.
     */
    public void setUnitTypeId(
	String unitTypeId) {
	this.unitTypeId = unitTypeId;
    }

    /**
     * Obtiene el valor del parámetro unitTypeName.
     * 
     * @return unitTypeName valor del campo a obtener.
     */
    public String getUnitTypeName() {
	return unitTypeName;
    }

    /**
     * Guarda el valor del parámetro unitTypeName.
     * 
     * @param unitTypeName
     *            valor del campo a guardar.
     */
    public void setUnitTypeName(
	String unitTypeName) {
	this.unitTypeName = unitTypeName;
    }

    /**
     * Obtiene el valor del parámetro authorityId.
     * 
     * @return authorityId valor del campo a obtener.
     */
    public Integer getAuthorityId() {
	return authorityId;
    }

    /**
     * Guarda el valor del parámetro authorityId.
     * 
     * @param authorityId
     *            valor del campo a guardar.
     */
    public void setAuthorityId(
	Integer authorityId) {
	this.authorityId = authorityId;
    }

    /**
     * Obtiene el valor del parámetro authorityName.
     * 
     * @return authorityName valor del campo a obtener.
     */
    public String getAuthorityName() {
	return authorityName;
    }

    /**
     * Guarda el valor del parámetro authorityName.
     * 
     * @param authorityName
     *            valor del campo a guardar.
     */
    public void setAuthorityName(
	String authorityName) {
	this.authorityName = authorityName;
    }

    /**
     * Obtiene el valor del parámetro bSameAddressFatherUnit.
     * 
     * @return bSameAddressFatherUnit valor del campo a obtener.
     */
    public String getBSameAddressFatherUnit() {
	return bSameAddressFatherUnit;
    }

    /**
     * Guarda el valor del parámetro bSameAddressFatherUnit.
     * 
     * @param bSameAddressFatherUnit
     *            valor del campo a guardar.
     */
    public void setBSameAddressFatherUnit(
	String bSameAddressFatherUnit) {
	this.bSameAddressFatherUnit = bSameAddressFatherUnit;
    }

    /**
     * Obtiene el valor del parámetro address.
     * 
     * @return address valor del campo a obtener.
     */
    public Address getAddress() {
	return address;
    }

    /**
     * Guarda el valor del parámetro address.
     * 
     * @param address
     *            valor del campo a guardar.
     */
    public void setAddress(
	Address address) {
	this.address = address;
    }

    /**
     * Obtiene el valor del parámetro addOfficialDate.
     * 
     * @return addOfficialDate valor del campo a obtener.
     */
    public Date getAddOfficialDate() {
	return addOfficialDate;
    }

    /**
     * Guarda el valor del parámetro addOfficialDate.
     * 
     * @param addOfficialDate
     *            valor del campo a guardar.
     */
    public void setAddOfficialDate(
	Date addOfficialDate) {
	this.addOfficialDate = addOfficialDate;
    }

    /**
     * Obtiene el valor del parámetro deleteOfficialDate.
     * 
     * @return deleteOfficialDate valor del campo a obtener.
     */
    public Date getDeleteOfficialDate() {
	return deleteOfficialDate;
    }

    /**
     * Guarda el valor del parámetro deleteOfficialDate.
     * 
     * @param deleteOfficialDate
     *            valor del campo a guardar.
     */
    public void setDeleteOfficialDate(
	Date deleteOfficialDate) {
	this.deleteOfficialDate = deleteOfficialDate;
    }

    /**
     * Obtiene el valor del parámetro extinctionDate.
     * 
     * @return extinctionDate valor del campo a obtener.
     */
    public Date getExtinctionDate() {
	return extinctionDate;
    }

    /**
     * Guarda el valor del parámetro extinctionDate.
     * 
     * @param extinctionDate
     *            valor del campo a guardar.
     */
    public void setExtinctionDate(
	Date extinctionDate) {
	this.extinctionDate = extinctionDate;
    }

    /**
     * Obtiene el valor del parámetro annulationDate.
     * 
     * @return annulationDate valor del campo a obtener.
     */
    public Date getAnnulationDate() {
	return annulationDate;
    }

    /**
     * Guarda el valor del parámetro annulationDate.
     * 
     * @param annulationDate
     *            valor del campo a guardar.
     */
    public void setAnnulationDate(
	Date annulationDate) {
	this.annulationDate = annulationDate;
    }

    /**
     * Obtiene el valor del parámetro generalObservations.
     * 
     * @return generalObservations valor del campo a obtener.
     */
    public String getGeneralObservations() {
	return generalObservations;
    }

    /**
     * Guarda el valor del parámetro generalObservations.
     * 
     * @param generalObservations
     *            valor del campo a guardar.
     */
    public void setGeneralObservations(
	String generalObservations) {
	this.generalObservations = generalObservations;
    }

    /**
     * Obtiene el valor del parámetro deleteObservations.
     * 
     * @return deleteObservations valor del campo a obtener.
     */
    public String getDeleteObservations() {
	return deleteObservations;
    }

    /**
     * Guarda el valor del parámetro deleteObservations.
     * 
     * @param deleteObservations
     *            valor del campo a guardar.
     */
    public void setDeleteObservations(
	String deleteObservations) {
	this.deleteObservations = deleteObservations;
    }

    /**
     * Obtiene el valor del parámetro contactObservations.
     * 
     * @return contactObservations valor del campo a obtener.
     */
    public String getContactObservations() {
	return contactObservations;
    }

    /**
     * Guarda el valor del parámetro contactObservations.
     * 
     * @param contactObservations
     *            valor del campo a guardar.
     */
    public void setContactObservations(
	String contactObservations) {
	this.contactObservations = contactObservations;
    }

    /**
     * Obtiene el valor del parámetro territorialScopeOfCompetenceId.
     * 
     * @return territorialScopeOfCompetenceId valor del campo a obtener.
     */
    public String getTerritorialScopeOfCompetenceId() {
	return territorialScopeOfCompetenceId;
    }

    /**
     * Guarda el valor del parámetro territorialScopeOfCompetenceId.
     * 
     * @param territorialScopeOfCompetenceId
     *            valor del campo a guardar.
     */
    public void setTerritorialScopeOfCompetenceId(
	String territorialScopeOfCompetenceId) {
	this.territorialScopeOfCompetenceId = territorialScopeOfCompetenceId;
    }

    /**
     * Obtiene el valor del parámetro territorialScopeOfCompetenceName.
     * 
     * @return territorialScopeOfCompetenceName valor del campo a obtener.
     */
    public String getTerritorialScopeOfCompetenceName() {
	return territorialScopeOfCompetenceName;
    }

    /**
     * Guarda el valor del parámetro territorialScopeOfCompetenceName.
     * 
     * @param territorialScopeOfCompetenceName
     *            valor del campo a guardar.
     */
    public void setTerritorialScopeOfCompetenceName(
	String territorialScopeOfCompetenceName) {
	this.territorialScopeOfCompetenceName = territorialScopeOfCompetenceName;
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
     * Obtiene el valor del parámetro islandId.
     * 
     * @return islandId valor del campo a obtener.
     */
    public String getIslandId() {
	return islandId;
    }

    /**
     * Guarda el valor del parámetro islandId.
     * 
     * @param islandId
     *            valor del campo a guardar.
     */
    public void setIslandId(
	String islandId) {
	this.islandId = islandId;
    }

    /**
     * Obtiene el valor del parámetro islandName.
     * 
     * @return islandName valor del campo a obtener.
     */
    public String getIslandName() {
	return islandName;
    }

    /**
     * Guarda el valor del parámetro islandName.
     * 
     * @param islandName
     *            valor del campo a guardar.
     */
    public void setIslandName(
	String islandName) {
	this.islandName = islandName;
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
     * Obtiene el valor del parámetro minorLocalEntityId.
     * 
     * @return minorLocalEntityId valor del campo a obtener.
     */
    public String getMinorLocalEntityId() {
	return minorLocalEntityId;
    }

    /**
     * Guarda el valor del parámetro minorLocalEntityId.
     * 
     * @param minorLocalEntityId
     *            valor del campo a guardar.
     */
    public void setMinorLocalEntityId(
	String minorLocalEntityId) {
	this.minorLocalEntityId = minorLocalEntityId;
    }

    /**
     * Obtiene el valor del parámetro minorLocalEntityName.
     * 
     * @return minorLocalEntityName valor del campo a obtener.
     */
    public String getMinorLocalEntityName() {
	return minorLocalEntityName;
    }

    /**
     * Guarda el valor del parámetro minorLocalEntityName.
     * 
     * @param minorLocalEntityName
     *            valor del campo a guardar.
     */
    public void setMinorLocalEntityName(
	String minorLocalEntityName) {
	this.minorLocalEntityName = minorLocalEntityName;
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
     * Obtiene el valor del parámetro competencesOfUnit.
     * 
     * @return competencesOfUnit valor del campo a obtener.
     */
    public String getCompetencesOfUnit() {
	return competencesOfUnit;
    }

    /**
     * Guarda el valor del parámetro competencesOfUnit.
     * 
     * @param competencesOfUnit
     *            valor del campo a guardar.
     */
    public void setCompetencesOfUnit(
	String competencesOfUnit) {
	this.competencesOfUnit = competencesOfUnit;
    }

    /**
     * Obtiene el valor del parámetro legalProvisionOfCompetencesOfUnit.
     * 
     * @return legalProvisionOfCompetencesOfUnit valor del campo a obtener.
     */
    public String getLegalProvisionOfCompetencesOfUnit() {
	return legalProvisionOfCompetencesOfUnit;
    }

    /**
     * Guarda el valor del parámetro legalProvisionOfCompetencesOfUnit.
     * 
     * @param legalProvisionOfCompetencesOfUnit
     *            valor del campo a guardar.
     */
    public void setLegalProvisionOfCompetencesOfUnit(
	String legalProvisionOfCompetencesOfUnit) {
	this.legalProvisionOfCompetencesOfUnit = legalProvisionOfCompetencesOfUnit;
    }

    /**
     * Obtiene el valor del parámetro rootUnit.
     * 
     * @return rootUnit valor del campo a obtener.
     */
    public String getRootUnit() {
	return rootUnit;
    }

    /**
     * Guarda el valor del parámetro rootUnit.
     * 
     * @param rootUnit
     *            valor del campo a guardar.
     */
    public void setRootUnit(
	String rootUnit) {
	this.rootUnit = rootUnit;
    }

    /**
     * Obtiene el valor del parámetro cif.
     * 
     * @return cif valor del campo a obtener.
     */
    public String getCif() {
	return cif;
    }

    /**
     * Guarda el valor del parámetro cif.
     * 
     * @param cif
     *            valor del campo a guardar.
     */
    public void setCif(
	String cif) {
	this.cif = cif;
    }

    /**
     * Obtiene el valor del parámetro language.
     * 
     * @return language valor del campo a obtener.
     */
    public Integer getLanguage() {
	return language;
    }

    /**
     * Guarda el valor del parámetro language.
     * 
     * @param language
     *            valor del campo a guardar.
     */
    public void setLanguage(
	Integer language) {
	this.language = language;
    }

    /**
     * Obtiene el valor del parámetro relationshipsSIRUO.
     * 
     * @return relationshipsSIRUO valor del campo a obtener.
     */
    public BasicDataOffice getRelationshipsSIRUO() {
	return relationshipsSIRUO;
    }

    /**
     * Guarda el valor del parámetro relationshipsSIRUO.
     * 
     * @param relationshipsSIRUO
     *            valor del campo a guardar.
     */
    public void setRelationshipsSIRUO(
	BasicDataOffice relationshipsSIRUO) {
	this.relationshipsSIRUO = relationshipsSIRUO;
    }

    /**
     * Obtiene el valor del parámetro organizationalRelationshipsUO.
     * 
     * @return organizationalRelationshipsUO valor del campo a obtener.
     */
    public List<BasicDataOffice> getOrganizationalRelationshipsUO() {
	return organizationalRelationshipsUO;
    }

    /**
     * Guarda el valor del parámetro organizationalRelationshipsUO.
     * 
     * @param organizationalRelationshipsUO
     *            valor del campo a guardar.
     */
    public void setOrganizationalRelationshipsUO(
	List<BasicDataOffice> organizationalRelationshipsUO) {
	this.organizationalRelationshipsUO = organizationalRelationshipsUO;
    }

    /**
     * Obtiene el valor del parámetro contacts.
     * 
     * @return contacts valor del campo a obtener.
     */
    public List<Contact> getContacts() {
	return contacts;
    }

    /**
     * Guarda el valor del parámetro contacts.
     * 
     * @param contacts
     *            valor del campo a guardar.
     */
    public void setContacts(
	List<Contact> contacts) {
	this.contacts = contacts;
    }

    /**
     * Obtiene el valor del parámetro systemCreationDate.
     * 
     * @return systemCreationDate valor del campo a obtener.
     */
    public Date getSystemCreationDate() {
	return systemCreationDate;
    }

    /**
     * Guarda el valor del parámetro systemCreationDate.
     * 
     * @param systemCreationDate
     *            valor del campo a guardar.
     */
    public void setSystemCreationDate(
	Date systemCreationDate) {
	this.systemCreationDate = systemCreationDate;
    }

    /**
     * Obtiene el valor del parámetro lastUpdateDate.
     * 
     * @return lastUpdateDate valor del campo a obtener.
     */
    public Date getLastUpdateDate() {
	return lastUpdateDate;
    }

    /**
     * Guarda el valor del parámetro lastUpdateDate.
     * 
     * @param lastUpdateDate
     *            valor del campo a guardar.
     */
    public void setLastUpdateDate(
	Date lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
    }

}