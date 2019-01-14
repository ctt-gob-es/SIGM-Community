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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for unit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="unit">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir3.msssi.es/}basicDataUnit">
 *       &lt;sequence>
 *         &lt;element name="acronyms" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addOfficialDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="address" type="{http://ws.dir3.msssi.es/}address" minOccurs="0"/>
 *         &lt;element name="administrationLevelId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="administrationLevelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="annulationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="authorityId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="authorityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autonomousCommunityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="autonomousCommunityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cif" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="competencesOfUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactObservations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contacts" type="{http://ws.dir3.msssi.es/}contact" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="countryId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="countryName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deleteObservations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deleteOfficialDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="entityPublicLawIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityUnitOfPublicLawId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityUnitOfPublicLawName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extinctionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="foreignLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="generalObservations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="geographicalEntityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="geographicalEntityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="islandId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="islandName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="lastUpdateDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="legalProvisionOfCompetencesOfUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="minorLocalEntityId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="minorLocalEntityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationalRelationshipsUO" type="{http://ws.dir3.msssi.es/}basicDataOffice" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="principalUnitId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="principalUnitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provinceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicEntityLevel" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="publicEntityTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="publicEntityTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relationshipsSIRUO" type="{http://ws.dir3.msssi.es/}basicDataOffice" minOccurs="0"/>
 *         &lt;element name="rootUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="systemCreationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="territorialScopeOfCompetenceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="territorialScopeOfCompetenceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitTypeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitTypeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bSameAddressFatherUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unit", propOrder = { "acronyms", "addOfficialDate", "address",
    "administrationLevelId", "administrationLevelName", "annulationDate", "authorityId",
    "authorityName", "autonomousCommunityId", "autonomousCommunityName", "cif", "cityId",
    "cityName", "competencesOfUnit", "contactObservations", "contacts", "countryId", "countryName",
    "deleteObservations", "deleteOfficialDate", "entityPublicLawIndicator",
    "entityUnitOfPublicLawId", "entityUnitOfPublicLawName", "externalId", "extinctionDate",
    "foreignLocation", "generalObservations", "geographicalEntityId", "geographicalEntityName",
    "islandId", "islandName", "language", "lastUpdateDate", "legalProvisionOfCompetencesOfUnit",
    "minorLocalEntityId", "minorLocalEntityName", "organizationalRelationshipsUO",
    "principalUnitId", "principalUnitName", "provinceId", "provinceName", "publicEntityLevel",
    "publicEntityTypeId", "publicEntityTypeName", "relationshipsSIRUO", "rootUnit",
    "systemCreationDate", "territorialScopeOfCompetenceId", "territorialScopeOfCompetenceName",
    "unitTypeId", "unitTypeName", "bSameAddressFatherUnit" })
public class Unit extends BasicDataUnit {

    protected String acronyms;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar addOfficialDate;
    protected Address address;
    protected String administrationLevelId;
    protected String administrationLevelName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar annulationDate;
    protected Integer authorityId;
    protected String authorityName;
    protected String autonomousCommunityId;
    protected String autonomousCommunityName;
    protected String cif;
    protected String cityId;
    protected String cityName;
    protected String competencesOfUnit;
    protected String contactObservations;
    @XmlElement(nillable = true)
    protected List<Contact> contacts;
    protected String countryId;
    protected String countryName;
    protected String deleteObservations;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar deleteOfficialDate;
    protected String entityPublicLawIndicator;
    protected String entityUnitOfPublicLawId;
    protected String entityUnitOfPublicLawName;
    protected String externalId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar extinctionDate;
    protected String foreignLocation;
    protected String generalObservations;
    protected String geographicalEntityId;
    protected String geographicalEntityName;
    protected String islandId;
    protected String islandName;
    protected Integer language;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdateDate;
    protected String legalProvisionOfCompetencesOfUnit;
    protected String minorLocalEntityId;
    protected String minorLocalEntityName;
    @XmlElement(nillable = true)
    protected List<BasicDataOffice> organizationalRelationshipsUO;
    protected String principalUnitId;
    protected String principalUnitName;
    protected String provinceId;
    protected String provinceName;
    protected Integer publicEntityLevel;
    protected String publicEntityTypeId;
    protected String publicEntityTypeName;
    protected BasicDataOffice relationshipsSIRUO;
    protected String rootUnit;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar systemCreationDate;
    protected String territorialScopeOfCompetenceId;
    protected String territorialScopeOfCompetenceName;
    protected String unitTypeId;
    protected String unitTypeName;
    protected String bSameAddressFatherUnit;

