package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica.estado;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.ConstantesString;


public class DipucrCambiaEstadoAdmAPaRSExpsRelConIntGen extends DipucrCambiaEstadoExpRelConvGenInt{
    
    private static final Logger LOGGER = Logger
    .getLogger(DipucrCambiaEstadoAdmAPaRSExpsRelConIntGen.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO +this.getClass().getName());
        
        estadoIni = "AP";
        estadofin = "RS";
        
        LOGGER.info(ConstantesString.FIN +this.getClass().getName());
        
        return true;
    }
}