/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.service.impl;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;
import java.util.zip.ZipException;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import es.msssi.dir3.api.helper.XmlDcoToObject;
import es.msssi.dir3.api.manager.OfficeManager;
import es.msssi.dir3.api.manager.RelationshipManager;
import es.msssi.dir3.api.manager.RelationshipRegistroManager;
import es.msssi.dir3.api.manager.UnitManager;
import es.msssi.dir3.api.manager.UnitRegistroManager;
import es.msssi.dir3.api.manager.UpdateStatusManager;
import es.msssi.dir3.api.utils.ZipUtils;
import es.msssi.dir3.api.vo.ContactsOFIVO;
import es.msssi.dir3.api.vo.ContactsUOVO;
import es.msssi.dir3.api.vo.HistoriesVO;
import es.msssi.dir3.api.vo.OfficesVO;
import es.msssi.dir3.api.vo.RelationshipsOFIUOVO;
import es.msssi.dir3.api.vo.ServicesVO;
import es.msssi.dir3.api.vo.UnitsVO;
import es.msssi.dir3.api.vo.UpdateStatusVO;
import es.msssi.dir3.core.UpdateServiceDCO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;
import es.msssi.dir3.exception.GetFileDownloadDCOException;
import es.msssi.dir3.services.DownloadServiceDCO;

/**
 * Implementacion por defecto del servicio de actualización del DCO.
 * 
 * @author cmorenog.
 * 
 */
