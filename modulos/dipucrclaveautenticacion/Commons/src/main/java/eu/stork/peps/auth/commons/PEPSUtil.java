/*
 * This work is Open Source and licensed by the European Commission under the
 * conditions of the European Public License v1.1 
 *  
 * (http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1); 
 * 
 * any use of this file implies acceptance of the conditions of this license. 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package eu.stork.peps.auth.commons;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.UrlBase64;

import eu.stork.peps.auth.commons.exceptions.InternalErrorPEPSException;
import eu.stork.peps.auth.commons.exceptions.InvalidParameterPEPSException;

/**
 * This class holds static helper methods.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.75 $, $Date: 2010-11-23 00:05:35 $
 */
public final class PEPSUtil {

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(PEPSUtil.class.getName());

    /**
     * Configurations object.
     */
    private static Properties configs;

    /**
     * Max prefix.
     */
    private static final String MAX_PARAM_PREFIX = "max.";

    /**
     * Code prefix to get error code.
     */
    private static final String CODE_PARAM_SUFFIX = ".code";

    /**
     * param's size prefix to get max param size.
     */
    private static final String MAX_PARAM_SUFFIX = ".size";

    /**
     * Message prefix to get error message.
     */
    private static final String MSG_PARAM_SUFFIX = ".message";

    /**
     * Private constructor. Prevents the class from being instantiated.
     */
    private PEPSUtil() {
        // empty constructor
    }

    /**
     * Creates a single instance of this class and sets the properties.
     *
     * @param nConfigs The set of available configurations.
     *
     * @return The created PEPSUtil's class.
     */
    public static PEPSUtil createInstance(final Properties nConfigs) {
        if (nConfigs != null) {
            PEPSUtil.configs = nConfigs;
        }
        return new PEPSUtil();
    }

    /**
     * Getter for the Properties.
     *
     * @return configs The properties value.
     */
    public Properties getConfigs() {
        return configs;
    }

    /**
     * Setter for the Properties.
     *
     * @param nConfigs The new properties value.
     */
    public static void setConfigs(final Properties nConfigs) {
        if (nConfigs != null) {
            PEPSUtil.configs = nConfigs;
        }
    }

    /**
     * Returns the identifier of some configuration given a set of
     * configurations and the corresponding configuration key.
     *
     * @param configKey The key that IDs some configuration.
     *
     * @return The configuration String value.
     */
    public static String getConfig(final String configKey) {
        return configs.getProperty(configKey);
    }

    /**
     * Validates the input paramValue identified by the paramName.
     *
     * @param paramName The name of the parameter to validate.
     * @param paramValue The value of the parameter to validate.
     *
     * @return true if the parameter is valid.
     */
    public static boolean isValidParameter(final String paramName,
            final String paramValue) {

        final String validationParam
                = PEPSUtil.getConfig(PEPSParameters.VALIDATION_ACTIVE.toString());
        boolean retVal = true;

        final String paramConf = MAX_PARAM_PREFIX + paramName + MAX_PARAM_SUFFIX;

        if (PEPSValues.TRUE.toString().equals(validationParam)) {
            final String paramSizeStr = PEPSUtil.getConfig(paramConf);
            // Checking if the parameter size exists and if it's numeric
            if (StringUtils.isNumeric(paramSizeStr)) {
                final int maxParamSize = Integer.valueOf(paramSizeStr);
                if (StringUtils.isEmpty(paramValue)
                        || paramValue.length() > maxParamSize) {
                    retVal = false;
                    LOG.warn("Invalid parameter [" + paramName + "] value " + paramValue);
                }
            } else {
                retVal = false;
                LOG.error("Missing " + paramConf
                        + " configuration in the pepsUtils.properties configuration file");
            }
        }
        return retVal;
    }

    /**
     * Validates the Parameter and throws an exception if an error occurs.
     * Throws an InvalidParameterPEPSException runtime exception if the
     * parameter is invalid.
     *
     * @param className The Class Name that invoked the method.
     * @param paramName The name of the parameter to validate.
     * @param paramValue The value of the parameter to validate.
     */
    public static void validateParameter(final String className,
            final String paramName, final Object paramValue) {

        if (paramValue == null) {
            PEPSUtil.validateParameter(className, paramName, "");
        } else {
            PEPSUtil.validateParameter(className, paramName, paramValue.toString());
        }
    }

    /**
     * Validates the Parameters and throws an exception if an error occurs.
     *
     * @param className The Class Name that invoked the method.
     * @param paramName The name of the parameter to validate.
     * @param paramValue The value of the parameter to validate.
     */
    public static void validateParameter(final String className,
            final String paramName, final String paramValue) {

        PEPSUtil.validateParameter(className, paramName, paramValue,
                PEPSUtil.getErrorCode(paramName), PEPSUtil.getErrorMessage(paramName));
    }

    /**
     * Validates the Parameters and throws an exception if an error occurs.
     *
     * @param className The Class Name that invoked the method.
     * @param paramName The name of the parameter to validate.
     * @param paramValue The value of the parameter to validate.
     * @param error The PEPSError to get error code and messages from configs.
     */
    public static void validateParameter(final String className,
            final String paramName, final String paramValue, final PEPSErrors error) {

        PEPSUtil.validateParameter(className, paramName, paramValue,
                PEPSUtil.getConfig(error.errorCode()),
                PEPSUtil.getConfig(error.errorMessage()));
    }

