package com.tsol.modulos.buscador.actions;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tsol.modulos.buscador.utils.BuscadorDocsUtils;

public class ShowDocumentAction extends Action {

	private static final Logger LOGGER = Logger.getLogger(ShowDocumentAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// Identificador del documento
        String documentId = request.getParameter("id");
        LOGGER.info("Recuperación del documento " + documentId);

		// API de sesión de iSPAC
        SessionAPI sessionAPI = SessionAPIFactory.getSessionAPI(request, response);
        IInvesflowAPI invesflowAPI = sessionAPI.getAPI();
        IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

        // Contexto para la auditoría
		BuscadorDocsUtils.setAuditContext(request, sessionAPI);

        // Obtener la información del documento
		IItem documentItem = entitiesAPI.getDocument(TypeConverter.parseInt(documentId, 0));
        if (documentItem == null) {
            return null;
        }

        //[dipucr-Felipe #828 #1216]
    	return BuscadorDocsUtils.imprimirDocumento(response, sessionAPI.getClientContext(), documentItem);
    }
	
}