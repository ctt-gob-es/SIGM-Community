/* 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 * 
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

package eu.stork.peps.auth.engine;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.parse.BasicParserPool;
import org.opensaml.xml.parse.XMLParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.SAMLEngineSignI;
import eu.stork.peps.auth.engine.core.STORKSAMLCore;
import eu.stork.peps.auth.engine.core.impl.CustomAttributeQueryMarshaller;
import eu.stork.peps.auth.engine.core.impl.CustomAttributeQueryUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SignModuleFactory;
import eu.stork.peps.configuration.ConfigurationCreator;
import eu.stork.peps.configuration.ConfigurationReader;
import eu.stork.peps.configuration.InstanceEngine;
import eu.stork.peps.exceptions.SAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineRuntimeException;

/**
 * Class that wraps the operations over SAML tokens, both generation and
 * validation of SAML requests and SAML responses. Compliant with "OASIS Secure
 * Assertion Markup Language (SAML) 2.0, May 2005", but taking into account
 * STORK specific requirements.
 * 
 * @author fjquevedo
 * @author iinigo
 */

public class SAMLEngine {

    /** The Document Builder Factory. */
    private static javax.xml.parsers.DocumentBuilderFactory dbf = null;

    /** The instance of every engine SAML. */
    private static Map<String, InstanceEngine> instanceConfigs;

