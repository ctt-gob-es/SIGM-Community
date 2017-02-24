
package es.msssi.tecdoc.fwktd.dir.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for datosUnidadOrganica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="datosUnidadOrganica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="descripcionEstado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionNivelAdministracion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaAltaOficial" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaAnulacion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaBajaOficial" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaExtincion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idExternoFuente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUnidadOrganicaEntidadDerechoPublico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUnidadOrganicaPrincipal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idUnidadOrganicaSuperior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="indicadorEntidadDerechoPublico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nivelAdministracion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nivelJerarquico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUnidadOrganicaEntidadDerechoPublico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUnidadOrganicaPrincipal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUnidadOrganicaSuperior" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datosUnidadOrganica", propOrder = {
    "descripcionEstado",
    "descripcionNivelAdministracion",
    "estado",
    "fechaAltaOficial",
    "fechaAnulacion",
    "fechaBajaOficial",
    "fechaExtincion",
    "id",
    "idExternoFuente",
    "idUnidadOrganicaEntidadDerechoPublico",
    "idUnidadOrganicaPrincipal",
    "idUnidadOrganicaSuperior",
    "indicadorEntidadDerechoPublico",
    "nivelAdministracion",
    "nivelJerarquico",
    "nombre",
    "nombreUnidadOrganicaEntidadDerechoPublico",
    "nombreUnidadOrganicaPrincipal",
    "nombreUnidadOrganicaSuperior"
})
public class DatosUnidadOrganica {

    protected String descripcionEstado;
    protected String descripcionNivelAdministracion;
    protected String estado;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAltaOficial;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaAnulacion;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaBajaOficial;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaExtincion;
    protected String id;
    protected String idExternoFuente;
    protected String idUnidadOrganicaEntidadDerechoPublico;
    protected String idUnidadOrganicaPrincipal;
    protected String idUnidadOrganicaSuperior;
    protected String indicadorEntidadDerechoPublico;
    protected String nivelAdministracion;
    protected Integer nivelJerarquico;
    protected String nombre;
    protected String nombreUnidadOrganicaEntidadDerechoPublico;
    protected String nombreUnidadOrganicaPrincipal;
    protected String nombreUnidadOrganicaSuperior;

    /**
     * Gets the value of the descripcionEstado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    /**
     * Sets the value of the descripcionEstado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionEstado(String value) {
        this.descripcionEstado = value;
    }

    /**
     * Gets the value of the descripcionNivelAdministracion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionNivelAdministracion() {
        return descripcionNivelAdministracion;
    }

    /**
     * Sets the value of the descripcionNivelAdministracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionNivelAdministracion(String value) {
        this.descripcionNivelAdministracion = value;
    }

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstado(String value) {
        this.estado = value;
    }

    /**
     * Gets the value of the fechaAltaOficial property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAltaOficial() {
        return fechaAltaOficial;
    }

    /**
     * Sets the value of the fechaAltaOficial property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAltaOficial(XMLGregorianCalendar value) {
        this.fechaAltaOficial = value;
    }

    /**
     * Gets the value of the fechaAnulacion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaAnulacion() {
        return fechaAnulacion;
    }

    /**
     * Sets the value of the fechaAnulacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAnulacion(XMLGregorianCalendar value) {
        this.fechaAnulacion = value;
    }

    /**
     * Gets the value of the fechaBajaOficial property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaBajaOficial() {
        return fechaBajaOficial;
    }

    /**
     * Sets the value of the fechaBajaOficial property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaBajaOficial(XMLGregorianCalendar value) {
        this.fechaBajaOficial = value;
    }

    /**
     * Gets the value of the fechaExtincion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaExtincion() {
        return fechaExtincion;
    }

    /**
     * Sets the value of the fechaExtincion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaExtincion(XMLGregorianCalendar value) {
        this.fechaExtincion = value;
    }

    /**
     * Gets the value of the id property.
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
     * Sets the value of the id property.
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
     * Gets the value of the idExternoFuente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdExternoFuente() {
        return idExternoFuente;
    }

    /**
     * Sets the value of the idExternoFuente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdExternoFuente(String value) {
        this.idExternoFuente = value;
    }

    /**
     * Gets the value of the idUnidadOrganicaEntidadDerechoPublico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnidadOrganicaEntidadDerechoPublico() {
        return idUnidadOrganicaEntidadDerechoPublico;
    }

    /**
     * Sets the value of the idUnidadOrganicaEntidadDerechoPublico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnidadOrganicaEntidadDerechoPublico(String value) {
        this.idUnidadOrganicaEntidadDerechoPublico = value;
    }

    /**
     * Gets the value of the idUnidadOrganicaPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnidadOrganicaPrincipal() {
        return idUnidadOrganicaPrincipal;
    }

    /**
     * Sets the value of the idUnidadOrganicaPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnidadOrganicaPrincipal(String value) {
        this.idUnidadOrganicaPrincipal = value;
    }

    /**
     * Gets the value of the idUnidadOrganicaSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdUnidadOrganicaSuperior() {
        return idUnidadOrganicaSuperior;
    }

    /**
     * Sets the value of the idUnidadOrganicaSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdUnidadOrganicaSuperior(String value) {
        this.idUnidadOrganicaSuperior = value;
    }

    /**
     * Gets the value of the indicadorEntidadDerechoPublico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicadorEntidadDerechoPublico() {
        return indicadorEntidadDerechoPublico;
    }

    /**
     * Sets the value of the indicadorEntidadDerechoPublico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicadorEntidadDerechoPublico(String value) {
        this.indicadorEntidadDerechoPublico = value;
    }

    /**
     * Gets the value of the nivelAdministracion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNivelAdministracion() {
        return nivelAdministracion;
    }

    /**
     * Sets the value of the nivelAdministracion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNivelAdministracion(String value) {
        this.nivelAdministracion = value;
    }

    /**
     * Gets the value of the nivelJerarquico property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNivelJerarquico() {
        return nivelJerarquico;
    }

    /**
     * Sets the value of the nivelJerarquico property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNivelJerarquico(Integer value) {
        this.nivelJerarquico = value;
    }

    /**
     * Gets the value of the nombre property.
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
     * Sets the value of the nombre property.
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
     * Gets the value of the nombreUnidadOrganicaEntidadDerechoPublico property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreUnidadOrganicaEntidadDerechoPublico() {
        return nombreUnidadOrganicaEntidadDerechoPublico;
    }

    /**
     * Sets the value of the nombreUnidadOrganicaEntidadDerechoPublico property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreUnidadOrganicaEntidadDerechoPublico(String value) {
        this.nombreUnidadOrganicaEntidadDerechoPublico = value;
    }

    /**
     * Gets the value of the nombreUnidadOrganicaPrincipal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreUnidadOrganicaPrincipal() {
        return nombreUnidadOrganicaPrincipal;
    }

    /**
     * Sets the value of the nombreUnidadOrganicaPrincipal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreUnidadOrganicaPrincipal(String value) {
        this.nombreUnidadOrganicaPrincipal = value;
    }

    /**
     * Gets the value of the nombreUnidadOrganicaSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreUnidadOrganicaSuperior() {
        return nombreUnidadOrganicaSuperior;
    }

    /**
     * Sets the value of the nombreUnidadOrganicaSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreUnidadOrganicaSuperior(String value) {
        this.nombreUnidadOrganicaSuperior = value;
    }

}
