package ieci.tdw.ispac.ispaclib.sign.portafirmas;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.expedients.Document;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.EventsDefines;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.sign.SignDetailEntry;
import ieci.tdw.ispac.ispaclib.sign.exception.InvalidSignatureValidationException;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.ProcessSignProperties;
import ieci.tdw.ispac.ispaclib.sign.portafirmas.vo.Signer;
import ieci.tecdoc.sgm.core.services.estructura_organizativa.Usuario;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;

import es.dipucr.sigem.portafirmas.UsuarioRechazo;

/**
 * Conector de gestión de firmas. Portafirmas
 *
 */
public interface ProcessSignConnector {


    int TYPE_OBJECT=EventsDefines.EVENT_OBJ_SIGN_CIRCUIT_PORTAFIRMAS_NO_DEFAULT;
    public static String SIGNPROCESS_SYSTEM_DEFAULT = "0";
	public static String SIGNPROCESS_SYSTEM_MPT = "1";


	/**
	 * Devuelve la lista de firmantes que pueden formar parte de un circuito de firma
	 * @param ctx
	 * @return
	 * @throws ISPACException
	 * @throws Exception
	 */
	public List<Signer> getSigners(ClientContext ctx, String query) throws ISPACException;
	/**
	 * Envía un documento a firmar a un circuito de firma
	 * @param ctx
	 * @param processTemplateId: Identificador del circuito de firma
	 * @param usuario //[dipucr-Felipe #1246]
	 * @param document: Documento a firmar
	 * @param properties: Propiedades del proceso de firma
	 * @return
	 * @throws ISPACException
	 */
	public String initSignProcess(ClientContext ctx, String processTemplateId, Document document, ProcessSignProperties properties) throws ISPACException;
	public String initSignProcess(ClientContext ctx, List<Usuario> listUsuarios, Document document, ProcessSignProperties properties) throws ISPACException;

	/**
	 * Obtiene un documento firmado
	 * @param ctx
	 * @param documentId: Identificador del documento firmado
	 * @return
	 * @throws ISPACException
	 */
	public byte[] getDocument(ClientContext ctx , String documentId) throws ISPACException;

	/**
	 * Devuelve la lista de firmas de un documento tipificado con el objeto SignDetailEntry cuyos atributos son:
	 * el autor, si el documento esta firmado, la fecha de firma y la integridad del documento
	 * @param ctx
	 * @param documentId: Identificador del documento del que se quiere consultar sus firmas.
	 * @return
	 * @throws ISPACException
	 * @throws InvalidSignatureValidationException
	 */
	public List <SignDetailEntry> getSigns(ClientContext ctx, String documentId) throws InvalidSignatureValidationException, ISPACException;
	
	/**
	 * Devuelve la lista de firmas de un documento tipificado con el objeto SignDetailEntry cuyos atributos son:
	 * el autor, si el documento esta firmado, la fecha de firma y la integridad del documento
	 * @param ctx
	 * @param documentId: Identificador del documento del que se quiere consultar sus firmas.
	 * @param includeSubstitutes - [dipucr-Felipe #1352] Incluye todos los firmantes de una línea (autorizados)
	 * @return
	 * @throws ISPACException
	 * @throws InvalidSignatureValidationException
	 */
	public List <SignDetailEntry> getSigns(ClientContext ctx, String documentId, boolean includeSubstitutes) throws InvalidSignatureValidationException, ISPACException;

	/**
	 * Este metodo permite obtener el fichero firmado que devuelve el portafirmas
	 * @param ctx
	 * @param documentId: Identificador del documento del que se quiere consultar sus firmas.
	 * @return
	 * @throws ISPACException
	 * @throws InvalidSignatureValidationException
	 */
	public byte[] getSignsContent(ClientContext ctx, String documentId) throws InvalidSignatureValidationException, ISPACException;
	
	/**
	 * Obtiene el  estado firma de un proceso de firma
	 * @param processId: Identificador del proceso de firma externo
	 * @return
	 * @throws ISPACException
	 */
	public String getState(ClientContext ctx, String processId) throws ISPACException;

	/**
	 * Devuelve el tipo de objeto para los circuitos de firma.
	 * En todas las implementaciones, salvo la de por defecto,
	 * se ha de devolver el atributo typeObject
	 * @return
	 */
	public int getTypeObject();

	/**
	 * Devuelve el identificador del sistema
	 * @return
	 */
	public String getIdSystem();
	
	/**
	 * Crea un usuario en portafirmas
	 * @param ctx
	 * @param DNI del usuario a crear
	 * @param nombre del usuario a crear
	 * @param primer apellido del usuario a crear
	 * @param segundo apellido del usuario a crear
	 * @return Si el usuario ha sido creado o no
	 * @throws ISPACException
	 */
	public boolean crearUsuarioPortafirmas(String entidadId, String dni, String nombre, String apellido1, String apellido2, String email) throws ISPACException;
	
	/**
	 * Modifica un usuario en portafirmas
	 * @param ctx
	 * @param DNI del usuario a crear
	 * @param nombre del usuario a crear
	 * @param primer apellido del usuario a crear
	 * @param segundo apellido del usuario a crear
	 * @return Si el usuario ha sido creado o no
	 * @throws ISPACException
	 */
	public boolean modificarUsuarioPortafirmas(String entidadId, String dni, String nombre, String apellido1, String apellido2, String email) throws ISPACException;

	/**
	 * Comprueba si existe un usuario en portafirmas
	 * @param ctx
	 * @param DNI del usuario a crear
	 * @return si el usuario existe en portafirmas
	 * @throws ISPACException
	 */
	public boolean existeUsuarioPortafirmas(String dni) throws ISPACException;
	
	/**
	 * Inserta una autorización en el portafirmas
	 * @param fecha de inicio
	 * @param fecha de fin
	 * @param usuario que autoriza
	 * @param usuario autorizado
	 * @param descripcion
	 * @return isnertado
	 * @throws ISPACException
	 */
	public boolean insertaAutorizacionPortafirmas(Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws ISPACException;
	
	/**
	 * Revocada una autorización en el portafirmas
	 * @param fecha de inicio
	 * @param fecha de fin
	 * @param usuario que autoriza
	 * @return revocado
	 * @throws ISPACException
	 */
	public boolean revocarAutorizacion(Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String entidad) throws ISPACException;
	
	/**
	 * se puede usar para inicializar variables propias del conector de firam
	 * @param ctx
	 * @return boolean
	 * @throws ISPACException
	 */
	public boolean inicializar(String entidadId);
	

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
	public void deleteDocument(ClientContext ctx, String processId) throws ISPACException;
	
	/**
	 * [dipucr-Felipe #1360] Metemos este método propio del portafirmas en la interfaz
	 * @param ctx
	 * @param itemDoc
	 * @return
	 * @throws ISPACException
	 */
	public UsuarioRechazo getUsuarioRechazo(IClientContext ctx, IItem itemDoc) throws ISPACException;
}
