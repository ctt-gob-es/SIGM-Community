
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.ListaInfoBProcedimientos;


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
 *         &lt;element name="getProcedimientosReturn" type="{}ListaInfoBProcedimientos"/>
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
    "getProcedimientosReturn"
})
@XmlRootElement(name = "getProcedimientosResponse")
public class GetProcedimientosResponse {

    @XmlElement(required = true)
    protected ListaInfoBProcedimientos getProcedimientosReturn;

    /**
     * Obtiene el valor de la propiedad getProcedimientosReturn.
     * 
     * @return
     *     possible object is
     *     {@link ListaInfoBProcedimientos }
     *     
     */
    public ListaInfoBProcedimientos getGetProcedimientosReturn() {
        return getProcedimientosReturn;
    }

    /**
     * Define el valor de la propiedad getProcedimientosReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaInfoBProcedimientos }
     *     
     */
    public void setGetProcedimientosReturn(ListaInfoBProcedimientos value) {
        this.getProcedimientosReturn = value;
    }

}
