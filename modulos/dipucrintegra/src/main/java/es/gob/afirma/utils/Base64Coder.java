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
 * <b>File:</b><p>es.gob.afirma.utils.Base64Coder.java.</p>
 * <b>Description:</b><p>Utility class for coding and decoding binary data in BASE64 format.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>23/02/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 23/02/2011.
 */
package es.gob.afirma.utils;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.transformers.TransformersException;

/**
 * <p>Utility class for coding and decoding binary data in BASE64 format.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 18/03/2011.
 */
public final class Base64Coder {

	/**
	 * Constructor method for the class Base64Coder.java.
	 */
	private Base64Coder() {
	}

	/**
	 * Encodes a byte array in base64.
	 * @param data data to encoder.
	 * @return codificated data.
	 * @throws TransformersException if an error happends
	 */
	public static byte[ ] encodeBase64(byte[ ] data) throws TransformersException {
		return encodeBase64(data, 0, data.length);
	}

	/**
	 * Encodes a byte array in base64.
	 * @param data data to encoder.
	 * @param offset offset.
	 * @param len length.
	 * @return codificated data.
	 * @throws TransformersException if an error happends
	 */
	public static byte[ ] encodeBase64(byte[ ] data, int offset, int len) throws TransformersException {
		UtilsBase64 encoder = null;
		byte result[] = null;

		if (data == null) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG001));
		}
		try {
			encoder = new UtilsBase64();

			result = UtilsBase64.encodeBytes(data, offset, len).getBytes();

			return result == null ? data : result;
		} catch (Exception e) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG002), e);
		}
	}

	/**
	 * Decodes a base64 byte array.
	 * @param data data to decoder.
	 * @return decodificated data.
	 * @throws TransformersException if an error happends
	 */
	public static byte[ ] decodeBase64(byte[ ] data) throws TransformersException {
		return decodeBase64(data, 0, data.length);
	}

	/**
	 * 
	 * Decodes a base64 byte array.
	 * @param data data to decoder.
	 * @param offset offset.
	 * @param len length.
	 * @return decodificated data.
	 * @throws TransformersException if an error happends
	 */
	public static byte[ ] decodeBase64(byte[ ] data, int offset, int len) throws TransformersException {
		UtilsBase64 decoder = null;
		byte result[] = null;

		if (data == null) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG001));
		}
		try {
			decoder = new UtilsBase64();

			result = UtilsBase64.decode(data, offset, len);

			return result == null ? data : result;
		} catch (Exception e) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG003), e);
		}
	}

	/**
	 * Checks if a string is encoded in Base64.
	 * @param data data to check.
	 * @return true if the string is encoded in base 64 and false otherwise.
	 * @throws TransformersException if the method fails.
	 */
	public static boolean isBase64Encoded(byte[ ] data) throws TransformersException {

		UtilsBase64 decoder = null;
		byte result[] = null;

		if (data == null) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG001));
		}

		try {
			decoder = new UtilsBase64();
			result = UtilsBase64.decode(new String(data));

			if (result == null || result.length == 0) {
				return false;
			}
			return true;
		} catch (Exception e) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG004), e);
		}

	}

	/**
	 * Encodes a string in base64 format.
	 * @param data string to encoded.
	 * @return a string encoded in base64.
	 * @throws TransformersException if an error happends
	 */
	public static String encodeBase64(String data) throws TransformersException {
		if (data == null) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG001));
		}
		try {
			byte[ ] content = data.getBytes();
			new UtilsBase64();
			return UtilsBase64.encodeBytes(content, 0, content.length);
		} catch (Exception e) {
			throw new TransformersException(e);
		}
	}

	/**
	 * Decodes a base64 string.
	 * @param data base64 string encoded.
	 * @return a string decoded.
	 * @throws TransformersException if an error happends
	 */
	public static String decodeBase64(String data) throws TransformersException {
		if (data == null) {
			throw new TransformersException(Language.getResIntegra(ILogConstantKeys.BC_LOG001));
		}
		try {
			new UtilsBase64();
			return UtilsBase64.decodeToString(data);
		} catch (Exception e) {
			throw new TransformersException(e);
		}
	}

}
