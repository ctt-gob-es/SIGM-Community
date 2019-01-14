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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Información de una oficina.
 * 
 * @author cmorenog
 * 
 */
public class OfficeVO extends BasicDataOfficeVO {

    private static final long serialVersionUID = 5515960788820716930L;

    /*
     * Datos Identificativos y Estructurales
     */

    /**
     * Designa si la oficina tiene competencias de REGISTRO.
     */
    private String registerOfficeIndicator;
    /**
     * Designa si la oficina tiene competencias de información.
     */
    private String informationOfficeIndicator;

    /**
     * Designa si la oficina tiene competencias de tramitación.
     */
    private String processingOfficeIndicator;

    /**
     * Designa si una oficina se encuentra adherida a la plataforma de
     * intercambio registral del SIR.
     */
    private String sirOfficeIndicator;

    /**
     * Designa si se trata de una oficina de registro que realiza el servicio
     * vía medios electrónicos (Registro Electrónico).
     */
    private String electronicRegisterIndicator;

    /**
     * Designa si una oficina de ámbito local puede registrar documentos
     * destinados a Unidades de la AGE.
     */
    private String interchangeLocalStateIndicator;

    /**
     * Designa si una oficina de ámbito local puede registrar documentos
     * destinados a Unidades de su Administración Autonómica, es decir de la
     * Comunidad Autónoma donde opera la oficina.
     */
    private String interchangeWithRestrictionLocalAutonomousIndicator;

    /**
     * Designa si una oficina de ámbito local puede registrar documentos
     * destinados a Unidades de cualquier Administración Autonómica.
     */
    private String interchangeLocalGeneralAutonomousIndicator;

    /**
     * Designa si una oficina de ámbito local puede registrar documentos
     * destinados a otras entidades locales de la Comunidad Autónoma donde opera
     * la oficina.
     */
    private String interchangeWithRestrictionLocalLocalIndicator;

    /**
     * Designa si una oficina de ámbito local puede registrar documentos
     * destinados a cualquier otra entidad local.
     */
    private String interchangeLocalGeneralLocalIndicator;

    /**
     * Designa si una oficina de un ayuntamiento puede registrar documentos
     * destinados a otros ayuntamientos de su misma Comunidad Autónoma.
     */
    private String interchangeWithRestrictionCityHallCityHallIndicator;

    /**
     * Designa si una oficina puede registrar documentos destinados a Unidades
     * de cualquier ámbito de la Administración pública.
     */
    private String interchangeWithoutRestrictionIndicator;

    /**
     * Indicador abierto a futuras necesidades.
     */
    private String genericIndicator;

    /**
     * Descripción que aporta información sobre el uso del Indicador genérico.
     */
    private String genericIndicatorUse;

    /**
     * Código del nivel de administración (Estatal, Autonómica o Local)
     */
    private String administrationLevelId;

    /**
     * Descripción del nivel de administración.
     */
    private String administrationLevelName;

    /**
     * Id de la oficina de la que depende
     */
    private String dependentOfficeId;
    /**
     * Id de la oficina de la que depende
     */
    private String dependentOfficeName;

    /*
     * Datos de Locaclización de la oficina
     */

    /**
     * Horarios de la oficina.
     */
    private String businessHours;

    /**
     * Días inhabiles de la oficina.
     */
    private String inhabiliesDays;

    /**
     * Indica si la Oficina comparte la misma dirección que la Unidad de la que
     * depende orgánicamente.
     */
    private String bSameAddress;

    /**
     * Dirección de la oficina.
     */
    private AddressVO address;

    /**
     * Lista de datos de contacto de la oficina.
     */
    private List<ContactOFIVO> contacts;

    /**
     * Lista de servicios de la oficina.
     */
    private List<ServiceVO> services;

    /*
     * Datos de Vigencia de la oficina
     */
    /**
     * Fecha de creación oficial.
     */
    private Date creationDate;

    /**
     * Fecha de extinción final.
     */
    private Date extinctionDate;

    /**
     * Fecha de anulación.
     */
    private Date annulationDate;

