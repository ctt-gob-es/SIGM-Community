package es.dipucr.sigem.firmaReparo.sign;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.common.constants.SignCircuitStates;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitInstanceDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitMgr;

public class SignReparoCircuitMgr extends SignCircuitMgr{

	private static final String IDSEQUENCE = "SPAC_SQ_ID_CTOS_FIRMA_INSTANCE";
	
	/**
	 * Contexto de cliente.
	 */
	private IClientContext mcontext = null;
		
	public SignReparoCircuitMgr(IClientContext clientContext) {
		super(clientContext);
		this.mcontext = clientContext;
	}
	
	protected void instanceNextStep(DbCnt cnt, int circuitId, int instancedCircuitId, int documentId) 
			throws ISPACException {
		
		SignCircuitInstanceDAO circuitInstance = SignCircuitInstanceDAO.nexStep(cnt, instancedCircuitId);
		if (circuitInstance != null) { // Siguiente paso del circuito de firmas
			
			// Actualizar el estado del paso del circuito de firmas.
			circuitInstance.set("ESTADO", SignCircuitStates.PENDIENTE);
			circuitInstance.store(cnt);
			
			// EVENTO: Inicio de paso de circuito de firmas
			processCircuitStepEvents(cnt, EventsDefines.EVENT_EXEC_START_CIRCUIT_STEP, circuitInstance);
			
		} else { // No hay más pasos. Fin del circuito de firmas

			// Actualizar estado de firma del documento
			IItem document = mcontext.getAPI().getEntitiesAPI().getDocument(documentId);
			document.set("ESTADOFIRMA", SignStatesConstants.FIRMADO_CON_REPAROS);
			document.store(mcontext);
			
			// EVENTO: Fin del circuito de firma
			processCircuitEvents(EventsDefines.EVENT_EXEC_END, circuitId, instancedCircuitId, documentId);
		}
	}
	


}