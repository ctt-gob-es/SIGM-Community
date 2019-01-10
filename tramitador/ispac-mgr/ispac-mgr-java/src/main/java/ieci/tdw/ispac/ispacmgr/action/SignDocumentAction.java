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

import java.io.StringReader;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
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
import es.dipucr.sigem.api.rule.common.utils.FirmaLotesError;
import es.dipucr.sigem.api.rule.common.utils.FirmaLotesUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorDatosFirma;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

@SuppressWarnings("restriction")
public class SignDocumentAction extends BaseDispatchAction {
	
	private final static String ITEM_SEPARATOR = ":";
	
	protected static final String ERROR_TITULO = "firmar.titulo";
	protected static final String ERROR_TEXTO = "firmar.texto";
	protected static final String ERROR_MAPPING = "nofirmar";
	
	private IState enterState(HttpServletRequest request, SessionAPI session, HttpServletResponse response) throws ISPACException{
		IManagerAPI managerAPI=ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
		// Se cambia el estado de tramitación
		@SuppressWarnings("rawtypes")
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
		
		//Código de entidad
		ClientContext cct = session.getClientContext();
		String codEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();

		//INICIO [eCenpri-Felipe #818]
		FirmaLotesError error = FirmaLotesUtil.getErrorDeshabilitar(codEntidad);
		if (null != error){
			request.setAttribute(ERROR_TITULO, error.getTitulo());
			request.setAttribute(ERROR_TEXTO, error.getTexto());
			return mapping.findForward(ERROR_MAPPING);
		}
		//FIN [eCenpri-Felipe #818]
		
		IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
		ISignAPI signAPI = session.getAPI().getSignAPI();
		IState state = enterState(request, session, response);
		
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
				
		SignForm signForm = ((SignForm) form);
				
		//Referencia del documento
		IItem itemDoc = entitiesAPI.getDocument(state.getEntityRegId());
		SignDocument signDocument = new SignDocument(itemDoc);
		signDocument.setEntityId(codEntidad); //2.0
		signDocument.addFirmante(infoFirmante);
			
		//PRE-FIRMA
		String docRef = signAPI.presign(signDocument, true);
		LOGGER.info("Prefirma realizada, el iddocumento temporal es: "+docRef);
		
		HashMap<String, String> infoFirmas = new HashMap<String, String>();
		String idDoc = String.valueOf(signDocument.getItemDoc().getKeyInt());
		LOGGER.info("El SignDocument iddocumento es: "+idDoc);	
		infoFirmas.put(docRef, idDoc);
		
		//Construimos el xml en caso que finalmente lo incorporemos, finalmente el xml de metadata ira vinculado pero no en la firma del documento
		//Este parametro ya no se usa lo dejo aqui por si hiciera falta en un futuro
		String xml = "<xml>FIRMA ELECTRONICA EN DIPUTACION DE CIUDAD REAL</xml>";
			
		//Añadimos los datos del documento
		StringBuffer sbDatosDoc = new StringBuffer();
		sbDatosDoc.append(docRef);			
		sbDatosDoc.append(ITEM_SEPARATOR);
		sbDatosDoc.append(codEntidad);
		sbDatosDoc.append(ITEM_SEPARATOR);
		sbDatosDoc.append(xml);
		
		//FIRMA POR LOTES #871
		ObjectFactoryFirmaLotesPeticion peticion = new ObjectFactoryFirmaLotesPeticion();
		Signbatch signbatch = FirmaLotesUtil.getSignBatch(cct, peticion);
		String extraparams = FirmaLotesUtil.getFirmaExtraParams(codEntidad, cct, infoFirmante, signDocument.getDocumentType(), docRef, 1, 1, new Date());
		Signbatch.Singlesign singlesign = FirmaLotesUtil.getSingleSign(cct, peticion, codEntidad, 0, extraparams);
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
		signForm.setSerialNumber(infoFirmante.getNumeroSerie());
		signForm.setListaDocs(result);
		signForm.setCodEntidad(codEntidad);
		signForm.setInfoFirma(infoFirmas);
		signForm.setDocumentId(idDoc);
		
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
			docref = docref.split(FirmaLotesUtil.EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER)[1];
			
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
			
			//[dipucr-Felipe #817]
			GestorDatosFirma.storeDatosFirma(session.getClientContext(), signDocument, null);
					
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