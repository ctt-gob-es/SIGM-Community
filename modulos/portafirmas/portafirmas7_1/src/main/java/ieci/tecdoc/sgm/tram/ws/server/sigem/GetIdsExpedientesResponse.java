
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.ListaIdentificadores;


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
 *         &lt;element name="getIdsExpedientesReturn" type="{}ListaIdentificadores"/>
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
    "getIdsExpedientesReturn"
})
@XmlRootElement(name = "getIdsExpedientesResponse")
public class GetIdsExpedientesResponse {

    @XmlElement(required = true)
    protected ListaIdentificadores getIdsExpedientesReturn;

    /**
     * Obtiene el valor de la propiedad getIdsExpedientesReturn.
     * 
     * @return
     *     possible object is
     *     {@link ListaIdentificadores }
     *     
     */
    public ListaIdentificadores getGetIdsExpedientesReturn() {
        return getIdsExpedientesReturn;
    }

    /**
     * Define el valor de la propiedad getIdsExpedientesReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaIdentificadores }
     *     
     */
    public void setGetIdsExpedientesReturn(ListaIdentificadores value) {
        this.getIdsExpedientesReturn = value;
    }

}
