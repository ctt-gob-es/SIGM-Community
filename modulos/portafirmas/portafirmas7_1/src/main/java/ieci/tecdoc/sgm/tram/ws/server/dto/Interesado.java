
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Interesado complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Interesado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idEnTerceros" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="interesadoPrincipal" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numIdentidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rol" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Interesado", propOrder = {
    "idEnTerceros",
    "interesadoPrincipal",
    "nombre",
    "numIdentidad",
    "rol"
})
public class Interesado {

    @XmlElement(required = true, nillable = true)
    protected String idEnTerceros;
    protected boolean interesadoPrincipal;
    @XmlElement(required = true, nillable = true)
    protected String nombre;
    @XmlElement(required = true, nillable = true)
    protected String numIdentidad;
    @XmlElement(required = true, nillable = true)
    protected String rol;

    /**
     * Obtiene el valor de la propiedad idEnTerceros.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEnTerceros() {
        return idEnTerceros;
    }

    /**
     * Define el valor de la propiedad idEnTerceros.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEnTerceros(String value) {
        this.idEnTerceros = value;
    }

    /**
     * Obtiene el valor de la propiedad interesadoPrincipal.
     * 
     */
    public boolean isInteresadoPrincipal() {
        return interesadoPrincipal;
    }

    /**
     * Define el valor de la propiedad interesadoPrincipal.
     * 
     */
    public void setInteresadoPrincipal(boolean value) {
        this.interesadoPrincipal = value;
    }

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad numIdentidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumIdentidad() {
        return numIdentidad;
    }

    /**
     * Define el valor de la propiedad numIdentidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumIdentidad(String value) {
        this.numIdentidad = value;
    }

    /**
     * Obtiene el valor de la propiedad rol.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRol() {
        return rol;
    }

    /**
     * Define el valor de la propiedad rol.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRol(String value) {
        this.rol = value;
    }

}
