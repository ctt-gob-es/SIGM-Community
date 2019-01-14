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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
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
    private static final Logger LOGGER = Logger.getLogger(UpdateServiceDCOImpl.class);
    private static final String OFFICES = "Oficinas.xml";
    private static final String CONTACTOFFICE = "ContactoOFI.xml";
    private static final String HISTORIESOFFICE = "HistoricosOFI.xml";
    private static final String SERVICESOFFICE = "ServiciosOFI.xml";
    private static final String ORGANIZATIONALRELATIONSHIPSOFFICE =	"RelacionesOrganizativasOFI.xml";
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
    
    /**
	 * Manager para la gestión de unidades orgánicas
	 */
//	protected EstadoActualizacionDCOManager estadoActualizacionDCOManager;
    
    public void reseteaDCO() throws DIR3Exception {
    	// Obtenemos la fecha de la ultima actualizacion
    	UpdateStatusVO updateStatus = null;
    	try {
    		updateStatus = updateStatusManager.getLastSuccessUpdate();
    		
    		updateStatus.setUpdateDate(new GregorianCalendar(2000, 01, 01).getTime());
    	} catch (DIR3Exception dIR3Exception) {
    		LOGGER.error( ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, dIR3Exception);
    		throw new DIR3Exception(DIR3ErrorCode.GET_LAST_UPDATE_UPDATESERVICE_ERROR, ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, dIR3Exception);
    	}    	    	
    	
    	updateDCO(updateStatus);
    	
    }
    
    public void updateDCO()	throws DIR3Exception {
    	// Obtenemos la fecha de la ultima actualizacion
    	UpdateStatusVO updateStatus = null;
    	try {
    		updateStatus = updateStatusManager.getLastSuccessUpdate();
    	} catch (DIR3Exception dIR3Exception) {
    		LOGGER.error( ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, dIR3Exception);
    		throw new DIR3Exception(DIR3ErrorCode.GET_LAST_UPDATE_UPDATESERVICE_ERROR, ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, dIR3Exception);
    	}
    	
    	updateDCO(updateStatus);
    }
    	
    
    public void updateDCO(UpdateStatusVO updateStatus)	throws DIR3Exception {
    	LOGGER.warn("*********** Comienza el proceso de actualización del DIR3 **************");

    	XmlDcoToObject converter = (XmlDcoToObject) getInstance().getBean("XmlDcoToObject");

    	LOGGER.warn("*********** RECOGIENDO UNIDADES ORGÁNICAS **************");
    	
    	String nameFileInitUnits = null;
    	File fileInitUnits = null;
    	File directoryUnits = null;
    	Map<String, String> filesUnzippedUnits = null;
    	    	
    	try {
    		nameFileInitUnits = getUpdateServiceDCO().getFileUpdateUnitsDCO( updateStatus.getUpdateDate());
    		
    		if(StringUtils.isNotEmpty(nameFileInitUnits)){
				fileInitUnits = new File( getTempFilesDir() + "/" + nameFileInitUnits);
				directoryUnits = new File( getTempFilesDir() + "/" + nameFileInitUnits.replace( ".zip", ""));
				directoryUnits.mkdirs();
				filesUnzippedUnits = ZipUtils.getInstance().unzipFile( fileInitUnits.getAbsolutePath(), directoryUnits.getAbsolutePath());
	    	
		    	if (null != filesUnzippedUnits){
		    		UnitsVO unitsDCO = converter.getUnitsFromXmlFile(filesUnzippedUnits.get(UNITS));

		    		replaceFileString(filesUnzippedUnits.get(CONTACTUO));
		    		ContactsUOVO contactsUO = converter.getContactsUOFromXmlFile( filesUnzippedUnits.get(CONTACTUO));
		    		HistoriesVO historiesUO = converter.getHistoriesFromXmlFile( filesUnzippedUnits.get(HISTORIESUO));
		
		    		getUnitManager().updateUnits( unitsDCO, contactsUO, historiesUO);
		    		getUnitRegisterManager().updateUnits( unitsDCO, contactsUO);
		
		        	unitsDCO = null;
		        	contactsUO = null;
		        	historiesUO = null;
		        	
		        	filesUnzippedUnits = null;
		        }
    		} else {
    			LOGGER.warn("*********** El fichero de las UNIDADES ORGÁNICAS está vacío. **************");
    		}
    		
    		if(null != fileInitUnits && fileInitUnits.exists()){
				fileInitUnits.delete();
			}
			if(null != directoryUnits && directoryUnits.exists()){
				try {
					FileUtils.deleteDirectory(directoryUnits);
				} catch (IOException e) {
					LOGGER.error("ERROR al eliminar el directorio: " + directoryUnits + ". " + e.getMessage(), e);
				}
			}
			
    	} catch (ZipException zipException) {
			LOGGER.error( ErrorConstants.UNZIP_ERROR_MESSAGE + ":" + fileInitUnits.getAbsolutePath(), zipException);
		} catch (GetFileDownloadDCOException e) {
			LOGGER.error( ErrorConstants.GET_FILE_UNIT_UPDATESERVICE_ERROR, e);
			throw new DIR3Exception( DIR3ErrorCode.GET_FILE_UNIT_UPDATESERVICE_ERROR, ErrorConstants.GET_FILE_UNIT_UPDATESERVICE_ERROR, e);
		} catch (Exception iOException) {
			LOGGER.error( ErrorConstants.IO_ERROR_MESSAGE + ":" + fileInitUnits.getAbsolutePath(), iOException);
		}
    	
    	LOGGER.warn("*********** RECOGIENDO UNIDADES NO ORGÁNICAS **************");
    	
    	String nameFileInitNotOrgUnits = null;
    	File fileInitNotOrgUnits = null;
    	File directoryNotOrgUnits = null;
    	Map<String, String> filesUnzippedNotOrgUnits = null;
    	
    	try {
    		nameFileInitNotOrgUnits = getUpdateServiceDCO().getFileUpdateNotOrgUnitsDCO(updateStatus.getUpdateDate());

    		if(StringUtils.isNotEmpty(nameFileInitNotOrgUnits)){
	    		fileInitNotOrgUnits = new File(getTempFilesDir() + "/" + nameFileInitNotOrgUnits);
	    		directoryNotOrgUnits = new File( getTempFilesDir() + "/" + nameFileInitNotOrgUnits.replace( ".zip", ""));
	    		directoryNotOrgUnits.mkdirs();
	    		filesUnzippedNotOrgUnits = ZipUtils.getInstance().unzipFile( fileInitNotOrgUnits.getAbsolutePath(), directoryNotOrgUnits.getAbsolutePath());
	    		
		    	if (null != filesUnzippedNotOrgUnits){
		    		UnitsVO NotOrgunitsDCO = converter.getUnitsFromXmlFile(filesUnzippedNotOrgUnits.get(UNITSUNO));

		    		replaceFileString(filesUnzippedNotOrgUnits.get(CONTACTUNO));
		    		ContactsUOVO contactsUNO = converter.getContactsUNOFromXmlFile( filesUnzippedNotOrgUnits.get(CONTACTUNO));
		
		    		getUnitManager().updateUNOUnits(NotOrgunitsDCO, contactsUNO);
		    		getUnitRegisterManager().updateUNOUnits( NotOrgunitsDCO, contactsUNO);
		    		
		    		NotOrgunitsDCO= null;
		    		contactsUNO = null;
		    		filesUnzippedNotOrgUnits = null;
		    	}
    		} else {
	    		LOGGER.warn("*********** El fichero de las UNIDADES NO ORGÁNICAS está vacío. **************");
	    	}
    		
    		if(null != fileInitNotOrgUnits && fileInitNotOrgUnits.exists()){
				fileInitNotOrgUnits.delete();
			}
			if(null != directoryNotOrgUnits && directoryNotOrgUnits.exists()){
				try {
					FileUtils.deleteDirectory(directoryNotOrgUnits);
				} catch (IOException e) {
					LOGGER.error("ERROR al eliminar el directorio: " + directoryNotOrgUnits + ". " + e.getMessage(), e);
				}
			}
			
    	} catch (ZipException zipException) {
			LOGGER.error( ErrorConstants.UNZIP_ERROR_MESSAGE + ":" + fileInitNotOrgUnits.getAbsolutePath(), zipException);
		} catch (GetFileDownloadDCOException e) {
			LOGGER.error( ErrorConstants.GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR, e);
			throw new DIR3Exception( DIR3ErrorCode.GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR, ErrorConstants.GET_FILE_NOTORGUNIT_UPDATESERVICE_ERROR, e);
		} catch (Exception iOException) {
			LOGGER.error( ErrorConstants.IO_ERROR_MESSAGE + ":" + fileInitNotOrgUnits.getAbsolutePath(), iOException);
		}
    	
    	LOGGER.warn("*********** RECOGIENDO OFICINAS **************");
    	
    	String nameFileInitOffice = null;
    	File fileInitOffice = null;
    	File directoryOffice = null;
    	Map<String, String> filesUnzippedOffice = null;
    	try {
    		nameFileInitOffice = getUpdateServiceDCO().getFileUpdateOfficesDCO( updateStatus.getUpdateDate());
    		
	    	if(StringUtils.isNotEmpty(nameFileInitOffice)){
	
	    		fileInitOffice = new File( getTempFilesDir() + "/" + nameFileInitOffice);
	    		directoryOffice = new File( getTempFilesDir() + "/" + nameFileInitOffice.replace( ".zip", ""));
	    		directoryOffice.mkdirs();
	
	    		filesUnzippedOffice = ZipUtils.getInstance().unzipFile( fileInitOffice.getAbsolutePath(), directoryOffice.getAbsolutePath());
		
		    	if (null != filesUnzippedOffice){
		    		OfficesVO officesDCO = converter.getOfficesFromXmlFile( filesUnzippedOffice.get(OFFICES));

		    		replaceFileString(filesUnzippedOffice.get(CONTACTOFFICE));
		    		ContactsOFIVO contactsDCO = converter.getContactsOFIFromXmlFile( filesUnzippedOffice.get(CONTACTOFFICE));

		    		ServicesVO servicesDCO = converter.getServicesFromXmlFile( filesUnzippedOffice.get(SERVICESOFFICE));
		    		HistoriesVO historiesDCO = converter.getHistoriesFromXmlFile( filesUnzippedOffice.get(HISTORIESOFFICE));
		    		
		        	getOfficeManager().updateOffices( officesDCO, contactsDCO, servicesDCO, historiesDCO);
		
		        	officesDCO = null;
		        	contactsDCO = null;
		        	historiesDCO = null;
		
		        	RelationshipsOFIUOVO organizationalRelationshipsDCO = converter.getRelationsFromXmlFile( filesUnzippedOffice.get(ORGANIZATIONALRELATIONSHIPSOFFICE));
		        	getOrganizationalRelationshipsManager().updateRelationships( organizationalRelationshipsDCO);
		        	RelationshipsOFIUOVO relationshipsSIRDCO = converter.getRelationsSIRFromXmlFile( filesUnzippedOffice.get(RELATIONSHIPSSIROFFICE));
		        	getSirRelationshipManager().updateRelationships( relationshipsSIRDCO);
		        	getRelationshipRegistroManager().updateRelationships( relationshipsSIRDCO, servicesDCO);
		        	
		        	organizationalRelationshipsDCO = null;
		        	relationshipsSIRDCO = null;
		        	servicesDCO = null;
		        	filesUnzippedOffice = null;
		        }
	    	} else {
	    		LOGGER.warn("*********** El fichero de las OFICINAS está vacío. **************");
	    	}
	    	
	    	if(null != fileInitOffice && fileInitOffice.exists()){
				fileInitOffice.delete();
			}
			if(null != directoryOffice && directoryOffice.exists()){
				try {
					FileUtils.deleteDirectory(directoryOffice);
				} catch (IOException e) {
					LOGGER.error("ERROR al eliminar el directorio: " + directoryOffice + ". " + e.getMessage(), e);
				}
			}
			
		} catch (ZipException zipException) {
			LOGGER.error( ErrorConstants.UNZIP_ERROR_MESSAGE + ":" + fileInitOffice.getAbsolutePath(), zipException);
		} catch (GetFileDownloadDCOException e) {
			LOGGER.error( ErrorConstants.GET_FILE_OFFICE_UPDATESERVICE_ERROR, e);
			throw new DIR3Exception( DIR3ErrorCode.GET_FILE_OFFICE_UPDATESERVICE_ERROR, ErrorConstants.GET_FILE_OFFICE_UPDATESERVICE_ERROR, e);
		} catch (Exception iOException) {
			LOGGER.error( ErrorConstants.IO_ERROR_MESSAGE + ":" + fileInitOffice.getAbsolutePath(), iOException);
		}

    	// Actualizamos la fecha de ultima actualizacion
    	updateStatus.setUpdateDate(Calendar.getInstance().getTime());
    	getUpdateStatusManager().save( updateStatus);

		LOGGER.warn("*********** Finaliza el proceso de actualización del DIR3 **************");
    }

    public static void replaceFileString(String filePath) throws IOException {
    	
        FileInputStream fis = new FileInputStream(filePath);
        
        String content = IOUtils.toString(fis, "UTF8");
        content = content.replaceAll("&", "&amp;");
        
        FileOutputStream fos = new FileOutputStream(filePath);
        IOUtils.write(content, new FileOutputStream(filePath), "UTF8");
        
        fis.close();
        fos.close();
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
    public void setUpdateServiceDCO( DownloadServiceDCO updateServiceDCO) {
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
    public void setOfficeManager(OfficeManager officeManager) {
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
    public void setUnitManager(UnitManager unitManager) {
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
    public void setUpdateStatusManager(	UpdateStatusManager updateStatusManager) {
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
    public void setTempFilesDir(String tempFilesDir) {
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
    public void setOrganizationalRelationshipsManager(RelationshipManager organizationalRelationshipsManager) {
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
    public void setSirRelationshipManager(RelationshipManager sirRelationshipManager) {
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
    public void setContext(ApplicationContext context) {
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
