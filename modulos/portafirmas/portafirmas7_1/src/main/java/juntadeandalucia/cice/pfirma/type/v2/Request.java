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


package juntadeandalucia.cice.pfirma.type.v2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for request complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="request">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fentry" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fstart" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fexpiration" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signType" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}signType" minOccurs="0"/>
 *         &lt;element name="application" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="importanceLevel" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}importanceLevel" minOccurs="0"/>
 *         &lt;element name="documentList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}documentList" minOccurs="0"/>
 *         &lt;element name="signLineList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}signLineList" minOccurs="0"/>
 *         &lt;element name="remitterList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}remitterList" minOccurs="0"/>
 *         &lt;element name="parameterList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}parameterList" minOccurs="0"/>
 *         &lt;element name="noticeList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}noticeList" minOccurs="0"/>
 *         &lt;element name="actionList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}actionList" minOccurs="0"/>
 *         &lt;element name="commentList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}commentList" minOccurs="0"/>
 *         &lt;element name="requestStatus" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}requestStatus" minOccurs="0"/>
 *         &lt;element name="timestampInfo" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}timestampInfo" minOccurs="0"/>
 *         &lt;element name="emailToNotifyList" type="{urn:juntadeandalucia:cice:pfirma:type:v2.0}stringList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "request", propOrder = {
    "identifier",
    "subject",
    "fentry",
    "fstart",
    "fexpiration",
    "reference",
    "text",
    "signType",
    "application",
    "importanceLevel",
    "documentList",
    "signLineList",
    "remitterList",
    "parameterList",
    "noticeList",
    "actionList",
    "commentList",
    "requestStatus",
    "timestampInfo",
    "emailToNotifyList"
})
public class Request {

