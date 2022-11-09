
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
 *         &lt;element name="fireTransactionResponse" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFireTrasactionResponse"/>
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
    "fireTransactionResponse"
})
@XmlRootElement(name = "fireTransactionResponse")
public class FireTransactionResponse {

    @XmlElement(required = true)
    protected MobileFireTrasactionResponse fireTransactionResponse;

    /**
     * Obtiene el valor de la propiedad fireTransactionResponse.
     * 
     * @return
     *     possible object is
     *     {@link MobileFireTrasactionResponse }
     *     
     */
    public MobileFireTrasactionResponse getFireTransactionResponse() {
        return fireTransactionResponse;
    }

    /**
     * Define el valor de la propiedad fireTransactionResponse.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileFireTrasactionResponse }
     *     
     */
    public void setFireTransactionResponse(MobileFireTrasactionResponse value) {
        this.fireTransactionResponse = value;
    }

}
