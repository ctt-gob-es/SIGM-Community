package es.dipucr.contratacion.common.avisos;

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

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.ResponsablesUtil;

public class AvisoElectronicoDepartamentoPetContr implements IRule{
	
	private static final Logger logger = Logger.getLogger(AvisoElectronicoDepartamentoPetContr.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
	 		//--------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //-----------------------------------------------------------------------------

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
	        String numexpPeticionContratacion = "";
	        if(itExpRel.hasNext()){
	        	IItem itemExpRel = itExpRel.next();
	        	numexpPeticionContratacion = itemExpRel.getString("NUMEXP_PADRE");
	        }
	        generarAvisoAnuncio(rulectx, ResponsablesUtil.getRespProcedimientoByNumexp(rulectx, numexpPeticionContratacion).getUID(), numexpPeticionContratacion);
	        
		}
		 catch(Exception e) {
			 logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
	     }
		return new Boolean(true);
	}
	
	private void generarAvisoAnuncio(IRuleContext rulectx, String sResponsable, String numexp)
			throws ISPACException
	{
		
		IClientContext ctx = rulectx.getClientContext();
		IInvesflowAPI invesflowAPI = ctx.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
			
		int processId = invesflowAPI.getProcess(numexp).getInt("ID");
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		String asunto = itemExpediente.getString("ASUNTO");
		//Enlace que dirige directamente al trámite
		String message = "Nueva Incidencia de Contrato" + "</a><br/>Asunto: " + asunto;
		AvisosUtil.generarAviso(entitiesAPI, processId, numexp, message, sResponsable, ctx);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
	
	
}
