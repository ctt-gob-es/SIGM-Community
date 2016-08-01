package es.dipucr.contratacion.rule;

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

public class GetNombreProcedimientoPadre implements IRule{

	public void cancel(IRuleContext arg0) throws ISPACRuleException {		
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        String nombreProcedimiento = "";
	        String numexp_padre = "";
	        String consulta = "WHERE NUMEXP_PADRE='"+rulectx.getNumExp()+"'";
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consulta);
	        
	        Iterator<IItem> it = collection.iterator();
	        if (it.hasNext())
	        {
	        	numexp_padre = ((IItem)it.next()).getString("NUMEXP_HIJO");
	        }

	        IItem expPadre = entitiesAPI.getExpedient(numexp_padre);
	        
	        if (expPadre != null)
	        {
	        	nombreProcedimiento = expPadre.getString("ASUNTO");
	        }

	        return nombreProcedimiento;
	        
	    } catch (Exception e) {
	        throw new ISPACRuleException("Error al obtener el nombre del tramite.", e);
	    }     
	}

	public boolean init(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
