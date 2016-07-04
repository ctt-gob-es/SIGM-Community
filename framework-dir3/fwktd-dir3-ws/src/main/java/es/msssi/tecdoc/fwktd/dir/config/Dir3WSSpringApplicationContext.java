package es.msssi.tecdoc.fwktd.dir.config;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * Clase que inicializa el contexto de spring.
 * 
 * @author cmorenog
 * 
 */
public class Dir3WSSpringApplicationContext {
	
	private static final String DEFAULT_CONFIG_FILE 	= "fwktd-dir3Context.xml";

	private static final Logger logger = Logger.getLogger(Dir3WSSpringApplicationContext.class);

	private ApplicationContext contenedor;

	protected static Dir3WSSpringApplicationContext _instance = null;

	/**
	 * Constructor.
	 */
	public Dir3WSSpringApplicationContext(){
		try {
			// Instanciamos el contenedor de Spring
			contenedor = new ClassPathXmlApplicationContext(DEFAULT_CONFIG_FILE);
		} catch (Throwable e) {
			logger.error("Error cargando propiedades de configuración iniciales.", e);
		}
	}

	/**
	 * Devuelve la instacia del objecto.
	 * @return Dir3WSSpringApplicationContext
	 * 			instancia del objecto.
	 */
	public synchronized static Dir3WSSpringApplicationContext getInstance() {
		if (_instance == null) {
			_instance = new Dir3WSSpringApplicationContext();
		}

		return _instance;
	}

	/**
	 * Devuelve el contexto de la aplicación.
	 * @return ApplicationContext
	 * 			contexto de la aplicación.
	 */
	public ApplicationContext getApplicationContext(){
		return contenedor;
	}
	
	/**
	 * Recoge el contexto de la aplicación.
	 * @param appContext
	 * 			contexto de la aplicación.
	 */
	public void setApplicationContext(ApplicationContext appContext){
		contenedor=appContext;
	}
}
