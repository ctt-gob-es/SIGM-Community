package es.dipucr.sigem.api.rule.common.estadoExpediente;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

public class DipucrSetEstadoPresentacion extends DipucrSetEstado{
	
	public static final Logger logger = Logger.getLogger(DipucrSetEstadoPresentacion.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		nuevoEstado = "PR";
		return true;
	}
}
