/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.businessobject;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import es.msssi.sgm.registropresencial.beans.Configuration;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ConfigurationUserDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;

/**
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con el cambio de configuracion.
 * 
 * @author cmorenog
 */
public class ConfigurationBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ConfigurationBo.class);
    private ConfigurationUserDAO configurationUserDao = null;
    private static ApplicationContext appContext;

    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
    }

    /**
     * Recuperar la configuracion de usuario.
     * 
     * @param idUser
     * @return
     * @throws RPGenericException
     */
    public Configuration getConfigurationUser(Integer idUser) throws RPGenericException {
	LOG.trace("Entrando en ConfigurationBo.getConfigurationUser()");
	Configuration configuration = null;
	try {
	    if (configurationUserDao == null) {
		configurationUserDao =
			(ConfigurationUserDAO) appContext.getBean("configurationUserDAO");
	    }
	    configuration = configurationUserDao.getConfigurationUser(idUser);
	    configuration.convertJSONtoArray();
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.GET_CONFIGURATION_ERROR_MESSAGE, exception);
	    throw new RPGenericException(RPGenericErrorCode.GET_CONFIGURATION_ERROR_MESSAGE,
		    ErrorConstants.GET_CONFIGURATION_ERROR_MESSAGE, exception);
	}
	return configuration;
    }

    /**
     * Guardar la configuracion de usuario.
     * 
     * @param idUser
     * @return
     * @throws RPGenericException
     */
    public void updateConfigurationUser(Configuration configuration) throws RPGenericException {
	LOG.trace("Entrando en ConfigurationBo.updateConfigurationUser()");
	try {
	    if (configurationUserDao == null) {
		configurationUserDao =
			(ConfigurationUserDAO) appContext.getBean("configurationUserDAO");
	    }
	    configuration.convertArraytoJSON();

	    if (configurationUserDao.existConfigurationUser(configuration.getIdUser())) {
		configurationUserDao.updateConfigurationUser(configuration);
	    }
	    else {
		configurationUserDao.insertConfigurationUser(configuration);
	    }
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.UPDATE_CONFIGURATION_ERROR_MESSAGE, exception);
	    throw new RPGenericException(RPGenericErrorCode.UPDATE_CONFIGURATION_ERROR_MESSAGE,
		    ErrorConstants.UPDATE_CONFIGURATION_ERROR_MESSAGE, exception);
	}
    }

}