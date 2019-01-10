package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;

/**
 * [ecenpri-Felipe Ticket #39] Nuevo procedimiento Propuesta de Solicitud de Anuncio
 * @since 04.08.2010
 * @author Felipe
 */
public class EliminarAvisosAnuncioRule implements IRule {
	
//	private static final Logger logger = Logger.getLogger(AvisoFinFirmaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return AvisosUtil.borrarAvisos(rulectx, AvisoAnuncio._ADVERTISE_MESSAGE);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
