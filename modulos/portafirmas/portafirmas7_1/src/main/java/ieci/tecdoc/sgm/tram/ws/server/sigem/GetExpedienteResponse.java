
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Expediente;


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
 *         &lt;element name="getExpedienteReturn" type="{}Expediente"/>
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
    "getExpedienteReturn"
})
@XmlRootElement(name = "getExpedienteResponse")
public class GetExpedienteResponse {

    @XmlElement(required = true)
    protected Expediente getExpedienteReturn;

    /**
     * Obtiene el valor de la propiedad getExpedienteReturn.
     * 
     * @return
     *     possible object is
     *     {@link Expediente }
     *     
     */
    public Expediente getGetExpedienteReturn() {
        return getExpedienteReturn;
    }

    /**
     * Define el valor de la propiedad getExpedienteReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Expediente }
     *     
     */
    public void setGetExpedienteReturn(Expediente value) {
        this.getExpedienteReturn = value;
    }

}
