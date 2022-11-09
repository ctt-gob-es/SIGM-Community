
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para InfoBExpediente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="InfoBExpediente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="datosIdentificativos" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numExp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoBExpediente", propOrder = {
    "datosIdentificativos",
    "id",
    "numExp"
})
public class InfoBExpediente {

    @XmlElement(required = true, nillable = true)
    protected String datosIdentificativos;
    @XmlElement(required = true, nillable = true)
    protected String id;
    @XmlElement(required = true, nillable = true)
    protected String numExp;

    /**
     * Obtiene el valor de la propiedad datosIdentificativos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatosIdentificativos() {
        return datosIdentificativos;
    }

    /**
     * Define el valor de la propiedad datosIdentificativos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatosIdentificativos(String value) {
        this.datosIdentificativos = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad numExp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumExp() {
        return numExp;
    }

    /**
     * Define el valor de la propiedad numExp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumExp(String value) {
        this.numExp = value;
    }

}
