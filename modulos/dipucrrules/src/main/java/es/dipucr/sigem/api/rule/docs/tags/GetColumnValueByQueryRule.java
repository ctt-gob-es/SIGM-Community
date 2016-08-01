package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

public class GetColumnValueByQueryRule implements IRule{
	
    public boolean init(IRuleContext rctx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rctx) throws ISPACRuleException{
        return true;
    }

    public String execute(IRuleContext rctx) throws ISPACRuleException{        
    	String result = "";

        try{        	
			IClientContext cct = rctx.getClientContext();
			
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            String sTable = rctx.get("entity").toUpperCase();
            String sColumn = rctx.get("propertySolicita").toUpperCase();
            String sQuery = rctx.get("queryCondicion");
            sQuery = sQuery.replace('#', '\'');
            
            String sqlQueryPart = sQuery+" AND NUMEXP = '"+rctx.getNumExp()+"'";	
			IItemCollection respuesta = entitiesAPI.queryEntities(sTable, sqlQueryPart);            
  
			Iterator<?> it = respuesta.iterator();
	        IItem item = null;
	        
	        while (it.hasNext()) {
	        	 item = ((IItem)it.next());
	        	 result = item.getString(sColumn);	        	 
	        }
        }
        catch(ISPACException e)
        {
            throw new ISPACRuleException("Error al obtener el valor de la columna. " + e.getMessage(), e);
        }
        return result;
    }

    public void cancel(IRuleContext rctx) throws ISPACRuleException{
    }
}
