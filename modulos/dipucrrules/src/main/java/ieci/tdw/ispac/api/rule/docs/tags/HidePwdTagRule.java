package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Sustituye los caracteres de una cadena de texto por asteriscos
 *
 */
public class HidePwdTagRule implements IRule {
	
	private static final Logger logger = Logger.getLogger(HidePwdTagRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	@SuppressWarnings("rawtypes")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
		String strHidden="";
        try
        {
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();

	        String strMode = rulectx.get("mode");
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
		            if(StringUtils.isNotEmpty(strProp)){
		            	if (strMode.equalsIgnoreCase("full"))
		            	{
		            		//Oculta todos los caracteres
		            		for (int i=0 ; i<strProp.length() ; i++)
		            		{
		            			strHidden += '*';
		            		}
		            	}
		            	else if (strMode.equalsIgnoreCase("partial"))
		            	{
		            		//Deja los 4 últimos caracteres sin ocultar
		            		if (strProp.length() > 4)
		            		{
			            		for (int i=0 ; i<strProp.length()-4 ; i++)
			            		{
			            			strHidden += '*';
			            		}
			            		strHidden += strProp.substring(strProp.length()-4);
		            		}
		            		else
		            		{
		            			//Cadena corta. Se deja tal cual
		            			strHidden = strProp;
		            		}
		            	}
		            	else
		            	{
		            		//Modo desconocido, no hago nada
		            		logger.warn("Modo desconocido");
		            		strHidden = strProp;
		            	}
		            }
		        }
	        }
	    }
        catch (Exception e) 
        {
	        throw new ISPACRuleException("Error en HidePwdTagRule.", e);
	    }
        return strHidden;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
