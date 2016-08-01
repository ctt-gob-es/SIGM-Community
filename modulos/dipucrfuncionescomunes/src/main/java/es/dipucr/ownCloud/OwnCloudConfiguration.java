package es.dipucr.ownCloud;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.PropertiesConfiguration;

/**
 * Clase que gestiona la configuración de ISPAC.
 *
 */
public class OwnCloudConfiguration extends PropertiesConfiguration {

	private static final long serialVersionUID = 5013658649831582105L;

	private static OwnCloudConfiguration mInstance = null;
	
	public static final String DEFAULT_CONFIG_FILENAME = "ownCloud.properties";

	public static final String URL_OWNCLOUD = "URL_OWNCLOUD";
	public static final String URL_SIN_OWNCLOUD = "URL_SIN_OWNCLOUD";
	public static final String DIR_WEBDAV_OWNCLOUD = "DIR_WEBDAV_OWNCLOUD";
	public static final String DIR_COMPARTIR = "DIR_COMPARTIR";
	
	public static final String USR_IMPRENTA = "USR_IMPRENTA";
	public static final String USR_PASSWORD = "USR_PASSWORD";
	
	/**
	 * Constructor.
	 */
	protected OwnCloudConfiguration() {
		super();
	}

	/**
	 * Obtiene una instancia de la clase.
	 * @return Instancia de la clase.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized OwnCloudConfiguration getInstance() 
			throws ISPACException {
		
		if (mInstance == null) {
			mInstance = new OwnCloudConfiguration();
			mInstance.initiate(DEFAULT_CONFIG_FILENAME);
		}
		return mInstance;
	}
}
