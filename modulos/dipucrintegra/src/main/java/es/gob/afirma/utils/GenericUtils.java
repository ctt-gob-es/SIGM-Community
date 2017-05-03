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

/**
 * <b>File:</b><p>es.gob.afirma.utils.GenericUtils.java.</p>
 * <b>Description:</b><p>Class with generic utilities.</p>
 * <b>Project:</b><p@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>18/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 18/03/2011.
 */
package es.gob.afirma.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersException;

/**
 * <p>Class with generic utilities.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 18/03/2011.
 */
public final class GenericUtils {

    /**
     * Constructor method for the class GeneralUtils.java.
     */
    private GenericUtils() {
    }

    /**
     * Asserts whether a value is valid (not null or not empty).
     * @param value string to validate.
     * @return true if string is valid and false otherwise.
     */
    public static boolean assertStringValue(String value) {

	if (value != null && !value.isEmpty()) {
	    return true;
	}
	return false;
    }

    /**
     * Asserts whether a array is valid (not null and not empty).
     * @param data array to validate.
     * @return true if array is valid and false otherwise.
     */
    public static boolean assertArrayValid(byte[ ] data) {

	if (data != null && data.length > 0) {
	    return true;
	}
	return false;
    }

    /**
     * Retrieves a value from a tree of several maps by a path given.
     * @param path path of maps tree separated by  '/'.
     * @param treeValues collection of maps (type: Map<String, Object>).
     * @return value of the key requested.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String getValueFromMapsTree(String path, Map<String, Object> treeValues) {
	String value = null;
	if (assertStringValue(path) && treeValues != null && !treeValues.isEmpty()) {
	    String[ ] keyNames = path.split(UtilsXML.PATH_DELIMITER);
	    Map<String, Object> mapTmp = treeValues;
	    for (int i = 0; i < keyNames.length; i++) {
		Object mapValue = mapTmp.get(keyNames[i]);
		if (mapValue instanceof Map) {
		    mapTmp = (Map) mapValue;
		    continue;
		} else if (keyNames.length - 1 == i && mapValue instanceof String) {
		    value = mapValue.toString();
		    break;
		} else {
		    break;
		}
	    }

	}
	return value;
    }

    /**
     * Reads and converts a bytes stream to byte array.
     * @param input input stream to convert.
     * @return byte array with data.
     * @throws IOException in error case
     */
    public static byte[ ] getDataFromInputStream(final InputStream input) throws IOException {
	if (input == null) {
	    return new byte[0];
	}
	int nBytes = 0;
	byte[ ] buffer = new byte[NumberConstants.INT_4096];
	final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	while ((nBytes = input.read(buffer)) != -1) {
	    baos.write(buffer, 0, nBytes);
	}
	return baos.toByteArray();
    }

    /**
     * Checks if a value is null.
     * @param values collection of values to validate.
     * @return true if any parameter is null and false if all parameters are valid (not null).
     */
    public static boolean checkNullValues(Object... values) {
	for (Object object: values) {
	    if (object == null) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Prints the resulting  data in base64 format.
     * @param result result bytes.
     * @param logger logger object used for print.
     * @return bytes given as input parameters.
     */
    public static byte[ ] printResult(byte[ ] result, Logger logger) {
	if (logger.isDebugEnabled()) {
	    try {
		logger.debug(Language.getResIntegra(ILogConstantKeys.GU_LOG001));
		logger.debug(new String(Base64Coder.encodeBase64(result)));
	    } catch (TransformersException e) {
		logger.error(e);
	    }
	}
	return result;
    }

}
