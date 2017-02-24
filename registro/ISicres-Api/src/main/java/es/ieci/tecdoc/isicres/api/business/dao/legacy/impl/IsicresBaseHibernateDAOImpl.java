package es.ieci.tecdoc.isicres.api.business.dao.legacy.impl;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.utils.HibernateUtil;

import es.ieci.tecdoc.isicres.api.business.manager.ContextoAplicacionManager;
import es.ieci.tecdoc.isicres.api.business.manager.ContextoAplicacionManagerFactory;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;



public abstract class IsicresBaseHibernateDAOImpl {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(IsicresBaseHibernateDAOImpl.class);
	
	
	protected ContextoAplicacionManager getContextoAplicacionManager() {
		return  ContextoAplicacionManagerFactory.getInstance();
	}

	public  Session getSession() throws HibernateException{
		Session result=null;
		HibernateUtil hibernateUtil = new HibernateUtil();
	    String entity=MultiEntityContextHolder.getEntity();
		result=hibernateUtil.currentSession(entity);
		return result;
	  }
	
	 public  void closeSession(Session session)
	    {
	        if(session != null)
	        {
	            logger.debug("Closing Hibernate Session");
	            HibernateUtil hibernateUtil = new HibernateUtil();
	            try
	            {
	                session.close();
	                String entity=MultiEntityContextHolder.getEntity();
	                hibernateUtil.closeSession(entity);
	            }
	            catch(HibernateException ex)
	            {
	                logger.debug("Could not close Hibernate Session", ex);
	            }
	            catch(Throwable ex)
	            {
	                logger.debug("Unexpected exception on closing Hibernate Session", ex);
	            }
	        }
	    }
	

}
