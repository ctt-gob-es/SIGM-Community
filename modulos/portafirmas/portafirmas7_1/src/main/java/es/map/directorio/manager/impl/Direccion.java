/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantacion de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderan si se vuelve a compilar el esquema de origen. 
// Generado el: 2019.07.31 a las 12:16:25 PM CEST 
//


package es.map.directorio.manager.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para direccion complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="direccion">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoVia" type="{http://impl.manager.directorio.map.es}maxDosCaracteres" minOccurs="0"/>
 *         &lt;element name="nombreVia" type="{http://impl.manager.directorio.map.es}denominacion" minOccurs="0"/>
 *         &lt;element name="numVia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codPostal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codPais" type="{http://impl.manager.directorio.map.es}tresCaracteres" minOccurs="0"/>
 *         &lt;element name="codComunidad" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codProvincia" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codLocalidad" type="{http://impl.manager.directorio.map.es}cuatroCaracteres" minOccurs="0"/>
 *         &lt;element name="codEntGeografica" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="dirExtranjera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locExtranjera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "direccion", propOrder = {
    "tipoVia",
    "nombreVia",
    "numVia",
    "codPostal",
    "codPais",
    "codComunidad",
    "codProvincia",
    "codLocalidad",
    "codEntGeografica",
    "dirExtranjera",
    "locExtranjera",
    "observaciones"
})
public class Direccion {
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String tipoVia;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String nombreVia;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String numVia;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codPostal;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codPais;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codComunidad;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codProvincia;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codLocalidad;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codEntGeografica;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String dirExtranjera;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String locExtranjera;
	@XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String observaciones;

    /**
     * Obtiene el valor de la propiedad tipoVia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoVia() {
        return tipoVia;
    }

    /**
     * Define el valor de la propiedad tipoVia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoVia(String value) {
        this.tipoVia = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreVia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreVia() {
        return nombreVia;
    }

    /**
     * Define el valor de la propiedad nombreVia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreVia(String value) {
        this.nombreVia = value;
    }

    /**
     * Obtiene el valor de la propiedad numVia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumVia() {
        return numVia;
    }

    /**
     * Define el valor de la propiedad numVia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumVia(String value) {
        this.numVia = value;
    }

    /**
     * Obtiene el valor de la propiedad codPostal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPostal() {
        return codPostal;
    }

    /**
     * Define el valor de la propiedad codPostal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPostal(String value) {
        this.codPostal = value;
    }

    /**
     * Obtiene el valor de la propiedad codPais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodPais() {
        return codPais;
    }

    /**
     * Define el valor de la propiedad codPais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodPais(String value) {
        this.codPais = value;
    }

    /**
     * Obtiene el valor de la propiedad codComunidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodComunidad() {
        return codComunidad;
    }

    /**
     * Define el valor de la propiedad codComunidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodComunidad(String value) {
        this.codComunidad = value;
    }

    /**
     * Obtiene el valor de la propiedad codProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodProvincia() {
        return codProvincia;
    }

    /**
     * Define el valor de la propiedad codProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodProvincia(String value) {
        this.codProvincia = value;
    }

    /**
     * Obtiene el valor de la propiedad codLocalidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodLocalidad() {
        return codLocalidad;
    }

    /**
     * Define el valor de la propiedad codLocalidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodLocalidad(String value) {
        this.codLocalidad = value;
    }

    /**
     * Obtiene el valor de la propiedad codEntGeografica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEntGeografica() {
        return codEntGeografica;
    }

    /**
     * Define el valor de la propiedad codEntGeografica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEntGeografica(String value) {
        this.codEntGeografica = value;
    }

    /**
     * Obtiene el valor de la propiedad dirExtranjera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDirExtranjera() {
        return dirExtranjera;
    }

    /**
     * Define el valor de la propiedad dirExtranjera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDirExtranjera(String value) {
        this.dirExtranjera = value;
    }

    /**
     * Obtiene el valor de la propiedad locExtranjera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocExtranjera() {
        return locExtranjera;
    }

    /**
     * Define el valor de la propiedad locExtranjera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocExtranjera(String value) {
        this.locExtranjera = value;
    }

    /**
     * Obtiene el valor de la propiedad observaciones.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Define el valor de la propiedad observaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservaciones(String value) {
        this.observaciones = value;
    }

}
