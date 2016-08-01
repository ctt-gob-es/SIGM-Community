package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitFilter;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.ProcessSignProperties;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.SignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.util.DocumentUtil;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

/**
 * Action para la firma de documentos en bloque.
 * 
 * @author Iecisa
 * @version $Revision$
 * 
 */
public class SignDocumentsAction extends BaseDispatchAction {
	
	protected static final String SIGN_DOCUMENT_IDS_PARAM = "SIGN_DOCUMENT_IDS"; 
	protected static final String SIGN_DOCUMENT_HASHCODES_PARAM = "SIGN_DOCUMENT_HASHCODES";
	
	/**
	 * Variables firma lotes
	 */
	private final static String DATASOURCE_WEBSERVICE = "webservice";
	private final static String EXTRAPARAM_DOC_CODIGO_ENTIDAD = "codent=";
	private final static String EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER = "guid=";
	private final static String EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE = "datasource=";

	/**
	 * Muestra las opciones disponibles para firmar los documentos.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward defaultExecute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		// Identificadores de los documentos seleccionados
		String[] documentIds = ((SignForm) form).getMultibox();

		if (ArrayUtils.isEmpty(documentIds)) {
			logger.warn("No se ha seleccionado ningún documento");
			throw new ISPACInfo(getResources(request).getMessage(
					"forms.listdoc.firmarDocumentos.empty"), false);
		}

		// Almacenar los identificadores de los documentos para el tratamiento posterior
		storeDocumentIds(session, documentIds);
		
		// Establecer el action para el asistente
		request.setAttribute("action", "signDocuments.do");
		
		return mapping.findForward("selectOption");
	}


	/**
	 * Muestra la página para firmar los documentos seleccionados.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
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
		//INICIO [DipuCR-Agustin #94] integracion de firma 3 fases en lotes
		
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Información del estado
		getCurrentState(request, session);

		// Codigos hash de los documentos
		// [DipuCR-Agustin #94] Firma lotes
		// Comento el calculo del hash de los ficheros ya que esta labor la hacer el servidor de firma
		// En su lugar realizo el proceso de prefirma
		//List<String> hashCodes = new ArrayList<String>();
		
		// Obtener la información de los documentos seleccionados
		IItemCollection documents = entitiesAPI.queryEntities(
				SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session)))
						.append(" ORDER BY ID")//[dipucr-Felipe #184]
						.toString());

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		
		//Código de entidad
		String codEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
		
		//Informacion del firmante
		ClientContext cct = session.getClientContext();
		String nombreFirmante = UsuariosUtil.getNombreFirma(cct);
		String docIdentidad = UsuariosUtil.getDni(cct);
		String numeroSerie = UsuariosUtil.getNumeroSerie(cct);
		InfoFirmante infoFirmante = new InfoFirmante();
		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(numeroSerie);
		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(docIdentidad);
		infoFirmante.setNombreFirmante(nombreFirmante);
		//INICIO [dipucr-Felipe 3#231]
	    if (StringUtils.isEmpty(nombreFirmante) || StringUtils.isEmpty(numeroSerie)) {
			
			//TODO: Coger los textos de un resources
			request.setAttribute("firmar.titulo", "Error al realizar la firma");
			request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
			"El usuario con el que está accediendo no tiene configurada la firma en SIGEM.<br/>" +
			"Por favor, consulte con el departamento de Informática para actualizar sus datos de firma." +
			"<br/><br/>Gracias y un saludo.");
			return mapping.findForward("nofirmar");
		}
	    //FIN [dipucr-Felipe 3#231]
		
		//Lista de documentos con su id temporal
		List<String> hashCodes = new ArrayList<String>();
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		String nodos = "";
		int count = 0;
		
		String firmar_lotes_algorithm = fc.getProperty("firmar.lotes.algorithm");
		String firmar_lotes_format = fc.getProperty("firmar.lotes.format");
		String firmar_lotes_stoponerror = fc.getProperty("firmar.lotes.stoponerror");
		String firmar_lotes_suboperation = fc.getProperty("firmar.lotes.suboperation");
		String firmar_lotes_concurrenttimeout = fc.getProperty("firmar.lotes.concurrenttimeout");
		String firmar_lotes_signsaver = fc.getProperty("firmar.lotes.signsaver");
		
		Signbatch signbatch;
		ObjectFactoryFirmaLotesPeticion peticion = new ObjectFactoryFirmaLotesPeticion();			
		BigInteger concurrenttimeout = new BigInteger(firmar_lotes_concurrenttimeout);	
						
		//Objeto raiz signbatch
		signbatch = peticion.createSignbatch();
		signbatch.setAlgorithm(firmar_lotes_algorithm);
		signbatch.setStoponerror(firmar_lotes_stoponerror);
		signbatch.setConcurrenttimeout(concurrenttimeout);
		
		
		logger.info("Se van a realizar la Prefirma");
		while (iterator.hasNext()) {
			
			
			///////////// SIGUIENTE DOCUMENTO DE LA LISTA DEL PANEL DE TRAMITE
			
			//Información del documento
			IItem document = iterator.next();

			// Obtener el codigo hash del documento
			// Esta tarea pasa a realizarla en servidor de firma
			//hashCodes.add(signAPI.generateHashCode(new SignDocument(document)));
			
			//PRE-FIRMA
			// Obtengo documento y agrego entidad junto con la informacion del firmante
			SignDocument signDocument = new SignDocument(document);
			signDocument.setEntityId(codEntidad);
			signDocument.addFirmante(infoFirmante);
			String idDoc = String.valueOf(signDocument.getItemDoc().getKeyInt());
			
			//Genero documento temporal
			String docRef = signAPI.presign(signDocument, true);
			logger.info("Prefirma realizada, el iddocumento temporal es: "+docRef);			
			
			//Agrego a la lista de documentos la referencia del documento temporal
			//Existe otra lista con las referencias los documentos originales
			hashCodes.add(docRef);
			
			// URL del documento			
			//TODO tengo un problema con los ficheros temporales
			//En la anterior version del servidor de firma 3 fases obtenia desde el servidor los ficheros a firmar mediante webservice
			//Esta version obliga a que se recuperen por url, como son ficheros temporales no estan en el repositorio de sigem
			//He tocado la clase SingleSign del servidor, si le paso la palabra webservice en el datasource como url llama a mi webservice
			//La otra opcion que tengo en mente es implementar un servlet en sigem que devuelva los ficheros temporales
			String url_ = DocumentUtil.generateURL(request, "download", session.getTicket(),	idDoc, null);
			String url = DATASOURCE_WEBSERVICE;
			
			//INICIO [eCenpri-Agustin #94] NUEVO SERVIDOR DE FIRMA 3 FASES POR LOTES 				
			byte[] extraparamsBase64 = null;
			byte[] configSignSaberBase64 = null;				
			
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
			firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(entityId);
			
			firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
			firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER);
			firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(hashCodes.get(count));
			
				
			firmar_lotes_extraparams = firmar_lotes_extraparams.concat(firmar_lotes_extraparams_sigem);		
			
			extraparamsBase64 = firmar_lotes_extraparams.getBytes();		
			configSignSaberBase64 = firmar_lotes_signsaver_config_path.getBytes();
			
			//Pruebas  con SignSaverFile al indicar a pelo la ruta donde guarda el fichero resultado el servidor
			//configSignSaberBase64 = org.apache.commons.codec.binary.Base64.decodeBase64(firmar_lotes_signsaver_config_path.getBytes());		
			//String example = "RmlsZU5hbWU9XC90bXBcL2Zpcm1hXC9zaWduYmF0Y2hcL2FndXN0aW5fY29uX2F1dG9maXJtYS5wZGY=";
			//configSignSaberBase64 = org.apache.commons.codec.binary.Base64.decodeBase64(example .getBytes());	
			
			///////////// AJM CONSTRUIR NODOS XML SINGLESIGN
			
			Signbatch.Singlesign singlesign;
			Signbatch.Singlesign.Signsaver signsaver;
							
			//Objeto signSaver 		
			signsaver =  peticion.createSignbatchSinglesignSignsaver();
			signsaver.setClazz(firmar_lotes_signsaver);
			signsaver.setConfig(configSignSaberBase64);
					
			singlesign = peticion.createSignbatchSinglesign();
			singlesign.setId(Integer.toString(count));
			singlesign.setDatasource(url);
			singlesign.setFormat(firmar_lotes_format);
			singlesign.setSuboperation(firmar_lotes_suboperation);
			singlesign.setExtraparams(extraparamsBase64);
			singlesign.setSignsaver(signsaver);
					
			signbatch.getSinglesign().add(singlesign);			
			
			count++;
		}
		
		///////////// AJM CONSTRUIR NODO XML ROOT SIGNBATCH
		
		//File file = new File("lotes_firma3.xml");		
		JAXBContext jaxbContext;
		try {
			
			jaxbContext = JAXBContext.newInstance(ObjectFactoryFirmaLotesPeticion.class);
		
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				
			//Obtener XML en un String
			java.io.StringWriter sw = new StringWriter();
			//Indico esto por que no se cual es la funcion del atributo standalone asi que lo pongo a false para que no aparezca, asi esta en la demo de integracion del servidor de firma
			jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			jaxbMarshaller.marshal(signbatch, sw);				
			result = result.concat(sw.toString());
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		// Almacenar los hash de los documentos para el tratamiento posterior
		storeDocumentHashCodes(session, hashCodes);
		
		request.setAttribute("hashCodes",hashCodes.toArray(new String[hashCodes.size()]));
		//Seteamos los parametros en la firma tres fases
		SignForm signForm = ((SignForm) form);
		signForm.setSerialNumber(numeroSerie);
		signForm.setListaDocs(result);
		signForm.setCodEntidad(codEntidad);
		
		//FIN [DipuCR-Agustin #94] integracion de firma 3 fases en lotes		
		
		return mapping.findForward("sign");
	}

	/**
	 * Firma almacena la firma de los documentos seleccionados y muestra una
	 * pantalla de resumen.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward sign(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		

		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Información del estado
		IState state = getCurrentState(request, session);

		SignForm signForm = (SignForm) form;
		//String[] signs = signForm.getSigns().split("!");
		String[] hashCodes = retrieveDocumentHashCodes(session);
		
		//TODO en principio no recojo las firmas, seguramente en el futuro no haga falta
		//Dejo este if por si luego quisiera entrarlas como metadatos
		//if (ArrayUtils.isEmpty(signs)
		//		|| (StringUtils.isBlank(signForm.getSignCertificate()) || "undefined"
		//				.equalsIgnoreCase(signForm.getSignCertificate()))) {
		//	return mapping.findForward("failure");
		//}

		List<ItemBean> documents_ok = new ArrayList<ItemBean>();
		List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();

		// Obtener la información de los documentos seleccionados
		IItemCollection documents = entitiesAPI.queryEntities(
				SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session)))
						.append(" ORDER BY ID")//[dipucr-Felipe #184]
						.toString());

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		int count = 0;
		//[DipuCR-Agustin] #150 Agregar metadatos
		String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
		
		//Se comprueba la validez del certificado
		//La validacion se hace en el servidor de firma
		//Pienso pasar por los extraparam el numero de serie del certificado
		//ResultadoValidacionCertificado resultado = signAPI.validateCertificate(signForm.getSignCertificate());
		//if (resultado.getResultadoValidacion().equals(ResultadoValidacionCertificado.VALIDACION_ERROR)){
		//	request.setAttribute("SIGN_ERROR", "Error en la validación del certificado. "+resultado.getMensajeValidacion());
		//	return mapping.findForward("success");
		//}
		
		while (iterator.hasNext()) {
			
			// Información del documento
			IItem document = iterator.next();

			try {

				// Comprobar que el documento no esté firmado o en un circuito de firma
				String estadoFirma = document.getString("ESTADOFIRMA");
				if (!StringUtils.isBlank(estadoFirma)
						&& !SignStatesConstants.SIN_FIRMA.equals(estadoFirma)) {
					throw new ISPACInfo(
							"exception.signProcess.cannotSignDocument", true);
				}

				SignDocument signDocument = new SignDocument(document);
				signDocument.setIdPcd(state.getPcdId());
				signDocument.setNumExp(state.getNumexp());
				//signDocument.addSign(signs[count]);
		        signDocument.addCertificate(signForm.getSignCertificate());
		        signDocument.setHash(hashCodes[count]);

	        	//signAPI.sign(signDocument, true);
		        signAPI.postsign(signDocument, hashCodes[count] , true);

				documents_ok.add(new ItemBean(document));				
				
				//[DipuCR-Agustin] #150 Agregar metadatos
				GestorMetadatos.storeMetada(session, signDocument, autorCert, null);

			} catch (ISPACException e) {
				logger.error(
						"Error al firmar el documento:"
								+ document.getKeyInt() + " - "
								+ document.getString("NOMBRE") + " ["
								+ document.getString("DESCRIPCION") + "]", e);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("documento", new ItemBean(document));
				map.put("error", e.getExtendedMessage(request.getLocale()));

				documents_ko.add(map);
			}
			
			count++;
		}
		
		signForm.clean();

		request.setAttribute("DOCUMENTS_OK", documents_ok);
		request.setAttribute("DOCUMENTS_KO", documents_ko);
			
		return mapping.findForward("success");
	}

	/**
	 * Muestra el listado de circuitos de firma.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward selectSignCircuit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {

		// Información del estado
		IState state = getCurrentState(request, session);

		// Filtro obtener los circuitos de firma genéricos definidos en el
		// sistema y los específicos del procedimiento del expediente
		SignCircuitFilter filter = new SignCircuitFilter();
		filter.setPcdId(state.getPcdId());
		filter.setTaskPcdId(state.getTaskPcdId()); //[eCenpri-Felipe #592]

		
		filter.setIdSistema(ProcessSignConnectorFactory.getInstance()
				.getProcessSignConnector().getIdSystem());
		filter.setDefaultPortafirmas(ProcessSignConnectorFactory.getInstance()
				.isDefaultConnector());

		// Obtenemos los circuitos de firma
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IItemCollection itemcol = signAPI.getCircuits(filter);
		request.setAttribute(ActionsConstants.SIGN_CIRCUIT_LIST,
				CollectionBean.getBeanList(itemcol));

		// Establecer el action para el asistente
		request.setAttribute("action", "signDocuments.do");
		
		//INICIO [eCenpri-Felipe #592] Obtenemos los circuitos del trámite
		IItemCollection itemcolTram = signAPI.getCircuitsTramite(filter);
		List listTram = CollectionBean.getBeanList(itemcolTram);
		request.setAttribute(ActionsConstants.SIGN_CIRCUITTRAM_LIST, listTram);
		//FIN [eCenpri-Felipe #592]

		return mapping.findForward("selectCircuit");
	}	
	
	/**
	 * Muestra el formulario para introducir la información necesaria del
	 * circuito de firmas.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward setPropertiesSignCircuit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {
		
		// Información del estado
		getCurrentState(request, session);

		// Reiniciar el formulario
		SignForm formulario = (SignForm) form;
		formulario.resetProperties();
		
		// Obtenemos identificador del circuito de firma a iniciar
		int circuitId = Integer.parseInt(request
				.getParameter(ActionsConstants.SIGN_CIRCUIT_ID));
		request.setAttribute(ActionsConstants.SIGN_CIRCUIT_ID, circuitId);
		
		// Establecer el action para el asistente
		request.setAttribute("action", "signDocuments.do");

		return mapping.findForward("showProperties");
	}

	/**
	 * Inicia los circuitos de firma de los documentos.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward initSignCircuit(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {
		
		// Obtenemos identificador del circuito de firma a iniciar
		int circuitId = Integer.parseInt(request
				.getParameter(ActionsConstants.SIGN_CIRCUIT_ID));

		// Propiedades del circuito de firmas
		SignForm formulario = (SignForm) form;
		ProcessSignProperties properties = new ProcessSignProperties(
				formulario.getSubject(), formulario.getFstart(),
				formulario.getFexpiration(), formulario.getContent(),
				formulario.getLevelOfImportance());
		
		IInvesflowAPI invesFlowAPI = session.getAPI();
		IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Guardar el estado en el ClientContext
		getCurrentState(request, session);

		List<ItemBean> documents_ok = new ArrayList<ItemBean>();
		List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();

		// Obtener la información de los documentos seleccionados
		IItemCollection documents = entitiesAPI.queryEntities(
				SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session)))
						.toString());

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		while (iterator.hasNext()) {
			
			// Información del documento
			IItem document = iterator.next();

			try {

				// Comprobar que el documento no esté firmado o en un circuito de firma
				String estadoFirma = document.getString("ESTADOFIRMA");
				if (!StringUtils.isBlank(estadoFirma)
						&& !SignStatesConstants.SIN_FIRMA.equals(estadoFirma)) {
					throw new ISPACInfo(
							"exception.signProcess.cannotSignDocument", true);
				}
				
				// Iniciar el circuito de firmas
				signAPI.initCircuitPortafirmas(circuitId, document.getKeyInt(), properties);

				documents_ok.add(new ItemBean(document));

			} catch (ISPACException e) {
				logger.error(
						"Error al iniciar el circuito de firmas para el documento:"
								+ document.getKeyInt() + " - "
								+ document.getString("NOMBRE") + " ["
								+ document.getString("DESCRIPCION") + "]", e);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("documento", new ItemBean(document));
				map.put("error", e.getExtendedMessage(request.getLocale()));

				documents_ko.add(map);
			}
		}

		request.setAttribute("CIRCUIT_DOCUMENTS_OK", documents_ok);
		request.setAttribute("CIRCUIT_DOCUMENTS_KO", documents_ko);

		return mapping.findForward("success");
	}

	private IState getCurrentState(HttpServletRequest request, SessionAPI session) throws ISPACException {
		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
		return managerAPI.currentState(getStateticket(request));
	}
	
	private void storeDocumentIds(SessionAPI session, String[] documentIds) throws ISPACException {
		
		// Almacenar los identificadores de los documentos para el tratamiento posterior
		ClientContext ctx = session.getClientContext();
		ctx.setSsVariable(SIGN_DOCUMENT_IDS_PARAM,
				StringUtils.join(documentIds, ","));
	}
	
	private String[] retrieveDocumentIds(SessionAPI session) throws ISPACException {

		// Recuperar los identificadores de los documentos seleccionados
		ClientContext ctx = session.getClientContext();
		String documentIdsString = ctx.getSsVariable(SIGN_DOCUMENT_IDS_PARAM);
		
		return StringUtils.split(documentIdsString, ",");
	}

	private void storeDocumentHashCodes(SessionAPI session, List<String> hashCodes) throws ISPACException {
		
		// Almacenar los hash de los documentos para el tratamiento posterior
		ClientContext ctx = session.getClientContext();
		ctx.setSsVariable(SIGN_DOCUMENT_HASHCODES_PARAM,
				StringUtils.join(hashCodes, ","));
	}
	
	private String[] retrieveDocumentHashCodes(SessionAPI session) throws ISPACException {

		// Recuperar los identificadores de los documentos seleccionados
		ClientContext ctx = session.getClientContext();
		String hashCodesString = ctx.getSsVariable(SIGN_DOCUMENT_HASHCODES_PARAM);
		
		return StringUtils.split(hashCodesString, ",");
	}

}