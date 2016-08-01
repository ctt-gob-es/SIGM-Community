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

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrAnotaValesImpresos implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrAnotaValesImpresos.class);

    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + DipucrAnotaValesImpresos.class);
        String numexpSol = "";
        IClientContext cct;
        try{
            //----------------------------------------------------------------------------------------------
            cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            IItemCollection relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXPEDIENTES, "WHERE NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE = '" +rulectx.getNumExp()+"') AND ESTADOADM='NE'");
            Iterator<?> relacionadosIterator = relacionadosCollection.iterator();
            while (relacionadosIterator.hasNext()){
                IItem expSol = (IItem) relacionadosIterator.next();
                numexpSol = expSol.getString("NUMEXP");
                
                if(expSol != null){
                    IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexpSol);
                    Iterator<?> solicitudIterator = solicitudCollection.iterator();
                    
                    String tipoAyuda = "";
                    String trimestre = "";
                    
                    double cantConcedida = 0;
                        
                    if (solicitudIterator.hasNext()) {
                        IItem solicitud = (IItem) solicitudIterator.next();
                        trimestre  = solicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);
                        tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                       
                        if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                            IItemCollection concedidoCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexpSol);
                            Iterator<?> concedidoIterator = concedidoCollection.iterator();
                            
                            if(concedidoIterator.hasNext()){
                                IItem concedido = (IItem) concedidoIterator.next();
                                
                                IItemCollection cantidadesCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexpSol);
                                Iterator<?> cantidadesIterator = cantidadesCollection.iterator();            
                                if (cantidadesIterator.hasNext()){
                                    IItem cantidades = (IItem) cantidadesIterator.next();
                                    cantConcedida = 0;
                                    if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                                        cantConcedida = Double.parseDouble(concedido.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO));
                                    } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                                        cantConcedida = Double.parseDouble(concedido.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2));
                                    } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                                        cantConcedida = Double.parseDouble(concedido.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3));
                                    } else{
                                        cantConcedida = Double.parseDouble(concedido.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4));
                                    }
                                                                            
                                    if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS,cantConcedida/30);
                                    } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS,cantConcedida/30);
                                    } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS,cantConcedida/30);
                                    } else{
                                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS,cantConcedida/30);
                                    }
                                    
                                    cantidades.store(cct);
                               }
                            }
                        }
                    }
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpSol + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpSol + ". " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpSol + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al anotar los vales del expediente: " + numexpSol + ". " + e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public boolean validate(IRuleContext paramIRuleContext)
            throws ISPACRuleException {
        return true;
    }

}
