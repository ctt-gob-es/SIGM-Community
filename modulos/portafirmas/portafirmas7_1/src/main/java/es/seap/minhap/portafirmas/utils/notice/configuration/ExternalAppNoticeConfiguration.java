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

package es.seap.minhap.portafirmas.utils.notice.configuration;

import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.SignatureSerializable;

/**
 * Class used to store configuration data for email server and user who sends
 * email notifications
 * 
 * @author Guadaltel
 */
public class ExternalAppNoticeConfiguration implements NoticeConfiguration {

	private static final long serialVersionUID = 1L;

	private String wsdlLocation;
	private String userName;
	private String password;
	private Request request;
	private SignatureSerializable[] signatures;
	/**
	 * @return the wsdlLocation
	 */
	public String getWsdlLocation() {
		return wsdlLocation;
	}
	/**
	 * @param wsdlLocation the wsdlLocation to set
	 */
	public void setWsdlLocation(String wsdlLocation) {
		this.wsdlLocation = wsdlLocation;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the request
	 */
	public Request getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(Request request) {
		this.request = request;
	}
	/**
	 * @return the signatures
	 */
	public SignatureSerializable[] getSignatures() {
		return signatures;
	}
	/**
	 * @param signatures the signatures to set
	 */
	public void setSignatures(SignatureSerializable[] signatures) {
		this.signatures = signatures;
	}


}
