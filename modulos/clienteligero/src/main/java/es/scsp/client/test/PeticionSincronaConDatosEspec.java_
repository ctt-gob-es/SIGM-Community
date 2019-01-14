package es.scsp.client.test;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.w3c.dom.Element;

import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import es.scsp.bean.common.Consentimiento;
import es.scsp.bean.common.Funcionario;
import es.scsp.bean.common.Peticion;
import es.scsp.bean.common.Procedimiento;
import es.scsp.bean.common.Solicitante;
import es.scsp.bean.common.SolicitudTransmision;
import es.scsp.client.test.datosespecificos.DatosEspecificosAdapterBase;
import es.scsp.client.test.datosespecificos.preprocessor.DatosEspecificosPreprocessor;
import es.scsp.client.test.excel.ExcelAdapter;
import es.scsp.client.test.excel.ExcelAdapterCommon;
import es.scsp.client.test.excel.ExcelConfiguration;
import es.scsp.client.test.gestusers.Usuario;
import es.scsp.common.core.ServiceManager;
import es.scsp.common.dao.EmisorCertificadoDao;
import es.scsp.common.dao.ParametroConfiguracionDao;
import es.scsp.common.domain.ParametroConfiguracion;
import es.scsp.common.exceptions.ScspException;
import es.scsp.common.exceptions.ScspExceptionConstants;
import es.scsp.common.utils.StaticContextSupport;


public abstract class PeticionSincronaConDatosEspec extends BaseAction {

	private static final Log log = LogFactory.getLog(PeticionSincronaConDatosEspec.class);
	private ServiceManager serviceManager;
	public static final String KEY_PARAM_MAP = "parameterRequestMap";
	public static final String KEY_EXCEL_FILE_ITEM = "excelFileItem";
	private static final int LONG_MAX_NOMBRE_SOLICITANTE=50;
	 public  EmisorCertificadoDao emisorCertificadoDao; 
	 private ParametroConfiguracionDao parametroConfiguracionDao;
	 
	 public PeticionSincronaConDatosEspec(){}

	public PeticionSincronaConDatosEspec(EmisorCertificadoDao emisorCertificadoDao, ServiceManager serviceManager  ,ParametroConfiguracionDao parametroConfiguracionDao){
		this.emisorCertificadoDao=emisorCertificadoDao;
		 
		this.serviceManager=serviceManager;
		this.parametroConfiguracionDao=  parametroConfiguracionDao;
	}
	
	protected Element generarSolicitudTransmision(HttpServletRequest request, IEntitiesAPI entitiesAPI) throws ScspException {
		log.warn("Generando solicitud de transmision.");
		String certificado = request.getParameter("certificado");
		String emisorServicio = request.getParameter("emisor");
		//Ejecutamos los preprocesadores, si los hubiera, antes de montar la peticion.
		try {
			ejecutarPreprocesadores(certificado,request, entitiesAPI);
		} catch (ISPACException e1) {
			logger.error(e1.getMessage(), e1);
		}
		
		String version = request.getParameter("version");
		

		String classname = request.getParameter("xmlDatosEspecificos");
		String modo = read(request, "modoEntrada");
		String xmlDatosEspecificos = request.getParameter("datosEspecificos");
		Element datosEspecificos = null;
		if ("formulario".equals(modo)) {
			if (classname != null) {
				log.warn("Generando datos especificos a partir de los datos del formulario.");
				try {
					Object obj = Class.forName(classname).newInstance();
					DatosEspecificosAdapterBase adapter = (DatosEspecificosAdapterBase) obj;
					//He comentado esto esta opcion es solo para datos especificos
					datosEspecificos = adapter.parseXmlDatosEspecificos(request, version);
				} catch (Exception e) {
					String a []= {"Error al generar los datos especificos del mensaje. " + e.getMessage() };
					throw   ScspException.getScspException(e, ScspExceptionConstants.ErrorProcesadoDatosEspecificos,a);
					
					 
				}
			}
		} else {
			log.warn("Generando datos especificos a partir del XML.");
			if (xmlDatosEspecificos != null) {
				datosEspecificos = parse(xmlDatosEspecificos);
			} else {
				log.warn("La peticion no contiene datos especificos.");
			}
		}
		return datosEspecificos;

	}
	
//	private Emisor getEmisor(String emisorServicio) throws ScspException {
//		String strQuery = "WHERE NOMBREURL = 'SW_SCSP'";
//    	IItemCollection recubrimientoURL = entitiesAPI.queryEntities("DPCR_SVD", strQuery);
//    	Iterator itRecubrimientoURL = recubrimientoURL.iterator();
//    	
//    	String urlSCSP = "";
//    	
//    	while(itRecubrimientoURL.hasNext()){
//    		IItem itemRecubrimientoURL = (IItem)itRecubrimientoURL.next();
//    		urlSCSP = itemRecubrimientoURL.getString("URL");
//    		logger.warn("url. "+urlSCSP);
//    	}
//		
//		ScspProxy clienteScsp = new ScspProxy(urlSCSP);
//		String cif= "";;
//		try {
//			cif = clienteScsp.getEmisorCertificadoByNombre(emisorServicio);
//		} catch (RemoteException e) {
//			logger.error(e.getMessage(), e);
//		}
//		Emisor beanEmisor = new Emisor();
//		beanEmisor.setNifEmisor(cif);
//		beanEmisor.setNombreEmisor(emisorServicio);
//		return beanEmisor;
//	}
	
