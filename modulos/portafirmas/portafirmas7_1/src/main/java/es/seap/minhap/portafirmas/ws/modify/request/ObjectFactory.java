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

package es.seap.minhap.portafirmas.ws.modify.request;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * juntadeandalucia.cice.pfirma.modify.request.v2 package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package:
	 * juntadeandalucia.cice.pfirma.modify.request.v2.
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link DeleteRequest }.
	 * 
	 * @return {@link DeleteRequest} instance.
	 * 
	 */
	public DeleteRequest createDeleteRequest() {
		return new DeleteRequest();
	}

	/**
	 * Create an instance of {@link DeleteRequestResponse }.
	 * 
	 * @return {@link DeleteRequestResponse} instance;
	 * 
	 */
	public DeleteRequestResponse createDeleteRequestResponse() {
		return new DeleteRequestResponse();
	}

	/**
	 * Create an instance of {@link UpdateRequestResponse }.
	 * 
	 * @return {@link UpdateRequestResponse} instance.
	 * 
	 */
	public UpdateRequestResponse createUpdateRequestResponse() {
		return new UpdateRequestResponse();
	}

	/**
	 * Create an instance of {@link SendRequestResponse }.
	 * 
	 * @return {@link SendRequestResponse} instance.
	 * 
	 */
	public SendRequestResponse createSendRequestResponse() {
		return new SendRequestResponse();
	}

	/**
	 * Create an instance of {@link CreateRequest }.
	 * 
	 * @return {@link CreateRequest} instance.
	 * 
	 */
	public CreateRequest createCreateRequest() {
		return new CreateRequest();
	}

	/**
	 * Create an instance of {@link InsertDocument }.
	 * 
	 * @return {@link InsertDocument} instance.
	 * 
	 */
	public InsertDocument createInsertDocument() {
		return new InsertDocument();
	}

	/**
	 * Create an instance of {@link InsertDocumentResponse }.
	 * 
	 * @return {@link InsertDocumentResponse} instance.
	 * 
	 */
	public InsertDocumentResponse createInsertDocumentResponse() {
		return new InsertDocumentResponse();
	}

	/**
	 * Create an instance of {@link CreateRequestResponse }.
	 * 
	 * @return {@link CreateRequestResponse} instance.
	 * 
	 */
	public CreateRequestResponse createCreateRequestResponse() {
		return new CreateRequestResponse();
	}

	/**
	 * Create an instance of {@link UpdateRequest }.
	 * 
	 * @return {@link UpdateRequest} instance.
	 * 
	 */
	public UpdateRequest createUpdateRequest() {
		return new UpdateRequest();
	}

	/**
	 * Create an instance of {@link DeleteDocument }.
	 * 
	 * @return {@link DeleteDocument} instance.
	 * 
	 */
	public DeleteDocument createDeleteDocument() {
		return new DeleteDocument();
	}

	/**
	 * Create an instance of {@link DeleteDocumentResponse }.
	 * 
	 * @return {@link DeleteDocumentResponse} instance.
	 * 
	 */
	public DeleteDocumentResponse createDeleteDocumentResponse() {
		return new DeleteDocumentResponse();
	}

	/**
	 * Create an instance of {@link SendRequest }.
	 * 
	 * @return {@link SendRequest} instance.
	 * 
	 */
	public SendRequest createSendRequest() {
		return new SendRequest();
	}

}
