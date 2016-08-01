package es.dipucr.sigem.api.rule.procedures.DecretosNuevo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DecretosUtil;

public class SiAutomatizacionDecretoRule implements IRule{
	
	public static final Logger logger = Logger.getLogger(SiAutomatizacionDecretoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		DecretosUtil.setAutomatizacion(rulectx, true);
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
