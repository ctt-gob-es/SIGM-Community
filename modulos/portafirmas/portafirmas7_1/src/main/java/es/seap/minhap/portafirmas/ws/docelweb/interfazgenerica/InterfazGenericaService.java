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

package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * @author MINHAP
 * InterfazGenerica web service definition extension.
 */
@WebServiceClient(name = "InterfazGenerica", wsdlLocation = "WSDL/InterfazGenerica.wsdl", targetNamespace = "http://ip2.docelweb.wsInterfazGenerica/")
public class InterfazGenericaService extends Service {

	public final static QName SERVICE = new QName("http://ip2.docelweb.wsInterfazGenerica/", "InterfazGenerica");
	public final static QName InterfazGenericaSoapHttpPort = new QName("http://ip2.docelweb.wsInterfazGenerica/", "InterfazGenericaSoapHttpPort");
	public final static QName InterfazGenericaSoap12HttpPort = new QName("http://ip2.docelweb.wsInterfazGenerica/", "InterfazGenericaSoap12HttpPort");
	private static URL urlLocal = InterfazGenericaService.class.getClassLoader().getResource("WSDL/InterfazGenerica.wsdl");

	/**
	 * Web Service constructor with WSDL.
	 */
	public InterfazGenericaService() {
		super(urlLocal, SERVICE);
	}

	/**
	 * Web Service constructor with WSDL.
	 * @param wsdlLocation The WSDL location
	 */
	public InterfazGenericaService(URL wsdlLocation) {
		super(wsdlLocation, SERVICE);
	}

	/**
	 * Gets the connector SOAP v1.1 for Web Services InterfazGenerica. 
	 * @return
	 *     returns InterfazGenerica
	 */
	@WebEndpoint(name = "InterfazGenericaSoapHttpPort")
	public InterfazGenerica getInterfazGenericaSoapHttpPort() {
		return super.getPort(InterfazGenericaSoapHttpPort, InterfazGenerica.class);
	}

	/**
	 * Gets the connector SOAP v1.1 for Web Services InterfazGenerica by the WSDL location.
	 * @param features
	 *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
	 * @return
	 *     returns InterfazGenerica
	 */
	@WebEndpoint(name = "InterfazGenericaSoapHttpPort")
	public InterfazGenerica getInterfazGenericaSoapHttpPort(WebServiceFeature... features) {
		return super.getPort(InterfazGenericaSoapHttpPort, InterfazGenerica.class, features);
	}

	/**
	 * Gets the connector SOAP v1.2 for Web Services InterfazGenerica. 
	 * @return
	 *     returns InterfazGenerica
	 */
	@WebEndpoint(name = "InterfazGenericaSoap12HttpPort")
	public InterfazGenerica getInterfazGenericaSoap12HttpPort() {
		return super.getPort(InterfazGenericaSoap12HttpPort, InterfazGenerica.class);
	}

	/**
	 * Gets the connector SOAP v1.2 for Web Services InterfazGenerica by the WSDL location.
	 * @param features
	 *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
	 * @return
	 *     returns InterfazGenerica
	 */
	@WebEndpoint(name = "InterfazGenericaSoap12HttpPort")
	public InterfazGenerica getInterfazGenericaSoap12HttpPort(WebServiceFeature... features) {
		return super.getPort(InterfazGenericaSoap12HttpPort, InterfazGenerica.class, features);
	}

}
