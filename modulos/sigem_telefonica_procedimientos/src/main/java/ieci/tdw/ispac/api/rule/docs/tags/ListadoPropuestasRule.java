package ieci.tdw.ispac.api.rule.docs.tags;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class ListadoPropuestasRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
    
            String listado = "";  //Listado de propuestas 
            
            String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' ORDER BY ORDEN ASC";
            IItemCollection collection = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
            Iterator<?> it = collection.iterator();
            IItem item = null;
            int orden = 1;
            
            while (it.hasNext()) {
                item = (IItem)it.next();
                listado += orden + ".- " + item.getString("EXTRACTO") + "\r";
                orden++;
            }
            
            //Mostraremos como último punto el apartado de ruegos y preguntas
            //listado += String.valueOf(orden) + ".- Ruegos y preguntas.\r";
            return listado;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la lista de propuestas",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
}