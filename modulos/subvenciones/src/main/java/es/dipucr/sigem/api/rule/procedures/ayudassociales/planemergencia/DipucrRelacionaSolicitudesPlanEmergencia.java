package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;

    public class DipucrRelacionaSolicitudesPlanEmergencia implements IRule{
        
        public static final Logger LOGGER = Logger.getLogger(DipucrRelacionaSolicitudesPlanEmergencia.class);
        
        public void cancel(IRuleContext rulectx) throws ISPACRuleException {
            
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
                
                IItemCollection solicitudCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, "WHERE NUMEXP='" +numexp+"'");
                Iterator<?> solicitudIterator = solicitudCollection.iterator();
                
                if(solicitudIterator.hasNext()){
                        IItem solicitud = (IItem) solicitudIterator.next();
                        nifBenef = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NIF);
                        numexpConvocatoria = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                        
                        //Buscamos todas las solicitudes que tiene este beneficiario en esta convocatoria
                        IItemCollection solicitudesBenefCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, "WHERE UPPER(NIF) = UPPER('" +nifBenef+"') AND CONVOCATORIA = '" +numexpConvocatoria+"' AND NUMEXP!='" +numexp+"' ORDER BY NUMEXP");
                        Iterator<?> solicitudesBenefIterator = solicitudesBenefCollection.iterator();
                        
                        while(solicitudesBenefIterator.hasNext()){
                            IItem solicitudBenf = (IItem) solicitudesBenefIterator.next();
                            String numexpSolicitudesBenef = solicitudBenf.getString("NUMEXP");
                                
                            IItem registro = entitiesAPI.createEntity(SpacEntities.SPAC_EXP_RELACIONADOS);
            
                            registro.set("NUMEXP_PADRE", numexpSolicitudesBenef);
                            registro.set("NUMEXP_HIJO", numexp);
                            registro.set("RELACION", ConstantesPlanEmergencia.RELACION_SOLICITUDES);
            
                            registro.store(cct);                
                        }//FIN solicitudesBenefIterator.hasNext()
                }//Fin solicitudesIteratro.hasHext()
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
