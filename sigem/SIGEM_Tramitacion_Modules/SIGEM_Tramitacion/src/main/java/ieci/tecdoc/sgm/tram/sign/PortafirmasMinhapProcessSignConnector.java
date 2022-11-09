package ieci.tecdoc.sgm.tram.sign;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.expedients.Document;
import ieci.tdw.ispac.api.impl.SignAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitDetailDAO;
import ieci.tdw.ispac.ispaclib.dao.sign.SignCircuitHeaderDAO;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.ProcessSignProperties;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.Signer;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.InetUtils;
import ieci.tdw.ispac.ispaclib.utils.MimetypeMapping;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Departamento;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Grupo;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.ServicioEstructuraOrganizativa;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.UsuarioData;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuarios;
import ieci.tecdoc.sgm.tram.helpers.EntidadHelper;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import _0.v2.type.pfirma.cice.juntadeandalucia.Authentication;
import _0.v2.type.pfirma.cice.juntadeandalucia.CommentList;
import _0.v2.type.pfirma.cice.juntadeandalucia.DocumentType;
import _0.v2.type.pfirma.cice.juntadeandalucia.EnhancedUser;
import _0.v2.type.pfirma.cice.juntadeandalucia.EnhancedUserJobInfo;
import _0.v2.type.pfirma.cice.juntadeandalucia.EnhancedUserList;
import _0.v2.type.pfirma.cice.juntadeandalucia.ImportanceLevel;
import _0.v2.type.pfirma.cice.juntadeandalucia.Job;
import _0.v2.type.pfirma.cice.juntadeandalucia.Parameter;
import _0.v2.type.pfirma.cice.juntadeandalucia.ParameterList;
import _0.v2.type.pfirma.cice.juntadeandalucia.RemitterList;
import _0.v2.type.pfirma.cice.juntadeandalucia.Request;
import _0.v2.type.pfirma.cice.juntadeandalucia.Seat;
import _0.v2.type.pfirma.cice.juntadeandalucia.SignLine;
import _0.v2.type.pfirma.cice.juntadeandalucia.SignLineList;
import _0.v2.type.pfirma.cice.juntadeandalucia.SignLineType;
import _0.v2.type.pfirma.cice.juntadeandalucia.SignType;
import _0.v2.type.pfirma.cice.juntadeandalucia.User;
import _0.v2.type.pfirma.cice.juntadeandalucia.UserJob;
import _0.v2.type.pfirma.cice.juntadeandalucia.UserList;
import es.dipucr.sigem.api.rule.common.firma.FirmaConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import es.dipucr.sigem.api.rule.common.utils.EntidadesUtil;
import es.dipucr.sigem.api.rule.common.utils.FechasUtil;
import es.dipucr.sigem.portafirmas.UsuarioRechazo;
import es.ieci.webservice.portafirma.PortafirmasMinhapWebServiceClient;

/**
 * Implementacion del conector de procesos de firma para el portafirmas Minhap
 * @author Iecisa
 * @version $Revision$
 *
 */
public class PortafirmasMinhapProcessSignConnector implements ProcessSignConnector {

	/**
    * Valor a introducir en la entidad
    */
	public static final String REFERENCE_SEPARATOR = "_";
	
	/**
	 * [dipucr-Felipe #1352] Separador entre nombres de firmantes de la misma línea de firma
	 */
	public static final String SIGNER_SEPARATOR = "<br/>";

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(PortafirmasMinhapProcessSignConnector.class);

	/**
	 * Authenticacion de los servicios
	 */
	private static Authentication authentication = null;
	
	/**
	 * URL del servicio de consulta
	 */
	private static String urlQuery = null;

	/**
	 * URL del servicio de modificacion 
	 */
	private static String urlModify = null;
	
	/**
	 * URL del servicio de admin 
	 */
	private static String urlAdmin = null;
	
	/**
	 * Formato de la fecha para la firma 
	 */
	public final static String DATE_FORMAT = "DATE_FORMAT";
	
	/**
	 * Formato por defecto para la fecha de la firma.
	 */
	protected static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Parámetro para indicar el email al crear o modificar usuarios.
	 */
	public static final String PARAMETRO_EMAIL = "email";
	
	/**
	 * [dipucr-Felipe #1246] Longitud máxima del asunto del portafirmas
	 */
	public static final int MAX_LENGTH_ASUNTO = 250;
	
	/**
	 * [dipucr-Felipe #1422] Nombre por defecto para usuarios y grupos de la E.Org. de ALSIGM
	 */
	public static final String USERGROUP_DEFAULT_NAME = "ALSIGM";
	
	/**
	 * Constructor.
	 */
	public PortafirmasMinhapProcessSignConnector() {
		super();
	}
	
	public PortafirmasMinhapProcessSignConnector(String entidadId) {
		super();
		
		inicializar(entidadId);
	}
	
	public PortafirmasMinhapProcessSignConnector(IClientContext cct) {
		super();
		
		String entidadId = EntidadesAdmUtil.obtenerEntidad(cct);
		inicializar(entidadId);
	}

	/**
	 * [dipucr-Felipe #1246 #1305] Sobrecargamos el método getRequestStatus 
	 * Devolvemos el request
	 *
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getState(ieci.tdw.ispac.ispaclib.context.ClientContext,
	 *      java.lang.String)
	 */
	public Request getRequest(ClientContext ctx, String processId)
			throws ISPACException {

		if (logger.isDebugEnabled()) {
			logger.info("MptProcessSignConnectorImpl->getState. ProcessId: "+processId);
		}
		
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);

