package es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Iecisa
 * @version $Revision$
 *
 *          Clase para el uso de datasources multiEntidad
 *
 *          Configuracion de Spring <bean id="dataSource" class="es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityJndiDatasource"
 *          > <property name="jndiBaseName">
 *          <value>java:comp/env/jdbc/person</value> </property> </bean>
 *
 *
 *
 */
public class MultiEntityJndiDatasourceScheduler extends MultiEntityDataSourceScheduler {
	/**
	 * Logger for this class
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiEntityJndiDatasourceScheduler.class);

	protected String jndiBaseName;

	public MultiEntityJndiDatasourceScheduler() {
		super();
		this.multiEntityDatasourceSchedulerHelper = new MultiEntityJndiDatasourceSchedulerHelper();
	}

	public String getJndiBaseName() {
		return jndiBaseName;
	}

	public void setJndiBaseName(String jndiBaseName) {
		this.jndiBaseName = jndiBaseName;
		((MultiEntityJndiDatasourceSchedulerHelper) this.multiEntityDatasourceSchedulerHelper).jndiBaseName = jndiBaseName;
	}

	public String getJndiName() {
		return multiEntityDatasourceSchedulerHelper.getJndiName();
	}
}
