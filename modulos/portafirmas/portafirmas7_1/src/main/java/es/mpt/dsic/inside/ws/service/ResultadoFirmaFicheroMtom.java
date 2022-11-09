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

import javax.activation.DataHandler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para resultadoFirmaFicheroMtom complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="resultadoFirmaFicheroMtom">
 *   &lt;complexContent>
 *     &lt;extension base="{http://service.ws.inside.dsic.mpt.es/}contenidoSalida">
 *       &lt;sequence>
 *         &lt;element name="formatoFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="modoFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="algoritmoFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="hashContenido" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="algoritmoHashContenido" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="firma" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="hashFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="algoritmoHashFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fechaFirma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="datosFirmante" type="{http://service.ws.inside.dsic.mpt.es/}datosFirmante"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultadoFirmaFicheroMtom", propOrder = {
    "formatoFirma",
    "modoFirma",
    "algoritmoFirma",
    "contenido",
    "hashContenido",
    "algoritmoHashContenido",
    "firma",
    "hashFirma",
    "algoritmoHashFirma",
    "fechaFirma",
    "datosFirmante"
})
public class ResultadoFirmaFicheroMtom
    extends ContenidoSalida
{

    @XmlElement(required = true)
    protected String formatoFirma;
    @XmlElement(required = true)
    protected String modoFirma;
    @XmlElement(required = true)
    protected String algoritmoFirma;
    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler contenido;
    @XmlElement(required = true)
    protected String hashContenido;
    @XmlElement(required = true)
    protected String algoritmoHashContenido;
    @XmlElement(required = true)
    @XmlMimeType("application/octet-stream")
    protected DataHandler firma;
    @XmlElement(required = true)
    protected String hashFirma;
    @XmlElement(required = true)
    protected String algoritmoHashFirma;
    @XmlElement(required = true)
    protected String fechaFirma;
    @XmlElement(required = true)
    protected DatosFirmante datosFirmante;

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
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setContenido(DataHandler value) {
        this.contenido = value;
    }

    /**
     * Obtiene el valor de la propiedad hashContenido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashContenido() {
        return hashContenido;
    }

    /**
     * Define el valor de la propiedad hashContenido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashContenido(String value) {
        this.hashContenido = value;
    }

    /**
     * Obtiene el valor de la propiedad algoritmoHashContenido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmoHashContenido() {
        return algoritmoHashContenido;
    }

    /**
     * Define el valor de la propiedad algoritmoHashContenido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmoHashContenido(String value) {
        this.algoritmoHashContenido = value;
    }

    /**
     * Obtiene el valor de la propiedad firma.
     * 
     * @return
     *     possible object is
     *     {@link DataHandler }
     *     
     */
    public DataHandler getFirma() {
        return firma;
    }

    /**
     * Define el valor de la propiedad firma.
     * 
     * @param value
     *     allowed object is
     *     {@link DataHandler }
     *     
     */
    public void setFirma(DataHandler value) {
        this.firma = value;
    }

    /**
     * Obtiene el valor de la propiedad hashFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashFirma() {
        return hashFirma;
    }

    /**
     * Define el valor de la propiedad hashFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashFirma(String value) {
        this.hashFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad algoritmoHashFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlgoritmoHashFirma() {
        return algoritmoHashFirma;
    }

    /**
     * Define el valor de la propiedad algoritmoHashFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlgoritmoHashFirma(String value) {
        this.algoritmoHashFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaFirma() {
        return fechaFirma;
    }

    /**
     * Define el valor de la propiedad fechaFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaFirma(String value) {
        this.fechaFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad datosFirmante.
     * 
     * @return
     *     possible object is
     *     {@link DatosFirmante }
     *     
     */
    public DatosFirmante getDatosFirmante() {
        return datosFirmante;
    }

    /**
     * Define el valor de la propiedad datosFirmante.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosFirmante }
     *     
     */
    public void setDatosFirmante(DatosFirmante value) {
        this.datosFirmante = value;
    }

}
