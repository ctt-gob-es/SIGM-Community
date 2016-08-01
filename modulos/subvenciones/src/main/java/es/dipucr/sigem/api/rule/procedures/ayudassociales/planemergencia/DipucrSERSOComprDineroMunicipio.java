package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOComprDineroMunicipio implements IRule {
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroMunicipio.class);
    
    private static final String[] ESTADOS_ADM = {"RS", "AP", "NT", "NE", "CR"};

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        String numexp = "";
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            numexp = rulectx.getNumExp();
            String convocatoria = "";
            String ciudad = "";
            String trimestre = "";
            String tipoAyuda = "";
            boolean excedeMax = false;

            double trimestral = 0;

            double primerTrim = 0;
            double segundoTrim = 0;
            double tercerTrim = 0;
            double cuartoTrim = 0;

            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
            Iterator<?> solicitudIterator = solicitudCollection.iterator();
            if (solicitudIterator.hasNext()) {
                IItem solicitud = (IItem) solicitudIterator.next();

                convocatoria = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                ciudad = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                trimestre = solicitud.getString(ConstantesPlanEmergencia.TRIMESTRE);
                tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);

                // Recuperamos las cantidades del ayuntamiento en cuestión
                IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, " WHERE LOCALIDAD = '" + ciudad
                        + "' AND NUMEXPCONVOCATORIA = '" + convocatoria + "' ORDER BY ID LIMIT 1");
                Iterator<?> cantidadesIt = cantidadesCol.iterator();
                while (cantidadesIt.hasNext()) {
                    IItem cantidades = (IItem) cantidadesIt.next();
                    trimestral = cantidades.getDouble(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
                }

                StringBuilder consulta2 = new StringBuilder(" WHERE ");
                if (ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)) {
                    consulta2.append(" CONVOCATORIA = '" + convocatoria + "'");
                } else {
                    consulta2.append(" CONVOCATORIA IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE CODPROCEDIMIENTO = 'PCD-221' AND (UPPER(ASUNTO) LIKE '%ALIMENT%' OR UPPER(ASUNTO) LIKE '%EXCEP%') AND ESTADOADM = 'PR')");
                }
                consulta2.append(" AND  CIUDAD = '" + ciudad + "'");
                consulta2.append(" AND TRIMESTRE = '" + trimestre + "'");
                consulta2.append(" AND (NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ESTADOADM IN ('RS','AP','NE','NA','NT'))");
                consulta2.append(" OR NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ESTADOADM IN ('RS','AP','NE','NA','NT')))");
                consulta2.append(" ORDER BY NIF, SUBSTR(NUMEXP, 5,4)::INT DESC, SUBSTR(NUMEXP, 10)::INTEGER DESC");

                IItemCollection solicitudesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, consulta2.toString());
                Iterator<?> solicitudesIterator = solicitudesCollection.iterator();
                while (solicitudesIterator.hasNext()) {
                    IItem solicitudes = (IItem) solicitudesIterator.next();

                    ArrayList<String> tieneHijosLista = (ArrayList<String>) getProcedimientosRelacionadosHijos(rulectx, solicitudes.getString("NUMEXP"), entitiesAPI);

                    if (tieneHijosLista == null || tieneHijosLista.size() == 1) {
                        IItem exp = ExpedientesUtil.getExpediente(cct, solicitudes.getString("NUMEXP"));
                        if (exp != null && StringUtils.isNotEmpty(exp.getString("ESTADOADM")) && !"RC".equals(exp.getString("ESTADOADM"))) {
                            IItemCollection concesionCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA,solicitudes.getString("NUMEXP"));
                            Iterator<?> concesionIterator = concesionCollection.iterator();
                            while (concesionIterator.hasNext()) {
                                IItem concesion = (IItem) concesionIterator.next();

                                if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)) {
                                    try {
                                        if (Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1)) > 0){
                                            primerTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1));
                                        } else{
                                            primerTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO));
                                        }
                                    } catch (Exception e) {
                                        LOGGER.debug("El campo TOTALCONCEDIDO es nulo o vacío o no es numérico. " + e.getMessage(), e);
                                        try {
                                            primerTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO));
                                        } catch (Exception e1) {
                                            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar las cantidades del expediente: " + solicitudes.getString("NUMEXP") + " del PRIMER TRIMESTRE. " + e1.getMessage(), e1);
                                        }
                                    }
                                } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)) {
                                    try {
                                        if (Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2)) > 0){
                                            segundoTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2));
                                        } else{
                                            segundoTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2));
                                        }
                                    } catch (Exception e) {
                                        LOGGER.debug("El campo TOTALCONCEDIDO2 es nulo o vacío o no es numérico. " + e.getMessage(), e);
                                        try {
                                            segundoTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2));
                                        } catch (Exception e1) {
                                            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar las cantidades del expediente: " + solicitudes.getString("NUMEXP") + " del SEGUNDO TRIMESTRE. " + e1.getMessage(), e1);
                                        }
                                    }
                                } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)) {
                                    try {
                                        if (Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3)) > 0){
                                            tercerTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3));
                                        } else{
                                            tercerTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3));
                                        }
                                    } catch (Exception e) {
                                        LOGGER.debug("El campo TOTALCONCEDIDO3 es nulo o vacío o no es numérico. " + e.getMessage(), e);
                                        try {
                                            tercerTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3));
                                        } catch (Exception e1) {
                                            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar las cantidades del expediente: " + solicitudes.getString("NUMEXP") + " del TERCER TRIMESTRE. " + e1.getMessage(), e1);
                                        }
                                    }
                                } else {
                                    try {
                                        if (Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4)) > 0){
                                            cuartoTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4));
                                        } else{
                                            cuartoTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4));
                                        }
                                    } catch (Exception e) {
                                        LOGGER.debug("El campo TOTALCONCEDIDO4 es nulo o vacío o no es numérico. " + e.getMessage(), e);
                                        try {
                                            cuartoTrim += Double.parseDouble(concesion.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4));
                                        } catch (Exception e1) {
                                            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar las cantidades del expediente: " + solicitudes.getString("NUMEXP") + " del CUARTO TRIMESTRE. " + e1.getMessage(), e1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)) {
                    if (primerTrim > trimestral){
                        excedeMax = true;
                    }
                } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)) {
                    if (segundoTrim > trimestral){
                        excedeMax = true;
                    }
                } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)) {
                    if (tercerTrim > trimestral){
                        excedeMax = true;
                    }
                } else {
                    if (cuartoTrim > trimestral){
                        excedeMax = true;
                    }
                }
                if (excedeMax) {
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                    if (expediente != null) {
                        String asunto = expediente.getString("ASUNTO");
                        if (asunto.indexOf(" - AVISO. Se ha sobrepasado el límite trimestral.") < 0) {
                            asunto += " - AVISO. Se ha sobrepasado el límite trimestral.";
                        }
                        expediente.set("ASUNTO", asunto);

                        expediente.store(cct);
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return true;

        } catch (Exception e) {
            LOGGER.error("No se ha podido comprobar si el expediente " + numexp + " ha sobrepasado el límite trimestral. " + e.getMessage(), e);
            throw new ISPACRuleException("No se ha podido comprobar si el expediente " + numexp + " ha sobrepasado el límite trimestral. "
                    + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }
    
    public List<String> getProcedimientosRelacionadosHijos(IRuleContext rulectx, String numExpPadre, IEntitiesAPI entitiesAPI) throws ISPACRuleException{
        return getProcedimientosRelacionadosHijos(rulectx, numExpPadre, entitiesAPI, new ArrayList<String>());        
    }
    
    public List<String> getProcedimientosRelacionadosHijos(IRuleContext rulectx, String numExpPadre, IEntitiesAPI entitiesAPI, List<String> resultadoTemp)throws ISPACRuleException{
        
        List<String> resultado = new ArrayList<String>();
        List<String> resultadoAux = new ArrayList<String>();
        
        try{
            //En primer lugar añadimos el padre, como primer expediente
            IItem expediente = ExpedientesUtil.getExpediente(rulectx.getClientContext(), numExpPadre);
            if(expediente != null){
                String estadoAdm = expediente.getString("ESTADOADM");
                if(Arrays.asList(ESTADOS_ADM).contains(estadoAdm)){
                    resultado.add(numExpPadre);
                }
            }
            //Buscamos los expedientes relacionados hijos
            String consultaSQL = "WHERE NUMEXP_PADRE = '" + numExpPadre + "'";
            IItemCollection itemCollection = entitiesAPI.queryEntities(SpacEntities.SPAC_EXP_RELACIONADOS, consultaSQL);
            
            //Si no tenemos hijos padre no hacemos nada
            if(itemCollection != null && itemCollection.next()){
                Iterator<?> it = itemCollection.iterator();
                IItem item = null;
                
                String numexpHijo = "";
                //Para cada hijo
                while (it.hasNext()) {
                    item = ((IItem)it.next());
                    numexpHijo = item.getString("NUMEXP_HIJO");

                    //Si ya está incluido en resultado de esta llamada, no hacemos nada
                    boolean existe = false;
                    for (int i = 0; i < resultado.size() && !existe; i++){                    
                        if(numexpHijo.equals(resultado.get(i))){
                                existe = true;
                        }
                    }
                    //Si ya está incluido en los resultado temporales que llevamos tampoco hacemos nada
                    boolean existe2 = false;
                    for (int i = 0; i < resultadoTemp.size() && !existe2; i++){                    
                        if(numexpHijo.equals(resultadoTemp.get(i))){
                            existe2 = true;                        
                        }
                    }
                    //Si no existe lo añadimos y buscamos sus hijos y los añadimos al resultado
                    if(!existe && !existe2) {                                   
                        resultadoAux = getProcedimientosRelacionadosHijos(rulectx, numexpHijo, entitiesAPI, (ArrayList<String>) resultado);
                        //Añadimos al resultado el resultado de resultadoTemp
                        for (int j=  0; j < resultadoAux.size(); j++){
                            resultado.add(resultadoAux.get(j));
                        }
                    }            
                }
            }
        } catch(ISPACException e){
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar los expedientes relacionados hijos: " +e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al recuperar los expedientes relacionados hijos: " +e.getMessage(), e);
        }
        return resultado;
    }

}