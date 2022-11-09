
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="setFicheroTempReturn" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "setFicheroTempReturn"
})
@XmlRootElement(name = "setFicheroTempResponse")
public class SetFicheroTempResponse {

    protected boolean setFicheroTempReturn;

    /**
     * Obtiene el valor de la propiedad setFicheroTempReturn.
     * 
     */
    public boolean isSetFicheroTempReturn() {
        return setFicheroTempReturn;
    }

    /**
     * Define el valor de la propiedad setFicheroTempReturn.
     * 
     */
    public void setSetFicheroTempReturn(boolean value) {
        this.setFicheroTempReturn = value;
    }

}
