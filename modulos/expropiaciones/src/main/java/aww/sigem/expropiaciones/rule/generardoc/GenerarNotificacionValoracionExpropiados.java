package aww.sigem.expropiaciones.rule.generardoc;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
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
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

public class GenerarNotificacionValoracionExpropiados implements IRule {
	
	private static final Logger logger = Logger.getLogger(GenerarNotificacionValoracionExpropiados.class);
	
	//private static final String plantilla = "EXPR-014 - Notificaci贸n Hoja Valoraci贸n";
	private static final String plantilla = "EXPR-014 - Notificaci髇 Hoja Valoraci髇";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla GenerarNotificacion");

			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			IGenDocAPI gendocAPI = cct.getAPI().getGenDocAPI();
			IProcedureAPI procedureAPI = cct.getAPI().getProcedureAPI();
	
			IItem entityDocument = null;
			int documentTypeId = 0;
			int templateId = 0;
			int taskId = rulectx.getTaskId();
			
			//logger.warn("Stage id: " + rulectx.getStageId());

			IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			String numExp = rulectx.getNumExp();
			//logger.warn("NumEXP:" + numExp);
			Object connectorSession = null;

			IItemCollection taskTpDocCollection = procedureAPI.getTaskTpDoc(idTramCtl);

			//logger.warn("idTramCtl: " + idTramCtl);
			
			if ((taskTpDocCollection == null)|| (taskTpDocCollection.toList().isEmpty())) {
				//logger.warn("error.decretos.acuses.TaskTpDoc");
				throw new ISPACInfo(// Messages.getString(
						"error.decretos.acuses.TaskTpDoc"
				// )
				);
			}

