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

package es.seap.minhap.portafirmas.ws.admin.service;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ws.AdminServiceBO;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.DocumentTypeList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJobList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserList;
import es.seap.minhap.portafirmas.ws.bean.StringList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

@Service("AdminServiceImpl")
@javax.jws.WebService(name = "AdminService", serviceName = "AdminService", portName = "AdminServicePort", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:v2.0", wsdlLocation = "WSDL/AdminService.wsdl", endpointInterface = "es.seap.minhap.portafirmas.ws.admin.service.AdminService")
public class AdminServiceImpl implements AdminService {

	private Logger log = Logger.getLogger(AdminServiceImpl.class);
	
	@Autowired
	private AdminServiceBO adminServiceBO;

	/**
	 * 
	 * @see es.seap.minhap.portafirmas.ws.admin.service.AdminService#insertDocumentsType(DocumentTypeList documentTypeList )
	 */
	public java.lang.String insertDocumentsType(String applicationId,
			DocumentTypeList documentTypeList) throws PfirmaException {
		log.debug("insertDocumentsType init");
		String appId = null;
		try {
			appId = adminServiceBO.insertDocumentsType(applicationId,
					documentTypeList);
			return appId;
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en insertDocumentsType:", t);
			throw new PfirmaException ("Error inesperado en insertDocumentsType");
		}
		
	}	
	

	/**
	 * 
	 * @see es.seap.minhap.portafirmas.ws.admin.service.AdminService#updateDocumentsType(DocumentTypeList documentTypeList )
	 */
	public java.lang.String updateDocumentsType(String applicationId,
			DocumentTypeList documentTypeList) throws PfirmaException {
		log.debug("updateDocumentsType init");
		String appId = null;
		try {
			appId = adminServiceBO.updateDocumentsType(applicationId,
					documentTypeList);
			return appId;
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en updateDocumentsType:", t);
			throw new PfirmaException ("Error inesperado en updateDocumentsType");
		}
		
	}

	/**
	 * 
	 * @see es.seap.minhap.portafirmas.ws.admin.service.AdminService#deleteDocumentsType(DocumentTypeList documentTypeList )
	 */
	public java.lang.String deleteDocumentsType(String applicationId,
			DocumentTypeList documentTypeList) throws PfirmaException {
		log.debug("deleteDocumentsType init");
		String appId = null;
		try {
			appId = adminServiceBO.deleteDocumentsType(applicationId,
					documentTypeList);
			return appId;
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteDocumentsType:", t);
			throw new PfirmaException ("Error inesperado en deleteDocumentsType");
		}
	}
	
	public Integer insertEnhancedUsers (Authentication authentication, EnhancedUserList enhancedUserList) throws PfirmaException {
		log.debug("insertEnhancedUsers init");
		
		Integer n = null;
		
		try {
			n = adminServiceBO.insertUsers(authentication, enhancedUserList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en insertEnhancedUsers:", t);
			throw new PfirmaException ("Error inesperado en insertEnhancedUsers");
		}
		
		return n;
		
	}
	

	public Integer insertEnhancedJobs (Authentication authentication, EnhancedJobList enhancedJobList) throws PfirmaException {
		log.debug("insertEnhancedJobs init");
		
		Integer n = null;
		
		try {
			n = adminServiceBO.insertJobs(authentication, enhancedJobList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en insertEnhancedJobs:", t);
			throw new PfirmaException ("Error inesperado en insertEnhancedJobs");
		}
		
		return n;
		
	}
	
	public Integer updateEnhancedUsers (Authentication authentication, EnhancedUserList enhancedUserList) throws PfirmaException {
		log.debug("updateEnhancedUsers init");
		
		Integer n = null;
		
		try {
			n = adminServiceBO.updateUsers(authentication, enhancedUserList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en updateEnhancedUsers:", t);
			throw new PfirmaException ("Error inesperado en updateEnhancedUsers");
		}
		
		return n;		
		
	}
	
	public Integer updateEnhancedJobs (Authentication authentication, EnhancedJobList enhancedJobList) throws PfirmaException {
		log.debug("updateEnhancedJobs init");
		
		Integer n = null;
		
		try {
			n = adminServiceBO.updateJobs(authentication, enhancedJobList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en updateEnhancedJobs:", t);
			throw new PfirmaException ("Error inesperado en updateEnhancedJobs");
		}
		
		return n;
		
	}
	
	public Integer deleteUsers (Authentication authentication, StringList identifierList) throws PfirmaException {
		log.debug("deleteUsers init");
		
		Integer n = null;
		
		try {
			n = adminServiceBO.deleteUsers(authentication, identifierList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteUsers:", t);
			throw new PfirmaException ("Error inesperado en deleteUsers");
		}
		
		return n;
		
	}
	
	public Integer deleteJobs (Authentication authentication, StringList identifierList) throws PfirmaException {
		log.debug("deleteJobs init");
		
		Integer n = null;
		
		try {
			n = adminServiceBO.deleteJobs(authentication, identifierList);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en deleteJobs:", t);
			throw new PfirmaException ("Error inesperado en deleteJobs");
		}
		
		return n;
		
	}
	
	public boolean autorizacionCreada (Authentication authentication,  
			XMLGregorianCalendar fstart, XMLGregorianCalendar fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws PfirmaException {
		log.debug("insertarAutorizaciones init");
		
		boolean insertada = false;
		
		try {

			insertada = adminServiceBO.estaCreadaAutorizacion(authentication, fstart, fend, userIdenAutoriza, userIdenAutorizado, descripcion, entidad);
		} catch (PfirmaException t) {
			log.error ("Error inesperado en insertarAutorizaciones:"+t.getMessage(), t);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+t.getMessage(), t);
		} catch (Throwable t) {
			log.error ("Error inesperado en insertarAutorizaciones:"+t.getMessage(), t);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+t.getMessage(), t);
		}
		
		return insertada;
		
	}
	

	public boolean insertarAutorizaciones (Authentication authentication,  
			XMLGregorianCalendar fstart, XMLGregorianCalendar fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws PfirmaException {
		log.debug("insertarAutorizaciones init");
		
		boolean insertada = false;
		
		try {

			insertada = adminServiceBO.insertarAutorizacion(authentication, fstart, fend, userIdenAutoriza, userIdenAutorizado, descripcion, entidad);
		} catch (PfirmaException t) {
			log.error ("Error inesperado en insertarAutorizaciones:"+t.getMessage(), t);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+t.getMessage(), t);
		} catch (Throwable t) {
			log.error ("Error inesperado en insertarAutorizaciones:"+t.getMessage(), t);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+t.getMessage(), t);
		}
		
		return insertada;
		
	}
	
	public boolean revocarAutorizacionActiva(Authentication authentication, String userIdenAutoriza, String userIdenAutorizado, XMLGregorianCalendar fstart, XMLGregorianCalendar fend, String entidad) throws PfirmaException{
		boolean revocado = false;
		try {

			revocado = adminServiceBO.revocarAutorizacionActiva(authentication, userIdenAutoriza, userIdenAutorizado, fstart, fend, entidad);
		} catch (PfirmaException t) {
			log.error ("Error inesperado en revocarAutorizacionActiva:"+t.getMessage(), t);
			throw new PfirmaException ("Error inesperado en revocarAutorizacionActiva"+t.getMessage(), t);
		} catch (Throwable t) {
			log.error ("Error inesperado en revocarAutorizacionActiva:"+t.getMessage(), t);
			throw new PfirmaException ("Error inesperado en revocarAutorizacionActiva"+t.getMessage(), t);
		}
		return revocado;
	}
	
	public boolean assignJobToUser(Authentication authentication,
			String jobIdentifier, String userIdentifier, XMLGregorianCalendar fstart, XMLGregorianCalendar fend) throws PfirmaException {
		
		log.debug("assignJobToUser init");
		
		Boolean r = false;
		
		try {
			r = adminServiceBO.assignJobToUser(authentication, jobIdentifier, userIdentifier, fstart, fend);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en assignJobToUser:", t);
			throw new PfirmaException ("Error inesperado en assignJobToUser");
		}
		
		return r;
	}

	public boolean separateJobToUser(Authentication authentication,
			String jobIdentifier, String userIdentifier) throws PfirmaException {
		log.debug("separateJobToUser init");
		Boolean r = false;
		try {
			r = adminServiceBO.separateJobToUser(authentication, jobIdentifier, userIdentifier);
		} catch (PfirmaException e) {
			throw e;
		} catch (Throwable t) {
			log.error ("Error inesperado en separateJobToUser:", t);
			throw new PfirmaException ("Error inesperado en separateJobToUser");
		}
		
		return r;
	}
	

}
