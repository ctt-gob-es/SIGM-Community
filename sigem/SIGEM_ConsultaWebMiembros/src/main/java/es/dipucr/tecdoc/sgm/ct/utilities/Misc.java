package es.dipucr.tecdoc.sgm.ct.utilities;
/*
 *  $Id: Misc.java,v 1.3.2.4 2008/07/09 08:43:04 jnogales Exp $
 */
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.FicheroHito;
import ieci.tecdoc.sgm.core.services.consulta.FicherosHito;
import ieci.tecdoc.sgm.core.services.consulta.Notificacion;
import ieci.tecdoc.sgm.core.services.consulta.Notificaciones;
import ieci.tecdoc.sgm.core.services.consulta.Pago;
import ieci.tecdoc.sgm.core.services.consulta.Pagos;
import ieci.tecdoc.sgm.core.services.consulta.Subsanacion;
import ieci.tecdoc.sgm.core.services.consulta.Subsanaciones;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.sesion.InfoUsuario;
import ieci.tecdoc.sgm.core.services.sesion.ServicioSesionUsuario;
import ieci.tecdoc.sgm.core.user.web.SesionUserHelper;
import ieci.tecdoc.sgm.ct.exception.ConsultaCodigosError;
import ieci.tecdoc.sgm.ct.exception.ConsultaExcepcion;

