
package es.gob.afirma.signfolder.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para mobileRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="view" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="text" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ref" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fentry" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fexpiration" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="importanceLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="application" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="workflow" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="forward" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rejected" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="rejectedText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cascadeSign" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="senders" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileStringList" minOccurs="0"/>
 *         &lt;element name="signLineList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileSignLineList" minOccurs="0"/>
 *         &lt;element name="documentList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileDocumentList"/>
 *         &lt;element name="attachList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileDocumentList" minOccurs="0"/>
 *         &lt;element name="requestType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="requestTagId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileRequest", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "identifier",
    "subject",
    "view",
    "text",
    "ref",
    "fentry",
    "fexpiration",
    "importanceLevel",
    "application",
    "workflow",
    "forward",
    "rejected",
    "rejectedText",
    "cascadeSign",
    "senders",
    "signLineList",
    "documentList",
    "attachList",
    "requestType",
    "requestTagId"
})
public class MobileRequest {

    @XmlElementRef(name = "identifier", type = JAXBElement.class, required = false)
    protected JAXBElement<String> identifier;
    @XmlElementRef(name = "subject", type = JAXBElement.class, required = false)
    protected JAXBElement<String> subject;
    protected String view;
    @XmlElementRef(name = "text", type = JAXBElement.class, required = false)
    protected JAXBElement<String> text;
    @XmlElementRef(name = "ref", type = JAXBElement.class, required = false)
    protected JAXBElement<String> ref;
    @XmlElementRef(name = "fentry", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> fentry;
    @XmlElementRef(name = "fexpiration", type = JAXBElement.class, required = false)
    protected JAXBElement<XMLGregorianCalendar> fexpiration;
    @XmlElementRef(name = "importanceLevel", type = JAXBElement.class, required = false)
    protected JAXBElement<String> importanceLevel;
    @XmlElementRef(name = "application", type = JAXBElement.class, required = false)
    protected JAXBElement<String> application;
    @XmlElementRef(name = "workflow", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> workflow;
    @XmlElementRef(name = "forward", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> forward;
    @XmlElementRef(name = "rejected", type = JAXBElement.class, required = false)
    protected JAXBElement<Boolean> rejected;
    @XmlElementRef(name = "rejectedText", type = JAXBElement.class, required = false)
    protected JAXBElement<String> rejectedText;
    protected boolean cascadeSign;
    protected MobileStringList senders;
    @XmlElementRef(name = "signLineList", type = JAXBElement.class, required = false)
    protected JAXBElement<MobileSignLineList> signLineList;
    @XmlElement(required = true)
    protected MobileDocumentList documentList;
    @XmlElementRef(name = "attachList", type = JAXBElement.class, required = false)
    protected JAXBElement<MobileDocumentList> attachList;
    protected String requestType;
    protected String requestTagId;

    /**
     * Obtiene el valor de la propiedad identifier.
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
     * Define el valor de la propiedad identifier.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setIdentifier(JAXBElement<String> value) {
        this.identifier = value;
    }

    /**
     * Obtiene el valor de la propiedad subject.
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
     * Define el valor de la propiedad subject.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubject(JAXBElement<String> value) {
        this.subject = value;
    }

    /**
     * Obtiene el valor de la propiedad view.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getView() {
        return view;
    }

    /**
     * Define el valor de la propiedad view.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setView(String value) {
        this.view = value;
    }

    /**
     * Obtiene el valor de la propiedad text.
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
     * Define el valor de la propiedad text.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setText(JAXBElement<String> value) {
        this.text = value;
    }

    /**
     * Obtiene el valor de la propiedad ref.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRef() {
        return ref;
    }

    /**
     * Define el valor de la propiedad ref.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRef(JAXBElement<String> value) {
        this.ref = value;
    }

    /**
     * Obtiene el valor de la propiedad fentry.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getFentry() {
        return fentry;
    }

    /**
     * Define el valor de la propiedad fentry.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setFentry(JAXBElement<XMLGregorianCalendar> value) {
        this.fentry = value;
    }

    /**
     * Obtiene el valor de la propiedad fexpiration.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public JAXBElement<XMLGregorianCalendar> getFexpiration() {
        return fexpiration;
    }

    /**
     * Define el valor de la propiedad fexpiration.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     *     
     */
    public void setFexpiration(JAXBElement<XMLGregorianCalendar> value) {
        this.fexpiration = value;
    }

    /**
     * Obtiene el valor de la propiedad importanceLevel.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getImportanceLevel() {
        return importanceLevel;
    }

    /**
     * Define el valor de la propiedad importanceLevel.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setImportanceLevel(JAXBElement<String> value) {
        this.importanceLevel = value;
    }

    /**
     * Obtiene el valor de la propiedad application.
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
     * Define el valor de la propiedad application.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setApplication(JAXBElement<String> value) {
        this.application = value;
    }

    /**
     * Obtiene el valor de la propiedad workflow.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getWorkflow() {
        return workflow;
    }

    /**
     * Define el valor de la propiedad workflow.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setWorkflow(JAXBElement<Boolean> value) {
        this.workflow = value;
    }

    /**
     * Obtiene el valor de la propiedad forward.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getForward() {
        return forward;
    }

    /**
     * Define el valor de la propiedad forward.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setForward(JAXBElement<Boolean> value) {
        this.forward = value;
    }

    /**
     * Obtiene el valor de la propiedad rejected.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public JAXBElement<Boolean> getRejected() {
        return rejected;
    }

    /**
     * Define el valor de la propiedad rejected.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     *     
     */
    public void setRejected(JAXBElement<Boolean> value) {
        this.rejected = value;
    }

    /**
     * Obtiene el valor de la propiedad rejectedText.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRejectedText() {
        return rejectedText;
    }

    /**
     * Define el valor de la propiedad rejectedText.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRejectedText(JAXBElement<String> value) {
        this.rejectedText = value;
    }

    /**
     * Obtiene el valor de la propiedad cascadeSign.
     * 
     */
    public boolean isCascadeSign() {
        return cascadeSign;
    }

    /**
     * Define el valor de la propiedad cascadeSign.
     * 
     */
    public void setCascadeSign(boolean value) {
        this.cascadeSign = value;
    }

    /**
     * Obtiene el valor de la propiedad senders.
     * 
     * @return
     *     possible object is
     *     {@link MobileStringList }
     *     
     */
    public MobileStringList getSenders() {
        return senders;
    }

    /**
     * Define el valor de la propiedad senders.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileStringList }
     *     
     */
    public void setSenders(MobileStringList value) {
        this.senders = value;
    }

    /**
     * Obtiene el valor de la propiedad signLineList.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MobileSignLineList }{@code >}
     *     
     */
    public JAXBElement<MobileSignLineList> getSignLineList() {
        return signLineList;
    }

    /**
     * Define el valor de la propiedad signLineList.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MobileSignLineList }{@code >}
     *     
     */
    public void setSignLineList(JAXBElement<MobileSignLineList> value) {
        this.signLineList = value;
    }

    /**
     * Obtiene el valor de la propiedad documentList.
     * 
     * @return
     *     possible object is
     *     {@link MobileDocumentList }
     *     
     */
    public MobileDocumentList getDocumentList() {
        return documentList;
    }

    /**
     * Define el valor de la propiedad documentList.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileDocumentList }
     *     
     */
    public void setDocumentList(MobileDocumentList value) {
        this.documentList = value;
    }

    /**
     * Obtiene el valor de la propiedad attachList.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MobileDocumentList }{@code >}
     *     
     */
    public JAXBElement<MobileDocumentList> getAttachList() {
        return attachList;
    }

    /**
     * Define el valor de la propiedad attachList.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MobileDocumentList }{@code >}
     *     
     */
    public void setAttachList(JAXBElement<MobileDocumentList> value) {
        this.attachList = value;
    }

    /**
     * Obtiene el valor de la propiedad requestType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Define el valor de la propiedad requestType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestType(String value) {
        this.requestType = value;
    }

    /**
     * Obtiene el valor de la propiedad requestTagId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestTagId() {
        return requestTagId;
    }

    /**
     * Define el valor de la propiedad requestTagId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestTagId(String value) {
        this.requestTagId = value;
    }

}
