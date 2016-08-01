package es.dipucr.sigem.sgm.tram.sign.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
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
import ieci.tdw.ispac.ispacmgr.action.BaseDispatchAction;
import ieci.tdw.ispac.ispacmgr.action.form.BatchSignForm;
import ieci.tdw.ispac.ispacmgr.common.constants.ActionsConstants;
import ieci.tdw.ispac.ispacmgr.menus.MenuFactory;
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

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.firma.xml.peticion.ObjectFactoryFirmaLotesPeticion;
import es.dipucr.sigem.api.firma.xml.peticion.Signbatch;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorMetadatos;
import es.dipucr.sigem.api.rule.common.utils.UsuariosUtil;

//INICIO [DipuCR-Agustin #94]
public class SignAllDocumentAction extends BaseDispatchAction
{

 /**
	 * Constantes firma lotes
  */
  private static final String DOC_SEPARATOR = "&&";
  private static final String ITEM_SEPARATOR = ":";
  protected static final String SIGN_DOCUMENT_IDS_PARAM = "SIGN_DOCUMENT_IDS";
  protected static final String SIGN_DOCUMENT_HASHCODES_PARAM = "SIGN_DOCUMENT_HASHCODES";
  private static final String DATASOURCE_WEBSERVICE = "webservice";
  private static final String EXTRAPARAM_DOC_CODIGO_ENTIDAD = "codent=";
  private static final String EXTRAPARAM_DOC_GLOBALLY_UNIQUE_IDENTIFIER = "guid=";
  private static final String EXTRAPARAM_DOC_TEMP_WEBSERVICE_ENDPOINT_DATASOURCE = "datasource=";
  public static final Logger logger = Logger.getLogger(SignAllDocumentAction.class);
  