    /**
     * Validates the HTTP Parameter and throws an exception if an error occurs.
     * Throws an InvalidParameterPEPSException runtime exception if the
     * parameter is invalid.
     *
     * @param className The Class Name that invoked the method.
     * @param paramName The name of the parameter to validate.
     * @param paramValue The value of the parameter to validate.
     * @param errorCode The error code to include on the exception.
     * @param errorMessage The error message to include on the exception.
     */
    public static void validateParameter(final String className,
            final String paramName, final String paramValue, final String errorCode,
            final String errorMessage) {

        if (!isValidParameter(paramName, paramValue)) {
            LOG.warn("Invalid parameter [" + paramName + "] value found at "
                    + className);
            throw new InvalidParameterPEPSException(errorCode, errorMessage);
        }
    }

    /**
     * Getter for the error code of some given error related to the input param.
     *
     * @param paramName The name of the parameter associated with the error.
     *
     * @return The code of the error.
     */
    private static String getErrorCode(final String paramName) {
        return getConfig(paramName + CODE_PARAM_SUFFIX);
    }

    /**
     * Getter for the error message of some given error related to the input
     * parameter.
     *
     * @param paramName The name of the parameter associated with the message.
     *
     * @return The message for the error.
     */
    private static String getErrorMessage(final String paramName) {
        return getConfig(paramName + MSG_PARAM_SUFFIX);
    }

    /**
     * {@link Base64} encodes the input samlToken parameter.
     *
     * @param samlToken the SAML Token to be encoded.
     *
     * @return The Base64 String representing the samlToken.
     *
     * @see Base64#encode
     */
    public static String encodeSAMLToken(final byte[] samlToken) {
        try {
            return new String(Base64.encode(samlToken), "UTF8");
        } catch (final UnsupportedEncodingException e) {
            LOG.error(PEPSErrors.INTERNAL_ERROR.errorMessage(), e);
            return null;
        }
    }

    /**
     * Encode samltoken url safe
     *
     * @param samlToken the saml token to encode
     * @return the bas64 encoded string
     */
    public static String encodeSAMLTokenUrlSafe(final byte[] samlToken) {
        try {
            return new String(UrlBase64.encode(samlToken), "UTF8");
        } catch (final UnsupportedEncodingException e) {
            LOG.error(PEPSErrors.INTERNAL_ERROR.errorMessage(), e);
            return null;
        }
    }

    /**
     * Decodes the {@link Base64} String input parameter representing a
     * samlToken.
     *
     * @param samlToken the SAML Token to be decoded.
     *
     * @return The samlToken decoded bytes.
     *
     * @see Base64#decode
     */
    public static byte[] decodeSAMLToken(final String samlToken) {
        return Base64.decode(samlToken);
    }

    /**
     * Decode URL save base64 saml token
     *
     * @param samlToken the SAML toke to decode
     * @return The decoded bytes
     */
    public static byte[] decodeSAMLTokenUrlSafe(final String samlToken) {
        return UrlBase64.decode(samlToken);
    }

    /**
     * Hashes a SAML token. Throws an InternalErrorPEPSException runtime
     * exception if the Cryptographic Engine fails.
     *
     * @param samlToken the SAML Token to be hashed.
     *
     * @return byte[] with the hashed SAML Token.
     */
    public static byte[] hashPersonalToken(final byte[] samlToken) {
        try {
            final String className
                    = PEPSUtil.getConfig(PEPSValues.HASH_DIGEST_CLASS.toString());

            final Digest digest
                    = (Digest) Class.forName(className).getConstructor()
                    .newInstance((Object[]) null);
            digest.update(samlToken, 0, samlToken.length);

            final int retLength = digest.getDigestSize();
            final byte[] ret = new byte[retLength];

            digest.doFinal(ret, 0);
            return ret;

        } catch (final Exception e) {
            // For all those exceptions that could be thrown, we always log it and
            // thrown an InternalErrorPEPSException.
            LOG.error(PEPSErrors.HASH_ERROR.errorMessage(), e);
            throw new InternalErrorPEPSException(
                    PEPSUtil.getConfig(PEPSErrors.HASH_ERROR.errorCode()),
                    PEPSUtil.getConfig(PEPSErrors.HASH_ERROR.errorMessage()), e);
        }
    }

    /**
     * Gets the Stork error code in the error message if exists!
     *
     * @param errorMessage The message to get the error code if exists;
     *
     * @return the error code if exists. Returns null otherwise.
     */
    public static String getStorkErrorCode(final String errorMessage) {
        if (StringUtils.isNotBlank(errorMessage)
                && errorMessage.indexOf(PEPSValues.ERROR_MESSAGE_SEP.toString()) >= 0) {
            final String[] msgSplitted
                    = errorMessage.split(PEPSValues.ERROR_MESSAGE_SEP.toString());
            if (msgSplitted.length == 2 && StringUtils.isNumeric(msgSplitted[0])) {
                return msgSplitted[0];
            }
        }
        return null;
    }

    /**
     * Gets the Stork error message in the saml message if exists!
     *
     * @param errorMessage The message to get in the saml message if exists;
     *
     * @return the error message if exists. Returns the original message
     * otherwise.
     */
    public static String getStorkErrorMessage(final String errorMessage) {
        if (StringUtils.isNotBlank(errorMessage)
                && errorMessage.indexOf(PEPSValues.ERROR_MESSAGE_SEP.toString()) >= 0) {
            final String[] msgSplitted
                    = errorMessage.split(PEPSValues.ERROR_MESSAGE_SEP.toString());
            if (msgSplitted.length == 2 && StringUtils.isNumeric(msgSplitted[0])) {
                return msgSplitted[1];
            }
        }
        return errorMessage;
    }

    /**
     * Get inputstream from string
     *
     * @param string the string to convert
     * @param codePage the codepage of string
     * @return an inputstream
     * @throws UnsupportedEncodingException
     */
    public static InputStream getStream(final String string, final String codePage) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(string.getBytes(codePage));
    }
}
