package es.dipucr.ispactx.tx;

import ieci.tdw.ispac.api.IBPMAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.bpm.BpmUIDs;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.procedure.PFaseDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXFaseDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXProcesoDAO;
import ieci.tdw.ispac.ispactx.ITXAction;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispactx.TXDAOGen;
import ieci.tdw.ispac.ispactx.TXNodeActivationManager;
import ieci.tdw.ispac.ispactx.TXProcedure;
import ieci.tdw.ispac.ispactx.TXProcedureMgr;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;
import ieci.tdw.ispac.resp.ResponsibleHelper;

import java.util.Iterator;

public class TXReabrirExpediente implements ITXAction {

	/**
	 * Identificdaor del proceso 
	 */
	private final int mnIdProcess;
	
	/**
	 * Identificador de la fase en el procedimiento usada de partida para retroceder 
	 */
	private final int mnIdPcdStage;

	public TXReabrirExpediente(int nIdProcess, int nIdPcdStage)
	{
		super();
		mnIdProcess=nIdProcess;
		mnIdPcdStage = nIdPcdStage;
	}
	
	public void run(ClientContext cs, TXTransactionDataContainer dtc,
			ITXTransaction itx) throws ISPACException {

		// Activar las fases anteriores según lo indicado en la definición
		// del procedimiento
		
		TXProcesoDAO process= dtc.getProcess(mnIdProcess);
		TXProcedure procedure=TXProcedureMgr.getInstance().getProcedure(cs,process.getIdProcedure());
		
		EventManager eventmgr=new EventManager(cs);
		eventmgr.newContext();
		eventmgr.getRuleContextBuilder().addContext(process);

		//Obtenemos la fase previa a la actual
		IBPMAPI bpmAPI = dtc.getBPMAPI();
		
		int milestoneType = TXConstants.MILESTONE_EXPED_RELOCATED;
		if (process.isSubProcess())
			milestoneType = TXConstants.MILESTONE_SUBPROCESS_RELOCATED;
		dtc.newMilestone(process.getKeyInt(),0,0,milestoneType);	
		
		PFaseDAO previousPStage = procedure.getStageDAO(cs.getConnection(), "" + mnIdPcdStage);
		//Se calcula el responsable a establecer en la fase a iniciar
		String processStageRespId = ResponsibleHelper.calculateStageResp(eventmgr, previousPStage, process.getString("ID_RESP"));
		
		eventmgr.newContext();
		
		//Se invoca al BPM para instanciar una fase
		BpmUIDs bpmUIDs = bpmAPI.instanceProcessStage(""+mnIdPcdStage, processStageRespId, process.getString("ID_PROCESO_BPM"));
		String processStageUID = bpmUIDs.getStageUID();
		
		//Se activa la fase en SPAC
		TXDAOGen genDAO= new TXDAOGen(cs,eventmgr);
		TXNodeActivationManager nodeActMgr=new TXNodeActivationManager(genDAO,procedure,dtc);
		TXFaseDAO stageInstanced = nodeActMgr.activateNode(mnIdPcdStage,previousPStage.getKeyInt(),process,processStageRespId);
		//Si no se instancio una fase sera que ya existe una fase de ese tipo instanciada
		if (stageInstanced == null)
			return;
		if (processStageUID == null)
			processStageUID = ""+stageInstanced.getKeyInt();
		//Se establece el UID de la fase instanciada retornado por el BPM y el responsable en SPAC
		stageInstanced.set("ID_FASE_BPM", processStageUID);
		//stageInstanced.set("ID_RESP", processStageRespId);
		
		//MQE comprobamos si la fase anterior tenía trámites, si es así recuperamos el id de la fase, 
		//para que no los muestre como de fases anteriores
		int id_fase_anterior = 0;
		
		IItemCollection tramitesCollection = cs.getAPI().getEntitiesAPI().getEntities("SPAC_DT_TRAMITES", process.getString("NUMEXP"));
		Iterator<?> tramitesIterator = tramitesCollection.iterator();
		int id_fase_stage = stageInstanced.getIdFase();
		
		boolean encontrado = false;
		
		while (tramitesIterator.hasNext() && !encontrado){
			IItem tramite = (IItem) tramitesIterator.next();
			int id_fase_pcd = tramite.getInt("ID_FASE_PCD");			
			if(id_fase_pcd == id_fase_stage){
				id_fase_anterior = tramite.getInt("ID_FASE_EXP");
				encontrado = true;
			}
		}

		if(id_fase_anterior != 0 && encontrado){
			cs.endTX(true);
			cs.beginTX();
			try{
				cs.getConnection().execute("update spac_fases set id = " + id_fase_anterior + ", id_fase_bpm = '"+id_fase_anterior+"' where id = " + stageInstanced.getKeyInt());
				cs.endTX(true);
			}
			catch(ISPACException e){
				//Se captura la excepción, en caso de que de algún error no hace nada y se comporta como siempre.
				cs.endTX(false);
			}
		}
		//MQE Fin cambios
		
		int eventObjectType = EventsDefines.EVENT_OBJ_PROCEDURE;
		if (process.isSubProcess())
			eventObjectType = EventsDefines.EVENT_OBJ_SUBPROCEDURE;
		//Ejecutar evento al reubicar el expediente.
		eventmgr.processEvents(eventObjectType, process.getInt("ID_PCD"), EventsDefines.EVENT_EXEC_REDEPLOY);
		
		eventObjectType = EventsDefines.EVENT_OBJ_STAGE;
		if(stageInstanced.isActivity())
			eventObjectType = EventsDefines.EVENT_OBJ_ACTIVITY;
		
		//Ejecutar evento al reubicar cada fase
		eventmgr.processEvents(eventObjectType, stageInstanced.getKeyInt(), EventsDefines.EVENT_EXEC_REDEPLOY);
	}
	public void lock(ClientContext cs, TXTransactionDataContainer dtc)
			throws ISPACException {
	}
	public Object getResult(String nameResult) {
		return null;
	}
}
