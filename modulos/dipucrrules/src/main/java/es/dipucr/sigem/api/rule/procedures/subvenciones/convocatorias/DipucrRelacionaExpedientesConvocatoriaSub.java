package es.dipucr.sigem.api.rule.procedures.subvenciones.convocatorias;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrRelacionaExpedientes;

public class DipucrRelacionaExpedientesConvocatoriaSub extends DipucrRelacionaExpedientes{
	
	public static final Logger logger = Logger.getLogger(DipucrRelacionaExpedientesConvocatoriaSub.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		relacion = "Solicitud Convocatoria Subvenciones";
		return true;
	}
}
