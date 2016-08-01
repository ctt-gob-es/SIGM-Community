package es.dipucr.bdns.common;

import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;


public class WSBDNSProperties {

	public final static String URL_REPLACE_APP = "[ID_APLICACION]";
	public final static String URL_REPLACE_USER = "[ID_USUARIO]";
	
	private static final Logger logger = Logger.getLogger(WSBDNSProperties.class);
	
	public static String getURL(String codAplicacionVarName) /**throws ISPACException**/ {
		String url = "";
		try {
			String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			ServiciosWebConfiguration wsConfig = ServiciosWebConfiguration.getInstance(idEntidad);
			url = wsConfig.get(ServiciosWebConfiguration.URL_BDNS);
			String user = wsConfig.get(ServiciosWebConfiguration.BDNS_USER);
			String app = wsConfig.get(codAplicacionVarName);
			url = url.replace(URL_REPLACE_APP, app);
			url = url.replace(URL_REPLACE_USER, user);
			
		} catch (Exception e) {
			String error = "Error al recuperar la dirección del servicio web de la BDNS - " + codAplicacionVarName;
			logger.error(error + " :: " + e.getMessage(), e);
//			throw new ISPACException(error, e);
		}
		return url;
	}
}
