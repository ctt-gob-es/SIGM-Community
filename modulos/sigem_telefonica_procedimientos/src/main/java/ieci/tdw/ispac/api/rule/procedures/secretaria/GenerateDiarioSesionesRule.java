package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateDiarioSesionesRule extends GenerateLibroActasRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_Entity = "SECR_DIARIOSESIONES";
		STR_DocLibro = "Diario de Sesiones";
		STR_DocInicio = "Diario de Sesiones - Inicio";
		STR_DocFinal = "Diario de Sesiones - Final";
		STR_DocFinActa = "Diario de Sesiones - Fin acta";
		return true;
	}
}