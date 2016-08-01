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
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOCompMaxAnualFamiliaPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOCompMaxAnualFamiliaPE.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        String numexp = "";
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------

            numexp = rulectx.getNumExp();
            int maxTrimestral = 0;

            int totalConcedido = 0;

            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem) solicitudIterator.next();
                String tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if(ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                    IItemCollection resolucionCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, numexp);
                    Iterator<?> resolucionIterator = resolucionCollection.iterator();
                    if (resolucionIterator.hasNext()) {
                        IItem resolucion = (IItem) resolucionIterator.next();
                        maxTrimestral += Double.parseDouble(resolucion.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE));
                        maxTrimestral += Double.parseDouble(resolucion.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE2));
                        maxTrimestral += Double.parseDouble(resolucion.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE3));
                        maxTrimestral += Double.parseDouble(resolucion.getString(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.MAXIMOSEMESTRE4));
        
                        totalConcedido += Double.parseDouble(StringUtils.isNotEmpty(resolucion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO)) ? resolucion
                                .getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO) : "0");
                        totalConcedido += Double.parseDouble(StringUtils.isNotEmpty(resolucion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2)) ? resolucion
                                .getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2) : "0");
                        totalConcedido += Double.parseDouble(StringUtils.isNotEmpty(resolucion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3)) ? resolucion
                                .getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3) : "0");
                        totalConcedido += Double.parseDouble(StringUtils.isNotEmpty(resolucion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4)) ? resolucion
                                .getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4) : "0");
        
                        if (maxTrimestral < totalConcedido) {
                            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                            if(expediente != null){
                                String asunto = expediente.getString("ASUNTO");
                                if (asunto.toUpperCase().indexOf("MÁXIMO FAMILIAR ANUAL") < 0) {
                                    asunto += " - AVISO EL BENEFICIARIO HA SUPERADO EL MÁXIMO FAMILIAR ANUAL.";
                                    expediente.set("ASUNTO", asunto);
        
                                    expediente.store(cct);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al calcular el máximo familiar anual en el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al calcular el máximo familiar anual en el expediente: " + numexp + ". " + e.getMessage(), e);
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