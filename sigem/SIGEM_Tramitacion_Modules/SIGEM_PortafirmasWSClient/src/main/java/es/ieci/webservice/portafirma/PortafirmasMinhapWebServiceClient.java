package es.ieci.webservice.portafirma;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;

import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;

import org.apache.commons.io.IOUtils;

import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceSoapBindingStub;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminService_PortType;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminService_ServiceLocator;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceSoapBindingStub;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyService_PortType;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyService_ServiceLocator;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceSoapBindingStub;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryService_PortType;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryService_ServiceLocator;
import _0.v2.type.pfirma.cice.juntadeandalucia.Authentication;
import _0.v2.type.pfirma.cice.juntadeandalucia.Document;
import _0.v2.type.pfirma.cice.juntadeandalucia.DocumentTypeList;
import _0.v2.type.pfirma.cice.juntadeandalucia.EnhancedUserList;
import _0.v2.type.pfirma.cice.juntadeandalucia.ExceptionInfo;
import _0.v2.type.pfirma.cice.juntadeandalucia.JobList;
import _0.v2.type.pfirma.cice.juntadeandalucia.Request;
import _0.v2.type.pfirma.cice.juntadeandalucia.Signature;
import _0.v2.type.pfirma.cice.juntadeandalucia.StateList;
import _0.v2.type.pfirma.cice.juntadeandalucia.UserList;
import axis.mtom.client.handler.XOPHandler;



/**
 * Cliente del servicio web de trans
 * 
 * @author Iecisa
 * @version $Revision$
 *
 */
public class PortafirmasMinhapWebServiceClient {

	private QueryService_PortType wsQueryService;
	private ModifyService_PortType wsModifyService;
	private AdminService_PortType wsAdminService;
	private Authentication authentication;
	
	/**
	 * Constructor para el portafirmas
	 * @param queryUrl url de consulta
	 * @param modifyUrl url de modificacion
	 * @param authentication objeto de autenticacion
	 * @throws MalformedURLException
	 */
	public PortafirmasMinhapWebServiceClient(String queryUrl, String modifyUrl, String adminUrl, Authentication authentication) throws MalformedURLException, ServiceException {
		QueryService_ServiceLocator  serviceLocatorQuery = new QueryService_ServiceLocator();      
		serviceLocatorQuery.setQueryServicePortEndpointAddress(queryUrl);  
		wsQueryService = (QueryServiceSoapBindingStub)serviceLocatorQuery.getQueryServicePort();
		ModifyService_ServiceLocator  serviceLocatorModify = new ModifyService_ServiceLocator();      
		serviceLocatorModify.setModifyServicePortEndpointAddress(modifyUrl);  
		wsModifyService = (ModifyServiceSoapBindingStub)serviceLocatorModify.getModifyServicePort();		
		AdminService_ServiceLocator serviceLocatorAdmin = new AdminService_ServiceLocator();
		serviceLocatorAdmin.setAdminServicePortEndpointAddress(adminUrl);
		wsAdminService = (AdminServiceSoapBindingStub)serviceLocatorAdmin.getAdminServicePort();

		this.authentication = authentication;
	}

