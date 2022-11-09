
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import ieci.tecdoc.sgm.tram.ws.server.dto.Binario;


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
 *         &lt;element name="getJustificanteFirmaReturn" type="{}Binario"/>
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
    "getJustificanteFirmaReturn"
})
@XmlRootElement(name = "getJustificanteFirmaResponse")
public class GetJustificanteFirmaResponse {

    @XmlElement(required = true)
    protected Binario getJustificanteFirmaReturn;

    /**
     * Obtiene el valor de la propiedad getJustificanteFirmaReturn.
     * 
     * @return
     *     possible object is
     *     {@link Binario }
     *     
     */
    public Binario getGetJustificanteFirmaReturn() {
        return getJustificanteFirmaReturn;
    }

    /**
     * Define el valor de la propiedad getJustificanteFirmaReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link Binario }
     *     
     */
    public void setGetJustificanteFirmaReturn(Binario value) {
        this.getJustificanteFirmaReturn = value;
    }

}
