package es.dipucr.sigem.api.rule.common.estadoExpediente;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

public class DipucrSetEstadoEstudio extends DipucrSetEstado{
		
	public static final Logger logger = Logger.getLogger(DipucrSetEstadoEstudio.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		nuevoEstado = "ES";
		return true;
	}
}