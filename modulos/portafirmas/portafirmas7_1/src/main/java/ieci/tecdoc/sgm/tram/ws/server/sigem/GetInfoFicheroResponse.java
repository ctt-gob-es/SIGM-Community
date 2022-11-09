
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.InfoFichero;


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
 *         &lt;element name="getInfoFicheroReturn" type="{}InfoFichero"/>
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
    "getInfoFicheroReturn"
})
@XmlRootElement(name = "getInfoFicheroResponse")
public class GetInfoFicheroResponse {

    @XmlElement(required = true)
    protected InfoFichero getInfoFicheroReturn;

    /**
     * Obtiene el valor de la propiedad getInfoFicheroReturn.
     * 
     * @return
     *     possible object is
     *     {@link InfoFichero }
     *     
     */
    public InfoFichero getGetInfoFicheroReturn() {
        return getInfoFicheroReturn;
    }

    /**
     * Define el valor de la propiedad getInfoFicheroReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoFichero }
     *     
     */
    public void setGetInfoFicheroReturn(InfoFichero value) {
        this.getInfoFicheroReturn = value;
    }

}
