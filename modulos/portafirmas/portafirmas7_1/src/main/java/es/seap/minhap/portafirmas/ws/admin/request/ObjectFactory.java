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

package es.seap.minhap.portafirmas.ws.admin.request;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the
 * es.seap.minhap.portafirmas.ws.admin.request package.
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
	 * es.seap.minhap.portafirmas.ws.admin.request
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link UpdateDocumentsTypeResponse }
	 * 
	 * @return {@link UpdateDocumentsTypeResponse} instance.
	 * 
	 */
	public UpdateDocumentsTypeResponse createUpdateDocumentsTypeResponse() {
		return new UpdateDocumentsTypeResponse();
	}

	/**
	 * Create an instance of {@link DeleteDocumentsType }
	 * 
	 * @return {@link DeleteDocumentsType} instance.
	 * 
	 */
	public DeleteDocumentsType createDeleteDocumentsType() {
		return new DeleteDocumentsType();
	}

	/**
	 * Create an instance of {@link DeleteDocumentsTypeResponse }
	 * 
	 * @return {@link DeleteDocumentsTypeResponse} instance.
	 */
	public DeleteDocumentsTypeResponse createDeleteDocumentsTypeResponse() {
		return new DeleteDocumentsTypeResponse();
	}

	/**
	 * Create an instance of {@link InsertDocumentsTypeResponse }
	 * 
	 * @return {@link InsertDocumentsTypeResponse} instance.
	 */
	public InsertDocumentsTypeResponse createInsertDocumentsTypeResponse() {
		return new InsertDocumentsTypeResponse();
	}

	/**
	 * Create an instance of {@link InsertDocumentsType }
	 * 
	 * @return {@link InsertDocumentsType} instance.
	 */
	public InsertDocumentsType createInsertDocumentsType() {
		return new InsertDocumentsType();
	}

	/**
	 * Create an instance of {@link UpdateDocumentsType }
	 * 
	 * @return {@link UpdateDocumentsType} instance.
	 */
	public UpdateDocumentsType createUpdateDocumentsType() {
		return new UpdateDocumentsType();
	}

}
