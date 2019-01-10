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
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.ConsignaTransferenciaUtil;
import aww.sigem.expropiaciones.util.FicherosUtil;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

public class GenerarOficiosPagoAllRule implements IRule {

	
	private static final Logger logger = Logger.getLogger(GenerarOficiosPagoAllRule.class);
	
	private static final String plantillaConsigna = "EXPR-020 - Oficio de consigna y fecha ocupacion";
	private static final String plantillaTransferencia = "EXPR-021 - Oficio de transferencia y fecha ocupacion";

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
			
			int templateId = 0;
			int documentTypeId = 0;
			
			int taskId = rulectx.getTaskId();
			
			//logger.warn("Stage id: " + rulectx.getStageId());

			IItem processTask = entitiesAPI.getTask(rulectx.getTaskId());
			int idTramCtl = processTask.getInt("ID_TRAM_CTL");
			String numExp = rulectx.getNumExp();
			//logger.warn("NumEXP:" + numExp);
			int documentId = 0;
			Object connectorSession = null;

					
			//logger.warn("iniciando creacion documento");

			try {

				connectorSession = gendocAPI.createConnectorSession();				
				
				//Buscar los expedientes de los Expropiados
				//logger.warn("Expediente de expropiacion: " + numExp);
				String strQuery = "WHERE NUMEXP_PADRE = '" + numExp + "' AND RELACION = 'Expropiado/Expropiacion'";
				
				IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				Iterator itProp = collection.iterator();
				IItem item = null;	
				List expExpropiados = new ArrayList();		
				
				while (itProp.hasNext()) {
				   item = (IItem)itProp.next();
				   expExpropiados.add(item.getString("NUMEXP_HIJO"));
				   //logger.warn("Expediente de Expropiado: " + item.getString("NUMEXP_HIJO"));			
				}
				
				//Si la lista de expropiados está vacía devolver Desconocido
				if(expExpropiados.isEmpty()){
					return "Desconocido";
				}
				
				//Obtiene los datos de los expropiados
				Iterator itExpExpropiados = expExpropiados.iterator();
				strQuery="WHERE NUMEXP = '" + itExpExpropiados.next() + "'";
				while (itExpExpropiados.hasNext()) {
					strQuery+=" OR NUMEXP = '" + itExpExpropiados.next() + "'";				 		
				}			
				
				//logger.warn("Expropiados a buscar: " + strQuery);	
				
				IItemCollection collectionExpropiados = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
				
				//logger.warn("Consulta realizada");
				
				//Si la lista de expropiados está vacía devolver Desconocido
				if(collectionExpropiados.toList().isEmpty()){
					return "Desconocido";
				}
				
				Iterator itExpropiados = collectionExpropiados.iterator();
				
				while (itExpropiados.hasNext()) {			 
					item = (IItem)itExpropiados.next();
					int currentId = taskId;
					//logger.warn("currentId " + currentId);
					// Ejecución en un contexto transaccional
					boolean bCommit = false;

					try {
						cct.setSsVariable("NUMEXP", item.getString("NUMEXP"));
						
						String plantilla = "";
						if (ConsignaTransferenciaUtil.isConsigna(entitiesAPI, item.getString("NUMEXP"))){
							plantilla = plantillaConsigna;
						}
						else{
							plantilla = plantillaTransferencia;
						}
						
						int identificadores [] = FicherosUtil.obtenerIdPlantilla(procedureAPI, entitiesAPI, idTramCtl, plantilla);
						
						documentTypeId = identificadores[0];
						templateId = identificadores[1];
						
						//logger.warn("abrir transaccion");						
						
						entityDocument = gendocAPI.createTaskDocument(
								currentId, documentTypeId);
						
						documentId = entityDocument.getKeyInt();
						//logger.warn("documentId " + documentId);
						
						// Generar el documento a partir la plantilla
						// seleccionada
						
						IItem entityTemplate = null;
						entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, currentId, documentId,
								templateId);
						
						// Referencia al fichero del documento en el gestor
						// documental
						String docref = entityTemplate.getString("INFOPAG");
						//logger.warn("infotag: " + docref);
						String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
						//logger.warn("mimeType: " + sMimetype);
						entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
						entityTemplate.set("DESCRIPCION", "Oficio para " + item.getString("NOMBRE") + "(" + item.getString("NDOC") + ")");
						
						//[Ticket #381# Teresa INICIO]
						String  id_ext = "";
						if ((String)item.getString("ID_EXT")!=null) id_ext = (String)item.getString("ID_EXT");
						entityTemplate.set("DESTINO_ID", id_ext);
						entityTemplate.set("DESTINO", item.getString("NOMBRE"));
						//[Ticket #381# Teresa FIN]
						
						entityTemplate.store(cct);
						
						//logger.warn("Documento generado" );
						// Si todo ha sido correcto se hace commit de la
						// transacción
						bCommit = true;	
						
						cct.deleteSsVariable("NUMEXP");

					} catch (Exception e) {

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

					} finally {
						cct.endTX(bCommit);
					}
				}				
					
			} finally {
				if (connectorSession != null) {
					gendocAPI.closeConnectorSession(connectorSession);
				}
			}
			//logger.warn("Fin de ejecución regla GenerarNotificacion");
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return null;

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
