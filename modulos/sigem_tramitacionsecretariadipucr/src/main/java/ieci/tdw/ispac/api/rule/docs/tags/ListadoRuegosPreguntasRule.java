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

import org.apache.log4j.Logger;

public class ListadoRuegosPreguntasRule implements IRule{
	private static final Logger logger = Logger.getLogger(ListadoRuegosPreguntasRule.class);

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
                String asunto = "";
                if(item.getString("ASUNTO")!=null) asunto = item.getString("ASUNTO");
                listado += String.valueOf(orden) + ".- " + asunto + "\r";
                String str = "";
                if(item.getString("OBSERVACIONES")!=null){
                	str = item.getString("OBSERVACIONES");
                	str = str.replaceAll("\r\n", "\r"); //Evita saltos de línea duplicados                              
                }
                if ( str != null && !str.equals(""))
                {
                	listado += str + "\r";
                }
            	listado += "\r";   
                orden++;
	        }
    		return listado;
    		
        } catch(Exception e) {
        	logger.error("No se ha podido obtener la lista de ruegos y preguntas" + rulectx.getNumExp()+" - "+e.getMessage() ,e);
        	throw new ISPACRuleException("No se ha podido obtener la lista de ruegos y preguntas" + rulectx.getNumExp()+" - "+e.getMessage() ,e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}