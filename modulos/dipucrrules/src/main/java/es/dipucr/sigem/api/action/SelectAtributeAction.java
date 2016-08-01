package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

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

public class SelectAtributeAction extends BaseAction{
	
	private static final Logger logger = Logger.getLogger(SelectAtributeAction.class);
	protected int MAX_TBL_SEARCH_VALUES_DEFAULT = 50;
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
		String urlSicalwin = objPropiedadFichero.getProperty( "sicalwin.url" ); 
		
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
	    
	    //ejercicio de la partida
		String ejercPartida = request.getParameter("ejerPartida"); 
		
		// Codigo de Organizacion
		String codOrgan = request.getParameter("codOrgan");
	
		// Codigo de la entidad
		String codFuncional = request.getParameter("codFuncional");
		
		//Ejecicion Contable
		String ejeContable = request.getParameter("ejerContab");

		try
	    {
			// Componer la consulta con los parámetros de búsqueda
			ParametrosBean parametros = new ParametrosBean();
			//String[] adm = {codOrgan,codEntidad,ejeContable};//sustituir por los valores adecuados de la BD
			String codOrganAdm = objPropiedadFichero.getProperty( "sicalwin.codOrgan" );
			String codEntidadAdm = objPropiedadFichero.getProperty( "sicalwin.codEntidad" );
			String ejeContableAdm = objPropiedadFichero.getProperty( "sicalwin.ejeContable" );
			
			String[] adm = {codOrganAdm,codEntidadAdm,ejeContableAdm};
			parametros.setAdm(adm);

			
			if(ejercPartida!=null){
				parametros.setApgEje(ejercPartida);
			}
			if(codOrgan!=null){
				//Clasificacion organica de la partida
			      parametros.setApgOrg(codOrgan);
			}
			if(codFuncional!=null){
				//Clasicacion funcional de la partida
			      parametros.setApgFun(codFuncional);
			}
			if(ejeContable!=null){
				//Clasificacion economica de la partida
			      parametros.setApgEco(ejeContable);
			}
			
			RetornoBean rB = binding.sgsp(25,parametros);//cambiar n por el entero que corresponda
			
			if(rB.isResultado())
		      {
				
				AplicacionesPresupuestariasBean [] vAP = rB.getAplicacionesPresupuestarias();
				
				// Generar la lista para la vista
				//A todos los elementos se añade un campo ficticio orden_to_show para que se muestre el orden de manera incremental sin ningun salto
				//intermedio.
				Vector<ItemBean> vect = new Vector<ItemBean>();
				Vector<String> vectContiene = new Vector<String>();
			  for(int i=0; i<vAP.length; i++)
		        {

				  AplicacionesPresupuestariasBean item=vAP[i];
				  if(ejercPartida == null && !vectContiene.contains(item.getApg_eje())){
					  ItemBean itemB = new ItemBean();
					  itemB.setProperty("VALOR", item.getApg_eje());
					  itemB.setProperty("FUNCIONAL", "");
					  itemB.setProperty("ORGANIZACION", "");
					  itemB.setProperty("ECONOMICO", "");
					  itemB.setProperty("EJERCICIO", item.getApg_eje());
					  vect.add(itemB);
					  vectContiene.add(item.getApg_eje());
				  }
				  else{
					  if(codOrgan==null && ejercPartida != null && !vectContiene.contains(item.getApg_org())){
						  ItemBean itemB = new ItemBean();
						  itemB.setProperty("VALOR", item.getApg_org());
						  itemB.setProperty("FUNCIONAL", "");
						  itemB.setProperty("ORGANIZACION", item.getApg_org());
						  itemB.setProperty("ECONOMICO", "");
						  itemB.setProperty("EJERCICIO", ejercPartida);
						  vect.add(itemB);
						  vectContiene.add(item.getApg_org());
					  }
					  else{
						  if(codFuncional==null && codOrgan != null && !vectContiene.contains(item.getApg_fun())){
							  ItemBean itemB = new ItemBean();
							  itemB.setProperty("VALOR", item.getApg_fun());
							  itemB.setProperty("FUNCIONAL", item.getApg_fun());
							  itemB.setProperty("ORGANIZACION", codOrgan);
							  itemB.setProperty("ECONOMICO", "");
							  itemB.setProperty("EJERCICIO", ejercPartida);
							  vect.add(itemB);
							  vectContiene.add(item.getApg_fun());
						  }
						  else{
							  if(ejeContable==null && codFuncional != null && !vectContiene.contains(item.getApg_eco())){
								  ItemBean itemB = new ItemBean();
								  itemB.setProperty("VALOR", item.getApg_eco());
								  itemB.setProperty("FUNCIONAL", codFuncional);
								  itemB.setProperty("ORGANIZACION", codOrgan);
								  itemB.setProperty("ECONOMICO", item.getApg_eco());
								  itemB.setProperty("EJERCICIO", ejercPartida);
								  vect.add(itemB);
								  vectContiene.add(item.getApg_eco());
							  }
							  else{
								  if(ejercPartida != null && codOrgan != null && codFuncional != null && ejeContable != null && !vectContiene.contains(item.getApg_des())){
									  ItemBean itemB = new ItemBean();
									  itemB.setProperty("VALOR", item.getApg_des());
									  itemB.setProperty("FUNCIONAL", codFuncional);
									  itemB.setProperty("ORGANIZACION", codOrgan);
									  itemB.setProperty("ECONOMICO", ejeContable);
									  itemB.setProperty("EJERCICIO", ejercPartida);
									  vect.add(itemB);
									  vectContiene.add(item.getApg_des());
								  }
							  }
						  }
					  }
				  }
	        	
		        }
				  List<ItemBean> list = vect.subList(0, vect.size());
				  request.setAttribute("ValueList", list);
				
				// Obtiene el decorador
				CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
				BeanFormatter formatter = factory.getFormatter(
						getISPACPath("/digester/valueformatter.xml"));
				request.setAttribute("Formatter", formatter);
		      }
	    }
		 catch(org.apache.axis.AxisFault eAxis)
	    {
	      logger.error(eAxis.getMessage(), eAxis);
	    }
	    catch(java.rmi.RemoteException e)
	    {
	      logger.error(e.getMessage(), e);
	    }
	    catch(Throwable t)
	    {
	      logger.error(t.getMessage(), t);
	    }
		
				
		return mapping.findForward("success");
	}

}
