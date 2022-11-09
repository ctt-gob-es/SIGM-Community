
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Emplazamiento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Emplazamiento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="comunidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="concejo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localizacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="pais" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="poblacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Emplazamiento", propOrder = {
    "comunidad",
    "concejo",
    "localizacion",
    "pais",
    "poblacion"
})
public class Emplazamiento {

    @XmlElement(required = true, nillable = true)
    protected String comunidad;
    @XmlElement(required = true, nillable = true)
    protected String concejo;
    @XmlElement(required = true, nillable = true)
    protected String localizacion;
    @XmlElement(required = true, nillable = true)
    protected String pais;
    @XmlElement(required = true, nillable = true)
    protected String poblacion;

    /**
     * Obtiene el valor de la propiedad comunidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComunidad() {
        return comunidad;
    }

    /**
     * Define el valor de la propiedad comunidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComunidad(String value) {
        this.comunidad = value;
    }

    /**
     * Obtiene el valor de la propiedad concejo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConcejo() {
        return concejo;
    }

    /**
     * Define el valor de la propiedad concejo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConcejo(String value) {
        this.concejo = value;
    }

    /**
     * Obtiene el valor de la propiedad localizacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalizacion() {
        return localizacion;
    }

    /**
     * Define el valor de la propiedad localizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalizacion(String value) {
        this.localizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define el valor de la propiedad pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtiene el valor de la propiedad poblacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPoblacion() {
        return poblacion;
    }

    /**
     * Define el valor de la propiedad poblacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPoblacion(String value) {
        this.poblacion = value;
    }

}
