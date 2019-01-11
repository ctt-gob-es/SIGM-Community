package es.msssi.sigm.servlets.logincert.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class LoginCertBaseDao extends SqlMapClientDaoSupport {

  /**
   * Logger de la clase.
   */
  private static final Logger logger = Logger.getLogger(LoginCertBaseDao.class);
  /**
   * Mensaje de error en tareas con registros: Error iniciando transacción.
   */
  private static final String START_TRANSACTION_ERROR = "Error iniciando transacción";
  /**
   * Mensaje de error en tareas con registros: Error en el commit de la
   * transacción.
   */
  private static final String COMMIT_TRANSACTION_ERROR = "Error en el commit de la transacción";
  /**
   * Mensaje de error en tareas con registros: Error cerrando la transacción.
   */
  private static final String END_TRANSACTION_ERROR = "Error cerrando la transacción";

  /**
   * Inicio de una transacción.
   * 
   * @throws SQLException.
   */
  public void startTransaction() throws SQLException {
    try {
      getSqlMapClient().startTransaction();
    } catch (SQLException sqlException) {
      logger.error(START_TRANSACTION_ERROR, sqlException);
      throw sqlException;
    }
  }

  /**
   * Commit de una transacción.
   * 
   * @throws SQLException.
   */
  public void commitTransaction() throws SQLException {
    try {
      getSqlMapClient().commitTransaction();
    } catch (SQLException sqlException) {
      logger.error(COMMIT_TRANSACTION_ERROR, sqlException);
    }
  }

  /**
   * Fin de una transacción.
   * 
   * @throws SQLException.
   */
  public void endTransaction() {
    try {
      getSqlMapClient().endTransaction();
    } catch (SQLException sqlException) {
      logger.error(END_TRANSACTION_ERROR, sqlException);
    }
  }
  /*
   * EXTRAER A UN DAO BASE SI SE CREAN MAS DAOS Y SE HA PUESTO ESTO EN MODULO APARTE
   */
  
  
  
}
