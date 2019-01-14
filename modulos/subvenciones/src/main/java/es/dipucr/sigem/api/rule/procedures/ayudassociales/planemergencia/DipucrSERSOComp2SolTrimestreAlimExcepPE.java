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
import es.dipucr.sigem.api.rule.common.utils.QueryUtils;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComp2SolTrimestreAlimExcepPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOComp2SolTrimestreAlimExcepPE.class);
    
    public static final String MENSAJE_AVISO = " - AVISO EL BENEFICIARIO YA TIENE UNA SOLICITUD EN EL TRIMESTRE";
    public static final String MENSAJE_ERROR = " - No se ha podido comprobar si hay dos solicitudes en el mismo trimestre para el expediente: ";

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
            String nifBenef = "";
            String trimestre = "";
            
            int numSolicitudes = 0;
            
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator(); 
            
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem)solicitudIterator.next();
                
                nifBenef = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                trimestre = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TRIMESTRE);
                
                //Buscamos todas las solicitudes que tiene este beneficiario en esta convocatoria
                IItemCollection solicitudesBenefCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, 
                        ConstantesString.WHERE + " UPPER(" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF + ") = UPPER('" + nifBenef + "')"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TRIMESTRE + " = '" + trimestre + "'"                        
                        + " AND SUBSTR(" + ExpedientesUtil.NUMEXP + ", 10)::INT < SUBSTR('" +numexp+"', 10)::INT"
                        + " AND SUBSTR(" + ExpedientesUtil.NUMEXP + ", 5, 4)::INT = SUBSTR('" +numexp+"', 5, 4)::INT"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + " != '" + ConstantesPlanEmergencia.COMEDOR + "'"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + " != '" + ConstantesPlanEmergencia.LIBROS + "'"
                        + ConstantesString.AND + ExpedientesUtil.NUMEXP + " NOT IN ( SELECT " + ExpedientesUtil.NUMEXP + ConstantesString.FROM + ExpedientesUtil.SPAC_EXPEDIENTES + ConstantesString.WHERE + ExpedientesUtil.ESTADOADM + " = '" + ExpedientesUtil.EstadoADM.RC + "')"
                        + ConstantesString.AND + ExpedientesUtil.NUMEXP + " NOT IN ( SELECT " + ExpedientesUtil.NUMEXP + ConstantesString.FROM + ExpedientesUtil.SPAC_EXPEDIENTES_H + ConstantesString.WHERE + ExpedientesUtil.ESTADOADM + " = '" + ExpedientesUtil.EstadoADM.RC + "')"
                        + " ORDER BY " + QueryUtils.NUMEXP_YEAR + " DESC, " + QueryUtils.NUMEXP_NUMBER + " DESC");
                
                numSolicitudes = solicitudesBenefCollection.toList().size();
                if(numSolicitudes > 0){
                    String numexpSolAnterior = "";
                    IItem expedienteRepe = (IItem) solicitudesBenefCollection.iterator().next();
                    
                    if(null != expedienteRepe){
                        numexpSolAnterior = SubvencionesUtils.getString(expedienteRepe, ExpedientesUtil.NUMEXP);                    
                    }
                    
                    if(StringUtils.isNotEmpty(numexpSolAnterior)){
                         numexpSolAnterior = " CON NÚMERO DE EXPEDIENTE: " + numexpSolAnterior + ".";
                    } else{
                        numexpSolAnterior = ".";
                    }
                    
                    SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, MENSAJE_AVISO, MENSAJE_AVISO + numexpSolAnterior);
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + MENSAJE_ERROR + numexp + ". " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + MENSAJE_ERROR + numexp + ". " + e.getMessage(), e);
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
