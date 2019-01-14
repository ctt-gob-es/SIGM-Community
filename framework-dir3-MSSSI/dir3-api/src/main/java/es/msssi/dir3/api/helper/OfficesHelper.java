/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.dir3.api.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.type.OperatorCriterionEnum;
import es.msssi.dir3.api.vo.AddressVO;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.ContactOFIVO;
import es.msssi.dir3.api.vo.ContactsOFIVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.OfficeVO;
import es.msssi.dir3.api.vo.OfficesVO;
import es.msssi.dir3.api.vo.ServiceVO;
import es.msssi.dir3.api.vo.ServicesVO;
import es.msssi.dir3.core.vo.Address;
import es.msssi.dir3.core.vo.BasicDataUnit;
import es.msssi.dir3.core.vo.Contact;
import es.msssi.dir3.core.vo.CriteriaOF;
import es.msssi.dir3.core.vo.CriterionOF;
import es.msssi.dir3.core.vo.Office;
import es.msssi.dir3.core.vo.Service;

/**
 * Clase de utilidad para la transformación de objetos con la información de
 * oficinas.
 * 
 * @author cmorenog
 * 
 */
public class OfficesHelper {
	
	private static final Logger LOGGER = Logger.getLogger(OfficesHelper.class);

	/**
	 * Convierte una lista de oficinasVO a oficinas.
	 * 
	 * @param officesVO
	 *            lista de oficinas VO.
	 * @return listaDatosBasicosOficina lista de oficinas.
	 */
	public static List<Office> getOffices(List<OfficeVO> officesVO) {

		List<Office> offices = new ArrayList<Office>();
		Office office = null;
		if (officesVO != null) {
			for (OfficeVO officeVO : officesVO) {
				office = getOffice(officeVO);
				if (office != null) {
					offices.add(office);
				}
			}
		}

		return offices;
	}

	/**
	 * Convierte una oficinaVO a oficina.
	 * 
	 * @param officeVO
	 *            oficina VO.
	 * @return office oficina.
	 */
	public static Office getOffice(OfficeVO officeVO) {

		Office office = null;

		if (officeVO != null) {

			office = new Office();
			office.setId(officeVO.getId());
			office.setName(officeVO.getName());
			office.setOfficeType(officeVO.getOfficeType());
			office.setOfficeTypeName(officeVO.getOfficeTypeName());
			office.setAdministrationLevelId(officeVO.getAdministrationLevelId());
			office.setAdministrationLevelName(officeVO.getAdministrationLevelName());
			office.setAnnulationDate(officeVO.getAnnulationDate());
			office.setBSameAddress(officeVO.getBSameAddress());
			office.setBusinessHours(officeVO.getBusinessHours());
			office.setContactObservations(officeVO.getContactObservations());
			office.setContacts(getContacts(officeVO.getContacts()));
			office.setCreationDate(officeVO.getCreationDate());
			office.setDeleteObservations(officeVO.getDeleteObservations());
			office.setDependentOfficeId(officeVO.getDependentOfficeId());
			office.setElectronicRegisterIndicator(officeVO.getElectronicRegisterIndicator());
			office.setExternalId(officeVO.getExternalId());
			office.setExtinctionDate(officeVO.getExtinctionDate());
			office.setGeneralObservations(officeVO.getGeneralObservations());
			office.setGenericIndicator(officeVO.getGenericIndicator());
			office.setGenericIndicatorUse(officeVO.getGenericIndicatorUse());
			office.setInformationOfficeIndicator(officeVO.getInformationOfficeIndicator());
			office.setInhabiliesDays(officeVO.getInhabiliesDays());
			office.setInterchangeLocalGeneralAutonomousIndicator(officeVO.getInterchangeLocalGeneralAutonomousIndicator());
			office.setInterchangeLocalGeneralLocalIndicator(officeVO.getInterchangeLocalGeneralLocalIndicator());
			office.setInterchangeLocalStateIndicator(officeVO.getInterchangeLocalStateIndicator());
			office.setInterchangeWithoutRestrictionIndicator(officeVO.getInterchangeWithoutRestrictionIndicator());
			office.setInterchangeWithRestrictionCityHallCityHallIndicator(officeVO.getInterchangeWithRestrictionCityHallCityHallIndicator());
			office.setInterchangeWithRestrictionLocalAutonomousIndicator(officeVO.getInterchangeWithRestrictionCityHallCityHallIndicator());
			office.setInterchangeWithRestrictionLocalLocalIndicator(officeVO.getInterchangeWithRestrictionLocalLocalIndicator());

			office.setProcessingOfficeIndicator(officeVO.getProcessingOfficeIndicator());
			office.setRegisterOfficeIndicator(officeVO.getRegisterOfficeIndicator());

			office.setResponsibleUnitId(officeVO.getResponsibleUnitId());
			office.setResponsibleUnitName(officeVO.getResponsibleUnitName());
			office.setSirOfficeIndicator(officeVO.getSirOfficeIndicator());
			office.setStatus(officeVO.getStatus());
			office.setAddress(getAddress(officeVO.getAddress()));
			office.setOrganizationalRelationshipsOFI(getRelationships(officeVO.getOrganizationalRelationshipsOFI()));
			office.setRelationshipsSIROFI(getRelationships(officeVO.getRelationshipsSIROFI()));
			office.setServices(getServices(officeVO.getServices()));
		}
		return office;
	}

