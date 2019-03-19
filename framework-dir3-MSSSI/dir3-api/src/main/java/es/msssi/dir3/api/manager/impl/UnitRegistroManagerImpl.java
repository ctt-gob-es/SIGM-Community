/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.dir3.api.manager.impl;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import org.apache.log4j.Logger;

import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.business.dao.UnidadAdministrativaDAO;
import es.ieci.tecdoc.isicres.api.business.keys.ConstantKeys;
import es.ieci.tecdoc.isicres.api.business.vo.CriterioBusquedaTipoUnidadAdministrativaVO;
import es.ieci.tecdoc.isicres.api.business.vo.TipoUnidadAdministrativaVO;
import es.msssi.dir3.api.dao.AddressDao;
import es.msssi.dir3.api.dao.AddressRegistroDao;
import es.msssi.dir3.api.dao.UnitRegistroDao;
import es.msssi.dir3.api.helper.UnitsHelper;
import es.msssi.dir3.api.manager.UnitRegistroManager;
import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.vo.AddressVO;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.ContactUOVO;
import es.msssi.dir3.api.vo.ContactsUOVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.UnitVO;
import es.msssi.dir3.api.vo.UnitsVO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;
import es.msssi.dir3.core.vo.AddressRegistroVO;
import es.msssi.dir3.core.vo.UnitRegistro;

