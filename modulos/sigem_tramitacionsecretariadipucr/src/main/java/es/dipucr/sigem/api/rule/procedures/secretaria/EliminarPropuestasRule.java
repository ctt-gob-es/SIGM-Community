package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

public class EliminarPropuestasRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(EliminarPropuestasRule.class);

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
			String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection collAllProps = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        Iterator<IItem> itAllProps = collAllProps.iterator();
	        logger.warn("collAllProps "+collAllProps);
	        logger.warn("itAllProps "+itAllProps);
	    	IItem iProp = null;
	        while(itAllProps.hasNext()) {
	        	iProp = ((IItem)itAllProps.next());
	        	iProp.delete(cct);
	        }
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta",e);
        	
        }
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
