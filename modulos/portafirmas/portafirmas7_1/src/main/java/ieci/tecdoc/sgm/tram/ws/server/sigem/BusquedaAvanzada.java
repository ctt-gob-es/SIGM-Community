
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
 *         &lt;element name="nombreGrupo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreFrmBusqueda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="xmlBusqueda" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="dominio" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "nombreGrupo",
    "nombreFrmBusqueda",
    "xmlBusqueda",
    "dominio"
})
@XmlRootElement(name = "busquedaAvanzada")
public class BusquedaAvanzada {

    @XmlElement(required = true)
    protected String idEntidad;
    @XmlElement(required = true)
    protected String nombreGrupo;
    @XmlElement(required = true)
    protected String nombreFrmBusqueda;
    @XmlElement(required = true)
    protected String xmlBusqueda;
    protected int dominio;

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
     * Obtiene el valor de la propiedad nombreGrupo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreGrupo() {
        return nombreGrupo;
    }

    /**
     * Define el valor de la propiedad nombreGrupo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreGrupo(String value) {
        this.nombreGrupo = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreFrmBusqueda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFrmBusqueda() {
        return nombreFrmBusqueda;
    }

    /**
     * Define el valor de la propiedad nombreFrmBusqueda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFrmBusqueda(String value) {
        this.nombreFrmBusqueda = value;
    }

    /**
     * Obtiene el valor de la propiedad xmlBusqueda.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlBusqueda() {
        return xmlBusqueda;
    }

    /**
     * Define el valor de la propiedad xmlBusqueda.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlBusqueda(String value) {
        this.xmlBusqueda = value;
    }

    /**
     * Obtiene el valor de la propiedad dominio.
     * 
     */
    public int getDominio() {
        return dominio;
    }

    /**
     * Define el valor de la propiedad dominio.
     * 
     */
    public void setDominio(int value) {
        this.dominio = value;
    }

}
