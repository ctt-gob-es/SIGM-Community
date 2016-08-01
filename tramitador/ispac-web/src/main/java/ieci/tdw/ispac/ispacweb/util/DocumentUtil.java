package ieci.tdw.ispac.ispacweb.util;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;

public class DocumentUtil {
	  private static final Logger LOGGER = Logger.getLogger("xml");
	  private static final Locale DEFAULT_LOCALE = new Locale("es_ES");
	  
	/**
	 * Ver un documento.
	 *
	 * @param servletCtx Contexto del servlet.
	 * @param request Petición actual.
	 * @param response Respuesta.
	 * @param servletName Nombre del servlet que se invoca para descargar el fichero.
	 * @param session Sesión del usuario.
	 * @param id Identificador de la entidad.
	 * @param docref Identificador único del documeno en el gestor documental
	 * @param mimeType Tipo del fichero.
	 * @param readonly Indicador de sólo lectura.
	 * @param defaultView Indicador para permitir ver cualquier tipo de fichero.
	 * @param reloadTopWindow Indicador para recargar la página principal.
	 * @return Cierto si se ve el documento, en caso contrario, falso.
	 * @throws Exception Si se produce algún error.
	 */
	public static boolean viewDocument(ServletContext servletCtx,
									   HttpServletRequest request,
			   						   HttpServletResponse response,
			   						   String servletName,
			   						   SessionAPI session,
			   						   String id,
			   						   String docref,
			   						   String mimeType,
			   						   String readonly,
			   						   boolean defaultView,
			   						   boolean reloadTopWindow) throws Exception {

		String url = generateURL(request, servletName, session.getTicket(), id, mimeType);

		boolean useOdtTemplantes = ConfigurationMgr.getVarGlobalBoolean(session.getClientContext(), ConfigurationMgr.USE_ODT_TEMPLATES, false);

	    if ( "application/msword".equalsIgnoreCase(mimeType)
	    		|| "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(mimeType)
	    		|| "application/excel".equalsIgnoreCase(mimeType)
	    		|| "application/x-excel".equalsIgnoreCase(mimeType)
				|| "application/x-msexcel".equalsIgnoreCase(mimeType)
				|| "application/vndms-excel".equalsIgnoreCase(mimeType)
				|| "application/vnd.ms-excel".equalsIgnoreCase(mimeType)
				|| "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(mimeType)
	    		|| "application/mspowerpoint".equalsIgnoreCase(mimeType)
				|| "application/powerpoint".equalsIgnoreCase(mimeType)
				|| "application/vndms-powerpoint".equalsIgnoreCase(mimeType)
				|| "application/vnd.ms-powerpoint".equalsIgnoreCase(mimeType)
				|| "application/x-mspowerpoint".equalsIgnoreCase(mimeType)
				|| ("application/vnd.oasis.opendocument.text".equalsIgnoreCase(mimeType))
				|| "application/vnd.openxmlformats-officedocument.presentationml.presentation".equalsIgnoreCase(mimeType)
				|| "application/vnd.openxmlformats-officedocument.presentationml.slideshow".equalsIgnoreCase(mimeType)) {

	    	String htmlPage = generateHtmlPage(servletCtx, request, url, mimeType, readonly, reloadTopWindow, useOdtTemplantes);

	    	ServletOutputStream out = response.getOutputStream();
	    	response.setHeader("Pragma", "public");
	    	response.setHeader("Cache-Control", "max-age=0");
	    	response.setContentType("text/html");
	    	out.write(htmlPage.getBytes());
	    	out.flush();
	    	out.close();

	    } else if ( defaultView
	    		|| mimeType.equalsIgnoreCase("application/pdf") ) {

		    // Para pdf se descarga como un adjunto, ya que si se intenta abrir
	    	// dentro de ActiveX de Internet Explorer no lo hace correctamente
	    	// en algunos equipos, sera por temas de configuracion

            ServletOutputStream out = response.getOutputStream();
	    	response.setContentType(mimeType);
	    	response.setHeader("Pragma", "public");
	    	response.setHeader("Cache-Control", "max-age=0");
            response.setHeader("Content-Transfer-Encoding", "binary");
        	response.setHeader("Content-Disposition", new StringBuffer()
        			.append("attachment; filename=\"").append(id).append(".")
        			.append(MimetypeMapping.getExtension(mimeType))
        			.append("\"").toString());
        	IGenDocAPI genDocAPI = session.getAPI().getGenDocAPI();
			Object connectorSession = null;
			try {
				connectorSession = genDocAPI.createConnectorSession();
	        	response.setContentLength(genDocAPI.getDocumentSize(connectorSession, docref));
	        	genDocAPI.getDocument(connectorSession, docref, out);
			}finally {
				if (connectorSession != null) {
					genDocAPI.closeConnectorSession(connectorSession);
				}
	    	}

            out.flush();
            out.close();

	    } else {
	    	return false;
	    }

	    return true;
	}

