package ieci.tdw.ispac.ispaclib.sign.portafirmas;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.expedients.Document;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.resp.Responsible;
import ieci.tdw.ispac.ispaclib.sign.SignCircuitMgr;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.ProcessSignProperties;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.Signer;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import es.dipucr.sigem.portafirmas.UsuarioRechazo;

public class ProcessSignConnectorImpl implements ProcessSignConnector {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(ProcessSignConnectorImpl.class);

	/**
	 * Constructor.
	 */
	public ProcessSignConnectorImpl() {
		super();
	}

	/**
	 * En conector por defecto no maneja estados distintos a los que se muestran ya en en
	 * la aplicacion (pendiente de firma, firmado, sin firma...)
	 *
	 * @throws ISPACException
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getState(ieci.tdw.ispac.ispaclib.context.ClientContext,
	 *      java.lang.String)
	 */
	public String getState(ClientContext ctx, String processId) throws ISPACException {

		return "";

	}

	public byte[] getDocument(ClientContext ctx, String documentId) throws ISPACException {
		IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
		IItem itemDoc = entitiesAPI.getDocument(Integer.parseInt(documentId));
		String infoPageRde = itemDoc.getString("INFOPAG_RDE");
		IGenDocAPI genDocAPI = ctx.getAPI().getGenDocAPI();
		Object connectorSession = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		connectorSession = genDocAPI.createConnectorSession();
		if (!genDocAPI.existsDocument(connectorSession, infoPageRde)) {
			logger
					.error("No se ha encontrado el documento f�sico con identificador: '"
							+ infoPageRde
							+ "' en el repositorio de documentos");
			throw new ISPACException("No se ha encontrado el documento f�sico con identificador: '"
					+ infoPageRde
					+ "' en el repositorio de documentos");
		}

		genDocAPI.getDocument(connectorSession, infoPageRde, baos);
		return baos.toByteArray();
	}



	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto no maneja estados,
	 * son los mismo que la propia aplicaci�n : Sin firma, firmado, pendiente de firma...
	 * {@inheritDoc}
	 * @throws ISPACException
	 * @throws InvalidSignatureValidationException
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getSigns(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String)
	 */
	public List <SignDetailEntry> getSigns(ClientContext ctx, String documentId, boolean includeSubstitutes) throws InvalidSignatureValidationException, ISPACException {
		return getSigns(ctx, documentId, false);
	}
	
	public List <SignDetailEntry> getSigns(ClientContext ctx, String documentId) throws InvalidSignatureValidationException, ISPACException {

			/*IEntitiesAPI entitiesAPI = ctx.getAPI().getEntitiesAPI();
			IItemCollection documentos = entitiesAPI.getEntities(
					SpacEntities.SPAC_DT_DOCUMENTOS, ctx.getStateContext()
							.getNumexp(), " ID_PROCESO_FIRMA'=" + documentId+"'");
			List <SignDetailEntry> lista=new ArrayList<SignDetailEntry>();
			if(documentos.next()){
				IItem documento=documentos.value();
			return ctx.getAPI().getSignAPI().showSignInfo(documento.getInt("ID"));
			}
			else{
				return lista;
			}*/

		return new ArrayList<SignDetailEntry>();
	}

	/**
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getSignsContent(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String)
	 */
	public byte[] getSignsContent(ClientContext ctx, String documentId)
			throws InvalidSignatureValidationException, ISPACException {
		return new byte[0];
	}
	
	/**
	 * En la implementaci�n por defecto el ID_PROCESO_FIRMA y el ID ser�n el mismo.
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#initSignProcess(ieci.tdw.ispac.ispaclib.context.ClientContext, java.lang.String, ieci.tdw.ispac.api.expedients.Document)
	 */
	public String initSignProcess(ClientContext ctx, String processTemplateId, Document document, ProcessSignProperties properties) throws ISPACException {

		SignCircuitMgr signCircuitMgr = new SignCircuitMgr(ctx);
		signCircuitMgr.initCircuit(Integer.parseInt(processTemplateId), Integer
				.parseInt(document.getId()));
		return document.getId();

	}
	
	/**
	 * [dipucr-Felipe #1246]
	 */
	public String initSignProcess(ClientContext ctx, List<Usuario> listUsuarios, Document document, ProcessSignProperties properties) throws ISPACException{
		return null;
	}
	
	
	/**
	 * Este m�todo no aplica en el conector por defecto ya que se seguir� tirando de la estructura organizativa como siempre
	 * y se permitir� navegar a trav�s de la misma a la hora de seleccionar u
	 * {@inheritDoc}
	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#getSigners(ieci.tdw.ispac.ispaclib.context.ClientContext)
	 */
	public List<Signer> getSigners(ClientContext ctx, String query)
			throws ISPACException {
		return (new ArrayList <Signer>()) ;
	}

	public int getTypeObject() {
		return EventsDefines.EVENT_OBJ_SIGN_CIRCUIT;
	}

	private Signer getSigner(Responsible resp) {
		Signer signer = new Signer();
		signer.setName(resp.getName());
		signer.setIdentifier(resp.getUID());
		return signer;
	}

	public String getIdSystem() {
		return SIGNPROCESS_SYSTEM_DEFAULT;
	}

	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto maneja los usuarios internos de la aplicaci�n
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#crearUsuarioPortafirmas(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean crearUsuarioPortafirmas(String entidadid, String dni, String nombre, String apellido1, String apellido2, String email) throws ISPACException {
		return false;
	}
	
	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto maneja los usuarios internos de la aplicaci�n
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#modificarUsuarioPortafirmas(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean modificarUsuarioPortafirmas(String entidadid, String dni, String nombre, String apellido1, String apellido2, String email) throws ISPACException {
		return false;
	}
	
	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto maneja los usuarios internos de la aplicaci�n
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#existeUsuarioPortafirmas(java.lang.String)
	 */
	public boolean existeUsuarioPortafirmas(String dni) throws ISPACException {
		return false;
	}
	
	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto maneja los usuarios internos de la aplicaci�n
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#insertaAutorizacionPortafirmas(java.util.Date, java.util.Date, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean insertaAutorizacionPortafirmas(Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws ISPACException {
		return false;
	}
	
	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto maneja los usuarios internos de la aplicaci�n
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#revocarAutorizacion(java.util.Date, java.util.Date, java.lang.String)
	 */
	public boolean revocarAutorizacion(Date fstart, Date fend, String userIdenAutoriza, String userIdentAutorizado, String entidad) throws ISPACException {
		return false;
	}

	/**
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas por defecto no necisita inicializar ninguna variable
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#incializar(java.lang.String)
	 */
	public boolean inicializar(String entidadId) {
		return false;
	}
	/**
	 * [Ticket1269#Teresa] Anular circuito de firmas
	 * Este m�todo no tiene aplicaci�n en la implementaci�n por defecto ya que el portafirmas
 	 * {@inheritDoc}
 	 * @see ieci.tdw.ispac.ispaclib.sign.portafirmas.ProcessSignConnector#deleteDocument(java.lang.String)
	 */
	public void deleteDocument(ClientContext ctx, String processId) throws ISPACException{
	}

	/**
	 * [dipucr-Felipe #1360] S�lo tiene sentido en portafirmas 
	 */
	public UsuarioRechazo getUsuarioRechazo(IClientContext ctx, IItem itemDoc) throws ISPACException {
		return null;
	}
}
