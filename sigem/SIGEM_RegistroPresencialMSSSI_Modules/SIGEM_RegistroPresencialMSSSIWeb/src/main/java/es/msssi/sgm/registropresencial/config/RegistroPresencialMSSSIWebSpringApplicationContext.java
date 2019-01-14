/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.config;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Clase que inicializa el contexto de spring.
 * 
 * @author jortizs
 * 
 */
public class RegistroPresencialMSSSIWebSpringApplicationContext {

    private static final String DEFAULT_CONFIG_FILE = "classpath:beans/SIGEM_RegistroPresencialMSSSIWeb/applicationContext.xml";

    private static final Logger LOGGER = Logger .getLogger(RegistroPresencialMSSSIWebSpringApplicationContext.class);

    private ApplicationContext contenedor;

    protected static RegistroPresencialMSSSIWebSpringApplicationContext _instance = null;

    /**
     * Constructor.
     */
    public RegistroPresencialMSSSIWebSpringApplicationContext() {
    	try {
    		// Instanciamos el contenedor de Spring
    		contenedor = new ClassPathXmlApplicationContext(DEFAULT_CONFIG_FILE);
    		
    	} catch (Exception e) {
    		LOGGER.error("Error cargando propiedades de configuración iniciales.", e);
    	}
    }

    /**
     * Devuelve la instacia del objecto.
     * 
     * @return Dir3WSSpringApplicationContext instancia del objecto.
     */
    public synchronized static RegistroPresencialMSSSIWebSpringApplicationContext getInstance() {
    	
    	if (_instance == null) {
    		_instance = new RegistroPresencialMSSSIWebSpringApplicationContext();
    	}
    	
    	return _instance;
    }

    /**
     * Devuelve el contexto de la aplicación.
     * 
     * @return ApplicationContext contexto de la aplicación.
     */
    public ApplicationContext getApplicationContext() {
    	return contenedor;
    }

    /**
     * Recoge el contexto de la aplicación.
     * 
     * @param appContext
     *            contexto de la aplicación.
     */
    public void setApplicationContext(ApplicationContext appContext) {
    	contenedor = appContext;
    }
}