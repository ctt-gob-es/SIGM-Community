package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateCertificadoAsistenciaCOMIPersRule extends GenerateCertificadoAsistenciaRule{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_NombreDocCertificado  = "Certificado de Asistencia de COMI de Personal";
		return true;
	}
}
