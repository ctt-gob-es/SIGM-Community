package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;

import java.io.File;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class InsertarDocInfFiscalSubvNomDirecRule implements IRule{
	
	private static final Logger logger = Logger.getLogger(InsertarDocInfFiscalSubvNomDirecRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		boolean creadoTramiteFiscaSub = false;
		try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IGenDocAPI genDocAPI = invesFlowAPI.getGenDocAPI();
			ITXTransaction transaction = invesFlowAPI.getTransactionAPI();
	        //----------------------------------------------------------------------------------------------
			//Obtengo el numero de expediente del procedimiento generico
			String sQuery = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Fiscalización de subvenciones' ORDER BY ID ASC";
    		IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, sQuery);
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        String numexpSubvencion = null;
	        while (exp_relacionadosIterator.hasNext()){
	        	numexpSubvencion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_PADRE");       	
	        }
			
        	IItemCollection tramiteCol = TramitesUtil.getTramites(cct, numexpSubvencion,  "NOMBRE='Fiscalización de subvenciones'", "");
        	Iterator<IItem> tramites = tramiteCol.iterator();
        	if(tramites.hasNext()){
        		creadoTramiteFiscaSub = true;
        	}
        	else{
        		rulectx.setInfoMessage("Falta tener el trámite abierto de Fiscalización de subvenciones de las Subvenciones Nominativas o Diectas, pongase en contacto con el funcionario responsable del expediente");
        	}
		} catch (ISPACException e) {
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
	        
		return creadoTramiteFiscaSub;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		try
    	{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
			
			DbCnt cnt = cct.getConnection();
			
			//Obtengo el numero de expediente del procedimiento generico
			String sQuery = "WHERE NUMEXP_HIJO='"+rulectx.getNumExp()+"' AND RELACION='Fiscalización de subvenciones' ORDER BY ID ASC";
    		IItemCollection exp_relacionadosCollection = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_EXP_RELACIONADOS, sQuery);
	        Iterator<IItem> exp_relacionadosIterator = exp_relacionadosCollection.iterator();
	        String numexpSubvencion = null;
	        while (exp_relacionadosIterator.hasNext()){
	        	numexpSubvencion = ((IItem)exp_relacionadosIterator.next()).getString("NUMEXP_PADRE");       	
	        }
	        
	        IItemCollection documentsCollection = entitiesAPI.getDocuments(rulectx.getNumExp(), "ID_TRAMITE="+rulectx.getTaskId(), "FDOC DESC");
	        Iterator<IItem> itDoc = documentsCollection.iterator();
	        while(itDoc.hasNext()){
	        	IItem itemDoc = itDoc.next();
	        	File fileDoc = DocumentosUtil.getFile(cct, itemDoc.getString("INFOPAG_RDE"), itemDoc.getString("NOMBRE"), itemDoc.getString("EXTENSION_RDE"));
	        	
	        	IItemCollection tramiteCol = TramitesUtil.getTramites(cct, numexpSubvencion,  "NOMBRE='Fiscalización de subvenciones'", "");
	        	Iterator<IItem> tramites = tramiteCol.iterator();
	        	if(tramites.hasNext()){
	        		IItem tramite = tramites.next();
	        		int idTipoDoc = DocumentosUtil.getTipoDoc(cct, "Informe", DocumentosUtil.BUSQUEDA_EXACTA, true);
	        		DocumentosUtil.generaYAnexaDocumento(rulectx, tramite.getInt("ID_TRAM_EXP"), idTipoDoc, itemDoc.getString("NOMBRE"), fileDoc, itemDoc.getString("EXTENSION_RDE"));
	        		TramitesUtil.cerrarTramite(tramite.getInt("ID_TRAM_EXP"), rulectx);
	        	}
	        }
		} catch (ISPACException e) {
			logger.error("Error al generar los documentos", e.getCause());
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}

		return new Boolean(true);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}

}
