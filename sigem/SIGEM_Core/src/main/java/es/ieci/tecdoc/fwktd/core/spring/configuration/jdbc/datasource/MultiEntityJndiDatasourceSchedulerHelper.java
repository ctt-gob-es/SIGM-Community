package es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource;

/**
 * @author Iecisa
 * @version $Revision$
 *
 */
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.EntidadesException;

import java.util.Hashtable;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiEntityJndiDatasourceSchedulerHelper implements MultiEntityDatasourceSchedulerHelper {
	
	private final static String NON_MULTIENTITY_DATASOURCE_KEY="NON_MULTIENTITY_KEY";
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiEntityJndiDatasourceSchedulerHelper.class);

	private Hashtable<String, DataSource> dataSources = new Hashtable<String, DataSource>();

	protected String jndiBaseName = "";

	public String getJndiBaseName() {
		return jndiBaseName;
	}

	public void setJndiBaseName(String jndiBaseName) {
		this.jndiBaseName = jndiBaseName;
	}
	
	public DataSource getDatasource() {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getDatasource() - start");
		}

		DataSource result = null;

		String currentEntity = MultiEntityContextHolder.getEntity();
		String datasourceKey = currentEntity;

		//si está sin setear la multientidad se usa una clave ficticia para que no rompa el put de la hashtable
		if (StringUtils.isEmpty(currentEntity)){
			datasourceKey = NON_MULTIENTITY_DATASOURCE_KEY;
		}
		
		result = (DataSource) dataSources.get(datasourceKey);
		
		//si no se obtiene de la cache de datasources se genera uno y se introduce en la cache
		if (result == null) {
			
			if(StringUtils.isEmpty(currentEntity)){
				
				List<?> entidades;
				try {
					entidades = LocalizadorServicios.getServicioEntidades().obtenerEntidades();
					for(Object entidad : entidades){
						currentEntity = ((Entidad)entidad).getIdentificador();
					}
					
				} catch (EntidadesException e) {
					LOGGER.error("ERROR al recuperar la lista de entidades: " + e.getMessage(), e);
				} catch (SigemException e) {
					LOGGER.error("ERROR al recuperar la lista de entidades: " + e.getMessage(), e);
				}
			}
			
			result = getJndiDatasourceFromInitialContext(currentEntity);
			
			putDatasource(datasourceKey, result);
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("getDatasource() - end");
		}
		return result;

	}

	private synchronized void putDatasource(String key, DataSource dataSource) {
		if (dataSource != null) {
			this.dataSources.put(key, dataSource);
		}
	}

	protected DataSource getJndiDatasourceFromInitialContext(String entity) {
		DataSource result = null;
		InitialContext ctx;
		String jndiName = getJndiName(entity);
		
		try {
			ctx = new InitialContext();
			result = (DataSource) ctx.lookup(jndiName);

		} catch (NamingException e) {
			String message = "No se ha podido obtener el datasource jndi: " + jndiName;
			LOGGER.error(message);
			throw new RuntimeException(message, e);
		}

		return result;
	}


	public String getJndiName(){
		return getJndiName(MultiEntityContextHolder.getEntity());
	}

	protected String getJndiName(String entity) {
		if (StringUtils.isNotBlank(entity)){
			return jndiBaseName + "_" + entity;
		}
		return jndiBaseName;
	}
}
