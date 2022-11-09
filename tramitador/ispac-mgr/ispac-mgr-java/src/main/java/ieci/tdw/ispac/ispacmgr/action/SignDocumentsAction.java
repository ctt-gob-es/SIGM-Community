package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
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
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.ResultadoValidacionCertificado;
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
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

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
	
	protected boolean bFirmarTodo = false;//[dipucr-Felipe #1246]
	protected boolean bFirmarDetalle = false;//[dipucr-Felipe #1246]

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

		if (!bFirmarTodo && !bFirmarDetalle && ArrayUtils.isEmpty(documentIds)) {
			LOGGER.warn("No se ha seleccionado ningún documento");
			throw new ISPACInfo(getResources(request).getMessage(
					"forms.listdoc.firmarDocumentos.empty"), false);
		}

		// Almacenar los identificadores de los documentos para el tratamiento posterior
		storeDocumentIds(session, documentIds);
		
		// [dipucr-Felipe #1246] Establecer el action para el asistente
		request.setAttribute("action", getAction());
		
		return mapping.findForward("selectOption");
	}
	
	/**
	 * [dipucr-Felipe #1246] Devuelve el action en el que nos encontramos
	 * - Firmar todo -> signAllDocumentAction
	 * - Firmar detalle -> signDocumentAction
	 * - Firmar selección -> signDocumentsAction
	 * @return
	 */
	protected String getAction(){
		
		if (bFirmarTodo){
			return "signAllDocument.do";
		}
		else if (bFirmarDetalle){
			return "signDocument.do";
		}
		else{
			return "signDocuments.do";
		}
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
		
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Información del estado
		getCurrentState(request, session);

		// Codigos hash de los documentos
		List<String> hashCodes = new ArrayList<String>();
		
		// Obtener la información de los documentos seleccionados
		IItemCollection documents = getDocuments(request, session);//[dipucr-Felipe #1246]

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		while (iterator.hasNext()) {
			
			// Información del documento
			IItem document = iterator.next();

			// Obtener el codigo hash del documento
			hashCodes.add(signAPI.generateHashCode(new SignDocument(document)));
		}

		// Almacenar los hash de los documentos para el tratamiento posterior
		storeDocumentHashCodes(session, hashCodes);
		
		request.setAttribute("hashCodes",
				hashCodes.toArray(new String[hashCodes.size()]));
		
		return mapping.findForward("sign");
	}

	/**
	 * [dipucr-Felipe #1246] Integración Port@firmas 
	 * @param request 
	 * @param session
	 * @return
	 * @throws ISPACException 
	 */
	public IItemCollection getDocuments(HttpServletRequest request, SessionAPI session) throws ISPACException {
		
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		return entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, new StringBuffer(" WHERE ")
						.append(DBUtil.addOrCondition("ID", retrieveDocumentIds(session))).toString());
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

		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Información del estado
		IState state = getCurrentState(request, session);

		SignForm signForm = (SignForm) form;
		String[] signs = signForm.getSigns().split("!");
		String[] hashCodes = retrieveDocumentHashCodes(session);
		
		if (ArrayUtils.isEmpty(signs)
				|| (StringUtils.isBlank(signForm.getSignCertificate()) || "undefined"
						.equalsIgnoreCase(signForm.getSignCertificate()))) {
			return mapping.findForward("failure");
		}

		List<ItemBean> documents_ok = new ArrayList<ItemBean>();
		List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();

		// Obtener la información de los documentos seleccionados
		IItemCollection documents = getDocuments(request, session);//[dipucr-Felipe #1246]

		@SuppressWarnings("unchecked")
		Iterator<IItem> iterator = documents.iterator();
		int count = 0;
		
		//Se comprueba la validez del certificado
		ResultadoValidacionCertificado resultado = signAPI.validateCertificate(signForm.getSignCertificate());
		if (resultado.getResultadoValidacion().equals(ResultadoValidacionCertificado.VALIDACION_ERROR)){
			request.setAttribute("SIGN_ERROR", "Error en la validación del certificado. "+resultado.getMensajeValidacion());
			return mapping.findForward("success");
		}
		
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
				signDocument.addSign(signs[count]);
		        signDocument.addCertificate(signForm.getSignCertificate());
		        signDocument.setHash(hashCodes[count]);

	        	signAPI.sign(signDocument, true);

				documents_ok.add(new ItemBean(document));

			} catch (ISPACException e) {
				LOGGER.error(
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

		
		filter.setIdSistema(ProcessSignConnectorFactory.getInstance(session.getClientContext()).getProcessSignConnector().getIdSystem());
		filter.setDefaultPortafirmas(ProcessSignConnectorFactory.getInstance(session.getClientContext()).isDefaultConnector());

		// Obtenemos los circuitos de firma
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IItemCollection itemcol = signAPI.getCircuits(filter);
		request.setAttribute(ActionsConstants.SIGN_CIRCUIT_LIST,
				CollectionBean.getBeanList(itemcol));

		// [dipucr-Felipe #1246] Establecer el action para el asistente
		request.setAttribute("action", getAction());
		
		
		//INICIO [eCenpri-Felipe #592] Obtenemos los circuitos del trámite
		IItemCollection itemcolTram = signAPI.getCircuitsTramite(filter);
		@SuppressWarnings("rawtypes")
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
	 * [dipucr-Felipe #1246] Adaptamos el código para que se pueda "mandar a mi firma"
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
		// INICIO [dipucr-Felipe #1246] Posibilidad de mandar a mi propia firma
		String sMyCircuit = request.getParameter(ActionsConstants.SIGN_MY_CIRCUIT);
		boolean myCircuit = (!StringUtils.isEmpty(sMyCircuit) && "true".equals(sMyCircuit));
		String circuitId = null;
		Usuario usuario = null;
		
		if (myCircuit){
			IClientContext cct = session.getClientContext();
			String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
			ServicioEstructuraOrganizativa servicioEstructuraOrganizativa = LocalizadorServicios.getServicioEstructuraOrganizativa();
			String idUsuario = cct.getUser().getUID().replace("1-", "");
			usuario = servicioEstructuraOrganizativa.getUsuario(Integer.valueOf(idUsuario), entidad);
		}
		else{
			circuitId = request.getParameter(ActionsConstants.SIGN_CIRCUIT_ID);
		}
		//FIN [dipucr-Felipe #1246]

		// Propiedades del circuito de firmas
		SignForm formulario = (SignForm) form;
		ProcessSignProperties properties = new ProcessSignProperties(
				formulario.getSubject(), formulario.getFstart(),
				formulario.getFexpiration(), formulario.getContent(),
				formulario.getLevelOfImportance());
		
		ISignAPI signAPI = session.getAPI().getSignAPI();

		// Guardar el estado en el ClientContext
		getCurrentState(request, session);

		List<ItemBean> documents_ok = new ArrayList<ItemBean>();
		List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();

		// Obtener la información de los documentos seleccionados
		IItemCollection documents = getDocuments(request, session);//[dipucr-Felipe #1246]

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
				// [dipucr-Felipe #1246]
				if (myCircuit){
					signAPI.initCircuitPortafirmas(usuario, document.getKeyInt(), properties);
				}
				else{
					signAPI.initCircuitPortafirmas(circuitId, document.getKeyInt(), properties);
				}

				documents_ok.add(new ItemBean(document));

			} catch (ISPACException e) {
				LOGGER.error(
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