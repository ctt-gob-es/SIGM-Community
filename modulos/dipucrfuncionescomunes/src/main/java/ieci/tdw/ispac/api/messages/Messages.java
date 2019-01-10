package ieci.tdw.ispac.api.messages;

import ieci.tdw.ispac.ispaclib.messages.MessagesFormatter;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class Messages {
	
	public static final Logger LOGGER = Logger.getLogger(Messages.class);

	/** Nombre del fichero de recursos. */
	private static final String BUNDLE_NAME = "ieci.tdw.ispac.api.resources.ApplicationResources";

	/** Recursos. */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);


	/**
	 * Obtiene el texto del recurso especificado.
	 * @param key Clave del texto.
	 * @return Texto.
	 */
	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			LOGGER.error("Error al recuperar el texto del recurso específico.", e);
			return key;
		}
	}

	/**
	 * Obtiene el texto del recurso especificado.
	 * @param key Clave del texto.
	 * @param params Parámetros para sustituir en el texto.
	 * @return Texto.
	 */
	public static String getString(String key, Object [] params) {
		try {
			String text = RESOURCE_BUNDLE.getString(key);
			if ((text != null) && (text.length() > 0)) {
				text = MessagesFormatter.format(text, params);
			}
			return text;
		} catch (MissingResourceException e){
			LOGGER.error("Error al recuperar el texto del recurso específico.", e);
			return key;
		}
	}

}
