/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.io.Serializable;
import java.util.List;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrTt;
import com.ieci.tecdoc.common.utils.EntityByLanguage;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con los
 * transportes.
 * 
 * @author cmorenog
 */
public class TransportBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TransportBo.class.getName());

    /**
     * Devuelve una lista de tipos de transporte.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * 
     * @return result Listado de tipos de transporte disponibles.
     * 
     * @throws AttributesException
     *             si ha habido algún problema con los atributos.
     * @throws SessionException
     *             si ha habido algún problema con la sesión.
     * @throws ValidationException
     *             si ha habido algún problema con en la validación.
     */
    @SuppressWarnings("unchecked")
    public String getScrttNameById(
	UseCaseConf useCaseConf, String id) {
	LOG.trace("Entrando en ValidationListBo.getScrttNameById()");

	
	Transaction tran = null;
	String result = null;
	try {
	    if (id != null && !"".equals(id)){
        	    Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
        	    tran = session.beginTransaction();
        	    // Recuperamos la sesión
        	    CacheFactory.getCacheInterface().getCacheEntry(
        		useCaseConf.getSessionID());
        
        	    Criteria criteriaResults =
        		session.createCriteria(EntityByLanguage.getScrTtLanguage(useCaseConf.getLocale()
        		    .getLanguage()));
        	    if (id.startsWith("0")){
        		id = id.substring(1);
        	    }
        	    criteriaResults.add(Expression.eq(
    			"id", Integer.parseInt(id)));
        	    List<ScrTt> list = (List<ScrTt>) criteriaResults.list();
        	    if (list != null && list.size() ==1){
        		result = list.get(0).getTransport();
        	    }
        	    HibernateUtil.commitTransaction(tran);
	    }
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_TRANSPORT_TYPES_LIST_ERROR_MESSAGE +
		    " [" + useCaseConf.getSessionID() + "]", sessionException);
	    result = null;
	}
	catch (Exception exception) {
	    LOG.error(
		ErrorConstants.GET_TRANSPORT_TYPES_LIST_ERROR_MESSAGE +
		    " [" + useCaseConf.getSessionID() + "]", exception);
	    result = null;
	    HibernateUtil.rollbackTransaction(tran);
	}
	finally {
	    HibernateUtil.closeSession(useCaseConf.getEntidadId());
	}
	return result;
    }
}