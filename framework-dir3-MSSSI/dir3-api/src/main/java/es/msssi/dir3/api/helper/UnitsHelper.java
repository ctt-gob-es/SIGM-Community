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

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.UnitDao;
import es.msssi.dir3.api.dao.direccion.ConstantesAddress;
import es.msssi.dir3.api.type.OperatorCriterionEnum;
import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.type.UORootEnum;
import es.msssi.dir3.api.vo.AddressVO;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.ContactUOVO;
import es.msssi.dir3.api.vo.ContactsUOVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.UnitVO;
import es.msssi.dir3.api.vo.UnitsVO;
import es.msssi.dir3.core.vo.Address;
import es.msssi.dir3.core.vo.BasicDataOffice;
import es.msssi.dir3.core.vo.Contact;
import es.msssi.dir3.core.vo.CriteriaUO;
import es.msssi.dir3.core.vo.CriterionUO;
import es.msssi.dir3.core.vo.Unit;

/**
 * Clase de utilidad para la transformación de objetos con la información de
 * unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class UnitsHelper {
	
	public static final Logger LOGGER = Logger.getLogger(UnitsHelper.class);

	/**
	 * Convierte una lista de UnidadOrganicaVO a UnidadOrganica.
	 * 
	 * @param unitListVO
	 *            lista de Unidades Orgánicas VO.
	 * @return listaDatosBasicosUnidadOrganica lista de Unidades Orgánicas.
	 */
	public static List<Unit> getUnits(List<UnitVO> unitListVO) {

		List<Unit> unitList = new ArrayList<Unit>();

		if (unitListVO != null) {
			for (UnitVO unitVO : unitListVO) {
				Unit unit = getUnit(unitVO);
				if (unit != null) {
					unitList.add(unit);
				}
			}
		}
		return unitList;
	}

	/**
	 * Convierte una UnidadOrganicaVO a UnidadOrganica.
	 * 
	 * @param unidadOrganicaVO
	 *            Unidad Organica VO.
	 * @return UnidadOrganica UnidadOrganica.
	 */
	public static Unit getUnit(UnitVO unitVO) {

		Unit unit = null;

		if (unitVO != null) {
			
			unit = new Unit();
			
			unit.setId(unitVO.getId());
			unit.setAcronyms(unitVO.getAcronyms());
			unit.setName(unitVO.getName());
			unit.setAdministrationLevelId(unitVO.getAdministrationLevelId());
			unit.setAdministrationLevelName(unitVO.getAdministrationLevelName());
			unit.setAuthorityId(unitVO.getAuthorityId());
			unit.setAuthorityName(unitVO.getAuthorityName());
			unit.setUnitTypeId(unitVO.getUnitTypeId());
			unit.setUnitTypeName(unitVO.getUnitTypeName());
			unit.setBSameAddressFatherUnit(unitVO.getBSameAddressFatherUnit());
			unit.setCif(unitVO.getCif());
			unit.setCompetencesOfUnit(unitVO.getCompetencesOfUnit());
			unit.setLegalProvisionOfCompetencesOfUnit(unitVO.getLegalProvisionOfCompetencesOfUnit());
			unit.setContactObservations(unitVO.getContactObservations());
			unit.setDeleteObservations(unitVO.getDeleteObservations());
			unit.setGeneralObservations(unitVO.getGeneralObservations());
			unit.setAddOfficialDate(unitVO.getAddOfficialDate());
			unit.setAnnulationDate(unitVO.getAnnulationDate());
			unit.setExtinctionDate(unitVO.getExtinctionDate());
			unit.setDeleteOfficialDate(unitVO.getDeleteOfficialDate());
			unit.setEntityPublicLawIndicator(unitVO.getEntityPublicLawIndicator());
			unit.setEntityUnitOfPublicLawId(unitVO.getEntityUnitOfPublicLawId());
			unit.setEntityUnitOfPublicLawName(unitVO.getEntityUnitOfPublicLawName());
			unit.setExternalId(unitVO.getExternalId());
			unit.setFatherUnitId(unitVO.getFatherUnitId());
			unit.setFatherUnitName(unitVO.getFatherUnitName());
			unit.setForeignLocation(unitVO.getForeignLocation());
			unit.setRootUnit(unitVO.getRootUnit());
			unit.setStatus(unitVO.getStatus());
			unit.setGeographicalEntityId(unitVO.getGeographicalEntityId());
			unit.setGeographicalEntityName(unitVO.getGeographicalEntityName());
			unit.setCountryId(unitVO.getCountryId());
			unit.setCountryName(unitVO.getCountryName());
			unit.setAutonomousCommunityId(unitVO.getAutonomousCommunityId());
			unit.setAutonomousCommunityName(unitVO.getAutonomousCommunityName());
			unit.setMinorLocalEntityId(unitVO.getMinorLocalEntityId());
			unit.setMinorLocalEntityName(unitVO.getMinorLocalEntityName());
			unit.setProvinceId(unitVO.getProvinceId());
			unit.setProvinceName(unitVO.getProvinceName());
			unit.setIslandId(unitVO.getIslandId());
			unit.setIslandName(unitVO.getIslandName());
			unit.setCityId(unitVO.getCityId());
			unit.setCityName(unitVO.getCityName());
			unit.setTerritorialScopeOfCompetenceId(unitVO.getTerritorialScopeOfCompetenceId());
			unit.setTerritorialScopeOfCompetenceName(unitVO.getTerritorialScopeOfCompetenceName());
			unit.setLanguage(unitVO.getLanguage());
			unit.setPrincipalUnitId(unitVO.getPrincipalUnitId());
			unit.setPrincipalUnitName(unitVO.getPrincipalUnitName());
			unit.setPublicEntityLevel(unitVO.getPublicEntityLevel());
			unit.setPublicEntityTypeId(unitVO.getPublicEntityTypeId());
			unit.setPublicEntityTypeName(unitVO.getPublicEntityTypeName());
			unit.setRelationshipsSIRUO(getRelationship(unitVO.getRelationshipsSIRUO()));
			unit.setOrganizationalRelationshipsUO(getRelationships(unitVO.getOrganizationalRelationshipsUO()));
			
			AddressVO direccion = unitVO.getAddress();
			if(null != direccion){
				direccion.setStreetName(UTFHelper.parseUTF8ToISO885916(direccion.getStreetName()));
				direccion.setForeignAddress(UTFHelper.parseUTF8ToISO885916(direccion.getForeignAddress()));
				unit.setAddress(getAddress(direccion));
			}
			
			unit.setLastUpdateDate(unitVO.getLastUpdateDate());
			unit.setSystemCreationDate(unitVO.getSystemCreationDate());
			unit.setContacts(getContacts(unitVO.getContacts()));
		}
		
		return unit;
	}

	/**
	 * Convierte una lista de ContactoVO a Contacto.
	 * 
	 * @param contactsVO
	 *            lista de ContactoVO.
	 * @return Contactos lista de Contactos.
	 */
	private static List<Contact> getContacts(List<ContactUOVO> contactsVO) {
		
		List<Contact> contacts = null;
		
		if (contactsVO != null) {
			
			contacts = new ArrayList<Contact>();
			Contact contact = null;
			
			for (ContactUOVO contactVO : contactsVO) {
				
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
	 * Convierte un DatosBasicosOficinaVO a DatosBasicosOficina.
	 * 
	 * @param relacion
	 *            DatosBasicosOficinaVO.
	 * @return DatosBasicosOficina lista de DatosBasicosOficina.
	 */
	private static BasicDataOffice getRelationship( BasicDataOfficeVO relationship) {
		BasicDataOffice rel = null;
		
		if (relationship != null) {
			
			rel = new BasicDataOffice();
			
			rel.setId(relationship.getId());
			rel.setName(relationship.getName());
			rel.setOfficeType(relationship.getOfficeType());
			rel.setOfficeTypeName(relationship.getOfficeTypeName());
			rel.setResponsibleUnitId(relationship.getResponsibleUnitId());
			rel.setResponsibleUnitName(relationship.getResponsibleUnitName());
			rel.setStatus(relationship.getStatus());
		}
		return rel;
	}

	/**
	 * Convierte una lista de DatosBasicosOficinaVO a DatosBasicosOficina.
	 * 
	 * @param relationships
	 *            lista de DatosBasicosOficinaVO.
	 * @return DatosBasicosOficina lista de DatosBasicosOficina.
	 */
	public static List<BasicDataOffice> getRelationships( List<BasicDataOfficeVO> relationships) {
		List<BasicDataOffice> list = null;
		
		if (relationships != null) {
			
			list = new ArrayList<BasicDataOffice>();
			BasicDataOffice office = null;
			
			for (BasicDataOfficeVO rel : relationships) {
				office = getRelationship(rel);
				list.add(office);
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
	 * Completa la lista de unidades con los contactos y con la raiz si no la trae.
	 * 
	 * @param unidadesDCO
	 *            lista de unidades a completar.
	 * @param contactosUO
	 *            lista de contactos con los que completar las unidades.
	 * @return list lista de unidades completa.
	 */
	public static List<UnitVO> completeUnits(UnitsVO unitsDCO, ContactsUOVO contactsUO, boolean isUnit) {
		
		List<UnitVO> listUO = null;
		
		Map<String, List<ContactUOVO>> mapaContactosUO = new HashMap<String, List<ContactUOVO>>();
		
		if(null != contactsUO && null != contactsUO.getContacts()){
			for (ContactUOVO contact : contactsUO.getContacts()) {
				List<ContactUOVO> listaContactos = mapaContactosUO.get(contact.getUnitId());
				
				if(null == listaContactos){
					listaContactos = new ArrayList<ContactUOVO>();
				}			
				listaContactos.add(contact);
				mapaContactosUO.put(contact.getUnitId(), listaContactos);
			}
		}
		
		if (unitsDCO != null && unitsDCO.getUnits() != null) {
			
			String codeUO = null;
			listUO = new ArrayList<UnitVO>();
			
			for (UnitVO unit : unitsDCO.getUnits()) {
				codeUO = unit.getId();
				if (isUnit && (unit.getRootUnit() == null || "".equals(unit.getRootUnit()))) {
					unit.setRootUnit(UORootEnum.getRaiz(codeUO).getName());
				}
				if (!isUnit) {
					unit.setTypeUnit(0);
					unit.setFatherUnitId(unit.getUnoCodUOResponsable());
				}
				if (contactsUO != null && contactsUO.getContacts() != null) {
					List<ContactUOVO> listaContactos = mapaContactosUO.get(codeUO);
					if(null != listaContactos){
						for (ContactUOVO contact : listaContactos) {
							unit.addContact(contact);
						}
					}
				}

				// [Dipucr-Manu] - INICIO - El campo acron de SCR_ORGS no puede ser null, si es null le meto un blanco porque la mayoría no trae siglas.
				if (null == unit.getAcronyms()) {
					unit.setAcronyms("");
				}
				// [Dipucr-Manu] - FIN -

				// [Dipucr-Manu] - INICIO - Al igual que con los datos básicos la Mancomunitat de Municipis de L'Àrea Metropolitana de Barcelona no ha puesto
				// apóstrofe en la descripción si no otra cosa y falla la codificación, se reemplaza.
				unit.setName(UTFHelper.parseUTF8ToISO885916(unit.getName()));
				unit.setFatherUnitName(UTFHelper.parseUTF8ToISO885916(unit.getFatherUnitName()));
				unit.setPrincipalUnitName(UTFHelper.parseUTF8ToISO885916(unit.getPrincipalUnitName()));

				unit.setContactObservations(UTFHelper.parseUTF8ToISO885916(unit.getContactObservations()));
				unit.setDeleteObservations(UTFHelper.parseUTF8ToISO885916(unit.getDeleteObservations()));
				unit.setGeneralObservations(UTFHelper.parseUTF8ToISO885916(unit.getGeneralObservations()));
				unit.setUnoobservationesbaja(UTFHelper.parseUTF8ToISO885916(unit.getUnoobservationesbaja()));
				unit.setUnoobservations(UTFHelper.parseUTF8ToISO885916(unit.getUnoobservations()));
				
				rellenaDireccion(unit.getAddress());

				listUO.add(unit);
			}
		}
		return listUO;
	}

	/**
	 * Convierte los criterios del core en criterios del API.
	 * 
	 * @param criteriaCore
	 *            criterios provenientes del core.
	 * @return Criterios<CriterioUnidadOrganicaEnum> criterios del API.
	 */
	public static Criteria<UOCriterionEnum> criteriaUOCoretoCriteriaAPIUO( CriteriaUO criteriaCore) {
		
		Criteria<UOCriterionEnum> criteriaAPI = null;
		
		if (criteriaCore != null) {
			criteriaAPI = new Criteria<UOCriterionEnum>();

			if (criteriaCore.getPageInfo() != null) {
				criteriaAPI.setPageInfo(criteriaCore.getPageInfo());
			}
			if (criteriaCore.getOrderBy() != null) {
				List<UOCriterionEnum> orderBy = new ArrayList<UOCriterionEnum>();
				for (es.msssi.dir3.core.type.UOCriterionEnum enumE : criteriaCore.getOrderBy()) {
					orderBy.add(UOCriterionEnum.getNameCriterion(enumE.getValue()));
				}
				criteriaAPI.setOrderBy(orderBy);
			}
			if (criteriaCore.getCriteria() != null) {
				List<Criterion<UOCriterionEnum>> criteria = new ArrayList<Criterion<UOCriterionEnum>>();
				for (CriterionUO criterionCore : criteriaCore.getCriteria()) {
					Criterion<UOCriterionEnum> criterionAPI = new Criterion<UOCriterionEnum>(UOCriterionEnum.getNameCriterion(criterionCore.getName().getValue()), OperatorCriterionEnum.getOperatorCriterion(criterionCore.getOperator().getValue()), criterionCore.getValue());
					criteria.add(criterionAPI);
				}
				criteriaAPI.setCriteria(criteria);
			}
		}
		return criteriaAPI;
	}

	/**
	 * Convierte los criterios del core en criterios del API.
	 * 
	 * @param criteriaCore
	 *            criterios provenientes del core.
	 * @return Criterios<CriterioUnidadOrganicaEnum> criterios del API.
	 */
	public static List<Criterion<UOCriterionEnum>> criterionListUOCoretoCriterionListAPIUO( List<CriterionUO> criteriaCore) {
		
		List<Criterion<UOCriterionEnum>> criteria = null;
		
		if (criteriaCore != null) {
			if (criteriaCore != null) {
				
				criteria = new ArrayList<Criterion<UOCriterionEnum>>();
				
				for (CriterionUO criterionCore : criteriaCore) {
					Criterion<UOCriterionEnum> criterionAPI = new Criterion<UOCriterionEnum>( UOCriterionEnum.getNameCriterion(criterionCore.getName().getValue()), OperatorCriterionEnum.getOperatorCriterion(criterionCore.getOperator().getValue()), criterionCore.getValue());
					criteria.add(criterionAPI);
				}
			}
		}
		return criteria;
	}

	public static void rellenaDireccion(AddressVO direccion) {
		if(null != direccion){
			if( StringUtils.isNotEmpty(direccion.getStreetName())){
				direccion.setStreetName(UTFHelper.parseUTF8ToISO885916(direccion.getStreetName()));
			}
			
			direccion.setForeignAddress(UTFHelper.parseUTF8ToISO885916(direccion.getForeignAddress()));	
			
			if(StringUtils.isNotEmpty(direccion.getCountryId())){
				direccion.setCountryName(UTFHelper.parseUTF8ToISO885916(ConstantesAddress.PAISES.get(direccion.getCountryId())));
			}
			if(StringUtils.isNotEmpty(direccion.getAutonomousCommunityId())){
				direccion.setAutonomousCommunityName(UTFHelper.parseUTF8ToISO885916(ConstantesAddress.COMUNIADES_AUTONOMAS.get(direccion.getAutonomousCommunityId())));
			}
			if(StringUtils.isNotEmpty(direccion.getProvinceId())){
				direccion.setProvinceName(UTFHelper.parseUTF8ToISO885916(ConstantesAddress.PROVINCIAS.get(direccion.getProvinceId())));
			}
			if(StringUtils.isNotEmpty(direccion.getStreetTypeId())){
				direccion.setStreetTypeName(UTFHelper.parseUTF8ToISO885916(ConstantesAddress.TIPOS_DE_VIA.get(direccion.getStreetTypeId())));
			}
		}
	}	
}