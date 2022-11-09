
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
 *         &lt;element name="fireRequestList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileFireRequestList"/>
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
    "fireRequestList"
})
@XmlRootElement(name = "signFireCloudResponse")
public class SignFireCloudResponse {

    @XmlElement(required = true)
    protected MobileFireRequestList fireRequestList;

    /**
     * Obtiene el valor de la propiedad fireRequestList.
     * 
     * @return
     *     possible object is
     *     {@link MobileFireRequestList }
     *     
     */
    public MobileFireRequestList getFireRequestList() {
        return fireRequestList;
    }

    /**
     * Define el valor de la propiedad fireRequestList.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileFireRequestList }
     *     
     */
    public void setFireRequestList(MobileFireRequestList value) {
        this.fireRequestList = value;
    }

}
