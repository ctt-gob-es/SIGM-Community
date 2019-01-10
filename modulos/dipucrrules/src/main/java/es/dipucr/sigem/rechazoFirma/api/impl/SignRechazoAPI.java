package es.dipucr.sigem.rechazoFirma.api.impl;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SignAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.ExpedientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.rechazoFirma.sign.SignCircuitInstanceRechazoDAO;
import es.dipucr.sigem.rechazoFirma.sign.SignRechazoCircuitMgr;

public class SignRechazoAPI extends SignAPI {

	public static final Logger logger = Logger.getLogger(SignRechazoAPI.class);
	
	ClientContext mcontext;
	
	public SignRechazoAPI(ClientContext mcontext) {
		super(mcontext);	
		this.mcontext = mcontext;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List rechazarFirma(String[] stepIds, String signCertificate, String entityId, String motivoRechazo) throws ISPACException{
		
		IInvesflowAPI invesflowAPI = mcontext.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		List signDocuments = new ArrayList();
		
		// Ejecución en un contexto transaccional
		boolean ongoingTX = mcontext.ongoingTX();
		boolean bCommit = false;
		
        try {
			if (!ongoingTX) {
				mcontext.beginTX();
			}

			// Rechazar la firma del documento asociado a cada paso de circuito de firma
			for (int i = 0; i < stepIds.length; i++) {
				
				// Paso del circuito
				IItem stepCircuit = getStepInstancedCircuit(Integer.parseInt(stepIds[i]));
				
				// Documento asociado al paso
//				IItem document = entitiesAPI.getDocument(stepCircuit.getInt("ID_DOCUMENTO"));
				int idDoc = stepCircuit.getInt("ID_DOCUMENTO");
				IItem document = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//[eCenpri-Felipe #1023]
				
				// Expediente del documento
				String numExp = document.getString("NUMEXP");
				int idPcd = ExpedientesUtil.getExpediente(mcontext, numExp).getInt("ID_PCD");
								
				// Documento a rechazar
				SignDocument signDocument = new SignDocument(document);
				
				signDocument.setEntityId(entityId);//2.0
				
				signDocument.setIdPcd(idPcd);
				signDocument.setNumExp(numExp);
				
				// Rechazamos asociada al paso del circuito
				//[eCenpri-Felipe #354/#206] Se pone el motivo rechazo antes de ejecutar las reglas de evento Fin Circuito de Firma
				rechazarFirma(signDocument, stepCircuit.getKeyInt(), motivoRechazo);
			
				signDocuments.add(signDocument);
				//MQE si hemos rechazado la firma sin problemas ponemos el motivo

				int tramite = document.getInt("ID_TRAMITE");
				//DTTramiteDAO tramiteDAO = new DTTramiteDAO(cnt, tramite);
				String consulta = "WHERE ID_TRAM_EXP = "+tramite;
//				IItemCollection tramitesAbiertos = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, consulta);
				IItemCollection tramitesAbiertos = TramitesUtil.queryTramites(mcontext, consulta);//[eCenpri-Felipe #1023]
				IItem tramiteDAO = (IItem) tramitesAbiertos.iterator().next();
				String observaciones = tramiteDAO.getString("OBSERVACIONES");
				if(observaciones != null && !observaciones.equals("")) observaciones += "\n";
				else observaciones="";
				String nombreUsuarioRechazo = mcontext.getUser().getName();
				observaciones += "Rechazado por " + nombreUsuarioRechazo + ": "+ motivoRechazo;
				//observaciones += motivoRechazo;
				if(observaciones.length()>254) observaciones = observaciones.substring(0,253);
				tramiteDAO.set("OBSERVACIONES", observaciones);
				tramiteDAO.store(mcontext);		
				
				//[eCenpri-Felipe #354/#206] Se pone el motivo rechazo antes de ejecutar las reglas de evento Fin Circuito de Firma
				// MQE Ticket #176, añadimos el motivo de reparo al documento
//				document.set("MOTIVO_RECHAZO", motivoRechazo);
//				document.store(mcontext);
				//MQE fin modificaciones ticket #176
			}
			
			// Si todo ha sido correcto se hace commit de la transacción
			bCommit = true;
	    }
	    finally {
			if (!ongoingTX) {
				mcontext.endTX(bCommit);
			}
		}
	    
	    return signDocuments;
	}	
	
	public boolean rechazarFirma(SignDocument signDocument, int instancedStepId, String motivoRechazo) throws ISPACException {

		// Ejecución en un contexto transaccional
		boolean ongoingTX = mcontext.ongoingTX();
		boolean bCommit = false;
		
        try {
			if (!ongoingTX) {
				mcontext.beginTX();
			}

			SignRechazoCircuitMgr signCircuitMgr = new SignRechazoCircuitMgr(mcontext);
			this.rechazar(signDocument, signCircuitMgr.isLastStep(instancedStepId));
			//[eCenpri-Felipe #354/#206] Se pone el motivo rechazo antes de ejecutar las reglas de evento Fin Circuito de Firma
			boolean ret = signCircuitMgr.rechazarStep(signDocument, instancedStepId, motivoRechazo);
			
			bCommit = true;
			
			return ret;
			
        } finally {
			if (!ongoingTX) {
				mcontext.endTX(bCommit);
			}
        }
	}
	
	public void rechazar(SignDocument signDocument, boolean changeState)throws ISPACException {

		// Ejecución en un contexto transaccional
		boolean ongoingTX = mcontext.ongoingTX();
		boolean bCommit = false;
		
        try {
			if (!ongoingTX) {
				mcontext.beginTX();
			}
			
			// Firmamos con el conector de firma que estemos utilizando
			//SignRechazoConnector signConnector = new SignRechazoConnector();
			//signConnector.initializate(signDocument, mcontext);
			//signConnector.rechazar(changeState);

			// Información del documento
			IItem doc = signDocument.getItemDoc();

			// EVENTO: Firmar documento
			ExpedientContext expCtx = new ExpedientContext(mcontext);
			ITXTransaction tx = mcontext.getAPI().getTransactionAPI();

			StateContext stateContext = mcontext.getStateContext();
			if (stateContext.getActivityId() > 0) {
				expCtx.setStage(stateContext.getStageId());
				expCtx.setTask(doc.getInt("ID_TRAMITE"));
				expCtx.setActivity(stateContext.getActivityId(), doc.getInt("ID_TRAMITE"), doc.getInt("ID_TRAMITE_PCD"));
				tx.executeEvents(EventsDefines.EVENT_OBJ_ACTIVITY, doc.getInt("ID_FASE_PCD"), EventsDefines.EVENT_DOCUMENT_SIGN, expCtx);
			} else if (doc.getInt("ID_TRAMITE") > 0) {
				expCtx.setStage(doc.getInt("ID_FASE"));
				expCtx.setTask(doc.getInt("ID_TRAMITE"));
				tx.executeEvents(EventsDefines.EVENT_OBJ_TASK, doc.getInt("ID_TRAMITE_PCD"), EventsDefines.EVENT_DOCUMENT_SIGN, expCtx);
			} else if (doc.getInt("ID_FASE") > 0) {
				expCtx.setStage(doc.getInt("ID_FASE"));
				tx.executeEvents(EventsDefines.EVENT_OBJ_STAGE, doc.getInt("ID_FASE_PCD"), EventsDefines.EVENT_DOCUMENT_SIGN, expCtx);
			}

			bCommit = true;
			
        } finally {
			if (!ongoingTX) {
				mcontext.endTX(bCommit);
			}
        }
	}
	
	//MQE Histórico de documentos Rechazados
	public IItemCollection getHistorics(String respId, Date init, Date end, int state) throws ISPACException {
        DbCnt cnt = mcontext.getConnection();
		try {
			return SignCircuitInstanceRechazoDAO.getHistorics(cnt, respId, init, end, state).disconnect();
		} finally {
			mcontext.releaseConnection(cnt);
		}	
	}
}
