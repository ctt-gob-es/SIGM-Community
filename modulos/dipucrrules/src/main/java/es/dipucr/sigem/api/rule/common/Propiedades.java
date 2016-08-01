package es.dipucr.sigem.api.rule.common;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class Propiedades {	
	
	private static final Logger logger = Logger.getLogger(Propiedades.class);

	public Properties misPropiedades;
	public String propertiesFilePath;

	public Propiedades(String path){
		this.propertiesFilePath = path;
		misPropiedades = new Properties();
		init();
	}	
	

	public void init(){
		
		InputStream in = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		// Cargamos el fichero de propiedades en ella
		try {
			in = loader.getResourceAsStream (propertiesFilePath);
			misPropiedades.load(in);
		} catch (Exception e){ 
			logger.error("Ha ocurrido una excepcion al abrir el fichero, no se encuentra o está protegido. " + e.getMessage(), e);
		} 
	
	}
	
	public String getProperty(String id){
		String res = "";
		if(id != null){
			res = misPropiedades.getProperty(id);
		}
		return res;		
	}
}
