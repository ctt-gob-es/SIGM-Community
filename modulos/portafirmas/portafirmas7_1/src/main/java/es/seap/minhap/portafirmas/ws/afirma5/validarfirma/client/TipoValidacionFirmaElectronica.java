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


package es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TipoValidacionFirmaElectronica complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TipoValidacionFirmaElectronica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="validacionFirmaElectronica" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="proceso" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                   &lt;element name="detalle" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                   &lt;element name="conclusion" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                   &lt;element name="informacionAdicional" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="firmante" maxOccurs="unbounded">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                       &lt;element name="selloTiempo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                       &lt;element name="certificadoTSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
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
@XmlType(name = "TipoValidacionFirmaElectronica", propOrder = {
    "validacionFirmaElectronica"
})
public class TipoValidacionFirmaElectronica {

    protected TipoValidacionFirmaElectronica.ValidacionFirmaElectronica validacionFirmaElectronica;

    /**
     * Gets the value of the validacionFirmaElectronica property.
     * 
     * @return
     *     possible object is
     *     {@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica }
     *     
     */
    public TipoValidacionFirmaElectronica.ValidacionFirmaElectronica getValidacionFirmaElectronica() {
        return validacionFirmaElectronica;
    }

    /**
     * Sets the value of the validacionFirmaElectronica property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica }
     *     
     */
    public void setValidacionFirmaElectronica(TipoValidacionFirmaElectronica.ValidacionFirmaElectronica value) {
        this.validacionFirmaElectronica = value;
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
     *         &lt;element name="proceso" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *         &lt;element name="detalle" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *         &lt;element name="conclusion" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
     *         &lt;element name="informacionAdicional" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="firmante" maxOccurs="unbounded">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                             &lt;element name="selloTiempo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                             &lt;element name="certificadoTSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "content"
    })
    public static class ValidacionFirmaElectronica {

        @XmlElementRefs({
            @XmlElementRef(name = "informacionAdicional", namespace = "http://afirmaws/ws/firma", type = JAXBElement.class),
            @XmlElementRef(name = "detalle", namespace = "http://afirmaws/ws/firma", type = JAXBElement.class),
            @XmlElementRef(name = "proceso", namespace = "http://afirmaws/ws/firma", type = JAXBElement.class),
            @XmlElementRef(name = "conclusion", namespace = "http://afirmaws/ws/firma", type = JAXBElement.class)
        })
        @XmlMixed
        protected List<Serializable> content;

        /**
         * Gets the value of the content property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the content property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getContent().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional }{@code >}
         * {@link JAXBElement }{@code <}{@link Object }{@code >}
         * {@link String }
         * {@link JAXBElement }{@code <}{@link Object }{@code >}
         * {@link JAXBElement }{@code <}{@link Object }{@code >}
         * 
         * 
         */
        public List<Serializable> getContent() {
            if (content == null) {
                content = new ArrayList<Serializable>();
            }
            return this.content;
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
         *         &lt;element name="firmante" maxOccurs="unbounded">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                   &lt;element name="selloTiempo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                   &lt;element name="certificadoTSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "firmante"
        })
        public static class InformacionAdicional {

            @XmlElement(required = true)
            protected List<TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante> firmante;

            /**
             * Gets the value of the firmante property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the firmante property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getFirmante().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante }
             * 
             * 
             */
            public List<TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante> getFirmante() {
                if (firmante == null) {
                    firmante = new ArrayList<TipoValidacionFirmaElectronica.ValidacionFirmaElectronica.InformacionAdicional.Firmante>();
                }
                return this.firmante;
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
             *         &lt;element name="certificado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
             *         &lt;element name="selloTiempo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
             *         &lt;element name="certificadoTSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
                "certificado",
                "selloTiempo",
                "certificadoTSA"
            })
            public static class Firmante {

                protected String certificado;
                protected String selloTiempo;
                protected String certificadoTSA;

                /**
                 * Gets the value of the certificado property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCertificado() {
                    return certificado;
                }

                /**
                 * Sets the value of the certificado property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCertificado(String value) {
                    this.certificado = value;
                }

                /**
                 * Gets the value of the selloTiempo property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getSelloTiempo() {
                    return selloTiempo;
                }

                /**
                 * Sets the value of the selloTiempo property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setSelloTiempo(String value) {
                    this.selloTiempo = value;
                }

                /**
                 * Gets the value of the certificadoTSA property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCertificadoTSA() {
                    return certificadoTSA;
                }

                /**
                 * Sets the value of the certificadoTSA property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCertificadoTSA(String value) {
                    this.certificadoTSA = value;
                }

            }

        }

    }

}
