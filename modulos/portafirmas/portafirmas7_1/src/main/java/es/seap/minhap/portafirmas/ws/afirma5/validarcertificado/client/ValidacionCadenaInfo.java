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
 * <p>Java class for ValidacionCadenaInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ValidacionCadenaInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoResultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descResultado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="errorCertificado" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="idCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="ValidacionSimple" type="{http://afirmaws/ws/validacion}ValidacionSimpleInfo"/>
 *                   &lt;element name="ValidacionEstado" type="{http://afirmaws/ws/validacion}ValidacionEstadoInfo"/>
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
@XmlType(name = "ValidacionCadenaInfo", propOrder = {
    "codigoResultado",
    "descResultado",
    "errorCertificado"
})
public class ValidacionCadenaInfo {

    @XmlElement(required = true)
    protected String codigoResultado;
    @XmlElement(required = true)
    protected String descResultado;
    protected List<ValidacionCadenaInfo.ErrorCertificado> errorCertificado;

    /**
     * Gets the value of the codigoResultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoResultado() {
        return codigoResultado;
    }

    /**
     * Sets the value of the codigoResultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoResultado(String value) {
        this.codigoResultado = value;
    }

    /**
     * Gets the value of the descResultado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescResultado() {
        return descResultado;
    }

    /**
     * Sets the value of the descResultado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescResultado(String value) {
        this.descResultado = value;
    }

    /**
     * Gets the value of the errorCertificado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the errorCertificado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getErrorCertificado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ValidacionCadenaInfo.ErrorCertificado }
     * 
     * 
     */
    public List<ValidacionCadenaInfo.ErrorCertificado> getErrorCertificado() {
        if (errorCertificado == null) {
            errorCertificado = new ArrayList<ValidacionCadenaInfo.ErrorCertificado>();
        }
        return this.errorCertificado;
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
     *         &lt;element name="idCertificado" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="ValidacionSimple" type="{http://afirmaws/ws/validacion}ValidacionSimpleInfo"/>
     *         &lt;element name="ValidacionEstado" type="{http://afirmaws/ws/validacion}ValidacionEstadoInfo"/>
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
        "idCertificado",
        "validacionSimple",
        "validacionEstado"
    })
    public static class ErrorCertificado {

        @XmlElement(required = true)
        protected String idCertificado;
        @XmlElement(name = "ValidacionSimple", required = true)
        protected ValidacionSimpleInfo validacionSimple;
        @XmlElement(name = "ValidacionEstado", required = true)
        protected ValidacionEstadoInfo validacionEstado;

        /**
         * Gets the value of the idCertificado property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIdCertificado() {
            return idCertificado;
        }

        /**
         * Sets the value of the idCertificado property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIdCertificado(String value) {
            this.idCertificado = value;
        }

        /**
         * Gets the value of the validacionSimple property.
         * 
         * @return
         *     possible object is
         *     {@link ValidacionSimpleInfo }
         *     
         */
        public ValidacionSimpleInfo getValidacionSimple() {
            return validacionSimple;
        }

        /**
         * Sets the value of the validacionSimple property.
         * 
         * @param value
         *     allowed object is
         *     {@link ValidacionSimpleInfo }
         *     
         */
        public void setValidacionSimple(ValidacionSimpleInfo value) {
            this.validacionSimple = value;
        }

        /**
         * Gets the value of the validacionEstado property.
         * 
         * @return
         *     possible object is
         *     {@link ValidacionEstadoInfo }
         *     
         */
        public ValidacionEstadoInfo getValidacionEstado() {
            return validacionEstado;
        }

        /**
         * Sets the value of the validacionEstado property.
         * 
         * @param value
         *     allowed object is
         *     {@link ValidacionEstadoInfo }
         *     
         */
        public void setValidacionEstado(ValidacionEstadoInfo value) {
            this.validacionEstado = value;
        }

    }

}
