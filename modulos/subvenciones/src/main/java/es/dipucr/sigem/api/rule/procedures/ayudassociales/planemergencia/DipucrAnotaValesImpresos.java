package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrAnotaValesImpresos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrAnotaValesImpresos.class);

    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No puede darse este caso        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + DipucrAnotaValesImpresos.class);
        
        String numexpParaLogear = "";
        double cantConcedida = 0;
        
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            List<String> expedientesRelacionadosList = ExpedientesRelacionadosUtil.getExpedientesRelacionadosHijosByEstadoAdm(rulectx, ExpedientesUtil.EstadoADM.NE);
            
            for(String numexpSol : expedientesRelacionadosList){
                numexpParaLogear = numexpSol;
                cantConcedida = 0;
                
                IItemCollection solicitudCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, ConstantesString.WHERE + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP + " = '" + numexpSol + "' AND " + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + " = '" + ConstantesPlanEmergencia.ALIMENTACION + "'");
                Iterator<?> solicitudIterator = solicitudCollection.iterator();
                
                IItemCollection concedidoCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexpSol);
                Iterator<?> concedidoIterator = concedidoCollection.iterator();
                
                IItemCollection cantidadesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexpSol);
                Iterator<?> cantidadesIterator = cantidadesCollection.iterator();
                
                if (solicitudIterator.hasNext() && concedidoIterator.hasNext() && cantidadesIterator.hasNext()){
                    
                    IItem solicitud = (IItem) solicitudIterator.next();                               
                    IItem concedido = (IItem) concedidoIterator.next();                                
                    IItem cantidades = (IItem) cantidadesIterator.next();
                    
                    String trimestre  = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.TRIMESTRE);
                    
                    if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                        cantConcedida = SubvencionesUtils.getDouble(concedido, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS, cantConcedida/30);
                        
                    } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                        cantConcedida = SubvencionesUtils.getDouble(concedido, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS, cantConcedida/30);
                        
                    } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                        cantConcedida = SubvencionesUtils.getDouble(concedido, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS, cantConcedida/30);
                        
                    } else{
                        cantConcedida = SubvencionesUtils.getDouble(concedido, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS, cantConcedida/30);
                    }
                                                            
                    cantidades.store(cct);
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpParaLogear + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpParaLogear + ". " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpParaLogear + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpParaLogear + ". " + e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public boolean validate(IRuleContext paramIRuleContext) throws ISPACRuleException {        
        return true;
    }

}
