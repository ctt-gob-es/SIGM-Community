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
 * <b>File:</b><p>es.gob.afirma.signature.XadesSignerTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>29/06/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 29/06/2011.
 */
package es.gob.afirma.signature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerFacade;
import es.gob.afirma.signature.xades.ReferenceData;
import es.gob.afirma.signature.xades.ReferenceData.TransformData;
import es.gob.afirma.signature.xades.XadesSigner;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.DSSConstants.DSSTagsRequest;
import es.gob.afirma.utils.DSSConstants.ReportDetailLevel;
import es.gob.afirma.utils.DSSConstants.ResultProcessIds;
import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.UtilsFileSystem;


/** 
 * <p>This class tests {@link es.gob.afirma.signature.xades.XadesSigner XadesSigner} .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 29/06/2011.
 */
public class XadesSignerTest extends AbstractSignatureTest{

	
	/**
	 * Attribute that allow verify signatures against an external validation platform.
	 */
	private static final boolean EXTERNAL_VERIFY = false;

	public void testSignInvalidValues() throws Exception {
		
		XadesSigner xadesSign = new XadesSigner();
		//Prueba con valores nulos
		try {
			xadesSign.sign(null, null, SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			xadesSign.sign(null, SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		
		//Prueba con valores no válidos (algoritmo no soportado)
		try {
			xadesSign.sign(new byte [0], "MD5withRSA", SignatureConstants.SIGN_FORMAT_XADES_DETACHED, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		//Modo de firma no soportado 
		try {
			xadesSign.sign(new byte [0], SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, "XMLDSig Enveloped", getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		
	}
	
	public void testSignBinaryEnveloping() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] dataToSign = getTextDocument();
		byte [] signature = xadesSign.sign(dataToSign, SignatureConstants.SIGN_ALGORITHM_SHA384WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, getCertificatePrivateKey(), getDataFormatParams());
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		
		externalSignVerify(signature);
		
		
	}
	
	public void testSignXmlEnveloping() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		
		byte [] dataToSign = getXmlDocument();
		
		byte [] signature = xadesSign.sign(dataToSign, SignatureConstants.SIGN_ALGORITHM_SHA384WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, getCertificatePrivateKey(), null);
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		
		externalSignVerify(signature);
		
	}
	
	public void testSignBinaryEnveloped() throws Exception {
		try {
			new XadesSigner().sign(getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPED, getCertificatePrivateKey(), null);
		fail("No se ha lanzado la excepción por firma enveloped sobre datos binarios");
		} catch(SigningException e) {}		
		
	}
	
	public void testSignXmlEnveloped() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		
			
		byte [] signature = xadesSign.sign(getXmlDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPED, getCertificatePrivateKey(), getDataFormatParams());
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		externalSignVerify(signature);
	}
	
	public void testSignBinaryDetached() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		
		byte [] signature = xadesSign.sign(getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_DETACHED, getCertificatePrivateKey(), getDataFormatParams());
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		externalSignVerify(signature);
		
	}
	
	public void testSignXmlDetached() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		
		byte [] signature = xadesSign.sign(getXmlDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_DETACHED, getCertificatePrivateKey(), null);
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		externalSignVerify(signature);
		
	}
	
	public void testSignExternallyDetached() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		
		//Creamos listado de propiedades adicionales que incluirá el objeto manifest con todas las referencias externas.
		Properties extraParams = new Properties();
		ReferenceData rd = new ReferenceData("http://www.w3.org/2000/09/xmldsig#sha1", "zyjp8GJOX69990Kkqw8ioPXGExk=");
		String xPath = "self::text()[ancestor-or-self::node()=/Class/e[1]]";
		TransformData transform = rd.new TransformData("http://www.w3.org/2000/09/xmldsig#base64", null);
		TransformData transform2 = rd.new TransformData("http://www.w3.org/TR/1999/REC-xpath-19991116", Collections.singletonList(xPath));
		List<TransformData> transformList = new ArrayList<TransformData> (2);
		transformList.add(transform);
		transformList.add(transform2);
		rd.setTransforms(transformList);
		rd.setId("idAttribute");
		rd.setType("typeAttribute");
		rd.setUri("uriAttribute");
		List<ReferenceData> rdlist = Collections.singletonList(rd);
		extraParams.put(SignatureConstants.MF_REFERENCES_PROPERTYNAME, rdlist);
		
		byte [] signature = xadesSign.sign(null, SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_EXTERNALLY_DETACHED, getCertificatePrivateKey(), extraParams);
		
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		externalSignVerify(signature);
		
	}
		
		
	public void testSignOtherAlgorithms() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] dataToSign = getTextDocument();
		byte [] signature = xadesSign.sign(dataToSign, SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, getCertificatePrivateKey(), null);
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		externalSignVerify(signature);
	}
	
	public void testSignWithPolicy() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] dataToSign = getXmlDocument();
		
		Properties optionalParams = getDataFormatParams();
		optionalParams.putAll(getPolicyParams());
		
		byte [] signature = xadesSign.sign(dataToSign, SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
		                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING, getCertificatePrivateKey(), optionalParams);
		//validamos firma
		assertTrue(xadesSign.verifySignature(signature));
		externalSignVerify(signature);
	}
	
	private Properties getPolicyParams() {
		Properties policyParams = new Properties();
		policyParams.put(SignatureProperties.XADES_POLICY_IDENTIFIER_PROP, "http://www.facturae.es/politica_de_firma_formato_facturae/politica_de_firma_formato_facturae_v3_1.pdf");
		policyParams.put(SignatureProperties.XADES_POLICY_DIGESTVALUE_PROP, "T76hEwl/oPYW7o0EdCXjEWki4as=");
		policyParams.put(SignatureProperties.XADES_POLICY_DESCRIPTION_PROP, "Política de firma electrónica");
		
		policyParams.put(SignatureProperties.XADES_CLAIMED_ROLE_PROP, "emisor");
		return policyParams;
	}
	
	private Properties getDataFormatParams() {
		Properties dataFormatProp = new Properties();
		dataFormatProp.put(SignatureProperties.XADES_DATA_FORMAT_DESCRIPTION_PROP, "Texto plano");
		dataFormatProp.put(SignatureProperties.XADES_DATA_FORMAT_ENCODING_PROP, "utf-8");
		dataFormatProp.put(SignatureProperties.XADES_DATA_FORMAT_MIME_PROP, "text/plain");
		return dataFormatProp;
	}
	
	/**
	 * Tests es.gob.afirma.signature.xades.XadesSigner.coSign method with Xades enveloping signatures.
	 * @throws Exception exception
	 */
	public void testCoSignInvalidValues() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		//Argumentos inválidos.
		try {
			xadesSign.coSign(null, null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			xadesSign.coSign(new byte[]{}, null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			xadesSign.coSign(new byte[]{}, new byte[]{}, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		
		try {
			xadesSign.coSign(new byte[]{}, new byte[]{}, "MD5", null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		//Algoritmo de firma no soportado
		try {
			xadesSign.coSign(new byte[]{}, new byte[]{}, "MD5", getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		//Firma a cofirmar inválida.
		try {
			xadesSign.coSign(new byte[]{}, getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		try {
			xadesSign.coSign("<Sign>".getBytes(), getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		try {
			xadesSign.coSign("<DOC><ds:Signature xmlns:ds='http://www.w3.org/2000/09/xmldsig#'>    <ds:SignedInfo><ds:CanonicalizationMethod Algorithm='http://www.w3.org/TR/2001/REC-xml-c14n-20010315' /><ds:SignatureMethod Algorithm='http://www.w3.org/2000/09/xmldsig#rsa-sha1' /><ds:Reference Id='Reference-cf5d9447-1f91-47bc-b60a-973b0132cbf2' URI='#d13b582e-8b60-47cf-8eaa-eaf0e900550a'><ds:Transforms><ds:Transform Algorithm='http://www.w3.org/2000/09/xmldsig#base64'/></ds:Transforms><ds:DigestMethod Algorithm='http://www.w3.org/2000/09/xmldsig#sha1'/><ds:DigestValue>ySErJYEqdFwOFyp2icgkiDF/MiQ=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue Id='Signature-a4f5a826-2895-4896-9f8b-76374fd2548e-SignatureValue'></ds:SignatureValue></ds:Signature></DOC>"
			                 .getBytes(),
					getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {
			assertTrue(e.getMessage().contains("modo no soportado"));
		}
		try {
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Invalid.xml", true);
			xadesSign.coSign(eSignature, getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {
			assertTrue(e.getMessage().contains("no es válida"));
		}
		
//		try {
//			xadesSign.coSign("<Signature />".getBytes(), getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
//			fail(ERROR_EXCEPTION_NOT_THROWED);
//		} catch (SigningException e) {}
//		
//		//Firma con digest inválido (no coincide con documento a firmar).
//		try {
//			xadesSign.coSign(UtilsFileSystem.readFile("signatures/XAdES-Enveloping-Binary_Invalid.xml", true), getTextDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
//			fail(ERROR_EXCEPTION_NOT_THROWED);
//		} catch (SigningException e) {}
//		
//		//Firma válida y documento inválido (xml en lugar de binario)
//		try {
//			xadesSign.coSign(UtilsFileSystem.readFile("signatures/XAdES-Enveloping-Binary.xml", true), getXmlDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
//			fail(ERROR_EXCEPTION_NOT_THROWED);
//		} catch (SigningException e) {}
//		
//		//Firma válida y documento inválido (binario en lugar de xml)
//		try {
//			xadesSign.coSign(UtilsFileSystem.readFile("signatures/XAdES-Enveloping-Xml.xml", true), getXmlDocument(), SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
//			fail(ERROR_EXCEPTION_NOT_THROWED);
//		} catch (SigningException e) {}
		
	}
	
	/**
	 * Tests es.gob.afirma.signature.xades.XadesSigner.coSign method with Xades enveloping signatures.
	 * @throws Exception exception
	 */
	public void testCoSignEnveloping() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Enveloping-Xml.xml", true);
		byte [] data = getXmlDocument();
		
		//cofirma documento xml
		byte [] coSignature = xadesSign.coSign(eSignature, data, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
		//validación firma
		assertTrue(xadesSign.verifySignature(coSignature));
		externalSignVerify(coSignature);
		
		//cofirma documento binario
		eSignature = UtilsFileSystem.readFile("signatures/XAdES-Enveloping-Binary.xml", true);
		data = getTextDocument();
		coSignature = xadesSign.coSign(eSignature, data, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), getDataFormatParams());
		//validación firma
		assertTrue(xadesSign.verifySignature(coSignature));
		externalSignVerify(coSignature);
	}
	
	/**
	 * Tests es.gob.afirma.signature.xades.XadesSigner.coSign method with Xades enveloped signatures.
	 * @throws Exception exception
	 */
	public void testCoSignEnveloped() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Enveloped.xml", true);
		byte [] data = getXmlDocument();
		assertTrue(xadesSign.verifySignature(eSignature));
		
//		//cofirma documento xml
		@SuppressWarnings("unused")
		byte [] coSignature = xadesSign.coSign(eSignature, data, SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, getCertificatePrivateKey(), getDataFormatParams());
		
		//validación firma
		assertTrue(xadesSign.verifySignature(coSignature)); 
		externalSignVerify(coSignature);
		
	}
	
	/**
	 * Tests es.gob.afirma.signature.xades.XadesSigner.coSign method with Xades detached signatures.
	 * @throws Exception exception
	 */
	public void testCoSignDetached() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Detached-Xml.xml", true);
		byte [] data = getXmlDocument();
		
		//cofirma documento xml
		byte [] coSignature = xadesSign.coSign(eSignature, data, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
		//validación firma
		assertTrue(xadesSign.verifySignature(coSignature));
		externalSignVerify(coSignature);
		
		//cofirma documento binario
		eSignature = UtilsFileSystem.readFile("signatures/XAdES-Detached-Binary.xml", true);
		data = getTextDocument();
		coSignature = xadesSign.coSign(eSignature, data, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), getDataFormatParams());
		//validación firma
		assertTrue(xadesSign.verifySignature(coSignature));
		externalSignVerify(coSignature);
	}
	
	/**
	 * Tests es.gob.afirma.signature.xades.XadesSigner.coSign method with Xades externally signatures.
	 * @throws Exception exception
	 */
	public void testCoSignExternallyDetached() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Externally_Detached.xml", true);
		
		//cofirma documento xml
		byte [] coSignature = xadesSign.coSign(eSignature, null, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), getDataFormatParams());
		//validación firma
		assertTrue(xadesSign.verifySignature(coSignature));
		externalSignVerify(coSignature);
		
	}
	
	/**
	 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with invalid arguments.
	 * @throws Exception exception
	 */
	public void testCounterSignInvalidValues() throws Exception {
		XadesSigner xadesSign = new XadesSigner();
		//Argumentos inválidos.
		try {
			xadesSign.counterSign(null, null, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			xadesSign.counterSign(new byte[0], SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, null, null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try {
			xadesSign.counterSign(new byte[0], "MD5withDSA", getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (IllegalArgumentException e) {}
		
		try { //sin firma 
			xadesSign.counterSign(new byte[0], SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {}
		
		try {//firma inválida
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Invalid.xml", true);
			xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), null);
			fail(ERROR_EXCEPTION_NOT_THROWED);
		} catch (SigningException e) {
			assertTrue(true);
		}
	}
		

		/**
		 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with a XAdES enveloping signature.
		 * @throws Exception exception
		 */
		public void testCounterSignEnveloping() throws Exception {
			XadesSigner xadesSign = new XadesSigner();
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Enveloping-Xml.xml", true);
			
			//contrafirma documento xml
			byte [] counterSign = xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), getDataFormatParams());
			//validación firma
			assertTrue(xadesSign.verifySignature(counterSign));
			externalSignVerify(counterSign);
			
			
		}
		
		/**
		 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with a XAdES enveloped signature.
		 * @throws Exception exception
		 */
		public void testCounterSignEnveloped() throws Exception {
			XadesSigner xadesSign = new XadesSigner();
			byte [] eSignature = xadesSign.sign(getXmlDocument(), SignatureConstants.SIGN_ALGORITHM_SHA1WITHRSA, 
				                                SignatureConstants.SIGN_FORMAT_XADES_ENVELOPED, getCertificatePrivateKey(), getPolicyParams());
			//validamos firma
			assertTrue(xadesSign.verifySignature(eSignature));
			
			//contrafirma documento xml
			byte [] counterSign = xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), getPolicyParams());
			//validación firma
			assertTrue(xadesSign.verifySignature(counterSign));
			externalSignVerify(counterSign);
			
		}
		
		/**
		 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with a XAdES Detached signature.
		 * @throws Exception exception
		 */
		public void testCounterSignDetached() throws Exception {
			XadesSigner xadesSign = new XadesSigner();
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Detached-Xml.xml", true);
			//contrafirma documento xml
			
			Properties optionalParams = getDataFormatParams();
			optionalParams.putAll(getPolicyParams());
			byte [] counterSign = xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA256WITHRSA, getCertificatePrivateKey(), optionalParams);
			//validación firma
			assertTrue(xadesSign.verifySignature(counterSign));
			externalSignVerify(counterSign);
		}
		
		/**
		 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with a XAdES cosignature.
		 * @throws Exception exception
		 */
		public void testCounterSignCoSign() throws Exception {
			XadesSigner xadesSign = new XadesSigner();
			
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Cosign.xml", true);
			//contrafirma documento xml
			byte [] counterSign = xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, getCertificatePrivateKey(), getDataFormatParams());
			//validación firma
			assertTrue(xadesSign.verifySignature(counterSign));
			externalSignVerify(counterSign);
		}
		
		/**
		 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with a XAdES countersignature.
		 * @throws Exception exception
		 */
		public void testCounterSignCounterSign() throws Exception {
			XadesSigner xadesSign = new XadesSigner();
			
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-Countersign.xml", true);
			//contrafirma documento xml
			byte [] counterSign = xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, getCertificatePrivateKey(), null);
			//validación firma
			assertTrue(xadesSign.verifySignature(counterSign));
			externalSignVerify(counterSign);
		}
		
		/**
		 * Tests es.gob.afirma.signature.xades.XadesSigner.counterSign method with a XAdES with parallels and serial signatures.
		 * @throws Exception exception
		 */
		public void testCounterSignCounterCoSign() throws Exception {
			XadesSigner xadesSign = new XadesSigner();
			
			byte [] eSignature = UtilsFileSystem.readFile("signatures/XAdES-CoSign-CounterSign.xml", true);
			//contrafirma documento xml
			byte [] counterSign = xadesSign.counterSign(eSignature, SignatureConstants.SIGN_ALGORITHM_SHA512WITHRSA, getCertificatePrivateKey(), null);
			//validación firma
			assertTrue(xadesSign.verifySignature(counterSign));
			externalSignVerify(counterSign);
		}
		
		/**
		 * Verifies a signature by external validation plataform (@Firma plataform).
		 * @param signature signature to verify.
		 * @throws Exception if happens an error.
		 */
		private void externalSignVerify(byte [] signature) throws Exception {
			if(EXTERNAL_VERIFY) {
				final String appName = "dipucr.sigem_quijote";
				Map<String, Object> inParams = new HashMap<String, Object>();

				inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, appName);
				inParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, Boolean.TRUE.toString());
				inParams.put(DSSTagsRequest.INCLUDE_REVOCATION, Boolean.TRUE.toString());
				inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
				inParams.put(DSSTagsRequest.RETURN_PROCESSING_DETAILS, "");
				inParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
				inParams.put(DSSTagsRequest.ADDICIONAL_REPORT_OPT_SIGNATURE_TIMESTAMP, "urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp");

				//pruebas con firmas de tipo XML
				inParams.put(DSSTagsRequest.SIGNATURE_PTR_ATR_WHICH, "1299585056969008");
				inParams.put(DSSTagsRequest.DOCUMENT_ATR_ID, "1299585056969008");
				inParams.put(DSSTagsRequest.BASE64XML, new String(Base64Coder.encodeBase64(signature)));

				String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
				String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, appName);
				Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST,
				                                                                                      GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
				//validamos si el resultado ha sido satisfactorio
				assertEquals("La firma no es válida según verificación contra la plataforma externa de @Firma", 
				             ResultProcessIds.VALID_SIGNATURE, 
				             propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
			}
		}
}
