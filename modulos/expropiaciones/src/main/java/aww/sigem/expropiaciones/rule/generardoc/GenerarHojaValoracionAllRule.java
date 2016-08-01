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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.PropietariosUtil;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

/**
 * Generar múltiples documentos a partir de una plantilla de prueba.
 */
public class GenerarHojaValoracionAllRule implements IRule {

	/** Logger de la clase. */
	private static final Logger logger = Logger
			.getLogger(GenerarActasPreviasPorPropietarioRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla GenerarHojaValoracionPorPropietarioRule");

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
			
			String numExprop = rulectx.getNumExp();
			
			String strQueryF = "WHERE NUMEXP_PADRE = '" + numExprop + "' AND RELACION = 'Finca/Expropiacion'";
			IItemCollection collectionF = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQueryF);
			
			Iterator itF = collectionF.iterator();
			IItem itemF = null;	
			
			
			while (itF.hasNext()) {
				itemF = (IItem)itF.next();
				String numExp = itemF.getString("NUMEXP_HIJO");
				//logger.warn("NumEXP:" + numExp);
				int documentId = 0;
				Object connectorSession = null;
				
				/* */
				String sqlQueryPart = "WHERE NUMEXP = '" + numExp + "'";
				IItemCollection fincas = entitiesAPI.queryEntities("EXPR_FINCAS",sqlQueryPart);
				
				if ((fincas != null) && (fincas.toList().size() >= 1)) {
					//logger.warn("Fincas no es null");
				}
				Iterator itFinca = fincas.iterator();
				
				if(!itFinca.hasNext()) 
					logger.error("No se encontraron Fincas");
				else{			
					// Extraemos los campos necesarios para calcular la valoracion.
					IItem itemFinca = (IItem)itFinca.next();

					IItemCollection taskTpDocCollection = procedureAPI.getTaskTpDoc(idTramCtl);
	
					//logger.warn("idTramCtl: " + idTramCtl);
					
					if ((taskTpDocCollection == null) || (taskTpDocCollection.toList().isEmpty())) {
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
						if (taskTpDoc.get("CT_TPDOC:NOMBRE").equals(
						// "Hoja Valoracion"
								"EXPR-014 - Hoja Valoracion")) {
							documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
							//logger.warn("Obteniendo documentTypeId: " + documentTypeId);
						} else {
							//logger.warn("No se puede obtener documentTypeId: " + documentTypeId);
						}
	
					}
					
					if (documentTypeId != 0) {
						//logger.warn("Obteniendo plantilla ");
						IItemCollection tpDocsTemplatesCollection = procedureAPI.getTpDocsTemplates(documentTypeId);
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
					
					
					String sqlPago = "WHERE NUMEXP = '" + numExp + "'";
					IItemCollection expropiadosFinca = entitiesAPI.queryEntities("EXPR_FINCA_EXPROPIADO_PAGO",
							sqlPago);
					
					
					//logger.warn("iniciando creacion documento");
	
					try {
	
						connectorSession = gendocAPI.createConnectorSession();				
						
						//Buscar los expedientes de los Expropiados
						//logger.warn("Expediente de Finca: " + numExp);
						String strQuery = "WHERE NUMEXP_PADRE = '" + numExp + "' AND RELACION = 'Expropiado/Finca'";
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
							String expropiados = "";
							String valoracion = "";
							
							String numExpTit = item.getString("NUMEXP");
							
							Iterator itPago = expropiadosFinca.iterator();
							IItem itemPago = null;					
							
							while (itPago.hasNext()){
								itemPago = (IItem)itPago.next();
								String exp = itemPago.getString("NUMEXP_EXPROPIADO");
								//logger.warn("Expediente: " + exp);	
								
								if (exp.equals(numExpTit)){
									valoracion = itemPago.getString("CANTIDADPAGO");
									//logger.warn("Valoracion: " + valoracion);	
								}
							}
							
							expropiados+=item.getString("NOMBRE");
							//Descomentar esto para que salgan los datos completos de los expropiados
							
							expropiados+=" ";
							expropiados+=item.getString("DIRNOT");
							expropiados+=" ";
							expropiados+=item.getString("LOCALIDAD");
							expropiados+=" (";
							expropiados+=item.getString("C_POSTAL");
							expropiados+=") ";
							expropiados+=item.getString("CAUT");
									
							int currentId = taskId;
							//logger.warn("currentId " + currentId);
							// Ejecución en un contexto transaccional
							boolean bCommit = false;
	
							try {
								//logger.warn("abrir transaccion" + numExpTit);
								// Abrir transacción para que no se pueda generar un
								// documento sin fichero
								cct.beginTX();
								
								cct.setSsVariable("NUMEXP", numExp);
								
								String claseFinca = itemFinca.getString("APROVECHAMIENTO");
								cct.setSsVariable("CLASE_FINCA", claseFinca);
								
								String municipio = itemFinca.getString("MUNICIPIO");
								cct.setSsVariable("MUNICIPIO", municipio);
								
								String numPoligono = itemFinca.getString("NUM_POLIGONO");
								cct.setSsVariable("NUM_POLIGONO", numPoligono);
								
								String numParcela = itemFinca.getString("NUM_PARCELA");
								cct.setSsVariable("NUM_PARCELA", numParcela);
								
								String catasMira = itemFinca.getString("CATAS_AMIRA");
								cct.setSsVariable("CATAS_AMIRA", catasMira);
								
								String supParcela = itemFinca.getString("SUP_PARCELA");
								cct.setSsVariable("SUP_PARCELA", supParcela);
								
								String supExpropiada = itemFinca.getString("SUP_EXPROPIADA");
								cct.setSsVariable("SUP_EXPROPIADA", supExpropiada);
								
								String importe = itemFinca.getString("IMPORTE");
								cct.setSsVariable("IMPORTE", importe);
	
								cct.setSsVariable("EXPROPIADO", expropiados);
								
								cct.setSsVariable("CANTIDAD", valoracion);
								
								//logger.warn("numexp "+numExp+" numExpTit "+numExpTit);
								String metros = ""+PropietariosUtil.getMetrosPorPropietario(entitiesAPI, numExp, numExpTit);
								//logger.warn("metros. "+metros);
								
								cct.setSsVariable("METROS", ""+metros);
								
								
								// Crear el documento
								//logger.warn("crear el documento (createTaskDocument) " + numExpTit);
								entityDocument = gendocAPI.createTaskDocument(currentId, documentTypeId);
								
								documentId = entityDocument.getKeyInt();
								//logger.warn("documentId " + documentId);
								
								// Generar el documento a partir la plantilla
								// seleccionada
								//logger.warn("generar documento a partir de plantilla " + numExpTit);
								IItem entityTemplate = null;
								entityTemplate = gendocAPI.attachTaskTemplate(
										connectorSession, currentId, documentId,
										templateId);
								
								// Referencia al fichero del documento en el gestor
								// documental
								String docref = entityTemplate.getString("INFOPAG");
								//logger.warn("infotag: " + docref);
								String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
								//logger.warn("mimeType: " + sMimetype);
								entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
								//entityTemplate.set("DESCRIPCION", municipio +" "+ item.getString("NOMBRE")+" "+numParcela+" "+numPoligono);
								entityTemplate.set("DESCRIPCION", municipio +" "+ item.getString("NOMBRE"));
								
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
	
								cct.deleteSsVariable("EXPROPIADO");
								cct.deleteSsVariable("CANTIDAD");
								cct.deleteSsVariable("METROS");
								cct.deleteSsVariable("NUMEXP");
								cct.deleteSsVariable("CLASE_FINCA");
								cct.deleteSsVariable("MUNICIPIO");
								cct.deleteSsVariable("NUM_POLIGONO");
								cct.deleteSsVariable("NUM_PARCELA");
								cct.deleteSsVariable("CATAS_AMIRA");
								cct.deleteSsVariable("SUP_PARCELA");
								cct.deleteSsVariable("SUP_EXPROPIADA");
								cct.deleteSsVariable("IMPORTE");
	
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
	
							} finally {
								cct.endTX(bCommit);
							}
						}
							
							
					} finally {
						if (connectorSession != null) {
							gendocAPI.closeConnectorSession(connectorSession);
						}
					}
				}
			
				
			}			
			
			
				//logger.warn("Fin de ejecución regla GenerarHojaValoracionPorPropietarioRule");
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}



}
