package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;

/**
 * Ticket #32 - Aviso fin circuito de firma
 * @author Felipe-ecenpri
 * @since 03.06.2010
 */
public class EliminarAvisosFirmaRule implements IRule {
	
//	private static final Logger logger = Logger.getLogger(AvisoFinFirmaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		ArrayList<String> listMensajes = new ArrayList<String>();
		listMensajes.add(AvisoFinFirmaRule._MENSAJE_FIRMADO);
		listMensajes.add(AvisoFinFirmaRule._MENSAJE_FIRMADO_REPARO);
		listMensajes.add(AvisoFinFirmaRule._MENSAJE_RECHAZADO);
		return AvisosUtil.borrarAvisos(rulectx, listMensajes);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
