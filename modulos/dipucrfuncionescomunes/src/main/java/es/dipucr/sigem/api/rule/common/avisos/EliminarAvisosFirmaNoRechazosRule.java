package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.ArrayList;

import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;


public class EliminarAvisosFirmaNoRechazosRule implements IRule {
	
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
		return AvisosUtil.borrarAvisos(rulectx, listMensajes);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}
