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

import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOComprTrabSoc implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprTrabSoc.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        String numexp = "";
        try{
            //----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            
            numexp = rulectx.getNumExp();
            String nifTrabSol = "";
            boolean encontrado = false;
            IItem solicitud = null;
            
            //Recuperamos el trabajador social de la solicitud
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();                
            if (solicitudIterator.hasNext()){
                solicitud = (IItem)solicitudIterator.next();
                nifTrabSol = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DOCUMENTOIDENTIDAD);
                if(nifTrabSol == null){
                    nifTrabSol = "";
                }
                
                //Recuperamos la lista de trabajadores sociales
                IItemCollection trabajadoresCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOTrabSocial.NOMBRE_TABLA, "WHERE 1=1");
                Iterator<?> trabajadoresIterator = trabajadoresCollection.iterator();                
                while(!encontrado && trabajadoresIterator.hasNext()){
                    IItem trabajador = (IItem) trabajadoresIterator.next();
                    String nifTrabajador = trabajador.getString(ConstantesPlanEmergencia.DpcrSERSOTrabSocial.DNI);
                    if(nifTrabajador != null && nifTrabajador.trim().equalsIgnoreCase(nifTrabSol.trim())){
                        encontrado = true;
                    }
                }
                if(encontrado){
                    solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.ESTRABAJADOR, "true");
                } else{
                    solicitud.set(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.ESTRABAJADOR, "false");
                }
                solicitud.store(cct);
            } 
            
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return encontrado;
            
        } catch(Exception e) {
            LOGGER.error("No se ha podido validar el trabajador social del expediente: " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido validar el trabajador social del expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}