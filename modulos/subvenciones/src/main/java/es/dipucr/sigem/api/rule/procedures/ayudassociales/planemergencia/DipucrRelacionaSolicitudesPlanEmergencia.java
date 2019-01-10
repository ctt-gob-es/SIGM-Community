package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

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

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

    public class DipucrRelacionaSolicitudesPlanEmergencia implements IRule{
        
        public static final Logger LOGGER = Logger.getLogger(DipucrRelacionaSolicitudesPlanEmergencia.class);
        
        public void cancel(IRuleContext rulectx) throws ISPACRuleException {
            //No se da nunca este caso
        }

        //Relacionamos la solicitud con todas las solicitudes que ya tiene el beneficiario
        
        public Object execute(IRuleContext rulectx) throws ISPACRuleException {
            String numexp = "";
            try            {
                LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
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
                
                if(solicitudIterator.hasNext()){
                    IItem solicitud = (IItem) solicitudIterator.next();
                    
                    nifBenef = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                    numexpConvocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                    
                    //Buscamos todas las solicitudes que tiene este beneficiario en esta convocatoria
                    IItemCollection solicitudesBenefCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, ConstantesString.WHERE + " UPPER(NIF) = UPPER('" +nifBenef+"') AND CONVOCATORIA = '" +numexpConvocatoria+"' AND NUMEXP!='" +numexp+"' ORDER BY NUMEXP");
                    Iterator<?> solicitudesBenefIterator = solicitudesBenefCollection.iterator();
                    
                    while(solicitudesBenefIterator.hasNext()){
                        IItem solicitudBenf = (IItem) solicitudesBenefIterator.next();
                        String numexpSolicitudesBenef = SubvencionesUtils.getString(solicitudBenf, "NUMEXP");
                        
                        ExpedientesRelacionadosUtil.relacionaExpedientes(cct, numexpSolicitudesBenef, numexp, ConstantesPlanEmergencia.RELACION_SOLICITUDES);
                    }
                }
                LOGGER.info(ConstantesString.FIN + this.getClass().getName());
                return Boolean.TRUE;
            } catch(Exception e){
                LOGGER.error("No se ha podido relacionar las solicitudes del mismo beneficiario, expediente: " + numexp + ". ",e);
                throw new ISPACRuleException("No se ha podido relacionar las solicitudes del mismo beneficiario, expediente: " + numexp + ". ",e);
            }
        }

        public boolean init(IRuleContext rulectx) throws ISPACRuleException {
            return true;
        }

        public boolean validate(IRuleContext rulectx) throws ISPACRuleException { 
            return true;
        }

    }
