package es.dipucr.portafirmas.client;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import _0.v2.query.pfirma.cice.juntadeandalucia.PfirmaException;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadDocumentResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.DownloadSignResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.GetCVSResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequestResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QuerySeats;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QuerySeatsResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Signature;
import es.dipucr.portafirmas.common.ServiciosConsulta;

public class PortaFirmasConsultaClient {
	
	/**
	 * Logger de la clase
	 */
	private String ruta = "";
	protected static final Logger logger = Logger.getLogger(PortaFirmasConsultaClient.class);
	
	public PortaFirmasConsultaClient(String rutaServidor){
		this.ruta = rutaServidor;
	}
	
	public QuerySeatsResponse recuperaSedes(Authentication authentication, QuerySeats querySeats) throws RemoteException, PfirmaException {

		/**
		 * authentication: Token de autenticación. Ver el punto 6.1 query:
		 * Código o nombre de la sede. Si es nulo se devolverán todas.
		 * **/

		ServiciosConsulta servConsulta = new ServiciosConsulta(authentication, ruta);

		return servConsulta.recuperaSedes(querySeats);
	}
	
	public DownloadSignResponse recuperaDocumentosBySolicitud (Authentication authentication, String documentId) throws RemoteException, PfirmaException{
		
		ServiciosConsulta servConsulta = new ServiciosConsulta(authentication, ruta);

		return servConsulta.recuperaDocumentosBySolicitud(documentId);
		
	}
	
	public DownloadDocumentResponse recuperaDocumentosAnexosSolicitud (Authentication authentication, String documentId) throws RemoteException, PfirmaException{

		ServiciosConsulta servConsulta = new ServiciosConsulta(authentication, ruta);

		return servConsulta.recuperaDocumentosAnexosSolicitud(documentId);
	}
	
	public QueryRequestResponse recuperaDetallePeticion (Authentication authentication, String documentId) throws RemoteException, PfirmaException{

		ServiciosConsulta servConsulta = new ServiciosConsulta(authentication, ruta);

		return servConsulta.recuperaDetallePeticion(documentId);
	}
	
	/**
	 * Permite generar el Codigo de Verificacion Seguro de una firma de documento
	 * @param signature 
	 * @return GetCVSResponse
	 * @throws PfirmaException 
	 * @throws RemoteException 
	 * 
	 * **/
	public GetCVSResponse devolverCVS (Authentication authentication, Signature signature) throws RemoteException, PfirmaException{
		ServiciosConsulta servConsulta = new ServiciosConsulta(authentication, ruta);
		return servConsulta.devolverCVS(signature);
		
	}

}
