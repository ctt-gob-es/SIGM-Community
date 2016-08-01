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
 * Inicia tarea aprobación inicial Procedimiento Aprobación y Modificación del Plan de Ordenación Municipal
 *
 */

public class InitTaskResolucionInicialPlaneamientoRule extends InitTaskResolucionPlaneamientoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PLAN_POM";
		STR_queryDocumentos = "NOMBRE = 'Providencia' OR " +  
								"NOMBRE = 'Documento de Avance'"; 
		
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        int pcdId = rulectx.getProcedureId();

	        String strQuery = "WHERE ID_PCD="+pcdId+" AND NOMBRE='Providencia'";
			IItemCollection col = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQuery);
	        Iterator it = col.iterator();
	        if (it.hasNext())
	        {
		        IItem tramite = (IItem)it.next();
		        int tramiteId = tramite.getInt("ID");
			
				STR_queryDocumentos +=
					" OR " +
					"ID_TRAMITE_PCD="+tramiteId;
	        }

        }
    	catch(Exception e) 
        {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido iniciar el trámite de resolución.",e);
        }
		
        return true;
    }
}
