package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemplateManager;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;

public class GenerateNotificationDecretoIniciaCircFirmaRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(GenerateNotificationDecretoIniciaCircFirmaRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			logger.info("GenerateNotificacionDecreto - Init");
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			
			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			String numExp = rulectx.getNumExp();
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	String recurso = "";
	    	String id_ext = "";
	    	//String recursoTexto = "";
	    	String observaciones = "";
	    	String tipoDireccion = "";//[eCenpri-Felipe #956]
	    	int documentId = 0;
	    	Object connectorSession = null;
			String sFileTemplate = null;
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			String sqlQueryPart = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '"+numExp+"' ORDER BY ID";	
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
			
				// 3. Obtener plantilla "Notificación Decreto"
				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
	        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
	        	}else {

        			//IItem taskTpDoc = (IItem)taskTpDocCollection.iterator().next();
	        		//documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        		
	        		//Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
	        		//Necesitamos el de Notificación del Decreto
	        		Iterator<IItem> it = taskTpDocCollection.iterator();
	        		while (it.hasNext()){
	        			IItem taskTpDoc = (IItem)it.next();
	        			if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals("Notificación Decreto")){
	        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        			}
	        		}
	        		
	        		//Comprobamos que haya encontrado el Tipo de documento
	        		if (documentTypeId != 0){
		        		
		        		// Comprobar que el tipo de documento tiene asociado una plantilla
			        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
			        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
			        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.tpDocsTemplates"));
			        	}else{
			        		IItem tpDocsTemplate = (IItem)tpDocsTemplatesCollection.iterator().next();
				        	templateId = tpDocsTemplate.getInt("ID");
				        	
				    		// 4. Para cada participante generar una notificación
							for (int i=0;i<participantes.toList().size();i++){
								try {
									connectorSession = gendocAPI.createConnectorSession();
									IItem participante = (IItem) participantes.toList().get(i);
									// Abrir transacción para que no se pueda generar un documento sin fichero
							        //cct.beginTX();
							        
								
									if (participante!=null){
							        
										// Añadir a la session los datos para poder utilizar <ispactag sessionvar='var'> en la plantilla
							        	if ((String)participante.get("NOMBRE")!=null){
							        		nombre = (String)participante.get("NOMBRE");
							        	}else{
							        		nombre = "";
							        	}
							        	if ((String)participante.get("DIRNOT")!=null){
							        		dirnot = (String)participante.get("DIRNOT");
							        	}else{
							        		dirnot = "";
							        	}
							        	if ((String)participante.get("C_POSTAL")!=null){
							        		c_postal = (String)participante.get("C_POSTAL");
							        	}else{
							        		c_postal = "";
							        	}
							        	if ((String)participante.get("LOCALIDAD")!=null){
							        		localidad = (String)participante.get("LOCALIDAD");
							        	}else{
							        		localidad = "";
							        	}
							        	if ((String)participante.get("CAUT")!=null){
							        		caut = (String)participante.get("CAUT");
							        	}else{
							        		caut = "";
							        	}
							        	if ((String)participante.get("RECURSO")!=null){
							        		recurso = (String)participante.get("RECURSO");
							        	}else{
							        		recurso = "";
							        	}
							        	/**
							        	 * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
							        	 * **/
							        	if ((String)participante.get("ID_EXT")!=null){
							        		id_ext = (String)participante.get("ID_EXT");
							        	}else{
							        		id_ext = "";
							        	}
							        	/**
							        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
							        	 * **/
							        	//if ((String)participante.get("RECURSO_TEXTO")!=null) recursoTexto = (String)participante.get("RECURSO_TEXTO");
							        	if ((String)participante.get("OBSERVACIONES")!=null){
							        		observaciones = (String)participante.get("OBSERVACIONES");
							        	}else{
							        		observaciones = "";
							        	}
							        	//INICIO [eCenpri-Felipe #956]
							        	if ((String)participante.get("TIPO_DIRECCION")!=null){
							        		tipoDireccion = (String)participante.get("TIPO_DIRECCION");
							        	}
							        	else{
							        		tipoDireccion = ""; //No necesario
							        	}
							        	//FIN [eCenpri-Felipe #956]

							        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
							        	sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
							        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
							        	if (colRecurso.iterator().hasNext()){
							        		IItem iRecurso = (IItem)colRecurso.iterator().next();
							        		recurso = iRecurso.getString("SUSTITUTO");
							        	}
							        	/**
							        	 * INICIO
							        	 * ##Ticket #172 SIGEM decretos y secretaria, modificar el recurso
							        	 * **/
							        	if (recurso.equals("")){
							        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.sinRECUSO;
							        	}
							        	else{
							        		recurso += es.dipucr.sigem.api.rule.procedures.Constants.SECRETARIAPROC.conRECUSO;
							        	}
							        	/**
							        	 * FIN
							        	 * ##Ticket #172 SIGEM decretos y secretaria, modificar el recurso
							        	 * **/
							        	
							        	cct.setSsVariable("NOMBRE", nombre);
							        	cct.setSsVariable("DIRNOT", dirnot);
							        	cct.setSsVariable("C_POSTAL", c_postal);
							        	cct.setSsVariable("LOCALIDAD", localidad);
							        	cct.setSsVariable("CAUT", caut);
							        	cct.setSsVariable("RECURSO", recurso);
							        	//cct.setSsVariable("RECURSO_TEXTO", recursoTexto);
							        	cct.setSsVariable("OBSERVACIONES", observaciones);
							        	cct.setSsVariable("TIPO_DIRECCION", tipoDireccion);//[eCenpri-Felipe #956]
							        	
							        	entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
										documentId = entityDocument.getKeyInt();
										
										IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='Plantilla de Notificaciones'", "FDOC DESC");
										String infoPag = "";
										int idPlantilla = -1;
										String autor = "";
										String autorInfo = "";
										IItem document = null;
										if (documentsCollection!=null && documentsCollection.next()){
											document = (IItem)documentsCollection.iterator().next();
											infoPag = document.getString("INFOPAG");
											idPlantilla = document.getInt("ID");
											autor = document.getString("AUTOR");
									        autorInfo = document.getString("AUTOR_INFO");
										}											
										
										sFileTemplate = getFile(gendocAPI, connectorSession, infoPag, templateId, idPlantilla);
										
										// Generar el documento a partir la plantilla "Notificación Decreto"
										IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
										
										// Referencia al fichero del documento en el gestor documental
										String docref = entityTemplate.getString("INFOPAG");
										String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
										entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
										String templateDescripcion = entityTemplate.getString("DESCRIPCION");
										templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
										entityTemplate.set("DESCRIPCION", templateDescripcion);
										entityTemplate.set("DESTINO", cct.getSsVariable("NOMBRE"));
										/**
							        	 * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
							        	 * **/
										entityTemplate.set("DESTINO_ID", id_ext);
										/**
							        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
							        	 * **/
										
										entityTemplate.set("AUTOR", autor);
										entityTemplate.set("AUTOR_INFO", autorInfo);
										
										entityTemplate.store(cct);
										
							        	// 5. Actualizar el campo 'DECRETO_NOTIFICADO' con valor 'Y'
										IItem participanteAActualizar = entitiesAPI.getParticipant(participante.getInt("ID"));
										participanteAActualizar.set("DECRETO_NOTIFICADO", "Y");
										participanteAActualizar.store(cct);
								        
										// Si todo ha sido correcto borrar las variables de la session
										cct.deleteSsVariable("NOMBRE");
										cct.deleteSsVariable("DIRNOT");
										cct.deleteSsVariable("C_POSTAL");
										cct.deleteSsVariable("LOCALIDAD");
										cct.deleteSsVariable("CAUT");
										cct.deleteSsVariable("RECURSO");
										//cct.deleteSsVariable("RECURSO_TEXTO");
										cct.deleteSsVariable("OBSERVACIONES");
										cct.deleteSsVariable("TIPO_DIRECCION");//[eCenpri-Felipe #956]
										
										deleteFile(sFileTemplate);
										
								    }
								}catch (Throwable e) {
									
									// Si se produce algún error se hace rollback de la transacción
									cct.endTX(false);
									
									String message = "exception.documents.generate";
									String extraInfo = null;
									Throwable eCause = e.getCause();
									
									if (eCause instanceof ISPACException) {
										
										if (eCause.getCause() instanceof NoConnectException) {
											extraInfo = "exception.extrainfo.documents.openoffice.off"; 
										}
										else {
											extraInfo = eCause.getCause().getMessage();
										}
									}
									else if (eCause instanceof DisposedException) {
										extraInfo = "exception.extrainfo.documents.openoffice.stop";
									}
									else {
										extraInfo = e.getMessage();
									}
									logger.error("Error al recuperar los documentos.: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
									throw new ISPACInfo(message, extraInfo);
									
								}finally {
									
									if (connectorSession != null) {
										gendocAPI.closeConnectorSession(connectorSession);
									}
								}
							}// for
				      	}
	
	        		}else{//if (documentTypeId != 0){
	        			throw new ISPACInfo("No existe el tipo de documento Notificación Decreto.");
	        		}
	        	}
			}
			// Si todo ha sido correcto se hace commit de la transacción
			//cct.endTX(true);
			DecretosUtil.mandarAfirmarCircuitoFirmaEspecificoTodosDocumentosTramite(rulectx);
			
		} catch(Exception e) {
			logger.error("Error al recuperar los documentos.: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}	
	
	/**
	 * Obtiene el fichero correspondiente al infoPag indicado 
	 *
	 * @param rulectx
	 * @param infoPag
	 * @param templateId
	 * @return 
	 * @throws ISPACException
	 */
	private String getFile(IGenDocAPI gendocAPI, Object connectorSession, String infoPag, int templateId, int idPlantilla) throws ISPACException{
		
		// API
		//IGenDocAPI gendocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();

		//Object connectorSession = null;
		try {
			connectorSession = gendocAPI.createConnectorSession();
			File file = null;
			try{
				String extension = MimetypeMapping.getExtension(gendocAPI.getMimeType(connectorSession, infoPag));
				
				FileTemplateManager templateManager = null;
//				FileTemporaryManager temporaryManager = null;
				// Obtiene el manejador de plantillas
				templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
				// Obtiene el manejador de ficheros temporales
//				temporaryManager = FileTemporaryManager.getInstance();
				
				//Se almacena documento
				String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				//fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
				
				//String fileNamePath = FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + fileName;
				String fileNamePath = templateManager.getFileMgrPath() + "/" + fileName;
				
				// Nombre de la plantilla
				String sName = Integer.toString(templateId) + "." + extension;
				
				//Control de plantillas por multientidad
				OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
				if (info != null){
					String organizationId = info.getOrganizationId();
					//Se añade el numExp al nombre de la plantilla para evitar colisiones al generar notificaciones simultaneamente desde
					//dos expedientes distintos de la misma entidad
					sName = organizationId + "_" + idPlantilla + "_" + sName; 
				}
				
				OutputStream out = new FileOutputStream(fileNamePath);
				gendocAPI.getDocument(connectorSession, infoPag, out);
				file = new File(fileNamePath);
				//File file2 = new File(FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sName);
				File file2 = new File(templateManager.getFileMgrPath() + "/" + sName);
				file.renameTo(file2);
				file.delete();
								
				
				OutputStream out2 = new FileOutputStream(fileNamePath);
				gendocAPI.getDocument(connectorSession, infoPag, out2);
				File file3 = new File(fileNamePath);
				File file4 = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sName);
				file3.renameTo(file4);
				file3.delete();
								
				return sName;
			} catch (FileNotFoundException e) {
				throw new ISPACRuleException("Error al intentar obtener el documento, no existe.", e);
			}
		}finally {
			if (connectorSession != null) {
				gendocAPI.closeConnectorSession(connectorSession);
			}
    	}
	} 

	
	/**
	 * Elimina el fichero template y el temporary correspondientes 
	 *
	 * @param rulectx
	 * @param infoPag
	 * @param templateId
	 * @return 
	 * @throws ISPACException
	 */
	private boolean deleteFile(String sFileTemplate) throws ISPACException{
		
		FileTemplateManager templateManager = null;
//		FileTemporaryManager temporaryManager = null;
		
		try{

			// Obtiene el manejador de plantillas
			templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
			// Obtiene el manejador de ficheros temporales
//			temporaryManager = FileTemporaryManager.getInstance();
			
			boolean resultado = true;
			//File fTemplate = new File(FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sFileTemplate);
			File fTemplate = new File(templateManager.getFileMgrPath() + "/" + sFileTemplate);
			File fTemporary = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
			
			try{
				if (fTemplate!= null && fTemplate.exists() && !fTemplate.delete()){
					//System.out.println ("No se pudo eliminar el documento: "+FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sFileTemplate);
					logger.error ("No se pudo eliminar el documento: "+templateManager.getFileMgrPath() + "/" + sFileTemplate);
					resultado = false;
				}
			}
			catch(Exception e){}
			try{
				if (fTemporary!=null && fTemporary.exists() && !fTemporary.delete()){
				logger.error("No se pudo eliminar el documento: "+FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
				resultado = false;
				}
			}
			catch(Exception e){}
			
			return resultado;
		} catch (Exception e) {
			//throw new ISPACRuleException("Error al eliminar el documento "+FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sFileTemplate, e);
			throw new ISPACRuleException("Error al eliminar el documento "+templateManager.getFileMgrPath() + "/" + sFileTemplate, e);
		}
	} 
	
	
}
