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

import org.apache.log4j.Logger;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna el IVA del anuncio. Ha sido necesario crear la regla pues debemos
 * multiplicar por 100
 *
 */
public class GetIVAAnuncioBopRule implements IRule 
{
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(AdministracionTagRule.class);
	
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
        	Double iva = null;
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
	        collection = entitiesAPI.queryEntities("BOP_SOLICITUD", strQuery);	
	        it = collection.iterator();
	        if (it.hasNext())
	        {
	        	item = (IItem)it.next();
	        	iva = item.getDouble("TIPO_IVA_CARACTERES") * 100;
	        }

	        return String.valueOf(iva.intValue());
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al obtener el IVA del anuncio.", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
