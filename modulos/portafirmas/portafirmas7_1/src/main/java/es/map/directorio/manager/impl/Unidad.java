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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para unidad complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="unidad">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo" type="{http://impl.manager.directorio.map.es}codigo"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="denominacion" type="{http://impl.manager.directorio.map.es}denominacion"/>
 *         &lt;element name="dnmLenguaCooficial" type="{http://impl.manager.directorio.map.es}denominacion"/>
 *         &lt;element name="idiomaLengua" type="{http://impl.manager.directorio.map.es}denominacion"/>
 *         &lt;element name="estado" type="{http://impl.manager.directorio.map.es}estado"/>
 *         &lt;element name="nifCif" type="{http://impl.manager.directorio.map.es}nifCif" minOccurs="0"/>
 *         &lt;element name="nivelAdministracion" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="nivelJerarquico" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codUnidadSuperior" type="{http://impl.manager.directorio.map.es}codigo"/>
 *         &lt;element name="vUnidadSuperior" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="denomUnidadSuperior" type="{http://impl.manager.directorio.map.es}denominacion"/>
 *         &lt;element name="codUnidadRaiz" type="{http://impl.manager.directorio.map.es}codigo"/>
 *         &lt;element name="vUnidadRaiz" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="denomUnidadRaiz" type="{http://impl.manager.directorio.map.es}denominacion"/>
 *         &lt;element name="esEDP" type="{http://impl.manager.directorio.map.es}indicador"/>
 *         &lt;element name="codEDPPrincipal" type="{http://impl.manager.directorio.map.es}codigo" minOccurs="0"/>
 *         &lt;element name="vEDPPrincipal" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="denomEDPPrincipal" type="{http://impl.manager.directorio.map.es}denominacion" minOccurs="0"/>
 *         &lt;element name="codPoder" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="codTipoEntPublica" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codTipoUnidad" type="{http://impl.manager.directorio.map.es}maxTresCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbTerritorial" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbEntGeografica" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbPais" type="{http://impl.manager.directorio.map.es}tresCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbComunidad" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbProvincia" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbMunicipio" type="{http://impl.manager.directorio.map.es}cuatroCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbIsla" type="{http://impl.manager.directorio.map.es}dosCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbElm" type="{http://impl.manager.directorio.map.es}cuatroCaracteres" minOccurs="0"/>
 *         &lt;element name="codAmbLocExtranjera" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="competencias" type="{http://impl.manager.directorio.map.es}denominacion" minOccurs="0"/>
 *         &lt;element name="disposicionLegal" type="{http://impl.manager.directorio.map.es}denominacion" minOccurs="0"/>
 *         &lt;element name="fechaAltaOficial" type="{http://impl.manager.directorio.map.es}fecha" minOccurs="0"/>
 *         &lt;element name="fechaBajaOficial" type="{http://impl.manager.directorio.map.es}fecha" minOccurs="0"/>
 *         &lt;element name="fechaExtincion" type="{http://impl.manager.directorio.map.es}fecha" minOccurs="0"/>
 *         &lt;element name="fechaAnulacion" type="{http://impl.manager.directorio.map.es}fecha" minOccurs="0"/>
 *         &lt;element name="fechaUltimaActualizacion" type="{http://impl.manager.directorio.map.es}fecha" minOccurs="0"/>
 *         &lt;element name="codExterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observGenerales" type="{http://impl.manager.directorio.map.es}observaciones" minOccurs="0"/>
 *         &lt;element name="observBaja" type="{http://impl.manager.directorio.map.es}observaciones" minOccurs="0"/>
 *         &lt;element name="direccion" type="{http://impl.manager.directorio.map.es}direccion" minOccurs="0"/>
 *         &lt;element name="comparteNif" type="{http://impl.manager.directorio.map.es}indicador"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "unidad", propOrder = {
    "codigo",
    "version",
    "denominacion",
    "dnmLenguaCooficial",
    "idiomaLengua",
    "estado",
    "nifCif",
    "nivelAdministracion",
    "nivelJerarquico",
    "codUnidadSuperior",
    "vUnidadSuperior",
    "denomUnidadSuperior",
    "codUnidadRaiz",
    "vUnidadRaiz",
    "denomUnidadRaiz",
    "esEDP",
    "codEDPPrincipal",
    "vedpPrincipal",
    "denomEDPPrincipal",
    "codPoder",
    "codTipoEntPublica",
    "codTipoUnidad",
    "codAmbTerritorial",
    "codAmbEntGeografica",
    "codAmbPais",
    "codAmbComunidad",
    "codAmbProvincia",
    "codAmbMunicipio",
    "codAmbIsla",
    "codAmbElm",
    "codAmbLocExtranjera",
    "competencias",
    "disposicionLegal",
    "fechaAltaOficial",
    "fechaBajaOficial",
    "fechaExtincion",
    "fechaAnulacion",
    "fechaUltimaActualizacion",
    "codExterno",
    "observGenerales",
    "observBaja",
    "direccion",
    "comparteNif"
})
public class Unidad {

    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String codigo;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected int version;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String denominacion;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String dnmLenguaCooficial;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String idiomaLengua;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    @XmlSchemaType(name = "string")
    protected Estado estado;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String nifCif;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected int nivelAdministracion;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected int nivelJerarquico;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String codUnidadSuperior;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected int vUnidadSuperior;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String denomUnidadSuperior;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String codUnidadRaiz;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected int vUnidadRaiz;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    protected String denomUnidadRaiz;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    @XmlSchemaType(name = "string")
    protected Indicador esEDP;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codEDPPrincipal;
    @XmlElement(name = "vEDPPrincipal", namespace = "http://impl.manager.directorio.map.es")
    protected int vedpPrincipal;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String denomEDPPrincipal;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected Integer codPoder;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codTipoEntPublica;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codTipoUnidad;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbTerritorial;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbEntGeografica;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbPais;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbComunidad;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbProvincia;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbMunicipio;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbIsla;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbElm;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codAmbLocExtranjera;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String competencias;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String disposicionLegal;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String fechaAltaOficial;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String fechaBajaOficial;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String fechaExtincion;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String fechaAnulacion;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String fechaUltimaActualizacion;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String codExterno;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String observGenerales;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected String observBaja;
    @XmlElement(namespace = "http://impl.manager.directorio.map.es")
    protected Direccion direccion;
    @XmlElement(required = true, namespace = "http://impl.manager.directorio.map.es")
    @XmlSchemaType(name = "string")
    protected Indicador comparteNif;

    /**
     * Obtiene el valor de la propiedad codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define el valor de la propiedad codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
     * 
     */
    public int getVersion() {
        return version;
    }

    /**
     * Define el valor de la propiedad version.
     * 
     */
    public void setVersion(int value) {
        this.version = value;
    }

    /**
     * Obtiene el valor de la propiedad denominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominacion() {
        return denominacion;
    }

    /**
     * Define el valor de la propiedad denominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominacion(String value) {
        this.denominacion = value;
    }

    /**
     * Obtiene el valor de la propiedad dnmLenguaCooficial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDnmLenguaCooficial() {
        return dnmLenguaCooficial;
    }

    /**
     * Define el valor de la propiedad dnmLenguaCooficial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDnmLenguaCooficial(String value) {
        this.dnmLenguaCooficial = value;
    }

    /**
     * Obtiene el valor de la propiedad idiomaLengua.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdiomaLengua() {
        return idiomaLengua;
    }

    /**
     * Define el valor de la propiedad idiomaLengua.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdiomaLengua(String value) {
        this.idiomaLengua = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link Estado }
     *     
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link Estado }
     *     
     */
    public void setEstado(Estado value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad nifCif.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifCif() {
        return nifCif;
    }

    /**
     * Define el valor de la propiedad nifCif.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifCif(String value) {
        this.nifCif = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelAdministracion.
     * 
     */
    public int getNivelAdministracion() {
        return nivelAdministracion;
    }

    /**
     * Define el valor de la propiedad nivelAdministracion.
     * 
     */
    public void setNivelAdministracion(int value) {
        this.nivelAdministracion = value;
    }

    /**
     * Obtiene el valor de la propiedad nivelJerarquico.
     * 
     */
    public int getNivelJerarquico() {
        return nivelJerarquico;
    }

    /**
     * Define el valor de la propiedad nivelJerarquico.
     * 
     */
    public void setNivelJerarquico(int value) {
        this.nivelJerarquico = value;
    }

    /**
     * Obtiene el valor de la propiedad codUnidadSuperior.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUnidadSuperior() {
        return codUnidadSuperior;
    }

    /**
     * Define el valor de la propiedad codUnidadSuperior.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUnidadSuperior(String value) {
        this.codUnidadSuperior = value;
    }

    /**
     * Obtiene el valor de la propiedad vUnidadSuperior.
     * 
     */
    public int getVUnidadSuperior() {
        return vUnidadSuperior;
    }

    /**
     * Define el valor de la propiedad vUnidadSuperior.
     * 
     */
    public void setVUnidadSuperior(int value) {
        this.vUnidadSuperior = value;
    }

    /**
     * Obtiene el valor de la propiedad denomUnidadSuperior.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomUnidadSuperior() {
        return denomUnidadSuperior;
    }

    /**
     * Define el valor de la propiedad denomUnidadSuperior.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomUnidadSuperior(String value) {
        this.denomUnidadSuperior = value;
    }

    /**
     * Obtiene el valor de la propiedad codUnidadRaiz.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodUnidadRaiz() {
        return codUnidadRaiz;
    }

    /**
     * Define el valor de la propiedad codUnidadRaiz.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodUnidadRaiz(String value) {
        this.codUnidadRaiz = value;
    }

    /**
     * Obtiene el valor de la propiedad vUnidadRaiz.
     * 
     */
    public int getVUnidadRaiz() {
        return vUnidadRaiz;
    }

    /**
     * Define el valor de la propiedad vUnidadRaiz.
     * 
     */
    public void setVUnidadRaiz(int value) {
        this.vUnidadRaiz = value;
    }

    /**
     * Obtiene el valor de la propiedad denomUnidadRaiz.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomUnidadRaiz() {
        return denomUnidadRaiz;
    }

    /**
     * Define el valor de la propiedad denomUnidadRaiz.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomUnidadRaiz(String value) {
        this.denomUnidadRaiz = value;
    }

    /**
     * Obtiene el valor de la propiedad esEDP.
     * 
     * @return
     *     possible object is
     *     {@link Indicador }
     *     
     */
    public Indicador getEsEDP() {
        return esEDP;
    }

    /**
     * Define el valor de la propiedad esEDP.
     * 
     * @param value
     *     allowed object is
     *     {@link Indicador }
     *     
     */
    public void setEsEDP(Indicador value) {
        this.esEDP = value;
    }

    /**
     * Obtiene el valor de la propiedad codEDPPrincipal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEDPPrincipal() {
        return codEDPPrincipal;
    }

    /**
     * Define el valor de la propiedad codEDPPrincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEDPPrincipal(String value) {
        this.codEDPPrincipal = value;
    }

    /**
     * Obtiene el valor de la propiedad vedpPrincipal.
     * 
     */
    public int getVEDPPrincipal() {
        return vedpPrincipal;
    }

    /**
     * Define el valor de la propiedad vedpPrincipal.
     * 
     */
    public void setVEDPPrincipal(int value) {
        this.vedpPrincipal = value;
    }

    /**
     * Obtiene el valor de la propiedad denomEDPPrincipal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenomEDPPrincipal() {
        return denomEDPPrincipal;
    }

    /**
     * Define el valor de la propiedad denomEDPPrincipal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenomEDPPrincipal(String value) {
        this.denomEDPPrincipal = value;
    }

    /**
     * Obtiene el valor de la propiedad codPoder.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCodPoder() {
        return codPoder;
    }

    /**
     * Define el valor de la propiedad codPoder.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCodPoder(Integer value) {
        this.codPoder = value;
    }

    /**
     * Obtiene el valor de la propiedad codTipoEntPublica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoEntPublica() {
        return codTipoEntPublica;
    }

    /**
     * Define el valor de la propiedad codTipoEntPublica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoEntPublica(String value) {
        this.codTipoEntPublica = value;
    }

    /**
     * Obtiene el valor de la propiedad codTipoUnidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodTipoUnidad() {
        return codTipoUnidad;
    }

    /**
     * Define el valor de la propiedad codTipoUnidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodTipoUnidad(String value) {
        this.codTipoUnidad = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbTerritorial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbTerritorial() {
        return codAmbTerritorial;
    }

    /**
     * Define el valor de la propiedad codAmbTerritorial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbTerritorial(String value) {
        this.codAmbTerritorial = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbEntGeografica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbEntGeografica() {
        return codAmbEntGeografica;
    }

    /**
     * Define el valor de la propiedad codAmbEntGeografica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbEntGeografica(String value) {
        this.codAmbEntGeografica = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbPais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbPais() {
        return codAmbPais;
    }

    /**
     * Define el valor de la propiedad codAmbPais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbPais(String value) {
        this.codAmbPais = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbComunidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbComunidad() {
        return codAmbComunidad;
    }

    /**
     * Define el valor de la propiedad codAmbComunidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbComunidad(String value) {
        this.codAmbComunidad = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbProvincia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbProvincia() {
        return codAmbProvincia;
    }

    /**
     * Define el valor de la propiedad codAmbProvincia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbProvincia(String value) {
        this.codAmbProvincia = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbMunicipio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbMunicipio() {
        return codAmbMunicipio;
    }

    /**
     * Define el valor de la propiedad codAmbMunicipio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbMunicipio(String value) {
        this.codAmbMunicipio = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbIsla.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbIsla() {
        return codAmbIsla;
    }

    /**
     * Define el valor de la propiedad codAmbIsla.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbIsla(String value) {
        this.codAmbIsla = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbElm.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbElm() {
        return codAmbElm;
    }

    /**
     * Define el valor de la propiedad codAmbElm.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbElm(String value) {
        this.codAmbElm = value;
    }

    /**
     * Obtiene el valor de la propiedad codAmbLocExtranjera.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodAmbLocExtranjera() {
        return codAmbLocExtranjera;
    }

    /**
     * Define el valor de la propiedad codAmbLocExtranjera.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodAmbLocExtranjera(String value) {
        this.codAmbLocExtranjera = value;
    }

    /**
     * Obtiene el valor de la propiedad competencias.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompetencias() {
        return competencias;
    }

    /**
     * Define el valor de la propiedad competencias.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompetencias(String value) {
        this.competencias = value;
    }

    /**
     * Obtiene el valor de la propiedad disposicionLegal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisposicionLegal() {
        return disposicionLegal;
    }

    /**
     * Define el valor de la propiedad disposicionLegal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisposicionLegal(String value) {
        this.disposicionLegal = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaAltaOficial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaAltaOficial() {
        return fechaAltaOficial;
    }

    /**
     * Define el valor de la propiedad fechaAltaOficial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaAltaOficial(String value) {
        this.fechaAltaOficial = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaBajaOficial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaBajaOficial() {
        return fechaBajaOficial;
    }

    /**
     * Define el valor de la propiedad fechaBajaOficial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaBajaOficial(String value) {
        this.fechaBajaOficial = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaExtincion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaExtincion() {
        return fechaExtincion;
    }

    /**
     * Define el valor de la propiedad fechaExtincion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaExtincion(String value) {
        this.fechaExtincion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaAnulacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaAnulacion() {
        return fechaAnulacion;
    }

    /**
     * Define el valor de la propiedad fechaAnulacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaAnulacion(String value) {
        this.fechaAnulacion = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaUltimaActualizacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaUltimaActualizacion() {
        return fechaUltimaActualizacion;
    }

    /**
     * Define el valor de la propiedad fechaUltimaActualizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaUltimaActualizacion(String value) {
        this.fechaUltimaActualizacion = value;
    }

    /**
     * Obtiene el valor de la propiedad codExterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodExterno() {
        return codExterno;
    }

    /**
     * Define el valor de la propiedad codExterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodExterno(String value) {
        this.codExterno = value;
    }

    /**
     * Obtiene el valor de la propiedad observGenerales.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservGenerales() {
        return observGenerales;
    }

    /**
     * Define el valor de la propiedad observGenerales.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservGenerales(String value) {
        this.observGenerales = value;
    }

    /**
     * Obtiene el valor de la propiedad observBaja.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservBaja() {
        return observBaja;
    }

    /**
     * Define el valor de la propiedad observBaja.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservBaja(String value) {
        this.observBaja = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link Direccion }
     *     
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link Direccion }
     *     
     */
    public void setDireccion(Direccion value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad comparteNif.
     * 
     * @return
     *     possible object is
     *     {@link Indicador }
     *     
     */
    public Indicador getComparteNif() {
        return comparteNif;
    }

    /**
     * Define el valor de la propiedad comparteNif.
     * 
     * @param value
     *     allowed object is
     *     {@link Indicador }
     *     
     */
    public void setComparteNif(Indicador value) {
        this.comparteNif = value;
    }

}
