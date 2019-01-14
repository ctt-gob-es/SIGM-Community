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
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComp2SolLibrosComedorExcep implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOComp2SolLibrosComedorExcep.class);

    public static final String MENSAJE_AVISO = " - AVISO EL BENEFICIARIO YA TIENE %s SOLICITUD/ES MÁS.";
    public static final String MENSAJE_ERROR = " - No se ha podido comprobar si se han presentado dos solicitudes para la misma ayuda para el expediente %s.";

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        String numexp = "";
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            numexp = rulectx.getNumExp();
            int numSolicitudes = 0;
            
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();                
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem)solicitudIterator.next();
                
                String nif = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                String tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                String convocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                
                if( StringUtils.isNotEmpty(tipoAyuda) && (ConstantesPlanEmergencia.EXCEPCIONAL.equals(tipoAyuda) || ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda))){
                    IItemCollection otrasSolicitudesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, 
                            ConstantesString.WHERE + ExpedientesUtil.NUMEXP + " !='" + numexp + "'"
                            + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF + " = '" + nif + "'"
                            + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA + " = '" + convocatoria + "'"
                            + ConstantesString.AND + ExpedientesUtil.NUMEXP + " NOT IN (SELECT " + ExpedientesUtil.NUMEXP + ConstantesString.FROM + ExpedientesUtil.SPAC_EXPEDIENTES + ConstantesString.WHERE + ExpedientesUtil.ESTADOADM + "='" + ExpedientesUtil.EstadoADM.RC + "')"
                            + ConstantesString.AND + ExpedientesUtil.NUMEXP + " NOT IN (SELECT " + ExpedientesUtil.NUMEXP + ConstantesString.FROM + ExpedientesUtil.SPAC_EXPEDIENTES_H + ConstantesString.WHERE + ExpedientesUtil.ESTADOADM + "='" + ExpedientesUtil.EstadoADM.RC + "')");
                    numSolicitudes = otrasSolicitudesCollection.toList().size();
                    
                    if(numSolicitudes > 0){
                        String textoAviso = String.format(MENSAJE_AVISO, numSolicitudes);
                        
                        SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, textoAviso, textoAviso);
                    }
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + String.format(MENSAJE_ERROR, numexp) + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + String.format(MENSAJE_ERROR, numexp) + e.getMessage(), e);
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