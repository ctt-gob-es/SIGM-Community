package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

/**
 * [eCenpri-Felipe #346]
 * @author Felipe
 * @since 29.06.2011
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Devuelve el número de meses totales del anticipo menos uno
 *
 */
public class GetMesesMenosUnoAnticiposRule implements IRule 
{
		
	public boolean init(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException 
	{
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException 
	{
        try
        {
        	int numMeses = 0;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator it = null;
	        IItem item = null;
        	
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        strQuery = "WHERE NUMEXP='" + rulectx.getNumExp() + "'";
	        collection = entitiesAPI.queryEntities("DPCR_ANTICIPOS", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext())
	        {
	        	item = (IItem)it.next();
	        	numMeses = item.getInt("NUM_MESES");
	        }

	        return String.valueOf(numMeses - 1);
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al obtener el los meses menos uno del anticipo.", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
