package ieci.tecdoc.sgm.registro.action;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.sun.xml.messaging.saaj.util.Base64;

import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.gob.fire.client.FireClient;
import es.gob.fire.client.TransactionResult;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.autenticacion.util.TipoAutenticacionCodigos;
import ieci.tecdoc.sgm.base.base64.Base64Util;
import ieci.tecdoc.sgm.base.miscelanea.Goodies;
import ieci.tecdoc.sgm.base.xml.core.XmlDocument;
import ieci.tecdoc.sgm.base.xml.core.XmlElement;
import ieci.tecdoc.sgm.base.xml.core.XmlTransformer;
import ieci.tecdoc.sgm.base.xml.lite.XmlTextBuilder;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.telematico.ServicioRegistroTelematico;
import ieci.tecdoc.sgm.core.web.locale.LocaleFilterHelper;
import ieci.tecdoc.sgm.registro.util.Definiciones;
import ieci.tecdoc.sgm.registro.utils.Defs;
import ieci.tecdoc.sgm.registro.utils.Misc;
import net.sf.jasperreports.engine.JasperCompileManager;

public class RegistrarSolicitudAction extends RegistroWebAction {
	
	private static final Logger logger = Logger.getLogger(RegistrarSolicitudAction.class);

    public ActionForward executeAction(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {    	
    	
    	HttpSession session = request.getSession();
    	FirmaConfiguration fc = null;
    	String xmlDataSpecific="";
    	Entidad entidad = null;
    	TransactionResult resultadoFirmaBase64 = null;
    	    	
    	String transactionId="";
    	String subjectID="";
    	String resultadoFirma="";
    	String xml_request =null;
    	String xml_request_transformado =null;
    	

	    try {
	    	String tramiteId = (String)session.getAttribute(Defs.TRAMITE_ID);
	    	String oficina = (String)session.getAttribute(Defs.OFICINA);
	    	String sessionId = (String)session.getAttribute(Defs.SESION_ID); 		    	

	    	String refresco = (String)request.getParameter(Defs.REFRESCO);
	    	boolean bRefresco = true;	    	
	    	
	    		    	
	    	if (refresco !=null && !"".equals(refresco)) {
	    		bRefresco = new Boolean(refresco).booleanValue();
	    	}
	    		    	
	    		
	    	//INICIO [DipuCR-Agustin #548 Integrar Clave]///////////////////////////////////////////////////////////////////////////////////////////////////////////
	    		
	    	entidad = Misc.obtenerEntidad(request);
			String entityId = entidad.getIdentificador();
	    	fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
	    	
	    	transactionId = (String)session.getAttribute(Defs.CLAVE_FIRMA_TRANSACTION_ID);
	    	subjectID = (String)session.getAttribute(Defs.CLAVE_FIRMA_SUBJECT_ID);
	    	
	    	if(null!=transactionId && null!=subjectID)
	    	{
	    		
		    	FireClient fireClient = new FireClient(fc.getProperty("fireClave"),fc);
		    	resultadoFirmaBase64 = fireClient.recoverSignResult(transactionId, subjectID, fc.getProperty("fire.registro.upgrade"));
		    	
		    	//resultadoFirma= Base64.encode(resultadoFirmaBase64.getResult());
		    	resultadoFirma = Goodies.fromUTF8ToStr(resultadoFirmaBase64.getResult());
	    		
	    	}
	    		
	    	//FIN [DipuCR-Agustin #548 Integrar Clave]///////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	
	    	
	    	if (bRefresco) {
	    			    		   		
			   	//Se borra el fichero xml de solicitud que puede consultar el usuario.
			   	//No se si se debería borrar aquí o una vez finalizado todo el proceso de registro
		    	String tmpXmlPath = (String)session.getServletContext().getAttribute(Defs.PLUGIN_TMP_PATH_XML);
		    	String separador = System.getProperty("file.separator");
			   	String ruta_xml = session.getServletContext().getRealPath("") + separador + tmpXmlPath + separador + sessionId + "_" + tramiteId + ".xml";
			   	File borrar_xml = new File(ruta_xml);
			   	borrar_xml.delete();

		        // Comprobar si la solicitud se ha firmado (configuración establecida para el trámite)
			   	// para entonces validar el certificado con el que se ha firmado
			   	boolean bFirma = true;
			   	String firmar_solicitud = (String)session.getAttribute(Defs.FIRMAR_SOLICITUD);
			   	if (firmar_solicitud == null || firmar_solicitud.equals("")) {
			   		firmar_solicitud = "1";
			   	}
			   	if (firmar_solicitud.equals("0")) {
			   		bFirma = false;
			   	}
			   	
			   	//Se crea el XML firmado por el usuario
			   	String firma = resultadoFirma;
			   	xml_request = (String)session.getAttribute(Defs.REQUEST);
			   	byte[] registryReq = Base64Util.decode(xml_request);
			   	XmlDocument xmlDoc = new XmlDocument();
			   	XmlElement elem, elem2, root;
			   	xmlDoc.createFromStringText(Goodies.fromUTF8ToStr(registryReq));
		        root = xmlDoc.getRootElement();
		        elem = root.getChildElement(Definiciones.SIGNATURE);
		        // La firma no se inserta utilizando el value del XmlElement ya que
		        // los saltos de línea aparecerían como &#13; en el XML de la solicitud de registro firmada
		        // Espacio en blanco en el nodo <Firma/> para que se abra y se cierre <Firma> </Firma>
		        // al convertirlo a cadena
		        elem.setValue(" ");		        
		                
		        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		        domFactory.setIgnoringComments(true);
		        Document doc = xmlDoc.getDomDocument(); 

		       	NodeList nodes = doc.getElementsByTagName(Definiciones.REQUEST_HEADER);
		        	
		        Text a = doc.createTextNode(" "); 
		        Element p = doc.createElement(Definiciones.ID_TRANSACCION); 
		        p.appendChild(a);
		        	
		        XmlTextBuilder x = new XmlTextBuilder();
		        x.addSimpleElement(Definiciones.ID_TRANSACCION, " ");
		        nodes.item(0).appendChild(p);		        	
		       
				//[Manu Ticket #1090] - FIN Poner en marcha la opción Consulta de Expedientes.
		        
		        
		        //[eCenpri-Agustin #1356] firmar solo con la identificacion
		        xml_request_transformado = new String(xmlDoc.getStringText(false));
		        
		        if(session.getAttribute(Defs.ACCESO_SEL).toString().equals(String.valueOf(TipoAutenticacionCodigos.CLAVE)))
		        {
		        	
		        	//Guardar evidencia de Clave como id Transaccion, Agustin #1356
		        	ServletContext srcServletContext = request.getSession().getServletContext();
			        ServletContext targetServletContext = srcServletContext.getContext("/SP2");
			        String partialAfirma = null;
			        	
				        try {
								partialAfirma=(String)targetServletContext.getAttribute("PartialAfirma").toString().subSequence(targetServletContext.getAttribute("PartialAfirma").toString().indexOf("[")+1, targetServletContext.getAttribute("PartialAfirma").toString().indexOf("]"));
						} catch (Exception e) {
					}
			        			        
				    if(partialAfirma!=null) {
				    	
				    	//Agustin #1356 si hay una firma comprobar que tiene el mismo nif con que hubo en la identificacion de Cl@ve
				    				
				    	if(bFirma)
				    	{
				    	
					        String serialNumberFirma = dameNumeroDeSerieDelX509Certificate(resultadoFirma);
					        String serialNumberClave = dameNumeroDeSerieDelClaveCertificate(partialAfirma);
					        
					        if(!serialNumberFirma.equals(serialNumberClave))
					        {
					        	//SI ES UNA FIRMA CON CLAVE FIRMA ES SEGURO QUE SÓLO DEJA FIRMAR CON UN CERTIFICADO ASOCIADO A SU NIF
					        	String subjectDelCert = dameSubjectDelX509Certificate(resultadoFirma);
					        	//SI EL CERTIFICADO FIRMANTE NO TIENE EN EL SUBJECT EL IDENTIFICADOR DE LA FIRMA CENTRALIZADA, ES QUE HA FIRMADO CON AUTOFIRMA Y HA COGIDO OTRO CERTIFICADO, LE MUESTRO UN ERROR				        	
					        	if(subjectDelCert.indexOf("(FIRMA CENTRALIZADA)")==-1)
					        	{
					        		logger.error("ERROR AL REGISTRAR, EL NÚMERO DE SERIE DE LOS CERTIFICADOS NO COINCIDE, DEJO QUE REGISTREN");
					        		logger.error("serialNumberFirma:" + serialNumberFirma);
					        		logger.error("serialNumberClave:" + serialNumberClave);
						        	//request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
								   	//request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: no se ha utilizado un número de certificado válido para firmar");
									//return mapping.findForward("error_login");
					        	}
								
					        }
				        
				    	}
				    	
				    	String idTransaction=partialAfirma;		
				    	int indexBeginIdTransaction = xml_request_transformado.indexOf("<"+Definiciones.ID_TRANSACCION+">");
				    	int indexEndIdTransaction = xml_request_transformado.indexOf("</"+Definiciones.ID_TRANSACCION+">");
				    	
					    //Guardar evidencia de Clave como id Transaccion = partialAfirma
					        
					    xml_request_transformado = xml_request_transformado.substring(0, indexBeginIdTransaction + Definiciones.ID_TRANSACCION.length() + 2)
					        			+ idTransaction
					        			+ xml_request_transformado.substring(indexEndIdTransaction, xml_request_transformado.length());
					    
					    //FIN Guardar evidencia de Clave como id Transaccion, , Agustin #1356	

				        // Incrustar la firma en la solicitud de registro en el nodo <Firma> correspondiente
				        // que será el último nodo <Firma> de la <Solicitud_Registro>
				        int indexBeginSignature = xml_request_transformado.lastIndexOf("<"+Definiciones.SIGNATURE+">");
				        int indexEndSignature = xml_request_transformado.lastIndexOf("</"+Definiciones.SIGNATURE+">");

				        xml_request_transformado = xml_request_transformado.substring(0, indexBeginSignature + Definiciones.SIGNATURE.length() + 2)
				        			+ Base64Util.encodeString(resultadoFirma).toString()
				        			+ xml_request_transformado.substring(indexEndSignature, xml_request_transformado.length());				        
				      
				  		    		 
				    }
				    else {
				    	logger.error("ERROR PARTIALAFIRMA ES NULLL, LE DEJO CONTINUAR");		        		
				    	//request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
					   	//request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: se ha caducado la sesión no existen datos de identificación en Cl@ve");
						//return mapping.findForward("error_login");			    		
				    }
			      
		        }
		       

		        String datosEspecificos = (String)session.getAttribute(Defs.DATOS_ESPECIFICOS);
		        if (datosEspecificos == null) {
		        	datosEspecificos = new String("");
		        }

		        Locale idioma = LocaleFilterHelper.getCurrentLocale(request);
			   	if (idioma == null || idioma.getLanguage() == null) {
			   		idioma = request.getLocale();
			   	}

		        // Plantilla de justificante de registro
			   	String plantillaPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "plantilla_", "jasper");
		   		if (plantillaPath == null) {
		   			String plantillaJRXMLPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "plantilla_", "jrxml");
		   			if(StringUtils.isNotEmpty(plantillaJRXMLPath)){
		   				JasperCompileManager.compileReportToFile(plantillaJRXMLPath);
		   			}
			   	}
		   		
		   		plantillaPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "plantilla_", "jasper");
		   		if (plantillaPath == null) {

		   			request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
			    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: No se ha encontrado la plantilla del justificante de registro para este trámite");
			   		return mapping.findForward("failure");
			   	}

		   		String certificado = (String)session.getServletContext().getAttribute(Defs.PLUGIN_CERTIFICADO);
		        byte[] registryRequest = Goodies.fromStrToUTF8(xml_request_transformado);

		        ServicioRegistroTelematico oServicio = LocalizadorServicios.getServicioRegistroTelematico();

		        // Registrar la solicitud de registro
		    	registryRequest = oServicio.registrar(sessionId,registryRequest, datosEspecificos,	idioma.getLanguage(), oficina, plantillaPath, certificado, Misc.obtenerEntidad(request));

			   	// Obtener la ruta de la xsl de información de registro
			   	String xslPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "informacion_registro_", "xsl");
			   	if (xslPath == null) {

			   		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_ENVIO_SOLICITUD);
			    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: No se ha encontrado el formulario para este trámite");
			   		return mapping.findForward("failure");
			   	}

			   	String xml_registryRequest = Goodies.fromUTF8ToStr(registryRequest);

			   	// Presentar la información del registro realizado (XSL + XML)
			   	XmlDocument xmlDocReg = new XmlDocument();
			   	xmlDocReg.createFromStringText(xml_registryRequest);
			   	String xml_w_xsl = XmlTransformer.transformXmlDocumentToHtmlStringTextUsingXslFile(xmlDocReg, xslPath);
			   	session.setAttribute(Defs.INFORMACION_REGISTRO, xml_w_xsl);
			   	session.setAttribute(Defs.JUSTIFICANTE_REGISTRO, xml_registryRequest);
	    	}
	    	else {	
				
	    		
	    		Locale idioma = LocaleFilterHelper.getCurrentLocale(request);
			   	if (idioma == null || idioma.getLanguage() == null) {
			   		idioma = request.getLocale();
			   	}

			   	// Obtener la ruta de la xsl de información de registro
			   	String xslPath = getResourceTramitePath(session, entidad.getIdentificador(), tramiteId, idioma, "informacion_registro_", "xsl");
			   	if (xslPath == null) {

			   		request.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_ENVIO_SOLICITUD);
			    	request.setAttribute(Defs.MENSAJE_ERROR_DETALLE, "Error: No se ha encontrado el formulario para este trámite");
			   		return mapping.findForward("failure");
			   	}

			   	String justificante = (String)session.getAttribute(Defs.JUSTIFICANTE_REGISTRO);
			   	XmlDocument xmlDocReg = new XmlDocument();
			   	xmlDocReg.createFromStringText(justificante);
			   	String xml_w_xsl = XmlTransformer.transformXmlDocumentToHtmlStringTextUsingXslFile(xmlDocReg, xslPath);
			   	session.setAttribute(Defs.INFORMACION_REGISTRO, xml_w_xsl);

	    		return mapping.findForward("refrescar");
	    	}
	    }
	    catch(Exception e) {

	    	session.setAttribute(Defs.MENSAJE_ERROR, Defs.MENSAJE_ERROR_REGISTRAR_SOLICITUD);
	    	session.setAttribute(Defs.MENSAJE_ERROR_DETALLE, e.toString());

	    	return mapping.findForward("failure");
   		}

	    return mapping.findForward("getJustificante");
   	}
    
    //Agustin #1356
    private String dameNumeroDeSerieDelX509Certificate(String resultadoFirma) {
		
		String aux = resultadoFirma;
		String numSerie = null;
		BigInteger bi = null;
		
		try 
		{		
		
			int indexBeginX509 = aux.indexOf(Defs.FIRMA_X509_BEGIN);
			int indexEndX509 = aux.indexOf(Defs.FIRMA_X509_END);
			aux = aux.substring(indexBeginX509, indexEndX509);
			aux = aux.replaceFirst(Defs.FIRMA_X509_BEGIN, "");
			aux = "-----BEGIN CERTIFICATE-----".concat("\n").concat(aux).concat("\n");
			aux = aux.concat("-----END CERTIFICATE-----");
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream stream = new ByteArrayInputStream(aux.getBytes());
			Certificate cert = cf.generateCertificate(stream);
	
			if (cert instanceof X509Certificate) {
			   numSerie =  new String(((X509Certificate) cert).getSerialNumber().toString());
			}
		
		}catch(Exception e) {
			
			return null;
			
		}
		
		return numSerie;		
	}
    
    //Agustin #1356
    private String dameSubjectDelX509Certificate(String resultadoFirma) {
		
		String aux = resultadoFirma;
		String subject = null;
		
		try 
		{		
		
			int indexBeginX509 = aux.indexOf(Defs.FIRMA_X509_BEGIN);
			int indexEndX509 = aux.indexOf(Defs.FIRMA_X509_END);
			aux = aux.substring(indexBeginX509, indexEndX509);
			aux = aux.replaceFirst(Defs.FIRMA_X509_BEGIN, "");
			aux = "-----BEGIN CERTIFICATE-----".concat("\n").concat(aux).concat("\n");
			aux = aux.concat("-----END CERTIFICATE-----");
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			InputStream stream = new ByteArrayInputStream(aux.getBytes());
			Certificate cert = cf.generateCertificate(stream);
	
			if (cert instanceof X509Certificate) {
				subject =  new String(((X509Certificate) cert).getSubjectDN().getName());
			}
		
		}catch(Exception e) {
			
			return null;
			
		}
		
		return subject;		
	}
    
    //Agustin #1356    
    private String dameNumeroDeSerieDelClaveCertificate(String partialAfirma) {
		
		String auxBase64 = partialAfirma;
		String aux = "";
		
		try 
		{
			
			aux = Base64.base64Decode(auxBase64);		
			int indexBeginSignature = aux.lastIndexOf(Defs.CLAVE_EVIDENCIAS_NUMERO_SERIE);
			aux = aux.substring(indexBeginSignature, aux.length());
			aux = aux.replaceFirst(Defs.CLAVE_EVIDENCIAS_NUMERO_SERIE, "");
			aux = aux.replaceFirst(Defs.CLAVE_EVIDENCIAS_VALOR, "");
			aux = aux.split("<", 2)[0];
		
		}catch(Exception e) {			
			
			return null;
			
		}
		
		return aux;		
	}
		
    
    
}