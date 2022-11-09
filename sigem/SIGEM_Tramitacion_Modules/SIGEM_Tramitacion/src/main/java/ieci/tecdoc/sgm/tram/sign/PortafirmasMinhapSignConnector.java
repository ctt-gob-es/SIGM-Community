package ieci.tecdoc.sgm.tram.sign;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ISessionAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.common.constants.SignStatesConstants;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitMgr;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;

import _0.v2.type.pfirma.cice.juntadeandalucia.Authentication;
import _0.v2.type.pfirma.cice.juntadeandalucia.Job;
import _0.v2.type.pfirma.cice.juntadeandalucia.Request;
import _0.v2.type.pfirma.cice.juntadeandalucia.SignLine;
import _0.v2.type.pfirma.cice.juntadeandalucia.SignLineList;
import _0.v2.type.pfirma.cice.juntadeandalucia.User;
import _0.v2.type.pfirma.cice.juntadeandalucia.UserJob;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;
import es.dipucr.sigem.api.rule.common.utils.CircuitosUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.GestorDecretos;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.portafirmas.DipucrDocPortafirmas;
import es.dipucr.sigem.portafirmas.DipucrDocPortafirmas.Estado;
import es.dipucr.sigem.rechazo.api.impl.SignRechazoAPI;
import es.ieci.webservice.portafirma.PortafirmasMinhapWebServiceClient;

public class PortafirmasMinhapSignConnector extends Sigm30SignConnector {

	/**
    * Logger de la clase.
    */
	private static final Logger logger = Logger.getLogger(PortafirmasMinhapSignConnector.class);
	
	/** APLICACIÓN PARA SESIONES **/
	private static String APLICACION_TRAMITADOR;
	static {
		try {
			APLICACION_TRAMITADOR = ISPACConfiguration.getInstance().get(ISPACConfiguration.TRAM_APPLICATION);
		} catch (ISPACException e) {
			ResourceBundle bundle = ResourceBundle.getBundle("ieci.tdw.ispac.api.errors.ISPACExceptionMessages");
			logger.error(bundle.getString("error.application.localization")+"\n"+e.toString());
		}
	}
	
	/**
    * Separador entre firmante y fecha
    */
	private static final String SIGNER_DATE_SEPARATOR = ";;";

	Authentication authentication;
	String urlQuery;
	String urlModify;
	String urlAdmin;
	
	public PortafirmasMinhapSignConnector() {
		super();
	}
	
	@Override
	public void initializate(SignDocument signDocument, IClientContext clientContext) {
		super.initializate(signDocument, clientContext);
		String idEntidad = EntidadesAdmUtil.obtenerEntidad(clientContext);
		inicializar(idEntidad);
	}
	
	/**
	* [dipucr-Felipe #1246] Modificamos el métodos para
	* - Gestionar los decretos
	* - Salvar el documento original
	* - Ejecutar los eventos de terminación del circuito
    * Realiza la firma del documento
    */
	public void sign(boolean changeState) throws ISPACException{

		DipucrDocPortafirmas docPortafirmas = new DipucrDocPortafirmas(clientContext, signDocument.getItemDoc());
		try{
			docPortafirmas.createNewOrGetExisting();
			
			// Control de números de decreto
			int idDoc = signDocument.getItemDoc().getKeyInt();
			if (Estado.CONTROL_DECRETOS.getCodigo() > docPortafirmas.getEstadoInicial()){
				if (GestorDecretos.isDocDecreto(signDocument) && GestorDecretos.esTramiteFirmaDecreto(clientContext, idDoc)
					&& GestorDecretos.esFirmaAlcaldiaPresidencia(clientContext, idDoc)) //[dipucr-Felipe #1564]
				{
					asignarNumeroDecreto();
				}
				docPortafirmas.updateEstado(Estado.CONTROL_DECRETOS);
			}
			
			if (Estado.JUSTIFICANTE.getCodigo() > docPortafirmas.getEstadoInicial()){
				generatePdfToSign(changeState);
				docPortafirmas.updateEstado(Estado.JUSTIFICANTE);
			}
			
			if (Estado.METADATOS_FIRMA.getCodigo() > docPortafirmas.getEstadoInicial()){
				storeInformacionFirmantes();
				docPortafirmas.updateEstado(Estado.METADATOS_FIRMA);
			}
	
			//Descarga de documento original
			if (Estado.DESCARGA_ORIGINAL.getCodigo() > docPortafirmas.getEstadoInicial()){
				savePortafirmasOriginalFile();
				docPortafirmas.updateEstado(Estado.DESCARGA_ORIGINAL);
			}
			
			//Ejecución de reglas
			if (Estado.PROCESADO.getCodigo() > docPortafirmas.getEstadoInicial()){
				//Lo volvemos en su estado inicial (antes de eventos) por si no es la primera pasada
				ponerEstadoDocumento(SignStatesConstants.FIRMADO);
				processCircuitEvents();
			}
			docPortafirmas.marcarProcesado();
		}
		catch(Exception ex){
			docPortafirmas.procesarError(ex);
			if (ex instanceof ISPACException){
				throw (ISPACException) ex;
			}
			else{
				throw new ISPACException(ex);
			}
		}
	}
	