			XmlFacade xmlFacade = new XmlFacade(processId);
			String requestHash = xmlFacade.get("/process/requestHash");
			Request request = portafirmasWsClient.getRequest(requestHash);
			return request;

		} catch (MalformedURLException e) {
			logger.error("No se ha podido acceder al servicio: '" + urlQuery);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlQuery);
			throw new ISPACException(e);
		} catch (Exception e){
			logger.error("Se ha producido un error al acceder al servicio: '" + urlQuery);
			throw new ISPACException(e);
		}
	}
	
	/**
	 * En conector por defecto no maneja estados distintos a los que se muestran ya en en
	 * la aplicacion (pendiente de firma, firmado, sin firma...)
	 *
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getState(ieci.tdw.ispac.ispaclib.context.ClientContext,
	 *      java.lang.String)
	 */
	public String getState(ClientContext ctx, String processId)
			throws ISPACException {

		return getRequest(ctx, processId).getRequestStatus().getValue();
	}

	/**
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getDocument(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String)
	 */
	public byte[] getDocument(ClientContext ctx, String documentId) throws ISPACException {

		try {
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			IItem itemDoc = entitiesAPI.getDocument(Integer.parseInt(documentId));

			String idProcesoFirma = itemDoc.getString("ID_PROCESO_FIRMA");


			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);

			XmlFacade xmlFacade = new XmlFacade(idProcesoFirma);
			String docHash = xmlFacade.get("/process/docHash");
			return portafirmasWsClient.getDocument(docHash);
		} catch (MalformedURLException e) {
			logger
			.error("No se ha podido acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (Exception e){
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		}
	}

	/**
	 * Permite obtener un documento no firmado
	 * @param ctx contexto
	 * @param documentId identificador del documento
	 * @return bytes del documento
	 * @throws ISPACException
	 */
	@SuppressWarnings("unused")
	private byte[] getNotSignedDocument(ClientContext ctx, String documentId) throws ISPACException {
		IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
		IItem itemDoc = entitiesAPI.getDocument(Integer.parseInt(documentId));
		String infoPageRde = itemDoc.getString("INFOPAG");
		IGenDocAPI genDocAPI = ctx.getAPI().getGenDocAPI();
		Object connectorSession = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		connectorSession = genDocAPI.createConnectorSession();
		if (!genDocAPI.existsDocument(connectorSession, infoPageRde)) {
			logger
					.error("No se ha encontrado el documento fisico con identificador: '"
							+ infoPageRde
							+ "' en el repositorio de documentos");
			throw new ISPACInfo("exception.documents.notExists", false);
		}

		genDocAPI.getDocument(connectorSession, infoPageRde, baos);
		return baos.toByteArray();
	}
	
	/**
	 * Este metodo no aplica en el conector por defecto ya que se seguira tirando de la estructura organizativa como siempre
	 * y se permitira navegar a traves de la misma a la hora de seleccionar u
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getSigners(ieci.tdw.ispac.ispaclib.context.ClientContext)
	 */
	public List<Signer> getSigners(ClientContext ctx, String query) throws ISPACException {

		if (logger.isDebugEnabled()) {
			logger
					.info("MptProcessSignConnectorImpl->getSigners. Query: "+query);
		}
		
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			
			String wildcardQuery = StringUtils.EMPTY;
			if (StringUtils.isNotEmpty(query)){
				wildcardQuery = "%"+query+"%";
			}
			List <Signer> signers = getSigners(portafirmasWsClient.getUsers(wildcardQuery));

			if (logger.isDebugEnabled()) {
				logger.info("ViewSignersAction->Hemos obtenido " + signers.size()
						+ " firmantes");
			}
			return signers;

		} catch (MalformedURLException e) {
			logger
			.error("No se ha podido acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (Exception e){
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		}
	}

	/**
	 * [dipucr-Felipe #1352]
	 * Sobrecargamos el método
	 */
	public List <SignDetailEntry> getSigns(ClientContext ctx, String documentId) throws InvalidSignatureValidationException, ISPACException {
		return getSigns(ctx, documentId, false);
	}
	
	/**
	 * Este metodo no tiene aplicacion en la implementacion por defecto ya que el portafirmas por defecto no maneja estados,
	 * son los mismo que la propia aplicacion : Sin firma, firmado, pendiente de firma...
	 * {@inheritDoc}
	 * @param includeSubstitutes - [dipucr-Felipe #1352] Incluye todos los firmantes de una línea (autorizados)
	 * @throws ISPACException
	 * @throws InvalidSignatureValidationException
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getSigns(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String)
	 */
	public List <SignDetailEntry> getSigns(ClientContext ctx, String documentId, boolean includeSubstitutes) throws InvalidSignatureValidationException, ISPACException {
		
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);

			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			IItem itemDoc = entitiesAPI.getDocument(Integer.parseInt(documentId));
			String idProcesoFirma = itemDoc.getString("ID_PROCESO_FIRMA");

			XmlFacade xmlFacade = new XmlFacade(idProcesoFirma);
			String requestHash = xmlFacade.get("/process/requestHash");
			Request request = portafirmasWsClient.getRequest(requestHash);
			
			List <SignDetailEntry> signDetailEntries = new ArrayList<SignDetailEntry>();
			SignLineList signLineList = request.getSignLineList();
			SignDetailEntry entry = null;
			UserJob userJob = null;
			User user = null;
			Job job = null;
			_0.v2.type.pfirma.cice.juntadeandalucia.SignerList signerList = null;
			_0.v2.type.pfirma.cice.juntadeandalucia.Signer signer = null;
			
			//[dipucr-Felipe #1246] Obtener el cargo de cada uno de los firmantes
			String idEntidad = EntidadHelper.getEntidad().getIdentificador();
			ServicioEstructuraOrganizativa servicioEO = LocalizadorServicios.getServicioEstructuraOrganizativa();
			
			for (SignLine signLine : signLineList.getSignLine()){
				signerList = signLine.getSignerList();
				
				if ((signerList!=null)&&(signerList.getSigner()!=null)&&(signerList.getSigner().length>0)){
					
					signer = signerList.getSigner(0);
					entry = new SignDetailEntry();
					userJob = signer.getUserJob();
					if (userJob instanceof User){
						user = (User) userJob;
						entry.setAuthor(getUserCompleteName(user));
						
						//INICIO [dipucr-Felipe #1352]
						//Si hay autorizados/sustitutos, sacamos todos los firmantes de la linea de firma en el detalle
						if (includeSubstitutes && signerList.getSigner().length > 1){
							String authors = entry.getAuthor();
							for (int i = 1; i < signerList.getSigner().length; i++){
								_0.v2.type.pfirma.cice.juntadeandalucia.Signer signerSubstitute = signerList.getSigner(i);
								User userSubstitute = (User) signerSubstitute.getUserJob();
								authors += (SIGNER_SEPARATOR + getUserCompleteName(userSubstitute));
							}
							entry.setAuthor(authors);
						}
						//FIN [dipucr-Felipe #1352]
						
					} else if (userJob instanceof Job){
						job = (Job) userJob;
						entry.setAuthor(job.getDescription());
					}

					//[dipucr-Felipe #1246]
					entry.setIdentifier(user.getIdentifier());
					
					if ("FIRMADO".equals(signer.getState().getIdentifier())){
						entry.setFirmado(true);
						entry.setRechazado(false);
					} else {
						entry.setFirmado(false);
						//INICIO [dipucr-Felipe #1246]
						if ("DEVUELTO".equals(signer.getState().getIdentifier())){
							entry.setRechazado(true);
						}
						else{
							entry.setRechazado(false);
						}
					}
					
					if (signer.getFstate()!=null){						
						/**
						 * [Dipucr-Manu Ticket #1658] * ALSIGM3 No se guarda la hora de la firma del presidente y el fedatario en decretos
						 * 
						 * Como no sé si esos formatters de la fecha se usan en más sitio, solo doy el formato bueno a la fecha de los firmantes.
						 * 
						 */
//						entry.setSignDate(getSignDateFormatter(DATE_FORMATTER).format(signer.getFstate().getTime()));

						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						entry.setSignDate(sdf.format(signer.getFstate().getTime()));
						
						/**
						 * Fin modificaciones ticket #1658
						 */
					}
					
//					entry.setIntegrity(SignAPI.INTEGRIDAD_NO_APLICA);//[dipucr-Felipe #1246]
					entry.setIntegrity(SignAPI.INTEGRIDAD_PORTAFIRMAS);
					
					// [dipucr-Felipe #1246] Obtención del cargo
					// [dipucr-Felipe #1514] Un empleado puede tener varios cargos dentro de la entidad
					String cargo = "";
					try{
						Usuarios usuarios = servicioEO.getUsuariosByNif(user.getIdentifier(), idEntidad);
						cargo = getCargo(ctx, usuarios.get_list(), itemDoc);
					}
					catch(Exception e) {
						//[Ticket1270 Teresa] El usuario es un firmante externo (firma de contratos) y no está en la estructura organizativa
						logger.info("El usuario " + user.getIdentifier() + "es un firmante externo y no está en la estructura organizativa");
					}
//					entry.setCargo(usuario.getUserData().getCargo()); //Usamos la descripcion
					entry.setCargo(cargo);
					
					signDetailEntries.add(entry);
				}
			}
			return signDetailEntries;
		} catch (MalformedURLException e) {
			logger
			.error("No se ha podido acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (Exception e){
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		}
	}

	/**
	 * [dipucr-Felipe #1514]
	 * @param user
	 * @param idEntidad
	 * @param servicioEO
	 * @param cargo
	 * @return
	 */
	private String getCargo(IClientContext cct, List<Usuario> listUsuarios, IItem itemDocumento) throws Exception{
		
		String cargo = "";
		
		if (listUsuarios.size() > 0){
			if (listUsuarios.size() == 1){//Un único usuario por NIF, lo habitual
				cargo = listUsuarios.get(0).get_description();
			}
			else{
				ISignAPI signAPI = cct.getAPI().getSignAPI();
				String sCircuito = itemDocumento.getString("ID_CIRCUITO");
				
				if (StringUtils.isEmpty(sCircuito)){//Firmar ahora / enviar a mi firma
					cargo = listUsuarios.get(0).get_description();
				}
				else{//Circuito de firma
					boolean encontrado = false;
					IItemCollection colCircuitSteps = SignCircuitDetailDAO.getSteps(cct.getConnection(), Integer.parseInt(sCircuito)).disconnect();
					@SuppressWarnings("unchecked")
					List<IItem> listCircuitSteps = colCircuitSteps.toList();
					
					//Recorremos por cada usuario los pasos del circuito
					for (int i = 0; i < listUsuarios.size() && !encontrado; i++){
						Usuario usuario = listUsuarios.get(i);
						String userName = usuario.get_name();
						
						for (int j=0; j < listCircuitSteps.size() && !encontrado; j++){
							IItem itemStep = listCircuitSteps.get(j);
							if (userName.equals(itemStep.getString("NOMBRE_FIRMANTE"))){
								encontrado = true;
								cargo = listUsuarios.get(i).get_description();
							}
						}
					}
					if (!encontrado){
						cargo = listUsuarios.get(0).get_description();
					}
				}
			}
		}
			
		return cargo;
	}

	/**
	 * [dipucr-Felipe #1246]
	 * @param user
	 * @return
	 */
	public String getUserCompleteName(User user) {
		return getUserCompleteName(user.getName(), user.getSurname1(), user.getSurname2());
	}
	
	public String getUserCompleteName(String name, String surname1, String surname2){
		
		StringBuffer sbName = new StringBuffer();
		sbName.append(name);
		if (!StringUtils.isEmpty(surname1)){
			sbName.append(" " + surname1);
		}
		if (!StringUtils.isEmpty(surname2)){
			sbName.append(" " + surname2);
		}
		return sbName.toString();
	}

	/**
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getSignsContent(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String)
	 */
	public byte[] getSignsContent(ClientContext ctx, String documentId) throws InvalidSignatureValidationException, ISPACException {
		
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);

			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			IItem itemDoc = entitiesAPI.getDocument(Integer.parseInt(documentId));
			String idProcesoFirma = itemDoc.getString("ID_PROCESO_FIRMA");

			XmlFacade xmlFacade = new XmlFacade(idProcesoFirma);
			String docHash = xmlFacade.get("/process/docHash");
			
			return portafirmasWsClient.getSigns(docHash).getContent();
		} catch (MalformedURLException e) {
			logger
			.error("No se ha podido acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (Exception e){
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		}
	}
	
	/**
	 * En la implementacion por defecto el ID_PROCESO_FIRMA y el ID seran el mismo.
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#initSignProcess(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String, ieci.tdw.ispac.api.expedients.Document)
	 */
	/** [dipucr-Felipe #1246] Añadimos el parámetro userSign para los casos de envío a mi propia firma
	 *  Sobrecargamos el método
	 * @param ctx
	 * @param processTemplateId
	 * @param userSign
	 * @param document
	 * @param properties
	 * @return
	 * @throws ISPACException
	 */
	//Id de circuito
	public String initSignProcess(ClientContext ctx, String processTemplateId,
			Document document, ProcessSignProperties properties) throws ISPACException {
		return initSignProcess(ctx, processTemplateId, null, document, properties);
	}
	
	//Lista de usuarios
	public String initSignProcess(ClientContext ctx, List<Usuario> listUsuarios,
			Document document, ProcessSignProperties properties) throws ISPACException {
		return initSignProcess(ctx, null, listUsuarios, document, properties);
	}
	
	//Funcionalidad
	private String initSignProcess(ClientContext ctx, String processTemplateId, List<Usuario> listUsuarios,
			Document document, ProcessSignProperties properties) throws ISPACException {

		String xmlProcesoFirma = null;
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			
			Request request = new Request();
			
			// Aplicacion 
			request.setApplication(FirmaConfiguration.getInstance(ctx).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_APPLICATION));

			// Usuario de envio
			//INICIO [dipucr-Felipe #1246]
			IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			IItem itemDoc = entitiesAPI.getDocument(Integer.parseInt(document.getId()));
			
