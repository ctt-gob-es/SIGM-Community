/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrTramunit;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.msssi.sgm.registropresencial.beans.QueryCompactSearchOrg;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;

/**
 * Clase que contiene los métodos para recuperar las unidades tramitadoras.
 * 
 * @author cmorenog
 * 
 */
public class UnidadTramitadoraDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(UnidadTramitadoraDAO.class.getName());



    /**
     * Devuelve una lista de unidades tramitadoras.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param query
     *            criterio de búsqueda.
     * @param queryCompactSearchUnitTram
     *            nivel de búsqueda y ordenación sobre el que se realiza la
     *            búsqueda rápida.
     * @return result Listado de organismos disponibles.
     * 
     * @throws exception
     *             si ha habido algún problema.
     */
    public List<UnidadTramitacionIntercambioRegistralSIRVO> buscarUnidadesTramitacion( String query, QueryCompactSearchOrg queryCompactSearchUnitTram) {
    	LOG.trace("Entrando en UnidadTramitadoraDAO.buscarUnidadesTramitacion");
    	Map<String, Object> map = new HashMap<String, Object>();
    	
    	if (query != null){
    		map.put("text", query);
    	}

    	if (queryCompactSearchUnitTram != null){
    		map.put("where", queryCompactSearchUnitTram.getWhere());
    		
    		if (queryCompactSearchUnitTram.getOrderBy() != null){
    			map.put("orderby", queryCompactSearchUnitTram.getOrderBy());
    		}
    	}
    	
    	@SuppressWarnings("unchecked")
    	List<UnidadTramitacionIntercambioRegistralSIRVO> result =(List<UnidadTramitacionIntercambioRegistralSIRVO>) getSqlMapClientTemplate().queryForList( "unidadTramitadora.listUnidadTramitadoraQuery",map);
    	
    	return result;
    }

    /**
     * Devuelve una unidad tramitadora.
     * 
     * @param id
     *            id de la unidad a recuperar.
     * @return result la unidad tramitadora recuperada.
     * 
     */
    public UnidadTramitacionIntercambioRegistralSIRVO getUnidadTramitadora( Integer id) {
    	LOG.trace("Entrando en UnidadTramitadoraDAO.getUnidadTramitadora");
    	
    	UnidadTramitacionIntercambioRegistralSIRVO result = (UnidadTramitacionIntercambioRegistralSIRVO) getSqlMapClientTemplate().queryForObject( "unidadTramitadora.getUnidadTramitadora",id);
    	
    	return result;
    }

    public List<UnidadTramitacionIntercambioRegistralSIRVO> buscarUnidadesTramitacionPag( SearchUnitsBean searchUnitsBean, int firstRow, int pageSize) {
    	LOG.trace("Entrando en UnidadTramitadoraDAO.countUnidadesTramitacion");
    	
    	@SuppressWarnings("unchecked")
    	List<UnidadTramitacionIntercambioRegistralSIRVO> result = (List<UnidadTramitacionIntercambioRegistralSIRVO>) getSqlMapClientTemplate().queryForList( "unidadTramitadora.buscarUnidadesTramitacionPag",searchUnitsBean, firstRow, pageSize);
    	
    	return result;
    }
    
    /**
     * Devuelve el número de unidades tramitadoras encontradas.
     * 
     * @param searchUnitsBean
     *            parametros de busqueda de las unidades a recuperar.
     * @return result el numero de unidades encontradas.
     * 
     */
    public int countUnidadesTramitacion( SearchUnitsBean searchUnitsBean) {
    	
    	LOG.trace("Entrando en UnidadTramitadoraDAO.countUnidadesTramitacion");
    	
    	int result = (Integer) getSqlMapClientTemplate().queryForObject( "unidadTramitadora.countUnidadesTramitacion",searchUnitsBean);
    	
    	return result;
    }
    
    
    /**
     * Devuelve true si es una unidad tramitadora.
     * 
     * @param idOrg
     *            id de la unidad a recuperar.
     * @return result true si el organismo es una unidad tramitadora.
     * 
     */
    @SuppressWarnings("unchecked")
	public boolean esUnidadTramitacion(UseCaseConf useCaseConf, Integer idOrg) {
    	boolean result = false;
    	Transaction tran = null;
    	
    	try{
    		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
    		
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();
			
			Criteria criteriaResults = session.createCriteria(ScrTramunit.class);
			criteriaResults.add(Expression.eq("id_orgs", idOrg));			

			List<ScrTramunit> unidadesTramitacion = (List<ScrTramunit>) criteriaResults.list();
			if(null != unidadesTramitacion && unidadesTramitacion.size() > 0){
				result = true;
			}
			if (session.isOpen()) {
				HibernateUtil.commitTransaction(tran);
			}
		}
    	catch (Exception e) {
    		LOG.error(ErrorConstants.GET_UNID_REGISTER_ERROR_MESSAGE, e);
    		HibernateUtil.rollbackTransaction(tran);    		
    	}
    	finally{
    		HibernateUtil.closeSession(useCaseConf.getEntidadId());
    	}
    	
    	return result;
    }
}