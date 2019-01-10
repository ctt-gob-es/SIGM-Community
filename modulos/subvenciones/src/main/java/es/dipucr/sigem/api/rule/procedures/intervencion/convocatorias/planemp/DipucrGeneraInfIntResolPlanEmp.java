package es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.planemp;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.LibreOfficeUtil;
import es.dipucr.sigem.api.rule.procedures.ConstantesString;
import es.dipucr.sigem.api.rule.procedures.intervencion.convocatorias.generica.DipucrGeneraInfIntResolConvIntGen;

public class DipucrGeneraInfIntResolPlanEmp extends DipucrGeneraInfIntResolConvIntGen{
    
    private static final Logger LOGGER = Logger .getLogger(DipucrGeneraInfIntResolPlanEmp.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        LOGGER.info(ConstantesString.INICIO +DipucrGeneraInfIntResolPlanEmp.class);
        
        tipoDocumento = "Informe de Intervención";
        plantilla = "Informe de Intervención Resolución Convocatoria Plan de Empleo";
        refTablas = LibreOfficeUtil.ReferenciasTablas.TABLA1 + "," + LibreOfficeUtil.ReferenciasTablas.TABLA2;

        LOGGER.info(ConstantesString.FIN +DipucrGeneraInfIntResolPlanEmp.class);
        return true;
    }
}
