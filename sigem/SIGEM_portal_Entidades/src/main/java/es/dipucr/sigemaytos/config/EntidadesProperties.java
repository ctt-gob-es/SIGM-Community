package es.dipucr.sigemaytos.config;

import java.io.InputStream;
import java.util.Properties;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

public class EntidadesProperties {

	private static final Logger LOGGER = Logger.getLogger(EntidadesProperties.class);
	
	//Clase de tipo Singleton, con un constructor privado
	private static EntidadesProperties INSTANCE = null;
	
	private Properties myProperties;
	 
    // Private constructor suppresses 
    private EntidadesProperties() {
    	try{
    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    	InputStream dataStream = externalContext.getResourceAsStream(ConstantesConfiguracion.RUTA_ENTIDADES);
	    	myProperties = new Properties();
	    	myProperties.load(dataStream);
    	}
    	catch(Exception e){
			e.printStackTrace();
    		LOGGER.error("Error al cargar las constantes de configuración", e);
    	}
    }
 
    // Creador sincronizado para protegerse de posibles problemas  multi-hilo
    private synchronized static void createInstance() {
        INSTANCE = new EntidadesProperties();
    }
 
    public static EntidadesProperties getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    
    public String getProperty(String key){
    	return myProperties.getProperty(key);
    }
    
    public Properties getProperties(){
    	return myProperties;
    }
	
}
