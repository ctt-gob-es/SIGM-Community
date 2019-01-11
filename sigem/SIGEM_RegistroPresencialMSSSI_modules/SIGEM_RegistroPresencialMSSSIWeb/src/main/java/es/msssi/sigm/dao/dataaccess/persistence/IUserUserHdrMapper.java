package es.msssi.sigm.dao.dataaccess.persistence;

import ieci.tecdoc.sgm.backoffice.expection.DaoException;
import es.msssi.sigm.dao.dataaccess.domain.IUserUserHdr;
/**
 * mapper en resources/es/msssi/sigm/dao/dataaccess/persistence/IUserUserHdrMapper.xml 
 */ 
public interface IUserUserHdrMapper {
 
	public IUserUserHdr getByName(String name) throws DaoException;
 
}