package ieci.tdw.ispac.ispacpublicador.business.reader.ispac.dao;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispacpublicador.business.dao.BaseDAO;
import ieci.tdw.ispac.ispacpublicador.business.vo.MilestoneVO;

import java.util.List;

import org.apache.log4j.Logger;

public class MilestoneDAO extends BaseDAO {

	/** Logger de la clase. */
    protected static final Logger logger = 
    	Logger.getLogger(MilestoneDAO.class);

    
	/**
	 * Obtiene la lista de nuevos hitos, tomando como filtro (límite inferior) 
	 * el valor <code>'infLimitId'</code>
	 * @param cnt Conexión a la base de datos.
	 * @param infLimitId Límite inferior de identifcador de Hito.
	 * @return Lista de hitos. 
	 */
	public static List getNewMilestoneList(DbCnt cnt, int infLimitId) 
			throws ISPACException {
		
	    //[Manu Ticket #1090] - INICIO Poner en marcha la opción Consulta de Expedientes.
	    //final String sql = "SELECT HITOS.*, PROCS.ID_PCD, PROCS.NUMEXP FROM SPAC_HITOS HITOS, SPAC_PROCESOS PROCS WHERE PROCS.ID = HITOS.ID_EXP AND HITOS.ID > ? ORDER BY HITOS.ID ASC";
	    final String sql = "SELECT * FROM (SELECT HITOS.*, PROCS.ID_PCD, PROCS.NUMEXP FROM SPAC_HITOS HITOS, SPAC_PROCESOS PROCS WHERE PROCS.ID = HITOS.ID_EXP UNION ALL SELECT HITOS.*, PROCS.ID_PCD, PROCS.NUMEXP FROM SPAC_HITOS_H HITOS, SPAC_PROCESOS PROCS WHERE PROCS.ID = HITOS.ID_EXP) AS HITOS WHERE HITOS.ID > ? ORDER BY HITOS.ID ASC";
		//[Manu Ticket #1090] - FIN Poner en marcha la opción Consulta de Expedientes.


	    return getVOs(cnt, sql, new Object[] { new Integer(infLimitId) }, 
	    		MilestoneVO.class);
	}
}
