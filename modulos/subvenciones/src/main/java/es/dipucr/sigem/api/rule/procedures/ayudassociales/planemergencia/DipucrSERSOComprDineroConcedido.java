package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Calendar;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOComprDineroConcedido implements IRule {
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroConcedido.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            String numexp = rulectx.getNumExp();

            float totalsemestre1 = 0;
            float totalsemestre2 = 0;
            float totalsemestre3 = 0;
            float totalsemestre4 = 0;
            float totalconcedido = 0;
            float totalconcedido2 = 0;
            float totalconcedido3 = 0;
            float totalconcedido4 = 0;

            String anio = "DPCR" + Calendar.getInstance().get(Calendar.YEAR) + "/";

            StringBuilder sql = new StringBuilder("WHERE ");
            sql.append(" NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ");
            sql.append(" NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE IN ( ");
            sql.append(" SELECT DISTINCT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS ");
            sql.append(" WHERE NUMEXP_PADRE NOT IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA' AND NUMEXP_HIJO LIKE '%" + anio + "%') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND NUMEXP NOT IN (SELECT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUDES%EMERGENCIA') ");
            sql.append(" AND ESTADOADM IN ('AP','NT','NE','RS','PR','DI','ES')) ");
            sql.append(" OR ");
            sql.append(" NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ");
            sql.append(" NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE IN ( ");
            sql.append(" SELECT DISTINCT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS ");
            sql.append(" WHERE NUMEXP_PADRE NOT IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA' AND NUMEXP_HIJO LIKE '%" + anio + "%') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND NUMEXP NOT IN (SELECT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUDES%EMERGENCIA') ");
            sql.append(" AND ESTADOADM IN ('AP','NT','NE','RS','PR','DI','ES')) ");

            IItemCollection expedientesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, sql.toString());
            Iterator<?> expedientesIterator = expedientesCollection.iterator();
            
            while (expedientesIterator.hasNext()) {
                IItem expediente = (IItem) expedientesIterator.next();

                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO))){
                    totalconcedido += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO));
                } else {
                    LOGGER.debug("El campo TOTALCONCEDIDO es nulo o vacío. ");
                }
                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2))){
                    totalconcedido2 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2));
                } else {
                    LOGGER.debug("El campo TOTALCONCEDIDO2 es nulo o vacío. ");
                }
                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3))){
                    totalconcedido3 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3));
                } else {
                    LOGGER.debug("El campo TOTALCONCEDIDO3 es nulo o vacío. ");
                }
                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4))){
                    totalconcedido4 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4));
                } else {
                    LOGGER.debug("El campo TOTALCONCEDIDO4 es nulo o vacío. ");
                }

                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1))){
                    totalsemestre1 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1));
                } else {
                    LOGGER.debug("El campo TOTALSEMESTRE1 es nulo o vacío. ");
                }
                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2))){
                    totalsemestre2 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2));
                } else {
                    LOGGER.debug("El campo TOTALSEMESTRE2 es nulo o vacío. ");
                }
                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3))){
                    totalsemestre3 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3));
                } else {
                    LOGGER.debug("El campo TOTALSEMESTRE3 es nulo o vacío. ");
                }
                if(StringUtils.isNotEmpty(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4)) && StringUtils.isDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4))){
                    totalsemestre4 += Double.parseDouble(expediente.getString(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4));
                } else {
                    LOGGER.debug("El campo TOTALSEMESTRE4 es nulo o vacío. ");
                }
            }

            StringBuilder sql2 = new StringBuilder(
                    " WHERE NUMEXPCONVOCATORIA IN ( ");
            sql2.append(" SELECT DISTINCT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS ");
            sql2.append(" WHERE NUMEXP_PADRE NOT IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql2.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA' AND NUMEXP_HIJO LIKE '%"
                    + anio + "%') AND LOCALIDAD = '999'");

            IItemCollection cantAcumCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, sql2.toString());
            Iterator<?> cantAcumIterator = cantAcumCollection.iterator();

            double totalDipu = 0;
                    
            if (cantAcumIterator.hasNext()){
                totalDipu = ((IItem) cantAcumIterator.next()).getFloat(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
            }

            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, rulectx.getNumExp());
            Iterator<?> solicitudIterator = solicitudCollection.iterator();

            String trimestre = "";
            boolean enviarcorreo = false;
            String content = "Que era de: <b>" + totalDipu + " euros, para el trimestre: " + trimestre;

            if (solicitudIterator.hasNext()) {
                trimestre = ((IItem) solicitudIterator.next()).getString(ConstantesPlanEmergencia.TRIMESTRE);
            }

            if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre)) {
                if ((totalconcedido + totalsemestre1) > totalDipu){
                    enviarcorreo = true;
                }
            } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre)) {
                if ((totalconcedido2 + totalsemestre2) > totalDipu){
                    enviarcorreo = true;
                }
            } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre)) {
                if ((totalconcedido3 + totalsemestre3) > totalDipu){
                    enviarcorreo = true;
                }
            } else {
                if ((totalconcedido4 + totalsemestre4) > totalDipu){
                    enviarcorreo = true;
                }
            }

            String subject = "AVISO. Se ha sobrepasado el límte trimestral para la Diputación.";

            if (enviarcorreo) {
                IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                if (expediente != null) {
                    String asunto = expediente.getString("ASUNTO");
                    if (asunto.indexOf(subject) < 0) {
                        asunto += subject;
                    }
                    expediente.set("ASUNTO", asunto);
                    expediente.store(cct);
                }
                String correo = "";

                String correos = ConfigurationMgr.getVarGlobal(cct, ConstantesPlanEmergencia.CORREOS_PLAN_EMERGENCIA);
                if(StringUtils.isNotEmpty(correos)){
                    String[] correosArray = correos.split(";");
                    if(correosArray.length == 1){
                        correosArray = correos.split(" ");
                    }
                    for (int i = 0; i < correosArray.length; i++){
                        try{
                            correo = correosArray[i];
                            if(StringUtils.isNotEmpty(correo)){
                                MailUtil.enviarCorreo(rulectx, correo, subject, content);
                            }
                        } catch (ISPACException e) {
                            LOGGER.error(this.getClass().getName() + ": Error al enviar e-mail a: " + correo + ". " + e.getMessage(), e);
                        }
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return Boolean.TRUE;

        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido. " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }

}