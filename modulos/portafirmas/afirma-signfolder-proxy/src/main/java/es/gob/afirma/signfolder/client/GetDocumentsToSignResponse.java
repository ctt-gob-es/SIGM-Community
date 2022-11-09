
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
 *         &lt;element name="documentList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileDocumentList"/>
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
    "documentList"
})
@XmlRootElement(name = "getDocumentsToSignResponse")
public class GetDocumentsToSignResponse {

    @XmlElement(required = true)
    protected MobileDocumentList documentList;

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

}
