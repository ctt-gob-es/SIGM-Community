package es.dipucr.sigem.arbolDocumental.plugin;

import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.util.Properties;

import es.dipucr.sigem.arbolDocumental.utils.Defs;

public class ConfigLoader {

	public static String CONFIG_SUBDIR="SIGEM_ArbolDocumentalWeb";
	public static String CONFIG_FILE="arbolDocumentalWebConfig.properties";

	protected Properties properties;


	public ConfigLoader(){
		properties= loadConfiguration();
	}

	public String getRedirAutenticacionValue(){
		String key=Defs.PLUGIN_REDIRAUTENTICACION;
		String result=getValue(key);
		return result;
	}

	protected String getValue(String key){
		String result="";
		result= properties.getProperty(key);
		return result;
	}

	protected Properties loadConfiguration(){

		Properties result=null;
		SigemConfigFilePathResolver pathResolver = SigemConfigFilePathResolver.getInstance();
		result= pathResolver.loadProperties(CONFIG_FILE, CONFIG_SUBDIR);

		return result;

	}

}
