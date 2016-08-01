package es.scsp.client.test;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import es.dipucr.integracion.general.Util;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import es.dipucr.verifdatos.services.ScspProxy;

public class PeticionSincronaConDatosEspecSync extends PeticionSincronaConDatosEspec {
	
	private static final Log logger = LogFactory.getLog(PeticionSincronaConDatosEspec.class);
	

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		//////logger.warn("INICIO PeticionSincronaConDatosEspecSync");

		SessionAPI sesion = null;
		try {
			sesion = SessionAPIFactory.getSessionAPI(request, response);
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		String version = request.getParameter("version");
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		Element elementDatosEspecificos = super.generarSolicitudTransmision(request, entitiesAPI);
		
		return envioPeticionDirecta(request, sesion, elementDatosEspecificos, version);

	}
	
	public String getStateticket(HttpServletRequest request) {
		return (String)request.getAttribute(SessionAPIFactory.ATTR_STATETICKET);
	}
	
	private ActionForward envioPeticionDirecta(HttpServletRequest request, SessionAPI session, Element elementDatosEspecificos, String version) throws ISPACRuleException {
		ActionForward actionForward = new ActionForward();
		IManagerAPI managerAPI = null;
		try{
			//----------------------------------------------------------------------------------------------
			ClientContext cct = session.getClientContext();
			managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			IState currentState = managerAPI.currentState(getStateticket(request));
	        //----------------------------------------------------------------------------------------------
			
			// Codigo de procedimiento
			String procSelec =  request.getParameter("certificado");
			String nombreProc = request.getParameter("nombreProc");
			
			//logger.warn("procedimiento "+procSelec);
			
			List<CharSequence> resultado = null;

			StringBuffer sbRespuestaPdf = null;
			try {
				resultado = invoke(procSelec, nombreProc, session, request, elementDatosEspecificos, version);
				if(resultado.size() > 1){
					sbRespuestaPdf = (StringBuffer) resultado.get(1);
				}
				
				
				
			} catch (Exception e) {
				logger.error(e);
				throw new ISPACRuleException(e);
			}
			if(sbRespuestaPdf!=null){
				byte decoded[] = Base64.decodeBase64(sbRespuestaPdf.toString());
				//Guarda el resultado en gestor documental
				String STR_nombreTramite = "Servicio de Verificación de Datos";
				
				int tpdoc = DocumentosUtil.getTipoDoc(cct, STR_nombreTramite, DocumentosUtil.BUSQUEDA_EXACTA, false);
		        int taskId = currentState.getTaskId();

				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() +  "/" + FileTemporaryManager.getInstance().newFileName(".pdf");
				FileOutputStream  fos = null;
				try {
					fos = new FileOutputStream(rutaFileName);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
				fos.write(decoded);
				fos.close();
				fos.flush();
				
				File file = new File(rutaFileName);
				
				IItem entityDoc = DocumentosUtil.generaYAnexaDocumento(cct, taskId, tpdoc, nombreProc, file, Constants._EXTENSION_PDF);

				entityDoc.set("FFIRMA", new Date());
				entityDoc.store(cct);
				file.delete();
			}
			else{
				String error = (String)resultado.get(0);
				logger.error("error ----------------- "+error);
				ISPACException info = new ISPACException(error, true);
				request.getSession().setAttribute("infoAlert", info);
			}
		} catch (ISPACException e) {
			logger.error("Se produjo una excepción. " + e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción. " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Se produjo una excepción. " + e.getMessage(), e);
			throw new ISPACRuleException("Se produjo una excepción. " + e.getMessage(), e);
		}
		
		IState currentState =null;
		try {
			currentState = managerAPI.currentState(getStateticket(request));
		} catch (ISPACException e) {
			logger.error(e.getMessage(), e);
		}
		int taskId = currentState.getTaskId();
		actionForward.setPath("/showTask.do?taskId=" + taskId);
		actionForward.setRedirect(true);
		return actionForward;
		
		
		
	}

	private List<CharSequence> invoke(String codigoCertif, String nombreProc, SessionAPI session,
			HttpServletRequest request, Element elementDatosEspecificos, String version) throws ISPACException {
		//----------------------------------------------------------------------------------------------
		ClientContext cct = session.getClientContext();
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState state = managerAPI.currentState(getStateticket(request));
        //----------------------------------------------------------------------------------------------
		
		List<CharSequence> lResultado = new ArrayList<CharSequence>();
				
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");  
		
		/**
		 * PETICIÓN 
		 * **/
		
		OMElement peticionSincrona = fac.createOMElement("PeticionSincrona", omNs);
   
		OMElement atributos = fac.createOMElement("Atributos", omNs);  
		OMElement codigoCertificado = fac.createOMElement("CodigoCertificado", omNs);  
		codigoCertificado.setText(codigoCertif);
		//logger.warn("procedimiento "+codigoCertif);
		atributos.addChild(codigoCertificado);
 
		OMElement solicitante = fac.createOMElement("Solicitante", omNs);  
		
		OMElement identificadorSolicitante = fac.createOMElement("IdentificadorSolicitante", omNs);  
		identificadorSolicitante.setText("P1300000E");
		OMElement nombreSolicitante = fac.createOMElement("NombreSolicitante", omNs);  
		nombreSolicitante.setText("Diputación Provincial de Ciudad Real");
		
		//Finalidad
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		OMElement finalidad = fac.createOMElement("Finalidad", omNs); 
		String SFinalidad = "Entidad: --> "+entidad+" ## "+nombreProc;
		
		
		//logger.warn("finalidad. La Entidad que va a hacer la solicitud es --> "+entidad);
		
		//Consentimiento
		OMElement consentimiento = fac.createOMElement("Consentimiento", omNs);  
		consentimiento.setText("Si");
		
		//Funcionario
		OMElement funcionario = fac.createOMElement("Funcionario", omNs);
		OMElement nifFuncionario = fac.createOMElement("NifFuncionario", omNs);
		String dni = request.getParameter("cifFuncionario");
		nifFuncionario.setText(dni);
		OMElement nombreCompletoFuncionario = fac.createOMElement("NombreCompletoFuncionario", omNs);
		String nombreDni = request.getParameter("nombreFuncionario");
		
		nombreCompletoFuncionario.setText(nombreDni);
		funcionario.addChild(nombreCompletoFuncionario);
		funcionario.addChild(nifFuncionario);
		//logger.warn("dni "+dni+" nombreDni "+nombreDni);
		
		//Unidad Tramitadora
		//Nombre del departamento
		String numexp = state.getNumexp();
		IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
        String sNombreDepartamento = itemExpediente.getString("SECCIONINICIADORA");
		OMElement unidadTramitadora = fac.createOMElement("UnidadTramitadora", omNs);
		unidadTramitadora.setText(sNombreDepartamento);
		//logger.warn("sNombreDepartamento "+sNombreDepartamento);
		
		//Procedimiento
		//COgerlo del expediente para saber que datos es
		OMElement procedimiento = fac.createOMElement("Procedimiento", omNs);
		OMElement codProcedimiento = fac.createOMElement("CodProcedimiento", omNs);
		int pdcId = state.getPcdId();
		IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, pdcId);
		//logger.warn("pdcId "+pdcId);
		String codProc = ctProcedure.getString("COD_PCD");
		codProcedimiento.setText(codProc);
		if(codigoCertif.equals("VDRSFWS02")){
			codProcedimiento.setText("CODSVDR_DCR_20120412");
		}
		OMElement nombreProcedimiento = fac.createOMElement("NombreProcedimiento", omNs);
		String nomProc = ctProcedure.getString("NOMBRE");
		nombreProcedimiento.setText(nomProc);
		//Para probar el procedimiento en pruebas de Verificacion de datos de residencia
		if(codigoCertif.equals("VDRSFWS02")){
			nombreProcedimiento.setText("PRUEBAS E INTEGRACION PARA DIPUTACION DE CIUDAD REAL");
		}
		procedimiento.addChild(codProcedimiento);
		procedimiento.addChild(nombreProcedimiento);
		
		//Identificador del expediente
		OMElement idExpediente = fac.createOMElement("IdExpediente", omNs);
		idExpediente.setText(numexp);
		
		//Finalidad con los datos del idexpediente y el codigo del procedimiento
		//Para probar el procedimiento en pruebas de Verificacion de datos de residencia
		if(codigoCertif.equals("VDRSFWS02")){
			finalidad.setText("CODSVDR_DCR_20120412"+"#::#"+numexp+"#::#"+SFinalidad);
		}
		else{
			finalidad.setText(SFinalidad);
		}
		
		solicitante.addChild(identificadorSolicitante);
		solicitante.addChild(nombreSolicitante);
		solicitante.addChild(unidadTramitadora);
		solicitante.addChild(procedimiento);
		solicitante.addChild(finalidad);
		solicitante.addChild(consentimiento);
		solicitante.addChild(funcionario);
		solicitante.addChild(idExpediente);
		

		//Estos datos los tiene que meter el funcionario
		OMElement titular = fac.createOMElement("Titular", omNs);  
		OMElement tipoDocumentacion = fac.createOMElement("TipoDocumentacion", omNs);  
		//tipoDocumentacion.setText("NIF");
		OMElement documentacion = fac.createOMElement("Documentacion", omNs);  
		
		//Datos del titular
		//Buscamos en spac_dt_intervinientes
		//logger.warn("numpex "+numexp+" strQuery. "+strQuery);
    	IItemCollection participantes = entitiesAPI.getParticipants(numexp, "(ROL != 'TRAS' OR ROL IS NULL)", "ID");
    	Iterator<?> itParticipante = participantes.iterator();
    	//logger.warn("participantes. "+participantes.toList().size());
    	String nombrePartic = "";
		String ndocPartic = "";
    	while(itParticipante.hasNext()){
    		IItem participante = (IItem)itParticipante.next();
    		
    		if ((String)participante.getString("NDOC")!=null) ndocPartic = (String)participante.getString("NDOC");
        	if ((String)participante.getString("NOMBRE")!=null) nombrePartic = (String)participante.getString("NOMBRE");
    	}
    	documentacion.setText(ndocPartic);
    	
    	//Compruebo si es NIF o CIF
    	String caracterPrimero = ndocPartic.substring(0, 1);
    	//logger.warn("caracterPrimero. "+caracterPrimero);
    	
    	if(isNumeric(caracterPrimero)){
    		tipoDocumentacion.setText("NIF");
    	}
    	else{
    		if(caracterPrimero.equals("X") || caracterPrimero.equals("Y") || caracterPrimero.equals("Z")){
    			tipoDocumentacion.setText("NIE");
    		}
    		else{
    			tipoDocumentacion.setText("CIF");
    		}
    	}
    	
    	documentacion.setText(ndocPartic);
    	
		titular.addChild(tipoDocumentacion);
		titular.addChild(documentacion);
		if(!codigoCertif.equals("VDRSFWS02")){
			OMElement nombreCompleto = fac.createOMElement("NombreCompleto", omNs);  
	    	nombreCompleto.setText(nombrePartic);
			titular.addChild(nombreCompleto);
		}
		
		OMElement datosGenericos = fac.createOMElement("DatosGenericos", omNs);  
		datosGenericos.addChild(solicitante);
		datosGenericos.addChild(titular);
		
		OMElement solicitudTransmision = fac.createOMElement("SolicitudTransmision", omNs);  
		solicitudTransmision.addChild(datosGenericos);
		
		
		if(elementDatosEspecificos!=null){
			String ruta = "";
			if(version.contains("V2")) {
				ruta = "http://www.map.es/scsp/esquemas/datosespecificos";
			} else {
				ruta = "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";;
			}

			OMNamespace omNsDe = fac.createOMNamespace(ruta, "");
			OMElement datosEspecificos = fac.createOMElement("DatosEspecificos", omNsDe);  
			
			
			//logger.warn("datos espeficificos "+elementDatosEspecificos.toString());
			
			// Iteramos sobre sus hijos  
			NodeList hijos = elementDatosEspecificos.getChildNodes(); 
			//logger.warn("1."+hijos.getLength());
			for(int i=0;i<hijos.getLength();i++){  
			   Node nodo = hijos.item(i);
			   
			   if (nodo instanceof Element){  
				  String nodo1 = nodo.getNodeName();
				  //logger.warn("nodo "+nodo1);
				  OMElement datos = fac.createOMElement(nodo1, omNsDe);
				  NodeList hijos2 = nodo.getChildNodes();
				  if(hijos2.getLength() > 0){
					  if(hijos2.getLength() == 1){
						  //logger.warn("legth"+hijos.getLength());
							for(int j=0;j<hijos.getLength();j++){  
							   if (nodo instanceof Element){  
								  Node nodeHijoRec = nodo.getFirstChild();
							      Text te = ((nodeHijoRec != null) && nodeHijoRec instanceof Text) ? (Text) nodeHijoRec : null;
							      if(te != null){
							    	  datos.setText(te.getData());
									  //logger.warn("nodo "+nodoRecursivo+"te "+te.getData());
							      }
							   }
							}
					  }
					  else{
						  recorrerArbol(datos, hijos2, fac, omNsDe);
					  }
					  datosEspecificos.addChild(datos);
				  }
			   }
			} 
			solicitudTransmision.addChild(datosEspecificos);
		}
		
		OMElement solicitudes = fac.createOMElement("Solicitudes", omNs);  
		solicitudes.addChild(solicitudTransmision);
		
		peticionSincrona.addChild(atributos);  
		peticionSincrona.addChild(solicitudes);
		
		//Busco la url del recubrimiento

    	String url = ServiciosWebSVDFunciones.getDireccionRecubrimientoSW();
		Element element = null;
		
		try {
			ServiceClient client = new ServiceClient();
			Options opts = new Options();  
			opts.setTimeOutInMilliSeconds(300000);

			//options.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
			opts.setTo(new EndpointReference(url));  
			opts.setAction("peticionSincrona");  
			client.setOptions(opts);

			//logger.warn(peticionSincrona.toString());
			OMElement res = client.sendReceive(peticionSincrona);  
			//logger.warn(res);
		
			element = XMLUtils.toDOM(res);
			client.cleanup();
			client.cleanupTransport();

		} catch (AxisFault e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException(e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ISPACRuleException(e);
		} 
		
		String literalError = "";
		try {
			literalError =  Util.getTextNode(element,"ns1:LiteralError");
			if(literalError == null){
				literalError =  Util.getTextNode(element,"LiteralError");
				if(literalError == null) literalError="";
			}
			//logger.warn("literalError "+literalError);
			lResultado.add(0, literalError);
		} catch (Exception e1) {
			logger.error(e1.getMessage(), e1);
		}
		StringBuffer sb = null;
		
		/**
		 * 
		 * PETICIÓN DEL PDF
		 * 
		 * **/
		if(codigoCertif.equals("SVDCDYGWS01") || codigoCertif.equals("SVDCTITWS01")){
			try {
				sb = new StringBuffer(Util.getTextNode(element,"pdf"));
				lResultado.add(1, sb);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		else{
			if(!literalError.equals("USUARIO NO AUTORIZADO")){
				
				sb = new StringBuffer();
				OMFactory facPDF = OMAbstractFactory.getOMFactory();
				OMNamespace omNsPDF = facPDF.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");
				OMElement peticionPdf = facPDF.createOMElement("PeticionPdf", omNsPDF);
		
				try {
					//String sIdPeticion = devuelveValor(element, "IdPeticion");
					String sIdPeticion =  Util.getTextNode(element,"IdPeticion");
					
					//Actualizar la tabla core_transmision con un servicio web
			    	
			    	String urlSCSP = ServiciosWebSVDFunciones.getDireccionSCSPSW();
					
					ScspProxy clienteScsp = new ScspProxy(urlSCSP);
					clienteScsp.updateCoreTransmision(sIdPeticion, sNombreDepartamento, codProc, nomProc, numexp);
					
					//
					OMElement idPeticion = fac.createOMElement("IdPeticion", omNsPDF);
					idPeticion.setText(sIdPeticion);
					
					//String sIdTransmision = devuelveValor(element, "IdTransmision");
					String sIdTransmision = Util.getTextNode(element,"IdTransmision");
					OMElement idTransmision = fac.createOMElement("IdTransmision", omNsPDF);
					idTransmision.setText(sIdTransmision);
					
					
					peticionPdf.addChild(idPeticion);
					peticionPdf.addChild(idTransmision);
					
					
					ServiceClient client = new ServiceClient();
					Options opts = new Options();  
					opts.setTimeOutInMilliSeconds(300000); 
		
					//options.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
					opts.setTo(new EndpointReference(url));  
					opts.setAction("PeticionPdf");  
					client.setOptions(opts);
					
					//logger.warn(peticionPdf.toString());
					OMElement res = client.sendReceive(peticionPdf);
					Element respuestaPdf = XMLUtils.toDOM(res);
					client.cleanup();
					client.cleanupTransport();
		//				Node nodePdf =Util.getNode(respuestaPdf, "pdf");
					
					sb = new StringBuffer(Util.getTextNode(respuestaPdf,"pdf"));
					
					lResultado.add(1, sb);
					
		//				FileOutputStream  fos = null;
		//				try {
		//					fos = new FileOutputStream("C://prueba.pdf");
		//				} catch (IOException e) {
		//					logger.error(e);
		//					throw new ISPACRuleException(e);
		//				}
		//				fos.write(decoded);
		//				fos.close();
					
					
		
		
				} catch (AxisFault e) {
					logger.error(e);
					throw new ISPACRuleException(e);
				} catch (Exception e) {
					logger.error(e);
					throw new ISPACRuleException(e);
				}
			  
			}
		}
		
		return lResultado;
	}

	private void recorrerArbol(OMElement datosEsp, NodeList hijos, OMFactory fac, OMNamespace omNs) {
		//logger.warn("legth"+hijos.getLength());
		for(int i=0;i<hijos.getLength();i++){  
		   Node nodo = hijos.item(i);
		   
		   if (nodo instanceof Element){  
			  String nodo1 = nodo.getNodeName();
			  OMElement datos = fac.createOMElement(nodo1, omNs);
			  Node node = nodo.getFirstChild();
		      Text te = ((node != null) && node instanceof Text) ? (Text) node : null;
		      if(te != null){
		    	  datos.setText(te.getData());
				  //logger.warn("nodo "+nodo1+"te "+te.getData());
		      }
		      NodeList hijos2 = nodo.getChildNodes();
			  if(hijos2.getLength() > 0){
				  recorrerArbol(datos, hijos2, fac, omNs);
				  datosEsp.addChild(datos);
			  }		      
		   }
		}
		
	}
	
	private boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}
