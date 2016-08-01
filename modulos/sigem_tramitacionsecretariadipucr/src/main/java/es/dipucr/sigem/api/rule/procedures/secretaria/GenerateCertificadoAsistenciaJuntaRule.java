package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateCertificadoAsistenciaJuntaRule extends GenerateCertificadoAsistenciaRule{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_NombreDocCertificado  = "Certificado de Asistencia de Junta de Gobierno";
		return true;
	}

}
