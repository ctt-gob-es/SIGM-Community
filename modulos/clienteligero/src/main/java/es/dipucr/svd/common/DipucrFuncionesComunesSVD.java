package es.dipucr.svd.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.dipucr.integracion.general.PersonalNamespaceContext;
import es.dipucr.sigem.api.rule.common.utils.ConsultasGenericasUtil;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;
import es.dipucr.svd.services.ServiciosWebSVDFunciones;
import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

public class DipucrFuncionesComunesSVD {
	
	public static final Logger LOGGER = Logger.getLogger(DipucrFuncionesComunesSVD.class);
	
	public static String getCODProcGenericoByCODProcEspecifico(IRuleContext rulectx, String cod_proc_sigem) throws ISPACRuleException{
		String cogProcGenerico = "";
		try {
			Iterator<IItem> itProc = ConsultasGenericasUtil.queryEntities(rulectx, "DPCR_SVD_PROCGEN_PROCESP", "VALOR='"+cod_proc_sigem+"'");
			if(itProc.hasNext()){
				IItem itemProc = itProc.next();
				if(StringUtils.isNotEmpty(itemProc.getString("SUSTITUTO"))) cogProcGenerico = itemProc.getString("SUSTITUTO");
			}
			else{
				//Si no tiene campo creado se manda el cod procedimiento de ALSIGM
				cogProcGenerico = cod_proc_sigem;
			}
		}
		 catch (ISPACException e) { 
			 LOGGER.error("Error al obtener el código de procedimiento a mandar al SVD en el número expediente - "+rulectx.getNumExp()+" - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el código de procedimiento a mandar al SVD en el número expediente - "+rulectx.getNumExp()+" - " + e.getMessage(), e);
		}
		
		return cogProcGenerico;
	}
	
	public static String getCODProcGenericoByCODProcEspecifico(IEntitiesAPI entitiesAPI, String cod_proc_sigem) throws ISPACRuleException{
		String cogProcGenerico = "";
		try {
			String squery = "WHERE VALOR='"+cod_proc_sigem+"'";
			IItemCollection itCollection = entitiesAPI.queryEntities("DPCR_SVD_PROCGEN_PROCESP", squery);			
			Iterator<IItem> itProc =  itCollection.iterator();	
			
			if(itProc.hasNext()){
				IItem itemProc = itProc.next();
				if(StringUtils.isNotEmpty(itemProc.getString("SUSTITUTO"))) cogProcGenerico = itemProc.getString("SUSTITUTO");
			}
			else{
				//Si no tiene campo creado se manda el cod procedimiento de ALSIGM
				cogProcGenerico = cod_proc_sigem;
			}
		}
		 catch (ISPACException e) { 
			 LOGGER.error("Error al obtener el código de procedimiento a mandar al SVD en el número expediente - "+ e.getMessage(), e);
			throw new ISPACRuleException("Error al obtener el código de procedimiento a mandar al SVD en el número expediente -  - " + e.getMessage(), e);
		}
		
		return cogProcGenerico;
	}

