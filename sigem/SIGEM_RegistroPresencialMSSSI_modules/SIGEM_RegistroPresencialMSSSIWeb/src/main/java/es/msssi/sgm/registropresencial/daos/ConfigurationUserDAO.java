/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.beans.Configuration;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que realiza las operaciones de configuracion de usuario a base de datos.
 * 
 * @author cmorenog
 */
public class ConfigurationUserDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(ConfigurationUserDAO.class.getName());


    /**
     * Devuelve la configuracion por el idUser.
     * 
    * @param idUser
     *            id user.
     * @return la configuracion del usuario.
     */
    public Configuration getConfigurationUser(Integer idUser) {
	LOG.trace("Entrando en ConfigurationUserDAO.getConfigurationUser()");

	Configuration result = null;

	result =
		(Configuration) getSqlMapClientTemplate().queryForObject("configurationUser.getConfigurationUser",
			idUser);

	LOG.trace("Saliendo en ConfigurationUserDAO.getConfigurationUser()");
	return result;
    }
    
    /**
     * Devuelve true si la configuracion por el idUser existe.
     * 
    * @param idUser
     *            id user.
     * @return true si la configuracion por el idUser existe.
     */
    public boolean existConfigurationUser(Integer idUser) {
	LOG.trace("Entrando en ConfigurationUserDAO.getConfigurationUser()");

	Integer count = null;
	boolean result = false;
	count =
		(Integer) getSqlMapClientTemplate().queryForObject("configurationUser.existUser",
			idUser);
	if (count != null && count > 0){
	    result = true;
	}
	
	LOG.trace("Saliendo en ConfigurationUserDAO.getConfigurationUser()");
	return result;
    }
 
    /**
     * Modifica la configuracion por el idUser existe.
     * 
    * @param config
     *            config user.
     */
    public void updateConfigurationUser(Configuration config) {
	LOG.trace("Entrando en ConfigurationUserDAO.updateConfigurationUser()");

	getSqlMapClientTemplate().update("configurationUser.updateConfigurationUser",
			config);
	LOG.trace("Saliendo en ConfigurationUserDAO.updateConfigurationUser()");
    }
    
    /**
     * Inserta la configuracion por el idUser existe.
     * 
    * @param config
     *            config user.
     */
    public void insertConfigurationUser(Configuration config) {
	LOG.trace("Entrando en ConfigurationUserDAO.insertConfigurationUser()");

	getSqlMapClientTemplate().update("configurationUser.insertConfigurationUser",
			config);
	LOG.trace("Saliendo en ConfigurationUserDAO.insertConfigurationUser()");
    }
    
}