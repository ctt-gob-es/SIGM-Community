// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/**
 * <b>File:</b><p>es.gob.afirma.i18n.Language.java.</p>
 * <b>Description:</b><p>Class that manages the access to the properties files used for generating messages in the platform.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>24/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 24/01/2014.
 */
package es.gob.afirma.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * <p>Class that manages the access to the properties files used for generating messages in the platform.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 24/01/2014.
 */
public final class Language {

	/**
	 * Constant attribute that represents the locale es_ES.
	 */
	public static final Locale LOCALE_ES_ES = new Locale("es", "ES");

	/**
	 * Constant attribute that represents the locale en_US.
	 */
	public static final Locale LOCALE_EN_US = new Locale("en", "US");

	/**
	 * Constant attribute that represents the name of messages directory inside configuration directory.
	 */
	private static final String MESSAGES_DIRECTORY = "messages";

	/**
	 * Attribute that represents the URL class loader for the messages files.
	 */
	private static URLClassLoader urlClassLoaderMessages = null;

	/**
	 * Constant attribute that represents the key for the configured locale for the platform.
	 */
	private static final String LANGUAGE = "LANGUAGE";

	/**
	 * Constant attribute that represents the string to identify the the bundle name for the file with the application language.
	 */
	private static final String BUNDLENAME_LANGUAGE = "Language";

	/**
	 * Constant attribute that represents the string to identify the bundle name to the logs.
	 */
	private static final String BUNDLENAME_INTEGRA = "integra";

	/**
	 * Attribute that represents the locale specified in the configuration.
	 */
	private static Locale currentLocale = null;

	/**
	 * Attribute that represents the properties for the locale for the messages.
	 */
	private static ResourceBundle resIntegra = null;

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(Language.class);

	/**
	 * Constructor method for the class Language.java.
	 */
	private Language() {
	}

	/**
	 * Method that initializes the resources for accessing to the properties files.
	 */
	static {
		URL url = Language.class.getClassLoader().getResource(MESSAGES_DIRECTORY);
		final File configDirFile = new File(url.getPath());
		urlClassLoaderMessages = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {

			public URLClassLoader run() {
				try {
					return new URLClassLoader(new URL[ ] { configDirFile.toURI().toURL() });
				} catch (MalformedURLException e) {
					LOGGER.error(e);
					return null;
				}
			}
		});
		reloadMessagesConfiguration();

	}

	/**
	 * Method that loads the configured locale and reload the text messages.
	 */
	public static void reloadMessagesConfiguration() {
		if (urlClassLoaderMessages != null) {
			boolean takeDefaultLocale = false;
			String propLocale = null;

			// Cargamos el recurso que determina el locale.
			URL url = Language.class.getClassLoader().getResource(BUNDLENAME_LANGUAGE + ".properties");
			if (url == null) {
				takeDefaultLocale = true;
			} else {
				try {
					URI uri = new URI(url.toString());
					Properties p = new Properties();
					p.load(new FileInputStream(new File(uri)));
					propLocale = p.getProperty(LANGUAGE);
				} catch (Exception e) {
					takeDefaultLocale = true;
				}
			}
			// Tratamos de inicializar el Locale.
			if (propLocale == null) {
				takeDefaultLocale = true;
			} else {
				propLocale = propLocale.trim();
				String[ ] localeSplit = propLocale.split("_");
				if (localeSplit == null || localeSplit.length != 2) {
					takeDefaultLocale = true;
				} else {
					currentLocale = new Locale(localeSplit[0], localeSplit[1]);
				}
			}
			// Si hay que tomar el locale por defecto...
			if (takeDefaultLocale) {
				LOGGER.error("No property was obtained correctly determining the Locale for log messages. Will take the default locale.");
				currentLocale = Locale.getDefault();
			}

			// Se informa en el log del Locale selecccionado.
			LOGGER.info("Take the next locale for messages logs: " + currentLocale.toString());

			// Se cargan los mensajes
			resIntegra = ResourceBundle.getBundle(MESSAGES_DIRECTORY + "." + BUNDLENAME_INTEGRA, currentLocale);
		}
	}

	/**
	 * Method that gets the bundle message for certain key.
	 * @param key Parameter that represents the key for obtain the message.
	 * @return The bundle message of the persistence module for certain key.
	 */
	public static String getResIntegra(String key) {
		return resIntegra.getString(key);
	}

	/**
	 * Method that gets the bundle message for certain key and values indicated as input parameters.
	 * @param key Parameter that represents the key for obtain the message.
	 * @param values Parameter that represents the list of values for insert in the message.
	 * @return the bundle message for certain key and values indicated as input parameters.
	 */
	public static String getFormatResIntegra(String key, Object[ ] values) {
		return new MessageFormat(resIntegra.getString(key), currentLocale).format(values);
	}
}
