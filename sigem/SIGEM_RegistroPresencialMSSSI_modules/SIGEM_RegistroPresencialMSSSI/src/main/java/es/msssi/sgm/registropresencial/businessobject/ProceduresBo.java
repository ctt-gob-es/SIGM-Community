package es.msssi.sgm.registropresencial.businessobject;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.invesdoc.Iusergroupuser;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.beans.ItemBean;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPProcedureException;

/**
 * 
 * Clase que implementa la interfaz IGenericBo que contiene los métodos
 * relacionados con los procedimientos administrativos.
 *
 * @author eacuna
 *
 */
public class ProceduresBo implements IGenericBo, Serializable, HibernateKeys {

	/**
	 * 
	 */
	private static final long serialVersionUID = -118316980425987390L;
	private static final Logger LOG = Logger.getLogger(ProceduresBo.class);

	/**
	 * Devuelve un bean con la lista de procedimientos disponibles.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * 
	 * @return List<ItemBean> Lista de procedimientos.
	 * 
	 * @throws RPBookException
	 *             si se ha producido algún error al tratar la información del procedimiento.
	 * 
	 * @throws RPGenericException
	 *             si se ha producido un error genérico en el proceso.
	 */
	@SuppressWarnings("unchecked")
	public List<ItemBean> getProcedures(UseCaseConf useCaseConf) throws RPGenericException,
			RPProcedureException {
		LOG.trace("Entrando en ProceduresBo.getProcedures()");
		List<ItemBean> listProcedures = new ArrayList<ItemBean>();
		HibernateUtil hibernateUtil = new HibernateUtil();
		Transaction tran = null;
		ItemBean itemBean = null;
		String squeme = "";//"SIGM_TRAMITADOR.";
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
	        	    Session session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
	        	    tran = session.beginTransaction();
	        	    // Recuperamos la sesión
	        	    CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	        	    
	        	    // Recuperamos la informacion y perfil del usuario
	    			AuthenticationUser user = (AuthenticationUser) cacheBag
	    					.get(HIBERNATE_Iuseruserhdr);
	    			
	    			List<Iusergroupuser> groups = ISicresQueries.getIUserGroupUser(session, user.getId());
	        
	    			StringBuilder permisos = null;
	    			// COmprobamos si es SUPERUSUARIO
	        	    if(isUserSupervisor(session.connection(), user.getId())) 
	        	    {
	        	    	permisos = new StringBuilder(" ");
	        	    }
	        	    else {
	        	    	permisos = new StringBuilder("AND UID_USR IN (");
		        	    permisos.append("'1-"+user.getId()+"',"); // Filtramos por usuario
		        	    permisos.append("'2-"+user.getDeptid()+"',"); // Filtramos por el departamento
		        	    if(groups != null && !groups.isEmpty()) {
			        	    for(Iusergroupuser idGroup: groups) {
			        	    	permisos.append("'3-"+idGroup.getGroupid()+"',"); // Filtramos por los grupos a los que pertenece el usuario
			        	    }
		        	    }
		        	    permisos.deleteCharAt(permisos.length() - 1);
		        	    permisos.append(")");
	        	    }
	        	    
	        	    // Recuperamos la lista de procedimientos a los que el usuario tiene permisos de poder Iniciar expediente
	        	    // Se comprueba tb que el procedimiento debe estar vigente
	        	    
	        	    String sql = "SELECT SPAC_CT_PROCEDIMIENTOS.COD_PCD, SPAC_CT_PROCEDIMIENTOS.NOMBRE FROM " + squeme + "SPAC_CT_PROCEDIMIENTOS, "+ squeme + "SPAC_P_PROCEDIMIENTOS"
	        	    		+ " WHERE SPAC_CT_PROCEDIMIENTOS.ID = SPAC_P_PROCEDIMIENTOS.ID_PCD_BPM"
	        	    		+ " AND SPAC_CT_PROCEDIMIENTOS.ID in ( SELECT ID_PCD from " + squeme + "SPAC_SS_PERMISOS"
	        	    		+ " WHERE PERMISO = 1 " + permisos.toString() + ")"
       						+ " AND (SPAC_P_PROCEDIMIENTOS.ESTADO = 2 AND SPAC_P_PROCEDIMIENTOS.TIPO = 1)"
       						+ " AND 1 = (SELECT FLAGS FROM IUSERUSERHDR WHERE NAME = ?) ORDER BY SPAC_CT_PROCEDIMIENTOS.NOMBRE DESC";
	        	    
	        	    ptmt = session.connection().prepareStatement(sql);
	        	    ptmt.setString(1, useCaseConf.getUserName());
	        	    
	        	    rs = ptmt.executeQuery();
	        	    while(rs.next()) {
	        	    	itemBean = new ItemBean(rs.getString(1), rs.getString(2));
	        	    	listProcedures.add(itemBean);
	        	    }
	        	    hibernateUtil.commitTransaction(tran);
		}
		catch (Exception exception) {
		    LOG.error(
			ErrorConstants.GET_PROCEDURES_ERROR_MESSAGE +
			    " [" + useCaseConf.getSessionID() + "]", exception);
		    hibernateUtil.rollbackTransaction(tran);
		}
		finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ptmt != null) {
				try {
					ptmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		    hibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
		return listProcedures;
	}
	
	/**
	 * Método que comprueba si el usuario es Supervisor
	 * @param con - Conexión hibernate
	 * @param iduser - Id del usuario
	 * @return boolean - (true - es superusuario / false - No es superusuario)
	 */
	protected boolean isUserSupervisor(Connection con, Integer iduser) {
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
	    	    String sql = "SELECT COUNT(*) FROM SPAC_SS_FUNCIONES WHERE UID_USR = '1-" + iduser + "' AND FUNCION = 5";
	    	    
	    	    ptmt = con.prepareStatement(sql);
	    	    rs = ptmt.executeQuery();
	    	    while(rs.next()) {
	    	    	if(rs.getInt(1) > 0) return true;
	    	    	else return false;
	    	    }
		}
		catch (Exception exception) {
		    LOG.error(
			ErrorConstants.GET_PROCEDURES_ERROR_MESSAGE +
			    " [" + "1-" + iduser + "]", exception);
		}
		finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(ptmt != null) {
				try {
					ptmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
}
