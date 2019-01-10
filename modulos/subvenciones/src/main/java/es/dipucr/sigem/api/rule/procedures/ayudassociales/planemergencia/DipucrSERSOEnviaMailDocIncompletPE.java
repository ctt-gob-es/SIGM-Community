package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

public class DipucrSERSOEnviaMailDocIncompletPE extends DipucrSERSOEnviaMailRechazadoPE {
    
    protected static final Logger LOGGER = Logger.getLogger(DipucrSERSOEnviaMailDocIncompletPE.class);

    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        
        emailSubjectVarName = "SERSO_PLAN_EMER_EMAIL_DI_SUBJECT";
        emailContentVarName = "SERSO_PLAN_EMER_EMAIL_DI_CONTENT";
        
        estadoAdmFiltro = ExpedientesUtil.EstadoADM.DI;    
        avanzarFase = false;
        
        return true;
    }
}
