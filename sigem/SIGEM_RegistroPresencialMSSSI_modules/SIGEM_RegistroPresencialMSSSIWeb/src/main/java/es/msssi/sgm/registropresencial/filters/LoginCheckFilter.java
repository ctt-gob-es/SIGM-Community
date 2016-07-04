/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.filters;

import ieci.tecdoc.sgm.core.admin.web.AutenticacionBackOffice;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.Utils;
import es.msssi.sgm.registropresencial.beans.WebParameter;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.utils.AuthenticationHelper;

/**
 * Filtro de control de login del registro presencial.
 * 
 * @author cmorenog
 * */
public class LoginCheckFilter implements javax.servlet.Filter, Keys {
    // public static final long PROV_CITY_DIR = 0;
    private static final Logger LOG = Logger.getLogger(LoginCheckFilter.class);
    private static final String PARAM_EXCLUDE_PATHS = "excludePaths";
    private static final String PARAM_LOGOUT = "logout";
    private static final String DEFAULT_ENTITY = "000";
    private static final String DEFAULT_APP = "RP";

    /**
     * The servlet context that includes set of methods that a servlet uses to
     * communicate with its servlet container.
     */
    private ServletContext ctx;

    /**
     * A filter configuration object used by the web container to pass
     * information to a filter during initialization.
     */
    @SuppressWarnings("unused")
    private FilterConfig filterConfig;

    // rutas a excluir de las comprobaciones del filtro. separadas por ';'.
    private String excludePaths = null;
    //
    private String logout = null;

    /**
     * Inicia el filtro de configuración.
     * 
     * @param filterConfig
     *            Filtro de configuración.
     */
    public void init(
	FilterConfig filterConfig) {
	LOG.trace("Entrando en LoginCheckFilter.init(FilterConfig)"
	    + " para iniciar el filtro de configuración");
	this.filterConfig = filterConfig;
	ctx = filterConfig.getServletContext();
	excludePaths = filterConfig.getInitParameter(PARAM_EXCLUDE_PATHS);
	logout = filterConfig.getInitParameter(PARAM_LOGOUT);
	init(ctx);
    }

    /**
     * Realiza un filtrado de la petición HTTP.
     * 
     * @param req
     *            Petición HTTP.
     * @param res
     *            Respuessta HTTP.
     * @param chain
     *            Cadena de filtrado.
     * 
     * @throws IOException
     *             Si se produce un error de entrada/salida.
     * @throws ServletException
     *             Se se produce un error en el servlet.
     */
    public void doFilter(
	ServletRequest req, ServletResponse res, FilterChain chain)
	throws IOException, ServletException {
	LOG.trace("Entrando en LoginCheckFilter.doFilter()");
	boolean exit = false;
	if (req != null) {
	    HttpServletRequest request = (HttpServletRequest) req;
	    HttpServletResponse response = (HttpServletResponse) res;
	    // could pass in false in the getSession() to return null for new
	    // session.
	    HttpSession mySession = request.getSession();
	    if (mySession == null) {
		LOG.error(SessionException.ERROR_SESSION_EXPIRED);
		request.setAttribute(
		    ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD, DEFAULT_ENTITY);
		response.sendRedirect(AutenticacionBackOffice.obtenerUrlLogin(
		    request, DEFAULT_APP));
		exit = true;
	    }
	    else {
		mySession.setAttribute(
		    ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD, DEFAULT_ENTITY);
		Object loginStatus = mySession.getAttribute(Keys.J_USECASECONF);

		// Seteamos datos para auditoria
		Utils.setAuditContext(request);

		try {
		    if (!containsPathToIgnore(request.getRequestURI()) &&
			!AuthenticationHelper.authenticate(request)) {
			LOG.info("LoginCheckFilter mySession: " +
			    mySession);
			LOG.info("LoginCheckFilter request.getRequestURI(): " +
			    request.getRequestURI());
			LOG.info("No hay sesion válida: ");
			LOG.info("loginStatus: " +
			    loginStatus);
			LOG.info("ServletPath: " +
			    request.getServletPath());
			LOG.info("RequestURI: " +
			    request.getRequestURI());

			if (loginStatus == null) {
			    request.setAttribute(
				ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD,
				DEFAULT_ENTITY);
			    response.sendRedirect(AutenticacionBackOffice.obtenerUrlLogin(
				request, DEFAULT_APP));
			    exit = true;
			}
			else if (!request.getServletPath().equals(
			    logout)) {
			    request.setAttribute(
				ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD,
				DEFAULT_ENTITY);
			    AuthenticationHelper.logout(request);
			    response.sendRedirect(logout);
			    exit = true;
			}
		    }
		}
		// Si falla, redireccionamos a la página de error
		catch (RPGenericException rpGenericException) {
		    LOG.error(ErrorConstants.AUTHENTICATION_ERROR_MESSAGE +
			". Código: " + rpGenericException.getCode().getCode() + " . Mensaje: " +
			rpGenericException.getShortMessage());
		    es.msssi.sgm.registropresencial.utils.Utils.redirectToErrorPage(
			rpGenericException, null, null);
		}
		catch (RPBookException rpBookException) {
		    LOG.error(ErrorConstants.AUTHENTICATION_ERROR_MESSAGE +
			".Código: " + rpBookException.getCode().getCode() + " .Mensaje: " +
			rpBookException.getShortMessage());
		    es.msssi.sgm.registropresencial.utils.Utils.redirectToErrorPage(
			rpBookException, null, null);
		}

	    }
	    if (exit) {
		return;
	    }
	    chain.doFilter(
		request, response);
	}
    }

