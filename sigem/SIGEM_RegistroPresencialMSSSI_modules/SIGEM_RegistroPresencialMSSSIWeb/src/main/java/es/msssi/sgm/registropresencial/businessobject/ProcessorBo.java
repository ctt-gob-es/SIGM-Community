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
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.TramDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPGenericErrorCode;
import es.msssi.sgm.registropresencial.errors.RPGenericException;

/**
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con el tramitador.
 * 
 * @author cmorenog
 */
public class ProcessorBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ProcessorBo.class);
    private TramDAO tramDAO = null;
    private static ApplicationContext appContext;

    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
    }

    /**
     * Recuperar la configuracion de usuario.
     * 
     * @param nregister
     * @return
     * @throws RPGenericException
     */
    public boolean existExpedient(String nregister) throws RPGenericException {
	LOG.trace("Entrando en ProcessorBo.nregister()");
	boolean result = false;
	try {
	    if (tramDAO == null) {
		tramDAO =
			(TramDAO) appContext.getBean("tramDAO");
	    }
	    result = tramDAO.existExpedient(nregister);
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
	    throw new RPGenericException(RPGenericErrorCode.GET_CONFIGURATION_ERROR_MESSAGE,
		    ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
	}
	return result;
    }
    
    
    /**
     * Comprobar si existe el expediente/procedimiento creado a partir de un numero de registro.
     * 
     * @param nregister
     * @return
     * @throws RPGenericException
     */
    public boolean existExpedientProcedure(String nregister, String cod_pcd) throws RPGenericException {
	LOG.trace("Entrando en ProcessorBo.existExpedientProcedure()");
	boolean result = false;
	try {
	    if (tramDAO == null) {
		tramDAO =
			(TramDAO) appContext.getBean("tramDAO");
	    }
	    result = tramDAO.existExpedientProcedure(nregister, cod_pcd);
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
	    throw new RPGenericException(RPGenericErrorCode.GET_CONFIGURATION_ERROR_MESSAGE,
		    ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
	}
	return result;
    }

    /**
     * Guardar el expedient.
     * 
     * @param nregister
     * @return
     * @throws RPGenericException
     */
    public void insertExpedient(String nregister) throws RPGenericException {
	LOG.trace("Entrando en ProcessorBo.insertExpedient()");
	try {
	    if (tramDAO == null) {
		tramDAO =
			(TramDAO) appContext.getBean("tramDAO");
	    }
	    tramDAO.insertExpedient(nregister);
	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
	    throw new RPGenericException(RPGenericErrorCode.UPDATE_CONFIGURATION_ERROR_MESSAGE,
		    ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
	}
    }
    
    /**
     * Guardar el expedient y el código de procedimiento.
     * 
     * @param nreg
     * @param cod_pcd
     * @return
     * @throws RPGenericException
     */
    public void insertExpedientProcedure(String nreg, String cod_pcd) throws RPGenericException {
		LOG.trace("Entrando en ProcessorBo.insertExpedientProcedure()");
		try {
		    if (tramDAO == null) {
			tramDAO =
				(TramDAO) appContext.getBean("tramDAO");
		    }
		    tramDAO.insertExpedientProcedure(nreg, cod_pcd);
		}
		catch (Exception exception) {
		    LOG.error(ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
		    throw new RPGenericException(RPGenericErrorCode.UPDATE_CONFIGURATION_ERROR_MESSAGE,
			    ErrorConstants.GET_EXPEDIENT_ERROR_MESSAGE, exception);
		}
    }

}