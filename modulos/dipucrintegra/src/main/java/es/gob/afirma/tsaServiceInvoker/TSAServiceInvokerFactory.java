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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerFactory.java.</p>
 * <b>Description:</b><p>Class that represents a factory to invoke web services published by TS@.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>09/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 09/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.tsaServiceInvoker.ws.TSA3WebServiceInvoker;

/**
 * <p>Class that represents a factory to invoke web services published by TS@.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 09/01/2014.
 */
public final class TSAServiceInvokerFactory {

    /**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TSAServiceInvokerFactory.class);

    /**
     * Constructor method for the class TSAServiceInvokerFactory.java.
     */
    private TSAServiceInvokerFactory() {
    }

    /**
     * Method that obtains the class used to invoke a TS@ web service.
     * @param applicationName Parameter that represents the name of the client application.
     * @param service Parameter that represents the name of the web service.
     * @return the class used to invoke a TS@ web service.
     * @throws TSAServiceInvokerException if the method fails.
     */
    public static AbstractTSAServiceInvoker getTSAServiceInvoker(String applicationName, String service) throws TSAServiceInvokerException {
	AbstractTSAServiceInvoker res = new TSA3WebServiceInvoker(applicationName, service);
	String className = "";
	if (res != null && res.getClass() != null) {
	    className = res.getClass().getName();
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TSIF_LOG001, new Object[ ] { service, className }));
	return res;
    }
}
