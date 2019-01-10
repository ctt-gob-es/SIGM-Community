package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

/**
 * [ecenpri-Teresa Ticket #404] Nueva regla de eliminación de Avisos electrónicos de Registro Telematico
 * @since 06.07.2011
 * @author Teresa
 */

public class EliminarAvisosRegistroTelematicoRule implements IRule {
	
//	private static final Logger logger = Logger.getLogger(AvisoFinFirmaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		AvisosUtil.borrarAvisos(rulectx, "notice.initExpedient");
		return AvisosUtil.borrarAvisos(rulectx, Constants.AVISOSELECTRONICOS.MENSAJEREGISTROTELEMATICO);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