    /*
     * Datos de auditoría y control
     */
    /**
     * Código externo utilizado por la entidad pública que aporta los datos para
     * el alta de la oficina en el Directorio Común.
     */
    private String externalId;

    /*
     * Datos de observaciones
     */

    /**
     * Observaciones generales de la oficina.
     */
    private String generalObservations;

    /**
     * Observaciones de baja de la oficina.
     */
    private String deleteObservations;

    /**
     * Observaciones de contacto de la oficina.
     */
    private String contactObservations;
    /**
     * Lista de unidades orgánicas de las que la oficina da servicios de SIR.
     */
    private List<BasicDataUnitVO> relationshipsSIROFI;
    /**
     * Lista de unidades orgánicas relacionadas con la oficina.
     */
    private List<BasicDataUnitVO> organizationalRelationshipsOFI;

    /**
     * Constructor.
     */
    public OfficeVO() {
	super();
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
     * Obtiene el valor del parámetro genericIndicator.
     * 
     * @return genericIndicator valor del campo a obtener.
     */
    public String getGenericIndicator() {
	return genericIndicator;
    }

    /**
     * Guarda el valor del parámetro genericIndicator.
     * 
     * @param genericIndicator
     *            valor del campo a guardar.
     */
    public void setGenericIndicator(
	String genericIndicator) {
	this.genericIndicator = genericIndicator;
    }

    /**
     * Obtiene el valor del parámetro genericIndicatorUse.
     * 
     * @return genericIndicatorUse valor del campo a obtener.
     */
    public String getGenericIndicatorUse() {
	return genericIndicatorUse;
    }

    /**
     * Guarda el valor del parámetro genericIndicatorUse.
     * 
     * @param genericIndicatorUse
     *            valor del campo a guardar.
     */
    public void setGenericIndicatorUse(
	String genericIndicatorUse) {
	this.genericIndicatorUse = genericIndicatorUse;
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
     * Obtiene el valor del parámetro dependentOfficeId.
     * 
     * @return dependentOfficeId valor del campo a obtener.
     */
    public String getDependentOfficeId() {
	return dependentOfficeId;
    }

    /**
     * Guarda el valor del parámetro dependentOfficeId.
     * 
     * @param dependentOfficeId
     *            valor del campo a guardar.
     */
    public void setDependentOfficeId(
	String dependentOfficeId) {
	this.dependentOfficeId = dependentOfficeId;
    }

    /**
     * Obtiene el valor del parámetro sirOfficeIndicator.
     * 
     * @return sirOfficeIndicator valor del campo a obtener.
     */
    public String getSirOfficeIndicator() {
	return sirOfficeIndicator;
    }

    /**
     * Guarda el valor del parámetro sirOfficeIndicator.
     * 
     * @param sirOfficeIndicator
     *            valor del campo a guardar.
     */
    public void setSirOfficeIndicator(
	String sirOfficeIndicator) {
	this.sirOfficeIndicator = sirOfficeIndicator;
    }

    /**
     * Obtiene el valor del parámetro registerOfficeIndicator.
     * 
     * @return registerOfficeIndicator valor del campo a obtener.
     */
    public String getRegisterOfficeIndicator() {
	return registerOfficeIndicator;
    }

    /**
     * Guarda el valor del parámetro registerOfficeIndicator.
     * 
     * @param registerOfficeIndicator
     *            valor del campo a guardar.
     */
    public void setRegisterOfficeIndicator(
	String registerOfficeIndicator) {
	this.registerOfficeIndicator = registerOfficeIndicator;
    }

    /**
     * Obtiene el valor del parámetro informationOfficeIndicator.
     * 
     * @return informationOfficeIndicator valor del campo a obtener.
     */
    public String getInformationOfficeIndicator() {
	return informationOfficeIndicator;
    }

    /**
     * Guarda el valor del parámetro informationOfficeIndicator.
     * 
     * @param informationOfficeIndicator
     *            valor del campo a guardar.
     */
    public void setInformationOfficeIndicator(
	String informationOfficeIndicator) {
	this.informationOfficeIndicator = informationOfficeIndicator;
    }

    /**
     * Obtiene el valor del parámetro processingOfficeIndicator.
     * 
     * @return processingOfficeIndicator valor del campo a obtener.
     */
    public String getProcessingOfficeIndicator() {
	return processingOfficeIndicator;
    }

    /**
     * Guarda el valor del parámetro processingOfficeIndicator.
     * 
     * @param processingOfficeIndicator
     *            valor del campo a guardar.
     */
    public void setProcessingOfficeIndicator(
	String processingOfficeIndicator) {
	this.processingOfficeIndicator = processingOfficeIndicator;
    }

    /**
     * Obtiene el valor del parámetro electronicRegisterIndicator.
     * 
     * @return electronicRegisterIndicator valor del campo a obtener.
     */
    public String getElectronicRegisterIndicator() {
	return electronicRegisterIndicator;
    }

    /**
     * Guarda el valor del parámetro electronicRegisterIndicator.
     * 
     * @param electronicRegisterIndicator
     *            valor del campo a guardar.
     */
    public void setElectronicRegisterIndicator(
	String electronicRegisterIndicator) {
	this.electronicRegisterIndicator = electronicRegisterIndicator;
    }

    /**
     * Obtiene el valor del parámetro interchangeWithoutRestrictionIndicator.
     * 
     * @return interchangeWithoutRestrictionIndicator valor del campo a obtener.
     */
    public String getInterchangeWithoutRestrictionIndicator() {
	return interchangeWithoutRestrictionIndicator;
    }

    /**
     * Guarda el valor del parámetro interchangeWithoutRestrictionIndicator.
     * 
     * @param interchangeWithoutRestrictionIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeWithoutRestrictionIndicator(
	String interchangeWithoutRestrictionIndicator) {
	this.interchangeWithoutRestrictionIndicator = interchangeWithoutRestrictionIndicator;
    }

    /**
     * Obtiene el valor del parámetro interchangeLocalStateIndicator.
     * 
     * @return interchangeLocalStateIndicator valor del campo a obtener.
     */
    public String getInterchangeLocalStateIndicator() {
	return interchangeLocalStateIndicator;
    }

    /**
     * Guarda el valor del parámetro interchangeLocalStateIndicator.
     * 
     * @param interchangeLocalStateIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeLocalStateIndicator(
	String interchangeLocalStateIndicator) {
	this.interchangeLocalStateIndicator = interchangeLocalStateIndicator;
    }

    /**
     * Obtiene el valor del parámetro
     * interchangeWithRestrictionLocalAutonomousIndicator.
     * 
     * @return interchangeWithRestrictionLocalAutonomousIndicator valor del
     *         campo a obtener.
     */
    public String getInterchangeWithRestrictionLocalAutonomousIndicator() {
	return interchangeWithRestrictionLocalAutonomousIndicator;
    }

    /**
     * Guarda el valor del parámetro
     * interchangeWithRestrictionLocalAutonomousIndicator.
     * 
     * @param interchangeWithRestrictionLocalAutonomousIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeWithRestrictionLocalAutonomousIndicator(
	String interchangeWithRestrictionLocalAutonomousIndicator) {
	this.interchangeWithRestrictionLocalAutonomousIndicator =
	    interchangeWithRestrictionLocalAutonomousIndicator;
    }

    /**
     * Obtiene el valor del parámetro
     * interchangeLocalGeneralAutonomousIndicator.
     * 
     * @return interchangeLocalGeneralAutonomousIndicator valor del campo a
     *         obtener.
     */
    public String getInterchangeLocalGeneralAutonomousIndicator() {
	return interchangeLocalGeneralAutonomousIndicator;
    }

    /**
     * Guarda el valor del parámetro interchangeLocalGeneralAutonomousIndicator.
     * 
     * @param interchangeLocalGeneralAutonomousIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeLocalGeneralAutonomousIndicator(
	String interchangeLocalGeneralAutonomousIndicator) {
	this.interchangeLocalGeneralAutonomousIndicator =
	    interchangeLocalGeneralAutonomousIndicator;
    }

    /**
     * Obtiene el valor del parámetro
     * interchangeWithRestrictionLocalLocalIndicator.
     * 
     * @return interchangeWithRestrictionLocalLocalIndicator valor del campo a
     *         obtener.
     */
    public String getInterchangeWithRestrictionLocalLocalIndicator() {
	return interchangeWithRestrictionLocalLocalIndicator;
    }

    /**
     * Guarda el valor del parámetro
     * interchangeWithRestrictionLocalLocalIndicator.
     * 
     * @param interchangeWithRestrictionLocalLocalIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeWithRestrictionLocalLocalIndicator(
	String interchangeWithRestrictionLocalLocalIndicator) {
	this.interchangeWithRestrictionLocalLocalIndicator =
	    interchangeWithRestrictionLocalLocalIndicator;
    }

    /**
     * Obtiene el valor del parámetro interchangeLocalGeneralLocalIndicator.
     * 
     * @return interchangeLocalGeneralLocalIndicator valor del campo a obtener.
     */
    public String getInterchangeLocalGeneralLocalIndicator() {
	return interchangeLocalGeneralLocalIndicator;
    }

    /**
     * Guarda el valor del parámetro interchangeLocalGeneralLocalIndicator.
     * 
     * @param interchangeLocalGeneralLocalIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeLocalGeneralLocalIndicator(
	String interchangeLocalGeneralLocalIndicator) {
	this.interchangeLocalGeneralLocalIndicator = interchangeLocalGeneralLocalIndicator;
    }

    /**
     * Obtiene el valor del parámetro
     * interchangeWithRestrictionCityHallCityHallIndicator.
     * 
     * @return interchangeWithRestrictionCityHallCityHallIndicator valor del
     *         campo a obtener.
     */
    public String getInterchangeWithRestrictionCityHallCityHallIndicator() {
	return interchangeWithRestrictionCityHallCityHallIndicator;
    }

    /**
     * Guarda el valor del parámetro
     * interchangeWithRestrictionCityHallCityHallIndicator.
     * 
     * @param interchangeWithRestrictionCityHallCityHallIndicator
     *            valor del campo a guardar.
     */
    public void setInterchangeWithRestrictionCityHallCityHallIndicator(
	String interchangeWithRestrictionCityHallCityHallIndicator) {
	this.interchangeWithRestrictionCityHallCityHallIndicator =
	    interchangeWithRestrictionCityHallCityHallIndicator;
    }

    /**
     * Obtiene el valor del parámetro creationDate.
     * 
     * @return creationDate valor del campo a obtener.
     */
    public Date getCreationDate() {
	return creationDate;
    }

    /**
     * Guarda el valor del parámetro creationDate.
     * 
     * @param creationDate
     *            valor del campo a guardar.
     */
    public void setCreationDate(
	Date creationDate) {
	this.creationDate = creationDate;
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
     * Obtiene el valor del parámetro bSameAddress.
     * 
     * @return bSameAddress valor del campo a obtener.
     */
    public String getBSameAddress() {
	return bSameAddress;
    }

    /**
     * Guarda el valor del parámetro bSameAddress.
     * 
     * @param bSameAddress
     *            valor del campo a guardar.
     */
    public void setBSameAddress(
	String bSameAddress) {
	this.bSameAddress = bSameAddress;
    }

    /**
     * Obtiene el valor del parámetro businessHours.
     * 
     * @return businessHours valor del campo a obtener.
     */
    public String getBusinessHours() {
	return businessHours;
    }

    /**
     * Guarda el valor del parámetro businessHours.
     * 
     * @param businessHours
     *            valor del campo a guardar.
     */
    public void setBusinessHours(
	String businessHours) {
	this.businessHours = businessHours;
    }

    /**
     * Obtiene el valor del parámetro inhabiliesDays.
     * 
     * @return inhabiliesDays valor del campo a obtener.
     */
    public String getInhabiliesDays() {
	return inhabiliesDays;
    }

    /**
     * Guarda el valor del parámetro inhabiliesDays.
     * 
     * @param inhabiliesDays
     *            valor del campo a guardar.
     */
    public void setInhabiliesDays(
	String inhabiliesDays) {
	this.inhabiliesDays = inhabiliesDays;
    }

    /**
     * Obtiene el valor del parámetro address.
     * 
     * @return address valor del campo a obtener.
     */
    public AddressVO getAddress() {
	return address;
    }

    /**
     * Guarda el valor del parámetro address.
     * 
     * @param address
     *            valor del campo a guardar.
     */
    public void setAddress(
	AddressVO address) {
	this.address = address;
    }

    /**
     * Obtiene el valor del parámetro contacts.
     * 
     * @return contacts valor del campo a obtener.
     */
    public List<ContactOFIVO> getContacts() {
	return contacts;
    }

    /**
     * Guarda el valor del parámetro contacts.
     * 
     * @param contacts
     *            valor del campo a guardar.
     */
    public void setContacts(
	List<ContactOFIVO> contacts) {
	this.contacts = contacts;
    }

    /**
     * Obtiene el valor del parámetro services.
     * 
     * @return services valor del campo a obtener.
     */
    public List<ServiceVO> getServices() {
	return services;
    }

    /**
     * Guarda el valor del parámetro services.
     * 
     * @param services
     *            valor del campo a guardar.
     */
    public void setServices(
	List<ServiceVO> services) {
	this.services = services;
    }

    /**
     * Obtiene el valor del parámetro relationshipsSIROFI.
     * 
     * @return relationshipsSIROFI valor del campo a obtener.
     */
    public List<BasicDataUnitVO> getRelationshipsSIROFI() {
	return relationshipsSIROFI;
    }

    /**
     * Guarda el valor del parámetro relationshipsSIROFI.
     * 
     * @param relationshipsSIROFI
     *            valor del campo a guardar.
     */
    public void setRelationshipsSIROFI(
	List<BasicDataUnitVO> relationshipsSIROFI) {
	this.relationshipsSIROFI = relationshipsSIROFI;
    }

    /**
     * Obtiene el valor del parámetro organizationalRelationshipsOFI.
     * 
     * @return organizationalRelationshipsOFI valor del campo a obtener.
     */
    public List<BasicDataUnitVO> getOrganizationalRelationshipsOFI() {
	return organizationalRelationshipsOFI;
    }

    /**
     * Guarda el valor del parámetro organizationalRelationshipsOFI.
     * 
     * @param organizationalRelationshipsOFI
     *            valor del campo a guardar.
     */
    public void setOrganizationalRelationshipsOFI(
	List<BasicDataUnitVO> organizationalRelationshipsOFI) {
	this.organizationalRelationshipsOFI = organizationalRelationshipsOFI;
    }

    /**
     * añade el contacto a las lista de contactos.
     * 
     * @param contacto
     *            el contacto a guardar.
     */
    public void addContact(
	ContactOFIVO contact) {
	if (this.contacts == null) {
	    this.contacts = new ArrayList<ContactOFIVO>();
	}
	this.contacts.add(contact);
    }

    /**
     * añade el contacto a las lista de servicios.
     * 
     * @param service
     *            el servicio a guardar.
     */
    public void addService(
	ServiceVO service) {
	if (this.services == null) {
	    this.services = new ArrayList<ServiceVO>();
	}
	this.services.add(service);
    }
    
    /**
     * Obtiene el valor del parámetro dependentOfficeName.
     * 
     * @return dependentOfficeName valor del campo a obtener.
     */
    public String getDependentOfficeName() {
        return dependentOfficeName;
    }

    /**
     * Guarda el valor del parámetro dependentOfficeName.
     * 
     * @param dependentOfficeName
     *            valor del campo a guardar.
     */
    public void setDependentOfficeName(
        String dependentOfficeName) {
        this.dependentOfficeName = dependentOfficeName;
    }
    
    
}
