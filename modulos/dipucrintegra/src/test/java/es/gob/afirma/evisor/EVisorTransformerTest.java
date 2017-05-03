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
 * <b>File:</b><p>es.gob.afirma.evisor.EVisorTransformerTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>19/12/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 19/12/2011.
 */
package es.gob.afirma.evisor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.EVisorConstants;
import es.gob.afirma.utils.EVisorConstants.EVisorTagsRequest;
import es.gob.afirma.utils.EVisorUtil;
import es.gob.afirma.utils.UtilsFileSystem;
import es.gob.afirma.utils.UtilsXML;



/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 19/12/2011.
 */
public class EVisorTransformerTest {
	
	/**
	 * Attribute that represents xsd schema definition for SignatureReportServices. 
	 */
	private File xsdFile = new File(ClassLoader.getSystemResource("eVisor/SignatureReportServices.xsd").getFile());
	
	/**
	 * Tests generateReport method (a method of SignatureReportService) without mandatory parameters.
	 * @throws Exception if an error happens.
	 */
	@Test
	public void generateReportWithoutMandatoryParams () throws Exception {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(EVisorTagsRequest.APPLICATION_ID, "applicationName");
		
		//creación xml sin todos los parámetros obligatorios
		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		try {
			UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes()));
			Assert.fail();
		} catch (TransformersException e) { }
	}
	
	/**
	 * Tests generateReport method (a method of SignatureReportService)  with mandatory parameters.
	 * @throws Exception if an error happens.
	 */
	@Test
	public void generateReportMandatoryParams() throws Exception {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(EVisorTagsRequest.APPLICATION_ID, "APPLICATION_NAME");
		inputParams.put(EVisorTagsRequest.TEMPLATE_ID, "TEMPLATE_ID");
		
		//Se indica la firma codificada en base64.
		inputParams.put(EVisorTagsRequest.ENCODED_SIGNATURE, Base64Coder.encodeBase64("signB64"));
		
		//creación xml sin todos los parámetros obligatorios
		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		
		//validamos xml generado con todos los parámetros obligatorios.
		Assert.assertNotNull(UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes())));
		
		//Se indica la firma almacenada en un repositorio
		inputParams.remove(EVisorTagsRequest.ENCODED_SIGNATURE);
		inputParams.put(EVisorTagsRequest.SIGN_REPO_REPOSITORY_ID, "repoId");
		inputParams.put(EVisorTagsRequest.SIGN_REPO_OBJECT_ID, "objectId");
		
		inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		Assert.assertNotNull(UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes())));
		
		//Se indica la firma almacenada en un repositorio
		inputParams.remove(EVisorTagsRequest.SIGN_REPO_REPOSITORY_ID);
		inputParams.remove(EVisorTagsRequest.SIGN_REPO_OBJECT_ID);
		
		inputParams.put(EVisorTagsRequest.VALIDATION_RESPONSE, Base64Coder.encodeBase64("VALIDATION_RESPONSE"));
		
		inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		Assert.assertNotNull(UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes())));
	}
	
	/**
	 * Tests generateReport method (a method of SignatureReportService)  with optional parameters.
	 * @throws Exception if an error happens.
	 */
	@Test
	public void generateReportWithOptionalParams() throws Exception {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(EVisorTagsRequest.APPLICATION_ID, "APPLICATION_NAME");
		inputParams.put(EVisorTagsRequest.TEMPLATE_ID, "TEMPLATE_ID");
		inputParams.put(EVisorTagsRequest.ENCODED_SIGNATURE, Base64Coder.encodeBase64("signB64"));
		
		//parámetro srsm:IncludeSignature
		inputParams.put(EVisorTagsRequest.INCLUDE_SIGNATURE, "true");
		
		//parámetro srsm:Document/srsm:EncodedDocument
		inputParams.put(EVisorTagsRequest.ENCODED_DOCUMENT, Base64Coder.encodeBase64("ENCODED_DOCUMENT"));
		
		//generamos xml
		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		
		//validamos xml generado
		Assert.assertNotNull(UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes())));
		
		//eliminamos srsm:Document/srsm:EncodedDocument para probar el parámetro srsm:Document/srsm:RepositoryLocation
		inputParams.remove(EVisorTagsRequest.ENCODED_DOCUMENT);
		
		//parámetros srsm:Document/srsm:RepositoryLocation
		inputParams.put(EVisorTagsRequest.DOC_REPO_ID, "srsm:RepositoryId");
		inputParams.put(EVisorTagsRequest.DOC_REPO_OBJECT_ID, "srsm:ObjectId");
		
		//parámetros srsm:Barcode
		Map<String, String> qRCodeParams = new HashMap<String, String>(3);
		qRCodeParams.put("QRCodeWidth", "600");
		qRCodeParams.put("QRCodeHeight", "600");
		qRCodeParams.put("Rotation", "90");

		// introducimos un conjunto de códigos de barra en los parámetros de entrada.
		inputParams.put(EVisorTagsRequest.BARCODE, new Map<?, ?>[ ] { EVisorUtil.newBarcodeMap("Prueba código barra tipo QRCode", "QRCode", qRCodeParams), 
		                                                              EVisorUtil.newBarcodeMap("986656487", "EAN128", null), 
		                                                              EVisorUtil.newBarcodeMap("Prueba código barra tipo DataMatrix", "DataMatrix", null) });
		//parámetro srsm:ExternalParameters
		Map<String, String> externalParameters = new HashMap<String, String>(2);
		externalParameters.put("externalParams1", "1111");
		externalParameters.put("externalParams2", "2222");
		inputParams.put(EVisorTagsRequest.EXTERNAL_PARAMETERS_PARAM, EVisorUtil.newParameterMap(externalParameters));
		
		//generamos xml
		inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		
		//validamos xml generado
		Assert.assertNotNull(UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes())));
	}
	
	/**
	 * Tests validateReport without mandatory parameters.
	 * @throws Exception if an error happens.
	 */
	@Test
	public void validateReportWithoutMandatoryParams () throws Exception {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(EVisorTagsRequest.APPLICATION_ID, "");
		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes()));
	}
	
	/**
	 * Tests validateReport with mandatory parameters.
	 * @throws Exception if an error happens.
	 */
	@Test
	public void validateReportWithMandatoryParams () throws Exception {
		Map<String, Object> inputParams = new HashMap<String, Object>();
		inputParams.put(EVisorTagsRequest.APPLICATION_ID, "");
		inputParams.put(EVisorTagsRequest.REPORT, Base64Coder.encodeBase64("REPORT"));
		String inputXml = TransformersFacade.getInstance().generateXml(inputParams, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		Assert.assertNotNull(UtilsXML.getDocumentWithXsdValidation(xsdFile, new ByteArrayInputStream(inputXml.getBytes())));
	}
	
	@Test
	public void generateReportResponse () throws Exception {
		String xmlResponse = new String(UtilsFileSystem.readFile("evisor/GenerationResponse.xml", true));
		Map<String, Object> outputParams = TransformersFacade.getInstance().parseResponse(xmlResponse, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.GENERATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		Assert.assertEquals("0", outputParams.get("srsm:Result/srsm:Code"));
	}
	
	@Test
	public void validateReportResponse () throws Exception {
		String xmlResponse = new String(UtilsFileSystem.readFile("evisor/ValidateReportResponse.xml", true));
		Map<String, Object> outputParams = TransformersFacade.getInstance().parseResponse(xmlResponse, EVisorConstants.SIGNATURE_REPORT_SERVICE, EVisorConstants.VALIDATE_REPORT_METHOD, TransformersConstants.VERSION_10);
		Assert.assertEquals("0", outputParams.get("srsm:Result/srsm:Code"));
	}
	
}
