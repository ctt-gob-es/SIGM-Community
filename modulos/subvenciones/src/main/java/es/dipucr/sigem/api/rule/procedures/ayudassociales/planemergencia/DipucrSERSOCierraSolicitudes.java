package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOCierraSolicitudes implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOCierraSolicitudes.class);
    
    protected String[] estadosAdm = {ExpedientesUtil.EstadoADM.NT, ExpedientesUtil.EstadoADM.RC};
    

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        IClientContext cct;
        try{            
            LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
            
            cct = rulectx.getClientContext();
            
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByVariosEstadosAdm(rulectx, Arrays.asList(estadosAdm));
            
            for(String numexpSolicitud : expedientesList){
                avanza4Fases(cct, numexpSolicitud, 0);                
            }
           
            LOGGER.info(ConstantesString.FIN +this.getClass().getName());
            return true;
        } catch (ISPACRuleException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al enviar las notificaciones a su solicitud.", e);
            throw new ISPACRuleException(e);
        } catch(Exception e) {            
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }
    
    public void avanza4Fases(IClientContext cct, String numexp, int iteracion){
         try{
             if(iteracion < 5){
                 ExpedientesUtil.avanzarFase(cct, numexp);
                 avanza4Fases(cct, numexp, iteracion+1);
             }
         } catch(Exception e){
             LOGGER.debug("No se ha podido avanzar la fase del expediente: " + numexp + ". " + e.getMessage(), e);
         }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}