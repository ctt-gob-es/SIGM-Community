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

import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

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
            
            String trimestre = "";
            boolean enviarcorreo = false;
            
            double totalDipu = 0;

            float totalsemestre1 = 0;
            float totalsemestre2 = 0;
            float totalsemestre3 = 0;
            float totalsemestre4 = 0;
            float totalconcedido = 0;
            float totalconcedido2 = 0;
            float totalconcedido3 = 0;
            float totalconcedido4 = 0;

            String anio = "DPCR" + Calendar.getInstance().get(Calendar.YEAR) + "/";

            IItemCollection expedientesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, getConsulta(anio));
            Iterator<?> expedientesIterator = expedientesCollection.iterator();
            
            while (expedientesIterator.hasNext()) {
                IItem expediente = (IItem) expedientesIterator.next();

                totalconcedido += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO);
                totalconcedido2 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO2);
                totalconcedido3 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO3);
                totalconcedido4 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALCONCEDIDO4);

                totalsemestre1 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE1);
                totalsemestre2 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE2);
                totalsemestre3 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE3);
                totalsemestre4 += SubvencionesUtils.getDouble(expediente, ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TOTALSEMESTRE4);
            }

            IItemCollection cantAcumCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.NOMBRE_TABLA, getConsulta2(anio));
            Iterator<?> cantAcumIterator = cantAcumCollection.iterator();

            if (cantAcumIterator.hasNext()){
                totalDipu = SubvencionesUtils.getFloat((IItem) cantAcumIterator.next(), ConstantesPlanEmergencia.DpcrSERSOPeCantAcum.TRIMESTRAL);
            }

            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, rulectx.getNumExp());
            Iterator<?> solicitudIterator = solicitudCollection.iterator();

            if (solicitudIterator.hasNext()) {
                trimestre = SubvencionesUtils.getString((IItem) solicitudIterator.next(), ConstantesPlanEmergencia.TRIMESTRE);
            }

            if (ConstantesPlanEmergencia.PRIMER_TRIMESTRE.equals(trimestre) && (totalconcedido + totalsemestre1) > totalDipu){
                enviarcorreo = true;
            } else if (ConstantesPlanEmergencia.SEGUNDO_TRIMESTRE.equals(trimestre) && (totalconcedido2 + totalsemestre2) > totalDipu){
                enviarcorreo = true;
            } else if (ConstantesPlanEmergencia.TERCER_TRIMESTRE.equals(trimestre) && (totalconcedido3 + totalsemestre3) > totalDipu){
                enviarcorreo = true;
            } else if ((totalconcedido4 + totalsemestre4) > totalDipu){
                enviarcorreo = true;
            }

            String subject = "AVISO. Se ha sobrepasado el límte trimestral para la Diputación.";
            String content = "Que era de: <b>" + totalDipu + " euros, para el trimestre: " + trimestre;

            if (enviarcorreo) {
                SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, subject, subject);

                String correos = ConfigurationMgr.getVarGlobal(cct, ConstantesPlanEmergencia.CORREOS_PLAN_EMERGENCIA);
                enviarCorreos(rulectx, correos, subject, content);
                
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            return Boolean.TRUE;

        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el dinero concedido. " + e.getMessage(), e);
        }
    }
    
    public void enviaCorreo(IRuleContext rulectx, String correo, String subject, String content){
        try{
            if(StringUtils.isNotEmpty(correo)){
                MailUtil.enviarCorreo(rulectx.getClientContext(), correo.trim(), subject, content);
            }
        } catch (ISPACException e) {
            LOGGER.error(this.getClass().getName() + ": Error al enviar e-mail a: " + correo + ". " + e.getMessage(), e);
        }
    }
    
    public String[] splitCorreos(String correos){
        String[] correosArray = null;
        
        if(StringUtils.isNotEmpty(correos)){
            correosArray = correos.split(";");
            if(correosArray.length == 1){
                correosArray = correos.split(" ");
            }
        }
        return correosArray;
    }
    
    public void enviarCorreos(IRuleContext rulectx, String correos, String subject, String content){
        String[] correosArray = splitCorreos(correos);
        if( null != correosArray){
            for (String correo : correosArray){
                enviaCorreo(rulectx, correo, subject, content);
            }
        }
    }
    
    public String getConsulta(String anio){
        StringBuilder sql = new StringBuilder(ConstantesString.WHERE);
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
        
        return sql.toString();
    }
    
    public String getConsulta2(String anio){
        StringBuilder sql = new StringBuilder(ConstantesString.WHERE);
        sql.append(" NUMEXPCONVOCATORIA IN ( ");
        sql.append(" SELECT DISTINCT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS ");
        sql.append(ConstantesString.WHERE + " NUMEXP_PADRE NOT IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
        sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA' AND NUMEXP_HIJO LIKE '%" + anio + "%') AND LOCALIDAD = '999'");
        
        return sql.toString();
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }

}