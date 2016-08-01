package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica.estado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrCambiaEstadoExpRelConvGenInt implements IRule{
    private static final Logger LOGGER = Logger
            .getLogger(DipucrCambiaEstadoExpRelConvGenInt.class);
    
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
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
            String numexp = rulectx.getNumExp();
            
            //Recuperamos los expedientes relacionados
            String strQuery = "WHERE NUMEXP_PADRE='" + numexp + "'";
            IItemCollection expRelCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, strQuery);
            Iterator<?> expRelIt = expRelCol.iterator();                  
            if(expRelIt.hasNext()){
                while (expRelIt.hasNext()){
                    IItem expRel = (IItem)expRelIt.next();
                    //Solo trabajamos con aquellos expedientes en estado RESOLUCION - RS
                    String numexpHijo = expRel.getString("NUMEXP_HIJO");
                    
                    //Comprobamos que sea un hijo de la convocatoria
                    IItemCollection expSolHijoCol = entitiesAPI.getEntities("DPCR_SOL_CONV_SUB", numexpHijo);
                    Iterator<?> expSolHijoIterator = expSolHijoCol.iterator();
                    
                    if(expSolHijoIterator.hasNext()){
                        IItem expSolHijo = (IItem) expSolHijoIterator.next();
                        
                        String numexpConvSolicitud = expSolHijo.getString("NUMEXP_PADRE");
                        if(numexpConvSolicitud != null && numexpConvSolicitud.equals(numexp)){
                            IItem  expHijo = ExpedientesUtil.getExpediente(cct, numexpHijo);                             
                            if(expHijo != null && expHijo.get("ESTADOADM").equals(estadoIni)){
                                expHijo.set("ESTADOADM", estadofin);
                                expHijo.store(cct);
                            }                            
                        }
                    }
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
        
    }

}