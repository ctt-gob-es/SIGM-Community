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

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.transformers.xmlTransformers.DSSXmlTransformer.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>04/02/2011.</p>
 * @author Ministerio de la Presidencia.
 * @version 1.0, 04/02/2011.
 */
package es.gob.afirma.transformers.xmlTransformers;

import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.utils.UtilsXML;

/**
 * <p>Class transforms input params (structure Map&ltString, Object&gt) to request xml used for @Firma services of type DSS.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 04/02/2011.
 */
public class DSSXmlTransformer implements IXmlTransformer {

    /**
     * Attribute that represents DSSS service name.
     */
    private String service = null;
    /**
     * Attribute that represents type.
     */
    private String type = null;
    /**
     * Attribute that represents message version.
     */
    private String messageVersion = null;

    /**
     * Attribute that represents method of webService.
     */
    private String method;

    /**
     * Attribute that represents application logger.
     */
    private static Logger logger = Logger.getLogger(DSSXmlTransformer.class);

    /**
     * Constructor method for the class DSSXmlTransformer.java.
     * @param svc service name.
     * @param methodParam method name of WS.
     * @param typ request or response.
     * @param msgVersion message version.
     */
    public DSSXmlTransformer(String svc, String methodParam, String typ, String msgVersion) {
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
	Object result;
	if (!(params instanceof Map) || ((Map) params).size() == 0) {
	    throw new TransformersException(Language.getResIntegra(ILogConstantKeys.DXT_LOG001));
	}

	if (messageVersion.equals(TransformersConstants.VERSION_10)) {
	    Document doc = TransformersFacade.getInstance().getXmlRequestFileByRequestType(service, method, type, messageVersion);
	    logger.debug(Language.getFormatResIntegra(ILogConstantKeys.DXT_LOG002, new Object[ ] { service, messageVersion }));

	    result = transformXmlVersion1((Map<String, Object>) params, doc);
	} else {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.DXT_LOG003, new Object[ ] { messageVersion, service });
	    logger.error(errorMsg);
	    throw new TransformersException(errorMsg);
	}

	return result;
    }

    /**
     * Generates a xml (dss version 1) by a parameters map.
     * @param parameters parameters Map has a estructure type
     * <code>&lt;String path, String value&gt;</code> for simple nodes and <code>&lt;String basePath, Map&lt;String,Object&gt;&gt;</code>
     * for nodes with multiples values (nodes with the same name and distinct values). Map keys are used for to indicate path to localizate
     * xml node.
     * @param doc represents the entire XML document
     * @return xml created
     * @throws TransformersException in case of a error generating the xml
     */
    private String transformXmlVersion1(Map<String, Object> parameters, Document doc) throws TransformersException {
	logger.debug(Language.getResIntegra(ILogConstantKeys.DXT_LOG004));
	String result = null;
	// creamos los nodos a partir de los parámetros de entrada
	createXmlNodes(parameters, doc.getDocumentElement());

	// Eliminamos nodos opcionales sin parámetros asignados.
	UtilsXML.deleteNodesNotUsed(doc.getDocumentElement(), TransformersConstants.OPTIONAL_ANODE_TYPES);

	try {
	    result = UtilsXML.transformDOMtoString(doc);
	} catch (Exception e) {
	    throw new TransformersException(e.getMessage(), e);
	}
	logger.debug(Language.getResIntegra(ILogConstantKeys.DXT_LOG005));
	return result;
    }

    /**
     * Creates xml nodes by input parameters (with xpath).
     * @param parameters parameters Map has a estructure type
     * <code>&lt;String path, String value&gt;</code> for simple nodes and <code>&lt;String basePath, Map&lt;String,Object&gt;&gt;</code>
     * for nodes with multiples values (nodes with the same name and distinct values). Map keys are used for to indicate path to localizate
     * xml node.
     * @param element root xml element.
     * @throws TransformersException in case of error.
     */
    private void createXmlNodes(Map<String, Object> parameters, Element element) throws TransformersException {

	for (String key: parameters.keySet()) {
	    Object value = parameters.get(key);
	    // comprobamos si es un parámetro para nodo simple o múltiple
	    // (varios nodos con el mismo nombre y al mismo nivel)
	    if (value instanceof Map[ ]) {
		addValueIntoNodeMultiple(element, key, (Map<String, Object>[ ]) value);
	    } else if (value instanceof String) {
		addValueIntoNode(element, key, (String) value);
	    } else {
		Object valueType = value;
		if (value != null && value.getClass() != null) {
		    valueType = value.getClass().getName();
		}
		throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.DXT_LOG006, new Object[ ] { key, valueType }));
	    }

	}
    }

    /**
     * Adds values into nodes with several ocurrences.
     * @param xmlElement xml element.
     * @param basePath base path.
     * @param valuesNodes values to put.
     * @throws TransformersException if an errors happens.
     */
    private void addValueIntoNodeMultiple(Element xmlElement, String basePath, Map<String, Object>[ ] valuesNodes) throws TransformersException {
	// obtenemos el nodo padre para añadir todos los subnodos de tipo
	// múltiple (repetidos)
	Element parent = (Element) UtilsXML.buscaHijo(xmlElement, basePath).getParentNode();
	// extraemos el nodo y subnodos a repetir (usándolo como plantilla)
	Element templateToRepeat = UtilsXML.eliminaElemento(xmlElement, basePath);

	for (Map<String, Object> parametersNode: valuesNodes) {
	    if (parametersNode.size() > 0) {
		// creamos una instancia del nodo por cada conjunto de valores.
		Element instance = (Element) templateToRepeat.cloneNode(true);
		// recursividad
		createXmlNodes(parametersNode, instance);
		parent.appendChild(instance);
	    }
	}
	UtilsXML.removeAfirmaAttribute(parent);
    }

    /**
     * Add value into a node.
     * @param xmlElement xml element.
     * @param path path.
     * @param value value.
     * @throws TransformersException if an errors happens.
     */
    private void addValueIntoNode(Element xmlElement, String path, String value) throws TransformersException {
	Element result = null;
	if (path.contains(TransformersConstants.ATTRIBUTE_SEPARATOR)) {
	    result = UtilsXML.insertAttributeValue(xmlElement, path, value);
	} else {
	    result = UtilsXML.insertValueElement(xmlElement, path, value);
	}
	if (result == null) {
	    throw new TransformersException(Language.getFormatResIntegra(ILogConstantKeys.DXT_LOG007, new Object[ ] { path }));
	}
	return;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.afirma.transformers.xmlTransformers.IXmlTransformer#getMethod()
     */
    public final String getMethod() {
	return method;
    }

}
