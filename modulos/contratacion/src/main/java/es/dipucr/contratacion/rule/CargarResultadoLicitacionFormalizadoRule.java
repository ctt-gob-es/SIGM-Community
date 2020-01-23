package es.dipucr.contratacion.rule;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class CargarResultadoLicitacionFormalizadoRule implements IRule{

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
			
			 /***********************************************************/
	        cct.beginTX();
			Iterator<IItem> itpartDep = ConsultasGenericasUtil.queryEntities(cct, "CONTRATACION_ADJUDICACION", "NUMEXP='"+rulectx.getNumExp()+"'");
			IItem itempartDep = null;
			if(itpartDep.hasNext()){
				itempartDep = itpartDep.next();
			}
			else{
				itempartDep = entitiesAPI.createEntity("CONTRATACION_ADJUDICACION",rulectx.getNumExp());
			}
			//https://contrataciondelestado.es/codice/cl/2.02/TenderResultCode-2.02.gc
			itempartDep.set("RES_LICITACION", "9 - Formalizado");			
			itempartDep.store(cct);			
			cct.endTX(true);
			
		} catch(Exception e) {
        	throw new ISPACRuleException("Error en el numexp. "+rulectx.getNumExp()+" - "+e.getMessage(),e);
        }
		
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
