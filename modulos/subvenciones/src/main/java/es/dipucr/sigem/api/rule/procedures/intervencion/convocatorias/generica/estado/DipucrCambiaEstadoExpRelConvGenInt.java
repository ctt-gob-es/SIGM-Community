package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica.estado;

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

public class DipucrCambiaEstadoExpRelConvGenInt implements IRule{
    
    private static final Logger LOGGER = Logger.getLogger(DipucrCambiaEstadoExpRelConvGenInt.class);
    
    protected String estadoIni;
    protected String estadofin;

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }
    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            //----------------------------------------------------------------------------------------------
            
            LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
            
            //Recuperamos los expedientes relacionados
            List<String> expedientesList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, estadoIni);
            
            for(String numexpHijo : expedientesList){
                IItem  expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo);

                if(expHijo != null && estadoIni.equals(expHijo.get(ExpedientesUtil.ESTADOADM))){
                    expHijo.set(ExpedientesUtil.ESTADOADM, estadofin);
                    expHijo.store(cct);
                }                            
            }
            
            LOGGER.info(ConstantesString.FIN +this.getClass().getName());
            
            return true;
        } catch (ISPACRuleException e){
            throw new ISPACRuleException(e);
        } catch(Exception e) {            
            throw new ISPACRuleException("No se ha podido obtener la lista de solicitudes",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}