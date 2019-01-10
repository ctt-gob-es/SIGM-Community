package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateDiarioSesionesRule extends GenerateLibroActasRule {
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntity = "SECR_DIARIOSESIONES";
        strDocLibro = "Diario de Sesiones";
        strDocInicio = "Diario de Sesiones - Inicio";
        strDocFinal = "Diario de Sesiones - Final";
        strDocFinActa = "Diario de Sesiones - Fin acta";
        return true;
    }
}