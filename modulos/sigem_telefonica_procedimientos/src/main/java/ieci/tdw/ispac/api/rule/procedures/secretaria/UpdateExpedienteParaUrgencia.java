package ieci.tdw.ispac.api.rule.procedures.secretaria;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class UpdateExpedienteParaUrgencia implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        //Copiado de los datos de urgencia a la propuesta
	        IItem urgencia = rulectx.getItem();
	        String numexp_prop = urgencia.getString("NUMEXP_ORIGEN");
	        String strQuery = "WHERE NUMEXP = '" + numexp_prop + "'";
	        IItemCollection coll = entitiesAPI.queryEntities("SECR_PROPUESTA", strQuery);
	        Iterator it = coll.iterator();
	        IItem iProp = null;
	        if (it.hasNext()) {
	        	iProp = (IItem)it.next();
	        	iProp.set("EXTRACTO", urgencia.getString("EXTRACTO"));
				iProp.set("ORIGEN", urgencia.getString("ORIGEN"));
				iProp.set("DESTINO", urgencia.getString("DESTINO"));
				iProp.set("PRIORIDAD", urgencia.getString("PRIORIDAD"));
				iProp.set("PRIORIDAD_MOTIVO", urgencia.getString("PRIORIDAD_MOTIVO"));
				iProp.set("ORDEN", urgencia.getString("ORDEN"));
				iProp.set("EXTRACTO", urgencia.getString("EXTRACTO"));
				iProp.set("NOTAS", urgencia.getString("NOTAS"));
				iProp.set("DEBATE", urgencia.getString("DEBATE"));
				iProp.set("ACUERDOS", urgencia.getString("ACUERDOS"));
				iProp.set("DICTAMEN", urgencia.getString("DICTAMEN"));
				iProp.set("N_SI", urgencia.getString("N_SI"));
				iProp.set("N_NO", urgencia.getString("N_NO"));
				iProp.set("N_ABS", urgencia.getString("N_ABS"));
				iProp.store(cct);
	        }
        	return new Boolean(true);
    		
        } catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se ha podido actualizar la propuesta",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}