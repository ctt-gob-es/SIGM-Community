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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.DocumentType;
import es.seap.minhap.portafirmas.ws.bean.DocumentTypeList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJobList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserList;
import es.seap.minhap.portafirmas.ws.bean.StringList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

/**
 * Java class for administration services definition.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@WebService(targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:v2.0", name = "AdminService")
public interface AdminService {

	/**
	 * Adds new document types to application.
	 * 
	 * @param applicationId
	 *            Application identifier.
	 * @param documentTypeList
	 *            List containing {@link DocumentType} list which will be
	 *            added.
	 * @return Application identifier.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "insertDocumentsTypeResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.InsertDocumentsTypeResponse")
	@RequestWrapper(localName = "insertDocumentsType", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.InsertDocumentsType")
	@WebResult(name = "applicationId", targetNamespace = "")
	@WebMethod
	public java.lang.String insertDocumentsType(
			@WebParam(name = "applicationId", targetNamespace = "")
			String applicationId,
			@WebParam(name = "documentTypeList", targetNamespace = "")
			DocumentTypeList documentTypeList) throws PfirmaException;

	/**
	 * Update document types in application.
	 * 
	 * @param applicationId
	 *            Application identifier.
	 * @param documentTypeList
	 *            List containing {@link DocumentType} list which will be
	 *            updated.
	 * @return Application identifier.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "updateDocumentsTypeResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.UpdateDocumentsTypeResponse")
	@RequestWrapper(localName = "updateDocumentsType", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.UpdateDocumentsType")
	@WebResult(name = "applicationId", targetNamespace = "")
	@WebMethod
	public java.lang.String updateDocumentsType(
			@WebParam(name = "applicationId", targetNamespace = "")
			String applicationId,
			@WebParam(name = "documentTypeList", targetNamespace = "")
			DocumentTypeList documentTypeList) throws PfirmaException;

	/**
	 * Delete document types from application.
	 * 
	 * @param applicationId
	 *            Application identifier.
	 * @param documentTypeList
	 *            List containing {@link DocumentType} list which will be
	 *            deleted.
	 * @return Application identifier.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteDocumentsTypeResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.DeleteDocumentsTypeResponse")
	@RequestWrapper(localName = "deleteDocumentsType", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.DeleteDocumentsType")
	@WebResult(name = "applicationId", targetNamespace = "")
	@WebMethod
	public java.lang.String deleteDocumentsType(
			@WebParam(name = "applicationId", targetNamespace = "")
			String applicationId,
			@WebParam(name = "documentTypeList", targetNamespace = "")
			DocumentTypeList documentTypeList) throws PfirmaException;
	
	
	
	/**
	 * Da de alta a usuarios en la aplicación.
	 * 
	 * Si alguno de los usuarios existía como vigente, dará error. Si existía pero no estaba vigente se modificarán sus datos por los datos de entrada.
	 * 
	 * @param enhancedUserList
	 *         Lista de usuarios para los que se quiere dar de alta.
	 * @return número de usuarios que se han dado de alta.	 *         
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "insertEnhancedUsersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.InsertEnhancedUsersResponse")
	@RequestWrapper(localName = "insertEnhancedUsers", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.InsertEnhancedUsers")
	@WebResult(name = "enhancedUsersInserted", targetNamespace = "")
	@WebMethod
	public Integer insertEnhancedUsers(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "enhancedUserList", targetNamespace = "") EnhancedUserList enhancedUserList			
			) throws PfirmaException;
	
	
	
	/**
	 * Da de alta a cargos en la aplicación.
	 * 
	 * Si alguno de los cargos existía como vigente, dará error. Si existía pero no estaba vigente se modificarán sus datos por los datos de entrada.
	 * 
	 * @param enhancedJobList
	 *         Lista de cargos para los que se quiere dar de alta.
	 * @return número de cargos que se han dado de alta.	 *         
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "insertEnhancedJobsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.InsertEnhancedJobsResponse")
	@RequestWrapper(localName = "insertEnhancedJobs", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.InsertEnhancedJobs")
	@WebResult(name = "enhancedJobsInserted", targetNamespace = "")
	@WebMethod
	public Integer insertEnhancedJobs (@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "enhancedJobList", targetNamespace = "") EnhancedJobList enhancedJobList			
			) throws PfirmaException;
	
	/**
	 * Modifica usuarios en la aplicación
	 * 
	 * Si el usuario no existe o no está vigente, dará error
	 * 
	 * @param enhancedUserList
	 *         Lista de usuarios que se quieren modificar.
	 * @return número de usuarios que se han modificado.	 *         
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "updateEnhancedUsersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.updateEnhancedUsersResponse")
	@RequestWrapper(localName = "updateEnhancedUsers", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.updateEnhancedUsers")
	@WebResult(name = "enhancedUsersUpdated", targetNamespace = "")
	@WebMethod
	public Integer updateEnhancedUsers(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "enhancedUserList", targetNamespace = "") EnhancedUserList enhancedUserList			
			) throws PfirmaException;
	
	/**
	 * Modifica un cargo en la aplicación
	 * 
	 * Si el cargo no existe o no está vigente, dará error
	 * 
	 * @param enhancedUserList
	 *         Lista de usuarios que se quieren modificar.
	 * @return número de usuarios que se han modificado.	 *         
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "updateEnhancedJobsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.updateEnhancedJobsResponse")
	@RequestWrapper(localName = "updateEnhancedJobs", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.updateEnhancedJobs")
	@WebResult(name = "enhancedJobsUpdated", targetNamespace = "")
	@WebMethod
	public Integer updateEnhancedJobs(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "enhancedJobList") EnhancedJobList enhancedJobList			
			) throws PfirmaException;
	
	/**
	 * Elimina usuarios en la aplicación
	 * 
	 * Si el usuario no existe, dará error
	 * 
	 * @param identifierList
	 *         Lista de identificadores de usuarios que se quieren modificar.
	 * @return número de usuarios que se han borrado.	 *         
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteUsersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.deleteUsersResponse")
	@RequestWrapper(localName = "deleteUsers", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.deleteUsers")
	@WebResult(name = "usersDeleted", targetNamespace = "")
	@WebMethod
	public Integer deleteUsers(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "userIdentifierList", targetNamespace = "") StringList identifierList			
			) throws PfirmaException;
	
	
	/**
	 * Elimina cargos en la aplicación
	 * 
	 * Si el cargo no existe, dará error
	 * 
	 * @param identifierList
	 *         Lista de identificadores de cargos que se quieren modificar.
	 * @return número de cargos que se han borrado.	 *         
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteJobsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.deleteJobsResponse")
	@RequestWrapper(localName = "deleteJobs", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.deleteJobs")
	@WebResult(name = "jobsDeleted", targetNamespace = "")
	@WebMethod
	public Integer deleteJobs(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "jobIdentifierList") StringList identifierList			
			) throws PfirmaException;
	
	
	/**
	 * Asigna un cargo a un usuario.
	 * Si el usuario o el cargo no existen o no están vigentes, dará error.
	 * Si el usuario ya tiene otro cargo asociado dará error.
	 * Si el cargo está asociado a otro usuario dará error.
	 * @param authentication
	 * @param jobIdentifier
	 * @param userIdentifier
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "assignJobToUserResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.assignJobToUserResponse")
	@RequestWrapper(localName = "assignJobToUser", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.assignJobToUser")
	@WebResult(name = "assigned", targetNamespace = "")
	@WebMethod	
	public boolean assignJobToUser (
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "jobIdentifier") String jobIdentifier,
			@WebParam(name = "userIdentifier") String userIdentifier,
			@WebParam(name = "fstart") XMLGregorianCalendar fstart,
			@WebParam(name = "fend") XMLGregorianCalendar fend
			) throws PfirmaException;
	
	
	
	/**
	 * Comprueba si esta creada una sustitución de firma.
	 * Si el usuario o el cargo no existen o no están vigentes, dará error.
	 * Si el usuario ya tiene otra sustitución dará error.
	 * @param authentication
	 * @param fstart
	 * @param fend
	 * @param userIdenAutoriza
	 * @param userIdenAutorizado
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "autorizacionCreadaResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.autorizacionCreadaResponse")
	@RequestWrapper(localName = "autorizacionCreada", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.autorizacionCreada")
	@WebResult(name = "insertada", targetNamespace = "")
	@WebMethod	
	public boolean autorizacionCreada (
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,  
			@WebParam(name = "fstart") XMLGregorianCalendar fstart, 
			@WebParam(name = "fend") XMLGregorianCalendar fend, 
			@WebParam(name = "userIdenAutoriza") String userIdenAutoriza, 
			@WebParam(name = "userIdenAutorizado") String userIdenAutorizado,
			@WebParam(name = "descripcion") String descripcion,
			@WebParam(name = "entidad") String entidad
			) throws PfirmaException;
	
	/**
	 *Crea una sustitución de firma.
	 * Si el usuario o el cargo no existen o no están vigentes, dará error.
	 * Si el usuario ya tiene otra sustitución dará error.
	 * @param authentication
	 * @param fstart
	 * @param fend
	 * @param userIdenAutoriza
	 * @param userIdenAutorizado
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "insertarAutorizacionesResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.insertarAutorizacionesResponse")
	@RequestWrapper(localName = "insertarAutorizaciones", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.insertarAutorizaciones")
	@WebResult(name = "insertada", targetNamespace = "")
	@WebMethod	
	public boolean insertarAutorizaciones (
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,  
			@WebParam(name = "fstart") XMLGregorianCalendar fstart, 
			@WebParam(name = "fend") XMLGregorianCalendar fend, 
			@WebParam(name = "userIdenAutoriza") String userIdenAutoriza, 
			@WebParam(name = "userIdenAutorizado") String userIdenAutorizado,
			@WebParam(name = "descripcion") String descripcion,
			@WebParam(name = "entidad") String entidad
			) throws PfirmaException;
	
	/**
	 * revocar una autorización
	 * Si el usuario o el cargo no existen o no están vigentes, dará error.
	 * Si el usuario ya tiene otra sustitución dará error.
	 * @param authentication
	 * @param userIdenAutoriza
	 * @param userIdenAutorizado
	 * @param fstart
	 * @param fend
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "revocarAutorizacionActivaResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.revocarAutorizacionActivaResponse")
	@RequestWrapper(localName = "revocarAutorizacionActiva", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.revocarAutorizacionActiva")
	@WebResult(name = "revocado", targetNamespace = "")
	@WebMethod	
	public boolean revocarAutorizacionActiva(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "userIdenAutoriza") String userIdenAutoriza,
			@WebParam(name = "userIdenAutorizado") String userIdenAutorizado,
			@WebParam(name = "fstart") XMLGregorianCalendar fstart, 
			@WebParam(name = "fend") XMLGregorianCalendar fend,
			@WebParam(name = "entidad") String entidad
			) throws PfirmaException;
	
	/**
	 * Desasigna un cargo de un usuario.
	 * Si el usuario o el cargo no existen, dará error.	 * 
	 * @param authentication
	 * @param jobIdentifier
	 * @param userIdentifier
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "separateJobToUserResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.separateJobToUserResponse")
	@RequestWrapper(localName = "separateJobToUser", targetNamespace = "urn:juntadeandalucia:cice:pfirma:admin:request:v2.0", className = "es.seap.minhap.portafirmas.ws.admin.request.separateJobToUser")
	@WebResult(name = "separated", targetNamespace = "")
	@WebMethod	
	public boolean separateJobToUser (
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "jobIdentifier") String jobIdentifier,
			@WebParam(name = "userIdentifier") String userIdentifier
			) throws PfirmaException;
	
	
}
