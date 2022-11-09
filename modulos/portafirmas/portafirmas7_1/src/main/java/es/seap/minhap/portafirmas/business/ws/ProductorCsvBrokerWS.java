/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.business.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.gob.aapp.csvbroker.webservices.querydocument.model.v1.CSVQueryDocumentResponse;
import es.gob.aapp.csvbroker.webservices.querydocument.model.v1.DocumentResponse;
import es.gob.aapp.csvbroker.webservices.querydocument.v1.CSVQueryDocumentException;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.ServletHelperBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;

@Service("CSVQueryDocumentServiceImpl")
@WebService(serviceName = "CSVQueryDocumentService", portName = "CSVQueryDocumentServicePort", endpointInterface = "es.gob.aapp.csvbroker.webservices.querydocument.v1.CSVQueryDocumentService", targetNamespace = "urn:es:gob:aapp:csvbroker:webservices:querydocument:v1.0", wsdlLocation = "WEB-INF/wsdl/NewWebServiceFromWSDL/CSVQueryDocumentService.wsdl")
public class ProductorCsvBrokerWS {

	private static final String C_PARAM = "cParam";
	
	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private RequestBO requestBO;
	
	@Autowired 
	private ServletHelperBO servletHelperBO;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private BinaryDocumentsBO binaryDocumentsBO;
	
	@Resource(name = "messageProperties")
	private Properties messages;
	
	protected final Log log = LogFactory.getLog(getClass());

	
	//Desde Carpeta Ciudadana
    public es.gob.aapp.csvbroker.webservices.querydocument.model.v1.CSVQueryDocumentResponse csvQueryDocument(es.gob.aapp.csvbroker.webservices.querydocument.model.v1.WSCredential credential, es.gob.aapp.csvbroker.webservices.querydocument.model.v1.CSVQueryDocumentRequest queryDocumentRequest) throws es.gob.aapp.csvbroker.webservices.querydocument.v1.CSVQueryDocumentException {
        
    	PfConfigurationsParameterDTO parametroClaveBroker = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", C_PARAM,
				Constants.C_PARAMETER_CLAVE_BROKER);
    	PfConfigurationsParameterDTO parametroUsuarioBroker = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				"administration.queryParameterByName", C_PARAM,
				Constants.C_PARAMETER_USUARIO_BROKER);
    	
    	if(!parametroClaveBroker.getTvalue().equalsIgnoreCase(credential.getPassword())
    			|| !parametroUsuarioBroker.getTvalue().equalsIgnoreCase(credential.getIdaplicacion())){
    		throw new CSVQueryDocumentException("Usuario y clave incorrectos",null);
    	}
    	
    	String csv = Util.formatearCsv(queryDocumentRequest.getCsv());
    		
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("csv", csv);
		boolean normalizado = false;
		PfSignsDTO firma = (PfSignsDTO) baseDAO.queryElementMoreParameters("request.signByCsv", parameters);
    	if (firma == null){
    		firma = (PfSignsDTO) baseDAO.queryElementMoreParameters("request.signByCsvNormalizado", parameters);
    		normalizado = true;
    	}
		
    	CSVQueryDocumentResponse returnValue = new CSVQueryDocumentResponse();

    	if(firma != null){		
        	PfRequestsDTO request = firma.getPfDocument().getPfRequest();
        	List<PfRequestTagsDTO> reqTags = requestBO.getAnulledRequestTags(request);
        	//Hay línea con etiqueta TIPO.ANULADA >> Petición anulada
        	if(!Util.esVacioONulo(reqTags)){
        		returnValue.setDocumentResponse(null);
	    		returnValue.setCode("2");
	        	returnValue.setDescription("El documento ha sido anulado");
        	}else{
        		byte[] report = null;
        		if (normalizado){
        			report = firma.getDocumentos().getBInformeNormalizado();
        		} else {
        			report = firma.getDocumentos().getBInforme();	
        		}
        		
        		if (report == null){
        			try {
        					report = servletHelperBO.obtenerInformePorCSV(firma, firma.getPfDocument().getPfRequest().getPfApplication(), normalizado);
        			} catch (Exception e) {
        				log.debug("ERROR: ValidCSVController.validCSV: " + messages.getProperty("CSVNotFound") + ", ", e);
        				
        			} 
        		}
        		if(report==null){
        			returnValue.setDocumentResponse(null);
        		    returnValue.setCode("2");
        		    returnValue.setDescription("El documento no existe en el portafirmas");
        		}else{
                	returnValue.setCode("0");
                	DocumentResponse documentResponse = new DocumentResponse();
                	documentResponse.setContent(report);
                	documentResponse.setMime("application/pdf");
                	documentResponse.setName(Constants.SIGN_REPORT_FILE_PREFIX+"_"+ System.currentTimeMillis() + ".pdf");
                	returnValue.setDocumentResponse(documentResponse);    
        		}
        	}
    	}else{
	    	returnValue.setDocumentResponse(null);
		    returnValue.setCode("2");
		    returnValue.setDescription("El documento no existe en el portafirmas");
    	}
        return returnValue;
    }
    
}
