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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrSERSOEnviaMailRechazadoPE implements IRule {
    protected static final Logger LOGGER = Logger.getLogger(DipucrSERSOEnviaMailRechazadoPE.class);

    private static final String EMAIL_SUBJECT_VAR_NAME = "SERSO_PLAN_EMER_EMAIL_SUBJECT";
    private static final String EMAIL_CONTENT_VAR_NAME = "SERSO_PLAN_EMER_EMAIL_CONTENT";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    
    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        String numexp = "";
        try {
            LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
            // ----------------------------------------------------------------------------------------------
            ClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------

            numexp = rulectx.getNumExp();
            
            Map<String,String> variables = new HashMap<String,String>();
            variables.put("NUMEXP", numexp);

            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);

            if (expediente != null) {
                String estado = expediente.getString("ESTADOADM");
                // Si está rechazado
                if ("RC".equals(estado)) {
                    // Recupermos el e-mail del expediente de trabajadores/as sociales
                    String dniSoli = expediente.getString("NIFCIFTITULAR");
                    String consulta = "WHERE UPPER(DNI) = UPPER('" + dniSoli+ "')";
                        
                    IItemCollection trabajadoresCollection = entitiesAPI.queryEntities(ConstantesPlanEmergencia.DpcrSERSOTrabSocial.NOMBRE_TABLA, consulta);
                    Iterator<?> trabajadoresIterator = trabajadoresCollection.iterator();
                    if (trabajadoresIterator.hasNext()) {
                        IItem trabajador = (IItem) trabajadoresIterator.next();
                        String correo = trabajador.getString(ConstantesPlanEmergencia.DpcrSERSOTrabSocial.EMAIL);

                        if (StringUtils.isNotEmpty(correo)) {
                            String observaciones = expediente.getString("OBSERVACIONES");
                            
                            String descripcionTipoAyuda = "";
                            String tipoAyuda = "";
                            
                            IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
                            Iterator<?> solicitudIterator = solicitudCollection.iterator();
                            
                            if(solicitudIterator.hasNext()){
                                IItem solicitud = (IItem) solicitudIterator.next();
                                descripcionTipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DESCRIPCION_TIPOAYUDA);
                                tipoAyuda = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                                
                                variables.put(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA, tipoAyuda);                            
                                variables.put(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DESCRIPCION_TIPOAYUDA, descripcionTipoAyuda);
                                
                                String nombreBenef = solicitud.getString(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);
                                if(StringUtils.isNotEmpty(nombreBenef)){        
                                    variables.put("NOMBREBENEF", nombreBenef);
                                }
                            }
                            
                            if(StringUtils.isNotEmpty(observaciones)){
                                variables.put("OBSERVACIONES", observaciones);
                            }

                            String subject = ConfigurationMgr.getVarGlobal(cct, EMAIL_SUBJECT_VAR_NAME);
                            String content = ConfigurationMgr.getVarGlobal(cct, EMAIL_CONTENT_VAR_NAME);

                            if (StringUtils.isNotBlank(subject)) {
                                subject = StringUtils.replaceVariables(subject, variables);
                            }

                            if (StringUtils.isNotBlank(content)) {
                                content = StringUtils.replaceVariables(content, variables);
                            }
                            
                            MailUtil.enviarCorreo(rulectx, correo, subject, content);
                            LOGGER.info("[DipucrSERSOEnviaMailRechazadoPE:execute()] Email enviado a: "    + correo);
                            ExpedientesUtil.avanzarFase(cct, numexp);                                
                        }
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al enviar el correo de documentación incompleta del expediente: " + numexp + ". " + e.getMessage(), e);
        }
        
        return Boolean.TRUE;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        
    }
}
