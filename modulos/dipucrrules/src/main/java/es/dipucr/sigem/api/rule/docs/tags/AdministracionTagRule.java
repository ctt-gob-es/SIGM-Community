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

import es.dipucr.sigem.api.rule.procedures.bop.BopUtils;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna una cadena que contiene un listado de documentos a subsanar.
 *
 */
public class AdministracionTagRule implements IRule 
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
        	String clasificacion = null;
	        String strQuery = null;
	        IItemCollection collection = null;
	        Iterator<?> it = null;
	        IItem item = null;
	        String administracion = null;
        	
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
	        	
	        	clasificacion = item.getString("CLASIFICACION");
	        }
	        
	        /*
	         * [eCenpri-Felipe #451] Se obtiene la administración de la BBDD
	         * Se estaba haciendo a pelo
	         */
	        administracion = BopUtils.getGrupoAdministracion(rulectx, clasificacion);
	        // FIN [eCenpri-Felipe #451]

	        return administracion;
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error confecionando listado de documentos a subsanar.", e);
	    }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
