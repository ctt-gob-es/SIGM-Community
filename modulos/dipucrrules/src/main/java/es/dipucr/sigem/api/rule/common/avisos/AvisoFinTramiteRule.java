package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class AvisoFinTramiteRule  implements IRule {
	
	/**
	 * [Ticket #487 TCG] (SIGEM Regla de aviso de fin de tramite)
	 * **/
	
	private static Logger logger = Logger.getLogger(AvisoFinTramiteRule.class);
	
	public static String _NEWTASK_MESSAGE = "Terminado Trámite ";

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String numexp = "";
		try {
			
			IClientContext ctx = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = ctx.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			IRespManagerAPI respAPI = invesflowAPI.getRespManagerAPI();
			
			numexp = rulectx.getNumExp();
			
			IItem itemProcess = invesflowAPI.getProcess(numexp);
			String sRespProceso = itemProcess.getString("ID_RESP");
			IResponsible responsable = respAPI.getResp(sRespProceso);
			String sNombrePropietario = responsable.getUID();
			
			int processId = invesflowAPI.getProcess(numexp).getInt("ID");
			IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + rulectx.getTaskId() +
				"\" class=\"displayLink\">" + _NEWTASK_MESSAGE +"</a>.<br/>Asunto: " + asunto;
			
			AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sNombrePropietario, ctx);
			
			return true;
			
		} catch (Exception e){
			logger.error("Error al notificar al nuevo responsable del trámite. Expediente: " + numexp + "." + e.getMessage(), e);
			throw new ISPACRuleException
				("Error al notificar al nuevo responsable del trámite.", e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

