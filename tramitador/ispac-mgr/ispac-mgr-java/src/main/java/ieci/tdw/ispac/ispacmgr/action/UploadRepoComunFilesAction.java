package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunes;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunesManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class UploadRepoComunFilesAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		
		List<File> listaDocumentos = new ArrayList<File>();
	
		//Establecer el tipo de documento si previamente se ha seleccionado
		String documentTypeId = request.getParameter("documentTypeId");		
		if (documentTypeId != null) {
			request.setAttribute("documentTypeId", documentTypeId);			
			
			int idTipoDoc = TypeConverter.parseInt(documentTypeId, -1);
			
			if (idTipoDoc > -1) {
				
				FileRepositorioPlantillasComunes fileRepositorioPlantillasComunes = FileRepositorioPlantillasComunesManager.getInstance();			
				String nombreDirectorio = fileRepositorioPlantillasComunes.getNombreDirectorio(session, idTipoDoc);
				
				if(StringUtils.isNotEmpty(nombreDirectorio)){
					String strdir = fileRepositorioPlantillasComunes.getFileRepositorioPlantillasComunesPath();
					File dir = new File(strdir + File.separator + nombreDirectorio);
					
					if ( dir.exists() && dir.isDirectory()) {
						File[] content = dir.listFiles();
						if ((content != null) && (content.length > 0)){
							for (int i = 0; i < content.length; i++) {
								if (content[i].isFile()) {
									listaDocumentos.add(content[i]);
								}
							}
						}
					}
				}
			}
			request.setAttribute("listaDocumentos", listaDocumentos);
			
			return mapping.findForward("success");
		}
		return null;
	}
}
