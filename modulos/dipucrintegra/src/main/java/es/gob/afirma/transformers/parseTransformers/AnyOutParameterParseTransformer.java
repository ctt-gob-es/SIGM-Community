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

package es.gob.afirma.transformers.parseTransformers;

import java.io.StringReader;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersProperties;
import es.gob.afirma.utils.UtilsXML;

/**
 * Parseador que logra obtener parsear par&aacute;metros de salida con nodos con el mismo nombre al mismo nivel del &aacute;rbol XML. El
 * resultado es mapeado como un Map que contiene el elemento ra&iacute;z con toda la estructura del &aacute;rbol.
 * Cada elemento es representado, de igual forma, como un Map que contiene como valor una lista de Maps (que alojan la informaci&oacute;n de los hijos),
 *  si el nodo tiene hijos, o una cadena con el valor del nodo.
 * 
 * @author SEPAOT
 *
 */
public class AnyOutParameterParseTransformer implements IParseTransformer {

    /**
     * Attribute that represents .
     */
    private static Logger logger = Logger.getLogger(AnyOutParameterParseTransformer.class);

    /**
     * Attribute that represents .
     */
    private String request = null;
    /**
     * Attribute that represents .
     */
    private String messageVersion = null;

    /**
     * Attribute that represents method name.
     */
    private String method = null;

