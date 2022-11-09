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


package es.seap.minhap.portafirmas.ws.eeutil.vis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para TipoFirmasElectronicas complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoFirmasElectronicas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoFirma" type="{https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma}tipoFirma"/>
 *         &lt;element name="ContenidoFirma">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CSV" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="ValorCSV" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                             &lt;element name="RegulacionGeneracionCSV" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="FirmaConCertificado" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="FirmaBase64" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoFirmasElectronicas", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", propOrder = {
    "tipoFirma",
    "contenidoFirma"
})
public class TipoFirmasElectronicas {

    @XmlElement(name = "TipoFirma", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", required = true)
    @XmlSchemaType(name = "string")
    protected TipoFirma tipoFirma;
    @XmlElement(name = "ContenidoFirma", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", required = true)
    protected TipoFirmasElectronicas.ContenidoFirma contenidoFirma;
    @XmlAttribute(name = "ref")
    protected String ref;

    /**
     * Obtiene el valor de la propiedad tipoFirma.
     * 
     * @return
     *     possible object is
     *     {@link TipoFirma }
     *     
     */
    public TipoFirma getTipoFirma() {
        return tipoFirma;
    }

    /**
     * Define el valor de la propiedad tipoFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoFirma }
     *     
     */
    public void setTipoFirma(TipoFirma value) {
        this.tipoFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad contenidoFirma.
     * 
     * @return
     *     possible object is
     *     {@link TipoFirmasElectronicas.ContenidoFirma }
     *     
     */
    public TipoFirmasElectronicas.ContenidoFirma getContenidoFirma() {
        return contenidoFirma;
    }

    /**
     * Define el valor de la propiedad contenidoFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoFirmasElectronicas.ContenidoFirma }
     *     
     */
    public void setContenidoFirma(TipoFirmasElectronicas.ContenidoFirma value) {
        this.contenidoFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad ref.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRef() {
        return ref;
    }

    /**
     * Define el valor de la propiedad ref.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRef(String value) {
        this.ref = value;
    }


    /**
     * <p>Clase Java para anonymous complex type.
     * 
     * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CSV" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="ValorCSV" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                   &lt;element name="RegulacionGeneracionCSV" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="FirmaConCertificado" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="FirmaBase64" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
        "csv",
        "firmaConCertificado"
    })
    public static class ContenidoFirma {

        @XmlElement(name = "CSV", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma")
        protected TipoFirmasElectronicas.ContenidoFirma.CSV csv;
        @XmlElement(name = "FirmaConCertificado", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma")
        protected TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado firmaConCertificado;

        /**
         * Obtiene el valor de la propiedad csv.
         * 
         * @return
         *     possible object is
         *     {@link TipoFirmasElectronicas.ContenidoFirma.CSV }
         *     
         */
        public TipoFirmasElectronicas.ContenidoFirma.CSV getCSV() {
            return csv;
        }

        /**
         * Define el valor de la propiedad csv.
         * 
         * @param value
         *     allowed object is
         *     {@link TipoFirmasElectronicas.ContenidoFirma.CSV }
         *     
         */
        public void setCSV(TipoFirmasElectronicas.ContenidoFirma.CSV value) {
            this.csv = value;
        }

        /**
         * Obtiene el valor de la propiedad firmaConCertificado.
         * 
         * @return
         *     possible object is
         *     {@link TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado }
         *     
         */
        public TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado getFirmaConCertificado() {
            return firmaConCertificado;
        }

        /**
         * Define el valor de la propiedad firmaConCertificado.
         * 
         * @param value
         *     allowed object is
         *     {@link TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado }
         *     
         */
        public void setFirmaConCertificado(TipoFirmasElectronicas.ContenidoFirma.FirmaConCertificado value) {
            this.firmaConCertificado = value;
        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="ValorCSV" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *         &lt;element name="RegulacionGeneracionCSV" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
            "valorCSV",
            "regulacionGeneracionCSV"
        })
        public static class CSV {

            @XmlElement(name = "ValorCSV", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", required = true)
            protected String valorCSV;
            @XmlElement(name = "RegulacionGeneracionCSV", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma", required = true)
            protected String regulacionGeneracionCSV;

            /**
             * Obtiene el valor de la propiedad valorCSV.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValorCSV() {
                return valorCSV;
            }

            /**
             * Define el valor de la propiedad valorCSV.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValorCSV(String value) {
                this.valorCSV = value;
            }

            /**
             * Obtiene el valor de la propiedad regulacionGeneracionCSV.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getRegulacionGeneracionCSV() {
                return regulacionGeneracionCSV;
            }

            /**
             * Define el valor de la propiedad regulacionGeneracionCSV.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setRegulacionGeneracionCSV(String value) {
                this.regulacionGeneracionCSV = value;
            }

        }


        /**
         * <p>Clase Java para anonymous complex type.
         * 
         * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       &lt;sequence>
         *         &lt;element name="FirmaBase64" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
            "firmaBase64"
        })
        public static class FirmaConCertificado {

            @XmlElement(name = "FirmaBase64", namespace = "https://ssweb.seap.minhap.es/Eeutil/XSD/v1.0/firma")
            protected byte[] firmaBase64;

            /**
             * Obtiene el valor de la propiedad firmaBase64.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getFirmaBase64() {
                return firmaBase64;
            }

            /**
             * Define el valor de la propiedad firmaBase64.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setFirmaBase64(byte[] value) {
                this.firmaBase64 = value;
            }

        }

    }

}
