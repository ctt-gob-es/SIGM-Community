package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.BeanFormatter;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.afirma.TelefonicaSignConnector;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.form.BatchSignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacmgr.menus.MenuFactory;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;
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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.GestorDecretos;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

public class BatchSignAction extends BaseDispatchAction {
	
	private final static String DOC_SEPARATOR = "&&";
	private final static String ITEM_SEPARATOR = ":";
	
	/**
	 * Variables firma lotes
	 */
	private final static String DATASOURCE_WEBSERVICE = "webservice";
	private final static String EXTRAPARAM_DOC_CODIGO_ENTIDAD = "codent=";
	private final static String EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER = "guid=";
	private final static String EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE = "datasource=";
	
	protected static final Logger logger = Logger.getLogger(BatchSignAction.class);	

	private IState enterState(HttpServletRequest request, 
							  SessionAPI session, 
							  HttpServletResponse response) throws ISPACException {
		
		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
		// Se cambia el estado de tramitación
		Map params = request.getParameterMap();
		//Al estar en un 'DispacthAction', cambiamos de estado si no nos encontramos ya en el estado de BATCHSIGNDOCUMENT  
		IState state = managerAPI.currentState(getStateticket(request));
		if (state.getState() != ManagerState.BATCHSIGNLIST)
			state = managerAPI.enterState(getStateticket(request),ManagerState.BATCHSIGNLIST, params);
		storeStateticket(state, response);
		
		return state;
	}
	
	private void setMenu(ClientContext cct, 
						 IState state, 
						 HttpServletRequest request) throws ISPACException {
		
		request.setAttribute("menus", MenuFactory.getSignedDocumentListMenu(cct, state, getResources(request)));		
	}
	