	/**
	 * Generar la URL del servlet que se invoca para descargar el fichero.
	 *
	 * @param request Petición actual.
	 * @param servletName Nombre del servlet.
	 * @param ticket Ticket de la sesión.
	 * @param id Identificador de la entidad.
	 * @param mimeType Tipo MIME del documento.
	 * @return URL del servlet que se invoca para descargar el fichero.
	 */
	public static String generateURL(HttpServletRequest request,
							   		 String servletName,
							   		 String ticket,
							   		 String id,
							   		 String mimeType) {

		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String organizationId = null;
		if (info != null)
			organizationId = info.getOrganizationId();

		StringBuffer url = new StringBuffer(request.getScheme());
		url.append("://");
		url.append(request.getServerName());
		url.append(":");
		url.append(Integer.toString(request.getServerPort()));
		url.append(request.getContextPath());
		url.append("/");
		url.append(servletName);
		if (organizationId != null){
			url.append("/");
			url.append(organizationId);
		}
		url.append("/");
		url.append(ticket);
		url.append("/");
		url.append(id);

		if (StringUtils.isNotBlank(mimeType)) {
			String ext = MimetypeMapping.getExtension(mimeType);
			if (!id.endsWith("." + ext)) {
				url.append(".").append(ext);
			}
		}

		return url.toString();
	}

	/**
	 * Generar la URL para la eliminación del fichero.
	 *
	 * @param request Petición actual.
	 * @param id Identificador de la entidad.
	 * @return URL del servlet que se invoca para descargar el fichero.
	 */
	public static String generateDocumentDeleteURL(HttpServletRequest request, String id) {

		return new StringBuffer(request.getScheme())
			.append("://")
			.append(request.getServerName())
			.append(":")
			.append(request.getServerPort())
			.append(request.getContextPath())
			.append("/deleteFile.do?file=")
			.append(id)
			.toString();
	}

	/**
	 * Generar el código HTML del applet que permite abrir documentos.
	 *
	 * @param servletCtx Contexto del servlet.
	 * @param context Contexto de la aplicación.
	 * @param url URL del servlet que se invoca para descargar el fichero.
	 * @param mimeType Tipo MIME
	 * @param readOnly Indica si el documento es de solo lectura.
	 * @param reloadTopWindow Indicador para recargar la página principal.
	 * @return Código HTML del applet que permite abrir el documento.
	 * @throws Exception Si se produce algún error.
	 */
	@Deprecated
	public static String generateHtmlPage(
			ServletContext servletCtx, String context, String url, String mimeType,
			String readOnly, boolean reloadTopWindow, boolean useOdtTemplates) throws Exception {

		LOGGER.warn("Método deprecado");
		return crearPaginaEdicionDocumentosPorProtocolo(url, reloadTopWindow, DEFAULT_LOCALE);
	}

	/**
	 * Generar el código HTML del applet que permite abrir documentos.
	 *
	 * @param servletCtx Contexto del servlet.
	 * @param request Petición.
	 * @param url URL del servlet que se invoca para descargar el fichero.
	 * @param mimeType Tipo MIME
	 * @param readOnly Indica si el documento es de solo lectura.
	 * @param reloadTopWindow Indicador para recargar la página principal.
	 * @return Código HTML del applet que permite abrir el documento.
	 * @throws Exception Si se produce algún error.
	 */
	@Deprecated
	public static String generateHtmlPage(
			ServletContext servletCtx, HttpServletRequest request, String url, String mimeType,
			String readOnly, boolean reloadTopWindow, boolean useOdtTemplates) throws Exception {

		LOGGER.warn("Método deprecado");
		return crearPaginaEdicionDocumentosPorProtocolo(url, reloadTopWindow, DEFAULT_LOCALE);
	}

