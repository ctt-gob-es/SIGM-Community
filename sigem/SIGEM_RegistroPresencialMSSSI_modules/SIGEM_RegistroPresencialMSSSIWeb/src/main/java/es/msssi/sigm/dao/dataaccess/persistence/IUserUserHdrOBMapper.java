package es.msssi.sigm.dao.dataaccess.persistence;

import java.util.Map;

import ieci.tecdoc.sgm.backoffice.expection.DaoException;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdrOB;
/**
 * mapper en resources/es/msssi/sigm/dao/dataaccess/persistence/IUserUserHdrOBMapper.xml 
 */ 
public interface IUserUserHdrOBMapper {
 
	public IUserUserHdrOB getByIdApp(Map<String, Object> params) throws DaoException;
	
	public int insert(IUserUserHdrOB obj) throws DaoException;
 
}