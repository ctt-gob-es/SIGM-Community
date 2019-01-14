package ieci.tdw.ispac.ispaccatalog.action;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.ispaccatalog.action.form.EntityForm;
import ieci.tdw.ispac.ispaccatalog.bean.PlantillaRepositorioComunBean;
import ieci.tdw.ispac.ispaccatalog.breadcrumbs.BreadCrumbsContainer;
import ieci.tdw.ispac.ispaccatalog.breadcrumbs.BreadCrumbsManager;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunes;
import ieci.tdw.ispac.ispaclib.util.FileRepositorioPlantillasComunesManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class ShowTemplatesRepositorioComunListAction extends BaseAction
{

 	public ActionForward executeAction( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
 		
 		int idTipoDoc = 0;
 		
		List<PlantillaRepositorioComunBean> listaDocumentos = new ArrayList<PlantillaRepositorioComunBean>();

		IInvesflowAPI invesFlowAPI = session.getAPI();
		ICatalogAPI catalogAPI= invesFlowAPI.getCatalogAPI();

		EntityForm entityForm = (EntityForm) form;
		
		String stype = request.getParameter("type");  //ID del tipo de documento
		
 		if (StringUtils.isEmpty(stype) ){
 			stype = entityForm.getProperty("ID_TPDOC");
 		}
 		
 		if(StringUtils.isNotEmpty(stype) && StringUtils.isNumeric(stype)){
 			idTipoDoc = Integer.parseInt(stype);
 		}			
 		if(idTipoDoc > 0){
 			FileRepositorioPlantillasComunes fileRepositorioPlantillasComunes = FileRepositorioPlantillasComunesManager.getInstance();
			String nombreDirectorio = fileRepositorioPlantillasComunes.getNombreDirectorio(session, idTipoDoc);
										
			if (StringUtils.isNotEmpty(nombreDirectorio)) {
				String strdir = fileRepositorioPlantillasComunes.getFileRepositorioPlantillasComunesPath();

				File dir = new File(strdir + File.separator + nombreDirectorio);
				
				if ( dir.exists() && dir.isDirectory()) {
					File[] content = dir.listFiles();
					if ((content != null) && (content.length > 0)){
						for (int i = 0; i < content.length; i++) {
							if (content[i].isFile()) {
								String nombre = content[i].getName();
								String path = content[i].getAbsolutePath().replace('\\', '/');
								long lastModified = content[i].lastModified();
								long length = content[i].length();
								
								PlantillaRepositorioComunBean plantilla = new PlantillaRepositorioComunBean();
								plantilla.set(PlantillaRepositorioComunBean.NOMBRE, nombre);
								plantilla.set(PlantillaRepositorioComunBean.PATH, path);
								plantilla.set(PlantillaRepositorioComunBean.LASTMODIFIED, new Date(lastModified));
								plantilla.set(PlantillaRepositorioComunBean.LENGTH, length);
								
								listaDocumentos.add(plantilla);
							}
						}
					}
				}
			}
 		}
        
       	CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
		BeanFormatter formatter = factory.getFormatter(getISPACPath("/formatters/templatesrepositoriocomunlistformatter.xml"));
		request.setAttribute("TemplatesRepositorioComunListFormatter", formatter);

   	 	request.setAttribute("TemplatesRepositorioComunList", listaDocumentos);

   	 	// Generamos la ruta de navegación hasta la pantalla actual.
		BreadCrumbsContainer bcm = BreadCrumbsManager.getInstance(catalogAPI).getBreadCrumbs(request);
 		request.setAttribute("BreadCrumbs", bcm.getList());

   	 	return mapping.findForward("success");
	}
}
