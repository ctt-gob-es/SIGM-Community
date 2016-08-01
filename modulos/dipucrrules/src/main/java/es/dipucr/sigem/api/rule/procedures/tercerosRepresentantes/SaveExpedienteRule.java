package es.dipucr.sigem.api.rule.procedures.tercerosRepresentantes;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

public class SaveExpedienteRule  implements IRule {
	
	/** Logger de la clase. */
	protected static final Logger logger = Logger.getLogger(SaveExpedienteRule.class);
	
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
    	try{
	    	//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
		
			IItem datosFormulario = rulectx.getItem();
			String nifciftitular = datosFormulario.getString("NIFCIFTITULAR");
			
			String consulta = "WHERE NIFCIFTITULAR='"+nifciftitular+"' AND (CODPROCEDIMIENTO='CR-TERC-01' OR CODPROCEDIMIENTO='PCD-222' OR CODPROCEDIMIENTO='PCD-223')";
	        IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXPEDIENTES, consulta);
	        if (collection.toList().size()<1)
	        {
	        	return true;
	        }
	        else{
	        	rulectx.setInfoMessage("Este representado ya existe");
	        	return false;
	        }
	        
		}catch(Exception e) {
        	if (e instanceof ISPACRuleException)
        	{
			    throw new ISPACRuleException(e);
        	}
        	throw new ISPACRuleException("No se ha podido inicializar la propuesta.",e);
        }
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		return rulectx;
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }
	
}
