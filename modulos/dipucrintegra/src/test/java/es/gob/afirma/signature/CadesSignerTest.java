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
 * <b>File:</b><p>es.gob.afirma.signature.CadesSignerTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>29/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 29/06/2011.
 */
package es.gob.afirma.signature;

import java.util.Properties;

import es.gob.afirma.signature.cades.CadesSigner;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.UtilsFileSystem;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 29/06/2011.
 */
public class CadesSignerTest extends AbstractSignatureTest{
	
	
	
	public void testVerifySignature() throws Exception {
		//test con valores nulos
		CadesSigner cs = new CadesSigner();
				
		try {
			cs.verifySignature(null, new byte [0]);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		//test con valores inválidos (documento a firmar vacío)
		assertFalse(cs.verifySignature(getCadesSignature(), new byte[1]));
		
		//test con valores inválidos (sin datos de la firma)
		try {
			cs.verifySignature(new byte [1], getTextDocument());
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		//test con valores válidos
		assertTrue(cs.verifySignature(getCadesSignature(), getTextDocument()));
	}
	
	public void testSign() throws Exception {
		CadesSigner cs = new CadesSigner();
		//test con valores nulos
		try {
			cs.sign(null, null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cs.sign(new byte [0], null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cs.sign(new byte [0], SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		
		//test con valores inválidos (algoritmo no soportado)
		try {
			cs.sign(getTextDocument(), "MD5withRSA", null, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		//test con valores válidos (firma explícita)
		byte [] result =cs.sign(getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, SignatureConstants.SIGN_MODE_EXPLICIT, getCertificatePrivateKey(), null);
		//validamos firma
		assertTrue(cs.verifySignature(result, getTextDocument()));
		System.out.println("\n-->>FIRMA RESULTANTE (explícita): \n" + new String(Base64Coder.encodeBase64(result)));
		
		//test con valores válidos (firma implícita)
		result = cs.sign(getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, SignatureConstants.SIGN_MODE_IMPLICIT, getCertificatePrivateKey(), null);
		System.out.println("\n-->>FIRMA RESULTANTE (implícita): \n" + new String(Base64Coder.encodeBase64(result)));
		//validamos firma
		assertTrue(cs.verifySignature(result, getTextDocument()));
		
		//test con valores válidos (firma implícita con política de firma de AGE)
		Properties extraParams = new Properties();
		extraParams.put(SignatureProperties.CADES_POLICY_DIGESTVALUE_PROP, "kn5sVW8nOUglMdzJYapHWhBeYWE=");
		extraParams.put(SignatureProperties.CADES_POLICY_IDENTIFIER_PROP, "2.16.724.1.3.1.1.2.1.8");
		
		result = cs.sign(getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, SignatureConstants.SIGN_MODE_IMPLICIT, getCertificatePrivateKey(), extraParams);
		System.out.println("\n-->>FIRMA RESULTANTE (firma implícita con política de firma de AGE): \n" + new String(Base64Coder.encodeBase64(result)));
		//validamos firma
		assertTrue(cs.verifySignature(result, getTextDocument()));
		
	}
	
	public void testCounterSign () throws Exception {
		CadesSigner cs = new CadesSigner();
		//test con valores nulos
		try {
			cs.counterSign(null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cs.counterSign(new byte [0], null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cs.counterSign(new byte [0], SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		
		//test con valores inválidos (algoritmo no soportado)
		try {
			cs.counterSign(getTextDocument(), "MD5withRSA", getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		//test con valores válidos
		byte [] result =cs.counterSign(getCadesSignature(), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, getCertificatePrivateKey(), null);
		//validamos firma
		assertTrue(cs.verifySignature(result, getTextDocument()));
		
		//test contrafirma de una contrafirma
		result = cs.counterSign(UtilsFileSystem.readFile("signatures/CADES_counterSign.p7s", true), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, getCertificatePrivateKey(), null);
		//validamos la contrafirma
		assertTrue(cs.verifySignature(result, getTextDocument()));
		
		//test firma con dos contrafirmas (en cascada) y una cofirma
		result = cs.counterSign(UtilsFileSystem.readFile("signatures/CADES_2CounterSign-1Cosign.p7s", true), SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, getCertificatePrivateKey(), null);
		//validamos la contrafirma
		assertTrue(cs.verifySignature(result, getTextDocument()));
	}
	
	public void testCoSign () throws Exception {
		CadesSigner cadesSigner = new CadesSigner();
		//test con valores nulos
		try {
			cadesSigner.coSign(null, null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cadesSigner.coSign(new byte [0], null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cadesSigner.coSign(new byte [0], new byte [0], null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			cadesSigner.coSign(new byte [0], new byte [0],SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		//test con valores válidos.
			//cofirma simple
		byte[] result = cadesSigner.coSign(getCadesSignature(),getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
		assertTrue(cadesSigner.verifySignature(result, getTextDocument()));
			
			//cofirma de una firma con 2 contrafirmas (en cascada)
		result = cadesSigner.coSign(UtilsFileSystem.readFile("signatures/CADES_2CounterSign.p7s", true), getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA384WITHRSA, getCertificatePrivateKey(), null);
		assertTrue(cadesSigner.verifySignature(result, getTextDocument()));
	}
	
}