	/**
	 * Permite obtener los usuarios a partir de una query
	 * @param query string de busqueda
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public UserList getUsers(String query)
			throws MalformedURLException, Exception {
		return wsQueryService.queryUsers(authentication, query);
	}

	/**
	 * Permite obtener los cargos a partir de una query
	 * @param query string de busqueda
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public JobList getJobs(String query)
			throws MalformedURLException, Exception {
		return wsQueryService.queryJobs(authentication, query);
	}

	/**
	 * Permite obtener los estados de la peticion
	 * @param query string de busqueda
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public StateList getStates(String query)
			throws MalformedURLException, Exception {
		return wsQueryService.queryStates(authentication, query);
	}

	/**
	 * Permite obtener los tipos de documento
	 * @param query string de busqueda
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public DocumentTypeList getDocumentTypes(String query)
			throws MalformedURLException, Exception {
		return wsQueryService.queryDocumentTypes(authentication, query);
	}

	/**
	 * Permite obtener una peticion
	 * @param requestHash hash de la peticion
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public Request getRequest(String requestHash) 
			throws MalformedURLException, Exception {
		return wsQueryService.queryRequest(authentication, requestHash);
	}

	/**
	 * Permite obtener las firmas de un documento
	 * @param docHash hash del documento
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public Signature getSigns(String docHash) 
			throws MalformedURLException, Exception {
		Signature signature = wsQueryService.downloadSign(authentication, docHash);
		InputStream is = XOPHandler.getDocumentStream();
		signature.setContent(IOUtils.toByteArray(is));
		return signature;
	}
	
	/**
	 * Permite obtener los bytes de un documento
	 * @param docHash hash del documento
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public byte[] getDocument(String docHash) 
			throws MalformedURLException, Exception {
		wsQueryService.downloadDocument(authentication, docHash);
		InputStream is = XOPHandler.getDocumentStream();
		return IOUtils.toByteArray(is);
		
	}
	
	/**
	 * Permite crear una peticion
	 * @param request peticion a crear
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public String createRequest(Request request) 
			throws MalformedURLException, Exception {
		return wsModifyService.createRequest(authentication, request);
	}

	/**
	 * Permite enviar una peticion
	 * @param requestHash hash de la peticion
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public void sendRequest(StringHolder requestHash) 
			throws MalformedURLException, Exception {
		wsModifyService.sendRequest(authentication, requestHash);
	}

	/**
	 * Permite insertar un documento a una peticion
	 * @param requestHash hash de la peticion
	 * @param document documento a insertar
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public String insertDocument(String requestHash, Document document) 
			throws MalformedURLException, Exception {
		return wsModifyService.insertDocument(authentication, requestHash,document);
	}
	
	/**
	 * [Ticket1269#Teresa] Anular circuito de firmas
	 * 
	 * Permite eliminar una petición
	 * @param requestHash hash de la peticion
	 * @param document documento a insertar
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public void deleteRequestSend(StringHolder requestHash) throws MalformedURLException, Exception {
		wsModifyService.deleteRequestSend(authentication, requestHash);
	}
	
	
	/**
	 * Permite crear un usario nuevo en Portafirmas
	 * @param enhancedUserList lista de usuarios extendidos a crear
	 *  
	 * @return número de usuarios creados
	 * 
	 * @throws ExceptionInfo
	 * @throws RemoteException
	 */
	public BigInteger insertEnhancedUsers(EnhancedUserList enhancedUserList) throws ExceptionInfo, RemoteException{
		return wsAdminService.insertEnhancedUsers(authentication, enhancedUserList);
	}
	
	/**
	 * Permite modificar un usario en Portafirmas
	 * @param enhancedUserList lista de usuarios extendidos a modificar
	 *  
	 * @return número de usuarios creados
	 * 
	 * @throws ExceptionInfo
	 * @throws RemoteException
	 */
	public BigInteger updateEnhancedUsers(EnhancedUserList enhancedUserList) throws ExceptionInfo, RemoteException{
		return wsAdminService.updateEnhancedUsers(authentication, enhancedUserList);
	}
	
	/**
	 * Permite modificar un usario en Portafirmas
	 * @param sede 
	 * @param enhancedUserList lista de usuarios extendidos a modificar
	 *  
	 * @return número de usuarios creados
	 * 
	 * @throws ExceptionInfo
	 * @throws RemoteException
	 */
	public EnhancedUserList getEnhancedUsers(String dni, String sede) throws ExceptionInfo, RemoteException{
		return wsQueryService.queryEnhancedUsers(authentication, dni, sede);		
	}
	
	/**
	 * Permite insetrar una autorizacion en Portafirmas
	 * @param Authentication authentication
	 * @param Date fstart
	 * @param Date fend
	 * @param String userIdenAutoriza
	 * @param String userIdenAutorizado
	 * @param  String descripcion
	 * @param  String entidad
	 *  
	 * @return si ha sido creado o no
	 * 
	 * @throws ExceptionInfo
	 * @throws RemoteException
	 */
	public boolean insertarAutorizaciones(Authentication authentication, Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws ExceptionInfo,
			RemoteException {
		return this.wsAdminService.insertarAutorizaciones(authentication, userIdenAutoriza, userIdenAutorizado, fstart, fend, descripcion, entidad);
	}
	
	/**
	 * Permite insetrar una autorizacion en Portafirmas
	 * @param Authentication authentication
	 * @param Date fstart
	 * @param Date fend
	 * @param String userIdenAutoriza	 *  
	 * @return si ha sido creado o no
	 * 
	 * @throws ExceptionInfo
	 * @throws RemoteException
	 */
	public boolean revocarAutorizacion(Authentication authentication, Date fstart, Date fend, String userIdenAutoriza, String userIdenAutorizado, String entidad) throws ExceptionInfo,
			RemoteException {
		return this.wsAdminService.revocarAutorizacionActiva(authentication, userIdenAutoriza, userIdenAutorizado, fstart, fend, entidad);
	}
}
