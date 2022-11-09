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

package es.seap.minhap.portafirmas.ws.mobile;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.mobile.MobileServiceBO;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileAccesoClave;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileApplicationList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocSignInfoList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocument;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileDocumentList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireRequestList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileFireTrasactionResponse;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSIMUser;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileSIMUserStatus;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequest;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestFilterList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileRequestList;
import es.seap.minhap.portafirmas.ws.mobile.bean.MobileStringList;
import es.seap.minhap.portafirmas.ws.mobile.exception.MobileException;

@Service("MobileServiceImpl")
@javax.jws.WebService(name = "MobileService", serviceName = "MobileService", portName = "MobileServicePort", targetNamespace = "urn:juntadeandalucia:cice:pfirma:mobile:v2.0", wsdlLocation = "WSDL/MobileService.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.mobile.MobileService")
public class MobileServiceImpl implements MobileService {

	private Logger log = Logger.getLogger(MobileServiceImpl.class);

	@Autowired
	private MobileServiceBO mobileServiceBO;

	public MobileRequestList queryRequestList(String certificate, String state, String initPage, String pageSize,
			MobileStringList signFormats, MobileRequestFilterList requestFilters)
			throws MobileException {
		log.debug("Mobile Service - queryRequest");
		return mobileServiceBO.queryRequestList(certificate, state, initPage, pageSize, signFormats, requestFilters);
	}

	public MobileRequest queryRequest(String certificate, String requestId)
			throws MobileException {
		log.debug("Mobile Service - queryRequest");
		return mobileServiceBO.queryRequest(certificate, requestId);
	}

	public MobileDocumentList getDocumentsToSign(String certificate, String requestTagId) throws MobileException {
		log.debug("Mobile Service - presign");
		return mobileServiceBO.getDocumentsToSign(certificate, requestTagId);
	}

	public String saveSign(String certificate, String requestTagId, MobileDocSignInfoList docSignInfoList) throws MobileException {
		log.debug("Mobile Service - postsign");
		return mobileServiceBO.saveSign(certificate, requestTagId, docSignInfoList);
	}

	public String rejectRequest(String certificate, String requestId, String textRejection)
			throws MobileException {
		return mobileServiceBO.rejectRequest(certificate, requestId, textRejection);
	}

	public MobileDocument documentPreview(String certificate, String documentId)
			throws MobileException {
		log.debug("Mobile Service - documentPreview");
		return mobileServiceBO.documentPreview(certificate, documentId);
	}

	public MobileApplicationList queryApplicationsMobile(String certificate) throws MobileException {
		log.debug("Mobile Service - queryApplicationsMobile");
		return mobileServiceBO.queryApplicationsMobile(certificate);
	}

	public String approveRequest(String certificate, String requestTagId) throws MobileException {
		log.debug("Mobile Service - queryApplicationsMobile");
		return mobileServiceBO.approveRequest(certificate, requestTagId);
	}

	public MobileDocument signPreview(String certificate, String documentId)
			throws MobileException {
		log.debug("Mobile Service - signPreview");
		return mobileServiceBO.signPreview(certificate, documentId);
	}

	public MobileDocument reportPreview(String certificate, String documentId)
			throws MobileException {
		log.debug("Mobile Service - reportPreview");
		return mobileServiceBO.reportPreview(certificate, documentId);
	}
	
	public MobileSIMUserStatus registerSIMUser(String certificate, MobileSIMUser register)
			throws MobileException {
		log.debug("Mobile Service - registerSIMUser");
		return mobileServiceBO.registerSIMUser(certificate, register);
	}
	
	public String validateUser(String certificate)
			throws MobileException {
		log.debug("Mobile Service - validateUser");
		return mobileServiceBO.validateUser(certificate);
	}
	
	public String updateNotifyPush(String certificate, String estadoNotifyPush)
			throws MobileException {
		log.debug("Mobile Service - updateNotifyPush");
		return mobileServiceBO.updateNotifyPush(certificate, estadoNotifyPush);
	}
	
	public String estadoNotifyPush(String certificate)
			throws MobileException {
		log.debug("Mobile Service - estadoNotifyPush");
		return mobileServiceBO.estadoNotifyPush(certificate);
	}
	
	public MobileAccesoClave solicitudAccesoClave(String spUrl, String spReturn) 
			throws MobileException {
		log.debug("Mobile Service - solicitudAccesoClave");
		return mobileServiceBO.solicitudAccesoClave(spUrl, spReturn);
	}

	public String procesarRespuestaClave(String samlresponse, String remoteHost) throws MobileException {
		log.debug("Mobile Service - procesarRespuestaClave");
		return mobileServiceBO.procesarRespuestaClave(samlresponse, remoteHost);
	}

	public MobileFireTrasactionResponse fireTransaction(String certificate, MobileStringList idRequestList) throws MobileException {
		return mobileServiceBO.getFIReTransaction(certificate, idRequestList);
	}

	public MobileFireRequestList signFireCloud(String certificate, MobileStringList idRequestList, String transactionId) throws MobileException {
		return mobileServiceBO.signFIReCloud(certificate, idRequestList, transactionId);
	}

}
