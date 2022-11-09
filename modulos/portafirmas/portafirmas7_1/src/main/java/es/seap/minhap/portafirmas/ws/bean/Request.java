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
 * Represents a request.
 * 
 * @author Guadaltel
 * @version 2.0.0
 * @since 2.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "request", propOrder = { "identifier", "subject", "fentry",
		"fstart", "fexpiration", "reference", "text", "signType",
		"application", "importanceLevel", "documentList", "signLineList", "remitterList",
		"parameterList", "noticeList", "actionList", "commentList", "requestStatus", "timestampInfo", "emailToNotifyList", 
		"signedMark"})
public class Request implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(required = true)
	protected String identifier;
	@XmlElement(required = true)
	protected String subject;
	@XmlElement(required = true)
	protected XMLGregorianCalendar fentry;
	@XmlElement(required = true)
	protected XMLGregorianCalendar fstart;
	@XmlElement(required = true)
	protected XMLGregorianCalendar fexpiration;
	@XmlElement(required = true)
	protected String reference;
	@XmlElement(required = true)
	protected String text;
	@XmlElement(required = true)
	protected String signType;
	@XmlElement(required = true)
	protected String application;
	@XmlElement(required = true)
	protected ImportanceLevel importanceLevel;
	@XmlElement(required = true)
	protected DocumentList documentList;
	@XmlElement(required = true)
	protected SignLineList signLineList;
	@XmlElement(required = true)
	protected RemitterList remitterList;
	@XmlElement(required = true)
	protected ParameterList parameterList;
	@XmlElement(required = true)
	protected NoticeList noticeList;
	@XmlElement(required = true)
	protected ActionList actionList;
	@XmlElement(required = true)
	protected CommentList commentList;
	@XmlElement(required = true)
	protected String requestStatus;
	@XmlElement(required = false)
	protected TimestampInfo timestampInfo;
	@XmlElement(required = false)
	protected StringList emailToNotifyList;
	@XmlElement(required = false)
	protected String signedMark;	

	/**
	 * Gets the value of the identifier property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the value of the identifier property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIdentifier(String value) {
		this.identifier = value;
	}

	/**
	 * Gets the value of the subject property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets the value of the subject property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSubject(String value) {
		this.subject = value;
	}

	/**
	 * Gets the value of the fentry property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getFentry() {
		return fentry;
	}

	/**
	 * Sets the value of the fentry property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setFentry(XMLGregorianCalendar value) {
		this.fentry = value;
	}

	/**
	 * Gets the value of the fstart property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getFstart() {
		return fstart;
	}

	/**
	 * Sets the value of the fstart property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setFstart(XMLGregorianCalendar value) {
		this.fstart = value;
	}

	/**
	 * Gets the value of the fexpiration property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getFexpiration() {
		return fexpiration;
	}

	/**
	 * Sets the value of the fexpiration property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setFexpiration(XMLGregorianCalendar value) {
		this.fexpiration = value;
	}

	/**
	 * Gets the value of the reference property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * Sets the value of the reference property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setReference(String value) {
		this.reference = value;
	}

	/**
	 * Gets the value of the text property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the value of the text property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setText(String value) {
		this.text = value;
	}

	/**
	 * Gets the value of the signType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSignType() {
		return signType;
	}

	/**
	 * Sets the value of the signType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSignType(String value) {
		this.signType = value;
	}

	/**
	 * Gets the value of the application property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * Sets the value of the application property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setApplication(String value) {
		this.application = value;
	}

	/**
	 * Gets the value of the importanceLevel property.
	 * 
	 * @return possible object is {@link ImportanceLevel }
	 * 
	 */
	public ImportanceLevel getImportanceLevel() {
		return importanceLevel;
	}

	/**
	 * Sets the value of the importanceLevel property.
	 * 
	 * @param value
	 *            allowed object is {@link ImportanceLevel }
	 * 
	 */
	public void setImportanceLevel(ImportanceLevel importanceLevel) {
		this.importanceLevel = importanceLevel;
	}

	/**
	 * Gets the value of the documentList property.
	 * 
	 * @return possible object is {@link DocumentList }
	 * 
	 */
	public DocumentList getDocumentList() {
		return documentList;
	}

	/**
	 * Sets the value of the documentList property.
	 * 
	 * @param value
	 *            allowed object is {@link DocumentList }
	 * 
	 */
	public void setDocumentList(DocumentList value) {
		this.documentList = value;
	}

	/**
	 * Gets the value of the signLineList property.
	 * 
	 * @return possible object is {@link SignLineList }
	 * 
	 */
	public SignLineList getSignLineList() {
		return signLineList;
	}

	/**
	 * Sets the value of the signLineList property.
	 * 
	 * @param value
	 *            allowed object is {@link SignLineList }
	 * 
	 */
	public void setSignLineList(SignLineList value) {
		this.signLineList = value;
	}

	/**
	 * Gets the value of the remitterList property.
	 * 
	 * @return possible object is {@link RemitterList }
	 * 
	 */
	public RemitterList getRemitterList() {
		return remitterList;
	}

	/**
	 * Sets the value of the remitterList property.
	 * 
	 * @param value
	 *            allowed object is {@link RemitterList }
	 * 
	 */
	public void setRemitterList(RemitterList value) {
		this.remitterList = value;
	}

	/**
	 * Gets the value of the parameterList property.
	 * 
	 * @return possible object is {@link ParameterList }
	 * 
	 */
	public ParameterList getParameterList() {
		return parameterList;
	}

	/**
	 * Sets the value of the parameterList property.
	 * 
	 * @param value
	 *            allowed object is {@link ParameterList }
	 * 
	 */
	public void setParameterList(ParameterList value) {
		this.parameterList = value;
	}

	/**
	 * Gets the value of the noticeList property.
	 * 
	 * @return possible object is {@link NoticeList }
	 * 
	 */
	public NoticeList getNoticeList() {
		return noticeList;
	}

	/**
	 * Sets the value of the noticeList property.
	 * 
	 * @param noticeList
	 *            allowed object is {@link NoticeList }
	 * 
	 */
	public void setNoticeList(NoticeList noticeList) {
		this.noticeList = noticeList;
	}

	/**
	 * Gets the value of the actionList property.
	 * 
	 * @return possible object is {@link ActionList }
	 * 
	 */
	public ActionList getActionList() {
		return actionList;
	}

	/**
	 * Sets the value of the actionList property.
	 * 
	 * @param actionList
	 *            allowed object is {@link ActionList }
	 * 
	 */
	public void setActionList(ActionList actionList) {
		this.actionList = actionList;
	}
	
	/**
	 * Gets the value of the actionList property.
	 * 
	 * @return possible object is {@link CommentList }
	 * 
	 */
	public CommentList getCommentList() {
		return commentList;
	}

	/**
	 * Sets the value of the actionList property.
	 * 
	 * @param commentList
	 *            allowed object is {@link CommentList }
	 * 
	 */
	public void setCommentList(CommentList commentList) {
		this.commentList = commentList;
	}

	/**
	 * @return the requestStatus
	 */
	public String getRequestStatus() {
		return requestStatus;
	}

	/**
	 * @param requestStatus the requestStatus to set
	 */
	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	/**
	 * @return the timestampInfo
	 */
	public TimestampInfo getTimestampInfo() {
		return timestampInfo;
	}

	/**
	 * @param timestampInfo the timestampInfo to set
	 */
	public void setTimestampInfo(TimestampInfo timestampInfo) {
		this.timestampInfo = timestampInfo;
	}

	public StringList getEmailToNotifyList() {
		return emailToNotifyList;
	}

	public void setEmailToNotifyList(StringList emailToNotifyList) {
		this.emailToNotifyList = emailToNotifyList;
	}

	public String getSignedMark() {
		return signedMark;
	}

	public void setSignedMark(String signedMark) {
		this.signedMark = signedMark;
	}
}