//			UserList users = portafirmasWsClient.getUsers(FirmaConfiguration.getInstance(ctx).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_USER));
			RemitterList remitters = new RemitterList();
//			remitters.setUser(users.getUser());

			//[dipucr-Felipe #1422]
			User remitter = getRemitter(ctx, itemDoc.getString("AUTOR"), itemDoc.getString("AUTOR_INFO"));

			//remitter.setIdentifier(users.getUser(0).getIdentifier());
			
			remitters.setUser(new User[]{ remitter });
			request.setRemitterList(remitters);
			//FIN [dipucr-Felipe #1246]

			// Firmantes de la linea
			_0.v2.type.pfirma.cice.juntadeandalucia.SignerList signers = null;
			_0.v2.type.pfirma.cice.juntadeandalucia.Signer signer = null;
			SignLine signLine = null;
			User user = null;
			Job job = null;
			
			if (!StringUtils.isEmpty(processTemplateId)){//[dipucr-Felipe #1246]
				int steps = SignCircuitDetailDAO.countSteps(ctx.getConnection(), Integer.parseInt(processTemplateId));
				SignLineList signLineList = new SignLineList();
				SignLine[] signLineArray = new SignLine[steps];
				
				int i = 0;
				IItemCollection itemcol = SignCircuitDetailDAO.getSteps(ctx.getConnection(), Integer.parseInt(processTemplateId)).disconnect();
				for (Iterator<IItem> iter = itemcol.iterator(); iter.hasNext();) {
					IItem item = (IItem) iter.next();
					signers = new _0.v2.type.pfirma.cice.juntadeandalucia.SignerList();
					signer = new _0.v2.type.pfirma.cice.juntadeandalucia.Signer();
					signLine = new SignLine();
					
					if ("U".equals(item.getString("TIPO_FIRMANTE"))){
						user = new User();
						user.setIdentifier(getNif(ctx, item.getString("ID_FIRMANTE")));//[dipucr-Felipe #1246]
						user.setName(item.getString("NOMBRE_FIRMANTE"));					
						signer.setUserJob(user);
					} else {
						job = new Job();
						job.setIdentifier(getNif(ctx, item.getString("ID_FIRMANTE")));//[dipucr-Felipe #1246]
						job.setDescription(item.getString("NOMBRE_FIRMANTE"));					
						signer.setUserJob(job);
					}
					
					signers.setSigner(new _0.v2.type.pfirma.cice.juntadeandalucia.Signer[] {signer});
					signLine.setSignerList(signers);
					signLine.setType(SignLineType.FIRMA);
					signLineArray[i]=signLine;
					i++;
				}
	
				// Linea de firma
				signLineList.setSignLine(signLineArray);
				request.setSignLineList(signLineList);
			}
			else{//INICIO [dipucr-Felipe #1246] Mandar a mi firma / circuitos al vuelo
				
				if (listUsuarios.size() > 0){
					SignLineList signLineList = new SignLineList();
					SignLine[] signLineArray = new SignLine[listUsuarios.size()];
					int i = 0;
					
					for (Usuario usuario : listUsuarios){
						user = new User();
						UsuarioData usuarioData = usuario.getUserData(); 
						user.setIdentifier(usuarioData.getDni());
						
						StringBuffer sbNombreCompleto = new StringBuffer();
						sbNombreCompleto.append(usuarioData.getNombre());
						if (StringUtils.isNotEmpty(usuarioData.getApellidos())){
							sbNombreCompleto.append(" ");
							sbNombreCompleto.append(usuarioData.getApellidos());
						}
						user.setName(sbNombreCompleto.toString()); 
						
						signers = new _0.v2.type.pfirma.cice.juntadeandalucia.SignerList();
						signer = new _0.v2.type.pfirma.cice.juntadeandalucia.Signer();
						signer.setUserJob(user);
						
						signers.setSigner(new _0.v2.type.pfirma.cice.juntadeandalucia.Signer[] {signer});
						
						signLine = new SignLine();
						signLine.setSignerList(signers);
						signLine.setType(SignLineType.FIRMA);
						signLineArray[i]=signLine;
						i++;
					}
					// Linea de firma
					signLineList.setSignLine(signLineArray);
					request.setSignLineList(signLineList);
				}
			}//FIN [dipucr-Felipe #1246] Mandar a mi firma

			//INICIO [dipucr-Felipe #1246]
			// Asunto