	protected void ejecutarPreprocesadores(String certificado,HttpServletRequest request, IEntitiesAPI entitiesAPI) throws ScspException, ISPACException {
		
   	
    	String url = ServiciosWebSVDFunciones.getDireccionClienteLigeroSW();   	
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy(url);
		
		//Ejecutamos los pre-procesadores
		String[] preprocessor = null;
		try {
			preprocessor = clienteLigero.getPreprocessorDatosEspecificos(certificado);
		} catch (RemoteException e1) {
			logger.error(e1.getMessage(), e1);
		}
		if (preprocessor != null) {
			for (String s : preprocessor) {
				log.warn("Ejecutando preprocesador " + s);
				try {
					Object obj = Class.forName(s).newInstance();
					DatosEspecificosPreprocessor dePre = (DatosEspecificosPreprocessor) obj;
					dePre.handleRequest(request);
				} catch (Exception e) {
					String a []= {"Error al ejecutar el preprocesador " + e + ". " + e.getMessage() };
					throw   ScspException.getScspException(e, ScspExceptionConstants.ErrorCargaConfiguracion,a);
				}
			}
		}
	}
	//Este metodo esta pensado para tratar la respuesta con n postprocesadores.
//	protected void ejecutarPostprocesadores(String certificado,Object respuesta,HttpServletRequest request,HttpServletResponse response) throws ScspException {
//		
//		//Ejecutamos los pre-procesadores
//		
//		String strQuery = "WHERE NOMBREURL = 'SW_ClienteLigero'";
//    	IItemCollection recubrimiento = entitiesAPI.queryEntities("DPCR_SVD", strQuery);
//    	Iterator itRecubrimiento = recubrimiento.iterator();
//    	
//    	String url = "";
//    	
//    	while(itRecubrimiento.hasNext()){
//    		IItem itemRecubrimiento = (IItem)itRecubrimiento.next();
//    		url = itemRecubrimiento.getString("URL");
//    		logger.warn("url. "+url);
//    	}
//    	
//		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy(url);
//		String[] postprocessor = null;
//		try {
//			postprocessor = clienteLigero.getPostprocessorDatosEspecificos(certificado);
//		} catch (RemoteException e1) {
//			logger.error(e1.getMessage(), e1);
//		}
//		if (postprocessor != null) {
//			for (String s : postprocessor) {
//				log.warn("Ejecutando postprocesador " + s);
//				try {
//					Object obj = Class.forName(s).newInstance();
//					DatosEspecificosPostprocessor dePost = (DatosEspecificosPostprocessor) obj;
//					dePost.handleResponse(respuesta,request,response);
//				} catch (Exception e) {
//					String a []= {"Error al ejecutar el postprocesador " + e + ". " + e.getMessage() };
//					throw   ScspException.getScspException(e, ScspExceptionConstants.ErrorCargaConfiguracion,a);
//				}
//			}
//		}
//	}
	//Este metodo devuelve los datos genericos del solicitante para el procesador de Excels.
	protected Solicitante getSolicitante(HttpServletRequest request) throws ScspException{
		Solicitante sol = new Solicitante();
		Funcionario func =new Funcionario();
		String nif =read(request, "nifFuncionario");
		String nombre =read(request, "funcionario"); 
		if(nif == null 
				|| nombre == null){
			Usuario funcionario = (Usuario)request.getSession().getAttribute("USUARIO");
			nif = funcionario.getNif();
			nombre = funcionario.getNombre()+" "+funcionario.getApellido1()+" "+funcionario.getApellido2();
		}
		func.setNifFuncionario(nif);
		func.setNombreCompletoFuncionario(nombre);
		if(func.getNifFuncionario() == null && func.getNombreCompletoFuncionario() == null) {
			sol.setFuncionario(null);
		}else sol.setFuncionario(func);
		
		sol.setProcedimiento(new Procedimiento());
		sol.getProcedimiento().setCodProcedimiento(read(request, "codigoProcedimiento"));
		sol.getProcedimiento().setNombreProcedimiento(read(request, "nombreProcedimiento"));
		sol.setUnidadTramitadora(read(request, "unidadTramitadora"));
		sol.setConsentimiento(Consentimiento.valueOf(read(request, "consentimiento")));
		String finalidad =read(request, "finalidad");
		sol.setFinalidad(finalidad);
		return sol;
	}


