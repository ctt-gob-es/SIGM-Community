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

package es.seap.minhap.portafirmas.ws.mobile.bean;

import java.io.Serializable;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileDocument", propOrder = { "identifier", "name", "mime",
												"signatureType", "signAlgorithm", "operationType",
												"signatureParameters", "data", "size" })
public class MobileDocument implements Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	protected String identifier;
	@XmlElement(required = true)
	protected String name;
	@XmlElement(required = true)
	protected String mime;
	@XmlElement(required = false)
	protected String signatureType;
	@XmlElement(required = false)
	protected String signAlgorithm;
	@XmlElement(required = true)
	protected String operationType;
	@XmlElement(required = false)
	protected String signatureParameters;
	@XmlMimeType("application/octet-stream")
	protected DataHandler data;
	@XmlElement(required = false)
	protected int size;


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

	public String getSignatureType() {
		return signatureType;
	}

	public void setSignatureType(String signatureType) {
		this.signatureType = signatureType;
	}

	public String getSignAlgorithm() {
		return signAlgorithm;
	}

	public void setSignAlgorithm(String signAlgorithm) {
		this.signAlgorithm = signAlgorithm;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getSignatureParameters() {
		return signatureParameters;
	}

	public void setSignatureParameters(String signatureParameters) {
		this.signatureParameters = signatureParameters;
	}

	public DataHandler getData() {
		return data;
	}

	public void setData(DataHandler data) {
		this.data = data;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
