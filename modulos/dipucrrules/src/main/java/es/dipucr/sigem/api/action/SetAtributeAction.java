package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import aytos.sw.sicalwin.AplicacionesPresupuestariasBean;
import aytos.sw.sicalwin.ParametrosBean;
import aytos.sw.sicalwin.RetornoBean;
import aytos.sw.sicalwin.SWXSgspInterfazServiceLocator;
import aytos.sw.sicalwin.SWXSgspInterfazSoapBindingStub;

public class SetAtributeAction extends BaseAction {
	
	private static final Logger logger = Logger.getLogger(SetAtributeAction.class);
	
	private static String ruta_path= "/forms/dipucr/sicalwin/"; 
	private static String fichero = "sicalwin.properties"; 
	private static Properties objPropiedadFichero = new Properties();

	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
		
		//Carga Fichero. 
		try {
	
			InputStream ArchivoConf = this.getServlet().getServletConfig().getServletContext().getResourceAsStream(ruta_path + fichero);
			
			objPropiedadFichero.load(ArchivoConf);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} 
		
		//Obteniendo Datos. 
		String urlSicalwin = objPropiedadFichero.getProperty( "sicalwin.url" ); //Identificador de 'Nombres' 
		
		
		SWXSgspInterfazSoapBindingStub binding = null;

	    //Contactar con el servicio web
	    try
	    {
	      URL dirSw = new URL(urlSicalwin);//sustituir por la URL correcta

	      binding = (SWXSgspInterfazSoapBindingStub)new
	        SWXSgspInterfazServiceLocator().getSWXSgspInterfaz(dirSw);
	      binding.setTimeout(60000);//esperamos 1 minuto
	    }
	    catch(java.net.MalformedURLException url)
	    {
			logger.error(url.getMessage(), url);
	    }
	    catch(javax.xml.rpc.ServiceException jre)
	    {
			logger.error(jre.getMessage(), jre);
	      if(jre.getLinkedCause() != null)
	      {
	      }
	    }
	    
	    
	    // Componer la consulta con los parámetros de búsqueda
		ParametrosBean parametros = new ParametrosBean();
		//String[] adm = {codOrgan,codEntidad,ejeContable};//sustituir por los valores adecuados de la BD
		String codOrganAdm = objPropiedadFichero.getProperty( "sicalwin.codOrgan" );
		String codEntidadAdm = objPropiedadFichero.getProperty( "sicalwin.codEntidad" );
		String ejeContableAdm = objPropiedadFichero.getProperty( "sicalwin.ejeContable" );
		
		String[] adm = {codOrganAdm,codEntidadAdm,ejeContableAdm};
		parametros.setAdm(adm);
		
			
		// Código del sustituto

		String ejercicio = request.getParameter("ejercicio");
		
		String organica = request.getParameter("organica");
		
		String funcional = request.getParameter("funcional");
		
		String economica = request.getParameter("economica");
		
		String param = request.getParameter("parameters");
		
		
		String [] divParam = param.split("_");
		
		ItemBean item = new ItemBean();
		//ejercicio
		if(divParam[2].equals("EJE")){
			parametros.setApgEje(ejercicio);
			RetornoBean rB = binding.sgsp(25,parametros);
			if(rB.isResultado())
		      {
				
				AplicacionesPresupuestariasBean [] vAP = rB.getAplicacionesPresupuestarias();
				if(vAP.length != 1){
					item.setProperty("EJERCICIO", ejercicio);
				}
				else{
					AplicacionesPresupuestariasBean aplicPresu=vAP[0];
					item.setProperty("EJERCICIO", ejercicio);
					item.setProperty("FUNCIONAL", aplicPresu.getApg_fun());
					item.setProperty("ORGANIZACION", aplicPresu.getApg_org());
					item.setProperty("ECONOMICO", aplicPresu.getApg_eco());
					item.setProperty("DESCRIPCION", aplicPresu.getApg_des());
				}
				
		      }
		}
		
		//organica
		if(divParam[2].equals("ORG")){
			parametros.setApgEje(ejercicio);
			parametros.setApgOrg(organica);
			RetornoBean rB = binding.sgsp(25,parametros);
			if(rB.isResultado())
		      {
				
				AplicacionesPresupuestariasBean [] vAP = rB.getAplicacionesPresupuestarias();

				if(vAP.length != 1){
					item.setProperty("ORGANIZACION", organica);			
				}
				else{
					AplicacionesPresupuestariasBean aplicPresu=vAP[0];
					item.setProperty("ORGANIZACION", organica);
					item.setProperty("FUNCIONAL", aplicPresu.getApg_fun());
					item.setProperty("ECONOMICO", aplicPresu.getApg_eco());
					item.setProperty("DESCRIPCION", aplicPresu.getApg_des());
				}
				
		      }
		}
		
		//funcional
		if(divParam[2].equals("FUN")){
			parametros.setApgEje(ejercicio);
			parametros.setApgOrg(organica);
			parametros.setApgFun(funcional);
			RetornoBean rB = binding.sgsp(25,parametros);
			if(rB.isResultado())
		      {
				
				AplicacionesPresupuestariasBean [] vAP = rB.getAplicacionesPresupuestarias();

				if(vAP.length != 1){
					item.setProperty("FUNCIONAL", funcional);			
				}
				else{
					AplicacionesPresupuestariasBean aplicPresu=vAP[0];
					item.setProperty("FUNCIONAL", aplicPresu.getApg_fun());
					item.setProperty("ECONOMICO", aplicPresu.getApg_eco());
					item.setProperty("DESCRIPCION", aplicPresu.getApg_des());
				}
				
		      }
		}
		
		//economica
		if(divParam[2].equals("ECO")){
			parametros.setApgEje(ejercicio);
			parametros.setApgOrg(organica);
			parametros.setApgFun(funcional);
			parametros.setApgEco(economica);
			RetornoBean rB = binding.sgsp(25,parametros);
			if(rB.isResultado())
		      {
				
				AplicacionesPresupuestariasBean [] vAP = rB.getAplicacionesPresupuestarias();

				if(vAP.length != 1){
					item.setProperty("ECONOMICO", economica);			
				}
				else{
					AplicacionesPresupuestariasBean aplicPresu=vAP[0];
					item.setProperty("ECONOMICO", economica);
					item.setProperty("DESCRIPCION", aplicPresu.getApg_des());
				}
				
		      }
		}


		// Salva en la petición el bean del sustituto
		//request.setAttribute("Substitute", item);
		request.setAttribute("Value", item);
        	
		return mapping.findForward("success");
	}
	


}
