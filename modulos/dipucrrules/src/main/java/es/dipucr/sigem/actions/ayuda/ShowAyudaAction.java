package es.dipucr.sigem.actions.ayuda;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.util.ActionRedirect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowAyudaAction extends BaseAction {
	
	public final static String CUADRO_PROCEDIMIENTO = "CUADRO_PROCEDIMIENTO";
	
	/**
	 * Ejecuta la lógica de la acción.
	 * @param mapping El ActionMapping utilizado para seleccionar esta instancia
	 * @param form El ActionForm bean (opcional) para esta petición
	 * @param request La petición HTTP que se está procesando
	 * @param response La respuesta HTTP que se está creando
	 * @param session Información de la sesión del usuario
	 * @return La redirección a la que se va a transferir el control.
	 * @throws ISPACException si ocurre algún error.
	 */
 	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
 		
 		request.getSession().removeAttribute(CUADRO_PROCEDIMIENTO);
		
		ActionRedirect ret = new ActionRedirect(mapping.findForward("home"));
		String path = ret.getPath();
//		path = path +"&entityId="+request.getParameter("entityId") + "&";
		path = path +"&entityId=22&";
		path = path +"regId="+request.getParameter("regId");
		ret.setPath(path);
		ret.setRedirect(true);
		return ret;
	}
}
