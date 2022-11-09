
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
 *         &lt;element name="anexarDocsTramiteReturn" type="{}Booleano"/>
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
    "anexarDocsTramiteReturn"
})
@XmlRootElement(name = "anexarDocsTramiteResponse")
public class AnexarDocsTramiteResponse {

    @XmlElement(required = true)
    protected Booleano anexarDocsTramiteReturn;

    /**
     * Obtiene el valor de la propiedad anexarDocsTramiteReturn.
     * 
     * @return
     *     possible object is
     *     {@link Booleano }
     *     
     */
    public Booleano getAnexarDocsTramiteReturn() {
        return anexarDocsTramiteReturn;
    }

    /**
     * Define el valor de la propiedad anexarDocsTramiteReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Booleano }
     *     
     */
    public void setAnexarDocsTramiteReturn(Booleano value) {
        this.anexarDocsTramiteReturn = value;
    }

}
