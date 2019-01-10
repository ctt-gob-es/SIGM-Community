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
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Ticket #473 - Aviso trámite delegado
 * @author Manu-ecenpri
 * @since 28.08.2013
 */
public class AvisoTramiteDelegadoRule implements IRule {
	
	public static String _DELEGATETASK_MESSAGE = "Trámite Delegado";

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

			String sNombrePropietario = null;
			
			String numexp = rulectx.getNumExp();
			
			IItem itemProcess = invesflowAPI.getProcess(numexp);
			String sRespProceso = itemProcess.getString("ID_RESP");

			IResponsible responsable = respAPI.getResp(sRespProceso);
			sNombrePropietario = responsable.getName();
			
			ITask task = invesflowAPI.getTask(rulectx.getTaskId());

			sRespTramite = task.getString("ID_RESP");
			
			if (!sRespTramite.equals(sRespProceso)){
				
				int processId = invesflowAPI.getProcess(numexp).getInt("ID");
				String nombreTramite = task.getString("NOMBRE");
				IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
				String asunto = itemExpediente.getString("ASUNTO");
				String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + rulectx.getTaskId() +
					"\" class=\"displayLink\">" + _DELEGATETASK_MESSAGE + " " + nombreTramite + " desde " + sNombrePropietario +
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
