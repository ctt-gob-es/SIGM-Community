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
 * <b>File:</b><p>es.gob.afirma.transformers.TransformersFacade.java.</p>
 * <b>Description:</b><p>This Class provides facade for transform input and output parameters of @Firma v.5 plataform.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>16/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 16/03/2011.
 */
package es.gob.afirma.transformers;

import ieci.tdw.ispac.ispaclib.configuration.ConfigurationHelper;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import es.dipucr.afirma.AfirmaConfigFilePathResolverImpl;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.parseTransformers.ParseTransformersFactory;
import es.gob.afirma.transformers.xmlTransformers.XmlTransformersFactory;
import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.GenericUtils;
import es.gob.afirma.utils.NumberConstants;
import es.gob.afirma.utils.UtilsXML;

/**
 * <p>Class provides facade for transform input and output parameters of @Firma v.5 plataform.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 16/03/2011.
 */
public final class TransformersFacade {

    /**
     *  Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(TransformersFacade.class);

    /**
     * Class instance.
     */
    private static TransformersFacade instance;
    
    /**
     * Class instance.
     */
    private static AfirmaConfigFilePathResolverImpl afirmaConfigFilePathResolver;

    /**
     * Properties for publicated services of @Firma v.5 plataform.
     */
    private Properties transformersProperties;

    /**
     * Attribute that represents parser parameters properties.
     */
    private Properties parserParamsProp;

    /**
     * Attribute that represents parameters number.
     */
    private static final int PARAM_NUMBERS = 4;

    /**
     * Retrieves a class instance.
     * @return a class instance.
     */
    public static TransformersFacade getInstance() {
	instance = new TransformersFacade();
	afirmaConfigFilePathResolver = new AfirmaConfigFilePathResolverImpl();
	return instance;
    }

    /**
     * Constructor method for the class TransformersFacade.java.
     */
    private TransformersFacade() {
	transformersProperties = TransformersProperties.getTransformersProperties();
	parserParamsProp = ParserParameterProperties.getParserParametersProperties();
    }

    /**
     * Retrieves a DOM tree that contains XML structure of the requested input parameter.
     * @param serviceReq requested service.
     * @param method method of service.
     * @param type type of parameter to generate, input or output. Posibles values: request and response.
     * @param version service version.
     * @return Document object that contains XML structure of the requested input parameter.
     * @throws TransformersException if an error happens.
     */
    public Document getXmlRequestFileByRequestType(String serviceReq, String method, String type, String version) throws TransformersException {
	File xmlFile;
	String fileName, xmlTemplateFolder;
	Document res = null;
	if (!GenericUtils.assertStringValue(serviceReq) || !GenericUtils.assertStringValue(type) || !GenericUtils.assertStringValue(version) || !GenericUtils.assertStringValue(method)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG003);
	    LOGGER.error(errorMsg);
	    throw new TransformersException(errorMsg);
	}
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG004, new Object[ ] { serviceReq, method, type, version }));

	try {
	    StringBuffer templateName = new StringBuffer(serviceReq).append(".");
	    templateName.append(method).append(".");
	    templateName.append(version).append(".");
	    templateName.append(type).append(".");
	    templateName.append(TransformersConstants.TEMPLATE_CTE);

	    fileName = transformersProperties.getProperty(templateName.toString());
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG005, new Object[ ] { fileName }));
	    
	    //URL url = TransformersFacade.class.getClassLoader().getResource(transformersProperties.getProperty(TransformersConstants.TRANSFORMERS_TEMPLATES_PATH_PROPERTIES)+ "/xmlTemplates/"+fileName);
	    
	    xmlTemplateFolder = afirmaConfigFilePathResolver.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA + File.separator + transformersProperties.getProperty(TransformersConstants.TRANSFORMERS_TEMPLATES_PATH_PROPERTIES)+ "/xmlTemplates/" + fileName);	    

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG007, new Object[ ] { xmlTemplateFolder }));
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TF_LOG008));
	    
	    xmlFile = new File(xmlTemplateFolder);
	    res = UtilsXML.parsearDocumento(new FileReader(xmlFile));
	    

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG006, new Object[ ] { res.getDocumentElement().getTagName() }));
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG003);
	    LOGGER.error(errorMsg, e);
	    throw new TransformersException(errorMsg, e);
	}

	return res;
    }

    /**
     * Retrieves a DOM tree that contains XML estructure of template response.
     * @param serviceReq service response.
     * @param method method response.
     * @param version service version.
     * @return objeto Document  represents the entire XML document of response.
     * @throws TransformersException if an error happens.
     */
    public Document getParserTemplateByRequestType(String serviceReq, String method, String version) throws TransformersException {
	File xmlFile;
	String fileName, xmlTemplateFolder;
	Document res = null;

	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG009, new Object[ ] { serviceReq, method, TransformersConstants.PARSER_CTE, version }));

	try {
	    fileName = transformersProperties.getProperty(serviceReq + "." + method + "." + version + "." + TransformersConstants.PARSER_CTE + "." + TransformersConstants.TEMPLATE_CTE);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG005, new Object[ ] { fileName }));

