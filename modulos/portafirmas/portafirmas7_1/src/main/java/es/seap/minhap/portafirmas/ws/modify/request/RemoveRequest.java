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

/**
 * Java class for deleteRequest element declaration.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "authentication", "requestId", "removingMessage" })
@XmlRootElement(name = "removeRequest")
public class RemoveRequest {

	@XmlElement(required = true)
	protected String requestId;
	@XmlElement(required = true)
	protected Authentication authentication;
	@XmlElement(required = true)
	protected String removingMessage;

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
	 * @return the removingMessage
	 */
	public String getRemovingMessage() {
		return removingMessage;
	}

	/**
	 * @param removingMessage the removingMessage to set
	 */
	public void setRemovingMessage(String removingMessage) {
		this.removingMessage = removingMessage;
	}

}