	/**
	 * Pone el estado del documento a Firmado/Rechazado para no provocar error en las reglas
	 * que comprueban el tipo de documento
	 * @param firmado
	 * @throws ISPACException 
	 */
	private void ponerEstadoDocumento(String estado) throws ISPACException {

		IItem itemDocumento = signDocument.getItemDoc();
		itemDocumento.set("ESTADOFIRMA", estado);
		itemDocumento.store(clientContext);
	}

	/**
	 * [dipucr-Felipe]
	 * Asigna número de decreto al documento actual
	 * @throws ISPACException 
	 */
	private void asignarNumeroDecreto() throws ISPACException {

		String documentId = signDocument.getItemDoc().getString("ID");
		PortafirmasMinhapProcessSignConnector processConnector = new PortafirmasMinhapProcessSignConnector(clientContext);
		List<SignDetailEntry> listFirmas = processConnector.getSigns((ClientContext) clientContext, documentId);
		
		//Recuperamos por si acaso tuviera ya número de decreto
		String numDecretoActual = GestorDecretos.getNumDecretoActual(clientContext, signDocument);
		boolean tieneNumDecreto = StringUtils.isNotEmpty(numDecretoActual);
		
		if (tieneNumDecreto){
			signDocument.setNumDecreto(numDecretoActual);
		}
		else{
			String numDecreto = GestorDecretos.getNuevoNumDecreto(clientContext);
			GestorDecretos.actualizarValoresDecretoPortafirmas(clientContext, signDocument, numDecreto, listFirmas);
			
			signDocument.setNumDecreto(numDecreto);
		}
		
	}

	/**
	 * [dipucr-Felipe #1246]
	 * Recupera el documento original y lo guarda en el repositorio
	 * @throws ISPACException 
	 */
	private void savePortafirmasOriginalFile() throws ISPACException {

		try{
			PortafirmasMinhapProcessSignConnector pfConnector = new PortafirmasMinhapProcessSignConnector(clientContext);
			IItem itemDoc = signDocument.getItemDoc();
//			byte[] arrBytesDocumento = pfConnector.getDocument((ClientContext) clientContext, itemDoc.getString("ID"));
			byte[] arrBytesDocumento = pfConnector.getSignsContent((ClientContext) clientContext, itemDoc.getString("ID"));
			
			final String EXTENSION = "pdf";
			FileTemporaryManager fileMgr = FileTemporaryManager.getInstance();
			File file = fileMgr.newFile("." + EXTENSION);
			FileUtils.writeFile(file, arrBytesDocumento);
			
			String sMimeType = MimetypeMapping.getMimeType(EXTENSION);
			String doctype = itemDoc.getString("NOMBRE");
			
			//Componemos los metadatos del documento firmado a guardar
			SignDocument signDocumentOriginal = new SignDocument(itemDoc);
			signDocumentOriginal.setDocument(new FileInputStream(file));
			signDocumentOriginal.setDocumentType(doctype);
			signDocumentOriginal.setLength((int)file.length());
			signDocumentOriginal.setMimetype(sMimeType);
			signDocumentOriginal.setFechaCreacion(new Date());
				
			//Se almacena el documento firmado
			String infoPagRDE = store(signDocumentOriginal);
				
			// Eliminar el fichero PDF una vez subido al gestor documental
			file.delete();
		
			//Actualizamos el documento
			itemDoc.set("INFOPAG_RDE_ORIGINAL", infoPagRDE);
			itemDoc.store(clientContext);
		}
		catch(Exception ex){
			String error = "Error al guardar el original firmado del portafirmas: " + ex.getMessage();
			logger.error(error, ex);
			throw new ISPACException(error, ex);
		}
		
	}
	
