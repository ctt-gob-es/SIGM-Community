package es.msssi.sgm.registropresencial.daos;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que contiene los métodos para desbloquear registros.
 * 
 * @author cmorenog
 * 
 */
public class UnlockRegisterDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(UnlockRegisterDAO.class.getName());

    
    /**
     * Desbloquea todos los registros.
     * 
     */
    public void unlockRegisters() {
	LOG.trace("Entrando en UnlockRegisterDAO.unlockRegisters");
	getSqlMapClientTemplate().delete(
		"unlockRegister.unlockRegisters");
    }
    

}