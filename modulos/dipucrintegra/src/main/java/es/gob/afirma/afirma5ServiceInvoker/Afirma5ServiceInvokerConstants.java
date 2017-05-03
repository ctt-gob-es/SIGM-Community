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

/**
 * Interfaz que contiene las constantes utilizadas por el componente afirma5ServiceInvoker.
 * @author sepaot
 *
 */
public interface Afirma5ServiceInvokerConstants {
	//Fichero de propiedades del componente afirma5ServiceInvoker, debe estar colocado en algún directorio de los
	//contenidos en la variable de entorno classpath
	/**
	 * Nombre del fichero de configuraci&oacute;n del API afirma5ServiceInvoker.
	 */
	String AFIRMA_SRV_INVOKER_PROP = "afirma5ServiceInvoker.properties";

	//Propiedades para la realización de llamadas a servicios Web, EJB e invocación
	/**
	 * Nombre de la propiedad que indica el m&eacute;todo de invocaci&oacute;n empleado para un contexto de aplicaci&oacute;n y un servicio determinado.
	 */
	String COM_TYPE_PROPERTIE = "comType";

	/**
	 * Cabecera empleada en el fichero de propiedades para indicar propiedades gen&eacute;ricas de la comunicaci&oacute;n.
	 */
	String COM_PROPERTIE_HEADER = "com";

	/**
	 * Protocolo de aplicaci&oacute;n SOAP.
	 */
	String WS_PROPERTIE_HEADER = "ws";

	//Propiedades WS
	/**
	 * Propiedad que indica donde pueden localizarse los Servicios Web publicados.
	 */
	String WS_ENDPOINT_PROPERTY = "endPoint";

	/**
	 * Propiedad que indica la ruta llegar a los Servicios Web publicados.
	 */
	String WS_SRV_PATH_PROPERTY = "servicePath";

	/**
	 * Propiedad que indica el almac&eacute;n de certificados de confianza empleado.
	 */
	String WS_TRUSTED_STORE_PROP = "trustedstore";

	/**
	 * Propiedad que indica el password del almac&eacute;n de certificados de confianza empleado.
	 */
	String WS_TRUSTED_STOREPASS_PROP = "trustedstorepassword";

	/**
	 * Propiedad que indica el tiempo de vida de una llamada SOAP.
	 */
	String WS_CALL_TIMEOUT_PROP = "callTimeout";

	/**
	 * Propiedad que indica el identificador de aplicaci&oacute;n @Firma usado en la llamada.
	 */
	String WS_APP_WSAPP_ID_PROP = "appId";

	/**
	 * Propiedad que indica el m&eacute;todo de autorizaci&oacute;n de ejecuci&oacute;n SOAP empleado. Posibles
	 * valores: none, UsernameToken y BinarySecurityToken.
	 */
	String WS_AUTHORIZATION_METHOD_PROP = "authorizationMethod";

	/**
	 * Propiedad que indica el usuario / alias del certificado empleado en los m&eacute;todos de autorizaci&oacute;n
	 * UsernameToken / BinarySecurityToken.
	 */
	String WS_AUTHORIZ_METHOD_USER_PROP = "user";

	/**
	 * Propiedad que indica la contraseña del usuario / alias certificado empleado en los m&eacute;todos de autorizaci&oacute;n
	 * UsernameToken / BinarySecurityToken.
	 */
	String WS_AUTHORIZATION_METHOD_PASS_PROP = "password";

	/**
	 * Propiedad que indica como viaja la contraseña de usuario en el m&eacute;todo de autorizaci&oacute;n
	 * UsernameToken. Posibles valores: clear y digest.
	 */
	String WS_AUTHORIZATION_METHOD_PASS_TYPE_PROP = "passwordType";

	/**
	 * Propiedad que indica la ruta del almac&eacute;n de certificados empleado por el usuario para el m&eacute;todo de autorizaci&oacute;n
	 * BinarySecurityToken.
	 */
	String WS_AUTHORIZATION_METHOD_USERKEYSTORE_PROP = "userKeystore";

	/**
	 * Propiedad que indica la contraseña del almac&eacute;n de certificados empleado por el usuario para el m&eacute;todo de autorizaci&oacute;n
	 * BinarySecurityToken.
	 */
	String WS_AUTHORIZATION_METHOD_USERKEYSTORE_PASS_PROP = "userKeystorePassword";

	/**
	 * Propiedad que indica el tipo del almac&eacute;n de certificados empleado por el usuario para el m&eacute;todo de autorizaci&oacute;n
	 * BinarySecurityToken.
	 */
	String WS_AUTHORIZATION_METHOD_USERKEYSTORE_TYPE_PROP = "userKeystoreType";

	/**
	 * Constante usada para indicar el contexto de aplicaci&oacute;n a las clases encargadas de ejecutar el servicio demandado.
	 */
	String APPLICATION_NAME = "applicationName";

	/**
	 * Constante usada para indicar el servicio demandado a las clases encargadas de realizar la llamada.
	 */
	String AFIRMA_SERVICE = "afirmaService";

	//Propiedades EJB
	/**
	 * Propiedad que indica el nombre JNDI del EJB.
	 */
	String BEAN_JNDI_NAME = "jndiName";

	//Propiedades invocación clase
	/**
	 * Propiedad que indica la clase invocadora del servicio.
	 */
	String CLASS_PROPERTY = "class";

	/**
	 * Propiedad que indica si el establecimiento de la comunicaci&oacute;n se encuentra securizado mediante SSL.
	 */
	String SECURE_MODE_PROPERTY = "secureMode";
	
	/**
	 * Attribute that represents name of property that indicates if the response is validated. 
	 */
	String RESPONSE_VALIDATE_PROPERTY = "validate";

	/**
	 * Attribute that represents name of property for define alias of certificate. 
	 */
	String RESPONSE_ALIAS_CERT_PROPERTY = "certificateAlias";
	
	/**
	 * Attribute that represents prefix used in the response of the webservices. 
	 */
	String PREFIX_RESPONSE_PROPERTY = "response";
	
	/**
	 * Constant attribute that identifies the key which indicates if to use the certificates validation responses cache (true) or not (false).
	 */
	String WS_CERTIFICATES_CACHE_USE_PROP = "com.certificatesCache.use";
	
	/**
	 * Constant attribute that identifies the key for the entries number of the certificates validation responses cache.
	 */
	String WS_CERTIFICATES_CACHE_ENTRIES_PROP = "com.certificatesCache.entries";
	
	/**
	 * Constant attribute that identifies the key for the life time of the certificates validation responses cache, in seconds.
	 */
	String WS_CERTIFICATES_CACHE_LIFETIME_PROP = "com.certificatesCache.lifeTime";
}
