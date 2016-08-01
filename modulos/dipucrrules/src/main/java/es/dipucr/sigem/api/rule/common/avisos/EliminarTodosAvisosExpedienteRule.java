package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * [eCenpri-Manu Ticket #303] ALSIGM3 Dar información de los documentos anexados a los expedientes.
 * @since 07.06.2016
 * @author Manu
 */
public class EliminarTodosAvisosExpedienteRule implements IRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return AvisosUtil.borrarTodosAvisosByNumExp(rulectx, rulectx.getNumExp());
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
