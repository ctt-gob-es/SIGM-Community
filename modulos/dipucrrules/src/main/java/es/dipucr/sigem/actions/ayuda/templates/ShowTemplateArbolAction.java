package es.dipucr.sigem.actions.ayuda.templates;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.ITemplateAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.dao.cat.CTTemplate;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowTemplateArbolAction extends BaseAction {
	
	private static Logger logger = Logger.getLogger(ShowTemplateArbolAction.class);
	
	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
		String template = request.getParameter("template");
		ServletOutputStream out = response.getOutputStream();
		
		String usuario = (String) request.getAttribute("User");
		String plantilla = "";
		String cod_plantilla = "";
		
		if (!StringUtils.isEmpty(template)) {
			IInvesflowAPI invesFlowAPI = session.getAPI();
	    	ITemplateAPI templateAPI = invesFlowAPI.getTemplateAPI();
	    	CTTemplate ctTemplate = templateAPI.getTemplate(Integer.parseInt(template));
	    	
	    	if (StringUtils.equalsIgnoreCase(ctTemplate.getMimetype(),"application/vnd.oasis.opendocument.text") && !ConfigurationMgr.getVarGlobalBoolean(session.getClientContext(), ConfigurationMgr.USE_ODT_TEMPLATES, false)){
	    		throw new ISPACInfo(getResources(request).getMessage("exception.template.odt.disabled"));
	    	}
	    	
			try {
		    	response.setHeader("Pragma", "public");
		    	response.setHeader("Cache-Control", "max-age=0");
	            response.setHeader("Content-Transfer-Encoding", "binary");
	            				
	        	String mimetype = ctTemplate.getMimetype();
	        	plantilla = ctTemplate.getName();
	        	cod_plantilla = ctTemplate.getString("TEMPLATE:COD_PLANT");
	        	
	            response.setContentType(mimetype);
	            
	            String extension = MimetypeMapping.getExtension(mimetype);
             	if ("application/pdf".equalsIgnoreCase(mimetype) || StringUtils.isBlank(extension)) { 
             		String name = template; 
             		if (StringUtils.isNotBlank(extension)){ 
             			name += "." + extension; 
             		} 
             		response.setHeader("Content-Disposition", "attachment; filename=\"" + name + "\""); 
             	} else {
	            	response.setHeader("Content-Disposition", "inline; filename=\"" + template + "." + extension + "\"");
	            }
	            
	            response.setContentLength(ctTemplate.getSize());
	            try {
	            	ctTemplate.getTemplate(session.getClientContext().getConnection(), out);
	            }
	            catch(ISPACException e){
	            	//Se saca el mensaje de error en la propia ventana, que habra sido lanzada con un popup
	            	response.setContentType("text/html");
	            	out.write(e.getCause().getMessage().getBytes());
	            }
	            finally{
	            	out.close();
	            }
			}
			finally {
				logger.info("USUARIO:'" + usuario + "' - PLANTILLA [CÓDIGO] '" + plantilla + "' [" + cod_plantilla + "] - MODO CONSULTA.");
			}
		}
        else {
        	//Se saca el mensaje de error en la propia ventana, que habra sido lanzada con un popup
        	response.setContentType("text/html");
        	logger.error("Idtenficaidor documento nulo");
        	String message = new ISPACInfo("exception.documents.notExists").getExtendedMessage(session.getClientContext().getLocale());
        	out.write(message.getBytes());
        	out.close();
        }

	    return null;
	}
	
}