public class UpdateServiceDCOImpl implements UpdateServiceDCO, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6853064167881781661L;
    private static final Logger logger = Logger.getLogger(UpdateServiceDCOImpl.class);
    private static final String OFFICES = "Oficinas.xml";
    private static final String CONTACTOFFICE = "ContactoOFI.xml";
    private static final String HISTORIESOFFICE = "HistoricosOFI.xml";
    private static final String SERVICESOFFICE = "ServiciosOFI.xml";
    private static final String ORGANIZATIONALRELATIONSHIPSOFFICE =
	"RelacionesOrganizativasOFI.xml";
    private static final String RELATIONSHIPSSIROFFICE = "RelacionesSIROFI.xml";
    private static final String UNITS = "Unidades.xml";
    private static final String CONTACTUO = "ContactoUO.xml";
    private static final String HISTORIESUO = "HistoricosUO.xml";
    private static final String UNITSUNO = "UnidadesNoOrganizativas.xml";
    private static final String CONTACTUNO = "ContactoUNO.xml";
    private static final String DEFAULT_CONFIG_FILE = "/beans/dir3-api-castor-applicationContext.xml";
    /**
     * Contexto Spring.
     */
    private static ApplicationContext context;
    /**
     * Servicio para obtener los ficheros de actualización del DCO.
     */
    private DownloadServiceDCO updateServiceDCO;
    /**
     * Manager para la gestión de oficinas.
     */
    private OfficeManager officeManager;
    /**
     * Manager para la gestión de unidades orgánicas.
     */
    private UnitManager unitManager;
    /**
     * Manager para la gestión de unidades orgánicas.
     */
    private UpdateStatusManager updateStatusManager;
    /**
     * Manager para la gestión de relaciones orgánicas.
     */
    private RelationshipManager organizationalRelationshipsManager;
    /**
     * Manager para la gestión de relacionesSIR.
     */
    private RelationshipManager sirRelationshipManager;
    /**
     * Directorio para dejar los ficheros.
     */
    private String tempFilesDir;
    /**
     * Manager para la gestión de relaciones Registro.
     */
    private RelationshipRegistroManager relationshipRegistroManager;
    /**
     * Manager para la gestión de relaciones Registro.
     */
    private UnitRegistroManager unitRegisterManager;
    /**
     * Método que actualiza el directorio común con los datos provenientes del
     * dir3.
     * 
     * @throws DIR3Exception .
     */
    public void updateDCO()
	throws DIR3Exception {
	logger.info("Comienza la actualización del sistema");
	
	XmlDcoToObject converter = (XmlDcoToObject) getInstance().getBean("XmlDcoToObject");
	
	// Obtenemos la fecha de la ultima actualizacion
	UpdateStatusVO updateStatus = null;
	try {
	    updateStatus = updateStatusManager.getLastSuccessUpdate();
	}
	catch (DIR3Exception dIR3Exception) {
	    logger.error(
		ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, dIR3Exception);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_LAST_UPDATE_UPDATESERVICE_ERROR,
		ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, dIR3Exception);
	}

	logger.info("Los datos de la última actualización son:" +
	    updateStatus);

	logger.info("RECOGIENDO UNIDADES");
	String nameFileInitUnits = null;
	File fileInitUnits = null;
	File directoryUnits = null;
	Map<String, String> filesUnzippedUnits = null;
	try {
	    nameFileInitUnits = getUpdateServiceDCO().getFileUpdateUnitsDCO(
		updateStatus.getUpdateDate());

	    try {
		    logger.info("Nombre del fichero despues de crearlo:" +
			    getTempFilesDir() +
			    "/" + nameFileInitUnits);
		    fileInitUnits = new File(
			getTempFilesDir() +
			    "/" + nameFileInitUnits);
		    logger.info("directorio :" +
			   getTempFilesDir() +
			    "/" + nameFileInitUnits.replace(".zip", ""));
		    directoryUnits = new File(
			getTempFilesDir() +
			    "/" + nameFileInitUnits.replace(
				".zip", ""));
		    directoryUnits.mkdirs();
		    logger.info("Nombre del fichero de actualización de unidades:" +
			nameFileInitUnits);
		filesUnzippedUnits = ZipUtils.getInstance().unzipFile(
		    fileInitUnits.getAbsolutePath(), directoryUnits.getAbsolutePath());
	    }
	    catch (ZipException zipException) {
		logger.error(
		    ErrorConstants.UNZIP_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), zipException);
		/*throw new DIR3Exception(
		    DIR3ErrorCode.UNZIP_ERROR_MESSAGE, ErrorConstants.UNZIP_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), zipException);*/
	    }
	    catch (Exception iOException) {
		logger.error(
		    ErrorConstants.IO_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), iOException);
		/*throw new DIR3Exception(
		    DIR3ErrorCode.IO_ERROR_MESSAGE, ErrorConstants.IO_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), iOException);*/
	    }
	}
	catch (GetFileDownloadDCOException e) {
	    logger.error(
		ErrorConstants.GET_FILE_UNIT_UPDATESERVICE_ERROR, e);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_FILE_UNIT_UPDATESERVICE_ERROR,
		ErrorConstants.GET_FILE_UNIT_UPDATESERVICE_ERROR, e);
	}

	// Transformamos los datos de los ficheros de oficinas a VOs

	if (filesUnzippedUnits!= null){
        	UnitsVO unitsDCO = converter.getUnitsFromXmlFile(filesUnzippedUnits.get(UNITS));
        	ContactsUOVO contactsUO = converter.getContactsUOFromXmlFile(
        	    filesUnzippedUnits.get(CONTACTUO));
        	HistoriesVO historiesUO = converter.getHistoriesFromXmlFile(
        	    filesUnzippedUnits.get(HISTORIESUO));
        	logger.debug("Actualizados los datos de oficinas y organismos");
        	getUnitManager().updateUnits(
        	    unitsDCO, contactsUO, historiesUO);
        	getUnitRegisterManager().updateUnits(
        		    unitsDCO, contactsUO);
        	unitsDCO = null;
        	contactsUO = null;
        	historiesUO = null;
	}
	logger.info("RECOGIENDO UNIDADES NO ORGANICAS");
	String nameFileInitNotOrgUnits = null;
	File fileInitNotOrgUnits = null;
	File directoryNotOrgUnits = null;
	Map<String, String> filesUnzippedNotOrgUnits = null;
	try {
	    nameFileInitNotOrgUnits = getUpdateServiceDCO().getFileUpdateNotOrgUnitsDCO(
		updateStatus.getUpdateDate());

	    try {
		    fileInitNotOrgUnits = new File(
				getTempFilesDir() +
				    "/" + nameFileInitNotOrgUnits);
			    directoryNotOrgUnits = new File(
				getTempFilesDir() +
				    "/" + nameFileInitNotOrgUnits.replace(
					".zip", ""));
			    directoryNotOrgUnits.mkdirs();
			    logger.info("Nombre del fichero de actualización de unidades no organicas:" +
				nameFileInitNotOrgUnits);
		filesUnzippedNotOrgUnits = ZipUtils.getInstance().unzipFile(
		    fileInitNotOrgUnits.getAbsolutePath(), directoryNotOrgUnits.getAbsolutePath());
	    }
	    catch (ZipException zipException) {
		logger.error(
		    ErrorConstants.UNZIP_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), zipException);
		/*throw new DIR3Exception(
		    DIR3ErrorCode.UNZIP_ERROR_MESSAGE, ErrorConstants.UNZIP_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), zipException);*/
	    }
	    catch (Exception iOException) {
		logger.error(
		    ErrorConstants.IO_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), iOException);
		/*throw new DIR3Exception(
		    DIR3ErrorCode.IO_ERROR_MESSAGE, ErrorConstants.IO_ERROR_MESSAGE +
			":" + fileInitUnits.getAbsolutePath(), iOException);*/
	    }
	}
	catch (GetFileDownloadDCOException e) {
	    logger.error(
		ErrorConstants.GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR, e);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR,
		ErrorConstants.GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR, e);
	}

	// Transformamos los datos de los ficheros de oficinas a VOs

	if (filesUnzippedNotOrgUnits != null){
        	UnitsVO NotOrgunitsDCO = converter.getUnitsFromXmlFile(filesUnzippedNotOrgUnits.get(UNITSUNO));
        	ContactsUOVO contactsUNO = converter.getContactsUNOFromXmlFile(
        		filesUnzippedNotOrgUnits.get(CONTACTUNO));
        	logger.debug("Actualizados los datos de oficinas y organismos");
        	getUnitManager().updateUNOUnits(
        		NotOrgunitsDCO, contactsUNO);
        	getUnitRegisterManager().updateUNOUnits(
        		NotOrgunitsDCO, contactsUNO);
        	NotOrgunitsDCO= null;
        	contactsUNO = null;
	}
	
	logger.info("RECOGIENDO OFICINAS");
	// Obtenemos los ficheros con los datos
	String nameFileInitOffice = null;
	File fileInitOffice = null;
	File directoryOffice = null;
	Map<String, String> filesUnzippedOffice = null;
	try {
	    nameFileInitOffice = getUpdateServiceDCO().getFileUpdateOfficesDCO(
		updateStatus.getUpdateDate());

	    try {
		    fileInitOffice = new File(
				getTempFilesDir() +
				    "/" + nameFileInitOffice);
			    directoryOffice = new File(
				getTempFilesDir() +
				    "/" + nameFileInitOffice.replace(
					".zip", ""));
			    directoryOffice.mkdirs();
			    logger.info("Nombre del fichero de actualización de oficinas:" +
				nameFileInitOffice);
		filesUnzippedOffice = ZipUtils.getInstance().unzipFile(
		    fileInitOffice.getAbsolutePath(), directoryOffice.getAbsolutePath());
	    }
	    catch (ZipException zipException) {
		logger.error(
		    ErrorConstants.UNZIP_ERROR_MESSAGE +
			":" + fileInitOffice.getAbsolutePath(), zipException);
		/*throw new DIR3Exception(
		    DIR3ErrorCode.UNZIP_ERROR_MESSAGE, ErrorConstants.UNZIP_ERROR_MESSAGE +
			":" + fileInitOffice.getAbsolutePath(), zipException);*/
	    }
	    catch (Exception iOException) {
		logger.error(
		    ErrorConstants.IO_ERROR_MESSAGE +
			":" + fileInitOffice.getAbsolutePath(), iOException);
		/*throw new DIR3Exception(
		    DIR3ErrorCode.IO_ERROR_MESSAGE, ErrorConstants.IO_ERROR_MESSAGE +
			":" + fileInitOffice.getAbsolutePath(), iOException);*/
	    }

	}
	catch (GetFileDownloadDCOException e) {
	    logger.error(
		ErrorConstants.GET_FILE_OFFICE_UPDATESERVICE_ERROR, e);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_FILE_OFFICE_UPDATESERVICE_ERROR,
		ErrorConstants.GET_FILE_OFFICE_UPDATESERVICE_ERROR, e);
	}
	if (filesUnzippedOffice != null){
        	// Transformamos los datos de los ficheros de oficinas a VOs
        	OfficesVO officesDCO = converter.getOfficesFromXmlFile(
        	    filesUnzippedOffice.get(OFFICES));
        	ContactsOFIVO contactsDCO = converter.getContactsOFIFromXmlFile(
        	    filesUnzippedOffice.get(CONTACTOFFICE));
        	ServicesVO servicesDCO = converter.getServicesFromXmlFile(
        	    filesUnzippedOffice.get(SERVICESOFFICE));
        	HistoriesVO historiesDCO = converter.getHistoriesFromXmlFile(
        	    filesUnzippedOffice.get(HISTORIESOFFICE));
        
        	getOfficeManager().updateOffices(
        	    officesDCO, contactsDCO, servicesDCO, historiesDCO);
        	officesDCO = null;
        	contactsDCO = null;
        	historiesDCO = null;
        	// Transformamos los datos de los ficheros de relaciones a VOs
        	RelationshipsOFIUOVO organizationalRelationshipsDCO =
        	    converter.getRelationsFromXmlFile(
        		filesUnzippedOffice.get(ORGANIZATIONALRELATIONSHIPSOFFICE));
        	getOrganizationalRelationshipsManager().updateRelationships(
        	    organizationalRelationshipsDCO);
        	RelationshipsOFIUOVO relationshipsSIRDCO =
        	    converter.getRelationsSIRFromXmlFile(
        		filesUnzippedOffice.get(RELATIONSHIPSSIROFFICE));
        	getSirRelationshipManager().updateRelationships(
        	    relationshipsSIRDCO);
        	getRelationshipRegistroManager().updateRelationships(
        		relationshipsSIRDCO, servicesDCO);
        	organizationalRelationshipsDCO = null;
        	relationshipsSIRDCO = null;
        	servicesDCO = null;
	}
	// Actualizamos la fecha de ultima actualizacion
	updateStatus.setUpdateDate(Calendar.getInstance().getTime());
	getUpdateStatusManager().save(
	    updateStatus);

	logger.info("Finaliza la actualización del sistema");
    }
    
    /**
     * Obtiene el valor del parámetro updateServiceDCO.
     * 
     * @return updateServiceDCO valor del campo a obtener.
     */
    public DownloadServiceDCO getUpdateServiceDCO() {
	return updateServiceDCO;
    }

    /**
     * Guarda el valor del parámetro updateServiceDCO.
     * 
     * @param updateServiceDCO
     *            valor del campo a guardar.
     */
    public void setUpdateServiceDCO(
	DownloadServiceDCO updateServiceDCO) {
	this.updateServiceDCO = updateServiceDCO;
    }
    
    /**
     * Obtiene el valor del parámetro officeManager.
     * 
     * @return officeManager valor del campo a obtener.
     */
    public OfficeManager getOfficeManager() {
	return officeManager;
    }

    /**
     * Guarda el valor del parámetro officeManager.
     * 
     * @param officeManager
     *            valor del campo a guardar.
     */
    public void setOfficeManager(
	OfficeManager officeManager) {
	this.officeManager = officeManager;
    }

    /**
     * Obtiene el valor del parámetro unitManager.
     * 
     * @return unitManager valor del campo a obtener.
     */
    public UnitManager getUnitManager() {
	return unitManager;
    }

    /**
     * Guarda el valor del parámetro unitManager.
     * 
     * @param unitManager
     *            valor del campo a guardar.
     */
    public void setUnitManager(
	UnitManager unitManager) {
	this.unitManager = unitManager;
    }

    /**
     * Obtiene el valor del parámetro updateStatusManager.
     * 
     * @return updateStatusManager valor del campo a obtener.
     */
    public UpdateStatusManager getUpdateStatusManager() {
	return updateStatusManager;
    }

    /**
     * Guarda el valor del parámetro updateStatusManager.
     * 
     * @param updateStatusManager
     *            valor del campo a guardar.
     */
    public void setUpdateStatusManager(
	UpdateStatusManager updateStatusManager) {
	this.updateStatusManager = updateStatusManager;
    }

    /**
     * Obtiene el valor del parámetro tempFilesDir.
     * 
     * @return tempFilesDir valor del campo a obtener.
     */
    public String getTempFilesDir() {
	return tempFilesDir;
    }

    /**
     * Guarda el valor del parámetro tempFilesDir.
     * 
     * @param tempFilesDir
     *            valor del campo a guardar.
     */
    public void setTempFilesDir(
	String tempFilesDir) {
	this.tempFilesDir = tempFilesDir;
    }

    /**
     * Obtiene el valor del parámetro organizationalRelationshipsManager.
     * 
     * @return organizationalRelationshipsManager valor del campo a obtener.
     */
    public RelationshipManager getOrganizationalRelationshipsManager() {
	return organizationalRelationshipsManager;
    }

    /**
     * Guarda el valor del parámetro organizationalRelationshipsManager.
     * 
     * @param organizationalRelationshipsManager
     *            valor del campo a guardar.
     */
    public void setOrganizationalRelationshipsManager(
	RelationshipManager organizationalRelationshipsManager) {
	this.organizationalRelationshipsManager = organizationalRelationshipsManager;
    }

    /**
     * Obtiene el valor del parámetro sirRelationshipManager.
     * 
     * @return sirRelationshipManager valor del campo a obtener.
     */
    public RelationshipManager getSirRelationshipManager() {
	return sirRelationshipManager;
    }

    /**
     * Guarda el valor del parámetro sirRelationshipManager.
     * 
     * @param sirRelationshipManager
     *            valor del campo a guardar.
     */
    public void setSirRelationshipManager(
	RelationshipManager sirRelationshipManager) {
	this.sirRelationshipManager = sirRelationshipManager;
    }
    
    /**
     * Obtiene el valor del parámetro context.
     * 
     * @return context valor del campo a obtener.
     */
    public ApplicationContext getContext() {
        return context;
    }
    
    /**
     * Guarda el valor del parámetro context.
     * 
     * @param context
     *            valor del campo a guardar.
     */
    public void setContext(
        ApplicationContext context) {
        UpdateServiceDCOImpl.context = context;
    }

    /**
     * Devuelve la instacia del objecto.
     * 
     * @return Dir3WSSpringApplicationContext instancia del objecto.
     */
    private synchronized ApplicationContext getInstance() {
	if (context == null) {
	    context  = new ClassPathXmlApplicationContext(DEFAULT_CONFIG_FILE);
	}
	return context;
    }
    /**
     * Obtiene el valor del parámetro relationshipRegistroManager.
     * 
     * @return relationshipRegistroManager valor del campo a obtener.
     */
    public RelationshipRegistroManager getRelationshipRegistroManager() {
        return relationshipRegistroManager;
    }
    /**
     * Guarda el valor del parámetro relationshipRegistroManager.
     * 
     * @param relationshipRegistroManager
     *            valor del campo a guardar.
     */
    public void setRelationshipRegistroManager(RelationshipRegistroManager relationshipRegistroManager) {
        this.relationshipRegistroManager = relationshipRegistroManager;
    }
    /**
     * Obtiene el valor del parámetro unitRegisterManager.
     * 
     * @return unitRegisterManager valor del campo a obtener.
     */
    public UnitRegistroManager getUnitRegisterManager() {
        return unitRegisterManager;
    }
    /**
     * Guarda el valor del parámetro unitRegisterManager.
     * 
     * @param unitRegisterManager
     *            valor del campo a guardar.
     */
    public void setUnitRegisterManager(UnitRegistroManager unitRegisterManager) {
        this.unitRegisterManager = unitRegisterManager;
    }
    
    
}
