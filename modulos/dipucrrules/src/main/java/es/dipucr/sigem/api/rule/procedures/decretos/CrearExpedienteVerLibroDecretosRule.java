package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dipucr.sigem.api.rule.common.decretos.DecretosConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.PdfUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * [eCenpri-Felipe ticket #164]
 * Clase para la creación de un nuevo expediente con un libro protegido para
 * la visualización de este por parte del grupo PP
 * @author Felipe
 * @since 03.11.2010
 */
public class CrearExpedienteVerLibroDecretosRule implements IRule 
{
	private static final String _DOC_LIBRO_DECRETOS = Constants.DECRETOS._DOC_LIBRO_DECRETOS;
	private static final String _EXPEDIENTE_VISUALIZACION = "Libro de Decretos visualización";
	private static final String _COD_TRAM_SELECCION_LIBRO = "LIBRO_DECRETOS";
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	/**
	 * Generación del libro de decretos
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		try{
			//*********************************************
			IClientContext cct = rulectx.getClientContext();
	  	    IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			ITXTransaction tx = invesFlowAPI.getTransactionAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
			//*********************************************
			
			String numexpActual = rulectx.getNumExp();
			IItemCollection iCollection = null;
			
			//Recuperamos el procedimiento
	        String strQuery = "WHERE UPPER(NOMBRE) LIKE UPPER('" + _EXPEDIENTE_VISUALIZACION + "')";
	        iCollection = entitiesAPI.queryEntities("SPAC_CT_PROCEDIMIENTOS", strQuery);
	        IItem itemProc = (IItem)iCollection.iterator().next();
	        String codProc = itemProc.getString("COD_PCD");
	        int idProc = itemProc.getInt("ID");
	        
	        //Creamos el expediente
	        Map params = new HashMap();
			params.put("COD_PCD", codProc);
			
	        int idExpediente = tx.createProcess(idProc, params);
			IProcess process = invesFlowAPI.getProcess(idExpediente);
			String numexpNuevo = process.getString("NUMEXP");
			
			//Ponemos el asunto al expediente nuevo
			IItem itemExpedienteActual = ExpedientesUtil.getExpediente(cct, numexpActual);
			IItem itemExpedienteNuevo = ExpedientesUtil.getExpediente(cct, numexpNuevo);
			itemExpedienteNuevo.set("ASUNTO", itemExpedienteActual.getString("ASUNTO"));
			itemExpedienteNuevo.store(cct);
			
			//Recuperamos los parámetros de búsqueda
			iCollection = entitiesAPI.getEntities("SGD_LIBRO_DECRETOS", numexpActual);
			IItem itemLibroDecretosActual = (IItem) iCollection.iterator().next();
			
			//Creamos la nueva entidad de parámetros para el nuevo expediente
			IItem itemLibroDecretosNuevo = entitiesAPI.createEntity("SGD_LIBRO_DECRETOS", numexpNuevo);
			Date dFechaInicio = itemLibroDecretosActual.getDate("FECHA_INICIO_DECRETO");
			itemLibroDecretosNuevo.set("FECHA_INICIO_DECRETO", dFechaInicio);
			Date dFechaFin = itemLibroDecretosActual.getDate("FECHA_FIN_DECRETO");
			itemLibroDecretosNuevo.set("FECHA_FIN_DECRETO", dFechaFin);
			itemLibroDecretosNuevo.store(cct);
			
			//Obtenemos el id de la fase del nuevo expediente
			iCollection = invesFlowAPI.getStagesProcess(idExpediente);
			IItem itemStage = (IItem) iCollection.iterator().next();
			int idStage = itemStage.getInt("ID");
			int idStagePcd = itemStage.getInt("ID_FASE");
			
			//Obtenemos el id del trámite de "Seleccion del libro de decretos" en el procedimiento, no en el catálogo
	        strQuery = "WHERE ID_FASE = '"+ idStagePcd +"' AND ID_CTTRAMITE = "
	         + "(SELECT ID FROM SPAC_CT_TRAMITES WHERE COD_TRAM = '"+ _COD_TRAM_SELECCION_LIBRO +"')";
	        iCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
	        int idTaskPcd = ((IItem)iCollection.iterator().next()).getInt("ID");
	        
	        //Creamos el trámite
			int idTask = tx.createTask(idStage, idTaskPcd);
			ITask task = invesFlowAPI.getTask(idTask);
			
			//Obtenemos el tipo de documento
			int idTramCtl = task.getInt("ID_CTTRAMITE");
	    	iCollection = (IItemCollection)procedureAPI.getTaskTpDoc(idTramCtl);
	    	IItem taskTpDoc = (IItem)iCollection.iterator().next();
	    	int documentTypeId = taskTpDoc.getInt("TASKTPDOC:ID_TPDOC");
			
			//Creamos el documento
			//Obtenemos el libro actual y sus propiedades
			IItem itemDocumentActual = DocumentosUtil.getPrimerDocumentByNombre(rulectx.getNumExp(), rulectx, _DOC_LIBRO_DECRETOS);
			//En el properties se configura si duplicamos el firmado o el libro antes de firmar
			//El problema de duplicar el firmado es que se queda corrupta la firma "Hay al menos una firma no válida"
			String strInfoPag = null;
			boolean bMostrarFirmado = Boolean.valueOf
					(DecretosConfiguration.getInstance(cct).getProperty(DecretosConfiguration.LIBRO_DECRETOS.MOSTRAR_FIRMADO));//[dipucr-Felipe 3#99]
			if (bMostrarFirmado) 
				strInfoPag = itemDocumentActual.getString("INFOPAG_RDE");
			else
				strInfoPag = itemDocumentActual.getString("INFOPAG");
			File fileLibro = DocumentosUtil.getFile(cct, strInfoPag, null, null);
			
			//Proteger archivo de copiado, impresión, pegado, etc..
			//Obtenemos la password del properties
			String password = DecretosConfiguration.getInstance(cct).getProperty(DecretosConfiguration.LIBRO_DECRETOS.PASSWORD);//[dipucr-Felipe 3#99]
			PdfUtil.limitarPermisosConPassword(fileLibro, password);
			
			DocumentosUtil.generaYAnexaDocumento(rulectx, idTask, documentTypeId, _DOC_LIBRO_DECRETOS, fileLibro, Constants._EXTENSION_PDF);

    		//Cerramos el trámite
    		tx.closeTask(idTask);
		}
		
		catch (Exception e) {
			throw new ISPACRuleException("Error al generar el nuevo expediente del libro", e);
		}
		return null;
	}
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
