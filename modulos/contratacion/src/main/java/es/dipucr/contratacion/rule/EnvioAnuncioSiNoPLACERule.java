package es.dipucr.contratacion.rule;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

public class EnvioAnuncioSiNoPLACERule implements IRule{
	
	private static final Logger logger = Logger.getLogger(EnvioAnuncioSiNoPLACERule.class);

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        IItemCollection colAdjudicacion = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			Iterator <IItem> iterAdjudicacion = colAdjudicacion.iterator();
			if(iterAdjudicacion.hasNext()){
				IItem itemAdjudicacion = iterAdjudicacion.next();
				itemAdjudicacion.set("CONTRATACION_ADJUDICACION","SI");
				itemAdjudicacion.store(cct);
			}
			else{
				IItem itemAdjudicacion = entitiesAPI.createEntity("CONTRATACION_ADJUDICACION","");
				itemAdjudicacion.set("CONTRATACION_ADJUDICACION","SI");
				itemAdjudicacion.store(cct);
			}
			
		} catch(Exception e) {
			logger.error("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error: " + rulectx.getNumExp() + ". " + e.getMessage(), e);	    	
	    }

		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

}
