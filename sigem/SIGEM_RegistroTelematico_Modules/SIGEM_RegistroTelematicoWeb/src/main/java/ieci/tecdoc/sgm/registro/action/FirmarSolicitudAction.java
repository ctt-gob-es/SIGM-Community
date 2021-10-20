package ieci.tecdoc.sgm.registro.action;

import ieci.tecdoc.sgm.base.base64.Base64Util;
import ieci.tecdoc.sgm.base.miscelanea.Goodies;
import ieci.tecdoc.sgm.base.xml.core.XmlDocument;
import ieci.tecdoc.sgm.base.xml.core.XmlElement;
import ieci.tecdoc.sgm.base.xml.core.XmlTransformer;
import ieci.tecdoc.sgm.base.xml.lite.XmlTextBuilder;
import ieci.tecdoc.sgm.core.config.impl.spring.MultiEntityContextHolder;
import ieci.tecdoc.sgm.core.config.ports.PortsConfig;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.cripto.firma.ServicioFirmaDigital;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ResultadoValidacion;
import ieci.tecdoc.sgm.core.services.cripto.validacion.ServicioCriptoValidacion;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.core.web.locale.LocaleFilterHelper;
import ieci.tecdoc.sgm.registro.form.FormularioSolicitudForm;
import ieci.tecdoc.sgm.registro.util.Definiciones;
import ieci.tecdoc.sgm.registro.utils.Defs;
import ieci.tecdoc.sgm.registro.utils.Misc;
import es.gob.fire.client.FireClient;
import es.gob.fire.client.SignOperationResult;
import ieci.tecdoc.sgm.registro.utils.Base64;







import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;

import java.security.cert.X509Certificate;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.gob.afirma.transformers.TransformersFacade;

