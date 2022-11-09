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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para convertirDocumentoAEni complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="convertirDocumentoAEni">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="documento" type="{https://ssweb.seap.minhap.es/Inside/XSD/v1.0/documento-e/conversion}TipoDocumentoConversionInside"/>
 *         &lt;element name="contenido" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="firmar" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "convertirDocumentoAEni", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/WebServiceFiles", propOrder = {
    "documento",
    "contenido",
    "firmar"
})
public class ConvertirDocumentoAEni {

    @XmlElement(namespace = "", required = true)
    protected TipoDocumentoConversionInside documento;
    @XmlElement(namespace = "")
    protected byte[] contenido;
    @XmlElement(namespace = "")
    protected Boolean firmar;

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumentoConversionInside }
     *     
     */
    public TipoDocumentoConversionInside getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumentoConversionInside }
     *     
     */
    public void setDocumento(TipoDocumentoConversionInside value) {
        this.documento = value;
    }

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
     * Obtiene el valor de la propiedad firmar.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFirmar() {
        return firmar;
    }

    /**
     * Define el valor de la propiedad firmar.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFirmar(Boolean value) {
        this.firmar = value;
    }

}
