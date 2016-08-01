package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Ticket #36 - Aviso nuevo trámite
 * @author Felipe-ecenpri
 * @since 14.06.2010
 */
public class AvisoNuevoTramiteRule implements IRule {
	
	public static String _NEWTASK_MESSAGE = "Nuevo Trámite ";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IRespManagerAPI respAPI = invesflowAPI.getRespManagerAPI();
			
			String sRespTramite = null;
//			String sRespFase = null;
			String sNombrePropietario = null;
			
			String numexp = rulectx.getNumExp();
			
			IItem itemProcess = invesflowAPI.getProcess(numexp);
			String sRespProceso = itemProcess.getString("ID_RESP");

			IResponsible responsable = respAPI.getResp(sRespProceso);
			sNombrePropietario = responsable.getName();
			
			ITask task = invesflowAPI.getTask(rulectx.getTaskId());
			IItem taskPCD = invesflowAPI.getProcedureTaskPCD(task.getInt("ID_TRAMITE"));
			sRespTramite = taskPCD.getString("ID_RESP");
			//INICIO [dipucr-Felipe #1151]
			//Para los casos en los que el responsable se setea en tiempo de ejecución
			if (StringUtils.isEmpty(sRespTramite)){
				sRespTramite = task.getString("ID_RESP");
			}
			//FIN [dipucr-Felipe #1151]
			
			if (!sRespTramite.equals(sRespProceso)){
				
				int processId = invesflowAPI.getProcess(numexp).getInt("ID");
				String nombreTramite = task.getString("NOMBRE");
				IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
				String asunto = itemExpediente.getString("ASUNTO");
				String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + rulectx.getTaskId() +
					"\" class=\"displayLink\">" + _NEWTASK_MESSAGE +  nombreTramite + " desde " + sNombrePropietario +
					"</a>.<br/>Asunto: " + asunto;
				
				AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sRespTramite, ctx);
			}
			
			return true;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error al notificar al nuevo responsable del trámite.", e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
