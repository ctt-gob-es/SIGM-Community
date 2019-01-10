package ieci.tecdoc.sgm.consulta_telematico.action;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.ieci.tecdoc.isicres.desktopweb.utils.IOUtils;

import ieci.tecdoc.sgm.consulta_telematico.utils.Defs;
import ieci.tecdoc.sgm.consulta_telematico.utils.Utils;
import ieci.tecdoc.sgm.core.services.ConstantesServicios;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.registro.Document;
import ieci.tecdoc.sgm.core.services.registro.DocumentQuery;
import ieci.tecdoc.sgm.core.services.registro.Page;
import ieci.tecdoc.sgm.core.services.registro.RegisterWithPagesInfoPersonInfo;
import ieci.tecdoc.sgm.core.services.registro.ServicioRegistro;
import ieci.tecdoc.sgm.core.services.registro.UserInfo;
import ieci.tecdoc.sgm.core.services.telematico.Registro;
import ieci.tecdoc.sgm.core.services.telematico.RegistroTelematicoException;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.registro.exception.RegistroCodigosError;

/**
 * Clase que descargar un documento asociado a un registro
 *
 */
public class MostrarDocumentoAction extends ConsultaRegistroTelematicoWebAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		HttpSession session = request.getSession();

		ServletOutputStream out = response.getOutputStream();

		try {
			ServicioRegistroTelematico oServicioRegistroTelematico = LocalizadorServicios.getServicioRegistroTelematico();

			String idEntidad = (String)session.getAttribute(Defs.ENTIDAD_ID);
	    	if (idEntidad == null || "".equals(idEntidad)) {
	    		//return mapping.findForward(Defs.LOGIN_FORWARD);
	    	}

	    	String idSesion = (String)session.getAttribute(Defs.SESION_ID);
	    	if (idSesion == null || "".equals(idSesion)) {
	    		//return mapping.findForward(Defs.LOGIN_FORWARD);
	    	}

			String numRegistro = request.getParameter(Defs.NUMERO_REGISTRO);
			if (numRegistro == null || "".equals(numRegistro))
				return null;

			String code = request.getParameter(Defs.CODE);
			if (code == null || "".equals(code))
				return null;

			String extension = request.getParameter(Defs.EXTENSION);

			// Obtener el registro del documento a mostrar
			Registro registro = oServicioRegistroTelematico.obtenerRegistro(idSesion, numRegistro, Utils.obtenerEntidad(idEntidad));

			// Comprobar que el registro pertenezca al usuario conectado
			String cnifUsuario = (String)session.getAttribute(Defs.CNIF_USUARIO);
			if (!registro.getSenderId().equalsIgnoreCase(cnifUsuario)) {

				StringBuffer cCodigo = new StringBuffer(ConstantesServicios.SERVICE_EREGISTRY_ERROR_PREFIX);
				cCodigo.append(String.valueOf(RegistroCodigosError.EC_DOCUMENTO_SIN_PERMISOS));
				throw new RegistroTelematicoException(Long.valueOf(cCodigo.toString()).longValue());
			}

			// Obtener el documento a mostrar
			ServicioRegistro servicioRegistro = LocalizadorServicios.getServicioRegistro();

			UserInfo userInfo = new UserInfo();
			userInfo.setUserName("REGISTRO_TELEMATICO");
			userInfo.setPassword("*");
			
			RegisterWithPagesInfoPersonInfo infoRegPresencial = servicioRegistro.getInputRegister(userInfo, registro.getRegistryNumber(), Utils.obtenerEntidad(idEntidad));
					
			Integer bookId = null;
			Integer folderId = null;
			Integer docID = null;
			Integer pageID = null;
			for(Document doc : infoRegPresencial.getDocInfo()){
				if (null != doc.getDocumentName() && doc.getDocumentName().equals(code)){
					bookId = Integer.parseInt(doc.getBookId());
					folderId = Integer.parseInt(doc.getFolderId());
					docID = Integer.parseInt(doc.getDocID());
					pageID = Integer.parseInt(((Page)doc.getPages().get(0)).getPageID());
				}				
			}		
			
			DocumentQuery documentoInfo = servicioRegistro.getDocumentFolder(userInfo , bookId, folderId, docID, pageID,  Utils.obtenerEntidad(idEntidad));
			byte[] documento = documentoInfo.getContent();
			
			response.setHeader("Pragma", "public");
	    	response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Transfer-Encoding", "binary");
           	response.setHeader("Content-Disposition", "attachment; filename=\"" + code + "_" + registro.getRegistryNumber() + "." + extension + "\"");
            
            IOUtils.copy(new BufferedInputStream(new ByteArrayInputStream(documento)),out);
            
        }
		catch (Exception e) {
        	response.setContentType("text/html");
        	out.write(e.getMessage().getBytes());
        }
		finally {
        	out.flush();
        	out.close();
        }

        return null;
   	}
}
