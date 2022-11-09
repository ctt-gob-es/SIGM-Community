
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DocElectronico complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocElectronico">
 *   &lt;complexContent>
 *     &lt;extension base="{}DocFisico">
 *       &lt;sequence>
 *         &lt;element name="extension" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="localizador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="repositorio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocElectronico", propOrder = {
    "extension",
    "localizador",
    "repositorio"
})
public class DocElectronico
    extends DocFisico
{

    @XmlElement(required = true, nillable = true)
    protected String extension;
    @XmlElement(required = true, nillable = true)
    protected String localizador;
    @XmlElement(required = true, nillable = true)
    protected String repositorio;

    /**
     * Obtiene el valor de la propiedad extension.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Define el valor de la propiedad extension.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension(String value) {
        this.extension = value;
    }

    /**
     * Obtiene el valor de la propiedad localizador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalizador() {
        return localizador;
    }

    /**
     * Define el valor de la propiedad localizador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalizador(String value) {
        this.localizador = value;
    }

    /**
     * Obtiene el valor de la propiedad repositorio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositorio() {
        return repositorio;
    }

    /**
     * Define el valor de la propiedad repositorio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositorio(String value) {
        this.repositorio = value;
    }

}
