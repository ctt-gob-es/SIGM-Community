
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
 *         &lt;element name="estado">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="cod_estado" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="desc_estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="informacion_antivirus">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="nombre_antivirus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="nombre_virus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="nombre_fichero_analizado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="fecha_analisis" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="tiempo_proceso_analisis" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="icap">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="cod_extendido" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="desc_extendido" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="tiempo_proceso_icap" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                             &lt;element name="mensaje_raw" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="response">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="cod_respuesta" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                                       &lt;element name="error_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                             &lt;element name="AvBypass" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
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
    "estado",
    "informacionAntivirus"
})
@XmlRootElement(name = "resultado", namespace = "http://xml.antivirus.msps/RespuestaAntivirus")
public class Resultado {

    @XmlElement(namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
    protected Resultado.Estado estado;
    @XmlElement(name = "informacion_antivirus", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
    protected Resultado.InformacionAntivirus informacionAntivirus;

    /**
     * Gets the value of the estado property.
     * 
     * @return
     *     possible object is
     *     {@link Resultado.Estado }
     *     
     */
    public Resultado.Estado getEstado() {
        return estado;
    }

    /**
     * Sets the value of the estado property.
     * 
     * @param value
     *     allowed object is
     *     {@link Resultado.Estado }
     *     
     */
    public void setEstado(Resultado.Estado value) {
        this.estado = value;
    }

    /**
     * Gets the value of the informacionAntivirus property.
     * 
     * @return
     *     possible object is
     *     {@link Resultado.InformacionAntivirus }
     *     
     */
    public Resultado.InformacionAntivirus getInformacionAntivirus() {
        return informacionAntivirus;
    }

    /**
     * Sets the value of the informacionAntivirus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Resultado.InformacionAntivirus }
     *     
     */
    public void setInformacionAntivirus(Resultado.InformacionAntivirus value) {
        this.informacionAntivirus = value;
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
     *         &lt;element name="cod_estado" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="desc_estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "codEstado",
        "descEstado"
    })
    public static class Estado {

        @XmlElement(name = "cod_estado", namespace = "http://xml.antivirus.msps/RespuestaAntivirus")
        protected int codEstado;
        @XmlElement(name = "desc_estado", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
        protected String descEstado;

        /**
         * Gets the value of the codEstado property.
         * 
         */
        public int getCodEstado() {
            return codEstado;
        }

        /**
         * Sets the value of the codEstado property.
         * 
         */
        public void setCodEstado(int value) {
            this.codEstado = value;
        }

        /**
         * Gets the value of the descEstado property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescEstado() {
            return descEstado;
        }

        /**
         * Sets the value of the descEstado property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescEstado(String value) {
            this.descEstado = value;
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
     *         &lt;element name="nombre_antivirus" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="nombre_virus" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="nombre_fichero_analizado" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="fecha_analisis" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="tiempo_proceso_analisis" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="icap">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="cod_extendido" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="desc_extendido" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="tiempo_proceso_icap" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                   &lt;element name="mensaje_raw" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="response">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="cod_respuesta" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *                             &lt;element name="error_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                   &lt;element name="AvBypass" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
        "nombreAntivirus",
        "nombreVirus",
        "nombreFicheroAnalizado",
        "fechaAnalisis",
        "tiempoProcesoAnalisis",
        "icap"
    })
    public static class InformacionAntivirus {

        @XmlElement(name = "nombre_antivirus", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
        protected String nombreAntivirus;
        @XmlElement(name = "nombre_virus", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
        protected String nombreVirus;
        @XmlElement(name = "nombre_fichero_analizado", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
        protected String nombreFicheroAnalizado;
        @XmlElement(name = "fecha_analisis", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
        protected String fechaAnalisis;
        @XmlElement(name = "tiempo_proceso_analisis", namespace = "http://xml.antivirus.msps/RespuestaAntivirus")
        protected int tiempoProcesoAnalisis;
        @XmlElement(namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
        protected Resultado.InformacionAntivirus.Icap icap;

        /**
         * Gets the value of the nombreAntivirus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreAntivirus() {
            return nombreAntivirus;
        }

        /**
         * Sets the value of the nombreAntivirus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreAntivirus(String value) {
            this.nombreAntivirus = value;
        }

        /**
         * Gets the value of the nombreVirus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreVirus() {
            return nombreVirus;
        }

        /**
         * Sets the value of the nombreVirus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreVirus(String value) {
            this.nombreVirus = value;
        }

        /**
         * Gets the value of the nombreFicheroAnalizado property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNombreFicheroAnalizado() {
            return nombreFicheroAnalizado;
        }

        /**
         * Sets the value of the nombreFicheroAnalizado property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNombreFicheroAnalizado(String value) {
            this.nombreFicheroAnalizado = value;
        }

        /**
         * Gets the value of the fechaAnalisis property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFechaAnalisis() {
            return fechaAnalisis;
        }

        /**
         * Sets the value of the fechaAnalisis property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFechaAnalisis(String value) {
            this.fechaAnalisis = value;
        }

        /**
         * Gets the value of the tiempoProcesoAnalisis property.
         * 
         */
        public int getTiempoProcesoAnalisis() {
            return tiempoProcesoAnalisis;
        }

        /**
         * Sets the value of the tiempoProcesoAnalisis property.
         * 
         */
        public void setTiempoProcesoAnalisis(int value) {
            this.tiempoProcesoAnalisis = value;
        }

        /**
         * Gets the value of the icap property.
         * 
         * @return
         *     possible object is
         *     {@link Resultado.InformacionAntivirus.Icap }
         *     
         */
        public Resultado.InformacionAntivirus.Icap getIcap() {
            return icap;
        }

        /**
         * Sets the value of the icap property.
         * 
         * @param value
         *     allowed object is
         *     {@link Resultado.InformacionAntivirus.Icap }
         *     
         */
        public void setIcap(Resultado.InformacionAntivirus.Icap value) {
            this.icap = value;
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
         *         &lt;element name="cod_extendido" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="desc_extendido" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="tiempo_proceso_icap" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *         &lt;element name="mensaje_raw" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="response">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="cod_respuesta" type="{http://www.w3.org/2001/XMLSchema}int"/>
         *                   &lt;element name="error_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                 &lt;/sequence>
         *               &lt;/restriction>
         *             &lt;/complexContent>
         *           &lt;/complexType>
         *         &lt;/element>
         *         &lt;element name="AvBypass" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "codExtendido",
            "descExtendido",
            "tiempoProcesoIcap",
            "mensajeRaw",
            "response",
            "avBypass"
        })
        public static class Icap {

            @XmlElement(name = "cod_extendido", namespace = "http://xml.antivirus.msps/RespuestaAntivirus")
            protected int codExtendido;
            @XmlElement(name = "desc_extendido", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
            protected String descExtendido;
            @XmlElement(name = "tiempo_proceso_icap", namespace = "http://xml.antivirus.msps/RespuestaAntivirus")
            protected int tiempoProcesoIcap;
            @XmlElement(name = "mensaje_raw", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
            protected String mensajeRaw;
            @XmlElement(namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
            protected Resultado.InformacionAntivirus.Icap.Response response;
            @XmlElement(name = "AvBypass", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
            protected String avBypass;

            /**
             * Gets the value of the codExtendido property.
             * 
             */
            public int getCodExtendido() {
                return codExtendido;
            }

            /**
             * Sets the value of the codExtendido property.
             * 
             */
            public void setCodExtendido(int value) {
                this.codExtendido = value;
            }

            /**
             * Gets the value of the descExtendido property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescExtendido() {
                return descExtendido;
            }

            /**
             * Sets the value of the descExtendido property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescExtendido(String value) {
                this.descExtendido = value;
            }

            /**
             * Gets the value of the tiempoProcesoIcap property.
             * 
             */
            public int getTiempoProcesoIcap() {
                return tiempoProcesoIcap;
            }

            /**
             * Sets the value of the tiempoProcesoIcap property.
             * 
             */
            public void setTiempoProcesoIcap(int value) {
                this.tiempoProcesoIcap = value;
            }

            /**
             * Gets the value of the mensajeRaw property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getMensajeRaw() {
                return mensajeRaw;
            }

            /**
             * Sets the value of the mensajeRaw property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setMensajeRaw(String value) {
                this.mensajeRaw = value;
            }

            /**
             * Gets the value of the response property.
             * 
             * @return
             *     possible object is
             *     {@link Resultado.InformacionAntivirus.Icap.Response }
             *     
             */
            public Resultado.InformacionAntivirus.Icap.Response getResponse() {
                return response;
            }

            /**
             * Sets the value of the response property.
             * 
             * @param value
             *     allowed object is
             *     {@link Resultado.InformacionAntivirus.Icap.Response }
             *     
             */
            public void setResponse(Resultado.InformacionAntivirus.Icap.Response value) {
                this.response = value;
            }

            /**
             * Gets the value of the avBypass property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAvBypass() {
                return avBypass;
            }

            /**
             * Sets the value of the avBypass property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAvBypass(String value) {
                this.avBypass = value;
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
             *         &lt;element name="cod_respuesta" type="{http://www.w3.org/2001/XMLSchema}int"/>
             *         &lt;element name="error_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
                "codRespuesta",
                "errorName"
            })
            public static class Response {

                @XmlElement(name = "cod_respuesta", namespace = "http://xml.antivirus.msps/RespuestaAntivirus")
                protected int codRespuesta;
                @XmlElement(name = "error_name", namespace = "http://xml.antivirus.msps/RespuestaAntivirus", required = true)
                protected String errorName;

                /**
                 * Gets the value of the codRespuesta property.
                 * 
                 */
                public int getCodRespuesta() {
                    return codRespuesta;
                }

                /**
                 * Sets the value of the codRespuesta property.
                 * 
                 */
                public void setCodRespuesta(int value) {
                    this.codRespuesta = value;
                }

                /**
                 * Gets the value of the errorName property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getErrorName() {
                    return errorName;
                }

                /**
                 * Sets the value of the errorName property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setErrorName(String value) {
                    this.errorName = value;
                }

            }

        }

    }

}
