package es.dipucr.sigem.api.rule.procedures.intervencion.planesprovinciales.convocatorias;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.cdj.convocatorias.DipucrGeneraNotificacionesConvocatoriasSub;

public class DipucrGeneraNotificacionesPlanObras extends DipucrGeneraNotificacionesConvocatoriasSub {

    public static final Logger LOGGER = Logger
            .getLogger(DipucrGeneraNotificacionesPlanObras.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + this.getClass().getName());
        
        plantilla = "Notificación Convocatoria Subvención Plan de Obras Municipales";
        
        LOGGER.info(ConstantesString.FIN + this.getClass().getName());
        return true;
    }
}