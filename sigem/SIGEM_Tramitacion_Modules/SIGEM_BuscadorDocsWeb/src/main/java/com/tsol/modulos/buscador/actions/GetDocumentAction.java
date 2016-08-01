package com.tsol.modulos.buscador.actions;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tsol.modulos.buscador.utils.BuscadorDocsUtils;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

/**
 * [eCenpri-Felipe #828] Devuelve un documento del repositorio El estado del
 * documento debe ser "PÚBLICO" Recibe como parámetro el id de documento, en la
 * URL
 * 
 * @since 25.01.2013
 * @author Felipe
 * 
 */
public class GetDocumentAction extends Action {
	
	private static final String TIPO_DOC_PUBLICO = "PÚBLICO";
	private static final String USUARIO_SESION = "sigem";

	private static final Logger logger = Logger.getLogger(GetDocumentAction.class);

	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String usuario = USUARIO_SESION;
		
		String idEntidad = (String) request.getParameter("entidad");
		if (null == idEntidad || idEntidad.equals("")) {
			return null;
		}
		String idDoc = (String) request.getParameter("doc");
		if (null == idDoc || idDoc.equals("")) {
			return null;
		}
		
		SessionAPI sessionAPI = BuscadorDocsUtils.createSession(request, response, idEntidad, usuario);
		ClientContext ctx = sessionAPI.getClientContext();
		IInvesflowAPI invesflowAPI = sessionAPI.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

		// Obtener la información del documento
		IItem itemDoc = DocumentosUtil.getDocumento(entitiesAPI, Integer.valueOf(idDoc));
		if (itemDoc == null) {
			return null;
		}

		// Comprobamos que el documento sea público
		String estadoDoc = itemDoc.getString("ESTADO");
		if (null == estadoDoc || estadoDoc.equals("") || !estadoDoc.equals(TIPO_DOC_PUBLICO)) {
			logger.debug("El documento " + idDoc + " no está marcado con estado " + TIPO_DOC_PUBLICO);
			return null;
		}

		return BuscadorDocsUtils.imprimirDocumento(response, ctx, itemDoc);
	}
}