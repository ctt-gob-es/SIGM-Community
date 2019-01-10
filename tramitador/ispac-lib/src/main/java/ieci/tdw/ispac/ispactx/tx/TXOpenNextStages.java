package ieci.tdw.ispac.ispactx.tx;

import ieci.tdw.ispac.api.IBPMAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.bpm.BpmUIDs;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.procedure.PFaseDAO;
import ieci.tdw.ispac.ispaclib.dao.procedure.PNodoDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXFaseDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXProcesoDAO;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispactx.ITXAction;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispactx.TXDAOGen;
import ieci.tdw.ispac.ispactx.TXNodeActivationManager;
import ieci.tdw.ispac.ispactx.TXProcedure;
import ieci.tdw.ispac.ispactx.TXProcedureMgr;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;
import ieci.tdw.ispac.resp.ResponsibleHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import es.dipucr.sigem.api.action.historico.GestionTablasHistorico;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class TXOpenNextStages implements ITXAction {
	
	private final int mnIdProcess;
	private final int mnIdPcdStageActivator;

	/**
	 * Parámetros para el contexto de las reglas.
	 */
	private final Map<?, ?> mparams;

	public TXOpenNextStages(int nIdProcess, int nIdPcdStageActivator) {
		this(nIdProcess, nIdPcdStageActivator, null);
	}

	public TXOpenNextStages(int nIdProcess, int nIdPcdStageActivator, Map<?, ?> params) {
		super();
		mnIdProcess=nIdProcess;
		mnIdPcdStageActivator = nIdPcdStageActivator;
		mparams = params;
	}

	public boolean testProcessClosed(ClientContext cs, TXTransactionDataContainer dtc,TXProcesoDAO exped)
	throws ISPACException
	{
		int nIdProc=exped.getKeyInt();

		int nCount=dtc.getProcessStages(nIdProc).size();
		nCount+=dtc.getProcessSyncNodes(nIdProc).size();
		nCount+=dtc.getProcessTasks(nIdProc).size();
		return (nCount==0);
	}

	
	public void run(ClientContext cs, TXTransactionDataContainer dtc,ITXTransaction itx)
	throws ISPACException{
		//-----
		//BPM
		//----		
		
		TXProcesoDAO process= dtc.getProcess(mnIdProcess);
		TXProcedure procedure=TXProcedureMgr.getInstance().getProcedure(cs,process.getIdProcedure());

		PNodoDAO pNodo = procedure.getNode(cs.getConnection(),mnIdPcdStageActivator);

		IBPMAPI bpmAPI = dtc.getBPMAPI();
		IInvesflowAPI invesflowAPI=cs.getAPI();
		IRespManagerAPI respManagerAPI= invesflowAPI.getRespManagerAPI();

		EventManager eventmgr=new EventManager(cs, mparams);
		eventmgr.newContext();
		eventmgr.getRuleContextBuilder().addContext(process);

		//Se invoca al BPM para obtener las siguientes fases a activar
		List<?> nextNodes = bpmAPI.getNextStages(process.getString("ID_PROCESO_BPM"), pNodo.getString("ID_NODO_BPM"));
		Iterator<?> it = nextNodes.iterator();
		
		TXDAOGen genDAO= new TXDAOGen(cs,eventmgr);
		TXNodeActivationManager nodeActMgr=new TXNodeActivationManager(genDAO,procedure,dtc);
		
		while(it.hasNext()){
			String nodeUID = (String)it.next();
			PNodoDAO node = procedure.getNode(cs.getConnection(), nodeUID);
			if (node.isStage()){
				PFaseDAO pStage = procedure.getStageDAO(cs.getConnection(), nodeUID);
				instanceStage(cs, bpmAPI, pStage, process, eventmgr, nodeActMgr, nodeUID, respManagerAPI);
			}else{//Se trata de un nodo de sincronizacion
				bpmAPI.processSyncNode(nodeUID);
				List<?> nextSpacStages = nodeActMgr.processSyncNode(mnIdPcdStageActivator, node.getKeyInt(), process);
				//Nos quedamos con las fases que nos retorna SPAC no las del BPM, ya que en el BPM propio para SPAC deberia tener acceso al dtc para aplicar los cambios 
				if (nextSpacStages !=null ){	
					for (Iterator<?> iter = nextSpacStages.iterator(); iter.hasNext();) {
						int stageId = ((Integer) iter.next()).intValue();
						PFaseDAO pStage = procedure.getStageDAO(stageId);
						PNodoDAO node1 = procedure.getNode(cs.getConnection(), pStage.getKeyInt());
						if (!nodeActMgr.testStageOpen(process.getKeyInt(), pStage.getKeyInt()))
							instanceStage(cs, bpmAPI, pStage, process, eventmgr, nodeActMgr, node1.getString("ID_NODO_BPM"), respManagerAPI);
					}
				}
			}
		}
		
		// Cierra el expediente cuando no hay fases, nodos o trámites activos
		if (testProcessClosed(cs,dtc,process))
		{
			//Se invoca al BPM para el cierre del proceso
			bpmAPI.endProcess(process.getString("ID_PROCESO_BPM"));
			
			//Eliminamos los nodos de sincronizacion que hayan podido quedar.
			//Habran podido quedar nodos de sincronizacion de tipo OR, cuando teniendo más de un nodo de entrada al nodo de sincronizacion,
			//no hayan llegado todos los nodos porque p.ej: esa rama no se haya ejecutado.
			//dtc.deleteSyncNodes();
//			dtc.loadProcessSyncNodes(process.getKeyInt());
//			dtc.deleteProcessSyncNodes(process.getKeyInt());
			
			// Establecer la fecha de cierre del expediente
			if (process.isProcess()) {
				dtc.setExpedientEndDate(process.getString("NUMEXP"));
			}

			//Ejecutar eventos al cerrar expediente.
			int eventObjectType = EventsDefines.EVENT_OBJ_PROCEDURE;
			if (process.isSubProcess())
				eventObjectType = EventsDefines.EVENT_OBJ_SUBPROCEDURE;
			eventmgr.processEvents(	eventObjectType, process.getInt("ID_PCD"), EventsDefines.EVENT_EXEC_END);

			process.set("ESTADO",TXConstants.STATUS_CLOSED);

			int milestoneType = TXConstants.MILESTONE_EXPED_END;
			if (process.isSubProcess())
				milestoneType = TXConstants.MILESTONE_SUBPROCESS_END;
			TXHitoDAO hitoexp=dtc.newMilestone(process.getKeyInt(),0,0, milestoneType);

			hitoexp.set("FECHA_LIMITE",process.getDate("FECHA_LIMITE"));
			
			//MQE #1023 Tablas de Histórico
			//Pasamos al histórico spac_expedientes, spac_dt_documentos, spac_dt_intervinientes y spac_dt_tramites
			GestionTablasHistorico gh = new GestionTablasHistorico(cs, process);
			gh.pasaAHistorico(dtc);				
			//Fin #1023 Tablas de histórico
		}				
		
		//----					
		
	}
	
	private void instanceStage(ClientContext cs, IBPMAPI bpmAPI, PFaseDAO pStage, TXProcesoDAO process, EventManager eventmgr, TXNodeActivationManager nodeActMgr, String nodeUID, IRespManagerAPI respManagerAPI) throws ISPACException{
		
		//eventmgr.getRuleContextBuilder().addContext(RuleProperties.RCTX_STAGEPCD, ""+pStage.getKeyInt());
		String processStageRespId = ResponsibleHelper.calculateStageResp(eventmgr, pStage, process.getString("ID_RESP"));
		String nombreRespId=((Responsible)respManagerAPI.getResp(processStageRespId)).getName();
		eventmgr.newContext();
		
		//Se invoca al BPM para instanciar una fase
		BpmUIDs bpmUIDs = bpmAPI.instanceProcessStage(nodeUID, processStageRespId, process.getString("ID_PROCESO_BPM"));
		String processStageUID = bpmUIDs.getStageUID();
		//Se crea el registro de la fase en SPAC
		TXFaseDAO stageInstanced = nodeActMgr.activateNode(mnIdPcdStageActivator, pStage.getKeyInt(), process, processStageRespId);
		//Si no se devuelve ninguna fase nueva es que ya existe una fase de este tipo creada para el expediente
		if (stageInstanced != null){
			if (processStageUID == null){
				processStageUID = ""+stageInstanced.getKeyInt();
			}
			//Se establece el UID de la fase instanciada retornado por el BPM y el responsable en SPAC
			stageInstanced.set("ID_FASE_BPM", processStageUID);
			stageInstanced.set("RESP", nombreRespId);
			//stageInstanced.set("ID_RESP", processStageRespId);
			
			//[Manu Ticket #620] INICIO SIGEM Muestra Trámites al retorceder fase
			// Comprobamos si la fase posterior tenía trámites, si es así recuperamos el id de la fase, 
			//para que no los muestre como de fases anteriores
			int id_fase_posterior = 0;
			
			IItemCollection tramitesCollection = TramitesUtil.getTramites(cs, process.getString("NUMEXP"));
			Iterator<?> tramitesIterator = tramitesCollection.iterator();
			int id_fase_stage = stageInstanced.getIdFase();
			
			boolean encontrado = false;
			
			while (tramitesIterator.hasNext() && !encontrado){
				IItem tramite = (IItem) tramitesIterator.next();
				int id_fase_pcd = tramite.getInt("ID_FASE_PCD");			
				if(id_fase_pcd == id_fase_stage){
					id_fase_posterior = tramite.getInt("ID_FASE_EXP");
					encontrado = true;
				}
			}

			if(id_fase_posterior != 0 && encontrado){
				cs.endTX(true);
				cs.beginTX();
				try{
					cs.getConnection().execute("update spac_fases set id = " + id_fase_posterior + ", id_fase_bpm = '" + id_fase_posterior + "' where id = " + stageInstanced.getKeyInt());
					cs.endTX(true);
				}
				catch(ISPACException e){
					//Se captura la excepción, en caso de que de algún error no hace nada y se comporta como siempre.
					cs.endTX(false);
				}
			}
			//[Manu Ticket #620] FIN - SIGEM Muestra Trámites al retorceder fase
			
			
		}
	}
	
	public Object getResult(String nameResult)
	{
		return null;
	}

	public void lock(ClientContext cs, TXTransactionDataContainer dtc)
			throws ISPACException
	{
//		TXFaseDAO stage=dtc.getStage(mnIdStage);
//		dtc.getLockManager().lockProcess(stage.getInt("ID_EXP"));
	}
	
}