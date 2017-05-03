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
 * <b>File:</b><p>es.gob.afirma.utils.CryptoUtilTest.java.</p>
 * <b>Description:</b><p>Class performs cryptographic hash funtions.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/06/2011.
 */
package es.gob.afirma.utils;

import junit.framework.TestCase;


/** 
 * <p>Tests CryptoUtil class.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 28/06/2011.
 */
public final class CryptoUtilTest extends TestCase {

	
	public void testDigest() throws Exception{
		byte[] data = "Test_prueba".getBytes();
		
		byte[] hash = CryptoUtil.digest(CryptoUtil.HASH_ALGORITHM_SHA1, data);
		String hashEncoded = new String(Base64Coder.encodeBase64(hash));
		assertEquals("71OLp4USkobZLnrT7Xvz2lELNAU=", hashEncoded);
		
		hash = CryptoUtil.digest(CryptoUtil.HASH_ALGORITHM_SHA256, data);
		hashEncoded = new String(Base64Coder.encodeBase64(hash));
		assertEquals("nuK6g3/q0exjbwildPBINNu7B/pd25XWcodW+kbSGc8=", hashEncoded);
		
		hash = CryptoUtil.digest(CryptoUtil.HASH_ALGORITHM_SHA384, data);
		hashEncoded = new String(Base64Coder.encodeBase64(hash));
		assertEquals("LqXgsJ9tQajoll8IKXQRJWpE/sJeiNjUSKJVYo6oESzKkhQNJuc4kjNyCHEcXcb/", hashEncoded);
		
		hash = CryptoUtil.digest(CryptoUtil.HASH_ALGORITHM_SHA512, data);
		hashEncoded = new String(Base64Coder.encodeBase64(hash));
		assertEquals("rvGVeH3jYDC5klpLr0kdwDVQ/2ENcuKNGByR0ooXYIu7HKqTdRBbBfHOC8sdV+8FAa1uery4Oc4L\nzFyuDVcbLw==", hashEncoded);
		
	}
	
}
