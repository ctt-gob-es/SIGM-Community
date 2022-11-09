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

package es.seap.minhap.portafirmas.ws.modify.service;

import javax.xml.ws.BindingType;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.ModifyServiceBO;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.Document;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.SignerList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

/**
 * Java class for modify service implementation.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("ModifyServiceImpl")
@javax.jws.WebService(name = "ModifyService", serviceName = "ModifyService", portName = "ModifyServicePort", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:v2.0", wsdlLocation = "WSDL/ModifyService.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.modify.service.ModifyService")
@BindingType(SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class ModifyServiceImpl implements ModifyService { 
	
	Logger log = Logger.getLogger(ModifyServiceImpl.class);

	@Autowired
	private ModifyServiceBO modifyServiceBO;
	
	// Inyecto el contexto de la aplicación para obtener los beans
	/*private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#sendRequest(java.lang.String
	 *      requestId )*
	 */
	public void sendRequest(Authentication authentication, javax.xml.ws.Holder<java.lang.String> requestId)	throws PfirmaException {
		log.debug("sendRequest init");
		try {
			modifyServiceBO.sendRequest(authentication, requestId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en sendRequest:", t);
			throw new PfirmaException ("Error inesperado en sendRequest");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#insertDocument(java.lang.String
	 *      requestId ,)juntadeandalucia.cice.pfirma.type.v2.Document document )*
	 */
	public java.lang.String insertDocument(Authentication authentication, String requestId, Document document) throws PfirmaException {
		log.debug("insertDocument init");
		try {
			return modifyServiceBO.insertDocument(authentication, requestId, document);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en insertDocument:", t);
			throw new PfirmaException ("Error inesperado en insertDocument");
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#updateRequest(juntadeandalucia.cice.pfirma.type.v2.Request
	 *      request )*
	 */
	public java.lang.String updateRequest(Authentication authentication, 
			es.seap.minhap.portafirmas.ws.bean.Request request)
			throws PfirmaException {
		
		log.debug("updateRequest init");
		try {
			return modifyServiceBO.updateRequest(authentication, request);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en updateRequest:", t);
			throw new PfirmaException ("Error inesperado en updateRequest");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#createRequest(juntadeandalucia.cice.pfirma.type.v2.Request
	 *      request )*
	 */
	public java.lang.String createRequest(Authentication authentication, Request request) throws PfirmaException {
		
		log.debug("createRequest init");
		try {
			return modifyServiceBO.createRequest(authentication, request);
		} catch (PfirmaException e) {
			log.error ("Error en createRequest:", e);
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en createRequest:", t);
			throw new PfirmaException ("Error inesperado en createRequest");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#deleteDocument(java.lang.String
	 *      documentId )*
	 */
	public void deleteDocument(Authentication authentication, Holder<String> documentId) throws PfirmaException {
		
		log.debug("deleteDocument init");
		try {
			modifyServiceBO.deleteDocument(authentication, documentId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteDocument:", t);
			throw new PfirmaException ("Error inesperado en deleteDocument");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#deleteRequest(java.lang.String
	 *      requestId )*
	 */
	public void deleteRequest(Authentication authentication, Holder<String> requestId) throws PfirmaException {
		
		log.debug("deleteRequest init");
		try {
			modifyServiceBO.deleteRequest(authentication, requestId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteRequest:", t);
			throw new PfirmaException ("Error inesperado en deleteRequest");
		}
	}
	
	/*
	 * [Ticket1269#Teresa] Anular circuito de firmas
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#deleteRequestSend(java.lang.String
	 *      requestId )*
	 */
	public void deleteRequestSend(Authentication authentication, Holder<String> requestId) throws PfirmaException {
		
		log.debug("deleteRequest init");
		try {
			modifyServiceBO.deleteRequestSend(authentication, requestId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteRequest:", t);
			throw new PfirmaException ("Error inesperado en deleteRequest");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#insertSigners(java.lang.String
	 *      requestId ,) int signLine
	 *      ,)juntadeandalucia.cice.pfirma.type.v2.SignerList signerList)*
	 */
	public void insertSigners(Authentication authentication, Holder<String> requestId, int signLine, SignerList signerList, String signLineType) throws PfirmaException {
		log.debug("insertSigners init");
		try {
			modifyServiceBO.insertSigners(authentication, requestId, signLine, signerList, signLineType);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en insertSigners:", t);
			throw new PfirmaException ("Error inesperado en insertSigners");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#deleteSigners(java.lang.String
	 *      requestId ,) juntadeandalucia.cice.pfirma.type.v2.SignerList
	 *      signerList)*
	 */
	public void deleteSigners(Authentication authentication, Holder<String> requestId, Integer signLineNumber, SignerList signerList) throws PfirmaException {
		log.debug("deleteSigners init");
		try {			
			modifyServiceBO.deleteSigners(authentication, requestId, signLineNumber, signerList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteSigners:", t);
			throw new PfirmaException ("Error inesperado en deleteSigners");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see juntadeandalucia.cice.pfirma.modify.v2.ModifyService#removeRequest(java.lang.String
	 *      requestId )*
	 */
	public void removeRequest(Authentication authentication, Holder<String> requestId, String removingMessage) throws PfirmaException {
		log.debug("removeRequest init");	
		try {		
			modifyServiceBO.removeRequest(authentication, requestId, removingMessage);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en removeRequest:", t);
			throw new PfirmaException ("Error inesperado en removeRequest");
		}	
	}

	
}
