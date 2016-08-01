package es.dipucr.contratacion.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.contratacion.rule.TrasladarDocumento;


public class TrasladarDocumentoINT  extends TrasladarDocumento {
	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		nombreDoc = "Carta digital";
		rol = "INT";
		return true;
	}

}
