
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.InfoOcupacion;


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
 *         &lt;element name="getInfoOcupacionReturn" type="{}InfoOcupacion"/>
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
    "getInfoOcupacionReturn"
})
@XmlRootElement(name = "getInfoOcupacionResponse")
public class GetInfoOcupacionResponse {

    @XmlElement(required = true)
    protected InfoOcupacion getInfoOcupacionReturn;

    /**
     * Obtiene el valor de la propiedad getInfoOcupacionReturn.
     * 
     * @return
     *     possible object is
     *     {@link InfoOcupacion }
     *     
     */
    public InfoOcupacion getGetInfoOcupacionReturn() {
        return getInfoOcupacionReturn;
    }

    /**
     * Define el valor de la propiedad getInfoOcupacionReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoOcupacion }
     *     
     */
    public void setGetInfoOcupacionReturn(InfoOcupacion value) {
        this.getInfoOcupacionReturn = value;
    }

}
