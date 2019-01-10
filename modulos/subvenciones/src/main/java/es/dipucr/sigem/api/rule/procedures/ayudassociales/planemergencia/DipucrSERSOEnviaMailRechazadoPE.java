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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.SubvencionesUtils;

public class DipucrSERSOEnviaMailRechazadoPE implements IRule {
    
    protected static final Logger LOGGER = Logger.getLogger(DipucrSERSOEnviaMailRechazadoPE.class);

    protected String emailSubjectVarName = "SERSO_PLAN_EMER_EMAIL_SUBJECT";
    protected String emailContentVarName = "SERSO_PLAN_EMER_EMAIL_CONTENT";
    
    protected String estadoAdmFiltro = ExpedientesUtil.EstadoADM.RC;    
    protected boolean avanzarFase = true;
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        
        emailSubjectVarName = "SERSO_PLAN_EMER_EMAIL_SUBJECT";
        emailContentVarName = "SERSO_PLAN_EMER_EMAIL_CONTENT";
        
        estadoAdmFiltro = ExpedientesUtil.EstadoADM.RC;    
        avanzarFase = true;
        
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
            IClientContext cct = (ClientContext) rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            // ----------------------------------------------------------------------------------------------

            numexp = rulectx.getNumExp();
            
            Map<String,String> variables = new HashMap<String,String>();
            variables.put("NUMEXP", numexp);


            String estadoAdm = ExpedientesUtil.getEstadoAdm(cct, numexp);
            // Si está rechazado
            
            if (estadoAdmFiltro.equals(estadoAdm)) {
                // Recupermos el e-mail del expediente de trabajadores/as sociales
                
                String correo = getCorreoTrabajadorSocial(cct, numexp);

                if (StringUtils.isNotEmpty(correo)) {
                    
                    IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
                    
                    String observaciones = SubvencionesUtils.getString(expediente, ExpedientesUtil.OBSERVACIONES);
                    
                    String descripcionTipoAyuda = "";
                    String tipoAyuda = "";
                    String nombreBenef = "";
                    
                    IItemCollection solicitudCollection = entitiesAPI.getEntities(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE_TABLA, numexp);
                    Iterator<?> solicitudIterator = solicitudCollection.iterator();
                    
                    if(solicitudIterator.hasNext()){
                        IItem solicitud = (IItem) solicitudIterator.next();
                        
                        descripcionTipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DESCRIPCION_TIPOAYUDA);
                        tipoAyuda = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA);
                        nombreBenef = SubvencionesUtils.getString(solicitud, ConstantesPlanEmergencia.DpcrSERSOPlanEmer.NOMBRE);                        
                    }
                    
                    variables.put(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.TIPOAYUDA, tipoAyuda);                            
                    variables.put(ConstantesPlanEmergencia.DpcrSERSOPlanEmer.DESCRIPCION_TIPOAYUDA, descripcionTipoAyuda);
                    
                    if(StringUtils.isNotEmpty(nombreBenef)){        
                        variables.put("NOMBREBENEF", nombreBenef);
                    }
                    
                    if(StringUtils.isNotEmpty(observaciones)){
                        variables.put("OBSERVACIONES", observaciones);
                    }

                    String subject = ConfigurationMgr.getVarGlobal(cct, emailSubjectVarName);
                    String content = ConfigurationMgr.getVarGlobal(cct, emailContentVarName);

                    subject = StringUtils.replaceVariables(subject, variables);
                    content = StringUtils.replaceVariables(content, variables);
                    
                    LOGGER.info("[DipucrSERSOEnviaMailRechazadoPE:execute()] Email enviado a: "    + correo);
                    
                    if(!enviaCorreo(rulectx, correo.trim(), subject, content)){
                        content = ConstantesString.LOGGER_ERROR + " - <br>" + content;
                        subject = ConstantesString.LOGGER_ERROR + " - " + subject;
                        
                        String usuario = ((ClientContext) cct).getUser().getName();                                
                        
                        correo = UsuariosUtil.getCorreoElectronico(cct, usuario);
                        
                        if(!enviaCorreo(rulectx, correo.trim(), subject, content)){
                            LOGGER.error(this.getClass().getName()+": Error al enviar e-mail a: " + correo + ". ");                                    
                            LOGGER.error(ConstantesString.LOGGER_ERROR + " al recuperar el correo electrónico del usuario: " + ((ClientContext) cct).getUser().getName() + ". ");                                
                        }
                    }
                    
                    if(avanzarFase){
                        ExpedientesUtil.avanzarFase(cct, numexp);    
                    }
                }
            }
            LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        } catch (ISPACException e) {
            LOGGER.error(ConstantesString.LOGGER_ERROR + " al enviar el correo de documentación incompleta del expediente: " + numexp + ". " + e.getMessage(), e);
        }
        
        return Boolean.TRUE;
    }

    public String getCorreoTrabajadorSocial(IClientContext cct, String numexp) {
        String correo = "";
        try{
            IItem expediente = ExpedientesUtil.getExpediente(cct, numexp);
            
            String dniSoli = SubvencionesUtils.getString(expediente, ExpedientesUtil.NIFCIFTITULAR);
            
            String consulta = ConstantesString.WHERE + " UPPER(DNI) = UPPER('" + dniSoli+ "')";
                
            IItemCollection trabajadoresCollection = cct.getAPI().getEntitiesAPI().queryEntities(ConstantesPlanEmergencia.DpcrSERSOTrabSocial.NOMBRE_TABLA, consulta);
            Iterator<?> trabajadoresIterator = trabajadoresCollection.iterator();
            
            if (trabajadoresIterator.hasNext()) {
                
                IItem trabajador = (IItem) trabajadoresIterator.next();
                correo = SubvencionesUtils.getString(trabajador, ConstantesPlanEmergencia.DpcrSERSOTrabSocial.EMAIL);
            }
        } catch (ISPACException e){
            LOGGER.error("ERROR al recuperar la dirección de correo electrónico del trabajador social del expediente: " + numexp + ". " + e.getMessage(), e);
        }
        return correo;
    }
    
    public boolean enviaCorreo(IRuleContext rulectx, String correo, String subject, String content){
        boolean envio = true;
         try{
             MailUtil.enviarCorreo(rulectx.getClientContext(), correo, subject, content);
             LOGGER.info("[DipucrSERSOEnviaMailDocIncompletPE:execute()] Email enviado a: " + correo);
         } catch(Exception e){
             LOGGER.error(this.getClass().getName()+": Error al enviar e-mail a: " + correo + ". " + e.getCause(), e);
             envio = false;             
         } 
         return envio;
    }


    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        //No se da nunca este caso
    }
}
