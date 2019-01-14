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
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class GenerarSolicitudInformeRecursosyReclamacionesRule extends CrearDocumentoconAnexosRule{
	
	@SuppressWarnings("unchecked")
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			/*********************************************************/
			IClientContext cct = rulectx.getClientContext();
			/*********************************************************/
			
			StringBuilder sqlQuery = new StringBuilder("WHERE NUMEXP='"+rulectx.getNumExp()+"' AND ((NOMBRE='Anexo' AND ID_TRAMITE="+rulectx.getTaskId()+") ");
			
			IItem itTramites = TramitesUtil.getTramiteByCode(rulectx, "Anex-Doc");			
			String consulta = "WHERE ID_TRAM_CTL = "+itTramites.getInt("ID")+" AND NUMEXP='"+rulectx.getNumExp()+"'";
			logger.warn("consulta "+consulta);
			IItemCollection tramspacDtTramite = TramitesUtil.queryTramites(rulectx.getClientContext(), consulta);
			Iterator<IItem> iteratorDtTramite = tramspacDtTramite.iterator();
			if(null!=iteratorDtTramite && iteratorDtTramite.hasNext()){
				IItem itTramitedt = iteratorDtTramite.next();
				sqlQuery.append("OR (ID_TRAMITE ="+itTramitedt.getInt("ID_TRAM_EXP")+")");
				
			}
			sqlQuery.append(")");
			logger.warn("sqlQuery "+sqlQuery.toString());
			super.setDocAnexos(DocumentosUtil.queryDocumentos(cct, sqlQuery.toString()));			
			
			String sqSqlQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND (EXTENSION='odt' or EXTENSION='doc') AND NOMBRE='Plantilla Informe Técnico' AND ID_TRAMITE="+rulectx.getTaskId()+"";		
			logger.warn("sqlQuery "+sqSqlQuery);
			IItemCollection documentos = DocumentosUtil.queryDocumentos(cct, sqSqlQuery);
			Iterator<IItem> iDoc = documentos.iterator();
			if(iDoc.hasNext()){
				super.setPlantillaInicial(iDoc.next());
			}
			
			
			super.setCodTipoDocGenerar("sol-inf-rec-recl");
			
			super.setCodTramiteGenerar("sol-inf-rec-recl");
			
			return true;
		} catch (ISPACException e) {
			logger.error("error: "+e.toString());			
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
	}

}
