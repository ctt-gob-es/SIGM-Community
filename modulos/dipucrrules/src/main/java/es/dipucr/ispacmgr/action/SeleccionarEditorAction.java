package es.dipucr.ispacmgr.action;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SeleccionarEditorAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		//Extensión del documento
		String extension = request.getParameter("extension");
		request.setAttribute("extension", extension);
		
		//id del documento
		String document = request.getParameter("document");
		request.setAttribute("document", document);
		
		//propiedad readonly
		String readonly = request.getParameter("readonly");
		request.setAttribute("readonly", readonly);		
				
		return mapping.findForward("success");
	}
}