	/**
	 * [dipucr-Felipe #1246]
	 * Sobrecargamos el método original para poder pasarle por parámetro un documento
	 * @param signDocument
	 * @return
	 * @throws ISPACException
	 */
	public String store(SignDocument signDocument) throws ISPACException {
		
		String docref = null;

		Integer rdeArchiveId = getRdeArchiveId();
		IGenDocAPI genDocAPI = clientContext.getAPI().getGenDocAPI();
		Object connectorSession = null;

		try {
			connectorSession = genDocAPI.createConnectorSession();
			docref = genDocAPI.newDocument(connectorSession, rdeArchiveId,
					signDocument.getDocument(), signDocument.getLength(),
					signDocument.getProperties());

			// Cerramos el fichero
			signDocument.getDocument().close();
			
		} catch (ISPACException e) {
			logger.error("Error al guardar el documento firmado", e);
			throw e;
		} catch (Exception e) {
			logger.error("Error al guardar el documento firmado", e);
			throw new ISPACException(e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
		return docref;
	}

	/**
	 * [dipucr-Felipe #1246]
	 * Ejecutamos los eventos de terminación del circuito
	 * @throws ISPACException 
	 */
	private void processCircuitEvents() throws ISPACException {
		processCircuitEvents(false);
	}
	
	/**
	 * [dipucr-Felipe #1246]
	 * Ejecutamos los eventos de terminación del circuito
	 * @throws ISPACException 
	 */
	private void processCircuitEvents(boolean bRechazado) throws ISPACException {
		
		ClientContext cct = getUserContext();
		SignCircuitMgr manager = new SignCircuitMgr(cct);
		
		IItem itemDoc = signDocument.getItemDoc();
		
		//En los casos de "mandar a mi firma", id_circuito irá vacío y por tanto no lanzaremos eventos
		if (!StringUtils.isEmpty(itemDoc.getString("ID_CIRCUITO"))){

			int circuitId = itemDoc.getInt("ID_CIRCUITO");
			
			//Circuitos al vuelo firma de convenios
			if (CircuitosUtil.CIRCUITO_VUELO == circuitId){
				if (bRechazado){
					mandarAvisoCircuitoAlVuelo(itemDoc, CircuitosUtil.CIRCUITO_VUELO_AVISO_RECHAZADO);
				}
				else{
					mandarAvisoCircuitoAlVuelo(itemDoc, CircuitosUtil.CIRCUITO_VUELO_AVISO_FIRMADO);
				}
			}
			else{
				int documentId = itemDoc.getKeyInt();
				int instancedCircuitId = Integer.MIN_VALUE;//No hay circuito instanciado. Ponemos valor nulo
				
				manager.processCircuitEvents(EventsDefines.EVENT_EXEC_END, circuitId, instancedCircuitId, documentId);
			}
		}
	}
	
	/**
	 * Para los circuitos creados al vuelo (sin eventos), mandamos aviso al tramitador
	 * @param aviso
	 * @throws ISPACException 
	 */
	private void mandarAvisoCircuitoAlVuelo(IItem itemDoc, String aviso) throws ISPACException{
		
		IInvesflowAPI invesflowAPI = clientContext.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		
		String numexp = itemDoc.getString("NUMEXP");
		int idProc = invesflowAPI.getProcess(numexp).getInt("ID");
		String idResp = itemDoc.getString("AUTOR");
		
		AvisosUtil.generarAviso(entitiesAPI, idProc, numexp, aviso, idResp, clientContext);
	}
	

	/**
	 * [dipucr-Felipe #1246]
	 * Pone en el contexto al usuario genérico PORTAFIRMAS necesario para la ejecución de reglas y creación de documentos
	 * @return
	 * @throws ISPACException
	 */
	private ClientContext getUserContext() throws ISPACException {
		
		try{
			ClientContext cct = (ClientContext) clientContext;
			String usuarioPortafirmas = Constants.DEFAULT_USER_PORTAFIRMAS;
			
			//Seteamos el usuario PORTAFIRMAS por defecto
			ServicioEstructuraOrganizativa servicioEO = LocalizadorServicios.getServicioEstructuraOrganizativa();
			Usuario usuario = servicioEO.getUsuario(Constants.DEFAULT_USER_PORTAFIRMAS, EntidadesAdmUtil.obtenerEntidad(clientContext));
			ieci.tdw.ispac.ispaclib.resp.User user = new ieci.tdw.ispac.ispaclib.resp.User("1-" + usuario.get_id(), usuario.get_name());
			cct.setUser(user);
			
			//Seteamos el stateContext
			String numexp = signDocument.getNumExp();
			IProcess exp = cct.getAPI().getProcess(numexp);
            StateContext stateContext = cct.getStateContext();
            if (stateContext == null) {
                stateContext = new StateContext();
            }
            stateContext.setPcdId(exp.getInt("ID_PCD"));
            stateContext.setProcessId(exp.getKeyInt());
			stateContext.setNumexp(signDocument.getNumExp());
			cct.setStateContext(stateContext);
			
			//Sesión
			ISessionAPI sessionAPI = SessionAPIFactory.getSessionAPI();
			IDirectoryEntry userEntry = sessionAPI.validate(usuarioPortafirmas, Constants.DEFAULT_USER_PORTAFIRMAS_PWD);
			sessionAPI.login("localhost", usuarioPortafirmas, userEntry, APLICACION_TRAMITADOR, new Locale("es_ES"));
			cct.setTicket(sessionAPI.getTicket());
			
			return cct;
		}
		catch(Exception ex){
			throw new ISPACException("Error al establecer el contexto de usuario: " + ex.getMessage(), ex);
		}
	}

	/**
    * Almacena la información de los firmantes
    *
    * @throws ISPACException
    */
	private void storeInformacionFirmantes() throws ISPACException {
		IGenDocAPI genDocAPI = clientContext.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			String infoPagRDE = signDocument.getItemDoc().getString("INFOPAG_RDE");

			if (logger.isInfoEnabled()){
				logger.info("Actualizando idTransaccion sobre el documento RDE: " + infoPagRDE);
			}

			List signers = getSignersListInfo(signDocument, true);
			connectorSession = genDocAPI.createConnectorSession();
			XmlFacade xmlFacade = new XmlFacade("<firmas/>");
			String xPath = null;
			xPath = "/firmas";
			xmlFacade.set(xPath+"/@tipo", "Portafirmas");

			int i = 0;
			if (signers!=null){
				Iterator it = signers.iterator();
				String encodedSigner = null;
				while (it.hasNext()){
					xPath = "/firmas/firma[@id='"+Integer.toString(i)+"']";
					encodedSigner = new String(Base64.encode(((String)it.next()).getBytes()));
					xmlFacade.set(xPath, encodedSigner);
					i++;
				}
			}

			genDocAPI.setDocumentProperty(connectorSession, infoPagRDE, "Firma", xmlFacade.toString() );
			logger.debug(xmlFacade.toString());
		}
		finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
	}

	/**
    * {@inheritDoc}
    * @see ieci.tecdoc.sgm.tram.sign.Sigem23SignConnector#getSignerList(ieci.tdw.ispac.ispaclib.sign.SignDocument)
    */
	//INICIO [dipucr-Felipe #1246]
//	protected List getSignerList(SignDocument signDocument) throws ISPACException {
//
//		return getSignersListInfo(signDocument, false);
//	}
	@Override
	protected List getSignerList(SignDocument signDocument) throws ISPACException {
		
		List<SignDetailEntry> signerList = new ArrayList<SignDetailEntry>();
		
		PortafirmasMinhapProcessSignConnector connector = new PortafirmasMinhapProcessSignConnector(clientContext);
		String sIdDoc = signDocument.getItemDoc().getString("ID");
		signerList = connector.getSigns((ClientContext) clientContext, sIdDoc);
					
		return signerList;
	}


	/**
    * Permite obtener la informacion de los firmantes indicando si se proporciona la fecha de la firma
    * @param signDocument documento firmando
    * @param withSignDate indica si se devuelve por cada firmante la fecha de firma
    * @return
    * @throws ISPACException
    */
	@Deprecated  /** [dipucr-Felipe #1246] En el método getSignerList, recuperamos la lista del processSignConnector**/
	private List getSignersListInfo(SignDocument signDocument, boolean withSignDate)
			throws ISPACException {
		PortafirmasMinhapWebServiceClient portafirmasWsClient;
		try {
			portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);

			IInvesflowAPI invesflowAPI = clientContext.getAPI();
			IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
			String idProcesoFirma = signDocument.getItemDoc().getString("ID_PROCESO_FIRMA");

			XmlFacade xmlFacade = new XmlFacade(idProcesoFirma);
			String requestHash = xmlFacade.get("/process/requestHash");
			String docHash = xmlFacade.get("/process/docHash");
			Request request = portafirmasWsClient.getRequest(requestHash);

			List signers = new ArrayList();
			SignLineList signLineList = request.getSignLineList();
			String signerDescription = null;
			UserJob userJob = null;
			User user = null;
			Job job = null;
			Calendar calendar = null;
			_0.v2.type.pfirma.cice.juntadeandalucia.SignerList signerList = null;
			_0.v2.type.pfirma.cice.juntadeandalucia.Signer signer = null;
			for (SignLine signLine : signLineList.getSignLine()){
				signerList = signLine.getSignerList();

				if ((signerList!=null)&&(signerList.getSigner()!=null)&&(signerList.getSigner().length>0)){
					signer = signerList.getSigner(0);
					calendar = signer.getFstate();
					userJob = signer.getUserJob();
					if (userJob instanceof User){
						user = (User) userJob;
						signerDescription = user.getName() + ((user.getSurname1()!=null)?" "+user.getSurname1():"") + ((user.getSurname2()!=null)?" "+user.getSurname2():"");
					} else if (userJob instanceof Job){
						job = (Job) userJob;
						signerDescription = job.getDescription();
					}
					if (withSignDate){
						signers.add(signerDescription+SIGNER_DATE_SEPARATOR+getSignDateFormatter(DATE_FORMATTER).format(calendar.getTime()));
					} else {
						signers.add(signerDescription);
					}
				}
			}

			return signers;
		} catch (MalformedURLException e) {
			throw new ISPACException(e);
		} catch (ServiceException e) {
			throw new ISPACException(e);
		} catch (Exception e) {
			throw new ISPACException(e);
		}
	}

