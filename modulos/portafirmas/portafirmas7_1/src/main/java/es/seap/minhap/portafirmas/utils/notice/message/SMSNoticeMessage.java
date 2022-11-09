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

import java.util.List;

/**
 * Class used to store message which will be sent by SMS
 * 
 * @author Guadaltel
 */
public class SMSNoticeMessage implements NoticeMessage {

	private static final long serialVersionUID = 1L;

	private String text;
	private List<String> receivers;

	/**
	 * Returns SMS text
	 * 
	 * @return SMS text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets SMS text
	 * 
	 * @param text
	 *            SMS text
	 */
	public void setText(String text) {
		this.text = text;
	}

	public List<String> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<String> receivers) {
		this.receivers = receivers;
	}

}
