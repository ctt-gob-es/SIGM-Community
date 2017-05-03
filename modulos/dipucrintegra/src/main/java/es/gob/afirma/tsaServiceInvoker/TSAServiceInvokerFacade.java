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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerFacade.java.</p>
 * <b>Description:</b><p>Class that represents the facade to invoke the web services of TS@.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>09/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 09/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.utils.GenericUtils;

/**
 * <p>Class that represents the facade to invoke the web services of TS@.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 09/01/2014.
 */
public final class TSAServiceInvokerFacade {

    /**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TSAServiceInvokerFacade.class);

    /**
     * Attribute that represents the unique instance of the class.
     */
    private static TSAServiceInvokerFacade instance;

    /**
     * Constructor method for the class TSAServiceInvokerFacade.java.
     */
    private TSAServiceInvokerFacade() {
    }

    /**
     * Method that obtains the unique instance of the class.
     * @return the unique instance of the class.
     */
    public static TSAServiceInvokerFacade getInstance() {
	if (instance == null) {
	    instance = new TSAServiceInvokerFacade();
	}
	return instance;
    }

    /**
     * Method that invokes a web service of TS@.
     * @param xmlInput Parameter that represents the input parameters of the web service.
     * @param service Parameter that represents the name of the web service.
     * @param applicationName Parameter that represents the name of the client application.
     * @return the result of the invocation of the service on XML format.
     * @throws TSAServiceInvokerException if the method fails.
     */
    public String invokeService(String xmlInput, String service, String applicationName) throws TSAServiceInvokerException {
	// Comprobamos que los parámetros de entrada no son nulos
	if (!GenericUtils.assertStringValue(xmlInput) || !GenericUtils.assertStringValue(service) || !GenericUtils.assertStringValue(applicationName)) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.TSI_LOG001));
	}
	AbstractTSAServiceInvoker tsaInvoker;
	Object[ ] serviceInParam;
	String res = null;
	try {
	    serviceInParam = new Object[1];
	    serviceInParam[0] = xmlInput;
	    tsaInvoker = TSAServiceInvokerFactory.getTSAServiceInvoker(applicationName, service);
	    res = tsaInvoker.invokeService(serviceInParam);
	} catch (Exception e) {
	    LOGGER.error(e);
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TSI_LOG002, new Object[ ] { service }), e);
	}
	return res;
    }

}
