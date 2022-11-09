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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
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
 *             &lt;restriction base="{http://afirmaws/ws/firma}CadenaSinEspacios">
 *               &lt;enumeration value="ValidarFirma"/>
 *               &lt;enumeration value="FirmaServidor"/>
 *               &lt;enumeration value="FirmaServidorCoSign"/>
 *               &lt;enumeration value="FirmaServidorCounterSign"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF1"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF1CoSign"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF1CounterSign"/>
 *               &lt;enumeration value="FirmaUsuario3FasesF3"/>
 *               &lt;enumeration value="FirmaUsuario2FasesF2"/>
 *               &lt;enumeration value="ValidarFirmaBloquesCompleto"/>
 *               &lt;enumeration value="ValidarFirmaBloquesDocumento"/>
 *               &lt;enumeration value="FirmaUsuarioBloquesF1"/>
 *               &lt;enumeration value="FirmaUsuarioBloquesF3"/>
 *               &lt;enumeration value="ObtenerIdDocumentosBloqueFirmas"/>
 *               &lt;enumeration value="ObtenerIdDocumentosBloqueFirmasBackwards"/>
 *               &lt;enumeration value="ObtenerInformacionBloqueFirmas"/>
 *               &lt;enumeration value="ObtenerInformacionBloqueFirmasBackwards"/>
 *               &lt;enumeration value="ObtenerInfoCompletaBloqueFirmas"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="versionMsg" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="respuesta">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{http://afirmaws/ws/firma}Respuesta">
 *                 &lt;choice>
 *                   &lt;element name="Respuesta">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;choice>
 *                             &lt;sequence>
 *                               &lt;sequence>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}estado"/>
 *                                 &lt;choice>
 *                                   &lt;element ref="{http://afirmaws/ws/firma}descripcion" minOccurs="0"/>
 *                                 &lt;/choice>
 *                               &lt;/sequence>
 *                               &lt;sequence>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}idTransaccion" minOccurs="0"/>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}hash" minOccurs="0"/>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}algoritmoHash" minOccurs="0"/>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}firmaElectronica" minOccurs="0"/>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}formatoFirma" minOccurs="0"/>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}justificanteFirmaElectronica" minOccurs="0"/>
 *                                 &lt;element ref="{http://afirmaws/ws/firma}idDocumento" minOccurs="0"/>
 *                                 &lt;element name="idDocumentos" minOccurs="0">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;sequence maxOccurs="unbounded">
 *                                           &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
 *                                         &lt;/sequence>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                                 &lt;element name="idDocumentosBloque" minOccurs="0">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;choice>
 *                                           &lt;element ref="{http://afirmaws/ws/firma}idDocumentos" minOccurs="0"/>
 *                                           &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados" minOccurs="0"/>
 *                                         &lt;/choice>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                                 &lt;element name="infoBloque" minOccurs="0">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;choice>
 *                                           &lt;element ref="{http://afirmaws/ws/firma}documentosBloque"/>
 *                                           &lt;element ref="{http://afirmaws/ws/firma}documentosMultifirma"/>
 *                                           &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados"/>
 *                                         &lt;/choice>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                                 &lt;element name="Bloque" minOccurs="0">
 *                                   &lt;complexType>
 *                                     &lt;complexContent>
 *                                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                         &lt;sequence maxOccurs="unbounded">
 *                                           &lt;element name="idBloque" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                                           &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
 *                                         &lt;/sequence>
 *                                       &lt;/restriction>
 *                                     &lt;/complexContent>
 *                                   &lt;/complexType>
 *                                 &lt;/element>
 *                               &lt;/sequence>
 *                             &lt;/sequence>
 *                           &lt;/choice>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="Excepcion" type="{http://afirmaws/ws/firma}Excepcion"/>
 *                 &lt;/choice>
 *               &lt;/extension>
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
    "respuesta"
})
@XmlRootElement(name = "mensajeSalida")
public class MensajeSalida {

