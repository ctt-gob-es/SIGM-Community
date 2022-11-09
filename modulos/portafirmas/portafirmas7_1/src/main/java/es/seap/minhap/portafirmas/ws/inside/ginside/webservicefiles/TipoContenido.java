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


package es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Clase Java para TipoContenido complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="TipoContenido">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="DatosXML" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *           &lt;element name="ValorBinario" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *           &lt;element name="referenciaFichero" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;/choice>
 *         &lt;element name="NombreFormato" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoContenido", namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e/contenido", propOrder = {
    "datosXML",
    "valorBinario",
    "referenciaFichero",
    "nombreFormato"
})
public class TipoContenido {

    @XmlElement(name = "DatosXML")
    protected Object datosXML;
    @XmlElement(name = "ValorBinario")
    protected byte[] valorBinario;
    protected String referenciaFichero;
    @XmlElement(name = "NombreFormato", required = true)
    protected String nombreFormato;
    @XmlAttribute(name = "Id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Obtiene el valor de la propiedad datosXML.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDatosXML() {
        return datosXML;
    }

    /**
     * Define el valor de la propiedad datosXML.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDatosXML(Object value) {
        this.datosXML = value;
    }

    /**
     * Obtiene el valor de la propiedad valorBinario.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getValorBinario() {
        return valorBinario;
    }

    /**
     * Define el valor de la propiedad valorBinario.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setValorBinario(byte[] value) {
        this.valorBinario = value;
    }

    /**
     * Obtiene el valor de la propiedad referenciaFichero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaFichero() {
        return referenciaFichero;
    }

    /**
     * Define el valor de la propiedad referenciaFichero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaFichero(String value) {
        this.referenciaFichero = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreFormato.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFormato() {
        return nombreFormato;
    }

    /**
     * Define el valor de la propiedad nombreFormato.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFormato(String value) {
        this.nombreFormato = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
