package ieci.tdw.ispac.ispaccatalog.action;

import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaccatalog.action.form.UploadForm;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunes;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunesManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class StoreTemplateRepositorioComunAction extends BaseAction{
	
	protected int BUFFER_SIZE = 4096;

	public ActionForward executeAction( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		
 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] { ISecurityAPI.FUNC_INV_DOCTYPES_EDIT, ISecurityAPI.FUNC_INV_REPOSITORIO_COMUN_EDIT});
		
		int idTipoDoc = 0;
		
    	// Formulario asociado a la acción
		UploadForm defaultForm = (UploadForm) form;

		String parameter = request.getParameter("type");
 		if (parameter == null)
 		{
 			parameter = defaultForm.getProperty("ID_TPDOC");
 		}
		idTipoDoc = Integer.parseInt(parameter);

		try {
			FormFile fichero = defaultForm.getUploadFile();
			String fileName = fichero.getFileName();

			if (StringUtils.isEmpty(fileName)) {
				fileName = "default.tmp";					
			} 
			if (fichero.getFileSize() > 0) {
				FileRepositorioPlantillasComunes fileRepositorioPlantillasComunes = FileRepositorioPlantillasComunesManager.getInstance();

				String nombreDirectorio = fileRepositorioPlantillasComunes.getNombreDirectorio(session, idTipoDoc); 
				
				if(StringUtils.isNotEmpty(nombreDirectorio)){
					fileName = nombreDirectorio + File.separator + fileName;

					InputStream fin = null;
					try{
						fin = fichero.getInputStream();
						fileRepositorioPlantillasComunes.put(fin, fileName);
					}finally{
						if(null != fin){
							fin.close();
						}
					}
				}
			}
		} catch(Exception e) {
			ActionForward action = mapping.findForward("success");
			String url = action.getPath() + "?type=" + idTipoDoc;

			request.getSession().setAttribute(BaseAction.LAST_URL_SESSION_KEY, url);
			
			if (e instanceof ISPACInfo) {
				throw e;
			} else {
				throw new ISPACInfo(e.getMessage());
			}
		}
		
		ActionForward action = mapping.findForward("success");
		String redirected = action.getPath() + "?type=" + idTipoDoc;
		return new ActionForward( action.getName(), redirected, true);
	}
}