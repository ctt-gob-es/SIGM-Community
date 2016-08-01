package es.dipucr.sigem.servlets;

import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.admsesion.backoffice.ServicioAdministracionSesionesBackOffice;
import ieci.tecdoc.sgm.core.services.admsesion.backoffice.Sesion;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * [eCenpri-Manu Ticket #307] ALSIGM3 Logotipo Entidad en Documentos
 * @author Manu
 *
 */
public class ImagenesServlet extends HttpServlet {
	
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ImagenesServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public final static String SUBDIR_PARAM_NAME="subdir";
	public final static String DEFAULT_SKIN="defaultSkin";
		
	protected String subdir="";
	
	protected String defaultSkin;
	
	
	
	public void init() throws ServletException {
		
		this.subdir=this.getInitParameter(SUBDIR_PARAM_NAME);
		this.defaultSkin=this.getInitParameter(DEFAULT_SKIN);
		
	}
	
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		doWork(req, resp);
	}
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		doWork(req,resp);
		
	}
	protected void doWork(HttpServletRequest req, HttpServletResponse resp){
		
		String logoLocation=this.getLocation(req, resp);
		try{
			InputStream input=new FileInputStream(logoLocation);
			OutputStream output = resp.getOutputStream();
			IOUtils.copy(input, output);
			input.close();
		    output.flush();
		}catch (Exception ex){
			logger.error("Error cargando recurso a traves de ResourceDispatcherServlet",ex);
		}
	}
	
	
	/**
	 * Obtiene la localizacion fisica del recurso lo buscará en el skin correspondiente o sino en el skin por defecto
	 * @param req
	 * @param resp
	 * @return
	 */
	protected String getLocation(HttpServletRequest req, HttpServletResponse resp){
		String result="";
						
		
		String fileName=getFileName(req);
		
		///SIGEM_AplicacionWeb/skinEntidad_numeroEntidad/ficheroLogo
		String skinName=getSkinName(req);
		String fullFileName=skinName+ File.separator+fileName;
		
				
		SigemConfigFilePathResolver pathResolver = SigemConfigFilePathResolver.getInstance();
		result = pathResolver.resolveFullPath(fullFileName,subdir);
		
		// si el fichero no existe devolver fichero con skin por defecto
		if (StringUtils.isEmpty(result)){
			fullFileName= getDefaultSkin()+File.separator+fileName;
			result = pathResolver.resolveFullPath(fullFileName,subdir);
		}
		
		if (StringUtils.isEmpty(result)){
			logger.warn("No se ha podido cargar el recurso solicitado:"+getFileName(req) +" a traves de "+this.getClass().getName());
		}
		
				
		return result;
	}
	
	/**
	 * Obtiene el filename a partir de lo que se concatena a la peticion del servlet
	 * ejemplo: 
	 * Si la peticion es /pruebaWar/logoServlet/images/logo.gif"
	 * devolveria como filename "images/logo.gif"
	 * @param req
	 * @return
	 */
	protected String getFileName(HttpServletRequest req){
		
		String result="";
		
		//"req.getServletPath()"= "/logoServlet" /"req.getContextPath()"= "/pruebaWar" "req.getRequestURI()"= "/pruebaWar/logoServlet/images/logo.gif"
		result= req.getRequestURI();
		//result=StringUtils.remove(result, req.getContextPath()+req.getServletPath());
		result=StringUtils.replace(result, req.getContextPath()+req.getServletPath(), "");
		
		
				
		return result;
	}
	
	/**
	 * Obtiene el nombre del skin a usar
	 * @param req
	 * @return
	 */
	protected String getSkinName(HttpServletRequest req){
		String result="";
		
		try {
			String idEntidad=getIdEntidad(req);
			
			result= "skinEntidad_"+idEntidad;
		} catch (Exception e) {
			logger.error("Error al obtener la entidad del usuario en sesión", e);
		}
		
		return result;
	}
	
	/**
	 * metodo que retorna el identificador de identidad para el usuario actualmente logado, para ello debe existir en la 
	 * request o en la session el parámetro <code>ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO</code>
	 * que actualmente tiene el valor "keySesionUsuario";
	 * Previamente comprueba que haya en sesion un atributo con "idEntidad" o "idEntidadAlmacenada" y si existe lo usará
	 * @param req
	 * @return
	 * @throws SigemException
	 */
	protected String getIdEntidad(HttpServletRequest req)  {
		String result="";
				
		//buscamos la entidad si existe en la session
		result= getIdEntidadFromContext(req);
		
		
		if (StringUtils.isEmpty(result)){
			ServicioAdministracionSesionesBackOffice oServicio;
			try {
				oServicio = LocalizadorServicios.getServicioAdministracionSesionesBackOffice();
			} catch (SigemException e) {
				logger.fatal("Imposible cargar LocalizadorServicios.getServicioAdministracionSesionesBackOffice() -",e);
				throw new RuntimeException("Imposible cargar LocalizadorServicios.getServicioAdministracionSesionesBackOffice()",e);
			}
			
			String key = req.getParameter(ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO);
	    	if(StringUtils.isEmpty(key)) {
	    		key = (String) (req.getSession().getAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO));
	    	}
	    	
	    	if(StringUtils.isEmpty(key)) {
	    		result="";
	    	} else  {
	    		Sesion sesion = oServicio.obtenerSesion(key);
	    		result= sesion.getIdEntidad();
	    		req.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD_ALMACENADA, result);
	    	}
		}
			
		return result;
	}
	
	protected String getIdEntidadFromContext(HttpServletRequest req){
		
		String result="";
		//TODO sacar a constantes;
		String idEntidad = (String) req.getParameter("idEntidad");
		if(StringUtils.isEmpty(idEntidad)) idEntidad = (String) req.getSession().getAttribute("idEntidad");
		String idEntidadAlmacenada=(String) req.getParameter("idEntidadAlmacenada");
		if(StringUtils.isEmpty(idEntidadAlmacenada)) idEntidadAlmacenada=(String) req.getSession().getAttribute("idEntidadAlmacenada");
		
		if (!StringUtils.isEmpty(idEntidadAlmacenada)){
			result=idEntidadAlmacenada;
		}else{
			result= idEntidad;
		}
		
		return result;
		
	}



	/**
	 * Obtiene el skin por defecto
	 * @return
	 */
	public String getDefaultSkin() {
		return defaultSkin;
	}

}
