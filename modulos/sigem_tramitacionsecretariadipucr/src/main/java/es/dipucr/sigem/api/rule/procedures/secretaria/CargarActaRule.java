package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class CargarActaRule  implements IRule{
	
	private static final Logger logger = Logger.getLogger(CargarActaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
			String extensionEntidad = DocumentosUtil.obtenerExtensionDocPorEntidad();
			/*********************************************************/
			
			String sqlQuery = "NUMEXP='"+rulectx.getNumExp()+"' AND (EXTENSION='odt' or EXTENSION='doc') AND NOMBRE='Borrador de Acta de Pleno'";			
			IItemCollection documentos = entitiesAPI.getDocuments(rulectx.getNumExp(), sqlQuery, "FDOC DESC");
			Iterator<IItem> iDoc = documentos.iterator();
			
			if(iDoc.hasNext()){
				IItem doc = iDoc.next();
				String infoPag = doc.getString("INFOPAG");
				
	        	int documentTypeId = DocumentosUtil.getTipoDoc(cct, "Borrador de Acta de Pleno", DocumentosUtil.BUSQUEDA_EXACTA, false);
	        	File resultado1 = DocumentosUtil.getFile(cct, infoPag, null, null);
	        	
	    		DocumentosUtil.generaYAnexaDocumento(rulectx, documentTypeId, "Borrador de Acta de Pleno", resultado1, extensionEntidad);
	    		
	    		
			}
		} catch (ISPACException e) {
			logger.error("Error al generar los documentos. " + e.getMessage(), e);
			throw new ISPACRuleException("Error al generar los documentos. " + e.getMessage(), e);
		}
		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {		
	}

}
