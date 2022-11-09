
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Booleano;


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
 *         &lt;element name="cambiarEstadoAdministrativoReturn" type="{}Booleano"/>
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
    "cambiarEstadoAdministrativoReturn"
})
@XmlRootElement(name = "cambiarEstadoAdministrativoResponse")
public class CambiarEstadoAdministrativoResponse {

    @XmlElement(required = true)
    protected Booleano cambiarEstadoAdministrativoReturn;

    /**
     * Obtiene el valor de la propiedad cambiarEstadoAdministrativoReturn.
     * 
     * @return
     *     possible object is
     *     {@link Booleano }
     *     
     */
    public Booleano getCambiarEstadoAdministrativoReturn() {
        return cambiarEstadoAdministrativoReturn;
    }

    /**
     * Define el valor de la propiedad cambiarEstadoAdministrativoReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Booleano }
     *     
     */
    public void setCambiarEstadoAdministrativoReturn(Booleano value) {
        this.cambiarEstadoAdministrativoReturn = value;
    }

}
