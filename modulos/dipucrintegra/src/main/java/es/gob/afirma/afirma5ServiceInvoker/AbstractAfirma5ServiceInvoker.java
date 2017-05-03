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

/**
 * Clase abstracta de la cual heredan aquellas clases encargadas de la invocaci&oacute;n de servicios publicados por la
 * plataforma @Firma.<br/><br/>
 * Hasta el momento, estos servicios invocados mediante sus interfaces Web Services, sus interfaces EJB locales o
 * invocaci&oacute;n directa de la clase que contiene la l&oacute;gica.
 * 
 * @author SEPAOT
 *
 */
public abstract class AbstractAfirma5ServiceInvoker {

    /**
     * Par&aacute;metros de configuraci&oacute;n del servicio a invocar.
     */
    private Properties serviceInvocationProperties;

    /**
     * Servicio a invocar.
     */
    private String service;

    /**
     * Nombre de aplicaci&oacute;n solicitante del servicio. Utilizado para acceder a los par&aacute;metros de invocaci&oacute;n
     * propios de cada aplicaci&oacute;n. En caso de que se desee invocar un servicio mediante un conjunto de propiedades no establecido
     * en el fichero de configuraci&oacute;n, este atributo deber&aacute; tener un valor <strong>null</strong>.
     */
    private String applicationName;

    /**
     * Inicializa los atributos de la clase.
     * @param svc Servicio a invocar. Por ejemplo ValidarCertificado.
     */
    public AbstractAfirma5ServiceInvoker(String svc) {
	this.applicationName = null;
	this.service = svc;
	this.serviceInvocationProperties = new Properties();
	this.serviceInvocationProperties.setProperty(Afirma5ServiceInvokerConstants.AFIRMA_SERVICE, this.service);
    }

    /**
     * Inicializa los atributos de la clase.
     * @param svc Servicio a invocar. Por ejemplo ValidarCertificado.
     * @param svcInvProperties Conjunto de propiedades requeridas para la invocaci&oacute;n del servicio.
     */
    public AbstractAfirma5ServiceInvoker(String svc, Properties svcInvProperties) {
	this.applicationName = null;
	this.service = svc;
	this.serviceInvocationProperties = new Properties(svcInvProperties);
	this.serviceInvocationProperties.setProperty(Afirma5ServiceInvokerConstants.AFIRMA_SERVICE, this.service);
    }

    /**
     * Inicializa los atributos de la clase.
     * @param appName Contexto de aplicaci&oacute;n solicitante del servicio.
     * @param svc Servicio a invocar. Por ejemplo ValidarCertificado.
     */
    public AbstractAfirma5ServiceInvoker(String appName, String svc) {
	this.applicationName = appName;
	this.service = svc;
	initializeProperties();
    }

    /**
     * Inicializa los par&aacute;metros de funcionamiento generales del API de invocaci&oacute;n de servicios @Firma
     * a partir del archivo de configuraci&oacute;n del componente.
     *
     */
    private void initializeProperties() {
	this.serviceInvocationProperties = Afirma5ServiceInvokerProperties.getAfirma5ServiceProperties();
	this.serviceInvocationProperties.setProperty(Afirma5ServiceInvokerConstants.APPLICATION_NAME, this.applicationName);
	this.serviceInvocationProperties.setProperty(Afirma5ServiceInvokerConstants.AFIRMA_SERVICE, this.service);
    }

    /**
     * Invoca un m&eacute;todo de un servicio publicado por la plataforma @Firma.
     * @param methodName nombre del m&eacute;todo a invocar.
     * @param parameters valores de los par&aacute;metros que debe recibir el m&eacute;todo a invocar.
     * @return resultado de invocar el m&eacute;todo del servicio solicitado. Todos los servicios de la plataforma
     * devuelven una cadena con formato XML.
     * @throws Afirma5ServiceInvokerException si ocurrio alg&uacute;n error.
     */
    public abstract String invokeService(String methodName, Object[ ] parameters) throws Afirma5ServiceInvokerException;

    /**
     * Obtiene el nombre del servicio para el cual fue configurado el invocador.
     * @return Servicio a invocar. Por ejemplo ValidarCertificado.
     */
    public final String getService() {
	return this.service;
    }

    /**
     * Obtiene el nombre de aplicación mediante el cual fue configurado el invocador.
     * @return Nombre de aplicaci&oacute;n solicitante del servicio. En caso de invocar
     * el servicio mediante un conjunto de propiedades no establecido en el fichero de configuraci&oacute;n
     * retornar&aacute; null.
     */
    public final String getApplicationName() {
	return this.applicationName;
    }

    /**
     * Obtiene todos los par&aacute;metros de configuraci&oacute;n del invocador. Estos par&aacute;metros pueden
     * ser extraidos de un archivo de configuraci&oacute;n o establecidos directamente por el integrador.
     * @return los par&aacute;metros de configuraci&oacute;n del invocador.
     */
    public final Properties getServiceInvocationProperties() {
	return this.serviceInvocationProperties;
    }

    /**
     * Establece un nuevo conjunto de par&aacute;metros de configuraci&oacute;n para el invocador.
     * @param srvInvokeProp serviceInvocationProperties
     */
    public final void setServiceInvocationProperties(Properties srvInvokeProp) {
	this.serviceInvocationProperties = srvInvokeProp == null ? new Properties() : new Properties(srvInvokeProp);

	this.serviceInvocationProperties.setProperty(Afirma5ServiceInvokerConstants.AFIRMA_SERVICE, this.service);
    }
}
