package es.dipucr.sigem.actions.manualesUsuario.catalogo;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IManualUsuarioAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.action.form.EntityForm;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DeleteManualUsuarioAction extends BaseAction
{
	public ActionForward executeAction(ActionMapping mapping, 
									   ActionForm form, 
									   HttpServletRequest request,
									   HttpServletResponse response, 
									   SessionAPI session) throws Exception	{
		
 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] {
				ISecurityAPI.FUNC_INV_PROCEDURES_EDIT,
				ISecurityAPI.FUNC_INV_TEMPLATES_EDIT});

		IInvesflowAPI invesFlowAPI = session.getAPI();
        IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
        IManualUsuarioAPI manualUsuarioAPI = invesFlowAPI.getManualUsuarioAPI();
        
		// Formulario asociado a la acción
		EntityForm defaultForm = (EntityForm) form;

		int keyId = Integer.parseInt(defaultForm.getKey());
		int entityId = Integer.parseInt(defaultForm.getEntity());	
		
		//si la plantilla es de tipo especifica hay que eliminar registro de la tabla q asocia plantilla a procedimientos
		if (manualUsuarioAPI.isProcedureManualUsuario(keyId)){
			manualUsuarioAPI.deleteManualUsuarioProc(keyId);
		} else {
			manualUsuarioAPI.deleteManualUsuario(keyId);
		}

		ActionForward action = mapping.findForward("success");
		String redirected = action.getPath();
		return new ActionForward( action.getName(), redirected, true);					
	}
}