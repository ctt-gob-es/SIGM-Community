package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

public class GetLongVarRule implements IRule{
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        String strValor = null;
        
        try {
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            String nombre = rulectx.get("nombre");
            String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "' AND NOMBRE = '" + nombre + "'";
            IItemCollection collection = entitiesAPI.queryEntities("TSOL_LONG_VARS", strQuery);
            Iterator<?> it = collection.iterator();
            IItem item = null;
            
            if (it.hasNext()) {
                item = (IItem)it.next();
                strValor = item.getString("VALOR");
            }
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido obtener la variable larga",e);
        }
        return strValor;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}


