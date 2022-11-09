
package es.gob.afirma.signfolder.client;

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileDocSignInfo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileDocSignInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documentId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="signature" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="signFormat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileDocSignInfo", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "documentId",
    "signature",
    "signFormat"
})
public class MobileDocSignInfo {

    protected String documentId;
    @XmlMimeType("application/octet-stream")
    protected DataHandler signature;
    protected String signFormat;

    /**
     * Obtiene el valor de la propiedad documentId.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * Define el valor de la propiedad documentId.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentId(String value) {
        this.documentId = value;
    }

    /**
     * Obtiene el valor de la propiedad signature.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getSignature() {
        return signature;
    }

    /**
     * Define el valor de la propiedad signature.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setSignature(DataHandler value) {
        this.signature = value;
    }

    /**
     * Obtiene el valor de la propiedad signFormat.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignFormat() {
        return signFormat;
    }

    /**
     * Define el valor de la propiedad signFormat.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignFormat(String value) {
        this.signFormat = value;
    }

}
