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

package es.seap.minhap.portafirmas.business.jobs.dto;

import java.io.Serializable;
import java.util.List;


import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfiguration;

import es.seap.minhap.portafirmas.utils.notice.message.NoticeMessage;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;

public class EmailJobDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NoticeConfiguration emailConfiguration;
	private NoticeMessage emailMessage;
	private List<FileAttachedDTO> ficheros;
	public NoticeConfiguration getEmailConfiguration() {
		return emailConfiguration;
	}
	public void setEmailConfiguration(NoticeConfiguration emailConfiguration) {
		this.emailConfiguration = emailConfiguration;
	}
	public NoticeMessage getEmailMessage() {
		return emailMessage;
	}
	public void setEmailMessage(NoticeMessage emailMessage) {
		this.emailMessage = emailMessage;
	}
	public List<FileAttachedDTO> getFicheros() {
		return ficheros;
	}
	public void setFicheros(List<FileAttachedDTO> ficheros) {
		this.ficheros = ficheros;
	}
	public EmailJobDTO(NoticeConfiguration emailConfiguration, NoticeMessage emailMessage,
			List<FileAttachedDTO> ficheros) {
		super();
		this.emailConfiguration = emailConfiguration;
		this.emailMessage = emailMessage;
		this.ficheros = ficheros;
	}
	public EmailJobDTO() {
		super();
	}

	
	
}
