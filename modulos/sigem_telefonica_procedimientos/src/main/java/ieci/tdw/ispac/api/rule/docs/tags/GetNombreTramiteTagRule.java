package ieci.tdw.ispac.api.rule.docs.tags;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import java.util.Iterator;

/**
 * Regla creada para ser llamada desde un tag en una plantilla.
 * Retorna el nombre del trámite en el que se encuentra el expediente.
 *
 */
public class GetNombreTramiteTagRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	
	        String nombreTramite = null;
	        String strQuery = "WHERE ID = "+ cct.getStateContext().getTaskId();
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_TRAMITES, strQuery);
	        
	        Iterator it = collection.iterator();
	        if (it.hasNext())
	        {
	        	nombreTramite = ((IItem)it.next()).getString("NOMBRE");
	        }
	        else
	        {
	        	//No encuentro el trámite. Probablemente se haya llamado a esta regla desde 
	        	//otra asociada al inicio de un trámite y aun no existe el taskId.
	        	//Pruebo a ver si se ha creado una variable de sesión NOMBRE_TRAMITE.
	        	nombreTramite = cct.getSsVariable("NOMBRE_TRAMITE");
	        }

	        return nombreTramite;
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al obtener el nombre del tramite.", e);
	    }     
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
