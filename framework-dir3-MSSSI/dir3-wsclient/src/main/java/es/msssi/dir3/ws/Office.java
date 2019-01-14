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
 * Java class for office complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="office">
 *   &lt;complexContent>
 *     &lt;extension base="{http://ws.dir3.msssi.es/}basicDataOffice">
 *       &lt;sequence>
 *         &lt;element name="address" type="{http://ws.dir3.msssi.es/}address" minOccurs="0"/>
 *         &lt;element name="administrationLevelId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="administrationLevelName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="annulationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="businessHours" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactObservations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contacts" type="{http://ws.dir3.msssi.es/}contact" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="creationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="deleteObservations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dependentOfficeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="electronicRegisterIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="externalId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extinctionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="generalObservations" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="genericIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="genericIndicatorUse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="informationOfficeIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inhabiliesDays" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeLocalGeneralAutonomousIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeLocalGeneralLocalIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeLocalStateIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeWithRestrictionCityHallCityHallIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeWithRestrictionLocalAutonomousIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeWithRestrictionLocalLocalIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interchangeWithoutRestrictionIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organizationalRelationshipsOFI" type="{http://ws.dir3.msssi.es/}basicDataUnit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="processingOfficeIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registerOfficeIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="relationshipsSIROFI" type="{http://ws.dir3.msssi.es/}basicDataUnit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="services" type="{http://ws.dir3.msssi.es/}service" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sirOfficeIndicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bSameAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "office", propOrder = { "address", "administrationLevelId",
    "administrationLevelName", "annulationDate", "businessHours", "contactObservations",
    "contacts", "creationDate", "deleteObservations", "dependentOfficeId",
    "electronicRegisterIndicator", "externalId", "extinctionDate", "generalObservations",
    "genericIndicator", "genericIndicatorUse", "informationOfficeIndicator", "inhabiliesDays",
    "interchangeLocalGeneralAutonomousIndicator", "interchangeLocalGeneralLocalIndicator",
    "interchangeLocalStateIndicator", "interchangeWithRestrictionCityHallCityHallIndicator",
    "interchangeWithRestrictionLocalAutonomousIndicator",
    "interchangeWithRestrictionLocalLocalIndicator", "interchangeWithoutRestrictionIndicator",
    "organizationalRelationshipsOFI", "processingOfficeIndicator", "registerOfficeIndicator",
    "relationshipsSIROFI", "services", "sirOfficeIndicator", "bSameAddress" })
public class Office extends BasicDataOffice {

    protected Address address;
    protected String administrationLevelId;
    protected String administrationLevelName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar annulationDate;
    protected String businessHours;
    protected String contactObservations;
    @XmlElement(nillable = true)
    protected List<Contact> contacts;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDate;
    protected String deleteObservations;
    protected String dependentOfficeId;
    protected String electronicRegisterIndicator;
    protected String externalId;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar extinctionDate;
    protected String generalObservations;
    protected String genericIndicator;
    protected String genericIndicatorUse;
    protected String informationOfficeIndicator;
    protected String inhabiliesDays;
    protected String interchangeLocalGeneralAutonomousIndicator;
    protected String interchangeLocalGeneralLocalIndicator;
    protected String interchangeLocalStateIndicator;
    protected String interchangeWithRestrictionCityHallCityHallIndicator;
    protected String interchangeWithRestrictionLocalAutonomousIndicator;
    protected String interchangeWithRestrictionLocalLocalIndicator;
    protected String interchangeWithoutRestrictionIndicator;
    @XmlElement(nillable = true)
    protected List<BasicDataUnit> organizationalRelationshipsOFI;
    protected String processingOfficeIndicator;
    protected String registerOfficeIndicator;
    @XmlElement(nillable = true)
    protected List<BasicDataUnit> relationshipsSIROFI;
    @XmlElement(nillable = true)
    protected List<Service> services;
    protected String sirOfficeIndicator;
    protected String bSameAddress;

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
     * Gets the value of the businessHours property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBusinessHours() {
	return businessHours;
    }

