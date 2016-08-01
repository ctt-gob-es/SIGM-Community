package es.dipucr.sigem.api.rule.publicador.dao;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacpublicador.business.dao.BaseDAO;

import java.util.List;

import es.dipucr.sigem.api.rule.publicador.vo.ExpedienteVO;
import es.dipucr.sigem.api.rule.publicador.vo.InteresadoExpedienteVO;
import es.dipucr.sigem.api.rule.publicador.vo.InteresadoVO;

/**
 * DAO de acceso a los expedientes
 *
 */
public class ExpedienteDAO extends BaseDAO {

	/**
	 * Obtiene la información del expediente.
	 * @param cnt Conexión a la base de datos.
	 * @param numExp Número del expediente.
	 * @return Información del expediente.
	 * @throws ISPACException si ocurre algún error.
	 */
    public static ExpedienteVO getExpediente(DbCnt cnt, String numExp) 
    		throws ISPACException {
		//[Manu Ticket #1090] - INICIO  Poner en marcha la opción Consulta de Expedientes.
	    //final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=?";
	    final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=? UNION ALL SELECT * FROM SPAC_EXPEDIENTES_H WHERE NUMEXP=?";
		//[Manu Ticket #1090] - FIN Poner en marcha la opción Consulta de Expedientes.

	    return (ExpedienteVO) getVO(cnt, sql, new Object[] { numExp, numExp }, 
	    		ExpedienteVO.class);
    }
    
	/**
	 * Obtiene el interesado principal del expediente.
	 * @param cnt Conexión a la base de datos.
	 * @param numExp Número del expediente.
	 * @return Información del expediente.
	 * @throws ISPACException si ocurre algún error.
	 */
    public static InteresadoExpedienteVO getInteresadoExpediente(DbCnt cnt, 
    		String numExp) throws ISPACException {

    	InteresadoExpedienteVO interesado = null;
		//[Manu Ticket #1090] - INICIO  Poner en marcha la opción Consulta de Expedientes.
	    //final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=?";	    
	    final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=? UNION ALL SELECT * FROM SPAC_EXPEDIENTES_H WHERE NUMEXP=?";
		//[Manu Ticket #1090] - FIN Poner en marcha la opción Consulta de Expedientes.

	    interesado = (InteresadoExpedienteVO) getVO(cnt, sql, 
	    		new Object[] { numExp, numExp }, InteresadoExpedienteVO.class);
	    
	    if ((interesado != null) && StringUtils.isBlank(interesado.getCnif())) {
	    	interesado = null;
	    }
	    
	    return interesado;
	}

	/**
	 * Obtiene la lista de interesados del expediente.
	 * @param cnt Conexión a la base de datos.
	 * @param numExp Número del expediente.
	 * @return Lista de interesados del expediente.
	 * @throws ISPACException si ocurre algún error.
	 */
    public static List getInteresados(DbCnt cnt, String numExp)
			throws ISPACException {
		//[Manu Ticket #1090] - INICIO  Poner en marcha la opción Consulta de Expedientes.
		//final String sql = "SELECT * FROM SPAC_DT_INTERVINIENTES WHERE NUMEXP=?";
		final String sql = "SELECT * FROM SPAC_DT_INTERVINIENTES WHERE NUMEXP=? UNION ALL SELECT * FROM SPAC_DT_INTERVINIENTES_H WHERE NUMEXP=?";
		//[Manu Ticket #1090] - FIN Poner en marcha la opción Consulta de Expedientes.

		return getVOs(cnt, sql, new Object[] { numExp, numExp }, InteresadoVO.class);
	}
    
}
