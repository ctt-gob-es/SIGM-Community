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

package es.seap.minhap.portafirmas.ws.query.service;

import javax.activation.DataHandler;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.QueryServiceBO;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.CsvJustificante;
import es.seap.minhap.portafirmas.ws.bean.DocumentTypeList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJobList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserJobAssociatedList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserList;
import es.seap.minhap.portafirmas.ws.bean.ImportanceLevelList;
import es.seap.minhap.portafirmas.ws.bean.JobList;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.SeatList;
import es.seap.minhap.portafirmas.ws.bean.Signature;
import es.seap.minhap.portafirmas.ws.bean.StateList;
import es.seap.minhap.portafirmas.ws.bean.UserList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

/**
 * Java class for query service implementation.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@Service("QueryServiceImpl")
@javax.jws.WebService(name = "QueryService", serviceName = "QueryService", portName = "QueryServicePort", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:v2.0", wsdlLocation = "WSDL/QueryService.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.query.service.QueryService")
@BindingType(SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class QueryServiceImpl implements QueryService {

	Logger log = Logger.getLogger(QueryServiceImpl.class);
	
	@Autowired
	private QueryServiceBO queryServiceBO;

	// Inyecto el contexto de la aplicación para obtener los beans
	/*private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#downloadDocument(java.lang.String
	 *      documentId )*
	 */
	public DataHandler downloadDocument(Authentication authentication, String documentId) throws PfirmaException {
		log.debug("downloadDocument init");	
		try {			
			return queryServiceBO.downloadDocument(authentication, documentId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en downloadDocument:", t);
			throw new PfirmaException ("Error inesperado en downloadDocument");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#queryUsers(java.lang.String
	 *      query )*
	 */
	public UserList queryUsers(Authentication authentication, String query) throws PfirmaException {
		log.debug("queryUsers init");		
		try {			
			return queryServiceBO.queryUsers(authentication, query);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryUsers:", t);
			throw new PfirmaException ("Error inesperado en queryUsers");
		}
	} 

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#downloadSign(java.lang.String
	 *      documentId )*
	 */
	public Signature downloadSign(Authentication authentication, String documentId) throws PfirmaException {
		try {
			log.debug("downloadSign init");				
			return queryServiceBO.downloadSign(authentication, documentId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en downloadSign:", t);
			throw new PfirmaException ("Error inesperado en downloadSign");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#queryDocumentTypes(java.lang.String
	 *      query )*
	 */
	public DocumentTypeList queryDocumentTypes(Authentication authentication, String query) throws PfirmaException {
		log.debug("queryDocumentTypes init");		
		try {			
			return queryServiceBO.queryDocumentTypes(authentication, query);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryDocumentTypes:", t);
			throw new PfirmaException ("Error inesperado en queryDocumentTypes");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#queryJobs(es.seap.minhap.portafirmas.ws.bean.Authentication, java.lang.String)
	 */
	public JobList queryJobs(Authentication authentication, String query) throws PfirmaException {
		log.debug("queryJobs init");
		try {			
			return queryServiceBO.queryJobs(authentication, query);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryJobs:", t);
			throw new PfirmaException ("Error inesperado en queryJobs");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#queryRequest(es.seap.minhap.portafirmas.ws.bean.Authentication, java.lang.String)
	 */
	public Request queryRequest(Authentication authentication, String requestId) throws PfirmaException {
		log.debug("queryRequest init");
		try {			
			return queryServiceBO.queryRequest(authentication, requestId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryRequest:", t);
			throw new PfirmaException ("Error inesperado en queryRequest");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#queryStates(es.seap.minhap.portafirmas.ws.bean.Authentication, java.lang.String)
	 */
	public StateList queryStates(Authentication authentication, String query) throws PfirmaException {
		log.debug("queryStates init");
		try {			
			return queryServiceBO.queryStates(authentication, query);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryStates:", t);
			throw new PfirmaException ("Error inesperado en queryStates");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#getCVS(es.seap.minhap.portafirmas.ws.bean.Authentication, es.seap.minhap.portafirmas.ws.bean.Signature)
	 */
	public String getCVS(Authentication authentication, Signature firma) throws PfirmaException {
		log.debug("getCVS init");
		try {			
			return queryServiceBO.getCVS(authentication, firma);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en getCVS:", t);
			throw new PfirmaException ("Error inesperado en getCVS");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#queryImportanceLevels(es.seap.minhap.portafirmas.ws.bean.Authentication, java.lang.String)
	 */
	
	public ImportanceLevelList queryImportanceLevels(Authentication authentication, String query) throws PfirmaException {
		log.debug("queryImportanceLevels init");
		try {			
			return queryServiceBO.queryImportanceLevels(authentication, query);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryImportanceLevels:", t);
			throw new PfirmaException ("Error inesperado en queryImportanceLevels");
		}
	}
	
	public EnhancedUserList queryEnhancedUsers (Authentication authentication, String queryUser, String querySeat) throws PfirmaException {		
		log.debug("queryEnhancedUsers init");
		try {					
			return queryServiceBO.queryEnhancedUsers(authentication, queryUser, querySeat);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryEnhancedUsers:", t);
			throw new PfirmaException ("Error inesperado en queryEnhancedUsers");
		}
	}
	
	public EnhancedJobList queryEnhancedJobs (Authentication authentication, String queryJob, String querySeat) throws PfirmaException {		
		log.debug("queryEnhancedJobs init");
		try {			
			return queryServiceBO.queryEnhancedJobs(authentication, queryJob, querySeat);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryEnhancedJobs:", t);
			throw new PfirmaException ("Error inesperado en queryEnhancedJobs");
		}
	}
	
	public SeatList querySeats (Authentication authentication, String query) throws PfirmaException {		
		log.debug("querySeats init");
		try {			
			return queryServiceBO.querySeats(authentication, query);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en querySeats:", t);
			throw new PfirmaException ("Error inesperado en querySeats");
		}
	}
	
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToJob (Authentication authentication, String jobIdentifier) throws PfirmaException {
		log.debug("queryEnhancedUserJobAssociatedToJob init");
		try {			
			return queryServiceBO.queryEnhancedUserJobAssociatedToJob(authentication, jobIdentifier);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryEnhancedUserJobAssociatedToJob:", t);
			throw new PfirmaException ("Error inesperado en queryEnhancedUserJobAssociatedToJob");
		}
	}
	
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToUser (Authentication authentication, String userIdentifier) throws PfirmaException {
		log.debug("queryEnhancedUserJobAssociatedToUser init");
		try {			
			return queryServiceBO.queryEnhancedUserJobAssociatedToUser(authentication, userIdentifier);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryEnhancedUserJobAssociatedToUser:", t);
			throw new PfirmaException ("Error inesperado en queryEnhancedUserJobAssociatedToUser");
		}
	}

	@Override
	public CsvJustificante queryCSVyJustificante(Authentication authentication, String documentId) throws PfirmaException {
		log.debug("queryCSVyJustificante init");
		try {		
			return queryServiceBO.queryCSVyJustificante(authentication, documentId);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en queryCSVyJustificante:", t);
			throw new PfirmaException ("Error inesperado en queryCSVyJustificante");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.seap.minhap.portafirmas.ws.query.service.QueryService#getReportFromCVS(es.seap.minhap.portafirmas.ws.bean.Authentication, java.lang.String)
	 */
//	@Override
//	public DataHandler getReportFromCSV(Authentication authentication, String csv) throws PfirmaException {
//		log.debug("getReportFromCSV init");
//		QueryServiceBO queryServiceBO = applicationContext.getBean(QueryServiceBO.class);
//		return queryServiceBO.getReportFromCSV(authentication, csv);
//	}

}
