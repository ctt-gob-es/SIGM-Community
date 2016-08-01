package es.dipucr.sigem.rechazoFirma.sign;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.common.constants.SignCircuitStates;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitInstanceDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitMgr;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;

public class SignRechazoCircuitMgr extends SignCircuitMgr {

	/**
	 * Secuencia para numerar la instacina de un circuito de firma, este id sera
	 * introducido en el campo ID_INSTANCIA_CIRCUITO de cada paso de un circuito
	 * instanciado
	 */
	private static final String IDSEQUENCE = "SPAC_SQ_ID_CTOS_FIRMA_INSTANCE";
	
	/**
	 * Contexto de cliente.
	 */
	private IClientContext mcontext = null;
	
	
	public SignRechazoCircuitMgr(IClientContext clientContext) {
		super(clientContext);
		this.mcontext = clientContext;
	}
	
	public boolean rechazarStep(SignDocument signDocument, int instancedStepId, String motivoRechazo) throws ISPACException {
		
		// Se debe comprobar si es el ultimo paso del circuito de firma para activar el siguiente
		DbCnt cnt = mcontext.getConnection();
		
		try {
			SignCircuitInstanceDAO signCircuitInstanceDAO = new SignCircuitInstanceDAO(cnt, instancedStepId);
		
			// Rechazamos el paso de firma
			signCircuitInstanceDAO.set("ESTADO", SignCircuitStates.RECHAZADO);
			signCircuitInstanceDAO.store(cnt);
			
			// EVENTO: Fin de paso de circuito de firmas
			processCircuitStepEvents(cnt, EventsDefines.EVENT_EXEC_END_CIRCUIT_STEP, signCircuitInstanceDAO);

			//[eCenpri-Felipe #421] Error al rechazar firma
			//Recuperamos todos los demas pasos del circuito de firma para irlos rechazando
			SignCircuitInstanceDAO circuitInstance = null;
			int idInstanciaCircuito = Integer.MIN_VALUE;
			do{
				idInstanciaCircuito = signCircuitInstanceDAO.getInt("ID_INSTANCIA_CIRCUITO");
				// Siguiente paso del circuito de firmas
				circuitInstance = SignCircuitInstanceDAO.nexStep(cnt, idInstanciaCircuito);
				
				if (circuitInstance != null) { 
					circuitInstance.set("ESTADO", SignCircuitStates.RECHAZADO);
					circuitInstance.store(cnt);
				}
							
			} while (circuitInstance != null); 
			

			IItem document = mcontext.getAPI().getEntitiesAPI().getDocument(signDocument.getItemDoc().getKeyInt());
			document.set("ESTADOFIRMA", SignStatesConstants.RECHAZADO);
			//[eCenpri-Felipe #354/#206] Se pone el motivo rechazo antes de ejecutar las reglas de evento Fin Circuito de Firma
			document.set("MOTIVO_RECHAZO", motivoRechazo);
			document.store(mcontext);
			
			// EVENTO: Fin del circuito de firma
			processCircuitEvents(EventsDefines.EVENT_EXEC_END,  signCircuitInstanceDAO.getInt("ID_CIRCUITO"), 
					signCircuitInstanceDAO.getInt("ID_INSTANCIA_CIRCUITO"), signDocument.getItemDoc().getKeyInt());
			
		} finally {
			mcontext.releaseConnection(cnt);
		}		
		return true;
	}

}
