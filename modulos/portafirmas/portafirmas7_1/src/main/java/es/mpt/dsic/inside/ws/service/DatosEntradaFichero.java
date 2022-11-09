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


package es.mpt.dsic.inside.ws.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para datosEntradaFichero complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="datosEntradaFichero">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="formatoFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modoFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="algoritmoFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cofirmarSiFirmado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="nodeToSign" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datosEntradaFichero", propOrder = {
    "contenido",
    "formatoFirma",
    "modoFirma",
    "algoritmoFirma",
    "cofirmarSiFirmado",
    "nodeToSign"
})
public class DatosEntradaFichero {

    @XmlElement(required = true)
    protected byte[] contenido;
    protected String formatoFirma;
    protected String modoFirma;
    protected String algoritmoFirma;
    protected boolean cofirmarSiFirmado;
    protected String nodeToSign;

    /**
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContenido(byte[] value) {
        this.contenido = value;
    }

    /**
     * Obtiene el valor de la propiedad formatoFirma.
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
     * Define el valor de la propiedad formatoFirma.
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
     * Obtiene el valor de la propiedad modoFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModoFirma() {
        return modoFirma;
    }

    /**
     * Define el valor de la propiedad modoFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModoFirma(String value) {
        this.modoFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad algoritmoFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmoFirma() {
        return algoritmoFirma;
    }

    /**
     * Define el valor de la propiedad algoritmoFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmoFirma(String value) {
        this.algoritmoFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad cofirmarSiFirmado.
     * 
     */
    public boolean isCofirmarSiFirmado() {
        return cofirmarSiFirmado;
    }

    /**
     * Define el valor de la propiedad cofirmarSiFirmado.
     * 
     */
    public void setCofirmarSiFirmado(boolean value) {
        this.cofirmarSiFirmado = value;
    }

    /**
     * Obtiene el valor de la propiedad nodeToSign.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeToSign() {
        return nodeToSign;
    }

    /**
     * Define el valor de la propiedad nodeToSign.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeToSign(String value) {
        this.nodeToSign = value;
    }

}
