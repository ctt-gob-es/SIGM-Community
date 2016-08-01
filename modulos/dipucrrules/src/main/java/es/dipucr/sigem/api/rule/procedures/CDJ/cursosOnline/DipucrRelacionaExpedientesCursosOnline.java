package es.dipucr.sigem.api.rule.procedures.CDJ.cursosOnline;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrRelacionaExpedientes;

public class DipucrRelacionaExpedientesCursosOnline extends DipucrRelacionaExpedientes{
	
	public static final Logger logger = Logger.getLogger(DipucrRelacionaExpedientesCursosOnline.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		relacion = "Solicitud de curso on line";
		return true;
	}
}
