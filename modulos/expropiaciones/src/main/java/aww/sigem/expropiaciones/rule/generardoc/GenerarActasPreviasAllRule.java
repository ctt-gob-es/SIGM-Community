package aww.sigem.expropiaciones.rule.generardoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import aww.sigem.expropiaciones.util.FicherosUtil;
import aww.sigem.expropiaciones.util.FuncionesUtil;
import aww.sigem.expropiaciones.util.PropietariosUtil;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

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

public class GenerarActasPreviasAllRule implements IRule{


	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(GenerarActasPreviasAllRule.class);
	
	private static final String plantilla = "EXPR-012 - Acta previa";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			//logger.warn("Ejecutando regla GenerarActasPreviasAllRule");

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
			String numExpExpropiacion = rulectx.getNumExp();
			//logger.warn("NumEXP:" + numExpExpropiacion);
			int documentId = 0;
			Object connectorSession = null;

					
			//logger.warn("iniciando creacion documento");

			try {

				connectorSession = gendocAPI.createConnectorSession();	
				
				//Buscar los expedientes de los Expropiados
				//logger.warn("Expediente de expropiacion: " + numExpExpropiacion);
				String strQuery = "WHERE NUMEXP_PADRE = '" + numExpExpropiacion + "' AND RELACION = 'Finca/Expropiacion'";
				
				IItemCollection collection = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
				Iterator itProp = collection.iterator();
				IItem item = null;	
				List expFincas = new ArrayList();		
				
				while (itProp.hasNext()) {
				   item = (IItem)itProp.next();
				   expFincas.add(item.getString("NUMEXP_HIJO"));
				   //logger.warn("Expediente de Finca: " + item.getString("NUMEXP_HIJO"));			
				}
				
				//Si la lista de expropiados está vacía devolver Desconocido
				if(expFincas.isEmpty()){
					return "Desconocido";
				}
				
				//Obtiene los datos de las fincas
				Iterator itExpFincas = expFincas.iterator();
				String strQueryFincas="WHERE NUMEXP = '" + itExpFincas.next() + "'";
				while (itExpFincas.hasNext()) {
					strQueryFincas+=" OR NUMEXP = '" + itExpFincas.next() + "'";				 		
				}			
				
				//logger.warn("Fincas a buscar: " + strQueryFincas);	
				
				IItemCollection collectionFincas = entitiesAPI.queryEntities("EXPR_FINCAS", strQueryFincas);
				
				//logger.warn("Consulta realizada");
				
				//Si la lista de fincas está vacía devolver Desconocido
				if(collectionFincas.toList().isEmpty()){
					return "Desconocido";
				}
				
				Iterator itFincas = collectionFincas.iterator();
				
				IItem itemFinca = null;	
				
				int identificadores [] = FicherosUtil.obtenerIdPlantilla(procedureAPI, entitiesAPI, idTramCtl, plantilla);			
								
				while (itFincas.hasNext()) {			 
					itemFinca = (IItem)itFincas.next();
					int currentId = taskId;
					//logger.warn("currentId " + currentId);
					// Ejecución en un contexto transaccional
					boolean bCommit = false;

					try {
						
						//logger.warn("Expediente de finca: " + itemFinca.getString("NUMEXP"));
						
						String numFinca = itemFinca.getString("NUM_FINCA");
						cct.setSsVariable("NUM_FINCA", numFinca);
						
						String numExpFinca = itemFinca.getString("NUMEXP");						
						cct.setSsVariable("NUMEXP", numExpFinca);						
						
						String municipio = itemFinca.getString("MUNICIPIO");
						cct.setSsVariable("MUNICIPIO", municipio);			
						
						String parcela = itemFinca.getString("NUM_PARCELA");
						cct.setSsVariable("NUM_PARCELA", parcela);
						logger.warn("parcela "+parcela);
						
						String poligono = itemFinca.getString("NUM_POLIGONO");
						cct.setSsVariable("NUM_POLIGONO", poligono);
						logger.warn("poligono "+poligono);
						
						String claseFinca = itemFinca.getString("CLASE_FINCA");
						cct.setSsVariable("CLASE_FINCA", claseFinca);
						
						String aprovechamiento = itemFinca.getString("APROVECHAMIENTO");
						cct.setSsVariable("APROVECHAMIENTO", aprovechamiento);
						logger.warn("aprovechamiento "+aprovechamiento);
						
						String supParcela = itemFinca.getString("SUP_PARCELA");
						cct.setSsVariable("SUP_PARCELA", supParcela);
						logger.warn("supParcela "+supParcela);
						
						String baseFiscal = itemFinca.getString("BASE_FISCAL");
						cct.setSsVariable("BASE_FISCAL", baseFiscal);
						
						String otrosDerechos = itemFinca.getString("OTROS_DERECHOS");
						cct.setSsVariable("OTROS_DERECHOS", otrosDerechos);
						
						String descripcion = itemFinca.getString("DESCRIPCION");
						cct.setSsVariable("DESCRIPCION", descripcion);					
						
						documentTypeId = identificadores[0];
						templateId = identificadores[1];						
						
						/*
						 *  Se debe generar un acta de ocupación por cada expropiado existente en una finca.
						 */												
						
						String strQueryExpropiados = "WHERE NUMEXP_PADRE = '" + numExpFinca + "' AND RELACION = 'Expropiado/Finca'";
						
						IItemCollection collectionExpropiados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQueryExpropiados);
						Iterator itExpropiado = collectionExpropiados.iterator();
						IItem itemExpropiado = null;	
						List expExpropiados = new ArrayList();		
						
						while (itExpropiado.hasNext()) {
							itemExpropiado = (IItem)itExpropiado.next();
							expExpropiados.add(itemExpropiado.getString("NUMEXP_HIJO"));
							//logger.warn("Expediente de Expropiado: " + itemExpropiado.getString("NUMEXP_HIJO"));			
						}
						
						//Si la lista de expropiados está vacía devolver Desconocido
						if(expExpropiados.isEmpty()){
							return "Desconocido";
						}
						
						//Obtiene los datos de las fincas
						Iterator itExpExpropiado = expExpropiados.iterator();						
									
						String strQueryExpExpropiados="WHERE NUMEXP = '" + itExpExpropiado.next() + "'";
						while (itExpExpropiado.hasNext()) {
							strQueryExpExpropiados+=" OR NUMEXP = '" + itExpExpropiado.next() + "'";				 		
						}			
					
						//logger.warn("Expropiados a buscar: " + strQueryExpExpropiados);	
						
						IItemCollection collectionExpExpropiados = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQueryExpExpropiados);
						
						//logger.warn("Consulta realizada");
						
						//Si la lista de fincas está vacía devolver Desconocido
						if(collectionExpExpropiados.toList().isEmpty()){
							return "Desconocido";
						}
						
						Iterator itExpropiados = collectionExpExpropiados.iterator();
						
						itemExpropiado = null;	
						
						while (itExpropiados.hasNext()) {
							
							itemExpropiado = (IItem) itExpropiados.next();							
							
							String expropiado = "";			
							String numExpTit = itemExpropiado.getString("NUMEXP");
							
							//Datos completos de los expropiados							
							expropiado+=itemExpropiado.getString("NOMBRE");
							
							expropiado+=" ";
							expropiado+=itemExpropiado.getString("DIRNOT");
							expropiado+=" ";
							expropiado+=itemExpropiado.getString("LOCALIDAD");
							expropiado+=" (";
							expropiado+=itemExpropiado.getString("C_POSTAL");
							expropiado+=") ";
							expropiado+=itemExpropiado.getString("CAUT");
							
							cct.setSsVariable("EXPROPIADO", expropiado);	
							logger.warn("expropiado "+expropiado);
							cct.setSsVariable("METROS", ""+FuncionesUtil.imprimirDecimales(PropietariosUtil.getMetrosPorPropietario(entitiesAPI, numExpFinca, numExpTit)));
							logger.warn("nombre "+itemExpropiado.getString("NOMBRE"));
														
							//logger.warn("abrir transaccion");						
							
							entityDocument = gendocAPI.createTaskDocument(currentId, documentTypeId);
							
							documentId = entityDocument.getKeyInt();
							//logger.warn("documentId " + documentId);						
							
							// Generar el documento a partir la plantilla
							// seleccionada
							
							IItem entityTemplate = null;
							entityTemplate = gendocAPI.attachTaskTemplate(connectorSession, currentId, documentId, templateId);
							
							// Referencia al fichero del documento en el gestor
							// documental
							String docref = entityTemplate.getString("INFOPAG");
							//logger.warn("infotag: " + docref);
							String sMimetype = gendocAPI.getMimeType(connectorSession, docref);
							//logger.warn("mimeType: " + sMimetype);
							entityTemplate.set("EXTENSION", MimetypeMapping.getExtension(sMimetype));
							entityTemplate.set("DESCRIPCION", "Acta de ocupación para " + itemExpropiado.getString("NOMBRE") + "(" + itemExpropiado.getString("NDOC") + "). " +
									"Finca: Parcela " + parcela + ", Poligono " + poligono + ", Municipio " + municipio);
							
							//[Ticket #381# Teresa INICIO]
							String  id_ext = "";
							if ((String)itemExpropiado.getString("ID_EXT")!=null) id_ext = (String)itemExpropiado.getString("ID_EXT");
							entityTemplate.set("DESTINO_ID", id_ext);
							entityTemplate.set("DESTINO", itemExpropiado.getString("NOMBRE"));
							//[Ticket #381# Teresa FIN]
							entityTemplate.store(cct);
							
							//logger.warn("Documento generado" );
							// Si todo ha sido correcto se hace commit de la
							// transacción
							bCommit = true;		
							
						}	
						cct.deleteSsVariable("NUM_FINCA");
						cct.deleteSsVariable("NUMEXP");
						cct.deleteSsVariable("MUNICIPIO");
						cct.deleteSsVariable("EXPROPIADO");
						cct.deleteSsVariable("NUM_PARCELA");
						cct.deleteSsVariable("NUM_POLIGONO");
						cct.deleteSsVariable("CLASE_FINCA");
						cct.deleteSsVariable("APROVECHAMIENTO");
						cct.deleteSsVariable("SUP_PARCELA");
						cct.deleteSsVariable("BASE_FISCAL");
						cct.deleteSsVariable("OTROS_DERECHOS");
						cct.deleteSsVariable("METROS");
						cct.deleteSsVariable("DESCRIPCION");				

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
			//logger.warn("Fin de ejecución regla GenerarActasPreviasAllRule");
		} catch (Exception e) {
			logger.error("Se produjo una excepción", e);
			throw new ISPACRuleException(e);
		}
		return null;

	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
