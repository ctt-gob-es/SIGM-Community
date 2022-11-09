
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
 *         &lt;element name="applicationList" type="{urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0}mobileApplicationList"/>
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
    "applicationList"
})
@XmlRootElement(name = "queryApplicationsMobileResponse")
public class QueryApplicationsMobileResponse {

    @XmlElement(required = true)
    protected MobileApplicationList applicationList;

    /**
     * Obtiene el valor de la propiedad applicationList.
     * 
     * @return
     *     possible object is
     *     {@link MobileApplicationList }
     *     
     */
    public MobileApplicationList getApplicationList() {
        return applicationList;
    }

    /**
     * Define el valor de la propiedad applicationList.
     * 
     * @param value
     *     allowed object is
     *     {@link MobileApplicationList }
     *     
     */
    public void setApplicationList(MobileApplicationList value) {
        this.applicationList = value;
    }

}