    @XmlElement(required = true)
    protected String peticion;
    @XmlElement(required = true)
    protected String versionMsg;
    @XmlElement(required = true)
    protected MensajeSalida.RespuestaX respuesta;

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
     * Gets the value of the respuesta property.
     * 
     * @return
     *     possible object is
     *     {@link MensajeSalida.Respuesta }
     *     
     */
    public MensajeSalida.RespuestaX getRespuesta() {
        return respuesta;
    }

    /**
     * Sets the value of the respuesta property.
     * 
     * @param value
     *     allowed object is
     *     {@link MensajeSalida.Respuesta }
     *     
     */
    public void setRespuesta(MensajeSalida.RespuestaX value) {
        this.respuesta = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{http://afirmaws/ws/firma}Respuesta">
     *       &lt;choice>
     *         &lt;element name="Respuesta">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;choice>
     *                   &lt;sequence>
     *                     &lt;sequence>
     *                       &lt;element ref="{http://afirmaws/ws/firma}estado"/>
     *                       &lt;choice>
     *                         &lt;element ref="{http://afirmaws/ws/firma}descripcion" minOccurs="0"/>
     *                       &lt;/choice>
     *                     &lt;/sequence>
     *                     &lt;sequence>
     *                       &lt;element ref="{http://afirmaws/ws/firma}idTransaccion" minOccurs="0"/>
     *                       &lt;element ref="{http://afirmaws/ws/firma}hash" minOccurs="0"/>
     *                       &lt;element ref="{http://afirmaws/ws/firma}algoritmoHash" minOccurs="0"/>
     *                       &lt;element ref="{http://afirmaws/ws/firma}firmaElectronica" minOccurs="0"/>
     *                       &lt;element ref="{http://afirmaws/ws/firma}formatoFirma" minOccurs="0"/>
     *                       &lt;element ref="{http://afirmaws/ws/firma}justificanteFirmaElectronica" minOccurs="0"/>
     *                       &lt;element ref="{http://afirmaws/ws/firma}idDocumento" minOccurs="0"/>
     *                       &lt;element name="idDocumentos" minOccurs="0">
     *                         &lt;complexType>
     *                           &lt;complexContent>
     *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                               &lt;sequence maxOccurs="unbounded">
     *                                 &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
     *                               &lt;/sequence>
     *                             &lt;/restriction>
     *                           &lt;/complexContent>
     *                         &lt;/complexType>
     *                       &lt;/element>
     *                       &lt;element name="idDocumentosBloque" minOccurs="0">
     *                         &lt;complexType>
     *                           &lt;complexContent>
     *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                               &lt;choice>
     *                                 &lt;element ref="{http://afirmaws/ws/firma}idDocumentos" minOccurs="0"/>
     *                                 &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados" minOccurs="0"/>
     *                               &lt;/choice>
     *                             &lt;/restriction>
     *                           &lt;/complexContent>
     *                         &lt;/complexType>
     *                       &lt;/element>
     *                       &lt;element name="infoBloque" minOccurs="0">
     *                         &lt;complexType>
     *                           &lt;complexContent>
     *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                               &lt;choice>
     *                                 &lt;element ref="{http://afirmaws/ws/firma}documentosBloque"/>
     *                                 &lt;element ref="{http://afirmaws/ws/firma}documentosMultifirma"/>
     *                                 &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados"/>
     *                               &lt;/choice>
     *                             &lt;/restriction>
     *                           &lt;/complexContent>
     *                         &lt;/complexType>
     *                       &lt;/element>
     *                       &lt;element name="Bloque" minOccurs="0">
     *                         &lt;complexType>
     *                           &lt;complexContent>
     *                             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                               &lt;sequence maxOccurs="unbounded">
     *                                 &lt;element name="idBloque" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *                                 &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
     *                               &lt;/sequence>
     *                             &lt;/restriction>
     *                           &lt;/complexContent>
     *                         &lt;/complexType>
     *                       &lt;/element>
     *                     &lt;/sequence>
     *                   &lt;/sequence>
     *                 &lt;/choice>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="Excepcion" type="{http://afirmaws/ws/firma}Excepcion"/>
     *       &lt;/choice>
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "respuesta",
        "excepcion"
    })
    public static class RespuestaX
        extends es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Respuesta2
    {

    	@XmlElement(name = "Respuesta", nillable = true)
        protected MensajeSalida.RespuestaX.Respuesta respuesta;
        @XmlElement(name = "Excepcion")
        protected Excepcion excepcion;

        /**
         * Gets the value of the respuesta property.
         * 
         * @return
         *     possible object is
         *     {@link MensajeSalida.RespuestaX.Respuesta }
         *     
         */
        public MensajeSalida.RespuestaX.Respuesta getRespuesta() {
            return respuesta;
        }

        /**
         * Sets the value of the respuesta property.
         * 
         * @param value
         *     allowed object is
         *     {@link MensajeSalida.RespuestaX.Respuesta }
         *     
         */
        public void setRespuesta(MensajeSalida.RespuestaX.Respuesta value) {
            this.respuesta = value;
        }
        /**
         * Gets the value of the excepcion property.
         * 
         * @return
         *     possible object is
         *     {@link Excepcion }
         *     
         */
        public Excepcion getExcepcion() {
            return excepcion;
        }

        /**
         * Sets the value of the excepcion property.
         * 
         * @param value
         *     allowed object is
         *     {@link Excepcion }
         *     
         */
        public void setExcepcion(Excepcion value) {
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
         *       &lt;choice>
         *         &lt;sequence>
         *           &lt;sequence>
         *             &lt;element ref="{http://afirmaws/ws/firma}estado"/>
         *             &lt;choice>
         *               &lt;element ref="{http://afirmaws/ws/firma}descripcion" minOccurs="0"/>
         *             &lt;/choice>
         *           &lt;/sequence>
         *           &lt;sequence>
         *             &lt;element ref="{http://afirmaws/ws/firma}idTransaccion" minOccurs="0"/>
         *             &lt;element ref="{http://afirmaws/ws/firma}hash" minOccurs="0"/>
         *             &lt;element ref="{http://afirmaws/ws/firma}algoritmoHash" minOccurs="0"/>
         *             &lt;element ref="{http://afirmaws/ws/firma}firmaElectronica" minOccurs="0"/>
         *             &lt;element ref="{http://afirmaws/ws/firma}formatoFirma" minOccurs="0"/>
         *             &lt;element ref="{http://afirmaws/ws/firma}justificanteFirmaElectronica" minOccurs="0"/>
         *             &lt;element ref="{http://afirmaws/ws/firma}idDocumento" minOccurs="0"/>
         *             &lt;element name="idDocumentos" minOccurs="0">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;sequence maxOccurs="unbounded">
         *                       &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
         *                     &lt;/sequence>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *             &lt;element name="idDocumentosBloque" minOccurs="0">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;choice>
         *                       &lt;element ref="{http://afirmaws/ws/firma}idDocumentos" minOccurs="0"/>
         *                       &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados" minOccurs="0"/>
         *                     &lt;/choice>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *             &lt;element name="infoBloque" minOccurs="0">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;choice>
         *                       &lt;element ref="{http://afirmaws/ws/firma}documentosBloque"/>
         *                       &lt;element ref="{http://afirmaws/ws/firma}documentosMultifirma"/>
         *                       &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados"/>
         *                     &lt;/choice>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *             &lt;element name="Bloque" minOccurs="0">
         *               &lt;complexType>
         *                 &lt;complexContent>
         *                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                     &lt;sequence maxOccurs="unbounded">
         *                       &lt;element name="idBloque" type="{http://www.w3.org/2001/XMLSchema}string"/>
         *                       &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
         *                     &lt;/sequence>
         *                   &lt;/restriction>
         *                 &lt;/complexContent>
         *               &lt;/complexType>
         *             &lt;/element>
         *           &lt;/sequence>
         *         &lt;/sequence>
         *       &lt;/choice>
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
            "descripcion",
            "idTransaccion",
            "hash",
            "algoritmoHash",
            "firmaElectronica",
            "formatoFirma",
            "justificanteFirmaElectronica",
            "idDocumento",
            "idDocumentos",
            "idDocumentosBloque",
            "infoBloque",
            "bloque"
        })
        public static class Respuesta {

