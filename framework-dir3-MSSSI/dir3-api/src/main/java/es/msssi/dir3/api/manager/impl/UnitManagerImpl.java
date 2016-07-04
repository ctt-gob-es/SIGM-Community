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

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.AddressDao;
import es.msssi.dir3.api.dao.ContactUODao;
import es.msssi.dir3.api.dao.HistoryDao;
import es.msssi.dir3.api.dao.UnitDao;
import es.msssi.dir3.api.helper.UnitsHelper;
import es.msssi.dir3.api.manager.UnitManager;
import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.vo.AddressVO;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.ContactUOVO;
import es.msssi.dir3.api.vo.ContactsUOVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.HistoriesVO;
import es.msssi.dir3.api.vo.HistoryVO;
import es.msssi.dir3.api.vo.UnitVO;
import es.msssi.dir3.api.vo.UnitsVO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;

/**
 * Implementación del manager de datos básicos de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class UnitManagerImpl implements UnitManager, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7016415655435529287L;
    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(UnitManagerImpl.class);
    private UnitDao unitDao;
    private AddressDao addressDao;
    private ContactUODao contactDao;
    private HistoryDao historyUODao;

    /**
     * Constructor.
     * 
     */
    public UnitManagerImpl() {

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
    public int count(
	List<Criterion<UOCriterionEnum>> criteriaAPI)
	throws DIR3Exception {

	logger.info("Obteniendo el número de unidades orgánicas. Criterios: {}");
	int result = 0;
	// Obtiene el número de unidades orgánicas en base a los criterios
	try {
	    result = unitDao.count(criteriaAPI);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_COUNT_UNIT_ERROR, ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de unidades orgánicas: " +
	    result);
	return result;
    }

    /**
     * Devuelve el número de unidades orgánicas existentes. Para obtener el
     * resultado delega en el dao asociado.
     * 
     * @return int el número de unidades orgánicas.
     * @throws DIR3Exception .
     */
    public int count()
	throws DIR3Exception {
	logger.info("Obteniendo el número de unidades orgánicas.");
	int result = 0;
	// Obtiene el número de unidades orgánicas
	try {
	    result = unitDao.count(null);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_COUNT_UNIT_ERROR, ErrorConstants.GET_COUNT_UNIT_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número total de unidades orgánicas: " +
	    result);
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
    public UnitVO get(
	String codeUnit)
	throws DIR3Exception {
	UnitVO result = null;
	try {
	    logger.info("Recuperando una unidad orgánica con id: " +
		codeUnit);
	    // Buscamos datos basicos de la unidad organica
	    result = (UnitVO) unitDao.get(codeUnit);

	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_UNIT_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_UNIT_ERROR, ErrorConstants.GET_UNIT_ERROR_MESSAGE, sqlException);
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
    public List<UnitVO> getAll()
	throws DIR3Exception {
	logger.info("Realizando búsqueda de todas las unidades orgánicas.");
	List<UnitVO> result = null;
	// Realiza la búsqueda de unidades orgánicas
	try {
	    result = unitDao.find(null);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_ALL_UNITS_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_ALL_UNITS_ERROR, ErrorConstants.GET_ALL_UNITS_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de unidades orgánicas encontradas: " +
	    result != null
	    ? result.size() : "null");
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
    public List<UnitVO> find(
	Criteria<UOCriterionEnum> criteria)
	throws DIR3Exception {

	logger.info("Realizando búsqueda de unidades orgánicas. Criterios: {}");
	List<UnitVO> result = null;
	// Realiza la búsqueda de unidades orgánicas en base a los criterios
	try {
	    result = unitDao.find(criteria);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.FIND_UNITS_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.FIND_UNITS_ERROR, ErrorConstants.FIND_UNITS_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de unidades orgánicas encontradas: " +
	    result != null
	    ? result.size() : "null");
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
    public List<BasicDataUnitVO> findBasicData(
	Criteria<UOCriterionEnum> criteria)
	throws DIR3Exception {
	logger.info("Realizando búsqueda de datos básicos de unidades orgánicas. Criterios: {}");
	List<BasicDataUnitVO> result = null;
	// Realiza la búsqueda de unidades orgánicas en base a los criterios
	try {
	    result = unitDao.findBasicData(criteria);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.FIND_UNITS_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.FIND_UNITS_ERROR, ErrorConstants.FIND_UNITS_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de unidades orgánicas encontradas: " +
	    result != null
	    ? result.size() : "null");
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
    public boolean exists(
	String id)
	throws DIR3Exception {
	boolean result = false;
	try {
	    logger.info("Comprobando la existencia de una unidad orgánica con id: " +
		id);
	    // Buscamos datos sobre la unidad orgánica
	    result = unitDao.exists(id);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.EXISTS_UNIT_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.EXISTS_UNIT_ERROR, ErrorConstants.EXISTS_UNIT_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("La unidad orgánica existe?: " +
	    result);
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
    public void save(
	UnitVO unit)
	throws DIR3Exception {
	Integer addressId = null;
	UnitVO tempUnit = null;
	logger.info("Insertando una unidad");
	try {
	    if (unit != null) {
		logger.info("Insertando la unidad: " +
		    unit.getId());
		tempUnit = unit;
		addressDao.setSqlMapClient(unitDao.getSqlMapClient());
		contactDao.setSqlMapClient(unitDao.getSqlMapClient());
		unitDao.startTransaction();
		if (unit.getAddress() != null &&
		    unit.getAddress().getStreetName() != null) {
		    logger.debug("Insertando la dirección nueva");
		    addressId = addressDao.saveReturn(unit.getAddress());
		    logger.debug("Nueva dirección es: " +
			addressId);
		    AddressVO address = unit.getAddress();
		    address.setId(Integer.toString(addressId));
		    tempUnit.setAddress(address);
		}
		logger.debug("Insertando la unidad");
		unitDao.save(tempUnit);

		if (unit.getContacts() != null &&
		    unit.getContacts().size() > 0) {
		    logger.debug("Insertando los contactos");
		    for (ContactUOVO contact : unit.getContacts()) {
			contactDao.save(contact);
		    }
		}
		unitDao.commitTransaction();
		logger.info("Fin de Insertando una unidad");
	    }
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.INSERT_UNIT_ERROR_MESSAGE, sqlException);
	    logger.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " +
		unit.getId());
	    throw new DIR3Exception(
		DIR3ErrorCode.INSERT_UNIT_ERROR, ErrorConstants.INSERT_UNIT_ERROR_MESSAGE,
		sqlException);
	}
	finally {
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
    public void update(
	UnitVO unit)
	throws DIR3Exception {
	Integer addressId = null;
	UnitVO tempUnit = null;
	try {
	    if (unit != null) {
		logger.info("Modificando la unidad: " +
		    unit.getId());
		tempUnit = unit;
		addressDao.setSqlMapClient(unitDao.getSqlMapClient());
		contactDao.setSqlMapClient(unitDao.getSqlMapClient());
		unitDao.startTransaction();
		logger.debug("buscando la dirección de la unidad");
		addressId = addressDao.getDirectionOrg(unit.getId());

		// si no tenia direccion
		if (addressId == null) {
		    logger.debug("La unidad no tiene dirección actualmente");
		    if (unit.getAddress() != null) {
			logger.debug("Insertamos la nueva dirección");
			addressId = addressDao.saveReturn(unit.getAddress());
			logger.debug("Nueva dirección con id: " +
			    addressId);
			AddressVO address = unit.getAddress();
			address.setId(Integer.toString(addressId));
			tempUnit.setAddress(address);
		    }
		    logger.debug("Modificamos unidad");
		    unitDao.update(tempUnit);
		}
		else {
		    logger.debug("La unidad si tiene dirección actualmente con id: " +
			addressId);
		    // SI TENIA DIRECCION
		    if (unit.getAddress() != null) {
			logger.debug("El dco trae una dirección.");
			AddressVO address = unit.getAddress();
			address.setId(Integer.toString(addressId));
			tempUnit.setAddress(address);
			logger.debug("Se modifica la dirección " +
			    addressId);
			addressDao.update(tempUnit.getAddress());
			logger.debug("Modificamos unidad");
			unitDao.update(tempUnit);
		    }
		    else {
			logger.debug("El dco no trae una dirección.");
			logger.debug("Modificamos unidad");
			unitDao.update(tempUnit);
			logger.debug("borramos dirección " +
			    addressId);
			addressDao.delete(addressId);
		    }
		}
		// CONTACTOS
		// lista de contactos actual
		List<ContactUOVO> contactsBBDD = contactDao.getContacts(unit.getId());

		if (unit.getContacts() != null &&
		    unit.getContacts().size() > 0) {

		    // TRAE CONTACTOS DIR3
		    if (contactsBBDD != null &&
			contactsBBDD.size() > 0) {
			// TIENE CONTACTOS VIGENTES
			logger.debug("tiene contactos actualmente");
			// vamos a comprobar si estaba el contacto actualmente y
			// no viene. en ese caso se modifican poniendo estado E
			boolean exists = false;
			ContactUOVO contactxml = null;
			ContactUOVO contactbbdd = null;
			Iterator<ContactUOVO> iterator = null;
			logger.debug("comprobamos si los contactos del dco existen actualmente");
			for (ContactUOVO contactBBDD : contactsBBDD) {
			    iterator = unit.getContacts().iterator();
			    exists = false;
			    while (!exists &&
				iterator.hasNext()) {
				contactxml = iterator.next();
				exists = compareContact(
				    contactBBDD, contactxml);
			    }
			    if (!exists) {
				contactBBDD.setStatusId("E");
				contactDao.update(contactBBDD);
			    }
			}

			// comprobamos los contactos que vienen. Si no están en
			// bbdd se insertan;
			// si está y tiene el mismo estado que la unidad se
			// deja; si está y tiene estado distinto se modifica.
			for (ContactUOVO contact : unit.getContacts()) {
			    iterator = contactsBBDD.iterator();
			    exists = false;
			    while (!exists &&
				iterator.hasNext()) {
				contactbbdd = iterator.next();
				exists = compareContact(
				    contactbbdd, contactxml);
			    }

			    if (!exists) {
				contact.setStatusId(unit.getStatus());
				contactDao.save(contact);
			    }
			    else {
				if (!contactbbdd.getStatusId().equals(
				    unit.getStatus())) {
				    contactbbdd.setStatusId(unit.getStatus());
				    contactDao.update(contactbbdd);
				}
			    }
			}
		    }
		    else {
			// NO TIENE CONTACTOS VIGENTES
			for (ContactUOVO contact : unit.getContacts()) {
			    // INSERTO LOS CONTACTOS
			    contact.setStatusId(unit.getStatus());
			    contactDao.save(contact);
			}
		    }
		}
		else {
		    // NO TRAE CONTACTOS
		    if (contactsBBDD != null &&
			contactsBBDD.size() > 0) {
			// SI EXISTEN CONTACTOS VIGENTES
			if (unit.getStatus().equals(
			    "E") ||
			    unit.getStatus().equals(
				"T") || unit.getStatus().equals(
				"A")) {
			    // SI LA UNIDAD TIENE ESTADO E,A O T, los contactos
			    // vigentes se modifican a estado E
			    for (ContactUOVO contactBBDD : contactsBBDD) {
				contactBBDD.setStatusId("E");
				contactDao.update(contactBBDD);
			    }
			}
		    }
		}
		unitDao.commitTransaction();
		logger.info("Fin de modificación de una unidad");
	    }
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE, sqlException);
	    logger.error("CÓDIGO DE UNIDAD ORGÁNICA FALLIDA: " +
		unit.getId());
	    throw new DIR3Exception(
		DIR3ErrorCode.UPDATE_UNIT_ERROR, ErrorConstants.UPDATE_UNIT_ERROR_MESSAGE,
		sqlException);
	}
	finally {
	    unitDao.endTransaction();
	}
    }

    /**
     * Comprueba si los dos contactos son el mismo.
     * 
     * @param contactbbdd
     *            contacto de bbdd.
     * @param contactxml
     *            el contacto de dco.
     * @return true si es el mismo contacto.
     */
    private boolean compareContact(
	ContactUOVO contactbbdd, ContactUOVO contactxml) {
	boolean result = false;
	if (contactbbdd != null &&
	    contactxml != null) {
	    if ((contactbbdd.getContactInfo() != null && contactbbdd.getContactInfo().equals(
		contactxml.getContactInfo())) &&
		(contactbbdd.getContactTypeId() != null && contactbbdd.getContactTypeId().equals(
		    contactxml.getContactTypeId()))) {
		result = true;
	    }
	}
	return result;
    }

    /**
     * Obtiene el valor del parámetro unitDao.
     * 
     * @return unitDao valor del campo a obtener.
     */
    public UnitDao getUnitDao() {
	return unitDao;
    }

    /**
     * Guarda el valor del parámetro unitDao.
     * 
     * @param unitDao
     *            valor del campo a guardar.
     */
    public void setUnitDao(
	UnitDao unitDao) {
	this.unitDao = unitDao;
    }

    /**
     * Obtiene el valor del parámetro addressDao.
     * 
     * @return addressDao valor del campo a obtener.
     */
    public AddressDao getAddressDao() {
	return addressDao;
    }

    /**
     * Guarda el valor del parámetro datosDireccionDao.
     * 
     * @param datosDireccionDao
     *            valor del campo a guardar.
     */
    public void setAddressDao(
	AddressDao addressDao) {
	this.addressDao = addressDao;
    }

    /**
     * Obtiene el valor del parámetro contactDao.
     * 
     * @return contactDao valor del campo a obtener.
     */
    public ContactUODao getContactDao() {
	return contactDao;
    }

    /**
     * Guarda el valor del parámetro contactDao.
     * 
     * @param contactDao
     *            valor del campo a guardar.
     */
    public void setContactDao(
	ContactUODao contactDao) {
	this.contactDao = contactDao;
    }

    /**
     * Obtiene el valor del parámetro historyUODao.
     * 
     * @return historyUODao valor del campo a obtener.
     */
    public HistoryDao getHistoryUODao() {
	return historyUODao;
    }

    /**
     * Guarda el valor del parámetro historyUODao.
     * 
     * @param historyUODao
     *            valor del campo a guardar.
     */
    public void setHistoryUODao(
	HistoryDao historyUODao) {
	this.historyUODao = historyUODao;
    }

    /**
     * Modificar o insertar los organismos de la lista.
     * 
     * @param unitsDCO
     *            lista de organismos a modificar.
     * @throws DIR3Exception .
     */
    public void insertUpdateUnits(
	List<UnitVO> unitsDCO)
	throws DIR3Exception {
	logger.info("Guardando una lista de unidades");
	ListIterator<UnitVO> itr = unitsDCO.listIterator();
	UnitVO unit = null;
	boolean exists = false;

	while (itr.hasNext()) {
	    try {
		unit = itr.next();
		exists = exists(unit.getId());
		if (exists) {
		    update(unit);
		}
		else {
		    save(unit);
		}
	    }
	    catch (DIR3Exception dir3Exception) {
		logger.error(
		    ErrorConstants.INSERTUPDATE_UNIT_ERROR_MESSAGE +
			unit.getId(), dir3Exception);
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
     * @param historiesUO
     *            lista de históricos a insertar.
     * @throws DIR3Exception .
     */
    public void updateUnits(
	UnitsVO unitsDCO, ContactsUOVO contactsUO, HistoriesVO historiesUO)
	throws DIR3Exception {
	logger.info("Guardando unidades, contactos.");
	if (unitsDCO != null &&
	    unitsDCO.getUnits() != null) {
	    List<UnitVO> listUO = UnitsHelper.completeUnits(
		unitsDCO, contactsUO, true);
	    insertUpdateUnits(listUO);
	}
	logger.info("Guardando históricos.");
	/*if (historiesUO != null &&
	    historiesUO.getHistories() != null) {
	    saveHistoriesUnits(historiesUO);
	}*/
    }

    /**
     * Guarda el histórico de la unidad proveniente del dco.
     * 
     * @param historiesUO
     *            histórico de la unidad.
     * @throws DIR3Exception .
     */
    public void saveHistoriesUnits(
	HistoriesVO historiesUO)
	throws DIR3Exception {

	try {
	    if (historiesUO != null &&
		historiesUO.getHistories() != null) {

		historyUODao.startTransaction();
		UnitVO previousUnit = null;
		UnitVO lastUnit = null;
		String causeId = null;
		Map<String, String> hist = null;
		for (HistoryVO history : historiesUO.getHistories()) {
		    try {
		    hist = new HashMap<String, String>();
		    hist.put(
			"lastId", history.getLastId());
		    hist.put(
			"previousId", history.getPreviousId());
		    boolean exist = historyUODao.exists(hist);
		    if (!exist) {
			previousUnit = unitDao.get(history.getPreviousId());
			history.setPreviousName(previousUnit.getName());
			history.setPreviousStatus(previousUnit.getStatus());
			lastUnit = unitDao.get(history.getLastId());
			history.setLastName(lastUnit.getName());
			history.setLastStatus(lastUnit.getStatus());
			history.setStatus("V");
			if (previousUnit.getDeleteObservations() != null) {
			    causeId = historyUODao.getCauseId(previousUnit.getDeleteObservations());
			    if (causeId == null) {
				causeId = "ZZZ";
				history.setObservations(previousUnit.getDeleteObservations());
			    }
			    history.setCauseId(causeId);
			}
			historyUODao.save(history);
		    }
		    }
		    catch (SQLException exception) {
			logger.error(
			    ErrorConstants.INSERT_HISTORIES_UNIT_ERROR_MESSAGE +
				"LastId:" + history.getLastId() + " PreviousId:" +
				history.getPreviousId() + "; ", exception);
		    }
		}
		historyUODao.commitTransaction();
	    }
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.INSERT_HISTORIES_UNIT_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.INSERT_HISTORIES_UNIT_ERROR,
		ErrorConstants.INSERT_HISTORIES_UNIT_ERROR_MESSAGE, sqlException);
	}
	finally {
	    historyUODao.endTransaction();
	}
    }
    
    /**
     * Modifica todos los elementos de las unidades no organicas que entran como parámetros.
     * 
     * @param unitsDCO
     *            lista de unidades a modificar.
     * @param contactsUO
     *            lista de contactos a modificar.
     * @throws DIR3Exception .
     */
    public void updateUNOUnits(
	UnitsVO unitsDCO, ContactsUOVO contactsUO)
	throws DIR3Exception {
	logger.info("Guardando unidades no organicas, contactos.");
	if (unitsDCO != null &&
	    unitsDCO.getUnits() != null) {
	    List<UnitVO> listUO = UnitsHelper.completeUnits(
		unitsDCO, contactsUO, false);
	    insertUpdateUnits(listUO);
	}

    }
}
