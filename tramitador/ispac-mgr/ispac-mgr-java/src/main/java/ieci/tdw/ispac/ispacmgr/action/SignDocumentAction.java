package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.SignDocumentsAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * [dipucr-Felipe #1246] Clase rehecha por completo a raiz del nuevo portafirmas
 * Manda a la firma el documento dónde nos encontramos del detalle
 */
public class SignDocumentAction extends SignDocumentsAction {

	public SignDocumentAction(){
		super();
		bFirmarDetalle = true;
	}
	
	@Override
	public IItemCollection getDocuments(HttpServletRequest request, SessionAPI session) throws ISPACException {

        ClientContext cct = session.getClientContext();
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		
		IState state = managerAPI.currentState(getStateticket(request));
		int idDocumento = state.getEntityRegId();
		
		//Consulta para obtener todos los documentos del tramite que no esten firmados
	    StringBuffer sbQuery = new StringBuffer();
	    sbQuery.append("WHERE ID = ");
	    sbQuery.append(idDocumento);
	    
	    return entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, sbQuery.toString());
	}
}