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

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

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
                String tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if (ConstantesPlanEmergencia.ALIMENTACION.equals(tipoAyuda)){
                    trimestre = solicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);
    
                    //Recuperamos las cantidades del ayuntamiento en cuestión        
                    IItemCollection cantidadesCol = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSONVales.NOMBRE_TABLA, numexp);
                    Iterator<?> cantidadesIt = cantidadesCol.iterator();
                    while(cantidadesIt.hasNext()){                        
                        IItem cantidades = (IItem)cantidadesIt.next();
                        try{
                            primerTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE1IMPRESOS).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo ConstantesPlanEmergencia.SEMESTRE1IMPRESOS es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            primerTrim = 0;
                        }
                        try{
                            primerTrimMax = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE1).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo MAXSEMESTRE1 es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            primerTrimMax = 0;
                        }
                        try{                        
                            segundoTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE2IMPRESOS).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo SEMESTRE2IMPRESOS es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            segundoTrim = 0;
                        }
                        try{                        
                            segundoTrimMax = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE2).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo MAXSEMESTRE2 es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            segundoTrimMax = 0;
                        }
                        try{
                            tercerTrimMax = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE3).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo MAXSEMESTRE3 es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            tercerTrimMax = 0;
                        }
                        try{
                            tercerTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE3IMPRESOS).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo SEMESTRE3IMPRESOS es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            tercerTrim = 0;
                        }
                        try{
                            cuartoTrimMax = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.MAXSEMESTRE4).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo MAXSEMESTRE4 es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            cuartoTrimMax = 0;                            
                        }
                        try{
                            cuartoTrim = Double.parseDouble(cantidades.getString(ConstantesPlanEmergencia.DpcrSERSONVales.SEMESTRE4IMPRESOS).trim());
                        } catch(Exception e){
                            LOGGER.debug("El campo SEMESTRE4IMPRESOS es nulo, vacío o no es numérico. " + e.getMessage(), e);
                            cuartoTrim = 0;
                        }
                        
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
                        if(excedeMax){
                            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                            if (expediente != null){
                                String asunto = expediente.getString("ASUNTO");
                                if(pidenCero){
                                    if(asunto.indexOf(" - AVISO. Se han solicitado 0 euros.")<0){
                                        asunto += " - AVISO. Se han solicitado 0 euros.";
                                    }
                                } else{
                                    if(asunto.indexOf(" - AVISO. Se ha sobrepasado el límite familiar.")<0){
                                        asunto += " - AVISO. Se ha sobrepasado el límite familiar.";
                                    }
                                }
                                expediente.set("ASUNTO", asunto);
                                                        
                                expediente.store(cct);                            
                            }
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
        
    }

}