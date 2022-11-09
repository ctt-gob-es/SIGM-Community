
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Procedimiento;


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
 *         &lt;element name="getProcedimientoReturn" type="{}Procedimiento"/>
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
    "getProcedimientoReturn"
})
@XmlRootElement(name = "getProcedimientoResponse")
public class GetProcedimientoResponse {

    @XmlElement(required = true)
    protected Procedimiento getProcedimientoReturn;

    /**
     * Obtiene el valor de la propiedad getProcedimientoReturn.
     * 
     * @return
     *     possible object is
     *     {@link Procedimiento }
     *     
     */
    public Procedimiento getGetProcedimientoReturn() {
        return getProcedimientoReturn;
    }

    /**
     * Define el valor de la propiedad getProcedimientoReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Procedimiento }
     *     
     */
    public void setGetProcedimientoReturn(Procedimiento value) {
        this.getProcedimientoReturn = value;
    }

}
