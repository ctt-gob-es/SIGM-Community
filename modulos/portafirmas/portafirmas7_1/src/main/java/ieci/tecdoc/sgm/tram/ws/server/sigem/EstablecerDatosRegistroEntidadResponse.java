
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Entero;


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
 *         &lt;element name="establecerDatosRegistroEntidadReturn" type="{}Entero"/>
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
    "establecerDatosRegistroEntidadReturn"
})
@XmlRootElement(name = "establecerDatosRegistroEntidadResponse")
public class EstablecerDatosRegistroEntidadResponse {

    @XmlElement(required = true)
    protected Entero establecerDatosRegistroEntidadReturn;

    /**
     * Obtiene el valor de la propiedad establecerDatosRegistroEntidadReturn.
     * 
     * @return
     *     possible object is
     *     {@link Entero }
     *     
     */
    public Entero getEstablecerDatosRegistroEntidadReturn() {
        return establecerDatosRegistroEntidadReturn;
    }

    /**
     * Define el valor de la propiedad establecerDatosRegistroEntidadReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Entero }
     *     
     */
    public void setEstablecerDatosRegistroEntidadReturn(Entero value) {
        this.establecerDatosRegistroEntidadReturn = value;
    }

}
