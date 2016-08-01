package ieci.tdw.ispac.ispacweb.servlet;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispacweb.util.DocumentUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class DownloadFileTemporaryServlet extends ISPACBaseServlet {
  
	private static final long serialVersionUID = 1L;
	
	/** 
	 * Logger de la clase. 
	 */
	protected static Logger logger = Logger.getLogger(DownloadFileTemporaryServlet.class);

	
	/**
	 * Process a GET request for the specified resource.
	 *
	 * @param request The servlet request we are processing
	 * @param response The servlet response we are creating
	 *
	 * @exception IOException if an input/output error occurs
	 * @exception ServletException if a servlet-specified error occurs
	 */
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		try {
			// Sesión de tramitación
	        SessionAPI session = getSession(request);
	
	        // Identificador del documento
	        int documentId = getDocumentId(request);
	        
	        // TODO Descargar el documento TEMPORAL, tengo que pasar de alguna forma la entidad y el path
	        DocumentUtil.downloadDocument(response, session, documentId);
	        //FileTemporaryManager.getInstance().get(out, path);
	        
		} catch (Exception e) {
			logger.error("Error al descargar el documento", e);
		}
	}
	
	protected int getDocumentId(HttpServletRequest request) {
		
		String aux = request.getPathInfo();
		
		int ix = aux.lastIndexOf("/");
		if (ix > 0) {
			aux = aux.substring(ix + 1);
		}
		
		ix = aux.lastIndexOf(".");
		if (ix > 0) {
			aux = aux.substring(0, ix);
		}
		
		return TypeConverter.parseInt(aux, 0);
	}
	
	protected String getDocumentEntity(HttpServletRequest request) {
		
		String aux = request.getPathInfo();
		
		int ix = aux.lastIndexOf("/");
		if (ix > 0) {
			aux = aux.substring(ix - 3,ix - 1);
		}
				
		return aux;
	}
}
