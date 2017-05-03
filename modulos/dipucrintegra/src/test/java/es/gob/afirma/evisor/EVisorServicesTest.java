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

package es.gob.afirma.evisor;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerException;
import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerFacade;
import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.utils.EVisorConstants;
import es.gob.afirma.utils.EVisorConstants.EVisorTagsRequest;
import es.gob.afirma.utils.EVisorUtil;
import es.gob.afirma.utils.UtilsFileSystem;

/**
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 26/01/2011.
 */
public class EVisorServicesTest {

	/**
	 * Attribute that represents application name for tests.
	 */
	private static final String APPLICATION_NAME = "tgs.nvm";

	/**
	 * Attribute that represents template identificator. 
	 */
	private static final String TEMPLATE_ID = "template_test";

	/**
	 * Tests generateReport method of SignatureReportService with invalid values (null values, service or method not exist, etc.).
	 * @throws Exception Exception
	 */
	@Test
	public void testGenerateReportInvalidValues() throws Exception {
		
		//interfaz de entrada xml no válida
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("<srsm:GenerationRequest />", EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, APPLICATION_NAME);
			Assert.fail();
		} catch (Afirma5ServiceInvokerException e) {}
		
		//método y servicio no existente
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("<srsm:GenerationRequest />", "service", "method", APPLICATION_NAME);
			Assert.fail();
		} catch (Afirma5ServiceInvokerException e) {}
		
		
		//aplicación no existente en EVisor
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("<?xml version='1.0' encoding='UTF-8'?><srsm:GenerationRequest xmlns:srsm='urn:es:gob:signaturereport:services:messages' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'><srsm:ApplicationId>tgs.nvm1</srsm:ApplicationId><srsm:TemplateId>template_test</srsm:TemplateId><srsm:Signature><srsm:EncodedSignature>JVBERi0xL</srsm:EncodedSignature></srsm:Signature></srsm:GenerationRequest>", 
			             EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, APPLICATION_NAME);
			Assert.fail();
		} catch (Afirma5ServiceInvokerException e) {}
		
		
	}

	/**
	 * Tests generateReport method of SignatureReportService with a valid value.
	 * @throws Exception Exception
	 */
	@Test
	public void testGenerateReport() throws Exception {

		Map<String, Object> inputParams = new HashMap<String, Object>();

		String signB64 = UtilsFileSystem.readFileBase64Encoded("evisor/PADES_Signature.pdf", true);

		inputParams.put(EVisorTagsRequest.APPLICATION_ID, APPLICATION_NAME);
		inputParams.put(EVisorTagsRequest.TEMPLATE_ID, TEMPLATE_ID);
		inputParams.put(EVisorTagsRequest.ENCODED_SIGNATURE, signB64);

		inputParams.put(EVisorTagsRequest.INCLUDE_SIGNATURE, "true");

		Map<String, String> qRCodeParams = new HashMap<String, String>(2);
		qRCodeParams.put("QRCodeWidth", "600");
		qRCodeParams.put("QRCodeHeight", "600");
		qRCodeParams.put("Rotation", "90");

		// introducimos un conjunto de códigos de barra en los parámetros de
		// entrada.
		inputParams.put(EVisorTagsRequest.BARCODE, new Map<?, ?>[ ] { EVisorUtil.newBarcodeMap("Prueba código barra tipo QRCode", "QRCode", qRCodeParams), EVisorUtil.newBarcodeMap("986656487", "EAN128", null), EVisorUtil.newBarcodeMap("Prueba código barra tipo DataMatrix", "DataMatrix", null) });

		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);

		String outputXml = Afirma5ServiceInvokerFacade.getInstance().invokeService(inputXml, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, APPLICATION_NAME);

		Map<String, Object> result = TransformersFacade.getInstance().parseResponse(outputXml, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);

		Assert.assertEquals("0", result.get("srsm:Result/srsm:Code"));

	}
	
	/**
	 * Tests validateReport method of SignatureReportService with invalid values (input xml message not valid, service or method not exist, etc.).
	 * @throws Exception Exception
	 */
	@Test
	public void testValidateReportInvalidValues() throws Exception {
		
		//interfaz de entrada xml no válida
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("<srsm:ValidationReportRequest />", EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, APPLICATION_NAME);
			Assert.fail();
		} catch (Afirma5ServiceInvokerException e) {}
		
		//método no existente
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("<srsm:ValidationReportRequest />", EVisorConstants.SIGNATURE_REPORT_SERVICE, "method", APPLICATION_NAME);
			Assert.fail();
		} catch (Afirma5ServiceInvokerException e) {}
		
		
		//aplicación no existente en EVisor
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("<?xml version='1.0' encoding='UTF-8'?><srsm:ValidationReportRequest xmlns:srsm='urn:es:gob:signaturereport:services:messages' 	xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'><srsm:ApplicationId>asddd</srsm:ApplicationId><srsm:Report /></srsm:ValidationReportRequest>", 
			             EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, APPLICATION_NAME);
			Assert.fail();
		} catch (Afirma5ServiceInvokerException e) {}
		
		
	}

	/**
	 * Tests validateReport method of SignatureReportService with a valid value.
	 * @throws Exception Exception
	 */
	@Test
	public void testValidateReport() throws Exception {
		Map<String, Object> inputParams = new HashMap<String, Object>();

		String evisorReport = UtilsFileSystem.readFileBase64Encoded("evisor/reportSigned.pdf", true);

		inputParams.put(EVisorTagsRequest.APPLICATION_ID, APPLICATION_NAME);
		inputParams.put(EVisorTagsRequest.REPORT, evisorReport);

		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, TransformersConstants.VERSION_10);

		String outputXml = Afirma5ServiceInvokerFacade.getInstance().invokeService(inputXml, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, APPLICATION_NAME);

		Map<String, Object> result = TransformersFacade.getInstance().parseResponse(outputXml, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		Assert.assertEquals("0", result.get("srsm:Result/srsm:Code"));
	}

}
