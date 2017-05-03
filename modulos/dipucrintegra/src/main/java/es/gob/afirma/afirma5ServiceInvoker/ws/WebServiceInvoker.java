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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.ConfigurationException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerConstants;
import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerException;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.utils.GenericUtils;

/**
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 23/03/2011.
 */
public class WebServiceInvoker {

    /**
     * Attribute that represents .
     */
    private static final String NO_SECURE_PROTOCOL = "http";
    /**
     * Attribute that represents .
     */
    private static final String SECURE_PROTOCOL = "https";

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(WebServiceInvoker.class);

    /**
     * Almacena las propiedades de comunicaci&oacute;n con el servicio web de @Firma Extensión que realiza la validaci&oacute;n.
     */
    private Properties properties;

    /**
     * Constructor de la clase.
     * @param prop Propiedades de configuraci&oacute;n de la llamada al servicio web.
     */
    public WebServiceInvoker(Properties prop) {
	this.properties = prop;
    }

    /**
     * Realiza una llamada mediante SOAP a un servicio web publicado en el n&uacute;cleo de firma.
     * @param methodName nombre del m&eacute;todo del servicio Web a invocar.
     * @param params parámetros del m&eacute;todo del servicio Web.
     * @return Resultado de realizar la llamada
     * @throws Afirma5ServiceInvokerException si ocurre alg&uacute;n error.
     * @throws AxisFault 
     */
    final Object performCall(String methodName, Object[ ] params) throws Afirma5ServiceInvokerException {
	Call call = null;
	ClientHandler requestHandler;
	Service service;
	String endPointURL, securityOption, secureMode, propertiesHeader, timeout, applicationName, afirmaService, protocol, endPoint, servicePath;
	Object res;

	res = null;
	try {

	    applicationName = this.properties.getProperty(Afirma5ServiceInvokerConstants.APPLICATION_NAME);
	    afirmaService = this.properties.getProperty(Afirma5ServiceInvokerConstants.AFIRMA_SERVICE);
	    // Calculamos la cabecera para obtener las propiedades para
	    // habilitar la posibilidad de invocar un servicio
	    // dado un conjunto de propiedades sin requerir que estas residan en
	    // el archivo de configuracion
	    propertiesHeader = applicationName == null ? "" : applicationName + ".";

	    secureMode = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.SECURE_MODE_PROPERTY);

	    // Propiedades de conexión con el repositorio de servicios Web
	    protocol = NO_SECURE_PROTOCOL;
	    if (secureMode != null && secureMode.equals("true")) {
		protocol = SECURE_PROTOCOL;
	    }
	    endPoint = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_ENDPOINT_PROPERTY);
	    checkSvcInvokerParams(Afirma5ServiceInvokerConstants.WS_ENDPOINT_PROPERTY, endPoint);
	    servicePath = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_SRV_PATH_PROPERTY);
	    checkSvcInvokerParams(Afirma5ServiceInvokerConstants.WS_SRV_PATH_PROPERTY, servicePath);

	    // "https://localhost:8080/afirmaws/services/ValidarCertificado";
	    endPointURL = protocol + "://" + endPoint + "/" + servicePath + "/" + afirmaService;

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG001, new Object[ ] { afirmaService }));
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG002, new Object[ ] { endPointURL }));

	    // Propiedades propias del servicio web a invocar

	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.WSI_LOG003));
	    // Creacion del manejador que securizará la petición SOAP
	    // validacionWS.ValidarCertificado.ws.authorizationMethod
	    securityOption = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP);
	    checkSvcInvokerParams(Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP, securityOption);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG004, new Object[ ] { securityOption }));
	    requestHandler = newRequestHandler(securityOption, propertiesHeader);

	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.WSI_LOG005));
	    // Creación del servicio y la llamada al método
	    service = new Service();
	    service.getEngine().cleanup();//refreshGlobalOptions();
	    call = (Call) service.createCall();
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.WSI_LOG006));

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG007, new Object[ ] { methodName }));
	    // Configuración de la llamada
	    call.setTargetEndpointAddress(endPointURL);

	    call.setOperationName(new QName("http://soapinterop.org/", methodName));
	    timeout = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_CALL_TIMEOUT_PROP);
	    checkSvcInvokerParams(Afirma5ServiceInvokerConstants.WS_CALL_TIMEOUT_PROP, timeout);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG008, new Object[ ] { timeout }));
	    call.setTimeout(Integer.valueOf(timeout));

	    // incluimos los manejadores de entrada (cabecera de seguridad en
	    // las peticiones)
	    // y los manejadores de salida (validación de las respuestas
	    // firmadas de los servicios @firma)
	    call.setClientHandlers(requestHandler, newResponseHandler(propertiesHeader));

	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.WSI_LOG009));
	    // Llamada al metodo del servicio web
	    
	    if (params == null) {
		res = call.invoke(new Object[0]);
	    } else {
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG010, new Object[ ] { params[0] }));
		res = call.invoke(params);
	    }
		
	    
	} catch (RemoteException e) {
		try {
			imprimirErrorEnvio(call);
		} catch (AxisFault e1) {
			LOGGER.error("Error en el envío. "+e1.getMessage());
			e1.printStackTrace();
		}
	    throw new Afirma5ServiceInvokerException(e);
	} catch (ServiceException e) {
		try {
			imprimirErrorEnvio(call);
		} catch (AxisFault e1) {
			LOGGER.error("Error en el envío. "+e1.getMessage());
			e1.printStackTrace();
		}
	    throw new Afirma5ServiceInvokerException(e);
	}

	Object result = "Null.";
	if (res != null) {
	    result = res;
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG011, new Object[ ] { result }));

	return res;
    }

    /**
     * Checks invoker parameters service.
     * @param parameterName parameter name.
     * @param parameterValue parameter value
     * @throws Afirma5ServiceInvokerException if an error happens.
     */
    private void checkSvcInvokerParams(String parameterName, String parameterValue) throws Afirma5ServiceInvokerException {
	if (!GenericUtils.assertStringValue(parameterValue)) {
	    throw new Afirma5ServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG012, new Object[ ] { parameterName, properties.getProperty(Afirma5ServiceInvokerConstants.APPLICATION_NAME) }));
	}
    }

    /**
     * Creates a new instance <code>ClientHandler</code>.
     * @param securityOption security options
     * @param propertiesHeader properties of header.
     * @return a new instance <code>ClientHandler</code>.
     * @throws Afirma5ServiceInvokerException if a error happens.
     */
    private ClientHandler newRequestHandler(String securityOption, String propertiesHeader) throws Afirma5ServiceInvokerException {
	String autUser, autPassword, autPassType, keystorePath, keystorePass, keystoreType;
	autUser = "";
	autPassword = "";
	autPassType = "";
	keystorePath = "";
	keystorePass = "";
	keystoreType = "";
	String securityOpt = securityOption;
	if (securityOpt != null) {
	    if (!securityOpt.equals(ClientHandler.NONEOPTION)) {
		autUser = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZ_METHOD_USER_PROP);
		autPassword = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PASS_PROP);
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG013, new Object[ ] { autUser }));
		if (securityOpt.equals(ClientHandler.USERNAMEOPTION)) {
		    autPassType = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PASS_TYPE_PROP);
		    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG014, new Object[ ] { autPassType }));
		} else if (securityOpt.equals(ClientHandler.CERTIFICATEOPTION)) {
		    keystorePath = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_USERKEYSTORE_PROP);
		    keystorePass = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_USERKEYSTORE_PASS_PROP);
		    keystoreType = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_USERKEYSTORE_TYPE_PROP);
		    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG015, new Object[ ] { keystorePath, keystoreType }));
		}
	    }
	} else {
	    securityOpt = ClientHandler.NONEOPTION;
	}
	ClientHandler sender = new ClientHandler(securityOpt);
	sender.setUserAlias(autUser);
	sender.setPassword(autPassword);
	// Este parametro solo tiene sentido si la autorizacion se realiza
	// mediante el tag de seguridad UserNameToken
	sender.setPasswordType(autPassType);
	// Propiedades para binarySecurityToken
	sender.setUserKeystore(keystorePath);
	sender.setUserKeystorePass(keystorePass);
	sender.setUserKeystoreType(keystoreType);
	return sender;
    }

    /**
     * creates a new instance of <code>ResponseHandler</code>.
     * @param propertiesHeader properties of header.
     * @return a new instance of <code>ResponseHandler</code>.
     */
    private ResponseHandler newResponseHandler(String propertiesHeader) {
	if (Boolean.valueOf(this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.PREFIX_RESPONSE_PROPERTY + "." + Afirma5ServiceInvokerConstants.RESPONSE_VALIDATE_PROPERTY))) {
	    String keystorePath = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_USERKEYSTORE_PROP);
	    String keystorePass = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_USERKEYSTORE_PASS_PROP);
	    String keystoreType = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_USERKEYSTORE_TYPE_PROP);
	    String autUser = this.properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.PREFIX_RESPONSE_PROPERTY + "." + Afirma5ServiceInvokerConstants.RESPONSE_ALIAS_CERT_PROPERTY);
	    String autPassword = properties.getProperty(propertiesHeader + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROP + "." + Afirma5ServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PASS_PROP);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.WSI_LOG016, new Object[ ] { keystorePath, keystoreType, autUser }));
	    return new ResponseHandler(keystorePath, keystorePass, keystoreType, autUser, autPassword);
	} else {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.WSI_LOG017));
	    return null;
	}
    }

    /**
     * 
     * @return Properties.
     */
    public final Properties getWSCallerProperties() {
	return this.properties;
    }

    /**
     * 
     * @param wsProperties wsProperties
     */
    public final void setWSCallerProperties(Properties wsProperties) {
	this.properties = wsProperties == null ? new Properties() : wsProperties;
    }
    
	public static void imprimirErrorEnvio(org.apache.axis.client.Call _call) throws AxisFault {
		
		FileWriter fileWriter = null;
        try {
			String requestXML = _call.getMessageContext().getRequestMessage().getSOAPPartAsString();
			String responseXML = _call.getMessageContext().getResponseMessage().getSOAPPartAsString();
			LOGGER.error("REQUEST");
			LOGGER.error(requestXML);
			LOGGER.error("RESPONSE");
			LOGGER.error(responseXML);
			
			File newTextFile = new File("request.txt");
            fileWriter = new FileWriter(newTextFile);
            fileWriter.write(requestXML);
            fileWriter.close();
            
            File newTextFile1 = new File("response.txt");
            fileWriter = new FileWriter(newTextFile1);           
            fileWriter.write(responseXML);
            fileWriter.close();   
			
        } catch (IOException ex) {
           System.out.println("Hola");
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
            	 System.out.println("Hola");
            }
        }
		
	}

}
