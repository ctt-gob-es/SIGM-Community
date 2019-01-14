package es.dipucr.pempleado.services;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class WSPEmpleadoAytosProperties {

	private static final Logger logger = Logger.getLogger(WSPEmpleadoAytosProperties.class);
	
	public static String getURL() {
		String url = "";
		try {
			String idEntidad = OrganizationUser.getOrganizationUserInfo().getOrganizationId();
			url = ServiciosWebConfiguration.getInstance(idEntidad).get(ServiciosWebConfiguration.URL_PEMPLEADO_AYTOS_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del servicio web de la Web del Empleado. " + e.getMessage(), e);			
		}
		return url;
	}
}
