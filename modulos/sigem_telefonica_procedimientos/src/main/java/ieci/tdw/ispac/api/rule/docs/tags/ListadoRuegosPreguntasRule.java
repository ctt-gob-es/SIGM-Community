package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

public class ListadoRuegosPreguntasRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
	        String listado = "";  //Listado de ruegos y preguntas

	        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
	        IItemCollection collection = entitiesAPI.queryEntities("SECR_RUEGOS_PREGUNTAS", strQuery);
	        Iterator it = collection.iterator();
	        IItem item = null;
	        int orden = 1;
	        while (it.hasNext()) {
                item = ((IItem)it.next());
                listado += String.valueOf(orden) + ".- " + item.getString("ASUNTO") + "\r";
                String str = item.getString("OBSERVACIONES");
                str = str.replaceAll("\r\n", "\r"); //Evita saltos de línea duplicados                
                if ( str != null )
                {
                	listado += str + "\r";
                }
            	listado += "\r";
                orden++;
	        }
    		return listado;
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de ruegos y preguntas",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}