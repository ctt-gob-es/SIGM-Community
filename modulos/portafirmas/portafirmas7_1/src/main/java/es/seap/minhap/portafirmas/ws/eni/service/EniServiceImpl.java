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

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package es.seap.minhap.portafirmas.ws.eni.service;

import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.EniServiceBO;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.CsvJustificante;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.eni.request.MetadataEni;
import es.seap.minhap.portafirmas.ws.eni.request.TipoMetadatosAdicionales;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

/**
 * Java class for modify service implementation.
 * 
 * @author jorge
 */
@Service("EniServiceImpl")
@javax.jws.WebService(name = "EniService", serviceName = "EniService", portName = "EniServicePort", targetNamespace = "urn:juntadeandalucia:cice:pfirma:eni:v2.0", wsdlLocation = "WSDL/EniService.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.eni.service.EniService")
@BindingType(SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class EniServiceImpl implements EniService { 
	
	Logger log = Logger.getLogger(EniServiceImpl.class);
	
	@Autowired
	private EniServiceBO eniServiceBO;
	
	@Override
	public Signature downloadSignEni(Authentication authentication, String documentId,
			MetadataEni metadatosEni, TipoMetadatosAdicionales metadatosAdicionales) throws PfirmaException {
		try {
			log.debug("downloadSignEni init");
			if (metadatosAdicionales != null) {
				return eniServiceBO.downloadSignEni(authentication, documentId, metadatosEni, metadatosAdicionales.getMetadatoAdicional());
			} else {
				return eniServiceBO.downloadSignEni(authentication, documentId, metadatosEni, null);
			}
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en downloadSignEni:", t);
			throw new PfirmaException ("Error inesperado en downloadSignEni");
		}
	}
	
	@Override
	public CsvJustificante justificanteCsvEni(Authentication authentication, String documentId,
			MetadataEni metadatosEni, TipoMetadatosAdicionales metadatosAdicionales) throws PfirmaException {
		log.debug("queryCSVyJustificanteEni init");
		try {		
			if (metadatosAdicionales != null) {
				return eniServiceBO.getReportEniByDocumentHash(authentication, documentId, metadatosEni, metadatosAdicionales.getMetadatoAdicional());
			} else {
				return eniServiceBO.getReportEniByDocumentHash(authentication, documentId, metadatosEni, null);
			}
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryCSVyJustificanteEni:", t);
			throw new PfirmaException ("Error inesperado en queryCSVyJustificanteEni");
		}
	}
	
}