//			request.setSubject(properties.getSubject());
			request.setSubject(getAsuntoDoc(ctx, itemDoc));
			
			// Texto
			request.setText(getTextoDoc(ctx, itemDoc));
			
			// Nivel de importancia
			ImportanceLevel importanceLevel = new ImportanceLevel();
			importanceLevel.setLevelCode(properties.getLevelOfImportance());
			request.setImportanceLevel(importanceLevel);
			
			// Fecha inicio
			if (properties.getFstart()!=null){
				request.setFentry(properties.getFstart());
			}

			// Fecha fin
//			if (properties.getFexpiration()!=null){
//				request.setFexpiration(properties.getFexpiration());
//			}
			Calendar calExpiration = Calendar.getInstance();
			calExpiration.setTime(FechasUtil.addAnios(new Date(), 1));
			request.setFexpiration(calExpiration);
			//FIN [dipucr-Felipe #1246]

			// Tipo de firma
			// INICIO [dipucr-Felipe #1246]
			if (!StringUtils.isEmpty(processTemplateId)){
				IItemCollection itemCircuit = SignCircuitHeaderDAO.getCircuit(ctx.getConnection(), Integer.parseInt(processTemplateId)).disconnect();
				IItem circuit = (IItem) itemCircuit.value();
				request.setSignType(SignType.fromValue(getSequence(circuit.getString("SECUENCIA"))));
			}
			else{
				//[dipucr-Felipe #1310]
				if (StringUtils.isEmpty(properties.getSignType())){
					request.setSignType(SignType.fromValue(SignType._value1));//CASCADA
				}
				else{
					request.setSignType(SignType.fromValue(properties.getSignType()));
				}
			}
			// FIN [dipucr-Felipe #1246]

			// Documento a firmar
			// [dipucr-Felipe #1246] Tomamos el documento previamente convertido a pdf desde SignAPI
