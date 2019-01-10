package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

public class ListadoAsistentesRule implements IRule{

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
    
            String listado = "";  //Listado de asistentes 

            String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND ASISTE='SI' ORDER BY NOMBRE ASC";
            IItemCollection collection = entitiesAPI.queryEntities("SPAC_DT_INTERVINIENTES", strQuery);
            Iterator<?> it = collection.iterator();
            
            IItem item = null;
            
            while (it.hasNext()) {
                item = (IItem)it.next();
                listado += item.getString("NDOC") + "\t" + item.getString("NOMBRE") + "\r";
            }
            
            return listado;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la lista de asistentes",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
    
}