  /**
     * [eCenpri-Agustin S3 #94] Integracion de firma 3 fases y firma lotes  
	 * Modificado para incorporar la firma en 3 fases
	 * Preparación y Pre-Firma de todos los documentos del trámite 
	 * [eCenpri-Felipe #871] 18.04.2013
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
  */
  public ActionForward initSign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
    throws Exception
  {
    OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
    String entityId = info.getOrganizationId();
    FirmaConfiguration fc = FirmaConfiguration.getInstanceNoSingleton(entityId);
    String sDeshabilitarFirma = fc.getProperty("firmar.deshabilitar");
    
    boolean bDeshabilitarFirma = false;
    if (StringUtils.isNotEmpty(sDeshabilitarFirma)) {
      bDeshabilitarFirma = Boolean.valueOf(sDeshabilitarFirma).booleanValue();
    }
    
    //Codigo para deshabilitar temporalmente la firma
    if (bDeshabilitarFirma)
    {
      String titulo = fc.getProperty("firmar.titulo");
      String texto = fc.getProperty("firmar.texto");
      request.setAttribute("firmar.titulo", titulo);
      request.setAttribute("firmar.texto", texto);
      return mapping.findForward("nofirmar");
    }
    ISignAPI signAPI = session.getAPI().getSignAPI();
    IState state = enterState(request, session, response);
    ClientContext cct = session.getClientContext();
    IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
    IState currentState = managerAPI.currentState(getStateticket(request));
    
    String stateTicket = getStateticket(request);
    
    String[] aStateTicket = stateTicket.split("\\|");
    String sIdTramite = "";
    if ((aStateTicket != null) && (aStateTicket.length > 11)) {
      sIdTramite = aStateTicket[5];
    }
    
    //Consulta para obtener todos los documentos del tramite que no esten firmados
    String strQuery = "";
    //INICIO [dipucr-Felipe #907]
    if (!StringUtils.isEmpty(sIdTramite) || !sIdTramite.equals("0")){
    	strQuery = "WHERE ID_TRAMITE=" + sIdTramite + " AND ESTADOFIRMA = '00'";
    } else {
    	return getActionForwardAtrasNavegador(request);
    }
    //FIN [dipucr-Felipe #907]
    //[dipucr-Felipe #184]
    strQuery += " ORDER BY ID";
    
    IItemCollection collAllDoc = DocumentosUtil.queryDocumentos(cct, strQuery);
    Iterator itcollAllDoc = collAllDoc.iterator();
    
    String codEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
    
    //Obtencion de datos del firmante
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
    
    //Lectura del properties de firma
    List<String> hashCodes = new ArrayList();
    String result = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    String nodos = "";
    int count = 0;
    
    String firmar_lotes_algorithm = fc.getProperty("firmar.lotes.algorithm");
    String firmar_lotes_format = fc.getProperty("firmar.lotes.format");
    String firmar_lotes_stoponerror = fc.getProperty("firmar.lotes.stoponerror");
    String firmar_lotes_suboperation = fc.getProperty("firmar.lotes.suboperation");
    String firmar_lotes_concurrenttimeout = fc.getProperty("firmar.lotes.concurrenttimeout");
    String firmar_lotes_signsaver = fc.getProperty("firmar.lotes.signsaver");
    
    //Construir peticion al servidor de firma
    ObjectFactoryFirmaLotesPeticion peticion = new ObjectFactoryFirmaLotesPeticion();
    BigInteger concurrenttimeout = new BigInteger(firmar_lotes_concurrenttimeout);
    
    Signbatch signbatch = peticion.createSignbatch();
    signbatch.setAlgorithm(firmar_lotes_algorithm);
    signbatch.setStoponerror(firmar_lotes_stoponerror);
    signbatch.setConcurrenttimeout(concurrenttimeout);
    if (itcollAllDoc.hasNext())
    {
      HashMap infoFirmas = new HashMap();
      
      StringBuffer sbListaDocs = new StringBuffer();
      String docRef = "";
      String xml = "";
      
      int i = 0;
      while (itcollAllDoc.hasNext())
      {
        IItem document = (IItem)itcollAllDoc.next();
        
        SignDocument signDocument = new SignDocument(document);
        signDocument.setEntityId(codEntidad);
        signDocument.addFirmante(infoFirmante);
        String idDoc = String.valueOf(signDocument.getItemDoc().getKeyInt());
        
        docRef = signAPI.presign(signDocument, true);
        logger.info("Prefirma realizada, el iddocumento temporal es: " + docRef);
        
        hashCodes.add(docRef);
        
        String url_ = DocumentUtil.generateURL(request, "download", session.getTicket(), idDoc, null);
        String url = "webservice";
        
        byte[] extraparamsBase64 = null;
        byte[] configSignSaberBase64 = null;
        
        String firmar_lotes_extraparams = fc.getProperty("firmar.lotes.extraparams");
        String firmar_lotes_signsaver_config_path = fc.getProperty("firmar.lotes.signsaver.config");
        firmar_lotes_extraparams = firmar_lotes_extraparams.concat("\n");
        
        String firmar_lotes_extraparams_sigem = "";
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("datasource=");
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(firmar_lotes_signsaver_config_path.substring(firmar_lotes_signsaver_config_path.indexOf("=") + 1));
        
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("codent=");
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat(entityId);
        
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("\n");
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat("guid=");
        firmar_lotes_extraparams_sigem = firmar_lotes_extraparams_sigem.concat((String)hashCodes.get(count));
        
        firmar_lotes_extraparams = firmar_lotes_extraparams.concat(firmar_lotes_extraparams_sigem);
        
        extraparamsBase64 = firmar_lotes_extraparams.getBytes();
        configSignSaberBase64 = firmar_lotes_signsaver_config_path.getBytes();
        
        Signbatch.Singlesign.Signsaver signsaver = peticion.createSignbatchSinglesignSignsaver();
        signsaver.setClazz(firmar_lotes_signsaver);
        signsaver.setConfig(configSignSaberBase64);
        
        Signbatch.Singlesign singlesign = peticion.createSignbatchSinglesign();
        singlesign.setId(Integer.toString(count));
        singlesign.setDatasource(url);
        singlesign.setFormat(firmar_lotes_format);
        singlesign.setSuboperation(firmar_lotes_suboperation);
        singlesign.setExtraparams(extraparamsBase64);
        singlesign.setSignsaver(signsaver);
        
        signbatch.getSinglesign().add(singlesign);
        
        count++;
      }
      try
      {
        JAXBContext jaxbContext = JAXBContext.newInstance(new Class[] { ObjectFactoryFirmaLotesPeticion.class });
        
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        
        StringWriter sw = new StringWriter();
        
        jaxbMarshaller.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
        jaxbMarshaller.marshal(signbatch, sw);
        result = result.concat(sw.toString());
      }
      catch (JAXBException e)
      {
        e.printStackTrace();
      }
      
      //Guardar en la sesion y en el form el xml generado para el envio al servidor de firma y 
      //los datos necesarios para volver a coger el control cuando termine el servidor de firma
      storeDocumentHashCodes(session, hashCodes);
      
      BatchSignForm batchSignForm = (BatchSignForm)form;
      
      batchSignForm.setSerialNumber(numeroSerie);
      batchSignForm.setListaDocs(result);
      batchSignForm.setCodEntidad(codEntidad);
      batchSignForm.setInfoFirma(infoFirmas);
      
      setMenu(session.getClientContext(), state, request);
      
      return mapping.findForward("sign");
    }
    int taskId = currentState.getTaskId();
    String numexp = state.getNumexp();
    
    ISPACInfo informacion = new ISPACInfo("No existen documentos a firmar", "", false);
    request.getSession().setAttribute("infoAlert", informacion);
    
    ActionForward actionForward = new ActionForward();
    actionForward.setPath("/showTask.do?numexp=" + numexp + "&taskId=" + taskId);
    actionForward.setRedirect(true);
    
    return actionForward;
  }
  
