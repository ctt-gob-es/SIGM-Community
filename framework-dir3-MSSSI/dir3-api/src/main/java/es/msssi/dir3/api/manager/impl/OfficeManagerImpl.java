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
import es.msssi.dir3.api.dao.ContactOFIDao;
import es.msssi.dir3.api.dao.HistoryDao;
import es.msssi.dir3.api.dao.OfficeDao;
import es.msssi.dir3.api.dao.ServiceDao;
import es.msssi.dir3.api.helper.OfficesHelper;
import es.msssi.dir3.api.manager.OfficeManager;
import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.vo.AddressVO;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.ContactOFIVO;
import es.msssi.dir3.api.vo.ContactsOFIVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.HistoriesVO;
import es.msssi.dir3.api.vo.HistoryVO;
import es.msssi.dir3.api.vo.OfficeVO;
import es.msssi.dir3.api.vo.OfficesVO;
import es.msssi.dir3.api.vo.ServiceVO;
import es.msssi.dir3.api.vo.ServicesVO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;

/**
 * Implementación del manager de datos básicos de oficinas.
 * 
 * @author cmorenog
 * 
 */
public class OfficeManagerImpl implements OfficeManager,Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(OfficeManagerImpl.class);
    private OfficeDao officeDao;
    private AddressDao addressDao;
    private ContactOFIDao contactDao;
    private ServiceDao serviceDao;
    private HistoryDao historyOFDao;

    /**
     * Constructor.
     */
    public OfficeManagerImpl() {
    }

    /**
     * Devuelve el número de oficinas existentes y que cumplan los criterios
     * establecidos.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return int el número de oficinas.
     * @throws DIR3Exception .
     */
    public int count(
	List<Criterion<OfficeCriterionEnum>> criteria)
	throws DIR3Exception {

	logger.info("Obteniendo el número de oficinas. Criterios: {}");
	int result = 0;
	// Obtiene el número de oficinas en base a los criterios
	try {
	    result = officeDao.count(criteria);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_COUNT_OFFICE_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_COUNT_OFFICE_ERROR,
		ErrorConstants.GET_COUNT_OFFICE_ERROR_MESSAGE, sqlException);
	}
	logger.info("Número de oficinas: " +
	    result);
	return result;
    }

    /**
     * Devuelve el número de oficinas existentes. Para obtener el resultado
     * delega en el dao asociado.
     * 
     * @return int el número de oficinas.
     * @throws DIR3Exception .
     */
    public int count()
	throws DIR3Exception {

	logger.info("Obteniendo el número total de oficinas.");
	int result = 0;
	// Obtiene el número de oficinas
	try {
	    result = officeDao.count(null);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_COUNT_OFFICE_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_COUNT_OFFICE_ERROR,
		ErrorConstants.GET_COUNT_OFFICE_ERROR_MESSAGE, sqlException);
	}
	logger.info("Número total de oficinas: " +
	    result);
	return result;
    }

    /**
     * Método genérico para recuperar una oficina basándonos en su
     * identificador.
     * 
     * @param codeOffice
     *            identificador de la oficina a recuperar.
     * @return el objeto recuperado.
     * @throws DIR3Exception .
     */
    public OfficeVO get(
	String codeOffice)
	throws DIR3Exception {
	OfficeVO result = null;
	try {
	    logger.info("Recuperando una oficina con id: " +
		codeOffice);
	    // Buscamos datos sobre la oficina
	    result = (OfficeVO) officeDao.get(codeOffice);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_OFFICE_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_OFFICE_ERROR, ErrorConstants.GET_OFFICE_ERROR_MESSAGE,
		sqlException);
	}
	return result;
    }

    /**
     * Método genérico para recuperar todas las oficinas. Viene a ser lo mismo
     * que recuperar todas las filas de una tabla de base de datos.
     * 
     * @return la lista de oficinas recuperados.
     * @throws DIR3Exception .
     */
    public List<OfficeVO> getAll()
	throws DIR3Exception {
	logger.info("Realizando búsqueda de todas las oficinas.");
	List<OfficeVO> result = null;
	// Realiza la búsqueda de oficinas en base a los criterios
	try {
	    result = officeDao.find(null);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.GET_ALL_OFFICES_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_ALL_OFFICES_ERROR, ErrorConstants.GET_ALL_OFFICES_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de oficinas encontradas: " +
	    result != null
	    ? result.size() : "null");
	return result;
    }

    /**
     * Método genérico para recuperar todas las oficinas y que cumplan con los
     * criterios establecidos. Si criterios es nulo viene a ser lo mismo que
     * recuperar todas las filas de una tabla de base de datos.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return la lista de objetos recuperados.
     * @throws DIR3Exception .
     */
    public List<OfficeVO> find(
	Criteria<OfficeCriterionEnum> criteria)
	throws DIR3Exception {

	logger.info("Realizando búsqueda de oficinas. Criterios: {}");
	List<OfficeVO> result = null;
	// Realiza la búsqueda de oficinas en base a los criterios
	try {
	    result = officeDao.find(criteria);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.FIND_OFFICES_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.FIND_OFFICES_ERROR, ErrorConstants.FIND_OFFICES_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de oficinas encontradas: " +
	    result != null
	    ? result.size() : "null");
	return result;
    }

    /**
     * Método genérico para recuperar todas las entidades de un mismo tipo y que
     * cumplan con los criterios establecidos. Si criterios es nulo viene a ser
     * lo mismo que recuperar todas las filas de una tabla de base de datos.
     * 
     * @param criteria
     *            criterios de búsqueda.
     * @return la lista de objetos recuperados.
     * @throws DIR3Exception .
     */
    public List<BasicDataOfficeVO> findBasicData(
	Criteria<OfficeCriterionEnum> criteria)
	throws DIR3Exception {

	logger.info("Realizando búsqueda de datos básicos de oficinas. Criterios: {}");
	List<BasicDataOfficeVO> result = null;
	// Realiza la búsqueda de oficinas en base a los criterios
	try {
	    result = officeDao.findBasicData(criteria);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.FIND_OFFICES_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.FIND_OFFICES_ERROR, ErrorConstants.FIND_OFFICES_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("Número de oficinas encontradas: " +
	    result != null
	    ? result.size() : "null");
	return result;
    }

    /**
     * Comprueba la existencia de una oficina con el identificador dado.
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
	    logger.info("Comprobando la existencia de una oficina con id:" +
		id);
	    // Buscamos datos sobre la oficina
	    result = officeDao.exists(id);
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.EXISTS_OFFICE_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.EXISTS_OFFICE_ERROR, ErrorConstants.EXISTS_OFFICE_ERROR_MESSAGE,
		sqlException);
	}
	logger.info("La oficina existe?: " +
	    result);
	return result;
    }

    /**
     * Inserta una oficina.
     * 
     * @param office
     *            oficina a insertar.
     * @throws DIR3Exception .
     * 
     */
    public void save(
	OfficeVO office)
	throws DIR3Exception {
	logger.info("Insertando una oficina");
	try {
	    Integer addressId = null;
	    OfficeVO tempOffice = null;
	    if (office != null) {
		logger.info("Insertando la oficina: " +
		    office.getId());
		tempOffice = office;
		addressDao.setSqlMapClient(officeDao.getSqlMapClient());
		contactDao.setSqlMapClient(officeDao.getSqlMapClient());
		serviceDao.setSqlMapClient(officeDao.getSqlMapClient());
		officeDao.startTransaction();
		if (office.getAddress() != null &&
		    office.getAddress().getStreetName() != null) {
		    logger.debug("Insertando la dirección nueva");
		    addressId = addressDao.saveReturn(office.getAddress());
		    logger.debug("Nueva dirección es: " +
			addressId);
		    AddressVO address = office.getAddress();
		    address.setId(Integer.toString(addressId));
		    tempOffice.setAddress(address);
		}
		logger.debug("Insertando la oficina");
		officeDao.save(tempOffice);

		if (office.getServices() != null &&
		    office.getServices().size() > 0) {
		    logger.debug("Insertando los servicios");
		    for (ServiceVO service : office.getServices()) {
			serviceDao.save(service);
		    }
		}
		if (office.getContacts() != null &&
		    office.getContacts().size() > 0) {
		    logger.debug("Insertando los contactos");
		    for (ContactOFIVO contact : office.getContacts()) {
			contactDao.save(contact);
		    }
		}
		officeDao.commitTransaction();
		logger.info("Fin de Insertando una oficina");
	    }
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.SAVE_OFFICE_ERROR_MESSAGE, sqlException);
	    logger.error("CÓDIGO DE OFICINA FALLIDA: " +
		office.getId());
	    throw new DIR3Exception(
		DIR3ErrorCode.SAVE_OFFICE_ERROR, ErrorConstants.SAVE_OFFICE_ERROR_MESSAGE,
		sqlException);
	}
	finally {
	    officeDao.endTransaction();
	}
    }

    /**
     * Modificar la oficina.
     * 
     * @param oficina
     *            oficina a modificar.
     * @throws DIR3Exception .
     */
    public void update(
	OfficeVO office)
	throws DIR3Exception {
	logger.info("Guardando una oficina.");
	Integer addressId = null;
	OfficeVO tempOffice = null;
	try {
	    if (office != null) {
		logger.info("Insertando la oficina: " +
		    office.getId());
		tempOffice = office;
		addressDao.setSqlMapClient(officeDao.getSqlMapClient());
		contactDao.setSqlMapClient(officeDao.getSqlMapClient());
		serviceDao.setSqlMapClient(officeDao.getSqlMapClient());
		officeDao.startTransaction();

		addressId = addressDao.getDirectionOfi(office.getId());
		// si no tenia direccion
		if (addressId == null) {
		    if (office.getAddress() != null) {
			logger.debug("insertando nueva dirección");
			addressId = addressDao.saveReturn(office.getAddress());
			logger.debug("Nueva dirección: " +
			    addressId);
			AddressVO address = office.getAddress();
			address.setId(Integer.toString(addressId));
			tempOffice.setAddress(address);
		    }
		    logger.debug("modificamos oficina");
		    officeDao.update(tempOffice);
		}
		else {
		    // SI TENIA DIRECCION
		    if (office.getAddress() != null) {

			AddressVO address = office.getAddress();
			address.setId(Integer.toString(addressId));
			tempOffice.setAddress(address);
			logger.debug("modificamos dirección: " +
			    addressId);
			addressDao.update(tempOffice.getAddress());
			logger.debug("modificamos oficina");
			officeDao.update(tempOffice);
		    }
		    else {
			logger.debug("modificamos oficina");
			officeDao.update(tempOffice);
			logger.debug("borramos dirección: " +
			    addressId);
			addressDao.delete(addressId);
		    }
		}
		// CONTACTOS
		// lista de contactos actual
		List<ContactOFIVO> contactsBBDD = contactDao.getContacts(office.getId());
		if (office.getContacts() != null &&
		    office.getContacts().size() > 0) {

		    // TRAE CONTACTOS DIR3
		    if (contactsBBDD != null &&
			contactsBBDD.size() > 0) {
			// TIENE CONTACTOS VIGENTES

			// vamos a comprobar si estaba el contacto actualmente y
			// no viene. en ese caso se modifican poniendo estado E
			boolean exists = false;
			ContactOFIVO contactxml = null;
			ContactOFIVO contactbbdd = null;
			Iterator<ContactOFIVO> iterator = null;
			for (ContactOFIVO contactBBDD : contactsBBDD) {
			    iterator = office.getContacts().iterator();
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
			for (ContactOFIVO contact : office.getContacts()) {
			    iterator = contactsBBDD.iterator();
			    exists = false;
			    while (!exists &&
				iterator.hasNext()) {
				contactbbdd = iterator.next();
				exists = compareContact(
				    contactbbdd, contactxml);
			    }

			    if (!exists) {
				contact.setStatusId(office.getStatus());
				contactDao.save(contact);
			    }
			    else {
				if (!contactbbdd.getStatusId().equals(
				    office.getStatus())) {
				    contactbbdd.setStatusId(office.getStatus());
				    contactDao.update(contactbbdd);
				}
			    }
			}
		    }
		    else {
			// NO TIENE CONTACTOS VIGENTES
			for (ContactOFIVO contact : office.getContacts()) {
			    // INSERTO LOS CONTACTOS
			    contact.setStatusId(office.getStatus());
			    contactDao.save(contact);
			}
		    }
		}
		else {
		    // NO TRAE CONTACTOS
		    if (contactsBBDD != null &&
			contactsBBDD.size() > 0) {
			// SI EXISTEN CONTACTOS VIGENTES
			if (office.getStatus().equals(
			    "E") ||
			    office.getStatus().equals(
				"T") || office.getStatus().equals(
				"A")) {
			    // SI LA UNIDAD TIENE ESTADO E,A O T, los contactos
			    // vigentes se modifican a estado E
			    for (ContactOFIVO contactBBDD : contactsBBDD) {
				contactBBDD.setStatusId("E");
				contactDao.update(contactBBDD);
			    }
			}
		    }
		}
		// SERVICIOS
		logger.debug("borramos servicios");
		serviceDao.deleteServices(office.getId());
		if (office.getServices() != null &&
		    office.getServices().size() > 0) {
		    logger.debug("insertamos servicios");
		    for (ServiceVO service : office.getServices()) {
			serviceDao.save(service);
		    }
		}
		officeDao.commitTransaction();
		logger.info("Fin de la modificación una oficina");
	    }
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.UPDATE_OFFICE_ERROR_MESSAGE, sqlException);
	    logger.error("CÓDIGO DE OFICINA FALLIDA: " +
		office.getId());
	    throw new DIR3Exception(
		DIR3ErrorCode.UPDATE_OFFICE_ERROR_MESSAGE,
		ErrorConstants.UPDATE_OFFICE_ERROR_MESSAGE, sqlException);
	}
	finally {
	    officeDao.endTransaction();
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
	ContactOFIVO contactbbdd, ContactOFIVO contactxml) {
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
     * Modificar o insertar las oficinas de la lista.
     * 
     * @param oficinasDCO
     *            lista de oficinas a modificar.
     * @throws DIR3Exception .
     */
    public void insertUpdateOffices(
	List<OfficeVO> officesDCO)
	throws DIR3Exception {
	logger.info("Guardando una lista de oficinas");
	ListIterator<OfficeVO> itr = officesDCO.listIterator();
	OfficeVO office = null;
	boolean exists = false;
	while (itr.hasNext()) {
	    try {
		office = itr.next();
		exists = exists(office.getId());
		if (exists) {
		    update(office);
		}
		else {
		    save(office);
		}
	    }
	    catch (DIR3Exception dir3Exception) {
		logger.error(
		    ErrorConstants.INSERTUPDATE_OFFICE_ERROR_MESSAGE +
			office.getId(), dir3Exception);
	    }
	}
    }

    /**
     * Obtiene el valor del parámetro officeDao.
     * 
     * @return officeDao valor del campo a obtener.
     */
    public OfficeDao getOfficeDao() {
	return officeDao;
    }

    /**
     * Guarda el valor del parámetro officeDao.
     * 
     * @param officeDao
     *            valor del campo a guardar.
     */
    public void setOfficeDao(
	OfficeDao officeDao) {
	this.officeDao = officeDao;
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
     * Guarda el valor del parámetro addressDao.
     * 
     * @param addressDao
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
    public ContactOFIDao getContactDao() {
	return contactDao;
    }

    /**
     * Guarda el valor del parámetro datosContactoDao.
     * 
     * @param datosContactoDao
     *            valor del campo a guardar.
     */
    public void setContactDao(
	ContactOFIDao contactDao) {
	this.contactDao = contactDao;
    }

    /**
     * Obtiene el valor del parámetro serviceDao.
     * 
     * @return serviceDao valor del campo a obtener.
     */
    public ServiceDao getServiceDao() {
	return serviceDao;
    }

    /**
     * Guarda el valor del parámetro serviceDao.
     * 
     * @param serviceDao
     *            valor del campo a guardar.
     */
    public void setServiceDao(
	ServiceDao serviceDao) {
	this.serviceDao = serviceDao;
    }

    /**
     * Obtiene el valor del parámetro historyOFDao.
     * 
     * @return historyOFDao valor del campo a obtener.
     */
    public HistoryDao getHistoryOFDao() {
	return historyOFDao;
    }

    /**
     * Guarda el valor del parámetro historyOFDao.
     * 
     * @param historyOFDao
     *            valor del campo a guardar.
     */
    public void setHistoryOFDao(
	HistoryDao historyOFDao) {
	this.historyOFDao = historyOFDao;
    }

    /**
     * Modifica todos los elementos de las oficinas que entran como parámetros.
     * 
     * @param officesDCO
     *            lista de oficinas a modificar.
     * @param contactsDCO
     *            lista de contactos a modificar.
     * @param servicesDCO
     *            lista de servicios a modificar.
     * @param historiesDCO
     *            lista de históricos a insertar.
     * @throws DIR3Exception .
     */
    public void updateOffices(
	OfficesVO officesDCO, ContactsOFIVO contactsDCO, ServicesVO servicesDCO,
	HistoriesVO historiesDCO)
	throws DIR3Exception {
	logger.info("Guardando oficinas, contactos, servicios.");
	if (officesDCO != null &&
	    officesDCO.getOffices() != null) {
	    List<OfficeVO> listOF = OfficesHelper.completeOffices(
		officesDCO, contactsDCO, servicesDCO);
	    insertUpdateOffices(listOF);
	}
	logger.info("Guardando históricos.");
	/*if (historiesDCO != null &&
	    historiesDCO.getHistories() != null) {
	    saveHistoriesOffice(historiesDCO);
	}*/
    }

    /**
     * Guarda el histórico de la oficina proveniente del dco.
     * 
     * @param histories
     *            histórico de la oficina.
     * @throws DIR3Exception .
     */
    public void saveHistoriesOffice(
	HistoriesVO histories)
	throws DIR3Exception {
	try {
	    if (histories != null &&
		histories.getHistories() != null) {

		historyOFDao.startTransaction();
		OfficeVO previousUnit = null;
		OfficeVO lastUnit = null;
		String causeId = null;
		Map<String, String> hist = null;
		for (HistoryVO history : histories.getHistories()) {
		    try {
			hist = new HashMap<String, String>();
			hist.put(
			    "lastId", history.getLastId());
			hist.put(
			    "previousId", history.getPreviousId());
			boolean exist = historyOFDao.exists(hist);
			if (!exist) {
			    previousUnit = officeDao.get(history.getPreviousId());
			    history.setPreviousName(previousUnit.getName());
			    history.setPreviousStatus(previousUnit.getStatus());
			    lastUnit = officeDao.get(history.getLastId());
			    history.setLastName(lastUnit.getName());
			    history.setLastStatus(lastUnit.getStatus());
			    history.setStatus("V");
			    if (previousUnit.getDeleteObservations() != null) {
				causeId =
				    historyOFDao.getCauseId(previousUnit.getDeleteObservations());
				if (causeId == null) {
				    causeId = "ZZZ";
				    history.setObservations(previousUnit.getDeleteObservations());
				}
				history.setCauseId(causeId);
			    }
			    historyOFDao.save(history);
			}
		    }
		    catch (SQLException exception) {
			logger.error(
			    ErrorConstants.INSERT_HISTORIES_OFFICE_ERROR_MESSAGE +
				"LastId:" + history.getLastId() + " PreviousId:" +
				history.getPreviousId() + "; ", exception);
		    }
		}
		historyOFDao.commitTransaction();
	    }
	}
	catch (SQLException sqlException) {
	    logger.error(
		ErrorConstants.INSERT_HISTORIES_OFFICE_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.INSERT_HISTORIES_OFFICE_ERROR,
		ErrorConstants.INSERT_HISTORIES_UNIT_ERROR_MESSAGE, sqlException);
	}
	finally {
	    historyOFDao.endTransaction();
	}
    }
}
