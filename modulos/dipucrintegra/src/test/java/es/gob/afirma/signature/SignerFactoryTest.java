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
 * <b>File:</b><p>es.gob.afirma.signature.SignerFactoryTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>28/09/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 28/09/2011.
 */
package es.gob.afirma.signature;

import es.gob.afirma.signature.cades.CadesSigner;
import es.gob.afirma.signature.pades.PadesSigner;
import es.gob.afirma.signature.xades.XadesSigner;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 28/09/2011.
 */
public class SignerFactoryTest extends AbstractSignatureTest {
	
	public void testGetSigner () throws Exception {
		
		// test con valor nulo;
		try {
			SignersFactory.getInstance().getSigner(null);
			fail();
		} catch (SigningException e) {}
		// test con valor no válido (formato de firma no soportado)
		try {
			assertNull(SignersFactory.getInstance().getSigner("CMS/PKCS#7"));
			fail();
		} catch (SigningException e) {}

		// test con valores válidos (todos los tipos de firma)
		Signer signer = SignersFactory.getInstance().getSigner(SignatureConstants.SIGN_FORMAT_CADES);
		assertTrue(signer instanceof CadesSigner);

		signer = SignersFactory.getInstance().getSigner(SignatureConstants.SIGN_FORMAT_PADES);
		assertTrue(signer instanceof PadesSigner);

		signer = SignersFactory.getInstance().getSigner(SignatureConstants.SIGN_FORMAT_XADES_DETACHED);
		assertTrue(signer instanceof XadesSigner);

		signer = SignersFactory.getInstance().getSigner(SignatureConstants.SIGN_FORMAT_XADES_ENVELOPED);
		assertTrue(signer instanceof XadesSigner);

		signer = SignersFactory.getInstance().getSigner(SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING);
		assertTrue(signer instanceof XadesSigner);

		signer = SignersFactory.getInstance().getSigner(SignatureConstants.SIGN_FORMAT_XADES_EXTERNALLY_DETACHED);
		assertTrue(signer instanceof XadesSigner);
	}

}
