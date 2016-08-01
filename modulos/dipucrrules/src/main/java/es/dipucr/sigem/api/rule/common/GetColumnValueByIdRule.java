/*
 * Created on 02-dic-2004
 *
 */
package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class GetColumnValueByIdRule implements IRule
{

    public boolean init(IRuleContext rctx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rctx) throws ISPACRuleException
    {
        return true;
    }

    public Object execute(IRuleContext rctx) throws ISPACRuleException
    {        

        try
        {
        	String result = null;
        	
			IClientContext cct = rctx.getClientContext();
			
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
            
            String sTable = rctx.get("table").toUpperCase();
            String sColumn = rctx.get("column").toUpperCase();
            String sKey = rctx.get("id").toUpperCase();
            int iKey = Integer.valueOf(sKey);
            
            IItem item = entitiesAPI.getEntity(sTable, iKey);
            result = item.getString(sColumn);
			
			return result;
        }
        catch(ISPACException e)
        {
            throw new ISPACRuleException("Error al obtener el valor de la columna.",e);
        }
    }

    public void cancel(IRuleContext rctx) throws ISPACRuleException
    {

    }
}
