package es.dipucr.sgm.registropresencial.configurations;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.faces.context.FacesContext;

import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;

public class EscanerConfiguration extends Properties {
	
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(EscanerConfiguration.class);

	private static final String CONFIG_FILENAME = "conf.properties";
	
	public static final String VERSION = "app.version";

	/**
	 * Constructor.
	 */
	public EscanerConfiguration() {
		super();
		
		try{
			String urlCheckForUpdates = Configurator.getInstance().getProperty(ConfigurationKeys.KEY_URL_CHECK_FOR_UPDATES_SCAN);
			String rutaAbsolutaInstalador = FacesContext.getCurrentInstance().getExternalContext().getRealPath(urlCheckForUpdates + CONFIG_FILENAME);
				
			InputStream in = new FileInputStream(rutaAbsolutaInstalador);
			load(in);
				
			if( null != in){
				in.close();
			}
			
			in.close();

		} catch (FileNotFoundException e){
			LOGGER.error("ERROR al instanciar el EscanerConfiguration." + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("ERROR al instanciar el EscanerConfiguration." + e.getMessage(), e);
		}
	}
}
