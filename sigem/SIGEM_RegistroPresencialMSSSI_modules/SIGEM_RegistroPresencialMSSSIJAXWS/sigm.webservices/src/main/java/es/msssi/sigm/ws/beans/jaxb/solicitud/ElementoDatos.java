/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/


package es.msssi.sigm.ws.beans.jaxb.solicitud;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for elementoDatos complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="elementoDatos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="oficina" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="destino" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="interesados" type="{http://www.msssi.es/Regtel/2015/1}elementoDatosInteresados" minOccurs="0"/>
 *         &lt;element name="numeroRegistroOriginal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaRegistroOriginal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoRegistroOriginal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoAsunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resumen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="datosExtendidos" type="{http://www.msssi.es/Regtel/2015/1}elementoDatosExtendidos" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "elementoDatos", namespace = "http://www.msssi.es/Regtel/2015/1", propOrder = {
    "tipoRegistro",
    "oficina",
    "origen",
    "destino",
    "interesados",
    "numeroRegistroOriginal",
    "fechaRegistroOriginal",
    "tipoRegistroOriginal",
    "tipoTransporte",
    "numeroTransporte",
    "tipoAsunto",
    "resumen",
    "refExpediente",
    "datosExtendidos"
})
public class ElementoDatos {

    @XmlElement(required = true)
    protected String tipoRegistro;
    protected String oficina;
    @XmlElement(required = true)
    protected String origen;
	@XmlElement(required = false)
    protected String destino;
	@XmlElement(required = false)
    protected ElementoDatosInteresados interesados;
	@XmlElement(required = false)
    protected String numeroRegistroOriginal;
	@XmlElement(required = false)
    protected String fechaRegistroOriginal;
	@XmlElement(required = false)
    protected String tipoRegistroOriginal;
	@XmlElement(required = false)
    protected String tipoTransporte;
	@XmlElement(required = false)
    protected String numeroTransporte;
	@XmlElement(required = false)
    protected String tipoAsunto;
	@XmlElement(required = false)
    protected String resumen;
	@XmlElement(required = false)
    protected String refExpediente;
	@XmlElement(required = false)
    protected ElementoDatosExtendidos datosExtendidos;

    /**
     * Gets the value of the tipoRegistro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * Sets the value of the tipoRegistro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegistro(String value) {
        this.tipoRegistro = value;
    }

    /**
     * Gets the value of the oficina property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficina() {
        return oficina;
    }

    /**
     * Sets the value of the oficina property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficina(String value) {
        this.oficina = value;
    }

    /**
     * Gets the value of the origen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Sets the value of the origen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigen(String value) {
        this.origen = value;
    }

    /**
     * Gets the value of the destino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Sets the value of the destino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestino(String value) {
        this.destino = value;
    }

    /**
     * Gets the value of the interesados property.
     * 
     * @return
     *     possible object is
     *     {@link ElementoDatosInteresados }
     *     
     */
    public ElementoDatosInteresados getInteresados() {
        return interesados;
    }

    /**
     * Sets the value of the interesados property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementoDatosInteresados }
     *     
     */
    public void setInteresados(ElementoDatosInteresados value) {
        this.interesados = value;
    }

    /**
     * Gets the value of the numeroRegistroOriginal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistroOriginal() {
        return numeroRegistroOriginal;
    }

    /**
     * Sets the value of the numeroRegistroOriginal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistroOriginal(String value) {
        this.numeroRegistroOriginal = value;
    }

    /**
     * Gets the value of the fechaRegistroOriginal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaRegistroOriginal() {
        return fechaRegistroOriginal;
    }

    /**
     * Sets the value of the fechaRegistroOriginal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaRegistroOriginal(String value) {
        this.fechaRegistroOriginal = value;
    }

    /**
     * Gets the value of the tipoRegistroOriginal property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoRegistroOriginal() {
        return tipoRegistroOriginal;
    }

    /**
     * Sets the value of the tipoRegistroOriginal property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoRegistroOriginal(String value) {
        this.tipoRegistroOriginal = value;
    }

    /**
     * Gets the value of the tipoTransporte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTransporte() {
        return tipoTransporte;
    }

    /**
     * Sets the value of the tipoTransporte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTransporte(String value) {
        this.tipoTransporte = value;
    }

    /**
     * Gets the value of the numeroTransporte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    /**
     * Sets the value of the numeroTransporte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTransporte(String value) {
        this.numeroTransporte = value;
    }

    /**
     * Gets the value of the tipoAsunto property.
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
     * Sets the value of the tipoAsunto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoAsunto(String value) {
        this.tipoAsunto = value;
    }

    /**
     * Gets the value of the resumen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResumen() {
        return resumen;
    }

    /**
     * Sets the value of the resumen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResumen(String value) {
        this.resumen = value;
    }

    /**
     * Gets the value of the refExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefExpediente() {
        return refExpediente;
    }

    /**
     * Sets the value of the refExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefExpediente(String value) {
        this.refExpediente = value;
    }

    /**
     * Gets the value of the datosExtendidos property.
     * 
     * @return
     *     possible object is
     *     {@link ElementoDatosExtendidos }
     *     
     */
    public ElementoDatosExtendidos getDatosExtendidos() {
        return datosExtendidos;
    }

    /**
     * Sets the value of the datosExtendidos property.
     * 
     * @param value
     *     allowed object is
     *     {@link ElementoDatosExtendidos }
     *     
     */
    public void setDatosExtendidos(ElementoDatosExtendidos value) {
        this.datosExtendidos = value;
    }

}
