package es.msssi.sigm.dao.dataaccess.service;

import java.util.Map;

import ieci.tecdoc.sgm.backoffice.expection.DaoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.config.ApplicationContextProvider;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdrOB;
import es.msssi.sigm.dao.dataaccess.persistence.IUserUserHdrOBMapper;
/**
 * mapper en resources/es/msssi/sigm/dao/dataaccess/persistence/IUserUserHdrOBMapper.xml 
 */
@Service(IUserUserHdrOBService.BEAN_NAME)
public class IUserUserHdrOBService {

    /**
     * BEAN_NAME.
     */
    protected static final String BEAN_NAME =
	    "es.msssi.sigm.dao.dataaccess.service.IUserUserHdrOBService";
 
    @Autowired
    private IUserUserHdrOBMapper iUserUserHdrOBMapper;

    /**
     * Carga una instancia del contexto.
     * 
     * @return Instancia del Bean.
     */
    public static IUserUserHdrOBService getInstance() {
    	return (IUserUserHdrOBService) ApplicationContextProvider.getApplicationContext().getBean(BEAN_NAME);
    }
 
	public IUserUserHdrOB getByIdApp(Map<String, Object> params) throws DaoException {
		IUserUserHdrOB result=null;
    	try {
    	    result = iUserUserHdrOBMapper.getByIdApp(params);
    	}
    	catch (Exception e) {
    	    throw new DaoException("IUserUserHdrOB.getByIdApp", e);
    	}    	
	    return result;
	}
	
	public int insert(IUserUserHdrOB object) throws DaoException {
		int result;
    	try {
    	    result = iUserUserHdrOBMapper.insert(object);
    	}
    	catch (Exception e) {
    	    throw new DaoException("IUserUserHdrOB.insert", e);
    	}    	
	    return result;
	}
}