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
 *         &lt;element name="peticion">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://afirmaws/ws/validacion}CadenaSinEspacios">
 *               &lt;enumeration value="ValidarCertificado"/>
 *               &lt;enumeration value="ObtenerInfoCertificado"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="versionMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="parametros">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                   &lt;element name="idAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="modoValidacion" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="obtenerInfo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "", propOrder = {
    "peticion",
    "versionMsg",
    "parametros"
})
@XmlRootElement(name = "mensajeEntrada")
public class MensajeEntrada {

    @XmlElement(required = true)
    protected String peticion;
    @XmlElement(required = true)
    protected String versionMsg;
    @XmlElement(required = true)
    protected MensajeEntrada.Parametros parametros;

    /**
     * Gets the value of the peticion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeticion() {
        return peticion;
    }

    /**
     * Sets the value of the peticion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeticion(String value) {
        this.peticion = value;
    }

    /**
     * Gets the value of the versionMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersionMsg() {
        return versionMsg;
    }

    /**
     * Sets the value of the versionMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersionMsg(String value) {
        this.versionMsg = value;
    }

    /**
     * Gets the value of the parametros property.
     * 
     * @return
     *     possible object is
     *     {@link MensajeEntrada.Parametros }
     *     
     */
    public MensajeEntrada.Parametros getParametros() {
        return parametros;
    }

    /**
     * Sets the value of the parametros property.
     * 
     * @param value
     *     allowed object is
     *     {@link MensajeEntrada.Parametros }
     *     
     */
    public void setParametros(MensajeEntrada.Parametros value) {
        this.parametros = value;
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
     *         &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *         &lt;element name="idAplicacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="modoValidacion" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="obtenerInfo" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
    public static class Parametros {

        @XmlElement(required = true)
        protected byte[] certificado;
        @XmlElement(required = true)
        protected String idAplicacion;
        protected Integer modoValidacion;
        protected Boolean obtenerInfo;

        /**
         * Gets the value of the certificado property.
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getCertificado() {
            return certificado;
        }

        /**
         * Sets the value of the certificado property.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setCertificado(byte[] value) {
            this.certificado = ((byte[]) value);
        }

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
         * Gets the value of the modoValidacion property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getModoValidacion() {
            return modoValidacion;
        }

        /**
         * Sets the value of the modoValidacion property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setModoValidacion(Integer value) {
            this.modoValidacion = value;
        }

        /**
         * Gets the value of the obtenerInfo property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isObtenerInfo() {
            return obtenerInfo;
        }

        /**
         * Sets the value of the obtenerInfo property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setObtenerInfo(Boolean value) {
            this.obtenerInfo = value;
        }

    }

}
