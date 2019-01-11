package es.msssi.sigm.servlets.logincert;

import ieci.tecdoc.sgm.backoffice.expection.DaoException;
import ieci.tecdoc.sgm.backoffice.msssi.conector.verdni.ConsultaRepresentaWSService;
import ieci.tecdoc.sgm.backoffice.utils.ResourceLDAP;
import ieci.tecdoc.sgm.base.guid.Guid;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.DatosUsuario;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import servcert.msssi.es.tipos.PeticionValidacionType;
import servcert.msssi.es.tipos.RespuestaValidacionType;

import com.ieci.tecdoc.isicres.audit.helper.ISicresAuditHelper;

import es.mscbs.wsclient.servcert.facade.ServcertWSService;
import es.mscbs.wsclient.servcert.utils.ServcertClientUtils;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdrOB;
import es.msssi.sigm.servlets.logincert.dao.LoginCertUserDao;
import es.msssi.verdni.esquemas.peticionrepresenta.DatosEspecificosRepresenta;
import es.msssi.verdni.esquemas.peticionrepresenta.PeticionRepresenta;
import es.msssi.verdni.esquemas.respuestarepresenta.RespuestaRepresenta;

public class LoginCertServlet extends HttpServlet {
  private static final Logger logger = Logger.getLogger(LoginCertServlet.class);
  private static final long serialVersionUID = -275770516314912613L;
  private static final int USER_IS_LOCKED = 1;
  private static String idApplication = null;
  private static String idEntity = null;
  private static String urlLogin = null;

  private static WebApplicationContext springContext;

  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    setIdApplication(config.getInitParameter("idApplication"));
    setIdEntity(config.getInitParameter("idEntity"));
    setUrlLogin(config.getInitParameter("urlLogin"));

