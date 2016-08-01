package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia solicitud BOP primera del Procedimiento Aprobación y Modificación del Plan de Ordenación
 *
 */

public class InitSolicitudBopPrimeraPlaneamientoRule extends InitSolicitudBopPlaneamientoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PLAN_POM";
		STR_BOP_entidad = "";
		STR_BOP_urgencia = "Normal";
		STR_BOP_sumario = "Publicación primera de expediente de Aprobación y Modificación del Plan de Ordenación Municipal";
		STR_BOP_observaciones = "Solicitud automática desde expediente: Aprobación y Modificación del Plan de Ordenación Municipal";

		//La entidad es el ayuntamiento correspondiente
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        String numexp_solicitud = rulectx.getNumExp();	
	        String strQuery = "WHERE NUMEXP_HIJO='"+numexp_solicitud+"'";
	        IItemCollection col = entitiesAPI.queryEntities("SPAC_EXP_RELACIONADOS", strQuery);
	        Iterator it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return true;
	        }
        	IItem relacion = (IItem)it.next();
        	String numexp_ent = relacion.getString("NUMEXP_PADRE");
        	col = entitiesAPI.getEntities(STR_entidad, numexp_ent);
	        it = col.iterator();
	        if (!it.hasNext())
	        {
	        	return true;
	        }
	        IItem entidad = (IItem)it.next();
	        STR_BOP_entidad = entidad.getString("MUNICIPIO");
	    }
		catch(Exception e) 
	    {
	    	if (e instanceof ISPACRuleException)
	    	{
			    throw new ISPACRuleException(e);
	    	}
	    	throw new ISPACRuleException("No se ha podido iniciar el trámite de publicación.",e);
	    }
		
		return true;
    }
}
