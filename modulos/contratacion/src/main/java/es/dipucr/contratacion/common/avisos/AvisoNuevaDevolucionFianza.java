package es.dipucr.contratacion.common.avisos;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.avisos.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;

public class AvisoNuevaDevolucionFianza implements IRule 
{
	private Logger logger = Logger.getLogger(AvisoNuevaDevolucionFianza.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		
		try{
			
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //---------------------------------------------------------------------------------------------- 
			
			//Obtengo el numexp del procedimiento de Petición de contratación
	        String sqlQueryPart = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"'";
	        IItemCollection exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        String numexpProceContratacion = "";
	        Iterator<IItem> itExpRel = exp_relacionados.iterator();
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpProceContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        }
	        
	        sqlQueryPart = "WHERE NUMEXP_HIJO='"+numexpProceContratacion+"' AND RELACION='Petición Contrato'";
	        exp_relacionados = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", sqlQueryPart);
	        itExpRel = exp_relacionados.iterator();
	        String numexpPetCont = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPetCont = itemExpRel.getString("NUMEXP_PADRE");
	        }
			
			//Generamos el aviso
			generarAvisoAnuncio(rulectx, ResponsablesUtil.getRespProcedimientoByNumexp(rulectx, numexpPetCont).getUID(), numexpPetCont);
		}
		catch (Exception e) {
			
			logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
		return null;
	}
	


	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
	
	public static String _ADVERTISE_MESSAGE = "Solicitud de Informe sobre Devolución de Fianza";
	
	private void generarAvisoAnuncio(IRuleContext rulectx, String sResponsable, String numexp)
			throws ISPACException
	{
		
		IClientContext ctx = rulectx.getClientContext();
		int stageId = rulectx.getStageId();
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			
		int processId = invesflowAPI.getProcess(numexp).getInt("ID");
//		ITask task = invesflowAPI.getTask(idTask);
//		String nombreTramite = task.getString("NOMBRE");
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		String asunto = itemExpediente.getString("ASUNTO");
		//Enlace que dirige directamente al trámite
		String message = "<a href=\"/SIGEM_TramitacionWeb/showTask.do?stageId=" + stageId +
			"\" class=\"displayLink\">" + _ADVERTISE_MESSAGE + "</a><br/>Asunto: " + asunto;
		AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sResponsable, ctx);
	}
}