    /**
     * Constructor method for the class AnyOutParameterParseTransformer.java.
     * @param req request
     * @param methodParam method name
     * @param msgVersion messageVersion
     */
    public AnyOutParameterParseTransformer(String req, String methodParam, String msgVersion) {
	request = req;
	method = methodParam;
	messageVersion = msgVersion;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.parseTransformers.IParseTransformer#getRequest()
     */
    public final String getRequest() {
	return request;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.parseTransformers.IParseTransformer#getMessageVersion()
     */
    public final String getMessageVersion() {
	return messageVersion;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.parseTransformers.IParseTransformer#transform(java.lang.String)
     */
    public final Object transform(String xmlResponse) throws TransformersException {
	Map<String, Object> xmlData, res;
	String xpathRespElement;

	res = null;
	xpathRespElement = getXpathRespElement();
	logger.debug(Language.getFormatResIntegra(ILogConstantKeys.AOPPT_LOG002, new Object[ ] { xpathRespElement }));
	if (xpathRespElement == null) {
	    throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.AOPPT_LOG001, new Object[ ] { TransformersConstants.RESP_ROOT_ELEMENT_CTE, request, messageVersion }));
	}

	xmlData = readResponse(xmlResponse, xpathRespElement);
	res = (Map<String, Object>) xmlData.get("errorResponse");

	if (res == null) {
	    res = (Map<String, Object>) xmlData.get("okResponse");
	}

	return res;
    }

    /**
     * Recupera la información obtenida como resultado de solicitar un servicio a la plataforma. Esta
     * información puede describir un error (excepción) o el resultado del servicio.
     * @param xmlResponse cadena que contiene el código xml de respuesta.
     * @param xpathRespElement xpathRespElement.
     * @return un mapa con la información de error o del resultado del servicio.
     * @throws TransformersException si se produjo algún error al leer el xml de respuesta.
     */
    private Map<String, Object> readResponse(String xmlResponse, String xpathRespElement) throws TransformersException {
	Document doc;
	Element rootElement;
	Map<String, Object> res, aux;

	res = null;

	try {
	    doc = UtilsXML.parsearDocumento(new StringReader(xmlResponse));
	} catch (Exception e) {
	    throw new TransformersException(Language.getResIntegra(ILogConstantKeys.AOPPT_LOG003), e);
	}

	if (doc != null) {
	    // Leemos los datos de la respuesta
	    rootElement = doc.getDocumentElement();
	    if (!rootElement.getTagName().equals("mensajeSalida") && !rootElement.getTagName().equals("outputMessage")) {// el
		// elemento
		// raiz
		// es
		// "mensajeSalida"
		throw new TransformersException(Language.getResIntegra(ILogConstantKeys.AOPPT_LOG004));
	    }

	    res = new Hashtable<String, Object>();
	    // Comprobamos si se ha producido algún error (en servicios en
	    // español e inglés)
	    aux = readErrorResponse(UtilsXML.dameElemento(rootElement, "respuesta/Excepcion"));
	    if (aux == null) {
		aux = readErrorResponse(UtilsXML.dameElemento(rootElement, "response/Exception"));
	    }
	    if (aux == null) {
		// No se produjo ningún error, leemos el mensaje SOAP OK
		aux = readOKResponse(UtilsXML.dameElemento(rootElement, xpathRespElement));
		if (aux == null) {
		    throw new TransformersException(Language.getResIntegra(ILogConstantKeys.AOPPT_LOG005));
		}
		res.put("okResponse", aux);
	    } else {
		// Mensaje de error
		res.put("errorResponse", aux);
	    }
	}

	return res;
    }

    /**
     * Parsea la cadena XML devuelta por el servicio web de validación para una repuesta de error.
     * @param exceptionElement elemento representado por el tag <strong>Excepcion</strong>. Null si no existe.
     * @return un mapa con los valores obtenidos de leer los hijos del tag <strong>Excepcion</strong>. Si este tag no existe
     * devuelve null.
     */
    private Map<String, Object> readErrorResponse(Element exceptionElement) {
	List<?> exceptionData;
	Node aux;
	Map<String, Object> res = null;
	String nodeValue;

	if (exceptionElement != null) {
	    // Se ha producido una excepcion al validar el certificado que no
	    // ha permitido realizar la validación completamente.
	    res = new Hashtable<String, Object>();

	    exceptionData = UtilsXML.buscaListaHijos(exceptionElement);
	    for (int i = 0; i < exceptionData.size(); i++) {
		aux = (Node) exceptionData.get(i);
		if (aux.getNodeType() == Node.ELEMENT_NODE) {
		    // Los tags posibles son: codigoError, descripcion y
		    // excepcionAsociada
		    nodeValue = UtilsXML.dameValorEl((Element) aux);
		    res.put(((Element) aux).getTagName(), nodeValue != null ? nodeValue : "");
		}
	    }
	}

	return res;
    }

    /**
     * Recupera la información recuperada tras validar una firma o multifirma de usuario o servidor.
     * @param upperRespElement elemento representado por el tag <strong>Respuesta</strong>. Null si no existe.
     * @return un mapa con los valores obtenidos de leer los hijos del tag <strong>Respuesta</strong>. Si este tag no existe
     * devuelve null.
     */
    private Map<String, Object> readOKResponse(Element upperRespElement) {
	Map<String, Object> res;
	List<?> auxV;

	res = null;

	if (upperRespElement != null) {
	    // Obtenemos la información de respuesta
	    auxV = UtilsXML.buscaListaHijos(upperRespElement);

	    res = parseInfoValRec(auxV, 0);
	}
	return res;
    }

    /**
     * 
     * @param elementChilds elementChilds.
     * @param index index.
     * @return Map<String, Object> .
     */
    private Map<String, Object> parseInfoValRec(List<?> elementChilds, int index) {
	Map<String, Object> res;
	List<?> auxV;
	Node auxN;
	String nodeValue;

	res = new Hashtable<String, Object>();

	if (elementChilds != null && index < elementChilds.size()) {
	    auxN = (Node) elementChilds.get(index);
	    // Obtenemos los elementos estado y descripcion
	    if (auxN.getNodeType() == Node.ELEMENT_NODE) {
		auxV = UtilsXML.buscaListaHijos((Element) auxN);

		if (auxV != null && auxV.size() > 0) {
		    res.put(auxN.getNodeName(), parseInfoValRec(auxV, 0));
		} else {
		    nodeValue = UtilsXML.dameValorEl((Element) auxN);
		    res.put(((Element) auxN).getTagName(), nodeValue != null ? nodeValue : "");
		    for (int i = 1; i < elementChilds.size(); i++) {
			res.putAll(parseInfoValRec(elementChilds, index + i));
		    }
		}
	    }
	}
	return res;
    }

    /**
     * 
     * @return XpathRespElement
     */
    private String getXpathRespElement() {
	Properties properties;
	String res;

	properties = TransformersProperties.getMethodParseTransformersProperties(request, method, messageVersion);
	res = properties.getProperty(request + "." + method + "." + messageVersion + "." + TransformersConstants.PARSER_CTE + "." + TransformersConstants.RESP_ROOT_ELEMENT_CTE);

	return res;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.parseTransformers.IParseTransformer#getMethod()
     */
    public final String getMethod() {
	return method;
    }

}
