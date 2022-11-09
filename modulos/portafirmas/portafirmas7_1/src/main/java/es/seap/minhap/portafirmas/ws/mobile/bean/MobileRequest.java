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

package es.seap.minhap.portafirmas.ws.mobile.bean;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileRequest", propOrder = { "identifier", "subject", "view", "text",
		"ref", "fentry", "importanceLevel", "application", "workflow", "forward", 
		"rejected", "rejectedText", "fexpiration", "cascadeSign",  "senders", "signLineList", 
		"documentList", "attachList", "requestType", "requestTagId"})
public class MobileRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XmlElement(required = true)
	protected String identifier;
	@XmlElement(required = true)
	protected String subject;
	@XmlElement(required = true)
	protected String view;
	@XmlElement(required = true)
	protected String text;
	@XmlElement(required = true)
	protected String ref;
	@XmlElement(required = true)
	protected XMLGregorianCalendar fentry;
	@XmlElement(required = false)
	protected XMLGregorianCalendar fexpiration;
	@XmlElement(required = true)
	protected String importanceLevel;
	@XmlElement(required = true)
	protected String application;
	@XmlElement(required = true)
	protected Boolean workflow;
	@XmlElement(required = true)
	protected Boolean forward;
	@XmlElement(required=false)
	protected Boolean rejected;
	@XmlElement(required=false)
	protected String rejectedText;
	@XmlElement(required = true)
	protected MobileStringList senders;
	@XmlElement(required = false)
	protected MobileSignLineList signLineList;
	@XmlElement(required = false)
	protected MobileDocumentList documentList;
	@XmlElement(required = false)
	protected MobileDocumentList attachList;
	//Cascada o En Paralelo
	@XmlElement(required = true)
	protected Boolean cascadeSign;
	@XmlElement(required = true)
	protected String requestType;
	@XmlElement(required = true)
	protected String requestTagId;

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
	 * Gets the value of the documentList property.
	 * 
	 * @return possible object is {@link MobileDocumentList }
	 * 
	 */
	public MobileDocumentList getDocumentList() {
		return documentList;
	}

	/**
	 * Sets the value of the documentList property.
	 * 
	 * @param value
	 *            allowed object is {@link MobileDocumentList }
	 * 
	 */
	public void setDocumentList(MobileDocumentList value) {
		this.documentList = value;
	}

	/**
	 * Gets the value of the attachList property.
	 * 
	 * @return possible object is {@link MobileDocumentList }
	 * 
	 */
	public MobileDocumentList getAttachList() {
		return attachList;
	}

	/**
	 * Sets the value of the attachList property.
	 * 
	 * @param value
	 *            allowed object is {@link MobileDocumentList }
	 * 
	 */
	public void setAttachList(MobileDocumentList value) {
		this.attachList = value;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getImportanceLevel() {
		return importanceLevel;
	}

	public void setImportanceLevel(String importanceLevel) {
		this.importanceLevel = importanceLevel;
	}

	public Boolean getWorkflow() {
		return workflow;
	}

	public void setWorkflow(Boolean workflow) {
		this.workflow = workflow;
	}

	public Boolean getForward() {
		return forward;
	}

	public void setForward(Boolean forward) {
		this.forward = forward;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public MobileSignLineList getSignLineList() {
		return signLineList;
	}

	public void setSignLineList(MobileSignLineList signLineList) {
		this.signLineList = signLineList;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequestTagId() {
		return requestTagId;
	}

	public void setRequestTagId(String requestTagId) {
		this.requestTagId = requestTagId;
	}

	public MobileStringList getSenders() {
		return senders;
	}

	public void setSenders(MobileStringList senders) {
		this.senders = senders;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Boolean getRejected() {
		return rejected;
	}

	public void setRejected(Boolean rejected) {
		this.rejected = rejected;
	}

	public String getRejectedText() {
		return rejectedText;
	}

	public void setRejectedText(String rejectedText) {
		this.rejectedText = rejectedText;
	}

	public XMLGregorianCalendar getFexpiration() {
		return fexpiration;
	}

	public void setFexpiration(XMLGregorianCalendar fexpiration) {
		this.fexpiration = fexpiration;
	}

	public Boolean getCascadeSign() {
		return cascadeSign;
	}

	public void setCascadeSign(Boolean cascadeSign) {
		this.cascadeSign = cascadeSign;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

}
