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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Representa un comentario de la petición.
 * @author rus
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comment", propOrder = { "subject", "textComment", "fmodify",
		"user" })
public class Comment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(required = true)
	protected String subject;
	@XmlElement(required = true)
	protected String textComment;
	@XmlElement(required = true)
	protected XMLGregorianCalendar fmodify;
	@XmlElement(required = true)
	protected UserJob user;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTextComment() {
		return textComment;
	}
	public void setTextComment(String textComment) {
		this.textComment = textComment;
	}
	public XMLGregorianCalendar getFmodify() {
		return fmodify;
	}
	public void setFmodify(XMLGregorianCalendar fmodify) {
		this.fmodify = fmodify;
	}
	public UserJob getUser() {
		return user;
	}
	public void setUser(UserJob user) {
		this.user = user;
	}
	
	
	

}
