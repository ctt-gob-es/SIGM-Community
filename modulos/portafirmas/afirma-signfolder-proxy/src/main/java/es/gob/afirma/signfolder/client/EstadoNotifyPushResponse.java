
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
 *         &lt;element name="valorNotifyPush" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "valorNotifyPush"
})
@XmlRootElement(name = "estadoNotifyPushResponse")
public class EstadoNotifyPushResponse {

    @XmlElement(required = true)
    protected String valorNotifyPush;

    /**
     * Obtiene el valor de la propiedad valorNotifyPush.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValorNotifyPush() {
        return valorNotifyPush;
    }

    /**
     * Define el valor de la propiedad valorNotifyPush.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValorNotifyPush(String value) {
        this.valorNotifyPush = value;
    }

}
