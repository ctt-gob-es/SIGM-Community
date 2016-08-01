package ieci.tdw.ispac.api.rule.docs.tags;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Convierte a mayúsculas la cadena correspondiente a la propiedad de la entidad indicada.
 *
 */
public class ToHCapsTagRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		String strMayusculas="";
        try
        {
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

	        String strEntity = rulectx.get("entity");
	        String strProperty = rulectx.get("property");
	        if ( ! StringUtils.isEmpty(strEntity) && 
	        	 ! StringUtils.isEmpty(strProperty) )
	        {
		        String strQuery = "WHERE NUMEXP = '" + rulectx.getNumExp() + "'";
		        IItemCollection collection = entitiesAPI.queryEntities(strEntity, strQuery);
		        Iterator it = collection.iterator();
		        IItem item = null;
		        if (it.hasNext()) {
	            	item = ((IItem)it.next());
	            	String strProp = item.getString(strProperty);
	            	strMayusculas = strProp.toUpperCase();
		        }
	        }
	    }
        catch (Exception e) 
        {
	        throw new ISPACRuleException("Error en ToHCapsTagRule.", e);
	    }
        return strMayusculas;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
