package ieci.tecdoc.sgm.registro.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.registro.Document;
import ieci.tecdoc.sgm.core.services.registro.DocumentQuery;
import ieci.tecdoc.sgm.core.services.registro.Page;
import ieci.tecdoc.sgm.core.services.registro.RegisterWithPagesInfoPersonInfo;
import ieci.tecdoc.sgm.core.services.registro.ServicioRegistro;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;
import ieci.tecdoc.sgm.registro.util.Definiciones;
import ieci.tecdoc.sgm.registro.utils.Defs;
import ieci.tecdoc.sgm.registro.utils.Misc;

public class DescargarJustificanteAction extends RegistroWebAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws Exception {

		ServletOutputStream out = response.getOutputStream();

		try {
			String numRegistro = (String)request.getParameter("numeroRegistro");

	         // Obtener el documento a mostrar
			ServicioRegistro servicioRegistroPresencial = LocalizadorServicios.getServicioRegistro();
	
			UserInfo userInfo = new UserInfo();
			userInfo.setUserName("REGISTRO_TELEMATICO");
			userInfo.setPassword("*");
			
			RegisterWithPagesInfoPersonInfo infoRegPresencial = servicioRegistroPresencial.getInputRegister(userInfo, numRegistro, Misc.obtenerEntidad(request));
					
			Integer bookId = null;
			Integer folderId = null;
			Integer docID = null;
			Integer pageID = null;
			for(Document doc : infoRegPresencial.getDocInfo()){
				if (null != doc.getDocumentName() && doc.getDocumentName().equals(Definiciones.REGISTRY_RECEIPT_CODE)){
					bookId = Integer.parseInt(doc.getBookId());
					folderId = Integer.parseInt(doc.getFolderId());
					docID = Integer.parseInt(doc.getDocID());
					pageID = Integer.parseInt(((Page)doc.getPages().get(0)).getPageID());
				}				
			}		
			
			DocumentQuery documentoInfo = servicioRegistroPresencial.getDocumentFolder(userInfo , bookId, folderId, docID, pageID,  Misc.obtenerEntidad(request));
			byte[] justificante = documentoInfo.getContent();
         
			response.setHeader("Pragma", "public");
	    	response.setHeader("Cache-Control", "max-age=0");
            response.setContentType("application/pdf");
            response.setHeader("Content-Transfer-Encoding", "binary");
           	response.setHeader("Content-Disposition", "attachment; filename=\"Justificante_" + numRegistro + ".pdf\"");
		
            response.setContentLength(justificante.length);
            out.write(justificante, 0, justificante.length);

        } catch(Exception e) {

    		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_OBTENER_JUSTIFICANTE);
	    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.getMessage());

	    	return mapping.findForward("failure");
        } finally {
        	if (out != null) {
        		out.flush();
        		out.close();
        	}
        }

	    return null;
   	}
}
