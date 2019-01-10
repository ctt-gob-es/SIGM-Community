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
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComprDineroConInicioExp implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroConInicioExp.class);

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
            String convocatoria = "";
            String ciudad = "";
            String trimestre = "";
        
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            
            if (solicitudIterator.hasNext()){
                IItem solicitud = (IItem)solicitudIterator.next();
                
                String tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if (ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                    
                    convocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                    ciudad = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                    trimestre = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.TRIMESTRE);
                   
                    int importe = 0;
                    importe += SubvencionesUtils.getInt(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                    importe += SubvencionesUtils.getInt(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA2_IMPORTE);
    
                    //Recuperamos las cantidades del ayuntamiento en cuestión        
                    IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, ConstantesString.WHERE + " LOCALIDAD = '" +ciudad+"' AND NUMEXPCONVOCATORIA = '" +convocatoria+"'");
                    Iterator<?> cantidadesIt = cantidadesCol.iterator();
                    
                    while(cantidadesIt.hasNext()){                        
                        IItem cantidades = (IItem)cantidadesIt.next();
                        
                        double primerTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE);
                        double segundoTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE); 
                        double tercerTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE);
                        double cuartoTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE);
                        
                        if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                            primerTrim += importe;
                        } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                            segundoTrim += importe;
                        } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                            tercerTrim += importe;
                        } else{
                            cuartoTrim += importe;
                        }                    
                      
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE, "" + primerTrim);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE, "" + segundoTrim); 
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE, "" + tercerTrim);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE, "" + cuartoTrim);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTAL, "" +(primerTrim + segundoTrim + tercerTrim + cuartoTrim));
                        
                        cantidades.store(cct);
                    }    
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
            
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido al iniciar el expediente " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido al iniciar el expediente " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}