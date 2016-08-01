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

public class DiligenciaSesionNoCelebrada implements IRule{

	public void cancel(IRuleContext arg0) throws ISPACRuleException {
		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		//Comprobar si el tipo de sesion es No Celebrada para decrementar el número de sesión en uno.
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
	        
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_CONVOCATORIA", strQuery+" ORDER BY ID");
	        if(collection.toList().size() != 0){
	        	Iterator <IItem> it = collection.iterator();
		        
		        IItem col=null;
		        
		        while (it.hasNext()) {
		        	int num = 0;
		        	col = (IItem)it.next();
		        	num = col.getInt("NUMERO");
		        	num = num - 1;		        	
		        	col.set("NUMERO", num);
		        	col.store(cct);
		        }
	        }
	       
    		
		} catch (Exception e) {
			if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido modificar el número de la sesion",e);
		}
		return new Boolean(true);
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
