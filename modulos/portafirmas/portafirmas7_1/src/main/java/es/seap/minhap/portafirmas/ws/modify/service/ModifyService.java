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

package es.seap.minhap.portafirmas.ws.modify.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.Document;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.SignerList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;

/**
 * Java class for modify services definition.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@WebService(targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:v2.0", name = "ModifyService")
public interface ModifyService {

	/**
	 * Sends a request.
	 * 
	 * @param requestId
	 *            Request id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "sendRequestResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.SendRequestResponse")
	@RequestWrapper(localName = "sendRequest", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.SendRequest")
	@WebMethod
	public void sendRequest(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "requestId", targetNamespace = "")	Holder<String> requestId)
			throws PfirmaException;

	/**
	 * Inserts a document into a request.
	 * 
	 * @param requestId
	 *            Request id.
	 * @param document
	 *            {@link Document} object with document data.
	 * @return Document id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "insertDocumentResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.InsertDocumentResponse")
	@RequestWrapper(localName = "insertDocument", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.InsertDocument")
	@WebResult(name = "documentId", targetNamespace = "")
	@WebMethod
	public java.lang.String insertDocument(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "requestId", targetNamespace = "") String requestId,
			@WebParam(name = "document", targetNamespace = "") Document document)
			throws PfirmaException;

	/**
	 * Update request data. Signers are not changed with this method, to add,
	 * update or delete signer use methods defined for each of these operations.
	 * 
	 * @param request
	 *            {@link Request} object with request data.
	 * @return Request id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "updateRequestResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.UpdateRequestResponse")
	@RequestWrapper(localName = "updateRequest", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.UpdateRequest")
	@WebResult(name = "requestId", targetNamespace = "")
	@WebMethod
	public java.lang.String updateRequest(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "request", targetNamespace = "") Request request)
			throws PfirmaException;

	/**
	 * Creates a new request.
	 * 
	 * @param request
	 *            {@link Request} object with request data.
	 * @return Request id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "createRequestResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.CreateRequestResponse")
	@RequestWrapper(localName = "createRequest", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.CreateRequest")
	@WebResult(name = "requestId", targetNamespace = "")
	@WebMethod
	public java.lang.String createRequest(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(name = "request", targetNamespace = "") Request request)
			throws PfirmaException;

	/**
	 * Deletes a document from a request.
	 * 
	 * @param documentId
	 *            Document id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteDocumentResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.DeleteDocumentResponse")
	@RequestWrapper(localName = "deleteDocument", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.DeleteDocument")
	@WebMethod
	public void deleteDocument(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "documentId", targetNamespace = "")
			javax.xml.ws.Holder<java.lang.String> documentId)
			throws PfirmaException;

	/**
	 * Delete a request. Request can be deleted only if it are not sent.
	 * 
	 * @param requestId
	 *            Request id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteRequestResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.DeleteRequestResponse")
	@RequestWrapper(localName = "deleteRequest", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.DeleteRequest")
	@WebMethod
	public void deleteRequest(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "requestId", targetNamespace = "")
			javax.xml.ws.Holder<java.lang.String> requestId)
			throws PfirmaException;
	
	/**
	 * [Ticket1269#Teresa] Anular circuito de firmas
	 * Delete a request. Request can be deleted if it are sent.
	 * 
	 * @param requestId
	 *            Request id.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteRequestSendResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.deleteRequestSendResponse")
	@RequestWrapper(localName = "deleteRequestSend", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.deleteRequestSend")
	@WebMethod
	public void deleteRequestSend(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "requestId", targetNamespace = "")
			javax.xml.ws.Holder<java.lang.String> requestId)
			throws PfirmaException;


	/**
	 * Insert signers into a request.
	 * 
	 * @param requestId
	 *            Request id.
	 * @param signLine
	 *            Sign line number where signer(s) will be inserted.
	 * @param signerList
	 *            {@link Signer} list containing signer(s) which will be
	 *            inserted.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "insertSignersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.InsertSignersResponse")
	@RequestWrapper(localName = "insertSigners", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.InsertSigners")
	@WebMethod
	public void insertSigners(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "requestId", targetNamespace = "") Holder<String> requestId,
			@WebParam(name = "signLine", targetNamespace = "") int signLine, 
			@WebParam(name = "signerList", targetNamespace = "") SignerList signerList,
			@WebParam(name = "signLineType", targetNamespace = "") String signLineType)
			throws PfirmaException;

	/**
	 * Delete signers from a request.
	 * 
	 * @param requestId
	 *            Request id.
	 * @param signerList
	 *            {@link Signer} list containing signer(s) which will be
	 *            deleted.
	 * @throws PfirmaException
	 *             If an error occurs during process.
	 */
	@ResponseWrapper(localName = "deleteSignersResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.DeleteSignersResponse")
	@RequestWrapper(localName = "deleteSigners", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.DeleteSigners")
	@WebMethod
	public void deleteSigners(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "requestId", targetNamespace = "") Holder<String> requestId,
			@WebParam(name = "signLineNumber", targetNamespace = "") Integer signLineNumber,
			@WebParam(name = "signerList", targetNamespace = "") SignerList signerList)
			throws PfirmaException;

	/**
	 * Remove a request even if it's sent only by the user that created it
	 * @param authentication Id and password
	 * @param requestId Request identifier
	 * @throws PfirmaException If an error occurs during process.
	 */
	@ResponseWrapper(localName = "removeRequestResponse", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.RemoveRequestResponse")
	@RequestWrapper(localName = "removeRequest", targetNamespace = "urn:juntadeandalucia:cice:pfirma:modify:request:v2.0", className = "es.seap.minhap.portafirmas.ws.modify.request.RemoveRequest")
	@WebMethod
	public void removeRequest(
			@WebParam(name = "authentication", targetNamespace = "") Authentication authentication,
			@WebParam(mode = WebParam.Mode.INOUT, name = "requestId", targetNamespace = "")
			javax.xml.ws.Holder<java.lang.String> requestId,
			@WebParam(name = "removingMessage", targetNamespace = "") String removingMessage)
			throws PfirmaException;
}
