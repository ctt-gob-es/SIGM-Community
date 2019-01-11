package ieci.tecdoc.mvc.control;

import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.gestion_administracion.ConstantesGestionUsuariosAdministracion;
import ieci.tecdoc.sgm.core.services.gestion_administracion.ServicioGestionUsuariosAdministracion;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.RequestProcessor;

public class ControlSesionRequestProcesor extends RequestProcessor{
	
	private static Logger logger = Logger.getLogger(AdmRequestProcessor.class);

    protected boolean processPreprocess(HttpServletRequest request,
            HttpServletResponse response)  {
    	
        boolean continueProcessing = true; // assume success
        String contextPath = request.getContextPath();

        
		try {
			if (!autenticarEntidad(request, ConstantesGestionUsuariosAdministracion.APLICACION_ESTRUCTURA_ORGANIZATIVA)) {
				String Url = obtenerUrlLogin(request, ConstantesGestionUsuariosAdministracion.APLICACION_ESTRUCTURA_ORGANIZATIVA);
				request.setAttribute("urlRedireccion", Url);
				response.sendRedirect(Url);
				continueProcessing = false;
			}
		} catch (IOException e) {
            logger.error("Fallo en redirect");
            try {
                response.sendRedirect(contextPath + "/acs/redireccion.jsp");
            } catch (IOException e1) {
            }
            continueProcessing = false;
		}
    	
    	return continueProcessing;
    }
    
    private static boolean autenticarEntidad(HttpServletRequest request, String idAplicacion) {
		
    	String key_entidad = request.getParameter(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
    	if(isNuloOVacio(key_entidad)) {
    		key_entidad = (String) request.getSession().getAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
    	}
    	
    	if(isNuloOVacio(key_entidad)) {
    		return false;
    	} else {
   			request.getSession().setAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD, key_entidad);
   			return true;
    	} 
	}
    
    // esta parte estaba en sigem_utilidadesAdministracion, pero es lo unico que se usa de ahi. No se si merece la pena cargar un modulo para esto
    private static String obtenerUrlLogin(HttpServletRequest request, String aplicacion){
      ServicioGestionUsuariosAdministracion oServicio;
      StringBuffer sbUrl;
      try {
        oServicio = LocalizadorServicios.getServicioAutenticacionUsuariosAdministracion();
        
        sbUrl = new StringBuffer(oServicio.obtenerDireccionLogado()).append("?");
        sbUrl.append(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_APLICACION).append("=").append(aplicacion);
        
        String entidadId = (String)request.getParameter(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD);
        if((entidadId == null) || ("".equals(entidadId)) || ("null".equals(entidadId))){
          entidadId = (String)request.getAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD);
        }
        if((entidadId == null) || ("".equals(entidadId)) || ("null".equals(entidadId))){
          sbUrl.append('&').append(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD).append("=").append(entidadId);
        }
      } catch (SigemException e) {
        logger.error("No se ha podido obtener la dirección de logado", e);
        return null;
      }
      
      return comprobarURL(request, sbUrl.toString());
  }

    private static String comprobarURL(HttpServletRequest request, String url) {
      if (url == null || "".equals(url)) 
        return null;
      
      if (url.indexOf("localhost") != -1) {
        int index = url.indexOf("localhost");
        return url.substring(0, index) + request.getServerName() + url.substring(index + "localhost".length());
      } else {
        return url;
      }
    }
    
    public static boolean isNuloOVacio(Object cadena)
    {
      if((cadena == null) || ("".equals(cadena)) || ("null".equals(cadena))) {
        return true;
      }
      return false;
    }
}
