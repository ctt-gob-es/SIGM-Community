package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;

public class EliminarAvisosFinTramiteRule implements IRule {
	
	/**
	 * [Ticket #487 TCG] (SIGEM Regla de aviso de fin de tramite)
	 * **/
	
//	private static final Logger logger = Logger.getLogger(AvisoFinFirmaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return AvisosUtil.borrarAvisos(rulectx, AvisoFinTramiteRule._NEWTASK_MESSAGE);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
