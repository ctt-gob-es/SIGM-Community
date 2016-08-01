package ieci.tdw.ispac.ispacpublicador.business.action.consultaTelematica.dao;

import java.util.List;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacpublicador.business.action.consultaTelematica.vo.ExpedienteVO;
import ieci.tdw.ispac.ispacpublicador.business.action.consultaTelematica.vo.InteresadoExpedienteVO;
import ieci.tdw.ispac.ispacpublicador.business.action.consultaTelematica.vo.InteresadoVO;
import ieci.tdw.ispac.ispacpublicador.business.dao.BaseDAO;

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
    	
    	//[Manu Ticket #1023] - INICIO  Históricos de expedientes, trámites, documentos e intervinientes.
	    //final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=?";
	    final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=? UNION ALL SELECT * FROM SPAC_EXPEDIENTES_H WHERE NUMEXP=?";
		//
	    return (ExpedienteVO) getVO(cnt, sql, new Object[] { numExp, numExp },
	    //return (ExpedienteVO) getVO(cnt, sql, new Object[] { numExp }, 
   		//[Manu Ticket #1023] - FIN Históricos de expedientes, trámites, documentos e intervinientes
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
	    //[Manu Ticket #1023] - INICIO  Históricos de expedientes, trámites, documentos e intervinientes.
	    //final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=?";	    
	    final String sql = "SELECT * FROM SPAC_EXPEDIENTES WHERE NUMEXP=? UNION ALL SELECT * FROM SPAC_EXPEDIENTES_H WHERE NUMEXP=?";
		
	    interesado = (InteresadoExpedienteVO) getVO(cnt, sql, 
	    		//new Object[] { numExp }, InteresadoExpedienteVO.class);
	    		new Object[] { numExp, numExp }, InteresadoExpedienteVO.class);
	    //[Manu Ticket #1023] - FIN Históricos de expedientes, trámites, documentos e intervinientes.
	    
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

    	//[Manu Ticket #1023] - INICIO  Históricos de expedientes, trámites, documentos e intervinientes.
    	//final String sql = "SELECT * FROM SPAC_DT_INTERVINIENTES WHERE NUMEXP=?";
    	final String sql = "SELECT * FROM SPAC_DT_INTERVINIENTES WHERE NUMEXP=? UNION ALL SELECT * FROM SPAC_DT_INTERVINIENTES_H WHERE NUMEXP=?";

		//return getVOs(cnt, sql, new Object[] { numExp }, InteresadoVO.class);
		return getVOs(cnt, sql, new Object[] { numExp, numExp }, InteresadoVO.class);
		//[Manu Ticket #1023] - FIN Históricos de expedientes, trámites, documentos e intervinientes.
    }
    
}
