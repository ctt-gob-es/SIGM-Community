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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parametros complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="parametros">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{http://afirmaws/ws/firma}idAplicacion"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}documento" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}idDocumento" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}idDocumentos" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}idTransaccion" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}firmaElectronica" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}certificadoFirmante" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}firmante" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}algoritmoHash" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}idReferencia" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}bloqueFirmas" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}formatoFirma" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}idTransacciones" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}hash" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}datos" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}firmanteObjetivo" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}nombreDocumento" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}tipoDocumento" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}custodiarDocumento" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}documentosMultifirma" minOccurs="0"/>
 *         &lt;element ref="{http://afirmaws/ws/firma}bloque" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parametros", propOrder = {

})
public class Parametros {

    @XmlElement(required = true)
    protected String idAplicacion;
    protected byte[] documento;
    protected String idDocumento;
    protected IdDocumentos idDocumentos;
    protected String idTransaccion;
    protected byte[] firmaElectronica;
    protected byte[] certificadoFirmante;
    protected String firmante;
    protected String algoritmoHash;
    protected String idReferencia;
    protected byte[] bloqueFirmas;
    protected String formatoFirma;
    protected IdTransacciones idTransacciones;
    protected byte[] hash;
    protected byte[] datos;
    protected byte[] firmanteObjetivo;
    protected String nombreDocumento;
    protected String tipoDocumento;
    @XmlElement(defaultValue = "false")
    protected Boolean custodiarDocumento;
    protected DocumentosMultifirma documentosMultifirma;
    protected Bloque bloque;

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
     * Gets the value of the documento property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDocumento() {
        return documento;
    }

    /**
     * Sets the value of the documento property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDocumento(byte[] value) {
        this.documento = ((byte[]) value);
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
     *     {@link IdDocumentos }
     *     
     */
    public IdDocumentos getIdDocumentos() {
        return idDocumentos;
    }

    /**
     * Sets the value of the idDocumentos property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdDocumentos }
     *     
     */
    public void setIdDocumentos(IdDocumentos value) {
        this.idDocumentos = value;
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
     * Gets the value of the certificadoFirmante property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getCertificadoFirmante() {
        return certificadoFirmante;
    }

    /**
     * Sets the value of the certificadoFirmante property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setCertificadoFirmante(byte[] value) {
        this.certificadoFirmante = ((byte[]) value);
    }

    /**
     * Gets the value of the firmante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirmante() {
        return firmante;
    }

    /**
     * Sets the value of the firmante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirmante(String value) {
        this.firmante = value;
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
     * Gets the value of the idReferencia property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdReferencia() {
        return idReferencia;
    }

    /**
     * Sets the value of the idReferencia property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdReferencia(String value) {
        this.idReferencia = value;
    }

    /**
     * Gets the value of the bloqueFirmas property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getBloqueFirmas() {
        return bloqueFirmas;
    }

    /**
     * Sets the value of the bloqueFirmas property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setBloqueFirmas(byte[] value) {
        this.bloqueFirmas = ((byte[]) value);
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
     * Gets the value of the idTransacciones property.
     * 
     * @return
     *     possible object is
     *     {@link IdTransacciones }
     *     
     */
    public IdTransacciones getIdTransacciones() {
        return idTransacciones;
    }

    /**
     * Sets the value of the idTransacciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdTransacciones }
     *     
     */
    public void setIdTransacciones(IdTransacciones value) {
        this.idTransacciones = value;
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
     * Gets the value of the datos property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDatos() {
        return datos;
    }

    /**
     * Sets the value of the datos property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDatos(byte[] value) {
        this.datos = ((byte[]) value);
    }

    /**
     * Gets the value of the firmanteObjetivo property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFirmanteObjetivo() {
        return firmanteObjetivo;
    }

    /**
     * Sets the value of the firmanteObjetivo property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFirmanteObjetivo(byte[] value) {
        this.firmanteObjetivo = ((byte[]) value);
    }

    /**
     * Gets the value of the nombreDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    /**
     * Sets the value of the nombreDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreDocumento(String value) {
        this.nombreDocumento = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Gets the value of the custodiarDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCustodiarDocumento() {
        return custodiarDocumento;
    }

    /**
     * Sets the value of the custodiarDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCustodiarDocumento(Boolean value) {
        this.custodiarDocumento = value;
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
     * Gets the value of the bloque property.
     * 
     * @return
     *     possible object is
     *     {@link Bloque }
     *     
     */
    public Bloque getBloque() {
        return bloque;
    }

    /**
     * Sets the value of the bloque property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bloque }
     *     
     */
    public void setBloque(Bloque value) {
        this.bloque = value;
    }

}
