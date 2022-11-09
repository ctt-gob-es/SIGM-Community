
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
 *         &lt;element name="registerStatus" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileSIMUserStatus"/>
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
    "registerStatus"
})
@XmlRootElement(name = "registerSIMUserResponse")
public class RegisterSIMUserResponse {

    @XmlElement(required = true)
    protected MobileSIMUserStatus registerStatus;

    /**
     * Obtiene el valor de la propiedad registerStatus.
     * 
     * @return
     *     possible object is
     *     {@link MobileSIMUserStatus }
     *     
     */
    public MobileSIMUserStatus getRegisterStatus() {
        return registerStatus;
    }

    /**
     * Define el valor de la propiedad registerStatus.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileSIMUserStatus }
     *     
     */
    public void setRegisterStatus(MobileSIMUserStatus value) {
        this.registerStatus = value;
    }

}
