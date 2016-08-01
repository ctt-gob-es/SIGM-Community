package es.dipucr.sigem.actions.manualesUsuario;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IManualUsuarioAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuario;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowManualAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		IInvesflowAPI invesflowAPI = session.getAPI();
		IManualUsuarioAPI manualUsuarioAPI = invesflowAPI.getManualUsuarioAPI();
		
		// Identificador del manual
		String strManualId = request.getParameter("id");
		int manualUsuarioId = Integer.parseInt(strManualId);

		// Recuperamos el manual por su id
		CTManualUsuario ctManualUsuario = manualUsuarioAPI.getManualUsuario(manualUsuarioId);
		
		if (ctManualUsuario != null) {
			
			//Miramos si tiene url
			String url = ctManualUsuario.getURL();
			if (StringUtils.isNotEmpty(url)){
				response.sendRedirect(url);
			}
			else{
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
				
		 		CTManualUsuario.getManualUsuario(cnt, manualUsuarioId, outputStream);
				
				outputStream.close();
			}
		}
		else {
			 throw new ISPACInfo("El manual con id " + manualUsuarioId + " no existe");
		}
		
		return null;
	}
}
