package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;

public class DipucrGeneraPropuResolConvIntGen extends DipucrGeneraInfIntResolConvIntGen {

    private static final Logger LOGGER = Logger.getLogger(DipucrGeneraPropuResolConvIntGen.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO + DipucrGeneraPropuResolConvIntGen.class);

        tipoDocumento = "Contenido de la propuesta";
        plantilla = "Propuesta Resolución Convocatoria de Subvención Intervención";
        refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2;
        
        LOGGER.info(ConstantesString.FIN + DipucrGeneraPropuResolConvIntGen.class);
        return true;
    }
}