            protected Boolean estado;
            protected Descripcion descripcion;
            protected String idTransaccion;
            protected byte[] hash;
            protected String algoritmoHash;
            protected byte[] firmaElectronica;
            protected String formatoFirma;
            protected byte[] justificanteFirmaElectronica;
            protected String idDocumento;
            protected MensajeSalida.RespuestaX.Respuesta.IdDocumentos idDocumentos;
            protected MensajeSalida.RespuestaX.Respuesta.IdDocumentosBloque idDocumentosBloque;
            protected MensajeSalida.RespuestaX.Respuesta.InfoBloque infoBloque;
            @XmlElement(name = "Bloque")
            protected MensajeSalida.RespuestaX.Respuesta.Bloque bloque;

            /**
             * Gets the value of the estado property.
             * 
             * @return
             *     possible object is
             *     {@link Boolean }
             *     
             */
            public Boolean isEstado() {
                return estado;
            }

            /**
             * Sets the value of the estado property.
             * 
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *     
             */
            public void setEstado(Boolean value) {
                this.estado = value;
            }

            /**
             * Gets the value of the descripcion property.
             * 
             * @return
             *     possible object is
             *     {@link Descripcion }
             *     
             */
            public Descripcion getDescripcion() {
                return descripcion;
            }

            /**
             * Sets the value of the descripcion property.
             * 
             * @param value
             *     allowed object is
             *     {@link Descripcion }
             *     
             */
            public void setDescripcion(Descripcion value) {
                this.descripcion = value;
            }

