package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

/**
 * [dipucr-Felipe #869]
 * Borra el informe de secretaría
 */
public class DeleteNumInformeSecretariaRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(DeleteNumInformeSecretariaRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
    	
        try{
        	IClientContext cct = rulectx.getClientContext();
        	IInvesflowAPI invesflowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

            String numexp = rulectx.getNumExp();
            
            IItemCollection colInformes = entitiesAPI.getEntities("INFORME_SECR", numexp);
            
            if (colInformes.next()){
            	IItem itemNumInforme = colInformes.value();
            	itemNumInforme.delete(cct);
            }
            
            return new Boolean(true);
            
        } catch(Exception e){
        	String error = "Error al borrar el número de informe de secretaría";
        	LOGGER.error(error, e);
            throw new ISPACRuleException(error, e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
    }
}