import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Misc {
	
	public static X509Certificate getCertificadoCliente(HttpServletRequest request)
	throws ConsultaExcepcion {

		X509Certificate cert = null;
	
		Object obj =
		request.getAttribute("javax.servlet.request.X509Certificate");
	
		if (obj instanceof X509Certificate[]) {
			X509Certificate[] certArr = (X509Certificate[])obj;
			cert = certArr[0];
		} else if(obj instanceof X509Certificate) {
			cert = (X509Certificate)obj;
		}
			
		if (cert == null) {	
			 throw new ConsultaExcepcion(ConsultaCodigosError.EC_CERTIFICADO_NO_ENCONTRADO);
		}
		
		return cert;

	}
	
	public static String getNIFDesdeCertificado(HttpServletRequest request)
	throws ConsultaExcepcion {
		HttpSession sesion = request.getSession();
		String NIF = (String)sesion.getAttribute("NIF");
		if ( NIF != null){
			return NIF;
		}
		X509Certificate cert = Misc.getCertificadoCliente(request);
		/*X500Principal principal = cert.getSubjectX500Principal();
		String nombre = principal.getName();
		NIF = nombre.substring(nombre.lastIndexOf("NIF ") + 4, nombre.length());*/
		int pos, pos1;
		String subjectCN = cert.getSubjectDN().getName();

		pos = subjectCN.indexOf("- NIF");
		pos1 = subjectCN.indexOf(", OU=");
		if (pos1 == -1)
			pos1 = subjectCN.length();
		//titular.m_nombre = subjectCN.substring(10, pos - 1);
		NIF = subjectCN.substring(pos + 6, pos1);
		sesion.setAttribute("NIF", NIF);
		return NIF;
	}
	
	public static String getNombreDesdeCertificado(HttpServletRequest request)
	throws ConsultaExcepcion {
		HttpSession sesion = request.getSession();
		String nombreUsuario = (String)sesion.getAttribute("nombreUsuario");
		if ( nombreUsuario != null ){
			return nombreUsuario;
		}
		X509Certificate cert = Misc.getCertificadoCliente(request);
		int pos, pos1;
		String subjectCN = cert.getSubjectDN().getName();

		pos = subjectCN.indexOf("- NIF");
		pos1 = subjectCN.indexOf(", OU=");
		if (pos1 == -1)
			pos1 = subjectCN.length();
		nombreUsuario = subjectCN.substring(10, pos - 1);
		//NIF = subjectCN.substring(pos + 6, pos1);
		
		sesion.setAttribute("nombreUsuario", nombreUsuario);
		return nombreUsuario;
	}
	
	
	public static String getNIFUsuario(HttpServletRequest request, String  sessionID){
		
		HttpSession session= request.getSession();
		String NIF = null;
		String nombre = null;
		 if(NIF == null || NIF.equals("")){
			 try {
				 ServicioSesionUsuario oServicio = LocalizadorServicios.getServicioSesionUsuario();
				 InfoUsuario solicitante = oServicio.getInfoUsuario(sessionID, Misc.obtenerEntidad(request));
//				 Solicitante solicitante = SesionManager.getSender(sessionID);
				 NIF = solicitante.getId();
				 nombre = solicitante.getName();
				 session.setAttribute("NIF", NIF);
				 session.setAttribute("nombreUsuario", nombre);
				 session.setAttribute("SESION_ID", sessionID);
			 } catch(Exception ex){
				 request.setAttribute("MENSAJE_ERROR",ex.getMessage());
			 }
		 }
		
		 return NIF;
	}

	public static boolean isEmpty(String cadena){
		return (cadena == null || cadena.equals("") || cadena.equals("null"));
	}
	
	public static Entidad obtenerEntidad(HttpServletRequest request){
		Entidad oEntidad = new Entidad();
		oEntidad.setIdentificador(SesionUserHelper.obtenerIdentificadorEntidad(request));
		return oEntidad;
	}
	
	public static String obtenerMensaje(HttpServletRequest request, String cadena) {
		Locale locale = (Locale)request.getSession().getAttribute("org.apache.struts.action.LOCALE");
		String idioma = "es";
		if (locale != null)
			idioma = locale.getLanguage();
		try {
			int index = cadena.indexOf("<label locale=\"" + idioma + "\">");
			if (index != -1) {
				int index1 = cadena.indexOf("</label>", index);
				if (index1 != -1)
					return cadena.substring(index+("<label locale=\"" + idioma + "\">").length(), index1);
			}
			index = cadena.indexOf("<label locale=\"es\">");
			if (index != -1) {
				int index1 = cadena.indexOf("</label>", index);
				if (index1 != -1)
					return cadena.substring(index+("<label locale=\"es\">").length(), index1);
			}
			return cadena;
		} catch (Exception e) {
			return cadena;
		}
	}
	
	public static void modificarMensajesSubsanaciones(HttpServletRequest request, Subsanaciones subsanaciones) {
		if (subsanaciones != null && subsanaciones.count() > 0) {
			for(int i=0; i<subsanaciones.count(); i++) {
				((Subsanacion)subsanaciones.get(i)).setMensajeParaElCiudadano(obtenerMensaje(request, ((Subsanacion)subsanaciones.get(i)).getMensajeParaElCiudadano()));
			}
		}
	}
	
	public static void modificarMensajesPagos(HttpServletRequest request, Pagos pagos) {
		if (pagos != null && pagos.count() > 0) {
			for(int i=0; i<pagos.count(); i++) {
				((Pago)pagos.get(i)).setMensajeParaElCiudadano(obtenerMensaje(request, ((Pago)pagos.get(i)).getMensajeParaElCiudadano()));
			}
		}
	}
	
	public static void modificarMensajesNotificaciones(HttpServletRequest request, Notificaciones notificaciones) {
		if (notificaciones != null && notificaciones.count() > 0) {
			for(int i=0; i<notificaciones.count(); i++) {
				((Notificacion)notificaciones.get(i)).setDescripcion(obtenerMensaje(request, ((Notificacion)notificaciones.get(i)).getDescripcion()));
			}
		}
	}
	
	public static FicherosHito modificarMensajesFicherosHito(HttpServletRequest request, FicherosHito ficherosHito) {
		if (ficherosHito != null && ficherosHito.count() > 0) {
			for(int i=0; i<ficherosHito.count(); i++) {
				((FicheroHito)ficherosHito.get(i)).setTitulo(obtenerMensaje(request, ((FicheroHito)ficherosHito.get(i)).getTitulo()));
			}
		}else new FicherosHito();
		return ficherosHito;
	}
	
	public static final String IDIOMAS_DISPONIBLES = "IdiomasDisponibles";
	public static final String LANG = "LANG";
	public static final String COUNTRY = "COUNTRY";
}
