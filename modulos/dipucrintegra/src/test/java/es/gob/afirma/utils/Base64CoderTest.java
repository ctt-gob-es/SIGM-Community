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
 * <b>File:</b><p>es.gob.afirma.utils.Base64CoderTest.java.</p>
 * <b>Description:</b><p>Class tests Base64Coder.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>23/02/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 23/02/2011.
 */
package es.gob.afirma.utils;

import junit.framework.TestCase;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.utils.Base64Coder;




/**
 * <p>Tests Base64Coder class</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 18/03/2011.
 */
public final class Base64CoderTest extends TestCase
{
	private final String ASCII_SAMPLE = "This an example..@Ññàèìòù%$!?¿Z[]^_`}~";
	private final String BASE64_ENCODED_SAMPLE = "VGhpcyBhbiBleGFtcGxlLi5Aw5HDscOgw6jDrMOyw7klJCE/wr9aW11eX2B9fg==";
	
	/**
	 * Tests decodeBase64 method
	 * @throws Exception if hapends an error.
	 */
	public void testDecodeBase64() throws Exception {
		
		//tests with invalid parameters.
		String nullData = null;
		try {
			Base64Coder.decodeBase64(nullData);
		fail("No se ha lanzado la excepción");
		} catch (TransformersException e) {}
		
		Base64Coder.decodeBase64("");
		Base64Coder.decodeBase64(new byte[0]);

		try {
			Base64Coder.decodeBase64(null, 0, 0);
		fail("No se ha lanzado la excepción");
		} catch (TransformersException e) {}
		
		
		//test with byte array
		byte [] result = Base64Coder.decodeBase64(BASE64_ENCODED_SAMPLE.getBytes());
		assertEquals(ASCII_SAMPLE, new String(result));
		
		//test with all parameters
		result = Base64Coder.decodeBase64(BASE64_ENCODED_SAMPLE.getBytes(), 0, BASE64_ENCODED_SAMPLE.getBytes().length);
		assertEquals(ASCII_SAMPLE, new String(result));

		//test with string
		assertEquals(ASCII_SAMPLE, Base64Coder.decodeBase64(BASE64_ENCODED_SAMPLE));
	}
	
	/**
	 * Tests encodeBase64 method
	 * @throws Exception if hapends an error.
	 */
	public void testEncodeBase64() throws Exception {
		//tests with invalid parameters.
		String nullData = null;
		try {
			Base64Coder.encodeBase64(nullData);
		fail("No se ha lanzado la excepción");
		} catch (TransformersException e) {}
		
		Base64Coder.encodeBase64("");
		Base64Coder.encodeBase64(new byte[0]);

		try {
			Base64Coder.encodeBase64(null, 0, 0);
		fail("No se ha lanzado la excepción");
		} catch (TransformersException e) {}
		
		
		//test with byte array
		byte [] result = Base64Coder.encodeBase64(ASCII_SAMPLE.getBytes());
		assertEquals(BASE64_ENCODED_SAMPLE, new String(result));
		
		//test with all parameters
		result = Base64Coder.encodeBase64(ASCII_SAMPLE.getBytes(), 0, ASCII_SAMPLE.getBytes().length);
		assertEquals(BASE64_ENCODED_SAMPLE, new String(result));

		//test with string
		assertEquals(BASE64_ENCODED_SAMPLE, Base64Coder.encodeBase64(ASCII_SAMPLE));
	}
	
	
	
	/**
	 * Tests isBase64Encoded method
	 * @throws Exception if hapends an error.
	 */
	public void testIsBase64Encoded() throws Exception {
		assertTrue(Base64Coder.isBase64Encoded(BASE64_ENCODED_SAMPLE.getBytes()));
		assertFalse(Base64Coder.isBase64Encoded(ASCII_SAMPLE.getBytes()));
	}
	
}