//	    URL url = TransformersFacade.class.getClassLoader().getResource(transformersProperties.getProperty(TransformersConstants.TRANSFORMERS_TEMPLATES_PATH_PROPERTIES)+ "/parserTemplates/"+fileName);
	    xmlTemplateFolder = afirmaConfigFilePathResolver.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA + File.separator + transformersProperties.getProperty(TransformersConstants.TRANSFORMERS_TEMPLATES_PATH_PROPERTIES)+ "/parserTemplates/" + fileName);	    

	    //xmlTemplateFolder = ConfigurationHelper.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA + File.separator + transformersProperties.getProperty(TransformersConstants.TRANSFORMERS_TEMPLATES_PATH_PROPERTIES)+ "/parserTemplates",fileName);
	    
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG010, new Object[ ] { xmlTemplateFolder }));
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TF_LOG008));

	    xmlFile = new File(xmlTemplateFolder);
	    res = UtilsXML.parsearDocumento(new FileReader(xmlFile));
	    
 
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG007, new Object[ ] { fileName }));
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TF_LOG008));
	    

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TF_LOG006, new Object[ ] { res.getDocumentElement().getTagName() }));
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG003);
	    LOGGER.error(errorMsg, e);
	    throw new TransformersException(errorMsg, e);
	}

	return res;
    }

    /**
     * Generates a xml string to invoke a @Firma service using a parameters map.
     * <br>Use this method with four parameters instead (with 'method' parameter included).</br>
     * @param parameters  map contains  keys/values of the input paramaters for @Firma plataform requests xml.
     * Key is formed by xpath node and value is the text of the node (<code>Map&ltString,String&gt</code>). <br> Furthermore, attributes are specificated by the pattern: <code>xpath_node</code>
     * + <code>'@'</code> + <code>attribute_name</code>. If xml node has a several ocurrences or instances (some nodes with the same tag name) xpath node will
     * be included as key and as value, an array of maps with all children nodes data/values (<code>Map&ltString, [Map&ltString,Object&gt]&gt</code>).
     * <p>Examples:<br><p>
     * <li>Entry for a text value: <code>key ->"dss:InputDocuments/dss:DocumentHash/ds:DigestValue" | value -> "jP+/ULR4xkgQhZ8KIq7hZUO1aNo="</code>.</li></p>
     * <li>Entry for an attribute: <code>key ->"dss:InputDocuments/dss:DocumentHash/ds:DigestMethod@Algorithm" | value -> "jP+/ULR4xkgQhZ8KIq7hZUO1aNo="</code>.</li>
     * <li>Entry for a node with several ocurrences: <code>key ->"afxp:Requests/dss:VerifyRequest" | value -> Array[Map&ltString,Object&gt]</code>.</li></p>
     * @param service @Firma service name
     * @param version message version to create.
     * @return string xml to invoke a @Firma service.
     * @throws TransformersException  if an error happens.
     */
    public String generateXml(Map<String, Object> parameters, String service, String version) throws TransformersException {
	return generateXml(parameters, service, getMethodName(service), version);
    }

    /**
     * Get method name for the service given.
     * @param service service to call.
     * @return method name for the service given.
     */
    private String getMethodName(String service) {
	if (GeneralConstants.DSS_AFIRMA_SIGN_REQUEST.equals(service)) {
	    return GeneralConstants.DSS_AFIRMA_SIGN_METHOD;
	} else if (GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST.equals(service)) {
	    return GeneralConstants.DSS_AFIRMA_VERIFY_METHOD;
	} else if (GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST.equals(service)) {
	    return GeneralConstants.DSS_AFIRMA_VERIFY_METHOD;
	} else if (GeneralConstants.DSS_BATCH_VERIFY_SIGNATURE_REQUESTS.equals(service)) {
	    return GeneralConstants.DSS_AFIRMA_VERIFY_SIGNATURES_METHOD;
	} else if (GeneralConstants.DSS_BATCH_VERIFY_CERTIFICATE_REQUEST.equals(service)) {
	    return GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATES_METHOD;
	} else if (GeneralConstants.DSS_ASYNC_REQUEST_STATUS.equals(service)) {
	    return GeneralConstants.DSS_ASYNC_REQUEST_STATUS_METHOD;
	} else {
	    return service;
	}
    }

    /**
     * Generates a xml string to invoke a @Firma service using a parameters map.
     * @param parameters  map contains  keys/values of the input paramaters for @Firma plataform requests xml.
     * Key is formed by xpath node and value is the text of the node (<code>Map&ltString,String&gt</code>). <br> Furthermore, attributes are specificated by the pattern: <code>xpath_node</code>
     * + <code>'@'</code> + <code>attribute_name</code>. If xml node has a several ocurrences or instances (some nodes with the same tag name) xpath node will
     * be included as key and as value, an array of maps with all children nodes data/values (<code>Map&ltString, [Map&ltString,Object&gt]&gt</code>).
     * <p>Examples:<br><p>
     * <li>Entry for a text value: <code>key ->"dss:InputDocuments/dss:DocumentHash/ds:DigestValue" | value -> "jP+/ULR4xkgQhZ8KIq7hZUO1aNo="</code>.</li></p>
     * <li>Entry for an attribute: <code>key ->"dss:InputDocuments/dss:DocumentHash/ds:DigestMethod@Algorithm" | value -> "jP+/ULR4xkgQhZ8KIq7hZUO1aNo="</code>.</li>
     * <li>Entry for a node with several ocurrences: <code>key ->"afxp:Requests/dss:VerifyRequest" | value -> Array[Map&ltString,Object&gt]</code>.</li></p>
     * @param service @Firma service name.
     * @param method method of service.
     * @param version message version to create.
     * @return string xml to invoke a @Firma service.
     * @throws TransformersException  if an error happens.
     */
    public String generateXml(Map<String, Object> parameters, String service, String method, String version) throws TransformersException {
	Class<?> transformerClass;
	if (parameters == null || !GenericUtils.assertStringValue(service) || !GenericUtils.assertStringValue(version) || !GenericUtils.assertStringValue(method)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG003);
	    LOGGER.error(errorMsg);
	    throw new TransformersException(errorMsg);
	}
	String res = null;
	try {
	    // Obtenemos la clase que creara la cadena xml que contiene el
	    // parametro esperado por @firma
	    transformerClass = XmlTransformersFactory.getXmlTransformer(service, method, TransformersConstants.REQUEST_CTE, version);
	    res = (String) invokeCommonXmlTransf(transformerClass, parameters, service, method, TransformersConstants.REQUEST_CTE, version);
	} catch (Exception e) {
	    LOGGER.error(e);
	    throw new TransformersException(e);
	}
	return res;
    }

    /**
     * <p>Parses a @Firma xml response to a structure of type Map(String/Object).
     * Response contains a structure with java.lang.String values for nodes without childrens and java.util.Map for nodes with childrens.</p>
     * 
     * Use this method with four parameters instead (with 'method' parameter included).<br/>
     * @param response xml response of @Firma.
     * @param service @Firma service name.
     * @param version xml version to parser.
     * @return a structure of type Map(String/Object) with obtained values in the parser process.
     * @throws TransformersException  if an error happens.
     */
    public Map<String, Object> parseResponse(String response, String service, String version) throws TransformersException {
	return parseResponse(response, service, getMethodName(service), version);
    }

    /**
     * <p>Parses a @Firma xml response to a structure of type Map(String/Object).</p>
     * Response contains a structure with java.lang.String values for nodes without childrens and java.util.Map for nodes with childrens.
     * 
     * @param response xml response of @Firma.
     * @param service @Firma service name.
     * @param method method name.
     * @param version xml version to parser.
     * @return a structure of type Map(String/Object) with obtained values in the parser process.
     * @throws TransformersException  if an error happens.
     */
    public Map<String, Object> parseResponse(String response, String service, String method, String version) throws TransformersException {
	Class<Object> transformerClass;
	Map<String, Object> res;

	res = null;
	if (!GenericUtils.assertStringValue(response) || !GenericUtils.assertStringValue(service) || !GenericUtils.assertStringValue(version) || !GenericUtils.assertStringValue(method)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG003);
	    LOGGER.error(errorMsg);
	    throw new TransformersException(errorMsg);
	}
	try {
	    // Obtenemos la clase que creara la estructura que contiene una
	    // respuesta generada
	    // por la plataforma
	    transformerClass = ParseTransformersFactory.getParseTransformer(service, method, version);
	    res = (Map) invokeParseTransf(response, transformerClass, service, method, version);
	} catch (Exception e) {
	    LOGGER.error(e);
	    throw new TransformersException(e);
	}
	return res;
    }

    /**
     * Invokes the method generates input xml message for @firma services.
     * @param transformerClass class generates xml request.
     * @param parameters input parameters collection
     * @param service @Firma service name.
     * @param methodWS method name of webServices.
     * @param type type of parameter to generate, input or output. Posibles values: request and response.
     * @param version message version.
     * @return input xml message for @firma services.
     * @throws TransformersException  if an error happens.
     */
    private Object invokeCommonXmlTransf(Class<?> transformerClass, Map<String, Object> parameters, String service, String methodWS, String type, String version) throws TransformersException {
	Class<?>[ ] constrParamClasses, methodParamClasses;
	Constructor<?> constructor;
	Method method;
	Object object;
	Object[ ] constrParamObjects, methodParamObjects;
	String res;

	res = null;

	try {
	    // Instanciamos un objeto transformardor
	    constrParamClasses = new Class[PARAM_NUMBERS];
	    constrParamClasses[0] = Class.forName(String.class.getName());
	    constrParamClasses[1] = Class.forName(String.class.getName());
	    constrParamClasses[2] = Class.forName(String.class.getName());
	    constrParamClasses[NumberConstants.INT_3] = Class.forName(String.class.getName());

	    constructor = transformerClass.getConstructor(constrParamClasses);
	    constrParamObjects = new Object[PARAM_NUMBERS];
	    constrParamObjects[0] = service;
	    constrParamObjects[1] = methodWS;
	    constrParamObjects[2] = type;
	    constrParamObjects[NumberConstants.INT_3] = version;
	    object = constructor.newInstance(constrParamObjects);

	    // Obtenemos el método que creara la cadena xml que contiene el
	    // parametro esperado por @firma
	    methodParamClasses = new Class[1];
	    methodParamClasses[0] = Class.forName("java.lang.Object");
	    method = transformerClass.getMethod("transform", methodParamClasses);

	    // Invocamos el método transformador
	    methodParamObjects = new Object[1];
	    methodParamObjects[0] = parameters;

	    res = (String) method.invoke(object, methodParamObjects);
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG001);
	    LOGGER.error(errorMsg, e);
	    throw new TransformersException(errorMsg, e);
	}

	return res;
    }

    /**
     * Invokes method that parses @Firma xml response.
     * @param response xml returns by @Firma web services.
     * @param transformerClass class parses xml response.
     * @param service @Firma service name.
     * @param methodParam method name.
     * @param version message version.
     * @return parameters collection parsed.
     * @throws TransformersException  if an error happens.
     */
    private Object invokeParseTransf(String response, Class<Object> transformerClass, String service, String methodParam, String version) throws TransformersException {
	Class<Object>[ ] constrParamClasses, methodParamClasses;
	Constructor<Object> constructor;
	Map<String, Object> res;
	Method method;
	Object object;
	Object[ ] constrParamObjects, methodParamObjects;

	res = null;

	try {
	    // Instanciamos un objeto transformardor
	    constrParamClasses = new Class[NumberConstants.INT_3];
	    Class<Object> stringClass = (Class<Object>) Class.forName("java.lang.String");
	    constrParamClasses[0] = stringClass;
	    constrParamClasses[1] = stringClass;
	    constrParamClasses[2] = stringClass;

	    constructor = transformerClass.getConstructor(constrParamClasses);
	    constrParamObjects = new Object[NumberConstants.INT_3];
	    constrParamObjects[0] = service;
	    constrParamObjects[1] = methodParam;
	    constrParamObjects[2] = version;
	    object = constructor.newInstance(constrParamObjects);

	    // Obtenemos el método que creara la cadena xml que contiene el
	    // parametro esperado por @firma
	    methodParamClasses = new Class[1];
	    methodParamClasses[0] = stringClass;
	    method = transformerClass.getMethod("transform", methodParamClasses);

	    // Invocamos el método transformador
	    methodParamObjects = new Object[1];
	    methodParamObjects[0] = response;
	    res = (Map) method.invoke(object, methodParamObjects);
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.TF_LOG001);
	    LOGGER.error(errorMsg, e);
	    throw new TransformersException(errorMsg, e);
	}

	return res;
    }

    /**
     * Retrieves the xpath node by identificator given.
     * @param parameterName name of parameter
     * @return value of parameter (xpath node).
     */
    public String getParserParameterValue(String parameterName) {
	String result = null;
	if (GenericUtils.assertStringValue(parameterName)) {
	    Object tmp = parserParamsProp.get(parameterName);
	    result = tmp == null ? null : tmp.toString();
	}
	return result;
    }
}
