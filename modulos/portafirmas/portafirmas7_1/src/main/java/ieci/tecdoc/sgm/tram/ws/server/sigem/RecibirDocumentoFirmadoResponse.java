
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Cadena;


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
 *         &lt;element name="recibirDocumentoFirmadoReturn" type="{}Cadena"/>
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
    "recibirDocumentoFirmadoReturn"
})
@XmlRootElement(name = "recibirDocumentoFirmadoResponse")
public class RecibirDocumentoFirmadoResponse {

    @XmlElement(required = true)
    protected Cadena recibirDocumentoFirmadoReturn;

    /**
     * Obtiene el valor de la propiedad recibirDocumentoFirmadoReturn.
     * 
     * @return
     *     possible object is
     *     {@link Cadena }
     *     
     */
    public Cadena getRecibirDocumentoFirmadoReturn() {
        return recibirDocumentoFirmadoReturn;
    }

    /**
     * Define el valor de la propiedad recibirDocumentoFirmadoReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Cadena }
     *     
     */
    public void setRecibirDocumentoFirmadoReturn(Cadena value) {
        this.recibirDocumentoFirmadoReturn = value;
    }

}
