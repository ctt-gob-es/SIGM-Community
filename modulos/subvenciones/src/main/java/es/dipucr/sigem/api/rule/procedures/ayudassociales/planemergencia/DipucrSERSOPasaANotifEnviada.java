package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOPasaANotifEnviada implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOPasaANotifEnviada.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexpHijoLogeo = "";
        try{
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            ClientContext cct = (ClientContext) rulectx.getClientContext();

            List<String> expedientesResolucion = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.AP);
            
            //Ya tenemos los expedientes que vamos a decretar en esta iteración
            for(String numexpHijo : expedientesResolucion){
                numexpHijoLogeo = numexpHijo;
                
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexpHijo);
                expediente.set(ExpedientesUtil.ESTADOADM, ExpedientesUtil.EstadoADM.NE);
                expediente.store(cct);
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al pasar a notificado el expediente: " + numexpHijoLogeo + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al pasar a notificado el expediente: " + numexpHijoLogeo + ". " + e.getMessage(), e);
        }
        return Boolean.TRUE;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}