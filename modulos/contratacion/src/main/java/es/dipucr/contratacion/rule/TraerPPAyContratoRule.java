package es.dipucr.contratacion.rule;


import java.io.File;
import java.util.Iterator;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.contratacion.utils.FuncionesUtils;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class TraerPPAyContratoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(TraerPPAyContratoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try {
			String sQuery = "NOMBRE='Pliego de Clausulas Económico - Administrativas' OR NOMBRE='Contrato'";
			String numexpPadre = FuncionesUtils.getNumExpProcContrat_PetContrat(rulectx);
			if(numexpPadre!=null && !numexpPadre.equals("")){
				IItemCollection itCollDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), numexpPadre, sQuery, "");
				Iterator<IItem> itDoc = itCollDoc.iterator();
				while (itDoc.hasNext()) {
					IItem iItemDocs = (IItem) itDoc.next();
					int tipoDoc = DocumentosUtil.getTipoDoc(rulectx.getClientContext(), "Documentación", DocumentosUtil.BUSQUEDA_EXACTA, true);
					String infopag = "";
					String extension = "";
					if(iItemDocs.getString("INFOPAG_RDE")!=null){
						infopag = iItemDocs.getString("INFOPAG_RDE");
						extension = iItemDocs.getString("EXTENSION_RDE");
					}
					else{
						infopag = iItemDocs.getString("INFOPAG");
						extension = iItemDocs.getString("EXTENSION");
					}
					File fileDoc = DocumentosUtil.getFile(rulectx.getClientContext(), infopag, iItemDocs.getString("NOMBRE"), extension);
					
					DocumentosUtil.generaYAnexaDocumento(rulectx, rulectx.getTaskId(), tipoDoc, iItemDocs.getString("NOMBRE"), fileDoc, extension);
				}
			}
			
		} catch (ISPACException e) {
			logger.error("Numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			throw new ISPACRuleException("Numexp. "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}
