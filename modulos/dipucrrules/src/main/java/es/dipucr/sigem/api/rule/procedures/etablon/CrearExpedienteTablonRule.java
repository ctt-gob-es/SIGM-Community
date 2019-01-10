package es.dipucr.sigem.api.rule.procedures.etablon;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sun.star.connection.NoConnectException;
import com.sun.star.lang.DisposedException;

/**
 * [eCenpri-Felipe #504]
 * Regla que inicia el procedimiento integral
 * @author Felipe
 */
public class CrearExpedienteTablonRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(CrearExpedienteTablonRule.class);
	
	/**
	 * Constantes
	 */
	public static final String _COD_PCD_TABLON = "PCD-ETABLON";
	public static final String _COD_TRAM_PUB_TABLON = "ETABLON_PUB";
	protected static final String _DOC_PUBLICACION = "eTablon - Publicación";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	/**
	 * Crea el expediente de "eTablón - Publicación"
	 * Crea su primer trámite
	 * Copia el documento de publicación
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			//Creación de las APIs
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
			//*********************************************
			
			//Variables comunes
			String numexpActual = rulectx.getNumExp();
			IItemCollection iCollection = null;
			
			//Recuperamos el id de procedimiento
	        String strQuery = "WHERE COD_PCD = '" + _COD_PCD_TABLON + "'";
	        iCollection = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
	        IItem itemProc = (IItem)iCollection.iterator().next();
	        int idProc = itemProc.getInt("ID");
	        
	        //Creamos el expediente
	        Map params = new HashMap();
			params.put("COD_PCD", _COD_PCD_TABLON);
	        int idExpediente = tx.createProcess(idProc, params);
			IProcess process = invesFlowAPI.getProcess(idExpediente);
			String numexpNuevo = process.getString("NUMEXP");
			
			//TODO: Ponemos el asunto al expediente nuevo
//			IItem itemExpedienteActual = ExpedientesUtil.getExpedientConHistorico(cct, numexpActual);
//			IItem itemExpedienteNuevo = ExpedientesUtil.getExpedientConHistorico(cct, numexpNuevo);
//			itemExpedienteNuevo.set("ASUNTO", itemExpedienteActual.getString("ASUNTO"));
//			itemExpedienteNuevo.store(cct);
			
//			//Recuperamos los parámetros de búsqueda y clonamos los datos de la publicación
//			iCollection = entitiesAPI.getEntities("ETABLON_PUBLICACION", numexpActual);
//			IItem itemTablonActual = (IItem) iCollection.iterator().next();
////			IItem itemDefEntidad = entitiesAPI.getCatalogEntity("ETABLON_PUBLICACION");
//			String [] camposNoClonar = {"ID","NUMEXP"};
//			String [] camposAClonar = {"TITULO","DESCRIPCION","COD_CATEGORIA","CATEGORIA",
//					"COD_SERVICIO","SERVICIO","FECHA_INI_VIGENCIA","FECHA_FIN_VIGENCIA",
//					"SERVICIO_OTROS","CATEGORIA_OTROS"};
////			entitiesAPI.cloneRegEntity(itemDefEntidad.getKeyInt(), 
////					itemTablonActual.getKeyInt(), numexpNuevo, idCamposAClonar);
//			IItem itemTablonNuevo = entitiesAPI.createEntity("ETABLON_PUBLICACION", numexpNuevo);
//			entitiesAPI.copyRegEntityData(itemTablonActual, itemTablonNuevo,
//					Arrays.asList(camposNoClonar), Arrays.asList(camposAClonar));
//			itemTablonNuevo.store(cct);
			
			//Lo marcamos como expediente relacionado
			//El procedimiento de eTablón será hijo del procedimiento integral
			IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
			registro.set("NUMEXP_PADRE", rulectx.getNumExp());
			registro.set("NUMEXP_HIJO", numexpNuevo);
			registro.set("RELACION", "Publicación eTablón");
			registro.store(cct);
//			
//			//Obtenemos el id de la fase del nuevo expediente
//			iCollection = invesFlowAPI.getStagesProcess(idExpediente);
//			IItem itemStage = (IItem) iCollection.iterator().next();
//			int idStage = itemStage.getInt("ID");
//			int idStagePcd = itemStage.getInt("ID_FASE");
//			
//			//Obtenemos el id del trámite de "Seleccion del libro de decretos" en el procedimiento, no en el catálogo
//	        strQuery = "WHERE ID_FASE = '"+ idStagePcd +"' AND ID_CTTRAMITE = "
//	         + "(SELECT ID FROM SPAC_CT_TRAMITES WHERE COD_TRAM = '"+ _COD_TRAM_PUB_TABLON +"')";
//	        iCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
//	        int idTaskPcd = ((IItem)iCollection.iterator().next()).getInt("ID");
//	        
//	        //Creamos el trámite
//			int idTask = tx.createTask(idStage, idTaskPcd);
//			ITask task = invesFlowAPI.getTask(idTask);
//			
//			//Obtenemos el tipo de documento
//			int idTramCtl = task.getInt("ID_CTTRAMITE");
//	    	iCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
//	    	IItem taskTpDoc = (IItem)iCollection.iterator().next();
//	    	int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
//			
//			//Creamos el documento desde plantilla y obtenemos el file
//			IItem itemDocPublicacion = crearDocumentoTramite(cct, entitiesAPI, genDocAPI, idTask, documentTypeId);
//			int documentIdNuevo = itemDocPublicacion.getKeyInt();
//			String strInfoPagNuevo = itemDocPublicacion.getString("INFOPAG");
//			File file = DipucrCommonFunctions.getFile(genDocAPI, strInfoPagNuevo);
//			file.delete();   		  		
//			
//			//Obtenemos el libro actual y sus propiedades
//			IItem itemDocumentActual = DipucrCommonFunctions.getItemDocument(rulectx, _DOC_PUBLICACION);
//			String strInfoPagActual = itemDocumentActual.getString("INFOPAG");
//			File fileLibro = DipucrCommonFunctions.getFile(genDocAPI, strInfoPagActual);
//			
//			//Lo volvemos a adjuntar
//			FileInputStream fis = new FileInputStream(fileLibro);
//			Object connectorSession = genDocAPI.createConnectorSession();
//			itemDocPublicacion = genDocAPI.attachTaskInputStream(connectorSession, idTask,
//					documentIdNuevo, fis, (int)fileLibro.length(), Constants._MIMETYPE_PDF, _DOC_PUBLICACION);
//			itemDocPublicacion.set("EXTENSION", Constants._EXTENSION_PDF);
//			itemDocPublicacion.store(cct);
//    		fis.close();
//    		if (null != connectorSession)
//    			genDocAPI.closeConnectorSession(connectorSession);
	        
		} catch (ISPACException e) { 
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		} catch (Exception e) {
        	logger.error("Error en el expediente: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Crea documento en un trámite concreto
	 * @param cct
	 * @param entitiesAPI
	 * @param genDocAPI
	 * @param taskId
	 * @param documentTypeId
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("unused")
	private IItem crearDocumentoTramite(IClientContext cct, IEntitiesAPI entitiesAPI, 
			IGenDocAPI genDocAPI, int taskId, int documentTypeId) throws ISPACException
	{
		Object connectorSession = null;
		IItem itemDocument = null;
		int documentId = Integer.MIN_VALUE;
		try{
			connectorSession = genDocAPI.createConnectorSession();
			// Abrir transacción para que no se pueda generar un documento sin fichero
	        cct.beginTX();
	        
        	itemDocument = genDocAPI.createTaskDocument(taskId, documentTypeId);
			documentId = itemDocument.getKeyInt();

			// Generar el documento a partir de la plantilla
			IItem entityTemplate = genDocAPI.attachTaskTemplate
				(connectorSession, taskId, documentId, documentTypeId);
			entityTemplate.set("EXTENSION", "odt");
			String templateDescripcion = entityTemplate.getString("DESCRIPCION");

			entityTemplate.set("DESCRIPCION", templateDescripcion);
			entityTemplate.store(cct);
		}
		catch (Exception e){
			// Si se produce algún error se hace rollback de la transacción
			cct.endTX(false);
			
			String message = "exception.documents.generate";
			String extraInfo = null;
			Throwable eCause = e.getCause();
			
			if (eCause instanceof ISPACException){
				if (eCause.getCause() instanceof NoConnectException){
					extraInfo = "exception.extrainfo.documents.openoffice.off"; 
				}
				else{
					extraInfo = eCause.getCause().getMessage();
				}
			}
			else if (eCause instanceof DisposedException){
				extraInfo = "exception.extrainfo.documents.openoffice.stop";
			}
			else{
				extraInfo = e.getMessage();
			}			
			throw new ISPACInfo(message, extraInfo);
		}
		finally{
			if (connectorSession != null){
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
		cct.endTX(true);
//		return documentId;
		return itemDocument;
	}
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
}