/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.dataaccess.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sigm.dao.dataaccess.domain.InputRegisterReportsCert;
import sigm.dao.dataaccess.persistence.InputRegisterReportsCertMapper;
import sigm.dao.exception.DaoException;
import core.config.ApplicationContextProvider;
 
@Service(InputRegisterReportsCertService.BEAN_NAME)
public class InputRegisterReportsCertService {

    /**
     * BEAN_NAME.
     */
    protected static final String BEAN_NAME = 
	    "sigm.dao.dataaccess.service.InputRegisterReportsCertService";
 
    @Autowired
    private InputRegisterReportsCertMapper inputRegisterReportsCertMapper;

    /**
     * Carga una instancia del contexto.
     * 
     * @return Instancia del Bean.
     */
    public static InputRegisterReportsCertService getInstance() {
	return (InputRegisterReportsCertService) ApplicationContextProvider.getApplicationContext().getBean(
		BEAN_NAME);
    }
 
	public InputRegisterReportsCert getByParams(Map<String, Object> params) throws DaoException {
		InputRegisterReportsCert result=null;
    	try {
    	    result = inputRegisterReportsCertMapper.getByParams(params);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getByParams", e);
    	}    	
	    return result;
	}
}