			//logger.warn("Obteniendo Tipo de documento");
			Iterator it = taskTpDocCollection.iterator();
			while (it.hasNext()) {
				IItem taskTpDoc = (IItem) it.next();
				//logger.warn("nombre doc: " + taskTpDoc.get("CT_TPDOC:NOMBRE"));
				
				// Notificaci贸n Hoja de valoracion.
				String plantTipo = (String) taskTpDoc.get("CT_TPDOC:NOMBRE");
				if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(plantilla)) {
					documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
					//logger.warn("Obteniendo documentTypeId: " + documentTypeId);
				} else {
					//logger.warn("No se puede obtener documentTypeId: " + documentTypeId);
				}

			}
			
			if (documentTypeId != 0) {
				//logger.warn("Obteniendo plantilla ");
				IItemCollection tpDocsTemplatesCollection = procedureAPI
						.getTpDocsTemplates(documentTypeId);
				if ((tpDocsTemplatesCollection == null)
						|| (tpDocsTemplatesCollection.toList().isEmpty())) {
					throw new ISPACInfo(// Messages.getString(
							"error.decretos.acuses.tpDocsTemplates"
					// )
					);
				}
				IItem tpDocsTemplate = (IItem) tpDocsTemplatesCollection
						.iterator().next();
				templateId = tpDocsTemplate.getInt("ID");
				//logger.warn("Obteniendo templateId:" + templateId);
			}		
			
			
			//logger.warn("iniciando creacion documento");

			try {

				connectorSession = gendocAPI.createConnectorSession();				
				
				StringBuffer consulta = new StringBuffer("");
				
				
				//logger.warn("Stage id: " + rulectx.getStageId());

				
				String numExprop = rulectx.getNumExp();
				
				String strQuery = "WHERE NUMEXP_PADRE = '" + numExp + "' AND RELACION = 'Finca/Expropiacion'";
				IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				Iterator itProp = collection.iterator();	
				
				while (itProp.hasNext()) {
				   IItem item = (IItem)itProp.next();
				   String numexpfinca = item.getString("NUMEXP_HIJO");
				   //logger.warn("Expediente de Expropiado: " + numexpfinca);	
				   
				   strQuery = "WHERE NUMEXP_PADRE = '" + numexpfinca + "' AND RELACION = 'Expropiado/Finca'";
				   IItemCollection collectionExp = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				   Iterator itPropExp = collectionExp.iterator();
				   List expExpropiados = new ArrayList();	
				   
				   while (itPropExp.hasNext()) {
					   IItem itemExp = (IItem)itPropExp.next();
					   expExpropiados.add(itemExp.getString("NUMEXP_HIJO"));
					   //logger.warn("Expediente de Expropiado: " + itemExp.getString("NUMEXP_HIJO"));			
					}
					
					//Si la lista de expropiados est谩 vac铆a devolver Desconocido
					if(expExpropiados.isEmpty()){
						return "Desconocido";
					}
					
					//Obtiene los datos de los expropiados
					Iterator itExpExpropiados = expExpropiados.iterator();
					StringBuffer strBQuery= new StringBuffer("WHERE NUMEXP = '" + itExpExpropiados.next() + "'");
					while (itExpExpropiados.hasNext()) {
						strBQuery.append(" OR NUMEXP = '" + itExpExpropiados.next() + "'");				 		
					}
					
					//logger.warn("itExpExpropiados "+strBQuery.toString());
					
					IItemCollection collectionExpropiados = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strBQuery.toString());
					
					//logger.warn("Consulta realizada");
					
					//Si la lista de expropiados est谩 vac铆a devolver Desconocido
					if(collectionExpropiados.toList().isEmpty()){
						return "Desconocido";
					}
					
					String sqlQueryPart = "WHERE NUMEXP = '" + numexpfinca + "'";
					IItemCollection fincas = entitiesAPI.queryEntities("EXPR_FINCAS",sqlQueryPart);
					
					IItem itemFinca = null;
					
					if ((fincas != null) && (fincas.toList().size() >= 1)) {
						//logger.warn("Fincas no es null");
					}
					Iterator itFinca = fincas.iterator();
					
					if(!itFinca.hasNext()) 
						logger.error("No se encontraron Fincas");
					else{			
						// Extraemos los campos necesarios para calcular la valoracion.
						itemFinca = (IItem)itFinca.next();
					}
					
					Iterator itExpropiados = collectionExpropiados.iterator();
					
					while (itExpropiados.hasNext()) {			 
						IItem participante = (IItem)itExpropiados.next();

						try {
							
							
							// Generar el documento a partir la plantilla
							// seleccionada
							
							String nombre = "";
					    	String dirnot = "";
					    	String c_postal = "";
					    	String localidad = "";
					    	String caut = "";
							
							//Datos sobre el participante
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
				        	
				        	cct.setSsVariable("NOMBRE", nombre);
				        	cct.setSsVariable("DIRNOT", dirnot);
				        	cct.setSsVariable("C_POSTAL", c_postal);
				        	cct.setSsVariable("LOCALIDAD", localidad);
				        	cct.setSsVariable("CAUT", caut);
				        	
				        	//logger.warn("abrir transaccion");	
				        	//logger.warn("nombre " + nombre);
				        	//logger.warn("dirnot " + dirnot);
							
				        	IItem entityDocumentT = gendocAPI.createTaskDocument(taskId, documentTypeId);
							int documentIdT = entityDocumentT.getKeyInt();

							IItem entityTemplateT = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentIdT, templateId);
							
							String infoPagT = entityTemplateT.getString("INFOPAG");
							int idPlantillaT = entityTemplateT.getInt("ID");
							entityTemplateT.store(cct);

							entityDocument = gendocAPI.createTaskDocument(taskId,documentTypeId);
							int documentId = entityDocument.getKeyInt();

							String sFileTemplate = getFile(gendocAPI, connectorSession,infoPagT, templateId, idPlantillaT);

							// Generar el documento a partir la plantilla
							IItem entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, taskId, documentId, templateId,sFileTemplate);

							
							// Referencia al fichero del documento en el gestor
							// documental
							String docref = entityTemplate.getString("INFOPAG");
							//logger.warn("infotag: " + docref);
							String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
							//logger.warn("mimeType: " + sMimetype);
							entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
							entityTemplate.set("DESTINO", nombre);
							
							entityTemplate.set("DESCRIPCION", "Notificaci髇 Hoja Valoraci髇 para " + nombre + "(" + participante.getString("NDOC") + ")" +
									" Poligono."+itemFinca.getString("NUM_POLIGONO")+" Parcela."+itemFinca.getString("NUM_PARCELA"));
							entityTemplate.store(cct);
							
							//logger.warn("Documento generado" );
							
							// Si todo ha sido correcto borrar las variables de la session
							cct.deleteSsVariable("NOMBRE");
							cct.deleteSsVariable("DIRNOT");
							cct.deleteSsVariable("C_POSTAL");
							cct.deleteSsVariable("LOCALIDAD");
							cct.deleteSsVariable("CAUT");	
							
							entityTemplateT.delete(cct);
							entityDocumentT.delete(cct);
							deleteFile(sFileTemplate);

						} catch (Throwable e) {

							String message = "exception.documents.generate";
							String extraInfo = null;
							if (e instanceof ISPACException) {
								if (e.getCause() instanceof NoConnectException) {
									extraInfo = "exception.extrainfo.documents.openoffice.off";
								} else if (e.getCause() instanceof DisposedException) {
									extraInfo = "exception.extrainfo.documents.openoffice.stop";
								} else if (e.getCause() != null) {
									extraInfo = e.getCause().getMessage();
								} else {
									extraInfo = ((ISPACException) e).getMessage();
								}
							} else if (e instanceof DisposedException) {
								extraInfo = "exception.extrainfo.documents.openoffice.stop";
							} else {
								extraInfo = e.getMessage();
							}
							logger.error(message + " >>" + extraInfo);
							throw new ISPACInfo(message, extraInfo, false);

						} 
					}				
						
				
				//logger.warn("Fin de ejecuci贸n regla GenerarNotificacion");
				   
				}
			} finally {
				if (connectorSession != null) {
					gendocAPI.closeConnectorSession(connectorSession);
				}
			}
				
		} catch (Exception e) {
			logger.error("Se produjo una excepci贸n", e);
			throw new ISPACRuleException(e);
		}
		return null;

	}
	
	
	private StringBuffer obtenerExpropiados(IRuleContext rulectx) throws ISPACException {
		
		StringBuffer consulta = new StringBuffer("");
		
		IClientContext cct = rulectx.getClientContext();
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		//logger.warn("Stage id: " + rulectx.getStageId());

		
		String numExprop = rulectx.getNumExp();
		
		//String strQueryF = "WHERE NUMEXP_PADRE = '" + numExprop + "' AND RELACION = 'Finca/Expropiacion'";
		String strQueryF = "SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE IN (select numexp_hijo from spac_exp_relacionados " +
				"WHERE NUMEXP_PADRE = '"+numExprop+"' AND RELACION = 'Finca/Expropiacion') AND RELACION = 'Expropiado/Finca' order by numexp_padre";
		
		//logger.warn("strQueryF "+strQueryF);
		ResultSet expropiados = cct.getConnection().executeQuery(strQueryF).getResultSet();
		
		try {
			if(expropiados.next()){
				consulta.append("WHERE NUMEXP = '" + expropiados.getString("NUMEXP_HIJO") + "'");
				while(expropiados.next()){
					if (expropiados.getString("NUMEXP_HIJO")!=null){
						consulta.append(" OR NUMEXP = '" + expropiados.getString("NUMEXP_HIJO") + "'"); 
					}
				}
			}
		} catch (SQLException e) {
			throw new ISPACRuleException(
					"Error SQL.", e);
		}
      	
		//logger.warn("consulta.toString() "+consulta.toString());
		
		return consulta;
	}

	private String getFile(IGenDocAPI gendocAPI, Object connectorSession,
			String infoPag, int templateId, int idPlantilla)
			throws ISPACException {
		try {
			connectorSession = gendocAPI.createConnectorSession();
			File file = null;
			try {
				String extension = MimetypeMapping.getExtension(gendocAPI
						.getMimeType(connectorSession, infoPag));

				FileTemplateManager templateManager = null;
				// Obtiene el manejador de plantillas
				templateManager = (FileTemplateManager) FileTemplateManager
						.getInstance();

				// Se almacena documento
				String fileName = FileTemporaryManager.getInstance()
						.newFileName("." + extension);
				String fileNamePath = templateManager.getFileMgrPath() + "/"
						+ fileName;

				// Nombre de la plantilla
				String sName = Integer.toString(templateId) + "." + extension;

				// Control de plantillas por multientidad
				OrganizationUserInfo info = OrganizationUser
						.getOrganizationUserInfo();
				if (info != null) {
					String organizationId = info.getOrganizationId();
					// Se a馻de el numExp al nombre de la plantilla para evitar
					// colisiones al generar notificaciones simultaneamente
					// desde
					// dos expedientes distintos de la misma entidad
					sName = organizationId + "_" + idPlantilla + "_" + sName;
				}

				OutputStream out = new FileOutputStream(fileNamePath);
				gendocAPI.getDocument(connectorSession, infoPag, out);
				file = new File(fileNamePath);
				File file2 = new File(templateManager.getFileMgrPath() + "/"
						+ sName);
				file.renameTo(file2);
				file.delete();
				
				if(out!= null) out.close();

				OutputStream out2 = new FileOutputStream(fileNamePath);
				gendocAPI.getDocument(connectorSession, infoPag, out2);
				File file3 = new File(fileNamePath);
				File file4 = new File(FileTemporaryManager.getInstance()
						.getFileTemporaryPath()
						+ "/" + sName);
				file3.renameTo(file4);
				file3.delete();

				if(out2 != null) out2.close();
				return sName;
			} catch (FileNotFoundException e) {
				throw new ISPACRuleException(
						"Error al intentar obtener el documento, no existe.", e);
			} catch (IOException e) {
				throw new ISPACRuleException(
						"Error al cerrar Input/OutputStream.", e);
			}
		} finally {
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
		
		templateManager = (FileTemplateManager) FileTemplateManager.getInstance();
		
		boolean resultado = true;
		File fTemplate = new File(templateManager.getFileMgrPath() + "/" + sFileTemplate);
		File fTemporary = new File(FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
		
		if (fTemplate!= null && fTemplate.exists() && !fTemplate.delete()){
			logger.error ("No se pudo eliminar el documento: "+templateManager.getFileMgrPath() + "/" + sFileTemplate);
			resultado = false;
		}
		
		if (fTemporary!=null && fTemporary.exists() && !fTemporary.delete()){
			logger.error("No se pudo eliminar el documento: "+FileTemporaryManager.getInstance().getFileTemporaryPath() + "/" + sFileTemplate);
			resultado = false;
		}
		
		return resultado;
	} 
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		// TODO Auto-generated method stub
		return true;
	}

}