            /**
             * Gets the value of the idTransaccion property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIdTransaccion() {
                return idTransaccion;
            }

            /**
             * Sets the value of the idTransaccion property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIdTransaccion(String value) {
                this.idTransaccion = value;
            }

            /**
             * Gets the value of the hash property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getHash() {
                return hash;
            }

            /**
             * Sets the value of the hash property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setHash(byte[] value) {
                this.hash = ((byte[]) value);
            }

            /**
             * Gets the value of the algoritmoHash property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getAlgoritmoHash() {
                return algoritmoHash;
            }

            /**
             * Sets the value of the algoritmoHash property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setAlgoritmoHash(String value) {
                this.algoritmoHash = value;
            }

            /**
             * Gets the value of the firmaElectronica property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getFirmaElectronica() {
                return firmaElectronica;
            }

            /**
             * Sets the value of the firmaElectronica property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setFirmaElectronica(byte[] value) {
                this.firmaElectronica = ((byte[]) value);
            }

            /**
             * Gets the value of the formatoFirma property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFormatoFirma() {
                return formatoFirma;
            }

            /**
             * Sets the value of the formatoFirma property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFormatoFirma(String value) {
                this.formatoFirma = value;
            }

            /**
             * Gets the value of the justificanteFirmaElectronica property.
             * 
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getJustificanteFirmaElectronica() {
                return justificanteFirmaElectronica;
            }

            /**
             * Sets the value of the justificanteFirmaElectronica property.
             * 
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setJustificanteFirmaElectronica(byte[] value) {
                this.justificanteFirmaElectronica = ((byte[]) value);
            }

            /**
             * Gets the value of the idDocumento property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getIdDocumento() {
                return idDocumento;
            }

            /**
             * Sets the value of the idDocumento property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setIdDocumento(String value) {
                this.idDocumento = value;
            }

            /**
             * Gets the value of the idDocumentos property.
             * 
             * @return
             *     possible object is
             *     {@link MensajeSalida.Respuesta.Respuesta.IdDocumentos }
             *     
             */
            public MensajeSalida.RespuestaX.Respuesta.IdDocumentos getIdDocumentos() {
                return idDocumentos;
            }

