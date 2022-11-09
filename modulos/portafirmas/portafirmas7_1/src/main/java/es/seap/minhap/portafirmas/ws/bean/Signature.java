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
 * Represents a signer.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "signature", propOrder = { "identifier", "sign", "signFormat", "content" })
public class Signature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement(required = true)
	protected String identifier;	
	@XmlElement(required = true)
	protected boolean sign;
	@XmlElement(required = true)
	protected String signFormat;
	@XmlElement(required = true)
	@XmlMimeType("application/octet-stream")
	protected DataHandler content;


	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	/**
	 * @return the sign
	 */
	public boolean isSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(boolean sign) {
		this.sign = sign;
	}
	/**
	 * @return the content
	 */
	public DataHandler getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(DataHandler content) {
		this.content = content;
	}
	/**
	 * @return the signFormat
	 */
	public String getSignFormat() {
		return signFormat;
	}
	/**
	 * @param signFormat the signFormat to set
	 */
	public void setSignFormat(String signFormat) {
		this.signFormat = signFormat;
	}

}
