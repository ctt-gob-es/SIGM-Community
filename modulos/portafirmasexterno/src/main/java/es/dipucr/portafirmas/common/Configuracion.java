package es.dipucr.portafirmas.common;

import _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Seat;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.Authentication;
import _0.v2.modify.pfirma.cice.juntadeandalucia.ModifyServiceStub.User;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

public class Configuracion {
	
	public static User getRemitentePeticionPADES(ClientContext cct) throws ISPACException {
		User usuario = new User();
		usuario.setIdentifier(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_PADES_IDENT+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		usuario.setName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_PADES_NAME+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		usuario.setSurname1(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_PADES_SURNAME1+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		usuario.setSurname2(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_PADES_SURNAME2+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return usuario;
	}
	
	public static User getRemitentePeticionXADES(ClientContext cct) throws ISPACException {
		User usuario = new User();
		usuario.setIdentifier(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_XADES_IDENT+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		usuario.setName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_XADES_NAME+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		usuario.setSurname1(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_XADES_SURNAME1+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		usuario.setSurname2(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_REMITENTE_XADES_SURNAME2+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return usuario;
	}
	
	public static _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication getAuthenticationAdminPADES(ClientContext cct) throws ISPACException{
		_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication authentication = new _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication();
		authentication.setUserName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_PADES_USER+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		authentication.setPassword(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_PADES_PASSW+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return authentication;
	}

	public static Authentication getAuthenticationModifyPADES(ClientContext cct) throws ISPACException {
		Authentication authentication = new Authentication();
		authentication.setUserName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_PADES_USER+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		authentication.setPassword(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_PADES_PASSW+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return authentication;
	}
	
	public static _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication getAuthenticationConsultaPADES(ClientContext cct) throws ISPACException {
		_0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication authentication = new _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication();
		authentication.setUserName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_PADES_USER+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		authentication.setPassword(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_PADES_PASSW+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return authentication;
	}
	
	

	public static _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication getAuthenticationAdminXADES(ClientContext cct) throws ISPACException{
		_0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication authentication = new _0.v2.admin.pfirma.cice.juntadeandalucia.AdminServiceStub.Authentication();
		authentication.setUserName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_XADES_USER+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		authentication.setPassword(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_XADES_PASSW+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return authentication;
	}

	public static Authentication getAuthenticationModifyXADES(ClientContext cct) throws ISPACException {
		Authentication authentication = new Authentication();
		authentication.setUserName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_XADES_USER+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		authentication.setPassword(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_XADES_PASSW+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return authentication;
	}
	public static _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication getAuthenticationConsultaXADES(ClientContext cct) throws ISPACException {
		_0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication authentication = new _0.v2.query.pfirma.cice.juntadeandalucia.QueryServiceStub.Authentication();
		authentication.setUserName(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_XADES_USER+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		authentication.setPassword(PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_XADES_PASSW+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador()));
		return authentication;
	}


	public static String getAplicacionXADES(ClientContext cct) throws ISPACException {
		return PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_APLICACION_XADES+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador());
	}
	
	public static String getAplicacionPADES(ClientContext cct) throws ISPACException {
		return PortafirmasConfiguration.getInstance().getProperty(PortafirmasConfiguration.PORTAFIRMAS.PORTAFIRMAS_APLICACION_PADES+EntidadesAdmUtil.obtenerEntidadObject(cct).getIdentificador());
	}

	public static Seat getSeatAdmin(ClientContext cct) {
		Seat seat = new Seat();
		seat.setCode(EntidadesAdmUtil.obtenerEntidadObject(cct).getDir3());
		seat.setDescription(EntidadesAdmUtil.obtenerEntidadObject(cct).getNombreLargo());
		return seat;
	}

}