    @XmlElementRef(name = "identifier", type = JAXBElement.class, required = false)
    protected JAXBElement<String> identifier;
    @XmlElementRef(name = "subject", type = JAXBElement.class, required = false)
    protected JAXBElement<String> subject;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fentry;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fstart;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fexpiration;
    @XmlElementRef(name = "reference", type = JAXBElement.class, required = false)
    protected JAXBElement<String> reference;
    @XmlElementRef(name = "text", type = JAXBElement.class, required = false)
    protected JAXBElement<String> text;
    @XmlElementRef(name = "signType", type = JAXBElement.class, required = false)
    protected JAXBElement<SignType> signType;
    @XmlElementRef(name = "application", type = JAXBElement.class, required = false)
    protected JAXBElement<String> application;
    @XmlElementRef(name = "importanceLevel", type = JAXBElement.class, required = false)
    protected JAXBElement<ImportanceLevel> importanceLevel;
    @XmlElementRef(name = "documentList", type = JAXBElement.class, required = false)
    protected JAXBElement<DocumentList> documentList;
    @XmlElementRef(name = "signLineList", type = JAXBElement.class, required = false)
    protected JAXBElement<SignLineList> signLineList;
    @XmlElementRef(name = "remitterList", type = JAXBElement.class, required = false)
    protected JAXBElement<RemitterList> remitterList;
    @XmlElementRef(name = "parameterList", type = JAXBElement.class, required = false)
    protected JAXBElement<ParameterList> parameterList;
    @XmlElementRef(name = "noticeList", type = JAXBElement.class, required = false)
    protected JAXBElement<NoticeList> noticeList;
    @XmlElementRef(name = "actionList", type = JAXBElement.class, required = false)
    protected JAXBElement<ActionList> actionList;
    @XmlElementRef(name = "commentList", type = JAXBElement.class, required = false)
    protected JAXBElement<CommentList> commentList;
    @XmlElementRef(name = "requestStatus", type = JAXBElement.class, required = false)
    protected JAXBElement<RequestStatus> requestStatus;
    @XmlElementRef(name = "timestampInfo", type = JAXBElement.class, required = false)
    protected JAXBElement<TimestampInfo> timestampInfo;
    @XmlElementRef(name = "emailToNotifyList", type = JAXBElement.class, required = false)
    protected JAXBElement<StringList> emailToNotifyList;

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdentifier(JAXBElement<String> value) {
        this.identifier = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubject(JAXBElement<String> value) {
        this.subject = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the fentry property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFentry() {
        return fentry;
    }

    /**
     * Sets the value of the fentry property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFentry(XMLGregorianCalendar value) {
        this.fentry = value;
    }

    /**
     * Gets the value of the fstart property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFstart() {
        return fstart;
    }

    /**
     * Sets the value of the fstart property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFstart(XMLGregorianCalendar value) {
        this.fstart = value;
    }

    /**
     * Gets the value of the fexpiration property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFexpiration() {
        return fexpiration;
    }

    /**
     * Sets the value of the fexpiration property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFexpiration(XMLGregorianCalendar value) {
        this.fexpiration = value;
    }

    /**
     * Gets the value of the reference property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReference(JAXBElement<String> value) {
        this.reference = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setText(JAXBElement<String> value) {
        this.text = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the signType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SignType }{@code >}
     *     
     */
    public JAXBElement<SignType> getSignType() {
        return signType;
    }

    /**
     * Sets the value of the signType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SignType }{@code >}
     *     
     */
    public void setSignType(JAXBElement<SignType> value) {
        this.signType = ((JAXBElement<SignType> ) value);
    }

    /**
     * Gets the value of the application property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getApplication() {
        return application;
    }

    /**
     * Sets the value of the application property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setApplication(JAXBElement<String> value) {
        this.application = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the importanceLevel property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ImportanceLevel }{@code >}
     *     
     */
    public JAXBElement<ImportanceLevel> getImportanceLevel() {
        return importanceLevel;
    }

    /**
     * Sets the value of the importanceLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ImportanceLevel }{@code >}
     *     
     */
    public void setImportanceLevel(JAXBElement<ImportanceLevel> value) {
        this.importanceLevel = ((JAXBElement<ImportanceLevel> ) value);
    }

    /**
     * Gets the value of the documentList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DocumentList }{@code >}
     *     
     */
    public JAXBElement<DocumentList> getDocumentList() {
        return documentList;
    }

    /**
     * Sets the value of the documentList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DocumentList }{@code >}
     *     
     */
    public void setDocumentList(JAXBElement<DocumentList> value) {
        this.documentList = ((JAXBElement<DocumentList> ) value);
    }

    /**
     * Gets the value of the signLineList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SignLineList }{@code >}
     *     
     */
    public JAXBElement<SignLineList> getSignLineList() {
        return signLineList;
    }

    /**
     * Sets the value of the signLineList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SignLineList }{@code >}
     *     
     */
    public void setSignLineList(JAXBElement<SignLineList> value) {
        this.signLineList = ((JAXBElement<SignLineList> ) value);
    }

    /**
     * Gets the value of the remitterList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RemitterList }{@code >}
     *     
     */
    public JAXBElement<RemitterList> getRemitterList() {
        return remitterList;
    }

    /**
     * Sets the value of the remitterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RemitterList }{@code >}
     *     
     */
    public void setRemitterList(JAXBElement<RemitterList> value) {
        this.remitterList = ((JAXBElement<RemitterList> ) value);
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ParameterList }{@code >}
     *     
     */
    public JAXBElement<ParameterList> getParameterList() {
        return parameterList;
    }

    /**
     * Sets the value of the parameterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ParameterList }{@code >}
     *     
     */
    public void setParameterList(JAXBElement<ParameterList> value) {
        this.parameterList = ((JAXBElement<ParameterList> ) value);
    }

    /**
     * Gets the value of the noticeList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link NoticeList }{@code >}
     *     
     */
    public JAXBElement<NoticeList> getNoticeList() {
        return noticeList;
    }

    /**
     * Sets the value of the noticeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link NoticeList }{@code >}
     *     
     */
    public void setNoticeList(JAXBElement<NoticeList> value) {
        this.noticeList = ((JAXBElement<NoticeList> ) value);
    }

    /**
     * Gets the value of the actionList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ActionList }{@code >}
     *     
     */
    public JAXBElement<ActionList> getActionList() {
        return actionList;
    }

    /**
     * Sets the value of the actionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ActionList }{@code >}
     *     
     */
    public void setActionList(JAXBElement<ActionList> value) {
        this.actionList = ((JAXBElement<ActionList> ) value);
    }

    /**
     * Gets the value of the commentList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CommentList }{@code >}
     *     
     */
    public JAXBElement<CommentList> getCommentList() {
        return commentList;
    }

    /**
     * Sets the value of the commentList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CommentList }{@code >}
     *     
     */
    public void setCommentList(JAXBElement<CommentList> value) {
        this.commentList = ((JAXBElement<CommentList> ) value);
    }

    /**
     * Gets the value of the requestStatus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link RequestStatus }{@code >}
     *     
     */
    public JAXBElement<RequestStatus> getRequestStatus() {
        return requestStatus;
    }

    /**
     * Sets the value of the requestStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link RequestStatus }{@code >}
     *     
     */
    public void setRequestStatus(JAXBElement<RequestStatus> value) {
        this.requestStatus = ((JAXBElement<RequestStatus> ) value);
    }

    /**
     * Gets the value of the timestampInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TimestampInfo }{@code >}
     *     
     */
    public JAXBElement<TimestampInfo> getTimestampInfo() {
        return timestampInfo;
    }

    /**
     * Sets the value of the timestampInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TimestampInfo }{@code >}
     *     
     */
    public void setTimestampInfo(JAXBElement<TimestampInfo> value) {
        this.timestampInfo = ((JAXBElement<TimestampInfo> ) value);
    }

    /**
     * Gets the value of the emailToNotifyList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link StringList }{@code >}
     *     
     */
    public JAXBElement<StringList> getEmailToNotifyList() {
        return emailToNotifyList;
    }

    /**
     * Sets the value of the emailToNotifyList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link StringList }{@code >}
     *     
     */
    public void setEmailToNotifyList(JAXBElement<StringList> value) {
        this.emailToNotifyList = ((JAXBElement<StringList> ) value);
    }

}