	/**
	 * Convierte una lista de DatosBasicosUnidadOrganicaVO a
	 * DatosBasicosUnidadOrganica.
	 * 
	 * @param relacionesSIROFI
	 *            lista de DatosBasicosUnidadOrganicaVO.
	 * @return DatosBasicosUnidadOrganica lista de DatosBasicosUnidadOrganica.
	 */
	public static List<BasicDataUnit> getRelationships(
			List<BasicDataUnitVO> relationships) {
		List<BasicDataUnit> list = null;
		if (relationships != null) {
			list = new ArrayList<BasicDataUnit>();
			BasicDataUnit unit = null;
			for (BasicDataUnitVO rel : relationships) {
				unit = new BasicDataUnit();
				unit.setId(rel.getId());
				unit.setName(rel.getName());
				unit.setStatus(rel.getStatus());
				unit.setFatherUnitId(rel.getFatherUnitId());
				unit.setFatherUnitName(rel.getFatherUnitName());
				list.add(unit);
			}
		}
		return list;
	}

	/**
	 * Convierte una DireccionVO a Direccion.
	 * 
	 * @param addressVO
	 *            DireccionVO.
	 * @return Direccion direccion.
	 */
	private static Address getAddress(AddressVO addressVO) {
		Address address = null;
		if (addressVO != null) {
			address = new Address();
			address.setId(addressVO.getId());
			address.setStreetName(addressVO.getStreetName());
			address.setAddressNum(addressVO.getAddressNum());
			address.setPostalCode(addressVO.getPostalCode());
			address.setAddessInformation(addressVO.getAddessInformation());
			address.setAutonomousCommunityId(addressVO.getAutonomousCommunityId());
			address.setAutonomousCommunityName(addressVO.getAutonomousCommunityName());
			address.setGeographicalEntityId(addressVO.getGeographicalEntityId());
			address.setGeographicalEntityName(addressVO.getGeographicalEntityName());
			address.setCityId(addressVO.getCityId());
			address.setCityName(addressVO.getCityName());
			address.setStreetTypeId(addressVO.getStreetTypeId());
			address.setStreetTypeName(addressVO.getStreetTypeName());
			address.setObservations(addressVO.getObservations());
			address.setProvinceId(addressVO.getProvinceId());
			address.setProvinceName(addressVO.getProvinceName());
			address.setCountryId(addressVO.getCountryId());
			address.setCountryName(addressVO.getCountryName());
		}
		return address;
	}

