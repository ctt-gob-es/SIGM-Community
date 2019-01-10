package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class GenerateCopiaPlenoRule implements IRule{
	
	private static final Logger LOGGER = Logger.getLogger(GenerateCopiaPlenoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		String sqlQuery = "NOMBRE='Borrador de Acta de Pleno'";
		String order = " ID DESC";
		
		try {
			IItemCollection itColdoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), sqlQuery, order);
			Iterator<IItem> itDoc = itColdoc.iterator();
			if(itDoc.hasNext()){
				IItem doc = itDoc.next();
				int tpdoc = DocumentosUtil.getIdTipoDocByCodigo(rulectx.getClientContext(), "actaPleno");
				String sTpdoc = DocumentosUtil.getTipoDocNombreByCodigo(rulectx.getClientContext(), "actaPleno");
				IItem docNuevo = DocumentosUtil.copiaDocumentoSinFirmaTramite(rulectx, doc, rulectx.getNumExp(), rulectx.getTaskId(), tpdoc, sTpdoc);
			}
		} catch (ISPACException e) {
			LOGGER.error("ERROR. " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
