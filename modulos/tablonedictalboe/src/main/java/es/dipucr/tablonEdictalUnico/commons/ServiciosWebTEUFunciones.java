package es.dipucr.tablonEdictalUnico.commons;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ServiciosWebTEUFunciones {
	
	private static Logger logger = Logger.getLogger(ServiciosWebTEUFunciones.class);
	
	public static String getDireccionSW(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_TEU_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web del TEU. " + e.getMessage(), e);
		}
        return url;
    }

}
