
package ieci.tecdoc.sgm.tram.ws.server.sigem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="idProc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaIni" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="fechaFin" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="tipoOrd" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "idProc",
    "fechaIni",
    "fechaFin",
    "tipoOrd"
})
@XmlRootElement(name = "getIdsExpedientes")
public class GetIdsExpedientes {

    @XmlElement(required = true)
    protected String idEntidad;
    @XmlElement(required = true)
    protected String idProc;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaIni;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaFin;
    protected int tipoOrd;

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
     * Obtiene el valor de la propiedad idProc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdProc() {
        return idProc;
    }

    /**
     * Define el valor de la propiedad idProc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdProc(String value) {
        this.idProc = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaIni.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaIni() {
        return fechaIni;
    }

    /**
     * Define el valor de la propiedad fechaIni.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaIni(XMLGregorianCalendar value) {
        this.fechaIni = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFin.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaFin() {
        return fechaFin;
    }

    /**
     * Define el valor de la propiedad fechaFin.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaFin(XMLGregorianCalendar value) {
        this.fechaFin = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoOrd.
     * 
     */
    public int getTipoOrd() {
        return tipoOrd;
    }

    /**
     * Define el valor de la propiedad tipoOrd.
     * 
     */
    public void setTipoOrd(int value) {
        this.tipoOrd = value;
    }

}
