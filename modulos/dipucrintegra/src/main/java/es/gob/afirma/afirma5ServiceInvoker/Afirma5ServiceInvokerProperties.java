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

package es.gob.afirma.afirma5ServiceInvoker;

import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.dipucr.afirma.AfirmaConfigFilePathResolverImpl;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.utils.GenericUtils;

/**
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 23/03/2011.
 */
public final class Afirma5ServiceInvokerProperties {

    /**
     * Constructor method for the class Afirma5ServiceInvokerProperties.java.
     */
    private Afirma5ServiceInvokerProperties() {
    }

    /**
     * Attribute that represents .
     */
    private static Logger logger = Logger.getLogger(Afirma5ServiceInvokerProperties.class);

    /**
     * Attribute that represents .
     */
    private static long propsFileLastUpdate = -1;
    /**
     * Attribute that represents .
     */
    private static long truststoreLastUpdate = -1;

    /**
     * Attribute that represents .
     */
    private static Properties afirma5ServiceInvokerProperties = new Properties();
    
    private static AfirmaConfigFilePathResolverImpl afirmaConfigFilePathResolver = new AfirmaConfigFilePathResolverImpl();

    /**
     * Attribute that represents truststore class name.
     */
    private static final String TRUSTSTORE_CLASSNAME = "javax.net.ssl.trustStore";

    static {
	init();
    }

    /**
     * Obtiene las propiedades y par&aacute;metros de funcionamiento del API de invocaci&oacute;n de servicios @Firma.
     * @return conjunto de propiedades y par&aacute;metros de funcionamiento del API de invocaci&oacute;n de servicios @Firma.
     */
    public static Properties getAfirma5ServiceProperties() {
	init();
	return afirma5ServiceInvokerProperties;
    }

    /**
     * Carga explícitamente las propiedades del API de invocaci&oacute;n de servicios @Firma.<br/>
     * Si ocurre alg&uacute;n error devuelve un conjunto de propiedades vac&iacute;o.
     *
     */
    private static synchronized void init() {
	boolean found;
	File file;
	Reader reader;
	InputStream in;
	Iterator<Object> iterator;
	String propertyName, propertyValue;

	try {
	    file = getPropertiesResource();

	    if (propsFileLastUpdate != file.lastModified()) {
		logger.debug(Language.getResIntegra(ILogConstantKeys.ASIP_LOG001));
		afirma5ServiceInvokerProperties = new Properties();
		in = new FileInputStream(file);
		afirma5ServiceInvokerProperties.load(in);
		propsFileLastUpdate = file.lastModified();
		
		logger.debug(Language.getFormatResIntegra(ILogConstantKeys.ASIP_LOG002, new Object[ ] { afirma5ServiceInvokerProperties }));
		logger.debug(Language.getFormatResIntegra(ILogConstantKeys.ASIP_LOG003, new Object[ ] { new Date(propsFileLastUpdate) }));

		// Establecemos el nuevo valor de la propiedad que indica el
		// almacen de confianza usado
		// en conexiones seguras, en el caso de que existan.
		iterator = afirma5ServiceInvokerProperties.keySet().iterator();
		found = false;

		while (iterator.hasNext() && !found) {
		    propertyName = (String) iterator.next();

		    if (propertyName.indexOf(Afirma5ServiceInvokerConstants.SECURE_MODE_PROPERTY) > 0) {
			propertyValue = afirma5ServiceInvokerProperties.getProperty(propertyName);

			if (propertyValue != null && propertyValue.trim().equalsIgnoreCase("true")) {
			    setTruststore(afirma5ServiceInvokerProperties.getProperty(Afirma5ServiceInvokerConstants.COM_PROPERTIE_HEADER + "." + Afirma5ServiceInvokerConstants.WS_TRUSTED_STORE_PROP), afirma5ServiceInvokerProperties.getProperty(Afirma5ServiceInvokerConstants.COM_PROPERTIE_HEADER + "." + Afirma5ServiceInvokerConstants.WS_TRUSTED_STOREPASS_PROP));
			    found = true;
			}
		    }
		}
	    }
	    
	} catch (Exception e) {
	    logger.error(Language.getFormatResIntegra(ILogConstantKeys.ASIP_LOG004, new Object[ ] { Afirma5ServiceInvokerConstants.AFIRMA_SRV_INVOKER_PROP }), e);
	}
    }

    /**
     * Obtiene el fichero de propiedades del API de invocaci&oacute;n de servicios de la plataforma @Firma.<br/>
     * Este fichero se obtiene como recurso del sistema.
     * @return fichero de propiedades del API de invocaci&oacute;n de servicios de la plataforma @Firma.
     * @throws URISyntaxException si la ruta no est&aacute; bien formada.
     */
    private static File getPropertiesResource() throws URISyntaxException {
	
    	File res;
    	//String path = ConfigurationHelper.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA,Afirma5ServiceInvokerConstants.AFIRMA_SRV_INVOKER_PROP);
    	
    	String path = afirmaConfigFilePathResolver.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA + File.separator + Afirma5ServiceInvokerConstants.AFIRMA_SRV_INVOKER_PROP);
    	res = new File(path);

	return res;
    }
    
    
    /**
     * Actualiza la propiedades del sistema que establecen el almac&eacute;n de CA  de confianza a emplear en conexiones seguras
     * si ha cambiado o ha sido modificado.
     * @param truststorePath ruta al almac&eacute;n de CA  de confianza.
     * @param truststorePass contraseña del almac&eacute;n.
     */
    private static synchronized void setTruststore(String truststorePath, String truststorePass) {
	boolean isChanged;
	File truststoreFile;

	isChanged = false;
	truststoreFile = new File(truststorePath);

	if (truststorePath != null && !truststorePath.trim().equals("") && System.getProperty(TRUSTSTORE_CLASSNAME) != null && !System.getProperty(TRUSTSTORE_CLASSNAME).equals(truststorePath)) {
	    logger.debug(Language.getFormatResIntegra(ILogConstantKeys.ASIP_LOG006, new Object[ ] { System.getProperty(TRUSTSTORE_CLASSNAME), truststorePath }));
	    isChanged = true;
	} else if (!GenericUtils.checkNullValues(truststorePass, System.getProperty("javax.net.ssl.trustStorePassword")) && !truststorePass.trim().equals("") && !System.getProperty("javax.net.ssl.trustStorePassword").equals(truststorePass)) {
	    logger.debug(Language.getResIntegra(ILogConstantKeys.ASIP_LOG007));
	    isChanged = true;
	} else if (truststoreLastUpdate != truststoreFile.lastModified()) {
	    logger.debug(Language.getResIntegra(ILogConstantKeys.ASIP_LOG008));
	    isChanged = true;
	}

	if (isChanged) {
	    logger.debug(Language.getFormatResIntegra(ILogConstantKeys.ASIP_LOG009, new Object[ ] { afirma5ServiceInvokerProperties.getProperty(Afirma5ServiceInvokerConstants.COM_PROPERTIE_HEADER + "." + Afirma5ServiceInvokerConstants.WS_TRUSTED_STORE_PROP) }));
	    System.setProperty(TRUSTSTORE_CLASSNAME, truststorePath);
	    System.setProperty("javax.net.ssl.trustStorePassword", truststorePass);
	    truststoreLastUpdate = truststoreFile.lastModified();
	}
    }
}
