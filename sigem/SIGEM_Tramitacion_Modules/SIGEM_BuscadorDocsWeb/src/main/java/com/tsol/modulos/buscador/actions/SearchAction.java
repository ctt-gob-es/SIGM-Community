package com.tsol.modulos.buscador.actions;

import ieci.tdw.ispac.api.errors.ISPACException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.tsol.modulos.buscador.utils.BuscadorDocsUtils;

public class SearchAction extends Action {
	
	private static final Logger LOGGER = Logger.getLogger(SearchAction.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String entidad = request.getParameter("entidad");
		String usuario = null;
		
		//INICIO [dipucr-Felipe #828 #1216]
		try{
			BuscadorDocsUtils.createSession(request, response, entidad, usuario);
		}
		catch (ISPACException e){
			
			LOGGER.error("Se ha producido un error inesperado", e);

			ActionErrors errors = new ActionErrors();
			errors.add("error", new ActionError("search.error"));

			saveErrors(request, errors);
		}
		//FIN [dipucr-Felipe #828 #1216]

		return mapping.findForward("success");
	}
}
