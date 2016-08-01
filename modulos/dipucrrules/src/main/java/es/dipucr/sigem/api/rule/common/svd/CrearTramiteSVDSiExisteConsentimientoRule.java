package es.dipucr.sigem.api.rule.common.svd;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

public class CrearTramiteSVDSiExisteConsentimientoRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(CrearTramiteSVDSiExisteConsentimientoRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	@SuppressWarnings("unchecked")
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean crearTramite = false;
		try {
			IItem datosTramite = TramitesUtil.getTramiteByCode(rulectx, "doc-consentim");
			int id_tramite = datosTramite.getInt("ID");
			String sqlQuery = "ID_TRAM_CTL="+id_tramite+"";
			IItemCollection itcollTramite = TramitesUtil.getTramites(rulectx.getClientContext(), rulectx.getNumExp(), sqlQuery, "");
			
			//[Manu ticket #1351] * INICIO - SIGEM Error al generar un tramite de SVD si no existe el trámite de consentimiento)
			if(itcollTramite.toList().size() > 0){
				Iterator<IItem> iterTramite = itcollTramite.iterator();
				while(iterTramite.hasNext()){
					IItem tramite = iterTramite.next();
					int id_tram_exp = tramite.getInt("ID_TRAM_EXP");
					sqlQuery = "ID_TRAMITE="+id_tram_exp+"";
					IItemCollection itcolDoc = DocumentosUtil.getDocumentos(rulectx.getClientContext(), rulectx.getNumExp(), sqlQuery, "");
					Iterator<IItem> itDoc = itcolDoc.iterator();
					if(itDoc.hasNext()){
						crearTramite = true;
					}
					else{
						rulectx.setInfoMessage("Debe introducir el documento de consentimiento de consulta de datos en el trámite 'Documento consentimiento'");
					}
				}			
			}
			else{
				rulectx.setInfoMessage("Debe introducir el documento de consentimiento de consulta de datos en el trámite 'Documento consentimiento'");
			}
			//[Manu ticket #1351] * FIN - SIGEM Error al generar un tramite de SVD si no existe el trámite de consentimiento)
		} catch (ISPACException e) {
			logger.error("Error : " + rulectx.getNumExp() + ". " + e.getMessage(), e);
			throw new ISPACRuleException("Error : " + rulectx.getNumExp() + ". " + e.getMessage(), e);
		}
		
		return crearTramite;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
