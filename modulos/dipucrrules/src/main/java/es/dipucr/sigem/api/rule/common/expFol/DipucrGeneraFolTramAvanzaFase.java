package es.dipucr.sigem.api.rule.common.expFol;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrGeneraFolTramAvanzaFase implements IRule {

	private static final Logger logger = Logger.getLogger(DipucrGeneraFolTramAvanzaFase.class);
	
	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		logger.info("INICIO - DipucrGeneraFolTramAvanzaFase");
		String numexp = "";
		

		int idFase = Integer.MIN_VALUE;
		int idFase_pcd = Integer.MIN_VALUE;
		int idTramite = Integer.MIN_VALUE;
		int idTramiteFoliado = Integer.MIN_VALUE;
		
		try{
			numexp = rulectx.getNumExp();
			boolean reabriendoExp = false;
			
			IClientContext cct = rulectx.getClientContext();
			IInvesflowAPI invesflowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			ITXTransaction txTransaction = invesflowAPI.getTransactionAPI();			
			
			IItemCollection docsFoliados = DocumentosUtil.getDocumentos(cct, numexp, "ID_TPDOC = '292'", "");
			if(docsFoliados.toList().size() > 0) reabriendoExp = true;

			if(!reabriendoExp){
				idFase = rulectx.getStageId();
				idFase_pcd = rulectx.getStageProcedureId();
				
				if(idFase == Integer.MIN_VALUE){
					logger.error("Error recuperando el id de la fase.");
					throw new ISPACRuleException("Error recuperando el id de la fase.");
				}
				
				String consulta = "WHERE ID_FASE = " + idFase_pcd + " AND (UPPER(NOMBRE) = UPPER('Generar Expediente Foliado Con Índice') OR UPPER(NOMBRE) = UPPER('Generar Documento Único Expediente')) ORDER BY ORDEN ASC";
				IItemCollection iTramiteProp = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_P_TRAMITES, consulta);
				Iterator<?> ITramiteProp = iTramiteProp.iterator();
				IItem tramite = (IItem)ITramiteProp.next();
				idTramite = tramite.getInt("ID");
				
				if(idTramite == Integer.MIN_VALUE){
					logger.error("Error recuperando el id del trámite.");
					throw new ISPACRuleException("Error recuperando el id del trámite.");
				}
				
				try {
					idTramiteFoliado = txTransaction.createTask(idFase, idTramite);
				} catch (ISPACException e) {
					logger.error("Error al crear el trámite con id: " + idTramite + ", en la fase: " + idFase + " del expediente: " + numexp + ". " + e.getMessage(), e);
					throw new ISPACRuleException("Error al crear el trámite con id: " + idTramite + ", en la fase: " + idFase + " del expediente: " + numexp + ". " + e.getMessage(), e);
				}
				
				if(idTramiteFoliado != Integer.MIN_VALUE){
					txTransaction.closeTask(idTramiteFoliado);
					ExpedientesUtil.avanzarFase(cct, numexp);
				}
			}
		}
		catch(ISPACRuleException e){
			logger.error("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);
		} catch (ISPACException e) {
			logger.error("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);
			throw new ISPACRuleException("ERROR al generar el trámite del foliado o al avanzar fase del expediente: " + numexp + ". " + e.getMessage(), e);		
		}
		logger.info("FIN - DipucrGeneraFolTramAvanzaFase");
		return null;
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}