	/**
	 * Convierte una lista de ContactoVO a Contacto.
	 * 
	 * @param contactsVO
	 *            lista de ContactoVO.
	 * @return Contactos lista de Contactos.
	 */
	private static List<Contact> getContacts(List<ContactOFIVO> contactsVO) {
		List<Contact> contacts = null;
		if (contactsVO != null) {
			contacts = new ArrayList<Contact>();
			Contact contact = null;
			for (ContactOFIVO contactVO : contactsVO) {
				contact = new Contact();
				contact.setId(contactVO.getId());
				contact.setContactInfo(contactVO.getContactInfo());
				contact.setStatusId(contactVO.getStatusId());
				contact.setContactTypeId(contactVO.getContactTypeId());
				contact.setObservations(contactVO.getObservations());
				contacts.add(contact);
			}
		}
		return contacts;
	}

	/**
	 * Convierte una lista de ServicioVO a Servicio.
	 * 
	 * @param servicesVO
	 *            lista de ServicioVO.
	 * @return Servicios lista de Servicios.
	 */
	private static List<Service> getServices(List<ServiceVO> servicesVO) {
		List<Service> services = null;
		if (servicesVO != null) {
			services = new ArrayList<Service>();
			Service service = null;
			for (ServiceVO serviceVO : servicesVO) {
				service = new Service();
				service.setId(serviceVO.getId());
				service.setDescription(serviceVO.getDescription());
				services.add(service);
			}
		}
		return services;
	}

	/**
	 * Completa la lista de oficinas con los contactos y servicios.
	 * 
	 * @param oficinasDCO
	 *            lista de oficinas a completar.
	 * @param contactosUO
	 *            lista de contactos con los que completar las unidades.
	 * @return list lista de oficinas completa.
	 */
	public static List<OfficeVO> completeOffices(OfficesVO officesDCO, ContactsOFIVO contactsDCO, ServicesVO servicesDCO) {
		List<OfficeVO> listOffice = null;
		
		Map<String, List<ContactOFIVO>> mapaContactosOFIVO = new HashMap<String, List<ContactOFIVO>>();		
		for (ContactOFIVO contact : contactsDCO.getContacts()) {
			List<ContactOFIVO> listaContactos = mapaContactosOFIVO.get(contact.getOfficeId());
			
			if(null == listaContactos){
				listaContactos = new ArrayList<ContactOFIVO>();
			}			
			listaContactos.add(contact);
			mapaContactosOFIVO.put(contact.getOfficeId(), listaContactos);
		}
		
		Map<String, List<ServiceVO>> mapaServiciosOFIVO = new HashMap<String, List<ServiceVO>>();		
		for (ServiceVO service : servicesDCO.getServices()) {
			List<ServiceVO> listaServicios = mapaServiciosOFIVO.get(service.getOfficeId());
			
			if(null == listaServicios){
				listaServicios = new ArrayList<ServiceVO>();
			}			
			listaServicios.add(service);
			mapaServiciosOFIVO.put(service.getOfficeId(), listaServicios);
		}
		
		if (officesDCO != null && officesDCO.getOffices() != null && officesDCO.getOffices().size() > 0) {
			String codigoOffice = null;
			listOffice = new ArrayList<OfficeVO>();
			for (OfficeVO office : officesDCO.getOffices()) {
				codigoOffice = office.getId();

				if (contactsDCO != null && contactsDCO.getContacts() != null) {
					List<ContactOFIVO> listaContactos = mapaContactosOFIVO.get(codigoOffice);
					if(null != listaContactos){
						for (ContactOFIVO contact : listaContactos) {
							office.addContact(contact);
						}
					}
				}
				if (servicesDCO != null && servicesDCO.getServices() != null) {
					List<ServiceVO> listaServicios = mapaServiciosOFIVO.get(codigoOffice);
					if(null != listaServicios){
						for (ServiceVO service : listaServicios) {
							office.addService(service);
						}
					}
				}
				
				office.setName(UTFHelper.parseUTF8ToISO885916(office.getName()));
				office.setBusinessHours(UTFHelper.parseUTF8ToISO885916(office.getBusinessHours()));
				
				AddressVO direccion = office.getAddress();
				if(null != direccion){
					direccion.setCityName(UTFHelper.parseUTF8ToISO885916(direccion.getCityName()));
					direccion.setCountryName(UTFHelper.parseUTF8ToISO885916(direccion.getCountryName()));
					direccion.setStreetName(UTFHelper.parseUTF8ToISO885916(direccion.getStreetName()));
					direccion.setAutonomousCommunityName(UTFHelper.parseUTF8ToISO885916(direccion.getAutonomousCommunityName()));
					direccion.setGeographicalEntityName(UTFHelper.parseUTF8ToISO885916(direccion.getGeographicalEntityName()));
					direccion.setProvinceName(UTFHelper.parseUTF8ToISO885916(direccion.getProvinceName()));
					direccion.setObservations(UTFHelper.parseUTF8ToISO885916(direccion.getObservations()));
					direccion.setObservationsloc(UTFHelper.parseUTF8ToISO885916(direccion.getObservationsloc()));
				} else {
					LOGGER.error("ERROR: dirección NULL para la oficina: " + office.getName());
				}
				
				office.setAddress(direccion);
				
				office.setContactObservations(UTFHelper.parseUTF8ToISO885916(office.getContactObservations()));
				office.setDeleteObservations(UTFHelper.parseUTF8ToISO885916(office.getDeleteObservations()));
				office.setGeneralObservations(UTFHelper.parseUTF8ToISO885916(office.getGeneralObservations()));
				
				listOffice.add(office);
			}
		}
		return listOffice;
	}

