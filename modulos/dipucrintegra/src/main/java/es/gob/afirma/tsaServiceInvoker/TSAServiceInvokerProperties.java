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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerProperties.java.</p>
 * <b>Description:</b><p>Class that allows to access to the properties defined inside of the configuration file for invoking the web services of TS@.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>15/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 15/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Class that allows to access to the properties defined inside of the configuration file for invoking the web services of TS@.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 15/01/2014.
 */
public final class TSAServiceInvokerProperties {

    /**
     * Constructor method for the class TSAServiceInvokerProperties.java.
     */
    private TSAServiceInvokerProperties() {
    }

    /**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TSAServiceInvokerProperties.class);

    /**
     * Attribute that represents the set of properties defined inside of the configuration file for invoking the web services of TS@.
     */
    private static Properties tsaServiceInvokerProperties = new Properties();

    /**
     * Gets the value of the attribute {@link #tsaServiceInvokerProperties}.
     * @return the value of the attribute {@link #tsaServiceInvokerProperties}.
     */
    public static Properties getTsaServiceInvokerProperties() {
	// Accedemos al archivo de propiedades para la invocación de servicios
	// de TS@
	URL url = TSAServiceInvokerProperties.class.getClassLoader().getResource(TSAServiceInvokerConstants.TSA_INVOKER_PROPERTIES);
	if (url == null) {
	    //LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.TSIP_LOG001, new Object[]{TSAServiceInvokerConstants.TSA_INVOKER_PROPERTIES}));
		try {
			url= new URL("/config/SIGEM/conf/SIGEM_Tramitacion/integra_afirma/tsaServiceInvoker.properties");
		} catch (MalformedURLException e) {
			LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.TSIP_LOG001, new Object[]{TSAServiceInvokerConstants.TSA_INVOKER_PROPERTIES}));
		}		
	}
	
	InputStream in = null;
	try {
		tsaServiceInvokerProperties = new Properties();
		in = new FileInputStream(new File(new URI(url.toString())));
		tsaServiceInvokerProperties.load(in);
	} catch (Exception e) {
		LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.TSIP_LOG002, new Object[]{TSAServiceInvokerConstants.TSA_INVOKER_PROPERTIES}));
	}
	
	return tsaServiceInvokerProperties;
	
    }

}
