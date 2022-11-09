package es.clave.sp;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.opensaml.saml2.core.Response;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import eu.eidas.auth.commons.xml.DocumentBuilderFactoryUtil;
import eu.eidas.auth.commons.xml.opensaml.OpenSamlHelper;
import eu.eidas.encryption.exception.UnmarshallException;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;

public class SPUtil {

    SPUtil() {};

    private static final Logger LOG = LoggerFactory.getLogger(SPUtil.class);

    private static final String NO_ASSERTION = "no assertion found";

    private static final String ASSERTION_XPATH = "//*[local-name()='Assertion']";

    public static String getConfigFilePath() {
        /*String envLocation = System.getenv().get(Constants.SP_CONFIG_REPOSITORY);
        String configLocation = System.getProperty(Constants.SP_CONFIG_REPOSITORY, envLocation);
        return configLocation;*/
        return ((String)ApplicationContextProvider.getApplicationContext().getBean(Constants.SP_REPO_BEAN_NAME)).trim();
    }

    private static Properties loadConfigs(String fileName) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(SPUtil.getConfigFilePath()+fileName));
        return properties;
    }

    public static Properties loadSPConfigs() {
        try {
            return SPUtil.loadConfigs(Constants.SP_PROPERTIES);
        } catch (IOException e) {
            LOG.error(e.getMessage());
            LOG.error("", e);
            throw new ApplicationSpecificServiceException("Could not load configuration file", e.getMessage());
        }
    }

    /**
     * Returns true when the input contains an encrypted SAML Response
     *
     * @param tokenSaml
     * @return
     * @throws EIDASSAMLEngineException
     */
    public static boolean isEncryptedSamlResponse(byte[] tokenSaml) throws UnmarshallException {
        XMLObject samlObject = OpenSamlHelper.unmarshall(tokenSaml);
        if (samlObject instanceof Response) {
            Response response = (Response) samlObject;
            return response.getEncryptedAssertions() != null && !response.getEncryptedAssertions().isEmpty();
        }
        return false;

    }

    /**
     * @param samlMsg the saml response as a string
     * @return a string representing the Assertion
     */
    public static String extractAssertionAsString(String samlMsg) {
        String assertion = NO_ASSERTION;
        try {
            Document doc = DocumentBuilderFactoryUtil.parse(samlMsg);

            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.evaluate(ASSERTION_XPATH, doc, XPathConstants.NODE);
            if (node != null) {
                assertion = DocumentBuilderFactoryUtil.toString(node);
            }
        } catch (ParserConfigurationException pce) {
            LOG.error("cannot parse response {}", pce);
        } catch (SAXException saxe) {
            LOG.error("cannot parse response {}", saxe);
        } catch (IOException ioe) {
            LOG.error("cannot parse response {}", ioe);
        } catch (XPathExpressionException xpathe) {
            LOG.error("cannot find the assertion {}", xpathe);
        } catch (TransformerException trfe) {
            LOG.error("cannot output the assertion {}", trfe);
        }

        return assertion;
    }
}
