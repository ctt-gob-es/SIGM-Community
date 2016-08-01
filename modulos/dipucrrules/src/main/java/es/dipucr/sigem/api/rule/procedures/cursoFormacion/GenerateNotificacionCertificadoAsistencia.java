package es.dipucr.sigem.api.rule.procedures.cursoFormacion;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.GenerateNotificacion;

public class GenerateNotificacionCertificadoAsistencia implements IRule {
	
	private static final Logger logger = Logger.getLogger(GenerateNotificacionCertificadoAsistencia.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			
			GenerateNotificacion.GenerarNotificaciones(rulectx, "TRAS","Certificación administrativa", "Certificado Asistencia");
			
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException("Error. ",e);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
