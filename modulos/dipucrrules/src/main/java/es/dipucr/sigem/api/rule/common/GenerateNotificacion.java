package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.util.Iterator;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class GenerateNotificacion {
private static final Logger logger = Logger.getLogger(GenerateNotificacion.class);

	/**
	 * DocNotifTexto: Variable del nombre primer tramite donde se inserta el texto de la notificacion
	 * DocNotif: Variable del nombre de la notificacion de salida
	 * **/
	@SuppressWarnings("unchecked")
	public static boolean GenerarNotificaciones(IRuleContext rulectx, String STR_Relacion, String DocNotifTexto, String DocNotif) throws ISPACRuleException {
		String numExp = "";
		try{
			logger.info("GenerateNotificacion - Init");
		
			// APIs
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
			
			// Variables
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			IItem processTask =  entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			
			numExp = rulectx.getNumExp();
			String nombre = "";
	    	String dirnot = "";
	    	String c_postal = "";
	    	String localidad = "";
	    	String caut = "";
	    	String observaciones = "";
	    	String ndoc = "";
	    	int documentId = 0;
	    	Object connectorSession = null;
	    	String recurso = "";
	    	String id_ext;
	    	
			// 1. Obtener participantes del expediente actual, con relación != "Trasladado"
			IItemCollection participantes = ParticipantesUtil.getParticipantes( cct, numExp, "ROL != '"+STR_Relacion+"'", "ID");
			
			// 2. Comprobar que hay algún participante para el cual generar su notificación
			if (participantes!=null && participantes.toList().size()>=1) {
				// 3. Obtener plantilla "Notificación Decreto"
				// Comprobar que el trámite tenga un tipo de documento asociado y obtenerlo
	        	IItemCollection taskTpDocCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	        	if(taskTpDocCollection==null || taskTpDocCollection.toList().isEmpty()){
	        		throw new ISPACInfo(Messages.getString("error.decretos.acuses.TaskTpDoc"));
	        	}else {
	        		//Hay dos tipos de documento asociados al trámite: Decreto y Notificación Decreto
	        		//Necesitamos el de Notificación del Decreto
	        		Iterator <IItem> it = taskTpDocCollection.iterator();
	        		while (it.hasNext()){
	        			IItem taskTpDoc = it.next();
	        			if (taskTpDoc.getString("CT_TPDOC:NOMBRE").equals(DocNotif)){
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
							        	if ((String)participante.get("OBSERVACIONES")!=null){
							        		observaciones = (String)participante.get("OBSERVACIONES");
							        	}else{
							        		observaciones = "";
							        	}
							        	if ((String)participante.get("NDOC")!=null){
							        		ndoc = (String)participante.get("NDOC");
							        	}else{
							        		ndoc = "";
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
							        	
							        	// Obtener el sustituto del recurso en la tabla SPAC_VLDTBL_RECURSOS
							        	String sqlQueryPart = "WHERE VALOR = '"+recurso+"'";
							        	IItemCollection colRecurso = entitiesAPI.queryEntities("DPCR_RECURSOS", sqlQueryPart);
							        	if (colRecurso.iterator().hasNext()){
							        		IItem iRecurso = (IItem)colRecurso.iterator().next();
							        		recurso = iRecurso.getString("SUSTITUTO");
							        	}
							        	
							        	cct.setSsVariable("NOMBRE", nombre);
							        	cct.setSsVariable("DIRNOT", dirnot);
							        	cct.setSsVariable("C_POSTAL", c_postal);
							        	cct.setSsVariable("LOCALIDAD", localidad);
							        	cct.setSsVariable("CAUT", caut);
							        	cct.setSsVariable("OBSERVACIONES", observaciones);
							        	cct.setSsVariable("NDOC", ndoc);
							        	cct.setSsVariable("RECURSO", recurso);
							        	
							        	
							        	entityDocument = gendocAPI.createTaskDocument(taskId, documentTypeId);
										documentId = entityDocument.getKeyInt();
										
										String  infoPag =getInfoDoc(rulectx, DocNotifTexto);
										//Plantilla de Notificaciones
										
										String sFileTemplate = DocumentosUtil.getFile(cct, infoPag, null, extensionEntidad).getName();
										
										// Generar el documento a partir la plantilla "Notificación Decreto"
										IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId, sFileTemplate);
										//IItem entityTemplate = gendocAPI.attachTaskTemplate(gendocAPI,connectorSession, taskId, documentId, templateId, sFileTemplate, infoPag);										
										// Referencia al fichero del documento en el gestor documental
										entityTemplate.set("EXTENSION", extensionEntidad);
										String templateDescripcion = entityTemplate.getString("DESCRIPCION");
										templateDescripcion = templateDescripcion + " - " + cct.getSsVariable("NOMBRE");
										entityTemplate.set("DESCRIPCION", templateDescripcion);
										entityTemplate.set("DESTINO", nombre);
										/**
							        	 * INICIO[Teresa] Ticket#106#: añadir el campo id_ext
							        	 * **/
										entityTemplate.set("DESTINO_ID", id_ext);
										/**
							        	 * FIN[Teresa] Ticket#106#: añadir el campo id_ext
							        	 */
										entityTemplate.store(cct);
										        
										// Si todo ha sido correcto borrar las variables de la session
										cct.deleteSsVariable("NOMBRE");
										cct.deleteSsVariable("DIRNOT");
										cct.deleteSsVariable("C_POSTAL");
										cct.deleteSsVariable("LOCALIDAD");
										cct.deleteSsVariable("CAUT");
										cct.deleteSsVariable("OBSERVACIONES");
										cct.deleteSsVariable("NDOC");
										cct.deleteSsVariable("RECURSO");
										
										DocumentosUtil.deleteFile(sFileTemplate);
							        }
								}catch (Exception e) {
									
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
			        	}
	        			
	        		}
	        	}
        		else{
        			throw new ISPACInfo("No existe el tipo de documento " + DocNotif + " para el expediente " + numExp + ".");
        		}			
    		}
		}
		return new Boolean(true);
		}catch(Exception e) {
			logger.error("Error al generar el documento: " + DocNotifTexto + ", en el expediente: " + numExp + ", con tipo de documento: " + DocNotif + ". " + e.getMessage(), e);
	    	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
	    	throw new ISPACRuleException("Error al generar el documento: " + DocNotifTexto + ", en el expediente: " + numExp + ", con tipo de documento: " + DocNotif + ". " + e.getMessage(), e);
	    }
	}
	
	private static String getInfoDoc (IRuleContext rulectx, String docNotifTexto){
		try{
			String infoPag = null;
			
			IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
			
			// Obtener el documento generado en la fase de Inicio, concretamente su campo infopag
			// Debe haber uno, ya que en la fase de Inicio se comprueba que se haya anexado sólo un doc (ValidateNumDocsTramiteRule)
			
			// Obtener el documento "Plantilla de Notificaciones" del expediente que se adjuntó en el primer trámite de "Creación del decreto"
			IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "NOMBRE='"+docNotifTexto+"'", "FDOC DESC");
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
			logger.error("Error en getInfoPag al obtener el valor INFOPAG del documento: " + e.getMessage(), e);
			return null;
		}
	}
}
