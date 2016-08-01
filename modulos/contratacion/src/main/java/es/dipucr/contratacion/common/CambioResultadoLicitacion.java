package es.dipucr.contratacion.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.rule.CambioResultadoLicitacionAdjudicacion;

public class CambioResultadoLicitacion implements IRule{
	
	public static final Logger logger = Logger.getLogger(CambioResultadoLicitacionAdjudicacion.class);
	public static String valorResultadoLicitacion = "";

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			/************************************************************************/
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			 /***********************************************************************/
			
			IItemCollection resLiciCollection = entitiesAPI.getEntities("CONTRATACION_ADJUDICACION", rulectx.getNumExp());
			@SuppressWarnings("unchecked")
			Iterator <IItem> resLiciIterator = resLiciCollection.iterator();
			if(resLiciIterator.hasNext()){
				IItem adj = resLiciIterator.next();
				adj.set("RES_LICITACION", valorResultadoLicitacion);
				adj.store(cct);
			}
			else{
				IItem adjudicacion = entitiesAPI.createEntity("CONTRATACION_ADJUDICACION","");
				adjudicacion.set("NUMEXP", rulectx.getNumExp());
				adjudicacion.set("RES_LICITACION", valorResultadoLicitacion);
				adjudicacion.store(cct);
			}
			

		
		}catch(ISPACRuleException e){
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
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
