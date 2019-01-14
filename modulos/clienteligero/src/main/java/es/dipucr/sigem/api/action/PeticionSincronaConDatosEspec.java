package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axiom.om.OMElement;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import es.dipucr.client.preprocesor.DatosEspecificosPreprocessor;
import es.dipucr.core.datos.DatosEspecificosAdapter;
import es.dipucr.core.datos.DatosEspecificosAdapterBase;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import es.dipucr.verifdatos.services.ClienteLigeroProxy;


public abstract class PeticionSincronaConDatosEspec extends BaseAction {

	private static final Log log = LogFactory.getLog(PeticionSincronaConDatosEspec.class);
	public static final String KEY_PARAM_MAP = "parameterRequestMap";
	public static final String KEY_EXCEL_FILE_ITEM = "excelFileItem";
	private static final int LONG_MAX_NOMBRE_SOLICITANTE=50;

	 
	 public PeticionSincronaConDatosEspec(){}

	
	protected OMElement generarSolicitudTransmision(HttpServletRequest request, ClientContext clientContext) throws ISPACException {
		log.warn("Generando solicitud de transmision.");
		String certificado = request.getParameter("certificado");
		String emisorServicio = request.getParameter("emisor");
		String version = request.getParameter("version");
		//Ejecutamos los preprocesadores, si los hubiera, antes de montar la peticion.
		try {
			ejecutarPreprocesadores(certificado,request, clientContext);
		} catch (ISPACException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
		
		String classname = request.getParameter("xmlDatosEspecificos");
		String modo = read(request, "modoEntrada");
		String xmlDatosEspecificos = request.getParameter("datosEspecificos");
		OMElement datosEspecificos = null;
		if ("formulario".equals(modo)) {
			if (classname != null) {
				log.warn("Generando datos especificos a partir de los datos del formulario.");
				try {
					String resultado = classname.substring(0,1).toUpperCase() + classname.substring(1);
					Object obj = Class.forName("es.dipucr.core.datos."+resultado).newInstance();
					DatosEspecificosAdapter adapter = (DatosEspecificosAdapter) obj;
					//He comentado esto esta opcion es solo para datos especificos
					if(adapter.parseXmlDatosEspecificos(request, version)!=null){
						datosEspecificos = adapter.parseXmlDatosEspecificos(request, version);
					}
					
				} catch (Exception e) {
					String a = "Error al generar los datos especificos del mensaje. " + e.getMessage();
					throw new ISPACException("Error certificado "+certificado+" emisorServicio "+emisorServicio+ " - "+a+"- "+e.getMessage(), e);
					
					 
				}
			}
		} else {
			log.warn("Generando datos especificos a partir del XML.");
			/*if (xmlDatosEspecificos != null) {
				datosEspecificos = parse(xmlDatosEspecificos);
			} else {
				log.warn("La peticion no contiene datos especificos.");
			}*/
		}
		return datosEspecificos;

	}
	
	protected Element parse(String xml) throws ISPACRuleException {
		if (xml == null || "".equals(xml)) {
			return null;
		}
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			return (Element) factory.newDocumentBuilder().parse(in).getDocumentElement();
		} catch (Exception F) {
			try{
				ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("ISO-8859-1"));
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(true);
				return (Element) factory.newDocumentBuilder().parse(in).getDocumentElement();
			}catch (Exception e) {
				String mensaje = "El valor de datos especificos no tiene una estructura XML correcta.";
				LOGGER.error(mensaje +" - "+e.getMessage(), e);
				throw new ISPACRuleException(mensaje +" - "+e.getMessage(), e);
			}
		}
	}
	
	protected void ejecutarPreprocesadores(String certificado,HttpServletRequest request, ClientContext clientContext) throws ISPACException {
		
    	String url = ServiciosWebSVDFunciones.getDireccionClienteLigeroSW();   	
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy(url);
		
		//Ejecutamos los pre-procesadores
		String[] preprocessor = null;
		try {
			preprocessor = clienteLigero.getPreprocessorDatosEspecificos(EntidadesAdmUtil.obtenerNombreLargoEntidadById(clientContext), certificado);
		} catch (RemoteException e1) {
			LOGGER.error(e1.getMessage(), e1);
		}
		if (preprocessor != null) {
			for (String s : preprocessor) {
				log.warn("Ejecutando preprocesador " + s);
				try {
					Object obj = Class.forName(s).newInstance();
					DatosEspecificosPreprocessor dePre = (DatosEspecificosPreprocessor) obj;
					dePre.handleRequest((DatosEspecificosAdapterBase) request);
				} catch (Exception e) {
					String a []= {"Error al ejecutar el preprocesador " + e + ". " + e.getMessage() };
					throw new ISPACException("Error certificado "+certificado+ " - "+e.getMessage(), e);
				}
			}
		}
	}
	//Este metodo esta pensado para tratar la respuesta con n postprocesadores.
	/*protected void ejecutarPostprocesadores(String certificado,Object respuesta,HttpServletRequest request,HttpServletResponse response, ClientContext clientContext) throws ISPACException {
		
		String url = ServiciosWebSVDFunciones.getDireccionClienteLigeroSW();   	
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy(url);
		
		String[] postprocessor = null;
		try {
			postprocessor = clienteLigero.getPostprocessorDatosEspecificos(EntidadesAdmUtil.obtenerNombreLargoEntidadById(clientContext), certificado);
		} catch (RemoteException e1) {
			logger.error(e1.getMessage(), e1);
		}
		if (postprocessor != null) {
			for (String s : postprocessor) {
				log.warn("Ejecutando postprocesador " + s);
				try {
					Object obj = Class.forName(s).newInstance();
					DatosEspecificosPostprocessor dePost = (DatosEspecificosPostprocessor) obj;
					dePost.handleResponse(respuesta,request,response);
				} catch (Exception e) {
					String a []= {"Error al ejecutar el postprocesador " + e + ". " + e.getMessage() };
					throw new ISPACException("Error certificado "+certificado+ " - "+e.getMessage(), e);
				}
			}
		}
	}*/

	
	/**
	 * Lee un campo de un formulario (que puede ser de tipo octect-stream).
	 * @param request
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected String read(HttpServletRequest request, String name) {
		if (request.getAttribute(KEY_PARAM_MAP) != null) {
			Map<String, String> mapRequest = (Map<String, String>) request.getAttribute(KEY_PARAM_MAP);
			return "".equals(mapRequest.get(name)) ? null : mapRequest.get(name);
		}
		//Caso en el que no se utiliza multipart para evniar peticiones
		if (!ServletFileUpload.isMultipartContent(request)) {
			Map<String, String> mapRequest = new LinkedHashMap<String, String>();
			request.setAttribute(KEY_PARAM_MAP, mapRequest);
			Enumeration<String> e = request.getParameterNames();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				mapRequest.put(key, request.getParameter(key));
			}
			return "".equals(mapRequest.get(name)) ? null : mapRequest.get(name);
		}
		//En el caso de que se trate de una peticion de tipo multipart comprobamos
		//si la hemos procesado previamente. En ese caso habremos adido a la
		//peticion un atributo que mapea todos los elementos de los formularios.
		//Si no la hemos procesado previamente la procesaremos y generaremos el map
		//con los atributos.
		if (ServletFileUpload.isMultipartContent(request)) {
			Map<String, String> mapRequest = (Map<String, String>) request.getAttribute(KEY_PARAM_MAP);
			if (mapRequest != null) {
				return "".equals(mapRequest.get(name)) ? null : mapRequest.get(name);
			} else {
				Map<String, String> map = new LinkedHashMap<String, String>();
				log.debug("Procesando peticion multipart.");
				try {
					FileItemFactory fileItemFactory = new DiskFileItemFactory();
					ServletFileUpload servletFileUpload = new ServletFileUpload(fileItemFactory);
					servletFileUpload.setSizeMax(25L * 1024L * 1024L);
					List<?> items = servletFileUpload.parseRequest(request);
					for (Object o : items) {
						FileItem fileItem = (FileItem) o;
						if (fileItem.isFormField()) {
							map.put(fileItem.getFieldName(), fileItem.getString());
						} else {
							//TODO
							request.setAttribute(KEY_EXCEL_FILE_ITEM, fileItem);
						}
					}
					request.setAttribute(KEY_PARAM_MAP, map);
					return map.get(name);
				} catch (Exception e) {
					String msg = "Error al procesar el contenido multipart de la peticion HTTP.";
					throw new RuntimeException(msg, e);
				}
			}
		}
		return null;
	}
}