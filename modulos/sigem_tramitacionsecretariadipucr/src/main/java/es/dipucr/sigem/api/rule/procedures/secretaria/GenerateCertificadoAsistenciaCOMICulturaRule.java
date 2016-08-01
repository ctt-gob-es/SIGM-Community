package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateCertificadoAsistenciaCOMICulturaRule extends GenerateCertificadoAsistenciaRule{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_NombreDocCertificado  = "Certificado de Asistencia de COMI de Cultura";
		return true;
	}
}
