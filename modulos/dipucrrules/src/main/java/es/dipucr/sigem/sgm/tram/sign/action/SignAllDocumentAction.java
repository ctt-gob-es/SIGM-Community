package es.dipucr.sigem.sgm.tram.sign.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
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
 * Manda a la firma todos los documentos de un trámite
 */
public class SignAllDocumentAction extends SignDocumentsAction {

	protected static int NO_TASK_REGISTRO = 0;
	
	public SignAllDocumentAction(){
		super();
		bFirmarTodo = true;
	}
	
	@Override
	public IItemCollection getDocuments(HttpServletRequest request, SessionAPI session) throws ISPACException {

        ClientContext cct = session.getClientContext();
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		
		IState state = managerAPI.currentState(getStateticket(request));
		int idTramite = state.getTaskId();
		
		//INICIO [dipucr-Felipe #1497] Han usado varias veces el atrás y adelante del navegador y se ha perdido la sesión
		// ID_TRAMITE = 0 -> Toda la historia de adjuntos del registro telemático y presencial de la entidad
		if (idTramite == NO_TASK_REGISTRO){
			throw new ISPACInfo("Ha perdido la sesión del trámite. Por favor, evite usar los botones Atrás y Adelante del navegador "
					+ "mientras está tramitando expedientes y documentos en ALSIGM");
		}
		//FIN [dipucr-Felipe #1497]
		
		//Consulta para obtener todos los documentos del tramite que no esten firmados
	    StringBuffer sbQuery = new StringBuffer();
	    sbQuery.append("WHERE ID_TRAMITE = ");
	    sbQuery.append(idTramite);
	    sbQuery.append(" AND ESTADOFIRMA = '00'");
	    sbQuery.append(" ORDER BY ID");
	    
	    return entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, sbQuery.toString());
	}
}