	public static Element enviaPeticionInteresadosSinDatosEspecificos(IRuleContext rulectx, String codProcedimiento, String nombreProc, String ndocPartic, String sNombreCompleto) throws ISPACRuleException {
		String url = ServiciosWebSVDFunciones.getDireccionRecubrimientoSW();
		Element resultadoServicio = null;
		
		try {
			// ----------------------------------------------------------------------------------------------
			ClientContext cct = (ClientContext) rulectx.getClientContext();
			Responsible resp = cct.getUser();
			IInvesflowAPI invesFlowAPI = cct.getAPI();
			IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
			IProcedureAPI procedureAPI = invesFlowAPI.getProcedureAPI();
			// ----------------------------------------------------------------------------------------------
	
			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMNamespace omNs = fac.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");
	
			/**
			 * PETICIÓN
			 **/
	
			OMElement peticionSincrona = fac.createOMElement("PeticionSincrona", omNs);
	
			OMElement atributos = fac.createOMElement("Atributos", omNs);
			OMElement codigoCertificado = fac.createOMElement("CodigoCertificado", omNs);
			codigoCertificado.setText(codProcedimiento);
			atributos.addChild(codigoCertificado);
	
			OMElement solicitante = fac.createOMElement("Solicitante", omNs);
	
			OMElement identificadorSolicitante = fac.createOMElement("IdentificadorSolicitante", omNs);
			// identificadorSolicitante.setText("P1300000E");
			Entidad entidadObjeto = null;
			try {
				ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
				entidadObjeto = servicioEntidades.obtenerEntidad(EntidadesAdmUtil.obtenerEntidad(cct));
			} catch (SigemException e2) {
				LOGGER.error("Error al obtener la entidad " + rulectx.getNumExp() + " - " + e2.getMessage(), e2);
				throw new ISPACRuleException("Error al obtener la entidad " + rulectx.getNumExp()  + " - " + e2.getMessage(),e2);
			}
	
			identificadorSolicitante.setText(entidadObjeto.getCif());
			OMElement nombreSolicitante = fac.createOMElement("NombreSolicitante", omNs);
			// nombreSolicitante.setText("Diputación Provincial de Ciudad Real");
			nombreSolicitante.setText(EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct));
	
			// Finalidad
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			OMElement finalidad = fac.createOMElement("Finalidad", omNs);
			
			IResponsible dep = resp.getRespOrgUnit();
			String servicio = EntidadesAdmUtil.obtenerNombreLargoEntidadById(cct);
			String area = "";
	        if (dep != null){
	        	servicio=dep.getName();
	        	dep = dep.getRespOrgUnit();
	        }
	        if (dep != null){
	        	area=dep.getName();
	        }
			finalidad.setText("Entidad: --> " + entidad + " ## " + area+"/"+servicio);
	
			// Consentimiento
			OMElement consentimiento = fac.createOMElement("Consentimiento", omNs);
			consentimiento.setText("Si");
	
			// Funcionario
			OMElement funcionario = fac.createOMElement("Funcionario", omNs);
			OMElement nifFuncionario = fac.createOMElement("NifFuncionario", omNs);			
			String dniFuncionario = UsuariosUtil.getDni(cct);			
			nifFuncionario.setText(dniFuncionario);
			
			OMElement nombreCompletoFuncionario = fac.createOMElement("NombreCompletoFuncionario", omNs);
			String strNombreCompletoFuncionario = UsuariosUtil.getNombreFirma(cct);
			nombreCompletoFuncionario.setText(strNombreCompletoFuncionario);
			funcionario.addChild(nombreCompletoFuncionario);
			funcionario.addChild(nifFuncionario);
	
			// Unidad Tramitadora
			// Nombre del departamento
			String numexp = rulectx.getNumExp();
			IItem itemExpediente = ExpedientesUtil.getExpediente(cct, numexp);
			String sNombreDepartamento = itemExpediente.getString("SECCIONINICIADORA");
			OMElement unidadTramitadora = fac.createOMElement("UnidadTramitadora", omNs);
			if (sNombreDepartamento == null) {
				int id_pcd_actual = itemExpediente.getInt("ID_PCD");
	
				// Buscamos el procedimiento asociado a dicho código para recuperar
				// el departamento
				IItem procedimientoProce = procedureAPI.getProcedureById(id_pcd_actual);
				if (procedimientoProce != null) {
					sNombreDepartamento = (String) procedimientoProce.get("CTPROCEDIMIENTOS:ORG_RSLTR");
				}
			}
			LOGGER.warn("unidadTramitadora " + sNombreDepartamento);
			unidadTramitadora.setText(sNombreDepartamento);
	
			// Procedimiento
			// COgerlo del expediente para saber que datos es
			OMElement procedimiento = fac.createOMElement("Procedimiento", omNs);
			OMElement omCodProcedimiento = fac.createOMElement("CodProcedimiento", omNs);		
			int pdcId = rulectx.getProcedureId();
			IItem ctProcedure = entitiesAPI.getEntity(SpacEntities.SPAC_CT_PROCEDIMIENTOS, pdcId);
			String codProc = DipucrFuncionesComunesSVD.getCODProcGenericoByCODProcEspecifico(entitiesAPI, ctProcedure.getString("COD_PCD"));

			omCodProcedimiento.setText(codProc);
			OMElement nombreProcedimiento = fac.createOMElement("NombreProcedimiento", omNs);
			String nomProc = ctProcedure.getString("NOMBRE");
			nombreProcedimiento.setText(nomProc);
			procedimiento.addChild(omCodProcedimiento);
			procedimiento.addChild(nombreProcedimiento);
	
			// Identificador del expediente
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
	
			// Estos datos los tiene que meter el funcionario
			OMElement titular = fac.createOMElement("Titular", omNs);
			OMElement tipoDocumentacion = fac.createOMElement("TipoDocumentacion", omNs);
	
			OMElement documentacion = fac.createOMElement("Documentacion", omNs);
			
			// Compruebo si es NIF o CIF
			if (codProcedimiento.equals("Q2827003ATGSS001")) {
				String caracterPrimero = ndocPartic.substring(0, 1);
	
				if (isNumeric(caracterPrimero)) {
					tipoDocumentacion.setText("NIF");
				} else {
					if (caracterPrimero.equals("X") || caracterPrimero.equals("Y") || caracterPrimero.equals("Z")) {
						tipoDocumentacion.setText("NIE");
					} else {
						tipoDocumentacion.setText("CIF");
					}
				}
			} else {
				tipoDocumentacion.setText("NIF");
			}
	
			documentacion.setText(ndocPartic);
	
			titular.addChild(tipoDocumentacion);
			titular.addChild(documentacion);
	
			if (codProcedimiento.equals("AEAT101I") || codProcedimiento.equals("AEAT102I") || codProcedimiento.equals("AEAT103I")
					|| codProcedimiento.equals("AEAT104I")) {
				OMElement nombreCompleto = fac.createOMElement("NombreCompleto", omNs);
				nombreCompleto.setText(sNombreCompleto);
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
			// Busco la url del recubrimiento
	
			
	
			// String url = "http://10.12.200.206:8081/scsp-ws/ws";
			// String url = "http://10.12.200.200:8081/scsp-ws/ws";
			// String url = "https://10.253.114.137/scsp-ws/ws/";
			Element element = null;

		
			Options opts = new Options();
			opts.setTimeOutInMilliSeconds(300000);
			opts.setTransportInProtocol(Constants.TRANSPORT_HTTP);
			opts.setTo(new EndpointReference(url));
			opts.setAction("peticionSincrona");

			ServiceClient client = new ServiceClient();
			client.setOptions(opts);
			OMElement res = client.sendReceive(peticionSincrona);		
			LOGGER.warn("----------------------------------"+res.toString());

			//resultadoServicio = XMLUtils.toDOM(res);
			resultadoServicio = toDOM(res);
			
			String literalError = getTextNode(resultadoServicio, "LiteralError");
			
			client.cleanup();
			client.cleanupTransport();


			// if(literalError.equals("TRAMITADA")){
			if (!literalError.equals("USUARIO NO AUTORIZADO")) {

				/**
				 * 
				 * PETICIÓN DEL PDF
				 * 
				 **/
				OMFactory facPDF = OMAbstractFactory.getOMFactory();
				OMNamespace omNsPDF = facPDF.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion",
						"");
				OMElement peticionPdf = facPDF.createOMElement("PeticionPdf", omNsPDF);

				String sIdPeticion = getTextNode(resultadoServicio, "IdPeticion");

				// Actualizar la tabla core_transmision con un servicio web
				// urlSCSP = ServiciosWebSVDFunciones.getDireccionSCSPSW();

				// ya lo almacena la aplicacion automaticamente
				// ScspProxy clienteScsp = new ScspProxy(urlSCSP);
				// clienteScsp.updateCoreTransmision(sIdPeticion,
				// sNombreDepartamento, codProc, nomProc, numexp);

				//
				OMElement idPeticion = fac.createOMElement("IdPeticion", omNsPDF);
				idPeticion.setText(sIdPeticion);

				// String sIdTransmision = devuelveValor(element,
				// "IdTransmision");
				String sIdTransmision = getTextNode(resultadoServicio, "IdTransmision");
				OMElement idTransmision = fac.createOMElement("IdTransmision", omNsPDF);
				idTransmision.setText(sIdTransmision);

				peticionPdf.addChild(idPeticion);
				peticionPdf.addChild(idTransmision);

				ServiceClient clientPDF = new ServiceClient();
				Options optsPDF = new Options();
				optsPDF.setTimeOutInMilliSeconds(300000);

				// options.setTransportInProtocol(Constants.TRANSPORT_HTTP);
				optsPDF.setTo(new EndpointReference(url));
				optsPDF.setAction("PeticionPdf");
				clientPDF.setOptions(optsPDF);

				OMElement resPDF = clientPDF.sendReceive(peticionPdf);
				Element respuestaPdf = toDOM(resPDF);				

				StringBuffer sbRespuestaPdf = new StringBuffer(getTextNode(respuestaPdf, DocumentosUtil.Extensiones.PDF));
				byte decoded[] = Base64.decodeBase64(sbRespuestaPdf.toString());
				
				String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() +  "/" + FileTemporaryManager.getInstance().newFileName("."+ DocumentosUtil.Extensiones.PDF);
				FileOutputStream  fos = null;
				try {
					fos = new FileOutputStream(rutaFileName);
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
				fos.write(decoded);
				fos.close();
				fos.flush();
				
				File fileDoc = new File(rutaFileName);		
				
				int documentId = DocumentosUtil.getIdTipoDocByCodigo(cct, "serv-ver");
				DocumentosUtil.generaYAnexaDocumento(rulectx, documentId, ndocPartic +" - "+ sNombreCompleto + " - "+ nombreProc, fileDoc, DocumentosUtil.Extensiones.PDF);
				
												
				if(fileDoc != null && fileDoc.exists()) fileDoc.delete();
				
				clientPDF.cleanup();
				clientPDF.cleanupTransport();


			} else {

				ISPACException info = new ISPACException(literalError, true);
				rulectx.setInfoMessage("infoAlert -"+ info);
			}

		} catch (AxisFault e) {
			LOGGER.error("Error al conectar con la url. " + url + " en el numExp "+rulectx.getNumExp()+" - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al conectar con la url. " + url + " en el numExp "+rulectx.getNumExp()+" - " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error al conectar con la url. " + url + " en el numExp "+rulectx.getNumExp()+" - " + e.getMessage(), e);
			throw new ISPACRuleException("Error al conectar con la url. " + url + " en el numExp "+rulectx.getNumExp()+" - " + e.getMessage(), e);
		}
		
		return resultadoServicio;
		
	}
	
	private static boolean isNumeric(String cadena) {
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
	
	public static org.w3c.dom.Element toDOM(OMElement element) throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        element.serialize(baos);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder().parse(bais).getDocumentElement();
    }

	/**
	 * @param elemento Objeto que representa un documento XML.
	 * @param nodeName Nombre del elemento del que se quiere buscar el valor asociado.
	 * @return Texto que corresponde al contenido del elemento buscado
	 * @throws Exception
	 */
	public static String getTextNode(Element elemento, String nodeName) throws Exception {
		NodeList listaHijos = elemento.getElementsByTagName(nodeName);
		Node node = listaHijos.item(0).getChildNodes().item(0);
		if (node==null) return null;
		else return node.getNodeValue();
	}
	
	/**
	 * @param nodo Cadena de texto que identifica al nodo a buscar.
	 * @param element Objeto que representa el documento XML.
	 * @return Lista de objetos que contienen los elementos que coinciden con el nodo buscado.
	 * @throws Exception
	 */
	public static List<Element> evaluateXPathAsList(String nodo, Element element) throws Exception {
		XPathExpression exp = null;
		try {
			
			//Crea una nueva instancia para retornar un objeto de tipo XPath usando el modelo de objeto W3C DOM.
			XPath xpath = XPathFactory.newInstance().newXPath();

			//Compila la expresión XPath pasada por parámetro para una posterior evaluación.
			exp = xpath.compile("//*[local-name()='" + nodo + "']");

			//Evalua la expresión XPath compilada sobre el elemento <element> pasado por parámetro y esperando un objeto de tipo Node.
			Object o = exp.evaluate(element, XPathConstants.NODESET);
			
			NodeList nodes = (NodeList) o;
			//Recorre cada uno de los elemento del nodo
			List<Element> result = new ArrayList<Element>();
			for (int i = 0; i < nodes.getLength(); i++) {
				result.add((Element) nodes.item(i));	//Se añaden al elemento que será devuelto.
				}
			return result;
			
		} catch (Exception e) {
			String msg = "Se produjo un error al evaluar la expresion de peticion '" + exp + "'.";
			throw new Exception(e);
		}
	}
	
	/**
	 * @param element
	 * @param strExp
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String evaluateString(Element element, String strExp) throws XPathExpressionException {
		//Crea una nueva instancia para retornar un objeto de tipo XPath usando el modelo de objeto W3C DOM.
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new PersonalNamespaceContext("ns1","http://intermediacion.redsara.es/scsp/esquemas/datosespecificos"));
		
		//Compila la expresión XPath pasada por parámetro para una posterior evaluación.
		XPathExpression exp = xpath.compile(strExp);

		//Evalua la expresión XPath compilada sobre el elemento <element> pasado por parámetro y esperando un objeto de tipo String.
		String result = (String) exp.evaluate(element, XPathConstants.STRING);
		return result;
	}

}
