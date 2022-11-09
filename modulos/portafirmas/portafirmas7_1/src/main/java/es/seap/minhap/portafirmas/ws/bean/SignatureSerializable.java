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

/**
 * Represents a signature. Serializable version
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
public class SignatureSerializable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String identifier;	
	protected boolean sign;
	protected String signFormat;
	protected byte[] contentBytes;
	/**
	 * @return the contentBytes
	 */
	public byte[] getContentBytes() {
		return contentBytes;
	}
	/**
	 * @param contentBytes the contentBytes to set
	 */
	public void setContentBytes(byte[] contentBytes) {
		this.contentBytes = contentBytes;
	}
	protected String mimeType;
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
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}
	/**
	 * @param mimeType the mimeType to set
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
