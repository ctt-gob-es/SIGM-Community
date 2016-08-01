package es.dipucr.contratacion.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ServiciosWebContratacionFunciones {
	
	private static Logger logger = Logger.getLogger(ServiciosWebContratacionFunciones.class);
	
	public static String getDireccionSW(){
    	String url = "";
        try {
			url = ServiciosWebConfiguration.getInstance(OrganizationUser
					.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_PLATAFORMACONTRATACION_SW);
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar la dirección del Servicio Web de la Plataforma de Contratación. " + e.getMessage(), e);
		}
        return url;
    }

}