            /**
             * Sets the value of the idDocumentos property.
             * 
             * @param value
             *     allowed object is
             *     {@link MensajeSalida.Respuesta.Respuesta.IdDocumentos }
             *     
             */
            public void setIdDocumentos(MensajeSalida.RespuestaX.Respuesta.IdDocumentos value) {
                this.idDocumentos = value;
            }

            /**
             * Gets the value of the idDocumentosBloque property.
             * 
             * @return
             *     possible object is
             *     {@link MensajeSalida.Respuesta.Respuesta.IdDocumentosBloque }
             *     
             */
            public MensajeSalida.RespuestaX.Respuesta.IdDocumentosBloque getIdDocumentosBloque() {
                return idDocumentosBloque;
            }

            /**
             * Sets the value of the idDocumentosBloque property.
             * 
             * @param value
             *     allowed object is
             *     {@link MensajeSalida.Respuesta.Respuesta.IdDocumentosBloque }
             *     
             */
            public void setIdDocumentosBloque(MensajeSalida.RespuestaX.Respuesta.IdDocumentosBloque value) {
                this.idDocumentosBloque = value;
            }

            /**
             * Gets the value of the infoBloque property.
             * 
             * @return
             *     possible object is
             *     {@link MensajeSalida.Respuesta.Respuesta.InfoBloque }
             *     
             */
            public MensajeSalida.RespuestaX.Respuesta.InfoBloque getInfoBloque() {
                return infoBloque;
            }

            /**
             * Sets the value of the infoBloque property.
             * 
             * @param value
             *     allowed object is
             *     {@link MensajeSalida.Respuesta.Respuesta.InfoBloque }
             *     
             */
            public void setInfoBloque(MensajeSalida.RespuestaX.Respuesta.InfoBloque value) {
                this.infoBloque = value;
            }

            /**
             * Gets the value of the bloque property.
             * 
             * @return
             *     possible object is
             *     {@link MensajeSalida.Respuesta.Respuesta.Bloque }
             *     
             */
            public MensajeSalida.RespuestaX.Respuesta.Bloque getBloque() {
                return bloque;
            }

            /**
             * Sets the value of the bloque property.
             * 
             * @param value
             *     allowed object is
             *     {@link MensajeSalida.Respuesta.Respuesta.Bloque }
             *     
             */
            public void setBloque(MensajeSalida.RespuestaX.Respuesta.Bloque value) {
                this.bloque = value;
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
             *       &lt;sequence maxOccurs="unbounded">
             *         &lt;element name="idBloque" type="{http://www.w3.org/2001/XMLSchema}string"/>
             *         &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
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
                "idBloqueAndIdDocumento"
            })
            public static class Bloque {

                @XmlElementRefs({
                    @XmlElementRef(name = "idBloque", namespace = "http://afirmaws/ws/firma", type = JAXBElement.class),
                    @XmlElementRef(name = "idDocumento", namespace = "http://afirmaws/ws/firma", type = JAXBElement.class)
                })
                protected List<JAXBElement<String>> idBloqueAndIdDocumento;

                /**
                 * Gets the value of the idBloqueAndIdDocumento property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the idBloqueAndIdDocumento property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getIdBloqueAndIdDocumento().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link JAXBElement }{@code <}{@link String }{@code >}
                 * {@link JAXBElement }{@code <}{@link String }{@code >}
                 * 
                 * 
                 */
                public List<JAXBElement<String>> getIdBloqueAndIdDocumento() {
                    if (idBloqueAndIdDocumento == null) {
                        idBloqueAndIdDocumento = new ArrayList<JAXBElement<String>>();
                    }
                    return this.idBloqueAndIdDocumento;
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
             *       &lt;sequence maxOccurs="unbounded">
             *         &lt;group ref="{http://afirmaws/ws/firma}IdDocumentos"/>
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
                "idDocumento"
            })
            public static class IdDocumentos {

