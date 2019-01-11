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

import ieci.tecdoc.sgm.backoffice.expection.DaoException;
import ieci.tecdoc.sgm.backoffice.utils.Utilidades;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdrOB;
import es.msssi.sigm.dao.dataaccess.service.SIGMServiceManager;

/**
 * Servlet de aceptacion de obligaciones.
 * 
 */
public class AcceptObligServlet extends HttpServlet {

    private static final long serialVersionUID = 7244072384073764702L;
    private static String urlLogin = null;

    @Override
    public void init(final ServletConfig config) throws ServletException {

	setUrlLogin(config.getInitParameter("urlLogin"));

    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	String idApplication = null;
	Long idUser = null;
	String accept = null;
	try {
	    if (request != null && request.getSession() != null) {
		if (request.getParameter("accept") != null) {
		    accept = request.getParameter("accept");
		}
		if (Utilidades.isNuloOVacio(accept) || "N".equals(accept)) {
		    request.getRequestDispatcher("logoutLdapServlet").forward(request, response);
		}
		else {
		    if ("S".equals(accept)) {
			if (request.getSession().getAttribute(
				ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION) != null) {
			    idApplication =
				    (String) request
					    .getSession()
					    .getAttribute(
						    ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION);
			}

			if (request.getSession().getAttribute(
				ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_USUARIO) != null) {
			    idUser =
				    (Long) request
					    .getSession()
					    .getAttribute(
						    ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_USUARIO);
			}
			insertObligationUsuarioSIGM(idUser, idApplication);
			 response.sendRedirect(request.getContextPath()+ getUrlLogin());
		    }
		}
	    }
	}
	catch (DaoException exception) {

	}
    }

    /**
     * Insertamos en base de datos la aceptación de las condiciones de uso de la
     * aplicación
     * 
     * @param id
     *            : id de usuario
     * @param app
     *            : acrónimo de la aplicación: TM (Expedientes) y RP (Registro)
     * @return
     * @throws DaoException
     */
    private int insertObligationUsuarioSIGM(long id, String app) throws DaoException {

	IUserUserHdrOB iUserUserHdrOB = new IUserUserHdrOB();

	iUserUserHdrOB.setId(id);
	iUserUserHdrOB.setFlag(1);
	iUserUserHdrOB.setApp(app);

	return SIGMServiceManager.getiUserUserHdrOBService().insert(iUserUserHdrOB);

    }
    /**
     * Obtiene el valor del parámetro urlLogin.
     * 
     * @return urlLogin valor del campo a obtener.
     */
    public static String getUrlLogin() {
        return urlLogin;
    }
    /**
     * Guarda el valor del parámetro urlLogin.
     * 
     * @param urlLogin
     *            valor del campo a guardar.
     */
    public static void setUrlLogin(String urlLogin) {
	AcceptObligServlet.urlLogin = urlLogin;
    }

}