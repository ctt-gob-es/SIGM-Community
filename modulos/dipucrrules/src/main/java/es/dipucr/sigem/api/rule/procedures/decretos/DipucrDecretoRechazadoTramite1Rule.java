package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class DipucrDecretoRechazadoTramite1Rule implements IRule{
	
	private static final Logger logger = Logger.getLogger(DipucrDecretoRechazadoTramite1Rule.class);
	
	public boolean init(IRuleContext ctx)
	throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext ctx) 
	throws ISPACRuleException {
		try{
			//APIs
			IClientContext cct = ctx.getClientContext();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();			
						
			//Recuperamos los documentos del trámite cuya firma ha sido rechazada

			int taskId = ctx.getTaskId();
			String sqlQuery = "ID_TRAMITE = "+taskId+" AND ESTADOFIRMA = '04'";
			IItemCollection itemCollection = entitiesAPI.getDocuments(ctx.getNumExp(), sqlQuery, "");
			
			if (itemCollection.next()){					
				ITXTransaction tx = invesFlowAPI.getTransactionAPI();
				
				/*String consulta = "WHERE OBLIGATORIO = 1 AND ID_FASE = "+ctx.getStageProcedureId();
				IItemCollection tramitesObligatorios = entitiesAPI.queryEntities("SPAC_P_TRAMITES", consulta);
				
				Iterator tramites = tramitesObligatorios.iterator();
				int idTramite2=0;
				while (tramites.hasNext()){
					IItem tramite = (IItem)tramites.next();
				
					String consulta2 = "WHERE NUMEXP = '"+ctx.getNumExp()+"' and ID_TRAM_PCD = "+tramite.getInt("ID");
					IItemCollection tramitesExistentes = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, consulta2);
					
					Iterator tramitesExpediente = tramitesExistentes.iterator();
					if(!tramitesExpediente.hasNext()){
						idTramite2 = tx.createTask(ctx.getStageId(),tramite.getInt("ID"));
					}				
					if(idTramite2>0){
						String sqlQuery2 = "ID_TRAMITE = "+idTramite2;//+" AND ESTADOFIRMA = '04'";
						IItemCollection itemCollectionDoc = entitiesAPI.getDocuments(ctx.getNumExp(), sqlQuery2, "");
						Iterator itemCollectionDocIt = itemCollectionDoc.iterator();
						while (itemCollectionDocIt.hasNext()){							
							IItem item = (IItem) itemCollectionDocIt.next();
							int idDocumento = item.getInt("ID");
							item.set("ESTADOFIRMA", "04");
							item.store(cct);							
						}
					}					
				}
				String consulta3 = "WHERE NUMEXP = '"+ctx.getNumExp()+"' AND ESTADO = 1";
				
				IItemCollection tramitesAbiertos = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, consulta3);
				Iterator tramitesAbiertosIterator = tramitesAbiertos.iterator();
				while (tramitesAbiertosIterator.hasNext()){
					IItem item = (IItem)tramitesAbiertosIterator.next();
					tx.closeTask(item.getInt("ID_TRAM_EXP"));
				}	*/			
				tx.closeProcess(ctx.getProcessId());
				ctx.setInfoMessage("El expediente "+ctx.getNumExp()+" ha sido cerrado.");
			}
		}
		catch(Exception e){
			logger.error("ERROR.DecretoRechazadoTramite1Rule: "+e.getMessage(), e);
		}
		return null;
	}

	public void cancel(IRuleContext arg0) throws ISPACRuleException {	
	}

	public boolean validate(IRuleContext arg0) throws ISPACRuleException {
		return true;
	}

}