    /**
     * Gets the value of the acronyms property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAcronyms() {
	return acronyms;
    }

    /**
     * Sets the value of the acronyms property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAcronyms(
	String value) {
	this.acronyms = value;
    }

    /**
     * Gets the value of the addOfficialDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getAddOfficialDate() {
	return addOfficialDate;
    }

    /**
     * Sets the value of the addOfficialDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setAddOfficialDate(
	XMLGregorianCalendar value) {
	this.addOfficialDate = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return possible object is {@link Address }
     * 
     */
    public Address getAddress() {
	return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *            allowed object is {@link Address }
     * 
     */
    public void setAddress(
	Address value) {
	this.address = value;
    }

    /**
     * Gets the value of the administrationLevelId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAdministrationLevelId() {
	return administrationLevelId;
    }

    /**
     * Sets the value of the administrationLevelId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAdministrationLevelId(
	String value) {
	this.administrationLevelId = value;
    }

    /**
     * Gets the value of the administrationLevelName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAdministrationLevelName() {
	return administrationLevelName;
    }

    /**
     * Sets the value of the administrationLevelName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAdministrationLevelName(
	String value) {
	this.administrationLevelName = value;
    }

    /**
     * Gets the value of the annulationDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getAnnulationDate() {
	return annulationDate;
    }

    /**
     * Sets the value of the annulationDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setAnnulationDate(
	XMLGregorianCalendar value) {
	this.annulationDate = value;
    }

    /**
     * Gets the value of the authorityId property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getAuthorityId() {
	return authorityId;
    }

    /**
     * Sets the value of the authorityId property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setAuthorityId(
	Integer value) {
	this.authorityId = value;
    }

    /**
     * Gets the value of the authorityName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getAuthorityName() {
	return authorityName;
    }

    /**
     * Sets the value of the authorityName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setAuthorityName(
	String value) {
	this.authorityName = value;
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
     * Gets the value of the cif property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCif() {
	return cif;
    }

    /**
     * Sets the value of the cif property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCif(
	String value) {
	this.cif = value;
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
     * Gets the value of the competencesOfUnit property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCompetencesOfUnit() {
	return competencesOfUnit;
    }

    /**
     * Sets the value of the competencesOfUnit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCompetencesOfUnit(
	String value) {
	this.competencesOfUnit = value;
    }

    /**
     * Gets the value of the contactObservations property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getContactObservations() {
	return contactObservations;
    }

    /**
     * Sets the value of the contactObservations property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setContactObservations(
	String value) {
	this.contactObservations = value;
    }

    /**
     * Gets the value of the contacts property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the contacts property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getContacts().add(
     *     newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Contact }
     * 
     * 
     */
    public List<Contact> getContacts() {
	if (contacts == null) {
	    contacts = new ArrayList<Contact>();
	}
	return this.contacts;
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
     * Gets the value of the deleteObservations property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDeleteObservations() {
	return deleteObservations;
    }

    /**
     * Sets the value of the deleteObservations property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDeleteObservations(
	String value) {
	this.deleteObservations = value;
    }

    /**
     * Gets the value of the deleteOfficialDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getDeleteOfficialDate() {
	return deleteOfficialDate;
    }

    /**
     * Sets the value of the deleteOfficialDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setDeleteOfficialDate(
	XMLGregorianCalendar value) {
	this.deleteOfficialDate = value;
    }

    /**
     * Gets the value of the entityPublicLawIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEntityPublicLawIndicator() {
	return entityPublicLawIndicator;
    }

    /**
     * Sets the value of the entityPublicLawIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEntityPublicLawIndicator(
	String value) {
	this.entityPublicLawIndicator = value;
    }

    /**
     * Gets the value of the entityUnitOfPublicLawId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEntityUnitOfPublicLawId() {
	return entityUnitOfPublicLawId;
    }

    /**
     * Sets the value of the entityUnitOfPublicLawId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEntityUnitOfPublicLawId(
	String value) {
	this.entityUnitOfPublicLawId = value;
    }

    /**
     * Gets the value of the entityUnitOfPublicLawName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getEntityUnitOfPublicLawName() {
	return entityUnitOfPublicLawName;
    }

    /**
     * Sets the value of the entityUnitOfPublicLawName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEntityUnitOfPublicLawName(
	String value) {
	this.entityUnitOfPublicLawName = value;
    }

    /**
     * Gets the value of the externalId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getExternalId() {
	return externalId;
    }

    /**
     * Sets the value of the externalId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setExternalId(
	String value) {
	this.externalId = value;
    }

    /**
     * Gets the value of the extinctionDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getExtinctionDate() {
	return extinctionDate;
    }

    /**
     * Sets the value of the extinctionDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setExtinctionDate(
	XMLGregorianCalendar value) {
	this.extinctionDate = value;
    }

    /**
     * Gets the value of the foreignLocation property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getForeignLocation() {
	return foreignLocation;
    }

    /**
     * Sets the value of the foreignLocation property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setForeignLocation(
	String value) {
	this.foreignLocation = value;
    }

    /**
     * Gets the value of the generalObservations property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGeneralObservations() {
	return generalObservations;
    }

    /**
     * Sets the value of the generalObservations property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGeneralObservations(
	String value) {
	this.generalObservations = value;
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
     * Gets the value of the islandId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIslandId() {
	return islandId;
    }

    /**
     * Sets the value of the islandId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIslandId(
	String value) {
	this.islandId = value;
    }

    /**
     * Gets the value of the islandName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getIslandName() {
	return islandName;
    }

    /**
     * Sets the value of the islandName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setIslandName(
	String value) {
	this.islandName = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getLanguage() {
	return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setLanguage(
	Integer value) {
	this.language = value;
    }

    /**
     * Gets the value of the lastUpdateDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getLastUpdateDate() {
	return lastUpdateDate;
    }

    /**
     * Sets the value of the lastUpdateDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setLastUpdateDate(
	XMLGregorianCalendar value) {
	this.lastUpdateDate = value;
    }

    /**
     * Gets the value of the legalProvisionOfCompetencesOfUnit property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLegalProvisionOfCompetencesOfUnit() {
	return legalProvisionOfCompetencesOfUnit;
    }

    /**
     * Sets the value of the legalProvisionOfCompetencesOfUnit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLegalProvisionOfCompetencesOfUnit(
	String value) {
	this.legalProvisionOfCompetencesOfUnit = value;
    }

    /**
     * Gets the value of the minorLocalEntityId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMinorLocalEntityId() {
	return minorLocalEntityId;
    }

    /**
     * Sets the value of the minorLocalEntityId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMinorLocalEntityId(
	String value) {
	this.minorLocalEntityId = value;
    }

    /**
     * Gets the value of the minorLocalEntityName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMinorLocalEntityName() {
	return minorLocalEntityName;
    }

    /**
     * Sets the value of the minorLocalEntityName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMinorLocalEntityName(
	String value) {
	this.minorLocalEntityName = value;
    }

    /**
     * Gets the value of the organizationalRelationshipsUO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the organizationalRelationshipsUO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getOrganizationalRelationshipsUO().add(
     *     newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BasicDataOffice }
     * 
     * 
     */
    public List<BasicDataOffice> getOrganizationalRelationshipsUO() {
	if (organizationalRelationshipsUO == null) {
	    organizationalRelationshipsUO = new ArrayList<BasicDataOffice>();
	}
	return this.organizationalRelationshipsUO;
    }

    /**
     * Gets the value of the principalUnitId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPrincipalUnitId() {
	return principalUnitId;
    }

    /**
     * Sets the value of the principalUnitId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPrincipalUnitId(
	String value) {
	this.principalUnitId = value;
    }

    /**
     * Gets the value of the principalUnitName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPrincipalUnitName() {
	return principalUnitName;
    }

    /**
     * Sets the value of the principalUnitName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPrincipalUnitName(
	String value) {
	this.principalUnitName = value;
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
     * Gets the value of the publicEntityLevel property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getPublicEntityLevel() {
	return publicEntityLevel;
    }

    /**
     * Sets the value of the publicEntityLevel property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setPublicEntityLevel(
	Integer value) {
	this.publicEntityLevel = value;
    }

    /**
     * Gets the value of the publicEntityTypeId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPublicEntityTypeId() {
	return publicEntityTypeId;
    }

    /**
     * Sets the value of the publicEntityTypeId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPublicEntityTypeId(
	String value) {
	this.publicEntityTypeId = value;
    }

    /**
     * Gets the value of the publicEntityTypeName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getPublicEntityTypeName() {
	return publicEntityTypeName;
    }

    /**
     * Sets the value of the publicEntityTypeName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setPublicEntityTypeName(
	String value) {
	this.publicEntityTypeName = value;
    }

    /**
     * Gets the value of the relationshipsSIRUO property.
     * 
     * @return possible object is {@link BasicDataOffice }
     * 
     */
    public BasicDataOffice getRelationshipsSIRUO() {
	return relationshipsSIRUO;
    }

    /**
     * Sets the value of the relationshipsSIRUO property.
     * 
     * @param value
     *            allowed object is {@link BasicDataOffice }
     * 
     */
    public void setRelationshipsSIRUO(
	BasicDataOffice value) {
	this.relationshipsSIRUO = value;
    }

    /**
     * Gets the value of the rootUnit property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRootUnit() {
	return rootUnit;
    }

    /**
     * Sets the value of the rootUnit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRootUnit(
	String value) {
	this.rootUnit = value;
    }

    /**
     * Gets the value of the systemCreationDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getSystemCreationDate() {
	return systemCreationDate;
    }

    /**
     * Sets the value of the systemCreationDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setSystemCreationDate(
	XMLGregorianCalendar value) {
	this.systemCreationDate = value;
    }

    /**
     * Gets the value of the territorialScopeOfCompetenceId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTerritorialScopeOfCompetenceId() {
	return territorialScopeOfCompetenceId;
    }

    /**
     * Sets the value of the territorialScopeOfCompetenceId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTerritorialScopeOfCompetenceId(
	String value) {
	this.territorialScopeOfCompetenceId = value;
    }

    /**
     * Gets the value of the territorialScopeOfCompetenceName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTerritorialScopeOfCompetenceName() {
	return territorialScopeOfCompetenceName;
    }

    /**
     * Sets the value of the territorialScopeOfCompetenceName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTerritorialScopeOfCompetenceName(
	String value) {
	this.territorialScopeOfCompetenceName = value;
    }

    /**
     * Gets the value of the unitTypeId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getUnitTypeId() {
	return unitTypeId;
    }

    /**
     * Sets the value of the unitTypeId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setUnitTypeId(
	String value) {
	this.unitTypeId = value;
    }

    /**
     * Gets the value of the unitTypeName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getUnitTypeName() {
	return unitTypeName;
    }

    /**
     * Sets the value of the unitTypeName property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setUnitTypeName(
	String value) {
	this.unitTypeName = value;
    }

    /**
     * Gets the value of the bSameAddressFatherUnit property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBSameAddressFatherUnit() {
	return bSameAddressFatherUnit;
    }

    /**
     * Sets the value of the bSameAddressFatherUnit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBSameAddressFatherUnit(
	String value) {
	this.bSameAddressFatherUnit = value;
    }

}
