/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.ibatis.User;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.UtilsPassword;

/**
 * Servicios disponibles para la tabla usuarios.
 * 
 * @author cmorenog
 * 
 */
public class UserDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(UserDAO.class.getName());
 
  
    /**
     * Inserta un nuevo user.
     * 
     * @param user
     *            Valor de descripcion de user.
     * 
     * @return userId.
     */
    public int insertUser(
	User user) {

    	User usuario = new User();
		try {
			usuario = user;

			getSqlMapClientTemplate().insert("User.insertUser", usuario);
			getSqlMapClientTemplate().update("User.updateUserNextId", usuario.getUserId()+1);
			
			usuario.setUserPassword(UtilsPassword.encryptPassword(user.getUserPassword(), usuario.getUserId()));

			getSqlMapClientTemplate().update("User.updatePassword", usuario);

			getSqlMapClientTemplate().insert("User.insertUserData", usuario);

			getSqlMapClientTemplate().insert("User.insertUserConf", usuario);

			getSqlMapClientTemplate().insert("User.insertPermissions", usuario);

			getSqlMapClientTemplate().insert("User.insertProfiles", usuario);

			getSqlMapClientTemplate().insert("User.insertUserPerm", usuario);

		} catch (Exception exception) {
			LOG.error(ErrorConstants.INSERT_USER_ERROR, exception);
			Utils.redirectToErrorPage(null, null, exception);
		}
		return usuario.getUserId();
    }

    
    /**
     * Saber si existe un user con el mismo nombre.
     * 
     * @param user
     *            user a buscar.
     * @return boolean si esiste el user.
     */
    public boolean existUser(
	User user) {
	boolean result = false;
	try {
	    int count = (Integer) getSqlMapClientTemplate().queryForObject("User.existUser",  user);
	    if (count > 0) {
		result = true;
	    }
	   
	} catch (Exception exception) {
	    LOG.error(ErrorConstants.QUERY_EXIST_USER, exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
	 return result;
    }
    
    
}