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

import es.dipucr.sigem.api.rule.common.utils.FechasUtil;

/**
 * [dipucr-Felipe #869]
 * Devuelve el número de informe de secretaría
 */
public class GetNumInformeSecretariaRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(GetNumInformeSecretariaRule.class);

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
            
            int anioActual = FechasUtil.getAnyoActual();
            String query = "WHERE ANIO = '" + anioActual + "' ORDER BY NUMERO DESC";
            
            IItemCollection colInformes = entitiesAPI.queryEntities("INFORME_SECR", query);
            
            int numero = 1;
            if (colInformes.next()){
            	IItem itemNumInforme = colInformes.value();
            	int ultimoNumero = itemNumInforme.getInt("NUMERO");
            	numero = ultimoNumero + 1;
            }
            
            String numexp = rulectx.getNumExp();
            String asunto = "Informe Nº " + anioActual + "/" + numero;
            
            IItem itemNumInforme = entitiesAPI.createEntity("INFORME_SECR", numexp);
            itemNumInforme.set("TITULO", asunto);
            itemNumInforme.set("ANIO", anioActual);
            itemNumInforme.set("NUMERO", numero);
            itemNumInforme.store(cct);
            
            IItem itemExpediente = entitiesAPI.getExpedient(numexp);
            itemExpediente.set("ASUNTO", asunto);
            itemExpediente.store(cct);
                        
            return new Boolean(true);
            
        } catch(Exception e){
        	String error = "Error al crear el número de informe de secretaría";
        	LOGGER.error(error, e);
            throw new ISPACRuleException(error, e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
    }
}
