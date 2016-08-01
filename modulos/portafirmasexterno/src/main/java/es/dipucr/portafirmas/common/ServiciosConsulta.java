package es.dipucr.portafirmas.common;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;

import _0.v2.query.pfirma.cice.juntadeandalucia.PfirmaException;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadDocument;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadDocumentResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadSign;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadSignResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.ExceptionInfo;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.GetCVS;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.GetCVSResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequest;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequestResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QuerySeats;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QuerySeatsResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Signature;


public class ServiciosConsulta {
	/**
	 * Objeto proxy que hará de web service para realizar las llamadas
	 */
	private QueryServiceStub ws = null;
	private Authentication authentication = new Authentication();
	//private User usuario = new User();
	
	protected static final Logger logger = Logger.getLogger(ServiciosConsulta.class);
	
	/**
	 * Constructor
	 * @throws AxisFault 
	 */
	public ServiciosConsulta (Authentication authentication, String ruta) throws AxisFault{
		ws = new QueryServiceStub(ruta);
		this.authentication.setUserName(authentication.getUserName());
		this.authentication.setPassword(authentication.getPassword());
		
		/*this.usuario.setIdentifier(usuario.getIdentifier());
		this.usuario.setName(usuario.getName());
		this.usuario.setSurname1(usuario.getSurname1());
		this.usuario.setSurname2(usuario.getSurname2());*/
	}
	
	/**
	 * Permite recuperar las sedes existentes en la aplicación.
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * @throws ExceptionInfo 
	 * **/
	public QuerySeatsResponse recuperaSedes(QuerySeats querySeats) throws RemoteException, PfirmaException{
		
		/**
		 * authentication: Token de autenticación. Ver el punto 6.1
		 * query: Código o nombre de la sede. Si es nulo se devolverán todas.
		 * **/
		return ws.querySeats(querySeats);
	}
	
	/**
	 * Permite descargar documentos anexos a una solicitud
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * @throws ExceptionInfo 
	 * **/
	public DownloadSignResponse recuperaDocumentosBySolicitud (String documentId) throws RemoteException, PfirmaException{

		
		DownloadSign downloadSign4 = new DownloadSign();
		downloadSign4.setAuthentication(authentication);
		downloadSign4.setDocumentId(documentId);
		return ws.downloadSign(downloadSign4);

	}
	/**
	 * Permite generar el Codigo de Verificacion Seguro de una firma de documento
	 * @param signature 
	 * @return GetCVSResponse
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * 
	 * **/
	public GetCVSResponse devolverCVS (Signature signature) throws RemoteException, PfirmaException{
		GetCVS cvs = new GetCVS();
		cvs.setAuthentication(authentication);
		cvs.setFirma(signature);
		return ws.getCVS(cvs);
	}
	
	public DownloadDocumentResponse recuperaDocumentosAnexosSolicitud (String documentId) throws RemoteException, PfirmaException{
		
		DownloadDocument documento = new DownloadDocument();
		documento.setAuthentication(authentication);
		documento.setDocumentId(documentId);		
		return ws.downloadDocument(documento);
	}
	
	public QueryRequestResponse recuperaDetallePeticion (String documentId) throws RemoteException, PfirmaException{
		
		QueryRequest queryRequest = new QueryRequest();
		queryRequest.setAuthentication(authentication);
		queryRequest.setRequestId(documentId);
		return ws.queryRequest(queryRequest);
	}
	
	
}
