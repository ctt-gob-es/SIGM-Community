/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sigm.servlets;

import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.DatosUsuario;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * Servlet de login de PR.
 * 
 */
public class LoginLdapRegServlet extends LoginLdapServlet {
    private static final Logger logger = Logger.getLogger(LoginLdapRegServlet.class);
    private static final long serialVersionUID = 7244072384073764702L;

    @Override
    public void createSession(HttpServletRequest request, long idUsuario, String userName) {
	String datosSesion =
		"<IdUsuario>" + idUsuario + "</IdUsuario>" + "<TipoAutenticacion>"
			+ DatosUsuario.AUTHENTICATION_TYPE_INVESDOC + "</TipoAutenticacion>";

	request.getSession().setAttribute(
		ConstantesGestionUsuariosBackOffice.PARAMETRO_DATOS_SESION, datosSesion);
	request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_USUARIO,
		userName);

	String key = new Guid().toString();

	request.getSession().setAttribute(
		ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO, key);
    }

}