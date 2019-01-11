
package es.msssi.sgm.registropresencial.connector.antivirus.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="acceso">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="id_aplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="cod_tipo_entrada" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="fichero">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="nombre_cacheado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "acceso",
    "fichero"
})
@XmlRootElement(name = "peticion", namespace = "http://xml.antivirus.msps/Peticion")
public class Peticion {

    @XmlElement(namespace = "http://xml.antivirus.msps/Peticion", required = true)
    protected Peticion.Acceso acceso;
    @XmlElement(namespace = "http://xml.antivirus.msps/Peticion", required = true)
    protected Peticion.Fichero fichero;

    /**
     * Gets the value of the acceso property.
     * 
     * @return
     *     possible object is
     *     {@link Peticion.Acceso }
     *     
     */
    public Peticion.Acceso getAcceso() {
        return acceso;
    }

    /**
     * Sets the value of the acceso property.
     * 
     * @param value
     *     allowed object is
     *     {@link Peticion.Acceso }
     *     
     */
    public void setAcceso(Peticion.Acceso value) {
        this.acceso = value;
    }

    /**
     * Gets the value of the fichero property.
     * 
     * @return
     *     possible object is
     *     {@link Peticion.Fichero }
     *     
     */
    public Peticion.Fichero getFichero() {
        return fichero;
    }

    /**
     * Sets the value of the fichero property.
     * 
     * @param value
     *     allowed object is
     *     {@link Peticion.Fichero }
     *     
     */
    public void setFichero(Peticion.Fichero value) {
        this.fichero = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="id_aplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="cod_tipo_entrada" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
        "idAplicacion",
        "codTipoEntrada"
    })
    public static class Acceso {

        @XmlElement(name = "id_aplicacion", namespace = "http://xml.antivirus.msps/Peticion", required = true)
        protected String idAplicacion;
        @XmlElement(name = "cod_tipo_entrada", namespace = "http://xml.antivirus.msps/Peticion")
        protected Integer codTipoEntrada;

        /**
         * Gets the value of the idAplicacion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdAplicacion() {
            return idAplicacion;
        }

        /**
         * Sets the value of the idAplicacion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdAplicacion(String value) {
            this.idAplicacion = value;
        }

        /**
         * Gets the value of the codTipoEntrada property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getCodTipoEntrada() {
            return codTipoEntrada;
        }

        /**
         * Sets the value of the codTipoEntrada property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setCodTipoEntrada(Integer value) {
            this.codTipoEntrada = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="nombre_cacheado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "nombre",
        "nombreCacheado"
    })
    public static class Fichero {

        @XmlElement(namespace = "http://xml.antivirus.msps/Peticion", required = true)
        protected String nombre;
        @XmlElement(name = "nombre_cacheado", namespace = "http://xml.antivirus.msps/Peticion")
        protected String nombreCacheado;

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
         * Gets the value of the nombreCacheado property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreCacheado() {
            return nombreCacheado;
        }

        /**
         * Sets the value of the nombreCacheado property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreCacheado(String value) {
            this.nombreCacheado = value;
        }

    }

}
