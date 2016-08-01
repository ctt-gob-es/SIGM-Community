package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class InitSolicitudRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		try 
		{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        String strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SUBV_SOLICITUD", strQuery);
	        Iterator it = coll.iterator();
	        IItem solicitud = null;
	        if (it.hasNext())
	        {
	        	solicitud = (IItem)it.next();
	        }
	        else
	        {
	        	solicitud = entitiesAPI.createEntity("SUBV_SOLICITUD", rulectx.getNumExp()); 
	        }
        	solicitud.set("PRESENTADA", "NO");
        	solicitud.set("SUBSANADA", "NO");
        	solicitud.set("SELECCIONADA", "NO");
        	solicitud.set("ACEPTADA", "NO");
        	solicitud.set("JUSTIFICADA", "NO");
        	solicitud.store(cct);
		} 
    	catch(Exception e)
    	{
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido iniciar la solicitud",e);
        }
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}
