
package ieci.tecdoc.sgm.tram.ws.server.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import ieci.tecdoc.sgm.core.services.dto.RetornoServicio;
import ieci.tecdoc.sgm.tram.ws.server.sigem.ArrayOfTns1Firma;


/**
 * <p>Clase Java para InfoFichero complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="InfoFichero">
 *   &lt;complexContent>
 *     &lt;extension base="{}RetornoServicio">
 *       &lt;sequence>
 *         &lt;element name="fechaAlta" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="firmas" type="{}ArrayOf_tns1_Firma"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InfoFichero", propOrder = {
    "fechaAlta",
    "firmas",
    "nombre"
})
public class InfoFichero
    extends RetornoServicio
{

    @XmlElement(required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAlta;
    @XmlElement(required = true, nillable = true)
    protected ArrayOfTns1Firma firmas;
    @XmlElement(required = true, nillable = true)
    protected String nombre;

    /**
     * Obtiene el valor de la propiedad fechaAlta.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Define el valor de la propiedad fechaAlta.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAlta(XMLGregorianCalendar value) {
        this.fechaAlta = value;
    }

    /**
     * Obtiene el valor de la propiedad firmas.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfTns1Firma }
     *     
     */
    public ArrayOfTns1Firma getFirmas() {
        return firmas;
    }

    /**
     * Define el valor de la propiedad firmas.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfTns1Firma }
     *     
     */
    public void setFirmas(ArrayOfTns1Firma value) {
        this.firmas = value;
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

}
