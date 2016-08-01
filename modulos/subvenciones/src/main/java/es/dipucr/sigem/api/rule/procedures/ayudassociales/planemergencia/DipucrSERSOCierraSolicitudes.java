package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrSERSOCierraSolicitudes implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOCierraSolicitudes.class);
    

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        IClientContext cct;
        try{
            //----------------------------------------------------------------------------------------------
            cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
            String numexp = rulectx.getNumExp();
            String numexpSolicitud = "";
            
            try{
                IItemCollection solicitudRelCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, "WHERE NUMEXP_PADRE='" + numexp + "'");
                Iterator<?> solicitudRelIterator = solicitudRelCollection.iterator();
                while (solicitudRelIterator.hasNext()){
                    IItem solicitudRel = (IItem) solicitudRelIterator.next();
                    numexpSolicitud = solicitudRel.getString("NUMEXP_HIJO");
                    
                    IItem solicitudExp = ExpedientesUtil.getExpediente(cct,  numexpSolicitud);
                    if(solicitudExp != null ){
                        String estadoadm=solicitudExp.getString("ESTADOADM");
                        if(null != estadoadm && ("NT".equals(estadoadm) || "RC".equals(estadoadm))){
                            try{                                
                                ExpedientesUtil.avanzarFase(cct, numexpSolicitud);
                                //Intentamos avanzar cuatro veces la fase para asegurarnos que se cierra el expediente
                                try{
                                    ExpedientesUtil.avanzarFase(cct, numexpSolicitud);
                                    try{
                                        ExpedientesUtil.avanzarFase(cct, numexpSolicitud);
                                        try{
                                            ExpedientesUtil.avanzarFase(cct, numexpSolicitud);
                                        } catch(Exception e){
                                            LOGGER.debug("No se ha podido avanzar la fase del expediente: " + numexpSolicitud + ". " + e.getMessage(), e);
                                        }
                                    } catch(Exception e){
                                        LOGGER.debug("No se ha podido avanzar la fase del expediente: " + numexpSolicitud + ". " + e.getMessage(), e);
                                    }
                                } catch(Exception e){
                                    LOGGER.debug("No se ha podido avanzar la fase del expediente: " + numexpSolicitud + ". " + e.getMessage(), e);
                                }
                            } catch(Exception e){
                                LOGGER.debug("No se ha podido avanzar la fase del expediente: " + numexpSolicitud + ". " + e.getMessage(), e);
                            }
                        }
                    }
                }
            } catch(Exception e){
                LOGGER.error(ConstantesString.LOGGER_ERROR + " al al cerrar el expediente relacionado: " + numexpSolicitud + " del expediente: " +numexp, e);
                throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al al cerrar el expediente relacionado: " + numexpSolicitud + " del expediente: " +numexp, e);
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

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }
}