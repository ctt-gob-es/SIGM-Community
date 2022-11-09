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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.SignerList;

/**
 * Java class for deleteSigners element declaration.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "authentication", "requestId", "signLineNumber", "signerList" })
@XmlRootElement(name = "deleteSigners")
public class DeleteSigners {

	@XmlElement(required = true)
	protected String requestId;
	@XmlElement(required = true)
	protected SignerList signerList;
	@XmlElement(required = true)
	protected Authentication authentication;	
	@XmlElement(required = true)
	protected Integer signLineNumber;

	/**
	 * Gets the value of the requestId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * Sets the value of the requestId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRequestId(String value) {
		this.requestId = value;
	}

	/**
	 * Gets the value of the signerList property.
	 * 
	 * @return possible object is {@link SignerList}
	 * 
	 */
	public SignerList getSignerList() {
		return signerList;
	}	
	
	/**
	 * Sets the value of the signerList property.
	 * 
	 * @param value
	 *            allowed object is {@link SignerList}
	 * 
	 */
	public void setSignerList(SignerList value) {
		this.signerList = value;
	}
	
	/**
	 * @return the authentication
	 */
	public Authentication getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	/**
	 * @return the signLineNumber
	 */
	public Integer getSignLineNumber() {
		return signLineNumber;
	}

	/**
	 * @param signLineNumber the signLineNumber to set
	 */
	public void setSignLineNumber(Integer signLineNumber) {
		this.signLineNumber = signLineNumber;
	}	
}
