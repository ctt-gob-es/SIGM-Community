package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSORestaDineroAlRetroceder implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSORestaDineroAlRetroceder.class);

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
                String tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if (ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                    convocatoria = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                    ciudad = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                    trimestre = solicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);
                                        
                    String importe1String = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA1_IMPORTE);
                    String importe2String = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.PROPUESTA2_IMPORTE);
                    int importe = 0;
                    if(StringUtils.isNotEmpty(importe1String)){
                        try{
                            importe += Integer.parseInt(importe1String);
                        } catch(Exception e){
                            LOGGER.debug("El campo PROPUESTA1_IMPORTE es nulo, vacío o no numérico. " + e.getMessage(), e);
                        }
                    }
                    if(StringUtils.isNotEmpty(importe2String)){
                        try{
                            importe += Integer.parseInt(importe2String);
                        } catch(Exception e){
                            LOGGER.debug("El campo PROPUESTA2_IMPORTE es nulo, vacío o no numérico. " + e.getMessage(), e);
                        }
                    }
                    
                    //Recuperamos las cantidades del ayuntamiento en cuestión
                    IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, " WHERE LOCALIDAD = '" +ciudad+"' AND NUMEXPCONVOCATORIA = '" +convocatoria+"'");
                    Iterator<?> cantidadesIt = cantidadesCol.iterator();
                    if(cantidadesIt.hasNext()){                        
                        IItem cantidades = (IItem)cantidadesIt.next();
                        double primerTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE));
                        double segundoTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE)); 
                        double tercerTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE));
                        double cuartoTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE));
                                                   
                        if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                            primerTrim -= importe;
                        } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                            segundoTrim -= importe;
                        } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                            tercerTrim -= importe;
                        } else{
                            cuartoTrim -= importe;
                        }
                        
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.PRIMERTRIMESTRE, "" + primerTrim);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.SEGUNDOTRIMESTRE, "" + segundoTrim); 
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TERCERTRIMESTRE, "" + tercerTrim);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.CUARTOTRIMESTRE, "" + cuartoTrim);
                        cantidades.set(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTAL, "" + (primerTrim + segundoTrim + tercerTrim + cuartoTrim));
                        cantidades.store(cct);
                    }                        
                }    
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
            
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al restar dinero del plan de emergencia al retroceder el expediente: " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al restar dinero del plan de emergencia al retroceder el expediente: " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}