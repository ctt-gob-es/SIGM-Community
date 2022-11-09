package ieci.tdw.ispac.ispactx.tx;

import ieci.tdw.ispac.api.IBPMAPI;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACLockedObject;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.CollectionDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXHitoDAO;
import ieci.tdw.ispac.ispaclib.dao.tx.TXTramiteDAO;
import ieci.tdw.ispac.ispaclib.notices.Notices;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispactx.ITXAction;
import ieci.tdw.ispac.ispactx.TXConstants;
import ieci.tdw.ispac.ispactx.TXTransactionDataContainer;

import java.util.Date;
import java.util.Map;

import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * Acci�n para cerrar un tr�mite.
 */
public class TXCloseTask implements ITXAction {
	
	/** 
	 * Identificador del tr�mite instanciado. 
	 */
	private final int mnIdTask;
	
	/**
	 * Par�metros para el contexto de las reglas.
	 */
	private final Map params;

	
	/**
	 * Constructor.
	 * @param nIdTask Identificador del tr�mite instanciado.
	 */
	public TXCloseTask(int nIdTask) {
		this(nIdTask, null);
	}

	/**
	 * Constructor.
	 * @param nIdTask Identificador del tr�mite instanciado.
	 */
	public TXCloseTask(int nIdTask, Map params) {
		super();
		this.mnIdTask = nIdTask;
		this.params = params;
	}

