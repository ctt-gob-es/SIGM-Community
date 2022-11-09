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

import java.io.Serializable;

/**
 * Class used to store configuration data for email server and user who sends
 * email notifications
 * 
 * @author Guadaltel
 */
public class EmailNoticeConfiguration implements NoticeConfiguration, Serializable {

	private static final long serialVersionUID = 1L;

	private String mailUser;
	private String mailPassword;
	private String mailRemitter;
	private String smtpHost;
	private String smtpPort;
	private String fromName;
	private String authenticate;

	/**
	 * Returns user mail address
	 * 
	 * @return user mail address
	 */
	public String getMailUser() {
		return mailUser;
	}

	/**
	 * Sets user mail address
	 * 
	 * @param mailUser
	 *            user mail address
	 */
	public void setMailUser(String mailUser) {
		this.mailUser = mailUser;
	}

	/**
	 * Returns password for mail authentication
	 * 
	 * @return password for mail authentication
	 */
	public String getMailPassword() {
		return mailPassword;
	}

	/**
	 * Sets password for mail authentication
	 * 
	 * @param mailPassword
	 *            password for mail authentication
	 */
	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	/**
	 * Returns email address for remitter
	 * 
	 * @return email address for remitter
	 */
	public String getMailRemitter() {
		return mailRemitter;
	}

	/**
	 * Sets email address for remitter
	 * 
	 * @param mailRemitter
	 *            email address for remitter
	 */
	public void setMailRemitter(String mailRemitter) {
		this.mailRemitter = mailRemitter;
	}

	/**
	 * Returns host of smtp server
	 * 
	 * @return host of smtp server
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * Sets host of smtp server
	 * 
	 * @param smtpHost
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	/**
	 * Returns port of smtp server
	 * 
	 * @return port of smtp server
	 */
	public String getSmtpPort() {
		return smtpPort;
	}

	/**
	 * Sets port of smtp server
	 * 
	 * @param smtpPort
	 *            port of smtp server
	 */
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * Returns name used for remitter
	 * 
	 * @return name used for remitter
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * Sets name used for remitter
	 * 
	 * @param fromName
	 *            name used for remitter
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * Returns authentication mode
	 * 
	 * @return authentication mode
	 */
	public String getAuthenticate() {
		return authenticate;
	}

	/**
	 * Sets authentication mode
	 * 
	 * @param authenticate
	 *            authentication mode
	 */
	public void setAuthenticate(String authenticateMode) {
		this.authenticate = authenticateMode;
	}

}
