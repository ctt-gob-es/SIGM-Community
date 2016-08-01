package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DipucrNoEliminarRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrNoEliminarRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean noEliminarSiDocFirmado = true;
		try {
			IItemCollection itDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), "INFOPAG_RDE IS NOT NULL", "");
			Iterator<IItem> docCol = itDoc.iterator();
			if(docCol.hasNext()){
				noEliminarSiDocFirmado=false;
			}
		} catch (ISPACException e) {
			logger.error("Error : " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error : " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		if(!noEliminarSiDocFirmado){
			rulectx.setInfoMessage("Existen documentos firmados, por lo tanto no se puede eliminar el trámite");
		}
		return noEliminarSiDocFirmado;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
