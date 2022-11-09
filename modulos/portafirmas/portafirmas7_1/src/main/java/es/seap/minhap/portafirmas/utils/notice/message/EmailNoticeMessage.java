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

package es.seap.minhap.portafirmas.utils.notice.message;

import java.io.Serializable;
import java.util.List;

/**
 * Class used to store message which will be sent by email
 * 
 * @author Guadaltel
 */
public class EmailNoticeMessage implements NoticeMessage, Serializable {

	private static final long serialVersionUID = 1L;

	private StringBuilder plainText;
	private StringBuilder htmlText;
	private List<String> receivers;
	private String subject;

	/**
	 * Returns email message in plain text format
	 * 
	 * @return email message in plain text format
	 */
	public StringBuilder getPlainText() {
		return plainText;
	}

	/**
	 * Sets email message in plain text format
	 * 
	 * @param text
	 *            email message in plain text format
	 */
	public void setPlainText(StringBuilder text) {
		this.plainText = text;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

	/**
	 * Returns subject of email message
	 * 
	 * @return subject of email message
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets subject of email message
	 * 
	 * @param subject
	 *            subject of email message
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Returns email message in html format with template
	 * 
	 * @return email message in html format with template
	 */
	public StringBuilder getHtmlText() {
		return htmlText;
	}

	/**
	 * Sets email message in html format with template
	 * 
	 * @param htmlText
	 *            email message in html format with template
	 */
	public void setHtmlText(StringBuilder htmlText) {
		this.htmlText = htmlText;
	}

}
