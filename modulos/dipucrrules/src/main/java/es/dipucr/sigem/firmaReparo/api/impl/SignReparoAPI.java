package es.dipucr.sigem.firmaReparo.api.impl;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SignAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;
import es.dipucr.sigem.firmaReparo.sign.SignReparoCircuitMgr;

@Deprecated
public class SignReparoAPI extends SignAPI{
	
	public static final Logger logger = Logger.getLogger(SignReparoAPI.class);
	
	ClientContext mcontext;
	
	public SignReparoAPI(ClientContext mcontext) {
		super(mcontext);	
		this.mcontext = mcontext;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List firmarReparo(String[] stepIds, String[] signs, String certificado, String entityId, String motivoReparo) throws ISPACException {
		
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
			
			// Firmar el documento asociado a cada paso de circuito de firma
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
				
				// Documento a firmar
				SignDocument signDocument = new SignDocument(document);
				
				signDocument.setEntityId(entityId);//2.0
				
				signDocument.setIdPcd(idPcd);
				signDocument.setNumExp(numExp);
				//TODO AGREGAR EL CERTIFICADO EN LA POSICION DEL FIRMANTE CORRESPONDINTE
				signDocument.addCertificate(certificado);
				signDocument.setHash(generateHashCode(signDocument));
				//TODO Pendiente de arreglo de metadatos, si se descomenta da error porque signs viene vacío.
//				signDocument.addSign(signs[i]);
				
				// Firma asociada al paso del circuito
				firmarReparo(signDocument, stepCircuit.getKeyInt());
				
				signDocuments.add(signDocument);
				
				//MQE si hemos firmado con reparo sin problemas ponemos el motivo en el trámite

				int tramite = document.getInt("ID_TRAMITE");
				//DTTramiteDAO tramiteDAO = new DTTramiteDAO(cnt, tramite);
				String consulta = "WHERE ID_TRAM_EXP = "+tramite;
//				IItemCollection tramitesAbiertos = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES, consulta);
				IItemCollection tramitesAbiertos = TramitesUtil.queryTramites(mcontext, consulta);//[eCenpri-Felipe #1023]
				IItem tramiteDAO = (IItem) tramitesAbiertos.iterator().next();
				String observaciones = tramiteDAO.getString(TramitesUtil.OBSERVACIONES);
				if(observaciones != null && !observaciones.equals("")) observaciones += "\n";
				else observaciones="";
				observaciones += "Reparo Firma: "+ motivoReparo;
				//observaciones += motivoRechazo;
				if(observaciones.length()>254) observaciones = observaciones.substring(0,253);
				tramiteDAO.set(TramitesUtil.OBSERVACIONES, observaciones);
				tramiteDAO.store(mcontext);	
				
				// MQE Ticket #176, añadimos el motivo de reparo al documento
				document.set("MOTIVO_REPARO", motivoReparo);
				document.store(mcontext);
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
	
	public boolean firmarReparo(SignDocument signDocument, int instancedStepId) throws ISPACException {

		// Ejecución en un contexto transaccional
		boolean ongoingTX = mcontext.ongoingTX();
		boolean bCommit = false;
		
        try {
			if (!ongoingTX) {
				mcontext.beginTX();
			}

			SignReparoCircuitMgr signCircuitMgr = new SignReparoCircuitMgr(mcontext);
			this.sign(signDocument, signCircuitMgr.isLastStep(instancedStepId));
			boolean ret = signCircuitMgr.signStep(signDocument, instancedStepId); //[dipucr-Felipe #1059]
//			boolean ret = signCircuitMgr.signStepReparo(signDocument, instancedStepId);
			
			bCommit = true;
			
			return ret;
			
        } finally {
			if (!ongoingTX) {
				mcontext.endTX(bCommit);
			}
        }
	}
}
