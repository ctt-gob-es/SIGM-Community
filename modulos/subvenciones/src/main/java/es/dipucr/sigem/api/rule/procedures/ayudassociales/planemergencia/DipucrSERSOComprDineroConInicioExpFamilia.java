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

public class DipucrSERSOComprDineroConInicioExpFamilia implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroConInicioExpFamilia.class);

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
            String trimestre = "";
            
            double primerTrimMax;
            double primerTrim;
            double segundoTrimMax;
            double segundoTrim;
            double tercerTrimMax;
            double tercerTrim;
            double cuartoTrimMax;
            double cuartoTrim;
            
            boolean excedeMax = false;
            boolean pidenCero = false;
        
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();  
            
            if (solicitudIterator.hasNext()){
                IItem solicitud = (IItem)solicitudIterator.next();
                
                String tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if (ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                    trimestre = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.TRIMESTRE);
    
                    //Recuperamos las cantidades del ayuntamiento en cuestión        
                    IItemCollection cantidadesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);
                    Iterator<?> cantidadesIt = cantidadesCol.iterator();
                    
                    while(cantidadesIt.hasNext()){                        
                        IItem cantidades = (IItem)cantidadesIt.next();
                        
                        primerTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS);
                        primerTrimMax = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1);

                        segundoTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS);
                        segundoTrimMax = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2);

                        tercerTrimMax = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3);
                        tercerTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS);

                        cuartoTrimMax = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4);
                        cuartoTrim = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS);
                        
                        excedeMax = false;
                        pidenCero = false;
                        
                        if(ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)){
                            if(primerTrim >= primerTrimMax){
                                excedeMax = true;
                                if(primerTrim == 0){
                                    pidenCero = true;
                                }
                            }
                        } else if(ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)){
                            if(segundoTrim >= segundoTrimMax) {
                                excedeMax = true;
                                if(segundoTrim == 0){
                                    pidenCero = true;
                                }
                            }
                        } else if(ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)){
                            if(tercerTrim >= tercerTrimMax) {
                                excedeMax = true;
                                if(tercerTrim == 0){
                                    pidenCero = true;
                                }
                            }
                        } else{
                            if(cuartoTrim >= cuartoTrimMax){
                                excedeMax = true;
                                if(cuartoTrim == 0){
                                    pidenCero = true;
                                }
                            }
                        }                    
                        if(excedeMax && pidenCero){
                                SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_PIDEN_0_EUROS);
                        } else if (excedeMax){
                            SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_LIMITE_FAMILIAR);
                        }
                    }    
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
            
        } catch (ISPACRuleException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el límite familiar al iniciar el expediente " + numexp + ". " + e.getMessage(), e);
            throw new ISPACRuleException(e);
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el límite familiar al iniciar el expediente " + numexp + ". " + e.getMessage(), e);            
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el límite familiar al iniciar el expediente " + numexp + ". " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}
