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
 * <b>File:</b><p>es.gob.afirma.signature.PadesSignerTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>29/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 29/06/2011.
 */
package es.gob.afirma.signature;

import java.util.Properties;

import es.gob.afirma.signature.pades.PadesSigner;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.CryptoUtil;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 29/06/2011.
 */
public class PadesSignerTest extends AbstractSignatureTest{
	
	
	/**
	 * Tests method {@link es.gob.afirma.signature.pades.PadesSigner#sign PadesSigner.sign}.
	 * @throws Exception exception
	 */
	public void testSign() throws Exception {
		PadesSigner ps = new PadesSigner();
		//test con valores nulos
		try {
			ps.sign(null, null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			ps.sign(new byte [0], null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			ps.sign(new byte [0], SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		
		//test con valores inválidos (algoritmo no soportado)
		try {
			ps.sign(getTextDocument(), "MD5withRSA", null, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		//test con valores válidos (firma explícita no soportada en firmas PDF y se ignora parámetro --> se realiza de forma implícita)
		byte [] result = ps.sign(getPdfDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, SignatureConstants.SIGN_MODE_EXPLICIT, getCertificatePrivateKey(), null);
		System.out.println("\n------>>PDF FIRMADO (firma explícita)PDF ------¬  \n" + new String(Base64Coder.encodeBase64(result)));
		
		//test con valores válidos (firma implícita)
		result = ps.sign(getPdfDocument(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, SignatureConstants.SIGN_MODE_IMPLICIT, getCertificatePrivateKey(), null);
		System.out.println("\n------>>PDF FIRMADO (firma implícita)PDF ------¬  \n" + new String(Base64Coder.encodeBase64(result)));
		
		//test con valores válidos (firma implícita con política de firma de AGE)
		Properties extraParams = new Properties();
		extraParams.put(SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP, "MTzLlancG0ULKbnNoepYhicaTKMNzLqa");
		extraParams.put(SignatureProperties.CADES_POLICY_IDENTIFIER_PROP, "2.16.724.1.3.2.2.2.1");
		extraParams.put(SignatureProperties.CADES_POLICY_DIGEST_ALGORITHM_PROP, CryptoUtil.HASH_ALGORITHM_SHA1);
		
		extraParams.put(SignatureProperties.PADES_CONTACT_PROP, "Telvent");
		extraParams.put(SignatureProperties.PADES_LOCATION_PROP, "Seville");
		extraParams.put(SignatureProperties.PADES_REASON_PROP, "Document signed for demonstrate this authenticity");
		
		result = ps.sign(getPdfDocument(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, SignatureConstants.SIGN_MODE_IMPLICIT, getCertificatePrivateKey(), extraParams);
		System.out.println("\n------>>PDF FIRMADO (firma implícita con política de firma de AGE)------¬ \n" + new String(Base64Coder.encodeBase64(result)));
		
		//test con algoritmo inválido para la política de firma (firma de AGE)--> tomará por defecto el algoritmo SHA1.(debe retornar una firma igual que la anterior)
		extraParams.put(SignatureProperties.CADES_POLICY_DIGEST_ALGORITHM_PROP, "ALGORITMO ERRÓNEO");
		assertNotNull(ps.sign(getPdfDocument(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, SignatureConstants.SIGN_MODE_IMPLICIT, getCertificatePrivateKey(), extraParams));
		
		//test sin algoritmo de política de firma.
		extraParams.remove(SignatureProperties.CADES_POLICY_DIGEST_ALGORITHM_PROP);
		assertNotNull(ps.sign(getPdfDocument(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, SignatureConstants.SIGN_MODE_IMPLICIT, getCertificatePrivateKey(), extraParams));
		
		
	}
	
}