    /**
     * Sets the value of the businessHours property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBusinessHours(
	String value) {
	this.businessHours = value;
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
     * Gets the value of the creationDate property.
     * 
     * @return possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getCreationDate() {
	return creationDate;
    }

    /**
     * Sets the value of the creationDate property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setCreationDate(
	XMLGregorianCalendar value) {
	this.creationDate = value;
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
     * Gets the value of the dependentOfficeId property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDependentOfficeId() {
	return dependentOfficeId;
    }

    /**
     * Sets the value of the dependentOfficeId property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDependentOfficeId(
	String value) {
	this.dependentOfficeId = value;
    }

    /**
     * Gets the value of the electronicRegisterIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getElectronicRegisterIndicator() {
	return electronicRegisterIndicator;
    }

    /**
     * Sets the value of the electronicRegisterIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setElectronicRegisterIndicator(
	String value) {
	this.electronicRegisterIndicator = value;
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
     * Gets the value of the genericIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGenericIndicator() {
	return genericIndicator;
    }

    /**
     * Sets the value of the genericIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGenericIndicator(
	String value) {
	this.genericIndicator = value;
    }

    /**
     * Gets the value of the genericIndicatorUse property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getGenericIndicatorUse() {
	return genericIndicatorUse;
    }

    /**
     * Sets the value of the genericIndicatorUse property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setGenericIndicatorUse(
	String value) {
	this.genericIndicatorUse = value;
    }

    /**
     * Gets the value of the informationOfficeIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInformationOfficeIndicator() {
	return informationOfficeIndicator;
    }

    /**
     * Sets the value of the informationOfficeIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInformationOfficeIndicator(
	String value) {
	this.informationOfficeIndicator = value;
    }

    /**
     * Gets the value of the inhabiliesDays property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInhabiliesDays() {
	return inhabiliesDays;
    }

    /**
     * Sets the value of the inhabiliesDays property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInhabiliesDays(
	String value) {
	this.inhabiliesDays = value;
    }

    /**
     * Gets the value of the interchangeLocalGeneralAutonomousIndicator
     * property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeLocalGeneralAutonomousIndicator() {
	return interchangeLocalGeneralAutonomousIndicator;
    }

    /**
     * Sets the value of the interchangeLocalGeneralAutonomousIndicator
     * property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeLocalGeneralAutonomousIndicator(
	String value) {
	this.interchangeLocalGeneralAutonomousIndicator = value;
    }

    /**
     * Gets the value of the interchangeLocalGeneralLocalIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeLocalGeneralLocalIndicator() {
	return interchangeLocalGeneralLocalIndicator;
    }

    /**
     * Sets the value of the interchangeLocalGeneralLocalIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeLocalGeneralLocalIndicator(
	String value) {
	this.interchangeLocalGeneralLocalIndicator = value;
    }

    /**
     * Gets the value of the interchangeLocalStateIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeLocalStateIndicator() {
	return interchangeLocalStateIndicator;
    }

    /**
     * Sets the value of the interchangeLocalStateIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeLocalStateIndicator(
	String value) {
	this.interchangeLocalStateIndicator = value;
    }

    /**
     * Gets the value of the interchangeWithRestrictionCityHallCityHallIndicator
     * property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeWithRestrictionCityHallCityHallIndicator() {
	return interchangeWithRestrictionCityHallCityHallIndicator;
    }

    /**
     * Sets the value of the interchangeWithRestrictionCityHallCityHallIndicator
     * property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeWithRestrictionCityHallCityHallIndicator(
	String value) {
	this.interchangeWithRestrictionCityHallCityHallIndicator = value;
    }

    /**
     * Gets the value of the interchangeWithRestrictionLocalAutonomousIndicator
     * property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeWithRestrictionLocalAutonomousIndicator() {
	return interchangeWithRestrictionLocalAutonomousIndicator;
    }

    /**
     * Sets the value of the interchangeWithRestrictionLocalAutonomousIndicator
     * property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeWithRestrictionLocalAutonomousIndicator(
	String value) {
	this.interchangeWithRestrictionLocalAutonomousIndicator = value;
    }

    /**
     * Gets the value of the interchangeWithRestrictionLocalLocalIndicator
     * property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeWithRestrictionLocalLocalIndicator() {
	return interchangeWithRestrictionLocalLocalIndicator;
    }

    /**
     * Sets the value of the interchangeWithRestrictionLocalLocalIndicator
     * property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeWithRestrictionLocalLocalIndicator(
	String value) {
	this.interchangeWithRestrictionLocalLocalIndicator = value;
    }

    /**
     * Gets the value of the interchangeWithoutRestrictionIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getInterchangeWithoutRestrictionIndicator() {
	return interchangeWithoutRestrictionIndicator;
    }

    /**
     * Sets the value of the interchangeWithoutRestrictionIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setInterchangeWithoutRestrictionIndicator(
	String value) {
	this.interchangeWithoutRestrictionIndicator = value;
    }

    /**
     * Gets the value of the organizationalRelationshipsOFI property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the organizationalRelationshipsOFI property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getOrganizationalRelationshipsOFI().add(
     *     newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BasicDataUnit }
     * 
     * 
     */
    public List<BasicDataUnit> getOrganizationalRelationshipsOFI() {
	if (organizationalRelationshipsOFI == null) {
	    organizationalRelationshipsOFI = new ArrayList<BasicDataUnit>();
	}
	return this.organizationalRelationshipsOFI;
    }

    /**
     * Gets the value of the processingOfficeIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProcessingOfficeIndicator() {
	return processingOfficeIndicator;
    }

    /**
     * Sets the value of the processingOfficeIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProcessingOfficeIndicator(
	String value) {
	this.processingOfficeIndicator = value;
    }

    /**
     * Gets the value of the registerOfficeIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getRegisterOfficeIndicator() {
	return registerOfficeIndicator;
    }

    /**
     * Sets the value of the registerOfficeIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setRegisterOfficeIndicator(
	String value) {
	this.registerOfficeIndicator = value;
    }

    /**
     * Gets the value of the relationshipsSIROFI property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the relationshipsSIROFI property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getRelationshipsSIROFI().add(
     *     newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BasicDataUnit }
     * 
     * 
     */
    public List<BasicDataUnit> getRelationshipsSIROFI() {
	if (relationshipsSIROFI == null) {
	    relationshipsSIROFI = new ArrayList<BasicDataUnit>();
	}
	return this.relationshipsSIROFI;
    }

    /**
     * Gets the value of the services property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the services property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getServices().add(
     *     newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Service }
     * 
     * 
     */
    public List<Service> getServices() {
	if (services == null) {
	    services = new ArrayList<Service>();
	}
	return this.services;
    }

    /**
     * Gets the value of the sirOfficeIndicator property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSirOfficeIndicator() {
	return sirOfficeIndicator;
    }

    /**
     * Sets the value of the sirOfficeIndicator property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSirOfficeIndicator(
	String value) {
	this.sirOfficeIndicator = value;
    }

    /**
     * Gets the value of the bSameAddress property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getBSameAddress() {
	return bSameAddress;
    }

    /**
     * Sets the value of the bSameAddress property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setBSameAddress(
	String value) {
	this.bSameAddress = value;
    }

}
