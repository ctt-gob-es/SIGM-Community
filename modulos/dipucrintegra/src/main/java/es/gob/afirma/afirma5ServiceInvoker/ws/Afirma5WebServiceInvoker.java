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

package es.gob.afirma.afirma5ServiceInvoker.ws;

import java.util.Properties;

import es.gob.afirma.afirma5ServiceInvoker.AbstractAfirma5ServiceInvoker;
import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerException;

/**
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 23/03/2011.
 */
public class Afirma5WebServiceInvoker extends AbstractAfirma5ServiceInvoker {


	/**
	 * Constructor method for the class Afirma5WebServiceInvoker.java.
	 * @param service service
	 */
	public Afirma5WebServiceInvoker(String service) {
		super(service);
	}

	/**
	 * Constructor method for the class Afirma5WebServiceInvoker.java.
	 * @param service service
	 * @param serviceInvocationProperties serviceInvocationProperties
	 */
	public Afirma5WebServiceInvoker(String service, Properties serviceInvocationProperties) {
		super(service,serviceInvocationProperties);
	}

	/**
	 * Constructor method for the class Afirma5WebServiceInvoker.java.
	 * @param applicationName applicationName
	 * @param service service
	 */
	public Afirma5WebServiceInvoker(String applicationName, String service) {
		super(applicationName,service);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.afirma.afirma5ServiceInvoker.AbstractAfirma5ServiceInvoker#invokeService(java.lang.String, java.lang.Object[])
	 */
	public final String invokeService(String methodName, Object [] parameters) throws Afirma5ServiceInvokerException {
		WebServiceInvoker 	wsInvoker = new WebServiceInvoker(this.getServiceInvocationProperties());

		return  (String) wsInvoker.performCall(methodName, parameters);

	}

}