public class FirmarSolicitudAction extends RegistroWebAction {

    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {

    	HttpSession session = request.getSession();
    	FirmaConfiguration fc = null;
    	String xmlDataSpecific="";
    	Entidad entidad = null;

	    try {
	    	String tramiteId = (String)session.getAttribute(Defs.TRAMITE_ID);
	    	String oficina = (String)session.getAttribute(Defs.OFICINA);
	    	String sessionId = (String)session.getAttribute(Defs.SESION_ID);

	    	String refresco = (String)request.getParameter(Defs.REFRESCO);
	    	String firma_solicitud = (String)session.getAttribute(Defs.FIRMAR_SOLICITUD);
	    	boolean bRefresco = true;
	    	if (refresco !=null && !"".equals(refresco)) {
	    		bRefresco = new Boolean(refresco).booleanValue();
	    	}
	    	
	    	int ifirma_solicitud = 0;
	    	if (firma_solicitud !=null && !"".equals(firma_solicitud)) {
	    		ifirma_solicitud = Integer.valueOf(firma_solicitud);
	    	}

	    	if (bRefresco) {
	    		
	    		//INICIO [DipuCR-Agustin #548 Integrar Clave]
	    		
	    		entidad = Misc.obtenerEntidad(request);
				String entityId = entidad.getIdentificador();
	    		fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
	    			    		
				xmlDataSpecific = (String)session.getAttribute(Defs.DATOS_ESPECIFICOS);	
				
				
				//Formar url de registro de la solicitud si va bien el proceso de firma
				String path = request.getContextPath();
				String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";				
				String serverPort = String.valueOf(request.getServerPort());
	    		
				//[eCenpri-Agustin #1356] Si la solicitud lleva firma configuro llamada a clave firma
	    		if(ifirma_solicitud==1) {  
	    			
	    		
				// Cojo la configuracion de server Port, mirar esta parte si hay algun apache proxy delante
				/**
				String proxyHttpPort = PortsConfig.getHttpFrontendPort();
				String proxyHttpsNoCertPort = PortsConfig.getHttpsFrontendPort();
				String proxyHttpsSiCertPort = PortsConfig.getHttpsFrontendAuthclientPort();
				
				
				if ((proxyHttpPort != null && proxyHttpPort.equals(serverPort)) ||
					(proxyHttpsNoCertPort != null && proxyHttpsNoCertPort.equals(serverPort)) ||
					(proxyHttpsSiCertPort != null && proxyHttpsSiCertPort.equals(serverPort))) {
					
					// Servidor Frontend por delante del Servidor de Aplicaciones (Ej: APACHE + TOMCAT)
					serverPort = proxyHttpsSiCertPort;
				}
				else {
					serverPort = PortsConfig.getCertPort();
				}
				
				*/
				
				String urlSiVaBienClaveFirma = "https://"+request.getServerName()+":" + serverPort + request.getContextPath() + "/registrarSolicitud.do";
				
	    	   	
    		   	final Properties confProperties = new Properties();
    	        // Configuramos la URL de nuestra aplicacion a la que redirigir en caso de exito en la firma (Obligatorio)
    	        confProperties.setProperty("redirectOkUrl",urlSiVaBienClaveFirma); //$NON-NLS-1$
    	        // Configuramos la URL de nuestra aplicacion a la que redirigir en caso de error en la firma (Obligatorio)
    	        confProperties.setProperty("redirectErrorUrl", fc.getProperty("fire.registro.redirectErrorUrl")); //$NON-NLS-1$
    	        // Configuramos el nombre del procedimiento de cara a la GISS (Oblisgatorio)
    	        confProperties.setProperty("procedureName", fc.getProperty("fire.registro.procedureName")); //$NON-NLS-1$
    	        // Configuramos si el certificado es local o de Cl@ve Firma (Opcional)
    	        confProperties.setProperty("certOrigin", fc.getProperty("fire.registro.certOrigin")); //$NON-NLS-1$
    	        // Configuramos el nombre de la aplicacion (opcional)
    	       	confProperties.setProperty("appName", fc.getProperty("fire.registro.appName")); //$NON-NLS-1$
	    	       	
    	       	FormularioSolicitudForm formularioSolicitud = new FormularioSolicitudForm();
    	       	procesaDatosEspecificos(formularioSolicitud, xmlDataSpecific);
    	       	
    	       	//Para probar con Simulador de Clave, descomentar al pasar a produccion
    	       	//formularioSolicitud.setDocumentoIdentidad("00001");
	    		   	
    		   	FireClient fireClient = new FireClient(fc.getProperty("fireClave"),fc);
    		   	SignOperationResult signResult= fireClient.sign(formularioSolicitud.getDocumentoIdentidad(), 
	    								   			fc.getProperty("fire.registro.critoperation"), 
	    								   			fc.getProperty("fire.registro.format"),
	    								   			fc.getProperty("fire.registro.algorithm"), 
	    								   			fc.getProperty("fire.registro.extraparams"), 
	    								   			Base64.encode(((String)(session.getAttribute(Defs.DATOS_A_FIRMAR))).getBytes(),true), 
	    								   			confProperties);		
	    		   	
    		   	
    		   	session.setAttribute(Defs.CLAVE_FIRMA_URL, signResult.getRedirectUrl());
    		   	session.setAttribute(Defs.CLAVE_FIRMA_TRANSACTION_ID, signResult. getTransactionId());
    		   	session.setAttribute(Defs.CLAVE_FIRMA_SUBJECT_ID,formularioSolicitud.getDocumentoIdentidad());
    		   	
    		   	
    			//FIN [DipuCR-Agustin #548]
    		   	
    		   	return mapping.findForward("registrarSolicitud");
	    		
	    		}
    		
	    	}
	    	}catch(Exception e) {

		    	session.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
		    	session.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.toString());

		    	return mapping.findForward("failure");
	   		}

	        //Si la solicitud no lleva firma salta a directamente a registrarSolicitud.do
	    	return mapping.findForward("registrarSolicitud_sinfirma");
	    	
	   	}
   	
    
    
	//[DipuCR-Agustin #538 Integrar Clave]	
	private FormularioSolicitudForm procesaDatosEspecificos(FormularioSolicitudForm formularioSolicitud, String datosEspecificos){
		
		formularioSolicitud.setDatosEspecificos(datosEspecificos);
		formularioSolicitud.procesaDatosEspecificos();		
		
		return formularioSolicitud;
		
	}
	

}