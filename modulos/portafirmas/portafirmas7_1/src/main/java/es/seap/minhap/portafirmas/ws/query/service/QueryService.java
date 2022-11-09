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

package es.seap.minhap.portafirmas.ws.query.service;

import javax.activation.DataHandler;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

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
 * Java class for query services definition.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@WebService(targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:v2.0", name = "QueryService")
public interface QueryService {

	/**
	 * Obtains binary content from a document.
	 * 
	 * @param documentId
	 *            Document id.
	 * @return {@link DataHandler} object with document binary content.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "downloadDocumentResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.DownloadDocumentResponse")
	@RequestWrapper(localName = "downloadDocument", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.DownloadDocument")
	@WebResult(name = "documentBinary", targetNamespace = "")
	@WebMethod	
	public DataHandler downloadDocument(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "documentId", targetNamespace = "") String documentId			
			) throws PfirmaException;

	/**
	 * Obtains a user list containing users who matches with received filter in
	 * query parameter.
	 * 
	 * @param query
	 *            Filter string which will be applied to name, first surname,
	 *            second surname and identifier. If no filter is passed, will
	 *            return all users existing in application.
	 * @return {@link UserList} containing users who matches with filter
	 *         received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryUsersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryUsersResponse")
	@RequestWrapper(localName = "queryUsers", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryUsers")
	@WebResult(name = "userList", targetNamespace = "")
	@WebMethod
	public es.seap.minhap.portafirmas.ws.bean.UserList queryUsers(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "query", targetNamespace = "")	String query
			) throws PfirmaException;
	
	

	/**
	 * Obtains binary content from a sign.
	 * 
	 * @param documentId
	 *            Document id.
	 * @return {@link DataHandler} object with sign binary content.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "downloadSignResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.DownloadSignResponse")
	@RequestWrapper(localName = "downloadSign", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.DownloadSign")
	@WebResult(name = "signature", targetNamespace = "")
	@WebMethod
	public Signature downloadSign(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "documentId", targetNamespace = "") String documentId) throws PfirmaException;

	/**
	 * Obtains a document type list containing document types which matches with
	 * received filter in query parameter.
	 * 
	 * @param query
	 *            Filter string which will be applied to document type code and
	 *            associated (if exist) application code.
	 * @return {@link DocumentTypeList} containing document types which matches
	 *         with filter received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryDocumentTypesResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryDocumentTypesResponse")
	@RequestWrapper(localName = "queryDocumentTypes", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryDocumentTypes")
	@WebResult(name = "documentTypeList", targetNamespace = "")
	@WebMethod
	public es.seap.minhap.portafirmas.ws.bean.DocumentTypeList queryDocumentTypes(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "query", targetNamespace = "") String query) throws PfirmaException;

	/**
	 * Obtains a job list containing jobs which matches with received filter in
	 * query parameter.
	 * 
	 * @param query
	 *            Filter string which will be applied to identifier. If no
	 *            filter is passed, will return all users existing in
	 *            application.
	 * @return {@link JobList} containing jobs whichF matches with filter
	 *         received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryJobsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryJobsResponse")
	@RequestWrapper(localName = "queryJobs", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryJobs")
	@WebResult(name = "jobList", targetNamespace = "")
	@WebMethod
	public es.seap.minhap.portafirmas.ws.bean.JobList queryJobs(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "query", targetNamespace = "") String query) throws PfirmaException;

	/**
	 * Obtains a request from request id received parameter.
	 * 
	 * @param requestId
	 *            Request id;
	 * @return {@link Request} object with request data.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryRequestResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryRequestResponse")
	@RequestWrapper(localName = "queryRequest", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryRequest")
	@WebResult(name = "request", targetNamespace = "")
	@WebMethod
	public es.seap.minhap.portafirmas.ws.bean.Request queryRequest(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "requestId", targetNamespace = "") String requestId) throws PfirmaException;

	/**
	 * Obtains a state list containing states which matches with received filter
	 * in query parameter.
	 * 
	 * @param query
	 *            Filter string which will be applied to state code. If no
	 *            filter is passed, will return all states existing in
	 *            application.
	 * @return {@link StateList} containing states which matches with filter.
	 *         received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryStatesResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryStatesResponse")
	@RequestWrapper(localName = "queryStates", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryStates")
	@WebResult(name = "stateList", targetNamespace = "")
	@WebMethod
	public es.seap.minhap.portafirmas.ws.bean.StateList queryStates(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "query", targetNamespace = "") String query) throws PfirmaException;
	

	/**
	 * Obtains a list of importance levels
	 * in query parameter.
	 * 
	 * @param query
	 *            Filter string which will be applied to importance level code and
	 *            associated (if exist) application code.
	 * @return {@link ImportanceLevelList} containing document types which matches
	 *         with filter received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryImportanceLevelsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryImportanceLevelsResponse")
	@RequestWrapper(localName = "queryImportanceLevels", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryImportanceLevels")
	@WebResult(name = "importanceLevelList", targetNamespace = "")
	@WebMethod
	public ImportanceLevelList queryImportanceLevels(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "query", targetNamespace = "") String query) throws PfirmaException;

	

	/**
	 * Obtiene el CVS de una firma
	 * @param authentication Par&aacute;metro de autenticaci&oacute;n.
	 * @param firma Firma de la que se obtiene el CVS.
	 * @return CVS de la firma.
	 * @throws PfirmaException Error del Portafirmas.
	 */
	@ResponseWrapper(localName = "getCVSResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.GetCVSResponse")
	@RequestWrapper(localName = "getCVS", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.GetCSV")
	@WebResult(name = "cvs", targetNamespace = "")
	@WebMethod
	public java.lang.String getCVS(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "firma", targetNamespace = "") Signature firma) throws PfirmaException;
	
	
	/**
	 * Obtains a list of users
	 * in query parameter.
	 * 
	 * @param queryUser
	 *            Filter string which will be applied to user identifier or user name	 *
	 * @param queryUser
	 *            Filter string which will be applied to code of user's seat or name of user's seat	 *            
	 * @return {@link EnhancedUserList} containing user which matches
	 *         with filter received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryEnhancedUsersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedUsersResponse")
	@RequestWrapper(localName = "queryEnhancedUsers", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedUsers")
	@WebResult(name = "enhancedUserList", targetNamespace = "")
	@WebMethod
	public EnhancedUserList queryEnhancedUsers(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "queryUser", targetNamespace = "") String queryUser,
			@WebParam(name = "querySeat", targetNamespace = "") String querySeat
			) throws PfirmaException;

	
	/**
	 * Obtains a list of jobs
	 * in query parameter.
	 * 
	 * @param queryUser
	 *            Filter string which will be applied to user identifier or user name	 *
	 * @param queryUser
	 *            Filter string which will be applied to code of user's seat or name of user's seat	 *            
	 * @return {@link EnhancedUserList} containing user which matches
	 *         with filter received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "queryEnhancedJobsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedJobsResponse")
	@RequestWrapper(localName = "queryEnhancedJobs", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedJobs")
	@WebResult(name = "enhancedJobList", targetNamespace = "")
	@WebMethod
	public EnhancedJobList queryEnhancedJobs(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "queryJob", targetNamespace = "") String queryUser,
			@WebParam(name = "querySeat", targetNamespace = "") String querySeat
			) throws PfirmaException;
	
	
	/**
	 * Obtains a list of seats
	 * in query parameter.
	 * 
	 * @param query
	 *         Filter string which will be applied to seat code or seat name
	 * @return {@link SeatList} containing user which matches
	 *         with filter received.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "querySeatsResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QuerySeatsResponse")
	@RequestWrapper(localName = "querySeats", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QuerySeats")
	@WebResult(name = "seatList", targetNamespace = "")
	@WebMethod
	public SeatList querySeats(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "query", targetNamespace = "") String query			
			) throws PfirmaException;
	
	
	/**
	 * Obtiene el usuario asociado a un cargo con identificador pasado como parámetro
	 * Si el cargo no existe dará error
	 * Si no está asociado a ningún usuario devuelve null 
	 * @param authentication
	 * @param jobIdentifier
	 * @return
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "queryEnhancedUserJobAssociatedToJobResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedUserJobAssociatedToJobResponse")
	@RequestWrapper(localName = "queryEnhancedUserJobAssociatedToJob", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedUserJobAssociatedToJob")
	@WebResult(name = "enhancedUserJobAssociatedList", targetNamespace = "")
	@WebMethod	
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToJob (
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication, 
			@WebParam(name = "jobIdentifier", targetNamespace = "") String jobIdentifier
			) throws PfirmaException;
	
	/**
	 * Obtiene el cargo asociado a un usuario con identificador pasado como parámetro
	 * Si el usuario no existe dará error
	 * Si no está asociado a ningún cargo devuelve null 
	 * @param authentication
	 * @param userIdentifier
	 * @return
	 * @throws PfirmaException
	 */
	@ResponseWrapper(localName = "queryEnhancedUserJobAssociatedToUserResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedUserJobAssociatedToUserResponse")
	@RequestWrapper(localName = "queryEnhancedUserJobAssociatedToUser", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.QueryEnhancedUserJobAssociatedToUser")
	@WebResult(name = "enhancedUserJobAssociatedList", targetNamespace = "")
	@WebMethod	
	public EnhancedUserJobAssociatedList queryEnhancedUserJobAssociatedToUser (
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication, 
			@WebParam(name = "userIdentifier", targetNamespace = "") String userIdentifier
			) throws PfirmaException;
	
	
	/**
	 * Obtiene el justificante de firma de un documento a partir de su CSV
	 * @param authentication Autenticación del usuario.
	 * @param documentId Identificador del documento.
	 * @return reportDocument Justificante de la firma
	 * @throws PfirmaException Error del Portafirmas.
	 */
	@ResponseWrapper(localName = "queryCSVyJustificanteResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.juntadeandalucia.cice.pfirma.ws.query.request.QueryCSVyJustificanteResponse")
	@RequestWrapper(localName = "queryCSVyJustificante", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.juntadeandalucia.cice.pfirma.ws.query.request.QueryCSVyJustificante")
	@WebResult(name = "csvJustificante", targetNamespace = "")
	@WebMethod
	public CsvJustificante queryCSVyJustificante(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "documentId", targetNamespace = "") String documentId)throws PfirmaException;
	
	
	/**
	 * Obtiene el informe de firma de un documento a partir de su CSV
	 * @param authentication Parámetro de autenticación.
	 * @param csv CSV del documento.
	 * @return reportDocument Informe del documento
	 * @throws PfirmaException Error del Portafirmas.
	 */
//	@ResponseWrapper(localName = "getReportFromCSVResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.GetReportFromCSVResponse")
//	@RequestWrapper(localName = "getReportFromCSV", targetNamespace = "urn:juntadeandalucia:cice:pfirma:query:request:v2.0", className = "es.seap.minhap.portafirmas.ws.query.request.GetReportFromCSV")
//	@WebResult(name = "reportDocument", targetNamespace = "")
//	@WebMethod
//	public DataHandler getReportFromCSV(
//			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
//			@WebParam(name = "csv", targetNamespace = "") String csv) throws PfirmaException;
	
	
}
