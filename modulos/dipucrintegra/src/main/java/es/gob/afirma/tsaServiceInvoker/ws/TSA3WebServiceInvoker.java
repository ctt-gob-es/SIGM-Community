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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.ws.TSA3WebServiceInvoker.java.</p>
 * <b>Description:</b><p>Class that represents the facade used to invoke the web services of TS@ version 3.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>15/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 15/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker.ws;

import java.util.Properties;

import es.gob.afirma.tsaServiceInvoker.AbstractTSAServiceInvoker;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerException;

/**
 * <p>Class that represents the facade used to invoke the web services of TS@ version 3.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 15/01/2014.
 */
public class TSA3WebServiceInvoker extends AbstractTSAServiceInvoker {

    /**
     * Constructor method for the class TSA3WebServiceInvoker.java.
     * @param service Parameter that represents the name of the web service to invoke.
     */
    public TSA3WebServiceInvoker(String service) {
	super(service);
    }

    /**
     * Constructor method for the class TSA3WebServiceInvoker.java.
     * @param service Parameter that represents the name of the web service to invoke.
     * @param serviceInvocationProperties Parameter that represents the set of properties related to the web service to invoke.
     */
    public TSA3WebServiceInvoker(String service, Properties serviceInvocationProperties) {
	super(serviceInvocationProperties, service);
    }

    /**
     * Constructor method for the class TSA3WebServiceInvoker.java.
     * @param applicationName Parameter that represents the name of application which invokes the web service.
     * @param service Parameter that represents the name of the web service to invoke.
     */
    public TSA3WebServiceInvoker(String applicationName, String service) {
	super(service, applicationName);
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.tsaServiceInvoker.AbstractTSAServiceInvoker#invokeService(java.lang.Object[])
     */
    @Override
    public final String invokeService(Object[ ] parameters) throws TSAServiceInvokerException {
	TSAWebServiceInvoker wsInvoker = new TSAWebServiceInvoker(this.getServiceInvocationProperties());
	return (String) wsInvoker.performCall(getService(), parameters);
    }

}
