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

import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

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
                
                tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                
                if(ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)){
                                
                    convocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                    ciudad = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                    
                    //Recuperamos el expediente de la otra convocatoria
                    if(ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda)){
                        otraConvocatoria = getOtraConvocatoria(cct, numexp, "CONVOCATORIALIBROS", convocatoria, "CONVOCATORIACOMEDOR");
                        if(StringUtils.isEmpty(otraConvocatoria)){
                            throw new ISPACRuleException("No se ha encontrado el número de expediente de la otra convocatoria. Convocatoria actual: " + convocatoria);
                        }
                    } else{
                        otraConvocatoria = getOtraConvocatoria(cct, numexp, "CONVOCATORIACOMEDOR", convocatoria, "CONVOCATORIALIBROS");
                        if(StringUtils.isEmpty(otraConvocatoria)){
                            throw new ISPACRuleException("No se ha encontrado el número de expediente de la otra convocatoria. Convocatoria actual: " + convocatoria);
                        }
                    }
                    
                    if(StringUtils.isNotEmpty(otraConvocatoria)){
                        //Recuperamos las cantidades del ayuntamiento en cuestión
                        IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, ConstantesString.WHERE + " LOCALIDAD = '" +ciudad+"' AND NUMEXPCONVOCATORIA IN ('" + convocatoria + "', '" + otraConvocatoria + "') ORDER BY ID LIMIT 2");
                        Iterator<?> cantidadesIt = cantidadesCol.iterator();
                        
                        while(cantidadesIt.hasNext()){
                            
                            IItem cantidades = (IItem) cantidadesIt.next();
                            
                            double cant = SubvencionesUtils.isDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
                            
                            if(Double.MIN_VALUE < cant){
                                maximoMunicipio += cant;
                            } else {
                                maximoMunicipio = 0;
                                LOGGER.error("No se han podido recuperar las cantidades del límite trimestral de la localidad: " + ciudad + " y los expediente de las convocatorias '" + convocatoria + "', '" + otraConvocatoria + "', revise que estén indicadas en el mantenimiento de trabajadores sociales. ");
                                SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, " - AVISO. No se han podido recuperar las cantidades del límite trimestral de la localidad: " + ciudad + " y los expediente de las convocatorias '" + convocatoria + "', '" + otraConvocatoria + "', revise que estén indicadas en el mantenimiento de trabajadores sociales.");
                            }
                        }
        
                        if(maximoMunicipio > 0){
                            StringBuilder consulta = new StringBuilder(ConstantesString.WHERE);
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
                                    String tAyda = SubvencionesUtils.getString(concesionLibrosComedor, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                                    
                                    if(ConstantesPlanEmergencia.LIBROS.equals(tAyda)){ 
                                        columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS;
                                    } else if(ConstantesPlanEmergencia.COMEDOR.equals(tAyda)){
                                        columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR;
                                    }
                                    double importe = SubvencionesUtils.isDouble(concesionLibrosComedor, columna);
                                    if(Double.MIN_VALUE < importe){
                                        importeSol += importe;
                                    } else {
                                        LOGGER.error(ConstantesString.LOGGER_ERROR + " al dar formato al importe " + SubvencionesUtils.getString(concesionLibrosComedor, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + SubvencionesUtils.getString(solicitudes, "NUMEXP") +". ");
                                        SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, " - AVISO. Error al dar formato al importe " + SubvencionesUtils.getString(concesionLibrosComedor, ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + SubvencionesUtils.getString(solicitudes, "NUMEXP") +". ");
                                    }
                                }
                            }
                            
                            IItemCollection concesionLibrosComedorSolCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.NOMBRE_TABLA, numexp);
                            Iterator<?> concesionLibrosComedorSolIterator = concesionLibrosComedorSolCollection.iterator();
                            
                            if(concesionLibrosComedorSolIterator.hasNext()){
                                IItem concesionLibrosSolComedor = (IItem) concesionLibrosComedorSolIterator.next();
                                
                                tipoAyuda = SubvencionesUtils.getString(concesionLibrosSolComedor, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);

                                if(ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda)){ 
                                    columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS;
                                } else if(ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)){
                                    columna = ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.IMPORTETOTALCOMEDOR;
                                }
                                double importe = SubvencionesUtils.isDouble(concesionLibrosSolComedor, columna);
                                if(Double.MIN_VALUE < importe){
                                    importeSol += importe;
                                } else {
                                    LOGGER.error(ConstantesString.LOGGER_ERROR + " al dar formato al importe " + concesionLibrosSolComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + numexp + ". ");
                                    throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al dar formato al importe " + concesionLibrosSolComedor.getString(ConstantesPlanEmergencia.DpcrSERSOLibrosComedor.TOTALLIBROS) + " de la ayuda de " + tipoAyuda + " del el expediente: " + numexp + ". ");
                                }
                            }
                            
                            if(importeSol > maximoMunicipio){
                                SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_LIMITE_CONJUNTO_LIBROS_COMEDOR);
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

    public String getOtraConvocatoria (IClientContext cct, String numexp, String campo1, String convocatoria, String campo2)throws ISPACException{
        String otraConvocatoria = "";
        
        IEntitiesAPI entitiesAPI = null;
        
        try{
            entitiesAPI = cct.getAPI().getEntitiesAPI();
            
            String numexpYear = "SUBSTRING(SPLIT_PART(" +campo2+", '/',1) FROM LENGTH(SPLIT_PART(" +campo2+", '/',1)) - 3)::INT";
            String numexpNumber = "SPLIT_PART(" +campo2+", '/',2)::INT";
        
            IItemCollection estadisticasCollection = entitiesAPI.queryEntities("DPCR_SERSO_ESTADISTICAS", ConstantesString.WHERE + campo1 + " = '" + convocatoria + "' AND " + campo2 + " IS NOT NULL ORDER BY " + numexpYear + " DESC, " + numexpNumber + " DESC ");
            Iterator<?> estadisticasIterator = estadisticasCollection.iterator();
            if(estadisticasIterator.hasNext()){
                IItem estadisticas = (IItem) estadisticasIterator.next();
                otraConvocatoria = estadisticas.getString(campo2);
            }
        } catch(Exception e){
            LOGGER.error("No se ha podido recuperar el expediente de la otra convocatoria, no se realizará la comprobación. " + e.getMessage(), e);
            SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_AVISO_ERROR_COMPROBACION);
        }
        return otraConvocatoria;
    }
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}