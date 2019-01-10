package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrAvisoNuevoExpedienteRule implements IRule {
	
	public static String _NUEVOEXPEDIENTE_MESSAGE = "Nuevo Expediente ";

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
						
			String sRespFase = null;
			String sNombrePropietario = null;
			String responsableId = "";
			
			String numexp = rulectx.getNumExp();
			
			IItem itemProcess = invesflowAPI.getProcess(numexp);
			String sRespProceso = itemProcess.getString("ID_RESP");
			
			IStage stage = invesflowAPI.getStage(rulectx.getStageId());
			sRespFase = stage.getString("ID_RESP");
			IResponsible responsable = null;
			
			//Tomamos el responsable de la fase actual, si no existe, el responsable del procedimiento
			if(sRespFase != null && !sRespFase.equals("")){
				responsable = respAPI.getResp(sRespFase);
				sNombrePropietario = responsable.getName();
				responsableId = sRespFase;
			}
			else{
				responsable = respAPI.getResp(sRespProceso);
				sNombrePropietario = responsable.getName();
				responsableId = sRespProceso;
			}
					
			
			int processId = invesflowAPI.getProcess(numexp).getInt("ID");
			String nombreProcedimiento = invesflowAPI.getProcedure(rulectx.getProcedureId()).getString("NOMBRE");
			IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?taskId=" + rulectx.getTaskId() +
				"\" class=\"displayLink\">" + _NUEVOEXPEDIENTE_MESSAGE +  nombreProcedimiento + " desde " + sNombrePropietario +
				"</a>.<br/>Asunto: " + asunto;
			
			AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, responsableId, ctx);
		
			return true;
			
		} catch (Exception e){
			
			throw new ISPACRuleException
				("Error al notificar al nuevo responsable del trámite.", e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
