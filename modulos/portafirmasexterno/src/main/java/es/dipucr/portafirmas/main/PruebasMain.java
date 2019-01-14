package es.dipucr.portafirmas.main;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import es.dipucr.portafirmas.client.PortaFirmasConsultaClient;
import _0.v2.query.pfirma.cice.juntadeandalucia.PfirmaException;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequest;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryRequestResponse;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryUsers;
import _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.QueryUsersResponse;

public class PruebasMain {

	public static void main(String[] args) {
		try {
			QueryServiceStub query = new QueryServiceStub("https://portafirmas.redsara.es/pf/servicesv2/QueryService");
			//Authentication authentication = Configuracion.getAuthenticationModifyPADES();
			QueryUsers queryUsers16 = new QueryUsers();
			Authentication authentication = new Authentication();
			authentication.setUserName("DIPUCR_WS_PADES");
			authentication.setPassword("DIPUCR_WS_PADES");
			queryUsers16.setAuthentication(authentication );
			queryUsers16.setQuery("05667699Q");
			QueryUsersResponse respuesta = query.queryUsers(queryUsers16);
			System.out.println(respuesta.getUserList());
			
			QueryRequest queryRequest = new QueryRequest();
			queryRequest.setAuthentication(authentication);
			queryRequest.setRequestId("c6XfktF3Go");			
			QueryRequestResponse informacion = query.queryRequest(queryRequest);
			String valorPet = informacion.getRequest().getRequestStatus().getValue();
			System.out.println("valorPet "+valorPet);
			
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PfirmaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
