package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class DipucrRelacionaExpedientes implements IRule{
	
	public static final Logger logger = Logger.getLogger(DipucrRelacionaExpedientes.class);
	public String relacion = "";
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try
    	{
    		logger.info("INICIO - " + this.getClass().getName());
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			cct.endTX(true);
			
			IItemCollection entidades = entitiesAPI.getEntities("DPCR_RELACIONA_EXP_REG_TEL", rulectx.getNumExp());
			Iterator<?> it = entidades.iterator();			
			if(it.hasNext()){
				String numexp_padre = ((IItem)it.next()).getString("NUMEXP_PADRE");
				
				IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);

				registro.set("NUMEXP_PADRE", numexp_padre);
				registro.set("NUMEXP_HIJO", rulectx.getNumExp());
				registro.set("RELACION", relacion);

				registro.store(cct);				
			}		
			logger.info("FIN - " + this.getClass().getName());
        	return new Boolean(true);
        }
    	catch(Exception e) 
        {
    		logger.error("No se ha podido inicializar la propuesta. " + e.getMessage(), e);
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta. " + e.getMessage(),e);
        }
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
		return true;
	}

}
