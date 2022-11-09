
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
 *         &lt;element name="requestList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileRequestList"/>
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
    "requestList"
})
@XmlRootElement(name = "queryRequestListResponse")
public class QueryRequestListResponse {

    @XmlElement(required = true)
    protected MobileRequestList requestList;

    /**
     * Obtiene el valor de la propiedad requestList.
     * 
     * @return
     *     possible object is
     *     {@link MobileRequestList }
     *     
     */
    public MobileRequestList getRequestList() {
        return requestList;
    }

    /**
     * Define el valor de la propiedad requestList.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileRequestList }
     *     
     */
    public void setRequestList(MobileRequestList value) {
        this.requestList = value;
    }

}
