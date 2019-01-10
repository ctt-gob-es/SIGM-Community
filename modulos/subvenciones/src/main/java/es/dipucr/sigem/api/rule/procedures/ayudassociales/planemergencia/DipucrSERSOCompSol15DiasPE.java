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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.QueryUtils;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOCompSol15DiasPE implements IRule {

    public static final Logger LOGGER = Logger.getLogger(DipucrSERSOCompSol15DiasPE.class);

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
            String numexpConvocatoria = "";
            
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            
            if (solicitudIterator.hasNext()) {                
                IItem solicitud = (IItem)solicitudIterator.next();
                
                nifBenef = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                numexpConvocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                
                //Buscamos todas las solicitudes que tiene este beneficiario en esta convocatoria
                IItemCollection solicitudesBenefCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, 
                        ConstantesString.WHERE + " UPPER(" + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF + ") = UPPER('" +nifBenef+"')"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA + " = '" +numexpConvocatoria+"'"
                        + " AND SUBSTR(NUMEXP, 10)::INT < SUBSTR('" +numexp+"', 10)::INT"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + " != '" + ConstantesPlanEmergencia.EXCEPCIONAL + "'"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + " != '" + ConstantesPlanEmergencia.COMEDOR + "'"
                        + ConstantesString.AND + ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA + " != '" + ConstantesPlanEmergencia.LIBROS + "'"
                        + " AND NUMEXP NOT IN ( SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ESTADOADM ='RC')"
                        + " AND NUMEXP NOT IN ( SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ESTADOADM ='RC')"
                        + " ORDER BY " + QueryUtils.NUMEXP_YEAR + " DESC, " + QueryUtils.NUMEXP_NUMBER + " DESC");
                
                Iterator<?> solicitudesBenefIterator = solicitudesBenefCollection.iterator();
                
                //Si tenemos solicitud anterior comprobamos lo de los 15 días
                if(solicitudesBenefIterator.hasNext()){
                    
                    IItem solicitudBenf = (IItem) solicitudesBenefIterator.next();
                    
                    String numexpSolicitudesBenef = SubvencionesUtils.getString(solicitudBenf, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NUMEXP);
                    
                    IItem expedienteAnt = ExpedientesUtil.getExpediente(cct, numexpSolicitudesBenef);
                    
                    Date fechaRegAnterior = null;
                    if(null != expedienteAnt){
                        fechaRegAnterior = SubvencionesUtils.getDate(expedienteAnt, ExpedientesUtil.FREG);                    
                    }
                    if(null != fechaRegAnterior){
                        IItem expediente = ExpedientesUtil.getExpediente(cct,  numexp);
                        
                        if (expediente != null){
                            Date fechaReg = SubvencionesUtils.getDate(expediente, ExpedientesUtil.FREG);
                            
                            GregorianCalendar fechaRegAnteriorCalendar = new GregorianCalendar();
                            fechaRegAnteriorCalendar.setTime(fechaRegAnterior);
                            
                            GregorianCalendar fechaRegCalendar = new GregorianCalendar();
                            fechaRegCalendar.setTime(fechaReg);
                            
                            int diasSolicitud = fechaRegCalendar.get(Calendar.DAY_OF_YEAR) - fechaRegAnteriorCalendar.get(Calendar.DAY_OF_YEAR);
                            
                            //Solicitan que sean dos solicitudes en 30 días
                            if(diasSolicitud <= 30){
                                String textoAsunto = ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_30_DIAS + " (" +numexpSolicitudesBenef+")";
                                SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.INDEXOF_30_DIAS, textoAsunto);
                            }
                        }
                    }
                }
            }            
        } catch (ISPACException e) {
            LOGGER.error("No se ha podido comprobar si hay dos solicitudes en menos del 15 días para el expediente: " + numexp + ". " + e.getMessage(), e);
        } catch(Exception e){
            LOGGER.error("No se ha podido comprobar si hay dos solicitudes en menos del 15 días para el expediente: " + numexp + ". " + e.getMessage(), e);
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
