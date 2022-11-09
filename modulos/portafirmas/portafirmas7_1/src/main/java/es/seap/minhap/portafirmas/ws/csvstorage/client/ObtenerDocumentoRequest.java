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


package es.seap.minhap.portafirmas.ws.csvstorage.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para obtenerDocumentoRequest complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerDocumentoRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dir3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idEni" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="recuperacion_original" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}recuperacionOriginal" minOccurs="0"/>
 *         &lt;element name="documento_eni" type="{urn:es:gob:aapp:csvstorage:webservices:document:model:v1.0}documentoEni" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerDocumentoRequest", propOrder = {
    "dir3",
    "csv",
    "idEni",
    "recuperacionOriginal",
    "documentoEni"
})
public class ObtenerDocumentoRequest {

    @XmlElement(required = true)
    protected String dir3;
    protected String csv;
    protected String idEni;
    @XmlElement(name = "recuperacion_original")
    @XmlSchemaType(name = "string")
    protected RecuperacionOriginal recuperacionOriginal;
    @XmlElement(name = "documento_eni")
    @XmlSchemaType(name = "string")
    protected DocumentoEni documentoEni;

    /**
     * Obtiene el valor de la propiedad dir3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDir3() {
        return dir3;
    }

    /**
     * Define el valor de la propiedad dir3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDir3(String value) {
        this.dir3 = value;
    }

    /**
     * Obtiene el valor de la propiedad csv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Define el valor de la propiedad csv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

    /**
     * Obtiene el valor de la propiedad idEni.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdEni() {
        return idEni;
    }

    /**
     * Define el valor de la propiedad idEni.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdEni(String value) {
        this.idEni = value;
    }

    /**
     * Obtiene el valor de la propiedad recuperacionOriginal.
     * 
     * @return
     *     possible object is
     *     {@link RecuperacionOriginal }
     *     
     */
    public RecuperacionOriginal getRecuperacionOriginal() {
        return recuperacionOriginal;
    }

    /**
     * Define el valor de la propiedad recuperacionOriginal.
     * 
     * @param value
     *     allowed object is
     *     {@link RecuperacionOriginal }
     *     
     */
    public void setRecuperacionOriginal(RecuperacionOriginal value) {
        this.recuperacionOriginal = value;
    }

    /**
     * Obtiene el valor de la propiedad documentoEni.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoEni }
     *     
     */
    public DocumentoEni getDocumentoEni() {
        return documentoEni;
    }

    /**
     * Define el valor de la propiedad documentoEni.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoEni }
     *     
     */
    public void setDocumentoEni(DocumentoEni value) {
        this.documentoEni = value;
    }

}