    /**
     * Guarda el valor del parámetro filterConfig.
     * 
     * @param filterConfig
     *            valor del campo a guardar.
     */
    public void setFilterConfig(
	FilterConfig filterConfig) {
	this.filterConfig = filterConfig;
    }

    /**
     * Destruye el filtro de configuración.
     */
    public void destroy() {
	this.filterConfig = null;
    }

    /**
     * Método que comprueba si el path se excluye de la validación.
     * 
     * @param requestURI
     *            URI a comprobar.
     * @return excludePath Valor a <i>true</i> si se excluye y <i>false</i> en
     *         caso contrario.
     */
    private boolean containsPathToIgnore(
	String requestURI) {
	LOG.trace("Entrando en LoginCheckFilter.containsPathToIgnore()");
	boolean excludePath = false;
	java.util.StringTokenizer tokenizer = new java.util.StringTokenizer(
	    excludePaths, ";");
	while (tokenizer.hasMoreElements() &&
	    !excludePath) {
	    String ignorePath = tokenizer.nextToken();
	    if (requestURI.indexOf(ignorePath) != -1) {
		excludePath = true;
		LOG.info("Path: " +
		    ignorePath + " excluído");
	    }
	}
	return excludePath;
    }

    /**
     * Inicia con el contexto de aplicación.
     * 
     * @param context
     *            ServletContext principal de la aplicación web.
     * 
     * @throws NamingException.
     */
    @SuppressWarnings("unchecked")
    private void init(
	ServletContext context) {
	LOG.trace("Entrando en LoginCheckFilter.init(ServletContext)"
	    + " para iniciar el contexto de la aplicación");
	Enumeration<String> e = context.getInitParameterNames();
	HashMap<String, String> initParameters = new HashMap<String, String>();
	String key = null;
	while (e.hasMoreElements()) {
	    key = e.nextElement();
	    initParameters.put(
		key, context.getInitParameter(key));
	}
	WebParameter.setInitParameters(initParameters);
	try {
	    Context inicial = new InitialContext();
	    Context miCtx = (Context) inicial.lookup("java:comp/env");
	    HashMap<String, Object> entryParameters = new HashMap<String, Object>();
	    Enumeration<NameClassPair> namingE = miCtx.list("");
	    while (namingE.hasMoreElements()) {
		key = ((NameClassPair) namingE.nextElement()).getName();
		entryParameters.put(
		    key, miCtx.lookup(key));
	    }
	    WebParameter.setEntryParameters(entryParameters);
	}
	catch (NamingException namingException) {
	    LOG.error(
		"Error al crear el contexto principal de la aplicación", namingException);
	    es.msssi.sgm.registropresencial.utils.Utils.redirectToErrorPage(
		null, null, namingException);
	}
    }
}