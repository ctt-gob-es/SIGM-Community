package es.dipucr.sigem.firmaReparo.action;

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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.sign.InfoFirmante;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.sign.afirma.TelefonicaSignConnector;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BatchSignAction;
import ieci.tdw.ispac.ispacmgr.action.form.BatchSignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacmgr.menus.MenuFactory;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.DipucrProperties;
import es.dipucr.sigem.api.rule.common.utils.GestorDecretos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;
import es.dipucr.sigem.firmaReparo.api.impl.SignReparoAPI;

public class BatchSignReparoAction extends BatchSignAction{
	
	private final static String DOC_SEPARATOR = "&&";
	private final static String ITEM_SEPARATOR = ":";	
	
	protected static final Logger logger = Logger.getLogger(BatchSignReparoAction.class);	

	private IState enterState(HttpServletRequest request, SessionAPI session,
			HttpServletResponse response) throws ISPACException {
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(
				session.getClientContext());
		// Se cambia el estado de tramitación
		Map params = request.getParameterMap();
		// Al estar en un 'DispacthAction', cambiamos de estado si no nos
		// encontramos ya en el estado de BATCHSIGNDOCUMENT
		IState state = managerAPI.currentState(getStateticket(request));
		if (state.getState() != ManagerState.BATCHSIGNLIST)
			state = managerAPI.enterState(getStateticket(request),
					ManagerState.BATCHSIGNLIST, params);
		storeStateticket(state, response);
	
		return state;
	}

	private void setMenu(ClientContext cct, IState state,
			HttpServletRequest request) throws ISPACException {
		request.setAttribute("menus", MenuFactory.getSignedDocumentListMenu(
				cct, state, getResources(request)));
	}		
	
