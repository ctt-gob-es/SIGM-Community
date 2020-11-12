package ieci.tecdoc.sgm.autenticacion.action;

import ieci.tecdoc.sgm.autenticacion.utils.Defs;
import ieci.tecdoc.sgm.core.config.ports.PortsConfig;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.sesion.ServicioSesionUsuario;
import ieci.tecdoc.sgm.core.services.sesion.SesionUsuarioException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.context.ApplicationContext;

//[DipuCR-Agustin] #548 integrar Cl@ve autentificacion
public class ValidacionClaveAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		HttpSession session = request.getSession();
		String nombre = "";
		String apellidos = "";
		String nif="";
		String nivel="";
		String primerApellido = "";
		String partialAfirma = "";
		String selectedIdP = "";
		String relayState = "";
		
		String entidadId = (String) request.getSession().getAttribute(Defs.ENTIDAD_ID);

		String xmlDataSpecific = (String) session.getAttribute(Defs.DATOS_ESPECIFICOS);
		if (!Defs.isNuloOVacio(xmlDataSpecific)) {
			session.setAttribute(Defs.DATOS_ESPECIFICOS, xmlDataSpecific);
		}
		
		//Conexto de SPProxy
		ServletContext srcServletContext = request.getSession().getServletContext();
    	ServletContext targetServletContext = srcServletContext.getContext("/SP2");
		
		try {			
			
			//RECUPERAR CAMPOS QUE DEVUELVE CLAVE 1
//			nombre=(String) targetServletContext.getAttribute(Defs.CLAVE_ATTRLIST_NAME);
//			apellidos=(String) targetServletContext.getAttribute(Defs.CLAVE_ATTRLIST_SURNAME);
//			nif=(String) targetServletContext.getAttribute(Defs.CLAVE_ATTRLIST_EIDENTIFIER);
//			nivel=(String) targetServletContext.getAttribute(Defs.CLAVE_ATTRLIST_NIVEL_CITIZENQAALEVEL);
			
			//RECUPERAR CAMPOS QUE DEVUELVE CLAVE 2
//			nombre=(String) request.getAttribute(Defs.CLAVE_ATTRLIST_NAME);
//			apellidos=(String) request.getAttribute(Defs.CLAVE_ATTRLIST_SURNAME);
//			nif=(String) request.getAttribute(Defs.CLAVE_ATTRLIST_EIDENTIFIER);
//			nivel=(String) request.getAttribute(Defs.CLAVE_ATTRLIST_NIVEL_CITIZENQAALEVEL);
						
//			apellidos=(String)  request.getAttribute("FamilyName").toString().subSequence(request.getAttribute("FamilyName").toString().indexOf("[")+1, request.getAttribute("FamilyName").toString().indexOf("]"));
//			nombre=(String)  request.getAttribute("FirstName").toString().subSequence(request.getAttribute("FirstName").toString().indexOf("[")+1, request.getAttribute("FirstName").toString().indexOf("]"));
//			nif=(String)  request.getAttribute("PersonIdentifier").toString().subSequence(request.getAttribute("PersonIdentifier").toString().indexOf("[")+1, request.getAttribute("PersonIdentifier").toString().indexOf("]"));
//			primerApellido=(String)  request.getAttribute("FirstSurname").toString().subSequence(request.getAttribute("FirstSurname").toString().indexOf("[")+1, request.getAttribute("FirstSurname").toString().indexOf("]"));
//			partialAfirma=(String)  request.getAttribute("PartialAfirma").toString().subSequence(request.getAttribute("PartialAfirma").toString().indexOf("[")+1, request.getAttribute("PartialAfirma").toString().indexOf("]"));
//			selectedIdP=(String)  request.getAttribute("SelectedIdP").toString().subSequence(request.getAttribute("SelectedIdP").toString().indexOf("[")+1, request.getAttribute("SelectedIdP").toString().indexOf("]"));
			
			try {
				apellidos=(String)  targetServletContext.getAttribute("FamilyName").toString().subSequence(targetServletContext.getAttribute("FamilyName").toString().indexOf("[")+1, targetServletContext.getAttribute("FamilyName").toString().indexOf("]"));
			} catch (Exception e) {
			}
			
			try {
				nombre=(String)  targetServletContext.getAttribute("FirstName").toString().subSequence(targetServletContext.getAttribute("FirstName").toString().indexOf("[")+1, targetServletContext.getAttribute("FirstName").toString().indexOf("]"));
			} catch (Exception e) {
			}
			
			try {
				nif=(String)  targetServletContext.getAttribute("PersonIdentifier").toString().subSequence(targetServletContext.getAttribute("PersonIdentifier").toString().indexOf("[")+1, targetServletContext.getAttribute("PersonIdentifier").toString().indexOf("]"));
			} catch (Exception e) {
			}
			
			try {
				primerApellido=(String)  targetServletContext.getAttribute("FirstSurname").toString().subSequence(targetServletContext.getAttribute("FirstSurname").toString().indexOf("[")+1, targetServletContext.getAttribute("FirstSurname").toString().indexOf("]"));
			} catch (Exception e) {
			}
			
			try {
				partialAfirma=(String)  targetServletContext.getAttribute("PartialAfirma").toString().subSequence(targetServletContext.getAttribute("PartialAfirma").toString().indexOf("[")+1, targetServletContext.getAttribute("PartialAfirma").toString().indexOf("]"));
			} catch (Exception e) {
			}
			
			try {
				selectedIdP=(String)  targetServletContext.getAttribute("SelectedIdP").toString().subSequence(targetServletContext.getAttribute("SelectedIdP").toString().indexOf("[")+1, targetServletContext.getAttribute("SelectedIdP").toString().indexOf("]"));
			} catch (Exception e) {
			}
			
			if (nombre == null || apellidos == null ||nif == null ||partialAfirma == null){
				
				request.setAttribute(Defs.MENSAJE_ERROR,Defs.MENSAJE_ERROR_SELECCIONAR_CERTIFICADO);
				return mapping.findForward("failure");

			}
			
//      ACCESO A VARIABLES DE SESION
//		Object campos_clave= request.getAttribute(Defs.CLAVE_ATRIBUTOS);		
//		request.getParameter("parm1");
//		request.getAttribute("parm1");
//		request.getSession().getAttribute("parm1");
			
		//Atributos request
//		System.out.println("Atributos request");
//		Enumeration requestAttributes = request.getAttributeNames();
//		while(requestAttributes.hasMoreElements()){
//			Object aux=requestAttributes.nextElement();
//			System.out.println(aux.toString()+"="+request.getAttribute(aux.toString()).toString());
//		}
//		
//		 ServletContext srcServletContext = request.getSession().getServletContext();
//		 ServletContext targetServletContext = srcServletContext.getContext("/SPProxy");
//			
//		//Atributos request.session
//		System.out.println("Atributos request.session");
//		Enumeration requestSessionAttributes = request.getSession().getAttributeNames();
//		while(requestSessionAttributes.hasMoreElements()){
//			Object aux1=requestSessionAttributes.nextElement();
//			System.out.println(aux1.toString()+"="+request.getSession().getAttribute(aux1.toString()).toString());
//		}
			
	    
			
		} catch (Exception e) {
			request.setAttribute(Defs.MENSAJE_ERROR,
					Defs.MENSAJE_ERROR_LISTA_CERTIFICADOS);
			request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.getMessage());
			return mapping.findForward("failure");
		}
		
		//CREAR LA SESION EN SIGEM UNA VEZ RECIBIMOS EL OK DE CLAVE
		String sessionIdIni = null;
		String sessionId = null;
		boolean aceptado = true;
		try {
			
			sessionIdIni = (String) session.getAttribute(Defs.SESION_ID);
			Entidad oEntidad = new Entidad();
			oEntidad.setIdentificador(entidadId);
			ServicioSesionUsuario oServicio = LocalizadorServicios.getServicioSesionUsuario();
			
			if (sessionIdIni != null && !sessionIdIni.equals("")) {
				sessionId = oServicio.login(sessionIdIni, nombre, apellidos, "", nif, oEntidad, "");
			} else {
				sessionId = oServicio.login(null, nombre, apellidos, "", nif, oEntidad, "");
			}
			
			session.setAttribute(Defs.SESION_ID, sessionId);

			String redireccion = (String) session.getAttribute(Defs.REDIRECCION);
			String url = (String) request.getSession().getServletContext().getAttribute("redir" + redireccion);
			String port = PortsConfig.getCertPort();
			session.setAttribute(Defs.URL_REDIRECCION, url);
			session.setAttribute(Defs.URL_PUERTO, port);
			
		} catch (SesionUsuarioException e) {

			// TODO Revisar mensajes
			if (aceptado) {
				if ((e.getErrorCode() == SesionUsuarioException.SECURITY_ERROR_CODE)
						|| (e.getErrorCode() == SesionUsuarioException.INVALID_CREDENTIALS_ERROR_CODE)) {
					if (e.getErrorCode() == SesionUsuarioException.INVALID_CREDENTIALS_ERROR_CODE) {
						request.setAttribute(Defs.MENSAJE_LOGIN,Defs.CERT_NO_VALIDO);
					} else {
						request.setAttribute(Defs.MENSAJE_LOGIN,Defs.CERT_REVOCADO);
					}
				} else {
					request.setAttribute(Defs.MENSAJE_LOGIN, Defs.CERT_REVOCADO);
				}
			} else
				request.setAttribute(Defs.MENSAJE_LOGIN,Defs.METODO_AUTH_NO_ACEPTADA);

			request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.getMessage());
			return mapping.findForward("failure");
		} catch (SigemException e) {

			request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.getMessage());
			return mapping.findForward("failure");
		}

		String mensaje = "";
		if (sessionId != null) {
			if (sessionId.equals(sessionIdIni))
				mensaje = Defs.CLAVE_YA_VALIDADO;
			else
				mensaje = Defs.CLAVE_PRIMER_ACCESO;
		} else {
			mensaje = Defs.CLAVE_NO_VALIDO;
			request.setAttribute(Defs.MENSAJE_LOGIN, mensaje);
			return mapping.findForward("failure");
		}

		request.setAttribute(Defs.MENSAJE_LOGIN, mensaje);
		return mapping.findForward("success");
	}

}
