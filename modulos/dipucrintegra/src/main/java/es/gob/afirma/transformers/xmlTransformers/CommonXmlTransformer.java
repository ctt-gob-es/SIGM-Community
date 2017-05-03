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

package es.gob.afirma.transformers.xmlTransformers;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.transformers.TransformersProperties;
import es.gob.afirma.utils.GenericUtils;
import es.gob.afirma.utils.UtilsXML;

/**
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 22/03/2011.
 */
public class CommonXmlTransformer implements IXmlTransformer {

    /**
     * Attribute that represents .
     */
    private static Logger logger = Logger.getLogger(CommonXmlTransformer.class);

    /**
     * Attribute that represents .
     */
    private String service = null;
    /**
     * Attribute that represents .
     */
    private String type = null;
    /**
     * Attribute that represents .
     */
    private String messageVersion = null;

    /**
     * Attribute that represents method of webService.
     */
    private String method;

    // Necesario para utilizar el patrón reflexión
    /**
     * Constructor method for the class CommonXmlTransformer.java.
     */
    public CommonXmlTransformer() {
	this(null, null, null, null);
    }

    /**
     * Constructor method for the class CommonXmlTransformer.java.
     * @param svc svc
     * @param methodParam method name of WS.
     * @param typ typ
     * @param msgVersion msgVersion
     */
    public CommonXmlTransformer(String svc, String methodParam, String typ, String msgVersion) {
	service = svc;
	method = methodParam;
	type = typ;
	messageVersion = msgVersion;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.xmlTransformers.IXmlTransformer#getService()
     */
    public final String getService() {
	return service;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.xmlTransformers.IXmlTransformer#getType()
     */
    public final String getType() {
	return type;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.xmlTransformers.IXmlTransformer#getMessageVersion()
     */
    public final String getMessageVersion() {
	return messageVersion;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.xmlTransformers.IXmlTransformer#transform(java.lang.Object)
     */
    public final Object transform(Object params) throws TransformersException {
	Document doc;
	Object res = null;

	if (messageVersion.equals(TransformersConstants.VERSION_10)) {
	    doc = TransformersFacade.getInstance().getXmlRequestFileByRequestType(service, method, type, messageVersion);
	    logger.debug(Language.getFormatResIntegra(ILogConstantKeys.CXT_LOG001, new Object[ ] { service, messageVersion }));

	    res = transformGenericVersion10((Map<?, ?>) params, doc);
	} else {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.CXT_LOG002, new Object[ ] { messageVersion, service });
	    logger.error(errorMsg);
	    throw new TransformersException(errorMsg);
	}

	return res;
    }

    /**
     * Construye un par&aacute;metro de entrada o salida para un servicio publicado por la plataforma @Firma.
     * @param parameters estructura clave / valor, donde la clave es una cadena con el siguiente formato:
     * XPATH_Parametro_desde_tagMensaje#tipo. Ej: parametros/idAplicacion#text.
     * @param docRequest Estructura XML con la plantilla a cumplimentar.
     * @return cadena que contiene la plantilla docRequest rellena con los datos indicados en parameters.
     * @throws TransformersException si ocurre algun error.
     */
    private Object transformGenericVersion10(Map<?, ?> parameters, Document docRequest) throws TransformersException {
	Element rootElement, aux;
	Iterator<?> iterator;
	Properties serviceProps;
	String key, value, res, schemaLocation;
	res = null;

	checkInputParameters(parameters, docRequest);

	rootElement = docRequest.getDocumentElement();
	serviceProps = TransformersProperties.getMethodTransformersProperties(service, method, messageVersion);
	schemaLocation = rootElement.getAttribute("xsi:SchemaLocation");

	// Establecimiento de la localizacion del esquema XSD que define el
	// parametro
	if (schemaLocation != null) {
	    if (serviceProps.getProperty(service + "." + messageVersion + "." + type + "." + TransformersConstants.SCHEMA_LOCATION_ADDRESS_PROP) != null) {
		schemaLocation = schemaLocation.replaceFirst(TransformersConstants.SCH_LOC_ADD_SEP, serviceProps.getProperty(service + "." + messageVersion + "." + type + "." + TransformersConstants.SCHEMA_LOCATION_ADDRESS_PROP));
	    }
	    rootElement.setAttribute("xsi:SchemaLocation", schemaLocation);
	}

	iterator = parameters.keySet().iterator();

	// Procesamos los parametros y los colocamos en el parametro de entrada
	logger.debug(Language.getResIntegra(ILogConstantKeys.CXT_LOG003));
	while (iterator.hasNext()) {
	    key = (String) iterator.next();
	    // data = key.split(TransformersConstants.paramSeparator);

	    aux = null;
	    if (parameters.get(key) instanceof String) {
		value = (String) parameters.get(key);

		logger.debug(Language.getFormatResIntegra(ILogConstantKeys.CXT_LOG004, new Object[ ] { key, value }));
		// if(data[1].equals(TransformersConstants.binaryValue)) {
		// aux =
		// UtilsXML.sustituyeElementoValorCDATA(rootElement,data[0],value);
		// } else if(data[1].equals(TransformersConstants.textValue)) {
		aux = UtilsXML.sustituyeElementoValor(rootElement, key, value);
		// }

		if (aux == null) {
		    throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.CXT_LOG005, new Object[ ] { key }));
		}
	    }
	}

	try {
	    res = UtilsXML.transformDOMtoString(docRequest);
	} catch (Exception e) {
	    logger.error(e);
	    throw new TransformersException(e.getMessage(), e);
	}
	logger.debug(Language.getResIntegra(ILogConstantKeys.CXT_LOG006));

	return res;
    }

    /**
     * Checks input parameters.
     * @param parameters parameters.
     * @param docRequest document of request of webservices.
     * @throws TransformersException whether the input parameters aren't valid.
     */
    private void checkInputParameters(Map<?, ?> parameters, Document docRequest) throws TransformersException {
	if (GenericUtils.checkNullValues(parameters, docRequest) || parameters.size() == 0) {
	    throw new TransformersException(Language.getResIntegra(ILogConstantKeys.CXT_LOG007));
	}
	Element rootElement = docRequest.getDocumentElement();

	if (type.equals(TransformersConstants.REQUEST_CTE)) {
	    if (!rootElement.getTagName().equals("mensajeEntrada") && !rootElement.getTagName().equals("inputMessage")) {
		// el elemento raiz es "mensajeEntrada"
		throw new TransformersException(Language.getResIntegra(ILogConstantKeys.CXT_LOG008));
	    }
	} else if (type.equals(TransformersConstants.RESPONSE_CTE)) {
	    if (!rootElement.getTagName().equals("mensajeSalida") && !rootElement.getTagName().equals("outputMessage")) {
		// el elemento raiz es "mensajeSalida"
		throw new TransformersException(Language.getResIntegra(ILogConstantKeys.CXT_LOG009));
	    }

	} else {
	    throw new TransformersException(Language.getResIntegra(ILogConstantKeys.CXT_LOG010));
	}
    }

    /**
     * 
     * @param msgVersion messageVersion
     */
    public final void setMessageVersion(String msgVersion) {
	messageVersion = msgVersion;
    }

    /**
     * 
     * @param svc service
     */
    public final void setService(String svc) {
	service = svc;
    }

    /**
     * 
     * @param typ type
     */
    public final void setType(String typ) {
	type = typ;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.xmlTransformers.IXmlTransformer#getMethod()
     */
    public final String getMethod() {
	return method;
    }

}
