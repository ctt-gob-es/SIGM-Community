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

public class DipucrSERSOComp2SolLibrosComedor implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOComp2SolLibrosComedor.class);

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
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
            
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();                
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem)solicitudIterator.next();
                String nif = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                String tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                String convocatoria = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                
                if( StringUtils.isNotEmpty(tipoAyuda) && ( ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda))){
                    IItemCollection otrasSolicitudesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA,  "WHERE NUMEXP !='" + numexp + "' AND NIF = '" + nif + "' AND CONVOCATORIA = '" + convocatoria + "'"
                            + " AND NUMEXP NOT IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ESTADOADM='RC') AND NUMEXP NOT IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ESTADOADM='RC')");
                    numSolicitudes = otrasSolicitudesCollection.toList().size();
                    if(numSolicitudes > 0){
                        IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                        if(expediente != null){
                            String asunto = expediente.getString("ASUNTO");
                            if(asunto.toUpperCase().indexOf("UNA SOL") < 0){
                                asunto += " - AVISO EL BENEFICIARIO YA TIENE UNA SOLICITUD.";
                                expediente.set("ASUNTO", asunto);
                                                                
                                expediente.store(cct);
                            }
                        }
                    }
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar si se han presentado dos solicitudes para la misma ayuda. " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar si se han presentado dos solicitudes para la misma ayuda. " + e.getMessage(), e);
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