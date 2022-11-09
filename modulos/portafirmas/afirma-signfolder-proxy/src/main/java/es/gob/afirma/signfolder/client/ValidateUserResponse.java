
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
 *         &lt;element name="nifCif" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "nifCif"
})
@XmlRootElement(name = "validateUserResponse")
public class ValidateUserResponse {

    @XmlElement(required = true)
    protected String nifCif;

    /**
     * Obtiene el valor de la propiedad nifCif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifCif() {
        return nifCif;
    }

    /**
     * Define el valor de la propiedad nifCif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifCif(String value) {
        this.nifCif = value;
    }

}
