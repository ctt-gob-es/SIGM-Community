package es.msssi.sigm.dao.dataaccess.service;

import ieci.tecdoc.sgm.backoffice.expection.DaoException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.config.ApplicationContextProvider;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
import es.msssi.sigm.dao.dataaccess.persistence.IUserUserHdrMapper;
 /**
  * mapper en resources/es/msssi/sigm/dao/dataaccess/persistence/IUserUserHdrMapper.xml 
  */
@Service(IUserUserHdrService.BEAN_NAME)
public class IUserUserHdrService {

    /**
     * BEAN_NAME.
     */
    protected static final String BEAN_NAME =
	    "es.msssi.sigm.dao.dataaccess.service.IUserUserHdrService";
 
    @Autowired
    private IUserUserHdrMapper iUserUserHdrMapper;

    /**
     * Carga una instancia del contexto.
     * 
     * @return Instancia del Bean.
     */
    public static IUserUserHdrService getInstance() {
    	return (IUserUserHdrService) ApplicationContextProvider.getApplicationContext().getBean(BEAN_NAME);
    }
 
	public IUserUserHdr getByName(String name) throws DaoException {
		IUserUserHdr result=null;
    	try {
    	    result = iUserUserHdrMapper.getByName(name);
    	}
    	catch (Exception e) {
    	    throw new DaoException("dao.getByName", e);
    	}    	
	    return result;
	}
}