    /** The instances of SAML engine. */
    private static Map<String, Map<String, Object>> instances;

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SAMLEngine.class
	    .getName());

    /** The Constant MODULE_SIGN_CONF. */
    private static final String MODULE_SIGN_CONF = "SignatureConf";

    /** The Constant SAML_ENGINE_SIGN_CLASS. */
    private static final String SAML_ENGINE_SIGN_CLASS = "class";

    /** The Constant SAML_ENGINE_CONF. */
    private static final String SAML_ENGINE_CONF = "SamlEngineConf";

    /** The Constant SAML_ENGINE_FILE_CONF. */
    private static final String SAML_ENGINE_FILE_CONF = "fileConfiguration";

    /** The codification of characters. */
    private static final String CHARACTER_ENCODING = "UTF-8";

    /** The SAML core. */
    private STORKSAMLCore samlCore;

    /** The Module of Signature. */
    private SAMLEngineSignI signer;

   
	/** Initializes the SAML engine. */
    /** Configure Document Builder Factory. */

    static {
	startUp();
	loadDocumentFactory();
    }

    /**
     * Load document factory.
     */
    private static void loadDocumentFactory() {

	try {
	    dbf = javax.xml.parsers.DocumentBuilderFactory.newInstance();
	    dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
	    dbf.setNamespaceAware(true);
	    dbf.setIgnoringComments(true);
	} catch (ParserConfigurationException e) {
	    LOG.error("Error parser configuration.");
	    throw new STORKSAMLEngineRuntimeException(e);
	}

    }

    /**
     * Method that initializes the basic services for the SAML Engine, like the
     * OpenSAML library and the BouncyCastle provider.
     */
    private static void startUp() {

	LOG.info("SAMLEngine: Initialize OpenSAML");

	try {
	    DefaultBootstrap.bootstrap();
	} catch (ConfigurationException e) {
	    LOG.error("Problem initializing the OpenSAML library.");
	    throw new STORKSAMLEngineRuntimeException(e);
	}

	LOG.debug("Read all file configurations. (instances of SAMLEngine)");
	try {
	    instanceConfigs = ConfigurationReader.readConfiguration();
	} catch (SAMLEngineException e) {
	    LOG.error("Error read configuration file.");
	    throw new STORKSAMLEngineRuntimeException(e);
	}

	LOG.debug("Create all instaces of saml engine. (instances of SAMLEngine)");
	try {
	    instances = ConfigurationCreator
		    .createConfiguration(instanceConfigs);
	} catch (STORKSAMLEngineException e) {
	    LOG.error("Error initializing instances from Stork SAML engine.");
	    throw new STORKSAMLEngineRuntimeException(e);
	}
    }

    /**
     * Instantiates a new SAML engine.
     */
    private SAMLEngine() {

    }

    /**
     * Instantiates a new SAML engine.
     * 
     * @param nameInstance the name instance
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    protected SAMLEngine(final String nameInstance)
	    throws STORKSAMLEngineException {
	LOG.info("Loading Specific Configuration.");

	LOG.debug("Create intance of saml messages.");

	Map<String, Object> instance = instances.get(nameInstance);

	if (instance == null || instance.isEmpty()) {
	    LOG.error("Instance: " + nameInstance + " not exist.");
	    throw new STORKSAMLEngineException("Instance: " + nameInstance
		    + " not exist.");
	}

	Properties properties = (Properties) instance.get(SAML_ENGINE_CONF);

	if (properties == null) {
		try {
			properties = new Properties();
			properties.loadFromXML(new FileInputStream(System.getProperty("SamlEnginePath")));
		} catch(Exception e) {
			if (LOG.isDebugEnabled())
				LOG.debug(e.getMessage(), e);
			properties = null;
		}
		
		if (properties == null) { 
			LOG.error("SamlEngine.xml: not exist.");
			throw new STORKSAMLEngineException("SamlEngine.xml: not exist.");
		}
	}

	samlCore = new STORKSAMLCore(properties);

	final HashMap<String, String> propertiesSign = (HashMap<String, String>) instance
		.get(MODULE_SIGN_CONF);

	LOG.debug("Loading Module of sign.");
	signer = SignModuleFactory.getInstance(propertiesSign
		.get(SAML_ENGINE_SIGN_CLASS));

	try {
	    LOG.info("Initialize module of sign.");
	    signer.init(propertiesSign.get(SAML_ENGINE_FILE_CONF));
	    LOG.info("Load cryptographic service provider of module of sign.");
	    signer.loadCryptServiceProvider();
	} catch (SAMLEngineException e) {
	    LOG.error("Error create signature module: "
		    + propertiesSign.get(SAML_ENGINE_FILE_CONF));
        LOG.info("Exception" + e);
	    throw new STORKSAMLEngineException(e);
	}
    }

    /**
     * Gets the Signer properties.
     * 
     * @return the SAML Sign properties
     */
    protected SAMLEngineSignI getSigner() {
		return signer;
	}
    
    /**
     * Gets the SAML core properties.
     * 
     * @return the SAML core properties
     */
    protected final STORKSAMLCore getSamlCoreProperties() {
    	return samlCore;
    }

    /**
     * Method that transform the received SAML object into a byte array
     * representation.
     * 
     * @param samlToken the SAML token.
     * 
     * @return the byte[] of the SAML token.
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    private byte[] marshall(final XMLObject samlToken)
	    throws SAMLEngineException {

	try {
	    javax.xml.parsers.DocumentBuilder docBuilder = null;

	    final MarshallerFactory marshallerFactory = Configuration
		    .getMarshallerFactory();

	    final Marshaller marshaller;
	    if (samlToken.getElementQName().toString().endsWith(CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME))
	    	marshaller = new CustomAttributeQueryMarshaller();
	    else
	    	marshaller = marshallerFactory
		    .getMarshaller(samlToken);

	    docBuilder = dbf.newDocumentBuilder();

	    final Document doc = docBuilder.newDocument();

	    marshaller.marshall(samlToken, doc);

	    // Obtain a byte array representation of the marshalled SAML object
	    final DOMSource domSource = new DOMSource(doc);
	    final StringWriter writer = new StringWriter();
	    final StreamResult result = new StreamResult(writer);
	    final TransformerFactory transFactory = TransformerFactory
		    .newInstance();
	    Transformer transformer;

	    transformer = transFactory.newTransformer();
	    transformer.transform(domSource, result);
        LOG.debug("SAML request \n"+ writer.toString());
	    return writer.toString().getBytes(CHARACTER_ENCODING);

	} catch (ParserConfigurationException e) {
	    LOG.error("ParserConfigurationException.");
	    throw new SAMLEngineException(e);
	} catch (MarshallingException e) {
	    LOG.error("MarshallingException.");
	    throw new SAMLEngineException(e);
	} catch (TransformerConfigurationException e) {
	    LOG.error("TransformerConfigurationException.");
	    throw new SAMLEngineException(e);
	} catch (TransformerException e) {
	    LOG.error("TransformerException.");
	    throw new SAMLEngineException(e);
	} catch (UnsupportedEncodingException e) {
	    LOG.error("UnsupportedEncodingException: " + CHARACTER_ENCODING);
	    throw new SAMLEngineException(e);
	}
    }

    /**
     * Method that signs a SAML Token.
     * 
     * @param tokenSaml the token SAML
     * 
     * @return the SAML object sign
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    private SignableSAMLObject sign(final SignableSAMLObject tokenSaml)
	    throws SAMLEngineException {
	LOG.debug("Sign SamlToken.");
	signer.sign(tokenSaml);
	return tokenSaml;
    }

    /**
     * Sign and transform to byte array.
     * 
     * @param samlToken the SAML token
     * 
     * @return the byte[] of the SAML token
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    protected final byte[] signAndMarshall(final SignableSAMLObject samlToken)
	    throws SAMLEngineException {
    	LOG.debug("Marshall Saml Token.");
    	SignableSAMLObject signElement = sign(samlToken);    	
    	return marshall(signElement);
    }

    /**
     * Method that unmarshalls a SAML Object from a byte array representation to
     * an XML Object.
     * 
     * @param samlToken Byte array representation of a SAML Object
     * 
     * @return XML Object (superclass of SAMLObject)
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    protected final XMLObject unmarshall(final byte[] samlToken)
	    throws SAMLEngineException {
	try {
	    // Get parser pool manager
	    final BasicParserPool ppMgr = new BasicParserPool();
	    // Note: this is necessary due to an unresolved Xerces deferred DOM
	    // issue/bug
	    final HashMap<String, Boolean> features = new HashMap<String, Boolean>();
	    features.put(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
	    ppMgr.setBuilderFeatures(features);

	    ppMgr.setNamespaceAware(true);

	    // Parse SAMLToken
	    Document document = ppMgr.parse(new ByteArrayInputStream(samlToken));
        if (document != null){
	    final Element root = document.getDocumentElement();
	    // Get appropriate unmarshaller
            final UnmarshallerFactory unmarshallerFact = Configuration.getUnmarshallerFactory();
	    // Unmarshall using the SAML Token root element
            if (unmarshallerFact != null && root != null){
            	final Unmarshaller unmarshaller;
            	if (root.getLocalName().equals(CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME))
            		unmarshaller = new CustomAttributeQueryUnmarshaller();
            	else
            		unmarshaller = unmarshallerFact.getUnmarshaller(root);
                try {
	    return unmarshaller.unmarshall(root);
                } catch (NullPointerException e){
                    LOG.error("Error element tag incomplet or null.");
                    throw new SAMLEngineException("NullPointerException", e);
                }
            } else {
                LOG.error("Error element tag incomplet or null.");
                throw new SAMLEngineException("NullPointerException : unmarshallerFact or root is null");
            }
        } else {
            LOG.error("Error element tag incomplet or null.");
            throw new SAMLEngineException("NullPointerException : document is null");
        }
	} catch (XMLParserException e) {
	    LOG.error("XML Parsing Error.", e);
	    throw new SAMLEngineException(e);
	} catch (UnmarshallingException e) {
	    LOG.error("TransformerException.", e);
	    throw new SAMLEngineException(e);
	} catch (NullPointerException e) {
	    LOG.error("Error element tag incomplet or null.", e);
	    throw new SAMLEngineException(e);
	}
    }

    /**
     * Method that validates an XML Signature contained in a SAML Token.
     * 
     * @param samlToken the SAML token
     * 
     * @return the SAML object
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    protected final SAMLObject validateSignature(
	    final SignableSAMLObject samlToken) throws SAMLEngineException {

	LOG.info("Validate Signature");
	signer.validateSignature(samlToken);

	return samlToken;
    }
}
