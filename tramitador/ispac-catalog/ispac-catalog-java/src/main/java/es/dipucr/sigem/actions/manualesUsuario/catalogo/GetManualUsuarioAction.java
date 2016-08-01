package es.dipucr.sigem.actions.manualesUsuario.catalogo;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IManualUsuarioAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuario;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class GetManualUsuarioAction extends BaseAction {

 	public ActionForward executeAction(ActionMapping mapping,
 									   ActionForm form,
 									   HttpServletRequest request,
 									   HttpServletResponse response,
 									   SessionAPI session) throws Exception {
 		
 		ClientContext cct = session.getClientContext();

 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, cct, new int[] {
				ISecurityAPI.FUNC_COMP_REPORTS_READ,
				ISecurityAPI.FUNC_COMP_REPORTS_EDIT });

		IInvesflowAPI invesFlowAPI = session.getAPI();
    	IManualUsuarioAPI manualUsuarioAPI = invesFlowAPI.getManualUsuarioAPI();
		
 		// Obtener el identificador del informe
 		String manualUsuarioId = request.getParameter("manualUsuarioId");
 		
    	CTManualUsuario ctManualUsuario = manualUsuarioAPI.getManualUsuario(Integer.parseInt(manualUsuarioId));

		if (ctManualUsuario != null) {
			
			//Obtener la propiedad que queremos que sea descargada
			String nombre = ctManualUsuario.getName();
			String mimeType = ctManualUsuario.getMimetype();
			String extension = MimetypeMapping.getExtension(mimeType);
		
			String fileName = null;
		
	 		// Descargar el informe
	 		fileName = nombre + "." + extension;
			if (StringUtils.isEmpty(fileName)){
				fileName = "ManualUsuario.pdf";
			}
			
			response.setContentType(mimeType);
	    	response.setHeader("Pragma", "public");
	    	response.setHeader("Cache-Control", "max-age=0");
	    	response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			response.setContentLength(ctManualUsuario.getSize());

	 		ServletOutputStream outputStream = response.getOutputStream();
	 		DbCnt cnt = session.getClientContext().getConnection();
			
	 		CTManualUsuario.getManualUsuario(cnt, Integer.parseInt(manualUsuarioId), outputStream);
			
			outputStream.close();
		}
		else {
			 throw new ISPACInfo("exception.entities.report.empty");
		}

		return null;
	}
 	
}