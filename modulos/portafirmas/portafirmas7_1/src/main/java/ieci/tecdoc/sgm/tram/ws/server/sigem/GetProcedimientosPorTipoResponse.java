
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
 *         &lt;element name="getProcedimientosPorTipoReturn" type="{}ListaInfoBProcedimientos"/>
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
    "getProcedimientosPorTipoReturn"
})
@XmlRootElement(name = "getProcedimientosPorTipoResponse")
public class GetProcedimientosPorTipoResponse {

    @XmlElement(required = true)
    protected ListaInfoBProcedimientos getProcedimientosPorTipoReturn;

    /**
     * Obtiene el valor de la propiedad getProcedimientosPorTipoReturn.
     * 
     * @return
     *     possible object is
     *     {@link ListaInfoBProcedimientos }
     *     
     */
    public ListaInfoBProcedimientos getGetProcedimientosPorTipoReturn() {
        return getProcedimientosPorTipoReturn;
    }

    /**
     * Define el valor de la propiedad getProcedimientosPorTipoReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link ListaInfoBProcedimientos }
     *     
     */
    public void setGetProcedimientosPorTipoReturn(ListaInfoBProcedimientos value) {
        this.getProcedimientosPorTipoReturn = value;
    }

}
