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
 * <b>File:</b><p>xmlTests.transformers.TransformerFacadeTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>21/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 21/03/2011.
 */
package es.gob.afirma.transformers;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.w3c.dom.Document;

import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.UtilsFileSystem;


/**
 * <p>Class that tests TransformerFacade class.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 21/03/2011.
 */
public class TransformerFacadeTest extends TestCase {


	public void testGenerateXml() throws Exception {
		// valores no válidos
		try {
			TransformersFacade.getInstance().generateXml(null, null, null, null);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		try {
			TransformersFacade.getInstance().generateXml(new HashMap<String, Object>(), null, null, null);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}
		
		try {
			TransformersFacade.getInstance().generateXml(newInputParams(), "noValid", null, "novalid");
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}
		
		try {
			TransformersFacade.getInstance().generateXml(newInputParams(), "noValid", "novalid", "novalid");
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		// valor válido
		String xmlInput = TransformersFacade.getInstance().generateXml(newInputParams(), GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, 
		                                         GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, TransformersConstants.VERSION_10);
		assertNotNull(xmlInput);
		assertTrue(xmlInput.indexOf("<peticion>ValidarCertificado") >= 0);
	}

	public void testParseResponse() throws Exception {
		String inputXml = new String(UtilsFileSystem.readFile("xmlTests/transformerService/ValidarCertificadoResponse.xml", true));
		
		// valores no válidos
		try {
			TransformersFacade.getInstance().parseResponse(null, null, null, null);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		try {
			TransformersFacade.getInstance().parseResponse("", "", "", "");
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		try {
			TransformersFacade.getInstance().parseResponse(inputXml, null, null, null);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		try {
			TransformersFacade.getInstance().parseResponse("no_válido", "no_válido", "no_válido", "no_válido");
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}
		
		try {
			TransformersFacade.getInstance().parseResponse("no_válido", GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, TransformersConstants.VERSION_10);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}


		Map<String, Object> outParams = TransformersFacade.getInstance().parseResponse(inputXml, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, 
		             GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, TransformersConstants.VERSION_10);
		assertEquals("0", ((Map<String, Object>)outParams.get("ResultadoValidacion")).get("resultado"));
	}


	public void testGetParserParameterValue() throws Exception {
		// valores no válidos
		assertNull(TransformersFacade.getInstance().getParserParameterValue(null));
		assertNull(TransformersFacade.getInstance().getParserParameterValue("no válidos"));

		// valor válido
		String nodeXpath = TransformersFacade.getInstance().getParserParameterValue("ResultMayor");
		assertEquals("dss:Result/dss:ResultMajor", nodeXpath);
	}

	public void testGetXmlRequestFileByRequestType() throws Exception{
		//valores no válidos
		try {
			TransformersFacade.getInstance().getXmlRequestFileByRequestType(null, null, "", null);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {
		}

		try {
			TransformersFacade.getInstance().getXmlRequestFileByRequestType(GeneralConstants.DSS_AFIRMA_VERIFY_REQUEST, null, null, TransformersConstants.VERSION_10);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {
		}

		try {
			TransformersFacade.getInstance().getXmlRequestFileByRequestType("valor no válido", "method_noValid", "valor no válido", TransformersConstants.VERSION_10);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		//valores válidos
		Document doc = TransformersFacade.getInstance().getXmlRequestFileByRequestType(GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, TransformersConstants.REQUEST_CTE, TransformersConstants.VERSION_10);
		assertNotNull(doc);
	}

	public void testGetParserTemplateByRequestType() throws Exception{
		//valores no válidos
		try {
			TransformersFacade.getInstance().getParserTemplateByRequestType(null, null, null);
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {
		}

		try {
			TransformersFacade.getInstance().getParserTemplateByRequestType(GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, null, "");
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {
		}

		try {
			TransformersFacade.getInstance().getParserTemplateByRequestType("valor no válido", "valor no válido", "valor no válido");
			fail("No se ha lanzado la excepción por parámetros entrada no válidos");
		} catch (TransformersException e) {}

		//valores válidos
		Document doc = TransformersFacade.getInstance().getParserTemplateByRequestType(GeneralConstants.DSS_AFIRMA_SIGN_REQUEST, GeneralConstants.DSS_AFIRMA_SIGN_METHOD, TransformersConstants.VERSION_10);
		assertNotNull(doc);
	}

	private Map<String, Object> newInputParams () {
		Map<String, Object> inParams = new HashMap<String, Object>();
		inParams.put("parametros/idAplicacion", "dipucr.sigem_quijote");
		inParams.put("parametros/certificado", UtilsFileSystem.readFileBase64Encoded("serversigner.cer", true));
		inParams.put("parametros/modoValidacion", "0");
		inParams.put("parametros/obtenerInfo", "true");
		return inParams;
	}
}
