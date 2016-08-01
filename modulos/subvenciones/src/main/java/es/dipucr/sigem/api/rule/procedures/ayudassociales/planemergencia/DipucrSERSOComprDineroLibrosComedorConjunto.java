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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOComprDineroLibrosComedorConjunto implements IRule{
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroLibrosComedorConjunto.class);

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
            
            double importeSol = 0;
            double maximoMunicipio = 0;

            String tipoAyuda = "";
            String columna = "";
            String convocatoria = "";
            String otraConvocatoria = "";
            String ciudad = "";
        
            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();                
            if (solicitudIterator.hasNext()){
                IItem solicitud = (IItem)solicitudIterator.next();
                tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if(ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)){
                                
                    convocatoria = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                    ciudad = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                    
                    //Recuperamos el expediente de la otra convocatoria
                    if(ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda)){
                        try{
                            otraConvocatoria = getOtraConvocatoria(entitiesAPI, "CONVOCATORIALIBROS", convocatoria, "CONVOCATORIACOMEDOR");
                            if(StringUtils.isEmpty(otraConvocatoria)){
                                throw new ISPACRuleException("No se ha encontrado el número de expediente de la otra convocatoria. Convocatoria actual: " + convocatoria);
                            }
                        } catch(Exception e){
                            LOGGER.error("No se ha podido recuperar el expediente de la otra convocatoria, no se realizará la comprobación. " + e.getMessage(), e);
                            anotaAviso(cct, entitiesAPI, numexp, " - AVISO. No se ha podido comprobar si se supera el máximo de ambas convocatorias conjuntas, revise que estén indicadas en el mantenimiento de trabajadores sociales.");
                        }
                    } else{
                        try{
                            otraConvocatoria = getOtraConvocatoria(entitiesAPI, "CONVOCATORIACOMEDOR", convocatoria, "CONVOCATORIALIBROS");
                            if(StringUtils.isEmpty(otraConvocatoria)){
                                throw new ISPACRuleException("No se ha encontrado el número de expediente de la otra convocatoria. Convocatoria actual: " + convocatoria);
                            }
                        } catch(Exception e){
                            LOGGER.error("No se ha podido recuperar el expediente de la otra convocatoria, no se realizará la comprobación. " + e.getMessage(), e);
                            anotaAviso(cct, entitiesAPI, numexp, " - AVISO. No se ha podido comprobar si se supera el máximo de ambas convocatorias conjuntas, revise que estén indicadas en el mantenimiento de trabajadores sociales.");
                        }
                    }
                    
                    if(StringUtils.isNotEmpty(otraConvocatoria)){
                        try{
                            //Recuperamos las cantidades del ayuntamiento en cuestión        
                            IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, " WHERE LOCALIDAD = '" +ciudad+"' AND NUMEXPCONVOCATORIA IN ('" + convocatoria + "', '" + otraConvocatoria + "') ORDER BY ID LIMIT 2");
                            Iterator<?> cantidadesIt = cantidadesCol.iterator();
                            while(cantidadesIt.hasNext()){                        
                                IItem cantidades = (IItem) cantidadesIt.next();
                                maximoMunicipio += cantidades.getDouble(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);                
                            }
                        } catch(Exception e){
                            maximoMunicipio = 0;
                            LOGGER.error("No se han podido recuperar las cantidades del límite trimestral de la localidad: " + ciudad + " y los expediente de las convocatorias '" + convocatoria + "', '" + otraConvocatoria + "', revise que estén indicadas en el mantenimiento de trabajadores sociales. " + e.getMessage(), e);
                            anotaAviso(cct, entitiesAPI, numexp, " - AVISO. No se han podido recuperar las cantidades del límite trimestral de la localidad: " + ciudad + " y los expediente de las convocatorias '" + convocatoria + "', '" + otraConvocatoria + "', revise que estén indicadas en el mantenimiento de trabajadores sociales.");
                        }
        
                        if(maximoMunicipio > 0){
                            StringBuilder consulta = new StringBuilder(" WHERE ");
                            consulta.append(" CONVOCATORIA IN ('" + convocatoria + "', '" + otraConvocatoria + "')");
                            consulta.append(" AND  CIUDAD = '" +ciudad+ "'");
                            consulta.append(" AND (NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ESTADOADM IN ('RS','AP', 'AP25','NE', 'NE25','NA','NT', 'NT25'))");
                            consulta.append(" OR NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ESTADOADM IN ('RS','AP', 'AP25','NE', 'NE25','NA','NT', 'NT25')))");
                            
                            IItemCollection solicitudesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, consulta.toString());
                            Iterator<?> solicitudesIterator = solicitudesCollection.iterator();
                            
                            while(solicitudesIterator.hasNext()){
                                
                                IItem solicitudes = (IItem) solicitudesIterator.next();
                                
                                IItemCollection concesionLibrosComedorCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, solicitudes.getString("NUMEXP"));
                                Iterator<?> concesionLibrosComedorIterator = concesionLibrosComedorCollection.iterator();
                                if(concesionLibrosComedorIterator.hasNext()){
                                    IItem concesionLibrosComedor = (IItem) concesionLibrosComedorIterator.next();
                                    String tAyda = concesionLibrosComedor.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                                    
                                    if(ConstantesPlanEmergencia.LIBROS.equals(tAyda)){ 
                                        columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS;
                                    } else if(ConstantesPlanEmergencia.COMEDOR.equals(tAyda)){
                                        columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR;
                                       }
                                    
                                    try{
                                           importeSol += Double.parseDouble(concesionLibrosComedor.getString(columna));
                                    } catch(Exception e){
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + " al dar formato al importe " + concesionLibrosComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + solicitudes.getString("NUMEXP") +". " + e.getMessage(), e);
                                        anotaAviso(cct, entitiesAPI, numexp, " - AVISO. Error al dar formato al importe " + concesionLibrosComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + solicitudes.getString("NUMEXP") +". ");
                                    }
                                }
                            }
                            
                            IItemCollection concesionLibrosComedorSolCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, numexp);
                            Iterator<?> concesionLibrosComedorSolIterator = concesionLibrosComedorSolCollection.iterator();
                            if(concesionLibrosComedorSolIterator.hasNext()){
                                IItem concesionLibrosSolComedor = (IItem) concesionLibrosComedorSolIterator.next();
                                tipoAyuda = concesionLibrosSolComedor.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);

                                if(ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda)){ 
                                    columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS;
                                } else if(ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)){
                                    columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR;
                                   }
                                
                                try{
                                       importeSol += Double.parseDouble(concesionLibrosSolComedor.getString(columna));
                                } catch(Exception e){
                                    LOGGER.error(ConstantesString.LOGGER_ERROR + " al dar formato al importe " + concesionLibrosSolComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + numexp + ". " + e.getMessage(), e);
                                    throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al dar formato al importe " + concesionLibrosSolComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + numexp + ". " + e.getMessage(), e);
                                }
                            }
                            
                            if(importeSol > maximoMunicipio){
                                anotaAviso(cct, entitiesAPI, numexp, " - AVISO. Se ha sobrepasado el límite conjunto de LIBROS-COMEDOR para el municipio.");
                            }
                        }
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;
            
        } catch(Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido al iniciar el expediente " + numexp + ". No se ha podido comprobar si se supera el máximo de ambas convocatorias conjuntas. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido al iniciar el expediente " + numexp + ". No se ha podido comprobar si se supera el máximo de ambas convocatorias conjuntas. " + e.getMessage(), e);
        }
    }

    public String getOtraConvocatoria (IEntitiesAPI entitiesAPI, String campo1, String convocatoria, String campo2)throws ISPACException{
        String otraConvocatoria = "";
        
        String numexpYear = "SUBSTRING(SPLIT_PART(" +campo2+", '/',1) FROM LENGTH(SPLIT_PART(" +campo2+", '/',1)) - 3)::INT";
        String numexpNumber = "SPLIT_PART(" +campo2+", '/',2)::INT";
        
        IItemCollection estadisticasCollection = entitiesAPI.queryEntities("DPCR_SERSO_ESTADISTICAS", "WHERE " + campo1 + " = '" + convocatoria + "' AND " + campo2 + " IS NOT NULL ORDER BY " + numexpYear + " DESC, " + numexpNumber + " DESC ");
        Iterator<?> estadisticasIterator = estadisticasCollection.iterator();
        if(estadisticasIterator.hasNext()){
            IItem estadisticas = (IItem) estadisticasIterator.next();
            otraConvocatoria = estadisticas.getString(campo2);
        }
        return otraConvocatoria;
    }
    
    public void anotaAviso(IClientContext cct, IEntitiesAPI entitiesAPI, String numexp, String texto) throws ISPACException{
        IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
        if (expediente != null){
            String asunto = expediente.getString("ASUNTO");
            if(asunto.indexOf(texto) < 0){
                asunto += texto;
            }
            expediente.set("ASUNTO", asunto);
                                    
            expediente.store(cct);                            
        }
    }
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}