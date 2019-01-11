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

import com.ieci.tecdoc.isicres.audit.helper.ISicresAuditHelper;
import es.msssi.sgm.registropresencial.daos.UserDAO;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdrOB;
import es.msssi.sigm.dao.dataaccess.service.SIGMServiceManager;
import es.msssi.verdni.esquemas.peticionrepresenta.DatosEspecificosRepresenta;
import es.msssi.verdni.esquemas.peticionrepresenta.PeticionRepresenta;
import es.msssi.verdni.esquemas.respuestarepresenta.RespuestaRepresenta;
import ieci.tecdoc.sgm.backoffice.expection.DaoException;
import ieci.tecdoc.sgm.backoffice.msssi.conector.ldapws.LdapWSService;
import ieci.tecdoc.sgm.backoffice.msssi.conector.verdni.ConsultaRepresentaWSService;
import ieci.tecdoc.sgm.backoffice.msssi.connector.ldapws.beans.*;
import ieci.tecdoc.sgm.backoffice.utils.ResourceLDAP;
import ieci.tecdoc.sgm.backoffice.utils.Utilidades;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * Servlet de login.
 */
public abstract class LoginLdapServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginLdapServlet.class);
    private static final long serialVersionUID = 7244072384073764702L;
    private static final int USER_IS_LOCKED = 1;

    private static String idApplication = null;
    private static String idEntity = null;
    private static String groupLDAP = null;
    private static String urlLogin = null;

    private static LdapWSService servicioWSLdap = null;
    private WebApplicationContext springContext;

    @Override
    public void init(final ServletConfig config) throws ServletException {
        super.init(config);
        setIdApplication(config.getInitParameter("idApplication"));
        setIdEntity(config.getInitParameter("idEntity"));
        setGroupLDAP(config.getInitParameter("groupLDAP"));
        setUrlLogin(config.getInitParameter("urlLogin"));

        springContext =
                WebApplicationContextUtils.getRequiredWebApplicationContext(config
                        .getServletContext());
        final AutowireCapableBeanFactory beanFactory =
                springContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(this);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuarioDominio = null;
        String usuario = null;
        //String dominio = null;
        String password = null;
        try {
            if (request != null && request.getSession() != null) {
                usuario = request.getParameter("username");
                password = request.getParameter("password");
                //dominio = request.getParameter("dominio");
                if (getIdApplication() != null) {
                    request.getSession().setAttribute(
                            ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION,
                            idApplication);
                }
                if (getIdEntity() != null) {
                    request.getSession().setAttribute(
                            ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD, idEntity);
                }
                if (Utilidades.isNuloOVacio(usuario)) {
                    ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP.getInstance()
                            .getProperty("autenticacion.error.username"));

                    request.setAttribute("ERROR",
                            ResourceLDAP.getInstance().getProperty("autenticacion.error.username"));
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    response.setContentType("text/html");
                    rd.include(request, response);
                    return;
                }
                if (Utilidades.isNuloOVacio(password)) {
                    ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP.getInstance()
                            .getProperty("autenticacion.error.password"));

                    request.setAttribute("ERROR",
                            ResourceLDAP.getInstance().getProperty("autenticacion.error.password"));
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    response.setContentType("text/html");
                    rd.include(request, response);
                    return;
                }

                // Concatenamos el dominio al usuario
        /*if (!Utilidades.isNuloOVacio(dominio)) {
            usuarioDominio = usuario + "@" + dominio;
		}
		else {
		    usuarioDominio = usuario;
		}*/
                usuarioDominio = usuario;
                // Comprobamos si el usuario existe en SIGM
                IUserUserHdr iUserUserHdr = getUsuarioSIGM(usuarioDominio);

                if (iUserUserHdr == null) {
                    ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP.getInstance()
                            .getProperty("autenticacion.error.sigm"));

                    request.setAttribute("ERROR",
                            ResourceLDAP.getInstance().getProperty("autenticacion.error.sigm"));
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    response.setContentType("text/html");
                    rd.include(request, response);
                    return;
                }

                // Comprobamos que el usuario no este bloqueado
                if (iUserUserHdr.getStat() == USER_IS_LOCKED) {
                    ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP.getInstance()
                            .getProperty("autenticacion.error.sigm.block"));

                    request.setAttribute("ERROR",
                            ResourceLDAP.getInstance()
                                    .getProperty("autenticacion.error.sigm.block"));
                    RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                    response.setContentType("text/html");
                    rd.include(request, response);
                    return;
                } else {
                    // Comprobamos autenticación del usuario en LDAP
                    ResponseType responseType =
                            comprobarUsuarioValidoLdap(usuarioDominio, password);

                    if (responseType.getError() != null) {

                        if (responseType.getError().getMensaje() != null) {
                            logger.error(responseType.getError().getMensaje());
                        }
                        if (responseType.getError().getDescripcion() != null) {
                            logger.error(responseType.getError().getDescripcion());
                        }
                        if (responseType.getError().getCodigo() != null) {
                            if (responseType.getError().getCodigo().equals("08")
                                    || responseType.getError().getCodigo().equalsIgnoreCase("23")) {
                                ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP
                                        .getInstance().getProperty("autenticacion.error.ldap"));

                                request.setAttribute("ERROR", ResourceLDAP.getInstance()
                                        .getProperty("autenticacion.error.ldap"));
                            } else {
                                if (responseType.getError().getCodigo().equals("05")) {
                                    ISicresAuditHelper.auditarFalloLogin(
                                            usuarioDominio,
                                            ResourceLDAP.getInstance().getProperty(
                                                    "autenticacion.error.ldap.noexist"));

                                    request.setAttribute("ERROR", ResourceLDAP.getInstance()
                                            .getProperty("autenticacion.error.ldap.noexist"));
                                } else {
                                    ISicresAuditHelper.auditarFalloLogin(
                                            usuarioDominio,
                                            ResourceLDAP.getInstance().getProperty(
                                                    "autenticacion.error.ldap.permissions"));

                                    request.setAttribute("ERROR", ResourceLDAP.getInstance()
                                            .getProperty("autenticacion.error.ldap.permissions"));
                                }
                            }
                        } else {
                            ISicresAuditHelper.auditarFalloLogin(
                                    usuarioDominio,
                                    ResourceLDAP.getInstance().getProperty(
                                            "autenticacion.error.ldap.permissions"));

                            request.setAttribute(
                                    "ERROR",
                                    ResourceLDAP.getInstance().getProperty(
                                            "autenticacion.error.ldap.permissions"));
                        }
                        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                        response.setContentType("text/html");
                        rd.include(request, response);
                        return;
                    } else {
                        if (responseType.getResultado() != null
                                && !responseType.getResultado().isResultadoOperacion()) {

                            ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP
                                    .getInstance().getProperty("autenticacion.error.ldap"));

                            request.setAttribute("ERROR",
                                    ResourceLDAP.getInstance().getProperty("autenticacion.error.ldap"));
                            RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                            response.setContentType("text/html");
                            rd.include(request, response);
                            return;
                        }
                    }

                    ResponseType responseTypeGroup =
                            comprobarUsuarioGrupo(usuarioDominio, getGroupLDAP());
                    if (responseTypeGroup.getError() != null) {
                        ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP
                                .getInstance().getProperty("autenticacion.error.ldap.permissions"));

                        logger.error(responseTypeGroup.getError().getMensaje());
                        request.setAttribute(
                                "ERROR",
                                ResourceLDAP.getInstance().getProperty(
                                        "autenticacion.error.ldap.permissions"));
                        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                        response.setContentType("text/html");
                        rd.include(request, response);
                        return;
                    } else if (responseTypeGroup.getResultado() != null
                            && !responseTypeGroup.getResultado().isResultadoOperacion()) {
                        ISicresAuditHelper.auditarFalloLogin(usuarioDominio, ResourceLDAP
                                .getInstance().getProperty("autenticacion.error.ldap.permissions"));
                        request.setAttribute(
                                "ERROR",
                                "El usuario no tiene permisos para acceder");
                        RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                        response.setContentType("text/html");
                        rd.include(request, response);
                        return;
                    }

                    request.getSession().setAttribute(
                            ConstantesGestionUsuariosBackOffice.PARAMETRO_USUARIO, usuarioDominio);
                    request.getSession().setAttribute(
                            ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_USUARIO,
                            iUserUserHdr.getId());
                    request.getSession().setAttribute(
                            ConstantesGestionUsuariosBackOffice.PARAMETRO_PASSWORD, password);
          /*  request.getSession().setAttribute(
                ConstantesGestionUsuariosBackOffice.PARAMETRO_DOMINIO, dominio);*/

                    createSession(request, iUserUserHdr.getId(), usuarioDominio);

                    // obligaciones de uso de la aplicación
                    IUserUserHdrOB iUserUserHdrOB =
                            getObligationUsuarioSIGM(iUserUserHdr.getId(), getIdApplication());


                    //añadimos en sesion el dni y si es funcionario habilitado
                    UserDAO userDAO = (UserDAO) springContext.getBean("userDAO");
                    String userDni = userDAO.getUserDni(Long.valueOf(iUserUserHdr.getId()).intValue());
                    request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_DNI,
                            userDni);
                    //comprobamos si es funcionario habilitado
                    boolean funcionarioHabilitado = isFuncionarioHabilitado(userDni);
                    request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_FUNCIONARIO_HABILITADO,
                            funcionarioHabilitado);




		    /*
		     * Si no ha aceptado las obligaciones reenviamos al
		     * formulario
		     */
                    if (iUserUserHdrOB == null) {
                        if (Utilidades.isNuloOVacio(iUserUserHdrOB)) {
                            RequestDispatcher rd = request.getRequestDispatcher("obligation.jsp");
                            response.setContentType("text/html");
                            rd.include(request, response);
                            return;
                        }
                    }
                    response.sendRedirect(request.getContextPath() + getUrlLogin());
                    return;
                }

            }
        } catch (Exception e) {
            logger.error("Se ha producido un error en el login", e);
            request.setAttribute("ERROR",
                    ResourceLDAP.getInstance().getProperty("autenticacion.error"));
            RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
            response.setContentType("text/html");
            rd.include(request, response);
        }

    }


    private boolean isFuncionarioHabilitado(String userDni) {
        boolean enableCompulsaValue = false;
        try {
            ConsultaRepresentaWSService consultaRepresentaWSService = (ConsultaRepresentaWSService) springContext.getBean("verdni_representa");
            if (consultaRepresentaWSService.isActiveConsultaRepresenta()) {
                if (StringUtils.isNotBlank(userDni)) {
                    PeticionRepresenta petcionRepresenta = new PeticionRepresenta();
                    DatosEspecificosRepresenta datosEspecificosRepresenta = new DatosEspecificosRepresenta();
                    datosEspecificosRepresenta.setIdAPP("ID APP");
                    datosEspecificosRepresenta.setNif(userDni);
                    DatosEspecificosRepresenta.ListaColectivos listaColectivos = new DatosEspecificosRepresenta.ListaColectivos();
                    DatosEspecificosRepresenta.ListaColectivos.Colectivo colectivo = new DatosEspecificosRepresenta.ListaColectivos.Colectivo();
                    colectivo.setIdColectivo("RFH");
                    listaColectivos.getColectivo().add(colectivo);
                    datosEspecificosRepresenta.setListaColectivos(listaColectivos);
                    petcionRepresenta.setDatosEspecificosRepresenta(datosEspecificosRepresenta);
                    RespuestaRepresenta respuestaRepresenta = consultaRepresentaWSService.consultarRepresenta(petcionRepresenta);
                    if ("true".equalsIgnoreCase(respuestaRepresenta.getDatosRespuestaRepresentaEspecificos().getListaColectivos().get(0).getPertenece())) {
                        enableCompulsaValue = true;
                    }
                    logger.info("enableCompulsaValue -->" + enableCompulsaValue);
                }
            } else {
                //sino está activa la llamada, activamos todas las opciones
                enableCompulsaValue = true;
            }
        } catch (Exception e) {
            logger.error("Error al llamar a Representa en isEnableCompulsa", e);
        }
        return enableCompulsaValue;
    }

    public abstract void createSession(HttpServletRequest request, long idUsuario, String userName);

    /**
     * Realizamos una consulta a SIGM para comprobar que el usuario esta dado de
     * alta y no esta bloqueado
     *
     * @param usuario
     * @return IUserUserHdr con los datos de la tabla IUSERUSERHDR DE SIGM
     * @throws DaoException
     */
    private IUserUserHdr getUsuarioSIGM(String usuario) throws DaoException {

        return SIGMServiceManager.getiUserUserHdrService().getByName(usuario);

    }

    /**
     * Realizamos una consulta a SIGM para comprobar que el usuario ha aceptado
     * las obligaciones de seguridad
     *
     * @param id
     * @param app
     * @return IUserUserHdr con los datos de la tabla IUSERUSERHDR DE SIGM
     * @throws DaoException
     */
    private IUserUserHdrOB getObligationUsuarioSIGM(long id, String app) throws DaoException {

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("id", id + "");
        params.put("app", app);

        return SIGMServiceManager.getiUserUserHdrOBService().getByIdApp(params);

    }

    /**
     * Realizamos una petición al servicio web de LDAP (BUS) para comprobar
     * usuario y contraseña
     *
     * @param usuario
     * @param password
     * @return ResponseType
     **/
    private ResponseType comprobarUsuarioValidoLdap(String usuario, String password) {

        UsuarioLogin usuarioLoginLdap = new UsuarioLogin();

        usuarioLoginLdap.setCn(usuario);
        usuarioLoginLdap.setUserPassword(password);

        return servicioWSLdap.comprobarUsuarioValido(usuarioLoginLdap);
    }

    /**
     * Realizamos una petición al servicio web de LDAP (BUS) para comprobar si
     * el usuario tiene permisos para acceder a la aplicación
     *
     * @param usuario
     * @param grupoLdap
     * @return ResponseType
     */
    protected ResponseType comprobarUsuarioGrupo(String usuario, String grupoLdap) {

        UsuarioGrupo usuarioGrupo = new UsuarioGrupo();

        UsuarioBase usuarioBase = new UsuarioBase();
        usuarioBase.setCn(usuario);
        usuarioGrupo.setUsuario(usuarioBase);

        GrupoBase grupoBase = new GrupoBase();
        grupoBase.setCn(grupoLdap);
        usuarioGrupo.setGrupo(grupoBase);

        return servicioWSLdap.comprobarUsuarioGrupo(usuarioGrupo);
    }

    /**
     * Obtiene el valor del parámetro idApplication.
     *
     * @return idApplication valor del campo a obtener.
     */
    public static String getIdApplication() {
        return idApplication;
    }

    /**
     * Guarda el valor del parámetro idApplication.
     *
     * @param idApplication valor del campo a guardar.
     */
    public static void setIdApplication(String idApplication) {
        LoginLdapServlet.idApplication = idApplication;
    }

    /**
     * Obtiene el valor del parámetro idEntity.
     *
     * @return idEntity valor del campo a obtener.
     */
    public static String getIdEntity() {
        return idEntity;
    }

    /**
     * Guarda el valor del parámetro idEntity.
     *
     * @param idEntity valor del campo a guardar.
     */
    public static void setIdEntity(String idEntity) {
        LoginLdapServlet.idEntity = idEntity;
    }

    /**
     * Obtiene el valor del parámetro servicioWSLdap.
     *
     * @return servicioWSLdap valor del campo a obtener.
     */
    public LdapWSService getServicioWSLdap() {
        return servicioWSLdap;
    }

    /**
     * Guarda el valor del parámetro servicioWSLdap.
     *
     * @param servicioWSLdap valor del campo a guardar.
     */
    @Autowired
    public void setServicioWSLdap(LdapWSService servicioWSLdap) {
        LoginLdapServlet.servicioWSLdap = servicioWSLdap;
    }

    /**
     * Obtiene el valor del parámetro groupLDAP.
     *
     * @return groupLDAP valor del campo a obtener.
     */
    public static String getGroupLDAP() {
        return groupLDAP;
    }

    /**
     * Guarda el valor del parámetro groupLDAP.
     *
     * @param groupLDAP valor del campo a guardar.
     */
    public static void setGroupLDAP(String groupLDAP) {
        LoginLdapServlet.groupLDAP = groupLDAP;
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
     * @param urlLogin valor del campo a guardar.
     */
    public static void setUrlLogin(String urlLogin) {
        LoginLdapServlet.urlLogin = urlLogin;
    }

}