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
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DipucrCommonFunctions;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOComprDineroConcedidoMaximoAnualDipu implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(DipucrSERSOComprDineroConcedidoMaximoAnualDipu.class);

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

            StringBuilder sql = new StringBuilder(ConstantesString.WHERE);
            sql.append(" NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES WHERE ");
            sql.append(" NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE IN ( ");
            sql.append(" SELECT DISTINCT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS ");
            sql.append(ConstantesString.WHERE + " NUMEXP_PADRE NOT IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA' AND NUMEXP_HIJO LIKE '%" + anio + "%') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND NUMEXP NOT IN (SELECT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUDES%EMERGENCIA') ");
            sql.append(" AND ESTADOADM IN ('AP','NT','NE','RS','PR','DI','ES')) ");
            sql.append(" OR ");
            sql.append(" NUMEXP IN (SELECT NUMEXP FROM SPAC_EXPEDIENTES_H WHERE ");
            sql.append(" NUMEXP IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE NUMEXP_PADRE IN ( ");
            sql.append(" SELECT DISTINCT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS ");
            sql.append(ConstantesString.WHERE + " NUMEXP_PADRE NOT IN (SELECT NUMEXP_HIJO FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA' AND NUMEXP_HIJO LIKE '%" + anio + "%') ");
            sql.append(" AND UPPER(RELACION) LIKE 'SOLICITUD%EMERGENCIA') ");
            sql.append(" AND NUMEXP NOT IN (SELECT NUMEXP_PADRE FROM SPAC_EXP_RELACIONADOS WHERE UPPER(RELACION) LIKE 'SOLICITUDES%EMERGENCIA') ");
            sql.append(" AND ESTADOADM IN ('AP','NT','NE','RS','PR','DI','ES')) ");

            IItemCollection expedientesCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.SERSOPlanEmerConcesion.NOMBRE_TABLA, sql.toString());
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

            double totalDipu = Double.parseDouble(DipucrCommonFunctions.getVarGlobal("MAX_ANUAL_DIPU_PLAN_EMER"));

            boolean enviarcorreo = false;

            if ((totalconcedido + totalsemestre1 + totalconcedido2 + totalsemestre2 + totalconcedido3 + totalsemestre3 + totalconcedido4 + totalsemestre4) > totalDipu){
                enviarcorreo = true;
            }
            
            if (enviarcorreo) {
                String subject = "AVISO. Se ha sobrepasado el máximo para la Diputación.";
                
                String contenido = "<img src='cid:escudo' width='200px'/>"
                        + "<p align=justify>"
                        + MailUtil.ESPACIADO_PRIMERA_LINEA + "Estimado señor/a:" 
                        + "<br/> <br/>" 
                        + MailUtil.ESPACIADO_PRIMERA_LINEA +  "Se ha sobrepasado el máximo para la Diputación. Que era de: <b>" + totalDipu + " euros."
                        + " </p>"
                        + "<br/> <br/>";

                String rutaImg = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "/SIGEM_TramitacionWeb");
                String logoCabecera = "logoCabecera.gif";

                Object[] imagen = {rutaImg, Boolean.TRUE, logoCabecera, "escudo"};
                List<Object[]> imagenes = new ArrayList<Object[]>();
                imagenes.add(imagen);
                
                SubvencionesUtils.concatenaTextoAAsunto(cct, numexp, subject, subject);
                
                String correo = "";

                String correos = ConfigurationMgr.getVarGlobal(cct, ConstantesPlanEmergencia.CORREOS_PLAN_EMERGENCIA);
                if(StringUtils.isNotEmpty(correos)){
                    String[] correosArray = correos.split(";");
                    if(correosArray.length == 1){
                        correosArray = correos.split(" ");
                    }
                    for (int i = 0; i < correosArray.length; i++){
                        correo = correosArray[i];
                        enviaCorreo (rulectx, correo, subject, contenido, imagenes);
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
            
            return Boolean.TRUE;

        } catch (Exception e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al comprobar el máximo para la Diputación. " + e.getMessage(), e);
            throw new ISPACRuleException(ConstantesString.LOGGER_ERROR + " al comprobar el máximo para la Diputación. " + e.getMessage(), e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
   
    public void enviaCorreo(IRuleContext rulectx, String direccionCorreo, String subject, String contenido, List<Object[]> imagenes) {
        try{
            if(StringUtils.isNotEmpty(direccionCorreo)){
                MailUtil.enviarCorreo(rulectx.getClientContext(), direccionCorreo, subject, contenido, imagenes);
            }
        } catch (ISPACException e) {
            LOGGER.error(this.getClass().getName() + ": Error al enviar e-mail a: " + direccionCorreo + ". " + e.getMessage(), e);
        }
    }
}