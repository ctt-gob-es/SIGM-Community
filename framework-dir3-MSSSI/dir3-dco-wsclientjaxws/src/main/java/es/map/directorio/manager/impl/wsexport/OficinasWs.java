/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.map.directorio.manager.impl.wsexport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OficinasWs complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OficinasWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clave" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formatoFichero" type="{http://impl.manager.directorio.map.es/wsExport}formatoFichero"/>
 *         &lt;element name="tipoConsulta" type="{http://impl.manager.directorio.map.es/wsExport}tipoConsultaOF"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nivelAdministracion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="estados" type="{http://impl.manager.directorio.map.es/wsExport}excluidos" minOccurs="0"/>
 *         &lt;element name="comunidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoUnidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="servicios" type="{http://impl.manager.directorio.map.es/wsExport}servicios" minOccurs="0"/>
 *         &lt;element name="fechaInicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaFin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OficinasWs", propOrder = { "usuario", "clave", "formatoFichero", "tipoConsulta",
    "codigo", "nivelAdministracion", "estados", "comunidad", "provincia", "codigoUnidad",
    "servicios", "fechaInicio", "fechaFin" })
public class OficinasWs {

    @XmlElement(required = true)
    protected String usuario;
    @XmlElement(required = true)
    protected String clave;
    @XmlElement(required = true)
    protected FormatoFichero formatoFichero;
    @XmlElement(required = true)
    protected TipoConsultaOF tipoConsulta;
    protected String codigo;
    protected Integer nivelAdministracion;
    protected Excluidos estados;
    protected String comunidad;
    protected String provincia;
    protected String codigoUnidad;
    protected Servicios servicios;
    protected String fechaInicio;
    protected String fechaFin;

    /**
     * Gets the value of the usuario property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getUsuario() {
	return usuario;
    }

    /**
     * Sets the value of the usuario property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setUsuario(
	String value) {
	this.usuario = value;
    }

    /**
     * Gets the value of the clave property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getClave() {
	return clave;
    }

    /**
     * Sets the value of the clave property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setClave(
	String value) {
	this.clave = value;
    }

    /**
     * Gets the value of the formatoFichero property.
     * 
     * @return possible object is {@link FormatoFichero }
     * 
     */
    public FormatoFichero getFormatoFichero() {
	return formatoFichero;
    }

    /**
     * Sets the value of the formatoFichero property.
     * 
     * @param value
     *            allowed object is {@link FormatoFichero }
     * 
     */
    public void setFormatoFichero(
	FormatoFichero value) {
	this.formatoFichero = value;
    }

    /**
     * Gets the value of the tipoConsulta property.
     * 
     * @return possible object is {@link TipoConsultaOF }
     * 
     */
    public TipoConsultaOF getTipoConsulta() {
	return tipoConsulta;
    }

    /**
     * Sets the value of the tipoConsulta property.
     * 
     * @param value
     *            allowed object is {@link TipoConsultaOF }
     * 
     */
    public void setTipoConsulta(
	TipoConsultaOF value) {
	this.tipoConsulta = value;
    }

    /**
     * Gets the value of the codigo property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCodigo() {
	return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCodigo(
	String value) {
	this.codigo = value;
    }

    /**
     * Gets the value of the nivelAdministracion property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getNivelAdministracion() {
	return nivelAdministracion;
    }

    /**
     * Sets the value of the nivelAdministracion property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setNivelAdministracion(
	Integer value) {
	this.nivelAdministracion = value;
    }

    /**
     * Gets the value of the estados property.
     * 
     * @return possible object is {@link Excluidos }
     * 
     */
    public Excluidos getEstados() {
	return estados;
    }

    /**
     * Sets the value of the estados property.
     * 
     * @param value
     *            allowed object is {@link Excluidos }
     * 
     */
    public void setEstados(
	Excluidos value) {
	this.estados = value;
    }

    /**
     * Gets the value of the comunidad property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getComunidad() {
	return comunidad;
    }

    /**
     * Sets the value of the comunidad property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setComunidad(
	String value) {
	this.comunidad = value;
    }

    /**
     * Gets the value of the provincia property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProvincia() {
	return provincia;
    }

    /**
     * Sets the value of the provincia property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setProvincia(
	String value) {
	this.provincia = value;
    }

    /**
     * Gets the value of the codigoUnidad property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getCodigoUnidad() {
	return codigoUnidad;
    }

    /**
     * Sets the value of the codigoUnidad property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setCodigoUnidad(
	String value) {
	this.codigoUnidad = value;
    }

    /**
     * Gets the value of the servicios property.
     * 
     * @return possible object is {@link Servicios }
     * 
     */
    public Servicios getServicios() {
	return servicios;
    }

    /**
     * Sets the value of the servicios property.
     * 
     * @param value
     *            allowed object is {@link Servicios }
     * 
     */
    public void setServicios(
	Servicios value) {
	this.servicios = value;
    }

    /**
     * Gets the value of the fechaInicio property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFechaInicio() {
	return fechaInicio;
    }

    /**
     * Sets the value of the fechaInicio property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFechaInicio(
	String value) {
	this.fechaInicio = value;
    }

    /**
     * Gets the value of the fechaFin property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getFechaFin() {
	return fechaFin;
    }

    /**
     * Sets the value of the fechaFin property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFechaFin(
	String value) {
	this.fechaFin = value;
    }

}
