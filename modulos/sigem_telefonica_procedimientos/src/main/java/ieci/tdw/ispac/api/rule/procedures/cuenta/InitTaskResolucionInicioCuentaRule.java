package ieci.tdw.ispac.api.rule.procedures.cuenta;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskResolucionInicioCuentaRule extends InitTaskResolucionCuentaRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "CUEN_CUENTA";
		STR_queryDocumentos = 
			"NOMBRE = 'Providencia' OR " + 
			"NOMBRE = 'Propuesta de aprobación inicial'"; 
		
		try
		{
	        IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
	        int pcdId = rulectx.getProcedureId();

	        String strQuery = "WHERE ID_PCD="+pcdId+" AND NOMBRE='Cuenta general'";
			IItemCollection col = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQuery);
	        Iterator it = col.iterator();
	        if (it.hasNext())
	        {
		        IItem tramite = (IItem)it.next();
		        int tramCuentaId = tramite.getInt("ID");
			
				STR_queryDocumentos +=
					" OR " +
					"ID_TRAMITE_PCD="+tramCuentaId;
	        }

	        strQuery = "WHERE ID_PCD="+pcdId+" AND NOMBRE='Informe de intervención'";
			col = entitiesAPI.queryEntities("SPAC_P_TRAMITES", strQuery);
	        it = col.iterator();
	        if (it.hasNext())
	        {
		        IItem tramite = (IItem)it.next();
		        int tramIntervencionId = tramite.getInt("ID");
			
				STR_queryDocumentos +=
					" OR " +
					"ID_TRAMITE_PCD="+tramIntervencionId;
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
