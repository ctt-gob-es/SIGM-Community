
package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="samlresponse" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="remoteHost" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "samlresponse",
    "remoteHost"
})
@XmlRootElement(name = "procesarRespuestaClave")
public class ProcesarRespuestaClave {

    @XmlElement(required = true)
    protected String samlresponse;
    @XmlElement(required = true)
    protected String remoteHost;

    /**
     * Obtiene el valor de la propiedad samlresponse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSamlresponse() {
        return samlresponse;
    }

    /**
     * Define el valor de la propiedad samlresponse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSamlresponse(String value) {
        this.samlresponse = value;
    }

    /**
     * Obtiene el valor de la propiedad remoteHost.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRemoteHost() {
        return remoteHost;
    }

    /**
     * Define el valor de la propiedad remoteHost.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRemoteHost(String value) {
        this.remoteHost = value;
    }

}
