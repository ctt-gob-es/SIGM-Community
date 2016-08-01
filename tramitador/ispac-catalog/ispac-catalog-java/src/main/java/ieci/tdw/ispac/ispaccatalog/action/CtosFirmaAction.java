package ieci.tdw.ispac.ispaccatalog.action;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CtosFirmaAction extends BaseDispatchAction {

	/**
	 * Añade el circuito de firma al procedimiento.
	 * @param mapping El ActionMapping utilizado para seleccionar esta instancia
	 * @param form El ActionForm bean (opcional) para esta petición
	 * @param request La petición HTTP que se está procesando
	 * @param response La respuesta HTTP que se está creando
	 * @param session Información de la sesión del usuario
	 * @return La redirección a la que se va a transferir el control.
	 * @throws ISPACException si ocurre algún error.
	 */
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws ISPACException {

		ClientContext cct = session.getClientContext();
		
 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, cct, new int[] {
				ISecurityAPI.FUNC_INV_PROCEDURES_EDIT });

        IInvesflowAPI invesFlowAPI = cct.getAPI();
		ICatalogAPI catalogAPI= invesFlowAPI.getCatalogAPI();
		
		//INICIO [eCenpri-Felipe #592]
		int entityId = Integer.parseInt(request.getParameter("entityId"));
		String sRegId = request.getParameter("pcdId");

		String ctofirmasId = request.getParameter("ctofirmasId");
		
		if (StringUtils.isNotBlank(sRegId)) {
			
			int regId = Integer.parseInt(sRegId);
			
			request.setAttribute("Refresh","true");
	
			//INICIO [eCenpri-Felipe #592]
			if (entityId == ICatalogAPI.ENTITY_P_PROCEDURE){ 
				catalogAPI.addCtoFirmas(regId, Integer.parseInt(ctofirmasId));
			}
			else if (entityId == ICatalogAPI.ENTITY_P_TASK){
				catalogAPI.addCtoFirmasTramite(regId, Integer.parseInt(ctofirmasId));
			}
			else{
	        	throw new ISPACException("CtosFirmaAction.add:" +
	        			"El entityId " + entityId + " no es válido");
	        }
			//FIN [eCenpri-Felipe #592]		
		}

		return mapping.findForward("closeIFrame");
	}
	
	/**
	 * Elimina el circuito de firmas seleccionado del procedimiento.
	 * @param mapping El ActionMapping utilizado para seleccionar esta instancia
	 * @param form El ActionForm bean (opcional) para esta petición
	 * @param request La petición HTTP que se está procesando
	 * @param response La respuesta HTTP que se está creando
	 * @param session Información de la sesión del usuario
	 * @return La redirección a la que se va a transferir el control.
	 * @throws ISPACException si ocurre algún error.
	 */
	public ActionForward delete(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws ISPACException {

        ClientContext cct = session.getClientContext();

 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, cct, new int[] {
				ISecurityAPI.FUNC_INV_PROCEDURES_EDIT });

        IInvesflowAPI invesFlowAPI = cct.getAPI();
		ICatalogAPI catalogAPI= invesFlowAPI.getCatalogAPI();

//		[eCenpri-Felipe #592]
		int regId = Integer.parseInt(request.getParameter("regId"));
		String delId = request.getParameter("delId");
		
		//INICIO [eCenpri-Felipe #592]
		int entityId = Integer.parseInt(request.getParameter("entityId"));
		
		if (delId != null){
			if (entityId == ICatalogAPI.ENTITY_P_PROCEDURE){ 
				catalogAPI.dropCtoFirmas(regId, Integer.parseInt(delId));
			}
			else if (entityId == ICatalogAPI.ENTITY_P_TASK){
				catalogAPI.dropCtoFirmasTramite(regId, Integer.parseInt(delId));
			}
			else{
	        	throw new ISPACException("CtosFirmaAction.add:" +
	        			"El entityId " + entityId + " no es válido");
	        }
		}
		//FIN [eCenpri-Felipe #592]
		    		
		return new ActionForward(new StringBuffer()
   	 			.append("/showProcedureEntity.do?method=ctosfirma&entityId=")
   	 			.append(request.getParameter("entityId"))
   	 			.append("&regId=")
   	 			.append(request.getParameter("regId"))
   	 			.toString(), true);
    }
}