//			byte [] documentBytes = getNotSignedDocument(ctx, document.getId());
			_0.v2.type.pfirma.cice.juntadeandalucia.Document doc = new _0.v2.type.pfirma.cice.juntadeandalucia.Document();
			_0.v2.type.pfirma.cice.juntadeandalucia.DocumentList docList = new _0.v2.type.pfirma.cice.juntadeandalucia.DocumentList();
			docList.setDocument(new _0.v2.type.pfirma.cice.juntadeandalucia.Document[] {doc});
			doc.setSign(true);
			DocumentType docType = new DocumentType();
			docType.setIdentifier(FirmaConfiguration.getInstance(ctx).getProperty(FirmaConfiguration.PROCESS_SIGN_CONNECTOR_DOCTYPE));
			doc.setDocumentType(docType);
			doc.setMime(MimetypeMapping.getMimeType(document.getExtension()));
			
//			String filename = itemDoc.getString("DESCRIPCION");
//			doc.setName(filename);
			doc.setName(document.getName());
//			doc.setContent(documentBytes);
			doc.setContent(IOUtils.toByteArray(document.getContent()));
			doc.setType(document.getExtension());
			request.setDocumentList(docList);
			
			// Referencia
			Entidad entidad = EntidadHelper.getEntidad();
//			request.setReference(ctx.getStateContext().getNumexp()
			request.setReference(itemDoc.getString("NUMEXP")
					+ REFERENCE_SEPARATOR + document.getId()
					+ REFERENCE_SEPARATOR + entidad.getIdentificador());

			// Creacion de la peticion
			String requestHash = portafirmasWsClient.createRequest(request);
			StringHolder holder= new StringHolder (requestHash);

			// Envio de la peticion
			portafirmasWsClient.sendRequest(holder);
			request = portafirmasWsClient.getRequest(requestHash);
			String docHash = request.getDocumentList().getDocument()[0].getIdentifier();
			xmlProcesoFirma = getProcesoFirmaXml(requestHash, docHash);

		} catch (MalformedURLException e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		} catch (Exception e) {
			logger
			.error("Se ha producido un error al acceder al servicio: '"
					+ urlQuery);
			throw new ISPACException(e);
		}
		
		return xmlProcesoFirma;

	}

	/**
	 * [dipucr-Felipe #1422]
	 * @param ctx
	 * @param autor
	 * @param autorInfo
	 * @return
	 * @throws SigemException 
	 */
	private User getRemitter(IClientContext ctx, String autor, String autorInfo) throws SigemException {
		
		User remitter = new User();
		
		if (autor.startsWith(String.valueOf(IDirectoryEntry.ET_PERSON))){ //Usuario
			Usuario usuario = getUsuario(ctx, autor);
			if(usuario.getUserData()!=null){
				UsuarioData userData = usuario.getUserData();
				if(StringUtils.isNotEmpty(userData.getDni())){
					remitter.setIdentifier(userData.getDni());
				}
				else{
					remitter.setIdentifier(EntidadesAdmUtil.obtenerEntidadObject(ctx).getIdentificador()+"_"+autorInfo);
				}
				
				if(StringUtils.isNotEmpty(userData.getNombre())){
					remitter.setName(userData.getNombre());
				}
				else{
					remitter.setName(USERGROUP_DEFAULT_NAME);
				}
				
				if(StringUtils.isNotEmpty(userData.getApellidos())){
					remitter.setSurname1(userData.getApellidos());
				}
				else{
					remitter.setSurname1(autorInfo);
				}
			}
		}
		else{//Grupos y departamentos
			remitter.setIdentifier(autorInfo);
			remitter.setName(USERGROUP_DEFAULT_NAME);
			remitter.setSurname1(autorInfo);
		}
		
		return remitter;
	}

	/**
	 * [dipucr-Felipe #1246]
	 * @param cct
	 * @param itemDoc
	 * @return
	 * @throws ISPACException 
	 */
	private String getAsuntoDoc(IClientContext cct, IItem itemDoc) throws ISPACException {
		
		String numexp = itemDoc.getString("NUMEXP");
		IEntitiesAPI entitiesAPI = cct.getAPI().getEntitiesAPI();
		
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		
		final String NEW_LINE = "<br/>";
		
		StringBuffer sbAsunto = new StringBuffer();
		sbAsunto.append("Nombre: " + itemDoc.getString("NOMBRE") + NEW_LINE);
		sbAsunto.append("Descripcion: " + itemDoc.getString("DESCRIPCION") + NEW_LINE);
		sbAsunto.append("Asunto: " + itemExpediente.getString("ASUNTO"));
		
		String asunto = sbAsunto.toString(); 
		if (asunto.length() > MAX_LENGTH_ASUNTO){
			asunto = asunto.substring(0, MAX_LENGTH_ASUNTO);
			asunto += "..."; 
		}
		return asunto;
	}
	
	/**
	 * [dipucr-Felipe #1246]
	 * @param cct
	 * @param itemDoc
	 * @return
	 * @throws ISPACException 
	 */
	private String getTextoDoc(IClientContext cct, IItem itemDoc) throws ISPACException {
		
		String idDoc = String.valueOf(itemDoc.getKeyInt());
		String idDocEncriptado = new String(Base64.encodeBase64(idDoc.getBytes()));

		String dir = ConfigurationMgr.getVarGlobal(cct, "DNS_ENTIDAD_EXTERNO");
		if (StringUtils.isEmpty(dir)){
			dir = InetUtils.getLocalHostAddress();
		}
		
		String direccionFoliado = "https://" + dir + ":4443/SIGEM_AutenticacionWeb/seleccionEntidad.do?REDIRECCION=IndiceElectronico&tramiteId=" 
				+ idDocEncriptado + "&ENTIDAD_ID=" + EntidadesAdmUtil.obtenerEntidad(cct);

		String link = "<a href=\"" + direccionFoliado + "\" target=\"_blank\" style=\"color:red;\"><b>Consultar expediente completo</b></a>";
		return link;
	}

	/**
	 * [dipucr-Felipe #1246]
	 * @param string
	 * @return
	 * @throws SigemException 
	 */
	private String getNif(IClientContext cct, String idUsuario) throws SigemException {
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		ServicioEstructuraOrganizativa servicioEstructuraOrganizativa = LocalizadorServicios.getServicioEstructuraOrganizativa();
		int id = Integer.parseInt(idUsuario.replace("1-", ""));
		Usuario usuario = servicioEstructuraOrganizativa.getUsuario(id, entidad);
		UsuarioData datosUsuario = usuario.getUserData();
		return datosUsuario.getDni();
	}
	
	private Usuario getUsuario(IClientContext cct, String idUsuario) throws SigemException {
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		ServicioEstructuraOrganizativa servicioEstructuraOrganizativa = LocalizadorServicios.getServicioEstructuraOrganizativa();
		int id = Integer.parseInt(idUsuario.replace(IDirectoryEntry.ET_PERSON + "-", ""));
		return servicioEstructuraOrganizativa.getUsuario(id, entidad);
	}
	
	/**
	 * [dipucr-Felipe #1422]
	 * @param cct
	 * @param idGrupo
	 * @return
	 * @throws SigemException
	 */
	@SuppressWarnings("unused")
	private Grupo getGrupo(IClientContext cct, String idGrupo) throws SigemException {
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		ServicioEstructuraOrganizativa servicioEstructuraOrganizativa = LocalizadorServicios.getServicioEstructuraOrganizativa();
		int id = Integer.parseInt(idGrupo.replace(IDirectoryEntry.ET_GROUP + "-", ""));
		return servicioEstructuraOrganizativa.getGrupo(id, entidad);
	}
	
	/**
	 * [dipucr-Felipe #1422]
	 * @param cct
	 * @param idGrupo
	 * @return
	 * @throws SigemException
	 */
	@SuppressWarnings("unused")
	private Departamento getDepartamento(IClientContext cct, String idDpto) throws SigemException {
		
		String entidad = EntidadesAdmUtil.obtenerEntidad(cct);
		ServicioEstructuraOrganizativa servicioEstructuraOrganizativa = LocalizadorServicios.getServicioEstructuraOrganizativa();
		int id = Integer.parseInt(idDpto.replace(IDirectoryEntry.ET_UNIT + "-", ""));
		return servicioEstructuraOrganizativa.getDepartamento(id, entidad);
	}

	
	public int getTypeObject() {
		return TYPE_OBJECT;
	}

	/**
	 * Obtener el formateador para la fecha de la firma.
	 * @param defaultSignDateFormatter Formateador por defecto.
	 *
	 * @return Formateador para la fecha de la firma con el formato establecido en la configuración (DATE_FORMAT)
	 * si existe y es correcto, en caso contrario, el formateador por defecto.
	 *
	 * @throws ISPACException Si se produce algún error.
	 */
	protected SimpleDateFormat getSignDateFormatter(SimpleDateFormat defaultSignDateFormatter) throws ISPACException {

		SimpleDateFormat singDateFormatter = null;

		String signDateFormat = ISPACConfiguration.getInstance().getProperty(DATE_FORMAT);
		if (StringUtils.isNotBlank(signDateFormat)) {

			try {
				singDateFormatter = new SimpleDateFormat(signDateFormat);
			} catch (Exception e) {
				logger.debug("Error en el formato configurado para la fecha de la firma", e);
			}
		}

		if (singDateFormatter == null) {
			singDateFormatter = defaultSignDateFormatter;
		}

		return singDateFormatter;
	}
	
	/**
	 * Obtiene el valor correspondiente para el identificador de secuencia especifido
	 * @param idSequence identificador de secuencia
	 * @return String con el valor de secuencia necesario para el portafirmas
	 * @throws ISPACInfo
	 */
	private String getSequence(String idSequence) throws ISPACInfo{
		if ("1".equals(idSequence)){
			return "CASCADA";
		} else if ("2".equals(idSequence)){
			return "PARALELA";
		} else if ("3".equals(idSequence)){
			return "PRIMER FIRMANTE";
		} else {
			throw new ISPACInfo("exception.sequence.value.incorrect", false);
		}
	}

	/**
	 * Permite obtener un firmante de tramitacion a partir de un usuario del portafirmas
	 * @param user usuario del portafirmas
	 * @return firmante 
	 */
	private Signer getSigner(User user) {
		Signer signer = new Signer();
		signer.setIdentifier(user.getIdentifier());

		if ((user.getSurname1()!=null) && StringUtils.isNotEmpty(user.getSurname1())){
			// Usuario
			signer.setName(user.getName() + " " + user.getSurname1());
			signer.setTipoFirmante(Signer.TYPE_SIGNER_USER);
		} else {
			// Cargo
			signer.setName(user.getName());
			signer.setTipoFirmante(Signer.TYPE_SIGNER_CARGO);
		}

		return signer;
	}
	
	/**
	 * Permite obtener una lista de firmantes de tramitacion a partir de una lista de usuarios del portafirmas
	 * @param users usuarios del portafirmas
	 * @return lista de firmantes
	 */
	private List<Signer> getSigners(UserList users) {
		List<Signer> signers = new ArrayList<Signer>();
		if (users!=null){
			for (User user: users.getUser()){
				signers.add(getSigner(user));
			}
		}
		return signers;
	}

	/**
	 * Permite obtener el xml para almacenar en la tabla de documentos
	 * @param requestHash hash de la peticion
	 * @param docHash hash del documento
	 * @return String con el xml 
	 */
	private String getProcesoFirmaXml(String requestHash, String docHash){
		return "<process><requestHash>"+requestHash+"</requestHash><docHash>"+docHash+"</docHash></process>";
		
	}
	
	public String getIdSystem() {
		return SIGNPROCESS_SYSTEM_MPT;
	}

	/**
	 * Crea un usuario en portafirmas
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#crearUsuarioPortafirmas(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean crearUsuarioPortafirmas(String entidadId, String dni, String nombre, String apellido1, String apellido2, String email) throws ISPACException{
		
		boolean usuarioCreado = false;
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			
			ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
			ieci.tecdoc.sgm.core.services.entidades.Entidad entidad = servicioEntidades.obtenerEntidad(entidadId);
			Seat sede = new Seat(entidad.getDir3(), entidad.getNombreLargo());
			
			Boolean valid = true;
			Boolean visibleOtherSeats = false;
			
			ParameterList parameterList = new ParameterList();
			Parameter _value = new Parameter();
			_value.setIdentifier(PARAMETRO_EMAIL);
			_value.setValue(email);
			Parameter [] emailParam = new Parameter[1];
			emailParam[0] = _value;
			parameterList.setParameter(emailParam);
			
			EnhancedUserJobInfo enhancedUserJobInfo = new EnhancedUserJobInfo(sede, valid, visibleOtherSeats, parameterList);

			if(StringUtils.isNotEmpty(dni)){
				dni = dni.replaceAll(" " , "").toUpperCase().trim();
			}
			
			User user = new User(dni, nombre, apellido1, apellido2);
			
			EnhancedUser usuario = new EnhancedUser(user, enhancedUserJobInfo);
			EnhancedUser[] arrayUsuarios = {usuario};
			
			EnhancedUserList listaUsuarios = new EnhancedUserList(arrayUsuarios);			
			
			//El usuario DIPUCR_WS_PADES necesita tener el perfil ADMINPROV y permiso sobre las sedes que sean.
			BigInteger resultado = portafirmasWsClient.insertEnhancedUsers(listaUsuarios);
			
			if(null != resultado && resultado.intValue() > 0){
				usuarioCreado = true;
			}

		} catch (MalformedURLException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		} catch (Exception e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		}
		
		return usuarioCreado;

	}
	
	/**
	 * modifica un usuario en portafirmas
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#modificarUsuarioPortafirmas(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean modificarUsuarioPortafirmas(String entidadId, String dni, String nombre, String apellido1, String apellido2, String email) throws ISPACException{
		
		boolean usuarioCreado = false;
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			
			ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
			ieci.tecdoc.sgm.core.services.entidades.Entidad entidad = servicioEntidades.obtenerEntidad(entidadId);
			Seat sede = new Seat(entidad.getDir3(), entidad.getNombreLargo());
			
			Boolean valid = true;
			Boolean visibleOtherSeats = false;
			
			ParameterList parameterList = new ParameterList();
			Parameter _value = new Parameter();
			_value.setIdentifier(PARAMETRO_EMAIL);
			_value.setValue(email);
			Parameter [] emailParam = new Parameter[1];
			emailParam[0] = _value;
			parameterList.setParameter(emailParam);
			EnhancedUserJobInfo enhancedUserJobInfo = new EnhancedUserJobInfo(sede, valid, visibleOtherSeats, parameterList);

			User user = new User(dni, nombre, apellido1, apellido2);
			
			EnhancedUser usuario = new EnhancedUser(user, enhancedUserJobInfo);
			EnhancedUser[] arrayUsuarios = {usuario};
			
			EnhancedUserList listaUsuarios = new EnhancedUserList(arrayUsuarios);			
			
			//El usuario DIPUCR_WS_PADES necesita tener el perfil ADMINPROV y permiso sobre las sedes que sean.
			BigInteger resultado = portafirmasWsClient.updateEnhancedUsers(listaUsuarios);
			
			if(null != resultado && resultado.intValue() > 0){
				usuarioCreado = true;
			}

		} catch (MalformedURLException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		} catch (Exception e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		}
		
		return usuarioCreado;

	}
	
	/**
	 * Comprueba si existe un usuario en portafirmas
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#existeUsuarioPortafirmas(java.lang.String)
	 */
	public boolean existeUsuarioPortafirmas(String dni) throws ISPACException{
		
		boolean existeUsuario = false;
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			
			//El usuario DIPUCR_WS_PADES necesita tener el perfil ADMINPROV y permiso sobre las sedes que sean.
			EnhancedUserList userList = portafirmasWsClient.getEnhancedUsers(dni, "");
			
			if(null != userList && null != userList.getEnhancedUser() && userList.getEnhancedUser().length > 0){
				existeUsuario = true;
			}
		} catch (MalformedURLException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		} catch (Exception e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + urlAdmin);
			throw new ISPACException(e);
		}
		
		return existeUsuario;
	}

	/**
	 * Incializa el conector de firma del Minhap
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#inicializar(java.lang.String)
	 */
	public boolean inicializar(String entidadId) {
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
	
	public boolean insertaAutorizacionPortafirmas(Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws ISPACException {
		boolean autorizacionCreada = false;
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			if(!userIdenAutoriza.equals(userIdenAutorizado)){
				autorizacionCreada = portafirmasWsClient.insertarAutorizaciones(authentication, fstart, fend, userIdenAutoriza, userIdenAutorizado, descripcion, entidad);
			}			
			
		} catch (MalformedURLException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio: '"  + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Se ha producido un error al acceder al servicio: '"  + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		}
		return autorizacionCreada;
	}
	
	public boolean revocarAutorizacion(Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String entidad) throws ISPACException{
		boolean revocadaAutorizacion = false;
		try {
			
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			if(!userIdenAutoriza.equals(userIdenAutorizado)){
				revocadaAutorizacion = portafirmasWsClient.revocarAutorizacion(authentication, fstart, fend, userIdenAutoriza, userIdenAutorizado, entidad);
			}
			
		} catch (MalformedURLException e) {
			logger.error("Se ha producido un error al acceder al servicio: '" + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio: '"  + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Se ha producido un error al acceder al servicio: '"  + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		}
		return revocadaAutorizacion;
	}
	
	/**
	 * [Ticket1269#Teresa] Anular circuito de firmas
	 * 
	 * Permite eliminar una petición
	 * @param requestHash hash de la peticion
	 * @param document documento a insertar
	 * @return
	 * @throws ISPACException 
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public void deleteDocument(ClientContext ctx, String processId) throws ISPACException{
		
		try {
			PortafirmasMinhapWebServiceClient portafirmasWsClient = new PortafirmasMinhapWebServiceClient(urlQuery, urlModify, urlAdmin, authentication);
			
			XmlFacade xmlFacade = new XmlFacade(processId);
			String requestHash = xmlFacade.get("/process/requestHash");
			portafirmasWsClient.deleteRequestSend(new StringHolder(requestHash));
			
			
		} catch (MalformedURLException e) {
			logger.error("Se ha producido un error al acceder al servicio de borrado: '" + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		} catch (ServiceException e) {
			logger.error("Se ha producido un error al acceder al servicio de borrado: '"  + e.getMessage(), e);
			throw new ISPACException("Error. " + e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Se ha producido un error al acceder al servicio de borrado: '"  + e.getMessage(), e);
			//Si no es uno de los dos errores controlados, lanzamos la excepción
			if (!ERROR_DELETE_REQUEST.equals(e.getMessage()) && !ERROR_DELETE_YA_FIRMADO.equals(e.getMessage()) &&
					!ERROR_DELETE_UNKNOWN.equals(e.getMessage()))
			{
				throw new ISPACException("Error. " + e.getMessage(), e);
			}
		}
	}
	
	/**
	 * [dipucr-Felipe #1246 #1305]
	 * En un documento rechazado, devuelve el usuario 
	 * @throws ISPACException 
	 * 
	 */
	public UsuarioRechazo getUsuarioRechazo(IClientContext ctx, IItem itemDoc) throws ISPACException{

		UsuarioRechazo usuario = null;
		try{
			String processId = itemDoc.getString("ID_PROCESO_FIRMA");
			Request request = getRequest((ClientContext)ctx, processId);
			
			CommentList listComments = request.getCommentList();
			int tamanio = listComments.getComment().length;
			
			if (tamanio > 0){//Hay motivo de rechazo
				
				_0.v2.type.pfirma.cice.juntadeandalucia.Comment comment = 
						listComments.getComment(tamanio - 1);//último
	
				User user = (User) comment.getUser();
				String idUser = StringUtils.defaultString(user.getIdentifier());
				String motivo = StringUtils.defaultString(comment.getTextComment());
				
				usuario = new UsuarioRechazo();
				usuario.setNif(idUser);
				usuario.setNombre(getUserCompleteName(user));
				usuario.setMotivo(motivo);
			}
		}
		catch(Exception ex){
			String error = "Error al obtener el usuario de rechazo en el documento " + itemDoc.getKeyInt();
			logger.error(error, ex);
			throw new ISPACException(error, ex);
		}
		return usuario;
	}
	
	
	//[dipucr-Felipe #1246]
	//TODO: Devolver un código de error o una tipología de excepción concreta (no excepción general)
	private final String ERROR_DELETE_YA_FIRMADO = "El documento ya ha sido firmado no se puede anular el circuito de firma";
	private final String ERROR_DELETE_REQUEST = "Request not found";
	private final String ERROR_DELETE_UNKNOWN = "Unknown error";
	
}
