package es.dipucr.sigem.api.rule.procedures.ayudassociales.planemergencia;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrRelacionaExpedientes;

public class DipucrRelacionaExpedientesPlanEmergencia extends DipucrRelacionaExpedientes{
    
    public static final Logger LOGGER = Logger.getLogger(DipucrRelacionaExpedientesPlanEmergencia.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        relacion = ConstantesPlanEmergencia.RELACION_CONVOCATORIA;
        return true;
    }
}
