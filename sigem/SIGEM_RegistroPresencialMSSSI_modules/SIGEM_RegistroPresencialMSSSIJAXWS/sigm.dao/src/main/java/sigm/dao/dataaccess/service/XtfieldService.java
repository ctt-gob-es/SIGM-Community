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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sigm.dao.dataaccess.domain.XtField;
import sigm.dao.dataaccess.persistence.XtfieldMapper;
import sigm.dao.exception.DaoException;
import core.config.ApplicationContextProvider;
 
@Service(XtfieldService.BEAN_NAME)
public class XtfieldService {

    /**
     * BEAN_NAME.
     */
    protected static final String BEAN_NAME =
	    "sigm.dao.dataaccess.service.XtfieldService";
 
    @Autowired
    private XtfieldMapper xtfieldMapper;

    /**
     * Carga una instancia del contexto.
     * 
     * @return Instancia del Bean.
     */
    public static XtfieldService getInstance() {
	return (XtfieldService) ApplicationContextProvider.getApplicationContext().getBean(
		BEAN_NAME);
    }

    public int insert(XtField obj) throws DaoException {
    	int result;
    	try {
    	    result = xtfieldMapper.insert(obj);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getAll", e);
    	}    	
	    return result;
    }
 
	public XtField getByName(String name) throws DaoException {
		XtField result=null;
    	try {
    	    result = xtfieldMapper.getByName(name);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getByName", e);
    	}    	
	    return result;
	}
	public XtField getByXtfield(XtField obj) throws DaoException {
		XtField result=null;
    	try {
    	    result = xtfieldMapper.getByXtfield(obj);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getByXtfield", e);
    	}    	
	    return result;
	}
    
    public XtField getById(int id) throws DaoException {
		XtField result=null;
    	try {
    	    result = xtfieldMapper.getById(id);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getById", e);
    	}    	
	    return result;
	    
//    	mapper.get
//	    Departament depart = departamentMapper.getDepartament(departamentId);
//	    List<Permission> permissions = departamentMapper.getPermissions(departamentId);
//	    depart.setPermissions(permissions);
//	    return depart;
//	}
//	catch (Throwable t) {
//	    ApplicationLogger.error(t.getMessage(), t);
//	    throw new CoreSigmAdminException(SigmAdminErrorCode.INVALID_PARAMETERS,
//		    ServiceError.GET_DEPARTAMENT_ERROR, t);
//	}
    }
 
    public List<XtField> getAll(){
    	return null;
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
    }

 
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