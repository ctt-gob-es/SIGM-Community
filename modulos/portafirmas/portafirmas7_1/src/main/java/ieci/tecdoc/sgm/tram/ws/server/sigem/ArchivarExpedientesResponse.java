
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;


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
 *         &lt;element name="archivarExpedientesReturn" type="{}RetornoServicio"/>
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
    "archivarExpedientesReturn"
})
@XmlRootElement(name = "archivarExpedientesResponse")
public class ArchivarExpedientesResponse {

    @XmlElement(required = true)
    protected RetornoServicio archivarExpedientesReturn;

    /**
     * Obtiene el valor de la propiedad archivarExpedientesReturn.
     * 
     * @return
     *     possible object is
     *     {@link RetornoServicio }
     *     
     */
    public RetornoServicio getArchivarExpedientesReturn() {
        return archivarExpedientesReturn;
    }

    /**
     * Define el valor de la propiedad archivarExpedientesReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link RetornoServicio }
     *     
     */
    public void setArchivarExpedientesReturn(RetornoServicio value) {
        this.archivarExpedientesReturn = value;
    }

}
