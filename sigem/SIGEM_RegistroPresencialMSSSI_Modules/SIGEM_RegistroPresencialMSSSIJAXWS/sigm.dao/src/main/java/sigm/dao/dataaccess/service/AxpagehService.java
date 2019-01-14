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

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sigm.dao.dataaccess.persistence.AxpagehMapper;
import sigm.dao.exception.DaoException;
import core.config.ApplicationContextProvider;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
 
@Service(AxpagehService.BEAN_NAME)
public class AxpagehService {

    /**
     * BEAN_NAME.
     */
    protected static final String BEAN_NAME = 
	    "sigm.dao.dataaccess.service.AxpagehService";
 
    @Autowired
    private AxpagehMapper axpagehMapper;

    /**
     * Carga una instancia del contexto.
     * 
     * @return Instancia del Bean.
     */
    public static AxpagehService getInstance() {
	return (AxpagehService) ApplicationContextProvider.getApplicationContext().getBean(
		BEAN_NAME);
    }
     
	public List<Axpageh> getByFdrid(Map<String, Object> params) throws DaoException {
		List<Axpageh> result=null;
    	try {
    	    result = axpagehMapper.getByFdrid(params);
    	}
    	catch (Exception e) {
    		//TODO: Cambiar mensaje de error
    	    throw new DaoException("dao.getByParams", e);
    	}    	
	    return result;
	}
}