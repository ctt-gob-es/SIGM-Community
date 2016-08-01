package es.dipucr.sigem.api.rule.procedures.bop.imprenta;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;


public class DipucrAvisoNuevoExpTrabImprenta implements IRule {
	
	private static final Logger logger =  Logger.getLogger(DipucrAvisoNuevoExpTrabImprenta.class);
	
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
						
			String responsableId = "";
			
			String numexp = rulectx.getNumExp();

			IResponsible responsable = ResponsablesUtil.getRespProcedimiento(rulectx);			
			responsableId = responsable.getUID();			
								
			int processId = invesflowAPI.getProcess(numexp).getInt("ID");
			String nombreProcedimiento = invesflowAPI.getProcedure(rulectx.getProcedureId()).getString("NOMBRE");
			IItem itemExpediente = ExpedientesUtil.getExpediente(ctx, numexp);
			String asunto = itemExpediente.getString("ASUNTO");
			String message = "<a href=\"/SIGEM_TramitacionWeb/showExpedient.do?stageId=" + rulectx.getStageId() +
				"\" class=\"displayLink\">" + _NUEVOEXPEDIENTE_MESSAGE +  nombreProcedimiento +
				"</a>.<br/>Asunto: " + asunto;
							
			AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, responsableId, ctx);
		
			return true;
			
		} catch (Exception e){
			logger.error("Error al insertar el nuevo aviso. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al insertar el nuevo aviso. " + e.getMessage(), e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
