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
 * Java class for insertSigners element declaration.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "authentication", "requestId", "signLine", "signerList", "signLineType" })
@XmlRootElement(name = "insertSigners")
public class InsertSigners {

	@XmlElement(required = true)
	protected Authentication authentication;
	@XmlElement(required = true)
	protected String requestId;
	@XmlElement(required = true)
	protected int signLine;
	@XmlElement(required = true)
	protected SignerList signerList;
	@XmlElement(required = true)
	protected String signLineType;

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
	 * Gets the value of the signLine property.
	 * 
	 * @return possible object is {@link Integer}
	 * 
	 */
	public int getSignLine() {
		return signLine;
	}

	/**
	 * Sets the value of the signLine property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer}
	 * 
	 */
	public void setSignLine(int value) {
		this.signLine = value;
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
	 * @return the signLineType
	 */
	public String getSignLineType() {
		return signLineType;
	}

	/**
	 * @param signLineType the signLineType to set
	 */
	public void setSignLineType(String signLineType) {
		this.signLineType = signLineType;
	}	

}
