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
 * <p>Clase Java para DocumentoEniFileInside complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DocumentoEniFileInside">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e}documento"/>
 *         &lt;element name="documentoEniBytes" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoEniFileInside", namespace = "https://ssweb.seap.minhap.es/Inside/XSD/v1.0/documento-e/file", propOrder = {
    "documento",
    "documentoEniBytes"
})
public class DocumentoEniFileInside {

    @XmlElement(namespace = "http://administracionelectronica.gob.es/ENI/XSD/v1.0/documento-e", required = true)
    protected TipoDocumento documento;
    @XmlElement(required = true)
    protected byte[] documentoEniBytes;

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumento }
     *     
     */
    public TipoDocumento getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumento }
     *     
     */
    public void setDocumento(TipoDocumento value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad documentoEniBytes.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDocumentoEniBytes() {
        return documentoEniBytes;
    }

    /**
     * Define el valor de la propiedad documentoEniBytes.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDocumentoEniBytes(byte[] value) {
        this.documentoEniBytes = value;
    }

}
