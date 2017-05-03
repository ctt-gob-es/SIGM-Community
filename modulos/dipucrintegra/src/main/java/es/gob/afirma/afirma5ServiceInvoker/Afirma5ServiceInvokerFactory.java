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

import java.util.Properties;

import org.apache.log4j.Logger;

import es.gob.afirma.afirma5ServiceInvoker.ws.Afirma5WebServiceInvoker;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * Esta clase implementa una f&aacute;brica de invocaci&oacute;n de servicios publicados por la plataforma @Firma. Los modos
 * actualmente permitidos son:
 * <ul>
 * 	<li>Servicios Web.</li>

 * </ul>
 * @author SEPAOT
 *
 */
// * <li>EJB locales.</li>
// * <li>Invocaci&oacute;n directa de clase.</li>
public final class Afirma5ServiceInvokerFactory {

    /**
     * Constructor method for the class Afirma5ServiceInvokerFactory.java.
     */
    private Afirma5ServiceInvokerFactory() {
    }

    /**
     *  Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(Afirma5ServiceInvokerFactory.class);

    /**
     * Obtiene la clase encargada de realizar la llamada a un servicio de la plataforma @Firma.
     * @param service servicio solicitado.
     * @return clase que extiende la clase AbstractAfirma5ServiceInvoker encargada de invocar un servicio de la plataforma @Firma
     * mediante uno de los modos permitidos (EJB local, Web services, invocaci&oacute;n de clase).
     * @throws Afirma5ServiceInvokerException si ocurre algún error al instanciar la clase encargada de invocar el servicio.
     */
    public static AbstractAfirma5ServiceInvoker getAfirma5ServiceInvoker(String service) throws Afirma5ServiceInvokerException {
	AbstractAfirma5ServiceInvoker res = new Afirma5WebServiceInvoker(service);
	String className = "";
	if (res != null) {
	    className = res.getClass().getName();
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.ASF_LOG001, new Object[ ] { service, className }));
	return res;
    }

    /**
     * Obtiene la clase encargada de realizar la llamada a un servicio de la plataforma @Firma.
     * @param service servicio solicitado.
     * @param serviceInvocationProperties propiedades de invocaci&oacute;n al servicio.
     * @return clase que extiende la clase AbstractAfirma5ServiceInvoker encargada de invocar un servicio de la plataforma @Firma
     * mediante uno de los modos permitidos (EJB local, Web services, invocaci&oacute;n de clase).
     * @throws Afirma5ServiceInvokerException si ocurre algún error al instanciar la clase encargada de invocar el servicio.
     */
    public static AbstractAfirma5ServiceInvoker getAfirma5ServiceInvoker(String service, Properties serviceInvocationProperties) throws Afirma5ServiceInvokerException {
	AbstractAfirma5ServiceInvoker res = new Afirma5WebServiceInvoker(service, serviceInvocationProperties);
	String className = "";
	if (res != null) {
	    className = res.getClass().getName();
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.ASF_LOG001, new Object[ ] { service, className }));
	return res;
    }

    /**
     * Obtiene la clase encargada de realizar la llamada a un servicio de la plataforma @Firma.
     * @param service servicio solicitado.
     * @param applicationName applicationName
     * @return clase que extiende la clase AbstractAfirma5ServiceInvoker encargada de invocar un servicio de la plataforma @Firma
     * mediante uno de los modos permitidos (EJB local, Web services, invocaci&oacute;n de clase).
     * @throws Afirma5ServiceInvokerException si ocurre algún error al instanciar la clase encargada de invocar el servicio.
     */
    public static AbstractAfirma5ServiceInvoker getAfirma5ServiceInvoker(String applicationName, String service) throws Afirma5ServiceInvokerException {
	AbstractAfirma5ServiceInvoker res = new Afirma5WebServiceInvoker(applicationName, service);
	String className = "";
	if (res != null) {
	    className = res.getClass().getName();
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.ASF_LOG001, new Object[ ] { service, className }));
	return res;
    }

}
