
package ieci.tecdoc.sgm.tram.ws.server.sigem;

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
 *         &lt;element name="idEntidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreEntidad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numExp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="xmlDatosEspecificos" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "idEntidad",
    "nombreEntidad",
    "numExp",
    "xmlDatosEspecificos"
})
@XmlRootElement(name = "establecerDatosRegistroEntidad")
public class EstablecerDatosRegistroEntidad {

    @XmlElement(required = true)
    protected String idEntidad;
    @XmlElement(required = true)
    protected String nombreEntidad;
    @XmlElement(required = true)
    protected String numExp;
    @XmlElement(required = true)
    protected String xmlDatosEspecificos;

    /**
     * Obtiene el valor de la propiedad idEntidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEntidad() {
        return idEntidad;
    }

    /**
     * Define el valor de la propiedad idEntidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEntidad(String value) {
        this.idEntidad = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreEntidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreEntidad() {
        return nombreEntidad;
    }

    /**
     * Define el valor de la propiedad nombreEntidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreEntidad(String value) {
        this.nombreEntidad = value;
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

    /**
     * Obtiene el valor de la propiedad xmlDatosEspecificos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlDatosEspecificos() {
        return xmlDatosEspecificos;
    }

    /**
     * Define el valor de la propiedad xmlDatosEspecificos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlDatosEspecificos(String value) {
        this.xmlDatosEspecificos = value;
    }

}
