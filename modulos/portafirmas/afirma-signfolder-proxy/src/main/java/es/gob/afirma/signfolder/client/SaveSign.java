
package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="certificate" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="requestTagId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="docSignInfoList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileDocSignInfoList"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "certificate",
    "requestTagId",
    "docSignInfoList"
})
@XmlRootElement(name = "saveSign")
public class SaveSign {

    @XmlElement(required = true)
    protected byte[] certificate;
    @XmlElement(required = true)
    protected String requestTagId;
    @XmlElement(required = true)
    protected MobileDocSignInfoList docSignInfoList;

    /**
     * Obtiene el valor de la propiedad certificate.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertificate() {
        return certificate;
    }

    /**
     * Define el valor de la propiedad certificate.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertificate(byte[] value) {
        this.certificate = value;
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

    /**
     * Obtiene el valor de la propiedad docSignInfoList.
     * 
     * @return
     *     possible object is
     *     {@link MobileDocSignInfoList }
     *     
     */
    public MobileDocSignInfoList getDocSignInfoList() {
        return docSignInfoList;
    }

    /**
     * Define el valor de la propiedad docSignInfoList.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileDocSignInfoList }
     *     
     */
    public void setDocSignInfoList(MobileDocSignInfoList value) {
        this.docSignInfoList = value;
    }

}
