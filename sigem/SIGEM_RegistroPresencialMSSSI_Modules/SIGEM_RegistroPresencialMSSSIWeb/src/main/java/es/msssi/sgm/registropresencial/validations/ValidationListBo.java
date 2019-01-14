/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o en cuanto sean aprobadas por laComisión Europea versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.validations;

import java.io.Serializable;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.hibernate.Criteria;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.Order;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.exception.AttributesException;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrCCAA;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrProv;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.invesicres.ScrTt;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.utils.EntityByLanguage;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.common.utils.Repository;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.msssi.sgm.registropresencial.businessobject.IGenericBo;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.IuserdepthdrDAO;
import es.msssi.sgm.registropresencial.daos.ScrCCAADAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.KeysRP;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con las
 * validaciones en lista de los formularios.
 * 
 * @author cmorenog
 */
public class ValidationListBo implements IGenericBo, HibernateKeys, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(ValidationListBo.class);
	
	private static ApplicationContext appContext;
	static {
		appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
	}

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
	public List<ScrTt> getScrtt(UseCaseConf useCaseConf) throws AttributesException, SessionException, ValidationException {
		
		LOG.trace("Entrando en ValidationListBo.getScrtt()");
		
		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

		Transaction tran = null;
		
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();
			// Recuperamos la sesión
			CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());

			Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrTtLanguage(useCaseConf.getLocale().getLanguage()));

			// Recuperamos los resultados
			criteriaResults.addOrder(Order.asc("transport"));
			List<ScrTt> result = (List<ScrTt>) criteriaResults.list();
			HibernateUtil.commitTransaction(tran);
			
			return result;
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_TRANSPORT_TYPES_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
			
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_TRANSPORT_TYPES_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException(AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
			
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
	}

	/**
	 * Devuelve una lista de oficinas.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * 
	 * @return result Listado de oficinas disponibles.
	 * 
	 * @throws AttributesException
	 *             si ha habido algún problema con los atributos.
	 * @throws SessionException
	 *             si ha habido algún problema con la sesión.
	 * @throws ValidationException
	 *             si ha habido algún problema con en la validación.
	 */
	@SuppressWarnings("unchecked")
	public List<ScrOfic> getScrOfic(UseCaseConf useCaseConf) throws AttributesException, SessionException, ValidationException {
		
		LOG.trace("Entrando en ValidationListBo.getScrOfic()");
		
		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

		Transaction tran = null;
		
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();
			// Recuperamos la sesión
			CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());

			Criteria criteriaResults = session.createCriteria(EntityByLanguage.getScrOficLanguage(useCaseConf.getLocale().getLanguage()));

			// Recuperamos los resultados
			criteriaResults.addOrder(Order.asc("code"));
			criteriaResults.add(Expression.isNull("disableDate"));
			List<ScrOfic> result = (List<ScrOfic>) criteriaResults.list();
			HibernateUtil.commitTransaction(tran);
			
			return result;
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_OFFICES_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
			
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_OFFICES_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException(AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
			
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
	}

	/**
	 * Devuelve una lista de asuntos por oficina.
	 * 
	 * @param useCaseConf
	 *            Usuario que realiza la petición.
	 * 
	 * @return result Listado de asuntos por oficina disponibles.
	 * 
	 * @throws BookException
	 *             si ha habido algún problema con el libro de registro.
	 * @throws AttributesException
	 *             si ha habido algún problema con los atributos.
	 * @throws SessionException
	 *             si ha habido algún problema con la sesión.
	 * @throws ValidationException
	 *             si ha habido algún problema con en la validación.
	 */
	public List<ScrCa> getScrCa(UseCaseConf useCaseConf) throws BookException, AttributesException, SessionException, ValidationException {
		
		LOG.trace("Entrando en ValidationListBo.getScrCa()");
		
		Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

		Map<String, Object> maps = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		ScrRegstate book = null;
		
		if (maps.get(KeysRP.J_BOOK) != null) {
			book = (ScrRegstate) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		}
		
		List<ScrCa> result = new ArrayList<ScrCa>();
		Transaction tran = null;

		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();

			// Recuperamos la sesión
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());

			// Es necesario tener el libro abierto para consultar su contenido.
			/*
			 * if (book != null && book.getId() != null) { if
			 * (!cacheBag.containsKey(book.getId())) { throw new BookException(
			 * BookException.ERROR_BOOK_NOT_OPEN); } }
			 */
			ScrOfic scrOfic = (ScrOfic) cacheBag.get(HIBERNATE_ScrOfic);

			// Obtenemos el listado de tipos de asunto
			Criteria criteria = null;
			criteria = session.createCriteria(EntityByLanguage.getScrCaLanguage(useCaseConf.getLocale().getLanguage()));

			criteria.addOrder(Order.asc("code"));

			if (book != null && book.getIdocarchhdr().getId() != null) {
				if (Repository.getInstance(useCaseConf.getEntidadId()).isInBook(book.getIdocarchhdr().getId()).booleanValue()) {
					criteria.add(Expression.eq("forEreg", Integer.valueOf(1)));
				} else {
					criteria.add(Expression.eq("forSreg", Integer.valueOf(1)));
				}
			}
			
			/*
			 * else { criteria.add(Expression.eq( "forEreg",
			 * Integer.valueOf(1))); criteria.add(Expression.eq( "forEreg",
			 * Integer.valueOf(1))); }
			 */
			criteria.add(Expression.eq("enabled", Integer.valueOf(1)));

			@SuppressWarnings("unchecked")
			List<ScrCa> scrCaList = criteria.list();

			// Obtenemos el listado de los tipos de asuntos filtrados por
			// oficina
			ScrCa scrCa = null;
			
			for (Iterator<ScrCa> it = scrCaList.iterator(); it.hasNext();) {
				scrCa = new ScrCa();
				// Copiamos las propiedades que son igual del objeto
				// iterado a un objeto tipo scrCa
				BeanUtils.copyProperties(it.next(), scrCa);

				if (scrCa.getAllOfics().equals(Integer.valueOf(1))) {
					result.add(scrCa);
					
				} else if (scrCa.getAllOfics().equals(Integer.valueOf(0))) {
					StringBuffer subquery = new StringBuffer();

					if (scrOfic != null) {
						subquery.append(EntityByLanguage.getScrCaOfic(HIBERNATE_ScrCaofic, scrOfic.getId(), scrCa.getId()));
						
						if (((Integer) session.iterate(subquery.toString()).next()).intValue() > 0) {
							result.add(scrCa);
						}
					}
				}
			}
			HibernateUtil.commitTransaction(tran);
			
			return result;
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.GET_OFFICE_ASUNTS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", sessionException);
			HibernateUtil.rollbackTransaction(tran);
			throw sessionException;
			
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_OFFICE_ASUNTS_LIST_ERROR_MESSAGE + " [" + useCaseConf.getSessionID() + "]", exception);
			HibernateUtil.rollbackTransaction(tran);
			throw new AttributesException( AttributesException.ERROR_CANNOT_FIND_VALIDATIONLISTFIELDS);
			
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
	}

	/**
	 * Devuelve una lista de CCAA.
	 * 
	 * @return list Listado de CCAA.
	 * 
	 * @throws ValidationException
	 *             si ha habido algún problema.
	 */
	public List<ScrCCAA> getListCCAA() throws ValidationException {
		
		LOG.trace("Entrando en ValidationListBo.getListCCAA()");
		
		ScrCCAADAO scrCCAADAO;
		List<ScrCCAA> result = null;
		
		try {
			ApplicationContext appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
			scrCCAADAO = (ScrCCAADAO) appContext.getBean("scrCCAADAO");
			result = scrCCAADAO.getListCCAA();
			
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_LIST_CCAA_MESSAGE, exception);
			throw new ValidationException(exception);
		}
		
		return result;
	}

	/**
	 * Devuelve una lista de CCAA.
	 * 
	 * @return list Listado de CCAA.
	 * 
	 * @throws ValidationException
	 *             si ha habido algún problema.
	 */
	@SuppressWarnings("unchecked")
	public List<ScrCCAA> getListCCAA(UseCaseConf useCaseConf) throws ValidationException {
		
		LOG.trace("Entrando en ValidationListBo.getListCCAA()");
		
		List<ScrCCAA> result = new ArrayList<ScrCCAA>();
		Transaction tran = null;

		try {
			Validator.validate_String_NotNull_LengthMayorZero( useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);

			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();

			Criteria criteriaResults = session.createCriteria(ScrCCAA.class);
			criteriaResults.addOrder(Order.asc("name"));

			result = (List<ScrCCAA>) criteriaResults.list();
			
			if (session.isOpen()) {
				HibernateUtil.commitTransaction(tran);
			}

			HibernateUtil.commitTransaction(tran);
			
		} catch (Exception e) {
			LOG.error(ErrorConstants.GET_LIST_CCAA_MESSAGE, e);
			HibernateUtil.rollbackTransaction(tran);
			throw new ValidationException(e);
			
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
		
		return result;
	}

	/**
	 * Devuelve una lista de provincias.
	 * 
	 * @return list Listado de provincias.
	 * 
	 * @throws ValidationException
	 *             si ha habido algún problema.
	 */
	@SuppressWarnings("unchecked")
	public List<ScrProv> getListProv(UseCaseConf useCaseConf) throws ValidationException {
		
		LOG.trace("Entrando en ValidationListBo.getListCCAA()");

		List<ScrProv> result = null;

		Transaction tran = null;
		
		try {
			Session session = HibernateUtil.currentSession(useCaseConf.getEntidadId());
			tran = session.beginTransaction();
			// Recuperamos la sesión
			CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());

			Criteria criteriaResults = session.createCriteria(ScrProv.class);
			criteriaResults.addOrder(Order.asc("name"));

			result = (List<ScrProv>) criteriaResults.list();
			
			result = ordenaListaProvincias(result);
			
			if (session.isOpen()) {
				HibernateUtil.commitTransaction(tran);
			}
			
		} catch (Exception exception) {
			HibernateUtil.rollbackTransaction(tran);
			LOG.error(ErrorConstants.GET_LIST_PROV_MESSAGE, exception);
			throw new ValidationException(exception);
			
		} finally {
			HibernateUtil.closeSession(useCaseConf.getEntidadId());
		}
		
		return result;
	}
	
	public static List<ScrProv> ordenaListaProvincias(List<ScrProv> list) {
        Collections.sort(list, new Comparator<ScrProv>() {
            Collator collator = Collator.getInstance();

            public int compare(ScrProv o1, ScrProv o2) {
                return collator.compare(o1.getName(), o2.getName());
            }
        });
        return list;
    }


	/**
	 *
	 * Metodo que obtiene el listado de objetos segun el userTypeId que pasemos
	 * como parametro El array de objetos puede ser: usuario, departamentos o
	 * grupos
	 *
	 * @param sessionID
	 * @param userTypeId
	 * @param entidad
	 * @return
	 * @throws SessionException
	 * @throws ValidationException
	 */
	@SuppressWarnings("rawtypes")
	public static List getDeptsGroupsUsers(String sessionID, Integer userTypeId, String entidad, List<Integer> orgIds) throws SessionException, ValidationException {
		
		Transaction tran = null;
		List<?> deptsGroupsUserList = null;

		IuserdepthdrDAO iuserdepthdrDAO = null;
		
		try {
			iuserdepthdrDAO = (IuserdepthdrDAO) appContext.getBean("iuserdepthdrDAO");
			Validator.validate_String_NotNull_LengthMayorZero(sessionID, ValidationException.ATTRIBUTE_SESSION);

			Session session = HibernateUtil.currentSession(entidad);
			tran = session.beginTransaction();

			// Recuperamos la sesion
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(sessionID);
			AuthenticationUser user = (AuthenticationUser) cacheBag.get(HIBERNATE_Iuseruserhdr);

			ScrOfic scrOfic = (ScrOfic) cacheBag.get(HIBERNATE_ScrOfic);
			List<?> iUserGroupUser = ISicresQueries.getIUserGroupUser(session, user.getId());

			if (userTypeId.intValue() == 1) {
				deptsGroupsUserList = ISicresQueries.getUsers(session, user.getId());
				
			} else if (userTypeId.intValue() == 2) {
				if (scrOfic != null) {
					if (orgIds == null || orgIds.size() == 0) {
						deptsGroupsUserList = iuserdepthdrDAO.getDeparts( new Integer(scrOfic.getDeptid()), null);
						
					} else {
						deptsGroupsUserList = iuserdepthdrDAO.getDeparts( new Integer(scrOfic.getDeptid()), orgIds);
					}
					
				} else {
					if (orgIds != null && orgIds.size() > 0) {
						deptsGroupsUserList = iuserdepthdrDAO.getDeparts(null, orgIds);
					}
				}
				
			} else {
				// obtenemos los grupos a excepcion de los que pertenece el
				// usuario
				deptsGroupsUserList = ISicresQueries.getGroups(session, iUserGroupUser);
			}

			HibernateUtil.commitTransaction(tran);

			return deptsGroupsUserList;
			
		} catch (SessionException sE) {
			HibernateUtil.rollbackTransaction(tran);
			throw sE;
			
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction(tran);
			LOG.error("Impossible to find deptsGroupsUserList", e);
			throw new ValidationException( ValidationException.ERROR_USERS_LIST_NOT_FOUND);
			
		} finally {
			HibernateUtil.closeSession(entidad);
		}
	}
}