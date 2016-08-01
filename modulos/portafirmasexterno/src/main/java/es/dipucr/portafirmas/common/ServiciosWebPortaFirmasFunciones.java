package es.dipucr.portafirmas.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ServiciosWebPortaFirmasFunciones {
	
	private static Logger logger = Logger.getLogger(ServiciosWebPortaFirmasFunciones.class);
	
	public static String getDireccionSWModify(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_PORTAFIRMASEXTERNOMODIFY);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web del TEU. " + e.getMessage(), e);
		}
        return url;
    }
	
	public static String getDireccionSWAdmin(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_PORTAFIRMASEXTERNOADMIN);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web del TEU. " + e.getMessage(), e);
		}
        return url;
    }
	
	public static String getDireccionSWConsulta(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_PORTAFIRMASEXTERNO_CONSULTA);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web del TEU. " + e.getMessage(), e);
		}
        return url;
    }

}
