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
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerFacadeTest.java.</p>
 * <b>Description:</b><p>Class tests Afirma5ServiceInvokerFacade class.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>18/03/2011.</p>
 * @author Gobierno de Espaï¿½a.
 * @version 1.0, 18/03/2011.
 */
package es.gob.afirma.afirma5ServiceInvoker;

import java.util.Properties;

import junit.framework.TestCase;
import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.UtilsFileSystem;


/**
 * <p>Class tests Afirma5ServiceInvokerFacade class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 18/03/2011.
 */
public class Afirma5ServiceInvokerFacadeTest extends TestCase {
	
	public static final String APPLICATION_NAME = "dipucr.sigem_quijote";

	private static final String MSG_ERROR_NOT_VALID_PARAM = "No se ha lanzado la excepción por parámetros entrada no válidos";
	
	private static final String MSG_ERROR_EMPTY_PARAM = "No se ha lanzado la excepción por parámetros entrada vacíos";
	
	public void testInvokeService () throws Exception {

		// valores nulos.
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(null, null, null, APPLICATION_NAME);
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}
		// valores vacíos
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("", "", "", APPLICATION_NAME);
			fail(MSG_ERROR_EMPTY_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}

		//VALORES NO VÁLIDOS:::..
		
		//xml no válido
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("xmlPr", GeneralConstants.FIRMA_SERVIDOR_REQUEST, GeneralConstants.FIRMA_SERVIDOR_REQUEST, APPLICATION_NAME);
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}
		
		//servicio no existente
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)), 
             "serviceTest", GeneralConstants.FIRMA_SERVIDOR_REQUEST, APPLICATION_NAME);
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}

		//método no existente
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)), 
			                                                                   GeneralConstants.FIRMA_SERVIDOR_REQUEST, "methodTest", APPLICATION_NAME);
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}
		
		//Nombre de aplicación no existente/válida
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)),
			   GeneralConstants.FIRMA_SERVIDOR_REQUEST, GeneralConstants.FIRMA_SERVIDOR_REQUEST, "AppNoExiste");
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}
		
		//valores válidos
		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)),
		                     GeneralConstants.FIRMA_SERVIDOR_REQUEST, GeneralConstants.FIRMA_SERVIDOR_REQUEST, APPLICATION_NAME);
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<Respuesta><estado>true</estado>") > 0);

	}


	public void testInvokeServiceWithProperties () throws Exception {

		// valores nulos.
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(null, null, null, new Properties());
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}
		// valores vacíos
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("", "", "", new Properties());
			fail(MSG_ERROR_EMPTY_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}

		//valores no válidos
		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService("xmlPr", GeneralConstants.FIRMA_SERVIDOR_REQUEST, GeneralConstants.FIRMA_SERVIDOR_REQUEST, new Properties());
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}

		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)),
			                                                        "serviceTest", GeneralConstants.FIRMA_SERVIDOR_REQUEST, getPropertiesForSvcInvoker());
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}

		try {
			Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)), 
			                                                        GeneralConstants.FIRMA_SERVIDOR_REQUEST, "methodTest", getPropertiesForSvcInvoker());
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {}

		//valores no válidos con colección de propiedades incompleta
		try {
			Properties prop = getPropertiesForSvcInvoker();
			prop.remove("endPoint");
			Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)),
			                                                        GeneralConstants.FIRMA_SERVIDOR_REQUEST, GeneralConstants.FIRMA_SERVIDOR_REQUEST, prop);
			fail(MSG_ERROR_NOT_VALID_PARAM);
		} catch (Afirma5ServiceInvokerException e) {
			assertTrue(true);
		}

		//valores válidos.
		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)),
		                                                                           GeneralConstants.FIRMA_SERVIDOR_REQUEST, GeneralConstants.FIRMA_SERVIDOR_REQUEST, getPropertiesForSvcInvoker());
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<Respuesta><estado>true</estado>") > 0);
	}


	/**Tests de los algunos servicios nativos @Firma con valores válidos.*/

	public void testValidateCertificate() throws Exception {
		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().
		invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/peticion_ValidarCertificado.xml",true)),
		              GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, APPLICATION_NAME);
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<resultado>0</resultado>") > 0);
	}

	public void testGetInfoCertificate() throws Exception {
		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().
		invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/peticion_ObtenerInfoCertificado.xml", true)), 
		              GeneralConstants.OBTENER_INFO_CERTIFICADO,GeneralConstants.OBTENER_INFO_CERTIFICADO, APPLICATION_NAME);
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<idCampo>serialNumber</idCampo>") > 0);
	}

	public void testServerSignature() throws Exception {

		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().
		invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureRequest.xml", true)), 
		              GeneralConstants.FIRMA_SERVIDOR_REQUEST,GeneralConstants.FIRMA_SERVIDOR_REQUEST, APPLICATION_NAME);
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<estado>true</estado>") > 0);
	}

	public void testServerSignatureCoSign() throws Exception {

		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().
		invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/ServerSignatureCoSignRequest.xml", true)),
		              GeneralConstants.FIRMA_SERVIDOR_COSIGN_REQUEST,GeneralConstants.FIRMA_SERVIDOR_COSIGN_REQUEST, APPLICATION_NAME);
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<estado>true</estado>") > 0);
	}


	public void testSignatureValidation() throws Exception {

		String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().
		invokeService(new String(UtilsFileSystem.readFile("xmlTests/serviceInvoker/SignatureValidationRequest.xml", true)), 
		              GeneralConstants.VALIDAR_FIRMA_REQUEST,GeneralConstants.VALIDAR_FIRMA_REQUEST, APPLICATION_NAME);
		assertNotNull(xmlOutput);
		assertTrue(xmlOutput.indexOf("<estado>true</estado>") > 0);
	}




	/**
	 * Gets a instance of properties collections used in configuration parameters.
	 * @return Gets a instance of properties collections used in configuration parameters.
	 */
	private Properties getPropertiesForSvcInvoker() {
		Properties result = new Properties();
		result.put("com.trustedstore", "truststoreWS.jks");
		result.put("com.trustedstorepassword", "12345");
		result.put("secureMode", "false");
		result.put("endPoint", "des-afirma.redsara.es");
		result.put("servicePath", "afirmaws/services");
		result.put("callTimeout", "20000");
		result.put("authorizationMethod", "none");
		result.put("authorizationMethod.user", "SEIVM");
		result.put("authorizationMethod.password", "12345");
		result.put("authorizationMethod.passwordType", "c");
		result.put("authorizationMethod.userKeystore", "D:/Workspace/Afirma/Integ@/IntegrationKit/src/test/resources/SoapSigner.p12");
		result.put("authorizationMethod.userKeystorePassword", "12345");
		result.put("authorizationMethod.userKeystoreType", "PKCS12");

		return result;
	}
}
