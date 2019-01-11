package es.msssi.sgm.registropresencial.businessobject;

import java.util.List;

import org.apache.log4j.Logger;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import com.ieci.tecdoc.common.invesdoc.Iuseruserhdr;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;

/**
 * Clase q implementa IGenericBo que contiene los métodos relacionados con el usuario de registro.
 * @author eacuna
 *
 */
public class UserBo implements IGenericBo {
	
    private static final Logger LOG = Logger.getLogger(UserBo.class.getName());
	
	public boolean isHasPermissInitTram(UseCaseConf useCaseConf) {
		
		HibernateUtil hibernateUtil = new HibernateUtil();
		Transaction tran = null;
		try {
			Session session = hibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();

			// Recuperamos el usuario
			List list = ISicresQueries.getUserUserHdrByName(session, useCaseConf.getUserName());

			if (list.size() == 0) {
				LOG.error("El usuario " + useCaseConf.getUserName() + " no existe");
				return false;
			}

			Iuseruserhdr userHdr = (Iuseruserhdr) session.load(Iuseruserhdr.class,
					((Iuseruserhdr) list.get(0)).getId());

			hibernateUtil.commitTransaction(tran);
			
			if(userHdr.getFlags() == 1) return true;
			else return false;
			
		} catch (HibernateException hE) {
			try {
				hibernateUtil.commitTransaction(tran);
			} catch (HibernateException cE) {
				LOG.warn("El usuario [" + useCaseConf.getUserName() + "] no tiene permisos de tramitación");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Error iniesperado: " + e.getMessage());
			return false;
		} finally {
			hibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
		return false;
	}
}
