package es.dipucr.notifica.commons;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.serviciosWeb.ServiciosWebConfiguration;

public class ServiciosWebNotificaFunciones {
	
	private static Logger logger = Logger.getLogger(ServiciosWebNotificaFunciones.class);
	
	public static String getDireccionSW(){
    	String url = "";
        //try {
			//url = ServiciosWebConfiguration.getInstance(OrganizationUser.getOrganizationUserInfo().getOrganizationId()).get(ServiciosWebConfiguration.URL_NOTIFICA_SW);
			url = "https://notificaws.redsara.es/ws/soap/NotificaWS";
//		} catch (ISPACRuleException e) {
//			logger.error("Error al recuperar la dirección del Servicio Web del Notifica. " + e.getMessage(), e);
//		}
        return url;
    }

}
