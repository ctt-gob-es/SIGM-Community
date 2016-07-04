package ieci.tecdoc.isicres.rpadmin.struts.util;

import ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.administracion.AdministracionException;
import ieci.tecdoc.sgm.core.services.administracion.ServicioAdministracion;
import ieci.tecdoc.sgm.core.services.administracion.Usuario;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.gestion_administracion.ConstantesGestionUsuariosAdministracion;
import ieci.tecdoc.sgm.sesiones.administrador.ws.client.Sesion;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


public class SesionHelper {

	private static final Logger logger = Logger.getLogger(SesionHelper.class);
	private static ServicioAdministracion oServicio;
	
	static {
		try {
			oServicio = LocalizadorServicios.getServicioAdministracion();
		}
		catch (SigemException e) {
		}
	}
	public static boolean authenticate(HttpServletRequest request) {

    	if(autenticarEntidad(request, ConstantesGestionUsuariosAdministracion.APLICACION_REGISTRO)) {
    		if(obtenerEntidad(request)==null) {
    			Sesion sesion = obtenerDatosEntidad(request);
    			guardarEntidad(request, sesion.getIdEntidad());
    			guardarSesion(request, sesion);
    		}
    		return true;
    	} else {
    		return false;
    	}
	}
	
	public static String getWebAuthURL(HttpServletRequest request){
		return AutenticacionAdministracion.obtenerUrlLogin(request, ConstantesGestionUsuariosAdministracion.APLICACION_REGISTRO);
	}
	
	public static String getWebAuthDesconectURL(HttpServletRequest request){
		return AutenticacionAdministracion.obtenerUrlLogout(request);
	}
	
	public static String getEntidad(HttpServletRequest request){
		Sesion sesion = obtenerDatosEntidad(request);
		return sesion.getIdEntidad();
	}
	
	public static Entidad obtenerEntidad(HttpServletRequest request) {
		Object oEntidad = request.getSession().getAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD);
		if(oEntidad==null) {
			return null;
		}
		Entidad entidad = new Entidad();
		entidad.setIdentificador(oEntidad.toString());
		return entidad;
	}
	
	public static void guardarEntidad(HttpServletRequest request, String idEntidad) {
		request.getSession().setAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD, idEntidad);
		return;
	}
	
	public static void guardarSesion(HttpServletRequest request, Sesion sesion) {
		request.getSession().setAttribute("datosSesion", sesion);
		return;
	}	
	
	public static void guardarCaseSensitive (HttpServletRequest request, String caseSensitive){
		request.getSession().setAttribute("caseSensitive", caseSensitive);
		return;
	}
	
	public static String obtenerCaseSensitive (HttpServletRequest request){
		Object oCaseSensitive = request.getSession().getAttribute("caseSensitive");
		
		if ((oCaseSensitive == null) || !(oCaseSensitive instanceof String)){
			return "CI";
		}
		
		return (String)oCaseSensitive;		
	}
	
	private static Sesion obtenerDatosEntidad(HttpServletRequest request) {
		
		String key =
				request.getParameter(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
		if (AutenticacionAdministracion.isNuloOVacio(key)) {
			key =
					(String) request
							.getSession()
							.getAttribute(
									ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
		}
		
		if (AutenticacionAdministracion.isNuloOVacio(key)) {
			return null;
		}
		else {
			Usuario oUsuario = null;
			Sesion newSesion = null;
			try {
				oUsuario = oServicio.obtenerUsuario(key.split("_")[0]);
			}
			catch (AdministracionException e) {
			}
			
			if (oUsuario != null){
				newSesion = new Sesion();
				String datos =
						(new StringBuilder(String.valueOf(oUsuario.getNombre())))
								.append(" ").append(oUsuario).toString();
				newSesion.setDatosEspecificos(datos);
				newSesion.setIdEntidad(key.split("_")[2]);
				newSesion.setIdSesion(key.split("_")[1]);
				newSesion.setUsuario(key.split("_")[0]);
				newSesion.setTipoUsuario(Sesion.TIPO_USUARIO_ADMINISTRADOR);
			}
			return newSesion;
		}
	}
	private static boolean autenticarEntidad(HttpServletRequest request, String idAplicacion) {
		
    	String key_entidad = request.getParameter(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
    	if(AutenticacionAdministracion.isNuloOVacio(key_entidad)) {
    		key_entidad = (String) request.getSession().getAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
    	}
    	
    	if(AutenticacionAdministracion.isNuloOVacio(key_entidad)) {
    		return false;
    	} else{
   			request.getSession().setAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD, key_entidad);
   			return true;
    	}
	}
}
