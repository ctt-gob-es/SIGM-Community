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
 * <b>File:</b><p>es.gob.afirma.afirma5ServiceInvoker.CacheCertificatesTest.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>04/02/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/02/2014.
 */
package es.gob.afirma.afirma5ServiceInvoker;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import es.gob.afirma.transformers.TransformersConstants;
import es.gob.afirma.transformers.TransformersFacade;
import es.gob.afirma.utils.GeneralConstants;
import es.gob.afirma.utils.GenericUtils;
import es.gob.afirma.utils.NativeTagsRequest;
import es.gob.afirma.utils.UtilsFileSystem;
import es.gob.afirma.utils.DSSConstants.DSSTagsRequest;
import es.gob.afirma.utils.DSSConstants.ReportDetailLevel;
import es.gob.afirma.utils.DSSConstants.ResultProcessIds;

import junit.framework.TestCase;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 04/02/2014.
 */
public class CacheCertificatesTest extends TestCase {

    @Test
    public void testCache() {
	/*
	 * Esta prueba verificará el uso y funcionalidad de la caché de respuestas para validación de certificados contra @Firma en sus servicios
	 * nativo y DSS. Básicamente se trata de comprobar que cada vez que se lleva a cabo una petición de validación de certificado contra @Firma
	 * se comprobará previamente si esa petición se hizo antes, si la respuesta está en la caché para acceder a ella sin tener que recurrir a la
	 * llamada al servicio, y cómo la caché se va actualizando en función del tiempo de vida y tamaño para la caché definidos.
	 */

	String application = "dipucr.sigem_quijote";

	Map<String, Object> inParams = new HashMap<String, Object>();

	try {

	    /*
	     * 1ª vez que se usa el certificado serversigner.cer en el servicio DSS. Se almacena el certificado en caché.
	     */
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, application);
	    inParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	    inParams.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	    inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	    inParams.put(DSSTagsRequest.CHECK_CERTIFICATE_STATUS, "true");
	    inParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	    inParams.put(DSSTagsRequest.X509_CERTIFICATE, UtilsFileSystem.readFileBase64Encoded("serversigner.cer", true));
	    String xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	    String xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, application);
	    Map<String, Object> propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	    assertEquals(ResultProcessIds.REQUESTER_ERROR, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));

	    /*
	     * 1ª vez que se usa el certificado serversigner2.crt en el servicio DSS. Se almacena el certificado en caché.
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(DSSTagsRequest.CLAIMED_IDENTITY, application);
	    inParams.put(DSSTagsRequest.INCLUDE_CERTIFICATE, "true");
	    inParams.put(DSSTagsRequest.INCLUDE_REVOCATION, "true");
	    inParams.put(DSSTagsRequest.REPORT_DETAIL_LEVEL, ReportDetailLevel.ALL_DETAILS);
	    inParams.put(DSSTagsRequest.CHECK_CERTIFICATE_STATUS, "true");
	    inParams.put(DSSTagsRequest.RETURN_READABLE_CERT_INFO, "");
	    inParams.put(DSSTagsRequest.X509_CERTIFICATE, UtilsFileSystem.readFileBase64Encoded("serversigner2.crt", true));
	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	    xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, application);
	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.DSS_AFIRMA_VERIFY_CERTIFICATE_REQUEST, GeneralConstants.DSS_AFIRMA_VERIFY_METHOD, TransformersConstants.VERSION_10);
	    assertEquals(ResultProcessIds.SUCESS, propertiesResult.get(TransformersFacade.getInstance().getParserParameterValue("ResultMayor")));
	    
	    /*
	     * 1ª vez que se usa el certificado serversigner.cer en el servicio nativo en inglés. Se almacena el certificado en caché junto con la respuesta
	     * del servicio nativo en inglés y se borra de la caché el mismo certificado pero con la respuesta del servicio DSS.
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(NativeTagsRequest.APPLICATION_ID, application);
	    inParams.put(NativeTagsRequest.CERTIFICATE, UtilsFileSystem.readFileBase64Encoded("serversigner.cer", true));
	    inParams.put(NativeTagsRequest.VALIDATION_MODE, "0");

	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, TransformersConstants.VERSION_10);
	    xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, application);
	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, GeneralConstants.CERTIFICATE_VALIDATION_REQUEST, TransformersConstants.VERSION_10);
	    assertNotNull(propertiesResult);
	    assertEquals("0", GenericUtils.getValueFromMapsTree("ValidationResult/result", propertiesResult));

	    /*
	     * Primera vez que se utiliza el certificado serversigner.cer en el servicio nativo en español. Se almacena el certificado en caché junto con la respuesta
	     * del servicio nativo en español y se borra de la caché el mismo certificado pero con la respuesta del servicio nativo en inglés.
	     */
	    inParams = new HashMap<String, Object>();
	    inParams.put(NativeTagsRequest.ID_APLICACION, application);
	    inParams.put(NativeTagsRequest.CERTIFICADO, UtilsFileSystem.readFileBase64Encoded("serversigner.cer", true));
	    inParams.put(NativeTagsRequest.MODO_VALIDACION, "1");
	    
	    xmlInput = TransformersFacade.getInstance().generateXml(inParams, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, TransformersConstants.VERSION_10);
	    xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, application);
	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, TransformersConstants.VERSION_10);
	     assertEquals("4", GenericUtils.getValueFromMapsTree("ResultadoValidacion/resultado", propertiesResult));
	    

	    // Dormimos durante 120 segundos
	    Thread.sleep(120000);

	    /*
	     * Segunda vez que se utiliza el certificado autenticacion_ricoh.cer en el servicio nativo en español. Se borra la entrada que existía
	     * pues ha pasado el tiempo de vida y se almacena el certificado en caché.
	     */
	    xmlOutput = Afirma5ServiceInvokerFacade.getInstance().invokeService(xmlInput, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, application);
	    propertiesResult = TransformersFacade.getInstance().parseResponse(xmlOutput, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, GeneralConstants.VALIDACION_CERTIFICADO_REQUEST, TransformersConstants.VERSION_10);
	    // Ahora esta línea de código sería la respuesta normal para el
	    // servicio nativo
	    assertEquals("4", GenericUtils.getValueFromMapsTree("ResultadoValidacion/resultado", propertiesResult));
	} catch (Exception e) {
	    assertTrue(false);
	}
    }

}
