package es.dipucr.svd.services;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ServiciosWebSVDFunciones {
	
	private static Logger logger = Logger.getLogger(ServiciosWebSVDFunciones.class);
	
	public static String getDireccionRecubrimientoSW(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_SVD_RECUBRIMIENTO_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web de Recubrimiento del Servicio de Verificación de Datos. " + e.getMessage(), e);
		}
        return url;
    }
	public static String getDireccionSCSPSW(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_SVD_SCSP_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web de SCSP del Servicio de Verificación de Datos. " + e.getMessage(), e);
		}
        return url;
    }
	public static String getDireccionClienteLigeroSW(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_SVD_CLIENTELIGERO_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web de Cliente Ligero del Servicio de Verificación de Datos. " + e.getMessage(), e);
		}
        return url;
    }

}