	/**
	 * Generar el código HTML del applet que permite abrir documentos.
	 *
	 * @param servletCtx Contexto del servlet.
	 * @param context Contexto de la aplicación.
	 * @param url URL del servlet que se invoca para descargar el fichero.
	 * @param mimeType Tipo MIME
	 * @param readOnly Indica si el documento es de solo lectura.
	 * @param reloadTopWindow Indicador para recargar la página principal.
	 * @return Código HTML del applet que permite abrir el documento.
	 * @throws Exception Si se produce algún error.
	 */
	@Deprecated
	public static String generateHtmlCode(
			ServletContext servletCtx, String context, String url, String mimeType,
			String readOnly, boolean reloadTopWindow) throws Exception {

		LOGGER.warn("Método deprecado");
		return crearPaginaEdicionDocumentosPorProtocolo(url, reloadTopWindow, DEFAULT_LOCALE);
	}

	/**
	 * Generar el código HTML del applet que permite abrir documentos.
	 *
	 * @param servletCtx Contexto del servlet.
	 * @param request Petición.
	 * @param url URL del servlet que se invoca para descargar el fichero.
	 * @param mimeType Tipo MIME
	 * @param readOnly Indica si el documento es de solo lectura.
	 * @param reloadTopWindow Indicador para recargar la página principal.
	 * @return Código HTML del applet que permite abrir el documento.
	 * @throws Exception Si se produce algún error.
	 */
	public static String generateHtmlCode(
			ServletContext servletCtx, HttpServletRequest request, String url, String mimeType,
			String readOnly, boolean reloadTopWindow) throws Exception {
		return crearPaginaEdicionDocumentosPorProtocolo(url, reloadTopWindow, (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY));
	}
	
	
	
	
	
	
	
	private static String crearPaginaEdicionDocumentosPorProtocolo(	String url, boolean reloadTopWindow, Locale locale) throws Exception {
		String urlDescargasComponentes = ISPACConfiguration.getInstance().get(ISPACConfiguration.COMPONETES_USUARIO_URL_DESCARGA);
		String hrefInvocacion = String.format("sigemEditLauncher:urlDoc=%s&lang=%s&urlCheckForUpdates=%s", url, locale.getLanguage(), urlDescargasComponentes);
		
		
        return new StringBuffer()
        	.append("<html>")
        	.append("<head>")
        	.append("<script type=\"text/javascript\" src='./ispac/scripts/jquery-1.3.2.min.js'></script>")
        	.append("<script language='javascript'>")
        	.append("var newhref; ")
        	.append("$(window).unload(function() { sleepFor(5000); }); ")
        	.append(generateReloadJavaScript())
        	.append(generateSleepJavascript())        	
        	.append(generateWaitAndReloadJavaScript(reloadTopWindow))
			.append("</script>")
        	.append("</head>")
        	.append("<body onload=\'").append("document.getElementById(\"protocolLink\").click();")
			.append(generateJSCodeReloadTopWindowWithTimeout(2000))
        	.append("\'>")
        	.append("<a id='protocolLink' href='" + hrefInvocacion + "'></a>")
        	.append("</body>")
        	.append("</html>")
        	.toString();
	}
	
	private static String generateReloadJavaScript() {
		StringBuffer result = new StringBuffer();
		result.append("function reloadPage() { ");
		result.append("top.window.location.href = newhref;");
		result.append("}");
		return result.toString();
	}
		
	private static String generateSleepJavascript() {
		StringBuffer codigoJs = new StringBuffer();
		codigoJs.append("function sleepFor(sleepDuration){");
		codigoJs.append("var now = new Date().getTime();");
		codigoJs.append("while(new Date().getTime() < now + sleepDuration){ /* do nothing, just wait */ }} ");
		return codigoJs.toString();
	}
	
	private static String generateJSCodeReloadTopWindowWithTimeout(int timeout) {
		return "waitAndReload(" + timeout + ");"; 
	}
	
	private static String generateWaitAndReloadJavaScript(boolean reload) {
		StringBuffer result = new StringBuffer();
		result.append("function waitAndReload(timeout) { ");	
		result.append("top.ispac_needToConfirm = false; ");
		result.append(generateJSCodeLocationHref());
		result.append("sleepFor(timeout); ");
		if (reload) {
			result.append("setTimeout(function() {reloadPage()},5000); ");
		}
		result.append("}");
		return result.toString();
	}
	
	
	private static String generateJSCodeLocationHref() {
		// Refrescar el top manteniendo el bloque activo
		return new StringBuffer().append("var activeBlock = top.window.document.getElementById('block');")
				  .append("newhref = top.window.location.href;")
				  .append("if (newhref.indexOf('?') == -1) {")
				  .append("newhref = newhref + '?reload=true';")
				  .append("} else if (newhref.indexOf('reload') == -1) {")
				  .append("newhref = newhref + '&reload=true';")
				  .append("}")
				  .append("if (activeBlock) {")
				  .append("if (newhref.indexOf('block') == -1) {")
				  .append("newhref = newhref + '&block=' + activeBlock.value;")
				  .append("} else {")
				  .append("index = newhref.indexOf('block');")
				  .append("newhref1 = newhref.substring(0,index);")
				  .append("newhref1 = newhref1 + 'block=' + activeBlock.value;")
				  .append("newhref2 = newhref.substr(index);")
				  .append("index2 = newhref2.indexOf('&');")
				  .append("if (index2 != -1) {")
				  .append("newhref1 = newhref1 + newhref2.substr(index2);")
				  .append("}")
				  .append("newhref = newhref1;")
				  .append("}")
				  .append("}").toString();
	}
	

	

	

	
	

