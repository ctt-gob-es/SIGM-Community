
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
 *         &lt;element name="numExp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="estadoAdm" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "numExp",
    "estadoAdm"
})
@XmlRootElement(name = "cambiarEstadoAdministrativo")
public class CambiarEstadoAdministrativo {

    @XmlElement(required = true)
    protected String idEntidad;
    @XmlElement(required = true)
    protected String numExp;
    @XmlElement(required = true)
    protected String estadoAdm;

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
     * Obtiene el valor de la propiedad estadoAdm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstadoAdm() {
        return estadoAdm;
    }

    /**
     * Define el valor de la propiedad estadoAdm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstadoAdm(String value) {
        this.estadoAdm = value;
    }

}
