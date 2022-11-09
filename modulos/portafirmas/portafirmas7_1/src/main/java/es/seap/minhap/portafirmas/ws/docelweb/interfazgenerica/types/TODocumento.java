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


package es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TODocumento complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TODocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipo_documento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="necesita_firma" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombre_documento" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TODocumento", propOrder = {
    "tipoDocumento",
    "necesitaFirma",
    "descripcion",
    "nombreDocumento",
    "contenido"
})
public class TODocumento {

    @XmlElement(name = "tipo_documento", required = true, nillable = true)
    protected String tipoDocumento;
    @XmlElement(name = "necesita_firma", required = true, nillable = true)
    protected String necesitaFirma;
    @XmlElement(required = true, nillable = true)
    protected String descripcion;
    @XmlElement(name = "nombre_documento", required = true, nillable = true)
    protected String nombreDocumento;
    @XmlElement(required = true, nillable = true)
    protected byte[] contenido;

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
     * Gets the value of the necesitaFirma property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNecesitaFirma() {
        return necesitaFirma;
    }

    /**
     * Sets the value of the necesitaFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNecesitaFirma(String value) {
        this.necesitaFirma = value;
    }

    /**
     * Gets the value of the descripcion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Sets the value of the descripcion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
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
     * Gets the value of the contenido property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getContenido() {
        return contenido;
    }

    /**
     * Sets the value of the contenido property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setContenido(byte[] value) {
        this.contenido = ((byte[]) value);
    }

}