	/**
	 * Generar el código HTML del applet que permite imprimir documentos.
	 *
	 * @param servletCtx Contexto del servlet.
	 * @param id Identificador del documento
	 * @param url URL del servlet que se invoca para descargar el fichero.
	 * @param mimeType Tipo MIME
	 * @param callbackFunction Función JavaScript de retorno.
	 * @return Código HTML del applet que permite imprimir el documento.
	 */
	public static String generateHtmlCodeForPrinting(
				ServletContext servletCtx, String id, String url, String mimeType,
				String callbackFunction) {

        return new StringBuffer()
        	.append("<script language='javascript'>")
			.append("if (window.ActiveXObject) {")
			.append(getActiveXCodeForPrinting(id, url, mimeType, callbackFunction))
			.append("}")
			.append("</script>")
        	.toString();
	}

	protected static String getActiveXCodeForPrinting(String id, String url,
			String mimeType, String callbackFunction) {

		StringBuffer code = new StringBuffer();

		code.append("var application = null;");
		code.append("try {");

	    if ("application/msword".equalsIgnoreCase(mimeType)
	    		|| "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(mimeType)) {

	    	// ActiveX para abrir MS Word
			code.append("application = new ActiveXObject('Word.Application');");
			code.append("var openDocument = application.Documents.Open('")
				.append(url).append("', false, true);");
			code.append("openDocument.PrintOut(false);");
			code.append("openDocument.Close(0);");

	    } else if ("application/excel".equalsIgnoreCase(mimeType)
	    		|| "application/x-excel".equalsIgnoreCase(mimeType)
				|| "application/x-msexcel".equalsIgnoreCase(mimeType)
				|| "application/vndms-excel".equalsIgnoreCase(mimeType)
				|| "application/vnd.ms-excel".equalsIgnoreCase(mimeType)
				|| "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equalsIgnoreCase(mimeType)) {

	    	// ActiveX para abrir MS Excel
			code.append("application = new ActiveXObject('Excel.Application');");
			code.append("var openDocument = application.Workbooks.Open('")
				.append(url).append("', false, true);");
			code.append("openDocument.PrintOut(false);");
			code.append("openDocument.Close(0);");

	    } else if ("application/mspowerpoint".equalsIgnoreCase(mimeType)
				|| "application/powerpoint".equalsIgnoreCase(mimeType)
				|| "application/vndms-powerpoint".equalsIgnoreCase(mimeType)
				|| "application/vnd.ms-powerpoint".equalsIgnoreCase(mimeType)
				|| "application/x-mspowerpoint".equalsIgnoreCase(mimeType)
				|| "application/vnd.openxmlformats-officedocument.presentationml.presentation".equalsIgnoreCase(mimeType)
				|| "application/vnd.openxmlformats-officedocument.presentationml.slideshow".equalsIgnoreCase(mimeType)) {

	    	// ActiveX para abrir MS Powerpoint
			code.append("application = new ActiveXObject('PowerPoint.Application');");
			code.append("var openDocument = application.Presentations.Open('")
				.append(url).append("', false, true);");
			code.append("openDocument.PrintOut(false);");
			code.append("openDocument.Close(0);");

		} else {

	    	// ActiveX para abrir Internet Explorer
			code.append("application = new ActiveXObject('InternetExplorer.Application');")
				.append("application.navigate('" + url + "');")
				.append("application.visible = true;")
				.append("application.execWB(6, true);");
		}

	    if (StringUtils.isNotBlank(callbackFunction)) {
	    	code.append(callbackFunction + "(true, '" + id + "');");
	    }

		code.append("} catch (e) {");

		if (StringUtils.isNotBlank(callbackFunction)) {
	    	code.append(callbackFunction + "(false, '" + id + "', e);");
	    }

		code.append("} finally {")
			.append("if (application != null) { application.Quit(); }")
			.append("}");

		return code.toString();
	}

	

	