	/**
	 * Método 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward calculateDocumentsHash(ActionMapping mapping, 
												ActionForm form, 
												HttpServletRequest request, 
												HttpServletResponse response, 
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
		//[eCenpri-Agustin #781]
		//Activamos nuevo estado
		IState state = enterState(request, session, response);
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();
		HashMap infoFirmas = new HashMap();
		
		BatchSignForm batchSignForm = ((BatchSignForm)form);
		//en 'ids' se recogen los identificados de los pasos de circuitos de firma instanciados que se han seleccionado
		String[] ids = batchSignForm.getMultibox();
		
		StringBuffer sbListaDocs = new StringBuffer();		
		String codEntidad = getCodEntidad();
		String docRef = null; //infopag o infopag_rde
		String xml = null;
		
		//AJM Obtenemos el nombre del firmante
		ClientContext cct = (ClientContext) session.getClientContext();
		String nombreFirmante = UsuariosUtil.getNombreFirma(cct);
		String docIdentidad = UsuariosUtil.getDni(cct);
		String numeroSerie = UsuariosUtil.getNumeroSerie(cct);
		InfoFirmante infoFirmante = new InfoFirmante();		
		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(docIdentidad);
		infoFirmante.setNombreFirmante(nombreFirmante);
		
		//Comprobar que el nombre firmante y numero de serie del certificado no son vacios 
		if (StringUtils.isEmpty(nombreFirmante) || StringUtils.isEmpty(numeroSerie))
		{	
			//TODO: Coger los textos de un resources
			request.setAttribute("firmar.titulo", "Error al realizar la firma");
			request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
					"El usuario con el que está accediendo no tiene configurada la firma en SIGEM.<br/>" +
					"Por favor, consulte con el departamento de Informática para actualizar sus datos de firma." +
					"<br/><br/>Gracias y un saludo.");
			return mapping.findForward("nofirmar");
		}
		
		// Se calcula el hash de los documentos a firmar
		IItem itemDoc;
		IItem itemStep;
		SignDocument signDocument;
		
		// Comprobamos si nos ha llegado algún decreto en la firma del presidente
		boolean bBloquearFirmaDecretos = false;
		SignDocument [] arrSignDocument = new SignDocument[ids.length];
		String [] arrNumDecretos = new String[ids.length]; 
		String sNumDecreto = null;
		
		//[dipucr-Felipe 3#243]
		List<Integer> listIdDocsRealesFirma = getIdDocsRealesFirma(cct, signAPI);
		
		// Vemos si hay algún documento que ya esté firmado y se muestre por un evento atrás del navegador [dipucr-Felipe 3#242]
		// Vemos si hay algún decreto
		for (int i = 0; i < ids.length; i++) {
			
			itemStep = signAPI.getStepInstancedCircuit(Integer.parseInt((String) ids[i]));
			int idDoc = itemStep.getInt("ID_DOCUMENTO");
			
			//INICIO [dipucr-Felipe 3#243]
			//Cuando se usa el botón atrás del navegador, evitar que se firme dos veces el mismo documento
			if (!listIdDocsRealesFirma.contains(idDoc)){
				request.setAttribute("firmar.titulo", "Error al realizar la firma de documentos");
				request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
						"Está intentando firmar documentos que ya había firmado previamente.<br/>" +
						"Pulse \"Inicio\" y vuelva a cargar la pantalla de documentos a firmar, o pulse F5 para actualizar.<br/>" +
						"Por favor, en lo sucesivo y para evitar este error, no utilice la fecha \"Atrás\" de su navegador cuando use Sigem." +
						"<br/><br/>Gracias y un saludo.");
				return mapping.findForward("nofirmar");
			}
			//FIN [dipucr-Felipe 3#243]
			
			itemDoc = entitiesAPI.getDocument(idDoc);
			signDocument = new SignDocument(itemDoc);
			arrSignDocument[i] = signDocument;
			
			// Comprobamos si es decreto, si estamos en la firma presidente o fedatario
			if (TelefonicaSignConnector.isDocDecreto(signDocument)){
				
				//Primer tramite=0, Presidente=1, Fedatario=2
				int tipoFirma = GestorDecretos.comprobarFirmaDecretos(cct, idDoc);
				
				if (tipoFirma != GestorDecretos.TIPO_FIRMA_NORMAL){
				
					if (tipoFirma == GestorDecretos.TIPO_FIRMA_PRESIDENTE){
						
						//Vemos si hemos bloqueado la firma y hemos actualizado las secuencias
						//Si no lo hemos hecho (!bHayDecretosPresidente), si bloquea la firma y se actualizamos las secuencias
						if (!bBloquearFirmaDecretos){
							bBloquearFirmaDecretos = true;
							
							if (GestorDecretos.puedeFirmar(cct)){
								// Bloqueamos la firma
								GestorDecretos.bloquearFirmaDecretos(cct);
								
								// Seteamos el valor de las secuencias
								// TODO: la secuencia real ya no es necesaria
								int ultimoNumDecreto = GestorDecretos.getUltimoNumDecreto(cct);
								GestorDecretos.actualizarSecuenciaAuxiliar(cct, ultimoNumDecreto);
								GestorDecretos.actualizarSecuenciaReal(cct, ultimoNumDecreto);
							}
							else{
								//TODO: Coger los textos de un resources
								request.setAttribute("firmar.titulo", "Error al realizar la firma de Decretos");
								request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
										"La firma de decretos está bloqueada actualmente por otro usuario.<br/>" +
										"Por favor, espere unos minutos y vuelva a recargar la página.<br/><br/>Gracias y un saludo.");
								return mapping.findForward("nofirmar");
							}
						}
						arrNumDecretos[i] = GestorDecretos.getNumDecreto(cct, false);
					}
					else if (tipoFirma == GestorDecretos.TIPO_FIRMA_FEDATARIO){
						arrNumDecretos[i] = GestorDecretos.CLAVE_FIRMA_FEDATARIO;
					}
				}
			}
		}
		
		List<String> hashCodes = new ArrayList<String>();		
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		
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
		
		//PRE-FIRMA DE LOS DOCUMENTOS
		for (int i = 0; i < arrSignDocument.length; i++) {
			
			signDocument = arrSignDocument[i];
			signDocument.addFirmante(infoFirmante);
			signDocument.setEntityId(codEntidad);//2.0
			sNumDecreto = arrNumDecretos[i];
			signDocument.setNumDecreto(sNumDecreto);
			
			//Llamada al conector firma para preparar el documento temporal
			docRef = signAPI.presign(signDocument, true);
			logger.info("Prefirma realizada, el iddocumento temporal es: "+docRef);	
			infoFirmas.put(docRef,ids[i]);
			
			//Rellenamos la cadena de lista de documentos
			if (i > 0){
				sbListaDocs.append(DOC_SEPARATOR);
			}
			
			//TODO: Construimos el xml en caso que finalmente lo incorporemos
			xml = "ID="+i;
			
			//Añadimos los parametros necesarios en la firma tres fases
			sbListaDocs.append(docRef);			
			sbListaDocs.append(ITEM_SEPARATOR);
			sbListaDocs.append(codEntidad);
			sbListaDocs.append(ITEM_SEPARATOR);
			sbListaDocs.append(xml);
			
			if (StringUtils.isNotEmpty(sNumDecreto)){
				sbListaDocs.append(ITEM_SEPARATOR);
				sbListaDocs.append(sNumDecreto);
			}
			
			//Agrego a la lista de documentos la referencia del documento temporal
			//Existe otra lista con las referencias los documentos originales
			hashCodes.add(docRef);
			
			// URL del documento			
			//TODO tengo un problema con los ficheros temporales
			//En la anterior version del servidor de firma 3 fases obtenia desde el servidor los ficheros a firmar mediante webservice
			//Esta version obliga a que se recuperen por url, como son ficheros temporales no estan en el repositorio de sigem
			//He tocado la clase SingleSign del servidor, si le paso la palabra webservice en el datasource como url llama a mi webservice
			//La otra opcion que tengo en mente es implementar un servlet en sigem que devuelva los ficheros temporales
			String url_ = DocumentUtil.generateURL(request, "download", session.getTicket(), String.valueOf(signDocument.getItemDoc().getKeyInt()), null);
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
			firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(docRef);
			
				
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
			singlesign.setId(Integer.toString(i));
			singlesign.setDatasource(url);
			singlesign.setFormat(firmar_lotes_format);
			singlesign.setSuboperation(firmar_lotes_suboperation);
			singlesign.setExtraparams(extraparamsBase64);
			singlesign.setSignsaver(signsaver);
					
			signbatch.getSinglesign().add(singlesign);	
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
		
		//Seteamos los parametros en la firma tres fases
		batchSignForm.setSerialNumber(numeroSerie);
		batchSignForm.setListaDocs(result);
		batchSignForm.setCodEntidad(sbListaDocs.toString());
		batchSignForm.setInfoFirma(infoFirmas);		

		setMenu(session.getClientContext(), state, request);		

		return mapping.findForward("batchSign");
	
		//FIN [eCenpri-Agustin #781]
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	public ActionForward batchSign(ActionMapping mapping, 
								   ActionForm form, 
								   HttpServletRequest request, 
								   HttpServletResponse response, 
								   SessionAPI session) throws Exception {
		
		//INICIO [dipucr-Felipe #1116]
		String[] arrNumDecretos = null;
		IItem[] arrItemDoc = null;
		HashMap[] arrHashDatosFirmaBefore = null;
		//FIN [dipucr-Felipe #1116]
		
		try{
			//AJM Obtenemos la sesion
			IState state = enterState(request, session, response);
			IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
			ISignAPI signAPI = session.getAPI().getSignAPI();
			
			BatchSignForm batchSignForm = ((BatchSignForm) form);
			HashMap infoFirmas = batchSignForm.getInfoFirma();	
			String listaDocs = batchSignForm.getCodEntidad();
			String[] infoDocs = listaDocs.split(DOC_SEPARATOR);
	
			// Acceso a los objetos que llevan en memoria la informacion de la firma
			IItem itemStep;
			SignDocument signDocument;
			
			// Comprobamos si nos ha llegado algún decreto en la firma del presidente
			String sNumDecreto = null;
			int numDecreto = Integer.MIN_VALUE;
			boolean bHayDecretosPresidente = false;
			IClientContext cct = session.getClientContext();
			String [] arrDocRefs = new String[infoDocs.length];
			arrNumDecretos = new String[infoDocs.length];
			arrItemDoc = new IItem[infoDocs.length];
			arrHashDatosFirmaBefore = new HashMap[infoDocs.length];
			String [] datosDoc = null;
			
			//Comprobamos que haya algún decreto
			for (int i = 0; i < infoDocs.length; i++) 
			{		
				datosDoc = infoDocs[i].split(ITEM_SEPARATOR);
				arrDocRefs[i] = datosDoc[0];
				
				if (datosDoc.length > 3){
					sNumDecreto = datosDoc[3];
					arrNumDecretos[i] = sNumDecreto;
					if (StringUtils.isNotEmpty(sNumDecreto) && 
						!sNumDecreto.equals(GestorDecretos.CLAVE_FIRMA_FEDATARIO))
					{
						bHayDecretosPresidente = true;
					}
				}
			}
			
			// Si se va a firmar algún decreto, cogemos el flag y actualizamos las secuencias
			if (bHayDecretosPresidente){
				if (GestorDecretos.puedeFirmar(cct)){
					// Bloqueamos la firma
					GestorDecretos.bloquearFirmaDecretos(cct);
				}
				else{
					//TODO: Coger los textos de un resources
					request.setAttribute("firmar.titulo", "Error al realizar la firma de Decretos");
					request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
							"La firma de decretos está bloqueada actualmente por otro usuario.<br/>" +
							"Por favor, espere unos minutos y vuelva a recargar la página.<br/><br/>Gracias y un saludo.");
					return mapping.findForward("nofirmar");
	//				throw new ISPACException("En estos momentos no se pueden firmar decretos. Espere unos minutos.");
				}
			}
			
			//[dipucr-Felipe 3#243]
			List<Integer> listIdDocsRealesFirma = getIdDocsRealesFirma((ClientContext)cct, signAPI);
			
			//Realizar la actualizacion en base de datos de las firmas una vez realizada/as la/as firma/as 3 fases
			for (int i = 0; i < arrDocRefs.length; i++) 
			{
				String docref = arrDocRefs[i];
				String id = (String)infoFirmas.get(docref);
				
				itemStep = signAPI.getStepInstancedCircuit(Integer.parseInt(id));
				int idDoc = itemStep.getInt("ID_DOCUMENTO");
				IItem itemDoc = entitiesAPI.getDocument(idDoc);
				
				//INICIO [dipucr-Felipe 3#243]
				//Cuando se usa el botón atrás del navegador, evitar que se firme dos veces el mismo documento
				if (!listIdDocsRealesFirma.contains(idDoc)){
					request.setAttribute("firmar.titulo", "Error al realizar la firma de documentos");
					request.setAttribute("firmar.texto", "NO SE HA PODIDO REALIZAR LA FIRMA.<br/><br/>" +
							"Está intentando firmar documentos que ya había firmado previamente.<br/>" +
							"Pulse \"Inicio\" y vuelva a cargar la pantalla de documentos a firmar, o pulse F5 para actualizar.<br/>" +
							"Por favor, en lo sucesivo y para evitar este error, no utilice la fecha \"Atrás\" de su navegador cuando use Sigem." +
							"<br/><br/>Gracias y un saludo.");
					return mapping.findForward("nofirmar");
				}
				//FIN [dipucr-Felipe 3#243]
				
				//INICIO [dipucr-Felipe #1116]
				arrItemDoc[i] = itemDoc;
				arrHashDatosFirmaBefore[i] = getHashCamposFirma(entitiesAPI, itemDoc);
				//FIN [dipucr-Felipe #1116]
				
				//[dipucr #186]
				String infopagRdeBefore = itemDoc.getString("INFOPAG_RDE");
				
				signDocument = new SignDocument(itemDoc);
				String hash = signAPI.generateHashCode(signDocument);
		        signDocument.setHash(hash);
						
				//Llamada al conector firma para preparar el documento temporal
		        //Ojo en este paso se carga el fichero temporal en un nuevo documento del repositorio
				signAPI.postsign(signDocument, docref , false);
				
				//Dependiendo de si es presidente o fedatario, actualizamos
				sNumDecreto = arrNumDecretos[i];
				if (StringUtils.isNotEmpty(sNumDecreto)){
					GestorDecretos.actualizarValoresDecreto(cct, signDocument, sNumDecreto);
					
					//Actualizamos la secuencia real
					if (!sNumDecreto.equals(GestorDecretos.CLAVE_FIRMA_FEDATARIO)){
						
						numDecreto = Integer.parseInt(GestorDecretos.getOnlyNumero(sNumDecreto));
						//TODO: La secuencia real ya no tiene mucho sentido
						GestorDecretos.actualizarSecuenciaReal(cct, numDecreto);
					}
				}
				
				//[dipucr #186]
				String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
				GestorMetadatos.storeMetada(session, signDocument, autorCert, infopagRdeBefore);
			}
			
			// Liberamos el bloqueo de firma de decretos
			if (bHayDecretosPresidente){
				GestorDecretos.liberarBloqueoFirmaDecretos(cct);
			}
					
			// Identificadores de los pasos de circuito de firma asociados a los documentos a firmar
			String[] stepIds = batchSignForm.getMultibox();
			if (stepIds == null) {
				
				return mapping.findForward("refresh");
			}		
								
			String[] signs = infoDocs;
			//String[] signs = batchSignForm.getSigns().split("!");
			//String[] signs = {""};
			
			if (((signs.length == 1) &&  (StringUtils.isEmpty(signs[0]))) || (signs.length != stepIds.length)) {
						
						throw new ISPACInfo(getResources(request).getMessage("error.message.numero.firmas.incorrecta"));
			}
					
			String entityId = (String)request.getSession().getAttribute("idEntidad");
			
			// Firmar los documentos asociados a los pasos de firma
			//List signDocuments = signAPI.batchSignSteps(stepIds, signs, batchSignForm.getSignCertificate(), entityId);
			List signDocuments = signAPI.batchSignSteps(stepIds, signs, null, entityId);
			
			//TODO CERTIFICADO
			//String autor_cert=signAPI.getFirmanteFromCertificado(batchSignForm.getSignCertificate());
			
			// Estados de firma para incluir el sustituto en la lista enviada a la vista
			IItemCollection itemcol = entitiesAPI.queryEntities(SpacEntities.SPAC_TBL_008, "");
			Map mapSignStates = CollectionBean.getBeanMap(itemcol, "VALOR");
			
			// Establecer el estado de firma para cada documento
			List ltSignsDocuments = new ArrayList();
			
			Iterator it = signDocuments.iterator();
			while (it.hasNext()) {
				
				SignDocument signDocumentaux = (SignDocument) it.next();
				
				signDocumentaux.setEntityId(entityId);
				
				IItem document = signDocumentaux.getItemDoc();
				
				// Este objeto es el que muestra el detalle de firma
				// Incluir el sustituto del estado de firma
				ItemBean itemBean = new ItemBean(document);
				String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
				itemBean.setProperty("AUTOR_CERT", autorCert);
				itemBean.setProperty("ESTADOFIRMA", ((ItemBean) mapSignStates.get(document.getString("ESTADOFIRMA"))).getProperty("VALOR"));
				itemBean.setProperty("SUSTITUTO_ESTADOFIRMA", ((ItemBean) mapSignStates.get(document.getString("ESTADOFIRMA"))).getProperty("SUSTITUTO"));
				itemBean.setProperty("HASH", signDocumentaux.getHash());
				
				ltSignsDocuments.add(itemBean);	
				
			}
					
			// Lista de documentos firmados para la vista
			request.setAttribute(ActionsConstants.SIGN_DOCUMENT_LIST, ltSignsDocuments);
			
			batchSignForm.clean();
			
		    ///////////////////////////////////////////////
		    // Formateador
			CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
			//BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedlistformatter.xml"));
			//Inicio [Ticket #315 Teresa]
			BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedHashformatter.xml"));
			//Fin [Ticket #315 Teresa]
			request.setAttribute(ActionsConstants.FORMATTER, formatter);
			
			setMenu(session.getClientContext(), state, request);
			
			return mapping.findForward("success");
		}
		//INICIO [dipucr-Felipe #1116] 09.10.14
		//Si ocurre algún error tendremos que deshacer las modificaciones sobre el documento y la BBDD
		catch(Exception ex){
			
			IClientContext cct = session.getClientContext();
			
			for (int i = 0; i < arrItemDoc.length; i++){
				
				IItem itemDoc = arrItemDoc[i];
				
				//Volvemos los datos del documento al estado anterior
				HashMap hsDatosFirma = arrHashDatosFirmaBefore[i];
				if (null != itemDoc && null != hsDatosFirma){
					itemDoc.set("INFOPAG", hsDatosFirma.get("INFOPAG"));
					itemDoc.set("INFOPAG_RDE", hsDatosFirma.get("INFOPAG_RDE"));
					itemDoc.set("FAPROBACION", hsDatosFirma.get("FAPROBACION"));
					itemDoc.set("REPOSITORIO", hsDatosFirma.get("REPOSITORIO"));
					itemDoc.set("EXTENSION", hsDatosFirma.get("EXTENSION"));
					itemDoc.set("FFIRMA", hsDatosFirma.get("FFIRMA"));
					itemDoc.set("EXTENSION_RDE", hsDatosFirma.get("EXTENSION_RDE"));
					itemDoc.set("COD_COTEJO", hsDatosFirma.get("COD_COTEJO"));
					itemDoc.set("MOTIVO_REPARO", hsDatosFirma.get("MOTIVO_REPARO"));
					itemDoc.set("MOTIVO_RECHAZO", hsDatosFirma.get("MOTIVO_RECHAZO"));
					itemDoc.store(cct);
				}
				
				//Dependiendo de si es presidente o fedatario, actualizamos
				String sNumDecreto = arrNumDecretos[i];
				if (StringUtils.isNotEmpty(sNumDecreto)){
					String sNumexp = itemDoc.getString("NUMEXP"); 
					GestorDecretos.deshacerValoresDecreto(cct, sNumexp, sNumDecreto);
				}
			}
			throw ex;
		}
		//FIN [dipucr-Felipe #1116]
	}
	
	/**
	 * Clonación del fichero
	 * @param entitiesAPI
	 * @param itemDoc
	 * @return
	 * @throws ISPACException
	 */
	private static HashMap getHashCamposFirma(IEntitiesAPI entitiesAPI, IItem itemDoc) throws ISPACException{
		
		HashMap hsResult = new HashMap();
		String property = null;
		String [] camposAClonar = {"ID", "NUMEXP", "FDOC", "NOMBRE", "AUTOR", "INFOPAG", "ESTADO", "ORIGEN", 
				"AUTOR_INFO", "ESTADOFIRMA", "FAPROBACION", "REPOSITORIO", "EXTENSION", "FFIRMA", "INFOPAG_RDE", 
				"EXTENSION_RDE", "COD_COTEJO", "COD_VERIFICACION", "MOTIVO_REPARO", "MOTIVO_RECHAZO"};
		
		for (int i = 0; i < camposAClonar.length; i++){
			
			property = camposAClonar[i];
			hsResult.put(property, itemDoc.get(property));
		}
		return hsResult;
	}
	
	
	/**
	 * Devuelve el código de la entidad actual
	 * @return
	 */
	private static String getCodEntidad() {
    	
    	String codEntidad = null;

		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		if (info != null) {
			codEntidad = info.getOrganizationId();
		}
    	
		return codEntidad;
    }
	
	
	/**
	 * [dipucr-Felipe 3#242]
	 * Recupera una lista con todos los id de documentos que tiene el firmante para firmar
	 * @param cct
	 * @param signAPI
	 * @return
	 * @throws ISPACException
	 */
	public static List<Integer> getIdDocsRealesFirma(ClientContext cct, ISignAPI signAPI) throws ISPACException{
		
		ArrayList<Integer> listIdDocs = new ArrayList<Integer>();
		@SuppressWarnings("unchecked")
		List<IItem> listPasosFirma = signAPI.getCircuitSetps(cct.getRespId()).toList();
		int idDoc = Integer.MIN_VALUE;
		for(IItem itemPaso : listPasosFirma){
			idDoc = itemPaso.getInt("SPAC_DT_DOCUMENTOS:ID");
			listIdDocs.add(idDoc);
		}
		return listIdDocs;
		
		
	}
	
}