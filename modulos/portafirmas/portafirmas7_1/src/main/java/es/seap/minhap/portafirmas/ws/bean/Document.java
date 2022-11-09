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

package es.seap.minhap.portafirmas.ws.bean;

import java.io.Serializable;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a document contained in a request.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "document", propOrder = { "identifier", "name", "mime",
		"documentType", "content", "type", "uri", "sign" })
public class Document implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	protected String identifier;
	@XmlElement(required = true)
	protected String name;
	@XmlElement(required = true)
	protected String mime;
	@XmlElement(required = true)
	protected DocumentType documentType;
	@XmlElement(required = true)
	@XmlMimeType("application/octet-stream")
	protected DataHandler content;
	@XmlElement(required = true)
	protected String type;
	@XmlElement(required = true)
	protected String uri;
	protected boolean sign;

	/**
	 * Gets the value of the identifier property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the value of the identifier property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdentifier(String value) {
		this.identifier = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the mime property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMime() {
		return mime;
	}

	/**
	 * Sets the value of the mime property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMime(String value) {
		this.mime = value;
	}

	/**
	 * Gets the value of the documentType property.
	 * 
	 * @return possible object is {@link DocumentType }
	 * 
	 */
	public DocumentType getDocumentType() {
		return documentType;
	}

	/**
	 * Sets the value of the documentType property.
	 * 
	 * @param value
	 *            allowed object is {@link DocumentType }
	 * 
	 */
	public void setDocumentType(DocumentType value) {
		this.documentType = value;
	}

	/**
	 * Gets the value of the content property.
	 * 
	 * @return possible object is {@link DataHandler }
	 * 
	 */
	public DataHandler getContent() {
		return content;
	}

	/**
	 * Sets the value of the content property.
	 * 
	 * @param value
	 *            allowed object is {@link DataHandler }
	 * 
	 */
	public void setContent(DataHandler value) {
		this.content = value;
	}

	/**
	 * Gets the value of the sign property.
	 * 
	 * @return possible object is boolean.
	 * 
	 */
	public boolean isSign() {
		return sign;
	}

	/**
	 * Sets the value of the sign property.
	 * 
	 * @param value
	 *            allowed object is boolean.
	 * 
	 */
	public void setSign(boolean value) {
		this.sign = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String}.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param type
	 *            allowed object is {@link String}.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the value of the uri property.
	 * 
	 * @return possible object is {@link String}.
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Sets the value of the uri property.
	 * 
	 * @param uri
	 *            allowed object is {@link String}.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

}
