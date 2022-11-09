/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;

@Repository
@Transactional
public class BaseDAO {

	Logger log = Logger.getLogger(BaseDAO.class);

	@PersistenceContext(unitName="portafirmasDataSource")
	private EntityManager entityManager = null;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void updateList(List<AbstractBaseDTO> list) {
		log.info("UpdateList size: " + list.size() + " values.");
		for (AbstractBaseDTO baseDTO : list) {
			update(baseDTO);
		}
		log.info("UpdateList done successfully.");
	}

	public void updateSet(Set<AbstractBaseDTO> list) {
		log.info("UpdateSet size: " + list.size() + " values.");
		for (AbstractBaseDTO baseDTO : list) {
			update(baseDTO);
		}
		log.info("UpdateSet done successfully.");
	}

	public void insertOrUpdateList(List<AbstractBaseDTO> list) {
		log.info("InsertOrUpdateList size: " + list.size() + " values.");
		for (AbstractBaseDTO baseDTO : list) {
			insertOrUpdate(baseDTO);
		}
		log.info("InsertOrUpdateList done successfully.");
	}

    public void insertOrUpdateSet(Set<AbstractBaseDTO> list) {
		log.info("InsertOrUpdateSet size: " + list.size() + " values.");
		for (AbstractBaseDTO baseDTO : list) {
			insertOrUpdate(baseDTO);
		}
		log.info("InsertOrUpdateSet done successfully.");
	}
    
    public void update(AbstractBaseDTO baseDTO) {
		log.info("Update element: " + baseDTO.getClass() + " (PrimaryKey = "
				+ baseDTO.getPrimaryKey() + " )");
		entityManager.merge(baseDTO);
		entityManager.flush();
		log.info("Update done successfully.");
	}

    @Transactional
    public void invokeDdl(String sql){
    	Query q = entityManager.createNativeQuery(sql);
    	q.executeUpdate();
    }
    
    @Transactional
    public void insertOrUpdate(AbstractBaseDTO baseDTO) {
		log.info("InsertOrUpdate element: "
				+ baseDTO.getClass()
				+ " (PrimaryKey = "
				+ (baseDTO.getPrimaryKey() == null ? "NEW" : baseDTO
						.getPrimaryKey()) + " )");
		if (baseDTO.getPrimaryKey() == null) {
			try {
				entityManager.persist(baseDTO);
				entityManager.flush();
				log.info("InsertOrUpdate done successfully.");
			} catch (Throwable t) {
				log.error(t.getMessage(),t);
			}
		} else {
			entityManager.merge(baseDTO);
			entityManager.flush();
			log.info("InsertOrUpdate done successfully.");
		}
		
	}

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Map<String, Object> insertOrUpdateWithBLOB(AbstractBaseDTO baseDTO, final List<String> campos, final List<String> camposDeBD, final String nameIdColumn) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		log.info("InsertOrUpdate element: "
				+ baseDTO.getClass()
				+ " (PrimaryKey = "
				+ (baseDTO.getPrimaryKey() == null ? "NEW" : baseDTO
						.getPrimaryKey()) + " )");
		Map<String, Object> bkCampos = new HashMap<String, Object>();
		for(String campo: campos){
			Method metodoGet = baseDTO.getClass().getMethod("get"+campo.substring(0, 1).toUpperCase()+campo.substring(1), null);
			Object bkObject = metodoGet.invoke(baseDTO, null);
			Method metodoSet;
			if(bkObject!=null){
				bkCampos.put(campo, bkObject);
				metodoSet = baseDTO.getClass().getMethod("set"+campo.substring(0, 1).toUpperCase()+campo.substring(1), bkObject.getClass());
				Object[] params = new Object[1];
				params[0]=null;
				metodoSet.invoke(baseDTO, params);
			}
		}
		if (baseDTO.getPrimaryKey() == null) {
			try {
				entityManager.persist(baseDTO);
				entityManager.flush();
			} catch (Throwable t) {
				log.error(t);
			}
		} else {
			entityManager.merge(baseDTO);
			entityManager.flush();
		}
		
