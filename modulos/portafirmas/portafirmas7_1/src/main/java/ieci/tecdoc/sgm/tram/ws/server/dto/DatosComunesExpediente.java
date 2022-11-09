
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1InteresadoExpediente;


/**
 * <p>Clase Java para DatosComunesExpediente complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DatosComunesExpediente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fechaRegistro" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="idOrganismo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="interesados" type="{}ArrayOf_tns1_InteresadoExpediente"/>
 *         &lt;element name="numeroRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoAsunto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatosComunesExpediente", propOrder = {
    "fechaRegistro",
    "idOrganismo",
    "interesados",
    "numeroRegistro",
    "tipoAsunto"
})
public class DatosComunesExpediente {

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaRegistro;
    @XmlElement(required = true, nillable = true)
    protected String idOrganismo;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1InteresadoExpediente interesados;
    @XmlElement(required = true, nillable = true)
    protected String numeroRegistro;
    @XmlElement(required = true, nillable = true)
    protected String tipoAsunto;

    /**
     * Obtiene el valor de la propiedad fechaRegistro.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Define el valor de la propiedad fechaRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaRegistro(XMLGregorianCalendar value) {
        this.fechaRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad idOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdOrganismo() {
        return idOrganismo;
    }

    /**
     * Define el valor de la propiedad idOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdOrganismo(String value) {
        this.idOrganismo = value;
    }

    /**
     * Obtiene el valor de la propiedad interesados.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1InteresadoExpediente }
     *     
     */
    public ArrayOfTns1InteresadoExpediente getInteresados() {
        return interesados;
    }

    /**
     * Define el valor de la propiedad interesados.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1InteresadoExpediente }
     *     
     */
    public void setInteresados(ArrayOfTns1InteresadoExpediente value) {
        this.interesados = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * Define el valor de la propiedad numeroRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistro(String value) {
        this.numeroRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoAsunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoAsunto() {
        return tipoAsunto;
    }

    /**
     * Define el valor de la propiedad tipoAsunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoAsunto(String value) {
        this.tipoAsunto = value;
    }

}
