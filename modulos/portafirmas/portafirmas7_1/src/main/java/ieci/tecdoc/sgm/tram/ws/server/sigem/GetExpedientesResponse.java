
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.ListaInfoBExpedientes;


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
 *         &lt;element name="getExpedientesReturn" type="{}ListaInfoBExpedientes"/>
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
    "getExpedientesReturn"
})
@XmlRootElement(name = "getExpedientesResponse")
public class GetExpedientesResponse {

    @XmlElement(required = true)
    protected ListaInfoBExpedientes getExpedientesReturn;

    /**
     * Obtiene el valor de la propiedad getExpedientesReturn.
     * 
     * @return
     *     possible object is
     *     {@link ListaInfoBExpedientes }
     *     
     */
    public ListaInfoBExpedientes getGetExpedientesReturn() {
        return getExpedientesReturn;
    }

    /**
     * Define el valor de la propiedad getExpedientesReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaInfoBExpedientes }
     *     
     */
    public void setGetExpedientesReturn(ListaInfoBExpedientes value) {
        this.getExpedientesReturn = value;
    }

}