		log.info("InsertOrUpdate done successfully.");
		return bkCampos;
	}
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertBLOB(AbstractBaseDTO baseDTO, final List<String> campos, final List<String> camposDeBD, final String nameIdColumn, Map<String, Object> bkCampos) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		log.info("InsertOrUpdate element: "
				+ baseDTO.getClass()
				+ " (PrimaryKey = "
				+ (baseDTO.getPrimaryKey() == null ? "NEW" : baseDTO
						.getPrimaryKey()) + " )");
		if (baseDTO.getPrimaryKey() != null) {
			try {
				
				final AbstractBaseDTO baseDTO2 = baseDTO;
				
				final Map<String, Object> bkCampos2 = bkCampos;
				
				getJdbcTemplate().execute(
						new ConnectionCallback<Object>() {

							@Override
							public Object doInConnection(Connection con) throws SQLException, DataAccessException {
								
								try{
									// update bytes
									Table tAnotation = baseDTO2.getClass().getAnnotation(Table.class);
									
									String stringUpdate = "UPDATE "+tAnotation.name()+" SET ";
									
									for(int i=0; i<camposDeBD.size(); ++i){
										stringUpdate += camposDeBD.get(i)+"=?";
										if(i<camposDeBD.size()-1){
											stringUpdate+=",";
										}
									}
									
									PreparedStatement pst = con
											.prepareStatement(stringUpdate+" WHERE "+nameIdColumn+" = ?");
									
									log.info("El query que se quiere ejecutar es "+stringUpdate+" WHERE "+nameIdColumn+" = ?");

									for(int i=0; i<camposDeBD.size(); ++i){
										if(bkCampos2.containsKey(campos.get(i)) && bkCampos2.get(campos.get(i))!=null ){
											InputStream inputStream = null;
											int tam = 0;
											if(bkCampos2.get(campos.get(i)) instanceof String){
												inputStream = new ByteArrayInputStream( ((String)bkCampos2.get(campos.get(i))).getBytes() );
												tam = ((String)bkCampos2.get(campos.get(i))).getBytes().length;
											}
											else if(bkCampos2.get(campos.get(i)) instanceof byte[]){
												inputStream = new ByteArrayInputStream( ((byte[])bkCampos2.get(campos.get(i))) );
												tam = ((byte[])bkCampos2.get(campos.get(i))).length;
											}
											pst.setBinaryStream(i+1, inputStream, tam);
										}
										else{
											pst.setNull(i+1,Types.BLOB);
										}
									}
									
									Query q = entityManager.createNativeQuery("");

									pst.setString(camposDeBD.size()+1, ""+baseDTO2.getPrimaryKey());
									int result = pst.executeUpdate();
									log.info("File uploaded: " + result);
									pst.close();
									return null;
								}catch(Exception e){
									e.printStackTrace();
								}
								return null;
							}
						}
					);
				
				entityManager.flush();
			} catch (Throwable t) {
				log.error(t);
			}
		}
		log.info("InsertOrUpdate done successfully.");
	}
    
    @Transactional
    public List<?> excecNamedQueryWithoutAbstractDTO(String namedQuery, Map<String, Object> params){
    	Query q = entityManager.createNamedQuery(namedQuery);
    	
    	Set<String> llavesMapa = params.keySet();
    	
    	for(String it: llavesMapa){
    		q.setParameter(it, params.get(it));
    	}
    	
    	return q.getResultList();
    }
    
    @Transactional
    public List excecQueryWithoutAbstractDTO(String queryString, Map<String, Object> params){
    	Query q = entityManager.createQuery(queryString);
    	
    	if(params!=null){
    		Set<String> llavesMapa = params.keySet();
        	
        	for(String it: llavesMapa){
        		q.setParameter(it, params.get(it));
        	}
    	}
    	return q.getResultList();
    }
    
	@SuppressWarnings("unchecked")
	public <T extends AbstractBaseDTO> List<T> queryListMoreParameters(String namedQuery,
			Map<String, Object> parameters) {
		List<T> list;

		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = (String) itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				log.debug("Parameter name: " + parameterName + " value: "
						+ parameterValue);
			}
		}

		list = query.getResultList();
		log.info("QueryList obtains: " + list.size() + " values.");

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<AbstractBaseDTO> queryListMoreParametersList(String namedQuery,
			Map<String, Object[]> parameters) {
		List<AbstractBaseDTO> list;

		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = (String) itParameter.next();
				Object[] parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, Arrays.asList(parameterValue));
				log.debug("Parameter name: " + parameterName + " value: " + Arrays.asList(parameterValue));
			}
		}

		list = query.getResultList();
		log.info("QueryList obtains: " + list.size() + " values.");

		return list;
	}

	public AbstractBaseDTO queryElementMoreParametersWithOutLog(
			String namedQuery, Map<String, Object> parameters)throws NonUniqueResultException {
		AbstractBaseDTO result = null;
		log.info("QueryElement: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
			}
		}
		try {
			result = (AbstractBaseDTO) query.getSingleResult();
		} catch (NoResultException e) {
			log.info("No result found: ", e);
		}catch(NonUniqueResultException e){
			log.error("More than one result found: ", e);
			throw e;
		}
		log.info("QueryElement obtains: " + (result == null ? "0" : "1") + " values.");

		return result;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractBaseDTO> List<T> queryListOneParameter(String namedQuery, String parameterName, Object parameterValue) {

		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameterName != null) {
			query.setParameter(parameterName, parameterValue);
		}

		log.debug("Parameter name: " + parameterName + " value: "
				+ parameterValue);

		List<T> list = query.getResultList();
		log.info("QueryList obtains: " + list.size() + " values.");
		
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractBaseDTO> List<T> queryListOneParameterList(String namedQuery,
			String parameterName, Object[] parameterValues) {
		List<T> list;

		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameterName != null) {
			query.setParameter(parameterName, Arrays.asList(parameterValues));
		}
		log.debug("Parameter name: " + parameterName + " value: " + Arrays.toString(parameterValues));

		list = query.getResultList();
		log.info("QueryList obtains: " + list.size() + " values.");

		return (List<T>) list;
	}

	public void massiveUpdate(String namedQuery, String parameterName,
			Object parameterValue) {
		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameterName != null) {
			query.setParameter(parameterName, parameterValue);
		}
		log.debug("Parameter name: " + parameterName + " value: " + parameterValue);

		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<AbstractBaseDTO> queryListMoreParametersListComplex(
			String namedQuery, Map<String, Object[]> parametersList,
			Map<String, Object> parametersSimple) {
		List<AbstractBaseDTO> list;

		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parametersList != null && !parametersList.isEmpty()) {
			log.debug("Query with parameters: " + parametersList.size());
			Iterator<String> itParameter = parametersList.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = (String) itParameter.next();
				Object[] parameterValue = parametersList.get(parameterName);
				query.setParameter(parameterName, Arrays.asList(parameterValue));
				log.debug("Parameter name: " + parameterName + " value: "
						+ Arrays.asList(parameterValue));
			}
		}

		if (parametersSimple != null && !parametersSimple.isEmpty()) {
			log.debug("Query with parameters: " + parametersSimple.size());
			Iterator<String> itParameter = parametersSimple.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = (String) itParameter.next();
				Object parameterValue = parametersSimple.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				log.debug("Parameter name: " + parameterName + " value: "
						+ parameterValue);
			}
		}

		list = query.getResultList();
		log.info("QueryList obtains: " + list.size() + " values.");

		return list;
	}
	
	public void deleteList(List<AbstractBaseDTO> list) {
		log.info("DeleteList size: " + list.size() + " values.");
		for (AbstractBaseDTO baseDTO : list) {
			delete(baseDTO);
		}
		log.info("DeleteList done successfully.");
	}

	public void delete(AbstractBaseDTO baseDTO) {
		log.info("Delete element: " + baseDTO.getClass() + " (PrimaryKey = "
				+ baseDTO.getPrimaryKey() + " )");
		// entityManager.remove(entityManager.merge(baseDTO));
		entityManager.remove(entityManager.find(baseDTO.getClass(), baseDTO.getPrimaryKey()));
		log.info("Delete done successfully.");
	}

	public void deleteSet(Set<AbstractBaseDTO> set) {
		log.info("DeleteSet init");
		Iterator<AbstractBaseDTO> it =  set.iterator();
		while (it.hasNext()) {
			delete(it.next());
		}
	}

	public AbstractBaseDTO queryElementMoreParameters(String namedQuery,
			Map<String, Object> parameters) {
		AbstractBaseDTO result = null;
		log.info("QueryElement: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				log.debug("Parameter name: " + parameterName + " value: "
						+ parameterValue);
			}
		}
		try {
			result = (AbstractBaseDTO) query.getSingleResult();
		} catch (NoResultException e) {
			log.debug("No result found");
		}
		log.info("QueryElement obtains: " + (result == null ? "0" : "1")
				+ " values.");

		return result;
	}

	@Transactional(readOnly=true)
	public AbstractBaseDTO queryElementOneParameter(String namedQuery,
			String parameterName, Object parameterValue) {
		AbstractBaseDTO result = null;
		log.info("QueryElement: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameterName != null) {
			query.setParameter(parameterName, parameterValue);
		}
		log.debug("Parameter name: " + parameterName + " value: "
				+ parameterValue);

		try {
			result = (AbstractBaseDTO) query.getSingleResult();
		} catch (NoResultException e) {
			log.debug("No result found");
		}
		log.info("QueryElement obtains: " + (result == null ? "0" : "1")
				+ " values.");

		return result;
	}

	public Long queryCount(String namedQuery, Map<String, Object> parameters) {
		Long result;
		//log.info("QueryCount: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			//log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				//log.debug("Parameter name: " + parameterName + " value: "
					//	+ parameterValue);
			}
		}

		result = (Long) query.getSingleResult();
		//log.info("QueryCount obtains: " + result.intValue() + " values.");

		return result;
	}

	public void executePLSQL(String PLSQL) {
		log.info("executePLSQL: " + PLSQL);
		entityManager.getTransaction().begin();
		Query query = entityManager.createNativeQuery(PLSQL);
		query.executeUpdate();
		entityManager.getTransaction().commit();
	}

	public void insertOrUpdateCommit(AbstractBaseDTO baseDTO) {
		entityManager.getTransaction().begin();
		insertOrUpdate(baseDTO);
		entityManager.getTransaction().commit();
	}

	public void refresh(AbstractBaseDTO baseDTO) {
		entityManager.merge(baseDTO);
	}

	public  <T extends AbstractBaseDTO> void refresh(Collection<T> baseDTOList) {
		entityManager.refresh(baseDTOList);		
	}	

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void flush() {
		entityManager.flush();		
	}

	@SuppressWarnings("unchecked")
	public List<AbstractBaseDTO> queryListOneParameterWithTransformer(String namedQuery,
			String parameterName, Object parameterValue, Class<?> dtoClass) {
		List<AbstractBaseDTO> list;

		log.info("QueryList: " + namedQuery);
		
		HibernateEntityManager hem = entityManager.unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		org.hibernate.Query query = session.getNamedQuery(namedQuery);

		if (parameterName != null) {
			query.setParameter(parameterName, parameterValue);
		}

		log.debug("Parameter name: " + parameterName + " value: "
				+ parameterValue);

		list = query.setResultTransformer(Transformers.aliasToBean(dtoClass)).list();
		log.info("QueryList obtains: " + list.size() + " values.");

		return list;
	}

	/**
	  * Método que elimina un objeto persistente de la sesión de Hibernate
	  * @param baseDTO Objeto persistente a eliminar de la sesión
	  */
	 public void detach(AbstractBaseDTO baseDTO) {
		 entityManager.detach(baseDTO);
	 }
	 
	 /**
	  * Método para limpiar la caché de Hibernate
	  */
	 public void clearSession() {
		 entityManager.clear();
	 }
	 
	 public void beginTransaction () {
		 if (entityManager.getTransaction() == null) {
			 entityManager.getTransaction().begin();	
		 }
	 }
	 
	 public void commitTransaction () {
		 entityManager.getTransaction().commit();
	 }
	
	 public void rollbackTransaction () {
		 entityManager.getTransaction().rollback();
	 }
	

	 /**
	  * Método que devuelve un resultado variable de la base de datos
	  * @param namedQuery Consulta
	  * @param parameters Parámetros de la consulta
	  * @return Lista de objetos (columnas)
	  */
	 @SuppressWarnings("unchecked")
	public List<Object[]> queryStandardResult(String namedQuery, Map<String, Object> parameters) {
		List<Object[]> result = null;
		Query query = entityManager.createNamedQuery(namedQuery);

		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				log.debug("Parameter name: " + parameterName + " value: " + parameterValue);
			}
		}

		try {
			result = query.getResultList();
		} catch (NoResultException e) {
			log.debug("No result found");
		}

		return result;
	}

	/**
	 * Método que devuelve un resultado variable de la base de datos a partir de una consulta estandar sql
	 * @param sqlQuery Consulta sql
	 * @return Lista de objetos (columnas)
	 */
	public List querySQLStandardResult(String sqlQuery) {
		List result = null;
		Query query = entityManager.createNativeQuery(sqlQuery);

		try {
			result = query.getResultList();
			log.debug("result found " + result.size());
		} catch (NoResultException e) {
			log.error("No result found, ", e);
		}

		return result;
	}
	
	

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Método que devuelve una lista de resultados paginados de la base de datos
	 * @param namedQuery
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AbstractBaseDTO> queryPaginatedListMoreParameters(String namedQuery,
																  Map<String, Object> parameters,
																  int firstPosition, int maxResults) {
		List<AbstractBaseDTO> list;

		log.info("QueryList: " + namedQuery);

		Query query = entityManager.createNamedQuery(namedQuery);

		// Se asignan los valores de los parámetros
		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = (String) itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				log.debug("Parameter name: " + parameterName + " value: "
						+ parameterValue);
			}
		}

		// Se limita el número de resultados
		query.setFirstResult(firstPosition);
		query.setMaxResults(maxResults);

		list = query.getResultList();
		log.info("QueryList obtains: " + list.size() + " values.");

		return list;
	}
	
	public List<AbstractBaseDTO> queryListMoreParametersWithTransformer(String namedQuery,
			  Map<String, Object> parameters,
			  int firstPosition, int maxResults, Class<?> dtoClass) {
		List<AbstractBaseDTO> list;

		log.info("QueryList: " + namedQuery);

		HibernateEntityManager hem = entityManager.unwrap(HibernateEntityManager.class);
		Session session = hem.getSession();
		org.hibernate.Query query = session.getNamedQuery(namedQuery);

		// Se asignan los valores de los parámetros
		if (parameters != null && !parameters.isEmpty()) {
			log.debug("Query with parameters: " + parameters.size());
			Iterator<String> itParameter = parameters.keySet().iterator();
			while (itParameter.hasNext()) {
				String parameterName = (String) itParameter.next();
				Object parameterValue = parameters.get(parameterName);
				query.setParameter(parameterName, parameterValue);
				log.debug("Parameter name: " + parameterName + " value: "
						+ parameterValue);
			}
		}

		// Se limita el número de resultados
		query.setFirstResult(firstPosition);
		query.setMaxResults(maxResults);
		
		list = query.setResultTransformer(Transformers.aliasToBean(dtoClass)).list();
		log.info("QueryList obtains: " + list.size() + " values.");

		return list;
	}
	
	
	public void insertOrUpdateListCommit(List<AbstractBaseDTO> list) {
		entityManager.getTransaction().begin();
		log.info("insertOrUpdateListCommit size: " + list.size() + " values.");
		try {
			for (AbstractBaseDTO baseDTO : list) {
				log.info("InsertOrUpdate element: "
						+ baseDTO.getClass()
						+ " (PrimaryKey = "
						+ (baseDTO.getPrimaryKey() == null ? "NEW" : baseDTO
								.getPrimaryKey()) + " )");
				if (baseDTO.getPrimaryKey() == null) {
					try {
						entityManager.persist(baseDTO);
						entityManager.flush();
					} catch (Throwable t) {
						log.error(t);
					}
				} else {
					entityManager.merge(baseDTO);
					entityManager.flush();
				}
			}
			entityManager.getTransaction().commit();
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			log.error("ERROR: BaseDAO.insertOrUpdateListCommit, ", e);
		}
		log.info("insertOrUpdateListCommit done successfully.");
	}
	

	public Object findEntitity(Class<?> claseEntidad, Object id){
		return entityManager.find(claseEntidad, id);
	}

}
