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


package es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ValidacionEstadoInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidacionEstadoInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descEstado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InfoMetodoVerificacion" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="descEstado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="fechaUltimaActualizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="fechaRevocacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="Metodo">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="urlServidor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="protocolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="tokenOCSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="excepcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/all>
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
@XmlType(name = "ValidacionEstadoInfo", propOrder = {
    "estado",
    "descEstado",
    "infoMetodoVerificacion"
})
public class ValidacionEstadoInfo {

    @XmlElement(required = true)
    protected String estado;
    @XmlElement(required = true)
    protected String descEstado;
    @XmlElement(name = "InfoMetodoVerificacion", required = true)
    protected List<ValidacionEstadoInfo.InfoMetodoVerificacion> infoMetodoVerificacion;

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

    /**
     * Gets the value of the infoMetodoVerificacion property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the infoMetodoVerificacion property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInfoMetodoVerificacion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValidacionEstadoInfo.InfoMetodoVerificacion }
     * 
     * 
     */
    public List<ValidacionEstadoInfo.InfoMetodoVerificacion> getInfoMetodoVerificacion() {
        if (infoMetodoVerificacion == null) {
            infoMetodoVerificacion = new ArrayList<ValidacionEstadoInfo.InfoMetodoVerificacion>();
        }
        return this.infoMetodoVerificacion;
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
     *       &lt;all>
     *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="descEstado" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="fechaUltimaActualizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="fechaRevocacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="Metodo">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="urlServidor" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="protocolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="tokenOCSP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="excepcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/all>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {

    })
    public static class InfoMetodoVerificacion {

        @XmlElement(required = true)
        protected String estado;
        @XmlElement(required = true)
        protected String descEstado;
        protected String fechaUltimaActualizacion;
        protected String fechaRevocacion;
        protected String motivo;
        @XmlElement(name = "Metodo", required = true)
        protected ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo metodo;
        protected String tokenOCSP;
        protected String excepcion;

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

        /**
         * Gets the value of the fechaUltimaActualizacion property.
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
         * Sets the value of the fechaUltimaActualizacion property.
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
         * Gets the value of the fechaRevocacion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFechaRevocacion() {
            return fechaRevocacion;
        }

        /**
         * Sets the value of the fechaRevocacion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFechaRevocacion(String value) {
            this.fechaRevocacion = value;
        }

        /**
         * Gets the value of the motivo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMotivo() {
            return motivo;
        }

        /**
         * Sets the value of the motivo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMotivo(String value) {
            this.motivo = value;
        }

        /**
         * Gets the value of the metodo property.
         * 
         * @return
         *     possible object is
         *     {@link ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo }
         *     
         */
        public ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo getMetodo() {
            return metodo;
        }

        /**
         * Sets the value of the metodo property.
         * 
         * @param value
         *     allowed object is
         *     {@link ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo }
         *     
         */
        public void setMetodo(ValidacionEstadoInfo.InfoMetodoVerificacion.Metodo value) {
            this.metodo = value;
        }

        /**
         * Gets the value of the tokenOCSP property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTokenOCSP() {
            return tokenOCSP;
        }

        /**
         * Sets the value of the tokenOCSP property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTokenOCSP(String value) {
            this.tokenOCSP = value;
        }

        /**
         * Gets the value of the excepcion property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getExcepcion() {
            return excepcion;
        }

        /**
         * Sets the value of the excepcion property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setExcepcion(String value) {
            this.excepcion = value;
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
         *         &lt;element name="urlServidor" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="protocolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "urlServidor",
            "protocolo"
        })
        public static class Metodo {

            @XmlElement(required = true)
            protected String urlServidor;
            @XmlElement(required = true)
            protected String protocolo;

            /**
             * Gets the value of the urlServidor property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getUrlServidor() {
                return urlServidor;
            }

            /**
             * Sets the value of the urlServidor property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setUrlServidor(String value) {
                this.urlServidor = value;
            }

            /**
             * Gets the value of the protocolo property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getProtocolo() {
                return protocolo;
            }

            /**
             * Sets the value of the protocolo property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setProtocolo(String value) {
                this.protocolo = value;
            }

        }

    }

}
