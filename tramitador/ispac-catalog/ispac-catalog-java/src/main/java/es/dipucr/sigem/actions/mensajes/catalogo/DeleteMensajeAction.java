package es.dipucr.sigem.actions.mensajes.catalogo;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IMensajeAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.entities.EntityManageAction;
import ieci.tdw.ispac.ispaccatalog.action.form.EntityForm;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeleteMensajeAction extends EntityManageAction {
	
	public ActionForward deleteMensaje(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception	{
		
 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] { ISecurityAPI.FUNC_INV_PROCEDURES_EDIT, ISecurityAPI.FUNC_INV_TEMPLATES_EDIT});

		IInvesflowAPI invesFlowAPI = session.getAPI();
        IMensajeAPI mensajeAPI = invesFlowAPI.getMensajeAPI();
        
        // Formulario asociado a la acción
        EntityForm defaultForm = (EntityForm) form;
        		
		if(StringUtils.isNumeric(defaultForm.getKey())){
			int keyId = Integer.parseInt(defaultForm.getKey());			
			mensajeAPI.deleteMensaje(keyId);
		}
			
		ActionForward action = mapping.findForward("success");
		String redirected = action.getPath();
		return new ActionForward( action.getName(), redirected, true);					
	}
	
	public ActionForward deleteMensajes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws ISPACException {

    	IInvesflowAPI invesFlowAPI = session.getAPI();
		IMensajeAPI mensajeAPI = invesFlowAPI.getMensajeAPI();
		
    	EntityForm frm = (EntityForm)form;
    	String[] mensajesIds = frm.getMultibox();
    	
    	try {
			for (int i = 0; i < mensajesIds.length; i++) {
				if(StringUtils.isNumeric(mensajesIds[i])){
					int mensajeId = Integer.parseInt(mensajesIds[i]);
					
					mensajeAPI.deleteMensaje(mensajeId);
				}
			}
    	}
    	catch (Exception e) {
    		saveAppErrors(request, getActionMessages(request, e));
    	}
    	
    	ActionForward action = mapping.findForward("success");
		String redirected = action.getPath();
		return new ActionForward( action.getName(), redirected, true);	
    }
}