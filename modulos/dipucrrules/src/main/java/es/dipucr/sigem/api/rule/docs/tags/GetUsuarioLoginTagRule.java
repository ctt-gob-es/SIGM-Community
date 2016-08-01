package es.dipucr.sigem.api.rule.docs.tags;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class GetUsuarioLoginTagRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			// ----------------------------------------------------------------------------------------------

			String usuario = null;

			usuario = cct.getUser().getName();

			return usuario;

		} catch (Exception e) {
			throw new ISPACRuleException("Error al obtener el usuario actual.", e);
		}
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}
