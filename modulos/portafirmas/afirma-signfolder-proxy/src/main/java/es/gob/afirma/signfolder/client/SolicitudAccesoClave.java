
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
 *         &lt;element name="spUrl" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="spReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "spUrl",
    "spReturn"
})
@XmlRootElement(name = "solicitudAccesoClave")
public class SolicitudAccesoClave {

    @XmlElement(required = true)
    protected String spUrl;
    @XmlElement(required = true)
    protected String spReturn;

    /**
     * Obtiene el valor de la propiedad spUrl.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpUrl() {
        return spUrl;
    }

    /**
     * Define el valor de la propiedad spUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpUrl(String value) {
        this.spUrl = value;
    }

    /**
     * Obtiene el valor de la propiedad spReturn.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpReturn() {
        return spReturn;
    }

    /**
     * Define el valor de la propiedad spReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpReturn(String value) {
        this.spReturn = value;
    }

}
