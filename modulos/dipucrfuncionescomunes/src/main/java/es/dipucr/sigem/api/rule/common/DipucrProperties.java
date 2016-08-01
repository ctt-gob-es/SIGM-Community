package es.dipucr.sigem.api.rule.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DipucrProperties {
	
	private static final Logger logger = Logger.getLogger(DipucrProperties.class);
	
	private static DipucrProperties INSTANCE = null;
	
	/** Objeto properties con los ids necesarios */
	private Properties props;
	
	/** Path para acceder al fichero de propiedades */
	private String RUTA_PATH= "/conf/dipucr/";
	private final String PATH_PROPERTIES_FILE = "DipucrResources.properties";
	
	private  DipucrProperties(){
		props = new Properties();
		
		String ruta = System.getProperty("SIGEM_TramitacionWeb.app.path");
		//INICIO [eCenpri-Felipe #390] Evitar errores en el tomcat externo, máquina 16
		if (null == ruta){
			//La ruta del publicador siempre viene rellena, luego sustituimos
			ruta = System.getProperty("SIGEM_PublicadorWeb.app.path");
			ruta = ruta.replace("SIGEM_PublicadorWeb", "SIGEM_TramitacionWeb");
		}
		//FIN [eCenpri-Felipe #390]
		InputStream is;
		try {
			is = new FileInputStream(ruta+RUTA_PATH+PATH_PROPERTIES_FILE);
			props.load(is);
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e){
			logger.error(e.getMessage(), e);
		}
		
	}
	
	public String getProperty(String key){
		return props.getProperty(key);
	}
	
	// creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instanciación múltiple 
    private synchronized static void createInstance() {
        INSTANCE = new DipucrProperties();
    }
 
    public static DipucrProperties getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    
    /**
     * [eCenpri-Felipe #818]
     * Devuelve la propiedad del fichero de forma 'No singleton'
     * @param key
     * @return
     */
    public static String getPropertyNoSingleton(String key){
    	createInstance();
    	return INSTANCE.props.getProperty(key);
    }
}
