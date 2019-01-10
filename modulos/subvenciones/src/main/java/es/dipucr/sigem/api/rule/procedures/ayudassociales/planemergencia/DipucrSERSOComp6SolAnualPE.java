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

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComp6SolAnualPE implements IRule {

    public static final Logger LOGGER = Logger
            .getLogger(DipucrSERSOComp6SolAnualPE.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            String numexp = rulectx.getNumExp();
            int numSolicitudes = 0;
            int numSolicitudes2 = 0;
            
            double maxTrimestral1 = 0;
            double maxTrimestral2 = 0;
            double maxTrimestral3 = 0;
            double maxTrimestral4 = 0;

            double totalConcedido1 = 0;
            double totalConcedido2 = 0;
            double totalConcedido3 = 0;
            double totalConcedido4 = 0;
            
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem)solicitudIterator.next();
                numSolicitudes += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA1);
                numSolicitudes += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA2);
                numSolicitudes += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA3);
                numSolicitudes += SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NUMAYUDA4);

                maxTrimestral1 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE);
                maxTrimestral2 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE2);
                maxTrimestral3 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE3);
                maxTrimestral4 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE4);
                
                totalConcedido1 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO);
                totalConcedido2 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2);
                totalConcedido3 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3);
                totalConcedido4 = SubvencionesUtils.getDouble(solicitud, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4);
                
                if(totalConcedido1 > 0){
                    numSolicitudes2++;
                    if(totalConcedido1 > maxTrimestral1/2){
                        numSolicitudes2++;
                    }
                }
                if(totalConcedido2 > 0){
                    numSolicitudes2++;
                    if(totalConcedido2 > maxTrimestral2/2){
                        numSolicitudes2++;
                    }
                }
                if(totalConcedido3 > 0){
                    numSolicitudes2++;
                    if(totalConcedido3 > maxTrimestral3/2){
                        numSolicitudes2++;
                    }
                }
                if(totalConcedido4 > 0){
                    numSolicitudes2++;
                    if(totalConcedido4 > maxTrimestral4/2){
                        numSolicitudes2++;
                    }
                }
                
                if(numSolicitudes > 6 || numSolicitudes2 > 6){
                    SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.INDEXOF_6_SOL, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_6_SOL);
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
                
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
        return true;
    }

}