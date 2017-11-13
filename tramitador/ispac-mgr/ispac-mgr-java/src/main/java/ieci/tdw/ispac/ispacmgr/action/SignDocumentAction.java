package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitFilter;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.SignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;
import ieci.tdw.ispac.ispacweb.util.DocumentUtil;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class SignDocumentAction extends BaseDispatchAction {
	
	private final static String ITEM_SEPARATOR = ":";
	private final static String DATASOURCE_WEBSERVICE = "webservice";
	private final static String EXTRAPARAM_DOC_CODIGO_ENTIDAD = "codent=";
	private final static String EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER = "guid=";
	private final static String EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE = "datasource=";
	

	private IState enterState(HttpServletRequest request, SessionAPI session, HttpServletResponse response) throws ISPACException{
		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
		// Se cambia el estado de tramitación
		Map params = request.getParameterMap();
		//Al estar en un 'DispacthAction', cambiamos de estado si no nos encontramos ya en el estado de SIGNDOCUMENT  
		IState state = managerAPI.currentState(getStateticket(request));
		if (state.getState() != ManagerState.SIGNDOCUMENT)
			state = managerAPI.enterState(getStateticket(request),ManagerState.SIGNDOCUMENT, params);
		
		storeStateticket(state, response);
		return state;
	}
	
	public ActionForward selectOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		//Activamos nuevo estado
		enterState(request, session, response);		
		return mapping.findForward("selectOption");
	}
	
	//===================
	//Firmar ahora

	public ActionForward initSign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		
		//INICIO [eCenpri-Felipe #818]
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String entityId = info.getOrganizationId();
		FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
		
		String sDeshabilitarFirma = fc.getProperty("firmar.deshabilitar");
		boolean bDeshabilitarFirma = false;
		if (StringUtils.isNotEmpty(sDeshabilitarFirma)){
			bDeshabilitarFirma = Boolean.valueOf(sDeshabilitarFirma).booleanValue();
		}
		if (bDeshabilitarFirma){

			String titulo = fc.getProperty("firmar.titulo");
			String texto = fc.getProperty("firmar.texto");
			request.setAttribute("firmar.titulo", titulo);
			request.setAttribute("firmar.texto", texto);
			return mapping.findForward("nofirmar");
		}
		//FIN [eCenpri-Felipe #818]
		 
		
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IState state = enterState(request, session, response);
		
		//INICIO [eCenpri-Agustin #871] FIRMA 3 FASES
		ClientContext cct = session.getClientContext();
		String nombreFirmante = UsuariosUtil.getNombreFirma(cct);
		String docIdentidad = UsuariosUtil.getDni(cct);
		String numeroSerie = UsuariosUtil.getNumeroSerie(cct);
		InfoFirmante infoFirmante = new InfoFirmante();
		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(numeroSerie);
		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(docIdentidad);
		infoFirmante.setNombreFirmante(nombreFirmante);
		if (StringUtils.isEmpty(nombreFirmante) || StringUtils.isEmpty(numeroSerie)) {
					
			//TODO: Coger los textos de un resources
			request.setAttribute("firmar.titulo", "Error al realizar la firma");
			request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
			"El usuario con el que está accediendo no tiene configurada la firma en SIGEM.<br/>" +
			"Por favor, consulte con el departamento de Informática para actualizar sus datos de firma." +
			"<br/><br/>Gracias y un saludo.");
			return mapping.findForward("nofirmar");
		}
				
		SignForm signForm = ((SignForm) form);
				
		//Código de entidad
		String codEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
				
		//Referencia del documento
		IItem itemDoc = entitiesAPI.getDocument(state.getEntityRegId());
		SignDocument signDocument = new SignDocument(itemDoc);
		signDocument.setEntityId(codEntidad); //2.0
		signDocument.addFirmante(infoFirmante);
			
		//PRE-FIRMA
		String docRef = signAPI.presign(signDocument, true);
		logger.info("Prefirma realizada, el iddocumento temporal es: "+docRef);
		
		HashMap<String, String> infoFirmas = new HashMap<String, String>();
		String idDoc = String.valueOf(signDocument.getItemDoc().getKeyInt());
		logger.info("El SignDocument iddocumento es: "+idDoc);	
		infoFirmas.put(docRef, idDoc);
		
		//Construimos el xml en caso que finalmente lo incorporemos, finalmente el xml de metadata ira vinculado pero no en la firma del documento
		//Este parametro ya no se usa lo dejo aqui por si hiciera falta en un futuro
		String xml = "<xml>FIRMA ELECTRONICA EN UNIVERSIDAD PUBLICA DE NAVARRA</xml>";
			
		//Añadimos los datos del documento
		StringBuffer sbDatosDoc = new StringBuffer();
		sbDatosDoc.append(docRef);			
		sbDatosDoc.append(ITEM_SEPARATOR);
		sbDatosDoc.append(codEntidad);
		sbDatosDoc.append(ITEM_SEPARATOR);
		sbDatosDoc.append(xml);
				
		//Seteamos los parametros en la firma tres fases
		signForm.setSerialNumber(numeroSerie);
		signForm.setListaDocs(sbDatosDoc.toString());
		signForm.setCodEntidad(codEntidad);
		signForm.setInfoFirma(infoFirmas);
		signForm.setDocumentId(idDoc);
		
		//INICIO [eCenpri-Agustin #94] NUEVO SERVIDOR DE FIRMA 3 FASES POR LOTES 
				
		byte[] extraparamsBase64 = null;
		byte[] configSignSaberBase64 = null;
		
		// URL del documento			
		//TODO tengo un problema con los ficheros temporales
		//En la anterior version del servidor de firma 3 fases obtenia desde el servidor los ficheros a firmar mediante webservice
		//Esta version obliga a que se recuperen por url, como son ficheros temporales no estan en el repositorio de sigem
		//He tocado la clase SingleSign del servidor, si le paso la palabra webservice en el datasource como url llama a mi webservice
		//La otra opcion que tengo en mente es implementar un servlet en sigem que devuelva los ficheros temporales
		String url_ = DocumentUtil.generateURL(request, "download", session.getTicket(),	idDoc, null);
		String url = DATASOURCE_WEBSERVICE;
						
		String firmar_lotes_algorithm = fc.getProperty("firmar.lotes.algorithm");
		String firmar_lotes_format = fc.getProperty("firmar.lotes.format");
		String firmar_lotes_stoponerror = fc.getProperty("firmar.lotes.stoponerror");
		String firmar_lotes_suboperation = fc.getProperty("firmar.lotes.suboperation");
		String firmar_lotes_concurrenttimeout = fc.getProperty("firmar.lotes.concurrenttimeout");
		String firmar_lotes_signsaver = fc.getProperty("firmar.lotes.signsaver");
		
		///////////// AJM Importante paso las referencias del documento al servidor de firma por los extraparams
		
		//Hay que convertir a Base64 extraparams y parametros de signsaver
		//Los extraparam deben ir en distintas lineas con el formato nombre_parametro=valor
		String firmar_lotes_extraparams = fc.getProperty("firmar.lotes.extraparams");
		String firmar_lotes_signsaver_config_path = fc.getProperty("firmar.lotes.signsaver.config");
		firmar_lotes_extraparams = firmar_lotes_extraparams.concat("\n");
		
		String firmar_lotes_extraparams_sigem = "";		
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE);
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(firmar_lotes_signsaver_config_path.substring(firmar_lotes_signsaver_config_path.indexOf("=")+1));				
	
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_CODIGO_ENTIDAD);
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(codEntidad);
		
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER);
		firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(docRef);
		
			
		firmar_lotes_extraparams = firmar_lotes_extraparams.concat(firmar_lotes_extraparams_sigem);		
		
		extraparamsBase64 = firmar_lotes_extraparams.getBytes();		
		configSignSaberBase64 = firmar_lotes_signsaver_config_path.getBytes();
		
		//Pruebas  con SignSaverFile al indicar a pelo la ruta donde guarda el fichero resultado el servidor
		//configSignSaberBase64 = org.apache.commons.codec.binary.Base64.decodeBase64(firmar_lotes_signsaver_config_path.getBytes());		
		//String example = "RmlsZU5hbWU9XC90bXBcL2Zpcm1hXC9zaWduYmF0Y2hcL2FndXN0aW5fY29uX2F1dG9maXJtYS5wZGY=";
		//configSignSaberBase64 = org.apache.commons.codec.binary.Base64.decodeBase64(example .getBytes());		  	
				
		///////////// AJM CONSTRUIR XML FIRMA LOTES
		
		Signbatch signbatch;
		Signbatch.Singlesign singlesign;
		Signbatch.Singlesign.Signsaver signsaver;
		ObjectFactoryFirmaLotesPeticion peticion = new ObjectFactoryFirmaLotesPeticion();		
		
		BigInteger concurrenttimeout = new BigInteger(firmar_lotes_concurrenttimeout);				
				
		//Objeto raiz signbatch
		signbatch = peticion.createSignbatch();
		signbatch.setAlgorithm(firmar_lotes_algorithm);
		signbatch.setStoponerror(firmar_lotes_stoponerror);
		signbatch.setConcurrenttimeout(concurrenttimeout);
			
		//Objeto signSaver 		
		signsaver =  peticion.createSignbatchSinglesignSignsaver();
		signsaver.setClazz(firmar_lotes_signsaver);
		signsaver.setConfig(configSignSaberBase64);
				
		singlesign = peticion.createSignbatchSinglesign();
		singlesign.setId("0");
		singlesign.setDatasource(url);
		singlesign.setFormat(firmar_lotes_format);
		singlesign.setSuboperation(firmar_lotes_suboperation);
		singlesign.setExtraparams(extraparamsBase64);
		singlesign.setSignsaver(signsaver);
				
		signbatch.getSinglesign().add(singlesign);
				
		//File file = new File("lotes_firma3.xml");		
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesPeticion.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				
		//Obtener XML en un String
		java.io.StringWriter sw = new StringWriter();
		//Indico esto por que no se cual es la funcion del atributo standalone asi que lo pongo a false para que no aparezca, asi esta en la demo de integracion del servidor de firma
		jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
		jaxbMarshaller.marshal(signbatch, sw);
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		result = result.concat(sw.toString());
		
		//FIN [eCenpri-Agustin #94] NUEVO SERVIDOR DE FIRMA 3 FASES POR LOTES 
				
		//Seteamos los parametros en la firma tres fases
		signForm.setSerialNumber(numeroSerie);
		signForm.setListaDocs(result);
		signForm.setCodEntidad(codEntidad);
		signForm.setInfoFirma(infoFirmas);
		signForm.setDocumentId(idDoc);	
		
		//FIN [eCenpri-Agustin #871] FIRMA 3 FASES
		
		return mapping.findForward("sign");
	}	
	
	/**
	 * [eCenpri-Felipe #871]
	 * Modificado método para firma en 3 fases
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward sign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		
		IState state = enterState(request, session, response);
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();
		SignForm signForm = ((SignForm) form);
		JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesPeticion.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		String infoDoc = signForm.getListaDocs();
		String documentId = signForm.getDocumentId();

		String entityId = (String)request.getSession().getAttribute("idEntidad");//2.0
		String forward = null;
		
		if (StringUtils.isEmpty(documentId))
		{			
			forward = "refresh";
		}
		else {
			//////////////////////////////////////////////
			//POST-FIRMA
			// Acceso a los objetos que llevan en memoria la informacion de la firma
			IItem itemDoc;
			SignDocument signDocument;
			
			StringReader reader = new StringReader(infoDoc);
			Signbatch signbatch = (Signbatch) jaxbUnmarshaller.unmarshal(reader);
			
			String docref = new String(signbatch.getSinglesign().get(0).getExtraparams());
			docref = docref.split(EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER)[1];
			
			String idDoc = documentId;		
			itemDoc = entitiesAPI.getDocument(Integer.valueOf(idDoc).intValue());
			signDocument = new SignDocument(itemDoc);
			
			//Llenamos las propiedades del signDocument
			// TODO: BORRAR O VERIFICAR signDocument.setEntityId(entityId);
			signDocument.setEntityId(entityId);//2.0
			signDocument.setIdPcd(state.getPcdId());
			signDocument.setNumExp(state.getNumexp());
			String hash = signAPI.generateHashCode(signDocument);
	        signDocument.setHash(hash);
	        
	        //La validacion se hara en el servidor de firma una vez firmado el documento
	        //String signCertificate = batchSignForm.getSignCertificate();
	        //signDocument.addCertificate(signCertificate);
					
			//Llamada al conector firma para preparar el documento temporal
			/*String result = */signAPI.postsign(signDocument, docref , true);
			
			//////////////////////////////////////////////
			//RESULTADO PARA EL SUMMARY
			// Establecer el estado de firma para cada documento
			List<ItemBean> ltSignsDocuments = new ArrayList<ItemBean>();
			
			// Estados de firma para incluir el sustituto en la lista enviada a la vista
			IItemCollection itemcol = entitiesAPI.queryEntities(SpacEntities.SPAC_TBL_008, "");
			Map mapSignStates = CollectionBean.getBeanMap(itemcol, "VALOR");

			// Incluir el sustituto del estado de firma
			ItemBean itemBean = new ItemBean(itemDoc);
			String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
			itemBean.setProperty("AUTOR_CERT", autorCert);
			itemBean.setProperty("SUSTITUTO_ESTADOFIRMA",
					((ItemBean) mapSignStates.get(itemDoc.getString("ESTADOFIRMA"))).getProperty("SUSTITUTO"));
			itemBean.setProperty("HASH", hash);
			ltSignsDocuments.add(itemBean);
					
			// Lista de documentos firmados para la vista
			request.setAttribute(ActionsConstants.SIGN_DOCUMENT_LIST, ltSignsDocuments);
			signForm.clean();
			
			//[DipuCR-Agustin] #150 Agregar metadatos
			GestorMetadatos.storeMetada(session, signDocument, autorCert, null);
					
		    ///////////////////////////////////////////////
		    // Formateador
			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
			//BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedformatter.xml"));
			//Inicio [Ticket #315 Teresa]
			BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedHashformatter.xml"));
			//Fin [Ticket #315 Teresa]
			request.setAttribute(ActionsConstants.FORMATTER, formatter);
			forward = "success";
					
		}
	
		return mapping.findForward(forward);
	}
	
	
	
	 public static X509Certificate getX509Certificate(HttpServletRequest request, String serialNumber)
	    {
	        String attributeName = "javax.servlet.request.X509Certificate";
	        Object obj = request.getAttribute(attributeName);
	        X509Certificate certificate = null;
	        if(obj instanceof X509Certificate[])
	        {
	            X509Certificate certArray[] = (X509Certificate[])obj;
	            for(int i = 0; i < certArray.length; i++)
	                if(certArray[i].getSerialNumber().toString().equals(serialNumber))
	                    return certArray[i];

	            return null;
	        }
	        if(obj instanceof X509Certificate)
	        {
	            certificate = (X509Certificate)obj;
	            if(certificate.getSerialNumber().toString().equals(serialNumber))
	                return certificate;
	            else
	                return null;
	        } else
	        {
	            return null;
	        }
	    }
	
	//===================
	//Circuito de firmar
	public ActionForward selectSignCircuit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IState state = enterState(request, session, response);

		// Filtro obtener los circuitos de firma genéricos definidos en el sistema y los específicos del procedimiento del expediente
		SignCircuitFilter filter = new SignCircuitFilter();
		filter.setPcdId(state.getPcdId());
		filter.setTaskPcdId(state.getTaskPcdId()); //[eCenpri-Felipe #592]

		filter.setIdSistema(ProcessSignConnectorFactory.getInstance().getProcessSignConnector().getIdSystem());
		filter.setDefaultPortafirmas(ProcessSignConnectorFactory.getInstance().isDefaultConnector());

		
		// Obtenemos los circuitos para el procedimiento
		IItemCollection itemcol = signAPI.getCircuits(filter);
		List list = CollectionBean.getBeanList(itemcol);
		request.setAttribute(ActionsConstants.SIGN_CIRCUIT_LIST, list);
		
		//INICIO [eCenpri-Felipe #592] Obtenemos los circuitos del trámite
		IItemCollection itemcolTram = signAPI.getCircuitsTramite(filter);
		List listTram = CollectionBean.getBeanList(itemcolTram);
		request.setAttribute(ActionsConstants.SIGN_CIRCUITTRAM_LIST, listTram);
		//FIN [eCenpri-Felipe #592]
		
		// Establecer el action para el asistente
		request.setAttribute("action", "signDocument.do");

		return mapping.findForward("selectCircuit");
	}

	public ActionForward initSignCircuit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		ISignAPI signAPI = session.getAPI().getSignAPI();
		//Activamos nuevo estado
		IState state = enterState(request, session, response);
		//Obtenemos identificador del circuito de firma a iniciar 
		int circuitId = Integer.parseInt(request.getParameter(ActionsConstants.SIGN_CIRCUIT_ID));

		//En el estado actual 'SignDocumentState' en el campo entityRegId estara almacenado el identificador del documento a firmar
		int documentId = state.getEntityRegId();
		signAPI.initCircuit(circuitId, documentId);
		
		return mapping.findForward("success");
	}
}