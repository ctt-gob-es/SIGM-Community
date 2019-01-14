package ieci.tdw.ispac.ispaccatalog.action;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaccatalog.action.form.EntityForm;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunes;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunesManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class DeleteTemplatesRepositorioComunListAction extends BaseAction{

 	public ActionForward executeAction( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
 		
 		String stype = null;
 		int idTipoDoc = 0;
 		String[] plantillas = null;

		EntityForm entityForm = (EntityForm) form;
		
		stype = request.getParameter("type");  //ID del tipo de documento
		
 		if (StringUtils.isEmpty(stype) ){
 			stype = entityForm.getProperty("ID_TPDOC");
 		}
 		
 		if(StringUtils.isNotEmpty(stype) && StringUtils.isNumeric(stype)){
 			idTipoDoc = Integer.parseInt(stype);
 		}
 		plantillas = entityForm.getMultibox();
 		
 		if(null == plantillas || plantillas.length == 0){
 			return mapping.findForward("success");
 		}
 		if(idTipoDoc > 0){
 			FileRepositorioPlantillasComunes fileRepositorioPlantillasComunes =FileRepositorioPlantillasComunesManager.getInstance();
			String nombreDirectorio = fileRepositorioPlantillasComunes.getNombreDirectorio(session, idTipoDoc);
			if(StringUtils.isNotEmpty(nombreDirectorio)){
				for(int i = 0; i < plantillas.length ; i++){
					String fileName = nombreDirectorio + File.separator + plantillas[i];
					fileRepositorioPlantillasComunes.delete(fileName);
				}
			}
 		}

   	 	return mapping.findForward("success");
	}
}