	/**
	 * Ejecuta la acci�n.
	 * @param cs Contexto de cliente.
	 * @param dtc Contenedor de los datos de la transacci�n.
	 * @param itx Transacci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void run(ClientContext cs, TXTransactionDataContainer dtc,
			ITXTransaction itx) throws ISPACException {
		

			// Informaci�n del tr�mite
			TXTramiteDAO task=dtc.getTask(mnIdTask);
			int nIdPCDTask=task.getInt("ID_TRAMITE");
			int nIdProc=task.getInt("ID_EXP");
			int nIdPCDStage=task.getInt("ID_FASE_PCD");
			Date taskdeadline=task.getDate("FECHA_LIMITE");

			// Comprobar si el tr�mite tiene documentos pendientes de firma
			IEntitiesAPI entitiesAPI = cs.getAPI().getEntitiesAPI();
			if (entitiesAPI.countTaskDocumentsInSignCircuit(task.getKeyInt()) > 0) {
				throw new ISPACInfo("exception.expedients.closeTask.docsInSignCircuit", 
						new String[] { task.getString("NOMBRE"), task.getString("NUMEXP")});
			}
			
			//[dipucr-Felipe #1246]
			// Comprobar si el tr�mite tiene documentos pendientes de descarga Portafirmas
			if (entitiesAPI.countTaskDocumentsErrorPortafirmas(task.getKeyInt()) > 0 
					&& !Constants.DEFAULT_USER_PORTAFIRMAS.equals(cs.getUser().getRespName()))
			{
				throw new ISPACInfo("exception.expedients.closeTask.docsErrorPortafirmas", 
						new String[] { task.getString("NOMBRE"), task.getString("NUMEXP")});
			}
			
			String bpmTaskId = task.getString("ID_TRAMITE_BPM");

			// Obtener el API de BPM
			IBPMAPI bpmAPI = dtc.getBPMAPI();
			
			//Se invoca al BPM para el cierre del tr�mite
			bpmAPI.endTask(bpmTaskId);

			EventManager eventmgr=new EventManager(cs, params);
			
			// Se construye el contexto de ejecuci�n de scripts
			eventmgr.getRuleContextBuilder().addContext(task);
	
			//Ejecutar eventos de sistema al cerrar tr�mite
			eventmgr.processSystemEvents(EventsDefines.EVENT_OBJ_TASK,
										 EventsDefines.EVENT_EXEC_END);
	
			//Ejecutar eventos al cerrar tr�mite
			eventmgr.processEvents(	EventsDefines.EVENT_OBJ_TASK,
									nIdPCDTask,
									EventsDefines.EVENT_EXEC_END);
			String idRespClosedTask = task.getString("ID_RESP");
			
			//Se elimina el tramite cerrado
			dtc.deleteTask(mnIdTask);
	
			//Se anota en la entidad del tr�mite la finalizaci�n del mismo y se establece responsable del tramite finalizado
			dtc.closeTaskEntity(mnIdTask, idRespClosedTask);
	
			// Marcar el hito
			TXHitoDAO hito=dtc.newMilestone(nIdProc,nIdPCDStage,nIdPCDTask, TXConstants.MILESTONE_TASK_END);
			
			hito.set("INFO", composeInfo());
			hito.set("FECHA_LIMITE",taskdeadline);
			hito.store(cs.getConnection());//[dipucr-Felipe Manuel #884]
			
			//Ejecutar eventos de sistema tras terminar el tr�mite.
			eventmgr.processSystemEvents(EventsDefines.EVENT_OBJ_TASK,
										 EventsDefines.EVENT_EXEC_END_AFTER);
	
			//Ejecutar eventos tras terminar el tr�mite.
			eventmgr.processEvents(	EventsDefines.EVENT_OBJ_TASK,
									nIdPCDTask,
									EventsDefines.EVENT_EXEC_END_AFTER);

			//Si existe un aviso electronico que indica que el tramite a cerrar ha sido delegado, se archiva
			Notices notices = new Notices(cs);
			notices.archiveDelegateTaskNotice(task.getInt("ID_FASE_EXP"), task.getKeyInt());
			
			//INICIO [dipucr-Felipe #1026] Comprobamos si se deleg� el tr�mite para mandar un aviso de fin de tr�mite
			String query = "HITO = " + TXConstants.MILESTONE_EXPED_DELEGATED + " AND ID_TRAMITE = " + nIdPCDTask;
			CollectionDAO colHitos = TXHitoDAO.getMilestones(cs.getConnection(), nIdProc, query);
			if (colHitos.next()){
				TXHitoDAO hitoDelegacion = (TXHitoDAO) colHitos.value();
				String idAutor = hitoDelegacion.getString("AUTOR");
				if (!StringUtils.isEmpty(idAutor) && !idAutor.equals(cs.getRespId())){
					String message = "<a href=\"showTask.do?taskId=" + mnIdTask + 
							"\" class=\"displayLink\">Terminado tr�mite " + task.getString("NOMBRE") + "</a>.";
					Notices.generateNotice(cs, nIdProc, cs.getStateContext().getStageId(), mnIdTask, cs.getStateContext().getNumexp(), message, 
							idAutor, Notices.TIPO_AVISO_TRAMITE_FINALIZADO, nIdPCDStage, cs.getStateContext().getTaskPcdId());
				}
			}
			//FIN [dipucr-Felipe #1026]
	}

	private String composeInfo(){
		return new StringBuffer()
			.append("<?xml version='1.0' encoding='ISO-8859-1'?>")
			.append("<infoaux><id_tramite>")
			.append(mnIdTask)
			.append("</id_tramite></infoaux>")
			.toString();
	}

	/**
	 * Bloquea el objeto de la acci�n.
	 * @param cs Contexto de cliente.
	 * @param dtc Contenedor de los datos de la transacci�n.
	 * @throws ISPACException si ocurre alg�n error.
	 */
	public void lock(ClientContext cs, TXTransactionDataContainer dtc)
			throws ISPACException {
		
		TXTramiteDAO task=dtc.getTask(mnIdTask);
		dtc.getLockManager().lockProcess(task.getInt("ID_EXP"));
		
		try {
			dtc.getLockManager().lockProcess(task.getInt("ID_EXP"));
			dtc.getLockManager().lockTask(mnIdTask);
		} catch (ISPACLockedObject ilo) {
			throw new ISPACInfo("exception.expedients.closeTask.statusBlocked");
		}
	}

	/**
	 * Obtiene el resultado de la acci�n.
	 * @param nameResult Nombre del resultado.
	 * @return Resultado de la acci�n.
	 */
	public Object getResult(String nameResult) {
		return null;
	}
}