    springContext =
        WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    final AutowireCapableBeanFactory beanFactory = springContext.getAutowireCapableBeanFactory();
    beanFactory.autowireBean(this);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
      doPost(request, response);
  }
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String userDni = null;
    String usuarioDominio = null;
    try {
      if (request != null && request.getSession() != null) {
        if (getIdApplication() != null) {
          request.getSession().setAttribute(
              ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION, idApplication);
        }
        if (getIdEntity() != null) {
          request.getSession().setAttribute(
              ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD, idEntity);
        }

        // parece que en real es un array de la cadena de certs??
        X509Certificate cert = null;
//        if (isLocal(request))  userDni = "26247803L"; // userDni = "99999999R"
//        else {
        if (isLocal(request)) {
          cert = ServcertClientUtils.getTestCertificateFromFile(springContext.getBean("PATH_REPO")+"ficheros/testCertLocal/testCertLocal.cer");
        } else {
          cert = ServcertClientUtils.getX509Certificate(request);
        }
        if (cert == null) {
          trazarErrorResponder("?", ResourceLDAP.getInstance().getProperty(
              "autenticacion.error.sigm.nocert"), request, response);
          return; // se apilan errores si no volvemos
        }
        // 2 Obtener cert info (servcert nuevo) bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, webServiceLocation);
        PeticionValidacionType petCert = new PeticionValidacionType();
        petCert.setCertificadoBytes(cert.getEncoded());
        petCert.setInformacionCompleta(true);
        petCert.setIdApp((String) springContext.getBean("IDAPP_SERVCERT"));
        RespuestaValidacionType respCert = 
          new ServcertWSService().verificaCertificado(petCert);
        
        String errorMsg = ServcertClientUtils.getErrorMsg(respCert);
        if (errorMsg != null) {
          trazarErrorResponder("?", errorMsg, request, response);
          return; // se apilan errores si no volvemos
        }
        
//      userDni = respCert.getInformacionCertificado().getCampos().getDni(); 
        userDni = ServcertClientUtils.getUpperPaddedNIF(respCert);
         
        // 3 Comprobamos si el DNI existe en SIGM
        // IUserUserHdr iUserUserHdr = getUsuarioSIGM(usuarioDominio);
        List<IUserUserHdr> iUserUserHdr = userDni == null ? null : getUsuarioSIGM(userDni);
        if (iUserUserHdr == null || iUserUserHdr.size() == 0) {
             trazarErrorResponder(userDni, ResourceLDAP.getInstance().getProperty(
                  "autenticacion.error.sigm"), request, response);
          return; // se apilan errores si no volvemos
        } else {
          // Verificamos que el usuario asociado al DNI pudiera ser el del certificado
          boolean isSimilarSurname = ServcertClientUtils.isSimilarSurname(iUserUserHdr.get(0).getSurname(), respCert);
          logger.error("NOT ERROR : " + iUserUserHdr.get(0).getSurname() + " contra " 
              + respCert.getInformacionCertificado().getCampos().getApellidos()
              + (isSimilarSurname ? " " : " NO ") + "ha validado");      
          if (!isSimilarSurname){
            trazarErrorResponder(userDni, ResourceLDAP.getInstance().getProperty(
                "autenticacion.error.sigm"), request, response);
            return; // se apilan errores si no volvemos
          }        
        }
        if (iUserUserHdr.size() > 1) {
          String affectedUsers = " (";
          for (IUserUserHdr iuser : iUserUserHdr) {
            affectedUsers +=
                iuser.getName() == null ? ("[" + iuser.getId() + "]") : iuser.getName();
            affectedUsers += ",";
          }
              trazarErrorResponder(userDni, ResourceLDAP.getInstance().getProperty(
                  "autenticacion.error.sigm.multi")
                  + affectedUsers, request, response);
          return; // se apilan errores si no volvemos
        }

        // Comprobamos que el usuario no este bloqueado
        if (iUserUserHdr.get(0).getStat() == USER_IS_LOCKED) {
              trazarErrorResponder(userDni, ResourceLDAP.getInstance().getProperty(
                  "autenticacion.error.sigm.block"), request, response);
        } else {
          usuarioDominio = iUserUserHdr.get(0).getName();
          request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_USUARIO,
              usuarioDominio);
          request.getSession()
              .setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_USUARIO,
                  iUserUserHdr.get(0).getId());
          /*
           * request.getSession().setAttribute(
           * ConstantesGestionUsuariosBackOffice.PARAMETRO_PASSWORD, password);
           */
          /*
           * request.getSession().setAttribute(
           * ConstantesGestionUsuariosBackOffice.PARAMETRO_DOMINIO, dominio);
           */

          createSession(request, iUserUserHdr.get(0).getId(), usuarioDominio);

          // obligaciones de uso de la aplicación
          IUserUserHdrOB iUserUserHdrOB =
              getObligationUsuarioSIGM(iUserUserHdr.get(0).getId(), getIdApplication());

          request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_DNI,
              userDni);
          // comprobamos si es funcionario habilitado
          boolean funcionarioHabilitado = isFuncionarioHabilitado(userDni);
          request.getSession().setAttribute(
              ConstantesGestionUsuariosBackOffice.PARAMETRO_FUNCIONARIO_HABILITADO,
              funcionarioHabilitado);

          /*
           * Si no ha aceptado las obligaciones reenviamos al formulario
           */
          if (iUserUserHdrOB == null) {
            RequestDispatcher rd = request.getRequestDispatcher("obligation.jsp");
            response.setContentType("text/html");
            rd.include(request, response);
            return;
          }
          response.sendRedirect(request.getContextPath() + getUrlLogin());
          // return;
        }

      }
    } catch (Exception e) {
      logger.error("Se ha producido un error en el login", e);
      request.setAttribute("ERROR", ResourceLDAP.getInstance().getProperty("autenticacion.error")
          + "(" + e.getMessage() + ")");
      RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
      response.setContentType("text/html");
      rd.include(request, response);
    }
  }

  private boolean trazarErrorResponder(String userDni, String mensaje, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {
    ISicresAuditHelper.auditarFalloLogin(userDni, mensaje);
    logger.error(mensaje + "(id:" + userDni + ")");
    request.setAttribute("ERROR", mensaje);
    RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
    response.setContentType("text/html");
    rd.include(request, response);
    return true;
  }

  private boolean isFuncionarioHabilitado(String userDni) {
    boolean enableCompulsaValue = false;
    try {
      ConsultaRepresentaWSService consultaRepresentaWSService =
          (ConsultaRepresentaWSService) springContext.getBean("verdni_representa");
      if (consultaRepresentaWSService.isActiveConsultaRepresenta()) {
        if (StringUtils.isNotBlank(userDni)) {
          PeticionRepresenta petcionRepresenta = new PeticionRepresenta();
          DatosEspecificosRepresenta datosEspecificosRepresenta = new DatosEspecificosRepresenta();
          datosEspecificosRepresenta.setIdAPP("ID APP");
          datosEspecificosRepresenta.setNif(userDni);
          DatosEspecificosRepresenta.ListaColectivos listaColectivos =
              new DatosEspecificosRepresenta.ListaColectivos();
          DatosEspecificosRepresenta.ListaColectivos.Colectivo colectivo =
              new DatosEspecificosRepresenta.ListaColectivos.Colectivo();
          colectivo.setIdColectivo("RFH");
          listaColectivos.getColectivo().add(colectivo);
          datosEspecificosRepresenta.setListaColectivos(listaColectivos);
          petcionRepresenta.setDatosEspecificosRepresenta(datosEspecificosRepresenta);
          RespuestaRepresenta respuestaRepresenta =
              consultaRepresentaWSService.consultarRepresenta(petcionRepresenta);
          if ("true".equalsIgnoreCase(respuestaRepresenta.getDatosRespuestaRepresentaEspecificos()
              .getListaColectivos().get(0).getPertenece())) {
            enableCompulsaValue = true;
          }
          logger.info("enableCompulsaValue -->" + enableCompulsaValue);
        }
      } else {
        // sino está activa la llamada, activamos todas las opciones
        enableCompulsaValue = true;
      }
    } catch (Exception e) {
      logger.error("Error al llamar a Representa en isEnableCompulsa", e);
    }
    return enableCompulsaValue;
  }

  // no se si es necesario de momento, ni siquiera parece asignar datos
  // especificos de registro
  // public abstract void createSession(HttpServletRequest request, long
  // idUsuario, String userName);
  public void createSession(HttpServletRequest request, long idUsuario, String userName) {
    String datosSesion =
        "<IdUsuario>" + idUsuario + "</IdUsuario>" + "<TipoAutenticacion>"
            + DatosUsuario.AUTHENTICATION_TYPE_INVESDOC + "</TipoAutenticacion>";

    request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_DATOS_SESION,
        datosSesion);
    request.getSession().setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_USUARIO,
        userName);

    String key = new Guid().toString();

    request.getSession().setAttribute(
        ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO, key);
  }

  /**
   * Realizamos una consulta a SIGM para comprobar que el usuario esta dado de
   * alta y no esta bloqueado
   * 
   * @param dni
   * @return IUserUserHdr con los datos de la tabla IUSERUSERHDR DE SIGM
   * @throws DaoException
   */
  private List<IUserUserHdr> getUsuarioSIGM(String dni) throws DaoException {
    LoginCertUserDao userDAO = (LoginCertUserDao) springContext.getBean("LoginCertUserDAO");
    return userDAO.getUsersByDni(dni);
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
    LoginCertUserDao userDAO = (LoginCertUserDao) springContext.getBean("LoginCertUserDAO");
    return userDAO.getUserObligation(id, app);
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
   * @param idApplication
   *          valor del campo a guardar.
   */
  public static void setIdApplication(String idApplication) {
    LoginCertServlet.idApplication = idApplication;
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
   * @param idEntity
   *          valor del campo a guardar.
   */
  public static void setIdEntity(String idEntity) {
    LoginCertServlet.idEntity = idEntity;
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
   *          valor del campo a guardar.
   */
  public static void setUrlLogin(String urlLogin) {
    LoginCertServlet.urlLogin = urlLogin;
  }

  /*
   * TEST
   */

  private static boolean isLocal(HttpServletRequest request) {
    return "localhost".equals(request.getServerName()) && 
     "/repositorio/wld-des-35/apps/sigm-reg-jee-r01a-id/".equals(springContext.getBean("PATH_REPO"));
  }

}
