package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.IThirdPartyAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.dipucr.integracion.general.Util;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import es.dipucr.verifdatos.services.ScspProxy;

public class PeticionSincronaSinDatosEspec {

	
	public static Map<String,String> datosTitular = new LinkedHashMap<String,String>();
	
	private static final Logger logger = Logger.getLogger(PeticionSincronaSinDatosEspec.class);
	private static String codigoCertif = "";
	private String nombreServicio = "";

	static {
		datosTitular.put("cdi."+codigoCertif+".nodo.nacionalidad"				,"Nacionalidad");
		datosTitular.put("cdi."+codigoCertif+".nodo.sexo"						,"Sexo");
	}


	public PeticionSincronaSinDatosEspec(String nombreProc) {
		this.nombreServicio = nombreProc;
	}
	
	public StringBuffer invoke(SessionAPI session, HttpServletRequest request, Element elementDatosEspecificos) throws ISPACException{
		
		//----------------------------------------------------------------------------------------------
		ClientContext cct = session.getClientContext();
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState state = managerAPI.currentState((String)request.getAttribute(SessionAPIFactory.ATTR_STATETICKET));
		IThirdPartyAPI thirdPartyAPI = invesFlowAPI.getThirdPartyAPI();
		IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
        //----------------------------------------------------------------------------------------------
		
		
		if(request.getParameter("procedimiento") != null) codigoCertif = request.getParameter("procedimiento");
		
		//dni
		String dni = "";
		if(request.getParameter("dniValue") != null) dni = request.getParameter("dniValue");
		//nombre dni
		String nombreDni = "";
		if(request.getParameter("nombreDniValue") != null) nombreDni = request.getParameter("nombreDniValue");
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");  
		
		/**
		 * PETICIÓN 
		 * **/
		
		OMElement peticionSincrona = fac.createOMElement("PeticionSincrona", omNs);
   
		OMElement atributos = fac.createOMElement("Atributos", omNs);  
		OMElement codigoCertificado = fac.createOMElement("CodigoCertificado", omNs);  
		codigoCertificado.setText(codigoCertif);
		atributos.addChild(codigoCertificado);
 
		OMElement solicitante = fac.createOMElement("Solicitante", omNs);  
		
		OMElement identificadorSolicitante = fac.createOMElement("IdentificadorSolicitante", omNs);  
		identificadorSolicitante.setText("Q3150012G");
		OMElement nombreSolicitante = fac.createOMElement("NombreSolicitante", omNs);  
		nombreSolicitante.setText("Universidad Pública de Navarra");
		
		//Finalidad
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		OMElement finalidad = fac.createOMElement("Finalidad", omNs);  
		finalidad.setText("Entidad: --> "+entidad+" ## "+this.nombreServicio);
		
		//Consentimiento
		OMElement consentimiento = fac.createOMElement("Consentimiento", omNs);  
		consentimiento.setText("Si");
		
		//Funcionario
		OMElement funcionario = fac.createOMElement("Funcionario", omNs);
		OMElement nifFuncionario = fac.createOMElement("NifFuncionario", omNs);
		nifFuncionario.setText(dni);
		OMElement nombreCompletoFuncionario = fac.createOMElement("NombreCompletoFuncionario", omNs);
		nombreCompletoFuncionario.setText(nombreDni);
		funcionario.addChild(nombreCompletoFuncionario);
		funcionario.addChild(nifFuncionario);
		
		//Unidad Tramitadora
		//Nombre del departamento
		String numexp = state.getNumexp();
		IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
        String sNombreDepartamento = itemExpediente.getString("SECCIONINICIADORA");
		OMElement unidadTramitadora = fac.createOMElement("UnidadTramitadora", omNs);
		if(sNombreDepartamento==null){
			int id_pcd_actual = itemExpediente.getInt("ID_PCD");
			
			//Buscamos el procedimiento asociado a dicho código para recuperar el departamento
			IItem procedimientoProce = procedureAPI.getProcedureById(id_pcd_actual);
			if(procedimientoProce != null){
				sNombreDepartamento = (String) procedimientoProce.get("CTPROCEDIMIENTOS:ORG_RSLTR");
			}
		}
		logger.warn("unidadTramitadora "+sNombreDepartamento);
		unidadTramitadora.setText(sNombreDepartamento);
		
		//Procedimiento
		//COgerlo del expediente para saber que datos es
		OMElement procedimiento = fac.createOMElement("Procedimiento", omNs);
		OMElement codProcedimiento = fac.createOMElement("CodProcedimiento", omNs);
		int pdcId = state.getPcdId();
		IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, pdcId);
		String codProc = ctProcedure.getString("COD_PCD");
		codProcedimiento.setText(codProc);
		OMElement nombreProcedimiento = fac.createOMElement("NombreProcedimiento", omNs);
		String nomProc = ctProcedure.getString("NOMBRE");
		nombreProcedimiento.setText(nomProc);
		procedimiento.addChild(codProcedimiento);
		procedimiento.addChild(nombreProcedimiento);
		
		//Identificador del expediente
		OMElement idExpediente = fac.createOMElement("IdExpediente", omNs);
		idExpediente.setText(numexp);
		
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
		
		OMElement documentacion = fac.createOMElement("Documentacion", omNs);
		
		//Datos del titular
		//Buscamos en spac_dt_intervinientes

		IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, numexp,  "ROL != 'TRAS' OR ROL IS NULL",  "ID");
    	Iterator<?> itParticipante = participantes.iterator();

    	String ndocPartic = "";
		String nombreCorrientePago = "";
    	while(itParticipante.hasNext()){
    		IItem participante = (IItem)itParticipante.next();
    		
    		if ((String)participante.getString("NDOC")!=null) ndocPartic = (String)participante.getString("NDOC");
        	IThirdPartyAdapter[] vThirdParty = thirdPartyAPI.lookup(ndocPartic);
        	if(vThirdParty!=null){
        		if(vThirdParty.length > 0){
        			IThirdPartyAdapter thirdParty = vThirdParty[0];
        			if(thirdParty != null){
        				nombreCorrientePago = thirdParty.getPrimerApellido()+" "+thirdParty.getSegundoApellido()+" "+thirdParty.getNombre();
        			}
        		}
        	}
    	}
    	//Compruebo si es NIF o CIF
    	String caracterPrimero = ndocPartic.substring(0, 1);
    	
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

		if(codigoCertif.equals("AEAT101I") || codigoCertif.equals("AEAT102I") || codigoCertif.equals("AEAT103I") || codigoCertif.equals("AEAT104I")){
			OMElement nombreCompleto = fac.createOMElement("NombreCompleto", omNs);  
	    	nombreCompleto.setText(nombreCorrientePago);
			titular.addChild(nombreCompleto);
		}
		
		
		OMElement datosGenericos = fac.createOMElement("DatosGenericos", omNs);  
		datosGenericos.addChild(solicitante);
		datosGenericos.addChild(titular);
		
		OMElement solicitudTransmision = fac.createOMElement("SolicitudTransmision", omNs);  
		solicitudTransmision.addChild(datosGenericos);
		
		OMElement solicitudes = fac.createOMElement("Solicitudes", omNs);  
		solicitudes.addChild(solicitudTransmision);
		
		peticionSincrona.addChild(atributos);  
		peticionSincrona.addChild(solicitudes);
		
		if(elementDatosEspecificos!=null){
			// Iteramos sobre sus hijos  
			NodeList hijos = elementDatosEspecificos.getChildNodes();  
			for(int i=0;i<hijos.getLength();i++){  
			   Node nodo = hijos.item(i);  
			   if (nodo instanceof Element){  
			   }  
			}  
		}
		
		//Busco la url del recubrimiento
    	
    	String url = ServiciosWebSVDFunciones.getDireccionRecubrimientoSW();
		
		//String url = "http://10.12.200.206:8081/scsp-ws/ws";
		//String url = "http://10.12.200.200:8081/scsp-ws/ws"; 
		//String url = "https://10.253.114.137/scsp-ws/ws/";
		Element element = null;
		
		try {
			Options opts = new Options();  
			opts.setTimeOutInMilliSeconds(300000);
			opts.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
			opts.setTo(new EndpointReference(url));  
			opts.setAction("peticionSincrona"); 

			ServiceClient client = new ServiceClient();
			client.setOptions(opts);
			OMElement res = client.sendReceive(peticionSincrona);  
		
			element = XMLUtils.toDOM(res);
			client.cleanup();
			client.cleanupTransport();

		} catch (AxisFault e) {
			logger.error("Error al conectar con la url. "+ url +" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al conectar con la url. "+ url +" - "+e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Error al conectar con la url. "+ url +" - "+e.getMessage(), e);
			throw new ISPACRuleException("Error al conectar con la url. "+ url +" - "+e.getMessage(), e);
		} 
		
		String literalError = "";
		try {
			literalError =  Util.getTextNode(element,"LiteralError");
		} catch (Exception e1) {
			logger.error("Error en literalError"+ literalError +" - "+e1.getMessage(), e1);
			throw new ISPACRuleException("Error en literalError"+ literalError +" - "+e1.getMessage(), e1);
		}
		StringBuffer sb = null;
		
		//if(literalError.equals("TRAMITADA")){
		if(!literalError.equals("USUARIO NO AUTORIZADO")){

			/**
			 * 
			 * PETICIÓN DEL PDF
			 * 
			 * **/
			OMFactory facPDF = OMAbstractFactory.getOMFactory();
			OMNamespace omNsPDF = facPDF.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");
			OMElement peticionPdf = facPDF.createOMElement("PeticionPdf", omNsPDF);
			String urlSCSP = "";
			String sIdPeticion =  "";
	
			try {
				//String sIdPeticion = devuelveValor(element, "IdPeticion");
				sIdPeticion =  Util.getTextNode(element,"IdPeticion");
				
				//Actualizar la tabla core_transmision con un servicio web	    	
		    	urlSCSP = ServiciosWebSVDFunciones.getDireccionSCSPSW();

				
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
				
				OMElement res = client.sendReceive(peticionPdf);
				Element respuestaPdf = XMLUtils.toDOM(res);
				client.cleanup();
				client.cleanupTransport();
				
				sb = new StringBuffer(Util.getTextNode(respuestaPdf,"pdf"));

			} catch (AxisFault e) {
				logger.error("Error en urlSCSP"+ urlSCSP +", con la petición "+sIdPeticion+" - "+e.getMessage(), e);
				throw new ISPACRuleException("Error en urlSCSP"+ urlSCSP +", con la petición "+sIdPeticion+" - "+e.getMessage(), e);
			} catch (Exception e) {
				logger.error("Error en urlSCSP"+ urlSCSP +", con la petición "+sIdPeticion+" - "+e.getMessage(), e);
				throw new ISPACRuleException("Error en urlSCSP"+ urlSCSP +", con la petición "+sIdPeticion+" - "+e.getMessage(), e);
			} 
		}
		else{

			ISPACException info = new ISPACException(literalError, true);
			request.getSession().setAttribute("infoAlert", info);
		}
		
		return sb;
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