	public ActionForward refrescar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		
		return mapping.findForward("refresh");	
	}
	
	
	
	public ActionForward calculateDocumentsHash(ActionMapping mapping,
			ActionForm form, HttpServletRequest request,
			HttpServletResponse response, SessionAPI session) throws Exception {
		
		//INICIO [eCenpri-Felipe #818]
		String sDeshabilitarFirma = DipucrProperties.getPropertyNoSingleton("firmar.deshabilitar");
		boolean bDeshabilitarFirma = false;
		if (StringUtils.isNotEmpty(sDeshabilitarFirma)){
			bDeshabilitarFirma = Boolean.valueOf(sDeshabilitarFirma).booleanValue();
		}
		if (bDeshabilitarFirma){

			String titulo = DipucrProperties.getPropertyNoSingleton("firmar.titulo");
			String texto = DipucrProperties.getPropertyNoSingleton("firmar.texto");
			request.setAttribute("firmar.titulo", titulo);
			request.setAttribute("firmar.texto", texto);
			return mapping.findForward("nofirmar");
		}
		//FIN [eCenpri-Felipe #818]
		
		//AJM Obtenemos la sesion		
		IState state = enterState(request, session, response);
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();
		HashMap infoFirmas = new HashMap();
		
		//AJM Obtenemos el nombre del firmante		
		ClientContext cct = session.getClientContext();
		String nombreFirmante = UsuariosUtil.getNombreFirma(cct);
		String docIdentidad = UsuariosUtil.getDni(cct);
		String numeroSerie = UsuariosUtil.getNumeroSerie(cct);
		InfoFirmante infoFirmante = new InfoFirmante();
//		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(numeroSerie);
		infoFirmante.setIdDocumentoDeIdentidadEnCertificado(docIdentidad);
		infoFirmante.setNombreFirmante(nombreFirmante);	
		
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
		
		BatchSignForm batchSignForm = ((BatchSignForm) form);
		// en 'ids' se recogen los identificados de los pasos de circuitos de
		// firma instanciados que se han seleccionado
		String[] ids = batchSignForm.getMultibox();

		// Se calcula el hash de los documentos a firmar
		IItem itemDoc;
		IItem itemStep;
		SignDocument signDocument;
		
		StringBuffer sbListaDocs = new StringBuffer();		
		String codEntidad = getCodEntidad();
		String docRef = null; //infopag o infopag_rde
		String xml = null;
		
		// Comprobamos si nos ha llegado algún decreto en la firma del presidente
		boolean bHayDecretosPresidente = false;
		SignDocument [] arrSignDocument = new SignDocument[ids.length];
		String [] arrNumDecretos = new String[ids.length]; 
		String sNumDecreto = null;
		
		// Vemos si hay algún decreto
		for (int i = 0; i < ids.length; i++) {
			
			itemStep = signAPI.getStepInstancedCircuit(Integer.parseInt((String) ids[i]));
			int idDoc = itemStep.getInt("ID_DOCUMENTO");
			itemDoc = entitiesAPI.getDocument(idDoc);
			signDocument = new SignDocument(itemDoc);
			arrSignDocument[i] = signDocument;
			
			// Comprobamos si es decreto, si estamos en la firma presidente o fedatario
			if (TelefonicaSignConnector.isDocDecreto(signDocument)){
				
				//Primer tramite=0, Presidente=1, Fedatario=2
				int tipoFirma = GestorDecretos.comprobarFirmaDecretos(cct, idDoc);
				
				if (tipoFirma != GestorDecretos.TIPO_FIRMA_NORMAL){
				
					if (tipoFirma == GestorDecretos.TIPO_FIRMA_PRESIDENTE){
						if (!bHayDecretosPresidente){
							bHayDecretosPresidente = true;
							// Seteamos el valor de la secuencia auxiliar 
							// al valor de la real para calcular el número de decreto
							GestorDecretos.actualizarSecuenciaAuxiliar(cct);
						}
						arrNumDecretos[i] = GestorDecretos.getNumDecreto(cct, false);
					}
					else if (tipoFirma == GestorDecretos.TIPO_FIRMA_FEDATARIO){
						arrNumDecretos[i] = GestorDecretos.CLAVE_FIRMA_FEDATARIO;
					}
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
//						throw new ISPACException("En estos momentos no se pueden firmar decretos. Espere unos minutos.");
			}
		}
		
		//PRE-FIRMA DE LOS DOCUMENTOS
		for (int i = 0; i < arrSignDocument.length; i++) {
			
			signDocument = arrSignDocument[i];
			signDocument.addFirmante(infoFirmante);
			signDocument.setEntityId(codEntidad);//
			sNumDecreto = arrNumDecretos[i];
			signDocument.setNumDecreto(sNumDecreto);
			
			//Llamada al conector firma para preparar el documento temporal
			docRef = signAPI.presign(signDocument, true);
			infoFirmas.put(docRef,ids[i]);
			
			//Rellenamos la cadena de lista de documentos
			if (i > 0){
				sbListaDocs.append(DOC_SEPARATOR);
			}
			
			//TODO: Construimos el xml en caso que finalmente lo incorporemos
			xml = "<xml>FIRMA ELECTRONICA EN DIPUTACION DE CIUDAD REAL</xml>";
			
			//Añadimos los parametros necesarios en la firma tres fases
			sbListaDocs.append(codEntidad);			
			sbListaDocs.append(ITEM_SEPARATOR);
			sbListaDocs.append(docRef);
			sbListaDocs.append(ITEM_SEPARATOR);
			sbListaDocs.append(xml);
			
			if (StringUtils.isNotEmpty(sNumDecreto)){
				sbListaDocs.append(ITEM_SEPARATOR);
				sbListaDocs.append(sNumDecreto);
			}
		}
		
		//Seteamos los parametros en la firma tres fases
		batchSignForm.setSerialNumber(numeroSerie);
		batchSignForm.setListaDocs(sbListaDocs.toString());
		batchSignForm.setCodEntidad(codEntidad);//TODO: Esto ya no se usa
		batchSignForm.setInfoFirma(infoFirmas);	

		setMenu(session.getClientContext(), state, request);

		return mapping.findForward("confirmaReparo");
	}

	
	
	public ActionForward firmarReparo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		//AJM Obtenemos la sesion
		IState state = enterState(request, session, response);
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
//		ISignAPI signAPI = session.getAPI().getSignAPI();
		SignReparoAPI signAPI = new SignReparoAPI(session.getClientContext());
		BatchSignForm batchSignForm = ((BatchSignForm) form);
		HashMap infoFirmas = batchSignForm.getInfoFirma();	
		String listaDocs = batchSignForm.getListaDocs();
		String[] infoDocs = listaDocs.split(DOC_SEPARATOR);
		
		//AJM Acceso a los objetos que llevan en memoria la informacion de la firma
		IItem itemDoc;
		IItem itemStep;
		SignDocument signDocument;
		
		// Comprobamos si nos ha llegado algún decreto en la firma del presidente
		String numDecreto = null;
		boolean bHayDecretosPresidente = false;
		IClientContext cct = session.getClientContext();
		String [] arrDocRefs = new String[infoDocs.length];
		String[] arrNumDecretos = new String[infoDocs.length];
		String [] datosDoc = null;
		
		//Comprobamos que haya algún decreto
		for (int i = 0; i < infoDocs.length; i++) 
		{		
			datosDoc = infoDocs[i].split(ITEM_SEPARATOR);
			arrDocRefs[i] = datosDoc[1];
			
			if (datosDoc.length > 3){
				numDecreto = datosDoc[3];
				arrNumDecretos[i] = numDecreto;
				if (StringUtils.isNotEmpty(numDecreto) && 
					!numDecreto.equals(GestorDecretos.CLAVE_FIRMA_FEDATARIO))
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
				//TODO: ERROR - Redireccionar
				throw new ISPACException("En estos momentos no se pueden firmar decretos. Espere unos minutos.");
			}
		}
		
		//Realizar la actualizacion en base de datos de las firmas una vez realizada/as la/as firma/as 3 fases
		for (int i = 0; i < arrDocRefs.length; i++) 
		{
			String docref = arrDocRefs[i];
			String id = (String)infoFirmas.get(docref);
			
			itemStep = signAPI.getStepInstancedCircuit(Integer.parseInt(id));
			itemDoc = entitiesAPI.getDocument(itemStep.getInt("ID_DOCUMENTO"));
			signDocument = new SignDocument(itemDoc);
			String hash = signAPI.generateHashCode(signDocument);
	        signDocument.setHash(hash);
					
			//Llamada al conector firma para preparar el documento temporal
			/*String result = */signAPI.postsign(signDocument, docref , false);
			
			//Dependiendo de si es presidente o fedatario, actualizamos
			numDecreto = arrNumDecretos[i];
			if (StringUtils.isNotEmpty(numDecreto)){
				GestorDecretos.actualizarValoresDecreto(cct, signDocument, numDecreto);
				
				//Actualizamos la secuencia real
				if (!numDecreto.equals(GestorDecretos.CLAVE_FIRMA_FEDATARIO)){
					GestorDecretos.actualizarSecuenciaReal(cct);
				}
			}
		}
		
		// Liberamos el bloqueo de firma de decretos
		if (bHayDecretosPresidente){
			GestorDecretos.liberarBloqueoFirmaDecretos(cct);
		}
		
		//AJM Identificadores de los pasos de circuito de firma asociados a los documentos a firmar
		String[] stepIds = batchSignForm.getMultibox();
		if (stepIds == null) {
			
			return mapping.findForward("refresh");
		}			
		
		//TODO A PARTIR DE AQUI LO QUE SE HACE ES RECUPERAR LAS FIRMAS REALIZADAS, 
		//TODO PARA QUE SEA MÁS RAPIDO UNA MEJORA QUE NO TENIA PREVISTA ES GUARDAR LOS METADATOS DE LAS FIRMAS EN UNA CAMPO NUEVO EN LA 
		//TODO BASE DE DATOS O EN EL REPOSITORIO DIRECTAMENTE PERO DESDE EL SERVIDOR DE FIRMA
		
		//TODO METADATOS --> LAS FIRMAS Y EL CERTIFICADO EN PRINCIPIO LOS PASO VACIOS, LO DEJO PARA LA IMPLEMENTACION DE LOS METADATOS
		String[] signs = {""};		
		String entityId = (String) request.getSession().getAttribute("idEntidad");		

		// Firmar con reparo los documentos asociados a los pasos de firma
		List signDocuments = signAPI.firmarReparo(stepIds, signs, batchSignForm.getSignCertificate(), entityId, batchSignForm.getMotivoRechazo());
//		String autor_cert = signAPI.getFirmanteFromCertificado(batchSignForm.getSignCertificate());//TODO: viene vacío
		
		// Estados de firma para incluir el sustituto en la lista enviada a la
		// vista
		IItemCollection itemcol = entitiesAPI.queryEntities(
				SpacEntities.SPAC_TBL_008, "");
		Map mapSignStates = CollectionBean.getBeanMap(itemcol, "VALOR");

		// Establecer el estado de firma para cada documento
		List ltSignsDocuments = new ArrayList();

		Iterator it = signDocuments.iterator();
		while (it.hasNext()) {
			
			SignDocument signDocument_aux = (SignDocument) it.next();
			// TODO: BORRAR O VERIFICAR signDocument.setEntityId(entityId);

			signDocument_aux.setEntityId(entityId);// 2.0

			IItem document = signDocument_aux.getItemDoc();
			// Incluir el sustituto del estado de firma
			ItemBean itemBean = new ItemBean(document);
			String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
			itemBean.setProperty("AUTOR_CERT", autorCert);
			//Inicio [Ticket #315 Teresa]
			itemBean.setProperty("HASH", signDocument_aux.getHash());
			//Fin [Ticket #315 Teresa]
			itemBean.setProperty("SUSTITUTO_ESTADOFIRMA",
					((ItemBean) mapSignStates.get(document
							.getString("ESTADOFIRMA")))
							.getProperty("SUSTITUTO"));
			ltSignsDocuments.add(itemBean);
		}

		// Lista de documentos firmados para la vista
		request.setAttribute(ActionsConstants.SIGN_DOCUMENT_LIST,
				ltSignsDocuments);

		batchSignForm.clean();
		// /////////////////////////////////////////////
		// Formateador
		CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
		//BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedlistformatter.xml"));
		//Inicio [Ticket #315 Teresa]
		BeanFormatter formatter = factory.getFormatter(getISPACPath("/digester/documentsignedHashformatter.xml"));
		//Fin [Ticket #315 Teresa]
		request.setAttribute(ActionsConstants.FORMATTER, formatter);

		setMenu(session.getClientContext(), state, request);
		return mapping.findForward("successReparo");
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

}
