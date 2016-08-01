package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import es.dipucr.contratacion.common.CrearDocumentoconAnexosRule;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class GenerarSolicitudInformeDocumentacionTecnicaRule extends CrearDocumentoconAnexosRule{
	
	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			/*********************************************************/
			String sqlQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND NOMBRE='Anexo' AND ID_TRAMITE="+rulectx.getTaskId()+"";
			super.setDocAnexos(DocumentosUtil.queryDocumentos(cct, sqlQuery));
			logger.warn("sqlQuery "+sqlQuery);
			
			sqlQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND (EXTENSION='odt' or EXTENSION='doc') AND NOMBRE='Plantilla Informe Técnico' "
					+ "AND ID_TRAMITE="+rulectx.getTaskId()+"";		
			logger.warn("sqlQuery "+sqlQuery);
			IItemCollection documentos = DocumentosUtil.queryDocumentos(cct, sqlQuery);
			Iterator<IItem> iDoc = documentos.iterator();
			if(iDoc.hasNext()){
				super.setPlantillaInicial(iDoc.next());
			}
			
			
			super.setCodPlantillaGenerar("SolInfDocTecnica");
			
			super.setCodTramiteGenerar("SOLINFTECNDOCTE");
			
			return true;
		} catch (ISPACException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
	}

}