	/**
	 * Convierte una lista de criterios del core a Criterios de oficinas del
	 * api.
	 * 
	 * @param criteriaCore
	 *            lista de criterios de oficinas del core.
	 * @return criteria lista de Criterios de oficinas del api.
	 */
	public static Criteria<OfficeCriterionEnum> criteriaOFCoretoCriteriaAPIOF(CriteriaOF criteriaCore) {
		Criteria<OfficeCriterionEnum> criteriaAPI = null;
		if (criteriaCore != null) {
			criteriaAPI = new Criteria<OfficeCriterionEnum>();

			if (criteriaCore.getPageInfo() != null) {
				criteriaAPI.setPageInfo(criteriaCore.getPageInfo());
			}
			if (criteriaCore.getOrderBy() != null) {
				List<OfficeCriterionEnum> orderBy = new ArrayList<OfficeCriterionEnum>();
				for (es.msssi.dir3.core.type.OFCriterionEnum enumE : criteriaCore.getOrderBy()) {
					orderBy.add(OfficeCriterionEnum.getNameCriterion(enumE.getValue()));
				}
				criteriaAPI.setOrderBy(orderBy);
			}
			if (criteriaCore.getCriteria() != null) {
				List<Criterion<OfficeCriterionEnum>> criteria = new ArrayList<Criterion<OfficeCriterionEnum>>();
				for (CriterionOF criterioCore : criteriaCore.getCriteria()) {
					Criterion<OfficeCriterionEnum> criterionAPI = new Criterion<OfficeCriterionEnum>( OfficeCriterionEnum.getCriterion(criterioCore.getName().getValue()), OperatorCriterionEnum.getOperatorCriterion(criterioCore.getOperator().getValue()), criterioCore.getValue());
					criteria.add(criterionAPI);
				}
				criteriaAPI.setCriteria(criteria);
			}
		}
		return criteriaAPI;

	}

	/**
	 * Convierte una lista de criterios del core a Criterios de oficinas del
	 * api.
	 * 
	 * @param criteriaCore
	 *            lista de criterios de oficinas del core.
	 * @return criteria lista de Criterios de oficinas del api.
	 */
	public static List<Criterion<OfficeCriterionEnum>> criterionListOFCoretoCriterionListAPIOF(
			List<CriterionOF> criteriaCore) {
		List<Criterion<OfficeCriterionEnum>> criteria = null;
		if (criteriaCore != null) {
			if (criteriaCore != null) {
				criteria = new ArrayList<Criterion<OfficeCriterionEnum>>();
				for (CriterionOF criterionCore : criteriaCore) {
					Criterion<OfficeCriterionEnum> criterionAPI = new Criterion<OfficeCriterionEnum>(OfficeCriterionEnum.getNameCriterion(criterionCore.getName().getValue()), OperatorCriterionEnum.getOperatorCriterion(criterionCore.getOperator().getValue()), criterionCore.getValue());
					criteria.add(criterionAPI);
				}
			}
		}
		return criteria;
	}

}