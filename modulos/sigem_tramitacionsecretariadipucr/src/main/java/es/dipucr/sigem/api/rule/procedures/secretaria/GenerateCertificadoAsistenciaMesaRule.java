package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateCertificadoAsistenciaMesaRule extends GenerateCertificadoAsistenciaRule {
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_NombreDocCertificado  = "Certificado de Asistencia de Mesa de Contratación";
		return true;
	}
}
