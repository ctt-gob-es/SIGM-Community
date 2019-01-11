package es.msssi.sigm.servlets.logincert.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdrOB;

public class LoginCertUserDao extends LoginCertBaseDao {

  private static final Logger LOG = Logger.getLogger(LoginCertUserDao.class.getName());

  /**
   * Mensaje de error al consultar al existencia de usuario.
   */
  public static final String QUERY_EXIST_USER = "Error al consultar la existencia de usuario en bbbdd";
  
  /**
   * Obtiene todos los usuarios estructura asociados al dni
   * @param dni
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IUserUserHdr> getUsersByDni(String dni) {
    List<IUserUserHdr> result = null;
    try {
      result = (List<IUserUserHdr>) getSqlMapClientTemplate().queryForList(
          "LoginCertUser.getUsersByDni", dni);
    } catch (Exception exception) {
      LOG.error(QUERY_EXIST_USER, exception);
    }
    return result;
  }

  public IUserUserHdrOB getUserObligation(long id, String app) {
    IUserUserHdrOB result = null;
    HashMap<String, Object> params = new HashMap<String, Object>();

    params.put("id", id + "");
    params.put("app", app);

    try {
      result = (IUserUserHdrOB) getSqlMapClientTemplate().queryForObject(
          "LoginCertUser.getOBByIdApp", params);
    } catch (Exception exception) {
      LOG.error(QUERY_EXIST_USER, exception);
    }
    return result;
  }
  
}
