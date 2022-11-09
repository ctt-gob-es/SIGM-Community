/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * @author MINHAP
 * NotificacionGenericaWS web service definition extension.
 */
@WebServiceClient(name = "NotificacionGenericaWS", wsdlLocation = "WSDL/NotificacionGenericaWS.wsdl", targetNamespace = "http://ip2/docelweb/proceso/notificacionGenerica/NotificacionGenericaWSImpl.wsdl")
public class NotificacionGenericaWSService extends Service {

	public final static QName SERVICE = new QName("http://ip2/docelweb/proceso/notificacionGenerica/NotificacionGenericaWSImpl.wsdl", "NotificacionGenericaWS");
	public final static QName NotificacionGenericaWSImplBindingPort = new QName("http://ip2/docelweb/proceso/notificacionGenerica/NotificacionGenericaWSImpl.wsdl", "NotificacionGenericaWSImplBindingPort");
	private static URL urlLocal = NotificacionGenericaWSService.class.getClassLoader().getResource("WSDL/NotificacionGenericaWS.wsdl");

	/**
	 * Web Service constructor with WSDL.
	 */
	public NotificacionGenericaWSService() {
		super(urlLocal, SERVICE);
	}

    /**
	 * Web Service constructor with WSDL.
	 * @param wsdlLocation The WSDL location
	 */
	public NotificacionGenericaWSService(URL wsdlLocation) {
		super(wsdlLocation, SERVICE);
	}

	/**
	 * Gets the connector SOAP for Web Services NotificacionGenericaWS.
	 * @return
	 *     returns NotificacionGenericaWS
	 */
	@WebEndpoint(name = "NotificacionGenericaWSImplBindingPort")
	public NotificacionGenericaWS getNotificacionGenericaWSPort() {
		return super.getPort(NotificacionGenericaWSImplBindingPort, NotificacionGenericaWS.class);
	}

	/**
	 * Gets the connector SOAP for Web Services NotificacionGenericaWS by WSDL location.
	 * @param features
	 *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
	 * @return
	 *     returns NotificacionGenericaWS
	 */
	@WebEndpoint(name = "NotificacionGenericaWSImplBindingPort")
	public NotificacionGenericaWS getNotificacionGenericaWSPort(WebServiceFeature... features) {
		return super.getPort(NotificacionGenericaWSImplBindingPort, NotificacionGenericaWS.class, features);
	}

}
