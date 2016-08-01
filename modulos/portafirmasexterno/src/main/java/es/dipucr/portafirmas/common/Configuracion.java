package es.dipucr.portafirmas.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Seat;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Authentication;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.User;

public class Configuracion {
	
	//private static final Logger logger = Logger.getLogger(Configuracion.class);
	//public static final String DIRECCION_PORTAFIRMASEXTERNOMODIFY = "DIRECCION_PORTAFIRMASEXTERNOMODIFY";
	//public static final String DIRECCION_PORTAFIRMASEXTERNOADMIN = "DIRECCION_PORTAFIRMASEXTERNOADMIN";
	//public static final String DIRECCION_PORTAFIRMASEXTERNO_CONSULTA = "DIRECCION_PORTAFIRMASEXTERNO_CONSULTA";

	public static User getRemitentePeticionPADES()
			throws ISPACRuleException {
		User usuario = new User();
		usuario.setIdentifier("DIPUCR_WS_PADES");
		usuario.setName("DIPUCR_WS_PADES");
		usuario.setSurname1("DIPUCR_WS_PADES");
		usuario.setSurname2("DIPUCR_WS_PADES");
		return usuario;
	}

	public static _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication getAuthenticationAdminPADES(){
		_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication authentication = new _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication();
		authentication.setUserName("DIPUCR_WS_PADES");
		authentication.setPassword("DIPUCR_WS_PADES");
		return authentication;
	}

	public static Authentication getAuthenticationModifyPADES()
			throws ISPACRuleException {
		Authentication authentication = new Authentication();
		authentication.setUserName("DIPUCR_WS_PADES");
		authentication.setPassword("DIPUCR_WS_PADES");
		return authentication;
	}
	
	public static User getRemitentePeticionXADES()
			throws ISPACRuleException {
		User usuario = new User();
		usuario.setIdentifier("DIPUCR_WS");
		usuario.setName("DIPUCR_WS");
		usuario.setSurname1("DIPUCR_WS");
		usuario.setSurname2("DIPUCR_WS");
		return usuario;
	}

	public static _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication getAuthenticationAdminXADES(){
		_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication authentication = new _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication();
		authentication.setUserName("DIPUCR_WS");
		authentication.setPassword("DIPUCR_WS");
		return authentication;
	}

	public static Authentication getAuthenticationModifyXADES()
			throws ISPACRuleException {
		Authentication authentication = new Authentication();
		authentication.setUserName("DIPUCR_WS");
		authentication.setPassword("DIPUCR_WS");
		return authentication;
	}
	public static _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication getAuthenticationConsultaXADES()
			throws ISPACRuleException {
		_0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication authentication = new _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication();
		authentication.setUserName("DIPUCR_WS");
		authentication.setPassword("DIPUCR_WS");
		return authentication;
	}
	public static _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication getAuthenticationConsultaPADES()
			throws ISPACRuleException {
		_0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication authentication = new _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication();
		authentication.setUserName("DIPUCR_WS_PADES");
		authentication.setPassword("DIPUCR_WS_PADES");
		return authentication;
	}

	public static String getAplicacionXADES() {
		return "DIPUCR_WS";
	}
	
	public static String getAplicacionPADES() {
		return "DIPUCR_WS_PADES";
	}

	public static Seat getSeatAdmin() {
		Seat seat = new Seat();
		seat.setCode("L02000013");
		seat.setDescription("Diputación de Ciudad Real");
		return seat;
	}

}
