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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sigm.dao.dataaccess.domain.IUserUserHdrWS;
import sigm.dao.dataaccess.persistence.IUserUserHdrWSMapper;
import sigm.dao.exception.DaoException;
import core.config.ApplicationContextProvider;
 
@Service(IUserUserHdrWSService.BEAN_NAME)
public class IUserUserHdrWSService {

    /**
     * BEAN_NAME.
     */
    protected static final String BEAN_NAME =
	    "sigm.dao.dataaccess.service.IUserUserHdrWSService";
 
    @Autowired
    private IUserUserHdrWSMapper iUserUserHdrWSMapper;

    /**
     * Carga una instancia del contexto.
     * 
     * @return Instancia del Bean.
     */
    public static IUserUserHdrWSService getInstance() {
	return (IUserUserHdrWSService) ApplicationContextProvider.getApplicationContext().getBean(
		BEAN_NAME);
    }

    public int insert(IUserUserHdrWS obj) throws DaoException {
    	int result;
    	try {
    	    result = iUserUserHdrWSMapper.insert(obj);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.insert", e);
    	}    	
	    return result;
    }
 
	public IUserUserHdrWS getByName(String name) throws DaoException {
		IUserUserHdrWS result=null;
    	try {
    	    result = iUserUserHdrWSMapper.getByName(name);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getByName", e);
    	}    	
	    return result;
	}
   
	
//    public List<XtField> getAll(){
//    	return null;
//	try {
//	    HashMap<String, Object> parameters = new  HashMap<String, Object>();
//	    if (departamentParentId != -1){
//		parameters.put("departamentParentId", departamentParentId);
//	    }
//	    if (departamentName != null && !"".equals(departamentName)){
//		parameters.put("departamentName", departamentName);
//	    }
//	    return departamentMapper.getTreeDepartaments(parameters);
//	}
//	catch (Throwable t) {
//	    ApplicationLogger.error(t.getMessage(), t);
//	    throw new CoreSigmAdminException(SigmAdminErrorCode.INVALID_PARAMETERS,
//		    ServiceError.GET_TREE_DEPARTAMENT_ERROR, t);
//	}
//    }

 
//    public boolean existDepartament(Departament departament) {
//	boolean result = false;
//	try {
//	    int count = departamentMapper.existDepartament(departament);
//	    if (count > 0){
//		result = true;
//	    }
//	    return result;
//	}
//	catch (Throwable t) {
//	    ApplicationLogger.error(t.getMessage(), t);
//	    throw new CoreSigmAdminException(SigmAdminErrorCode.INVALID_PARAMETERS,
//		    ServiceError.GET_DEPARTAMENT_ERROR, t);
//	}
//    }
}