package es.dipucr.contratacion.common.avisos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;

public class AvisoElectrContratacionValoresAnormales implements IRule 
{
	private Logger logger = Logger.getLogger(AvisoElectrContratacionValoresAnormales.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		
		try{
			
			//Generamos el aviso
			generarAvisoAnuncio(rulectx, ResponsablesUtil.get_ID_RESP_Fase(rulectx));
		}
		catch (Exception e) {
			
			logger.error("Se produjo una excepci√≥n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return null;
	}
	


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	public static String _ADVERTISE_MESSAGE = "Valores Anormales en el expediente del Licitador";
	
	private void generarAvisoAnuncio(IRuleContext rulectx, String sResponsable)
			throws ISPACException
	{
		
		IClientContext ctx = rulectx.getClientContext();
		int stageId = rulectx.getStageId();
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		String numexp = rulectx.getNumExp();		
		int processId = invesflowAPI.getProcess(numexp).getInt("ID");
//		ITask task = invesflowAPI.getTask(idTask);
//		String nombreTramite = task.getString("NOMBRE");
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		String asunto = itemExpediente.getString("ASUNTO");
		//Enlace que dirige directamente al tr·mite
		String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?stageId=" + stageId +
			"\" class=\"displayLink\">" + _ADVERTISE_MESSAGE + "</a><br/>Asunto: " + asunto;
		AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sResponsable, ctx);
	}
}