	/**
	 * Generar el código JavaScript que permite recargar la página principal.
	 * @return Código JavaScript que permite recargar la página principal.
	 */
	protected static String generateJSCodeReloadTopWindow() {

    	return "top.ispac_needToConfirm = false; " + generateJSCodeLocationHref() + " top.window.location.href = newhref;";
	}



	

	/**
	 * Obtiene la información de un documento.
	 * @param session Sesión de tramitación.
	 * @param documentId Identificador del documento.
	 * @return Información del documento.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static IItem getDocumentItem(SessionAPI session, int documentId) throws ISPACException {

		IInvesflowAPI invesflowAPI = session.getAPI();
        IEntitiesAPI entityAPI = invesflowAPI.getEntitiesAPI();

        // Información del documento
        return entityAPI.getDocument(documentId);
	}

	/**
	 * Obtiene el tipo MIME de un documento.
	 * @param session Sesión de tramitación.
	 * @param documentId Identificador del documento.
	 * @return Tipo MIME
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String getDocumentMimeType(SessionAPI session, int documentId) throws ISPACException {

		String mimeType = null;

        // Información del documento
        IItem docItem = getDocumentItem(session, documentId);

        if (docItem != null) {

        	// GUID del documento
        	String guid = docItem.getString("INFOPAG_RDE");
        	if (StringUtils.isBlank(guid)) {
        		guid = docItem.getString("INFOPAG");
        	}

        	// Obtener el tipo MIME a partir del GUID
        	mimeType = getDocumentMimeType(session, guid);
        }

		return mimeType;
	}

	/**
	 * Obtiene el tipo MIME de un documento.
	 * @param session Sesión de tramitación.
	 * @param guid GUID del documento.
	 * @return Tipo MIME
	 * @throws ISPACException si ocurre algún error.
	 */
	public static String getDocumentMimeType(SessionAPI session, String guid) throws ISPACException {

		String mimeType = null;

    	if (StringUtils.isNotBlank(guid)) {

    		IGenDocAPI genDocAPI = session.getAPI().getGenDocAPI();
			Object connectorSession = null;

			try {

				// Iniciar conexión con el conector documental
				connectorSession = genDocAPI.createConnectorSession();

	        	// Obtener el tipo MIME del documento
				mimeType = genDocAPI.getMimeType(connectorSession, guid);

			} finally {
				if (connectorSession != null) {
					genDocAPI.closeConnectorSession(connectorSession);
				}
			}
    	}

    	return mimeType;
	}

	/**
	 * Descarga un documento.
	 * @param session Sesión de tramitación.
	 * @param documentId Identificador del documento.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static void downloadDocument(HttpServletResponse response,
			SessionAPI session, int documentId) throws ISPACException {

        // Obtener la información del documento
        IItem documentItem = DocumentUtil.getDocumentItem(session, documentId);
        if (documentItem != null) {

        	// Obtener el nombre del documento
        	String name = documentItem.getString("NOMBRE");

        	// Obtener el GUID del documento
        	String guid = documentItem.getString("INFOPAG");

    		if (StringUtils.isNotBlank(guid)) {

    			IGenDocAPI genDocAPI = session.getAPI().getGenDocAPI();
    			Object connectorSession = null;

    			try {
    				connectorSession = genDocAPI.createConnectorSession();

    				// Tipo MIME del documento
    				String mimetype = genDocAPI.getMimeType(connectorSession, guid);

    				// Información de respuesta
    				ServletOutputStream out = response.getOutputStream();
    		    	response.setHeader("Pragma", "public");
    		    	response.setHeader("Cache-Control", "max-age=0");
    				response.setHeader("Content-Transfer-Encoding", "binary");
    				response.setContentType(mimetype);

    				String extension = documentItem.getString("EXTENSION");
    	            if (StringUtils.isBlank(extension)) {
    	            	response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
    	            } else {
    	            	response.setHeader("Content-Disposition", "inline; filename=\"" + name + "."
    	            			+ extension + "\"");
    	            }

    	            // Tamaño del fichero
    	            response.setContentLength(genDocAPI.getDocumentSize(connectorSession, guid));

    				// Descarga del documento
    				genDocAPI.getDocument(connectorSession, guid, out);
    			} catch (IOException e) {
    				throw new ISPACException(e);
    			} finally {
    				if (connectorSession != null) {
    					genDocAPI.closeConnectorSession(connectorSession);
    				}
    			}
    		}
        }
	}

}