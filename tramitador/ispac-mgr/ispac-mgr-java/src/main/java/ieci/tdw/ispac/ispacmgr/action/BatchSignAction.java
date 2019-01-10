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
import ieci.tdw.ispac.ispaclib.db.DbCnt;
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

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
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
import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.FirmaLotesError;
import es.dipucr.sigem.api.rule.common.utils.FirmaLotesUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorDatosFirma;
import es.dipucr.sigem.api.rule.common.utils.GestorDecretos;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

@SuppressWarnings("restriction")
public class BatchSignAction extends BaseDispatchAction {
	
	private final static String DOC_SEPARATOR = "&&";
	private final static String ITEM_SEPARATOR = ":";
	
	protected static final String ERROR_TITULO = "firmar.titulo";
	protected static final String ERROR_TEXTO = "firmar.texto";
	protected static final String ERROR_MAPPING = "nofirmar";

	protected static final Logger LOGGER = Logger.getLogger(BatchSignAction.class);	

	private IState enterState(HttpServletRequest request, 
							  SessionAPI session, 
							  HttpServletResponse response) throws ISPACException {
		
		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
		// Se cambia el estado de tramitación
		Map<?, ?> params = request.getParameterMap();
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
		
		//Código de entidad
		ClientContext cct = session.getClientContext();
		String codEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
		FirmaConfiguration fc = FirmaConfiguration.getInstance(cct);

		//INICIO [eCenpri-Felipe #818]
		FirmaLotesError error = FirmaLotesUtil.getErrorDeshabilitar(codEntidad);
		if (null != error){
			request.setAttribute(ERROR_TITULO, error.getTitulo());
			request.setAttribute(ERROR_TEXTO, error.getTexto());
			return mapping.findForward(ERROR_MAPPING);
		}
		//FIN [eCenpri-Felipe #818]

		//[eCenpri-Agustin #781]
		//Activamos nuevo estado
		IState state = enterState(request, session, response);
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();
		HashMap<String, String> infoFirmas = new HashMap<String, String>();
		
		BatchSignForm batchSignForm = ((BatchSignForm)form);
		//en 'ids' se recogen los identificados de los pasos de circuitos de firma instanciados que se han seleccionado
		String[] ids = batchSignForm.getMultibox();
		
		StringBuffer sbListaDocs = new StringBuffer();
		String docRef = null; //infopag o infopag_rde
		String xml = null;
		
		//Informacion del firmante
		InfoFirmante infoFirmante = FirmaLotesUtil.getInfoFirmante(cct, codEntidad);
		
		//INICIO [dipucr-Felipe 3#231]
		error = FirmaLotesUtil.getErrorFirmanteNoConfigurado(codEntidad, infoFirmante);
	    if (null != error) {
			request.setAttribute(ERROR_TITULO, error.getTitulo());
			request.setAttribute(ERROR_TEXTO, error.getTexto());
			return mapping.findForward(ERROR_MAPPING);
		}
	    //FIN [dipucr-Felipe 3#231]
		
		// Se calcula el hash de los documentos a firmar
		IItem itemDoc;
		IItem itemStep;
		SignDocument signDocument;
		
		// Comprobamos si nos ha llegado algún decreto en la firma del presidente
		boolean bBloquearFirmaDecretos = false;
		SignDocument [] arrSignDocument = new SignDocument[ids.length];
		IItem [] arrItemStep = new IItem[ids.length];//[dipucr-Felipe #791]
		String [] arrNumDecretos = new String[ids.length]; 
		String sNumDecreto = null;
		
		//[dipucr-Felipe 3#243]
		List<Integer> listIdDocsRealesFirma = getIdDocsRealesFirma(cct, signAPI);
		
		// Vemos si hay algún documento que ya esté firmado y se muestre por un evento atrás del navegador [dipucr-Felipe 3#242]
		// Vemos si hay algún decreto
		for (int i = 0; i < ids.length; i++) {
			
			itemStep = signAPI.getStepInstancedCircuit(Integer.parseInt((String) ids[i]));
			arrItemStep[i] = itemStep;//[dipucr-Felipe #791]
			int idDoc = itemStep.getInt("ID_DOCUMENTO");
			
			//INICIO [dipucr-Felipe 3#243]
			//Cuando se usa el botón atrás del navegador, evitar que se firme dos veces el mismo documento
			if (!listIdDocsRealesFirma.contains(idDoc)){
				request.setAttribute(ERROR_TITULO, fc.getProperty(FirmaLotesUtil.ERROR.FIRMAPREVIA.TITULO));
				request.setAttribute(ERROR_TEXTO, fc.getProperty(FirmaLotesUtil.ERROR.FIRMAPREVIA.TEXTO));
				return mapping.findForward(ERROR_MAPPING);
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
								request.setAttribute(ERROR_TITULO, fc.getProperty(FirmaLotesUtil.ERROR.DECRETOS.TITULO));
								request.setAttribute(ERROR_TEXTO, fc.getProperty(FirmaLotesUtil.ERROR.DECRETOS.TEXTO));
								return mapping.findForward(ERROR_MAPPING);
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
		
		ObjectFactoryFirmaLotesPeticion peticion = new ObjectFactoryFirmaLotesPeticion();
		Signbatch signbatch = FirmaLotesUtil.getSignBatch(cct, peticion);
				
		LOGGER.info("Se van a realizar la Prefirma");
		
		//PRE-FIRMA DE LOS DOCUMENTOS
		for (int i = 0; i < arrSignDocument.length; i++) {
			
			signDocument = arrSignDocument[i];
			signDocument.addFirmante(infoFirmante);
			signDocument.setEntityId(codEntidad);//2.0
			sNumDecreto = arrNumDecretos[i];
			signDocument.setNumDecreto(sNumDecreto);
			
			//Llamada al conector firma para preparar el documento temporal
			docRef = signAPI.presign(signDocument, true);
			LOGGER.info("Prefirma realizada, el iddocumento temporal es: "+docRef);	
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
			
			///////////// AJM Importante paso las referencias del documento al servidor de firma por los extraparams
			//Hay que convertir a Base64 extraparams y parametros de signsaver
			//[dipucr-Felipe #791]
			itemStep = arrItemStep[i];
			int signerPosition = itemStep.getInt("ID_PASO");
			int idCircuito = itemStep.getInt("ID_CIRCUITO");
			int numberOfSigners = CircuitosUtil.getNumFirmantesCircuito(cct, idCircuito);
			
			String extraparams = FirmaLotesUtil.getFirmaExtraParams(codEntidad, cct, infoFirmante, signDocument.getDocumentType(), docRef, numberOfSigners, signerPosition, new Date());
			Signbatch.Singlesign singlesign = FirmaLotesUtil.getSingleSign(cct, peticion, codEntidad, i, extraparams);
			signbatch.getSinglesign().add(singlesign);	
					
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
			LOGGER.error("ERROR al preparar el lote de firmas: " + e.getMessage(), e);
		}
		
		//Seteamos los parametros en la firma tres fases
		batchSignForm.setSerialNumber(infoFirmante.getNumeroSerie());
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
		IEntitiesAPI entitiesAPI = null;
		ISignAPI signAPI = null;
		BatchSignForm batchSignForm = null;
		IState state = null;
		
		SignDocument signDocument;
		List<?> signDocuments;
		String entityId = null;
				
		String[] arrNumDecretos = null;
		IItem[] arrItemDoc = null;
		HashMap[] arrHashDatosFirmaBefore = null;
		//FIN [dipucr-Felipe #1116]

		// Acceso a los objetos que llevan en memoria la informacion de la firma
		IItem itemStep = null;
		
		try{
			//AJM Obtenemos la sesion
			state = enterState(request, session, response);
			entitiesAPI = session.getAPI().getEntitiesAPI();
			signAPI = session.getAPI().getSignAPI();

			IClientContext cct = session.getClientContext();
			entityId = (String)request.getSession().getAttribute("idEntidad");
			FirmaConfiguration fc = FirmaConfiguration.getInstance(cct);
			
			batchSignForm = ((BatchSignForm) form);
			HashMap<?, ?> infoFirmas = batchSignForm.getInfoFirma();	
			String listaDocs = batchSignForm.getCodEntidad();
			String[] infoDocs = listaDocs.split(DOC_SEPARATOR);
			
			// Comprobamos si nos ha llegado algún decreto en la firma del presidente
			String sNumDecreto = null;
			int numDecreto = Integer.MIN_VALUE;
			boolean bHayDecretosPresidente = false;
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
					request.setAttribute(ERROR_TITULO, fc.getProperty(FirmaLotesUtil.ERROR.DECRETOS.TITULO));
					request.setAttribute(ERROR_TEXTO, fc.getProperty(FirmaLotesUtil.ERROR.DECRETOS.TEXTO));
					return mapping.findForward(ERROR_MAPPING);
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
					request.setAttribute(ERROR_TITULO, fc.getProperty(FirmaLotesUtil.ERROR.FIRMAPREVIA.TITULO));
					request.setAttribute(ERROR_TEXTO, fc.getProperty(FirmaLotesUtil.ERROR.FIRMAPREVIA.TEXTO));
					return mapping.findForward(ERROR_MAPPING);
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
				
				//[dipucr-Felipe #817]
				GestorDatosFirma.storeDatosFirma(cct, signDocument, itemStep);
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
			
			// Firmar los documentos asociados a los pasos de firma
			//List signDocuments = signAPI.batchSignSteps(stepIds, signs, batchSignForm.getSignCertificate(), entityId);
			signDocuments = signAPI.batchSignSteps(stepIds, signs, null, entityId);
		
		}
		//INICIO [dipucr-Felipe #1116] 09.10.14
		//Si ocurre algún error tendremos que deshacer las modificaciones sobre el documento y la BBDD
		catch(Exception ex){	
			LOGGER.error("ERROR: " + ex.getMessage(), ex);

			IClientContext cct = session.getClientContext();
			DbCnt cnt = null;
			try{
				cnt = cct.getConnection();//[dipucr-Felipe 3#727]
				
				for (int i = 0; i < arrItemDoc.length; i++){
					
					IItem itemDoc = arrItemDoc[i];
					
					//Volvemos los datos del documento al estado anterior
					HashMap<?, ?> hsDatosFirma = arrHashDatosFirmaBefore[i];
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
						
						//[dipucr-Felipe #817]
						if (null != itemStep){
							GestorDatosFirma.deleteDatosFirma(cct, itemDoc, itemStep.getInt("ID_PASO"));
						}
							
					}
					
					//Dependiendo de si es presidente o fedatario, actualizamos
					String sNumDecreto = arrNumDecretos[i];
					if (StringUtils.isNotEmpty(sNumDecreto)){
						String sNumexp = itemDoc.getString("NUMEXP"); 
						GestorDecretos.deshacerValoresDecreto(cct, sNumexp, sNumDecreto);
					}
				}
			
			}
			finally{
				if (null != cct){
					cct.releaseConnection(cnt);//[dipucr-Felipe 3#727]
				}
			}
			
			throw ex;
		}
		
		String resultado = "success";
		
		try{
			generaResumen(session, request, entitiesAPI, signDocuments, entityId, batchSignForm, state);			
		} catch(Exception e){			
			resultado = "failureResumen";
		}
		
		return mapping.findForward(resultado);
		//FIN [dipucr-Felipe #1116]
	}
	
	public void generaResumen(SessionAPI session, HttpServletRequest request, IEntitiesAPI entitiesAPI, List<?> signDocuments, String entityId, BatchSignForm batchSignForm, IState state) throws ISPACException{
		try{
			// Estados de firma para incluir el sustituto en la lista enviada a la vista
			IItemCollection itemcol = entitiesAPI.queryEntities(SpacEntities.SPAC_TBL_008, "");
			Map<?, ?> mapSignStates = CollectionBean.getBeanMap(itemcol, "VALOR");
			
			// Establecer el estado de firma para cada documento
			List<ItemBean> ltSignsDocuments = new ArrayList<ItemBean>();
			Iterator<?> it = signDocuments.iterator();
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
			//Inicio [Ticket #315 Teresa]
			BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedHashformatter.xml"));
			//Fin [Ticket #315 Teresa]
			request.setAttribute(ActionsConstants.FORMATTER, formatter);
			
			setMenu(session.getClientContext(), state, request);
		} catch(ISPACException e){
			LOGGER.error("ERROR al generar el resumen del proceso de firma. " + e.getMessage(), e);
			throw e;
		}		
	}
	
	/**
	 * Clonación del fichero
	 * @param entitiesAPI
	 * @param itemDoc
	 * @return
	 * @throws ISPACException
	 */
	private static HashMap<String, Object> getHashCamposFirma(IEntitiesAPI entitiesAPI, IItem itemDoc) throws ISPACException{
		
		HashMap<String, Object> hsResult = new HashMap<String, Object>();
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