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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesRelacionadosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComprDineroMunicipio implements IRule {
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroMunicipio.class);
    
    private static final String[] ESTADOS_ADM = {ExpedientesUtil.EstadoADM.RS, ExpedientesUtil.EstadoADM.AP, ExpedientesUtil.EstadoADM.NT, ExpedientesUtil.EstadoADM.NE, ExpedientesUtil.EstadoADM.CR};

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

                convocatoria = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CONVOCATORIA);
                ciudad = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.CIUDAD);
                trimestre = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.TRIMESTRE);
                tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);

                // Recuperamos las cantidades del ayuntamiento en cuestión
                IItemCollection cantidadesCol = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, ConstantesString.WHERE + " LOCALIDAD = '" + ciudad + "' AND NUMEXPCONVOCATORIA = '" + convocatoria + "' ORDER BY ID LIMIT 1");
                Iterator<?> cantidadesIt = cantidadesCol.iterator();
                while (cantidadesIt.hasNext()) {
                    IItem cantidades = (IItem) cantidadesIt.next();
                    trimestral = SubvencionesUtils.getDouble(cantidades, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
                }

                StringBuilder consulta2 = new StringBuilder(ConstantesString.WHERE);
                
                if (ConstantesPlanEmergencia.LIBROS.equals(tipoAyuda) || ConstantesPlanEmergencia.COMEDOR.equals(tipoAyuda)) {
                    consulta2.append(" CONVOCATORIA = '" + convocatoria + "'");
                } else {
                    consulta2.append(" CONVOCATORIA IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE CODPROCEDIMIENTO = 'PCD-221' AND (UPPER(ASUNTO) LIKE '%ALIMENT%' OR UPPER(ASUNTO) LIKE '%EXCEP%') AND ESTADOADM = 'PR')");
                }
                consulta2.append(" AND CIUDAD = '" + ciudad + "'");
                consulta2.append(" AND TRIMESTRE = '" + trimestre + "'");
                consulta2.append(" AND (NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ESTADOADM IN ('RS','AP','NE','NA','NT'))");
                consulta2.append(" OR NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ESTADOADM IN ('RS','AP','NE','NA','NT')))");
                consulta2.append(" ORDER BY NIF, SUBSTR(NUMEXP, 5,4)::INT DESC, SUBSTR(NUMEXP, 10)::INTEGER DESC");

                IItemCollection solicitudesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, consulta2.toString());
                Iterator<?> solicitudesIterator = solicitudesCollection.iterator();
                
                while (solicitudesIterator.hasNext()) {
                    IItem solicitudes = (IItem) solicitudesIterator.next();

                    List<String> tieneHijosLista = ExpedientesRelacionadosUtil.getProcedimientosRelacionadosHijosByVariosEstadosAdm(rulectx, solicitudes.getString("NUMEXP"), new ArrayList<String>(), Arrays.asList(ESTADOS_ADM));

                    if (tieneHijosLista == null || tieneHijosLista.size() == 1) {
                        String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, SubvencionesUtils.getString(solicitudes, "NUMEXP"));
                        
                        if (StringUtils.isNotEmpty(estadoAdm) && !ExpedientesUtil.EstadoADM.RC.equals(estadoAdm)) {
                            IItemCollection concesionCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, solicitudes.getString("NUMEXP"));
                            Iterator<?> concesionIterator = concesionCollection.iterator();
                            while (concesionIterator.hasNext()) {
                                IItem concesion = (IItem) concesionIterator.next();

                                if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)) {
                                    if (SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1) > 0){
                                        primerTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1);
                                    } else{
                                        primerTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO);
                                    }
                                } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)) {
                                    if (SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2) > 0){
                                        segundoTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2);
                                    } else{
                                        segundoTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2);
                                    }
                                } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)) {
                                    if (SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3) > 0){
                                        tercerTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3);
                                    } else{
                                        tercerTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3);
                                    }
                                } else {
                                    if (SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4) > 0){
                                        cuartoTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4);
                                    } else{
                                        cuartoTrim += SubvencionesUtils.getDouble(concesion, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4);
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
                    SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, ConstantesPlanEmergencia.DpcrSERSOAvisos.TEXTOASUNTO_LIMITE_TRIMESTRAL);
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
        //No se da nunca este caso
    }
}