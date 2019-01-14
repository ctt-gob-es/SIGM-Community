package ieci.tdw.ispac.ispaccatalog.action;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.form.UploadForm;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunes;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunesManager;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ShowTemplateRepositorioComunAction extends BaseAction{
	
	protected int BUFFER_SIZE = 4096;

 	public ActionForward executeAction( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {

 		String documentTypeId;
 		String fileName = null;
 		
		UploadForm defaultForm = (UploadForm) form;
		
		documentTypeId = request.getParameter("type");		
 		if (documentTypeId == null)
 		{
 			documentTypeId = defaultForm.getProperty("ID_TPDOC");
 		}
		
 		fileName = request.getParameter("fileName");
 		
		if (null == documentTypeId || null == fileName) {
            return null;
        }
        
		if (documentTypeId != null) {
			
			int docTypeId = TypeConverter.parseInt(documentTypeId, -1);
			
			if (docTypeId > -1) {
				
				FileRepositorioPlantillasComunes fileRepositorioPlantillasComunes = FileRepositorioPlantillasComunesManager.getInstance();
				String strdir = fileRepositorioPlantillasComunes.getFileRepositorioPlantillasComunesPath();
				String nombreDirectorio = fileRepositorioPlantillasComunes.getNombreDirectorio(session, docTypeId);
				
            	File documento = new File(strdir + File.separator + nombreDirectorio + File.separator + fileName);
				ServletOutputStream out = response.getOutputStream();			

				response.setHeader("Pragma", "public");
		    	response.setHeader("Cache-Control", "max-age=0");
	            response.setHeader("Content-Transfer-Encoding", "binary");
	            
	            String extension = FilenameUtils.getExtension(fileName);	            
	            String mimetype = MimetypeMapping.getMimeType(extension);
	            response.setContentType(mimetype);
	            
             	response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");	            
	            response.setContentLength(new Long(documento.length()).intValue());
	            
	            try {
	            	FileInputStream is = new FileInputStream(documento);
	            	byte buffer[] = new byte[BUFFER_SIZE];
	            	int length = -1;
	            	while (true){
	            		length = is.read(buffer);
	            		if (length == -1) break;
	            		out.write(buffer, 0, length);
	            	}
	            	is.close();
	            } catch(Exception e){
	            	//Se saca el mensaje de error en la propia ventana, que habra sido lanzada con un popup
	            	response.setContentType("text/html");
	            	out.write(e.getCause().getMessage().getBytes());
	            }
	            finally{
	            	out.close();
	            }
			}
		}
		
        return null;
	}

}