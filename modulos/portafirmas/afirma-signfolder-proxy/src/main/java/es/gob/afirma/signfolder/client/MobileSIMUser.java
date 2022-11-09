
package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para mobileSIMUser complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="mobileSIMUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Plataforma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdDispositivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mobileSIMUser", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
    "idRegistro",
    "plataforma",
    "idDispositivo"
})
public class MobileSIMUser {

    @XmlElement(name = "IdRegistro")
    protected String idRegistro;
    @XmlElement(name = "Plataforma")
    protected String plataforma;
    @XmlElement(name = "IdDispositivo")
    protected String idDispositivo;

    /**
     * Obtiene el valor de la propiedad idRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdRegistro() {
        return idRegistro;
    }

    /**
     * Define el valor de la propiedad idRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdRegistro(String value) {
        this.idRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad plataforma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlataforma() {
        return plataforma;
    }

    /**
     * Define el valor de la propiedad plataforma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlataforma(String value) {
        this.plataforma = value;
    }

    /**
     * Obtiene el valor de la propiedad idDispositivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDispositivo() {
        return idDispositivo;
    }

    /**
     * Define el valor de la propiedad idDispositivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDispositivo(String value) {
        this.idDispositivo = value;
    }

}
