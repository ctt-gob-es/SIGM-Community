package es.dipucr.contratacion.rule;

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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class ReqSubsanDocAdminNuevaLeyRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(ReqSubsanDocAdminNuevaLeyRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			logger.warn("GenerateNotificacionDecreto - Init");
		
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
	    	int documentId = 0;
	    	Object connectorSession = null;
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			String sqlQueryPart = "WHERE (ROL != 'TRAS' OR ROL IS NULL) AND NUMEXP = '"+numExp+"' ORDER BY ID";	
			IItemCollection participantes = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", sqlQueryPart);
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
			
				logger.warn("Hay participantes");

				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
	        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
	        	}else {

        			//IItem taskTpDoc = (IItem)taskTpDocCollection.iterator().next();
	        		//documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        		
	        		String plantilla = DocumentosUtil.getPlantillaDefecto(cct, rulectx.getTaskProcedureId());
	        		String tipoDocumento = "";
	    			
	    			if(StringUtils.isNotEmpty(plantilla)){
	    				tipoDocumento = DocumentosUtil.getTipoDocumentoByPlantilla(cct, plantilla);
	    			}
	        		
	        		Iterator<IItem> it = taskTpDocCollection.iterator();
	        		while (it.hasNext()){
	        			IItem taskTpDoc = (IItem)it.next();
	        			if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(tipoDocumento)){
	        				documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
	        			}
	        		}
	        		
	        		//Comprobamos que haya encontrado el Tipo de documento
	        		if (documentTypeId != 0){
		        		
	        			logger.warn("Tipo de documento encontrado");
		        		// Comprobar que el tipo de documento tiene asociado una plantilla
			        	IItemCollection tpDocsTemplatesCollection = (IItemCollection)procedureAPI.getTpDocsTemplates(documentTypeId);
			        	if(tpDocsTemplatesCollection==null || tpDocsTemplatesCollection.toList().isEmpty()){
			        		throw new ISPACInfo(Messages.getString("Error en el tipo de documento Requerimiento de Subsanación de la Doc Adm"));
			        	}else{
			        		Iterator<IItem> itPlantillas = tpDocsTemplatesCollection.iterator();
			        		while(itPlantillas.hasNext()){
			        			IItem tpDocsTemplate = (IItem)itPlantillas.next();
			        			if(tpDocsTemplate.getString("NOMBRE").equals(plantilla)){
			        				templateId = tpDocsTemplate.getInt("ID");
						        	logger.warn("Plantilla encontrada");
			        			}
					        	
			        		}
			        		
				        	
				    		// 4. Para cada participante generar una notificación
							for (int i=0;i<participantes.toList().size();i++){
								try {
									connectorSession = gendocAPI.createConnectorSession();
									IItem participante = (IItem) participantes.toList().get(i);
									// Abrir transacción para que no se pueda generar un documento sin fichero
							        cct.beginTX();
							        
								
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
							        	cct.setSsVariable("OBSERVACIONES", observaciones);
							        	
							        	StringBuffer subsanacion = new StringBuffer();
							        	
							        	//Saco los documentos que hay que subsanar
							        	IItemCollection plicaCol = entitiesAPI.getEntities("CONTRATACION_PLICA", numExp);
								    	Iterator<IItem> itPlica = plicaCol.iterator();
								    									    	
								    	if(itPlica.hasNext()){
								    		
								    		IItem plica = (IItem)itPlica.next();
								    		String apto = "";
								    		if(plica.getString("APTO")!=null)apto = plica.getString("APTO");							

									    	if(apto.equals("NO")){
									    		
									    		subsanacion.append("NO APTO");
								    		
									    		cct.setSsVariable("SUBSANACION", subsanacion.toString());
								        	
										    	IItem entityDocumentT = gendocAPI.createTaskDocument(taskId, documentTypeId);
												documentId = entityDocumentT.getKeyInt();
												
												// Generar el documento a partir la plantilla "Notificación Decreto"
												IItem entityTemplateT = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId);
												//IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
												
												String infoPagT = entityTemplateT.getString("INFOPAG");
												int idPlantillaT = entityTemplateT.getInt("ID");
												entityTemplateT.store(cct);
	
												entityDocument = gendocAPI.createTaskDocument(taskId,documentTypeId);
												documentId = entityDocument.getKeyInt();
	
												String sFileTemplate = getFile(gendocAPI, connectorSession,infoPagT, templateId, idPlantillaT);
	
												// Generar el documento a partir la plantilla
												IItem entityTemplate = gendocAPI.attachTaskTemplate(
														connectorSession, taskId, documentId, templateId,
														sFileTemplate);
												
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
												entityTemplate.store(cct);
										        												
												entityTemplateT.delete(cct);
												entityDocumentT.delete(cct);
									    	}
								    	}
										
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
									logger.error(e.getMessage(), e);
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
			cct.endTX(true);
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

/**	private String getInfoPag (IRuleContext rulectx, String plantillaDoc){
		try{
			String infoPag = null;
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			//IGenDocAPI genDocAPI = rulectx.getClientContext().getAPI().getGenDocAPI();
			
			// Obtener el documento generado en la fase de Inicio, concretamente su campo infopag
			// Debe haber uno, ya que en la fase de Inicio se comprueba que se haya anexado sólo un doc (ValidateNumDocsTramiteRule)
			
			// Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='"+plantillaDoc+"'", "FDOC DESC");
			IItem document = null;
			if (documentsCollection!=null && documentsCollection.next()){
				document = (IItem)documentsCollection.iterator().next();
			}
				
			// Obtener el valor del campo INFOPAG
			if (document!=null){
				infoPag = document.getString("INFOPAG");
			}
			return infoPag;
		}catch(Exception e){
			System.out.println ("Error en getInfoPag al obtener el valor INFOPAG del documento: "+e);
			return null;
		}
			
		}
	
	
	private int getDocumentId (IRuleContext rulectx, String plantillaDoc){
		try{
			int id = -1;
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='"+plantillaDoc+"'", "FDOC DESC");
			IItem document = null;
			if (documentsCollection!=null && documentsCollection.next()){
				document = (IItem)documentsCollection.iterator().next();
			}
				
			// Obtener el valor del campo ID
			if (document!=null){
				id = document.getInt("ID");
			}
			return id;
		}catch(Exception e){
			System.out.println ("Error en getDocumentId al obtener el valor ID del documento: "+e);
			return -1;
		}
			
		}
	
	*/
	
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

				// Obtiene el manejador de plantillas
				templateManager = (FileTemplateManager) FileTemplateManager.getInstance();

				
				
				
				//Se almacena documento
				String fileName = FileTemporaryManager.getInstance().newFileName("."+extension);
				//fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + fileName;
				
				//String fileNamePath = FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + fileName;
				String fileNamePath = templateManager.getFileMgrPath() + "/" + fileName;
				
				logger.warn("GetFile. fileName: "+fileName);
				logger.warn("GetFile. fileNamePath: "+fileNamePath);
				
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
				logger.warn("GetFile. sName: "+sName);
				logger.warn("GetFile. infoPag: "+infoPag);
				
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
				logger.error(e.getMessage(), e);
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
	/*private boolean deleteFile(String sFileTemplate) throws ISPACException{
		
		FileTemplateManager templateManager = null;
		FileTemporaryManager temporaryManager = null;
		
		try{

			// Obtiene el manejador de plantillas
			templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
			// Obtiene el manejador de ficheros temporales
			temporaryManager = FileTemporaryManager.getInstance();
			
			boolean resultado = true;
			//File fTemplate = new File(FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sFileTemplate);
			File fTemplate = new File(templateManager.getFileMgrPath() + "/" + sFileTemplate);
			File fTemporary = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
			
			
			if (!fTemplate.delete()){
				//System.out.println ("No se pudo eliminar el documento: "+FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sFileTemplate);
				System.out.println ("No se pudo eliminar el documento: "+templateManager.getFileMgrPath() + "/" + sFileTemplate);
				resultado = false;
			}
			if (!fTemporary.delete()){
				System.out.println ("No se pudo eliminar el documento: "+FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
				resultado = false;
			}
			
			return resultado;
		} catch (Exception e) {
			//throw new ISPACRuleException("Error al eliminar el documento "+FileTemplateManager.getInstance().getFileTemplateMgrPath() + "/" + sFileTemplate, e);
			throw new ISPACRuleException("Error al eliminar el documento "+templateManager.getFileMgrPath() + "/" + sFileTemplate, e);
		}
	} */
	
	
}