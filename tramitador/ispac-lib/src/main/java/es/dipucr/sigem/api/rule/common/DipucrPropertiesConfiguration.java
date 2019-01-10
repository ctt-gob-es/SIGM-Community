package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;
import ieci.tdw.ispac.ispaclib.util.PropertiesConfiguration;

import java.io.File;

import org.apache.log4j.Logger;

@SuppressWarnings("serial")
public abstract class DipucrPropertiesConfiguration extends PropertiesConfiguration {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(DipucrPropertiesConfiguration.class);
	
	protected static String baseFilePath;//[dipucr-Felipe #507]
	
	/**
	 * Constructor.
	 */
	public DipucrPropertiesConfiguration() {
		super();
	}
	
	/**
	 * Creación de la instancia con la ruta correspondiente
	 * @param entidad
	 * @param configFileName
	 * @throws ISPACRuleException
	 */
	protected void createInstance(String entidad, String configFileName) throws ISPACRuleException{

		String filePath = null;
		try{		
			try {
				filePath = "config_" + entidad + File.separator + configFileName;
				this.initiate(filePath);				
			} 
			catch (ISPACException e) {
				try {
					logger.warn("No existe el fichero de configuración: " + filePath);
					filePath = "config_" + File.separator + configFileName;
					this.initiate(filePath);
				} 
				catch (ISPACException e1) {
					try {
						logger.warn("No existe el fichero de configuración: " + filePath);
						filePath = "";//[dipucr-Felipe #507]
						this.initiate(configFileName);
					} 
					catch (ISPACException e2) {
						logger.warn("No existe el fichero de configuración: " + configFileName);
						String error = "Error en la lectura del fichero de properties " + configFileName + ": "+ e2.getMessage();
						logger.error(error, e2);
						throw new ISPACRuleException(error, e2);
					}
				}
			}
		} catch (Exception e3) {
			String error = "Error general en la lectura del fichero de properties" + filePath + ": " + e3.getMessage();
			logger.error(error, e3);
			throw new ISPACRuleException(error, e3);
		}
		String basePath = filePath.replace(File.separator + configFileName, "");
		baseFilePath = ConfigurationHelper.getConfigFilePath(basePath);//[dipucr-Felipe #507]
	}
	
	/**
	 * Creación de la instancia con la ruta correspondiente
	 * @param configFileName
	 * @throws ISPACRuleException
	 */
	protected void createInstance(String configFileName) throws ISPACRuleException{

		try {
			this.initiate(configFileName);
		} 
		catch (ISPACException e2) {
			logger.warn("No existe el fichero de configuración: " + configFileName);
			String error = "Error en la lectura del fichero de properties " + configFileName + ": "+ e2.getMessage();
			logger.error(error, e2);
			throw new ISPACRuleException(error, e2);
		}
	}
	
	public String getBaseFilePath() {
		return baseFilePath;
	}
}