  public ActionForward batchSign(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
    throws Exception
  {
    IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
    ISignAPI signAPI = session.getAPI().getSignAPI();
    ClientContext cct = session.getClientContext();
    
    IState state = getCurrentState(request, session);
    
    BatchSignForm signForm = (BatchSignForm)form;
    
    String[] hashCodes = retrieveDocumentHashCodes(session);
    
    String stateTicket = getStateticket(request);
    
    String[] aStateTicket = stateTicket.split("\\|");
    String sIdTramite = "";
    if ((aStateTicket != null) && (aStateTicket.length > 11)) {
      sIdTramite = aStateTicket[5];
    }
    List<ItemBean> documents_ok = new ArrayList();
    List<Map<String, Object>> documents_ko = new ArrayList();
    
    String strQuery = "";
    //INICIO [dipucr-Felipe #907]
    if (!StringUtils.isEmpty(sIdTramite) || !sIdTramite.equals("0")){
    	strQuery = "WHERE ID_TRAMITE=" + sIdTramite + " AND ESTADOFIRMA = '00'";
    } else {
    	return getActionForwardAtrasNavegador(request);
    }
    //FIN [dipucr-Felipe #907]
    //[dipucr-Felipe #184]
    strQuery += " ORDER BY ID";
    		
    IItemCollection collAllDoc = DocumentosUtil.queryDocumentos(cct, strQuery);
    Iterator itcollAllDoc = collAllDoc.iterator();
    int count = 0;
    //[DipuCR-Agustin] #150 Agregar metadatos
  	String autorCert = UsuariosUtil.getNombreFirma(session.getClientContext());
    
    while (itcollAllDoc.hasNext())
    {
      IItem document = (IItem)itcollAllDoc.next();
      try
      {
        String estadoFirma = document.getString("ESTADOFIRMA");
        if ((!StringUtils.isBlank(estadoFirma)) && (!"00".equals(estadoFirma))) {
          throw new ISPACInfo("exception.signProcess.cannotSignDocument", true);
        }
        SignDocument signDocument = new SignDocument(document);
        signDocument.setIdPcd(state.getPcdId());
        signDocument.setNumExp(state.getNumexp());
        
        signDocument.addCertificate(signForm.getSignCertificate());
        signDocument.setHash(hashCodes[count]);
        
        signAPI.postsign(signDocument, hashCodes[count], true);
        
        documents_ok.add(new ItemBean(document));
        
        //[DipuCR-Agustin] #150 Agregar metadatos
		GestorMetadatos.storeMetada(session, signDocument, autorCert, null);
		
      }
      catch (ISPACException e)
      {
        logger.error("Error al firmar el documento:" + document.getKeyInt() + " - " + document.getString("NOMBRE") + " [" + document.getString("DESCRIPCION") + "]", e);
        
        Map<String, Object> map = new HashMap();
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
  
  private void setMenu(ClientContext cct, IState state, HttpServletRequest request)
    throws ISPACException
  {
    request.setAttribute("menus", MenuFactory.getSignedDocumentListMenu(cct, state, getResources(request)));
  }
  
	public ActionForward selectOption(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
			throws Exception{
		
		//INICIO [dipucr-Felipe #907]
		ClientContext cct = session.getClientContext();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState state = managerAPI.currentState(getStateticket(request));
		int taskId = state.getTaskId();
		if (taskId == 0){
			return getActionForwardAtrasNavegador(request);
		}
		else{//FIN [dipucr-Felipe #907]
			return mapping.findForward("selectOptionAllDocument");
		}
	}
  
  private String crearStateTicket(String[] stateTicket)
  {
    String resStaTic = "";
    for (int i = 0; i < stateTicket.length - 1; i++) {
      resStaTic = resStaTic + stateTicket[i] + "|";
    }
    resStaTic = resStaTic + stateTicket[(stateTicket.length - 1)];
    
    return resStaTic;
  }
  
  public ActionForward initSignCircuit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
    throws Exception
  { //Este es el metodo
    ClientContext cct = session.getClientContext();
    IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
    IState state = managerAPI.currentState(getStateticket(request));
    IState currentState = managerAPI.currentState(getStateticket(request));
    
    int circuitId = Integer.parseInt(request.getParameter(ActionsConstants.SIGN_CIRCUIT_ID));
    
    boolean envioDoc = initCircuitSign(request, session, response, circuitId);
    if (envioDoc) {
      return mapping.findForward("success");
    }
    int taskId = currentState.getTaskId();
    String numexp = state.getNumexp();
    
    ActionForward actionForward = new ActionForward();
    actionForward.setPath("/showTask.do?numexp=" + numexp + "&taskId=" + taskId);
    actionForward.setRedirect(true);
    
    return actionForward;
  }
  
  /**
   * [DipuCR-Agustin #124] modifico todo el metodo le agrego un control nuevo para el caso de pulsar 
   * @param request
   * @param session
   * @param response
   * @param circuitId
   * @return
   * @throws ISPACException
   */
  private boolean initCircuitSign(HttpServletRequest request, SessionAPI session, HttpServletResponse response, int circuitId)
    throws ISPACException
  {
	  
	IItem IDoc = null;
    List<ItemBean> documents_ok = new ArrayList<ItemBean>();
	List<Map<String, Object>> documents_ko = new ArrayList<Map<String, Object>>();
	ISignAPI signAPI = session.getAPI().getSignAPI();
    boolean docMandadoCircuitoFirma = false;    
    ClientContext cct = session.getClientContext();    
    String stateTicket = getStateticket(request);
	
	  
	try{    
		    String[] aStateTicket = stateTicket.split("\\|");
		    String sIdTramite = "";
		    if ((aStateTicket != null) && (aStateTicket.length > 11)) {
		      sIdTramite = aStateTicket[5];
		    }
		    String strQuery = "";
		    if (((sIdTramite != null ? 1 : 0) & (sIdTramite != "0" ? 1 : 0)) != 0) {
		      strQuery = "WHERE ID_TRAMITE=" + sIdTramite + " AND ESTADOFIRMA = '00'";
		    } else {
		      strQuery = "WHERE ID_TRAMITE=null AND ESTADOFIRMA = '00'";
		    }
		    IItemCollection collAllDoc = DocumentosUtil.queryDocumentos(cct, strQuery);
		    Iterator itcollAllDoc = collAllDoc.iterator();
		    
		    if (itcollAllDoc.hasNext())
		    {
		      docMandadoCircuitoFirma = true;
		      while (itcollAllDoc.hasNext())
		      {
		        IDoc = (IItem)itcollAllDoc.next();
		        String idDocum = IDoc.getString("ID");
		        signAPI.initCircuit(circuitId, Integer.parseInt(idDocum));
		      }
		      
		      documents_ok.add(new ItemBean(IDoc));
		      
		    }		    
		    else{
		    	ISPACInfo informacion = new ISPACInfo("No existen documentos a firmar o ya se han enviado a circuito de firma", "", false);
		        request.getSession().setAttribute("infoAlert", informacion);
		    }
		    
	} catch (ISPACException e) {
		
			logger.error(
					"Error al iniciar el circuito de firmas para el documento:"
							+ IDoc.getKeyInt() + " - "
							+ IDoc.getString("NOMBRE") + " ["
							+ IDoc.getString("DESCRIPCION") + "]", e);
	
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("documento", new ItemBean(IDoc));
			map.put("error", e.getExtendedMessage(request.getLocale()));
	
			documents_ko.add(map);
	}
	
	request.setAttribute("FIRMAR_TODO_OK", documents_ok);
	request.setAttribute("FIRMAR_TODO_KO", documents_ko);
        
    return docMandadoCircuitoFirma;
  }
  
  private IState enterState(HttpServletRequest request, SessionAPI session, HttpServletResponse response)
    throws ISPACException
  {
    ClientContext cct = session.getClientContext();
    IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
    
    IState state = managerAPI.currentState(getStateticket(request));
    if (state.getState() != 18)
    {
      String stateTicket = getStateticket(request);
      
      String[] aStateTicket = stateTicket.split("\\|");
      String sIdTramite = "";
      if ((aStateTicket != null) && (aStateTicket.length > 11)) {
        sIdTramite = aStateTicket[5];
      }
      String strQuery = "";
      if (((sIdTramite != null ? 1 : 0) & (sIdTramite != "0" ? 1 : 0)) != 0) {
        strQuery = "WHERE ID_TRAMITE=" + sIdTramite + " AND ESTADOFIRMA = '00'";
      } else {
        strQuery = "WHERE ID_TRAMITE=null AND ESTADOFIRMA = '00'";
      }
      IItemCollection collAllDoc = DocumentosUtil.queryDocumentos(cct, strQuery);
      Iterator itcollAllDoc = collAllDoc.iterator();
      if (itcollAllDoc.hasNext())
      {
        IItem IDoc = (IItem)itcollAllDoc.next();
        String idDocum = IDoc.getString("ID");
        
        aStateTicket[10] = idDocum;
        String sResStateTicket = crearStateTicket(aStateTicket);
        
        Map paramsKeyDoc = new HashMap();
        
        String[] keystr = new String[1];
        keystr[0] = idDocum;
        
        paramsKeyDoc.put("key", keystr);
        
        state = managerAPI.enterState(sResStateTicket, 18, paramsKeyDoc);
        storeStateticket(state, response);
      }
      else
      {
        ISPACInfo informacion = new ISPACInfo("No existen documentos a firmar", "", false);
        request.getSession().setAttribute("infoAlert", informacion);
      }
    }
    return state;
  }
  
  public ActionForward selectSignCircuit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
    throws Exception
  {
    ISignAPI signAPI = session.getAPI().getSignAPI();
    IState state = enterState(request, session, response);
    
    //INICIO [dipucr-Felipe #907]
	int taskId = state.getTaskId();
	if (taskId == 0){
		return getActionForwardAtrasNavegador(request);
	}
	//FIN [dipucr-Felipe #907]
    
    SignCircuitFilter filter = new SignCircuitFilter();
    filter.setPcdId(state.getPcdId());
    filter.setTaskPcdId(state.getTaskPcdId());
    
    filter.setIdSistema(ProcessSignConnectorFactory.getInstance().getProcessSignConnector().getIdSystem());
    filter.setDefaultPortafirmas(ProcessSignConnectorFactory.getInstance().isDefaultConnector());
    
    IItemCollection itemcol = signAPI.getCircuits(filter);
    List list = CollectionBean.getBeanList(itemcol);
    request.setAttribute(ActionsConstants.SIGN_CIRCUIT_LIST, list);
    
    IItemCollection itemcolTram = signAPI.getCircuitsTramite(filter);
    List listTram = CollectionBean.getBeanList(itemcolTram);
    request.setAttribute(ActionsConstants.SIGN_CIRCUITTRAM_LIST, listTram);
    
    return mapping.findForward("selectCircuitAllDocument");
  }
  
  private void storeDocumentHashCodes(SessionAPI session, List<String> hashCodes)
    throws ISPACException
  {
    ClientContext ctx = session.getClientContext();
    ctx.setSsVariable("SIGN_DOCUMENT_HASHCODES", StringUtils.join(hashCodes, ","));
  }
  
  private String[] retrieveDocumentHashCodes(SessionAPI session)
    throws ISPACException
  {
    ClientContext ctx = session.getClientContext();
    String hashCodesString = ctx.getSsVariable("SIGN_DOCUMENT_HASHCODES");
    
    return StringUtils.split(hashCodesString, ",");
  }
  
  private void storeDocumentIds(SessionAPI session, String[] documentIds)
    throws ISPACException
  {
    ClientContext ctx = session.getClientContext();
    ctx.setSsVariable("SIGN_DOCUMENT_IDS", StringUtils.join(documentIds, ","));
  }
  
  private String[] retrieveDocumentIds(SessionAPI session)
    throws ISPACException
  {
    ClientContext ctx = session.getClientContext();
    String documentIdsString = ctx.getSsVariable("SIGN_DOCUMENT_IDS");
    
    return StringUtils.split(documentIdsString, ",");
  }
  
  private IState getCurrentState(HttpServletRequest request, SessionAPI session)
    throws ISPACException
  {
    IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(session.getClientContext());
    return managerAPI.currentState(getStateticket(request));
  }
  
  /**
   * [dipucr-Felipe #907]
   * @return
   */
  public ActionForward getActionForwardAtrasNavegador(HttpServletRequest request){
	  ISPACInfo informacion = new ISPACInfo("Error en la aplicación - Atrás del navegador", "Por favor, no use el botón 'Atrás' del navegador cuando se encuentre dentro de SIGEM, "
	  		+ "pues este hecho provoca errores no deseados en la Aplicación. Cierre SIGEM y vuelva a abrirlo. Gracias", false);
	  request.getSession().setAttribute("infoAlert", informacion);
	  
	  ActionForward actionForward = new ActionForward();
	  actionForward.setPath("/showProcedureList.do");
	  actionForward.setRedirect(true);

	  return actionForward;
  }
  
//FIN [DipuCR-Agustin #94]
}