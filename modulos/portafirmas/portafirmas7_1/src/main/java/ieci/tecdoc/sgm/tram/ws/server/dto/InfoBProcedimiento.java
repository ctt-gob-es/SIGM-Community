
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para InfoBProcedimiento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="InfoBProcedimiento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codSistProductor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreSistProductor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoBProcedimiento", propOrder = {
    "codSistProductor",
    "codigo",
    "id",
    "nombre",
    "nombreSistProductor"
})
public class InfoBProcedimiento {

    @XmlElement(required = true, nillable = true)
    protected String codSistProductor;
    @XmlElement(required = true, nillable = true)
    protected String codigo;
    @XmlElement(required = true, nillable = true)
    protected String id;
    @XmlElement(required = true, nillable = true)
    protected String nombre;
    @XmlElement(required = true, nillable = true)
    protected String nombreSistProductor;

    /**
     * Obtiene el valor de la propiedad codSistProductor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodSistProductor() {
        return codSistProductor;
    }

    /**
     * Define el valor de la propiedad codSistProductor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodSistProductor(String value) {
        this.codSistProductor = value;
    }

    /**
     * Obtiene el valor de la propiedad codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define el valor de la propiedad codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
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
     * Obtiene el valor de la propiedad nombreSistProductor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSistProductor() {
        return nombreSistProductor;
    }

    /**
     * Define el valor de la propiedad nombreSistProductor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSistProductor(String value) {
        this.nombreSistProductor = value;
    }

}
