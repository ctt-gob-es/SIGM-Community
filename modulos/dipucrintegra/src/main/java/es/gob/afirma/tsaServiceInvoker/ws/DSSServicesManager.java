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

package es.gob.afirma.tsaServiceInvoker.ws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.xml.security.c14n.Canonicalizer;

import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerFacade;
import es.gob.afirma.afirma5ServiceInvoker.ws.WebServiceInvoker;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerFacade;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.DSSConstants;
import es.gob.afirma.utils.DSSConstants.AlgorithmTypes;
import es.gob.afirma.utils.DSSConstants.DSSTagsRequest;
import es.gob.afirma.utils.DSSConstants.ReportDetailLevel;
import es.gob.afirma.utils.DSSConstants.ResultProcessIds;
import es.gob.afirma.utils.DSSConstants.SignTypesURIs;
import es.gob.afirma.utils.DSSConstants.SignatureForm;
import es.gob.afirma.utils.DSSConstants.XmlSignatureMode;
import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.UtilsBase64;
import es.gob.afirma.utils.UtilsFileSystem;

import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class DSSServicesManager extends TestCase {

    /**
     * Attribute that represents application name for tests.
     */
    private static final String APPLICATION_NAME = "id_aplicacion.en@firma";
    
    private static final Logger LOGGER = Logger.getLogger(WebServiceInvoker.class);

    /**
     * Attribute that represents server signer for tests.
     */
    private static final String SERVER_SIGNER_NAME = "serversigner";

    /**
     * Attribute that represents xml signature in base64.
     */
    private static String signatureB64XML = null;

    /**
     * Attribute that represents xml enveloping signature.
     */
    private static String signXMLEnveloping = null;

    /**
     * Attribute that represents xml signature in base64.
     */
    private static String signatureB64 = null;

    /**
     * Attribute that represents signature archive Identifier .
     */
    private static String archiveIdentifier = null;

    /**
     * Attribute that represents signature archive Identifier .
     */
    private static String batchVerifyResultId = null;

    /**
     * Attribute that represents certificate path.
     */
    private static final String CERTIFICATE_NAME = "NOMBRE_JIMENEZ_MORENO_AGUSTIN_FNMT_ID_old.cer";

    public void doDSSAfirmaSign() throws Exception {

	String documentB64 = UtilsFileSystem.readFileBase64Encoded("ficheroAfirmar.txt", true);

	Map<String, Object> inParams = new HashMap<String, Object>();
	inParams.put(DSSTagsRequest.BASE64DATA, documentB64);
	inParams.put(DSSTagsRequest.KEY_SELECTOR, SERVER_SIGNER_NAME);
	inParams.put(DSSTagsRequest.SIGNATURE_TYPE, SignTypesURIs.CMS);
	inParams.put(DSSTagsRequest.HASH_ALGORITHM, AlgorithmTypes.SHA1);
	inParams.put(DSSTagsRequest.ADDITIONAL_DOCUMENT_NAME, "ficheroAfirmar.txt");
	inParams.put(DSSTagsRequest.ADDITIONAL_DOCUMENT_TYPE, "txt");
	
	// paramámetros erróneos (generan un excepción).
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, null);
	
	try {
	    TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	    fail("no se ha lanzado la excepción por parámetros inválidos");
	} catch (TransformersException e) {}

	// paramámetros no válidos (generan un mensaje de respuesta con un
	// error)
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, "no_valido");
	String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.REQUESTER_ERROR, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	// parámetros válidos (firma de tipo CADES y XADES)
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);

	// ->CADES
	inParams.put(DSSTagsRequest.SIGNATURE_TYPE, SignTypesURIs.CADES);
	inParams.put(DSSTagsRequest.SIGNATURE_FORM, SignatureForm.BES);
	xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, APPLICATION_NAME);
	propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	assertNotNull(propertiesResult);
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	assertEquals(SignatureForm.BES, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureForm")));
	signatureB64 = propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureBase64")).toString();

	// ->XADES DETACHED
	inParams.put(DSSTagsRequest.SIGNATURE_TYPE, SignTypesURIs.XADES_V_1_3_2);
	inParams.put(DSSTagsRequest.SIGNATURE_FORM, SignatureForm.BES);
	inParams.put(DSSTagsRequest.XML_SIGNATURE_MODE, XmlSignatureMode.DETACHED);
	xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, APPLICATION_NAME);
	propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	signatureB64XML = propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureBase64XML")).toString();
	archiveIdentifier = propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ArchiveIdentifier")).toString();
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	assertEquals(SignatureForm.BES, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureForm")));

	// ->XADES ENVELOPING
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);

	inParams.put(DSSTagsRequest.SIGNATURE_TYPE, SignTypesURIs.XADES_V_1_3_2);
	inParams.put(DSSTagsRequest.SIGNATURE_FORM, SignatureForm.BES);
	inParams.put(DSSTagsRequest.XML_SIGNATURE_MODE, XmlSignatureMode.ENVELOPING);
	xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, APPLICATION_NAME);
	propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	signXMLEnveloping = propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureXML")).toString();
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	assertEquals(SignatureForm.BES, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureForm")));
    }

    public void doDSSAfirmaCoSign() throws Exception {

	Map<String, Object> inParams = new HashMap<String, Object>();
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);
	inParams.put(DSSTagsRequest.KEY_SELECTOR, SERVER_SIGNER_NAME);
	inParams.put(DSSTagsRequest.HASH_ALGORITHM, AlgorithmTypes.SHA1);
	inParams.put(DSSTagsRequest.DOCUMENT_ARCHIVE_ID, archiveIdentifier);
	inParams.put(DSSTagsRequest.PARALLEL_SIGNATURE, "");

	String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
    }

    public void doDSSAfirmaCounterSign() throws Exception {

	Map<String, Object> inParams = new HashMap<String, Object>();
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);
	inParams.put(DSSTagsRequest.KEY_SELECTOR, SERVER_SIGNER_NAME);
	inParams.put(DSSTagsRequest.HASH_ALGORITHM, AlgorithmTypes.SHA1);
	inParams.put(DSSTagsRequest.DOCUMENT_ARCHIVE_ID, archiveIdentifier);
	inParams.put(DSSTagsRequest.COUNTER_SIGNATURE, "");

	String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
    }

    public Map<String, Object> doDSSAfirmaVerify(String firma) throws Exception {

	Map<String, Object> inParams = new HashMap<String, Object>();

	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);
	inParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, Boolean.TRUE.toString());
	inParams.put(DSSTagsRequest.INCLUDE_REVOCATION, Boolean.TRUE.toString());
	inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	inParams.put(DSSTagsRequest.RETURN_PROCESSING_DETAILS, "");
	inParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	inParams.put(DSSTagsRequest.ADDICIONAL_REPORT_OPT_SIGNATURE_TIMESTAMP, "urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp");

	// pruebas con firmas de tipo XML (XAdES Detached o Enveloped)
	Map<String, Object> xadesParams = new HashMap<String, Object>(inParams);
	xadesParams.put(DSSTagsRequest.SIGNATURE_PTR_ATR_WHICH, "1"); //Este valor debe ir rellenado en principio es irrelevante ante tenia 1299585056969008
	xadesParams.put(DSSTagsRequest.DOCUMENT_ATR_ID, "1"); //Este valor debe ir rellenado en principio es irrelevante ante tenia 1299585056969008
	xadesParams.put(DSSTagsRequest.BASE64XML, firma);

	String xmlInput = TransformersFacade.getInstance().generateXml(xadesParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	// validamos si el resultado ha sido satisfactorio
	assertEquals(ResultProcessIds.VALID_SIGNATURE, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	// pruebas con firma de tipo XML (XAdES Enveloping)
//	xadesParams = new HashMap<String, Object>(inParams);
//	xadesParams.put(DSSTagsRequest.SIGNATURE_OBJECT, signXMLEnveloping);
//	xmlInput = TransformersFacade.getInstance().generateXml(xadesParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
//	xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
//	propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
//	// validamos si el resultado ha sido satisfactorio
//	assertEquals(ResultProcessIds.VALID_SIGNATURE, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	// pruebas con firmas de tipo CAdES
//	Map<String, Object> cadesParams = new HashMap<String, Object>(inParams);
//	cadesParams.put(DSSTagsRequest.BASE64DATA, UtilsFileSystem.readFileBase64Encoded("ficheroAfirmar.txt", true));
//	cadesParams.put(DSSTagsRequest.SIGNATURE_BASE64, signatureB64);
//	cadesParams.put(DSSTagsRequest.SIGNATURE_BASE64_ATR_TYPE, SignTypesURIs.CADES);
//
//	xmlInput = TransformersFacade.getInstance().generateXml(cadesParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
//	xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
//	propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);

	// extraemos la información de la firma
	Map<String, Object>[ ] signReports = (Map[ ]) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("IndividualSignatureReport"));
	Map<String, Object> individualSignReport = signReports[0];
	// comprobamos valores de la información del certificado (tipo mapa)
	//Map<String, String> certificateInfo = (Map<String, String>) individualSignReport.get(TransformersFacade.getInstance().getParserParameterValue("ReadableCertificateInfo"));
	//assertEquals("3729941487142038484", certificateInfo.get("serialNumber"));
	//assertEquals("ES", certificateInfo.get("pais"));
	//assertEquals("DEFAULT", certificateInfo.get("idPolitica"));

	// comprobamos los valores del "ProcessingDetails"
	Map<?, ?>[ ] details = (Map[ ]) individualSignReport.get(TransformersFacade.getInstance().getParserParameterValue("ValidDetail"));
	assertFalse(details.length == 0);
	assertNotNull(details[0].get("dss:OptionalOutputs/vr:VerificationReport/vr:IndividualSignatureReport/vr:Details/dss:ProcessingDetails/dss:ValidDetail@Type"));

	return propertiesResult;
	
    }

    public void doUpdateSignature() throws Exception {

	Map<String, Object> inParams = new HashMap<String, Object>();
	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);
	inParams.put(DSSTagsRequest.RETURN_UPDATED_SIGNATURE_ATR_TYPE, SignatureForm.T);

	// pruebas con firmas de tipo PAdES
	signatureB64 = UtilsFileSystem.readFileBase64Encoded("evisor/PADES_Signature.pdf", true);
	inParams.put(DSSTagsRequest.SIGNATURE_BASE64, signatureB64);
	inParams.put(DSSTagsRequest.RETURN_UPDATED_SIGNATURE_ATR_TYPE, "urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:LTV");
	String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
	imprimirRespuesta(xmlOutput);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	assertEquals(SignatureForm.LTV, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureForm")));

	
	// pruebas con firmas de tipo CAdES
	//inParams.put(DSSTagsRequest.SIGNATURE_BASE64, signatureB64);
	//inParams.put(DSSTagsRequest.SIGNATURE_PTR_ATR_WHICH, "1298045604559");

	//xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	//xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
	//propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	//assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	//assertEquals(SignatureForm.T, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureForm")));

	// pruebas con firmas de tipo XAdES Detached
	////inParams.remove(DSSTagsRequest.SIGNATURE_BASE64);
	//inParams.put(DSSTagsRequest.BASE64XML, signatureB64XML);
	//inParams.put(DSSTagsRequest.DOCUMENT_ATR_ID, archiveIdentifier);
	//inParams.put(DSSTagsRequest.SIGNATURE_PTR_ATR_WHICH, archiveIdentifier);

	//xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	//xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
	//propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	//assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	//assertEquals(SignatureForm.T, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("SignatureForm")));

	// pruebas con firmas de tipo XAdES Enveloping
	//inParams.remove(DSSTagsRequest.BASE64XML);
	//inParams.remove(DSSTagsRequest.DOCUMENT_ATR_ID);
	//inParams.remove(DSSTagsRequest.SIGNATURE_PTR_ATR_WHICH);
	//inParams.put(DSSTagsRequest.RETURN_UPDATED_SIGNATURE_ATR_TYPE, SignatureForm.T);
	//inParams.put(DSSTagsRequest.SIGNATURE_OBJECT, signXMLEnveloping);

	//xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	//xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
	//propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	String signXml = propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("UpdateSignatureBase64")).toString();
	assertFalse(signXml.isEmpty());
	UtilsFileSystem.writeFile(UtilsBase64.decode(signXml), "PADES_Signature_LTV.pdf");
    }

    public Map<String, Object> doDSSAfirmaVerifyCertificate(String b64Certificate) throws Exception {

	Map<String, Object> inParams = new HashMap<String, Object>();

	inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);
	inParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	inParams.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	inParams.put(DSSTagsRequest.CHECK_CERTIFICATE_STATUS, "true");
	inParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	
	
	//Ejemplo con fichero plano
	//CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
	//InputStream in = new ByteArrayInputStream(UtilsFileSystem.readFile(CERTIFICATE_NAME, true));
	//X509Certificate cert = (X509Certificate)certFactory.generateCertificate(in);		
	//inParams.put(DSSTagsRequest.X509_CERTIFICATE, new String(UtilsBase64.encodeBytes(cert.getEncoded())));
	
	
	inParams.put(DSSTagsRequest.X509_CERTIFICATE, b64Certificate);
	String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	// comprobamos valores de la información del certificado (tipo mapa)
	Map<String, String> certificateInfo = (Map<String, String>) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("CertificateInfo"));
	//assertEquals("3729941487142038484", certificateInfo.get("serialNumber"));
	//assertEquals("ES", certificateInfo.get("pais"));
	//assertEquals("DEFAULT", certificateInfo.get("idPolitica"));

	//assertEquals("C=ES,O=Telvent,OU=Telvent Global Services,CN=AfirmaSubCA", propertiesResult.get("dss:OptionalOutputs/vr:CertificatePathValidity/vr:CertificateIdentifier/ds:X509IssuerName"));
	assertTrue(propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("CertificateValidity")) instanceof Map[ ]);
	
	return propertiesResult;
    }

    public void doDSSBatchVerifySignature() throws Exception {
	// parámetros de entrada generales
	Map<String, Object> inputParam = new HashMap<String, Object>();

	// parámetros para la firma 1 (XADES)
	Map<String, Object> signParams = new HashMap<String, Object>();
	signParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	signParams.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	signParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	signParams.put(DSSTagsRequest.RETURN_PROCESSING_DETAILS, "");
	signParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	signParams.put(DSSTagsRequest.ADDICIONAL_REPORT_OPT_SIGNATURE_TIMESTAMP, "urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp");

	signParams.put(DSSTagsRequest.SIGNATURE_PTR_ATR_WHICH, "1299585056969008");
	signParams.put(DSSTagsRequest.DOCUMENT_ATR_ID, "1299585056969008");
	signParams.put(DSSTagsRequest.BASE64XML, signatureB64XML);
	signParams.put(DSSTagsRequest.VERIFY_REQUEST_ATTR_REQUEST_ID, "BCJJHGFBBCGEAA");

	// parámetros para la firma 2 (de tipo CAdES)
	Map<String, Object> signParams2 = new HashMap<String, Object>();

	signParams2.put(DSSTagsRequest.VERIFY_REQUEST_ATTR_REQUEST_ID, "BCJJHGFBBCGEBB");
	signParams2.put(DSSTagsRequest.BASE64DATA, UtilsFileSystem.readFileBase64Encoded("ficheroAfirmar.txt", true));
	signParams2.put(DSSTagsRequest.SIGNATURE_BASE64, signatureB64);
	signParams2.put(DSSTagsRequest.SIGNATURE_BASE64_ATR_TYPE, SignTypesURIs.CADES);

	signParams2.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	signParams2.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	signParams2.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	signParams2.put(DSSTagsRequest.RETURN_PROCESSING_DETAILS, "");
	signParams2.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	signParams2.put(DSSTagsRequest.ADDICIONAL_REPORT_OPT_SIGNATURE_TIMESTAMP, "urn:afirma:dss:1.0:profile:XSS:SignatureProperty:SignatureTimeStamp");

	// creamos dos peticiones de validación firma asíncronas.
	Map<?, ?>[ ] requests = { signParams, signParams2 };
	inputParam.put(DSSTagsRequest.VERIFY_REQUEST, requests);
	inputParam.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);

	// tipo de validación por lotes: verificación de firmas
	inputParam.put(DSSTagsRequest.BATCH_REQUEST_ATTR_TYPE, DSSTagsRequest.BATCH_VERIFY_SIGN_TYPE);

	String xmlInput = TransformersFacade.getInstance().generateXml(inputParam, GeneralConstants.DSS_BATCH_VERIFY_SIGNATURE_REQUESTS, GeneralConstants.DSS_AFIRMA_VERIFY_SIGNATURES_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_BATCH_VERIFY_SIGNATURE_REQUESTS, GeneralConstants.DSS_AFIRMA_VERIFY_SIGNATURES_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_BATCH_VERIFY_SIGNATURE_REQUESTS, GeneralConstants.DSS_AFIRMA_VERIFY_SIGNATURES_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.PENDING, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	batchVerifyResultId = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultProcessId"));
    }

    public void doDSSBatchVerifyCertificate() throws Exception {
	// parámetros de entrada generales
	Map<String, Object> inputParams = new HashMap<String, Object>();

	// parámetros para el certificados 1
	Map<String, Object> certParams = new HashMap<String, Object>();

	certParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	certParams.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	certParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	certParams.put(DSSTagsRequest.CHECK_CERTIFICATE_STATUS, "true");
	certParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	certParams.put(DSSTagsRequest.X509_CERTIFICATE, UtilsFileSystem.readFileBase64Encoded(CERTIFICATE_NAME, true));

	certParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	certParams.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	certParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	certParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	certParams.put(DSSTagsRequest.VERIFY_REQUEST_ATTR_REQUEST_ID, "BCJJHGFBBCGEAA");

	// parámetros para el certificados 2
	Map<String, Object> certParams2 = new HashMap<String, Object>();
	certParams2.putAll(certParams);
	certParams2.put(DSSTagsRequest.VERIFY_REQUEST_ATTR_REQUEST_ID, "BCJJHGFBBCGEBB");

	Map<?, ?>[ ] requests = { certParams, certParams2 };
	inputParams.put(DSSTagsRequest.VERIFY_REQUEST, requests);
	inputParams.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);

	// tipo de validación por lotes: verificación de certificados
	inputParams.put(DSSTagsRequest.BATCH_REQUEST_ATTR_TYPE, DSSTagsRequest.BATCH_VERIFY_CERT_TYPE);

	String xmlInput = TransformersFacade.getInstance().generateXml(inputParams, GeneralConstants.DSS_BATCH_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATES_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_BATCH_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATES_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_BATCH_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATES_METHOD, TransformersConstants.VERSION_10);
	assertEquals(ResultProcessIds.PENDING, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
    }

    public void doDSSAsyncRequestStatus() throws Exception {
	// parámetros de entrada
	Map<String, Object> inputParam = new HashMap<String, Object>();

	inputParam.put(DSSTagsRequest.CLAIMED_IDENTITY, APPLICATION_NAME);
	inputParam.put(DSSTagsRequest.ASYNC_RESPONSE_ID, "1301394845513802");// certificados
	inputParam.put(DSSTagsRequest.ASYNC_RESPONSE_ID, batchVerifyResultId);// firmas

	String xmlInput = TransformersFacade.getInstance().generateXml(inputParam, GeneralConstants.DSS_ASYNC_REQUEST_STATUS, GeneralConstants.DSS_ASYNC_REQUEST_STATUS_METHOD, TransformersConstants.VERSION_10);
	String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_ASYNC_REQUEST_STATUS, GeneralConstants.DSS_ASYNC_REQUEST_STATUS_METHOD, APPLICATION_NAME);
	Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_ASYNC_REQUEST_STATUS, GeneralConstants.DSS_ASYNC_REQUEST_STATUS_METHOD, TransformersConstants.VERSION_10);

	assertEquals(ResultProcessIds.PENDING, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	// Para verificar los resultados de las validaciones de firma, esperar a
	// procesar las peticiones asíncronas.
	assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	assertEquals(2, ((Map[ ]) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("VerifyResponse"))).length);
    }

    /**
     * Method that tests the service of time-stamp (creation, validation and re-creation) from TS@.
     */
    public void doDSSTimestampTSA() {

	/*
	 * Prueba 1:
	 * - Tipo de Sello de Tiempo: RFC 3161
	 * - Input Document: DocumentHash
	 */

	// Obtenemos el fichero que se va a sellar
	byte[ ] file = UtilsFileSystem.readFile("ficheroAfirmar.txt", true);
	try {
	    /*
	     * INICIO SELLADO
	     */
	    MessageDigest md = MessageDigest.getInstance("SHA1");
	    md.update(file);
	    String inputDocumentProcessed = new String(Base64Coder.encodeBase64(md.digest()));

	    Map<String, Object> inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.RFC_3161);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String rfcTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("RFC3161Timestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_RFC3161_TIMESTAMPTOKEN, rfcTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_RFC3161_TIMESTAMPTOKEN, rfcTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	} catch (Exception e) {
	    assertTrue(false);
	}

	/*
	 * Prueba 2:
	 * - Tipo de Sello de Tiempo: RFC 3161
	 * - Input Document: Base64Data
	 */
	try {
	    /*
	     * INICIO SELLADO
	     */
	    file = UtilsFileSystem.readFile("ficheroAfirmar.txt", true);
	    Map<String, Object> inParams = new HashMap<String, Object>();
	    String base64Data = new String(Base64Coder.encodeBase64(file));
	    inParams.put(DSSTagsRequest.BASE64DATA, base64Data);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.RFC_3161);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String rfcTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("RFC3161Timestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64DATA, base64Data);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_RFC3161_TIMESTAMPTOKEN, rfcTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64DATA, base64Data);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_RFC3161_TIMESTAMPTOKEN, rfcTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64DATA, base64Data);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */
	} catch (Exception e) {
	    assertTrue(false);
	}

	/*
	 * Prueba 3:
	 * - Tipo de Sello de Tiempo: XML
	 * - Input Document: DocumentHash con transformada
	 */
	try {
	    /*
	     * INICIO SELLADO
	     */
	    file = UtilsFileSystem.readFile("ficheroAfirmar.xml", true);
	    org.apache.xml.security.Init.init();
	    byte[ ] canonicalizedFile = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS).canonicalize(file);
	    MessageDigest md = MessageDigest.getInstance("SHA1");
	    md.update(canonicalizedFile);
	    String inputDocumentProcessed = new String(Base64Coder.encodeBase64(md.digest()));
	    Map<String, Object> inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DOCUMENT_HASH_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64XML, new String(Base64Coder.encodeBase64(file)));
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DOCUMENT_HASH_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_XML_TIMESTAMPTOKEN, xmlTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlReTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.DOCUMENT_HASH_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.DIGEST_METHOD_ATR_ALGORITHM, DSSConstants.AlgorithmTypes.SHA1);
	    inParams.put(DSSTagsRequest.DIGEST_VALUE, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlReTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */
	} catch (Exception e) {
	    assertTrue(false);
	}

	/*
	 * Prueba 4:
	 * - Tipo de Sello de Tiempo: XML
	 * - Input Document: Base64XML
	 * - Mecanismo de Autenticación: Usuario/Contraseña
	 * - La respuesta no viene firmada
	 * - La respuesta no viene encriptada
	 */
	try {
	    /*
	     * INICIO SELLADO
	     */
	    file = UtilsFileSystem.readFile("ficheroAfirmar.xml", true);
	    String inputDocumentProcessed = new String(Base64Coder.encodeBase64(file));
	    Map<String, Object> inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64XML, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64XML, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64XML, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_XML_TIMESTAMPTOKEN, xmlTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlReTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.BASE64XML, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlReTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	} catch (Exception e) {
	    assertTrue(false);
	}

	/*
	 * Prueba 5:
	 * - Tipo de Sello de Tiempo: XML
	 * - Input Document: InlineXML
	 * - Mecanismo de Autenticación: Usuario/Contraseña
	 * - La respuesta no viene firmada
	 * - La respuesta no viene encriptada
	 */
	try {
	    /*
	     * INICIO SELLADO
	     */
	    file = UtilsFileSystem.readFile("ficheroAfirmar.xml", true);
	    Map<String, Object> inParams = new HashMap<String, Object>();
	    String inlineXML = new String(file);
	    inParams.put(DSSTagsRequest.INLINEXML, inlineXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.INLINEXML, inlineXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.INLINEXML, inlineXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_XML_TIMESTAMPTOKEN, xmlTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlReTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.INLINEXML, inlineXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlReTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */
	} catch (Exception e) {
	    assertTrue(false);
	}

	/*
	 * Prueba 6:
	 * - Tipo de Sello de Tiempo: XML
	 * - Input Document: EscapedXML
	 * - Mecanismo de Autenticación: Usuario/Contraseña
	 * - La respuesta no viene firmada
	 * - La respuesta no viene encriptada
	 */
	try {
	    /*
	     * INICIO SELLADO
	     */
	    file = UtilsFileSystem.readFile("ficheroAfirmarEscapado.xml", true);
	    Map<String, Object> inParams = new HashMap<String, Object>();
	    String escapedXML = new String(file);
	    inParams.put(DSSTagsRequest.ESCAPEDXML, escapedXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.ESCAPEDXML, escapedXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.ESCAPEDXML, escapedXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_XML_TIMESTAMPTOKEN, xmlTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlReTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.ESCAPEDXML, escapedXML);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlReTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */
	} catch (Exception e) {
	    assertTrue(false);
	}

	/*
	 * Prueba 7:
	 * - Tipo de Sello de Tiempo: XML
	 * - Input Document: TransformedData
	 */
	try {
	    /*
	     * INICIO SELLADO
	     */
	    file = UtilsFileSystem.readFile("ficheroAfirmar.xml", true);
	    org.apache.xml.security.Init.init();
	    byte[ ] canonicalizedFile = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS).canonicalize(file);
	    String inputDocumentProcessed = new String(Base64Coder.encodeBase64(canonicalizedFile));
	    Map<String, Object> inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_BASE64DATA, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    String xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_SERVICE, APPLICATION_NAME);

	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    String xmlTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN SELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_BASE64DATA, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */

	    /*
	     * INICIO RESELLADO
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_BASE64DATA, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_PREVIOUS_XML_TIMESTAMPTOKEN, xmlTimeStamp);
	    inParams.put(DSSTagsRequest.SIGNATURE_TYPE, DSSConstants.TimestampForm.XML);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_RETIMESTAMP_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_RETIMESTAMP_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    String xmlReTimeStamp = (String) propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("XMLTimestamp"));
	    /*
	     * FIN RESELLADO
	     */

	    /*
	     * INICIO VALIDACIÓN
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_TRANSFORM_ATR_ALGORITHM, Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    inParams.put(DSSTagsRequest.TRANSFORMED_DATA_BASE64DATA, inputDocumentProcessed);
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY_TSA, APPLICATION_NAME);
	    inParams.put(DSSTagsRequest.TIMESTAMP_XML_TIMESTAMPTOKEN, xmlReTimeStamp);

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    xmlOutput = TSAServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, APPLICATION_NAME);

	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_TSA_REQUEST, GeneralConstants.TSA_TIMESTAMP_VALIDATION_SERVICE, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    /*
	     * FIN VALIDACIÓN
	     */
	} catch (Exception e) {
	    assertTrue(false);
	}

    }
    
    public static void imprimirRespuesta(String responseXML) throws AxisFault {
		
		FileWriter fileWriter = null;
        try {	
            
            File newTextFile1 = new File("response_sin_error.txt");
            fileWriter = new FileWriter(newTextFile1);           
            fileWriter.write(responseXML);
            fileWriter.close();   
			
        } catch (IOException ex) {
           LOGGER.error("ERROR AL PASAR A FICHERO LA RESPUESTA");
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
            	 LOGGER.error("ERROR AL PASAR A FICHERO LA RESPUESTA");
            }
        }
		
	}
    
    public static void main (String args[]){
    	
    	DOMConfigurator.configure("conf/log4j.xml");
    	
    	DSSServicesManager dssst = new DSSServicesManager();
    	
    	try {
			//dssst.testUpdateSignature();
    		//dssst.testDSSTimestampTSA();
    		//dssst.doDSSAfirmaVerifyCertificate("aaa");
    		dssst.doDSSAfirmaVerify("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0id2luZG93cy0xMjUyIj8+CjxkczpTaWduYXR1cmUgSWQ9IlNpZ25hdHVyZS00ZGM2OGUzNC1iNDM1LTQxMGUtYmFlMy1mOWJlMDg5OThhNmMtU2lnbmF0dXJlIiB4bWxuczpkcz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC8wOS94bWxkc2lnIyI+PGRzOlNpZ25lZEluZm8+PGRzOkNhbm9uaWNhbGl6YXRpb25NZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy9UUi8yMDAxL1JFQy14bWwtYzE0bi0yMDAxMDMxNSIvPjxkczpTaWduYXR1cmVNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjcnNhLXNoYTEiLz48ZHM6UmVmZXJlbmNlIElkPSJSZWZlcmVuY2UtYWRjZmVlMzAtN2Y1YS00NWU0LThiOGYtNDNhMmVkNWI2NzZiIiBUeXBlPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwLzA5L3htbGRzaWcjT2JqZWN0IiBVUkk9IiNPYmplY3QtMDdjMDk5OTEtNWY0Zi00OGNiLTg5YzMtZjQ1Mzg3OTI3MzQ3Ij48ZHM6VHJhbnNmb3Jtcz48ZHM6VHJhbnNmb3JtIEFsZ29yaXRobT0iaHR0cDovL3d3dy53My5vcmcvVFIvMjAwMS9SRUMteG1sLWMxNG4tMjAwMTAzMTUiLz48L2RzOlRyYW5zZm9ybXM+PGRzOkRpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3NoYTUxMiIvPjxkczpEaWdlc3RWYWx1ZT5DWklTWXlCbEN2eUsrMTB5ZXh0Q3NLby9KRExzYm5MSzF2OU9TWmg5eXVQUlk0T1VJRlIwWHVUOTNkb3dVT1lKUzJ2WGhxQzkwSE1CCmlEc1VkTnVxRUE9PTwvZHM6RGlnZXN0VmFsdWU+PC9kczpSZWZlcmVuY2U+PGRzOlJlZmVyZW5jZSBUeXBlPSJodHRwOi8vdXJpLmV0c2kub3JnLzAxOTAzI1NpZ25lZFByb3BlcnRpZXMiIFVSST0iI1NpZ25hdHVyZS00ZGM2OGUzNC1iNDM1LTQxMGUtYmFlMy1mOWJlMDg5OThhNmMtU2lnbmVkUHJvcGVydGllcyI+PGRzOkRpZ2VzdE1ldGhvZCBBbGdvcml0aG09Imh0dHA6Ly93d3cudzMub3JnLzIwMDEvMDQveG1sZW5jI3NoYTUxMiIvPjxkczpEaWdlc3RWYWx1ZT5Md0dWVWRsTVFkUmNJZnVpQ2RKVnovM2J6WTZkOUtPb3BRRFFabEkrVnkzWnltVkdWYnBKbXRyNHU1Y3BTWVVSL1RHQ1NYczJibS95CkdML1hUUkJqMnc9PTwvZHM6RGlnZXN0VmFsdWU+PC9kczpSZWZlcmVuY2U+PGRzOlJlZmVyZW5jZSBVUkk9IiNTaWduYXR1cmUtNGRjNjhlMzQtYjQzNS00MTBlLWJhZTMtZjliZTA4OTk4YTZjLUtleUluZm8iPjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGE1MTIiLz48ZHM6RGlnZXN0VmFsdWU+NkV1SGRtZ1gwcVNsS0NVUitOMWU1bHVXT3ZCNVBGSHhUT3ZjN1Rob0JnVlhvZ2tNZXBHTXovMkRuVGt0cEpjYWpncHFoUENlVjZKSwpyU1RtbEZmdXp3PT08L2RzOkRpZ2VzdFZhbHVlPjwvZHM6UmVmZXJlbmNlPjwvZHM6U2lnbmVkSW5mbz48ZHM6U2lnbmF0dXJlVmFsdWUgSWQ9IlNpZ25hdHVyZS00ZGM2OGUzNC1iNDM1LTQxMGUtYmFlMy1mOWJlMDg5OThhNmMtU2lnbmF0dXJlVmFsdWUiPmZKZUh3VkhGaWxzclhzdlNxWncvTEVhVDJ0S0t3RFNCNTR6Y1NzdW5EMUxDRDZGci8xODBzQ1lrN2t6M1ZENVZ2TFFld1BSTklsNlAKbVZUelVwMTZaMFh3MzlpRFJsYmNHN0VPVm0yWHlqbTV5OXB5bVYydlJMNVNkTmcwZ01MakJlN2xOYjRYeWVVYjVCVkY1S2VRanhDeQp5MUFkY2V6ZEdTV3VXWjkxN3BNPTwvZHM6U2lnbmF0dXJlVmFsdWU+PGRzOktleUluZm8gSWQ9IlNpZ25hdHVyZS00ZGM2OGUzNC1iNDM1LTQxMGUtYmFlMy1mOWJlMDg5OThhNmMtS2V5SW5mbyI+PGRzOlg1MDlEYXRhPjxkczpYNTA5Q2VydGlmaWNhdGU+TUlJRkh6Q0NCSWlnQXdJQkFnSUVQUTJsSXpBTkJna3Foa2lHOXcwQkFRVUZBREEyTVFzd0NRWURWUVFHRXdKRlV6RU5NQXNHQTFVRQpDaE1FUms1TlZERVlNQllHQTFVRUN4TVBSazVOVkNCRGJHRnpaU0F5SUVOQk1CNFhEVEUwTURNeE56RXhNekkxT1ZvWERURTNNRE14Ck56RXhNekkxT1Zvd2dZQXhDekFKQmdOVkJBWVRBa1ZUTVEwd0N3WURWUVFLRXdSR1RrMVVNUmd3RmdZRFZRUUxFdzlHVGsxVUlFTnMKWVhObElESWdRMEV4RWpBUUJnTlZCQXNUQ1Rjd01UQXdNamc0TVRFME1ESUdBMVVFQXhRclRrOU5RbEpGSUVOQjBVRlRJRk5QUWxKSgpUazhnVEVWUFRrOVNJQzBnVGtsR0lEQTFOak16Tmpnd1dqQ0JuekFOQmdrcWhraUc5dzBCQVFFRkFBT0JqUUF3Z1lrQ2dZRUFzMUw3CkdzMTBOZjNHT3dKN1ZzcndzMm91QkhDRXRSMUEzZHBuN2ViNER5Tm51bHVaMlpNQXdOY1BRWjZWZ0tZNkVnVDFIZWp3WU9NZUgrNUYKeXZSQWlqb3VLWnFzZmw5eVRKaVFrbVdDOHRjSjR2OUVBYzlOSXRNMWtkNlpPVElIa1RxdkxnY0NTRyt6bE8xRTAwdjIyWnR1NnZxLwo3TlBmc0svZ3RrMGxlOUVDQXdFQUFhT0NBdTB3Z2dMcE1Hd0dBMVVkRVFSbE1HT2tZVEJmTVJnd0ZnWUpLd1lCQkFHc1pnRUVFd2t3Ck5UWXpNelk0TUZveEZqQVVCZ2tyQmdFRUFheG1BUU1UQjFOUFFsSkpUazh4RkRBU0Jna3JCZ0VFQWF4bUFRSVVCVU5CMFVGVE1SVXcKRXdZSkt3WUJCQUdzWmdFQkV3Wk1SVTlPVDFJd0NRWURWUjBUQkFJd0FEQXJCZ05WSFJBRUpEQWlnQTh5TURFME1ETXhOekV4TXpJMQpPVnFCRHpJd01UY3dNekUzTVRFek1qVTVXakFMQmdOVkhROEVCQU1DQmFBd0VRWUpZSVpJQVliNFFnRUJCQVFEQWdXZ01CMEdBMVVkCkRnUVdCQlNOVGZUWTN1THEyQnhlc1VBQlpuaHFwTWQycmpBZkJnTlZIU01FR0RBV2dCUkFtblpFbDNRSHhLd1V5eDZOVHpwRmZERFgKWVRDQ0FURUdBMVVkSUFTQ0FTZ3dnZ0VrTUlJQklBWUpLd1lCQkFHc1pnTUZNSUlCRVRBMEJnZ3JCZ0VGQlFjQ0FSWW9hSFIwY0RvdgpMM2QzZHk1alpYSjBMbVp1YlhRdVpYTXZZMjl1ZG1WdWFXOHZaSEJqTG5Ca1pqQ0IyQVlJS3dZQkJRVUhBZ0l3Z2NzYWdjaERaWEowCmFXWnBZMkZrYnlCU1pXTnZibTlqYVdSdklHVjRjR1ZrYVdSdklITmxaL3B1SUd4bFoybHpiR0ZqYWZOdUlIWnBaMlZ1ZEdVdVZYTnYKSUd4cGJXbDBZV1J2SUdFZ2JHRWdRMjl0ZFc1cFpHRmtJRVZzWldOMGN2TnVhV05oSUhCdmNpQjJZV3h2Y2lCdDRYaHBiVzhnWkdVZwpNVEF3SUdVZ2MyRnNkbThnWlhoalpYQmphVzl1WlhNZ1pXNGdSRkJETGtOdmJuUmhZM1J2SUVaT1RWUTZReTlLYjNKblpTQktkV0Z1CklERXdOaTB5T0RBd09TMU5ZV1J5YVdRdFJYTndZZkZoTGpBZEJna3JCZ0VFQWF4bUFTRUVFQllPVUVWU1UwOU9RU0JHU1ZOSlEwRXcKTHdZSUt3WUJCUVVIQVFNRUl6QWhNQWdHQmdRQWprWUJBVEFWQmdZRUFJNUdBUUl3Q3hNRFJWVlNBZ0ZrQWdFQU1Gd0dBMVVkSHdSVgpNRk13VWFCUG9FMmtTekJKTVFzd0NRWURWUVFHRXdKRlV6RU5NQXNHQTFVRUNoTUVSazVOVkRFWU1CWUdBMVVFQ3hNUFJrNU5WQ0JECmJHRnpaU0F5SUVOQk1SRXdEd1lEVlFRREV3aERVa3d4TkRFNE1UQU5CZ2txaGtpRzl3MEJBUVVGQUFPQmdRQnNyc3FRSmxwb1FFNXUKdWRHK3hUeTdlTWJWVGxKM24xODY3Y1NTRmhkQitrY0d5MTFHcGxhUEI4NnYvamhRTXdsempLZS9zZmlHaFV5eVBlQ2Z0TkxwRCtiagp3YU81NGJvTk5mekhuNXBYMnpDQXltVGFhVXVXNEY4MmZaY3ArOCtPVGdyaWtaa1BvZUQrZUphN1BXRGlJZGZWVmtjamFUbS91bXIyCmVoV09SUT09PC9kczpYNTA5Q2VydGlmaWNhdGU+PGRzOlg1MDlDZXJ0aWZpY2F0ZT5NSUlDK1RDQ0FtS2dBd0lCQWdJRU52RWJHVEFOQmdrcWhraUc5dzBCQVFVRkFEQTJNUXN3Q1FZRFZRUUdFd0pGVXpFTk1Bc0dBMVVFCkNoTUVSazVOVkRFWU1CWUdBMVVFQ3hNUFJrNU5WQ0JEYkdGelpTQXlJRU5CTUI0WERUazVNRE14T0RFME5UWXhPVm9YRFRFNU1ETXgKT0RFMU1qWXhPVm93TmpFTE1Ba0dBMVVFQmhNQ1JWTXhEVEFMQmdOVkJBb1RCRVpPVFZReEdEQVdCZ05WQkFzVEQwWk9UVlFnUTJ4aApjMlVnTWlCRFFUQ0JuVEFOQmdrcWhraUc5dzBCQVFFRkFBT0Jpd0F3Z1ljQ2dZRUFtRCt0R1RhVFBUNytka0lVL1RWdjhmcXRJbnBZCjQwYlFYY1phK1dJdGp6RmUvclF3L2xCMHJOYWRIZUJpeGtuZEZCSjljUXVzQnNFLzF3YUg0SkNKMXVYakE3THlKN0dmTThpcWF6WksKbzhRL2VVR2RpVVl2S3o1ajFEaFdrYW9kc1ExQ2RVM3poMDdqRDAzTXRHeS9ZaE9INnRDYmpyYmkveG4wbEFuVmxtRUNBUU9qZ2dFVQpNSUlCRURBUkJnbGdoa2dCaHZoQ0FRRUVCQU1DQUFjd1dBWURWUjBmQkZFd1R6Qk5vRXVnU2FSSE1FVXhDekFKQmdOVkJBWVRBa1ZUCk1RMHdDd1lEVlFRS0V3UkdUazFVTVJnd0ZnWURWUVFMRXc5R1RrMVVJRU5zWVhObElESWdRMEV4RFRBTEJnTlZCQU1UQkVOU1RERXcKS3dZRFZSMFFCQ1F3SW9BUE1UazVPVEF6TVRneE5EVTJNVGxhZ1E4eU1ERTVNRE14T0RFME5UWXhPVm93Q3dZRFZSMFBCQVFEQWdFRwpNQjhHQTFVZEl3UVlNQmFBRkVDYWRrU1hkQWZFckJUTEhvMVBPa1Y4TU5kaE1CMEdBMVVkRGdRV0JCUkFtblpFbDNRSHhLd1V5eDZOClR6cEZmRERYWVRBTUJnTlZIUk1FQlRBREFRSC9NQmtHQ1NxR1NJYjJmUWRCQUFRTU1Bb2JCRlkwTGpBREFnU1FNQTBHQ1NxR1NJYjMKRFFFQkJRVUFBNEdCQUdGTW9IeFpZMXRtK081bEU4NURnRWU1c2pYSnlJVEhhM05nUmVTZE41MzFqaVc1K2FxcXl1UDRRNXd2b0lrRgpzVVV5bENvZUE0MWRwdDdQVjVYYTN5WmdYOHZmbFI2NHpnalkrSXJKVDZsb2RaUGpMd1ZNWkdBQ29rSWViNFpvWlZVTzJFTnY4cEV4ClBxTkhQQ2dGcjBXMm5TSk1KbnRMZlZzVitSbEczd2hkPC9kczpYNTA5Q2VydGlmaWNhdGU+PC9kczpYNTA5RGF0YT48ZHM6S2V5VmFsdWU+PGRzOlJTQUtleVZhbHVlPjxkczpNb2R1bHVzPnMxTDdHczEwTmYzR093SjdWc3J3czJvdUJIQ0V0UjFBM2RwbjdlYjREeU5udWx1WjJaTUF3TmNQUVo2VmdLWTZFZ1QxSGVqd1lPTWUKSCs1Rnl2UkFpam91S1pxc2ZsOXlUSmlRa21XQzh0Y0o0djlFQWM5Tkl0TTFrZDZaT1RJSGtUcXZMZ2NDU0cremxPMUUwMHYyMlp0dQo2dnEvN05QZnNLL2d0azBsZTlFPTwvZHM6TW9kdWx1cz48ZHM6RXhwb25lbnQ+QVFBQjwvZHM6RXhwb25lbnQ+PC9kczpSU0FLZXlWYWx1ZT48L2RzOktleVZhbHVlPjwvZHM6S2V5SW5mbz48ZHM6T2JqZWN0IEVuY29kaW5nPSJ3aW5kb3dzLTEyNTIiIElkPSJPYmplY3QtMDdjMDk5OTEtNWY0Zi00OGNiLTg5YzMtZjQ1Mzg3OTI3MzQ3IiBNaW1lVHlwZT0idGV4dC94bWwiPjxkYXRvc0Zvcm11bGFyaW8+ClBFUmhkRzl6WDBkbGJtVnlhV052Y3o0OFQzSm5ZVzVwYzIxdlBqd2hXME5FUVZSQlcwVjRZMjFoTGlCRWFYQjFkR0ZqYWNPemJpQmtaU0JEYVhWa1lXUWdVbVZoYkYxZFBqd3ZUM0puWVc1cGMyMXZQanhKWkdsdmJXRStaWE04TDBsa2FXOXRZVDQ4VW1WdGFYUmxiblJsUGp4T2IyMWljbVUrUENGYlEwUkJWRUZiUTBIRGtVRlRJRk5QUWxKSlRrOGdURVZQVGs5U1hWMCtQQzlPYjIxaWNtVStQRVJ2WTNWdFpXNTBiMTlKWkdWdWRHbG1hV05oWTJsdmJqNDhWR2x3Yno0eFBDOVVhWEJ2UGp4T2RXMWxjbTgrTURVMk16TTJPREJhUEM5T2RXMWxjbTgrUEM5RWIyTjFiV1Z1ZEc5ZlNXUmxiblJwWm1sallXTnBiMjQrUEVOdmNuSmxiMTlGYkdWamRISnZibWxqYno0OElWdERSRUZVUVZ0dFlXNTFaV3hBWW1GeVlXaHZibUV1WlhOZFhUNDhMME52Y25KbGIxOUZiR1ZqZEhKdmJtbGpiejQ4TDFKbGJXbDBaVzUwWlQ0OFFYTjFiblJ2UGp4RGIyUnBaMjgrUTA5T1ZGSkJWRUZEU1U5T1BDOURiMlJwWjI4K1BFUmxjMk55YVhCamFXOXVQandoVzBORVFWUkJXMUJ5WlhObGJuUmhZMm5EczI0Z1pHVWdVR3hwWTJGelhWMCtQQzlFWlhOamNtbHdZMmx2Ymo0OEwwRnpkVzUwYno0OFJHVnpkR2x1Yno0OFEyOWthV2R2UGtSUU1UazhMME52WkdsbmJ6NDhMMFJsYzNScGJtOCtQRTUxYldWeWIxOUZlSEJsWkdsbGJuUmxMejQ4TDBSaGRHOXpYMGRsYm1WeWFXTnZjejQ4UkdGMGIzTmZSWE53WldOcFptbGpiM00rUEdGNWRXNTBZVzFwWlc1MGJ6NUNRVkpCU0U5T1FTQlBRbEpCVXlCWklGTkZVbFpKUTBsUFV5d2dVeTVNTGp3dllYbDFiblJoYldsbGJuUnZQanhqYVdZK1FqRXpNVGMyTURJNVBDOWphV1krUEdSdmJXbGphV3hwYjA1dmRHbG1hV05oWTJsdmJqNVNUMDVFUVNCRVJVd2dWRkpGVGtsTVRFOHNJRTdDdWlBeE5Ud3ZaRzl0YVdOcGJHbHZUbTkwYVdacFkyRmphVzl1UGp4c2IyTmhiR2xrWVdRK1RVOVNRVXdnUkVVZ1EwRk1RVlJTUVZaQlBDOXNiMk5oYkdsa1lXUStQSEJ5YjNacGJtTnBZVDVEU1ZWRVFVUWdVa1ZCVER3dmNISnZkbWx1WTJsaFBqeGpiMlJwWjI5UWIzTjBZV3crTVRNek5UQThMMk52WkdsbmIxQnZjM1JoYkQ0OGRHVnNaV1p2Ym04K09USTJNek13TmprNVBDOTBaV3hsWm05dWJ6NDhZMjl1ZG05allYUnZjbWxoUGtSUVExSXlNREUyTHpJNE5URTBQQzlqYjI1MmIyTmhkRzl5YVdFK1BFUmxjMk55YVhCamFXOXVYMk52Ym5adlkyRjBiM0pwWVQ1UWNtOTVaV04wYnlCa1pTQmhiWEJzYVdGamFjT3piaUJrWlNCTVlXSnZjbUYwYjNKcGJ5QmtaU0JXYVdGeklIa2dUMkp5WVhNZ1JHbHdkWFJoWTJuRHMyNGdVSEp2ZG1sdVkybGhiRHd2UkdWelkzSnBjR05wYjI1ZlkyOXVkbTlqWVhSdmNtbGhQanhsZUhCdmJtVStVWFZsSUdWdWRHVnlZV1J2SUdSbElHeGhJSFJ5WVcxcGRHRmphY096YmlCd2IzSWdiR0VnUkdsd2RYUmhZMm5EczI0Z1VISnZkbWx1WTJsaGJDQmtaU0JEYVhWa1lXUWdVbVZoYkNCa1pXd2daWGh3WldScFpXNTBaU0JrWlNCamIyNTBjbUYwWVdOcHc3TnVJR0Z5Y21saVlTQnBibVJwWTJGa1lTd2daVzRnWlhOMFpTQmhZM1J2SUcxaGJtbG1hV1Z6ZEc4Z2JHRWdkbTlzZFc1MFlXUWdaR1ZzSUd4cFkybDBZV1J2Y2lCeGRXVWdjMlVnYzJYRHNXRnNZU0JrWlNCd1lYSjBhV05wY0dGeUlHVnVJR1Z6WlNCd2NtOWpaV1JwYldsbGJuUnZMaUE4TDJWNGNHOXVaVDQ4YzI5c2FXTnBkR0UrVVhWbElITmxJSFJsYm1kaElIQnZjaUJ3Y21WelpXNTBZV1JoSUd4aElIQnliM0J2YzJsamFjT3piaUJ4ZFdVZ2MyVWdZV052YlhCaHc3RmhMaUE4TDNOdmJHbGphWFJoUGp4a2IyTjFiV1Z1ZEc5SlpHVnVkR2xrWVdRK01EVTJNek0yT0RCYVBDOWtiMk4xYldWdWRHOUpaR1Z1ZEdsa1lXUStQRzV2YldKeVpWTnZiR2xqYVhSaGJuUmxQa05CdzVGQlV5QlRUMEpTU1U1UElFeEZUMDVQVWp3dmJtOXRZbkpsVTI5c2FXTnBkR0Z1ZEdVK1BHVnRZV2xzVTI5c2FXTnBkR0Z1ZEdVK2JXRnVkV1ZzUUdKaGNtRm9iMjVoTG1WelBDOWxiV0ZwYkZOdmJHbGphWFJoYm5SbFBqeHpiMkp5WlRFK1UyazhMM052WW5KbE1UNDhjMjlpY21VeVBrNXZQQzl6YjJKeVpUSStQSE52WW5KbE16NU9iend2YzI5aWNtVXpQanh2ZEhKdmN5OCtQR052WkY5dmNtZGhibTgrUkZBeE9Ud3ZZMjlrWDI5eVoyRnViejQ4WkdWelkzSmZiM0puWVc1dlBqd2hXME5FUVZSQlcxTkZVbFpKUTBsUElFUkZJRkpGUjBsVFZGSlBJRVVnU1U1R1QxSk5RVU5KVDA0Z1dTQlFRVlJTU1UxUFRrbFBYVjArUEM5a1pYTmpjbDl2Y21kaGJtOCtQQzlFWVhSdmMxOUZjM0JsWTJsbWFXTnZjejQ4Ukc5amRXMWxiblJ2Y3k4KzwvZGF0b3NGb3JtdWxhcmlvPjwvZHM6T2JqZWN0PjxkczpPYmplY3Q+PHhhZGVzOlF1YWxpZnlpbmdQcm9wZXJ0aWVzIElkPSJTaWduYXR1cmUtNGRjNjhlMzQtYjQzNS00MTBlLWJhZTMtZjliZTA4OTk4YTZjLVF1YWxpZnlpbmdQcm9wZXJ0aWVzIiBUYXJnZXQ9IiNTaWduYXR1cmUtNGRjNjhlMzQtYjQzNS00MTBlLWJhZTMtZjliZTA4OTk4YTZjLVNpZ25hdHVyZSIgeG1sbnM6ZHM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvMDkveG1sZHNpZyMiIHhtbG5zOnhhZGVzPSJodHRwOi8vdXJpLmV0c2kub3JnLzAxOTAzL3YxLjMuMiMiPjx4YWRlczpTaWduZWRQcm9wZXJ0aWVzIElkPSJTaWduYXR1cmUtNGRjNjhlMzQtYjQzNS00MTBlLWJhZTMtZjliZTA4OTk4YTZjLVNpZ25lZFByb3BlcnRpZXMiPjx4YWRlczpTaWduZWRTaWduYXR1cmVQcm9wZXJ0aWVzPjx4YWRlczpTaWduaW5nVGltZT4yMDE2LTA5LTEyVDEzOjI4OjAyKzAyOjAwPC94YWRlczpTaWduaW5nVGltZT48eGFkZXM6U2lnbmluZ0NlcnRpZmljYXRlPjx4YWRlczpDZXJ0Pjx4YWRlczpDZXJ0RGlnZXN0PjxkczpEaWdlc3RNZXRob2QgQWxnb3JpdGhtPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxLzA0L3htbGVuYyNzaGE1MTIiLz48ZHM6RGlnZXN0VmFsdWU+UDN3UGVleG1VUS9SM1VjcjR4Q1lPUXozbzdOK3dpRlJ5ZEUzQkRadkhGcWpIdXZJd1dLQlRrNUlpdEpsU3ZHa1A1NkZWMFFSYXN5Y2hobmlOdXhNYXc9PTwvZHM6RGlnZXN0VmFsdWU+PC94YWRlczpDZXJ0RGlnZXN0Pjx4YWRlczpJc3N1ZXJTZXJpYWw+PGRzOlg1MDlJc3N1ZXJOYW1lPk9VPUZOTVQgQ2xhc2UgMiBDQSwgTz1GTk1ULCBDPUVTPC9kczpYNTA5SXNzdWVyTmFtZT48ZHM6WDUwOVNlcmlhbE51bWJlcj4xMDI0MzA0NDE5PC9kczpYNTA5U2VyaWFsTnVtYmVyPjwveGFkZXM6SXNzdWVyU2VyaWFsPjwveGFkZXM6Q2VydD48L3hhZGVzOlNpZ25pbmdDZXJ0aWZpY2F0ZT48L3hhZGVzOlNpZ25lZFNpZ25hdHVyZVByb3BlcnRpZXM+PHhhZGVzOlNpZ25lZERhdGFPYmplY3RQcm9wZXJ0aWVzPjx4YWRlczpEYXRhT2JqZWN0Rm9ybWF0IE9iamVjdFJlZmVyZW5jZT0iI1JlZmVyZW5jZS1hZGNmZWUzMC03ZjVhLTQ1ZTQtOGI4Zi00M2EyZWQ1YjY3NmIiPjx4YWRlczpEZXNjcmlwdGlvbi8+PHhhZGVzOk9iamVjdElkZW50aWZpZXI+PHhhZGVzOklkZW50aWZpZXIgUXVhbGlmaWVyPSJPSURBc1VSTiI+dXJuOm9pZDoxLjIuODQwLjEwMDAzLjUuMTA5LjEwPC94YWRlczpJZGVudGlmaWVyPjx4YWRlczpEZXNjcmlwdGlvbi8+PC94YWRlczpPYmplY3RJZGVudGlmaWVyPjx4YWRlczpNaW1lVHlwZT50ZXh0L3htbDwveGFkZXM6TWltZVR5cGU+PHhhZGVzOkVuY29kaW5nPndpbmRvd3MtMTI1MjwveGFkZXM6RW5jb2Rpbmc+PC94YWRlczpEYXRhT2JqZWN0Rm9ybWF0PjwveGFkZXM6U2lnbmVkRGF0YU9iamVjdFByb3BlcnRpZXM+PC94YWRlczpTaWduZWRQcm9wZXJ0aWVzPjwveGFkZXM6UXVhbGlmeWluZ1Byb3BlcnRpZXM+PC9kczpPYmplY3Q+PC9kczpTaWduYXR1cmU+");
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    
    }
 
 

}