/**
 * Implementación del manager de datos básicos de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class UnitRegistroManagerImpl implements UnitRegistroManager,
		Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 7016415655435529287L;
	/**
	 * Logger de la clase.
	 */
	private static final Logger LOGGER = Logger.getLogger(UnitRegistroManagerImpl.class);
	private UnitRegistroDao unitDao;
	private AddressRegistroDao addressRegistroDao;
	private AddressDao addressDao;
	
	private UnidadAdministrativaDAO unidadAdministrativaDAO;

	/**
	 * Constructor.
	 * 
	 */
	public UnitRegistroManagerImpl() {

	}

	/**
	 * Devuelve el número de unidades orgánicas existentes y que cumplan los
	 * criterios establecidos.
	 * 
	 * @param criteria
	 *            criterios de búsqueda.
	 * @return int el número de unidades orgánicas.
	 * @throws DIR3Exception .
	 */
	public int count(List<Criterion<UOCriterionEnum>> criteriaAPI) throws DIR3Exception {

		LOGGER.info("Obteniendo el número de unidades orgánicas. Criterios: {}");
		int result = 0;
		// Obtiene el número de unidades orgánicas en base a los criterios
		try {
			result = unitDao.count(criteriaAPI);
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.GET_COUNT_UNIT_ERROR, ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE, sqlException);
		}
		LOGGER.info("Número de unidades orgánicas: " + result);
		return result;
	}

	/**
	 * Devuelve el número de unidades orgánicas existentes. Para obtener el
	 * resultado delega en el dao asociado.
	 * 
	 * @return int el número de unidades orgánicas.
	 * @throws DIR3Exception .
	 */
	public int count() throws DIR3Exception {
		LOGGER.info("Obteniendo el número de unidades orgánicas.");
		int result = 0;
		// Obtiene el número de unidades orgánicas
		try {
			result = unitDao.count(null);
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.GET_COUNT_UNIT_ERROR, ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE, sqlException);
		}
		
		LOGGER.info("Número total de unidades orgánicas: " + result);
		return result;
	}

	/**
	 * Método genérico para recuperar una unidad orgánica basándonos en su
	 * identificador.
	 * 
	 * @param codeUnit
	 *            identificador de la unidad orgánica a recuperar.
	 * @return el objeto recuperado.
	 * @throws DIR3Exception .
	 */
	public UnitVO get(String codeUnit) throws DIR3Exception {
		UnitVO result = null;
		
		try {
			LOGGER.info("Recuperando una unidad orgánica con id: " + codeUnit);
			// Buscamos datos basicos de la unidad organica
			result = (UnitVO) unitDao.get(codeUnit);

		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.GET_UNIT_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.GET_UNIT_ERROR, ErrorConstants.GET_UNIT_ERROR_MESSAGE, sqlException);
		}
		return result;
	}

	/**
	 * Método genérico para recuperar todas las unidades orgánicas. Viene a ser
	 * lo mismo que recuperar todas las filas de una tabla de base de datos.
	 * 
	 * @return la lista de unidades orgánicas recuperados.
	 * @throws DIR3Exception .
	 */
	public List<UnitVO> getAll() throws DIR3Exception {
		LOGGER.info("Realizando búsqueda de todas las unidades orgánicas.");
		List<UnitVO> result = null;
		// Realiza la búsqueda de unidades orgánicas
		try {
			result = unitDao.find(null);
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.GET_ALL_UNITS_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.GET_ALL_UNITS_ERROR, ErrorConstants.GET_ALL_UNITS_ERROR_MESSAGE, sqlException);
		}
		LOGGER.info("Número de unidades orgánicas encontradas: " + result != null ? result .size() : "null");
		return result;
	}

	/**
	 * Método genérico para recuperar todas las unidades orgánicas y que cumplan
	 * con los criterios establecidos. Si criterios es nulo viene a ser lo mismo
	 * que recuperar todas las filas de una tabla de base de datos.
	 * 
	 * @param criteria
	 *            criterios de búsqueda.
	 * @return la lista de objetos recuperados.
	 * @throws DIR3Exception
	 */
	public List<UnitVO> find(Criteria<UOCriterionEnum> criteria) throws DIR3Exception {

		LOGGER.info("Realizando búsqueda de unidades orgánicas. Criterios: {}");
		List<UnitVO> result = null;
		// Realiza la búsqueda de unidades orgánicas en base a los criterios
		try {
			result = unitDao.find(criteria);
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.FIND_UNITS_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.FIND_UNITS_ERROR, ErrorConstants.FIND_UNITS_ERROR_MESSAGE, sqlException);
		}
		LOGGER.info("Número de unidades orgánicas encontradas: " + result != null ? result .size() : "null");
		
		return result;
	}

	/**
	 * Método genérico para recuperar todas las unidades orgánicas y que cumplan
	 * con los criterios establecidos. Si criterios es nulo viene a ser lo mismo
	 * que recuperar todas las filas de una tabla de base de datos.
	 * 
	 * @param criteria
	 *            criterios de búsqueda.
	 * @return la lista de objetos recuperados.
	 * @throws DIR3Exception
	 */
	public List<BasicDataUnitVO> findBasicData( Criteria<UOCriterionEnum> criteria) throws DIR3Exception {
		LOGGER.info("Realizando búsqueda de datos básicos de unidades orgánicas. Criterios: {}");
		List<BasicDataUnitVO> result = null;
		// Realiza la búsqueda de unidades orgánicas en base a los criterios
		try {
			result = unitDao.findBasicData(criteria);
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.FIND_UNITS_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.FIND_UNITS_ERROR, ErrorConstants.FIND_UNITS_ERROR_MESSAGE, sqlException);
		}
		LOGGER.info("Número de unidades orgánicas encontradas: " + result != null ? result.size() : "null");
		return result;
	}

	/**
	 * Comprueba la existencia de una unidad orgánica con el identificador dado.
	 * 
	 * @param id
	 *            identificador del objeto.
	 * @return <code>true</code> si el objeto existe, <code>false</code> en caso
	 *         contrario.
	 * @throws DIR3Exception .
	 */
	public boolean exists(String id) throws DIR3Exception {
		boolean result = false;
		try {
			LOGGER.info("Comprobando la existencia de una unidad orgánica con id: " + id);
			// Buscamos datos sobre la unidad orgánica
			result = unitDao.exists(id);
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.EXISTS_UNIT_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.EXISTS_UNIT_ERROR, ErrorConstants.EXISTS_UNIT_ERROR_MESSAGE, sqlException);
		}
		LOGGER.info("La unidad orgánica existe?: " + result);
		return result;
	}

	/**
	 * Inserta una unidad.
	 * 
	 * @param unit
	 *            unidad a insertar.
	 * @throws DIR3Exception
	 * 
	 */
	public void save(UnitVO unit) throws DIR3Exception {
		UnitRegistro tempUnit = null;
		LOGGER.info("Insertando una unidad");
		try {
			if (unit != null) {
				LOGGER.info("Insertando la unidad: " + unit.getId());
				tempUnit = mappingUnidadRegistral(unit);
				addressRegistroDao.setSqlMapClient(unitDao.getSqlMapClient());
				unitDao.startTransaction();

				LOGGER.debug("Insertando la unidad");
				unitDao.save(tempUnit);
				Integer idaddressDao = addressDao.getDirectionOrg(unit.getId());
				if (idaddressDao != null) {
					AddressVO address = addressDao.get(idaddressDao);
					AddressRegistroVO addressRegistroVO = mappingAddressRegistro( address, unit.getContacts());
					addressRegistroVO.setId_orgs(unit.getId());
					
					if(addressRegistroDao.exists((addressRegistroDao.getIdOrgs(unit.getId())).toString())){
						addressRegistroDao.update(addressRegistroVO);
					} else {
						addressRegistroDao.save(addressRegistroVO);
					}
				}
				unitDao.commitTransaction();
				LOGGER.info("Fin de Insertando una unidad");
			}
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.INSERT_UNIT_ERROR_MESSAGE, sqlException);
			LOGGER.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " + unit.getId());
			throw new DIR3Exception(DIR3ErrorCode.INSERT_UNIT_ERROR, ErrorConstants.INSERT_UNIT_ERROR_MESSAGE, sqlException);
			
		} catch (Exception sqlException) {
			LOGGER.error(ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);
			LOGGER.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " + unit.getId());
			throw new DIR3Exception(DIR3ErrorCode.UPDATE_UNIT_ERROR, ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);
		
		} finally {
			unitDao.endTransaction();
		}
	}
	
	private UnitRegistro mappingUnidadRegistral(UnitVO entity) {
		UnitRegistro result = new UnitRegistro();
		result.setCode(entity.getId());
		if(null != entity.getId() && !entity.getId().equals(entity.getFatherUnitId())){
			result.setCodeFather(entity.getFatherUnitId());
		}
		result.setName(entity.getName());
		result.setEnabled(1);
		
		Locale locale = new Locale(ConstantKeys.LOCALE_LENGUAGE_DEFAULT, ConstantKeys.LOCALE_COUNTRY_DEFAULT);
		CriterioBusquedaTipoUnidadAdministrativaVO criterio = new CriterioBusquedaTipoUnidadAdministrativaVO();
		criterio.setLimit(Long.valueOf(Integer.MAX_VALUE));
		criterio.setOffset(0L);
		
		TipoUnidadAdministrativaVO tipoUnidad = unidadAdministrativaDAO.getTipoUnidadesAdminByCode(entity.getId().substring(0, 1), locale, criterio);
		
		if(null == tipoUnidad){
			tipoUnidad = unidadAdministrativaDAO.getTipoUnidadesAdminByCode("I", locale, criterio);
		}
		
		result.setType(Integer.parseInt(tipoUnidad.getId()));
		
		result.setCif(entity.getCif());
		result.setHierarchicalLevel(entity.getPublicEntityLevel());
		result.setAdminLevel(entity.getAdministrationLevelId());
		result.setEntityType(entity.getPublicEntityTypeId());
		result.setUoType(entity.getUnitTypeId());
		result.setCodeRoot(entity.getRootUnit());
		result.setIdCCAA(entity.getAutonomousCommunityId());

		// [Dipucr-Manu] El campo acron no puede ser nulo
		result.setAcron(entity.getAcronyms());

		if (StringUtils.isInteger(entity.getProvinceId())) {
			result.setIdProv(Integer.parseInt(entity.getProvinceId()));
		}

		return result;
	}


	/**
	 * Inserta una unidad.
	 * 
	 * @param unit
	 *            unidad a insertar.
	 * @throws DIR3Exception
	 * 
	 */
	public void delete(UnitVO unit) throws DIR3Exception {

		LOGGER.info("borramos una unidad");
		try {
			if (unit != null) {
				LOGGER.info("borramos la unidad: " + unit.getId());
				unitDao.startTransaction();

				LOGGER.debug("borramos la unidad");
				unitDao.delete(unit.getId());

				unitDao.commitTransaction();
				LOGGER.info("Fin de borramos una unidad");
			}
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.INSERT_UNIT_ERROR_MESSAGE, sqlException);
			LOGGER.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " + unit.getId());
			throw new DIR3Exception(DIR3ErrorCode.INSERT_UNIT_ERROR, ErrorConstants.INSERT_UNIT_ERROR_MESSAGE, sqlException);
		} finally {
			unitDao.endTransaction();
		}
	}

	/**
	 * Modificar la unidad orgánica.
	 * 
	 * @param unit
	 *            unidad orgánica a modificar.
	 * @throws DIR3Exception .
	 */
	public void update(UnitVO unit) throws DIR3Exception {
		UnitRegistro tempUnit = null;
		try {
			if (unit != null) {
				LOGGER.info("Modificando la unidad: " + unit.getId());
				tempUnit = mappingUnidadRegistral(unit);
				addressRegistroDao.setSqlMapClient(unitDao.getSqlMapClient());
				unitDao.startTransaction();
				LOGGER.debug("buscando la dirección de la unidad");
				unitDao.update(tempUnit);
				addressRegistroDao.delete(unit.getId());
				Integer idaddressDao = addressDao.getDirectionOrg(unit.getId());
				if (idaddressDao != null) {
					AddressVO address = addressDao.get(idaddressDao);
					AddressRegistroVO addressRegistroVO = mappingAddressRegistro( address, unit.getContacts());
					addressRegistroVO.setId_orgs(unit.getId());
					
					if(addressRegistroDao.exists((addressRegistroDao.getIdOrgs(unit.getId())).toString())){
						addressRegistroDao.update(addressRegistroVO);
					} else {
						addressRegistroDao.save(addressRegistroVO);
					}					
				}
				unitDao.commitTransaction();
				LOGGER.info("Fin de modificación de una unidad");
			}
		} catch (SQLException sqlException) {
			LOGGER.error(ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);
			LOGGER.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " + unit.getId());
			throw new DIR3Exception(DIR3ErrorCode.UPDATE_UNIT_ERROR, ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);

		} catch (Exception sqlException) {
			LOGGER.error(ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);
			LOGGER.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " + unit.getId());
			throw new DIR3Exception(DIR3ErrorCode.UPDATE_UNIT_ERROR, ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);
			
		} finally {
			unitDao.endTransaction();
		}
	}

	private AddressRegistroVO mappingAddressRegistro(AddressVO address,
			List<ContactUOVO> contacts) {
		AddressRegistroVO result = null;
		if (address != null || (contacts != null && contacts.size() > 0)) {
			result = new AddressRegistroVO();
			
			String direccion = "";
			if(StringUtils.isNotEmpty(address.getStreetTypeName())){
				direccion += address.getStreetTypeName();
			}
			if(StringUtils.isNotEmpty(address.getStreetName())){
				if(StringUtils.isNotEmpty(direccion)){
					direccion += " ";
				}
				direccion += address.getStreetName();
			}
			if(StringUtils.isNotEmpty(address.getAddressNum())){
				if(StringUtils.isNotEmpty(direccion)){
					direccion += ", ";
				}
				direccion += address.getAddressNum();
			}
			
			result.setAddress(direccion);
			result.setZip(address.getPostalCode());
			result.setCountry(address.getProvinceName());
			result.setCity(address.getCityName());
			if (contacts != null) {
				for (ContactUOVO contact : contacts) {
					if (contact.getContactTypeId().equals("T")) {
						result.setTelephone(contact.getContactInfo());
					} else {
						if (contact.getContactTypeId().equals("E")) {
							result.setEmail(contact.getContactInfo());
						} else {
							if (contact.getContactTypeId().equals("F")) {
								result.setFax(contact.getContactInfo());
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Obtiene el valor del parámetro addressRegistroDao.
	 * 
	 * @return addressRegistroDao valor del campo a obtener.
	 */
	public AddressRegistroDao getAddressRegistroDao() {
		return addressRegistroDao;
	}

	/**
	 * Guarda el valor del parámetro addressRegistroDao.
	 * 
	 * @param addressRegistroDao
	 *            valor del campo a guardar.
	 */
	public void setAddressRegistroDao(AddressRegistroDao addressRegistroDao) {
		this.addressRegistroDao = addressRegistroDao;
	}

	/**
	 * Guarda el valor del parámetro unitDao.
	 * 
	 * @param unitDao
	 *            valor del campo a guardar.
	 */
	public void setUnitDao(UnitRegistroDao unitDao) {
		this.unitDao = unitDao;
	}

	/**
	 * Obtiene el valor del parámetro unitDao.
	 * 
	 * @return unitDao valor del campo a obtener.
	 */
	public UnitRegistroDao getUnitDao() {
		return unitDao;
	}

	/**
	 * Modificar o insertar los organismos de la lista.
	 * 
	 * @param unitsDCO
	 *            lista de organismos a modificar.
	 * @throws DIR3Exception .
	 */
	public void insertUpdateUnits(List<UnitVO> unitsDCO) throws DIR3Exception {
		LOGGER.info("Guardando una lista de unidades");
		ListIterator<UnitVO> itr = unitsDCO.listIterator();
		UnitVO unit = null;
		boolean exists = false;

		while (itr.hasNext()) {
			try {
				unit = itr.next();
				exists = exists(unit.getId());
				if (exists) {
					if (unit.getStatus().equals("E")) {
						delete(unit);
					} else {
						update(unit);
					}
				} else {
					if (!unit.getStatus().equals("E")) {
						save(unit);
					}
				}
			} catch (DIR3Exception dir3Exception) {
				LOGGER.error(ErrorConstants.INSERTUPDATE_UNIT_ERROR_MESSAGE + unit.getId(), dir3Exception);
			}
		}
	}

	/**
	 * Modifica todos los elementos de las unidades que entran como parámetros.
	 * 
	 * @param unitsDCO
	 *            lista de unidades a modificar.
	 * @param contactsUO
	 *            lista de contactos a modificar.
	 * 
	 * @throws DIR3Exception .
	 */
	public void updateUnits(UnitsVO unitsDCO, ContactsUOVO contactsUO) throws DIR3Exception {
		LOGGER.info("Guardando unidades, contactos.");
		if (unitsDCO != null && unitsDCO.getUnits() != null) {
			List<UnitVO> listUO = UnitsHelper.completeUnits(unitsDCO, contactsUO, true);
			
			String entidadOriginal = MultiEntityContextHolder.getEntity();
			String entidadTrabajo = "";
			
			try {
				ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
				@SuppressWarnings("unchecked")
				List<Entidad> listaEntidades = (List<Entidad>)servicioEntidades.obtenerEntidades();
				
				LOGGER.warn("Procesando " + listaEntidades.size() + " entidades.");
				int contador = 0;
				for(Entidad entidad : listaEntidades) {
				
					if( 0 == contador%10){
						LOGGER.warn("Llevamos " + contador + " entidades procesadas. Vamos por la " + entidad.getIdentificador());
					}
					contador++;
					
					entidadTrabajo = entidad.getIdentificador();
					MultiEntityContextHolder.setEntity(entidadTrabajo);				
				
					insertUpdateUnits(listUO);
				}
			
			} catch (SigemException e) {
				LOGGER.error("ERROR al actualizar las unidades en la entidad: " + entidadTrabajo + ". " + e.getMessage(), e);
				
			} finally {
				MultiEntityContextHolder.setEntity(entidadOriginal);
			}
		}
	}

	/**
	 * Modifica todos los elementos de las unidades que entran como parámetros.
	 * 
	 * @param unitsDCO
	 *            lista de unidades a modificar.
	 * @param contactsUO
	 *            lista de contactos a modificar.
	 * 
	 * @throws DIR3Exception .
	 */
	public void updateUNOUnits(UnitsVO unitsDCO, ContactsUOVO contactsUO) throws DIR3Exception {
		LOGGER.info("Guardando unidades, contactos.");
		if (unitsDCO != null && unitsDCO.getUnits() != null) {
			List<UnitVO> listUO = UnitsHelper.completeUnits(unitsDCO, contactsUO, false);
			
			String entidadOriginal = MultiEntityContextHolder.getEntity();
			String entidadTrabajo = "";
			
			try {
				ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
				@SuppressWarnings("unchecked")
				List<Entidad> listaEntidades = (List<Entidad>)servicioEntidades.obtenerEntidades();
				
				int contador = 0;
				for(Entidad entidad : listaEntidades) {
				
					if( 0 == contador%10){
						LOGGER.warn("Llevamos " + contador + " entidades procesadas. Vamos por la " + entidad.getIdentificador());
					}
					contador++;

					entidadTrabajo = entidad.getIdentificador();
					MultiEntityContextHolder.setEntity(entidadTrabajo);				
				
					insertUpdateUnits(listUO);
				}
			
			} catch (SigemException e) {
				LOGGER.error("ERROR al actualizar las unidades no organizativas en la entidad: " + entidadTrabajo + ". " + e.getMessage(), e);
				
			} finally {
				MultiEntityContextHolder.setEntity(entidadOriginal);
			}
		}

	}

	public AddressDao getAddressDao() {
		return addressDao;
	}

	public void setAddressDao(AddressDao addressDao) {
		this.addressDao = addressDao;
	}

	public UnidadAdministrativaDAO getUnidadAdministrativaDAO() {
		return unidadAdministrativaDAO;
	}

	public void setUnidadAdministrativaDAO(UnidadAdministrativaDAO unidadAdministrativaDAO) {
		this.unidadAdministrativaDAO = unidadAdministrativaDAO;
	}

}
