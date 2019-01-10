package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class StopProcessRule implements IRule {
    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(StopProcessRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        IItem exp = null;

        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            //----------------------------------------------------------------------------------------------

            ITXTransaction tx = invesFlowAPI.getTransactionAPI();
            
            String numExp = rulectx.getNumExp();
            // Cerramos el expediente
            exp = ExpedientesUtil.getExpediente(cct, numExp);
                
            if (null != exp) {
                int id = exp.getInt("ID");
                
                LOGGER.warn("Cerrando la solicitud de anuncio con ID = " + id);
                
                tx.closeProcess(id);
            }
            return Boolean.TRUE;
            
        }catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);

        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido cerrar la sesión",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
