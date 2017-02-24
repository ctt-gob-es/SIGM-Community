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
 * Java class for UnidadesWs complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="UnidadesWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="clave" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="formatoFichero" type="{http://impl.manager.directorio.map.es/wsExport}formatoFichero"/>
 *         &lt;element name="tipoConsulta" type="{http://impl.manager.directorio.map.es/wsExport}tipoConsultaUO"/>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadesDependientes" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="nivelAdministracion" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nivelJerarquico" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="estados" type="{http://impl.manager.directorio.map.es/wsExport}estados" minOccurs="0"/>
 *         &lt;element name="comunidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="provincia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "UnidadesWs", propOrder = { "usuario", "clave", "formatoFichero", "tipoConsulta",
    "codigo", "unidadesDependientes", "nivelAdministracion", "nivelJerarquico", "estados",
    "comunidad", "provincia", "fechaInicio", "fechaFin" })
public class UnidadesWs {

    @XmlElement(required = true)
    protected String usuario;
    @XmlElement(required = true)
    protected String clave;
    @XmlElement(required = true)
    protected FormatoFichero formatoFichero;
    @XmlElement(required = true)
    protected TipoConsultaUO tipoConsulta;
    protected String codigo;
    protected Boolean unidadesDependientes;
    protected Integer nivelAdministracion;
    protected Integer nivelJerarquico;
    protected Estados estados;
    protected String comunidad;
    protected String provincia;
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
     * @return possible object is {@link TipoConsultaUO }
     * 
     */
    public TipoConsultaUO getTipoConsulta() {
	return tipoConsulta;
    }

    /**
     * Sets the value of the tipoConsulta property.
     * 
     * @param value
     *            allowed object is {@link TipoConsultaUO }
     * 
     */
    public void setTipoConsulta(
	TipoConsultaUO value) {
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
     * Gets the value of the unidadesDependientes property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isUnidadesDependientes() {
	return unidadesDependientes;
    }

    /**
     * Sets the value of the unidadesDependientes property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setUnidadesDependientes(
	Boolean value) {
	this.unidadesDependientes = value;
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
     * Gets the value of the nivelJerarquico property.
     * 
     * @return possible object is {@link Integer }
     * 
     */
    public Integer getNivelJerarquico() {
	return nivelJerarquico;
    }

    /**
     * Sets the value of the nivelJerarquico property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setNivelJerarquico(
	Integer value) {
	this.nivelJerarquico = value;
    }

    /**
     * Gets the value of the estados property.
     * 
     * @return possible object is {@link Estados }
     * 
     */
    public Estados getEstados() {
	return estados;
    }

    /**
     * Sets the value of the estados property.
     * 
     * @param value
     *            allowed object is {@link Estados }
     * 
     */
    public void setEstados(
	Estados value) {
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