	/**
	 * [dipucr-Felipe]
	 * Realiza el rechazo del documento
	 * @param changeState
	 * @param motivo
	 * @param nif
	 * @param nombre
	 * @throws ISPACException 
	 */
	public void rechazarDocumento(boolean changeState, String motivo, String nif, String nombre) throws ISPACException {
		
		DipucrDocPortafirmas docPortafirmas = new DipucrDocPortafirmas(clientContext, signDocument.getItemDoc());
		try{
			docPortafirmas.createNewOrGetExisting();
			docPortafirmas.updateEstado(Estado.SIN_RECHAZAR);
			
			if (Estado.RECHAZADO.getCodigo() > docPortafirmas.getEstadoInicial()){

				SignRechazoAPI signRechazoAPI = new SignRechazoAPI((ClientContext) clientContext);
				signRechazoAPI.rechazar(signDocument, changeState, motivo, nombre);
			}
			docPortafirmas.updateEstado(Estado.RECHAZADO);
			
			//Ejecución de reglas
			if (Estado.PROCESADO.getCodigo() > docPortafirmas.getEstadoInicial()){
				//Lo volvemos en su estado inicial (antes de eventos) por si no es la primera pasada
				ponerEstadoDocumento(SignStatesConstants.RECHAZADO);
				processCircuitEvents(true);
			}
			docPortafirmas.marcarProcesado();
		}
		catch(Exception ex){
			docPortafirmas.procesarError(ex);
			if (ex instanceof ISPACException){
				throw (ISPACException) ex;
			}
			else{
				throw new ISPACException(ex);
			}
		}
	}
	
	/**
	 * Sobrecarga del método sólo para que vuelva a procesar los eventos
	 * @param changeState
	 * @throws ISPACException
	 */
	public void rechazarDocumento(boolean changeState) throws ISPACException {
		rechazarDocumento(changeState, null, null, null);
	}
	
	public boolean inicializar(String entidadId){
		authentication = new Authentication();
		try {
			authentication.setUserName(FirmaConfiguration.getInstance(entidadId).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_USER));
			authentication.setPassword(FirmaConfiguration.getInstance(entidadId).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_PASSWORD));

			urlQuery = FirmaConfiguration.getInstance(entidadId).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_QUERY_URL);
			urlModify = FirmaConfiguration.getInstance(entidadId).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_MODIFY_URL);
			urlAdmin = FirmaConfiguration.getInstance(entidadId).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_ADMIN_URL);
			
		} catch (ISPACException e) {
			logger.error(e);
		}
		
		return true;
	}
}