                @XmlElement(required = true)
                protected List<String> idDocumento;

                /**
                 * Gets the value of the idDocumento property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the idDocumento property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getIdDocumento().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link String }
                 * 
                 * 
                 */
                public List<String> getIdDocumento() {
                    if (idDocumento == null) {
                        idDocumento = new ArrayList<String>();
                    }
                    return this.idDocumento;
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
             *       &lt;choice>
             *         &lt;element ref="{http://afirmaws/ws/firma}idDocumentos" minOccurs="0"/>
             *         &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados" minOccurs="0"/>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "idDocumentos",
                "idDocumentosMultifirmados"
            })
            public static class IdDocumentosBloque {

                protected es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos idDocumentos;
                protected IdDocumentosMultifirmados idDocumentosMultifirmados;

                /**
                 * Gets the value of the idDocumentos property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos }
                 *     
                 */
                public es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos getIdDocumentos() {
                    return idDocumentos;
                }

                /**
                 * Sets the value of the idDocumentos property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos }
                 *     
                 */
                public void setIdDocumentos(es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.IdDocumentos value) {
                    this.idDocumentos = value;
                }

                /**
                 * Gets the value of the idDocumentosMultifirmados property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IdDocumentosMultifirmados }
                 *     
                 */
                public IdDocumentosMultifirmados getIdDocumentosMultifirmados() {
                    return idDocumentosMultifirmados;
                }

                /**
                 * Sets the value of the idDocumentosMultifirmados property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IdDocumentosMultifirmados }
                 *     
                 */
                public void setIdDocumentosMultifirmados(IdDocumentosMultifirmados value) {
                    this.idDocumentosMultifirmados = value;
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
             *       &lt;choice>
             *         &lt;element ref="{http://afirmaws/ws/firma}documentosBloque"/>
             *         &lt;element ref="{http://afirmaws/ws/firma}documentosMultifirma"/>
             *         &lt;element ref="{http://afirmaws/ws/firma}idDocumentosMultifirmados"/>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "documentosBloque",
                "documentosMultifirma",
                "idDocumentosMultifirmados"
            })
            public static class InfoBloque {

                protected DocumentosBloque documentosBloque;
                protected DocumentosMultifirma documentosMultifirma;
                protected IdDocumentosMultifirmados idDocumentosMultifirmados;

                /**
                 * Gets the value of the documentosBloque property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link DocumentosBloque }
                 *     
                 */
                public DocumentosBloque getDocumentosBloque() {
                    return documentosBloque;
                }

                /**
                 * Sets the value of the documentosBloque property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link DocumentosBloque }
                 *     
                 */
                public void setDocumentosBloque(DocumentosBloque value) {
                    this.documentosBloque = value;
                }

                /**
                 * Gets the value of the documentosMultifirma property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link DocumentosMultifirma }
                 *     
                 */
                public DocumentosMultifirma getDocumentosMultifirma() {
                    return documentosMultifirma;
                }

                /**
                 * Sets the value of the documentosMultifirma property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link DocumentosMultifirma }
                 *     
                 */
                public void setDocumentosMultifirma(DocumentosMultifirma value) {
                    this.documentosMultifirma = value;
                }

                /**
                 * Gets the value of the idDocumentosMultifirmados property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link IdDocumentosMultifirmados }
                 *     
                 */
                public IdDocumentosMultifirmados getIdDocumentosMultifirmados() {
                    return idDocumentosMultifirmados;
                }

                /**
                 * Sets the value of the idDocumentosMultifirmados property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link IdDocumentosMultifirmados }
                 *     
                 */
                public void setIdDocumentosMultifirmados(IdDocumentosMultifirmados value) {
                    this.idDocumentosMultifirmados = value;
                }

            }

        }

    }

}
