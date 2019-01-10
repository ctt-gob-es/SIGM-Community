package es.dipucr.sigem.actions.ayuda.templates;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ITemplateAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.dao.cat.CTTemplate;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.util.DocumentUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditTemplateArbolAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(EditTemplateArbolAction.class);
		
	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		
		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] {ISecurityAPI.FUNC_INV_TEMPLATES_EDIT});

		String template = request.getParameter("template");

		if (!StringUtils.isEmpty(template)) {
		
			IInvesflowAPI invesFlowAPI = session.getAPI();
	    	ITemplateAPI templateAPI = invesFlowAPI.getTemplateAPI();
	    	CTTemplate ctTemplate = templateAPI.getTemplate(Integer.parseInt(template));
	    	
			String usuario = (String) request.getAttribute("User");
			String plantilla = ctTemplate.getName();
        	String cod_plantilla = ctTemplate.getString("TEMPLATE:COD_PLANT");

        	logger.info("USUARIO:'" + usuario + "' - PLANTILLA [CÓDIGO] '" + plantilla + "' [" + cod_plantilla + "] - MODO EDICIÓN.");
        	
        	if (StringUtils.equalsIgnoreCase(ctTemplate.getMimetype(),"application/vnd.oasis.opendocument.text") && !ConfigurationMgr.getVarGlobalBoolean(session.getClientContext(), ConfigurationMgr.USE_ODT_TEMPLATES, false)){
	    		throw new ISPACInfo(getResources(request).getMessage("exception.template.odt.disabled"));
	    	}
	    	
			viewDocument(getServlet().getServletContext(), request, response, "template", session, template, null, ctTemplate.getMimetype(), null, false, false);
		}

	    return null;
	}
	
	public boolean viewDocument(ServletContext servletCtx, HttpServletRequest request, HttpServletResponse response, String servletName,
			SessionAPI session, String id, String docref, String mimeType, String readonly, boolean defaultView, boolean reloadTopWindow) throws Exception {
		
		String url = DocumentUtil.generateURL(request, servletName, session.getTicket(), id, mimeType);
		
		if ( DocumentUtil.esMimeTypeEditable(mimeType)){
			
			String htmlPage = generateHtmlPageNoJava(url, mimeType, id, readonly, reloadTopWindow);
			
			ServletOutputStream out = response.getOutputStream();
			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.setContentType("text/html");
			out.write(htmlPage.getBytes());
			out.flush();
			out.close();	
		} else if ( defaultView || mimeType.equalsIgnoreCase("application/pdf")){
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
	
	public String generateHtmlPageNoJava(String url, String mimeType, String document, String readOnly, boolean reloadTopWindow) throws Exception {
			
        return new StringBuffer()
        	.append("<html>\n")
        	.append("	<head>\n")
        	.append("		<script language='javascript'>\n")        	
        	.append(getFunctionOfficeNoJavaProtocolo(url, reloadTopWindow, mimeType, document, readOnly))
			.append("		</script>\n")
        	.append("	</head>\n")
        	.append("	<body onload=\"lazarOffice()\">\n")
        	.append("	</body>\n")
        	.append("</html>")
        	.toString();
	}
	
	private String getFunctionOfficeNoJavaProtocolo(String url, boolean reloadTopWindow, String mimeType, String document, String readonly)throws Exception {
		String ext = MimetypeMapping.getExtension(mimeType);
		
		StringBuffer resultado =  new StringBuffer()
			.append("			function lazarOffice() {\n")
			.append("				var protocolo = localStorage.getItem('" + ext + "');\n")
			.append("				if(String(protocolo) == 'null'){\n")			
			.append("					parent.showFrame(\"workframe_seleccion_editor\", \"seleccionarEditorArbol.do?extension=" + ext + "&document=" + document + "&readonly=" + readonly + "\", \"\", \"\", \"\", false);")
			.append("				}\n")
			.append("				else{\n");
			if(reloadTopWindow){
				resultado.append(DocumentUtil.generateJSCodeReloadTopWindowWithTimeout(5000));
			}
			resultado.append("					window.location.href = localStorage.getItem('" + ext + "') + \"" + url + "\";\n")		
			.append("				}\n")
			.append("				return true;\n")
			.append("			}\n");
			return resultado.toString();
	}	
}