	protected Element parse(String xml) throws ScspException {
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
				String a []= {"El valor de datos especificos no tiene una estructura XML correcta." };
				throw   ScspException.getScspException(e, ScspExceptionConstants.ErrorProcesadoDatosEspecificos,a);
			}
		}
	}

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
		//si la hemos procesado previamente. En ese caso habremos a�adido a la
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

	protected Object getApplicationContextBean(String name) {
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		// Puede darse el caso de que no se este dentro de un contenedor web,
		// en ese caso gestionamos el contexto de la aplicacion como una
		// variable estatica
		if (ctx == null) {
			ctx = StaticContextSupport.getContextInstance();
		}
		return ctx.getBean(name);
	}
	
	/**
	 * Modifica una peticion para incorporar un conjunto de solicitudes de transmison
	 * obtenidas a partir de una hoja excel subida en la peticion http.
	 * @param peticion
	 * @param request
	 * @param response
	 * @throws ScspException
	 */
	protected void procesarExcel(Peticion peticion, HttpServletRequest request, HttpServletResponse response) throws ScspException {
		log.debug("Procesando peticion a partir de los datos de la hoja Excel.");
		String certificado = read(request, "certificado");
		String cif = read(request, "cif");
		ExcelConfiguration excelConfig = ExcelConfiguration.getInstance(request);
		String className = excelConfig.getExcelProcessorClassname(cif, certificado);
		if(className == null) {
			className = ExcelAdapterCommon.class.getCanonicalName();
		}
		//Obtenemos la clase encargada de procesar la peticion
		ExcelAdapter excelProcessor;
		try {
			Object obj = Class.forName(className).newInstance();
			excelProcessor = (ExcelAdapter) obj;
			excelProcessor.setSolicitante(getSolicitante(request));
			excelProcessor.setRequest(request);
		} catch (Exception e) {
			
			String args[] = {"  No se encuentra la clase encargada de procesar el Excel asociado al servicio."};
			throw   ScspException.getScspException(e, ScspExceptionConstants.ErrorCargaConfiguracion,args);
		}
		
		List<SolicitudTransmision> list = excelProcessor.generarPeticion(request);
		for(SolicitudTransmision item : list) {
			peticion.getSolicitudes().getSolicitudTransmision().add(item);
		}
	}
	
	
	private String getParamValue(String configKey) {
		ParametroConfiguracion pc = parametroConfiguracionDao.select(configKey);
		if (pc == null) {
			return null;
		} else {
			return pc.getValor();
		}
	}
}
